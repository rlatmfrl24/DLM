package util.hm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class rwupdate {
	private static WebDriver driver;
	private static final String board_rearwarning = "http://v12.battlepage.com/??=Board.RearWarning.Table";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		rwupdate b = new rwupdate();
		b.Login();
		try {
			Document doc = Jsoup.connect(board_rearwarning).get();
			System.out.println(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void Login() {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("https://v12.battlepage.com/??=Guest.Signup.LoginForm");
		driver.findElement(By.cssSelector("#login_id")).sendKeys("rlatmfrl24");
		driver.findElement(By.cssSelector("#login_pw")).sendKeys("397love");
		driver.findElement(By.cssSelector("body > form > table > tbody > tr:nth-child(1) > td:nth-child(3) > input")).click();
		driver.close();
		driver.quit();
	}
	
	
}
