package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.example.OrderInfoExample.Criteria;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.lianshang.generator.commons.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
		PageInfo pageInfo = orderInfoService.getPageInfo(pageNo, pageSize, orderInfoExample);

		return pageInfo;
	}


}
