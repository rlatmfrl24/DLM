package util.hd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class GalleryDownloader implements Runnable {

	int sequence;
	String imgurl;
	String filenpath;
	File savefile;

	public GalleryDownloader(int seq, String link, String path) {
		// TODO Auto-generated constructor stub
		this.sequence = seq;
		this.imgurl = link;
		this.filenpath = path + "/" + imgurl.substring(imgurl.lastIndexOf('/'));
		this.savefile = new File(filenpath);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			URL url = new URL(this.imgurl);
			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENT);
			inputStream = con.getInputStream();
			outputStream = new FileOutputStream(this.filenpath);
			byte[] buffer = new byte[2048];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
			System.err.println(imgurl);
		}

	}
}
