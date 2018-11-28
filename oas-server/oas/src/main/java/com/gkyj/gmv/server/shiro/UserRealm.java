package com.gkyj.gmv.server.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.cascv.oas.core.exception.CaptchaException;
import com.cascv.oas.core.exception.RoleBlockedException;
import com.cascv.oas.core.exception.UserBlockedException;
import com.cascv.oas.core.exception.UserNotExistsException;
import com.cascv.oas.core.exception.UserPasswordNotMatchException;
import com.cascv.oas.core.exception.UserPasswordRetryLimitExceedException;
import com.gkyj.gmv.server.user.model.UserModel;
import com.gkyj.gmv.server.user.service.PermService;
import com.gkyj.gmv.server.user.service.RoleService;
import com.gkyj.gmv.server.user.service.UserService;
import com.gkyj.gmv.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermService permService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        String userUuid = ShiroUtils.getUserUuid();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 
        info.setRoles(roleService.getRolesByUserUuid(userUuid));
        // 
        info.setStringPermissions(permService.getPermsByUserUuid(userUuid));
        return info;
    }

    // 
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
    	
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";
      
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }
        UserModel userModel = null;
        try  {
        	userModel = userService.findUserByName(username);
        	String calcPassword=new Md5Hash(username + password + userModel.getSalt()).toHex().toString();
        	log.info("password {} calc {}", userModel.getPassword(), calcPassword);
        	log.info("name {} password {} salt {}", username, password, userModel.getSalt());
        	if (!userModel.getPassword().equals(calcPassword)) {
        		throw new UserNotExistsException();
        	}
        } catch (CaptchaException e) {
           throw new AuthenticationException(e.getMessage(), e);
        }
        catch (UserNotExistsException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        }
        catch (UserPasswordNotMatchException e) {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
        catch (UserPasswordRetryLimitExceedException e) {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        }
        catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (RoleBlockedException e)  {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (Exception e)   {
            throw new AuthenticationException(e.getMessage(), e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userModel, password, getName());
        return info;
    }

    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
