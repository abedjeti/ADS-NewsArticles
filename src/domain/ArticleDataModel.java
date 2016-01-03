package domain;

/**
 * The ArticleDataModel class contains the metadata that can be 
 * extracted from an article page.
 */
public class ArticleDataModel {
	
	private String title;

	private String image;
	private String caption;

	private String image2;
	private String caption2;

	private String video;
	private String videoCaption;

	private String text;
	private String publicationDate;

	private String id;
	private String url;

	private TextRazorDataModel textRazorData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TextRazorDataModel getTextRazorData() {
		return textRazorData;
	}

	public void setTextRazorData(TextRazorDataModel textRazorData) {
		this.textRazorData = textRazorData;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getCaption2() {
		return caption2;
	}

	public void setCaption2(String caption2) {
		this.caption2 = caption2;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getVideoCaption() {
		return videoCaption;
	}

	public void setVideoCaption(String videoCaption) {
		this.videoCaption = videoCaption;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}