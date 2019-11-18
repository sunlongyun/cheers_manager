package goods.platform.controller;

import goods.platform.commons.Response;
import goods.platform.utils.QiniuAuthUtil;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 七牛云资源管理
 * @AUTHOR 孙龙云
 * @date 2019-11-17 12:05
 */
@RestController
@RequestMapping("qiniu")
@Slf4j
public class QiniuController {
	private  static QiniuAuthUtil qiniuAuthUtil;
	@Value("${bucket}")
	private String bucket;

	@Value("${accessKey}")
	private String accessKey;

	@Value("${secretKey}")
	private String secretKey;

	@Value("${baseDir}")
	private String baseDir;

	@Value("${uploadUrl}")
	private String uploadUrl;

	@Value("${download}")
	private String download;

	@RequestMapping("token")
	public Response getTocken(String dir, String fileName){
		if(null == qiniuAuthUtil){
			synchronized(QiniuAuthUtil.class){
				if(null == qiniuAuthUtil){
					qiniuAuthUtil = QiniuAuthUtil.create(accessKey,
						secretKey);
				}
			}
		}

		StringBuilder keyBuilder = new StringBuilder(baseDir);
		if(!baseDir.endsWith("/")){
			keyBuilder.append("/");
		}
		if(!StringUtils.isEmpty(dir)){
			keyBuilder.append(dir);
			if(!dir.endsWith("/")){
				keyBuilder.append("/");
			}
		}

		if(!StringUtils.isEmpty(fileName)){
			keyBuilder.append(fileName);
			if(!fileName.contains(".")){
				keyBuilder.append(".jpg");
			}
		}else{
			fileName =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			int randowNumber = (int)(Math.random()*100);
			fileName += randowNumber+".jpg";
			keyBuilder.append(fileName);
		}
		String key  = keyBuilder.toString();
		String token = qiniuAuthUtil.uploadToken(bucket);

		Map<String,String> dataMap = new HashMap<>();
		dataMap.put("token",token);
		dataMap.put("uploadUrl", uploadUrl);
//		dataMap.put("key", key);
	    dataMap.put("download", download);

		return Response.success(dataMap);
	}
}
