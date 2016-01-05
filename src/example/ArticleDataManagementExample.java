package example;

import org.apache.xpath.operations.String;

import domain.NewsSourceDataModel;
import domain.news.sources.NewYorkTimesDataModel;
import service.data.FileManager;

/**
 * This class represents an example about how to use the FileManager as
 * described in the README file.
 *
 * @see FileManager
 */
public class ArticleDataManagementExample {

	public static void main(String[] args) {

		FileManager fileManager = new FileManager();

		NewsSourceDataModel newYorkTimes = new NewYorkTimesDataModel();

		// extract all article and image URLs and save the data locally as
		// specified by the file paths
		fileManager.extractArticleTextAndImageFromFile("data/NYT-Output.jsonld", newYorkTimes, "data/NYT-articles/", "data/NYT-images/");

	}

}
