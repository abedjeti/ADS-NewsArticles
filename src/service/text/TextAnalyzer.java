package service.text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;
import com.textrazor.annotations.Topic;

import domain.TextRazorDataModel;

/**
 * This class uses TextRazor API (http://www.textrazor.com) to analyze article texts
 * and extract topics, coarse topics and entities. Make sure you install the TextRazor 
 * Java SDK and get an API. Beware that you might have a request limit based on your 
 * API key.
 * 
 *@see TextRazorDataModel
 */
public class TextAnalyzer {

	/**
	 * The API Key for accessing TextRazor. It is a free key that supports only
	 * max. 500 requests per day. Insert your own key there
	 */
	private static String TEXTRAZORAPI = "YOUR_TEXTRAZOR_API_KEY";

	public static final String ENTITIES = "entities";
	public static final String TOPICS = "topics";

	public static List<String> extractors;
	public static TextRazor razor;

	public static final double ENTITY_RELEVANCE_THRESHOLD = 1.0;
	public static final double ENTITY_CONFIDENCE_THRESHOLD = 1.0;
	public static final double TOPIC_RELEVANCE_THRESHOLD = 1.0;

	public TextAnalyzer() {
		razor = new TextRazor(TEXTRAZORAPI);
		/*
		 * Extractors are needed to tell TextRazor what information to deliver
		 * back, but they have an influence on the performance. make sure that
		 * all the extractors that are set are really needed.
		 */
		extractors = new ArrayList<String>();
		extractors.add(ENTITIES);
		extractors.add(TOPICS);

		razor.setExtractors(extractors);
		razor.setDoEncryption(true);
	}

	/**
	 * Analyses a piece of text as string with TextRazor
	 * 
	 * @param text the article text to be analyzed
	 * @return the extracted data stored in TextRazorDataModel
	 */
	public TextRazorDataModel analyzeText(String text) {
		try {
			if (text != null && !text.isEmpty()) {
				AnalyzedText analyzedText = razor.analyze(text);
				return extractTextRazorData(analyzedText);
			}
		} catch (NetworkException e) {
			System.err.println("Could not connect to TextRazor " + e.getMessage());
		} catch (AnalysisException e) {
			System.err.println("Could not analyze text " + e.getMessage());
		}
		return null;
	}

	/**
	 * Extracts topics and entities from an AnalyzedText object which is typically
	 * returned from TextRazor anlysis methods, and stores them in a TextRazorDataModel
	 * 
	 * @param analyzedText the AnalyzedText object contained the analyzed data returned from TextRazor
	 * @return the extracted data stored in TextRazorDataModel
	 */
	private TextRazorDataModel extractTextRazorData(AnalyzedText analyzedText) {

		Set<String> coarseTopics = new HashSet<String>();
		Set<String> topics = new HashSet<String>();

		List<Topic> coarseTopicsList = analyzedText.getResponse().getCoarseTopics();
		List<Topic> topicsList = analyzedText.getResponse().getTopics();
		List<Entity> entityList = analyzedText.getResponse().getEntities();

		// entities are also considered topics
		if (entityList != null) {
			for (Entity entity : entityList) {
				if (entity.getRelevanceScore() > ENTITY_RELEVANCE_THRESHOLD) {
					topics.add(entity.getWikiLink());
				}
			}
		}

		// topics
		if (topicsList != null) {
			for (Topic topic : topicsList) {
				if (topic.getScore() >= TOPIC_RELEVANCE_THRESHOLD) {
					topics.add(topic.getWikiLink());
				}
			}
		}

		// coarse topics
		if (coarseTopicsList != null) {
			for (Topic topic : coarseTopicsList) {
				coarseTopics.add(topic.getWikiLink());
			}
		}

		return new TextRazorDataModel(coarseTopics, topics);
	}
}
