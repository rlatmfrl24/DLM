package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i < 20; i++) {
				list.add(i);
			}
			System.out.println(list);
			ArrayList<Thread> thread = new ArrayList<Thread>();
			
			for (int i : list) {
				Thread t = new Thread(new Task(i));
				t.start();
				thread.add(t);
			}
			for (Thread t : thread) {
				t.join();
			}
			System.out.println("done");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class Task implements Runnable {

	int task_id;

	public Task(int i) {
		// TODO Auto-generated constructor stub
		this.task_id = i;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int time = randomRange(1000, 9000);
			Thread.sleep(time);
			System.out.println(task_id+"::"+time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int randomRange(int n1, int n2) {
		return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	}

}
