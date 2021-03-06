package com.cascv.oas.core.common;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PageIODomain<T> implements Serializable {
	  private static final long serialVersionUID = 1L;
	  @Getter @Setter private Integer pageNum;
	  @Getter @Setter private Integer pageSize;
	  @Getter @Setter private String orderByColumn;
	  @Getter @Setter private String asc;
	  @Getter @Setter private Integer total;
	  @Getter @Setter private Integer offset;
	  @Getter @Setter private List<T> rows;
	  @Getter @Setter private Integer inOrOut;
	}