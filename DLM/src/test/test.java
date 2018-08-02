package test;

import java.util.List;
import main.dbManager;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			dbManager dm = new dbManager();
			dm.Connect();
			List<String> list = dm.getLogs("v12.battlepage.com");
			dm.temp(list);
			for(String link : list) {
				System.out.println(link.replaceAll("\\&page=[0-9]", ""));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}