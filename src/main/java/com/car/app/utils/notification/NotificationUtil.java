package com.car.app.utils.notification;

import org.json.JSONArray;
import org.json.JSONObject;


public class NotificationUtil {
	private static String appkey = "5b8b68def43e486f4b0000e4";
	private static String appMasterSecret = "s61u1deg7ccj5iafuptdstliozmczsfu";
	private String timestamp = null;
	private static PushClient client = new PushClient();
	
	public NotificationUtil(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(appkey,appMasterSecret);

		broadcast.setAlert("IOS 广播测试");
		broadcast.setBadge( 0);
		broadcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		broadcast.setTestMode();
		// Set customized fields
		broadcast.setCustomizedField("test", "helloworld");
		client.send(broadcast);
	}
	
	public static void sendIOSUnicast(String deviceToken, String name) throws Exception {
		IOSUnicast unicast = new IOSUnicast(appkey,appMasterSecret);
		// TODO Set your device token
//		unicast.setDeviceToken("ed2cd1ddca6e632afee8bd329a700fef948af3afd6dc50df1558fba9ffed3d41");
		unicast.setDeviceToken(deviceToken);
		if(name == null) {
			unicast.setAlert("您的車罩被移動了");
		}else {
			unicast.setAlert("您的好友（" + name+ "）的車罩被移動了");
		}
		unicast.setBadge( 0);
		unicast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		unicast.setTestMode();
		// Set customized fields
//		unicast.setCustomizedField("test", "helloworld");
		client.send(unicast);
	}
	
	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast(appkey,appMasterSecret);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"iostest"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", "iostest");
		tagArray.put(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());
		
		// Set filter condition into rootJson
		groupcast.setFilter(filterJson);
		groupcast.setAlert("IOS 组播测试");
		groupcast.setBadge( 0);
		groupcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		groupcast.setTestMode();
		client.send(groupcast);
	}
	
	public void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey,appMasterSecret);
		// TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setAlert("IOS 个性化测试");
		customizedcast.setBadge( 0);
		customizedcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		customizedcast.setTestMode();
		client.send(customizedcast);
	}
	
	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast(appkey,appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens 
		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
		filecast.setFileId( fileId);
		filecast.setAlert("IOS 文件播测试");
		filecast.setBadge( 0);
		filecast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		filecast.setTestMode();
		client.send(filecast);
	}
	
	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		NotificationUtil demo = new NotificationUtil("5b8b68def43e486f4b0000e4", "s61u1deg7ccj5iafuptdstliozmczsfu");
		try {
//			demo.sendAndroidUnicast();
//			 TODO these methods are all available, just fill in some fields and do the test
//			 * demo.sendAndroidCustomizedcastFile();
//			 * demo.sendAndroidBroadcast();
//			 * demo.sendAndroidGroupcast();
//			 * demo.sendAndroidCustomizedcast();
//			 * demo.sendAndroidFilecast();
//			 * 
//			 * demo.sendIOSBroadcast();
//			 demo.sendIOSUnicast();
//			 * demo.sendIOSGroupcast();
//			 * demo.sendIOSCustomizedcast();
//			 * demo.sendIOSFilecast();
			 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}
