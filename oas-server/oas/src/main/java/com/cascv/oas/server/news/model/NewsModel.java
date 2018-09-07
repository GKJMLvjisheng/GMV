/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.model;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

public class NewsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer newsId;
	@Getter @Setter private String newsTitle;
	@Getter @Setter private String newsAbstract;
	@Getter @Setter private String newsUrl;
	@Getter @Setter private String newsPicturePath;
}