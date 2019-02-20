package com.yiyao.app.controller;

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
import com.yiyao.app.common.R;
import com.yiyao.app.common.SystemConstant;
import com.yiyao.app.common.request.FriendManagerRequest;
import com.yiyao.app.common.request.GetUserInfoRequest;
import com.yiyao.app.common.request.IOTInfoRequest;
import com.yiyao.app.common.request.IOTLocRequest;
import com.yiyao.app.common.request.LoginRequest;
import com.yiyao.app.common.request.RegisterRequest;
import com.yiyao.app.common.request.ResetRequest;
import com.yiyao.app.common.request.UpdateUserInfoRequest;
import com.yiyao.app.mapper.FriendsMapper;
import com.yiyao.app.mapper.ImeiUserMapper;
import com.yiyao.app.mapper.InfoMapper;
import com.yiyao.app.mapper.UserMapper;
import com.yiyao.app.model.Friends;
import com.yiyao.app.model.ImeiUser;
import com.yiyao.app.model.Information;
import com.yiyao.app.model.User;
import com.yiyao.app.service.Cache;
import com.yiyao.app.utils.JwtUtils;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/funrockcar")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
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
	
	
}
