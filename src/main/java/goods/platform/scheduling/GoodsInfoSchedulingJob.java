package goods.platform.scheduling;

import com.chisong.green.farm.app.constants.enums.OrderStatusEnum;
import com.chisong.green.farm.app.constants.enums.Validity;
import com.chisong.green.farm.app.dto.GoodsDto;
import com.chisong.green.farm.app.dto.GoodsSpecsDto;
import com.chisong.green.farm.app.dto.OrderDetailDto;
import com.chisong.green.farm.app.example.GoodsExample;
import com.chisong.green.farm.app.example.GoodsSpecsExample;
import com.chisong.green.farm.app.example.OrderInfoExample;
import com.chisong.green.farm.app.service.GoodsService;
import com.chisong.green.farm.app.service.GoodsSpecsService;
import com.chisong.green.farm.app.service.OrderDetailService;
import com.chisong.green.farm.app.service.OrderInfoService;
import com.lianshang.generator.commons.PageInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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
public class GoodsInfoSchedulingJob {

	@Autowired
	private GoodsSpecsService goodsSpecsService;
	@Autowired
	private    GoodsService goodsService;
	@Autowired
	private OrderDetailService orderDetailService;
	/**
	 * 维护商品状态
	 */
	@Scheduled(fixedRate = 3 * 60 * 1000)
	public void updateGoodsInfo() {
		log.info("开始更新商品促销状态-----");
		//开始促销活动
		startPromote();
		//促销活动到期
		endPromote();
	}

	//更新商品的权重
	@Scheduled(cron = "30 59 23 * * ? ")
	public void updateWeight(){
		//1.不再权重保护期内的商品，
		//2. 根据总盈利额排序 60%
		//3.根据总销售量排序  40%
		log.info("开始更新商品权重-----");
		int pageNo = 1;
		int pageSize = 10;
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code());
//		goodsExample.or(goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
//		.andWeightProjectTimeLessThan(new Date()));

		 boolean hasMore= true;
		 List<GoodsDto> goodsDtos = new ArrayList<>();
		while(hasMore){
			PageInfo pageInfo = goodsService.getPageInfo(pageNo++, pageSize,goodsExample);
			goodsDtos.addAll(pageInfo.getDataList());
			hasMore = pageInfo.getHasMore();
		}

		int total = goodsDtos.size();
		log.info("商品总数量:{}", total);
		AtomicInteger index = new AtomicInteger(total);
		goodsDtos.stream().sorted((a,b)->{
			//查询总销售量
			//查询总盈利额
			double aWeight = ((a.getSalesNum()==null?0:a.getSalesNum())*0.4)
				+ (a.getSalesAmount() == null ?0:a.getSalesAmount())*0.6;

			double bWeight = ((b.getSalesNum()==null?0:b.getSalesNum())*0.4)
				+ (b.getSalesAmount() == null ?0:b.getSalesAmount())*0.6;
			return  (int)(aWeight -bWeight);
		}).forEach(goodsDto -> {

			int size = total>100 ?total/100:1;
			int weight = index.get()/size;

			goodsDto.setWeight(weight);
			if(null == goodsDto.getWeightProjectTime() ||  goodsDto.getWeightProjectTime().before(new Date())){
				goodsService.update(goodsDto);
			}
			index.set(index.get()- size);
		});
	}

	/**
	 * 开始活动到期
	 */
	private void startPromote() {
		LocalDateTime now = LocalDateTime.now();

		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andPromoteStartTimeIsNotNull().andPromoteEndTimeIsNotNull()
			.andPromoteStartTimeLessThan(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
			.andPromoteEndTimeGreaterThan(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
			.andPromoteEqualTo(0);

		goodsService.getList(goodsExample).forEach(goodsDto -> {

			GoodsSpecsExample goodsSpecsExample = new GoodsSpecsExample();
			goodsSpecsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
				.andGoodsIdEqualTo(goodsDto.getId());

			List<GoodsSpecsDto> goodsSpecsDtos = goodsSpecsService.getList(goodsSpecsExample);
			AtomicInteger atomicInteger = new AtomicInteger(0);

			goodsSpecsDtos.forEach(goodsSpecs->{
				goodsSpecs.setPromote(1);
				int index = atomicInteger.get();
				GoodsSpecsDto lowGoodsSpecsDto = goodsSpecsDtos.get(index);
				if(lowGoodsSpecsDto.getPromotionPrice() < goodsSpecs.getPromotionPrice()){
					atomicInteger.set(goodsSpecsDtos.indexOf(goodsSpecs));
				}
				goodsSpecsService.update(goodsSpecs);
			});

			goodsDto.setPromote(true);
			GoodsSpecsDto lowGoodsSpecsDto = goodsSpecsDtos.get(atomicInteger.get());
			goodsDto.setPromotePrice(Long.parseLong(lowGoodsSpecsDto.getPromotionPrice()+""));
			goodsDto.setPrice(Long.parseLong(lowGoodsSpecsDto.getPrice()+"") );

			goodsService.update(goodsDto);
		});
	}

	/**
	 * 促销活动到期
	 */
	private void endPromote() {
		LocalDateTime now = LocalDateTime.now();

		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
			.andPromoteStartTimeIsNotNull().andPromoteEndTimeIsNotNull()
			.andPromoteEndTimeLessThan(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
			.andPromoteEqualTo(1);

			goodsService.getList(goodsExample).forEach(goodsDto -> {


			GoodsSpecsExample goodsSpecsExample = new GoodsSpecsExample();
			goodsSpecsExample.createCriteria().andValidityEqualTo(Validity.AVAIL.code())
				.andGoodsIdEqualTo(goodsDto.getId()).andPromoteEqualTo(1);

			goodsSpecsService.getList(goodsSpecsExample).forEach(goodsSpecs->{
				goodsSpecs.setPromotionPrice(goodsSpecs.getPrice());
				goodsSpecs.setPromote(0);
				goodsSpecsService.update(goodsSpecs);

			});

			goodsDto.setPromotePrice(goodsDto.getPrice());
			goodsDto.setPromote(false);

			goodsService.update(goodsDto);
		});
	}


}
