package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.GoodsDto;
import com.chisong.green.farm.app.dto.SupplierDto;
import com.chisong.green.farm.app.example.GoodsExample;
import com.chisong.green.farm.app.service.GoodsService;
import com.chisong.green.farm.app.service.SupplierService;
import com.chisong.green.farm.exception.BizException;
import com.lianshang.generator.commons.PageInfo;
import com.sun.javafx.binding.StringFormatter;
import goods.platform.commons.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private SupplierService supplierService;

	@RequestMapping("/save")
	public Response saveGoods(@RequestBody GoodsDto goodsDto){
		SupplierDto supplierDto =  supplierService.getById(goodsDto.getSupplierId());
		goodsDto.setSupplierCompanyName(supplierDto.getCompanyName());
		goodsDto.setCountryName("中国");
		goodsDto.setPrice(goodsDto.getPrice() * 100);
		goodsDto.setOriginPrice(goodsDto.getOriginPrice() * 100);
		String skuCode = "BE"+new SimpleDateFormat("yyyyMMddHHMMss_").format(new Date())
			+(int)(Math.random()*1000);
		goodsDto.setSkuCode(skuCode);
		goodsDto.setDetailPicUrl(goodsDto.getPicUrl());
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
		String title, String enTitle, String countryName, String chateauTitle, String supplierCompanyName) {
		GoodsExample goodsExample = new GoodsExample();
		GoodsExample.Criteria criteria = goodsExample.createCriteria();
		goodsExample.setOrderByClause("id desc");
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
