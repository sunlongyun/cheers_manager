package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.OrderStatusEnum;
import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.OrderDeliveryAddressMappingDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.miniProgram.request.PayToPersonRequest;
import com.chisong.green.farm.app.miniProgram.service.WxPayService;
import com.chisong.green.farm.app.service.OrderDeliveryAddressMappingService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.chisong.green.farm.app.service.PaymentService;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品管理controller
 *
 * @author 孙龙云
 */

@RequestMapping("admin/order")
@RestController
public class OrderInfoController {

	@Autowired
	private OrderInfoService orderInfoService;
	@Autowired
	private OrderDeliveryAddressMappingService orderDeliveryAddressMappingService;

	@Autowired
	private WxPayService wxPayService;

	@RequestMapping("/pay")
	public Response pay(){
		PayToPersonRequest payToPersonRequest = new PayToPersonRequest();
		payToPersonRequest.setAmount(100);
//		payToPersonRequest.setCheckName("孙龙云");
		payToPersonRequest.setReUserName("孙龙云");
		payToPersonRequest.setPartnerTradeNo("test001111");
		payToPersonRequest.setDesc("付款测试");
		payToPersonRequest.setOpenid("oqrTq4jLQt0I_9F4vQVQLQGDrBbM");
		wxPayService.payToPerson(payToPersonRequest);
		return Response.success();
	}
	/**
	 * 修改订单
	 *
	 * @return
	 */
	@RequestMapping("/updateOrder")
	public Response updateOrder(@RequestBody OrderInfoDto orderInfoDtoReq) {
		OrderInfoDto oldOrderInfo = orderInfoService.getOrderById(orderInfoDtoReq.getId());
		//覆盖
		coverFileds(orderInfoDtoReq, oldOrderInfo);
		if(null == oldOrderInfo.getTotalAmount() ||
			null == oldOrderInfo.getCostAmount() ||
			oldOrderInfo.getTotalAmount() < oldOrderInfo.getCostAmount()) {
			return Response.fail("商品售价不得低于进价");
		}
		oldOrderInfo.setIncome(oldOrderInfo.getTotalAmount() - oldOrderInfo.getCostAmount());
		//发货
		if(!StringUtils.isEmpty(orderInfoDtoReq.getLogisticsNumber())
			&& orderInfoDtoReq.getStatus() == OrderStatusEnum.DELIVERY.code()) {
			oldOrderInfo.setSendTime(new Date());
		}
		//给供应商结款
		if(orderInfoDtoReq.getSupplierAmount() != null) {
			if(oldOrderInfo.getSupplierAmount() > ( oldOrderInfo.getCostAmount()
				+ oldOrderInfo.getPostage())) {
				return Response.fail("给供应商结算金额不能大于\"进货金额+运费\"");
			}
			if(oldOrderInfo.getSupplierAmount() ==( oldOrderInfo.getCostAmount()
				+ oldOrderInfo.getPostage())) {
				oldOrderInfo.setSupplierStatus(2);
			} else {
				oldOrderInfo.setSupplierStatus(1);
			}
		}
		orderInfoService.update(oldOrderInfo);

		OrderDeliveryAddressMappingDto orderDeliveryAddressMappingDto =
			orderDeliveryAddressMappingService.getById(oldOrderInfo.getOrderDeliveryAddressMappingDto().getId());
		orderDeliveryAddressMappingDto.setAddress(orderInfoDtoReq.getUserAddress());

		orderDeliveryAddressMappingDto.setMobile(orderInfoDtoReq.getUserMobile());
		orderDeliveryAddressMappingDto.setContact(orderInfoDtoReq.getUserName());
		orderDeliveryAddressMappingService.update(orderDeliveryAddressMappingDto);

		return Response.success();
	}

	//覆盖非空字段
	private void coverFileds(@RequestBody OrderInfoDto orderInfoDtoReq,
		OrderInfoDto oldOrderInfo) {
		Field[] fields = OrderInfoDto.class.getDeclaredFields();
		for(Field f : fields) {
			if(f.getName().contains("serialVersionUID")) {
				continue;
			}
			try {
				f.setAccessible(true);
				Object value = f.get(orderInfoDtoReq);
				if(null != value) {
					f.set(oldOrderInfo, value);
				}
				f.setAccessible(false);
			} catch(Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * 查询订单详情
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail/{id}")
	public Response getDetail(@PathVariable Long id) {
		OrderInfoDto orderInfoDto = orderInfoService.getOrderById(id);
		return Response.success(orderInfoDto);
	}

	/**
	 * 分页查询
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String orderNo,
		String customerName, String mobile, Integer status, HttpSession session) {

		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");

		Map<String, Object> params = new HashMap<>();
		if(!StringUtils.isEmpty(orderNo)) {
			params.put("orderNo", orderNo);
		}
		if(!StringUtils.isEmpty(customerName)) {
			params.put("customerName", customerName);
		}
		if(!StringUtils.isEmpty(mobile)) {
			params.put("mobile", mobile);
		}
		if(null != status && status != -1) {
			params.put("status", status);
		}

		Integer customerId = null;
		if(customerInfoDto.getType() != UserTypeEnum.ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()) {
			throw new RuntimeException("只有管理员用户才可以查询供应商");
		}

		if(customerInfoDto.getType() == UserTypeEnum.ADMIN.code()) {
			customerId = Integer.parseInt(customerInfoDto.getId() + "");
			params.put("managerId", customerId);
		}

		PageInfo pageInfo = orderInfoService.getOrderInfoPageInfo(pageNo, pageSize, params);

		return pageInfo;
	}


}
