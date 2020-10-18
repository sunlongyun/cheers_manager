package goods.platform.scheduling;

import com.chisong.green.farm.app.example.MerchantPaymentExample;
import com.chisong.green.farm.app.service.MerchantPaymentService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 转账任务
 * @AUTHOR 孙龙云
 * @date 2020-10-06 16:51
 */
@Component
@Slf4j
public class TransferSchedulingJob {
	@Autowired
	private MerchantPaymentService merchantPaymentService;

	/**
	 * 15秒扫码一次待转账记录
	 */
	@Scheduled(fixedRate =  15 * 1000)
	public void transder(){
		MerchantPaymentExample merchantPaymentExample = new MerchantPaymentExample();
		merchantPaymentExample.createCriteria().andValidityEqualTo(Boolean.TRUE)
			.andPreTransferTimeLessThan(new Date());

		merchantPaymentService.getList(merchantPaymentExample).stream().forEach(merchantPaymentDto -> {
			if(merchantPaymentDto.getAmount() <100){//一元之内无法结算
				merchantPaymentDto.setStatus(2);
				merchantPaymentService.update(merchantPaymentDto);
			}else{
				try {
					merchantPaymentService.tranfer(merchantPaymentDto.getId());
				}catch(Exception e){
					log.error("转账失败", e);
				}

			}

		});
	}
}
