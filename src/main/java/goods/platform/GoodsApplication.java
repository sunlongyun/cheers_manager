package goods.platform;

import org.springframework.boot.SpringApplication;

import goods.platform.annotation.ChiSongTechApplicationRun;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"com.caichao.chateau","goods.platform"})
@ChiSongTechApplicationRun
@PropertySource("file:${green_farm_path_file}")
public class GoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}
}