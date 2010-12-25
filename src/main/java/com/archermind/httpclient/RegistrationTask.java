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

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import kilim.nio.EndPoint;
import kilim.nio.SockEvent;

/**
 * 
 * @author tiger
 * Created on 2010-11-8 14:46:51
 */
public class RegistrationTask extends Task {

    Mailbox<SockEvent> mbx;
    Selector selector;

    public RegistrationTask(Mailbox<SockEvent> ambx, Selector asel) {
        mbx = ambx;
        selector = asel;
    }

    @Override
    public void execute() throws Pausable, Exception {
        while (true) {
            SockEvent ev = mbx.get();
            SelectionKey sk = ev.ch.register(selector, ev.interestOps);
            sk.attach(ev);
            ((EndPoint)ev.replyTo).setSk(sk);
        }
    }
}
