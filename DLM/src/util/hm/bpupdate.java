package util.hm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import main.dbManager;

public class bpupdate {
	private static WebDriver driver;
	private static final int num_search_page = 3;
	private static final String board_humor = "http://v12.battlepage.com/??=Board.Humor.Table";
	private static final String board_etc = "http://v12.battlepage.com/??=Board.ETC.Table";
	private List<String> log_list = new ArrayList<>();
	private dbManager dm;
	
	public bpupdate(dbManager dm) {
		try {
			this.dm = dm;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Login() {
		System.setProperty("webdriver.gecko.driver", "./driver/geckodriver/geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("https://v12.battlepage.com/??=Guest.Signup.LoginForm");
		driver.findElement(By.cssSelector("#login_id")).sendKeys("rlatmfrl24");
		driver.findElement(By.cssSelector("#login_pw")).sendKeys("397love");
		driver.findElement(By.cssSelector("body > form > table > tbody > tr:nth-child(1) > td:nth-child(3) > input")).click();
	}
	
	public Map<String, List<String>> LoadBP() {
		Map<String, List<String>> data_map = new TreeMap<>();
		try {
			log_list = dm.getLogs("v12.battlepage.com");
			for(int i=1; i<=num_search_page; i++) {
				Document doc = Jsoup.connect(board_humor+"&page="+i).get();
				Element table = doc.select("#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable").get(0);
				for(Element e : table.getElementsByAttribute("href")) {
					String url = e.attr("href");
					if(!log_list.contains(url)) {
						String title = e.getElementsByClass("search_title").get(0).text();
						String id = url.substring(url.indexOf("no=")).replace("no=", "");
						List<String> entry = new ArrayList<>();
						entry.add("유머게시판");
						entry.add(title);
						entry.add(url);
						data_map.put(id, entry);
					}
				}
			}
			for(int i=1; i<=num_search_page; i++) {
				Document doc = Jsoup.connect(board_etc+"&page="+i).get();
				Element table = doc.select("#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable").get(0);
				for(Element e : table.getElementsByAttribute("href")) {
					String url = e.attr("href");
					if(!log_list.contains(url)) {
						String title = e.getElementsByClass("search_title").get(0).text();
						String id = url.substring(url.indexOf("no=")).replace("no=", "");
						List<String> entry = new ArrayList<>();
						entry.add("기타게시판");
						entry.add(title);
						entry.add(url);
						data_map.put(id, entry);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data_map;
	}
}
