package goods.platform.constant;
/**
 * 请求数据流动方向
 * @author 孙龙云
 *
 */
public enum ReqLogDirectionEnum {
	IN(0, "流入"), OUT(1, "流出");
	private int code;
	private String msg;
	ReqLogDirectionEnum(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	public int code(){
		return this.code;
	}
	public String msg(){
		return this.msg;
	}
}
