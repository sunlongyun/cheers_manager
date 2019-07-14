package goods.platform.params;

import lombok.Data;
/**
 * 文件分页查询
 * @author 孙龙云
 *
 */
@Data
public class FileQueryParams extends QueryParam {
	private Integer type;
	private String fileName;
	private String userName;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
