package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.example.GoodsExample;
import com.chisong.green.farm.app.service.GoodsService;
import com.lianshang.generator.commons.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 商品管理controller
 *
 * @AUTHOR 孙龙云
 * @date 2019-07-18 21:53
 */
@RestController("goodsMangerController")
@RequestMapping("/admin/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	/**
	 * 分页查询
	 */
	@RequestMapping("getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize,
		String title, String enTitle, String countryName, String chateauTitle, String supplierCompanyName) {
		GoodsExample goodsExample = new GoodsExample();
		GoodsExample.Criteria criteria = goodsExample.createCriteria()
			.andValidityEqualTo(Validity.AVAIL.code());

		if(!StringUtils.isEmpty(title)) {
			criteria.andTitleLike("%" + title + "%");
		}
		if(!StringUtils.isEmpty(enTitle)) {
			criteria.andEnTitleLike("%" + enTitle + "%");
		}

		if(!StringUtils.isEmpty(countryName)) {
			criteria.andCountryNameLike("%" + countryName + "%");
		}


		PageInfo pageInfo = goodsService.getPageInfo(pageNo, pageSize, goodsExample);

		return pageInfo;
	}
}
