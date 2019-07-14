//package goods.platform.aspect;
//
//import goods.platform.commons.Response;
//import goods.platform.utils.JsonUtils;
//import javax.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
///**
// * 描述:
// *
// * @AUTHOR 孙龙云
// * @date 2019-06-09 16:09
// */
//@Component
//@Aspect
//@Slf4j
//public class ControllerAspect {
//
//	@Pointcut("execution(* goods..controller.*(..))")
//	private void aroundMethod() {
//
//	}
//
//	@Around(value = "aroundMethod()")
//	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//			.getRequest();
//
//		long start = System.currentTimeMillis();
//		Object[] args = proceedingJoinPoint.getArgs();
//		Signature signature = proceedingJoinPoint.getSignature();
//		String className = proceedingJoinPoint.getTarget().getClass().getName();
//		String methodName = signature.getName();
//		Object result = null;
//		try {
//			log.info("【请求入参】className:[{}],methodName:[{}],args:[{}]", className, methodName,args);
//
//			result = proceedingJoinPoint.proceed();
//			return  result;
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			log.error("【请求异常】", ex);
//
//			String errorMsg = ex.getMessage();
//			if(StringUtils.isEmpty(errorMsg)) {
//				errorMsg = "请求服务异常";
//			}
//			return  Response.fail(errorMsg);
//		} finally {
//			long end = System.currentTimeMillis();
//			log.info("【响应参数】耗时:[{}]，出参:[{}]", end - start, JsonUtils.object2JsonString(result));
//		}
//
//	}
//}
