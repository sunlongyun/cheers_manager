package goods.platform.scheduling;

import com.chisong.green.farm.app.constants.enums.OrderStatusEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.GoodsSpecsDto;
import com.chisong.green.farm.app.dto.OrderDetailDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.service.GoodsSpecsService;
import com.chisong.green.farm.app.service.OrderInfoService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2020-04-12 11:40
 */
@Component
@Slf4j
public class OrderInfoSchedulingJob {

	@Autowired
	private OrderInfoService orderInfoService;
	private Integer pageNo = 1;
	private Integer pageSize = 100;
	@Autowired
	private GoodsSpecsService goodsSpecsService;

	//每5分钟检查一次订单状态
	//1.下单半个小时未付款的订单，自动取消。取消后释放商品数量占用
	//2.发货10天以上的订单，自动确认收货
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void updateOrderStatus() {
		//1.取消未支付订单
		notPayedAfterHalfHour();

		//2.发货十天以上，未确认收货的订单，自动确认收货
		confirmOrders();
	}

	/**
	 * 超过十天的订单，自动确认收货
	 */
	private void confirmOrders(){
		//卖家发货超过十天的订单，如果顾客未确认收货，则系统自动确认
		LocalDateTime before10Days = LocalDateTime.now().minus(10,
			ChronoUnit.DAYS);
		;	OrderInfoExample orderInfoExample = new OrderInfoExample();
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andStatusEqualTo(OrderStatusEnum.DELIVERY.code())
			.andCreateTimeLessThan(Date.from(before10Days.atZone(ZoneId.systemDefault()).toInstant()));

		List<OrderInfoDto> orderInfoDtoList =
			orderInfoService.getPageInfo(pageNo, pageSize, orderInfoExample).getDataList();
		orderInfoDtoList.stream().forEach(orderInfoDto -> {
			orderInfoDto.setStatus(OrderStatusEnum.RECEIVED.code());
			orderInfoService.update(orderInfoDto);
		});
	}
	/**
	 * 半个小时内未支付订单撤销
	 */
	private void notPayedAfterHalfHour() {
		//半个小时之前
		LocalDateTime beforeHalfHour = LocalDateTime.now().minus(30,
			ChronoUnit.MINUTES);
		;
		OrderInfoExample orderInfoExample = new OrderInfoExample();
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andStatusEqualTo(OrderStatusEnum.UN_PAY.code())
			.andCreateTimeLessThan(Date.from(beforeHalfHour.atZone(ZoneId.systemDefault()).toInstant()));
		List<OrderInfoDto> orderInfoDtoList =
			orderInfoService.getPageInfo(pageNo, pageSize, orderInfoExample).getDataList();
		orderInfoDtoList.stream().forEach(orderInfoDto -> {
			orderInfoDto.setStatus(OrderStatusEnum.DELETED.code());
			orderInfoDto.setCancelRemark("超时未付款,系统取消");
			orderInfoService.update(orderInfoDto);
			//商品库存需要加回去
			List<OrderDetailDto> orderDetailDtoList = orderInfoDto.getOrderDetailDtoList();
			if(null != orderDetailDtoList) {
				orderDetailDtoList.stream().forEach(orderDetailDto -> {
					GoodsSpecsDto goodsSpecsDto = goodsSpecsService.getById(orderDetailDto.getSpecsId());
					goodsSpecsDto.setStock(goodsSpecsDto.getStock() + orderDetailDto.getNum());
					goodsSpecsService.update(goodsSpecsDto);
				});
			}
		});
	}
}
