package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

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
