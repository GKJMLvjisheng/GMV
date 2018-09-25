package com.cascv.oas.server.verson.model;
/**
 * @author Ming Yang
 */
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class VersionModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String uuid;
	@Getter @Setter private String versionCode;
	@Getter @Setter private String appUrl;
	@Getter @Setter private String created;
}