package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.*;

public class hrmupdate {

	private static WebDriver driver;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		List<String> log_list = new ArrayList<>(); 
		try {
			File logfile = new File("./temp/hrm.log");
			if(!logfile.exists()) logfile.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logfile)));
			String tmp;
			while((tmp=br.readLine())!=null) {
				log_list.add(tmp);
			}
			br.close();
			
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
			
			WebElement we = driver.findElement(By.xpath("//*[@id=\"hrmbody\"]"));
			for(WebElement e : we.findElements(By.cssSelector("td"))) {
				
				try {
					String url = e.findElement(By.cssSelector("a")).getAttribute("href");
					if(!log_list.contains(url)) {
						System.out.println(url);
						log_list.add(url);
					}
				}catch(Exception ex) {
					break;
				}

			}
			//driver.close();
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile)));
			for(int i=0; i<log_list.size(); i++) {
				bw.write(log_list.get(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
