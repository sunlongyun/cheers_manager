package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
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
import com.chisong.green.farm.app.utils.AppUtils;
import com.chisong.green.farm.app.utils.CurrentUserUtils;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
		String mobile, String telephone, HttpSession session){
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
		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");

		if(customerInfoDto.getType() != UserTypeEnum.ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()){
			throw new RuntimeException("只有管理员用户才可以查询供应商");
		}

		if(customerInfoDto.getType() == UserTypeEnum.ADMIN.code()){
			criteria.andCreatorIdEqualTo(Integer.parseInt(customerInfoDto.getId()+""));
		}

		PageInfo pageInfo =  supplierService.getPageInfo(pageNo, pageSize, supplierExample);
		return pageInfo;
	}

	/**
	 * 跟姐姐id查询供应商信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getSupplier/{id}")
	public Response getSupplier(@PathVariable Integer id){
		SupplierDto supplierDto = supplierService.getById(id);
		return Response.success(supplierDto);
	}
	/**
	 * 添加供应商
	 * @return
	 */
	@RequestMapping("/saveSupplier")
	public Response addSupplier(@RequestBody SupplierDto supplierDto, HttpSession session){



		Integer provinceId = supplierDto.getProvinceId();
		Integer cityId = supplierDto.getCityId();
		Integer areaId = supplierDto.getAreaId();

		ProvinceCityAreaDto province =  provinceCityAreaService.getById(provinceId);
		ProvinceCityAreaDto city =  provinceCityAreaService.getById(cityId);
		ProvinceCityAreaDto area =  provinceCityAreaService.getById(areaId);
		supplierDto.setProvince(province.getName());
		supplierDto.setCity(city.getName());
		if(null != area){
			supplierDto.setArea(area.getName());
		}


		if(supplierDto.getId() == null){
			CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
			supplierDto.setCreatorId(customerInfoDto.getId());
			supplierDto.setCreatorName(customerInfoDto.getNickName());
			supplierDto.setAppInfoId(AppUtils.get());
			supplierService.save(supplierDto);
		}else{
			supplierService.update(supplierDto);
		}


		return Response.success();
	}


}
