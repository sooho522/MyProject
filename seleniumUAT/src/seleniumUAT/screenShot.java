package seleniumUAT;

import java.io.File;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class screenShot extends CafeTestSetup {
	public void setScreenShot(String screenShotName)throws IOException{
	String path = "S:/selenium_web_test/swat_web_test/QA/seleniumUAT/screenshot.png";
	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	FileUtils.copyFile(scrFile, new File("S:/selenium_web_test/swat_web_test/QA/seleniumUAT/"+screenShotName+".jpg"));
	
	}
}
