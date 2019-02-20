package com.car.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.car.app.common.R;
import com.car.app.common.SystemConstant;
import com.car.app.common.request.FriendManagerRequest;
import com.car.app.common.request.GetUserInfoRequest;
import com.car.app.common.request.IOTInfoRequest;
import com.car.app.common.request.IOTLocRequest;
import com.car.app.common.request.LoginRequest;
import com.car.app.common.request.RegisterRequest;
import com.car.app.common.request.ResetRequest;
import com.car.app.common.request.UpdateUserInfoRequest;
import com.car.app.mapper.FriendsMapper;
import com.car.app.mapper.ImeiUserMapper;
import com.car.app.mapper.InfoMapper;
import com.car.app.mapper.UserMapper;
import com.car.app.model.Friends;
import com.car.app.model.ImeiUser;
import com.car.app.model.Information;
import com.car.app.model.User;
import com.car.app.service.Cache;
import com.car.app.utils.JwtUtils;
import com.car.app.utils.notification.NotificationUtil;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/funrockcar")
public class CarController {

	private static final Logger logger = LoggerFactory.getLogger(CarController.class);
	
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
	private ImeiUserMapper iuMapper;
	
	@Autowired
	private InfoMapper infoMapper;
	
	@Autowired
	private FriendsMapper friendsMapper;
	
	@Autowired
	private Cache cache;
	
	@ResponseBody
	@RequestMapping(value = "register", method = RequestMethod.POST,consumes = "application/json")
	public R register(@RequestBody RegisterRequest request) {
		String account = request.getAccount();
		String password = request.getPassword();
		String imei = request.getImei();
		if(account == null || password == null) {
			return R.error().put("status", "5");
		}
		List<User> users = userMapper.getUserByAccount(account);
		if(users != null && users.size()>0) {
			return R.error().put("status", "2");
		}
		User user = new User();
		user.setAccount(account);
		user.setPassword(password);
		
		if(request.getImei() != null) {
			// account与imei一对多，放开这里的绑定判断
//			List<ImeiUser> imeis = iuMapper.getIMEIUserByIMEIAndCount(imei, 1);
//			if(imeis != null && imeis.size()>0) {
//				return R.error().put("status", "3");
//			}
			ImeiUser imeiUser = new ImeiUser();
			imeiUser.setAccount(account);
			imeiUser.setImei(imei);
			imeiUser.setCount(1);
			iuMapper.insertIMEIUser(imeiUser);
			user.setImei(imei);
		}
		userMapper.insertUser(user);
		
		return R.ok().put("status", "1");
	}
	
