package service.scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.google.gson.stream.JsonWriter;

import domain.ArticleDataModel;
import domain.JSONProperties;
import domain.NewsSourceDataModel;
import service.text.TextAnalyzer;
import utilities.SHAHashGenerator;

/**
 * The NewsArticleScraper is the entry point to the extraction process which defines
 * methods to support the extraction of multiple article metadata. It makes of of GSON 
 * (https://google-gson.googlecode.com) for the generation of JSON and HtmlUnit 
 * (http://htmlunit.sourceforge.net/) for extraction of data in HTML pages based on XPaths
 */
public class NewsArticleScraper {

	/**
	 * Extract data from each article accessed with its URL in the input file
	 * 
	 * @param inputFile
	 *            the input file that contains all URL to be crawled in a CSV
	 *            format. The URL should be given one URL per line. Additionally
	 *            a line in the input file can (and should) contain the data in
	 *            the following format DD-MM-YYYY separated from the URL by a
	 *            space.
	 * @param outputFile
	 *            the file path where the output file should be generated in the
	 *            JSON-LD format (the file should contain the extension .jsonld)
	 * @param newsSource
	 *            the NewsSourceDataModel object containing all setting for the
	 *            news publisher
	 * @param contentAnalysis
	 *            if true, the content will also be analyzed with TextRazor,
	 *            otherwise topics and entities will not be given (Make sure to
	 *            modify TextAnalyzer with your TextRazor API Key)
	 */
	public void extractArticleFromFile(String inputFile, String outputFile, NewsSourceDataModel newsSource, boolean contentAnalysis) {

		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page;

		try {
			if (inputFile != null && !inputFile.isEmpty()) {
				BufferedReader bReader = new BufferedReader(new FileReader(inputFile));
				String line = null;

				// generate a JSON file
				File file = new File(outputFile);
				file.getParentFile().mkdirs();
				JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				writer.setIndent("	");

				writer.beginObject();
				writer.name(JSONProperties.CONTEXT);

				writer.beginObject();
				writer.name(JSONProperties.VOCAB).value("http://schema.org/");
				writer.name(JSONProperties.DATE);
				writer.beginObject();
				writer.name(JSONProperties.TYPE).value("http://www.w3.org/2001/XMLSchema#dateTime");
				writer.endObject();
				writer.endObject();

				writer.name(JSONProperties.ID).value(newsSource.getID());
				writer.name(JSONProperties.TYPE).value(newsSource.getType());
				writer.name(JSONProperties.NAME).value(newsSource.getName());

				writer.name(JSONProperties.REVERSE);
				writer.beginObject();

				writer.name(JSONProperties.PUBLISHER);
				writer.beginArray();

				// the line includes also the date
				while ((line = bReader.readLine()) != null && !line.isEmpty()) {
					String lineDate = null;
					String lineURL = line;

					if (line.indexOf(' ') != -1) {
						lineDate = line.substring(0, line.indexOf(' '));
						lineURL = line.substring(line.indexOf(' ') + 2, line.length());
					}

					if (newsSource.getExceptionUrls() != null) {
						for (String exception : newsSource.getExceptionUrls()) {
							if (lineURL.contains(exception))
								continue;
						}
					}

					if (newsSource.getMusthaveUrls() != null) {
						for (String muthave : newsSource.getMusthaveUrls()) {
							if (!lineURL.contains(muthave))
								continue;
						}
					}

					try {
						page = webClient.getPage(lineURL);
						ArticleDataModel articleData = new ArticleDataModel();

						// title
						extractTitle(page, newsSource, articleData);

						// text
						extractText(page, newsSource, articleData);

						// image url
						extractImage(page, newsSource, articleData);

						// image caption
						extractCaption(page, newsSource, articleData);

						// publicationDate
						articleData.setPublicationDate(lineDate);
						extractPublicationDate(page, newsSource, articleData);

						String articleID = SHAHashGenerator.generateHashCode(lineURL);

						/*
						 * An article will be included if it at least has a URL
						 * (and an ID), a headline and an article body (text).
						 */
						if (articleData.getTitle() != null && articleID != null && articleData.getText() != null) {

							if (contentAnalysis && !articleData.getText().isEmpty()) {
								TextAnalyzer textAnalyzer = new TextAnalyzer();
								articleData.setTextRazorData(textAnalyzer.analyzeText(articleData.getText()));
							}
							writer.beginObject();
							writer.name(JSONProperties.ID).value(articleID);
							writer.name(JSONProperties.DATE).value(articleData.getPublicationDate());
							writer.name(JSONProperties.TITLE).value(articleData.getTitle());
							writer.name(JSONProperties.URL).value(lineURL);
							writer.name(JSONProperties.IMAGE);
							writer.beginObject();
							writer.name(JSONProperties.ID).value(articleID + ".jpg");
							writer.name(JSONProperties.CAPTION).value(articleData.getCaption());
							writer.name(JSONProperties.URL).value(articleData.getImage());
							writer.endObject();

							if (articleData.getTextRazorData() != null) {
								Set<String> coarseTopics = (HashSet<String>) articleData.getTextRazorData().getCoarseTopics();
								Set<String> topics = (HashSet<String>) articleData.getTextRazorData().getTopics();

								if (coarseTopics != null && !coarseTopics.isEmpty()) {
									writer.name(JSONProperties.COARSE_TOPICS);
									writer.beginArray();
									for (String coarseTopic : coarseTopics) {
										writer.value(coarseTopic);
									}
									writer.endArray();
								}

								if (topics != null && !topics.isEmpty()) {
									writer.name(JSONProperties.TOPICS);
									writer.beginArray();
									for (String topic : topics) {
										writer.value(topic);
									}
									writer.endArray();
								}
							}
							writer.endObject();
						}

					} catch (FailingHttpStatusCodeException e) {
						System.err.println("FailingHttpStatusCodeException " + lineURL);

						continue;
					} catch (ScriptException e) {
						System.err.println("ScriptException " + lineURL);

						continue;
					} catch (MalformedURLException e) {
						System.err.println("MalformedURLException " + lineURL);

						continue;
					} catch (IOException e) {
						System.err.println("IOException " + lineURL);

						continue;
					}
				}

				writer.endArray();
				writer.endObject();
				writer.endObject();
				writer.close();
				bReader.close();

			}

		} catch (IOException e) {
			System.err.println("IOException");
		} finally {
			webClient.close();
		}
	}

