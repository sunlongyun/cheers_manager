package goods.platform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import goods.platform.constant.BaseServiceEnum;
/**
 * 枚举类型解析注解
 * @value  枚举类
 * @type 类型 ALL-显示枚举类的所有字段；VALUE-只显示value值
 * @author 孙龙云
 * 2018年6月23日 下午6:15:39
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ServiceTypeAnnotation {
	
	/**
	 * 枚举类型
	 * @return
	 */
	public Class<? extends BaseServiceEnum> value();
	/**
	 * 枚举字段显示类型
	 * @type ALL-显示枚举类的所有字段，VALUE-只显示枚举类的值
	 * @return
	 */
	public Type type() default Type.ALL;
	
	static enum Type{
		ALL,//枚举类型的所有字段都显示
		VALUE//只显示value字段
	}
}
