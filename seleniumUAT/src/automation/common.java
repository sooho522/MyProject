package automation;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import seleniumUAT.screenShot;

public class common {
    private static WebDriver driver;
    private static Map<String, String> searchConditions = new HashMap<String, String>();
    private static Map<String, WebElement> ipr_left_contents_tree = new HashMap<String,WebElement>();
    public static Map<String, Map<String, Map<WebElement, String>>> ipr_right_content = new HashMap<String, Map<String, Map<WebElement, String>>>();
    public static Map<String, WebElement> menu_buttons = new HashMap<String, WebElement>();
    public static Map<Integer, String> ipr_search_id = new HashMap<Integer, String>();
    public static Map<Integer, String> ipr_search_type = new HashMap<Integer, String>();
    public static Map<Integer, WebElement> ipr_search_location = new HashMap<Integer, WebElement>();
    public static String errCode = null;
    public static String screenshot = null;
    
    public void clear() {
    	searchConditions.clear();
    	ipr_left_contents_tree.clear();
    	ipr_right_content.clear();
    	menu_buttons.clear();
    	ipr_search_id.clear();
    	ipr_search_type.clear();
    	ipr_search_location.clear();
    }
    
    public void set_driver(WebDriver arg) {
	driver = arg;
    }
    
    public void set_searchCondition(Map<String, String> arg) {
	searchConditions.clear();
	searchConditions.putAll(arg);
    }
    
    public WebElement get_top_window() throws InterruptedException {
	WebElement top_window = null;
	try {
	    java.util.List<WebElement> window_list = driver.findElements(By.xpath("//html/body/div"));
	    System.out.println("get_top_window() " + window_list.size());
	    for (int i = 0; i < window_list.size(); i++) {
		if (window_list.get(i).getAttribute("class").contains("panel window") || window_list.get(i).getAttribute("class").contains("panel combo-p")) {
		    if (window_list.get(i).getAttribute("style").contains("display: block")) {
			top_window = window_list.get(i);
		    }
		}
	    }
	} catch (Exception e) {
	    System.out.println(e);
	    return null;
	}
	return top_window;
    }
    
    public java.util.List<WebElement> get_buttons_on_panel(WebElement panel) throws InterruptedException {
	java.util.List<WebElement> buttons = new ArrayList<>();
	try {
	   java.util.List<WebElement> button_javascript = panel.findElements(By.xpath(".//a[@href='javascript:;']"));
	    System.out.println("get_buttons_on_panel() " + button_javascript.size());
	   for (int i = 0; i < button_javascript.size(); i++) {
	       String attribute = button_javascript.get(i).getAttribute("class");
	       if (attribute.contains("easyui-linkbutton") && attribute.contains("l-btn")) {
		   buttons.add(button_javascript.get(i));
	       }
	   }
	} catch (Exception e) {
	    System.out.println(e);
	    return null;
	}
	Thread.sleep(500);
	    System.out.println("get_buttons_on_panel() return " + buttons.size());
	return buttons;
    }
    
    public boolean click_button(java.util.List<WebElement> buttons, String buttonname) throws InterruptedException {
	try {
	    System.out.println("click_button(); " + buttons.size());
	    for (int i = 0; i < buttons.size(); i++) {
		if (buttons.get(i).getText().contains(buttonname)) {
		    buttons.get(i).click();
		}
	    }
	} catch (Exception e) {
	    System.out.println(e);
	    return false;
	}
	Thread.sleep(500);
	return true;
    }
    
