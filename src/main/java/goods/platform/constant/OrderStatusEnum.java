package goods.platform.constant;

import com.caichao.chateau.app.constants.enums.BaseServiceEnum;

/**
 * 订单状态
 * @author 孙龙云
 * 2018年6月23日 下午6:04:59
 */
public enum OrderStatusEnum implements BaseServiceEnum {
	/**
	 * -1：取消，0-初始，1：已提交，2：部分支付, 3：支付完成'
	 */
	CANCEL(-1,"已取消"),
	INIT(0, "初始"),
	COMMIT(1, "已提交"),
	PARTPAY(2,"部分支付"),
	COMPLETE(3, "支付完成"),
	CLOSEED(4, "已关闭");

	private int code;
	private String value;
	OrderStatusEnum(int code, String value){
		this.code = code;
		this.value = value;
	}
	
	public int code(){
		return this.code;
	}
	public String value(){
		return this.value;
	}
}
