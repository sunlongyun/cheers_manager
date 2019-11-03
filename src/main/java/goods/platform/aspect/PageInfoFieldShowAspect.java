package goods.platform.aspect;

import com.lianshang.generator.commons.PageInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.format.annotation.DateTimeFormat;

import com.chisong.green.farm.app.annotation.AmountUnitChange;
import com.chisong.green.farm.app.annotation.ServiceTypeAnnotation;
import com.chisong.green.farm.app.constants.enums.BaseServiceEnum;
/**
 * pageInfo中的bean字段根据枚举类型自动回显
 * @author 孙龙云
 * 2018年6月23日 下午4:50:07
 */
@Aspect
public class PageInfoFieldShowAspect {
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)"
			+ " || @annotation(org.springframework.web.bind.annotation.GetMapping)"
			+ " || @annotation(org.springframework.web.bind.annotation.PostMapping)")
	public Object around(final ProceedingJoinPoint joinPoint){
		Object target  = null;
		try {
			target =  joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(target instanceof PageInfo){

			PageInfo pageInfo = (PageInfo) target;
			List<Map<String, Object>> newDataList = new ArrayList<>();
			if(null != pageInfo){
				List<?> list = pageInfo.getDataList();
				if(null != list){
					for(Object obj : list){
						newDataList.add(getMapByObj(obj));
					}
				}
			}
			pageInfo.setDataList(newDataList);
		}
		return target;
	}
	/**
	 * 根据对象返回map
	 * @param obj
	 * @return
	 */
	private Map<String, Object> getMapByObj(Object obj){
		Map<String, Object> data = new HashMap<>();
		Class<?> clazz = obj.getClass();
		Field[] fields =  clazz.getDeclaredFields();
		if(null != fields){
			for(Field f : fields){
				f.setAccessible(true);
				String fieldName = f.getName();
				Object value = null;
				try {
					 value = f.get(obj);
					 if(null == value){
						 continue;
					 }
					 //处理注解
					 //1.日期格式化
					 DateTimeFormat dateTimeFormat= f.getDeclaredAnnotation(DateTimeFormat.class);
					 if(null != dateTimeFormat){
						 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat.pattern());
						 value = simpleDateFormat.format(value);
					 }
					 //2.金额转换
					 AmountUnitChange amountUnitChange = f.getDeclaredAnnotation(AmountUnitChange.class);
					 if(null != amountUnitChange){
						 AmountUnitChange.ChangeType changeType= amountUnitChange.values();
						 boolean showUnit =  amountUnitChange.showUnit();
						 Double v = Double.valueOf(value+"");
						 if(changeType == AmountUnitChange.ChangeType.FenToYuan){
							 v = v / 100;
							 value = v;
							 if(showUnit){
								 value += " 元";
							 }
						 }else if(changeType == AmountUnitChange.ChangeType.YuanToFen){
							 v = v * 100;
							 value = v;
							 if(showUnit) {
								 value += " 分";
							 }
						 }
						 
					 }
					 
					 //3.自定义注解
					 ServiceTypeAnnotation serviceTypeAnnotation = f.getDeclaredAnnotation(ServiceTypeAnnotation.class);
					 if(null != serviceTypeAnnotation){
						Class<? extends BaseServiceEnum> baseServiceClass =  serviceTypeAnnotation.value();
						ServiceTypeAnnotation.Type  type =  serviceTypeAnnotation.type();
						//获取所有的枚举常量
						BaseServiceEnum[] enums =  baseServiceClass.getEnumConstants();
						if(null == enums || enums.length == 0){
							continue;
						}
						//获取key
						Method getKeyField =  baseServiceClass.getMethod("getKeyFieldName");
						String keyField = (String) getKeyField.invoke(enums[0]);
						//获取value
						Method getKeyValue =  baseServiceClass.getMethod("getValueFieldName");
						String valueField =  (String) getKeyValue.invoke(enums[0]);
						
						Class clz =  enums[0].getClass();
						Field[] efls = clz.getDeclaredFields();
						for(BaseServiceEnum e : enums){
							//获取key的字段
							Field keyF = clz.getDeclaredField(keyField);
							keyF.setAccessible(true);
							Object kv =  keyF.get(e);
							keyF.setAccessible(false);
							//获取value的字段
							Field keyV = clz.getDeclaredField(valueField);
							keyV.setAccessible(true);
							Object vv =  keyV.get(e);
							keyV.setAccessible(false);
							if(kv.equals(value)){
								if(ServiceTypeAnnotation.Type.VALUE == type){
									value = vv;
								}else if(ServiceTypeAnnotation.Type.ALL == type){
									Map<String, Object> dd = new HashMap<>();
									for(Field f1 : efls){
										f1.setAccessible(true);
										dd.put(f1.getName(), f1.get(e));
										f1.setAccessible(false);
									}
									value = dd;
								}
								break;
							}
						}
						
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
				f.setAccessible(false);
				
			   data.put(fieldName, value);
			}
		}
		return data;
	}
}
