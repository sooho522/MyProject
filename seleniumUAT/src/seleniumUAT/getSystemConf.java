package seleniumUAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class getSystemConf {
	public static Map<String, String> CUSTOMER = new HashMap<String, String>();
	public static Map<String, String> CENTER = new HashMap<String, String>();
	public static Map<String, String> NODE = new HashMap<String, String>();
	public static Map<String, String> TENANT = new HashMap<String, String>();
	public static Map<String, String> ACD = new HashMap<String, String>(); // group dn
	public static Map<String, String> CTIQueue = new HashMap<String, String>(); // CTI queue dn
	public static Map<String, String> SIPTrunk = new HashMap<String, String>(); // SIP Trunk dn
	public static Map<String, String> ENDPOINT = new HashMap<String, String>();

	
	private String url = new String();
	private static WebDriver driver;
	private static String SUT_url;
	
	public void setDriver(WebDriver driver, String SUT_url) {
		this.driver = driver;
		this.SUT_url = SUT_url;
	}
	
	public void getConf() {
		getCustomer();
		getTenant();
		getCenter();
		getSystemNode();
		getEachNodeTenant();
		getACD();
		getCTIQueue();
		getSIPTrunk();		
		getEndPoint();
	}
	
	public String findGDNtree(String gdnName) {
		Map<String, String> map = new HashMap<String, String>();
		String retValue = null;
		String type = null;
		map.clear();
		System.out.println ("Input GDN Key:" + gdnName);
		
		String cells[] = gdnName.split(",");
		
		if (cells[3].equals("CTIQ")) {map.putAll(CTIQueue); type = "CTIQ";}
		else if (cells[3].equals("ACD")) {map.putAll(ACD); type = "ACD";}
		else if (cells[3].equals("SIP TRUNK")) {map.putAll(SIPTrunk); type = "SIP TRUNK";}
		else return null;
		System.out.println(type + " tree");
		try {
			for (String key : map.keySet()) {
				System.out.println("1st key: " + key + "  value: " + cells[0]);
				if (map.get(key).contains(cells[0])) {
				    	System.out.println("2nd key: " + map.get(key));
					for (String nodes : NODE.keySet()) {
					    	System.out.println ("3rd key: " + nodes + "  value: " + cells[1]);
						if (NODE.get(nodes).contains(cells[1])) {
						    	System.out.println("3rd key node: " + NODE.get(nodes));
							retValue = cells[0] + "," + key + "," + nodes + "," + type;
							System.out.println("findGDNTree: " + cells[0] + "," + key + "," + nodes + "," + type);
							Thread.sleep(1);
							return retValue;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println (e);
		}

		return null;
	}
	
	public java.util.List<String> findNodebyTenant(String tenant) {
		java.util.List<String> nodes = new ArrayList<String>();
		for (String key : NODE.keySet()) {
			if (tenant.contains(NODE.get(key))) {
				if (!nodes.contains(key))
					nodes.add(key);
			}
		}
		return nodes;
	}
	
	private void getEndPoint() {
		try {
			url = "/BT-EMS/ipr20/p10/IPR20S1010.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']"));
			java.util.List<WebElement> structMenu = struct.findElements(By.xpath(".//ul/li"));
			//System.out.println ("struct size: " + structMenu.size());
			for (int i = 0; i < structMenu.size(); i++) {
				//System.out.println (structMenu.get(i).getText());
				if (NODE.containsKey(structMenu.get(i).getText())) {
					structMenu.get(i).click();
					
					WebElement list = driver.findElement(By.xpath("//*[@class='datagrid-view2']"));
					list = list.findElement(By.xpath(".//*[@class='datagrid-body']"));
					java.util.List<WebElement> rows = list.findElements(By.xpath(".//table/tbody/tr"));

					for (int j = 0; j < rows.size(); j++) {
						java.util.List<WebElement> tabs = rows.get(j).findElements(By.xpath(".//td/div"));
						//String center = tabs.get(0).getText();
						String node = structMenu.get(i).getText();
						String endpoint = tabs.get(1).getText() + "," + tabs.get(0).getText();
						if (ENDPOINT.containsKey(node)) {
							if (ENDPOINT.get(node) != null) {
								endpoint = ENDPOINT.get(node) + "," + endpoint;
							}
							ENDPOINT.replace(node, endpoint);
						} else {
							ENDPOINT.put(node, endpoint);
						}
						System.out.println ("Add endpoint: " + node + " : " + ENDPOINT.get(node));
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void getCustomer() {
		//System.out.println ("get customer info");
		try {
			url = "/BT-EMS/ipr10/p10/IPR10S1010.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement list = driver.findElement(By.xpath("//*[@class='datagrid-view2']"));
			list = list.findElement(By.xpath(".//*[@class='datagrid-body']"));
			java.util.List<WebElement> customers = list.findElements(By.xpath(".//table/tbody/tr"));
			//System.out.println (customers.size());
			for (int i = 0; i < customers.size(); i++) {
				java.util.List<WebElement> tabs = customers.get(i).findElements(By.xpath(".//td/div"));
				System.out.println ("Add customer: " + tabs.get(0).getText());
				CUSTOMER.put(tabs.get(0).getText(), null);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void getCenter() {
		try {
			url = "/BT-EMS/ipr10/p20/IPR10S2010.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement list = driver.findElement(By.xpath("//*[@class='datagrid-view2']"));
			list = list.findElement(By.xpath(".//*[@class='datagrid-body']"));
			java.util.List<WebElement> customers = list.findElements(By.xpath(".//table/tbody/tr"));
			//System.out.println (customers.size());
			for (int i = 0; i < customers.size(); i++) {
				java.util.List<WebElement> tabs = customers.get(i).findElements(By.xpath(".//td/div"));
				System.out.println ("Add center: " + tabs.get(1).getText());
				CENTER.put(tabs.get(1).getText(), null);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void getSystemNode() {
		try {
			url = "/BT-EMS/ipr10/p20/IPR10S2020.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']"));
			java.util.List<WebElement> structMenu = struct.findElements(By.xpath(".//ul/li"));
			//System.out.println ("struct size: " + structMenu.size());
			for (int i = 0; i < structMenu.size(); i++) {
				//System.out.println (structMenu.get(i).getText());
				if (CENTER.containsKey(structMenu.get(i).getText())) {
					structMenu.get(i).click();
					Thread.sleep(500);
					
					WebElement list = driver.findElement(By.xpath("//*[@class='datagrid-view2']"));
					list = list.findElement(By.xpath(".//*[@class='datagrid-body']"));
					java.util.List<WebElement> nodes = list.findElements(By.xpath(".//table/tbody/tr"));
					//System.out.println (customers.size());
					for (int j = 0; j < nodes.size(); j++) {
						java.util.List<WebElement> tabs = nodes.get(j).findElements(By.xpath(".//td/div"));
						String center = tabs.get(0).getText();
						String node = tabs.get(2).getText();
						if (CENTER.containsKey(center)) {
							if (CENTER.get(center) != null) {
								node = CENTER.get(center) + "," + node;	
							}
							CENTER.replace(center, node);
						}
						System.out.println ("Add node: " + tabs.get(2).getText());
						NODE.put(tabs.get(2).getText(), null);
					}
				}
			}
			Thread.sleep(1);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void getTenant() {
		try {
			url = "/BT-EMS/ipr10/p10/IPR10S1020.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement list = driver.findElement(By.xpath("//*[@class='datagrid-view2']"));
			list = list.findElement(By.xpath(".//*[@class='datagrid-body']"));
			java.util.List<WebElement> tenants = list.findElements(By.xpath(".//table/tbody/tr"));
			for (int i = 0; i < tenants.size(); i++) {
				java.util.List<WebElement> tabs = tenants.get(i).findElements(By.xpath(".//td/div"));
				String customer = tabs.get(0).getText();
				String tenant = tabs.get(1).getText();
				if (CUSTOMER.containsKey(customer)) {
					if (CUSTOMER.get(customer) != null) {
						tenant = CUSTOMER.get(customer) + "," + tenant;	
					}
					CUSTOMER.replace(customer, tenant);
				}
				System.out.println ("Add tenant by customer: " + customer + ":" + tenant);
				TENANT.put(tabs.get(1).getText(), null);
			}

		} catch (Exception e) {
			System.out.println (e);
		}
	}
	
	private void getEachNodeTenant() {
		try {
			url = "/BT-EMS/ipr10/p20/IPR10S2030.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']/ul/li"));
			java.util.List<WebElement> strlist = struct.findElements(By.xpath(".//div"));
			//System.out.println("tntlist: " + strlist.size());
			for (int i = 0; i < strlist.size(); i++) {
				if (NODE.containsKey(strlist.get(i).getText())) {
					strlist.get(i).click();
					Thread.sleep(100);
					WebElement tenants = driver.findElement(By.xpath("//*[@class='panel layout-panel layout-panel-center']"));
					tenants = tenants.findElement(By.xpath(".//*[@class='datagrid-view2']"));
					java.util.List<WebElement> tntList = tenants.findElements(By.xpath(".//*[@class='datagrid-btable']/tbody/tr"));
					//System.out.println("tntlist: " + tntList.size());
					for (int j = 0; j < tntList.size(); j++) {
						java.util.List<WebElement> cells = tntList.get(j).findElements(By.xpath(".//td"));
						String node = strlist.get(i).getText();
						String tenant = NODE.get(node);
						if (tenant != null) {
							tenant = NODE.get(strlist.get(i).getText()) + "," + cells.get(0).getText();
						} else tenant = cells.get(0).getText();
						NODE.put(node, tenant);						
						System.out.println ("replace NODE by tenant: " + strlist.get(i).getText() + " : " + cells.get(0).getText());
					}
				}
			}
		} catch (Exception e) {
			System.out.println (e);
		}
	}
	
// get gdn
// gdn number,gdn name, gdn type
	private void getACD() {
		try {
			url = "/BT-EMS/ipr20/p30/IPR20S3010.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']/ul/li"));
			java.util.List<WebElement> strlist = struct.findElements(By.xpath(".//div"));
			//System.out.println("strlist: " + strlist.size());
			for (int i = 0; i < strlist.size(); i++) {
				if (TENANT.containsKey(strlist.get(i).getText())) {
					strlist.get(i).click();
					Thread.sleep(100);
					WebElement acdlist = driver.findElement(By.xpath("//*[@class='panel layout-panel layout-panel-center']"));
					acdlist = acdlist.findElement(By.xpath(".//*[@class='datagrid-view2']"));
					java.util.List<WebElement> ACDlist = acdlist.findElements(By.xpath(".//*[@class='datagrid-btable']/tbody/tr"));
					//System.out.println("acdlist: " + ACDlist.size());
					for (int j = 0; j < ACDlist.size(); j++) {
						java.util.List<WebElement> cells = ACDlist.get(j).findElements(By.xpath(".//td"));
						String tenant = strlist.get(i).getText();
						String acds = null;
						if (ACD.containsKey(tenant)) acds = ACD.get(tenant);
						//String acds = ACD.get(tenant);
						String values = cells.get(1).getText() + "," + cells.get(2).getText() + "," + cells.get(8).getText();
						if (acds != null) {
							acds = ACD.get(strlist.get(i).getText()) + "," + values;
						} else {
							acds = values;
						}
						ACD.put(tenant, acds);						
						System.out.println ("Add ACD: " + tenant + " : " + acds);
					}
				}
			}
		} catch (Exception e) {
			System.out.println (e);
		}
	}
	
	private void getCTIQueue() {
		try {
			url = "/BT-EMS/ipr20/p30/IPR20S3020.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']/ul/li"));
			java.util.List<WebElement> strlist = struct.findElements(By.xpath(".//div"));
			//System.out.println("strlist: " + strlist.size());
			for (int i = 0; i < strlist.size(); i++) {
				if (TENANT.containsKey(strlist.get(i).getText())) {
					strlist.get(i).click();
					Thread.sleep(100);
					WebElement ctilist = driver.findElement(By.xpath("//*[@class='panel layout-panel layout-panel-center']"));
					ctilist = ctilist.findElement(By.xpath(".//*[@class='datagrid-view2']"));
					java.util.List<WebElement> CTIlist = ctilist.findElements(By.xpath(".//*[@class='datagrid-btable']/tbody/tr"));
					//System.out.println("acdlist: " + CTIlist.size());
					for (int j = 0; j < CTIlist.size(); j++) {
						java.util.List<WebElement> cells = CTIlist.get(j).findElements(By.xpath(".//td"));
						String tenant = strlist.get(i).getText();
						String ctis = null;
						if (CTIQueue.containsKey(tenant)) ctis = CTIQueue.get(tenant);
						//String acds = ACD.get(tenant);
						String values = cells.get(2).getText() + "," + cells.get(3).getText();// + "," + cells.get(8).getText();
						if (ctis != null) {
							ctis = CTIQueue.get(strlist.get(i).getText()) + "," + values;
						} else {
							ctis = values;
						}
						CTIQueue.put(tenant, ctis);						
						System.out.println ("Add CTI Queue: " + tenant + " : " + ctis);
					}
				}
			}
		} catch (Exception e) {
			System.out.println (e);
		}
	}
	
	private void getSIPTrunk() {
		try {
			url = "/BT-EMS/ipr20/p30/IPR20S3030.do";
			driver.get(SUT_url + url);
			Thread.sleep(1000);
			WebElement struct = driver.findElement(By.xpath("//*[@id='ipr_left_contents']/ul/li"));
			java.util.List<WebElement> strlist = struct.findElements(By.xpath(".//div"));
			//System.out.println("strlist: " + strlist.size());
			for (int i = 0; i < strlist.size(); i++) {
				if (TENANT.containsKey(strlist.get(i).getText())) {
					strlist.get(i).click();
					Thread.sleep(100);
					WebElement siplist = driver.findElement(By.xpath("//*[@class='panel layout-panel layout-panel-center']"));
					siplist = siplist.findElement(By.xpath(".//*[@class='datagrid-view2']"));
					java.util.List<WebElement> SIPlist = siplist.findElements(By.xpath(".//*[@class='datagrid-btable']/tbody/tr"));
					//System.out.println("acdlist: " + CTIlist.size());
					for (int j = 0; j < SIPlist.size(); j++) {
						java.util.List<WebElement> cells = SIPlist.get(j).findElements(By.xpath(".//td"));
						String tenant = strlist.get(i).getText();
						String ctis = null;
						if (SIPTrunk.containsKey(tenant)) ctis = SIPTrunk.get(tenant);
						//String acds = ACD.get(tenant);
						String values = cells.get(1).getText() + "," + cells.get(2).getText();// + "," + cells.get(3).getText();// + "," + cells.get(8).getText();
						if (ctis != null) {
							ctis = SIPTrunk.get(strlist.get(i).getText()) + "," + values;
						} else {
							ctis = values;
						}
						SIPTrunk.put(tenant, ctis);						
						System.out.println ("Add SIP Trunk GDN: " + tenant + " : " + ctis);
					}
				}
			}
		} catch (Exception e) {
			System.out.println (e);
		}
	}
	
	private void getBSRGroup() {
		url = "/BT-EMS/ipr20/p30/IPR20S3040.do";
	}
// get gdn
	
}
