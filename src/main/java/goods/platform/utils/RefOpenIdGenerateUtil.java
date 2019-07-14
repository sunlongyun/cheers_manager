package goods.platform.utils;
/**
 * openId生成器
 * @author 孙龙云
 * 2018年6月23日 下午2:44:27
 */
public class RefOpenIdGenerateUtil {
	private static String ADMINUSER = "ad";
	private static String SHOPUSER = "sp";
	private static String SELLER = "sr";
	/**
	 * 获取随机数字
	 * @return
	 */
	private static String getRandom(){
		int r = (int)(Math.random() * 100000);
		StringBuilder str = new StringBuilder();
		str.append(r);
		int l = 5 - str.length();
		for(int i=0;i<l;i++){
			str.insert(0, '0');
		}
		return str.toString();
	}
	/**
	 * 获取平台管理员openId
	 * @return
	 */
	public static String getAdminRefOpenId(){
		return ADMINUSER+"_"+System.currentTimeMillis()+"_"+getRandom();
	}
	/**
	 * 获取商户openId
	 * @return
	 */
	public static String getShopRefOpenId(){
		return SHOPUSER+"_"+System.currentTimeMillis()+"_"+getRandom();
	}
	/**
	 * 获取销售员openId
	 * @return
	 */
	public static String getSellerRefOpenId(){
		return SELLER+"_"+System.currentTimeMillis()+"_"+getRandom();
	}
}
