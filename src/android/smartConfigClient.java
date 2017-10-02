package smartConfigClient;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.util.Log;
import com.example.client.util.NetWorkUtil;
//import com.example.smartconfigclientdemo.R;
import com.iflytek.smartconfig.client.SmartConfigClient;
import com.iflytek.smartconfig.listener.RecvListener;
import com.iflytek.smartconfig.message.NotifyMessage;


/**
 * This class echoes a string called from JavaScript.
 */
public class smartConfigClient extends CordovaPlugin {
	private SendThread mSendThread;
	private String mSSID;
	private String mPassword;
	private int mLocalIP;
	private CallbackContext callbackContextBack;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("initConfig")) {
			int packetInterval = args.getInt(0);
			int pecvTimeOut = args.getInt(1);
			SmartConfigClient.setPacketInterval(packetInterval);
			SmartConfigClient.setRecvTimeOut(pecvTimeOut);
			callbackContext.success();
			return true;
		}
		else if (action.equals("stopListen")) {
			SmartConfigClient.stopListen();
			callbackContext.success();
			return true;
		}
		else if(action.equals("startSendAndListen")) {
			callbackContextBack=callbackContext;
			String ssid = args.getString(0);
			String password = args.getString(1);
			int localIP = args.getInt(2);
			mSendThread = new SendThread(ssid, password, localIP);
			mSendThread.start();
			this.startListen();
			return true;
		}
		else if(action.equals("startListen")){
			this.startListen();
			return true;
		}
		return false;
	}

	public void startListen(){
		RecvListener recvListener = new RecvListener() {
			@Override
			public void onReceiveTimeOut() {
				JSONObject receiveTimeOut = new JSONObject();
				try{
					receiveTimeOut.put("type", "receiveTimeOut");
				} 
				catch(JSONException e){
					e.printStackTrace();
				}
				try{
					sendPluginResult(callbackContextBack,receiveTimeOut);
				} catch(Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onError(int errorCode) {
				JSONObject error = new JSONObject();
				try{
					error.put("type", "error");
					error.put("errorCode", errorCode);
				} 
				catch(JSONException e){
					e.printStackTrace();
				}
				try{
					sendPluginResult(callbackContextBack,error);
				} catch(Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onReceived(NotifyMessage message) {
				JSONObject received = new JSONObject();
				try{
					received.put("type", "received");
					received.put("mac", message.getMac());
					received.put("ip", message.getIp());
					received.put("port", message.getPort());
					received.put("HostName", message.getHostName());
				} 
				catch(JSONException e){
					e.printStackTrace();
				}
				try{
					sendPluginResult(callbackContextBack,received);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		SmartConfigClient.startListen(recvListener);
	}

	public void sendPluginResult(CallbackContext callbackContext,JSONObject obj){
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
		pluginResult.setKeepCallback(true);
		callbackContext.sendPluginResult(pluginResult);
	}

	class SendThread extends Thread {
		public SendThread(String ssid, String password, int localIP) {
			mSSID = ssid;
			mPassword = password;
			mLocalIP=localIP;
		}
		public SendThread(){}
		
		private boolean mStopRun = false;

		public void stopRun() {
			mStopRun = true;
			interrupt();
		}

		@Override
		public void run() {
			while (!mStopRun) {
				try{
					SmartConfigClient.send(mSSID, mPassword, mLocalIP);
				} catch (Exception e) {
					//Log.e(TAG, "Exception when send smartconfig info");
				}
			}
		}
	}
}


