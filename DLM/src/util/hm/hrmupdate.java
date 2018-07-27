package util.hm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ahocorasick.trie.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.*;

public class hrmupdate {

	private static final File LOG_FILE = new File("./temp/hrm.log");
	private static WebDriver driver;
	private Map<String, String> Filter_Map = new HashMap<>();
	private Map<String, List<String>> res_map = new HashMap<>();
	private Trie linkTrie;
	private List<String> log_list = new ArrayList<>();
	
	public hrmupdate() {
		Filter_Map.put("http://www.dostream.com/", "dostream");
		Filter_Map.put("imgur.com", "imgur");
		Filter_Map.put("youtube.com", "youtube");
		Filter_Map.put("youtu.be", "youtube");
		Filter_Map.put("tumblr.", "tumblr");
		Filter_Map.put(".etoland", "etorrent");
		Filter_Map.put(".etobang", "etorrent");
		Filter_Map.put("gall.dcinside.com", "dcinside");
		Filter_Map.put("bbs.ruliweb.com", "ruliweb");
		linkTrie = Trie.builder().addKeywords(Filter_Map.keySet()).build();
	}
	
	public void classify(String url) {
		String tag="ETC";
		List<String> list;
		
		Collection<Emit> Emits= linkTrie.parseText(url);
		if(!Emits.isEmpty()) {
			for(Emit e : Emits) {
				tag = Filter_Map.get(e.getKeyword());
			}
		}
		
		if(!res_map.containsKey(tag)) list = new ArrayList<>();
		else list = res_map.get(tag);

		list.add(url);
		res_map.put(tag, list);
	}
	
	public void LoadLog() throws Exception {
		if(!LOG_FILE.exists()) LOG_FILE.createNewFile();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_FILE)));
		String tmp;
		while((tmp=br.readLine())!=null) {
			log_list.add(tmp);
		}
		br.close();
	}
	public void SaveLog(List<String> list_visited) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true));
		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LOG_FILE)));
		for(int i=0; i<list_visited.size(); i++) {
			bw.write(list_visited.get(i));
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	public void printResMap() {
		for(String key : res_map.keySet()) {
			System.out.println("Tag: "+key);
			for(String url : res_map.get(key)) {
				System.out.println("ㄴ"+url);
			}
		}
	}
	
	public void LoadHrm() {
		try {
			res_map.clear();
			log_list.clear();
			LoadLog();
			
			//파폭 드라이버
			/*
			System.setProperty("webdriver.gecko.driver", "./driver/geckodriver/geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get("http://insagirl-toto.appspot.com/hrm/?where=2");
			driver.findElement(By.cssSelector("#hrmbodyexpand")).click();
			Thread.sleep(1000);
			*/
			System.setProperty("phantomjs.binary.path", "./driver/phantomjs/phantomjs.exe");
			driver = new PhantomJSDriver();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get("http://insagirl-toto.appspot.com/hrm/?where=2");
			driver.findElement(By.cssSelector("#hrmbodyexpand")).click();
			Thread.sleep(1000);
			WebElement we = driver.findElement(By.cssSelector("#hrmbody"));
			Element elements = Jsoup.parse(we.getAttribute("innerHTML"));
			driver.close();
			driver.quit();
			for(Element e : elements.getElementsByAttribute("href")) {
				String url = e.attr("href");
				if(!url.isEmpty() && !log_list.contains(url)) {
					classify(url);
					log_list.add(url);
				}
			}
			
			res_map.remove("dostream");
			
			//printResMap();
			//SaveLog();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, List<String>> getResMap() {
		return this.res_map;
	}
}
