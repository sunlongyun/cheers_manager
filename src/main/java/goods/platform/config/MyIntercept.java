package goods.platform.config;

import com.caichao.chateau.app.dto.CustomerInfoDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			}
		}
		this.log.info("checkResult===>{}", checkResult);
		if(!checkResult){
			response.sendRedirect("/login");
			return false;
		}

		return true;
	}

}
