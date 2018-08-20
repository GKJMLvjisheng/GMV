package com.cascv.oas.core.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class UUIDUtils {

    private static final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
    
    // 获取UUID
    public static String getUUID(){
        return getRawUUID().replaceAll("-","");
    }

    // 获取原始UUID:df870a7a-dc58-11e5-b826-28b2bd440a9d
    public static String getRawUUID(){
        return timeBasedGenerator.generate().toString();
    }

    public static void main(String[] args) {
        String str = getUUID();
        System.out.println(str.substring(0,8));
    }

}
