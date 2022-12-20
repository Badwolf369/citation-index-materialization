package dev.scwilde;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


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
	
	public static Scanner openFile(String fileName) {
		
		try {
			
			File infile = new File(fileName);
			Scanner sken = new Scanner(infile);
			
			return sken;
		} catch (FileNotFoundException e) {
			
			System.out.printf("File \"%s\" Not Found", fileName);
			return null;
		}
	}
	
	public static WebDriver browser;
	public static JavascriptExecutor jse;
	public static FileWriter outputIndex;
	
	public static void InitializeBrowser(){
		/*
		 * Start the Web Driver and navigate it to the proper site to begin web scraping
		 */
		
		// Start and setup the browser
		
		System.setProperty("webdriver.chrome.driver", "S:\\Program Files\\ChromeDriver\\chromedriver.exe");
		browser = new ChromeDriver();
		jse = (JavascriptExecutor)browser;
		browser.manage().timeouts().implicitlyWait(Duration.ofMillis(750));
		
		browser.navigate().to("https://scriptures.byu.edu");
		
		// Open the output file
		
		
	}

	public static String findElementTextByXpath_R(String xpath, int runs) {
		
		try { 
			
			return browser.findElement(By.xpath(xpath)).getText();
		} catch (StaleElementReferenceException e) {
			
			if(runs < 5) {
				browser.navigate().refresh();
				
				return findElementTextByXpath_R(xpath, runs+1);
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static String findElementTextByClassName_R(String className, int runs) {
		
		try { 
			
			return browser.findElement(By.className(className)).getText();
		} catch (StaleElementReferenceException e) {
			
			if(runs < 5) {
				browser.navigate().refresh();
				
				return findElementTextByClassName_R(className, runs+1);
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		long start_time = System.currentTimeMillis();
		
		outputIndex = new FileWriter("output.txt");
		String outputLine = "";
		
		InitializeBrowser();
		Scanner index = openFile("input.txt");
		
		int line_i = 1;
		while(index.hasNextLine()) {
			
			String line = index.nextLine();
			String[] splitLine = line.split(",");
//			browser.navigate().refresh();
			String talkcode = splitLine[4].substring(9).replace("')", "");
			browser.navigate().to("https://scriptures.byu.edu/content/talks_ajax/" + talkcode);
			if(line_i < 1824) {
				// This method crashes on talk #1824, talk code 1971-A,SM-1
				try {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"talkwrapper\"]/div/div[1]/p[3]", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} catch (Exception e) {
					
					outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				}
				
			} else if(1824 <= line_i && line_i < 1866) {
				// This method crashes on talk #1909, talk code 1972-A,RM-1
				
				String speakerRole = findElementTextByXpath_R("//*[@id=\"details\"]/h2/div/p[2]", 0);
				
				outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
				System.out.printf("%d. %s\n", line_i, outputLine);
			} else if(1866 <= line_i && line_i < 3384) {
				// This method crashes on talk #3379, talk code 1980-A,NM-2
				
				if(line_i == 2598) {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"primary\"]/p[1]/span", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} else if(3379 <= line_i && line_i <= 3383) {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"details\"]/p", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} else {
					
					try {
						
						String speakerRole = findElementTextByXpath_R("//*[@id=\"details\"]/h2/div/p[2]", 0);
						
						outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
						System.out.printf("%d. %s\n", line_i, outputLine);
					} catch (NoSuchElementException e) {

						findElementTextByXpath_R("//*[@id=\"details\"]/h2/div/p", 0);
						
						outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
						System.out.printf("%d. %s\n", line_i, outputLine);
					}
				}
			} else if(3384 <= line_i && line_i < 5154){
				
				try {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"details\"]/h2/div/p[2]", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} catch (NoSuchElementException e) {

					outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				}
			} else if(5154 <= line_i && line_i < 5297) {
				
				try {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"p3\"]", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} catch (NoSuchElementException e) {

					outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				}
			} else if(5297 <= line_i && line_i < 5367) {			
				
				try {
					
					String speakerRole = findElementTextByXpath_R("//*[@id=\"p2\"]", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} catch (NoSuchElementException e) {

					outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				}
			} else if(5367 <= line_i) {
				
				try {
					
					String speakerRole = findElementTextByClassName_R("author-role", 0);
					
					outputLine = String.format("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], speakerRole, talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				} catch (NoSuchElementException e) {

					outputLine = String.format("%s,%s,%s,%s,President of the Church,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], talkcode);
					System.out.printf("%d. %s\n", line_i, outputLine);
				}
			}
			
			line_i++;
			outputIndex.write(outputLine);
			outputIndex.flush();
		}
		
		index.close();
		
		browser.quit();
		
		long end_time = System.currentTimeMillis();
		System.out.printf("\n\n Finished. Time Elapsed - %dms", end_time - start_time);
	}
}
