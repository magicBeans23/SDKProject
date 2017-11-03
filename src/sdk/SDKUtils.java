package sdk;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

// TODO: Auto-generated Javadoc
/**
 * The Class SDKUtils.
 * @author shivam
 */
public class SDKUtils {

	/** The properties. */
	private static Properties orProps=null;

	/** The config props. */
	private static Properties configProps=null;

	/** The driver. */
	protected WebDriver driver=null;

	/** The wait. */
	protected WebDriverWait wait=null;

	/** The default timeout. */
	protected static int defaultTimeout=40;

	/** The setup config path. */
	private static String setupConfigPath=new String();

	/** The screen shotcount. */
	private int screenShotcount=0;

	/** The sdk home handle. */
	String  sdkHomeHandle=new String();

	/**
	 * Instantiates a new SDK utils.
	 */
	SDKUtils(){

	}
	/**
	 * call this constructor when opening a nbew site along with the main portal.
	 * Not intended for GUI action on one/more pages like the ones embeded in object tag.
	 * true/false doesnot matter, argument only differentiates it from the default contructor
	 * SDK.class must have been initialized before, its not for standalone self.
	 *
	 * @param newSite the new site
	 */
	SDKUtils(boolean newSite)
	{
		FirefoxProfile fp=new FirefoxProfile();
		fp.setPreference("network.proxy.type", 0);
		driver=new FirefoxDriver();
		wait=new WebDriverWait(driver, defaultTimeout);	
	}

	SDKUtils(SDKUtils sup)
	{
		this.driver=sup.getDriver();
		this.wait=sup.wait;
		this.sdkHomeHandle=sup.sdkHomeHandle;
	}
	/**
	 * Instantiates a new SDK utils.
	 *
	 * @param driver the driver
	 */
	/*SDKUtils(WebDriver driver)
	{
		this.driver=driver;
		wait=new WebDriverWait(driver, defaultTimeout);
	}*/
	/**
	 * Instantiates a new SDK utils.
	 *
	 * @param setupConfigPath the setup config path
	 */
	SDKUtils(String setupConfigPath)
	{
		System.out.print("firefox >44 recommended");
		SDKUtils.setupConfigPath=setupConfigPath;
		init();
		FirefoxProfile fp=new FirefoxProfile();
		fp.setPreference("network.proxy.type", 0);
		driver=new FirefoxDriver(fp);
		wait=new WebDriverWait(driver, defaultTimeout);	
	}

	/**
	 * Instantiates a new SDK utils.
	 * 
	 * @param setupConfigPath the setup config path
	 * @param init the init : false only to load props. true : same as SDKUtils(String setupConfigPath)
	 */
	SDKUtils(String setupConfigPath,boolean init)
	{
		if(init)
		{
			System.out.print("firefox >44 recommended");
			SDKUtils.setupConfigPath=setupConfigPath;
			init();
			FirefoxProfile fp=new FirefoxProfile();
			fp.setPreference("network.proxy.type", 0);
			driver=new FirefoxDriver(fp);
			wait=new WebDriverWait(driver, defaultTimeout);
		}
		else
		{
			SDKUtils.setupConfigPath=setupConfigPath;
			init();
		}

	}

