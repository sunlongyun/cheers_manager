package goods.platform.scheduling;

import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.GoodsDto;
import com.chisong.green.farm.app.dto.GoodsSpecsDto;
import com.chisong.green.farm.app.dto.OrderInfoDto;
import com.chisong.green.farm.app.dto.SummaryInfoDto;
import com.chisong.green.farm.app.entity.Goods;
import com.chisong.green.farm.app.example.CustomerInfoExample;
import com.chisong.green.farm.app.example.CustomerInfoExample.Criteria;
import com.chisong.green.farm.app.example.GoodsExample;
import com.chisong.green.farm.app.example.GoodsSpecsExample;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.example.SummaryInfoExample;
import com.chisong.green.farm.app.example.SupplierExample;
import com.chisong.green.farm.app.service.CustomerInfoService;
import com.chisong.green.farm.app.service.GoodsService;
import com.chisong.green.farm.app.service.GoodsSpecsService;
import com.chisong.green.farm.app.service.OrderDetailService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.chisong.green.farm.app.service.SummaryInfoService;
import com.chisong.green.farm.app.service.SupplierService;
import com.lianshang.generator.commons.PageInfo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicInteger;
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
public class SummeryInfoSchedulingJob {

	@Autowired
	private    GoodsService goodsService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private CustomerInfoService customerInfoService;
	@Autowired
	private SummaryInfoService summaryInfoService;
	@Autowired
	private OrderInfoService orderInfoService;
	/**
	 * 每两个小时更新一次汇总统计信息
	 */
	@Scheduled(fixedRate = 3 * 60 * 60 * 1000)
	public void updateGoodsInfo() {
		log.info("更新统计汇总-----");
		SummaryInfoExample summaryInfoExample = new SummaryInfoExample();
		summaryInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		List<SummaryInfoDto> summaryInfoDtos = summaryInfoService.getList(summaryInfoExample);
		summaryInfoDtos.stream().forEach(summaryInfoDto -> {
			//1.商品总数量
			buildGoodsNum(summaryInfoDto);

//		//2. 订单总数量
			buildOrderNum(summaryInfoDto);

			//3.支付总金额
			buildPayedAmount(summaryInfoDto);

			//4.供应商数量
			buildSupplierNum(summaryInfoDto);

			//5.顾客数量
			buildCustomerNum(summaryInfoDto);




			//日开始、结束日期
			LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
			LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

			//周 开始、结束日期
			TemporalAdjuster FIRST_OF_WEEK =
				TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue()- DayOfWeek.MONDAY.getValue()));
			LocalDateTime weekStart = LocalDateTime.now().with(FIRST_OF_WEEK);
			TemporalAdjuster LAST_OF_WEEK =
				TemporalAdjusters.ofDateAdjuster(localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
			LocalDateTime weekEnd = LocalDateTime.now().with(LAST_OF_WEEK);

			//月开始、结束日期
			LocalDateTime monthStart = LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth(),1, 0
				,0,01);

			LocalDateTime monthEnd =  LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
				LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(), 23,59,59)  ;

			//6. 本日、本周、本月新增粉丝
			summaryInfoDto.setTodayCustomerNum(buildTodayWeekMonthCustomerNum(todayStart, todayEnd, summaryInfoDto.getAppInfoId()));
			summaryInfoDto.setWeekCustomerNum(buildTodayWeekMonthCustomerNum(weekStart, weekEnd, summaryInfoDto.getAppInfoId()));
			summaryInfoDto.setMonthCustomerNum(buildTodayWeekMonthCustomerNum(monthStart, monthEnd, summaryInfoDto.getAppInfoId()));

			//7.本日、本周、本月新增订单量
			summaryInfoDto.setTodayPayedNum(Long.parseLong(buildTodayWeekMonthOrderNum(todayStart, todayEnd, summaryInfoDto.getAppInfoId())+""));
			summaryInfoDto.setWeekPayedNum(Long.parseLong(buildTodayWeekMonthOrderNum(weekStart, weekEnd, summaryInfoDto.getAppInfoId())+""));
			summaryInfoDto.setMonthPayedNum(Long.parseLong(buildTodayWeekMonthOrderNum(monthStart, monthEnd, summaryInfoDto.getAppInfoId())+""));

