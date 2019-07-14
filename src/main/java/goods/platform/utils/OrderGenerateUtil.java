package goods.platform.utils;

import java.util.UUID;
/**
 * 点餐订单生成器
 * @author 孙龙云
 * 2018年6月23日 下午2:54:50
 */
public class OrderGenerateUtil {
	/**
	 * 获取订单
	 * @return
	 */
	public static String getOrder(String shopOpenId, String tableNo, String userOpenId){
		String orderNo = shopOpenId+"_"+tableNo+"_"+userOpenId+"_"+System.currentTimeMillis()+"_"+UUID.randomUUID();
		return orderNo;
	}
}
