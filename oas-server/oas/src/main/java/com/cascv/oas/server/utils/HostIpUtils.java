package com.cascv.oas.server.utils;
<<<<<<< HEAD

=======
>>>>>>> aa491c7b962e094363c24e14e5e24597762ea1ae
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;    
public class HostIpUtils{  
<<<<<<< HEAD
  @SuppressWarnings("unused")
  public static String getHostIp(){
    try{
      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
      while (allNetInterfaces.hasMoreElements()){
        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements()){
          InetAddress ip = (InetAddress) addresses.nextElement();
          if (ip != null 
              && ip instanceof Inet4Address
                        && !ip.isLoopbackAddress() 
                        && ip.getHostAddress().indexOf(":")==-1){
            System.out.println("LocalHostIP = " + ip.getHostAddress());
            return ip.getHostAddress();
          } 
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }
=======
	@SuppressWarnings("unused")
	public static String getHostIp(){
		try{
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()){
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()){
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null 
							&& ip instanceof Inet4Address
                    		&& !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                    		&& ip.getHostAddress().indexOf(":")==-1){
						System.out.println("本机的IP = " + ip.getHostAddress());
						return ip.getHostAddress();
					} 
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
>>>>>>> aa491c7b962e094363c24e14e5e24597762ea1ae

    }  