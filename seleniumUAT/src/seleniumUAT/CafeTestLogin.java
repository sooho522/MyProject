package seleniumUAT;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CafeTestLogin extends CafeTestSetup {
	private static String ID = "test";
	private static String PW = "test1234";
	private static String NAME = "홍길동";
	private static List loginNameList = new ArrayList();
	
	public void test(String tc) throws InterruptedException {
		try {
			if (tc == "tc-0001" || tc == null) {
				LoginTest("test","test1234!!");
			}
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * Login Test Case
	 */
	public boolean LoginTest(String id, String pw) {
		try {
			driver.get("http://oasis1418.cafe24.com");
			/*
			 *  Login ID 및 Pass 입력 후 로그인
			 */
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"header\"]/div[1]/div/div[2]/a[1]")).click();
			driver.findElement(By.xpath("//*[@id=\"member_id\"]")).clear();
			driver.findElement(By.xpath("//*[@id=\"member_id\"]")).sendKeys(id);
			driver.findElement(By.xpath("//*[@id=\"member_passwd\"]")).clear();
			driver.findElement(By.xpath("//*[@id=\"member_passwd\"]")).sendKeys(pw);
			Thread.sleep(1000);
			//*[@id="member_form_6108169245"]/div/fieldset/button
			driver.findElement(By.className("btnLogin")).click();
			Thread.sleep(2000);
			/*
			 * Login 이름이 일치 한다면 로그인 성공
			 */
			WebElement name = driver.findElement(By.xpath("//*[@id=\"header\"]/div[1]/div/div[2]/span/strong/span"));
			loginNameList.add(name.getText());
			if (loginNameList.contains(NAME)){
				System.out.println("Login Success : 이름(" + NAME + ")");
			} else {
				System.out.println("Login Fail : 이름이 일치하지 않습니다.");
			}
		}catch (Exception e){
			System.out.println("Login Fail");
			return false;
		}
		return true;
	}
}
