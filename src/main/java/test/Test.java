package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import kilim.Pausable;
import kilim.Task;
import kilim.nio.EndPoint;

import com.archermind.httpclient.BackendScheduler;
import com.archermind.httpclient.HttpClient;
import com.archermind.httpclient.HttpClientHelper;
import com.archermind.httpclient.HttpRequest;
import com.archermind.httpclient.HttpResponse;
import com.archermind.httpclient.ResponseCallable;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		
		try {
			BackendScheduler bsc = new BackendScheduler();
			ConcurrentHashMap<String, ArrayList<EndPoint>> eps = new ConcurrentHashMap<String, ArrayList<EndPoint>>();
			final HttpClient client = new HttpClient(eps, bsc);

//			final ResponseCallable callable = new ResponseCallable() {
//				public void call(HttpResponse resp) {
//					try {
//						System.out.println(resp.status()+resp.tempFile.getAbsolutePath());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			
//			
//			HttpClientHelper.get(new URL("http://www.archermind.com/images/hq.jpg"), client, callable);
			
	
			
			long ll = System.currentTimeMillis();
			int times = 2;
			CountDownLatch cdl = new CountDownLatch(40*4*times);
			System.out.println("start test......." + ll);
			
			int i = 0;
			while (i < 40*times) {
				Task t = new Test().new TestTask(cdl,client,
						"http://www.sina.com.cn");
				t.start();
				i++;
			}
//			i=0;
//			while (i < 40*times) {
//				Task t = new Test().new TestTask(cdl,client,
//						"http://www.archermind.com");
//				t.start();
//				i++;
//			}
			i=0;
			while (i < 40*times) {
				Task t = new Test().new TestTask(cdl,client,
						"http://www.google.com");
				t.start();
				i++;
			}
			i=0;
			while (i < 40*times) {
				Task t = new Test().new TestTask(cdl,client,
						"http://www.baidu.com/");
				t.start();
				i++;
			}
			i=0;
			while (i < 40*times) {
				Task t = new Test().new TestTask(cdl,client,
						"http://www.163.com");
				t.start();
				i++;
			}
			cdl.await();
			System.out.println("over:"+(System.currentTimeMillis() - ll));
			bsc.shutdown();
			System.exit(0);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	class TestTask extends Task {

		HttpClient client;
		String s;
		CountDownLatch cdl;

		public TestTask(CountDownLatch cdl,HttpClient c, String s) {
			this.cdl = cdl;
			this.client = c;
			this.s = s;
		}

		@Override
		public void execute() throws Pausable, MalformedURLException {
			int i = 0;
			HttpRequest req = new HttpRequest(new URL(s), "get");
			while (i < 1) {
//				System.out.println("index:" + i);
				HttpResponse resp;
				try {
					resp = client.doRequest(req);
					System.out.println(s + " status code: " + resp.status());
					// System.out.println(i+" content: "+resp.getContentToString());
				} catch (Exception e) {
//					e.printStackTrace();
					System.out.println(s + " status code: 0");
				}
				i++;
			}
			cdl.countDown();
		}

	}


}
