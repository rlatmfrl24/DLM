package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import util.AutoCategorizer;

public class test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AutoCategorizer ac = new AutoCategorizer();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("G:/정리목록.txt"))));
			String tmp;
			while((tmp=br.readLine())!=null) {
				System.out.println(ac.GetCategorizedName(new File(tmp)));
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
