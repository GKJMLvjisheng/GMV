package com.cascv.oas.server.blockchain.service;

import java.io.File;


import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KeyStoreService {
	
	String path=System.getProperties().getProperty("user.home")+File.separator+"keystore";
	
	public KeyStoreService(){
		File directory = new File(path);
		try {
			FileUtils.forceMkdir(directory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public File keyFile(String user) {
		return new File(path+File.separator+user);
	}
	public void saveKey(String user, String key) {
		File file = this.keyFile(user);
		this.destroyKey(user);
		try {
			log.info("save key to {}", user);
			FileUtils.writeStringToFile(file, key, "utf-8");
		} catch (Exception e) {
			
		}
	}
	
	public String loadKey(String user) {
		String key = null;
		try {
			key= FileUtils.readFileToString(keyFile(user),"utf-8");
		} catch (Exception e) {
			key = null;
		}
		return key;
	}
	
	public void destroyKey(String user) {
		log.info("destroy key from {}", user);
		FileUtils.deleteQuietly(this.keyFile(user));
	}
}
