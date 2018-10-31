package deprecated;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Document doc = Jsoup.connect("https://db.tt/VrhDl9TpY8").get();
			System.out.println(doc.html());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
