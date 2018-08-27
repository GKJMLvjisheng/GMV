package com.cascv.oas.core.utils;


public class InviteCodeUtils {
  public static final String SA="01456pq23rsvtuCDwxy78NOP9ABEFQRSTUGHIJKLMVWefgXYZabcdhijklmnoz";
  
  public static String getFromUuid(String uuid)
  {
    int length = uuid.length(); 
    if (length < 32)
      return "0";
    StringBuffer stringBuffer = new StringBuffer();
    for (int i=0; i < 6; i++) {
      String substr = uuid.substring(length-30 + i * 5, length-25 + i * 5);
      int index = Integer.parseInt(substr, 16) % SA.length();
      stringBuffer.append(SA.charAt(index));
    }
    return stringBuffer.toString();
  }
}
