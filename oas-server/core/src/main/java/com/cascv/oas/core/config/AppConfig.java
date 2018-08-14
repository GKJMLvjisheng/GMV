package com.cascv.oas.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


// 读取项目相关配置
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig
{
    
    @Setter @Getter private String name;     			/** 项目名称 */
    @Setter @Getter private String version;  			/** 版本 */
    @Setter @Getter private String copyrightYear;     	/** 版权年份 */
    private static String profile;    					/** 上传路径 */
    private static boolean addressEnabled;				/** 获取地址开关 */

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        AppConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        AppConfig.addressEnabled = addressEnabled;
    }

}