	@ResponseBody
	@RequestMapping(value = "login", method = RequestMethod.POST,consumes = "application/json")
	public R login(@RequestBody LoginRequest request) {
		if(request.getAccount() == null || request.getPassword() == null) {
			return R.error().put("status", "5");
		}
		List<User> users = userMapper.getUserByAccount(request.getAccount());
		Date exp = new Date();
		if(users != null && users.size()>0) {
			User u = users.get(0);
			if(u.getPassword().equals(request.getPassword())) {
				String deviceToken = request.getDeviceToken();
				logger.info("username is ->" + request.getAccount() + "device token is ->" + request.getDeviceToken());
				if(deviceToken != null) {
					if (cache.get(request.getAccount()) == null || "".equals(cache.get(request.getAccount()))) {
						cache.save(request.getAccount(), deviceToken);
					}else {
						cache.update(request.getAccount(), deviceToken);
					}
				}
				//把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
				String JWT = JwtUtils.createJWT("1", u.getAccount(), SystemConstant.JWT_TTL);
				List<String> imeis = new ArrayList<String>();
				imeis.add(u.getImei());
				try {	
					exp = JwtUtils.parseJWT(JWT).getExpiration();
				} catch (Exception e) {
					e.printStackTrace();
					return R.error().put("status", "4").put("msg", "JWT解析异常");
				}
				return R.ok().put("expirationDate", exp).put("status", "1").put("accessToken", JWT).put("userID", u.getId()).put("imeis", imeis);
			}else {
				return R.error().put("status", "3");
			}
		}else {
			return R.error().put("status", "2");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "resetpassword", method = RequestMethod.POST,consumes = "application/json")
	public R resetpassword(@RequestBody ResetRequest request) {
		String account = request.getAccount();
		String password = request.getPassword();
		if(account == null || password == null) {
			return R.error().put("status", "5");
		}
		List<User> users = userMapper.getUserByAccount(request.getAccount());
		if(users != null && users.size()>0) {
			userMapper.updatePassword(account, password);;
			return R.ok().put("status", "1");
			
		}else {
			return R.error().put("status", "2");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "iotinformation", method = RequestMethod.POST,consumes = "application/json")
	public R iotinformation(@RequestBody IOTInfoRequest request) {
		String imei = request.getImei();
		String sim = request.getSim();
		String collectTime = request.getTime();
		String longitude = request.getLongitude();
		String latitude = request.getLatitude();
		String iotstate = request.getIotstate();
		String utype = request.getType();
		if(imei == null || collectTime == null || longitude == null || latitude == null || iotstate == null) {
			return R.error().put("status", "5");
		}
		
		List<Information> infos = infoMapper.getAll();
		if(infos != null && infos.size()==100) {
			Information info = infos.get(0);
			infoMapper.deleteOne(info.getTimestamps(), info.getImei(), info.getIotstate());
		}
		List<Information> oldInfos = infoMapper.getInfosByImeiAndIotstateAndTime(iotstate, imei, collectTime);
		if(oldInfos != null && oldInfos.size() > 0) {
			R.error().put("status", "6").put("duplicate", true);
		}
		Information info = new Information();
		info.setTimestamps(new Date().getTime());
		info.setImei(imei);
		info.setIotstate(iotstate);
		info.setLatitude(latitude);
		info.setLongitude(longitude);
		info.setSim(sim);
		info.setuType(utype);
		info.setCollectTime(collectTime);
		infoMapper.insertInfo(info);
		Integer count = 0;
		List<ImeiUser> imus = iuMapper.getIMEIUserByIMEI(imei);
		if(imus != null) {
			ImeiUser imu = imus.get(0);
			count = imu.getCount();
		}else {
			R.error().put("status", "4").put("msg", "未绑定imei与account，导致消息推送失败");
		}
		
		
		/**
		 * 数据库中建立一个绑定状态表，表信息包括IMEI、账号、状态、
		 * 时间，主Key、账号类型是IMEI码，其中状态0表示未被绑定，1表示第一次被绑定，
		 * 2表示第一次被解绑，3表示绑定，接收到IOT上传数据后，把这个状态返回给IOT设备，
		 * 如果状态为1，返回设备后数据库此状态变为3，如果状态为2，返回设备后数据库此状态变为0
		 */
		if(count == 1) {
			iuMapper.updateImei(imei, 3);
		}else if(count == 2) {
			iuMapper.updateImei(imei, 0);
		}
		if("2".equals(iotstate)) {
//			List<ImeiUser> imeiUsers = iuMapper.getIMEIUserByIMEI(imei);
//			if(imeiUsers == null || imeiUsers.size()==0) {
//				R.error().put("status", "4").put("msg", "未绑定imei与account，导致消息推送失败");
//			}
			for(ImeiUser iu: imus) {
				String account = iu.getAccount();
				String myToken = cache.get(account);
				
				if(myToken != null && !"".equals(myToken)) {
					try {
						NotificationUtil.sendIOSUnicast(myToken, null);
					} catch (Exception e) {
						e.printStackTrace();
						return R.error().put("status", "4").put("msg", "消息推送失败");
					}
				}else {
					continue;
				}
				List<Friends> toFriends = friendsMapper.getFriendsByAccountAndOperationAndMesend(account, "2", "1");
				
				for(Friends f: toFriends) {
					List<Friends> backFs = friendsMapper.getFriendsByFriendaccountAndOperationAndMereceive(f.getFriendaccount(), "2", "1");
					if(backFs !=null && backFs.size()>0) {
						
						String deviceToken = cache.get(f.getFriendaccount());
						if(deviceToken == null) {
							continue;
						}
						String nick = null;
						List<User> users = userMapper.getUserByAccount(f.getAccount());
						if(users != null || users.size() > 0) {
							nick = users.get(0).getNick();
						}
						if(deviceToken != null && !"".equals(deviceToken)) {
							try {
								NotificationUtil.sendIOSUnicast(deviceToken, nick == null ? f.getAccount():nick);
							} catch (Exception e) {
								e.printStackTrace();
								R.error().put("status", "4").put("msg", "您的好友" + (nick == null ? f.getFriendaccount():nick) + "车罩被移动");
							}
						}
					}
				}
			}
		}
		return R.ok().put("status", "1").put("binding", count);
	}
	
	@ResponseBody
	@RequestMapping(value = "iotlocations", method = RequestMethod.POST,consumes = "application/json")
	public R iotlocations(@RequestBody IOTLocRequest request) {
		String imei = request.getImei();
		String account = request.getAccount();
		String dataType = request.getDataType();
		String count = request.getCount();
//		String utype = request.getType();
		if(imei == null || account == null || dataType == null || count == null ) {
			return R.error().put("status", "5");
		}
		List<Information> infos = new ArrayList<Information>();
		if("3".endsWith(dataType)) {
			infos = infoMapper.getInfosByImei(imei);
		}else {
			infos = infoMapper.getInfosByImeiAndIotstate(dataType, imei);
		}
		if(infos == null || infos.size() == 0) {
			return R.notFound().put("status", "3");
		}
		if(!"0".endsWith(count)) {
			if(infos.size() < Integer.valueOf(count)) {
				count = String.valueOf(infos.size());
			}
			infos = infos.subList(0, Integer.valueOf(count));
		}
		return R.ok().put("status", "1").put("informations", infos);
	}
	
	@ResponseBody
	@RequestMapping(value = "getuserinfo", method = RequestMethod.POST,consumes = "application/json")
	public R getuserinfo(@RequestBody GetUserInfoRequest request) {
		String account = request.getAccount();
		if(account == null ) {
			return R.error().put("status", "5");
		}
		List<User> users = userMapper.getUserByAccount(account);
		if(users == null || users.size()==0) {
			return R.error().put("status", "2");
		}
		String nick = users.get(0).getNick();
		List<ImeiUser> imeiUsers = iuMapper.getIMEIUserByAccount(account);
		List<String> imeis = new ArrayList<String>();
		if(imeiUsers != null && imeiUsers.size()>0) {
			for(ImeiUser iu: imeiUsers) {
				imeis.add(iu.getImei());
			}
		}
		
		JSONArray array = new JSONArray();
		List<Friends> tofriends = friendsMapper.getFriendsByAccount(account);
		for(Friends f: tofriends) {
			JSONObject obj = new JSONObject();
			obj.put("account", f.getFriendaccount());
			List<User> list = userMapper.getUserByAccount(f.getFriendaccount());
			obj.put("nick", list.get(0).getNick());
			if(f.getOperation().equals("2")) {
				obj.put("addstate", "3");
				List<Friends> fds = friendsMapper.getFriendsByAccountAndFriend(f.getFriendaccount(), f.getAccount());
				obj.put("friendagree", fds.get(0).getMesend());
			}else if(f.getOperation().equals("1")) {
				obj.put("addstate", "2");
				obj.put("friendagree", "0");
			}
			obj.put("mesend", f.getMesend());
			obj.put("meagree", f.getMereceive());
			List<ImeiUser> ius = iuMapper.getIMEIUserByAccount(f.getFriendaccount());
			List<String> fImeis = new ArrayList<String>();
			for(ImeiUser iu: ius) {
				fImeis.add(iu.getImei());
			}
			obj.put("imeis", fImeis);
			array.add(obj);
		}
		List<Friends> fromfriends = friendsMapper.getFriendsByFriendaccountAndOperation(account,"1");
		for(Friends f: fromfriends) {
			JSONObject obj = new JSONObject();
			obj.put("account", f.getAccount());
			List<User> list = userMapper.getUserByAccount(f.getAccount());
			obj.put("nick", list.get(0).getNick());
			obj.put("addstate", "1");
			obj.put("friendagree", f.getMesend());
			obj.put("meagree", "0");
			obj.put("mesend", f.getMesend());
			List<ImeiUser> ius = iuMapper.getIMEIUserByAccount(f.getAccount());
			List<String> fImeis = new ArrayList<String>();
			for(ImeiUser iu: ius) {
				fImeis.add(iu.getImei());
			}
			obj.put("imeis", fImeis);
			array.add(obj);
		}
		return R.ok().put("status", "1").put("nick", nick).put("imeis", imeis).put("friends", array);
	}
	
	@ResponseBody
	@RequestMapping(value = "updateuserinfo", method = RequestMethod.POST,consumes = "application/json")
	public R updateuserinfo(@RequestBody UpdateUserInfoRequest request) {
		String account = request.getAccount();
		String nick = request.getNick();
		List<String> imeis = request.getImeis();
		if(account == null ) {
			return R.error().put("status", "5");
		}
		if(nick == null && imeis == null) {
			return R.ok().put("status", "1");
		}
		List<User> accounts = userMapper.getUserByAccount(account);
		if(accounts == null || accounts.size() == 0) {
			return R.error().put("status", "2");
		}
		iuMapper.deleteByAccount(account);
		if(imeis != null && imeis.size()>0) {
			for(String imei: imeis) {
				ImeiUser imu = new ImeiUser();
				imu.setAccount(account);
				imu.setImei(imei);
				imu.setCount(1);
				iuMapper.insertIMEIUser(imu);
			}
		}
		if(nick != null) {
			userMapper.updateNick(account, nick);
		}
		
		return R.ok().put("status", "1");
	}
	
	@ResponseBody
	@RequestMapping(value = "friendmanager", method = RequestMethod.POST,consumes = "application/json")
	public R friendmanager(@RequestBody FriendManagerRequest request) {
		String account = request.getAccount();
		String friendaccount = request.getFriendaccount();
		String operation = request.getOperation();
		String mesend = request.getMesend();
		String mereceive = request.getMereceive();
		if(account == null || friendaccount == null || operation == null || mesend == null || mereceive == null) {
			return R.error().put("status", "5");
		}
		List<User> accounts = userMapper.getUserByAccount(account);
		if(accounts == null || accounts.size() == 0) {
			return R.error().put("status", "2");
		}
		
		List<User> friendaccounts = userMapper.getUserByAccount(friendaccount);
		if(friendaccounts == null || friendaccounts.size() == 0) {
			return R.error().put("status", "3");
		}
		
		if("1".equals(operation)) {
			Friends friends = new Friends();
			friends.setAccount(account);
			friends.setFriendaccount(friendaccount);
			friends.setMesend(mesend);
			friends.setMereceive(mereceive);
			friends.setOperation(operation);
			friendsMapper.insertFriends(friends);
		} else if("2".equals(operation)) {
			// 对端发送确认时，需要颠倒顺序
			List<Friends> friends = friendsMapper.getFriendsByAccountAndFriend(friendaccount, account);
			if(friends == null || friends.size()==0) {
				return R.error().put("status", "4");
			}
			operation = "2";
			friendsMapper.updateFriend(operation, friendaccount, account, mesend, mereceive);
			Friends newFriend = new Friends();
			newFriend.setAccount(account);
			newFriend.setFriendaccount(friendaccount);
			newFriend.setMesend(mesend);
			newFriend.setMereceive(mereceive);
			newFriend.setOperation(operation);
			friendsMapper.insertFriends(newFriend);
		} else if("3".equals(operation)) {
			friendsMapper.deleteByAccountAndFriend(account, friendaccount);
			friendsMapper.deleteByAccountAndFriend(friendaccount, account);
			logger.debug("解除好友关系");
		} else if("4".equals(operation)) {
			operation = "2";
			friendsMapper.updateFriend(operation, account, friendaccount, mesend, mereceive);
			logger.debug("更新好友状态");
		}
		
		return R.ok().put("status", "1");
	}
	
}
