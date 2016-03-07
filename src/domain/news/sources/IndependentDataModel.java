package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

public class IndependentDataModel extends NewsSourceDataModel {

	public IndependentDataModel() {

		super.setTitleTag("//h1[@itemprop='headline']");
		super.setImageTag("//figure[@class='featured-media medium-width']/img");
		super.setCaptionTag("//figcaption[@class='caption']");
		super.setTextTag("//div[@class='main-content-column']/div[@itemprop='articleBody']/p");
		super.setPublicationDateTag("//time[@class='dateline']");
		super.setName("The Independent");
		super.setID("http://www.independent.co.uk/");
		super.setLang("en");
		super.setType("Newspaper");
		super.setMusthaveUrls(new ArrayList<String>(Arrays.asList("news")));
	}
}
