package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.ProvinceCityAreaDto;
import com.chisong.green.farm.app.dto.SupplierDto;
import com.chisong.green.farm.app.entity.ProvinceCityArea;
import com.chisong.green.farm.app.example.SupplierExample;
import com.chisong.green.farm.app.example.SupplierExample.Criteria;
import com.chisong.green.farm.app.miniProgram.response.LoginResponse;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.chisong.green.farm.app.service.ProvinceCityAreaService;
import com.chisong.green.farm.app.service.SupplierService;
import com.chisong.green.farm.app.utils.CurrentUserUtils;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
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
	@Autowired
	private CustomerInfoService customerInfoService;
	@Autowired
	private ProvinceCityAreaService provinceCityAreaService;
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

	/**
	 * 添加供应商
	 * @return
	 */
	@RequestMapping("/addSupplier")
	public Response addSupplier(@RequestBody SupplierDto supplierDto, HttpSession session){
		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
		supplierDto.setCreatorId(customerInfoDto.getId());
		supplierDto.setCreatorName(customerInfoDto.getNickName());

		Integer provinceId = supplierDto.getProvinceId();
		Integer cityId = supplierDto.getCityId();
		Integer areaId = supplierDto.getAreaId();

		ProvinceCityAreaDto province =  provinceCityAreaService.getById(provinceId);
		ProvinceCityAreaDto city =  provinceCityAreaService.getById(cityId);
		ProvinceCityAreaDto area =  provinceCityAreaService.getById(areaId);
		supplierDto.setProvince(province.getName());
		supplierDto.setCity(city.getName());
		supplierDto.setArea(area.getName());

		supplierService.save(supplierDto);

		return Response.success();
	}


}
