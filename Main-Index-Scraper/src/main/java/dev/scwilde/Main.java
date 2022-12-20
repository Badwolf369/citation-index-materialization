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
import java.util.List;



public class Main {
	
	public static int[] ChapterCountPerBook = {
	//000-Genesis		001-Exodus			002-Leviticus		003-Numbers			004-Deuteronomy
	  50, 				40, 				27, 				36, 				34,
	//005-Joshua		006-Judges			007-Ruth			008-1 Samuel		009-2 Samuel
	  24, 				21,					4,					31,					24,
	//010-1 Kings		011-2 Kings			012-1 Chronicles	013-2 Chronicles	014-Ezra
	  22,				25,					29,					36,					10,
	//015-Nehemiah		016-Esther			017-Job				018-Psalms			019-Propherbs
	  13,				10,					42,					150,				31,
	//020-Ecclesiastes	021-S.O.S			022-Isaiah			023-Jeremiah		024-Lamentations
	  12,				8,					66,					52,					5,
	//025-Ezekiel		026-Daniel			027-Hosea			028-Joel			029-Amos
	  48,				12,					14,					3,					9,
	//030-Obadiah		031-Jonah			032-Micah			033-Nahum			034-Habakkuk
	  1,				4,					7,					3,					3,
	//035-Zephaniah		036-Haggai			037-Zechariah		038-Malachi
	  3,				2,					14,					4,
	  
	//039-Matthew		040-Mark			041-Luke			042-John			043-Act
	  28,				16,					24,					21,					28,
	//044-Romans		045-1 Corinthians	046-2 Corinthians	047-Galatians		048-Ephesians
	  16,				16,					13,					6,					6,
	//049-Philippians	050-Colossians		051-1 Thessalonians	052-2 Thessalonians	053-1 Timothy
	  4,				4,					5,					3,					6,
	//054-2 Timothy		055-Titus			056-Philemon		057-Hebrews			058-James
	  4,				3,					1,					13,					5,
	//059-1 Peter		060-2 Peter			061-1 John			062-2 John			063-3 John
	  5,				3,					5,					1,					1,
	//064-Jude			065-Revelation
	  1,				22,
	  
	//066-Title Page	067-Introduction	068-Testimony *3	069-Tetsimony *8	070-1 Nephi
	  1,				1,					1,					1,					22,
	//071-2 Nephi		072-Jacob			073-Enos			074-Jarom			075-Omni
	  33,				7,					1,					1,					1,
	//076-W.O.M.		077-Mosiah			078-Alma			079-Helaman			080-3 Nephi
	  1,				29,					63,					16,					30,
	//081-4 Nephi		082-Mormon			083-Ether			084-Mormoni
	  1,				9,					15,					10,
	  
	//085-Intro			086-Sections		087-O.D.
	  1,				138,				2,
	  
	//088-Moses			089-Abraham			090-JS Matthew		091-JS History		092-Articles Of Faith
	  8,				5,					1,					1,					1			
	};
	public static int chapterCounterBook = 0;
	
	public static WebDriver browser;
	public static JavascriptExecutor jse;
	public static FileWriter file;
	
	public static void Initialize() throws IOException {
		/*
		 * Start the Web Driver and navigate it to the proper site to begin web scraping
		 */
		
		// Start and setup the browser
		
		System.setProperty("webdriver.chrome.driver", "S:\\Program Files\\ChromeDriver\\chromedriver.exe");
		browser = new ChromeDriver();
		jse = (JavascriptExecutor)browser;
		browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		
		options.add_argument("--headless"); //Runs Chrome in headless mode
		options.add_argument('--no-sandbox'); //Bypass OS security model
		
		browser.navigate().to("https://birdsoftheworld.org/bow/species/stejay/cur/introduction");
		
		// Open the output file
		
//		file = new FileWriter("output.txt");
		
	}
	
	public static void FilterByConference() {
		
		jse.executeScript("openFilter();");
		// Wait until element with id "corpusfilter" is loaded before clicking it
		new WebDriverWait(browser, Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(By.id("corpusfilter"))).click();
		// Wait until element with id "G" is loaded before clicking it
		new WebDriverWait(browser, Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(By.id("G"))).click();
		// If this script isn't run twice it doesn't work for some reason
		jse.executeScript("getFilterParameters()");
		jse.executeScript("getFilterParameters()");
		
		// Wait for the filter to come into effect
		new WebDriverWait(browser, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOfElementLocated(By.className("filter_message")));
	}
	
	
	public class shelf {
		public static void GetCitations() throws IOException {
			
			browser.navigate().refresh();
			WebElement focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
			
			List<WebElement> citations_main = focusPane.findElements(By.className("reference"));
			List<WebElement> citations_titles = focusPane.findElements(By.className("talktitle"));
			int citations_count = citations_main.size();
			
			for (int citations_i = 0; citations_i < citations_count; citations_i++) {
				
//				browser.navigate().refresh();
				focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
				citations_main = focusPane.findElements(By.className("reference"));
				citations_titles = focusPane.findElements(By.className("talktitle"));
				WebElement[] this_citation = {citations_main.get(citations_i), citations_titles.get(citations_i)};
								
				System.out.printf("    T-%s \t\t\t\"%s\"\n", this_citation[0].getText(), this_citation[1].getText());
				file.write(String.format("    T-%s \t\t\t\"%s\"\n", this_citation[0].getText(), this_citation[1].getText()));
			}
		}
		
