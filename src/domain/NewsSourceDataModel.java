package domain;

import java.util.List;

/**
 * The NewsSourceDataModel defined the data specific to each news publisher such 
 * as name, id as well as the XPath to the to-be-extracted attributes. This class 
 * is meant to be either be extended for each news publisher like it is done in
 * domain.news.sources or be instantiated whereby all the attributes can be set 
 * with the setter methods. 
 */
public class NewsSourceDataModel {

	private String name;
	private String ID;
	private String lang;

	private String titleTag;
	private String textTag;

	private String imageTag;
	private String captionTag;

	private String publicationDateTag;
	private String type;

	private List<String> exceptionUrls;

	private List<String> musthaveUrls;

	public List<String> getMusthaveUrls() {
		return musthaveUrls;
	}

	public void setMusthaveUrls(List<String> musthaveUrls) {
		this.musthaveUrls = musthaveUrls;
	}

	public List<String> getExceptionUrls() {
		return exceptionUrls;
	}

	public void setExceptionUrls(List<String> exceptionUrls) {
		this.exceptionUrls = exceptionUrls;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTitleTag() {
		return titleTag;
	}

	public void setTitleTag(String titleTag) {
		this.titleTag = titleTag;
	}

	public String getTextTag() {
		return textTag;
	}

	public void setTextTag(String textTag) {
		this.textTag = textTag;
	}

	public String getImageTag() {
		return imageTag;
	}

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public String getCaptionTag() {
		return captionTag;
	}

	public void setCaptionTag(String captionTag) {
		this.captionTag = captionTag;
	}

	public String getPublicationDateTag() {
		return publicationDateTag;
	}

	public void setPublicationDateTag(String publicationDateTag) {
		this.publicationDateTag = publicationDateTag;
	}
}