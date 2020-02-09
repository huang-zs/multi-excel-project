package com.zs.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * 门户图片
 * @author MagicBook
 *
 */
public class DoorImage {
	//标题
	private String name;
	//图片路径
	private String url;
	//图片描述
	private String content;
	//图片顺序
	private String imageIndex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageIndex() {
		return imageIndex;
	}
	public void setImageIndex(String imageIndex) {
		this.imageIndex = imageIndex;
	}
	@Override
	public String toString() {
		return "DoorImage [name=" + name + ", url=" + url + ", content=" + content + ", imageIndex=" + imageIndex + "]";
	}
	
	
	
}
