package com.cascv.oas.server.shiro.model;

import org.springframework.stereotype.Service;
import lombok.Data;

@Data
@Service
public class SysPermissionInit {
       private String url;
       private String  permissionInit;
       private Integer sort;
}
