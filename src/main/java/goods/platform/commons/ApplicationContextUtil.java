package goods.platform.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/**
	 * 根据类型查询bean
	 * @param clazz
	 * @return
	 */
	public static <M> M getBeanByType(Class<M> clazz){
		return applicationContext.getBean(clazz);
	}
	/**
	 * 根据名称查询bean
	 * @param beanName
	 * @return
	 */
	public static <M> M getBeanByName(String beanName){
		return (M) applicationContext.getBean(beanName);
	}
}
