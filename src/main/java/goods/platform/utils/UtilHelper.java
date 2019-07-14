package goods.platform.utils;

import org.apache.commons.codec.binary.Base64;
/**
 * bytes 和base64转换工具
 * @author 孙龙云
 *
 */
public class UtilHelper {
	//base64字符串转byte[]
	public static byte[] base64String2ByteFun(String base64Str){
		return Base64.decodeBase64(base64Str);
	}
	//byte[]转base64
	public static String byte2Base64StringFun(byte[] b){
		return Base64.encodeBase64String(b);
	}

}
