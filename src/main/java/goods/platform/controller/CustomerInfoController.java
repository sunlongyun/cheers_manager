//package goods.platform.controller;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
///**
// * 商品管理controller
// * @author 孙龙云
// *
// */
//
//import goods.platform.commons.PageInfo;
//import goods.platform.commons.Response;
//import goods.platform.entity.AdminUser;
//import goods.platform.entity.CustomerInfo;
//import goods.platform.service.CustomerInfoService;
//@RequestMapping("/customer")
//@RestController
//public class CustomerInfoController {
//	@Autowired
//	private CustomerInfoService customerInfoService;
//	/**
//	 * 添加客户信息
//	 * @param goods
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/save")
//	public Response saveCustomerInfo(CustomerInfo customerInfo, HttpSession session){
//
//		AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
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
//
//	/**
//	 * 分页查询
//	 * @param pageNo
//	 * @param pageSize
//	 * @param status
//	 * @param title
//	 * @return
//	 */
//	@RequestMapping("/getPageInfo")
//	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String name, String mobile, String companyName){
//
//		Map<String, String> dataMap = new HashMap<>();
//		if(!StringUtils.isEmpty(name)){
//			dataMap.put("name", name);
//		}
//		if(!StringUtils.isEmpty(mobile)){
//			dataMap.put("mobile", mobile);
//		}
//		if(!StringUtils.isEmpty(companyName)){
//			dataMap.put("companyName", companyName);
//		}
//		PageInfo pageInfo = customerInfoService.getPageInfo(pageNo, pageSize, dataMap);
//		return pageInfo;
//	}
//
//
//}
