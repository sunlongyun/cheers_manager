package goods.platform.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件分页查询
 * @author 孙龙云
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileQueryParams extends QueryParam {
	private Integer type;
	private String fileName;
	private String userName;

}
