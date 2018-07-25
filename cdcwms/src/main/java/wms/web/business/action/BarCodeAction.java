package wms.web.business.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.plat.common.action.BaseAction;

@Controller
@RequestMapping("/barCode")
public class BarCodeAction extends BaseAction {
    private static final Map<EncodeHintType, Object> hints = new HashMap<>();// 二维码参数

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        hints.put(EncodeHintType.MARGIN, 2);// 二维码与图片边距
    }

    /**
     * 生成条码
     */
    private void getCode(OutputStream out, BarcodeFormat format, String str, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(str, format, width, height, hints);
        MatrixToImageWriter.writeToStream(matrix, "png", out);
    }

    /**
     * 变更任务状态
     */
    @RequestMapping("/getImage")
    public void getImage(HttpServletResponse resp, String code, int height, int width, String type) throws Exception {
        OutputStream out = resp.getOutputStream();
        if ("1".equals(type)) {
            getCode(out, BarcodeFormat.CODE_128, code, height, width);
        } else if ("2".equals(type)) {
            getCode(out, BarcodeFormat.QR_CODE, code, height, width);
        }
        out.flush();
        out.close();
    }

    @RequestMapping("/toList")
    public String toList() {
        return "barCode";
    }

}
