//package goods.platform.utils;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Iterator;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentSkipListMap;
//import org.springframework.util.StringUtils;
//
///**
// * 描述:
// *
// * @AUTHOR 孙龙云
// * @date 2019-06-09 10:12
// */
//public class LoginUserInfoUtil {
//
//	private static ConcurrentSkipListMap<String, LoginResponse> userInfoMap = new ConcurrentSkipListMap<>();
//
//	/**
//	 * 根据userCode查询登录返回信息
//	 * @param userCode
//	 * @return
//	 */
//	public static LoginResponse getLoginResponse(String userCode){
//		if(StringUtils.isEmpty(userCode)){
//			return null;
//		}
//		return userInfoMap.get(userCode);
//	}
//
//	/**
//	 * 添加并返回userCode
//	 */
//	public static String put(LoginResponse loginResponse) {
//		Iterator<String> it = userInfoMap.keySet().iterator();
//		while(it.hasNext()) {
//			String key = it.next();
//			LoginResponse loginResponse1 = userInfoMap.get(key);
//			if(null != loginResponse1 && loginResponse.getOpenid().equals(loginResponse1.getOpenid())
//				&& (loginResponse.getUnionId() == null || loginResponse.getUnionId()
//				.equals(loginResponse1.getUnionId()))) {
//				userInfoMap.remove(key);
//				break;
//			}
//		}
//		String userCode =  LocalDateTime.now().format(DateTimeFormatter.ofPattern
//			("yyyyMMddHHmmss"))+"_"+UUID.randomUUID().toString();
//		userInfoMap.put(userCode,loginResponse);
//		return userCode;
//	}
//}
