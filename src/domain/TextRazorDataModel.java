package domain;

import java.util.Set;

/**
 * The TextRazorDataModel contains the topics and entities extracted from TextRazor.
 */
public class TextRazorDataModel {

	private Set<String> coarseTopics;
	private Set<String> topics;

	public TextRazorDataModel(Set<String> coarseTopics, Set<String> topics) {
		this.coarseTopics = coarseTopics;
		this.topics = topics;
	}

	public Set<String> getCoarseTopics() {
		return coarseTopics;
	}

	public void setCoarseTopics(Set<String> coarseTopics) {
		this.coarseTopics = coarseTopics;
	}

	public Set<String> getTopics() {
		return topics;
	}

	public void setTopics(Set<String> topics) {
		this.topics = topics;
	}

}