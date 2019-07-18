package goods.platform.controller;
import com.caichao.chateau.app.constants.enums.UserTypeEnum;
import com.caichao.chateau.app.constants.enums.Validity;
import com.caichao.chateau.app.dto.CustomerInfoDto;
import com.caichao.chateau.app.example.CustomerInfoExample;
import com.caichao.chateau.app.example.CustomerInfoExample.Criteria;
import com.caichao.chateau.app.service.CustomerInfoService;
import com.lianshang.generator.commons.PageInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 商品管理controller
 * @author 孙龙云
 *
 */

import goods.platform.commons.Response;
@RequestMapping("/admin/customer")
@RestController
public class CustomerInfoController {
	@Autowired
	private CustomerInfoService customerInfoService;
	/**
	 * 添加客户信息
	 * @param customerInfo
	 * @param session
	 * @return
	 */
//	@RequestMapping("/save")
//	public Response saveCustomerInfo(CustomerInfoDto customerInfo, HttpSession session){
//
//		CustomerInfoDto adminUser = (CustomerInfoDto) session.getAttribute("adminUser");
//
//		if(null == customerInfo.getId()){
//			customerInfo.setCreateId(adminUser.getId());
//			customerInfo.setCreator(adminUser.getUserName());
//			customerInfoService.saveCustomerInfo(customerInfo);
//		}else{
//			customerInfoService.updateCustomerInfo(customerInfo);
//		}
//		return Response.success();
//	}

	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param userName
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String userName, String mobile, String companyName){

		CustomerInfoExample customerInfoExample = new CustomerInfoExample();
		Criteria criteria =  customerInfoExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code()).andTypeIn(Arrays.asList(UserTypeEnum.CUSTOMER.code()));;
		if(!StringUtils.isEmpty(userName)){
			criteria.andUserNameLike("%"+userName+"%");
		}
		if(!StringUtils.isEmpty(mobile)){
			criteria.andMobileLike("%"+mobile+"%");
		}
		PageInfo pageInfo = customerInfoService.getPageInfo(pageNo, pageSize, customerInfoExample);
		return pageInfo;
	}


}
