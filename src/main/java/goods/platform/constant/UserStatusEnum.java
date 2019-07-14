package goods.platform.constant;
/**
 * 用户状态
 * @author 孙龙云
 */
public enum UserStatusEnum implements BaseServiceEnum{
	NORMAL(1,"正常"),
	FORZEN(2, "冻结"),
	DELETED(-1, "已删除");
	private int code;
	private String value;
	UserStatusEnum(int code, String value){
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
