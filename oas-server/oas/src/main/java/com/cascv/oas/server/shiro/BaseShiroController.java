package com.cascv.oas.server.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.druid.support.json.JSONUtils;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.user.wrapper.LoginResult;
@Controller
public abstract class BaseShiroController {
    /**
      * 登录认证异常
     */
 /**   @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
        if (WebUtilsPro.isAjaxRequest(request)) {
            // 输出JSON
            Map<String,Object> map = new HashMap<>();
            map.put("code", "-999");
            map.put("message", "未登录");
            writeJson(map, response);
            return null;
        } else {
            return "redirect:/system/login";
        }
    }**/

    /**
     * 权限异常
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
//        if (WebUtilsPro.isAjaxRequest(request)) {
//    }
       Map<String,Object> info = new HashMap<>();
       info.put("code", "-998");
       info.put("message", "在当前时间段，XXX功能暂时无法使用。\n欲知详情，请参见社区公告或联系系统管理员：oases@oases.pro");
       writeJson(info, response);
//       return new ResponseEntity.Builder<>()
//               .setErrorCode(ErrorCode.GENERAL_ERROR).build();
       return null;
    }

    /**
     * 输出JSON
     *
     * @param response
     * @author SHANHY
     * @create 2017年4月4日
     */
   private void writeJson(Map<String,Object> map, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            //out.write(JsonUtil.mapToJson(map));
            out.write(JSONUtils.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    /**
     *  public static class WebUtilsPro {

      
         * 是否是Ajax请求
         *
         * @param request
         * @return
         * @author LVJISHENG
         * @create 2018.10.15
         */
      /**  public static boolean isAjaxRequest(HttpServletRequest request) {
            String requestedWith = request.getHeader("x-requested-with");
            if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
                return true;
            } else {
                return false;
            }
        }
    }**/
}
