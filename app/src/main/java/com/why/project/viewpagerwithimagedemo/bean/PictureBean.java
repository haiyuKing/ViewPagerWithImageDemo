package com.why.project.viewpagerwithimagedemo.bean;

/**
 * Created by HaiyuKing
 * Used
 */

public class PictureBean {
	//图片ID值
	private String picId = "";
	//图片的res id值【这里模拟的是图片的url地址，所以使用的res id值进行代替】
	private int picResId = 0;
	//图片标题
	private String picTitle = "";

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public int getPicResId() {
		return picResId;
	}

	public void setPicResId(int picResId) {
		this.picResId = picResId;
	}

	public String getPicTitle() {
		return picTitle;
	}

	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}
}