	/**
	 * Extracts the title from an article page based on the tag defined in the news 
	 * source and saves it in the article data
	 * 
	 * @param page the article page the title of which needs to be extracted
	 * @param newsSource the news publisher
	 * @param articleData the article data where the extracted title will be stored
	 */
	public void extractTitle(HtmlPage page, NewsSourceDataModel newsSource, ArticleDataModel articleData) {
		List<?> title = page.getByXPath(newsSource.getTitleTag());
		if (title != null && title.size() >= 1) {
			HtmlHeading1 h1 = (HtmlHeading1) title.get(0);
			articleData.setTitle(StringEscapeUtils.escapeCsv(h1.asText()));
		}
	}

	/**
	 * Extracts the article text from an article page based on the tag defined in the news 
	 * source and saves it in the article data
	 * 
	 * @param page the article page the title of which needs to be extracted
	 * @param newsSource the news publisher
	 * @param articleData the article data where the extracted title will be stored
	 */
	public void extractText(HtmlPage page, NewsSourceDataModel newsSource, ArticleDataModel articleData) {
		// It can happen that the text is dispersed into
		// several section of similar properties, then combine
		// everything
		List<?> articleBody = page.getByXPath(newsSource.getTextTag());
		if (articleBody != null && articleBody.size() >= 1) {
			StringBuffer textBuffer = new StringBuffer(2048);
			for (int n = 0; n < articleBody.size(); n++) {
				HtmlElement article = (HtmlElement) articleBody.get(n);
				textBuffer.append(" " + article.asText());
			}
			articleData.setText(textBuffer.toString());
		}

	}

	/**
	 * Extracts the image url from an article page based on the tag defined in the news 
	 * source and saves it in the article data
	 * 
	 * @param page the article page the title of which needs to be extracted
	 * @param newsSource the news publisher
	 * @param articleData the article data where the extracted title will be stored
	 */
	public void extractImage(HtmlPage page, NewsSourceDataModel newsSource, ArticleDataModel articleData) {
		// image url
		List<?> imageTag = page.getByXPath(newsSource.getImageTag());
		if (imageTag != null && imageTag.size() >= 1) {
			HtmlImage image = (HtmlImage) imageTag.get(0);
			articleData.setImage(image.getSrcAttribute());
		}
	}
	
	/**
	 * Extracts the caption next to the image from an article page based on the tag defined in the news 
	 * source and saves it in the article data
	 * 
	 * @param page the article page the title of which needs to be extracted
	 * @param newsSource the news publisher
	 * @param articleData the article data where the extracted title will be stored
	 */
	public void extractCaption(HtmlPage page, NewsSourceDataModel newsSource, ArticleDataModel articleData) {
		List<?> captionTag = page.getByXPath(newsSource.getCaptionTag());
		if (captionTag != null && captionTag.size() >= 1) {
			HtmlSpan caption = (HtmlSpan) captionTag.get(0);
			articleData.setCaption(StringEscapeUtils.escapeCsv(caption.asText()));
		}

	}

	/**
	 * Extracts the article publication date from an article page based on the tag defined in the news 
	 * source and saves it in the article data
	 * 
	 * @param page the article page the title of which needs to be extracted
	 * @param newsSource the news publisher
	 * @param articleData the article data where the extracted title will be stored
	 */
	public void extractPublicationDate(HtmlPage page, NewsSourceDataModel newsSource, ArticleDataModel articleData) {
		if (articleData.getPublicationDate() == null && newsSource.getPublicationDateTag() != null) {
			List<?> dateTag = page.getByXPath(newsSource.getPublicationDateTag());
			if (dateTag != null && dateTag.size() >= 1) {
				HtmlUnknownElement time = (HtmlUnknownElement) dateTag.get(0);
				articleData.setPublicationDate(time.getAttribute("datetime"));
			}
		}

	}
}
