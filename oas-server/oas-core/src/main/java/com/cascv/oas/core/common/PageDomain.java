package com.cascv.oas.core.common;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PageDomain<T> implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer pageNum;
  @Getter @Setter private Integer pageSize;
  @Getter @Setter private String orderByColumn;
  @Getter @Setter private String asc;
  @Getter @Setter private Integer total;
  @Getter @Setter private Integer totalOfLast;
  @Getter @Setter private String msg;
  @Getter @Setter private Integer offset;
  @Getter @Setter private String searchValue;//搜索关键词
  @Getter @Setter private String startTime;
  @Getter @Setter private String endTime;
  @Getter @Setter private Integer number;
  @Getter @Setter private String time;
  @Getter @Setter private List<T> rows;
}
