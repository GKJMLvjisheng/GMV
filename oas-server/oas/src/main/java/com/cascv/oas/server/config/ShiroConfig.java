package com.cascv.oas.server.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import com.cascv.oas.server.filter.CaptchaValidateFilter;
import com.cascv.oas.server.filter.KickoutSessionControlFilter;
import com.cascv.oas.server.filter.LogoutFilter;
import com.cascv.oas.server.filter.OnlineSessionFilter;
import com.cascv.oas.server.filter.SyncOnlineSessionFilter;
import com.cascv.oas.server.session.OnlineWebSessionManager;
import com.cascv.oas.server.shiro.OnlineSessionDAO;
import com.cascv.oas.server.shiro.OnlineSessionFactory;
import com.cascv.oas.server.shiro.UserRealm;
import com.cascv.oas.core.utils.StringUtils;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

@Configuration
public class ShiroConfig {
	 public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	    // Session超时时间，单位为毫秒（默认30分钟）
	    @Value("${shiro.session.expireTime}")
	    private int expireTime;

	    @Value("${shiro.session.sessionCount}")
	    private Integer sessionCount;
	    
	    
	    @Value("${shiro.session.kickoutUrl}")
	    private String kickoutUrl;
	    // 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
	    @Value("${shiro.session.validationInterval}")
	    private int validationInterval;

	    @Value("${shiro.cache.prefix}")
	    private String cachePrefix;
	    // 验证码开关
	    @Value("${shiro.user.captchaEbabled}")
	    private boolean captchaEbabled;

	    // 验证码类型
	    @Value("${shiro.user.captchaType}")
	    private String captchaType;

	    // 设置Cookie的域名
	    @Value("${shiro.cookie.domain}")
	    private String domain;

	    // 设置cookie的有效访问路径
	    @Value("${shiro.cookie.path}")
	    private String path;

	    // 设置HttpOnly属性
	    @Value("${shiro.cookie.httpOnly}")
	    private boolean httpOnly;

	    // 设置Cookie的过期时间，秒为单位
	    @Value("${shiro.cookie.maxAge}")
	    private int maxAge;

	    // 登录地址
	    @Value("${shiro.user.loginUrl}")
	    private String loginUrl;

	    // 权限认证失败地址
	    @Value("${shiro.user.unauthorizedUrl}")
	    private String unauthorizedUrl;

	    
	    // * 缓存管理器 使用Ehcache实现
	    @Bean
	    public EhCacheManager getEhCacheManager()  {
	        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("app");
	        EhCacheManager em = new EhCacheManager();
	        if (StringUtils.isNull(cacheManager)) {
	            em.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
	            return em;
	        } else {
	            em.setCacheManager(cacheManager);
	            return em;
	        }
	    }

	    
	    // 自定义Realm
	    @Bean
	    public UserRealm userRealm(EhCacheManager cacheManager) {
	        UserRealm userRealm = new UserRealm();
	        userRealm.setCacheManager(cacheManager);
	        return userRealm;
	    }

	    // 自定义sessionDAO会话
	    @Bean
	    public OnlineSessionDAO sessionDAO() {
	        OnlineSessionDAO sessionDAO = new OnlineSessionDAO();
	        return sessionDAO;
	    }

