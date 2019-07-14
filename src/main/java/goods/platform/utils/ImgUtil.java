package goods.platform.utils;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImgUtil {
	/** 
     * 根据设置的宽高等比例压缩图片文件<br> 先保存原文件，再压缩、上传 
     * @param oldFile  要进行压缩的文件 
     * @param newFile  新文件 
     * @param width  宽度 //设置宽度时（高度传入0，等比例缩放） 
     * @param height 高度 //设置高度时（宽度传入0，等比例缩放） 
     * @param quality 质量 
     * @return 返回压缩后的文件的全路径 
     */  
    public static String zipImageFile(File oldFile,File newFile, int width, int height,float quality) {  
        if (oldFile == null) {  
            return null;  
        }  
        try {  
            /** 对服务器上的临时文件进行处理 */  
            Image srcFile = ImageIO.read(oldFile);  
            int w = srcFile.getWidth(null);  
            int h = srcFile.getHeight(null);  
            double bili;  
            if(width>0){  
                bili=width/(double)w;  
                height = (int) (h*bili);  
            }else{  
                if(height>0){  
                    bili=height/(double)h;  
                    width = (int) (w*bili);  
                }  
            }  
            
            String srcImgPath = newFile.getAbsoluteFile().toString();
            System.out.println(srcImgPath);
            String subfix = "jpg";
    		subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());
 
    		BufferedImage buffImg = null; 
    		if(subfix.equals("png")){
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		}else{
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		}
 
    		Graphics2D graphics = buffImg.createGraphics();
    		graphics.setBackground(new Color(255,255,255));
    		graphics.setColor(new Color(255,255,255));
    		graphics.fillRect(0, 0, width, height);
    		graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  
 
    		ImageIO.write(buffImg, subfix, new File(srcImgPath));  
  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return newFile.getAbsolutePath();  
    }  
  
    /** 
     * 按设置的宽度高度压缩图片文件<br> 先保存原文件，再压缩、上传 
     * @param oldFile  要进行压缩的文件全路径 
     * @param newFile  新文件 
     * @param width  宽度 
     * @param height 高度 
     * @param quality 质量 
     * @return 返回压缩后的文件的全路径 
     */  
	public static String zipWidthHeightImageFile(File oldFile,File newFile, int width, int height,float quality) {  
        if (oldFile == null) {  
            return null;  
        }  
        String newImage = null;  
        try {  
            /** 对服务器上的临时文件进行处理 */  
            Image srcFile = ImageIO.read(oldFile);  
            
            String srcImgPath = newFile.getAbsoluteFile().toString();
            String subfix = "jpg";
    		subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());
    		BufferedImage buffImg = null; 
    		if(subfix.equals("png")){
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		}else{
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		}
 
    		Graphics2D graphics = buffImg.createGraphics();
    		graphics.setBackground(new Color(255,255,255));
    		graphics.setColor(new Color(255,255,255));
    		graphics.fillRect(0, 0, width, height);
    		graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  
 
    		ImageIO.write(buffImg, subfix, new File(srcImgPath));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return newImage;  
    }  
    /**
     * 获取图片宽度和高度
     * 
     * @param 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = { 0, 0 };
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            result[0] =src.getWidth(null); // 得到源图片宽
            result[1] =src.getHeight(null);// 得到源图片高
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }
}
    
