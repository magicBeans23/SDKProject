/**
 * 
 */
package sdk;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * @author shivam
 *
 */
public class CustomException extends WebDriverException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3109854245601393090L;

	/**
	 * 
	 */
	public CustomException() {
		// TODO Auto-generated constructor stub
	}
	public CustomException(SDKUtils sdk ) {
		super();
		takeScreenshot(sdk);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param cause the cause
	 * @param sdk the sdk
	 */
	public CustomException(Throwable cause,SDKUtils sdk) {
		super(cause);
		takeScreenshot(sdk);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param sdk the sdk
	 */
	public CustomException(String message, Throwable cause,SDKUtils sdk) {
		super(message, cause);
		takeScreenshot(sdk);
	}

	/**
	 * Take screenshot.
	 *
	 * @param sdk the sdk
	 */
	private void takeScreenshot (SDKUtils sdk)
	{
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyyHHmmssSSS");
		String dt=sdf.format(date);
		WebDriver driver=sdk.getDriver();
		File pic=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String fileName="ExpSS"+dt+".png";
		try {
			FileUtils.copyFile(pic, new File(fileName));
			System.out.println("screenshotsaved "+fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("could not save screenshot");
			e.printStackTrace();
		}
	}

}

