package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import kilim.Pausable;
import kilim.Task;
import kilim.nio.EndPoint;

import com.archermind.httpclient.BackendScheduler;
import com.archermind.httpclient.HttpClient;
import com.archermind.httpclient.HttpRequest;
import com.archermind.httpclient.HttpResponse;

public class Test {

	public static void main(String[] args) {
		try {
			BackendScheduler bsc = new BackendScheduler();
			ConcurrentHashMap<String, ArrayList<EndPoint>> eps = new ConcurrentHashMap<String, ArrayList<EndPoint>>();
			HttpClient client = new HttpClient(eps, bsc);
			System.out.println("start test.......");
			Task t = new Test().new TestTask(client);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class TestTask extends Task {

		HttpClient client;

		public TestTask(HttpClient c) {
			this.client = c;
		}

		@Override
		public void execute() throws Pausable, MalformedURLException {
			int i = 0;
			HttpRequest req = new HttpRequest(
					new URL("http://www.sina.com.cn"), "get");
			while (true) {
				System.out.println("index:" + i);
				HttpResponse resp;
				try {
					resp = client.doRequest(req);
					System.out.println(resp.status());
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
		}

	}

	public static Task get(final HttpRequest req, final HttpClient client)
			throws Pausable {
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(req);
				this.exitResult = resp;
			}
		};
		task.start();
		return task;
	}

	class TestaTask extends Task {

		HttpClient client;

		public TestaTask(HttpClient c) {
			this.client = c;
		}

		@Override
		public void execute() throws Pausable, MalformedURLException {
			int i = 0;
			HttpRequest req = new HttpRequest(
					new URL("http://www.sina.com.cn"), "get");
			while (i < 10000) {
				HttpResponse resp = (HttpResponse) get(req, client).join().task.exitResult;
				System.out.println(resp.status());
				i++;
			}
		}
	}

}
