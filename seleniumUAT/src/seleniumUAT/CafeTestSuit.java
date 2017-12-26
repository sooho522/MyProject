package seleniumUAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import automation.chrometerminal;

public class CafeTestSuit extends CafeTestSetup {
	private static errorReason errReason = new errorReason();
	public static List testcase = new ArrayList();
	private static resultJiraReport jiraReport = new resultJiraReport();

	public void suit() throws InterruptedException {
	    String testID = null;
		String retString = null;
		boolean result = false;	
		String ts = null;
		/*
		 * 
		 */
		CafeTestLogin 				Login = new CafeTestLogin();
			Login.test("tc-0001");
		CafeMainProduct 			MainProduct = new CafeMainProduct();
			MainProduct.test("tc-0101");
		CafeProductBuy 			ProductBuy = new CafeProductBuy();
			ProductBuy.test("tc-0201");
		
		}
}