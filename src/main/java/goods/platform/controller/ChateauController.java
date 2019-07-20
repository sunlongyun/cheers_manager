package goods.platform.controller;

import com.caichao.chateau.app.constants.enums.Validity;
import com.caichao.chateau.app.dto.CountryChateauDto;
import com.caichao.chateau.app.example.CountryChateauExample;
import com.caichao.chateau.app.example.CountryChateauExample.Criteria;
import com.caichao.chateau.app.service.CountryChateauService;
import com.lianshang.generator.commons.PageInfo;
import goods.platform.utils.QrCodeUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 酒庄管理controller
 * @AUTHOR 孙龙云
 * @date 2019-07-19 23:18
 */
@RequestMapping("/admin/chateau")
@RestController("admin_chateauController")
public class ChateauController {

	@Autowired
	private CountryChateauService countryChateauService;
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping("/getPageInfo")
	public PageInfo getPageInfo(Integer pageNo, Integer pageSize, String title, String countryName){

		CountryChateauExample countryChateauExample = new CountryChateauExample();
		Criteria criteria = countryChateauExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		if(!StringUtils.isEmpty(title)){
			criteria.andTitleLike("%"+title+"%");
		}
		if(!StringUtils.isEmpty(countryName)){
			criteria.andCountryNameLike("%"+countryName+"%");
		}

		PageInfo<CountryChateauDto> pageInfo = countryChateauService.getPageInfo(pageNo, pageSize, countryChateauExample);
		pageInfo.getDataList().forEach(countryChateauDto -> {
			countryChateauDto.setDailyBroadcastPusher(countryChateauService.getDailyPusherUlr(countryChateauDto.getId()));
			countryChateauDto.setMasterBroadcastPusher(countryChateauService.getMasterPusherUlr(countryChateauDto.getId()));
		});
		return pageInfo;
	}

	@RequestMapping("/showQrCode")
	public void showQrCode(String content, Integer width, Integer height, HttpServletResponse httpServletResponse){

		try {
			QrCodeUtil.getQrcode(content,width,height,httpServletResponse);
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}