		public static void GetReferences() throws IOException {
			
			//66-69, 85
			List<Integer> troubleChildren = Arrays.asList(66, 67, 68, 69, 85);
			
			if (!troubleChildren.contains(chapterCounterBook)) {
				
				browser.navigate().refresh();
				WebElement focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
				
				List<WebElement> references = focusPane.findElements(By.tagName("li"));
				int references_count = references.size();
				
				for (int reference_i = 0; reference_i < references_count; reference_i++) {
					
					browser.navigate().refresh();
					focusPane = browser.findElement(By.xpath("//*[@id=\"citationindex2\"]/div[1]/div[2]/div[1]/ul"));
					references = focusPane.findElements(By.tagName("li"));
					WebElement this_reference = references.get(reference_i);
					this_reference = references.get(reference_i);
					
					String this_ref_text = this_reference.getText();
					
					System.out.printf("   R-%d. %s\n",reference_i,  this_ref_text);
					file.write(String.format("   R-%d. %s\n",reference_i, this_ref_text));
					
					if (!(this_ref_text.contains("JST") || this_ref_text.contains("Headnote") || this_ref_text.contains("Endnote"))) {
					
						this_reference.click();
						GetCitations();
						browser.navigate().back();
					}
				}
			} else {
			
				GetCitations();
			}
		}
		
		public static void GetChapters() throws IOException {
			
			if (ChapterCountPerBook[chapterCounterBook] != 1) {
				
				int chapter_offset = 0;
				int chapters_count = ChapterCountPerBook[chapterCounterBook];
				for (int chapter_i = 0; chapter_i < chapters_count; chapter_i++) {
					
					browser.navigate().refresh();
					WebElement focusPane = browser.findElement(By.className("sciwrapper"));
					List<WebElement> chapCitationCounts = focusPane.findElements(By.className("citationcount"));
					List<WebElement> chapters = focusPane.findElements(By.className("chap"));
					
					String printText;
					if (chapter_i - chapter_offset >= chapters.size()) {
						
						printText = "[0]";
					} else {
					
						WebElement[] this_chapter = {chapters.get(chapter_i - chapter_offset), chapCitationCounts.get(chapter_i - chapter_offset)};
						
						if (Integer.parseInt(this_chapter[0].getText()) == chapter_i + 1) {
							printText = this_chapter[1].getText();
						} else {
							printText = "[0]";
							chapter_offset++;
						}
					}
					
					System.out.printf("  C-%d. ch. %-3d %s\n", chapter_i, chapter_i + 1, printText);
					file.write(String.format("  C-%d. ch. %-3d %s\n", chapter_i, chapter_i + 1, printText));
//					
					if (printText != "[0]") {
						WebElement this_chapter = chapters.get(chapter_i - chapter_offset);
						this_chapter.findElement(By.xpath("./..")).click();
						GetReferences();
						browser.navigate().back();
					}
				}
			} else {
//				
				GetReferences();
			}
		}
		
		public static void GetBooks(int volumes_i) throws IOException {
			
			browser.navigate().refresh();
			WebElement focusPane = browser.findElement(By.className("sciwrapper"));
			List<WebElement> volumes = focusPane.findElements(By.className("volumecontents"));
			
			WebElement this_volume = volumes.get(volumes_i);
			
			List<WebElement> books = this_volume.findElements(By.className("grid"));
			int books_count = books.size();
			
			for(int book_i = 0; book_i < books_count; book_i++, chapterCounterBook++) {

				browser.navigate().refresh();
//				new WebDriverWait(browser, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("volumetitle")));

				focusPane = browser.findElement(By.className("sciwrapper"));
				volumes = focusPane.findElements(By.className("volumecontents"));
				this_volume = volumes.get(volumes_i);
				books = this_volume.findElements(By.className("grid"));
				WebElement this_book = books.get(book_i);
				String this_book_text = this_book.getText().replace('\n', ' ');

				System.out.printf(" B-%02d. %s\n", book_i, this_book_text);
				file.write(String.format(" B-%02d. %s\n", book_i, this_book_text));
				
				this_book.click();
				GetChapters();
				browser.navigate().back();
			}	
		}
		
		public static void GetVolumes() throws IOException {
			
			WebElement focusPane = browser.findElement(By.className("sciwrapper"));
			List<WebElement> volumesTitles = focusPane.findElements(By.className("volumetitle"));
			List<WebElement> volumesContents = focusPane.findElements(By.className("volumecontents"));
			int volumes_count = volumesContents.size();
			
			for(int volumes_i = 0; volumes_i < volumes_count; volumes_i++) {
				
				browser.navigate().refresh();
				focusPane = browser.findElement(By.className("sciwrapper"));
				volumesTitles = focusPane.findElements(By.className("volumetitle"));
				volumesContents = focusPane.findElements(By.className("volumecontents"));
				WebElement this_volume[] = {volumesTitles.get(volumes_i), volumesContents.get(volumes_i)};
				String this_volume_text = this_volume[0].getText().replace('\n', ' ');
				
				System.out.printf("V-%d. %s\n", volumes_i, this_volume_text);
				file.write(String.format("V-%d. %s\n", volumes_i, this_volume_text));
				
				GetBooks(volumes_i);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		long start_time = System.currentTimeMillis();
		
		Initialize();
//		FilterByConference();
//		shelf.GetVolumes();
//		file.close();
		
//		browser.quit();
		
		long end_time = System.currentTimeMillis();
		System.out.printf("\n\n Finished. Time Elapsed - %dms", end_time - start_time);
		
	}
}
