package seleniumUAT;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

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

public class xlsReport {
	public void createXLS(LinkedList results) throws FileNotFoundException {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		String xlsFileName = "UST-" + sdf.format(d) + ".xls";
		
		//FileOutputStream fileOut = new FileOutputStream(xlsFileName);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet("Unmanned SWAT Test Report");
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hlink_style = workbook.createCellStyle();
        Font hlink_font = workbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);

		String[] titles = {
				"Test No.", "Result", "Report", "screen shot"
		};

		//title
		Row titleRow = worksheet.createRow(0);
		titleRow.setHeightInPoints(45);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(xlsFileName);
		worksheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$C$1"));
		
		//header
		Row headerRow = worksheet.createRow(1);
		headerRow.setHeightInPoints(40);
		Cell headerCell;
		for (int i = 0; i < titles.length; i++) {
			headerCell = headerRow.createCell(i);
			headerCell.setCellValue(titles[i]);;
		}
		
		int rownum = 2;
		str_tcResult retValue = new str_tcResult();
		Iterator<str_tcResult> iterator = results.iterator();
		while(iterator.hasNext()) {
			retValue = iterator.next();
			Row row = worksheet.createRow(rownum++);
			Cell cell = row.createCell(0);
			cell.setCellValue(retValue.id);
			cell = row.createCell(1);
			if (retValue.result) {
				cell.setCellValue("Pass");
			} else {
				cell.setCellValue("Fail");				
			}
			cell = row.createCell(2);
			cell.setCellValue(retValue.inputValues);
			worksheet.setColumnWidth(2, 120*256);
			cell = row.createCell(3);
			if (retValue.screenshot_file_path != "" && retValue.screenshot_file_path != null) {
				HSSFHyperlink file_link=new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
				file_link.setAddress(retValue.screenshot_file_path);
				cell.setCellValue("image");
				cell.setHyperlink(file_link);
				/*
				cell.setCellValue("image");
				Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
				link.setAddress(retValue.screenshot_file_path);
				cell.setHyperlink(link);
				cell.setCellStyle(hlink_style);*/
			} else {
				cell.setCellValue("-");
			}
			System.out.println (retValue.id + ":" + retValue.result + ":" + retValue.inputValues);
		}
		
		try (FileOutputStream outputStream = new FileOutputStream (xlsFileName)) {
			workbook.write(outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
