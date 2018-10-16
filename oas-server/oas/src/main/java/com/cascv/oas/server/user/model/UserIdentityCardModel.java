package com.cascv.oas.server.user.model;
/**
 * @author Ming Yang
 */
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
public class UserIdentityCardModel implements Serializable {
	    private static final long serialVersionUID = 1L;
	    @Getter @Setter private Integer uuid;
	    @Getter @Setter private String userName;
	    @Getter @Setter private String userIdentityName;
	    @Getter @Setter private String userIdentityNumber;
	    @Getter @Setter private String frontOfPhoto;
	    @Getter @Setter private String backOfPhoto;
	    @Getter @Setter private String holdInHand;
	    @Getter @Setter private String remark;
	    @Getter @Setter private Integer verifyStatus;
	    @Getter @Setter private String created;
	    @Getter @Setter private String updated;

}
