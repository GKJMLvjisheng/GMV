package com.cascv.oas.server.timezone.model;
/**
 * @author Ming Yang
 */
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class CountryPromaryModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer uuid;
	@Getter @Setter private String timeZone;
	@Getter @Setter private String countryName;
	@Getter @Setter private String promaryName;
}