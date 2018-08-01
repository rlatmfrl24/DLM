package util.hm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.dbManager;

public class ddupdate {
	private dbManager dm;
	private static final int num_search_page = 6;
	private List<String> log_list = new ArrayList<>();

	public ddupdate(dbManager dm) {
		this.dm = dm;
	}
	
	public Map<String, List<String>> LoadDD() {
		Map<String, List<String>> data_map = new TreeMap<>();
		log_list = dm.getLogs("www.dogdrip.net");
		try {
			for(int i=1; i<=num_search_page; i++) {
				Document doc = Jsoup.connect("http://www.dogdrip.net/index.php?mid=dogdrip&page="+i).get();
				Elements items = doc.getElementsByTag("tbody").get(0).getElementsByTag("a");
				for(Element item : items) {
					if(!log_list.contains(item.attr("href"))) {
						String url = item.attr("href");
						String title = item.text();
						String id = url.substring(url.lastIndexOf('/')+1);
						List<String> entry = new ArrayList<>();
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ddupdate du = new ddupdate(new dbManager());
		du.LoadDD();
	}

}
