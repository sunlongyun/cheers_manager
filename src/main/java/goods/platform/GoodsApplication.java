package goods.platform;

import com.caichao.chateau.app.mapper.CountryMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import goods.platform.annotation.ChiSongTechApplicationRun;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"com.caichao.chateau","goods.platform"})
@ChiSongTechApplicationRun
public class GoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}
}