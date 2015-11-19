package nl.cwi.domain;

import java.util.List;

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