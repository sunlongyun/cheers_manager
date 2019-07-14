package goods.platform.constant;
/**
 * 用户类型
 * @author 孙龙云
 */
public enum UserTypeEnum implements BaseServiceEnum{
	ADMINPLATFORM(1,"平台管理员"),
	SALER(2, "销售人员"),
	MERCHANT(3, "商户");
	private int code;
	private String value;
	UserTypeEnum(int code, String value){
		this.code = code;
		this.value = value;
	}
	
	public int code(){
		return this.code;
	}
	public String value(){
		return this.value;
	}
	
	/**
	 * 根据code获取value
	 * @param code
	 * @return
	 */
	public static String getValue(int code){
		UserTypeEnum[] values = UserTypeEnum.values();
		for(UserTypeEnum e : values){
			if(e.code == code){
				return e.value;
			}
		}
		return "";
	}
}
