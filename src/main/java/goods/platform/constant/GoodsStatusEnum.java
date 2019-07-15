package goods.platform.constant;

import com.caichao.chateau.app.constants.enums.BaseServiceEnum;

/**
 * 商品状态
 * @author 孙龙云
 */
public enum GoodsStatusEnum implements BaseServiceEnum {
	NORMAL(1,"正常"),
	FORZEN(2, "已下架"),
	DELETED(-1, "已删除");
	private int code;
	private String value;
	GoodsStatusEnum(int code, String value){
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
