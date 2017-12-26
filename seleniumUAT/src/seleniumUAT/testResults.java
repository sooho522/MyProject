package seleniumUAT;

import java.util.Iterator;
import java.util.LinkedList;


public class testResults {
	//public LinkedList<str_tcResult> resultList = new LinkedList<str_tcResult>();
	public LinkedList resultList = new LinkedList();
	public boolean add(String testid, boolean result, String inputvalues, String scr_shot_file_path) {
		try {
			str_tcResult retVal = new str_tcResult();
			retVal.id = testid;
			retVal.result = result;
			retVal.inputValues = inputvalues;
			retVal.screenshot_file_path = scr_shot_file_path;
			resultList.add(retVal);
		} catch (Exception e) {
			return false;
		}
		//printResultsToConsole();
		return true;
	}
	
	public void printResultsToConsole() {
		str_tcResult retValue = new str_tcResult();
		Iterator<str_tcResult> iterator = resultList.iterator();
		while(iterator.hasNext()) {
			retValue = iterator.next();
			System.out.println (retValue.id + ":" + retValue.result + ":" + retValue.inputValues + ":" + retValue.screenshot_file_path);
		}
	}
}
