package goods.platform.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
		registry.addViewController("/setPostageTemplate").setViewName("html/setPostageTemplate");
		registry.addViewController("/login").setViewName("login/login");
		registry.addViewController("/adminList").setViewName("html/adminList");
		registry.addViewController("/goodsList").setViewName("html/goodsList");
		registry.addViewController("/addGoods").setViewName("html/addGoods");
		registry.addViewController("/updateGoods").setViewName("html/updateGoods");
		registry.addViewController("/addSupplier").setViewName("html/addSupplier");
		registry.addViewController("/updateSupplier").setViewName("html/updateSupplier");
		registry.addViewController("/customerList").setViewName("html/customerList");
		registry.addViewController("/supplierList").setViewName("html/supplierList");
		registry.addViewController("/salesOrderList").setViewName("html/salesOrderList");
		registry.addViewController("/updateSalesOrder").setViewName("html/updateSalesOrder");

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
