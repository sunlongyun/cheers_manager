package goods.platform.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 金额 单元转换
 * @author 孙龙云
 * values 转换方向  ChangeType.FenToYuan-分转元；ChangeType.YuanToFen-元转分
 * showUnit 是否显示单位
 * 2018年6月24日 下午7:58:28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AmountUnitChange {
	/**
	 * 转换方向
	 * @return
	 */
	public ChangeType values() default ChangeType.FenToYuan;
	/**
	 * 是否显示单位
	 * @return
	 */
	public boolean showUnit() default false;
	/**
	 * 转换类型
	 * @author 孙龙云
	 * 2018年6月24日 下午8:00:58
	 */
	static enum ChangeType{
		FenToYuan,
		YuanToFen
	}
}
