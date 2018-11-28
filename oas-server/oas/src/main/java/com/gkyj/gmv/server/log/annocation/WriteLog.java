package com.gkyj.gmv.server.log.annocation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteLog {
  String value();
}
