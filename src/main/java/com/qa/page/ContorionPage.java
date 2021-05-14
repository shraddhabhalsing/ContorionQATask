package com.qa.page;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.qa.base.Base;

public class ContorionPage extends Base {

	String expProduktInList;
	Boolean verifyWebElement=null;

	@FindBy(xpath="//button[@title='Alles akzeptieren']")
	WebElement cookieBtn;

	@FindBy(xpath="//div[@class='container header__container ']/div[4]/a")
	WebElement anmeldenBtn;

	@FindBy(xpath="//input[@id='login_email']")
	WebElement emailAdresseTextField;

	@FindBy(xpath="//input[@id='login_password']")
	WebElement passwordTextField;

	@FindBy(xpath="//span[contains(text(),'Jetzt anmelden')]")
	WebElement jetztAnmeldenBtn;

	@FindBy(xpath="//input[@class='js-searchbar-input reset-validation']")
	WebElement suchenProducktnameTextField;

	@FindBys(@FindBy(xpath="//div[@class='js-product-loading-indicator product-widgets flex flex--wrap active layout-icon']//a[@class='js-url a-link--dark _fit']"))
	List<WebElement>producktLst;

	@FindBy(xpath="//select[@name='weight_g']")
	WebElement gewichtSelect;

	@FindBy(xpath="//button[@class='button--buy _fit _mt2 button button--primary']")
	WebElement aufdenWarenstaplerBtn;

	@FindBy(xpath="//a[@class='button button--primary']")
	WebElement zumWarenstaplerBtn;

	@FindBys(@FindBy(xpath="//div[@class='flex flex--wrap flex--between _p3 cart-product-item relative']//div[@class='item--fit']/a[@class='link _mb1 _mr3 _pr3 lg_pr0']"))
	List<WebElement> cartProduktverification;

	@FindBy(xpath="//div[@class='flex flex--wrap _fit lg_c-auto flex--end']//span[contains(text(),'Zur Kasse')]")
	WebElement zurKasseBtn;

	@FindBy(xpath="//input[@id='order_billingAddress_firstName']")
	WebElement vornameTextField;

	@FindBy(xpath="//input[@id='order_billingAddress_lastName']")
	WebElement nachnameTextField;

	@FindBy(xpath="//input[@id='order_billingAddress_address1']")
	WebElement addressTextField;

	@FindBy(xpath="//input[@id='order_billingAddress_zipCode']")
	WebElement postleitZahlTextField;

	@FindBy(xpath="//input[@id='order_billingAddress_city']")
	WebElement stadtTextField;

	@FindBys(@FindBy(xpath="//div[@class='co-payment__methods form__field']//div[@class=' form__field form__field--radio']/label"))
	List<WebElement> ZahlungsmethodeText;

	//Initialize WebElements mentioned in the current page
	public void initializeWebElement(){
		PageFactory.initElements(driver, this);
	}

	// Login Method
	public Boolean login(String userName, String password)
	{ 

		try
		{
			cookieBtn.click();
			WebDriverWait w=new WebDriverWait(driver, 40);
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='container header__container ']/div[4]/a")));
			anmeldenBtn.click();
			emailAdresseTextField.sendKeys(userName);
			passwordTextField.sendKeys(password);
			jetztAnmeldenBtn.click();
			if (suchenProducktnameTextField.isDisplayed())
				verifyWebElement=true;
			else
				verifyWebElement=false;
		}catch(Exception e)
		{

			System.out.println(e);
		}
		return verifyWebElement;
	}

	//Method to search and select products
	public Boolean enterAndSearchProductName(String productName,int expProduct)
	{  
		suchenProducktnameTextField.sendKeys(productName);
		suchenProducktnameTextField.sendKeys(Keys.ENTER);
		WebElement actProcukt=producktLst.get(expProduct);
		actProcukt.click();
		if (gewichtSelect.isDisplayed())
			verifyWebElement=true;
		else
			verifyWebElement=false;
		return verifyWebElement;
	}

	//Method to add product to cart
	public Boolean addToCart(String weight)
	{ 
		Select weightSelect=new Select(gewichtSelect);
		weightSelect.selectByValue(weight);
		JavascriptExecutor js2=(JavascriptExecutor)driver;
		js2.executeScript("window.scrollBy(0,1000)");
		aufdenWarenstaplerBtn.click();
		WebElement expProduct=driver.findElement(By.xpath("//div[@class='o-add-to-cart-modal__product-details']//p[@class='a-txt__title']"));	
		expProduktInList=expProduct.getText();
		zumWarenstaplerBtn.click();
		if (zurKasseBtn.isDisplayed())
			verifyWebElement=true;
		else
			verifyWebElement=false;
		return verifyWebElement;
	}

	// Method to verify Products in the cart and checkout
	public Boolean verifyCartItemAndCheckOutProduct(int expProduct )
	{
		try
		{
			String actProductInCart= cartProduktverification.get(expProduct).getText();
			System.out.println(actProductInCart);
			if (actProductInCart.contains(expProduktInList))
			{
				zurKasseBtn.click();
			}

			if (vornameTextField.isDisplayed())
				verifyWebElement=true;
			else
				verifyWebElement=false;
		}catch(Exception e)
		{

			System.out.println(e);
		}

		return verifyWebElement;
	}

	//Method  to  enter Billing address

	public Boolean enterBillingAddressAndSelectPaymentMethod(String firstName,String lastName,String address,String postal,String city,String expPaymentMethod )
	{	
		vornameTextField.sendKeys(firstName);
		nachnameTextField.sendKeys(lastName);
		addressTextField.sendKeys(address);
		postleitZahlTextField.sendKeys(postal);
		stadtTextField.sendKeys(city);
		try
		{
			for (int i=0;i<=ZahlungsmethodeText.size();i++)
			{
				String actPaymentMethod=ZahlungsmethodeText.get(i).getText();
				if (actPaymentMethod.equals(expPaymentMethod))
				{ 
					List<WebElement> e1=driver.findElements(By.xpath("//div[@class='co-payment__methods form__field']//div[@class=' form__field form__field--radio']/input"));
					Thread.sleep(1000);
					if (!e1.get(i).isSelected())
					{
						Actions a=new Actions(driver);
						a.moveToElement(e1.get(i)).click().perform();
						verifyWebElement=true;
						break;
					}
				}
				else
				{
					verifyWebElement=false;
				}
			}

		}catch(Exception e)
		{

			System.out.println(e);
		}
		return verifyWebElement;
	}
}
