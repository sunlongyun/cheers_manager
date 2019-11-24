package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.PostageTemplateDto;
import com.chisong.green.farm.app.example.PostageTemplateExample;
import com.chisong.green.farm.app.service.PostageTemplateService;
import goods.platform.commons.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 费项模板管理
 * @AUTHOR 孙龙云
 * @date 2019-11-24 18:30
 */
@RestController
@RequestMapping("/template")
@Slf4j
public class FeeTemplateController {
	@Autowired
	private PostageTemplateService postageTemplateService;

	/**
	 * 查询商品下的快递费模板配置列表
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/postage-template/list/{goodsId}")
	public Response getPostageTemplateList(@PathVariable Long goodsId){
		PostageTemplateExample postageTemplateExample = new PostageTemplateExample();
		postageTemplateExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andGoodsIdEqualTo(goodsId);
		List<PostageTemplateDto> list =  postageTemplateService.getList(postageTemplateExample);
		return Response.success(list);
	}

	/**
	 * 保存模板设置
	 * @param postageTemplateDtos
	 * @return
	 */
	@RequestMapping("/postage-template/save")
	public Response saveTemplate(@RequestBody List<PostageTemplateDto> postageTemplateDtos){
		log.info("postageTemplateDtos:{}", postageTemplateDtos);
		postageTemplateService.savePostageTemplateDtoList(postageTemplateDtos);
		return Response.success();
	}
}
