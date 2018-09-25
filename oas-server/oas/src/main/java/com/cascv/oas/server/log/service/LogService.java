package com.cascv.oas.server.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.log.mapper.LogModelMapper;
import com.cascv.oas.server.log.model.LogModel;

@Service
public class LogService {
  @Autowired
  private LogModelMapper logModelMapper;
  
  public Integer addLog(LogModel logModel) {
      return logModelMapper.insertSelective(logModel);
  }
}
