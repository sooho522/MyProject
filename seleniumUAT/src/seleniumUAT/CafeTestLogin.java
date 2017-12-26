package seleniumUAT;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CafeTestLogin extends CafeTestSetup {
	private static String ID = "test";
	private static String PW = "test1234";
	private static String NAME = "ȫ�浿";
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
			 *  Login ID �� Pass �Է� �� �α���
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
			 * Login �̸��� ��ġ �Ѵٸ� �α��� ����
			 */
			WebElement name = driver.findElement(By.xpath("//*[@id=\"header\"]/div[1]/div/div[2]/span/strong/span"));
			loginNameList.add(name.getText());
			if (loginNameList.contains(NAME)){
				System.out.println("Login Success : �̸�(" + NAME + ")");
			} else {
				System.out.println("Login Fail : �̸��� ��ġ���� �ʽ��ϴ�.");
			}
		}catch (Exception e){
			System.out.println("Login Fail");
			return false;
		}
		return true;
	}
}
