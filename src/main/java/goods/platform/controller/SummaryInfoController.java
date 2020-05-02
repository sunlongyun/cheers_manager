package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.SummaryInfoDto;
import com.chisong.green.farm.app.example.SummaryInfoExample;
import com.chisong.green.farm.app.service.SummaryInfoService;
import javax.xml.ws.soap.Addressing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2020-05-02 16:58
 */
@RestController
@RequestMapping("/admin/summary")
public class SummaryInfoController {
	@Autowired
	private SummaryInfoService summaryInfoService;
	@RequestMapping("/info")
	public SummaryInfoDto getInfo(){
		SummaryInfoExample summaryInfoExample = new SummaryInfoExample();
		summaryInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		return summaryInfoService.getList(summaryInfoExample).get(0);
	}
}
