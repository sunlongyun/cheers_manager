package goods.platform.commons;

import java.io.Serializable;

import goods.platform.constant.ResponseCodeEnum;
import lombok.Data;
/**
 * 返回值
 * @author 孙龙云
 */
@Data
public class Response implements Serializable{
	private static final long serialVersionUID = -7096601129585145021L;
	private String code;
	private String msg;
	private Object data;
	private Response(String code, String msg, Object data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public static Response success(){
		return success("操作成功");
	}
	public static Response success(String msg){
		Response res = new Response(ResponseCodeEnum.SUCCESS.code(), msg, null);
		return res;
	}
	public static Response success(Object data){
		Response res = new Response(ResponseCodeEnum.SUCCESS.code(), ResponseCodeEnum.SUCCESS.msg(), data);
		return res;
	}
	public static Response fail(String msg){
		Response res = new Response(ResponseCodeEnum.FAIL.code(), msg, null);
		return res;
	}
	public static Response fail(){
		return fail(ResponseCodeEnum.FAIL.msg());
	}
	
}
