package seleniumUAT;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

//import seleniumUAT.testReport.xlsReport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProxySelector;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.log4j.xml.DOMConfigurator;
//import org.apache.logging.log4j.*;

public class CafeTestSetup {
	static WebDriver driver;
    static String SUT_url = "http://oasis1418.cafe24.com"; 
    static String ErrRsn = "";
    static Logger logger = LogManager.getLogger(CafeTestSetup.class.getClass());
    static testResults results = new testResults();
    static screenShot screenShot = new screenShot();
    
    public static getSystemConf getSysConf = new getSystemConf();
    
    public static void main(String[] args) throws InterruptedException {
    	xlsReport report = new xlsReport();
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
    	File file = new File("S:/selenium_web_test/swat_web_test/QA/seleniumUAT/config/Log4j2.xml");
    	context.setConfigLocation(file.toURI());
    	Date date = new Date();
    	SimpleDateFormat f = new SimpleDateFormat (" E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    	
    	/*
    	 * 	set Chrome browser 
    	 */
        System.setProperty("webdriver.chrome.driver", "S:/selenium_web_test/library/chromedriver.exe");
        //driver = new ChromeDriver();
        Thread.sleep(1000);    
    	ChromeOptions chromeOptions = new ChromeOptions();
    	chromeOptions.addArguments("--start-maximized");
    	driver = new ChromeDriver(chromeOptions);
    	
    	/*
  		 *	set Firefox browser
  		 */
    	/*
    	driver = new FirefoxDriver();
    	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    	capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);
        driver = new FirefoxDriver(binary, profile);
        */
    	
        /*
         * set IE browser
         */
        /*
        System.setProperty("webdriver.ie.driver", "S:/selenium_web_test/library/IEDriverServer.exe");
    	DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability("nativeEvents", false);    
		ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
		ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
		ieCapabilities.setCapability("disable-popup-blocking", true);
		ieCapabilities.setCapability("enablePersistentHover", true);
		ieCapabilities.setCapability("INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS", true);
		driver = new InternetExplorerDriver(ieCapabilities);
         */
    	getSysConf.setDriver(driver, SUT_url);
	
    	System.out.println ("Test start:" + f.format(date));
    	CafeRunTest();
    	System.out.println ("Test end:" + f.format(date));

        try {
			report.createXLS(results.resultList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static void CafeRunTest() throws InterruptedException {
    	CafeTestSuit suit = new CafeTestSuit();
    	suit.suit();
    }
}
