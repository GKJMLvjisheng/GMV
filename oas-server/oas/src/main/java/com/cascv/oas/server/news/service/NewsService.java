/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.server.news.mapper.NewsModelMapper;
import com.cascv.oas.server.news.model.NewsModel;


@Service
public class NewsService {
	@Autowired
	private NewsModelMapper newsModelMapper;
	
	public Integer addNews(NewsModel newsModel) 
	{
		
		return newsModelMapper.insertNews(newsModel);
		
	}
	
	public Integer deleteNews(String newsId) 
	{
		
		return newsModelMapper.deleteNews(newsId);
	}
	
	public Integer updateNews(NewsModel newsModel)
	{
		
		return newsModelMapper.updateNews(newsModel);
		
	}
	
	public List<NewsModel> selectAllNews() 
	{
		
		return newsModelMapper.selectAllNews();
		
	}
	
}