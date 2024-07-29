package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static Properties x;
	public static WebDriver driver;

	// for browser and url we use property file
	public static WebDriver startBrowser() throws Throwable {
		x = new Properties();
		// load the file
		x.load(new FileInputStream("./PropertyFile/Environment.properties"));
		if (x.getProperty("Browser").equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		} else if (x.getProperty("Browser").equalsIgnoreCase("FireFox")) {
			driver = new FirefoxDriver();

		} else {
			Reporter.log("Browser key value is not matched", true);
		}
		return driver;
	}

	// method for launching url
	public static void openUrl() {
		driver.get(x.getProperty("url"));
	}

	/*
	 * in excel test data wait we gave 10 which is integer so we have to convert to
	 * string using integer parseint
	 */
	// method for webelement to wait
	public static void waitForElement(String LocatorName, String LocatorValue, String TestData) {
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if (LocatorName.equalsIgnoreCase("xpath")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if (LocatorName.equalsIgnoreCase("id")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
		if (LocatorName.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
	}

	// method for type action
	public static void typeAction(String LocatorName, String LocatorValue, String TestData) {
		if (LocatorName.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if (LocatorName.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if (LocatorName.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}

	}

	// method for click action
	public static void clickAction(String LocatorName, String LocatorValue) {
		if (LocatorName.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if (LocatorName.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).click();
		}
		if (LocatorName.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue))
			.sendKeys(Keys.ENTER); /*
			 * WHETHER THE ELEMENT VISBIBLE OR NOT HAVE TO CLICK WHILE USING ID
			 */
		}
	}

	// method for pagetitle validation
	public static void validateTitle(String ExpectedTitle) {
		String ActualTitle = driver.getTitle();
		try {
			Assert.assertEquals(ActualTitle, ExpectedTitle, "Title is not matching");
			/*
			 * while using asserts we will get lot of expections to handle we are using try
			 * catch
			 */
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	// method for close browser
	public static void closeBrowser() {
		driver.quit();
	}

	// method for date generation
	public static String dateGeneration() {
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd_hh_mm_ss");
		return df.format(d);

	}

	// method for mouse click
	public static void mouseClick() throws Throwable {
		Actions ac = new Actions(driver);
		ac.moveToElement(driver.findElement(By.xpath("//a[text()='Stock Items ']"))).perform();
		Thread.sleep(3000);
		ac.moveToElement(driver.findElement(By.xpath("(//a[contains(.,'Stock Categories')])[2]"))).click().perform();
		Thread.sleep(3000);
	}

	// method category table
	public static void categoryTable(String Expected) throws Throwable {
		// if search box is not displayed click search panel
		if (!driver.findElement(By.xpath(x.getProperty("search-textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(x.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).sendKeys(Expected);
		driver.findElement(By.xpath(x.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String actual_data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[4]/div/span/span")).getText();
		Reporter.log(Expected + "  " + actual_data, true);
		try {
			Assert.assertEquals(actual_data, Expected, "Category name is not matching");
		} catch (Exception e) {
			Reporter.log(e.getMessage(), true);
		}
	}

	// method for listbox
	public static void dropDownAction(String Lname, String Lvalue, String Test_data) {
		if (Lname.equalsIgnoreCase("xpath")) {
			int value = Integer.parseInt(Test_data);
			Select element = new Select(driver.findElement(By.xpath(Lvalue)));
			element.selectByIndex(value);
		}
		if (Lname.equalsIgnoreCase("name")) {
			int value = Integer.parseInt(Test_data);
			Select element = new Select(driver.findElement(By.name(Lvalue)));
			element.selectByIndex(value);
		}
		if (Lname.equalsIgnoreCase("id")) {
			int value = Integer.parseInt(Test_data);
			Select element = new Select(driver.findElement(By.id(Lvalue)));
			element.selectByIndex(value);
		}
	}

	// method for capture stock number into notepad
	public static void captureStock(String Lname, String Lvalue) throws Throwable {
		String stocknumber="";
		if (Lname.equalsIgnoreCase("xpath")) {
			stocknumber = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
		}
		if (Lname.equalsIgnoreCase("name")) {
			stocknumber = driver.findElement(By.name(Lvalue)).getAttribute("value");
		}
		if (Lname.equalsIgnoreCase("id")) {
			stocknumber = driver.findElement(By.id(Lvalue)).getAttribute("value");
		}
		// create notepad and stock number
		FileWriter fw = new FileWriter("./Capture data/stocknumber.txt");// file writer is to create physical file
		BufferedWriter bw = new BufferedWriter(fw);// buffered writer to allocate memory
		bw.write(stocknumber);
		bw.flush();
		bw.close();
	}
	//method for stock table
	public static void stockTable() throws Throwable {
		//read stock number from notepad
		FileReader fr=new FileReader("./Capture data/stocknumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String exp_data=br.readLine();
		// if search box is not displayed click search panel
		if (!driver.findElement(By.xpath(x.getProperty("search-textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(x.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).sendKeys(exp_data);
		driver.findElement(By.xpath(x.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String act_data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(act_data+"  "+exp_data,true);
		try {
			Assert.assertEquals(exp_data, act_data,"stock number is not matching");
		}
		catch(AssertionError e) {
			Reporter.log(e.getMessage(),true);
		}

	}
	//method for capture supplier number into notepad
	public static void captureSupplier(String Lname, String Lvalue) throws Throwable {
		String suppliernumber="";
		if (Lname.equalsIgnoreCase("xpath")) {
			suppliernumber = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
		}
		if (Lname.equalsIgnoreCase("name")) {
			suppliernumber = driver.findElement(By.name(Lvalue)).getAttribute("value");
		}
		if (Lname.equalsIgnoreCase("id")) {
			suppliernumber = driver.findElement(By.id(Lvalue)).getAttribute("value");
		}
		// create notepad and stock number
		FileWriter fw = new FileWriter("./Capture data/suppliernumber.txt");// file writer is to create physical file
		BufferedWriter bw = new BufferedWriter(fw);// buffered writer to allocate memory
		bw.write(suppliernumber);
		bw.flush();
		bw.close();
	}
	//method for supplier table
	public static void supplierTable() throws Throwable {
		//read supplier number from notepad
		FileReader fr=new FileReader("./Capture data/suppliernumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String exp_data=br.readLine();
		// if search box is not displayed click search panel
		if (!driver.findElement(By.xpath(x.getProperty("search-textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(x.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(x.getProperty("search-textbox"))).sendKeys(exp_data);
		driver.findElement(By.xpath(x.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String act_data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(act_data+"  "+exp_data,true);
		try {
			Assert.assertEquals(exp_data, act_data,"supplier number is not matching");
		}
		catch(AssertionError e) {
			Reporter.log(e.getMessage(),true);
		}

	}
	
	//method for capture customer number into notepad
		public static void captureCustomer(String Lname, String Lvalue) throws Throwable {
			String customernumber="";
			if (Lname.equalsIgnoreCase("xpath")) {
				customernumber = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
			}
			if (Lname.equalsIgnoreCase("name")) {
				customernumber = driver.findElement(By.name(Lvalue)).getAttribute("value");
			}
			if (Lname.equalsIgnoreCase("id")) {
				customernumber = driver.findElement(By.id(Lvalue)).getAttribute("value");
			}
			// create notepad and customernumber
			FileWriter fw = new FileWriter("./Capture data/customernumber.txt");// file writer is to create physical file
			BufferedWriter bw = new BufferedWriter(fw);// buffered writer to allocate memory
			bw.write(customernumber);
			bw.flush();
			bw.close();
		}
		//method for customer table
		public static void customerTable() throws Throwable {
			//read stock number from notepad
			FileReader fr=new FileReader("./Capture data/customernumber.txt");
			BufferedReader br=new BufferedReader(fr);
			String exp_data=br.readLine();
			// if search box is not displayed click search panel
			if (!driver.findElement(By.xpath(x.getProperty("search-textbox"))).isDisplayed())
				// click search panel button
				driver.findElement(By.xpath(x.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(x.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(x.getProperty("search-textbox"))).sendKeys(exp_data);
			driver.findElement(By.xpath(x.getProperty("search-button"))).click();
			Thread.sleep(3000);
			String act_data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span")).getText();
			Reporter.log(act_data+"  "+exp_data,true);
			try {
				Assert.assertEquals(exp_data, act_data,"customer number is not matching");
			}
			catch(AssertionError e) {
				Reporter.log(e.getMessage(),true);
			}
		}
}



