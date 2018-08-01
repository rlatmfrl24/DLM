package util.hd;

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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class DownlaodManager implements Runnable{

	private static List<Gallery> gallery_list = new ArrayList<>();
	private static List<String> download_log = new ArrayList<>();
	private static File downlog = new File("./hiyobi/downlog.log");
	private static File homepath = new File("./hiyobi/");
	private static WebDriver driver;

	private int pages;
	private int itemcount;
	private int current_process=0;
	private int selection_drvier;
	private Label lbl;
	private ProgressBar pbar;

	@Override
	public void run() {

		// TODO Auto-generated method stub
		try {
			if(!homepath.exists()) homepath.mkdirs();
			if(!downlog.exists()) downlog.createNewFile();
			
			download_log.clear();
			gallery_list.clear();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(downlog)));
			String tmp;
			while((tmp=br.readLine())!=null) {
				download_log.add(tmp);
			}
			br.close();
			
			for(int i = 1; i<pages+1; i++) {
				Document doc = Jsoup.connect("https://hiyobi.me/list/"+i).get();
				getGalleryDataFromPage(doc);
			}
			
			if(itemcount == 0) itemcount = gallery_list.size();
			ziputil zu = new ziputil();
			
			switch(selection_drvier) {
			case 1:
				System.setProperty("phantomjs.binary.path", "./driver/phantomjs/phantomjs.exe");
				driver = new PhantomJSDriver();
				break;
			case 2:
				System.setProperty("webdriver.chrome.driver", "./driver/chromedriver/chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless");
				options.addArguments("--no-sandbox");
				//options.addArguments("--disable-gpu");
				options.setBinary("./driver/chromedriver/chromedriver.exe");
				driver = new ChromeDriver(options);
				break;
			}
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			for(current_process = 0; current_process<itemcount; current_process++) {
				if(gallery_list.size()==0) break;
				Gallery g = gallery_list.get(current_process);
				download_log.add(g.getCode());
				lbl.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						lbl.setText(g.getTitle());
						pbar.setSelection(0);
					}
				});
				getGalleryImages(g);
				String toPath = homepath.getPath()+"/";
				if(g.getOriginal()!=null && !g.getOriginal().contains(",")) {
					toPath += g.getType()+"/"+g.getOriginal()+"/";
				}else {
					toPath += g.getType()+"/";
				}
				if(!new File(toPath).exists()) new File(toPath).mkdirs();
				
				zu.createZipFile(homepath.getPath()+"/"+g.getPath()+"/", toPath, g.getPath()+".zip");
				deleteDirectory(new File(homepath.getPath()+"/"+g.getPath()+"/"));
			}
			driver.close();
			driver.quit();
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
	
	public DownlaodManager(Label lbl, ProgressBar pbar, int pages, int itemcount, int selection) {
		// TODO Auto-generated constructor stub
		this.pages = pages;
		this.itemcount = itemcount;
		this.lbl = lbl;
		this.pbar = pbar;
		this.selection_drvier = selection;
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
		File folder = new File(homepath.getPath()+"/"+gal.getPath()+"/");
		if(!folder.exists()) folder.mkdirs();
		else return;
		
		try {
			Elements img_list = new Elements();
			while(img_list.size() == 0) {
				driver.get(gal.getUrl());
				Thread.sleep(1000);
				Document doc = Jsoup.parse(driver.getPageSource());
				img_list = doc.select(".img-url");
			}
			int max_size = img_list.size();
			pbar.getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					pbar.setMaximum(max_size);
				}
			});
			for(Element e : img_list) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				URL url = new URL(e.text());
				String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
				URLConnection con = url.openConnection();
				con.setRequestProperty("User-Agent", USER_AGENT);
				inputStream = con.getInputStream();
				outputStream = new FileOutputStream(folder.getPath()+"/"+e.text().substring(e.text().lastIndexOf('/'), e.text().length())+".jpg");
				byte[] buffer = new byte[2048];
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				outputStream.close();
				inputStream.close();
				int selection = img_list.indexOf(e);
				pbar.getDisplay().asyncExec(new Runnable() {					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						pbar.setSelection(selection+1);
						
					}
				});
			}
			Thread.sleep(1000);
		}catch(Exception e) {
			e.printStackTrace();
		}
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
				if(!download_log.contains(g.getUrl())) {
					g.setCode(g.getUrl().substring(g.getUrl().lastIndexOf("/")+1));
					if(download_log.contains(g.getCode())) continue;
					Elements DataSet = content.getElementsByTag("td");
					for(int i=0; i < DataSet.size(); i++) {
						if(DataSet.get(i).text().equals("작가 :")) {
							Pattern p = Pattern.compile("\\((.*?)\\)");
							Matcher m = p.matcher(DataSet.get(i+1).text());
							while(m.find()) {
								g.setGroup(m.group(0).replaceAll("[\\(|\\)]", "").trim());
							}
							if(g.getGroup()!=null) {
								g.setArtist(DataSet.get(i+1).text().replaceAll(g.getGroup(), "").replaceAll("[\\(|\\)]", "").trim());
							}else {
								g.setArtist(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
							}
						}else if(DataSet.get(i).text().equals("원작 :")) {
							g.setOriginal(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
						}else if(DataSet.get(i).text().equals("종류 :")) {
							g.setType(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
						}
					}
					
					String tmp="";
					if(g.getArtist() != null) {
						tmp += "["+StringUtils.capitalize(g.getArtist())+"]";
					}
					tmp += g.getTitle().replaceAll("[/|\\|:|\\*|\\?|\"|<|>|\\|]", "_");
					tmp += "("+g.getCode()+")";
					g.setPath(tmp);
					gallery_list.add(g);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Gallery{
	
	private String title = null;
	private String code = null;
	private String url = null;
	private String artist = null;
	private String group = null;
	private String original = null;
	private String type = null;
	private String keyword = null;
	private String path = null;
	
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}