package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

/**
 * The NewYorkTimesDataModel serves as a container for the New York Times 
 * news publisher and contains the XPaths to the data that needs to be 
 * extracted from the article page. Some URLs can be defined to be excluded. 
 */
public class NewYorkTimesDataModel extends NewsSourceDataModel {

	public NewYorkTimesDataModel() {

		super.setTitleTag("//h1");
		super.setImageTag("//div[@class='image']/img");
		super.setCaptionTag("//figcaption[@class='caption']/span[@class='caption-text']");
		super.setTextTag("//p[@itemprop='articleBody']");
		super.setPublicationDateTag("//time[@class='dateline']");
		super.setName("New York Times");
		super.setID("http://www.nytimes.com/");
		super.setLang("en");
		super.setType("Newspaper");
		super.setExceptionUrls(new ArrayList<String>(Arrays.asList("/reuters/", "/aponline/", "paid-notice")));

	}
}
