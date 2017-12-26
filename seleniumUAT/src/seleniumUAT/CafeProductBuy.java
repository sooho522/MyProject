package seleniumUAT;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class CafeProductBuy extends CafeTestSetup {
	private static String RNAME = "홍길동";
	private static String PNAME = "홍길동";
	private static String PHONE1 = "1234";
	private static String PHONE2 = "5678";
	private static String EMAIL = "test2101";
	private static List PRODUCTLIST = new ArrayList();
	private static List PRODUCTLIST2 = new ArrayList();
	
	public void test(String tc) throws InterruptedException {
		try {
			if (tc == "tc-0201" || tc == null) {
				ProductBuy(RNAME,PHONE1,PHONE2,EMAIL,PNAME);
			}
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * Product Buy
	 */
	public boolean ProductBuy(String RNAME, String PHONE1, String PHONE2, String EMAIL, String PNAME) {
		try {
			Thread.sleep(1000);
			/*
			 *  상품 구매
			 */
			// 구매 기본 정보 입력		
			WebElement Rname = driver.findElement(By.xpath("//*[@id=\"rname\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Rname);
			Rname.click();
			Rname.clear();
			Rname.sendKeys(RNAME);
			WebElement Phone1 = driver.findElement(By.xpath("//*[@id=\"rphone2_2\"]"));
			Phone1.click();
			Phone1.clear();
			Phone1.sendKeys(PHONE1);
			WebElement Phone2 = driver.findElement(By.xpath("//*[@id=\"rphone2_3\"]"));
			Phone2.click();
			Phone2.clear();
			Phone2.sendKeys(PHONE2);
			WebElement Email = driver.findElement(By.xpath("//*[@id=\"oemail1\"]"));
			Email.click();
			Email.clear();
			Email.sendKeys(EMAIL);
			Thread.sleep(1000);

			// 결재 수단 입력
			WebElement Pname = driver.findElement(By.xpath("//*[@id=\"pname\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Pname);
			Pname.click();
			Pname.clear();
			Pname.sendKeys(PNAME);
			WebElement BankAccount = driver.findElement(By.xpath("//*[@id=\"bankaccount\"]"));
			BankAccount.click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"bankaccount\"]/option[2]")).click();
			Thread.sleep(1000);

			// 결재 하기
			driver.findElement(By.xpath("//*[@id=\"chk_purchase_agreement0\"]")).click();
			driver.findElement(By.xpath("//*[@id=\"btn_payment\"]")).click();
				
		}catch (Exception e){
			System.out.println("상품 구매 실패");
			return false;
		}
		return true;
	}
}
