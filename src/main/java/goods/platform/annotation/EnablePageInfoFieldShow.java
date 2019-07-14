package goods.platform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import goods.platform.aspect.PageInfoFieldShowAspect;

/**
 * 分页显示时枚举类型字段自动翻译
 *可以自动解析实现BaseServiceEnum接口的类型字段
 * @author 孙龙云
 * 2018年6月23日 下午4:47:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(PageInfoFieldShowAspect.class)
public @interface EnablePageInfoFieldShow {
	
}
