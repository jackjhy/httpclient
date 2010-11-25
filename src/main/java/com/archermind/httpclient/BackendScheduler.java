/*
 *  你看，你看，我的程序
 *  http://www.@!#!&.com
 *  jackjhy@gmail.com
 * 
 *  听说牛粪离钻石只有一步之遥，听说稻草离金条只有一步之遥，
 *  听说色情离艺术只有一步之遥，听说裸体离衣服只有一步之遥，
 *  听说龙芯离AMD只有一步之遥，听说神舟离月球只有一步之遥，
 *  听说现实离乌邦只有一步之遥，听说社会离共产只有一步之遥，
 *  听说台湾离独立只有一步之遥，听说日本离玩完只有一步之遥，
 */

package com.archermind.httpclient;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import kilim.Mailbox;
import kilim.RingQueue;
import kilim.Scheduler;
import kilim.Task;
import kilim.nio.SessionTask;
import kilim.nio.SockEvent;

/**
 * 
 * @author tiger Created on 2010-11-8 14:23:05
 */
public class BackendScheduler extends Scheduler {

	public Selector sel;
	
    /* 
     * The thread in which the selector runs. THe NioSelectorScheduler only runs one thread,
     * unlike typical schedulers that manage a pool of threads.
     */
    public SelectorThread     selectorThread;

	public Mailbox<SockEvent> registrationMbx = new Mailbox<SockEvent>(
			1000);

	public BackendScheduler() throws IOException {
		sel = Selector.open();
		new RegistrationTask(registrationMbx, sel).setScheduler(this).start();
		new SelectorThread(this).start();
	}
	
	
    public synchronized int numRunnables() {
        return runnableTasks.size();
    }
    
    @Override
    public void schedule(Task t) {
        addRunnable(t);
        if (Thread.currentThread() != selectorThread) {
            sel.wakeup();
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        sel.wakeup();
    }
    
    synchronized void addRunnable(Task t) {
        runnableTasks.put(t);
    }


    
    synchronized RingQueue<Task> swapRunnables(RingQueue<Task> emptyRunnables) {
        RingQueue<Task> ret = runnableTasks;
        runnableTasks = emptyRunnables;
        return ret;
    }

	class SelectorThread extends Thread {
		BackendScheduler _scheduler;

		public SelectorThread(BackendScheduler scheduler) {
			this._scheduler = scheduler;
		}

		@Override
		public void run() {

			Selector sel = _scheduler.sel;
			RingQueue<Task> runnables = new RingQueue<Task>(100); // to swap
																	// with
																	// scheduler
			while (true) {
				int n;
				try {
					if (_scheduler.isShutdown()) {
						Iterator<SelectionKey> it = sel.keys().iterator();
						while (it.hasNext()) {
							SelectionKey sk = it.next();
							sk.cancel();
							Object o = sk.attachment();
							if (o instanceof SockEvent
									&& ((SockEvent) o).ch instanceof ServerSocketChannel) {
								// TODO FIX: Need a proper, orderly shutdown
								// procedure for tasks. This closes down the
								// task
								// irrespective of the thread it may be running
								// on. Terrible.
								try {
									((ServerSocketChannel) ((SockEvent) o).ch)
											.close();
								} catch (IOException ignore) {
								}
							}
						}
						break;
					}
					if (_scheduler.numRunnables() > 0) {
						n = sel.selectNow();
					} else {
						n = sel.select();
					}
				} catch (IOException ignore) {
					n = 0;
					ignore.printStackTrace();
				}
				if (n > 0) {
					Iterator<SelectionKey> it = sel.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey sk = it.next();
						it.remove();
						Object o = sk.attachment();
						sk.interestOps(0);
						if (o instanceof SockEvent) {
							SockEvent ev = (SockEvent) o;
							ev.replyTo.putnb(ev);
						} else if (o instanceof Task) {
							Task t = (Task) o;
							t.resume();
						}
					}
				}
				runnables.reset();
				runnables = _scheduler.swapRunnables(runnables);
				// Now execute all runnables inline
				// if (runnables.size() == 0) {
				// System.out.println("IDLE");
				// }
				while (runnables.size() > 0) {
					Task t = runnables.get();
					t._runExecute(null);
					// If task calls Task.yield, it would have added itself to
					// scheduler already.
					// If task's pauseReason is YieldToSelector, then nothing
					// more to do.
					// Task should be registered for the appropriate Selector
					// op.
					// In all other cases, (Task.sleep(), Mailbox.get() etc.),
					// unregister
					// the channel
					if (t instanceof SessionTask) {
						SessionTask st = (SessionTask) t;
						if (st.isDone()) {
							st.close();
						}
					}
				}
			}
		}

	}
}
