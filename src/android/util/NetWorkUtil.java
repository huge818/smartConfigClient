package com.example.client.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * wifi相关工具类
 * 
 * @author <a href="http://www.xfyun.cn">讯飞开放平台</a>
 * @date 2016年12月26日 上午9:40:06
 *
 */
public class NetWorkUtil {

	/**
	 * 获取设备IP
	 * @param context
	 * @return ip(int型)
	 */
	public static int getLocalIP(Context context) {
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();

		return wifiInfo.getIpAddress();
	}

	/**
	 * 获取路由SSID
	 * 
	 * @param context
	 * @return ssid
	 */
	public static String getSSID(Context context) {
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();

		//replace过滤非常重要，否则服务端无法接收正确SSID
		return wifiInfo.getSSID().replace("\"", "");
	}
}
