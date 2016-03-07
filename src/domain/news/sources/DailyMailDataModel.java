package domain.news.sources;

import java.util.ArrayList;
import java.util.Arrays;

import domain.NewsSourceDataModel;

public class DailyMailDataModel extends NewsSourceDataModel {

	public DailyMailDataModel() {

		super.setTitleTag("//h1");
		super.setImageTag("//div[@class='mol-img']/img");
		super.setCaptionTag("//div[@class='artSplitter mol-img-group']/p[@class='imageCaption']");
		super.setTextTag("//div[@itemprop='articleBody']/p[@class='mol-para-with-font']");
		super.setPublicationDateTag("//time[@class='dateline']");
		super.setName("Daily Mail");
		super.setID("http://www.dailymail.co.uk/");
		super.setLang("en");
		super.setType("Newspaper");
		super.setMusthaveUrls(new ArrayList<String>(Arrays.asList("/news/article-")));
	}
}
