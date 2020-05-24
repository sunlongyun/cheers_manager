package goods.platform.scheduling;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.AccountInfoDto;
import com.chisong.green.farm.app.example.CustomerInfoExample;
import com.chisong.green.farm.app.service.AccountInfoService;
import com.chisong.green.farm.app.service.CustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 对于已授权通过的用户，自动创建账户
 * @AUTHOR 孙龙云
 * @date 2020-05-23 10:53
 */
//@Component
public class AccountInfoSchedulingJob {

 @Autowired
 private AccountInfoService accountInfoService;

 @Autowired
 private CustomerInfoService customerInfoService;
 /**
  * 对于已经授权通过的顾客，自动创建账户
  */
// @Scheduled(fixedRate = 3* 60 * 60 * 1000)
 public void updateGoodsInfo() {
  CustomerInfoExample customerInfoExample = new CustomerInfoExample();
  customerInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code()).andAvatarUrlIsNotNull();

  customerInfoService.getList(customerInfoExample).forEach(customerInfoDto -> {
     accountInfoService.createAccountInfo(customerInfoDto);
  });
 }
}