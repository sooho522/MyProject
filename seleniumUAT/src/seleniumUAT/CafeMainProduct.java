package seleniumUAT;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class CafeMainProduct extends CafeTestSetup {
	private static String PRODUCT = "jackets";
	private static String PRICE = "15,000";
	private static List PRODUCTLIST = new ArrayList();
	private static List PRODUCTLIST2 = new ArrayList();
	
	public void test(String tc) throws InterruptedException {
		try {
			if (tc == "tc-0101" || tc == null) {
				ProductSearch(PRODUCT,PRICE);
			}
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * Product Search
	 */
	public boolean ProductSearch(String product, String price) {
		try {
			driver.get("http://oasis1418.cafe24.com");
			Thread.sleep(1000);
			/*
			 *  Main ������ ��ǰ ��ȸ
			 */
			WebElement productName = driver.findElement(By.xpath("//*[@id=\"anchorBoxId_10\"]/div/div[2]/p/strong/a/span[2]"));
			PRODUCTLIST.add(productName.getText());
				if(PRODUCTLIST.contains(PRODUCT)){
					Thread.sleep(1000);
					System.out.println(PRODUCTLIST + "�� ��ȸ �Ǿ����ϴ�.");
					productName.click();
					Thread.sleep(1000);
					
					
					WebElement productName2 = driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/div[2]/div[2]/div[1]/table/tbody/tr[1]/td/span"));

					PRODUCTLIST2.add(productName2.getText());
						if(PRODUCTLIST2.contains(PRODUCT)){
							System.out.println(PRODUCTLIST2 + "���� ��ǰ �� ���� ������ �̵� ���� ");
							WebElement buyBotton = driver.findElement(By.xpath("//*[@id=\"btnBuy\"]"));
							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", buyBotton);
							buyBotton.click();
							Thread.sleep(1000);
							
						}else {
							System.out.println("���� ��ǰ �� ���� ������ �̵� ����");
						}
				}else {
					System.out.println(PRODUCTLIST + "��ȸ ��ǰ�� �����ϴ�.");
				}
		}catch (Exception e){
			return false;
		}
		return true;
	}
}
