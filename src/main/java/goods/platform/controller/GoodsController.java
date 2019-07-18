package goods.platform.controller;

import com.caichao.chateau.app.constants.enums.Validity;
import com.caichao.chateau.app.example.CountryChateauBeverageExample;
import com.caichao.chateau.app.service.CountryChateauBeverageService;
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
@RestController
@RequestMapping("/admin/goods")
public class GoodsController {

	@Autowired
	private CountryChateauBeverageService countryChateauBeverageService;

	/**
	 * 分页查询
	 */
	@RequestMapping("getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize,
		String title, String enTitle, String countryName, String chateauTitle, String supplierCompanyName) {
		CountryChateauBeverageExample countryChateauBeverageExample = new CountryChateauBeverageExample();
		CountryChateauBeverageExample.Criteria criteria = countryChateauBeverageExample.createCriteria()
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
		if(!StringUtils.isEmpty(chateauTitle)) {
			criteria.andChateauTitleLike("%" + chateauTitle + "%");
		}
		if(!StringUtils.isEmpty(supplierCompanyName)) {
			criteria.andSupplierCompanyNameLike("%" + supplierCompanyName + "%");
		}

		PageInfo pageInfo = countryChateauBeverageService.getPageInfo(pageNo, pageSize, countryChateauBeverageExample);

		return pageInfo;
	}
}
