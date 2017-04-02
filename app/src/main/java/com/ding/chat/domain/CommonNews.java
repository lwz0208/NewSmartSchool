package com.ding.chat.domain;


import java.io.Serializable;

public class CommonNews implements Serializable {

	private String id;
	private String title;
	private String imageurl1;// 新闻缩略图
	private String content;
	private String datetime;
	private String DetailUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageurl1() {
		return imageurl1;
	}

	public void setImageurl1(String imageurl1) {
		this.imageurl1 = imageurl1;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDetailUrl() {
		return DetailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		DetailUrl = detailUrl;
	}
}
