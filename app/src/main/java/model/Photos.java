package model;

import java.io.Serializable;


public class Photos  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7360577954824679170L;
	private String id;
	private String ImgUrl;
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Photos(String id, String imgUrl) {
		this.id = id;
		ImgUrl = imgUrl;
	}
}
