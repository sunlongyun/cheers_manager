package goods.platform;

import org.springframework.boot.SpringApplication;

import goods.platform.annotation.ChiSongTechApplicationRun;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.chisong.green.farm","goods.platform"})
@ChiSongTechApplicationRun
@PropertySource("file:${green_farm_path_file}")
@EnableScheduling
public class GoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}
}