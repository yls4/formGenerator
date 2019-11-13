package formGenerator;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Driver;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class pdfFormReader {
	static PDDocument pd;
	static String gravityFormsString = "Forms";
	public static void main(String[] args) throws InterruptedException {
		try {
			File file = new File("form_b410.pdf");
			pd = PDDocument.load(file);
			PDDocumentCatalog pdCatalog = pd.getDocumentCatalog();
			PDAcroForm pdAcroForm = pdCatalog.getAcroForm();
			NameFieldPair[] pairs = new NameFieldPair[pdAcroForm.getFields().size()];
			LoadData(pairs, pdCatalog, pdAcroForm);
			for (NameFieldPair s : pairs) {
				System.out.println(s.name + " " + s.type);
			}
			//CreateGravityForms();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void LoadData(NameFieldPair[] pairs, PDDocumentCatalog pdCatalog, PDAcroForm pdAcroForm) {
		int counter = 0;
		for(PDField pdField : pdAcroForm.getFields()) {
			pairs[counter] = new NameFieldPair(pdField.getFullyQualifiedName(), pdField.getFieldType());
			counter++;
		}
	}
	
	private static void CreateGravityForms() throws InterruptedException {
		File f = new File("C:\\Program Files\\Nightly\\firefox.exe");
		FirefoxBinary ffBinary = new FirefoxBinary(f);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("capability.policy.default.Window.QueryInterface", "allAccess"); 
		profile.setPreference("capability.policy.default.Window.frameElement.get","allAc‌​cess"); 
		profile.setAcceptUntrustedCertificates(true); 
		profile.setAssumeUntrustedCertificateIssuer(true); 
		profile.setPreference("capability.policy.default.Window.frameElement.get","allAccess");
		WebDriver driver = new FirefoxDriver(ffBinary, profile);
		String pass = "%HIk6IEiP$%MGXb^%x";
		driver.get("http://104.131.36.26/wp-admin/");
		driver.findElement(By.id("user_login")).sendKeys("pro");
		Thread.sleep(1000);
		driver.findElement(By.id("user_pass")).sendKeys(pass);
		Thread.sleep(1000);
		driver.findElement(By.id("wp-submit")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		driver.get("http://104.131.36.26/wp-admin/admin.php?page=gf_new_form");
		Thread.sleep(3000);
		driver.findElement(By.id("new_form_title")).sendKeys("Test3");
		Thread.sleep(800);
		driver.findElement(By.id("save_new_form")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		WebElement fields = driver.findElement(By.id("add_standard_fields"));
		List<WebElement> types = fields.findElements(By.cssSelector("input"));
		
		WebElement field;
		WebElement label;
		int counter = 1;
		JavascriptExecutor ex = (JavascriptExecutor) driver;
		driver.switchTo().activeElement();
		for (WebElement e : types) {
			//System.out.println(e.getAttribute("value"));
				if (e.getAttribute("value").equals("Single Line Text")) {
					e.sendKeys(Keys.ENTER);
					Thread.sleep(500);
					field = driver.findElement(By.id("field_1"));
					field.click();
					ex.executeScript("document.getElementById('field_1').click()");
					//label = field.findElement(By.id("input_1"));
					//label.sendKeys("test");
					counter++;
				}
			}
		
		/*List<WebElement> menus = driver.findElements(By.className("wp-menu-name"));
		for (WebElement e : menus) {
			if (e.getText().equals(gravityFormsString)) {
				WebElement parent = e.findElement(By.xpath(".."));
				parent.click();
				System.out.println(parent.getText());
			}
		}*/
	}
	
	private static class NameFieldPair {
		public String name;
		public String type;
		public NameFieldPair(String s, String t) {
			name = s;
			type = t;
		}
	}
}
