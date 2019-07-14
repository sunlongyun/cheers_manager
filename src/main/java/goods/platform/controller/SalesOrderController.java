//package goods.platform.controller;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestBody;
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
//import goods.platform.entity.SalesOrder;
//import goods.platform.service.SalesOrderService;
//@RequestMapping("/order")
//@RestController
//public class SalesOrderController {
//	@Autowired
//	private SalesOrderService salesOrderService;
//	/**
//	 * 添加订单信息
//	 * @param goods
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/save")
//	public Response saveOrder(@RequestBody SalesOrder salesOrder, HttpSession session){
//
//		AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
//
//		if(null == salesOrder.getId()){
//			salesOrder.setCreateId(adminUser.getId());
//			salesOrder.setCreator(adminUser.getUserName());
//			salesOrderService.save(salesOrder);
//		}else{
//			throw new RuntimeException("暂未开通编辑功能");
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
//	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String code, String customerCompanyName){
//
//		Map<String, String> dataMap = new HashMap<>();
//		if(!StringUtils.isEmpty(code)){
//			dataMap.put("code", code);
//		}
//		if(!StringUtils.isEmpty(customerCompanyName)){
//			dataMap.put("customerCompanyName", customerCompanyName);
//		}
//		PageInfo pageInfo = salesOrderService.getPageInfo(pageNo, pageSize, dataMap);
//		return pageInfo;
//	}
//
//
//}
