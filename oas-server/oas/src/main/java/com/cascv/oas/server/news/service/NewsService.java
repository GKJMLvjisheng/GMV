/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.news.mapper.NewsModelMapper;
import com.cascv.oas.server.news.model.NewsModel;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsService {
	@Autowired
	private NewsModelMapper newsModelMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	@Autowired
  private MediaServer mediaServer;
	
	public Integer addNews(NewsModel newsModel) 
	{
		
		return newsModelMapper.insertNews(newsModel);
		
	}
	
	public ErrorCode deleteNews(Integer newsId) 
	{
		
		newsModelMapper.deleteNews(newsId);
		return ErrorCode.SUCCESS;
	}
	
	public Integer updateNews(NewsModel newsModel)
	{
		
		return newsModelMapper.updateNews(newsModel);
		
	}
	
	public List<NewsModel> selectAllNews() 
	{
		List<NewsModel> newsModelList = newsModelMapper.selectAllNews();
		for (NewsModel newsModel : newsModelList) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
			String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, newsModel.getCreated(), dstFormater, dstTimeZoneId);
			newsModel.setCreated(created);
			log.info("newCreated={}",created);
		  }
		return newsModelList;

		
	}
	
	public List<NewsModel> selectPage(Integer offset,  Integer limit){
	  List<NewsModel> newsModelList = newsModelMapper.selectPage(offset, limit);
	  for (NewsModel newsModel : newsModelList) {
	    String fullLink = mediaServer.getImageHost() + newsModel.getNewsPicturePath();
	    newsModel.setNewsPicturePath(fullLink);
	  }
	  return newsModelList;
	}
	
	public Integer countTotal() {
	  return newsModelMapper.countTotal();
	}
}