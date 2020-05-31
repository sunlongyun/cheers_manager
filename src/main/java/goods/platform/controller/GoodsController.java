package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.UserTypeEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.dto.GoodsDto;
import com.chisong.green.farm.app.dto.SupplierDto;
import com.chisong.green.farm.app.example.GoodsExample;
import com.chisong.green.farm.app.example.SupplierExample;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.chisong.green.farm.app.service.GoodsService;
import com.chisong.green.farm.app.service.SupplierService;
import com.chisong.green.farm.exception.BizException;
import com.lianshang.generator.commons.PageInfo;
import com.sun.javafx.binding.StringFormatter;
import goods.platform.commons.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
@Slf4j
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private SupplierService supplierService;

	@Autowired
	private CustomerInfoService customerInfoService;

	@RequestMapping("/save")
	public Response saveGoods(@RequestBody GoodsDto goodsDto){

		log.info("goodsDto :{}", goodsDto);

		SupplierDto supplierDto =  supplierService.getById(goodsDto.getSupplierId());
		goodsDto.setSupplierCompanyName(supplierDto.getCompanyName());
		String skuCode = "BE"+new SimpleDateFormat("yyyyMMddHHMMss_").format(new Date())
			+(int)(Math.random()*1000);
		goodsDto.setSkuCode(skuCode);
		goodsDto.setDetailPicUrl(goodsDto.getPicUrl());
		if(StringUtils.isEmpty(goodsDto.getMinPicUrl())){
			goodsDto.setMinPicUrl(goodsDto.getPicUrl());
		}
		goodsService.saveOrUpdateGoods(goodsDto);

		return Response.success();
	}

	/**
	 * 根据id查询商品详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail/{id}")
	public Response getDetail(@PathVariable Long id){
		GoodsDto goodsDto = goodsService.getDetailById(id);
		return Response.success(goodsDto);
	}
	/**
	 * 改变商品状态，（上架，下架)
	 * @param goodsId
	 * @param status
	 * @return
	 */
	@RequestMapping("/changeStatus/{goodsId}/{status}")
	public Response changeStatus(@PathVariable Long goodsId, @PathVariable Integer status){
		GoodsDto goodsDto = goodsService.getDetailById(goodsId);
		if(null == goodsDto){
			throw new RuntimeException("商品不存在");
		}
		goodsDto.setStatus(status);
		goodsService.update(goodsDto);
		return Response.success();
	}
	/**
	 * 分页查询
	 */
	@RequestMapping("getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize,
		String title, String produceArea, String countryName,
		String supplierCompanyName, Integer salesNumStart, Integer salesNumEnd, HttpSession session) {
		CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
		Integer customerId = null;
		if(customerInfoDto.getType() != UserTypeEnum.ADMIN.code()
			&& customerInfoDto.getType() != UserTypeEnum.SUPER_ADMIN.code()
		&&  customerInfoDto.getType() != UserTypeEnum.SALER_AND_CUSTOMER.code()){
			throw new RuntimeException("只有管理员用户才可以查询供应商");
		}

		if(customerInfoDto.getType() == UserTypeEnum.ADMIN.code()
			||  customerInfoDto.getType() == UserTypeEnum.SALER_AND_CUSTOMER.code()
		){
			customerId = Integer.parseInt(customerInfoDto.getId()+"");
		}

		GoodsExample goodsExample = getGoodsExample(title, produceArea, countryName, supplierCompanyName, salesNumStart,
			salesNumEnd,  customerId);
		PageInfo pageInfo = goodsService.getPageInfo(pageNo, pageSize, goodsExample);

		return pageInfo;
	}

	private GoodsExample getGoodsExample(String title, String produceArea, String countryName,
		String supplierCompanyName, Integer salesNumStart, Integer salesNumEnd, Integer customerId) {
		GoodsExample goodsExample = new GoodsExample();
		GoodsExample.Criteria criteria = goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		goodsExample.setOrderByClause("id desc");

		if(!StringUtils.isEmpty(title)) {
			criteria.andTitleLike("%" + title + "%");
		}
		if(!StringUtils.isEmpty(produceArea)) {
			criteria.andProduceAreaLike("%" + produceArea + "%");
		}


		if(!StringUtils.isEmpty(supplierCompanyName)) {
			criteria.andSupplierCompanyNameLike("%" + supplierCompanyName + "%");
		}
		if(null != salesNumStart){
			criteria.andSalesNumGreaterThanOrEqualTo(salesNumStart);
		}
		if(null != salesNumEnd){
			criteria.andSalesNumLessThanOrEqualTo(salesNumEnd);
		}
		if(null != customerId){
			SupplierExample supplierExample = new SupplierExample();
			supplierExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
				.andCreatorIdEqualTo(customerId);
			criteria.andSupplierIdIn(supplierService.getList(supplierExample).stream().map(SupplierDto::getId).collect(Collectors.toList()));
		}
		return goodsExample;
	}
}
