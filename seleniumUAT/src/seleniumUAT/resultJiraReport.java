package seleniumUAT;
import java.util.ArrayList;
import automation.common;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class resultJiraReport extends CafeTestSetup {
	static WebDriver driver1;
	private String url = "http://qa.bridgetec.co.kr/jira/secure/Dashboard.jspa";

    public static Map<Integer, java.util.List<String>> map = new HashMap<Integer, java.util.List<String>>();
	private static errorReason errReason = new errorReason();
	private static Map<String, String> searchConditions = new HashMap<String, String>();
	private static Map<String, String> menu_map = new LinkedHashMap<String, String>();
	private static common commands = new common();
	public String retString = null;
	public boolean result = false;
	
	// 怨꾩젙 �젙蹂�
	private static String ID = "hjoh";
	private static String PW = "1206";
	private static String loginName ="�삤�쁽醫�";
	
	// JIRA �뾽�뜲�씠�듃 �궗�슜 �븞�븿
	String update = "no";
	
	public static List errcodeinfo = new ArrayList();
	private static resultJiraReport ResourceErrorCodeManage = new resultJiraReport();

	

	
	public void test(String tc, boolean result, String comment) throws InterruptedException {
		if (update == "yes"){
			conditions cond = new conditions();
			System.out.println ("[JIRA REPORT]"+tc+" : JIRA ISSUE RESULT REPORT");
			System.out.println(result);
			String testID = null;
			openPage();
			jiraLogin(ID,PW);
			
			if (tc != null) {
				issueSearch(tc);
			}
			if (result == true) {
				WebElement status = driver1.findElement(By.xpath("//*[@id='status-val']"));
				String issueStatus = status.getText();
				if (issueStatus.contains("誘몄떎�떆")) {
					WebElement passButton = driver1.findElement(By.xpath("//*[@id='action_id_11']"));
					System.out.println("[JIRA REPORT] �빀寃⑹쿂由� �븯���뒿�땲�떎.");
					passButton.click();
				}
				if (issueStatus.contains("遺덊빀寃�")) {
					
					System.out.println("[JIRA REPORT] 遺덊빀寃⑹뿉�꽌 �빀寃⑹쿂由� �븯���뒿�땲�떎.");
				}
				if (issueStatus.contains("�빀寃�")) {
					System.out.println("[JIRA REPORT] �빀寃⑹쿂由� �릺�뼱 �엳�뒿�땲�떎..");
				}
			}
			if (result == false) {
				if (comment != null) {
					WebElement commentButton = driver1.findElement(By.xpath("//*[@id='footer-comment-button']"));
					commentButton.click();
					Thread.sleep(1000);
					WebElement commentTextArea = driver1.findElement(By.xpath("//*[@id='comment']"));
					commentTextArea.click();
					Thread.sleep(1000);
					commentTextArea.clear();
					commentTextArea.sendKeys(comment);
					Thread.sleep(1000);
					WebElement commentAddButton = driver1.findElement(By.xpath("//*[@id='issue-comment-add-submit']"));
					commentAddButton.click();
					Thread.sleep(3000);
				}
				WebElement status = driver1.findElement(By.xpath("//*[@id='status-val']"));
				String issueStatus = status.getText();
			}
		} 

		if (update == "no") {
			System.out.println("");
			results.add(tc, result, comment, commands.getscreenshot());
		}
	}
	
	
	public boolean openPage() throws InterruptedException {
		
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--start-maximized");
	    driver1 = new ChromeDriver(chromeOptions);
		driver1.get(url);
		Thread.sleep(3000);
		System.out.println("[JIRA REPORT] Open Page Success");
	    return true;
	}
	public boolean jiraLogin (String ID,String PW) throws InterruptedException{
		try {
			Thread.sleep(1000);
			
			driver1.switchTo().defaultContent();
			driver1.switchTo().frame("gadget-0");
			WebElement loginId = driver1.findElement(By.xpath("//*[@id='login-form-username']"));
			WebElement loginPw = driver1.findElement(By.xpath("//*[@id='login-form-password']"));
			
			loginId.click();
			Thread.sleep(1000);
			loginId.clear();
			Thread.sleep(1000);
			loginId.sendKeys(ID);
			Thread.sleep(1000);
			
			loginPw.click();
			Thread.sleep(1000);
			loginPw.clear();
			Thread.sleep(1000);
			loginPw.sendKeys(PW);
			Thread.sleep(1000);

			driver1.findElement(By.id("login")).click();
			Thread.sleep(3000);
			try {
				String loginErr = driver1.findElement(By.id("usernameerror")).getText();
				System.out.println("[JIRA REPORT] LOGIN FAIL : " + loginErr);
				return false;
			} catch (Exception e) {
				String loginPass = driver1.findElement(By.xpath("//*[@id='header-details-user-fullname']")).getText();
				if (loginPass.contains(loginName)) {
					System.out.println("[JIRA REPORT] LOGIN SUCCESS : "+ID);
				} else {
					System.out.println("[JIRA REPORT] LOGIN PAGE LOADING FAIL");
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean issueSearch(String tc) throws InterruptedException {
		try {
			Thread.sleep(1000);
			WebElement tmp = driver1.findElement(By.xpath("//*[@id='quicksearch']"));

			WebElement issueSearchLabel = tmp.findElement(By.xpath(".//div/label"));
			WebElement issueSearchInput = tmp.findElement(By.xpath(".//div/input"));
			Thread.sleep(500);
			
			issueSearchLabel.click();
			Thread.sleep(1000);
			issueSearchInput.clear();
			Thread.sleep(1000);
			issueSearchInput.sendKeys(tc);
			Thread.sleep(1000);
			issueSearchInput.sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			
		} catch (Exception e) {
			System.out.println("[JIRA REPORT] TC Search Fail");
		}
		//*[@id="key-val"]
		System.out.println("[JIRA REPORT] TC Search Success");
	    return true;
	}
	
	public boolean search(String tc, String status) throws InterruptedException {
	    return true;
	}
}