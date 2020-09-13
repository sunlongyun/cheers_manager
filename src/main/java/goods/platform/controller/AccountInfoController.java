package goods.platform.controller;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.example.WithDrawApplyExample;
import com.chisong.green.farm.app.example.WithDrawApplyExample.Criteria;
import com.chisong.green.farm.app.service.WithDrawApplyService;
import com.lianshang.generator.commons.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 账户管理controller
 * @AUTHOR 孙龙云
 * @date 2020-07-01 15:44
 */
@RestController("adminAccountInfoController")
@RequestMapping("/accountManage")
public class AccountInfoController {

	@Autowired
	private WithDrawApplyService withDrawApplyService;

	@RequestMapping("getPageInfo")
	public PageInfo getPageInfo(int pageNo, int pageSize,String mobile,  String nickName, String realName,
		Integer status){

		WithDrawApplyExample withDrawApplyExample = new WithDrawApplyExample();
		Criteria criteria = withDrawApplyExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		if(!StringUtils.isEmpty(mobile)){
			criteria.andMobileLike("%"+mobile+"%");
		}
		if(!StringUtils.isEmpty(nickName)){
			criteria.andNickNameLike("%"+nickName+"%");
		}
		if(!StringUtils.isEmpty(realName)){
			criteria.andRealNameLike("%"+nickName+"%");
		}
		if(null != status){
			criteria.andStatusEqualTo(status);
		}
		PageInfo pageInfo = withDrawApplyService.getPageInfo(pageNo, pageSize,withDrawApplyExample);
		return pageInfo;
	}
}
