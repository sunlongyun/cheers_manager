package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerDeliveryAddressDto;
import com.chisong.green.farm.app.dto.OrderDeliveryAddressMappingDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.example.OrderInfoExample.Criteria;
import com.chisong.green.farm.app.service.OrderDeliveryAddressMappingService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
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
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String orderNo, String customerName, String mobile) {

		OrderInfoExample orderInfoExample = new OrderInfoExample();
		Criteria criteria = orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		if(!StringUtils.isEmpty(orderNo)) {
			criteria.andOrderNoLike("%" + orderNo.trim() + "%");
		}
		if(!StringUtils.isEmpty(customerName)) {
			criteria.andCustomerNameLike("%" + customerName + "%");
		}
		if(!StringUtils.isEmpty(mobile)) {
			criteria.andMobileLike("%" + mobile + "%");
		}
		orderInfoExample.setOrderByClause("id desc");
		PageInfo pageInfo = orderInfoService.getPageInfo(pageNo, pageSize, orderInfoExample);

		return pageInfo;
	}


}
