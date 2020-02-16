package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.UserStatusEnum;
import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.example.CustomerInfoExample;
import com.chisong.green.farm.app.example.CustomerInfoExample.Criteria;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.lianshang.generator.commons.PageInfo;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.CRC32;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import goods.platform.commons.Response;
/**
 * 管理员管理controller
 * @author 孙龙云
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

	public static final String ADMIN_USER = "adminUser";

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
			CustomerInfoDto customerInfoDto = customerInfoService.getAdminUserByUserName(userName.trim());
			if(null == customerInfoDto){
				return Response.fail("管理员信息不存在");
			}else if(customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.ADMIN.code()){
				return Response.fail("只有管理员角色才能登录");
			}else if(customerInfoDto.getStatus() == UserStatusEnum.FORZEN.code()){
				return Response.fail("账号已经冻结，请联系平台管理员");
			}else{
				String prePass = MD5(userName+password);
				log.info("prePass:{}", prePass);
				if(!prePass.equals(customerInfoDto.getPassword())){
					return Response.fail("密码错误");
				}
			}
			session.setAttribute("adminUser", customerInfoDto);
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
	public PageInfo getPageInfo(int pageNo, int pageSize, String userName, Integer type){

		CustomerInfoExample customerInfoExample = new CustomerInfoExample();
		Criteria criteria =  customerInfoExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code()).andTypeIn(Arrays.asList(UserTypeEnum.ADMIN.code(),
			UserTypeEnum.SUPER_ADMIN.code(),UserTypeEnum.SUPPLIER.code()));

		if(!StringUtils.isEmpty(userName)){
			criteria.andUserNameLike("%"+userName+"%");
		}
		if(null != type){
			criteria.andTypeEqualTo(type);
		}
		PageInfo pageInfo = customerInfoService.getPageInfo(pageNo, pageSize, customerInfoExample);

		return pageInfo;
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
	public Response changePass(Long customerId, String newPass, HttpSession session){
		if( StringUtils.isEmpty(newPass)){session.setMaxInactiveInterval(1);
			return Response.fail("新密码都不能为空!");
		}else{
			CustomerInfoDto adminUser = (CustomerInfoDto) session.getAttribute("adminUser");
			if(null == adminUser){
				return Response.fail("登录状态过期!");
			}else{
				CustomerInfoDto customerInfoDto = new CustomerInfoDto();

				if(null == customerId){
					customerInfoDto.setPassword(MD5(adminUser.getUserName()+newPass));
					customerInfoDto.setId(adminUser.getId());
				}else{
					customerInfoDto = customerInfoService.getById(customerId);
					customerInfoDto.setPassword(MD5(customerInfoDto.getUserName()+newPass));
				}

				customerInfoService.update(customerInfoDto);

				session.removeAttribute(ADMIN_USER);
			}
		}
		return Response.success();
	}

	/**
	 * 查询用户名
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserName", method=RequestMethod.GET)
	public Response  getUserName(HttpSession session){
		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
		if(null == customerInfoDto){
			return Response.fail( "会话已过期");
		}else{
			return Response.success(customerInfoDto);
		}

	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAdminUser/{id}")
	public Response deleteAdminUser(@PathVariable Long id){
		customerInfoService.deleteById(id);
		return Response.success();
	}
	/**
	 * 修改账户状态
	 * @param id
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeStatus")
	public Response changeStatus(Long id, Integer status){
		customerInfoService.changeStatus(id, status);
		return Response.success();
	}
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
