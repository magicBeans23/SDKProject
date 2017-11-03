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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomTimeoutException.
 * Purspose : to take a screenshot in case of timeout exception.
 * Why timeout exception ? coz we always wait for an element before doning anything, so this is what we get everytime.
 *
 * @author shivam
 */
public class CustomTimeoutException  extends TimeoutException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5817116669685549570L;

	/**
	 * Instantiates a new custom timeout exception.
	 */
	public CustomTimeoutException() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param message the message
	 */
	public CustomTimeoutException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param cause the cause
	 */
	public CustomTimeoutException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public CustomTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param sdk the sdk
	 */
	public CustomTimeoutException(SDKUtils sdk ) {
		super();
		takeScreenshot(sdk);
	}

	/**
	 * Instantiates a new custom timeout exception.
	 *
	 * @param cause the cause
	 * @param sdk the sdk
	 */
	public CustomTimeoutException(Throwable cause,SDKUtils sdk) {
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
	public CustomTimeoutException(String message, Throwable cause,SDKUtils sdk) {
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
