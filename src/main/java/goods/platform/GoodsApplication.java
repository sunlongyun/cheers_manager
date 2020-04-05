package goods.platform;

import com.chisong.green.farm.app.aspect.ControllerAspect;
import org.springframework.boot.SpringApplication;

import goods.platform.annotation.ChiSongTechApplicationRun;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"com.chisong.green.farm","goods.platform"})
@ChiSongTechApplicationRun
@PropertySource("file:${green_farm_path_file_test}")
public class GoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}
}