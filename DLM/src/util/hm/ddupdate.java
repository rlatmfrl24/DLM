package util.hm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.dbManager;

public class ddupdate {
	private dbManager dm;
	private static final int num_search_page = 6;
	private List<String> log_list = new ArrayList<>();

	public ddupdate(dbManager dm) {
		this.dm = dm;
	}
	
	public Map<String, String> LoadDD() {
		Map<String, String> data_map = new TreeMap<>();
		log_list = dm.getLogs("www.dogdrip.net");
		try {
			for(int i=1; i<=num_search_page; i++) {
				Document doc = Jsoup.connect("http://www.dogdrip.net/index.php?mid=dogdrip&page="+i).get();
				Elements items = doc.getElementsByTag("tbody").get(0).getElementsByClass("ed link-reset");
				
				for(Element item : items) {
					if(!log_list.contains(item.attr("href").replaceAll("index.*=", "dogdrip/"))) {
						String url = item.attr("href").replaceAll("index.*=", "dogdrip/");
						String title = item.text();
						data_map.put(url, title);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data_map;
	}
}
