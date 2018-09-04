/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.mapper;

import java.util.List;

import com.cascv.oas.server.news.model.NewsModel;
import org.springframework.stereotype.Component;

@Component
public interface NewsModelMapper {
	 Integer insertNews(NewsModel newsModel);

	 Integer deleteNews(String newsId);
	 
	 Integer updateNews(NewsModel newsModel);
	 
	 List<NewsModel> selectAllNews();
}

