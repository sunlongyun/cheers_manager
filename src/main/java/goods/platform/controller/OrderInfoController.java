package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.OrderStatusEnum;
import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.OrderDeliveryAddressMappingDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.miniProgram.request.PayToPersonRequest;
import com.chisong.green.farm.app.miniProgram.service.WxPayService;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.chisong.green.farm.app.service.OrderDeliveryAddressMappingService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.chisong.green.farm.app.service.RefundOrderService;
import com.chisong.green.farm.app.utils.CurrentUserUtils;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
	@Autowired
	private RefundOrderService refundOrderService;

	@Autowired
	private CustomerInfoService customerInfoService;

	@RequestMapping("/pay")
	public Response pay(){
		PayToPersonRequest payToPersonRequest = new PayToPersonRequest();
		payToPersonRequest.setAmount(100);
//		payToPersonRequest.setCheckName("FORCE_CHECK");
//		payToPersonRequest.setReUserName("孙龙");
		payToPersonRequest.setPartnerTradeNo(System.currentTimeMillis()+"");
		payToPersonRequest.setDesc("test");
		payToPersonRequest.setOpenid("oqrTq4jLQt0I_9F4vQVQLQGDrBbM");
		wxPayService.payToPerson(payToPersonRequest);
		return Response.success();
	}

	/**
	 * 发起退款申请
	 * @param orderId
	 * @param applyAmount
	 * @return
	 */
	@RequestMapping("/refund")
	public Response refund(Long orderId, int applyAmount,  HttpSession session){


		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
		if(customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.SALER_AND_CUSTOMER.code()){
			return Response.fail("只有平台管理员才能发起退款");
		}

		OrderInfoDto orderInfoDto = orderInfoService.getOrderById(orderId);
		if(null == orderInfoDto){
			return Response.fail("未找到对应的订单");
		}
		if(StringUtils.isEmpty(orderInfoDto.getPayNo())){
			return Response.fail("未支付的订单不能发起退款申请");
		}

		Long refundAmount = orderInfoDto.getRefundAmount();
		if(null == refundAmount) {
			refundAmount = 0l;
		}

		Long income = orderInfoDto.getIncome();
		if(income == null){
			income=0l;
		}

		//已取消或者已删除的订单不可以退款
		if(OrderStatusEnum.DELETED.code() == orderInfoDto.getStatus()
		|| OrderStatusEnum.CANCELED.code() == orderInfoDto.getStatus()){
			return Response.fail("已经删除或者取消的订单不可以发起退款");
		}

		Date feeTransferTime = orderInfoDto.getFeeTransferTime();
		if(null != feeTransferTime){
			feeTransferTime = new Date();
		}
		//结算之前1个小时之后，不可执行退款
		LocalDateTime hourBefore =
			feeTransferTime.toInstant().plus(-1, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toLocalDateTime();
		Date date =   Date.from(hourBefore.atZone(ZoneId.systemDefault()).toInstant());
		if(date.after(new Date())){
			return Response.fail("订单即将进行结算，无法发起退款申请");
		}

		//已发货或者已收货的订单，普通管理员最多只可以退款利润的50%
		if(OrderStatusEnum.PAYED.code() != orderInfoDto.getStatus()
			&& customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code() &&
			(refundAmount+(applyAmount*100))>(income/2)){
			return Response.fail("您不能发起退款金额不得大于利润的50%的操作，请联系超级管理员");
		}



		refundOrderService.applyRefund(orderInfoDto.getOrderNo(), applyAmount);
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
		if(!StringUtils.isEmpty(orderInfoDtoReq.getLogisticsNumber())) {
			oldOrderInfo.setSendTime(new Date());
			oldOrderInfo.setStatus(OrderStatusEnum.DELIVERY.code());
			oldOrderInfo.setPostage(null);
		}
		//给供应商结款
		if(orderInfoDtoReq.getSupplierAmount() != null) {
			if(oldOrderInfo.getSupplierAmount() > ( oldOrderInfo.getCostAmount()
				+ oldOrderInfo.getPostage())) {
				return Response.fail("给供应商结算金额不能大于\"进货金额+运费\"");
			}
			if(oldOrderInfo.getSupplierAmount() ==( oldOrderInfo.getCostAmount()
				+ oldOrderInfo.getPostage()) || Integer.valueOf(2).equals(orderInfoDtoReq.getSupplierStatus())) {
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
