package example;

import org.apache.xpath.operations.String;

import domain.NewsSourceDataModel;
import domain.news.sources.NewYorkTimesDataModel;
import service.scraper.NewsArticleScraper;

/**
 * This class represents an example about how to use the NewsArticleScraper as
 * described in the README file.
 *
 * @see NewsArticleScraper
 */
public class DatasetGenerationExample {

	public static void main(String[] args) {

		NewsArticleScraper scraper = new NewsArticleScraper();

		NewsSourceDataModel newYorkTimes = new NewYorkTimesDataModel();

		// extract all metadata for each of the URLs in NYT-Input.csv and save
		// it in NYT-Output.jsonld
		scraper.extractArticleFromFile("data/NYT-Input.csv", "data/NYT-Output.jsonld", newYorkTimes, true);

	}

}