    public boolean get_ipr_search() throws InterruptedException {
	Map<String, WebElement> ipr_search_conditions_inner = new HashMap<String, WebElement>();
	java.util.List<WebElement> ipr_search_panels = new ArrayList();
	WebElement ipr_search = null;
	try {
	    try {
		ipr_search = driver.findElement(By.xpath("/html/body/form/div[@class='panel layout-panel layout-panel-center']"));
	    } catch (Exception e) {
		try {
		    ipr_search = driver.findElement(By.xpath("/html/body/div[@class='panel layout-panel layout-panel-center']"));
		} catch (Exception e1) {
		    return false;
		}
	    }
	    //WebElement ipr_search = driver.findElement(By.xpath("/html/body/form/div[@class='panel layout-panel layout-panel-center']"));
	    try {
		ipr_search = ipr_search.findElement(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-north']"));
		ipr_search = ipr_search.findElement(By.xpath(".//div[@id='ipr_search']"));
		ipr_search_panels.add(ipr_search);
		System.out.println("single panel: " + ipr_search_panels.size());
	    } catch (Exception e2) {
		ipr_search_panels = ipr_search.findElements(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div"));
		for (int i = 0; i < ipr_search_panels.size(); i++) {
		    if (ipr_search_panels.get(i).getAttribute("class").contains("layout-split-proxy")) {
			ipr_search_panels.remove(i);
			i = i - 1;
			System.out.println("removed: " + i);
		    }
		}
		for (int i = 0; i < ipr_search_panels.size(); i++) {
		    System.out.println("class: " + ipr_search_panels.get(i).getAttribute("class"));
		    WebElement temp = ipr_search_panels.get(i).findElement(By.xpath(".//div/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-north']/div[@id='ipr_search']"));
		    ipr_search_panels.set(i, temp);
		}
		System.out.println("multiple panels: " + ipr_search_panels.size());
	    }
	    
	    for (int panel_count = 0; panel_count < ipr_search_panels.size(); panel_count++) {
		ipr_search = ipr_search_panels.get(panel_count);
		String id = null;
		String type = null;
		WebElement location = null;
		if (ipr_search.findElements(By.xpath(".//div[@class='rowspace_line']/table")).size() > 0) {
		    java.util.List<WebElement> tds = ipr_search.findElements(By.xpath(".//tbody/tr/td"));
		    System.out.println("td count: " + tds.size());
		    for (int i = 0; i < tds.size(); i++) {
			java.util.List<WebElement> tds_child = tds.get(i).findElements(By.xpath(".//*"));
			System.out.println("tds_child count: " + tds_child.size());
			if (tds_child.size() > 0) {
			    for (int j = 0; j < tds_child.size(); j++) {
				id = null;
				type = null;
				location = null;
				if (tds_child.get(j).getTagName().equals("input")) {
				    if (tds_child.get(j).getAttribute("class").contains("easyui-datebox")) {
					id = tds_child.get(j).getAttribute("id");
					type = "datebox";
					location = tds_child.get(j + 4);
					j = j + 5;
				    } else if (tds_child.get(j).getAttribute("class").contains("easyui-timespinner")) {
					id = tds_child.get(j).getAttribute("id");
					type = "spinner";
					location = tds_child.get(j);
				    } else if (tds_child.get(j).getAttribute("type").contains("checkbox")) {
					id = tds_child.get(j).getAttribute("id");
					type = "checkbox";
					location = tds_child.get(j);
				    } else if (tds_child.get(j).getAttribute("type").equals("text")) {
					id = tds_child.get(j).getAttribute("id");
					type = "text";
					location = tds_child.get(j);
				    }
				} else if (tds_child.get(j).getTagName().equals("select")) {
				    if (tds_child.get(j).getAttribute("class").contains("easyui-combobox")) {
					type = "combobox";
				    } else if (tds_child.get(j).getAttribute("class").contains("easyui-combotree")) {
					type = "combotree";
				    }
				    id = tds_child.get(j).getAttribute("id");
				    type = "combo";
				    location = tds_child.get(j + 4);
				    j = j + 5;
				} else if (tds_child.get(j).getTagName().equals("a")) {
				    if (tds_child.get(j).getAttribute("class").contains("easyui-linkbutton")) {
					id = tds_child.get(j + 2).getText();
					location = tds_child.get(j + 2);
					menu_buttons.put(id, location);
					j = j + 2;
				    }
				}
				if (id != null && type != null) {
				    Integer ipr_search_hash = id.hashCode();
				    System.out.println("id : " + id + " type: " + type + " index: " + i + " key: " + ipr_search_hash);
				    ipr_search_id.put(ipr_search_hash, id);
				    ipr_search_type.put(ipr_search_hash, type);
				    ipr_search_location.put(ipr_search_hash, location);
				}
			    }
			}
		    }
		} else if (ipr_search.findElements(By.xpath(".//div[@class='rowspace_line']/label")).size() > 0) {
		    System.out.println("label structure");
		    java.util.List<WebElement> elem = ipr_search.findElements(By.tagName("*"));
		    System.out.println("get_ipr_search: " + elem.size());
		    for (int i = 0; i < elem.size(); i ++) {
			ipr_search_conditions_inner.clear();
			type = null;
			location = null;
			id = null;
			String tag = elem.get(i).getTagName();
			if (tag.equals("input")) {
			    System.out.println("get_ipr_search tag: " + tag);
			    if (elem.get(i).getAttribute("type").contains("text")) {
				id = elem.get(i).getAttribute("name");
				type = "text";
				location = elem.get(i);
			    }			    
			} else if (tag.equals("select")) {
			    System.out.println("get_ipr_search tag: " + tag);
			    id = elem.get(i).getAttribute("comboname");
			    i++;
			    for (int k = i; k < elem.size(); k++) {
				if (!elem.get(k).getTagName().equals("option")) {
				    i = k;
				    break;
				}
			    }
			    type = "combobox";
			    i = i + 3;
			    location = elem.get(i);
			}
			if (type != null && location != null && id != null) {
			    Integer ipr_search_hash = id.hashCode();
			    System.out.println("id : " + id + " type: " + type + " index: " + i + " key: " + ipr_search_hash + " int key: " + (int)id.hashCode());
			    ipr_search_id.put(ipr_search_hash, id);
			    ipr_search_type.put(ipr_search_hash, type);
			    ipr_search_location.put(ipr_search_hash, location);
			}
		    }		
		    
		    WebElement btn_panel = ipr_search.findElement(By.xpath(".//div[@class='rowspace_line']/div[@class='optionCol2']"));
		    java.util.List<WebElement> btn = btn_panel.findElements(By.xpath(".//a/span/span"));
		    for (int i = 0; i < btn.size(); i++) {
			System.out.println("buttons: " + btn.get(i).getText());
			menu_buttons.put(btn.get(i).getText(), btn.get(i));
		    }
		} else if (ipr_search.findElements(By.xpath(".//div[@class='optionCol2']")).size() > 0) {
		    WebElement btn_panel = ipr_search.findElement(By.xpath(".//div[@class='rowspace_line']/div[@class='optionCol2']"));
		    java.util.List<WebElement> btn = btn_panel.findElements(By.xpath(".//a/span/span"));
		    for (int i = 0; i < btn.size(); i++) {
			System.out.println("button: " + btn.get(i).getText());
			menu_buttons.put(btn.get(i).getText(), btn.get(i));
		    }			
		}
	    }
	} catch (Exception e) {
	    System.out.println(e);
	    return false;
	}
	return true;
    }
	
    public boolean get_ipr_right_contents() throws InterruptedException {
	try {
	    Map<String, WebElement> panel_list = new HashMap<String, WebElement>();
	    panel_list.putAll(getTableInfo());
	    if (panel_list.isEmpty()) {
		System.out.println("Could not found panels");
		return false;
	    }
	    for (Map.Entry<String, WebElement> entity : panel_list.entrySet()) {
		if (!getSinglePanelContent(entity.getKey(), entity.getValue())) {
		    System.out.println("Could not get panel elements");
		    return false;
		}
	    }
	    for (Entry<String, Map<String, Map<WebElement, String>>> entity : ipr_right_content.entrySet()) {
		System.out.println("ipr_right_content_panels key: " + entity.getKey());
		for (Map.Entry<String, Map<WebElement, String>> entities : entity.getValue().entrySet()) {
		    System.out.println("second key: " + entities.getKey());
		    int k = 0;
		    for (Map.Entry<WebElement, String> unit : entities.getValue().entrySet()) {
			System.out.println("unit : " + unit.getValue() + " count: " + k++);			    
		    }
		}
	    }
	} catch (Exception e) {
	    return false;
	}
	return true;
    }
	
    	public Map<String, WebElement> getTableInfo() throws InterruptedException {
	    Map<String, WebElement> panels_map = new HashMap<String, WebElement>();
	    WebElement back_panel = null;
	    try {
		try {
		    back_panel = driver.findElement(By.xpath("//html/body/form/div[@class='panel layout-panel layout-panel-center']"));
		} catch (Exception e) {
		    try {
			back_panel = driver.findElement(By.xpath("//html/body/div[@class='panel layout-panel layout-panel-center']"));
		    } catch (Exception e1) {
			System.out.println(e1);
			return null;
		    }
		}
		//WebElement back_panel = driver.findElement(By.xpath("//html/body/form/div[@class='panel layout-panel layout-panel-center']"));
		WebElement layout_panel_center = null;
		try {
		    layout_panel_center = back_panel.findElement(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-center layout-split-center']"));
		    java.util.List<WebElement> easyui_layout = layout_panel_center.findElements(By.xpath(".//div[@id='ipr_right_contents']/div[@class='easyui-layout layout']/div"));
		    for (int i = 0; i < easyui_layout.size(); i++) {
			java.util.List<WebElement> divs =easyui_layout.get(i).findElements(By.xpath(".//div[@class='easyui-layout layout']/div"));
			for (int j = 0; j < divs.size(); j++) {
			    if (!divs.get(j).getAttribute("class").contains("layout-split-proxy")) {
				String title = divs.get(j).findElement(By.xpath(".//div[@class='panel-title']")).getText();
				WebElement panel = divs.get(j);
				panels_map.put(title, panel);
				System.out.println("Panel title: " + title);
			    }
			}
		    }
		} catch (Exception e1) {
		    layout_panel_center = back_panel.findElement(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-center']"));
		    String title = layout_panel_center.findElement(By.xpath(".//div[@class='panel-title']")).getText();
		    try { // find multi-panel
			java.util.List<WebElement> child_panels = layout_panel_center.findElements(By.xpath(".//div[@id='ipr_right_contents']/div[@class='easyui-layout layout']/div"));
			if (child_panels.size() <= 0) {
			    child_panels = layout_panel_center.findElements(By.xpath(".//div[@id='ipr_right_contents']/div[@class='panel datagrid']/div"));
			}
			System.out.println("panel size: " + child_panels.size());
			for (int i = 0; i < child_panels.size(); i++) {
			    System.out.println("panel: " + i);
			    if (!child_panels.get(i).getAttribute("class").contains("layout-split-proxy")) {
				String title_entry = title + ":" + i;
				panels_map.put(title_entry, child_panels.get(i));
				System.out.println("add panel information: " + title_entry);
			    }
			}
		    } catch (Exception e) {
			System.out.println(e);
			return null;
		    }
		    //System.out.println("Panel title: " + title);
		}
	    } catch (Exception e) {
		System.out.println(e);
		return null;
	    }
	    return panels_map;
	}
	
	public boolean getSinglePanelContent(String title, WebElement panel) throws InterruptedException {
	    Map<String, Map<WebElement, String>> ipr_child = new LinkedHashMap<String, Map<WebElement, String>>();
	    try {
		try {
		    panel = panel.findElement(By.xpath(".//div[@class='datagrid-view2']"));
		    WebElement panel_header = panel.findElement(By.xpath(".//div[@class='datagrid-header']"));
		    WebElement panel_body = panel.findElement(By.xpath(".//div[@class='datagrid-body']"));
		    WebElement panel_footer = panel.findElement(By.xpath(".//div[@class='datagrid-footer']"));
		    // get header values
		    java.util.List<WebElement> td = null; 
		    java.util.List<WebElement> tr = null;
		    td = panel_header.findElements(By.xpath(".//table/tbody/tr/td"));
		    ipr_child.put("header", getSingleRowValues(td));
		    ipr_right_content.put(title, ipr_child);
		    //ipr_right_content_panels.put(title, getSingleRowValues(td));
		    // get body values
		    tr = panel_body.findElements(By.xpath(".//table/tbody/tr"));
		    for (int i = 0; i < tr.size(); i++) {
			td = tr.get(i).findElements(By.xpath(".//td"));
			System.out.println("Body row td count: " + td.size());
			String map_name = "body:" + i;
			ipr_child.put(map_name, getSingleRowValues(td));
			ipr_right_content.put(title, ipr_child);
			//ipr_right_content_panels.put(title, getSingleRowValues(td));
		    }
		    // get footer values
		    try { 
			td = panel_footer.findElements(By.xpath(".//table/tbody/tr/td"));
			Map<WebElement, String> singleRowValues = getSingleRowValues(td);
			System.out.println("singleRowValues size: " + singleRowValues.size());
			if (singleRowValues.size() > 0) {
			    ipr_child.put("footer", singleRowValues);
			    ipr_right_content.put(title, ipr_child);
			}
			//ipr_right_content_panels.put(title, getSingleRowValues(td));			
		    } catch (Exception e) {
			System.out.println("Could not found footer");
			return false;
		    }
		} catch (Exception e1) {
		    System.out.println("Could not found datagrid");
		    return false;
		}
	    } catch (Exception e) {
		return false;
	    }
	    return true;
	}
	
	public Map<WebElement, String> getSingleRowValues(java.util.List<WebElement> td) throws InterruptedException {
	    Map<WebElement, String> retValue = new LinkedHashMap<WebElement, String>();
	    try {
		String idd;
		for (int i = 0; i < td.size(); i++) {
			WebElement cell = td.get(i);
			if (!cell.getAttribute("style").contains("display")) {
			    while (!cell.isDisplayed()) {
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cell);
			    }
			}
			idd = cell.getText();
			if (idd.length() <= 0 || idd.isEmpty()) {
			    idd = null;
			}
			//System.out.println("getSingleRowValues: " + i + " " + idd);
			retValue.put(cell, idd);
		}	
	    } catch (Exception e) {
		System.out.println(e);
		return null;
	    }
	    return retValue;
	}
	
	public boolean scrollingapanel(WebElement panel) throws InterruptedException {
	    try {
		WebElement table = panel.findElement(By.xpath("//div[@class='datagrid-view2']"));
		WebElement table1 = table.findElement(By.xpath(".//div[@class='datagrid-body']"));
		int listCnt = 0;
		java.util.List<WebElement> tableList = table1.findElements(By.xpath(".//table"));
		while (listCnt < tableList.size()) {
		    System.out.print("table size: " + tableList.size());
		    listCnt = tableList.size();
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 10000", table1);
		    Thread.sleep(1000);
		    tableList = table1.findElements(By.xpath(".//table"));			
		}
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}

	public boolean command_confirm() {
	    try {
		java.util.List<WebElement> div_panels = driver.findElements(By.xpath("//html/body/div"));
		WebElement panel = null;
		boolean popup = false;
		for (int i = 0; i < div_panels.size(); i++) {
		    if (div_panels.get(i).getAttribute("class").contains("panel window")) {
	    	    	if (div_panels.get(i).getAttribute("style").contains("block")) {
	    	    	    popup = true;
	    	    	    panel = div_panels.get(i);
	    	    	}
	    		}
		}
		
		if (!popup) return true;
		System.out.println("1:" + panel.getAttribute("class"));
		System.out.println("panel title: " + panel.findElement(By.xpath(".//*[@class='panel-title']")).getText());
		System.out.println("panel text: " + panel.getText());
		if (panel.getAttribute("style").contains("block")) {
		    System.out.println("2");
		    if (panel.findElement(By.xpath(".//*[@class='panel-title']")).getText().contains("泥섎━�솗�씤") || panel.findElement(By.xpath(".//*[@class='panel-title']")).getText().contains("泥섎━寃곌낵")) {
			System.out.println("3");
			WebElement cmd_panel = panel.findElement(By.xpath(".//*[@class='messager-button']"));
			System.out.println("4");
			java.util.List<WebElement> cmd_btn = cmd_panel.findElements(By.xpath(".//*[@class='l-btn']"));
			System.out.println("5 " + cmd_btn.size());
			for (int i = 0; i < cmd_btn.size(); i++) {
			    if (cmd_btn.get(i).getText().contains("�솗�씤")) {
				String result_msg = null;
				if (panel.findElements(By.tagName("textarea")).size() > 0) {
				    result_msg = panel.findElement(By.tagName("textarea")).getText();
				} else if (panel.findElements(By.xpath(".//*[@class='messager-body panel-body panel-body-noborder window-body']")).size() > 0) {
				    result_msg = panel.findElement(By.xpath(".//*[@class='messager-body panel-body panel-body-noborder window-body']")).getText();
				}
				System.out.println("6");
				Thread.sleep(200);
				cmd_btn.get(i).click();
				Thread.sleep(500);
				if (!result_msg.contains("異붽��릺�뿀�뒿�땲�떎") && !result_msg.contains("�닔�젙 �릺�뿀�뒿�땲�떎") && !result_msg.contains("�궘�젣�븯�떆寃좎뒿�땲源�") && !result_msg.contains("�젙�긽�쟻�쑝濡�") && !result_msg.contains("�궘�젣 �븯�떆寃좎뒿�땲源�")) {
				    errCode = result_msg;
				    System.out.println("�젙�긽 泥섎━ �떎�뙣: " + result_msg);
				    return false;
				}
				System.out.println("7");
				Thread.sleep(1000);
				if (!command_confirm()) {
				    System.out.println("泥섎━ �떎�뙣");
				    return false;
				}
				break;
			    }
			}
		    } else if (panel.findElement(By.xpath(".//*[@class='panel-title']")).getText().contains("�삤瑜�")) {
			System.out.println("8");
		    	screenShot(errCode);
		    	errCode = panel.findElement(By.xpath(".//*[@class='panel']")).getText();
		    	panel.findElement(By.xpath(".//*[@class='l-btn-left']")).click(); //�떕湲�
		    	return false;
		    }
		}
	    } catch (Exception e) {
		System.out.println("泥섎━ �떎�뙣");
		System.out.println(e);
		return false;
	    }
	    return true;
	}

	public boolean get_ipr_left_contents_child(WebElement ipr_left_contents, String path, String parent_name) throws InterruptedException {
	    try {
		System.out.println("Parent name: " + parent_name);
		path = path + "/ul/li";
		java.util.List<WebElement> child = ipr_left_contents.findElements(By.xpath(path + "/div"));
		for (int i = 0; i < child.size(); i++) {
		    String element_name = child.get(i).getText();
		    if (parent_name != null) {
			element_name = parent_name + "," + element_name;
		    }
		    WebElement key = child.get(i);
		    ipr_left_contents_tree.put(element_name, key);
		    System.out.println("element name: " + element_name);
		    get_ipr_left_contents_child(ipr_left_contents, path, element_name);
		}
	    } catch (Exception e) {
		return false;
	    }
	    return true;
	}
	
	public int get_menu_depth(WebElement panel, int depth, String path) throws InterruptedException {
	    try {
		path = path + "/ul/li";
		java.util.List<WebElement> temp = panel.findElements(By.xpath(path));
		if (temp.size() <= 0) return depth;
		depth = depth + 1;
		depth = get_menu_depth(panel, depth, path);
	    } catch (Exception e) {
		return depth;
	    }
	    return depth;
	}
	
	public boolean get_ipr_left_content() throws InterruptedException {
	    WebElement ipr_left_contents = null;
	    ipr_left_contents_tree.clear();
	    try {
		try {
		    ipr_left_contents = driver.findElement(By.xpath("//html/body/form/div[@class='panel layout-panel layout-panel-center']"));
		} catch (Exception e) {
		    try {
			ipr_left_contents = driver.findElement(By.xpath("//html/body/div[@class='panel layout-panel layout-panel-center']"));
		    } catch (Exception e1) {
			System.out.println(e1);
			return false;
		    }
		}
		ipr_left_contents = ipr_left_contents.findElement(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-west layout-split-west']"));
		ipr_left_contents = ipr_left_contents.findElement(By.xpath(".//div[@id='ipr_left_contents']"));
		ipr_left_contents_tree.putAll(get_ipr_left_rec(ipr_left_contents));
		
		for (Map.Entry<String, WebElement> elem : ipr_left_contents_tree.entrySet()) {
		    System.out.println("ipr_left_content: " + elem.getKey());
		}
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public Map<String, WebElement> get_ipr_left_rec(WebElement root) {
	    Map<String, WebElement> entity = new HashMap<String, WebElement>();
	    try {
		java.util.List<WebElement> nodes = root.findElements(By.xpath("./ul"));
		for (int i = 0; i < nodes.size(); i++) {
		    java.util.List<WebElement> childs = nodes.get(i).findElements(By.xpath("./li"));
		    for (int j = 0; j < childs.size(); j++) {
			String nodeId = childs.get(j).findElement(By.xpath("./div/span[@class='tree-title']")).getText();
			entity.put(nodeId, childs.get(j).findElement(By.xpath("./div")));
			Map<String, WebElement> tmp = new HashMap<String, WebElement>();
			if ((tmp = get_ipr_left_rec(childs.get(j))) != null) {
			    for (Map.Entry<String, WebElement> element : tmp.entrySet()) {
				entity.put(nodeId + "," + element.getKey(), element.getValue());
			    }
			}
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		return null;
	    }
	    return entity;
	}
	
	public boolean get_ipr_left_contents() throws InterruptedException {
	    int define_level = 0;
	    WebElement ipr_left_contents = null;
	    try {
		try {
		    ipr_left_contents = driver.findElement(By.xpath("//html/body/form/div[@class='panel layout-panel layout-panel-center']"));
		} catch (Exception e) {
		    try {
			ipr_left_contents = driver.findElement(By.xpath("//html/body/div[@class='panel layout-panel layout-panel-center']"));
		    } catch (Exception e1) {
			System.out.println(e1);
			return false;
		    }
		}
		
		ipr_left_contents = ipr_left_contents.findElement(By.xpath(".//div[@id='ipr_contents']/div[@class='easyui-layout layout']/div[@class='panel layout-panel layout-panel-west layout-split-west']"));
		String path = ".//div[@id='ipr_left_contents']";
		define_level = get_menu_depth(ipr_left_contents, 0, path);
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }

	    System.out.println("menu depth: " + define_level);
	    
	    ipr_left_contents = driver.findElement(By.xpath("//*[@id='ipr_left_contents']"));
	    String ipr_left_contents_id = driver.findElement(By.xpath("//*[@id='ipr_left_contents']/ul")).getAttribute("id");
	    WebElement left_contents_panel = ipr_left_contents.findElement(By.xpath("//*[@id='" + ipr_left_contents_id + "']"));
	    try {
		java.util.List<WebElement> btn = left_contents_panel.findElements(By.xpath(".//*[@class='tree hit tree-collapsed']"));
		for (int i = 0; i < btn.size(); i++) {
		    btn.get(i).click();
		}
	    } catch (Exception e) {
		System.out.println(e);
		System.out.println("No tree collapsed");
	    }
	    java.util.List<WebElement> lv1 = ipr_left_contents.findElements(By.xpath(".//ul[@id='" + ipr_left_contents_id + "']/li"));
	    //System.out.println("node list size: " + customer.size());
	    if (lv1.size() <= 0) {
		//errReason.add("怨좉컼�궗 �젙蹂� 諛� �꽱�꽣 �꽕�젙�씠 �뾾�쓬");
		return false;
	    }
	    //depth 0
	    try {
		for (int i = 0; i < lv1.size(); i++) {
		    lv1 = ipr_left_contents.findElements(By.xpath(".//ul[@id='" + ipr_left_contents_id + "']/li/div"));
		    String lv1Name = lv1.get(i).getText();
		    ipr_left_contents_tree.put(lv1Name, lv1.get(i));
		    //System.out.println(customer.get(i).getText() + " Key: " + customerName);
		    java.util.List<WebElement> lv2 = ipr_left_contents.findElements(By.xpath(".//ul[@id='" + ipr_left_contents_id + "']/li/ul/li/div"));
		    java.util.List<WebElement> lv2List = ipr_left_contents.findElements(By.xpath(".//ul[@id='" + ipr_left_contents_id + "']/li/ul/li"));
		    for (int j = 0 ; j < lv2.size(); j++) {
			boolean cond = true;
			String key = lv1Name + "," + lv2.get(j).getText();
			ipr_left_contents_tree.put(key, lv2.get(j));
			//System.out.println(lv2.get(j).getText() + " Key: " + key + " CenterList Size: " + lv2List.size());
			if (define_level == 3) {
			    java.util.List<WebElement> lv3List = null;
			    try {
				lv3List = lv2List.get(j).findElements(By.xpath(".//ul/li/div"));
				//System.out.println("node size: " + node.size());
				if (lv3List.size() <= 0) cond = false;
			    } catch (Exception e) {
				cond = false;
			    }
			    for (int k = 0; k <lv3List.size() && cond; k++) {
				String lv3Name = key + "," + lv3List.get(k).getText();
				ipr_left_contents_tree.put(lv3Name, lv3List.get(k));
				System.out.println("add ipr_left_contents_tree: " + lv3List.get(k).getText() + " Key: " + lv3Name);
			    }
			} else if (define_level == 4) {
			    java.util.List<WebElement> lv3List = null;
			    java.util.List<WebElement> lv3 = null;
			    java.util.List<WebElement> lv4 = null;
			    try {
				
				lv3List = lv2List.get(j).findElements(By.xpath(".//ul/li/div"));
				lv3 = lv2List.get(j).findElements(By.xpath(".//ul/li"));
				//System.out.println("node size: " + node.size());
				if (lv3List.size() <= 0) cond = false;
			    } catch (Exception e) {
				cond = false;
			    }
			    
			    for (int k = 0; k <lv3List.size() && cond; k++) {
				boolean cond2 = true;
				String lv3Name = key + "," + lv3List.get(k).getText();
				ipr_left_contents_tree.put(lv3Name, lv3List.get(k));
				System.out.println("add ipr_left_contents_tree lv3: " + lv3List.get(k).getText() + " Key: " + lv3Name);
				try {
				    lv4 = lv3.get(k).findElements(By.xpath(".//ul/li/div"));
				    if (lv4.size() <= 0) cond2 = false;
				} catch (Exception e) {
				    cond2 = false;
				}
				for (int l = 0; l < lv4.size() && cond2; l++) {
				    String lv4Name = lv3Name + "," + lv4.get(l).getText();
				    ipr_left_contents_tree.put(lv4Name, lv4.get(l));
				    System.out.println("add ipr_left_contents_tree lv4: " + lv4.get(l).getText() + " Key: " + lv4Name);
				}
			    }			    
			}
		    }
		}
		
	    } catch (Exception e) {
		System.out.println(e);
		//System.out.println("No centers");
		//errReason.add("�꽱�꽣 �꽕�젙�씠 �뾾�쓬");
		return false;
	    }
	    //System.out.println(tree.toString());
	    return true;
	}
	
	//public boolean getCenter(String select) throws InterruptedException {
	public boolean getCenter() throws InterruptedException {
	    WebElement leftContents = driver.findElement(By.xpath("//*[@id='ipr_left_contents']"));
	    WebElement centerNode = leftContents.findElement(By.xpath("//*[@id='trCenterNode']"));
	    try {
		java.util.List<WebElement> btn = centerNode.findElements(By.xpath(".//*[@class='tree hit tree-collapsed']"));
		for (int i = 0; i < btn.size(); i++) {
		    btn.get(i).click();
		}
	    } catch (Exception e) {
		System.out.println("No tree collapsed");
	    }
	    java.util.List<WebElement> customer = leftContents.findElements(By.xpath(".//ul[@id='trCenterNode']/li"));
	    System.out.println("node list size: " + customer.size());
	    if (customer.size() <= 0) {
		//errReason.add("怨좉컼�궗 �젙蹂� 諛� �꽱�꽣 �꽕�젙�씠 �뾾�쓬");
		return false;
	    }
	    //depth 0
	    try {
		for (int i = 0; i < customer.size(); i++) {
		    customer = leftContents.findElements(By.xpath(".//ul[@id='trCenterNode']/li/div"));
		    String customerName = customer.get(i).getText();
		    ipr_left_contents_tree.put(customerName, customer.get(i));
		    java.util.List<WebElement> node = leftContents.findElements(By.xpath(".//ul[@id='trCenterNode']/li/ul/li/div"));
		    for (int j = 0 ; j < node.size(); j++) {
			String key = customerName + "," + node.get(j).getText();
			ipr_left_contents_tree.put(key, node.get(j));
			/*
			if (!select.equals(null)) {
			    if (key.equals(select)) {
				node.get(j).click();
				Thread.sleep(500);
			    }
			}*/
			System.out.println("get_center: " + node.get(j).getText());
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		System.out.println("No centers");
		//errReason.add("�꽱�꽣 �꽕�젙�씠 �뾾�쓬");
		return false;
	    }
	    //System.out.println(tree.toString());
	    return true;
	}
	
	public boolean select_ipr_left_contents () throws InterruptedException {
		/*
	    String treeSelect = null;
	    if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗") && searchConditions.containsKey("�뀒�꼳�듃") && searchConditions.containsKey("�끂�뱶")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗") + "," + searchConditions.get("�뀒�꼳�듃") + "," + searchConditions.get("�끂�뱶");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗") && searchConditions.containsKey("�뀒�꼳�듃")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗") + "," + searchConditions.get("�뀒�꼳�듃");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥");
	    }
	    System.out.println("treeSelect:" + treeSelect);
	    if (ipr_left_contents_tree.containsKey(treeSelect)) {
		try {
		    ipr_left_contents_tree.get(treeSelect).click();
		    Thread.sleep(500);
		} catch (Exception e) {
		    System.out.println(e);
		}
	    } else {
		System.out.println("Must select node");
		//errReason.add("�꽑�깮�븳 �떆�뒪�뀥, 怨좉컼�궗, �뀒�꼳�듃媛� �뾾�쓬");
		return false;
	    }
	    
	    return true;
	    */
		
	    String treeSelect = null;
	    if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗") && searchConditions.containsKey("�뀒�꼳�듃") && searchConditions.containsKey("�끂�뱶")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗") + "," + searchConditions.get("�뀒�꼳�듃") + "," + searchConditions.get("�끂�뱶");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗") && searchConditions.containsKey("�뀒�꼳�듃")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗") + "," + searchConditions.get("�뀒�꼳�듃");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥") && searchConditions.containsKey("怨좉컼�궗")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥") + "," + searchConditions.get("怨좉컼�궗");
	    } else if (searchConditions.containsKey("�떆�뒪�뀥")) {
		treeSelect = searchConditions.get("�떆�뒪�뀥");
	    }
	    
	    String key = "depth0";
	    int i = 0;
	    while (searchConditions.containsKey(key)) {
		if (treeSelect == null) treeSelect = searchConditions.get(key);
		else treeSelect = treeSelect + "," + searchConditions.get(key);
		i++;
		key = "depth" + i;
	    }
	    System.out.println("treeSelect:" + treeSelect);

	    if (ipr_left_contents_tree.containsKey(treeSelect)) {
		try {
		    ipr_left_contents_tree.get(treeSelect).click();
		    Thread.sleep(500);
		} catch (Exception e) {
		    System.out.println(e);
		}
	    } else {
		System.out.println("Must select node");
		//errReason.add("�꽑�깮�븳 �떆�뒪�뀥, 怨좉컼�궗, �뀒�꼳�듃媛� �뾾�쓬");
		return false;
	    }
	    
	    return true;
	}
	
	public Map<String, WebElement> getSingleDepthMultiSelectDropDownMenu(WebElement base) throws InterruptedException {
	    Thread.sleep(500);
	    Map<String, WebElement> list = new HashMap<String, WebElement>();
	    java.util.List<WebElement> menuList = base.findElements(By.xpath(".//div"));
	    for (int i = 0; i < menuList.size(); i++) {
		WebElement btn = null;
		//System.out.println("Multi Select Drop Down Menu: " + menuList.get(i).getText());
		try {
		    btn = menuList.get(i).findElement(By.xpath(".//span[@class='tree-checkbox tree-checkbox0']"));
		} catch (Exception e) {
		    try {
			btn = menuList.get(i).findElement(By.xpath(".//span[@class='tree-checkbox tree-checkbox1']"));
		    } catch (Exception e1) {
			btn = menuList.get(i).findElement(By.xpath(".//span[@class='tree-checkbox tree-checkbox2']"));
		    }
		}
		list.put(menuList.get(i).getText(), btn);
	    }
	    Thread.sleep(500);
	    return list;
	}
	
	public boolean selectSingleDepthMultiSelectDropDownMenu(Map<String, WebElement> list, String keys) throws InterruptedException {
	    String[] key = keys.split(",");
	    Thread.sleep(500);
	    try {
		for (Map.Entry<String, WebElement> elem : list.entrySet()) { // uncheck all
		    if (elem.getValue().getAttribute("class").contains("checkbox1")) {
			elem.getValue().click();
			Thread.sleep(500);
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		//errReason.add("泥댄겕諛뺤뒪 珥덇린�솕 �떎�뙣: ");
		return false;
	    }
	    Thread.sleep(500);

	    try {
		for (int i = 0; i < key.length; i++) {
		    if (list.containsKey(key[i])) {
			try {
			    list.get(key[i]).click();
			    Thread.sleep(500);
			} catch (Exception e) {
			    System.out.println(e);
			}
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		//errReason.add("泥댄겕諛뺤뒪 �꽑�깮 �떎�뙣: " + keys);
		return false;
	    }
	    Thread.sleep(500);
	    return true;
	}
	
	public boolean selectRowonPanel(WebElement tab_panels) throws InterruptedException {
	    try {
		Thread.sleep(1000);
		WebElement selected_panel = null;
		java.util.List<WebElement> tab_panel = tab_panels.findElements(By.xpath(".//*[@class='panel']"));
		System.out.println("panel size: " + tab_panel.size());
		java.util.List<WebElement> tabs = tab_panels.findElements(By.xpath(".//div/ul[@class='tabs']/li"));
		System.out.println("tabs size: " + tabs.size());
		if (tabs.size() <= 0) {
		    tabs = tab_panels.findElements(By.xpath(".//div[@class='panel datagrid']"));
		    System.out.println("single tabs size: " + tabs.size());
		}
		
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean selectValues(WebElement tab_panels) throws InterruptedException {
	    try {
		Thread.sleep(1000);
		WebElement selected_panel = null;
		java.util.List<WebElement> tab_panel = tab_panels.findElements(By.xpath(".//*[@class='panel']"));
		System.out.println("panel size: " + tab_panel.size());
		java.util.List<WebElement> tabs = tab_panels.findElements(By.xpath(".//div/ul[@class='tabs']/li"));
		System.out.println("tabs size: " + tabs.size());
		if (tabs.size() <= 0) {
		    tabs = tab_panels.findElements(By.xpath(".//div[@class='panel datagrid']"));
		    System.out.println("single tabs size: " + tabs.size());
		}
		for (int t = 0; t < tabs.size(); t++) {
		    boolean datagridpanel = false;
		    System.out.println("tab name: " + tabs.get(t).getText());
		    System.out.println(" ");
		    if (tabs.size() > 1)
			tabs.get(t).click();
		    Thread.sleep(1000);
		    if (tab_panel.size() > 0) {
			for (int i = 0; i < tab_panel.size(); i++) {
			    String style = tab_panel.get(i).getAttribute("style");
			    if (style.contains("display") && style.contains("block")) {
				selected_panel = tab_panel.get(i);
			    }
			}
			System.out.println("selected_panel: " + selected_panel.getClass());
		    } else {
			selected_panel = tab_panels;
		    }
		    try {
			String key = null;
			WebElement chkbox = null;
			selected_panel = selected_panel.findElement(By.xpath(".//*[@class='datagrid-view']"));
			java.util.List<WebElement> tr = selected_panel.findElements(By.xpath(".//div[@class='datagrid-view2']/div[@class='datagrid-body']/table/tbody/tr"));
			System.out.println("tr size: " + tr.size());
			for (int i = 0; i < tr.size(); i++) {
			    key = null;
			    //String type = null;
			    java.util.List<WebElement> td = tr.get(i).findElements(By.xpath(".//td"));
			    for (int j = 0; j < td.size(); j++) {
			    	if (td.get(j).getAttribute("field").contains("check")) {
			    	    key = "check";
			    	    if (td.get(j).isSelected()) {
			    		td.get(j).click();
			    		Thread.sleep(200);
			    	    }
			    	    String value = null;
			    	    for (int k = j + 1; k < td.size(); k++) {
			    		if (value == null) value = td.get(k).getText();
			    		else value = value + "," + td.get(k).getText();
			    	    }
			    	    System.out.println("check value: " + value);
			    	    if (searchConditions.containsValue(value)) {
			    		if (!td.get(j).isSelected()) {
			    		    td.get(j).click();
			    		    Thread.sleep(200);
			    		}
			    	    }
			    	    j = j + td.size();
			    	}
			    }
			    if (key == null) {
				if (searchConditions.containsValue(tr.get(i).getText().replace("\n", ","))) {
				    tr.get(i).click();
				}
			    }
			}
		    } catch (Exception e1) {
			System.out.println(e1);
			return false;
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean insertValues(WebElement tab_panels) throws InterruptedException {	    
	    Thread.sleep(1000);
	    //WebElement textBox = null;
	    //System.out.println("panel class attribute: " + tab_panels.getAttribute("class"));
	    WebElement selected_panel = null;
	    java.util.List<WebElement> tab_panel = tab_panels.findElements(By.xpath(".//*[@class='panel']"));
	    System.out.println("panel size: " + tab_panel.size());
	    java.util.List<WebElement> tabs = tab_panels.findElements(By.xpath(".//div/ul[@class='tabs']/li"));
	    System.out.println("tabs size: " + tabs.size());
	    if (tabs.size() <= 0) {
		tabs = tab_panels.findElements(By.xpath(".//div[@class='panel datagrid']"));
		System.out.println("single tabs size: " + tabs.size());
		if (tabs.size() <= 0) {
		    tabs = tab_panels.findElements(By.xpath(".//table[@class='tab_table']"));
		}
	    }
	    for (int t = 0; t < tabs.size(); t++) {
			boolean datagridpanel = false;
			System.out.println("tab name: " + tabs.get(t).getText());
			System.out.println(" ");
			if (tabs.size() > 1)
			    tabs.get(t).click();
			Thread.sleep(1000);
		if (tab_panel.size() > 0) {
		    for (int i = 0; i < tab_panel.size(); i++) {
			String style = tab_panel.get(i).getAttribute("style");
			if (style.contains("display") && style.contains("block")) {
			    selected_panel = tab_panel.get(i);
			}
		    }
		    System.out.println("selected_panel: " + selected_panel.getClass());
		} else {
		    selected_panel = tab_panels;
		}

		try {
		    String key = null;
		    WebElement chkbox = null;
		    java.util.List<WebElement> tr = null;
		    try {
			selected_panel = selected_panel.findElement(By.xpath(".//*[@class='datagrid-view']"));
			tr = selected_panel.findElements(By.xpath(".//div[@class='datagrid-view2']/div[@class='datagrid-body']/table/tbody/tr"));
			    System.out.println("tr size: " + tr.size());
		    } catch (Exception e) {
			tr = selected_panel.findElements(By.xpath(".//tbody/tr"));
			System.out.println("tr size: .//tbody/tr: " + tr.size());
		    }
		    System.out.println("tr size: " + tr.size());
		    
		    for (int i = 0; i < tr.size(); i++) {
			key = null;
			String type = null;
			java.util.List<WebElement> td = tr.get(i).findElements(By.xpath(".//td"));
			for (int j = 0; j < td.size(); j++) {
			    if (td.get(j).getAttribute("field").contains("ck") && !td.get(j).getAttribute("field").contains("check")) {
				System.out.println("insertValue contains ck " + td.get(j).getAttribute("field"));
				type = "ck";
				String value = null;
				for (int k = 1; k < td.size() - 1; k++) {
				    if (value == null) {
					value = td.get(k).getText();
				    } else {
					value = value + "," + td.get(k).getText();
				    }
				}
				System.out.println("insert key: " + value);
				if (searchConditions.containsKey(value)) {
				    td.get(td.size() - 1).click();
				    Thread.sleep(100);
				    td.get(td.size() - 1).findElement(By.xpath(".//input[@type='text']")).click();
				    Thread.sleep(100);
				    td.get(td.size() - 1).findElement(By.xpath(".//input[@type='text']")).clear();
				    Thread.sleep(100);
				    td.get(td.size() - 1).findElement(By.xpath(".//input[@type='text']")).sendKeys(searchConditions.get(value));
				    Thread.sleep(100);
				    j = j + td.size() - 1;
				}
			    } else if (td.get(j).getAttribute("field").contains("chkValue")) {
				chkbox = td.get(i).findElement(By.xpath(".//div/input"));
				if (chkbox.isSelected()) {
				    chkbox.click();
				    Thread.sleep(200);
				}
			    } else if (td.get(j).getAttribute("field").contains("check")) {
				if (td.get(j).isSelected()) {
				    td.get(j).click();
				    Thread.sleep(200);
				}
				String value = null;
				for (int k = j + 1; k < td.size(); k++) {
				    if (value == null) value = td.get(k).getText();
				    else value = value + "," + td.get(k).getText();
				}
				System.out.println("check value: " + value);
				if (searchConditions.containsValue(value)) {
				    if (!td.get(j).isSelected()) {
					td.get(j).click();
					Thread.sleep(200);
				    }
				}
				j = j + td.size();
			    } else {
				for (int k = j; k < td.size(); k++) {
				    if (key == null) key = td.get(k).getText();
				    else key = key + "," + td.get(k).getText();
				}
				System.out.println("key:" + key);
				j = td.size();
			    }
			}
			if (type != "ck" && type != "check") {
			    for (Map.Entry<String, String> entity : searchConditions.entrySet()) {
				System.out.println("row: " + entity.getKey() + " : " + entity.getValue());
				if (entity.getKey().contains(tabs.get(t).getText())) {
				    if (entity.getValue().contains(key)) {
					chkbox.click();
					Thread.sleep(200);
				    }
				}
			    }
			}
		    }
		    datagridpanel = true;
		} catch (Exception e) {
		    System.out.println(e);
		}

		java.util.List<WebElement> td = selected_panel.findElements(By.xpath(".//tr/td"));
		for (int i = 0; i < td.size(); i++) {
		    System.out.println("ELEMENT: " + td.get(i).getText().replaceAll("\\r\\n|\\r|\\n", " "));
		    if (td.get(i).getAttribute("class").contains("dlg_label") && !td.get(i).getAttribute("class").contains("dlg_label_category" ) && !datagridpanel) {
			if (searchConditions.containsKey(td.get(i).getText().replaceAll("\\r\\n|\\r|\\n", " "))) {
			    String input_type = null;
			    java.util.List<WebElement> input = null;
			    try {
				input = td.get(i + 1).findElements(By.tagName("select"));
				if (td.get(i + 1).findElements(By.tagName("select")).size() > 0) {
				    input = td.get(i + 1).findElements(By.tagName("select"));
				    input_type = input.get(0).getAttribute("class");
				} else if (td.get(i + 1).findElements(By.tagName("textarea")).size() > 0) {
				    input = td.get(i + 1).findElements(By.tagName("textarea"));
				    input_type = input.get(0).getTagName();
				} else if (td.get(i + 1).findElements(By.tagName("text")).size() > 0) {
				    input = td.get(i + 1).findElements(By.tagName("text"));
				    input_type = input.get(0).getAttribute("type");
				} else if (td.get(i + 1).findElements(By.xpath(".//*[@type='text']")).size() > 0) {
				    input = td.get(i + 1).findElements(By.xpath(".//*[@type='text']"));
				    input_type = input.get(0).getAttribute("type");					
				} else if (td.get(i + 1).findElements(By.xpath(".//*[@type='password']")).size() > 0) {
				    input = td.get(i + 1).findElements(By.xpath(".//*[@type='password']"));
				    input_type = input.get(0).getAttribute("type");					
				} else if (td.get(i + 1).findElements(By.xpath(".//*[@type='radio']")).size() > 0) {
				    input = td.get(i + 1).findElements(By.xpath(".//*[@type='radio']"));
				    input_type = input.get(0).getAttribute("type");
				} else {
				    java.util.List<WebElement> list = td.get(i + 1).findElements(By.tagName("input"));
				    for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getAttribute("numberboxname") != null) {
					    input.add(list.get(j));
					    input_type = "numberspinner-f";
					}
				    }
				}
			    } catch (Exception e) {
				System.out.println(e);
			    }
			    System.out.println("set value: " + input_type);
			    if (input_type.equals("text") || input_type.equals("password")) {
				WebElement txtbox = null;
				try {
				    txtbox = td.get(i + 1).findElement(By.xpath(".//input[@type='text']"));
				} catch (Exception e) {
				    txtbox = td.get(i + 1).findElement(By.xpath(".//input[@type='password']"));
				}
				System.out.println("child of child: " + searchConditions.get(td.get(i).getText()) + " : " + td.get(i + 1).getText());
				try {
				    txtbox.getAttribute("disabled").equals("disabled");
				} catch (Exception e3) {
				    String tmp = searchConditions.get(td.get(i).getText()); 
				    if (!tmp.equals("�닔�젙") && !tmp.equals("異붽�") && !tmp.equals("�궘�젣") && !tmp.equals("�꽑�깮")) {
					txtbox.click();
					Thread.sleep(200);
					txtbox.clear();
					Thread.sleep(100);
					txtbox.sendKeys(searchConditions.get(td.get(i).getText()));
					Thread.sleep(200);
				    }
				}
			    } else if (input_type.contains("combobox-f")) {
				try {
				    td.get(i + 1).findElement(By.tagName("select")).getAttribute("disabled").equals("disabled");
				} catch (Exception e3) {
				    WebElement comboArrow = td.get(i + 1).findElement(By.xpath(".//*[@class='combo-arrow']"));
				    comboArrow.click();
				    Thread.sleep(500);
				    java.util.List<WebElement> combo_p = driver.findElements(By.xpath("//div[@class='panel combo-p']"));
				    java.util.List<WebElement> combo_items = null;
				    for (int j = 0; j < combo_p.size(); j++) {
					if (combo_p.get(j).getAttribute("style").contains("block")) {
					    combo_items = combo_p.get(j).findElements(By.xpath(".//div/div"));
					    break;
					}
				    }
				    for (int j = 0; j < combo_items.size(); j ++) {
					if (combo_items.get(j).getText().equals(searchConditions.get(td.get(i).getText()))) {
					    combo_items.get(j).click();
					}
				    }				
				}
				Thread.sleep(500);
			    } else if (input_type.contains("textarea")) {
				try {
				    WebElement textarea = td.get(i + 1).findElement(By.xpath(".//textarea[@class='dlg_textbox2']"));
				    System.out.println("textarea text: " + searchConditions.get(td.get(i).getText().replaceAll("\\r\\n|\\r|\\n", " ")));
				    textarea.click();
				    Thread.sleep(200);
				    textarea.clear();
				    Thread.sleep(100);
				    textarea.sendKeys("textarea sendkey: " + searchConditions.get(td.get(i).getText().replaceAll("\\r\\n|\\r|\\n", " ")));
				    System.out.println("以묐났");
				    Thread.sleep(200);				
				} catch (Exception e4) {
				    System.out.println("以묐났");
				}
			    } else if (input_type.contains("radio")) {
				try {
				    java.util.List<WebElement> radio_btn = td.get(i + 1).findElements(By.xpath(".//*[@class='dlg_radio1']"));
				    String[] td_txt = td.get(i + 1).getText().split(" ");
				    for (int j = 0; j < td_txt.length; j++) {
					if (td_txt[j].contains(searchConditions.get(td.get(i).getText()))) {
					    radio_btn.get(j).click();
					    Thread.sleep(100);
					}
				    }
				} catch (Exception e3) {
				    System.out.println(e3);
				}
			    } else if (input_type.contains("checkbox")) {
				try {
				    td.get(i + 1).findElement(By.xpath(".//*[@type='checkbox']")).getAttribute("disabled").equals("disabled");
				} catch (Exception e3) {
				    java.util.List<WebElement> checkbox_btn = td.get(i + 1).findElements(By.xpath(".//*[@type='checkbox']"));
				    if (checkbox_btn.size() < 2) {
					if (!checkbox_btn.get(0).isSelected()) {
					    if (searchConditions.get(td.get(i).getText()).equals("check")) {
						System.out.println("checkbox click");
						checkbox_btn.get(0).click();
						Thread.sleep(100);
					    }
					} else {
					    if (searchConditions.get(td.get(i).getText()).equals("uncheck")) {
						checkbox_btn.get(0).click();
						Thread.sleep(100);
					    }						
					}
				    } else {
					System.out.println("Too many checkbox");
					return false;					    
				    }
				}
			    } else if (input_type.equals("numberspinner-f")) {
				try {
				    java.util.List<WebElement> list = td.get(i + 1).findElements(By.tagName("input"));
				    for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getAttribute("class").contains("numberspinner-f")) {
					    list.get(j).click();
					    Thread.sleep(200);
					    list.get(j).clear();
					    Thread.sleep(200);
					    list.get(j).sendKeys(searchConditions.get(td.get(i).getText()));
					    Thread.sleep(200);
					}
				    }
				} catch (Exception e3) {
				    System.out.println(e3);
				}
			    }
			} 
		    } 
		}
	    }
	    System.out.println("save");
	    
	    WebElement popup_btns = null;
	    popup_btns = tab_panels.findElement(By.xpath(".//*[@class='ipr_dialog_buttonbox']"));
	    WebElement cancel_btn = popup_btns.findElement(By.xpath(".//*[contains(text(), '痍⑥냼')]"));
	    WebElement save_btn = popup_btns.findElement(By.xpath(".//*[contains(text(), '���옣')]"));
	    save_btn.click();
	    Thread.sleep(500);
	    
	    if (!command_confirm()) {
	    	cancel_btn.click();
		return false;
	    }
	    return true;
	}
	
	public Map<String, WebElement> getContentsbyRow(java.util.List<WebElement> content) throws InterruptedException {
	    Map<String, WebElement> map = new HashMap<String, WebElement>();
	    try {
		for (int i = 0; i < content.size(); i++) {
		    while (!content.get(i).isDisplayed()) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", content.get(i));
		    }
		    String key = content.get(i).getText();
		    System.out.println("each cell: " + key);
		    WebElement value = content.get(i);
		    if (key.length() > 0) {
			map.put(key, value);
		    } else if (key.isEmpty()) {
			key = null;
			map.put(key, value);		    
		    }
		}
		return map;
	    } catch (Exception e) {
		return null;
	    }
	}
	
	public boolean set_combobox_f(WebElement comboarrow, String txt) {
	    try {
		comboarrow.click();
		Thread.sleep(500);
		java.util.List<WebElement> combo_p = driver.findElements(By.xpath("//div[@class='panel combo-p']"));
		java.util.List<WebElement> combo_items = new ArrayList();
		for (int j = 0; j < combo_p.size(); j++) {
		    if (combo_p.get(j).getAttribute("style").contains("block")) {
			combo_items = combo_p.get(j).findElements(By.xpath(".//div/div"));
			break;
		    }
		}
		for (int j = 0; j < combo_items.size(); j ++) {
		    System.out.println("combobox items: " + combo_items.get(j).getText());
		    if (combo_items.get(j).getText().equals(txt)) {
			combo_items.get(j).click();
			Thread.sleep(200);
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		return false;
	    }
	    return true;
	}
	
	public boolean setText(WebElement txtBox, String txt) {
	    try {
		System.out.println("Set Text: " + txt);
		txtBox.click();
		Thread.sleep(200);
		txtBox.clear();
		Thread.sleep(100);
		txtBox.sendKeys(txt);
		return true;
	    } catch (Exception e) {
		return false;		
	    }
	}

	public boolean setCalendar(WebElement cal, String date) {
	    try {
		String yyyy, mm, dd;
		String[] tmp = date.split("-");
		yyyy = tmp[0];
		mm = tmp[1];
		dd = tmp[2];
		char[] tmp1 = tmp[1].toCharArray();
		if (tmp1[0] == '0') mm = "" + tmp1[1];
		cal.findElement(By.xpath(".//div[@class='calendar-header']/div[@class='calendar-title']")).click();
		Thread.sleep(200);
		setText(cal.findElement(By.xpath(".//input[@class='calendar-menu-year']")), yyyy);
		cal.findElement(By.xpath(".//div[@class='calendar-menu-month-inner']/table/tbody/tr/td[@abbr='" + mm + "']")).click();
		Thread.sleep(200);
		cal.findElement(By.xpath(".//div[@class='calendar-body']/table/tbody/tr/td[@abbr='" + yyyy + "," + mm + "," + dd + "']")).click();
		Thread.sleep(200);
		return true;
	    } catch (Exception e) {
		return false;
	    }
	}

	public boolean selectRadioButton(java.util.List<WebElement> btns, String key) throws InterruptedException {
	    Thread.sleep(500);
	    try {
		for (int i = 0; i < btns.size(); i++) {
		    if (btns.get(i).getAttribute("value").equals(key)) {
			btns.get(i).click();
			Thread.sleep(500);
			break;
		    }
		}
	    } catch (Exception e) {
		System.out.println(e);
		//errReason.add("�씪�뵒�삤踰꾪듉 �꽑�깮 �떎�뙣");
		return false;
	    }
	    Thread.sleep(500);
	    return true;
	}
	
	public Map<String, WebElement> getSingleDepthSingleSelectDropDownMenu(WebElement base) throws InterruptedException {
	    Thread.sleep(500);
	    Map<String, WebElement> list = new HashMap<String, WebElement>();
	    java.util.List<WebElement> menuList = base.findElements(By.xpath(".//div"));
	    for (int i = 0; i < menuList.size(); i++) {
		list.put(menuList.get(i).getText(), menuList.get(i));
	    }
	    Thread.sleep(500);
	    return list;
	}
	
	public boolean selectSingleDepthSingleSelectDropDownMenu(Map<String, WebElement> list, String key) throws InterruptedException {
	    Thread.sleep(500);
	    try {
		if (list.containsKey(key)) {
		    list.get(key).click();
		    Thread.sleep(500);
		}
	    } catch (Exception e) {
		System.out.println(e);
		//errReason.add("�꽑�깮 �떎�뙣: "+ key);
		return false;
	    }
	    Thread.sleep(500);
	    return true;
	}
	
	public String getscreenshot() {
		if (screenshot != null && screenshot != "") {
			String tmp = screenshot;
			screenshot = null;
			return tmp;
		} else
			return null;
	}
	public void screenShot(String screenShotName) throws IOException{
		//String path = "S:/selenium_web_test/swat_web_test/QA/seleniumUAT/screenshot.png";
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String jpgFileName = "SCR_" + sdf.format(d);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		screenshot = "S:/selenium_web_test/swat_web_test/QA/seleniumUAT/"+jpgFileName+".jpg";
		FileUtils.copyFile(scrFile, new File(screenshot));
	}
}
