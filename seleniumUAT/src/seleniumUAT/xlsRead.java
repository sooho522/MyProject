package seleniumUAT;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class xlsRead {
	
	private static List sheetlist = new ArrayList(); 
	//private static List contentstypelist = new ArrayList();
	public static Map<String, String> xlslist = new LinkedHashMap<String, String>();
	public int rowcnt =0;
	public int columncnt =0;
	public int contentsColumncnt =0;
	public static List cnt = new ArrayList();
	
	public void getXlsContents(String tcSheetName) {
		try{
			File src = new File("S:/selenium_web_test/swat_web_test/QA/seleniumUAT/testdata.xls");
			FileInputStream files = new FileInputStream(src);
			HSSFWorkbook wb = new HSSFWorkbook(files);		
			HSSFSheet sheet = wb.getSheet(tcSheetName);
			int sheetcnt = wb.getNumberOfSheets();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
			for (int i = 0 ; i < sheetcnt ; i++) {
				String sheetName = wb.getSheetName(i);
				sheetlist.add(sheetName);
			}
			
			//String contentsTypeValue = sheet.getRow(1).getCell(2).getStringCellValue();
			contentsColumncnt = sheet.getRow(1).getLastCellNum();
			//System.out.println("XLS data Type =" + contentsTypeValue);
			cnt.clear();
			for (int i=2 ; i < contentsColumncnt ; i++) {
				String contentstypelist = (sheet.getRow(1).getCell(i).getStringCellValue());
				System.out.println("[XLS] data Type list = " + contentstypelist.toString());
				//System.out.println("contens type 1 = " + contentsXlsType);
				//if (contentsXlsType.contains(contentstypelist) || contentsXlsType.size() == 0){
				if (contentstypelist != null) {
					
					rowcnt = sheet.getLastRowNum()+1;
					cnt.add(i-1);
					//System.out.println("[XLS] columnsize 1 : " +cnt);
					//columncnt = sheet.getRow(2).getLastCellNum();
					System.out.println("[XLS] rowcnt : "+ rowcnt);
					//System.out.println("column cnt : " + columncnt);
					//System.out.println("cnt 1 : " +cnt.toString());
					for (int j=2 ; j < rowcnt ; j++){
	
						String keylist = sheet.getRow(j).getCell(1).getStringCellValue();
						keylist = keylist;
						System.out.println("[XLS] key  : " + keylist);
						String valuelist = sheet.getRow(j).getCell(i).getStringCellValue();
						System.out.println("[XLS] value :  " + valuelist);
						xlslist.put(keylist+(i-1) , valuelist);
					}
				} else {
					System.out.println("[XLS] data 가 존재하지 않습니다.");
				}
				System.out.println("[XLS] xlslist : " +xlslist);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public void boundaryXlsValue (String tcSheetName){
		try{
			File src = new File("S:/selenium_web_test/swat_web_test/QA/seleniumUAT/testdata.xls");
			FileInputStream files = new FileInputStream(src);
			HSSFWorkbook wb = new HSSFWorkbook(files);		
			HSSFSheet sheet = wb.getSheet(tcSheetName);
			int sheetcnt = wb.getNumberOfSheets();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
			for (int i = 0 ; i < sheetcnt ; i++) {
				String sheetName = wb.getSheetName(i);
				sheetlist.add(sheetName);
			}	
			rowcnt = sheet.getLastRowNum()+1;
			columncnt = sheet.getRow(2).getLastCellNum();
			System.out.println("rowcnt : "+ rowcnt);
			System.out.println("column cnt : " + columncnt);
			for (int j=2 ; j < rowcnt ; j++){
				String keylist = sheet.getRow(j).getCell(1).getStringCellValue();
				keylist = keylist;
				System.out.println("key  : " + keylist);
				
				for (int k=2 ; k < columncnt ; k++){
					try {
						String valuelist = sheet.getRow(j).getCell(k).getStringCellValue();
						System.out.println("value :  " + valuelist);
						xlslist.put(keylist+(k-1) , valuelist);
					}catch (Exception e){
						Date datelist = sheet.getRow(j).getCell(k).getDateCellValue();
						String valuelist = sdf.format(datelist);
					}
				}
			}
				System.out.println("xlslist : " +xlslist);
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
