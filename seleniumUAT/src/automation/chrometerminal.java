package automation;
import java.util.ArrayList;
import automation.common;
import seleniumUAT.testResults;
import seleniumUAT.CafeTestSetup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.jetty.html.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class chrometerminal {
	static WebDriver driver;
	static String chrometerminal = "chrome-extension://pnhechapfaindjhompbnflcldabbghjo/html/nassh.html";
	public java.util.List<String> last_msg = new ArrayList<>();
	
	public boolean init() throws InterruptedException {
	    System.setProperty("webdriver.chrome.driver", "/home/tonylee/Downloads/selenium/chromedriver");
	    String profilePath = "/home/tonylee/.config/google-chrome/Default";
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("user-data-dir=" + profilePath);
	    chromeOptions.addArguments("start-maximized");
	    driver = new ChromeDriver(chromeOptions);
	    driver.get(chrometerminal);
	    driver.manage().window().maximize();
	    driver.switchTo().frame(0);
	    Thread.sleep(1000);
	    
	    return true;
	}
	
	public boolean init_icli()  throws InterruptedException {
	    try {
		if (!get_ipron()) return false;
		if (!get_icli()) return false;
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean send_commands(String cmd) throws InterruptedException {
	    try {
		get_msg();
		WebElement textbox = driver.findElement(By.xpath("//html/body/x-screen"));
		System.out.println("send key: " + last_msg.get(last_msg.size() - 1));
		textbox.sendKeys(cmd);
		textbox.sendKeys(Keys.RETURN);
		Thread.sleep(1500);
		get_msg();
		Thread.sleep(1000);
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public java.util.List<String> get_msg() throws InterruptedException {
	    java.util.List<String> msg = new ArrayList<>();
	    try { ///html/body/x-screen/div[1]/x-row[9]
		Thread.sleep(2000);
		WebElement x_screen = driver.findElement(By.xpath("//html/body/x-screen"));
		java.util.List<WebElement> rows = x_screen.findElements(By.xpath(".//x-row"));
		for (int i = 0; i < rows.size(); i++) {
		    if (!rows.get(i).getText().isEmpty()) {
			System.out.println("row" + rows.get(i).getText());
			msg.add(rows.get(i).getText());
		    }
		}
		
		if (last_msg.isEmpty()) {
		    last_msg.addAll(msg);
		} else {
		    last_msg.clear();
		    last_msg.addAll(msg);		    
		}
	    } catch (Exception e) {
		System.out.println(e);
		return null;
	    }
	    return msg;
	}
	
	public boolean get_ipron() throws InterruptedException {
	    try {
		WebElement textbox = driver.findElement(By.xpath("//html/body/x-screen"));
		textbox.sendKeys("su - ipron");
		textbox.sendKeys(Keys.RETURN);
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean get_icli() throws InterruptedException {
	    try {
		WebElement textbox = driver.findElement(By.xpath("//html/bodyx-screen"));
		textbox.sendKeys("icli");
		textbox.sendKeys(Keys.RETURN);
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean login() throws InterruptedException {
	    try {
		String login_wait = "root@100.100.103.80's password:";
		String login_passwd = "123qwe";
		int times = 3;
		get_msg();
		
		    Thread.sleep(1000);
		    WebElement textbox = driver.findElement(By.xpath("//*[@id='field-description']"));
		    textbox.click();
		    Thread.sleep(200);
		    textbox.clear();
		    Thread.sleep(200);
		    textbox.sendKeys("root@100.100.103.80");
		    textbox.sendKeys(Keys.RETURN);
		    
		
		while (times > 0) {
		    if (last_msg.get(last_msg.size() - 1).contains(login_wait)) {
			textbox = driver.findElement(By.xpath("//html/body/x-screen"));
			textbox.sendKeys(login_passwd);
			textbox.sendKeys(Keys.RETURN);
			Thread.sleep(1000);
			int sub_time = 3;
			while (sub_time > 0) {
			    get_msg();
			    System.out.println("line size - 2: " + last_msg.get(last_msg.size() - 2));
			    System.out.println("line size - 1: " + last_msg.get(last_msg.size() - 1));
			    if (last_msg.get(last_msg.size() - 1).contains("(R)econnect, (C)hoose another connection, or E(x)it")) {
				textbox.sendKeys("r");
			    } else if (last_msg.get(last_msg.size() - 2).contains("Last login:")) {
				return true;
			    } 
			    Thread.sleep(1000);
			    sub_time--;
			}
		    }
		    times--;
		    Thread.sleep(1000);
		}
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
}
