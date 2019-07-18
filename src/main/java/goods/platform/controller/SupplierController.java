package goods.platform.controller;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.caichao.chateau.app.constants.enums.Validity;
import com.caichao.chateau.app.example.SupplierExample;
import com.caichao.chateau.app.example.SupplierExample.Criteria;
import com.caichao.chateau.app.service.SupplierService;
import com.lianshang.generator.commons.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-07-18 21:32
 */
@RestController("admin_supplierController")
@RequestMapping("/admin/supplier")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param companyName
	 * @param address
	 * @param mobile
	 * @param telephone
	 * @return
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String companyName, String address,
		String mobile, String telephone){
		SupplierExample supplierExample = new SupplierExample();
		Criteria criteria = supplierExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		if(!StringUtils.isEmpty(companyName)){
			criteria.andCompanyNameLike("%"+companyName+"%");
		}
		if(!StringUtils.isEmpty(address)){
			criteria.andAddressLike("%"+address+"%");
		}
		if(!StringUtils.isEmpty(mobile)){
			criteria.andMobileLike("%"+mobile+"%");
		}
		if(!StringUtils.isEmpty(telephone)){
			criteria.andTelephoneLike("%"+telephone+"%");
		}
		PageInfo pageInfo =  supplierService.getPageInfo(pageNo, pageSize, supplierExample);
		return pageInfo;
	}
}
