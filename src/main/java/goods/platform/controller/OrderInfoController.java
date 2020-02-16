package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerDeliveryAddressDto;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.OrderDeliveryAddressMappingDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.example.OrderInfoExample.Criteria;
import com.chisong.green.farm.app.service.OrderDeliveryAddressMappingService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
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

	/**
	 * 修改订单
	 * @return
	 */
	@RequestMapping("/updateOrder")
	public Response updateOrder(@RequestBody OrderInfoDto orderInfoDto){
		OrderInfoDto oldOrderInfo =  orderInfoService.getOrderById(orderInfoDto.getId());
		orderInfoService.update(orderInfoDto);

		CustomerDeliveryAddressDto customerDeliveryAddressDto =  orderInfoDto.getAddress();

		OrderDeliveryAddressMappingDto orderDeliveryAddressMappingDto =
			orderDeliveryAddressMappingService.getById(oldOrderInfo.getOrderDeliveryAddressMappingDto().getId());
		orderDeliveryAddressMappingDto.setAddress(orderInfoDto.getUserAddress());

		orderDeliveryAddressMappingDto.setAddress(orderInfoDto.getUserAddress());
		orderDeliveryAddressMappingDto.setMobile(orderInfoDto.getMobile());
		orderDeliveryAddressMappingDto.setContact(orderInfoDto.getCustomerName());
		orderDeliveryAddressMappingService.update(orderDeliveryAddressMappingDto);

		return Response.success();
	}

	/**
	 * 查询订单详情
 	 * @param id
	 * @return
	 */
	@RequestMapping("/detail/{id}")
	public Response getDetail(@PathVariable Long id){
		OrderInfoDto orderInfoDto =  orderInfoService.getOrderById(id);
		return Response.success(orderInfoDto);
	}
	/**
	 * 分页查询
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String orderNo,
		String customerName, String mobile, HttpSession session) {

		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");


		Map<String, Object> params = new HashMap<>();
		if(!StringUtils.isEmpty(orderNo)){
			params.put("orderNo", orderNo);
		}
		if(StringUtils.isEmpty(customerName)){
			params.put("customerName", customerName);
		}
		if(!StringUtils.isEmpty(mobile)){
			params.put("mobile", mobile);
		}

		Integer customerId = null;
		if(customerInfoDto.getType() != UserTypeEnum.ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()){
			throw new RuntimeException("只有管理员用户才可以查询供应商");
		}

		if(customerInfoDto.getType() == UserTypeEnum.ADMIN.code()){
			customerId = Integer.parseInt(customerInfoDto.getId()+"");
			params.put("managerId", customerId);
		}

		PageInfo pageInfo = orderInfoService.getOrderInfoPageInfo(pageNo,pageSize, params);

		return pageInfo;
	}


}
