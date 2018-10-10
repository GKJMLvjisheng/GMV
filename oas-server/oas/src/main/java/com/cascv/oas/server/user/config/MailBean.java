package com.cascv.oas.server.user.config;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class MailBean implements Serializable {
	   private static final long serialVersionUID = 1L;
	   @Getter@Setter private String rootMail;
	   @Getter@Setter private String password;
	   @Getter@Setter private String name;
	   @Getter@Setter private String subject;
}
