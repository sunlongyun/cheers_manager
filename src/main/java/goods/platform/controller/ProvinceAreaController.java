package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.ProvinceCityAreaDto;
import com.chisong.green.farm.app.example.ProvinceCityAreaExample;
import com.chisong.green.farm.app.service.ProvinceCityAreaService;
import goods.platform.commons.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 省市区管理
 * @AUTHOR 孙龙云
 * @date 2019-11-10 12:01
 */
@RestController
@RequestMapping("/area")
public class ProvinceAreaController {

	public static final int CHINA = 0;
	@Autowired
	private ProvinceCityAreaService provinceCityAreaService;
	/**
	 * 获取省份列表
	 * @return
	 */
	@RequestMapping("/getProvinceList")
	public Response getProvinceList(){
		return getList(CHINA);
	}

	@RequestMapping("/list/{parentId}")
	public Response getList(@PathVariable("parentId") Integer parentId){
		ProvinceCityAreaExample provinceCityAreaExample = new ProvinceCityAreaExample();
		provinceCityAreaExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andPidEqualTo(parentId);
		List<ProvinceCityAreaDto> list = provinceCityAreaService.getList(provinceCityAreaExample);
		 return Response.success(list);
	}
}
