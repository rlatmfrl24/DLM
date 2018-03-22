package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class hiyobiDownloader {

	private static List<Gallery> gallery_list = new ArrayList<>();
	private static List<String> download_log = new ArrayList<>();
	private static File downlog = new File("./hiyobi/downlog.log");
	private static File homepath = new File("./hiyobi/");

	public hiyobiDownloader(int pages, int itemcount) {
		try {
			if(!homepath.exists()) homepath.mkdirs();
			if(!downlog.exists()) downlog.createNewFile();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(downlog)));
			String tmp;
			while((tmp=br.readLine())!=null) {
				download_log.add(tmp);
			}
			br.close();
			
			for(int i = 0; i<pages; i++) {
				Document doc = Jsoup.connect("https://hiyobi.me/list/"+i).get();
				getGalleryDataFromPage(doc);
			}

			if(itemcount == 0) itemcount = gallery_list.size();
			for(int i = 0; i<itemcount; i++) {
				getGalleryImages(gallery_list.get(i));
			}
			compressZip();
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downlog)));
			for(int i=0; i<download_log.size(); i++) {
				bw.write(download_log.get(i)+"\n");
			}
			bw.flush();
			bw.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void compressZip() {
		ziputil zu = new ziputil();

		for(File gal : homepath.listFiles()) {
			if(gal.isDirectory()) {
				zu.createZipFile(gal.getPath(), homepath.getPath(), gal.getName()+".zip");
				deleteDirectory(gal);
			}
		}
	}
	
	public boolean deleteDirectory(File path) {
		if (!path.exists()) {
			return false;
		}
		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				file.delete();
			}
		}
		return path.delete();
	}

	public void getGalleryImages(Gallery gal) {
		try {
			Document doc = Jsoup.connect(gal.getUrl()).get();
			Elements imgurl = doc.select("div.img-url");
			
			String folderpath = homepath.getPath()+"/"+"["+StringUtils.capitalize(gal.getArtist())+"]"+gal.getTitle().replaceAll("[/|\\|:|\\*|\\?|\"|<|>|\\|]", "_");
			File folder = new File(folderpath);
			
			if(!folder.exists()) folder.mkdirs();
			else return;
			
			System.out.println("Download to "+gal.getUrl());
			for(Element e : imgurl) {
				System.out.println(e.text());
				File f = new File(folderpath+"/"+e.text().substring(e.text().lastIndexOf('/')));
				while(f.length() < 1000) {
					DownloadImage(e.text(), f.getPath());
				}
			}
			download_log.add(0 ,gal.getUrl());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void DownloadImage(String search, String path) throws Exception {

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
	
	public void printGalleryInfos() {
		for(int i=0; i<gallery_list.size(); i++) {
			Gallery gallery = gallery_list.get(i);
			System.out.println("=========================");
			System.out.println("URL	: "+gallery.getUrl());
			System.out.println("Title	: "+gallery.getTitle());
			System.out.println("Code	: "+gallery.getCode());
			System.out.println("Artist	: "+gallery.getArtist());
			System.out.println("Group	: "+gallery.getGroup());
			System.out.println("Origin	: "+gallery.getOriginal());
			System.out.println("Type	: "+gallery.getType());
			System.out.println("Keyword	: "+gallery.getKeyword());
			System.out.println("=========================");
			System.out.println();
		}
	}
	
	public void getGalleryDataFromPage(Document doc) {
		try {
			Elements contents = doc.select("div.gallery-content");
			for(Element content : contents) {
				Gallery g = new Gallery();
				g.setTitle(content.select("b").text());
				g.setUrl("https://hiyobi.me"+content.child(0).attr("href"));
				if(download_log.contains(g.getUrl())) continue;
				g.setCode(g.getUrl().substring(g.getUrl().lastIndexOf("/")+1));
				Element artist = content.getElementsByTag("td").get(1);
				Element original = content.getElementsByTag("td").get(3);
				Element type = content.getElementsByTag("td").get(5);
				Element keyword = content.getElementsByTag("td").get(7);
				
				Pattern p = Pattern.compile("\\((.*?)\\)");
				Matcher m = p.matcher(artist.text());
				while(m.find()) {
					g.setGroup(m.group(0).replaceAll("[\\(|\\)]", "").trim());
				}
				g.setArtist(artist.text().replaceAll("\\((.*?)\\)", "").trim());
				g.setOriginal(original.text().trim());
				g.setType(type.text());
				g.setKeyword(keyword.text());
				gallery_list.add(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Gallery{
	
	private String title;
	private String code;
	private String url;
	private String artist;
	private String group;
	private String original;
	private String type;
	private String keyword;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}