package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

public class HuffingtonPostDataModel extends NewsSourceDataModel {

	public HuffingtonPostDataModel() {

		super.setTitleTag("//h1[@class='headline__title']");
		super.setImageTag("//img[@class='image__src']");
		super.setCaptionTag("//figcaption[@class='image__caption']");
		super.setTextTag("//div[@class='content-list-component text']/p");
		super.setPublicationDateTag("//time[@class='dateline']");
		super.setName("The huffington Post");
		super.setID("http://www.huffingtonpost.com/");
		super.setLang("en");
		super.setType("Newspaper");
		super.setMusthaveUrls(new ArrayList<String>(Arrays.asList("/201")));
	}
}
