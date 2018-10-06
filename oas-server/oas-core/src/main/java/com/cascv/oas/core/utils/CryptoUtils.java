package com.cascv.oas.core.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptoUtils {
  public static String DES = "AES"; // optional value AES/DES/DESede

  public static String CIPHER_ALGORITHM = "AES"; // optional value AES/DES/DESede


  public static Key getKey(String strKey) {
      try {
          if (strKey == null) {
              strKey = "";
          }
          KeyGenerator _generator = KeyGenerator.getInstance("AES");
          SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
          secureRandom.setSeed(strKey.getBytes());
          _generator.init(128, secureRandom);
          return _generator.generateKey();
      } catch (Exception e) {
          throw new RuntimeException("exception during initialization ");
      }
  }

  public static String encrypt(String data, String key)  {
    try {
      SecureRandom sr = new SecureRandom();
      Key secureKey = getKey(key);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
      byte[] bt = cipher.doFinal(data.getBytes());
      String strS = Base64.encodeBase64String(bt);
      return strS;
    } catch (Exception e) {
      return null;
    }
  }


  public static String decrypt(String message, String key) {
    try {
      SecureRandom sr = new SecureRandom();
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      Key secureKey = getKey(key);
      cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
      byte[] res = Base64.decodeBase64(message);
      res = cipher.doFinal(res);
      return new String(res);
    } catch (Exception e) {
      return null;
    }
  }

  public static void test_encrypto() throws Exception {
      String message = "123456";
      String key = "landLeaf";
      String encryptMsg = encrypt(message, key);
      log.info("encrypt:{}", encryptMsg);
  }
  
  public static void test_decrypto() throws Exception{
    String encryptMsg = "QLNYZyjRnKF/zxAjzDt/lw==";
    String key = "landLeaf";
    String decryptedMsg = decrypt(encryptMsg, key);
    log.info("decrypted:{}", decryptedMsg);

    
  }
}