	    // 自定义sessionFactory会话
	    @Bean
	    public OnlineSessionFactory sessionFactory() {
	        OnlineSessionFactory sessionFactory = new OnlineSessionFactory();
	        return sessionFactory;
	    }

    
	    // session manager
	    @Bean
	    public OnlineWebSessionManager sessionValidationManager()
	    {
	        OnlineWebSessionManager manager = new OnlineWebSessionManager();
	        // 加入缓存管理器
	        manager.setCacheManager(getEhCacheManager());
	        // 删除过期的session
	        manager.setDeleteInvalidSessions(true);
	        // 设置全局session超时时间
	        manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
	        // 去掉 JSESSIONID
	        manager.setSessionIdUrlRewritingEnabled(false);
	        // 是否定时检查session
	        manager.setSessionValidationSchedulerEnabled(true);
	        // 自定义SessionDao
	        manager.setSessionDAO(sessionDAO());
	        // 自定义sessionFactory
	        manager.setSessionFactory(sessionFactory());
	        return manager;
	    }

	
	// 会话管理器
	@Bean
	public OnlineWebSessionManager sessionManager()	{
		OnlineWebSessionManager manager = new OnlineWebSessionManager();
		// cache
		manager.setCacheManager(getEhCacheManager());
		// invalidate session
		manager.setDeleteInvalidSessions(true);
		// global session timeout
		manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
		// remove JSESSIONID
		manager.setSessionIdUrlRewritingEnabled(false);
		// 是否定时检查session
		manager.setSessionValidationSchedulerEnabled(true);
		// customized SessionDao
		manager.setSessionDAO(sessionDAO());
		// customized sessionFactory
		manager.setSessionFactory(sessionFactory());
		return manager;
	}

	    
	// 安全管理器
	@Bean
	public SecurityManager securityManager(UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(userRealm);
		// 记住我
		securityManager.setRememberMeManager(rememberMeManager());
		// 注入缓存管理器;
		securityManager.setCacheManager(getEhCacheManager());
		// session管理器
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	    
	// 退出过滤器
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setLoginUrl(loginUrl);
		return logoutFilter;
	}

	    
	// Shiro filter
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);	
		shiroFilterFactoryBean.setLoginUrl(loginUrl);	        	
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);	
		// Shiro连接约束配置，即过滤链的定义
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 对静态资源设置匿名访问
		filterChainDefinitionMap.put("/favicon.ico**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/image/**", "anon");
		filterChainDefinitionMap.put("/ajax/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/web3j/**", "anon");
		filterChainDefinitionMap.put("/logout", "logout");
		
		//让微信接口免登录访问
		filterChainDefinitionMap.put("/api/v1/wx/**", "anon");
		//验证码接口免登录访问
		filterChainDefinitionMap.put("/api/v1/userCenter/getCode", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/contrastCode", "anon");
		//忘记密码接口免登陆访问
		filterChainDefinitionMap.put("/api/v1/userCenter/inquireUserInfoByMobile", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/resetPassword", "anon");
		
		filterChainDefinitionMap.put("/api/v1/userCenter/login", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/register", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/registerConfirm", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/inquireName", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/mobileCheckCode", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/sendMobile", "anon");
		filterChainDefinitionMap.put("/api/v1/userCenter/resetPassword", "anon");
		//filterChainDefinitionMap.put("/api/v1/ethWallet/selectContractSymbol", "anon");
		filterChainDefinitionMap.put(loginUrl, "anon");		
		
		Map<String, Filter> filters = new LinkedHashMap<>();
		filters.put("onlineSession", onlineSessionFilter());
		filters.put("syncOnlineSession", syncOnlineSessionFilter());

		filters.put("logout", logoutFilter());
		filters.put("kickout", kickoutSessionControlFilter());
		shiroFilterFactoryBean.setFilters(filters);


		filterChainDefinitionMap.put("/**", "user,onlineSession,syncOnlineSession");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	 @Bean
	  public KickoutSessionControlFilter kickoutSessionControlFilter() {
	    EhCacheManager cacheManager = getEhCacheManager();
	    KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
	    // 
	    kickoutSessionControlFilter.setCache(cacheManager.getCache(cachePrefix));
	    
	    kickoutSessionControlFilter.setSessionManager(sessionManager());
	    
	    // kickout the one come after, default is false, that is kickout the one before this login user
	    kickoutSessionControlFilter.setMaxSession(sessionCount);  // max session for one user
	    kickoutSessionControlFilter.setKickoutUrl(kickoutUrl);    // kickout to url
	    return kickoutSessionControlFilter;
	  }
	 
	// * 自定义在线用户处理过滤器
	@Bean
	public OnlineSessionFilter onlineSessionFilter()  {
		OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
		onlineSessionFilter.setLoginUrl(loginUrl);
		return onlineSessionFilter;
	}

	// * 自定义在线用户同步过滤器
	@Bean
	public SyncOnlineSessionFilter syncOnlineSessionFilter()  {
		SyncOnlineSessionFilter syncOnlineSessionFilter = new SyncOnlineSessionFilter();
		return syncOnlineSessionFilter;
	}

	// captcha filter
	@Bean
	public CaptchaValidateFilter captchaValidateFilter() {
		CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
		captchaValidateFilter.setCaptchaEbabled(captchaEbabled);
		captchaValidateFilter.setCaptchaType(captchaType);
		return captchaValidateFilter;
	}

	    
	// cookie 属性设置
	public SimpleCookie rememberMeCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge * 24 * 60 * 60);
		return cookie;
	}

	    
	// rememberMe
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
		return cookieRememberMeManager;
	}

	    
	// thymeleaf engine and shiro framework
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

		
		
	// 开启Shiro注解通知器
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
