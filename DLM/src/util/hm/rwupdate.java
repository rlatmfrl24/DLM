package util.hm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import main.dbManager;
import util.hd.DownloadUtil;
import util.rc.Expansion;

public class rwupdate {
	private static WebDriver driver;
	
	private static final String board_rearwarning = "http://v12.battlepage.com/??=Board.RearWarning.Table";
	private static final String login_url = "https://v12.battlepage.com/??=Guest.Signup.LoginForm";
	private static final String save_path = "./temp/rw/";
	private static int pageNum = 1;
	private static Trie expansion_check_trie;
	private static DownloadUtil download_util;
	Expansion exp = new Expansion();

	public rwupdate(dbManager dm) {
		download_util = new DownloadUtil(dm);
		
		//이미지 확장자 로드
		TrieBuilder tb = Trie.builder();
		for(String expansion : exp.expansion_image) tb.addKeyword(expansion);
		expansion_check_trie = tb.ignoreCase().build();
		
		//저장폴더 체크 및 생성
		File save_folder = new File(save_path);
		if(!save_folder.exists() || !save_folder.isDirectory()) {
			save_folder.mkdir();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		dbManager dm = new dbManager();
		dm.Connect();
		dm.initialize();
		rwupdate r = new rwupdate(dm);
		r.BP_Login();
		for(String link : r.BP_getList()) {
			r.BP_getContent(link);
		}
		driver.close();
		driver.quit();
		
	}

	private void BP_Login() {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		
		options.addArguments("headless");
		options.addArguments("window-size=1920x1080");
		options.addArguments("disable-gpu");
		
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(login_url);
		driver.findElement(By.cssSelector("#login_id")).sendKeys("rlatmfrl24");
		driver.findElement(By.cssSelector("#login_pw")).sendKeys("397love");
		driver.findElement(By.cssSelector("body > form > table > tbody > tr:nth-child(1) > td:nth-child(3) > input")).click();
	}
	
	public List<String> BP_getList() {
		List<String> bp_visit_list = new ArrayList<>();
		for(int i=1; i <= pageNum; i++) {
			driver.get(board_rearwarning+"&page="+i);
			WebElement listable = driver.findElement(By.cssSelector("#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable"));
			for(WebElement we : listable.findElements(By.tagName("a"))) {
				bp_visit_list.add(we.getAttribute("href"));
			}
		}
		return bp_visit_list;
	}
	
	public void BP_getContent(String url) {
		try {
			System.out.println("[SYSTEM] Access to "+url);
			driver.get(url);
			String content_title = driver.findElement(By.className("search_title"))
									.getText().replaceAll("[ |.|/]", "_");
			System.out.println(content_title);
			List<String> download_list = new ArrayList<>();
			WebElement content = driver.findElement(By.className("search_content"));
			
			System.out.println("[SYSTEM] Get Data from a Tag..");
			for(WebElement we : content.findElements(By.tagName("a"))) {
				String link = we.getAttribute("href");
				String LinkData = makeLinkData(link, "a");
				System.out.println(LinkData);
				download_list.add(LinkData);
			}
			
			System.out.println("[SYSTEM] Get Data from img Tag..");
			for(WebElement we : content.findElements(By.tagName("img"))) {
				String link = we.getAttribute("src");
				String LinkData = makeLinkData(link, "img");
				System.out.println(LinkData);
				download_list.add(LinkData);
			}
			
			if(download_list.size() > 0) {
				System.out.println("[SYSTEM]Download Start!");
				File content_save_path = new File(save_path+content_title+"/");
				if(!content_save_path.exists()) content_save_path.mkdirs();
				for(String LinkData : download_list) {
					String link_tag = LinkData.split("\t")[1];
					if(exp.expansion_image.contains("."+link_tag.toLowerCase())) {
						String img_link = LinkData.split("\t")[2];
						String img_path = content_save_path.getCanonicalPath() + "/"
								+ img_link.toLowerCase().substring(img_link.lastIndexOf('/') + 1,
								img_link.toLowerCase().lastIndexOf(img_validation(img_link).toLowerCase())
								+img_validation(img_link).length());
						System.out.println(img_path);
						download_util.ImageDownload(img_link, img_path, 0);
					} else {
						String img_link = LinkData.split("\t")[2];
						System.out.println("=====OTHER_TAG=====");
						System.out.println(img_link);
					}
				}
				System.out.println("[SYSTEM]Download Done.");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String makeLinkData(String link, String tagname) {
		String LinkData = "";
		if(url_validation(link)) {
			LinkData += "["+tagname+"]:\t";
			LinkData += img_validation(link)+"\t";
			LinkData += link;
		}
		return LinkData;
	}
	
	public String img_validation(String url) {
		if(expansion_check_trie.containsMatch(url)) {
			return expansion_check_trie.firstMatch(url).getKeyword().replaceAll("\\.", "").toUpperCase();
		}else {
			return "OTHER";
		}
	}
	public boolean url_validation(String url) {
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}
}
