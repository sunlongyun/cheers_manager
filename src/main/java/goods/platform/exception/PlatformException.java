package goods.platform.exception;
/**
 * 平台异常
 * @author 孙龙云
 *
 */
public class PlatformException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3691233347667862182L;
	private String msg;
	@Override
	public String getMessage() {
		return this.msg;
	}
	PlatformException(String msg){
		this.msg = msg;
	}
}
