package goods.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import goods.platform.annotation.ChiSongTechApplicationRun;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"com.caichao.chateau","goods.platform"})
@ChiSongTechApplicationRun
@MapperScan(basePackages = {"com.caichao.chateau.*.mapper"})
public class GoodsApplication {


	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}
}