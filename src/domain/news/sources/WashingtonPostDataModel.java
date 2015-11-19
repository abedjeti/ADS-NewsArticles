package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

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
