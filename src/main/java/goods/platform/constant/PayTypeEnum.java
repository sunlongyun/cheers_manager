package goods.platform.constant;

import com.chisong.green.farm.app.constants.enums.BaseServiceEnum;

/**
 * 支付类型
 * @author 孙龙云
 */
public enum PayTypeEnum implements BaseServiceEnum {

	SHOP("shop","店内支付"),
	WECHAT("wechat","微信支付");

	private String code;
	private String value;
	PayTypeEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
	
	public String code(){
		return this.code;
	}
	public String value(){
		return this.value;
	}
}
