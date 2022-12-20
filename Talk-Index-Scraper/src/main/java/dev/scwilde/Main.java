package dev.scwilde;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class Main {

	static class AppendString {
		String appendValue;
		
		AppendString(String appendValue) {
			this.appendValue = appendValue;
		}
		
		String[] ToStringArr(String[] inputArray) {
			
			int len = inputArray.length;
			String[] outputArray = new String[len + 1];
			
			for(int i = 0; i < len; i++) {	
				outputArray[i] = inputArray[i];
			}
			outputArray[len] = appendValue;
			
			return outputArray;
		}
	}
	static class AppendStringArr {
		String[] appendValue;
		
		AppendStringArr(String[] appendValue) {
			this.appendValue = appendValue;
		}
		
		String[][] ToStringArrArr(String[][] inputArray) {
			
			int len = inputArray.length;
			String[][] outputArray = new String[len + 1][];
			
			for(int i = 0; i < len; i++) {	
				outputArray[i] = inputArray[i];
			}
			outputArray[len] = appendValue;
			
			return outputArray;
		}
	}
	
	
	public static WebDriver browser;
	public static JavascriptExecutor jse;
	public static FileWriter outputIndex;
	
	public static void Initialize() throws IOException {
		/*
		 * Start the Web Driver and navigate it to the proper site to begin web scraping
		 */
		
		// Start and setup the browser
		
		System.setProperty("webdriver.chrome.driver", "S:\\Program Files\\ChromeDriver\\chromedriver.exe");
		browser = new ChromeDriver();
		jse = (JavascriptExecutor)browser;
		browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		
		browser.navigate().to("https://scriptures.byu.edu/#::g");
		
		// Open the output file
		
		
	}

	public static void main(String[] args) throws IOException {
		
		long start_time = System.currentTimeMillis();
		
		Initialize();

		String[][] equinx_arr = {};
		
		WebElement focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
		List<WebElement> equinoxes = focusPane.findElements(By.tagName("li"));
		for(int equinx_i = 0; equinx_i < equinoxes.size(); equinx_i++) {
			
			browser.navigate().refresh();
			focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
			equinoxes = focusPane.findElements(By.tagName("li"));
			
			WebElement this_equinx = equinoxes.get(equinx_i);
			String this_equinx_text = this_equinx.getText();
			System.out.printf(" %s\n", this_equinx_text);
			
			this_equinx.click();
			
			String[] this_equinx_data = {};
			browser.navigate().refresh();
			focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]"));
			List<WebElement> sessionTitles = focusPane.findElements(By.className("sessiontitle"));
			List<WebElement> talksBlocks;
			for (int session_i = 0; session_i < sessionTitles.size(); session_i++) {
				
//				browser.navigate().refresh();
				focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]"));
				sessionTitles = focusPane.findElements(By.className("sessiontitle"));
				talksBlocks = focusPane.findElements(By.className("talksblock"));
				
				WebElement this_sessionTitle = sessionTitles.get(session_i);
				WebElement this_talksBlock = talksBlocks.get(session_i);
				String this_sessionTitle_text = this_sessionTitle.getText();
				System.out.printf("  %s\n", this_sessionTitle_text);
				
				List<WebElement> talks = this_talksBlock.findElements(By.tagName("li"));
				for(int talk_i = 0; talk_i < talks.size(); talk_i++) {
					
					focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]"));
					sessionTitles = focusPane.findElements(By.className("sessiontitle"));
					talksBlocks = focusPane.findElements(By.className("talksblock"));
					this_sessionTitle = sessionTitles.get(session_i);
					this_sessionTitle_text = this_sessionTitle.getText();
					this_talksBlock = talksBlocks.get(session_i);
					
					talks = this_talksBlock.findElements(By.tagName("li"));
					WebElement this_talk = talks.get(talk_i);
					String this_talk_speaker = this_talk.findElement(By.className("speaker")).getText();
					String this_talk_title = this_talk.findElement(By.className("talktitle")).getText();
					String this_talk_jsclicker = this_talk.findElement(By.tagName("a")).getAttribute("onclick");
					String dataLine = String.format("%s,%s-%d,%s,%s,%s\n", this_equinx_text, this_sessionTitle_text, talk_i, this_talk_title, this_talk_speaker, this_talk_jsclicker);
					System.out.printf("   %s", dataLine);
					
					this_equinx_data = new AppendString(dataLine).ToStringArr(this_equinx_data);
				}
			}
			equinx_arr = new AppendStringArr(this_equinx_data).ToStringArrArr(equinx_arr);
			
			browser.navigate().back();
		}
		
		
		Collections.reverse(Arrays.asList(equinx_arr));
		
		outputIndex = new FileWriter("output.txt");
		for(int i = 0; i < equinx_arr.length; i++) {
			
			String[] this_equinx = equinx_arr[i];
			for(int l = 0;; l++) {
				
				if(l >= this_equinx.length) {
					break;
				}
				
				outputIndex.write(this_equinx[l]);
				outputIndex.flush();
			}
		}
		
		outputIndex.close();
		
		long end_time = System.currentTimeMillis();
		System.out.printf("\n\n Finished. Time Elapsed - %dms", end_time - start_time);

		browser.quit();
	}
}
