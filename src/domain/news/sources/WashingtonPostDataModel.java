package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

/**
 * The WashingtonPostDataModel serves as a container for the Washington Post 
 * news publisher and contains the XPaths to the data that needs to be 
 * extracted from the article page. Some URLs can be defined to be excluded,
 * whereas strings can be defined the existence of which needs to be verified
 * in the article URL.
 */
public class WashingtonPostDataModel extends NewsSourceDataModel {

	public WashingtonPostDataModel() {

		super.setTitleTag("//h1[@itemprop='headline']");
		super.setImageTag("//div[@class='inline-content inline-photo inline-photo-normal']/img");
		super.setCaptionTag("//div[@class='inline-content inline-photo inline-photo-normal']/span[@class='pb-caption']");
		super.setTextTag("//article[@itemprop='articleBody']");
		super.setPublicationDateTag("//span[@itemprop='datePublished']");
		super.setName("The Washington Post");
		super.setID("https://www.washingtonpost.com/");
		super.setLang("en");
		super.setType("Newspaper");
		super.setExceptionUrls(new ArrayList<String>(Arrays.asList("/posttv/", "/pb/recipes/", "/local/tn-lottery/")));
		super.setMusthaveUrls(new ArrayList<String>(Arrays.asList("_story")));
	}
}
