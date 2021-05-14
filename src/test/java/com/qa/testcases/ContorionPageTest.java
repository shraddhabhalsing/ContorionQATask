package com.qa.testcases;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qa.base.Base;
import com.qa.page.ContorionPage;
import com.qa.util.CommonUtil;

public class ContorionPageTest extends Base {

	//Create Object of MainPage where OR and methods are maintained
	ContorionPage mainPageObj=new ContorionPage();
	public static ExtentHtmlReporter pathHtml;
	public static ExtentReports exReport;
	public static ExtentTest exLog,exLog1,exLog2,exLog3,exLog4,exLog5,exLog6,exLog7,exLog8;

	//Prerequisite steps to be executed prior executing actual TestCases

	@BeforeTest
	public void basicSetUp()
	{
		DriversetUp();
		pathHtml=new ExtentHtmlReporter(System.getProperty("user.dir")+prop.getProperty("ReportPath"));
		exReport=new ExtentReports();
		exReport.attachReporter(pathHtml);

	}

	// Testcase to Launch URL 

	@Test(priority=1)
	public void launchURL(){
		String pageTitle=LaunchBrowser();
		mainPageObj.initializeWebElement();
		exLog=exReport.createTest("Verify user can launch URL", "Automation");
		if (pageTitle.equals("Contorion: Der smarte Shop fürs Handwerk"))
		{
			exLog.log(Status.PASS,"URL is launched successfully") ;
		}

		else

		{
			exLog.log(Status.FAIL,"Failed to launch URL") ;
		}
	}



	@DataProvider
	public Object[][] passSheet()
	{
		Object[][] val=CommonUtil.readDataFromExcel("InputData");
		return val;
	}


	// TestCase to verify user is able to Log into application

	@Test(priority=2,dataProvider="passSheet")
	@Parameters({"userName","password"})
	public void loginIntoContorionPortal(String userName ,String password )
	{
		Boolean verifyUserLoggedIn=mainPageObj.login(userName, password);
		exLog1=exReport.createTest("Verify user logged in app", "Automation");
		if (verifyUserLoggedIn)
		{
			exLog1.log(Status.PASS,"User logged in successfully") ;
		}
		else
		{
			exLog1.log(Status.FAIL,"Failed to logged in ") ;
		}

	}

	//Test Case to search for “Hammer" and select the product from list 

	@Test(priority=3)
	@Parameters({"productName","expectedProduct"})
	public void selectFirstProduct(String productName ,int expectedProduct )
	{
		Boolean vSearchProductSelected=mainPageObj.enterAndSearchProductName(productName, expectedProduct);
		exLog2=exReport.createTest("Verify user is able search and select product", "Automation");
		if (vSearchProductSelected)
		{
			exLog2.log(Status.PASS,"User successfully searched and selected the first product") ;
		}
		else
		{
			exLog2.log(Status.FAIL,"Failed to search and select the first product") ;
		}
	}

	// TestCase to add product to basket

	@Test(priority=4)
	@Parameters({"weight"})
	public void addProductToCart(String weight)
	{
		Boolean vProductAddedInCart= mainPageObj.addToCart(weight);
		exLog3=exReport.createTest("Add product to cart", "Automation");
		if (vProductAddedInCart)
		{
			exLog3.log(Status.PASS,"User successfully added product to cart") ;
		}
		else
		{
			exLog3.log(Status.FAIL,"Failed to add product in cart ") ;
		}
	}


	// Testcase to validate added items are visible in cart

	@Test(priority=5)
	@Parameters({"expectedProduct"})
	public void verifyItemsInCart(int expectedProduct )
	{
		Boolean verifyCarList=mainPageObj.verifyCartItemAndCheckOutProduct(expectedProduct);
		exLog6=exReport.createTest("Validate added items are visible in cart", "Automation");
		if (verifyCarList)
		{
			exLog6.log(Status.PASS,"Added items are visible in cart") ;
		}
		else
		{
			exLog6.log(Status.FAIL,"Failed to view items in cart") ;
		}
	}

	//TestCase to enter billing address and select payment method

	@Test(priority=6)
	@Parameters({"firstName","lastName","address","postal","city","paymentMethod"})

	public void enterBillingAddressToProceedForPayment(String firstName,String lastName,String address,String postal,String city,String paymentMethod )

	{ Boolean vAbleTosaveProductForLater=mainPageObj.enterBillingAddressAndSelectPaymentMethod(firstName,lastName,address,postal,city,paymentMethod);
	exLog7=exReport.createTest("Validate user is able to add billing address and select payment method", "Automation");
	if (vAbleTosaveProductForLater)
	{
		exLog7.log(Status.PASS,"User is able to add billing address and select payment method") ;
	}
	else
	{
		exLog7.log(Status.FAIL,"Failed to add billing address and select payment method") ;
	}
	}

	//Close browser

	@AfterTest
	public void closeBrowser() {
		exReport.flush();
		driver.close();
	}
}
