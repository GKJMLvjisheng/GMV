package com.cascv.oas.server.shiro;

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
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.PermService;
import com.cascv.oas.server.user.service.RoleService;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j
public class UserRealm extends AuthorizingRealm
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermService permService;

    /**
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        Long userId = ShiroUtils.getUserId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 
        info.setRoles(roleService.getRolesByUserId(userId));
        // 
        info.setStringPermissions(permService.getPermsByUserId(userId));
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
        log.info("c");
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
        catch (UserNotExistsException e)
        {
        	log.info("f");
            throw new UnknownAccountException(e.getMessage(), e);
        }
        catch (UserPasswordNotMatchException e)
        {
        	log.info("g");
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
        catch (UserPasswordRetryLimitExceedException e)
        {
        	log.info("h");
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        }
        catch (UserBlockedException e)
        {
        	log.info("i");
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (RoleBlockedException e)
        {
        	log.info("j");
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (Exception e)
        {
            throw new AuthenticationException(e.getMessage(), e);
        }
        log.info("k");
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userModel, password, getName());
        log.info("l");
        return info;
    }

    /**
     * 娓呯悊缂撳瓨鏉冮檺
     */
    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
