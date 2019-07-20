package goods.platform.utils;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具
 * @author 孙龙云
 *
 */
public class QrCodeUtil {
	/**
	 * 获取二维码
	 * @param content
	 * @param width
	 * @param height
	 * @param res
	 * @throws WriterException
	 */
	public static void getQrcode(String content, int width, int height, HttpServletResponse res) throws Exception{
		  //定义二维码的参数
        HashMap map = new HashMap();
        String format = "png";
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置纠错等级
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 0);
        //生成二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToStream(bitMatrix, format, res.getOutputStream());
	}
}
