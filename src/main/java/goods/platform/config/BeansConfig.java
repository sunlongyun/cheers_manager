package goods.platform.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