			//8. 本日，本周，本月营业额
			summaryInfoDto.setTodayPayedAmount(buildTodayWeekMonthPayedAmount(todayStart, todayEnd, summaryInfoDto.getAppInfoId()));
			summaryInfoDto.setWeekPayedAmount(buildTodayWeekMonthPayedAmount(weekStart, weekEnd, summaryInfoDto.getAppInfoId()));
			summaryInfoDto.setMonthPayedAmount(buildTodayWeekMonthPayedAmount(monthStart, monthEnd, summaryInfoDto.getAppInfoId()));
			summaryInfoService.update(summaryInfoDto);
		});


	}

	private long buildTodayWeekMonthPayedAmount(LocalDateTime todayStart,
		LocalDateTime todayEnd, Long appInfoId) {
		OrderInfoExample orderInfoExample = new OrderInfoExample();
		OrderInfoExample.Criteria criteria = orderInfoExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code());
		criteria.andPayedAmountGreaterThan(0L);
		criteria.andAppInfoIdEqualTo(appInfoId);
		criteria.andCreateTimeGreaterThan(Date.from(todayStart.atZone(ZoneId.systemDefault()).toInstant()));
		criteria.andCreateTimeLessThan(Date.from(todayEnd.atZone(ZoneId.systemDefault()).toInstant()));

		OptionalLong result =
			orderInfoService.getList(orderInfoExample).stream().mapToLong(OrderInfoDto::getPayedAmount).reduce((a,
			b)-> a+b);

		if(result.isPresent()){
			return result.getAsLong();
		}
		return 0l;
	}

	private int buildTodayWeekMonthOrderNum(LocalDateTime todayStart,
		LocalDateTime todayEnd, Long appInfoId) {
		OrderInfoExample orderInfoExample = new OrderInfoExample();
		OrderInfoExample.Criteria criteria = orderInfoExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code());
		criteria.andPayedAmountGreaterThan(0L);
		criteria.andAppInfoIdEqualTo(appInfoId);
		criteria.andCreateTimeGreaterThan(Date.from(todayStart.atZone(ZoneId.systemDefault()).toInstant()));
		criteria.andCreateTimeLessThan(Date.from(todayEnd.atZone(ZoneId.systemDefault()).toInstant()));

		return  orderInfoService.getCount(orderInfoExample);
	}

	private int buildTodayWeekMonthCustomerNum(LocalDateTime todayStart,
		LocalDateTime todayEnd, Long appInfoId) {
		CustomerInfoExample todayCustomerExample = new CustomerInfoExample();
		Criteria criteria = todayCustomerExample.createCriteria();
		criteria.andValidityEqualTo(Validity.AVAIL.code());
		criteria.andAppInfoIdEqualTo(appInfoId);
		criteria.andCreateTimeGreaterThan(Date.from(todayStart.atZone(ZoneId.systemDefault()).toInstant()));
		criteria.andCreateTimeLessThan(Date.from(todayEnd.atZone(ZoneId.systemDefault()).toInstant()));

		return  customerInfoService.getCount(todayCustomerExample);
	}

	private void buildCustomerNum(SummaryInfoDto summaryInfoDto) {
		CustomerInfoExample customerInfoExample = new CustomerInfoExample();
		customerInfoExample.createCriteria().andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId());

		summaryInfoDto.setCustomerNum((Long.parseLong(customerInfoService.getCount(customerInfoExample)+"")));
	}

	private void buildSupplierNum(SummaryInfoDto summaryInfoDto) {
		SupplierExample supplierExample = new SupplierExample();
		supplierExample.createCriteria().andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId())
			.andValidityEqualTo(Validity.AVAIL.code());

		summaryInfoDto.setSupplierNum(supplierService.getCount(supplierExample));
	}

	private void buildPayedAmount(SummaryInfoDto summaryInfoDto) {
		OrderInfoExample orderInfoExample =new OrderInfoExample();
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId());

		summaryInfoDto.setOrderNums(Long.parseLong(orderInfoService.getCount(orderInfoExample)+""));
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId()).andPayedAmountGreaterThan(0L);

		long payed = 0l;

		OptionalLong payedResult =
			orderInfoService.getList(orderInfoExample).stream().mapToLong(OrderInfoDto::getPayedAmount).reduce((a,b)-> a+b);;
		if(payedResult.isPresent()){
			payed = payedResult.getAsLong();
		}
		summaryInfoDto.setPayedAmount(payed);
	}

	private void buildOrderNum(SummaryInfoDto summaryInfoDto) {
		OrderInfoExample orderInfoExample =new OrderInfoExample();
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId());

		summaryInfoDto.setOrderNums(Long.parseLong(orderInfoService.getCount(orderInfoExample)+""));
		orderInfoExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId()).andPayedAmountGreaterThan(0L);
		summaryInfoDto.setPayedNums((Long.parseLong(orderInfoService.getCount(orderInfoExample)+"")));
	}

	private void buildGoodsNum(SummaryInfoDto summaryInfoDto) {
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andAppInfoIdEqualTo(summaryInfoDto.getAppInfoId());
		summaryInfoDto.setGoodsNum(goodsService.getCount(goodsExample));

		;goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
		summaryInfoDto.setGoodsNormalNum( goodsService.getCount(goodsExample));
	}


}
