package util.hm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.ahocorasick.trie.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.*;

import main.dbManager;

public class hrmupdate {

	private static WebDriver driver;
	private Map<String, String> Filter_Map = new HashMap<>();
	private Map<String, String> res_map = new TreeMap<>();
	
	private Trie linkTrie;
	private List<String> log_list = new ArrayList<>();
	private dbManager dm;
	
	public hrmupdate(dbManager dm) {
		Filter_Map.put("http://www.dostream.com/", "dostream");
		Filter_Map.put("imgur.com", "imgur");
		Filter_Map.put("youtube.com", "Youtube");
		Filter_Map.put("youtu.be", "Youtube");
		Filter_Map.put("tumblr.", "tumblr");
		Filter_Map.put(".etoland", "etorrent");
		Filter_Map.put(".etobang", "etorrent");
		Filter_Map.put("gall.dcinside.com", "dcinside");
		Filter_Map.put("bbs.ruliweb.com", "ruliweb");
		linkTrie = Trie.builder().addKeywords(Filter_Map.keySet()).build();
		this.dm = dm;
	}
	
	public void classify(String url) {
		String tag="ETC";
		Collection<Emit> Emits= linkTrie.parseText(url);
		if(!Emits.isEmpty()) {
			for(Emit e : Emits) {
				tag = Filter_Map.get(e.getKeyword());
			}
		}
		if(!tag.equals("dostream"))
			res_map.put(url, tag);
	}
		
	public Map<String, String> LoadHrm() {
		try {
			res_map.clear();
			log_list.clear();
			log_list = dm.getDataFromDB("link", "tb_link_info");
			
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
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		res_map = res_map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return res_map;
	}
}
