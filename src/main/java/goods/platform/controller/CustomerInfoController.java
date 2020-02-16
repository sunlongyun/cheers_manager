package goods.platform.controller;
import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.SupplierDto;
import com.chisong.green.farm.app.example.CustomerInfoExample;
import com.chisong.green.farm.app.example.CustomerInfoExample.Criteria;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.chisong.green.farm.app.service.SupplierService;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.commons.Response;
import java.util.Arrays;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 商品管理controller
 * @author 孙龙云
 *
 */

@RequestMapping("/admin/customer")
@RestController
public class CustomerInfoController {

	public static final int ACCOUNT_TAG = 1;
	@Autowired
	private CustomerInfoService customerInfoService;
	@Autowired
	private SupplierService supplierService;

	@PostMapping("/bindSupplier/{id}/{supplierId}")
	public Response bindSupplier(@PathVariable Long id, @PathVariable Integer supplierId, Integer primary){
		CustomerInfoDto customerInfoDto=  customerInfoService.getById(id);
		if(null == customerInfoDto){
			throw new RuntimeException("客户信息不存在");
		}
	    SupplierDto supplierDto =  supplierService.getById(supplierId);
		customerInfoDto.setSupplierId(supplierId);
		customerInfoDto.setCompanyName(supplierDto.getCompanyName());

		if(primary == ACCOUNT_TAG){
			CustomerInfoExample customerInfoExample = new CustomerInfoExample();
			customerInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
				.andSupplierIdEqualTo(supplierId);

			customerInfoService.getList(customerInfoExample).forEach(customerInfoDto1 -> {
				customerInfoDto1.setSupplierAccount(false);
				customerInfoService.update(customerInfoDto1);
			});

			customerInfoDto.setSupplierAccount(true);
		}

		customerInfoService.update(customerInfoDto);
		return Response.success();
	}
	/**
	 * 添加客户信息
	 * @param customerInfo
	 * @param session
	 * @return
	 */
	@RequestMapping("/save")
	public Response saveCustomerInfo(CustomerInfoDto customerInfo, HttpSession session){

		CustomerInfoDto adminUser = (CustomerInfoDto) session.getAttribute("adminUser");

//		if(null == customerInfo.getId()){
//			customerInfo.setCreateId(adminUser.getId());
//			customerInfo.setCreator(adminUser.getUserName());
//			customerInfoService.saveCustomerInfo(customerInfo);
//		}else{
//			customerInfoService.updateCustomerInfo(customerInfo);
//		}
		return Response.success();
	}

	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param userName
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String userName, String mobile, String companyName){

		CustomerInfoExample customerInfoExample = new CustomerInfoExample();
		Criteria criteria =  customerInfoExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code()).andTypeIn(Arrays.asList(UserTypeEnum.CUSTOMER.code()));;
		if(!StringUtils.isEmpty(userName)){
			criteria.andUserNameLike("%"+userName+"%");
		}
		if(!StringUtils.isEmpty(mobile)){
			criteria.andMobileLike("%"+mobile+"%");
		}
		PageInfo pageInfo = customerInfoService.getPageInfo(pageNo, pageSize, customerInfoExample);
		return pageInfo;
	}


}
