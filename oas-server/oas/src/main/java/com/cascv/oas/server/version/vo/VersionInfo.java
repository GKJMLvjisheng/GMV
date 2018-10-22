package com.cascv.oas.server.version.vo;
/**
 * @author Ming Yang
 */
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class VersionInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer uuid;
	@Getter @Setter private Integer versionCode;
	@Getter @Setter private String appUrl;
	@Getter @Setter private String versionStatus;
	@Getter @Setter private Integer upGradeStatus;
}