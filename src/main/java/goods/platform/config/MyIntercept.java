package goods.platform.config;

import com.chisong.green.farm.app.dto.AppInfoDto;
import com.chisong.green.farm.app.dto.CustomerInfoDto;
import com.chisong.green.farm.app.service.AppInfoService;
import com.chisong.green.farm.app.utils.AppUtils;
import goods.platform.commons.ApplicationContextUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MyIntercept extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = request.getServletPath();
		this.log.info("path===>{}", path);
		boolean checkResult = false;
		HttpSession session = request.getSession(false);
		if(null != session){
			CustomerInfoDto customerInfoDto = (CustomerInfoDto) session.getAttribute("adminUser");
			log.info("customerInfoDto:{}", customerInfoDto);
			if(null != customerInfoDto){
				checkResult = true;
				 AppInfoService appInfoService = ApplicationContextUtil.getBeanByType(AppInfoService.class);
				AppInfoDto appInfoDto = appInfoService.getById(customerInfoDto.getAppInfoId());
				AppUtils.setName(appInfoDto.getAppId());
				AppUtils.setSecret(appInfoDto.getAppSecret());
				AppUtils.set(customerInfoDto.getAppInfoId());
			}
		}
		this.log.info("checkResult===>{}", checkResult);
		if(!checkResult){
			response.sendRedirect("/login");
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		AppUtils.remove();
	}
}
