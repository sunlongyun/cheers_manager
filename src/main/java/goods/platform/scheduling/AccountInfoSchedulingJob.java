package goods.platform.scheduling;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.AccountInfoDto;
import com.chisong.green.farm.app.example.AccountFlowExample;
import com.chisong.green.farm.app.example.CustomerInfoExample;
import com.chisong.green.farm.app.service.AccountFlowService;
import com.chisong.green.farm.app.service.AccountInfoService;
import com.chisong.green.farm.app.service.CustomerInfoService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 自动入账
 * @AUTHOR 孙龙云
 * @date 2020-05-23 10:53
 */
@Component
@Slf4j
public class AccountInfoSchedulingJob {

 @Autowired
 private AccountInfoService accountInfoService;

 @Autowired
 private CustomerInfoService customerInfoService;

 @Autowired
 private AccountFlowService accountFlowService;
 /**
  * 到期的待入账流水入账
  */
 @Scheduled(fixedRate = 5 * 60 * 1000)
 public void incomeAccountFlow() {
   AccountFlowExample accountFlowExample = new AccountFlowExample();
   accountFlowExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
       .andTypeEqualTo(1) .andStatusEqualTo(0).andInAccountTimeLessThan(new Date());

   accountFlowService.getList(accountFlowExample).stream().forEach(accountFlowDto -> {
    try {
     accountFlowService.inCome(accountFlowDto);
    }catch(Exception ex){
     log.error("入账失败,", ex);
    }

   });
 }

    /**
     * 到期的提现中的金额，执行转出
     */
    @Scheduled(fixedRate =10 * 60 * 1000)
    public void widthDrawAmountFlow() {
        AccountFlowExample accountFlowExample = new AccountFlowExample();
        accountFlowExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
            .andTypeEqualTo(0).andSourceEqualTo(1)
            .andStatusEqualTo(0).andInAccountTimeLessThan(new Date());
        try {
            accountFlowService.getList(accountFlowExample).stream().forEach(accountFlowDto -> {
                accountFlowService.withdraw(accountFlowDto);
            });
        }catch(Exception ex){
            log.error("提现出现异常，请注意,", ex);
            throw  ex;
        }

    }
}