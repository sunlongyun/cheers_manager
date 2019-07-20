package goods.platform.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * mvc 配置
 * @author 孙龙云
 *
 */
@Component
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("html/index");
		registry.addViewController("/firstPage").setViewName("html/firstPage");
		registry.addViewController("/login").setViewName("login/login");
		registry.addViewController("/adminList").setViewName("html/adminList");
		registry.addViewController("/goodsList").setViewName("html/goodsList");
		registry.addViewController("/customerList").setViewName("html/customerList");
		registry.addViewController("/supplierList").setViewName("html/supplierList");
		registry.addViewController("/salesOrderList").setViewName("html/salesOrderList");
		registry.addViewController("/chateauList").setViewName("html/chateauList");
		//

	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(new MyIntercept())
			.excludePathPatterns("/admin/login")
			.excludePathPatterns("/login/**")
			.excludePathPatterns("/html/**")
			.excludePathPatterns("/login")
			.excludePathPatterns("/error")
			.excludePathPatterns("/admin/loginout");
	}



}
