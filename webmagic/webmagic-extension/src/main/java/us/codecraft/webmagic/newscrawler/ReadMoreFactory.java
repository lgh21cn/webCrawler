package us.codecraft.webmagic.newscrawler;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ReadMoreFactory {

	static FirefoxProfile getFirefoxProfile() {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("network.proxy.type", 1);
		// Set "127.0.0.1",8087
		firefoxProfile.setPreference("network.proxy.http", "127.0.0.1");
		firefoxProfile.setPreference("network.proxy.http_port", 8087);
		firefoxProfile.setPreference("network.proxy.no_proxies_on", "");
		return firefoxProfile;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// "C:\Program Files (x86)\Mozilla Firefox\firefox.exe"
		// System.setProperty("webdriver.gecko.driver",
		// "C:\\Users\\ASUS\\Downloads\\selenium-java-3.0.1\\client-combined-3.0.1-nodeps.jar");
		// System.setProperty("webdriver.firefox.bin", "C:\\Program Files
		// (x86)\\Mozilla Firefox\\firefox.exe");
		// System.setProperty("webdriver.firefox.bin",
		// "C:\\Users\\ASUS\\Downloads\\selenium-java-3.0.1");
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\ASUS\\Downloads\\geckodriver-v0.11.1-win64\\geckodriver.exe");
//		System.setProperty("phantomjs.binary.path", "C:\\Users\\ASUS\\Downloads\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
		//
//		WebDriver driver = 
//				//new PhantomJSDriver();
//				new FirefoxDriver(getFirefoxProfile());
//		driver.get("http://search.globaltimes.cn/QuickSearchCtrl");
//		
//		WebElement element=driver.findElement(By.name("search_txt"));
//		element.sendKeys("Hupu");
//		
//		element.submit();
//		driver.close();
		// 获取 网页的 title
//		System.out.println("1 Page title is: " + driver.getTitle());

		loadingCrawler("Uyghur");
		

	}
	
	private static void loadingCrawler(String query){
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\ASUS\\Downloads\\geckodriver-v0.11.1-win64\\geckodriver.exe");
		
		String ori_url="http://search.globaltimes.cn/QuickSearchCtrl";
		
		WebDriver driver = 
				//new PhantomJSDriver();
				new FirefoxDriver(getFirefoxProfile());
		
		driver.get(ori_url);
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		
		
		WebElement input_txt=driver.findElement(By.name("search_txt"));
		input_txt.sendKeys(query);
		
		WebElement btn=driver.findElement(By.className("btn"));
		btn.submit();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.name("search_txt"), query));
		
		wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy(ByXPath.xpath("//div[@class='container-fluid row-content']")));
		
		List<WebElement>   urlElements=driver.findElements(By.xpath("//div[class='container-fluid row-content']/div[@class='row-fluid']/div[@class='span9']"));
		
		//找链接，写入文件
		String fileName=FileUtils.GLOBALTIMES_NEWS+"global_time_urls.txt";
		StringBuilder builder=new StringBuilder();
		for (WebElement webElement : urlElements) {
			String url=webElement.findElement(By.xpath("//span9/blockquote/a")).getAttribute("href");
			builder.append(url);
			builder.append("\n");
			System.out.println(url);
		}
		FileUtils.writeCorpus(fileName, builder.toString());

		
		
		//页面跳转
//		driver.
		
		
		//关闭driver
		driver.close();
	}

}