	/**
	 * Instantiates a new SDK utils.
	 * Only tested for firefox
	 * @param setupConfigPath the setup config path
	 * @param browser the browser
	 */
	SDKUtils(String setupConfigPath,Browser browser)
	{
		SDKUtils.setupConfigPath=setupConfigPath;
		init();
		if(browser.equals(Browser.Firefox))
		{
			FirefoxProfile fp=new FirefoxProfile();
			fp.setPreference("network.proxy.type", 0);
			driver=new FirefoxDriver(fp);
		}
		else if((browser.equals(Browser.Chrome)))
		{
			System.setProperty("webdriver.chrome.driver", "C:/res/chromedriver.exe");
			driver = new ChromeDriver();
		}
		else if(browser.equals(Browser.IE))
		{
			File file = new File("C:/res/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver=new InternetExplorerDriver(capabilities);
		}
		wait=new WebDriverWait(driver, defaultTimeout);
	}
	/**
	 * Inits the.
	 */
	public void init()
	{
		configProps=new Properties();
		orProps=new Properties();
		try {
			//configProps.load
			configProps.load(new FileInputStream(setupConfigPath));
			orProps.load(new FileInputStream(configProps.getProperty(Props.ORpath.name())));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inits the driver.
	 *
	 * @param driver the driver
	 */
	void initDriver(WebDriver driver)
	{
		this.driver=driver;
		wait=new WebDriverWait(driver, defaultTimeout);

	}
	/**
	 * Gets the config.
	 *
	 * @param key the key
	 * @return the config
	 */
	public String getConfig(Props key){
		return configProps.getProperty(key.name());
	}
	/**
	 * Sets the browser.
	 *
	 * @param browser the new browser
	 */
	public void setBrowser(Browser browser)
	{
		switch (browser){
		case Firefox : driver=new FirefoxDriver(); break;
		case Chrome : break;
		case IE : break;
		}
	}


	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * Gets the wait.
	 *
	 * @return the wait
	 */
	public WebDriverWait getWait()
	{
		return wait;
	}
	/**
	 * Gets the objects by.
	 *
	 * @param objName the obj name
	 * @return the objects by
	 */
	public By getObjectsBy(Props objName)
	{
		String [] arrP=getProperty(objName);
		return getObjectsBy(arrP[0],arrP[1]);
	}

	/**
	 * Gets the objects by.
	 *
	 * @param identifier the identifier
	 * @param value the value
	 * @return By.identifier(value)
	 */
	public By getObjectsBy(String identifier,String value)
	{
		switch (identifier){
		case "id" : return By.id(value);
		case "name" : return By.name(value);
		case "linkText" : return By.linkText(value);
		case "partialLinkText" : return By.partialLinkText(value);
		case "tagName" : return By.tagName(value);
		case "xpath" : return By.xpath(value);
		case "className" : return By.className(value);
		default : return null;
		}
	}

	/**
	 * Gets the property.
	 *
	 * @param propKey the prop key
	 * @return the property
	 */
	public String[] getProperty(Props propKey)
	{
		String val=orProps.getProperty(propKey.name());
		return val.split("(?<!\\\\)[*][*](?![*])");
	}

	/**
	 * Find element.
	 *
	 * @param ctlProp the ctl prop
	 * @return the web element
	 */
	public WebElement findElement(Props ctlProp)
	{
		return findElement(getObjectsBy(ctlProp));
	}

	/**
	 * Find element.
	 *
	 * @param by the by
	 * @return the web element
	 * @throws CustomTimeoutException the custom timeout exception
	 */
	public WebElement findElement(By by) throws CustomTimeoutException
	{
		try{
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			return driver.findElement(by);
		}catch(TimeoutException te)
		{
			throw new CustomTimeoutException(te,this);
		}
	}

	/**
	 * Find elements.
	 *
	 * @param ctlProp the ctl prop
	 * @return the list
	 */
	public List<WebElement> findElements(Props ctlProp)
	{
		return findElements(getObjectsBy(ctlProp));
	}

	/**
	 * Find elements.
	 *
	 * @param by the by
	 * @return the list
	 */
	public List<WebElement> findElements(By by)
	{
		try{
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
			return driver.findElements(by);
		}catch(Exception e)
		{
			throw new CustomException(e,this);
		}
	}
	/**
	 * Manage.
	 *
	 * @param command the command
	 */
	public void manage(String command)
	{
		switch (command){
		case ("back") : driver.navigate().back(); break;
		case ("forward") : driver.navigate().forward(); break;
		case ("maximize") : driver.manage().window().maximize(); break;
		}
	}

	/**
	 * Gets the page text.
	 *gets all text inside body tag
	 * @return the page text
	 */
	public String getPageText()
	{
		return getText(By.tagName("body"));
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public WebElement getBody()
	{
		return findElement(By.tagName("body"));
	}

	/**
	 * Input text.
	 *
	 * @param prop the prop
	 * @param text the text
	 * @param eraseExisting the erase existing
	 */
	public void inputText(Props prop,String text,boolean eraseExisting)
	{
		inputText(getObjectsBy(prop),text,eraseExisting);
	}
	/**
	 * Input text.
	 *
	 * @param by the By
	 * @param text the text
	 * @param eraseExisting true erase existing, false keep existing
	 */
	public void inputText(By by,String text,boolean eraseExisting)
	{
		WebElement we=findElement(by);
		if(eraseExisting)
		{
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String clearKeys=Keys.chord(Keys.CONTROL,"a");//,Keys.DELETE);
			we.sendKeys(clearKeys);
			we.sendKeys(Keys.DELETE);
		}
		we.sendKeys(text);
	}

	/**
	 * Gets the text.
	 *
	 * @param prop the prop
	 * @return the text
	 */
	public String getText(Props prop)
	{
		return getText(getObjectsBy(prop));
	}

	/**
	 * Gets the text.
	 *
	 * @param by the by
	 * @return the text
	 */
	public String getText(By by)
	{
		try{
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return findElement(by).getText().length() != 0;
				}
			});
		}catch(Exception e){
			System.out.println("waited for "+defaultTimeout+" for text to appear :"+e.toString());
		}
		return findElement(by).getText();
	}

	/**
	 * click.
	 *
	 * @param prop the prop
	 */
	public void click(Props prop)
	{
		click(getObjectsBy(prop));
	}

	/**
	 * Click.
	 *Added on 6/18/2016, for elements alredy searched.
	 *Used in searchattribute() in Configuration.java
	 * @param we the we
	 */
	public void click(WebElement we)
	{
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);",we);
		wait.until(ExpectedConditions.elementToBeClickable(we));
		we.click();
	}
	/**
	 * Click.
	 *
	 * @param by the by
	 */
	public void click(By by)
	{
		WebElement we=findElement(by);
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);",we);
		wait.until(ExpectedConditions.elementToBeClickable(by));
		we.click();
	}

	/**
	 * uses jQuery executor to click, in case click doesnt work, use this.
	 *
	 * @param prop the prop
	 */
	public void jClick(Props prop)
	{
		jClick(getObjectsBy(prop));
	}

	/**
	 * uses jQuery executor to click, in case click doesnt work, use this.
	 *
	 * @param by the by
	 */
	public void jClick(By by)
	{
		WebElement we=findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", we);
	}
	/**
	 * Operate check box.
	 *
	 * @param prop the prop
	 * @param checkState the desired state : true check , false uncheck
	 * @return current state of checkbox after operation
	 */
	public boolean operateCheckBox(Props prop,boolean checkState)
	{
		return operateCheckBox(getObjectsBy(prop),checkState);
	}

	/**
	 * Operate check box.
	 *
	 * @param by the by
	 * @param checkState the check state
	 * @return true, if successful
	 */
	public boolean operateCheckBox(By by,boolean checkState)
	{
		WebElement we=findElement(by);
		if(!(we.isSelected()==checkState))
			we.click();
		return we.isSelected();
	}
	/**
	 * Toggles the checkbox state.
	 *
	 * @param prop the prop
	 * @return current state of checkbox after operation
	 */
	public boolean operateCheckBox(Props prop)
	{
		WebElement we=findElement(prop);
		we.click();
		return we.isSelected();
	}

	/**
	 * Clickn type.
	 *
	 * @param by the by
	 * @param text the text
	 */
	public void clicknType(By by,String text)
	{
		wait.until(ExpectedConditions.elementToBeClickable(by));
		WebElement we=findElement(by);
		Actions action =new Actions(driver);
		action.doubleClick(we).sendKeys(text).perform();
	}

	/**
	 * Clickn type.
	 *
	 * @param by the by
	 * @param text the text
	 * @param clear the clear
	 * @throws InterruptedException the interrupted exception
	 */
	public void clicknType(By by,String text,boolean clear) throws InterruptedException
	{
		if(clear)
		{
			wait.until(ExpectedConditions.elementToBeClickable(by));
			WebElement we=findElement(by);
			String selectKeys=Keys.chord(Keys.CONTROL,"a");
			Actions action =new Actions(driver);
			action.doubleClick(we);
			Thread.sleep(500);
			we.sendKeys(selectKeys);
			Thread.sleep(500);
			we.sendKeys(Keys.BACK_SPACE);
			Thread.sleep(500);
			we.sendKeys(text);
		}
		else
		{
			clicknType(by,text);
		}
	}

	/**
	 * Clickn type.
	 *
	 * @param prop the prop
	 * @param text the text
	 * @param clear the clear
	 * @throws InterruptedException the interrupted exception
	 */
	public void clicknType(Props prop,String text,boolean clear) throws InterruptedException
	{
		clicknType(getObjectsBy(prop),text,clear);
	}
	/**
	 * Clickn type.
	 *
	 * @param prop the prop
	 * @param text the text
	 */
	public void clicknType(Props prop,String text)
	{
		clicknType(getObjectsBy(prop),text);
	}

	/**
	 * Check presence.
	 *
	 * @param prop the prop
	 * @return true, if successful
	 */
	public boolean checkPresence(Props prop)
	{
		return !(findElements(prop).isEmpty());

	}

	/**
	 * Select drop down option.
	 *
	 * @param prop the prop
	 * @param text the text
	 */
	public void selectDropDownOption(Props prop,String text)
	{
		selectDropDownOption(getObjectsBy(prop),text);
	}

	/**
	 * Select drop down option.
	 *
	 * @param by the by
	 * @param text the text
	 */
	public void selectDropDownOption(By by,String text)
	{

		WebElement we=findElement(by);
		//wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(we)));
		Select select=new Select(we);
		try{
			select.selectByVisibleText(text);	
		}catch(NoSuchElementException nse)
		{
			select.selectByValue(text);
		}
	}

	/**
	 * Gets the drop down options.
	 *
	 * @param prop the prop
	 * @return the drop down options
	 * @see SDKUtils.getDropDownOptions(By by)
	 */
	public List<String> getDropDownOptions(Props prop)
	{
		return getDropDownOptions(getObjectsBy(prop));
	}

	/**
	 * Gets the drop down options.
	 *gets the text displayed, not the value attribute
	 * @param by the by
	 * @return the drop down options
	 */
	public List<String> getDropDownOptions(By by)
	{
		WebElement we=findElement(by);
		List<WebElement> options=we.findElements(By.tagName("option"));
		List<String> optList=new ArrayList<String>();
		for(WebElement option: options)
		{
			optList.add(option.getText());
		}
		return optList;
	}

	/**
	 * Gets the dropdown selected option.
	 *
	 * @param prop the prop
	 * @return the dropdown selected option
	 */
	public String getDropdownSelectedOption(Props prop)
	{
		WebElement we=findElement(prop);
		Select select=new Select(we);
		return select.getFirstSelectedOption().getText();
	}
	/**
	 * Write tofile.
	 *writes in UTF-8 format
	 * @param toWrite the to write
	 * @param filePath the file path
	 */
	public void writeTofile(String toWrite, String filePath)
	{
		PrintWriter writer;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
			writer.println(toWrite);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Close all opened browser windows
	 * Quit.
	 */
	public void quit()
	{
		driver.quit();
	}

	/**
	 * opens new window and switch focus to it.
	 */
	public void openNewWindow()
	{
		Set<String> lstHandles=driver.getWindowHandles();
		int handleCount=lstHandles.size();
		Set<String> newHandles=null;
		int newHandleCount=0;
		while(newHandleCount<=handleCount){
			driver.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.CONTROL,"n"));
			newHandles=driver.getWindowHandles();
			newHandleCount=newHandles.size();
		}

		newHandles.removeAll(lstHandles);
		String newHandle=(String) newHandles.toArray()[0];
		driver.switchTo().window(newHandle);
	}

	/**
	 * Goto object.
	 * for pages that have embebed HTML pages within themselves in object html tag,
	 *This method finds the ulr mentioned in the object tag goes to that url and gives driver control of that page.
	 * @param windowTitle the window title
	 * @param subUrl the sub url
	 */
	void gotoObject(String windowTitle,String subUrl)
	{
		boolean found=false;
		Set<String> lstHandles=driver.getWindowHandles();
		for(String handle :lstHandles){
			if(driver.switchTo().window(handle).getTitle().toLowerCase().equals(windowTitle.toLowerCase()))
			{
				found=true;
				break;
			}
		}
		if(!found)
		{
			openNewWindow();
			driver.get(subUrl);
		}
	}

	/**
	 * Gets the current time stamp.
	 *
	 * @return the current time stamp in yyyyMMddHHmmss_SSS
	 */
	public static String getCurrentTimeStamp()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss_SSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Gets the current time stamp.
	 *
	 * @param format the format
	 * @return the current time stamp in given format
	 */
	public static String getCurrentTimeStamp(String format)
	{
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Reads files with format SET [key] [value]
	 * leaves empty lines,
	 * leaves lines starting #,
	 * format : SET key value.
	 *
	 * @param file the file
	 * @return the hash map key value pairs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String,String> loadConfigValues(File file) throws IOException
	{
		Scanner sc = new Scanner(file);
		Map<String,String> hm=loadConfigValues(sc);
		sc.close();
		return hm;

	}

	/**
	 * Reads String with format SET [key] [value]
	 * leaves empty lines,
	 * leaves lines starting #,
	 * format : SET key value.
	 * 
	 *txt must be of format SET key value
	 * @param txt the txt
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String,String> loadConfigValues(String txt) throws IOException
	{
		Scanner sc = new Scanner(txt);
		Map<String,String> hm=loadConfigValues(sc);
		sc.close();
		return hm;
	}

	/**
	 * Load config values,.
	 *
	 * @param sc the sc
	 * @return the map HashMap<String,String>
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Map<String,String> loadConfigValues(Scanner sc) throws IOException
	{
		Map<String,String> hm=new HashMap<String,String>();
		while (sc.hasNextLine()) {
			String line=sc.nextLine();
			if(line.length() == 0 || line.startsWith("#")){

			}
			else
			{
				int begin=line.indexOf(" ", line.indexOf("SET"))+1;
				line=line.substring( begin, line.length());
				int div=line.indexOf(" ");
				String key=line.substring(0, div);
				String val=line.substring(div+1);
				if(!val.isEmpty())
				{//34 is the unicode for "
					if(val.charAt(0)==34 && val.charAt(val.length()-1)==34)
					{
						char[] arrChar=val.toCharArray();
						arrChar[0]=32;
						arrChar[arrChar.length-1]=32;
						val=new String(arrChar).trim();
					}
				}
				hm.put(key, val);
			}

		}
		sc.close();
		return hm;
	}

	/**
	 * Switch back from frame to default content of the page.
	 */
	public void switchBackfromFrame()
	{
		driver.switchTo().defaultContent();
	}

	/**
	 * File upload.
	 * if its a input type='file' try sendKeys directly than using this.
	 * @param filePath the file path
	 * @throws AWTException the AWT exception
	 */
	void fileUpload(String filePath) throws AWTException
	{
		StringSelection stringSelection = new StringSelection(filePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot= new Robot();
		//robot.keyPress(KeyEvent.VK_F6);
		//robot.keyPress(KeyEvent.VK_F7);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	/**
	 * Gets the attribute.
	 *
	 * @param prop the prop
	 * @param attrName the attr name
	 * @return the attribute
	 */
	public String getAttribute(Props prop,String attrName)
	{
		return findElement(prop).getAttribute(attrName);
	}

	/**
	 * Gets the attribute.
	 *
	 * @param by the by
	 * @param attrName the attr name
	 * @return the attribute
	 */
	public String getAttribute(By by,String attrName)
	{
		return findElement(by).getAttribute(attrName);
	}

	/**
	 * Take screen shot.
	 */
	public void takeScreenShot()
	{
		File pic=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String fileName="screenshot"+(++screenShotcount)+".png";
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
