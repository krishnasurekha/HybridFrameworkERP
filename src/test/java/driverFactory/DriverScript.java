package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
	String inputpath = "./FileInput/DataEngine.xlsx";
	String outputpath = "./FileOutput/HybridResults.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String TestCases = "MasterTestCases";

	public void startTest() throws Throwable {
		String Module_Status;// for validation purpose
		// create object for ExcelFileUtil class to access the methods
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		// iterate all the test cases in TestCases
		for (int i = 1; i <= xl.rowCount(TestCases); i++) {
			if (xl.getCellData(TestCases, i, 2).equalsIgnoreCase("Y")) {
				// read corresponding sheet or TestCases
				String TCModule = xl.getCellData(TestCases, i, 1);
				//define the path of html
				report=new ExtentReports("./target/ExtentReports/"+TCModule+FunctionLibrary.dateGeneration()+".html");
				logger=report.startTest(TCModule);
				logger.assignAuthor("Surekha");
				// iterate all the rows in TCModule sheet
				for (int j = 1; j <= xl.rowCount(TCModule); j++) {
					// read all the cell from TCModule
					String Description = xl.getCellData(TCModule, j, 0);
					String ObjectType = xl.getCellData(TCModule, j, 1);
					String Lname = xl.getCellData(TCModule, j, 2);
					String Lvalue = xl.getCellData(TCModule, j, 3);
					String Test_data = xl.getCellData(TCModule, j, 4);
					try {
						if (ObjectType.equalsIgnoreCase("startBrowser")) {
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("openUrl")) {
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("waitForElement")) {
							FunctionLibrary.waitForElement(Lname, Lvalue, Test_data);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(Lname, Lvalue, Test_data);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(Lname, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("validateTitle")) {
							FunctionLibrary.validateTitle(Test_data);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("mouseClick")) {
							FunctionLibrary.mouseClick();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("categoryTable")) {
							FunctionLibrary.categoryTable(Test_data);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(Lname,Lvalue,Test_data);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captureStock")) {
							FunctionLibrary.captureStock(Lname,Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captureSupplier")) {
							FunctionLibrary.captureSupplier(Lname,Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("supplierTable")) {
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captureCustomer")) {
							FunctionLibrary.captureCustomer(Lname,Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("customerTable")) {
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						
						// write as pass into TCModule in status cell
						xl.setCellData(TCModule, j, 5, "pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_Status = "True";

					} catch (Exception e) {
						System.out.println(e.getMessage());
						// write as fail into TCModule in status cell
						xl.setCellData(TCModule, j, 5, "fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_Status = "False";
					}
					if (Module_Status.equalsIgnoreCase("True")) {
						// write a pass into TestCases Sheet
						xl.setCellData(TestCases, i, 3, "pass", outputpath);
					} else {
						// write a fail into TestCases Sheet
						xl.setCellData(TestCases, i, 3, "fail", outputpath);

					}
					report.endTest(logger);
					report.flush();
				}
			} else {
				// write as blocked for test cases flag to N in TestCases Sheet
				xl.setCellData(TestCases, i, 3, "Blocked", outputpath);
			}
		}

	}

}
