package main;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Webdriver {

	private ChromeDriver driver;
	
	public Webdriver() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("window-size=1920x1080");
		options.addArguments("disable-gpu");
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
	}
	
	public static Webdriver newInstance() {
		Webdriver wd = new Webdriver();
		return wd;
	}
	
	public ChromeDriver getWebDriver() {
		return driver;
	}
}
