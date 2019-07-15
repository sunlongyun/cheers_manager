package goods.platform;

import com.caichao.chateau.app.constants.enums.Validity;
import com.caichao.chateau.app.dto.CountryDto;
import com.caichao.chateau.app.example.CountryExample;
import com.caichao.chateau.app.mapper.CountryMapper;
import com.caichao.chateau.app.service.CountryService;
import com.lianshang.generator.commons.GenerateFileTypeEnum;
import com.lianshang.utils.LsCodeGeneratorUtil;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-07-14 20:51
 */
@SpringBootTest
@Slf4j
public class GoodsApplicationTests {

	@Autowired
	private CountryService countryService;

	@Test
	public void testCountryService(){
//		log.info("countryMapper:{}", countryService.getList(new CountryExample()));
	}
}
