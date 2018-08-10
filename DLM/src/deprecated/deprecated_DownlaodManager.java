package deprecated;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import main.dbManager;
import util.hd.Gallery;
import util.hd.ziputil;

public class deprecated_DownlaodManager implements Runnable{

	private static List<Gallery> gallery_list = new ArrayList<>();
	private static List<String> download_log = new ArrayList<>();
	private static File homepath = new File("./hiyobi/");
	private static WebDriver driver;

	private int pages;
	private int itemcount;
	private int current_process=0;
	private int pbar_selection = 1;
	private Label lbl;
	private ProgressBar pbar;
	private dbManager dm;

	@Override
	public void run() {

		// TODO Auto-generated method stub
		try {
			if(!homepath.exists()) homepath.mkdirs();
			download_log.clear();
			gallery_list.clear();
			
			for(String code : dm.getDataFromDB("code", "tb_hiyobi_info")) {
				download_log.add(code);
			}
			
			for(int i = 1; i<pages+1; i++) {
				Document doc = Jsoup.connect("https://hiyobi.me/list/"+i).get();
				getGalleryDataFromPage(doc);
			}
			
			if(itemcount == 0) itemcount = gallery_list.size();
			ziputil zu = new ziputil();
			
			System.setProperty("phantomjs.binary.path", "./driver/phantomjs/phantomjs.exe");
			driver = new PhantomJSDriver();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			for(current_process = 0; current_process<itemcount; current_process++) {
				if(gallery_list.size()==0) break;
				Gallery g = gallery_list.get(current_process);
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
				dm.insertDownloadLog(g);
			}
			driver.close();
			driver.quit();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public deprecated_DownlaodManager(dbManager dm, Label lbl, ProgressBar pbar, int pages, int itemcount) {
		// TODO Auto-generated constructor stub
		this.pages = pages;
		this.itemcount = itemcount;
		this.lbl = lbl;
		this.pbar = pbar;
		this.dm = dm;
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
	
	public void download(String link, String path) throws Exception {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		URL url = new URL(link);
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
			
			pbar_selection=1;
			List<Thread> threads = new ArrayList<>();
			for(Element e : img_list) {
				String save_path = folder.getPath()+"/"+e.text().substring(e.text().lastIndexOf('/'), e.text().length())+".jpg";
				Runnable task = new Runnable() {					
					@Override
					public void run() {
						try {
							download(e.text(), save_path);
							pbar.getDisplay().asyncExec(new Runnable() {					
								@Override
								public void run() {
									pbar.setSelection(pbar_selection);
									pbar_selection++;
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(task);
				t.start();
				threads.add(t);
			}
			for(Thread t : threads) {
				t.join();
			}
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
						}else if(DataSet.get(i).text().equals("태그 :")) {
							String str_tag = "";
							for(Element tag : DataSet.get(i+1).getElementsByTag("a")) {
								str_tag += "|"+tag.text();
							}
							if(str_tag.length() > 0)str_tag = str_tag.substring(1);
							g.setKeyword(str_tag);
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