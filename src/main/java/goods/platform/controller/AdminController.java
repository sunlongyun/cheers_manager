package goods.platform.controller;

import com.caichao.chateau.app.service.CustomerInfoService;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import goods.platform.commons.PageInfo;
import goods.platform.commons.Response;
import goods.platform.constant.UserTypeEnum;
import goods.platform.utils.RefOpenIdGenerateUtil;
import lombok.extern.slf4j.Slf4j;
/**
 * 管理员管理controller
 * @author 孙龙云
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	@Autowired
	private CustomerInfoService customerInfoService;

	/**
	 * 登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public Response login(String userName, String password, HttpSession session){
		this.log.info("登录==={}", userName);
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
			return Response.fail("用户名或者密码为空");
		}else{
//			AdminUser adminUser = adminUserService.getAdminUserByUserName(userName.trim());
//			if(null == adminUser){
//				return Response.fail("管理员信息不存在");
//			}else {
//				String prePass = MD5(password);
//				if(!prePass.equals(adminUser.getPassword())){
//					return Response.fail("密码错误");
//				}else if(!adminUser.getStatus().equals(UserStatusEnum.NORMAL.code())){
//					return Response.fail("账户被锁定(或者失效)，请联系管理员");
//				}
//			}
//			session.setAttribute("adminUser", adminUser);
			return Response.success("登录成功");
		}
	}
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPageInfo", method=RequestMethod.GET)
	public PageInfo getPageInfo(int pageNo, int pageSize, String userName){
		Map<String, String> params = new HashMap<>();
		if(!StringUtils.isEmpty(userName)){
			params.put("userName", userName);
		}
//		PageInfo pageInfo = adminUserService.getPageInfo(pageNo, pageSize, params);
		return null;
	}
	/**
	 * 退出登录
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/loginout", method=RequestMethod.GET)
	public Response loginout(HttpSession session){
		session.removeAttribute("adminUser");
		session.setMaxInactiveInterval(1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Response.success("登出成功");
	}
	/**
	 * 修改密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changePass", method = RequestMethod.POST)
	public Response changePass(String lastPass, String newPass, HttpSession session){
		if(StringUtils.isEmpty(lastPass) || StringUtils.isEmpty(newPass)){
			return Response.fail("远密码和新密码都不能为空!");
		}else{
//			AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
//			if(null == adminUser){
//				return Response.fail("登录状态过期!");
//			}else if(!MD5(lastPass).equals(adminUser.getPassword())){
//				return Response.fail("原密码错误!");
//			}else{
//				AdminUser admin = new AdminUser();
//				admin.setPassword(MD5(newPass));
//				admin.setId(adminUser.getId());
//				adminUserService.editAdminUser(admin);
//				session.setMaxInactiveInterval(1);
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		}
		return Response.success();
	}

//	/**
//	 * 管理员注册
//	 * @param adminUser
//	 * @param request
//	 * @param session
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/addUser",method=RequestMethod.POST)
//	public Response createUser(AdminUser adminUser,
//			HttpServletRequest request,
//			HttpSession session){
//		AdminUser oldAminUser = adminUserService.getAdminUserByUserName(adminUser.getUserName());
//		if(null != oldAminUser){
//			return Response.fail("用户名已经存在");
//		}else{
//			adminUser.setPassword(MD5(adminUser.getPassword()));
//			adminUser.setRefOpenId(RefOpenIdGenerateUtil.getAdminRefOpenId());
//			adminUser.setUserType(UserTypeEnum.ADMINPLATFORM.code());
//			adminUserService.addAdminUser(adminUser);
//			session.setAttribute("adminUser", adminUser);
//		}
//		return Response.success();
//	}
//	/**
//	 * 查询用户名
//	 * @param session
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/getUserName", method=RequestMethod.GET)
//	public Response  getUserName(HttpSession session){
//		AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
//		if(null == adminUser){
//			return Response.fail( "会话已过期");
//		}else{
//			return Response.success(adminUser.getUserName());
//		}
//
//	}
//	/**
//	 * 删除
//	 * @param id
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/deleteAdminUser/{id}")
//	public Response deleteAdminUser(@PathVariable Long id){
//		adminUserService.deleleAdminUser(id);
//		return Response.success();
//	}
//	/**
//	 * 修改账户状态
//	 * @param id
//	 * @param status
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/changeStatus")
//	public Response changeStatus(Integer id, Integer status){
//		adminUserService.changeStatus(id, status);
//		return Response.success();
//	}
	private static String MD5(String key) {
		CRC32 crc32 = new CRC32();
		crc32.update(key.getBytes());
		//加盐值
		key = key+crc32.getValue()+"chisong_tech";
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }


}
