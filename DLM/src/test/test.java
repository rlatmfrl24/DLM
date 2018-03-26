package test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class test implements Runnable {
	int seq;
	public test(int seq) {
		this.seq = seq;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        System.out.println(this.seq+" thread start.");
        try {
            Thread.sleep(5000);
        }catch(Exception e) {
        }
        System.out.println(this.seq+" thread end.");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
	        ArrayList<Thread> threads = new ArrayList<Thread>();
	        for(int i=0; i<20; i++) {
	            Thread t = new Thread(new test(i));
	            t.start();
	            threads.add(t);
	        }

	        for(int i=0; i<threads.size(); i++) {
	            Thread t = threads.get(i);
	            try {
	                t.join();
	            }catch(Exception e) {
	            }
	        }
	        System.out.println("main end.");
			
			
			//DownloadImage("https://aa.hiyobi.me/data/1202913/1.jpg", "./temp/a.jpg");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void DownloadImage(String search, String path) throws Exception {

		InputStream inputStream = null;
		OutputStream outputStream = null;
		URL url = new URL(search);
		String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

		URLConnection con = url.openConnection();
		con.setRequestProperty("User-Agent", USER_AGENT);
		inputStream = con.getInputStream();
		outputStream = new FileOutputStream(path);
		byte[] buffer = new byte[2048];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, length);
		}
		outputStream.close();
		inputStream.close();
	}


}
