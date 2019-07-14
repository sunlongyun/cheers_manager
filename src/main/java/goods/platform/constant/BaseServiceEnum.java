package goods.platform.constant;
/**
 * 业务注解基础类
 * @author 孙龙云
 * 2018年6月23日 下午5:21:58
 */
public interface BaseServiceEnum {
	/**
	 * 枚举类作为键的字段名称
	 * @return
	 */
	public default String getKeyFieldName(){
		return "code";
	}
	/**
	 * 枚举类作为值的字段名称
	 * @return
	 */
	public default String getValueFieldName(){
		return "value";
	}
}
