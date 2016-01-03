package service.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import domain.JSONProperties;
import domain.NewsSourceDataModel;
import service.scraper.NewsArticleScraper;

/**
 * The FileManager reads a JSON-LD file and locally stores the article text and article
 * image in .txt and .jpeg format respectively. The format of the JSON-LD file should be
 * according to the attributes specified in JSONProperties. The JSON-LD file should be 
 * previously generated with NewsArticleScraper.extractArticleFromFile(). The purpose of
 * this class is to facilitate the storage of the text and images for further analysis. 
 *
 *@see JSONProperties
 *@see NewsArticleScraper
 */
public class FileManager {

	/**
	 * The URLs for articles and images will be be read from the JSON-LD file.
	 * Since the file can be large, the generated text files and images will be
	 * categorized according to the publishing date (also in the JSON-LD) file
	 * to avoid one directory having too many files.
	 * 
	 * Both for article text and images the name of the files will be the ID of
	 * the article found in the JSON-LD. This way the text and the image of the
	 * same article will have the same name, albeit different extensions, to
	 * make it easier to map it later to the exact entry in the JSON-LD file.
	 * 
	 * Files of articles that do not have any date will be stored in the
	 * respective directory under a new directory called "unknownDate".
	 * 
	 * @param filePath
	 *            the JSON/JSON-LD file which the article URLs can be read from
	 * @param newsSource
	 *            the domain data for the news source containing the XPath
	 *            information
	 * @param articleDir
	 *            relative path to the directory where the text files (.txt)
	 *            should be stored, e.g. "data/articles/". if null the article
	 *            text will not be scraped.
	 * @param imageDir
	 *            relative path to the directory where the image files (.jpg)
	 *            should be stored, e.g. "data/images/". If not the images will
	 *            not be stored.
	 */
	public void extractArticleTextAndImageFromFile(String filePath, NewsSourceDataModel newsSource, String articleDir, String imageDir) {
		File file = new File(filePath);
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals(JSONProperties.REVERSE)) {
					reader.beginObject();
					if (reader.nextName().equals(JSONProperties.PUBLISHER)) {
						reader.beginArray();
						while (reader.hasNext()) {
							reader.beginObject();
							String id = null;
							String date = JSONProperties.UNKNOWN_DATE;
							while (reader.hasNext()) {
								String property = reader.nextName();
								if (property.equals(JSONProperties.ID)) {
									id = reader.nextString();
								} else if (property.equals(JSONProperties.DATE)) {
									if (reader.peek() != JsonToken.NULL) {
										date = reader.nextString();
									} else {
										reader.skipValue();
									}
									if (date.isEmpty()) {
										date = JSONProperties.UNKNOWN_DATE;
									}
								} else if (property.equals(JSONProperties.URL) && articleDir != null) {
									String scrapedText = scrapeArticleText(reader.nextString(), newsSource);
									if (scrapedText != null && !scrapedText.isEmpty()) {
										saveTextInFile(articleDir + date + "/" + id + ".txt", scrapedText);
									}
								} else if (property.equals(JSONProperties.IMAGE) && imageDir != null) {
									reader.beginObject();
									while (reader.hasNext()) {
										if (reader.nextName().equals(JSONProperties.URL)) {
											saveImageFile(imageDir + date + "/" + id + ".jpg", getImage(reader.nextString()));
										} else {
											reader.skipValue();
										}
									}
									reader.endObject();
								} else {
									reader.skipValue();
								}
							}
							reader.endObject();
						}
						reader.endArray();
						reader.endObject();
					} else {
						reader.skipValue();
					}
				} else {
					reader.skipValue();
				}

			}
			reader.endObject();

			reader.close();
		} catch (UnsupportedEncodingException e) {
			System.err.println("UnsupportedEncodingException: " + e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}

	}

	/**
	 * It scrapes an HTML document given its URL and return the scraped text,\
	 * which is to be found inside the div tag of class "story-body__inner". If
	 * another tag is needed or wanted, adjust the tagIdentifier accordingly.
	 * 
	 * @param url
	 *            The URL of the page it needs to be scraped
	 * @return The scraped text
	 */

	public static String scrapeArticleText(String url, NewsSourceDataModel newsSource) {
		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page;
		String pageAsText = null;
		try {
			page = webClient.getPage(url);
			List<?> articleBody = page.getByXPath(newsSource.getTextTag());
			if (articleBody != null && articleBody.size() >= 1) {
				StringBuffer textBuffer = new StringBuffer(2048);
				for (int n = 0; n < articleBody.size(); n++) {
					HtmlElement article = (HtmlElement) articleBody.get(n);
					textBuffer.append(" " + article.asText());
				}
				pageAsText = textBuffer.toString();
			}
		} catch (FailingHttpStatusCodeException e) {
			System.err.println("FailingHttpStatusCodeException");
		} catch (ScriptException e) {
			System.err.println("ScriptException");
		} catch (MalformedURLException e) {
			System.err.println("MalformedURLException");
		} catch (IOException e) {
			System.err.println("IOException");
		} finally {
			webClient.close();
		}

		return pageAsText;
	}

	/**
	 * It fetched the image given its URL as InputStream
	 * 
	 * @param url
	 *            The URL of the image
	 * @return The image data as InputStream
	 */
	private static InputStream getImage(String url) {
		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		UnexpectedPage page;
		InputStream input = null;
		try {
			page = webClient.getPage(url);
			input = page.getWebResponse().getContentAsStream();
		} catch (FailingHttpStatusCodeException e) {
			System.err.println("FailingHttpStatusCodeException" + e.getMessage());
		} catch (ScriptException e) {
			System.err.println("ScriptException" + e.getMessage());
		} catch (MalformedURLException e) {
			System.err.println("MalformedURLException" + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException" + e.getMessage());
		} finally {
			webClient.close();
		}

		return input;
	}

	/**
	 * Saves the text input in a local file
	 * 
	 * @param filePath the file path which needs to be created and written to
	 * @param text the string to be written in the file
	 */
	public static void saveTextInFile(String filePath, String text) {
		try {
			File textFile = new File(filePath);
			textFile.getParentFile().mkdirs();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(textFile));
			bufferedWriter.write(text);
			bufferedWriter.close();
		} catch (IOException e) {
			System.err.println("MalformedURLException" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Saves an image defined as InputStream to a local file
	 * 
	 * @param filePath the file path which needs to be created and written to
	 * @param is the image represented as an InputStream
	 */
	private static void saveImageFile(String filePath, InputStream is) {
		try {
			File img = new File(filePath);
			img.getParentFile().mkdirs();
			OutputStream os;
			os = new FileOutputStream(img);
			if (is != null) {
				IOUtils.copy(is, os);
				is.close();
			}
			os.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException" + e.getMessage());
		} catch (IOException e) {

		}
	}

}
