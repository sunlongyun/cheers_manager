package goods.platform.constant;

import com.caichao.chateau.app.constants.enums.BaseServiceEnum;

/**
 * 支付状态 0-未支付，1-支付成功
 * @author 孙龙云
 */
public enum PayStatusEnum implements BaseServiceEnum {

	NO_APY(0,"未支付"),
	PAYED(1, "支付成功"),
	PART_PAYED(2, "部分支付");
	private int code;
	private String value;
	PayStatusEnum(int code, String value){
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
