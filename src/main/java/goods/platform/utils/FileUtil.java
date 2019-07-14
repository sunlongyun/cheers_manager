package goods.platform.utils;

import java.io.File;

import goods.platform.constant.FileTypeEnum;
/**
 * 文件类型管理
 * @author 孙龙云
 *
 */
public class FileUtil {
	public static int getFileType(String fileName){
		int type = FileTypeEnum.OTHER.code();
		if(fileName.indexOf(".mp4") != -1){
			type = FileTypeEnum.VIDEO.code();
		}else if(fileName.indexOf(".mp3") != -1){
			type = FileTypeEnum.AUDIO.code();
		}else if(fileName.indexOf(".jpg") != -1
				|| fileName.indexOf(".png") != -1
				|| fileName.indexOf(".gif") != -1
				|| fileName.indexOf(".jpeg") != -1
				|| fileName.indexOf(".JPG") != -1
				|| fileName.indexOf(".JPEG") != -1
				|| fileName.indexOf(".PNG") != -1
				|| fileName.indexOf(".GIF") != -1){
			type = FileTypeEnum.IMG.code();
		}
		return type;
	}
	/**
	 * 获取绝对路径
	 * @param fileDir
	 * @param fileName
	 * @return
	 */
	public static String getAbsolutePath(String fileDir, String fileName){
		String secondDir = "";
		int type = getFileType(fileName);
		if(type == FileTypeEnum.IMG.code()){
			secondDir = "/image/";
		}else if(type == FileTypeEnum.VIDEO.code()){
			secondDir = "/video/";
		}else if(type == FileTypeEnum.AUDIO.code()){
			secondDir = "/audio/";
		}else{
			secondDir = "/other/";
		}
		String filePath2 = fileDir + secondDir;
		File directory = new File(filePath2);
		if(!directory.exists()){
			directory.mkdirs();
		}
		return filePath2+fileName;
	}
}
