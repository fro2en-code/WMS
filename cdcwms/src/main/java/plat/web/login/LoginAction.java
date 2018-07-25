package plat.web.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultRespT;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserWarehouse;
import com.ymt.utils.DockUtils;
import com.ymt.utils.UserUtils;
import com.ymt.utils.WmsLog;

import cn.rtzltech.user.model.Plat_User;
import net.sf.json.JSONObject;
import wms.business.biz.UserBiz;

@Controller
public class LoginAction extends BaseAction {

    /** 验证码字符个数 */
    private int codeCount = 4;

    char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '2', '3', '4', '5', '6', '7', '8', '9' };

    private int codeY;

    @Resource
    private DockUtils dockUtils;

    // 字体高度
    private int fontHeight;

    /** 验证码图片的高度 */
    private int height = 30;
    @Resource
    private UserUtils userUtils;
    @Resource
    private UserBiz userBiz;
    /** 验证码图片的宽度 */
    private int width = 85;

    // 字符间距
    private int x = 0;

    @RequestMapping("/authcode")
    public void genAuthCode(HttpSession session, HttpServletResponse resp) {
        x = width / (codeCount + 1);
        fontHeight = height - 4;
        codeY = height - 4;
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 创建一个随机数生成器类
        Random random = new Random();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN | Font.BOLD, fontHeight);
        // 设置字体。
        g.setFont(font);

        // 画边框。
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.WHITE);
        for (int i = 0; i < 160; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(34)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x - 6, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        // 将四位数字的验证码保存到Session中。
        session.setAttribute("validateCode", randomCode.toString());
        // 设置响应的类型格式为图片格式
        resp.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        try {
            OutputStream out = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    @WmsLog("loginname")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletResponse resp, HttpSession session, String loginname, String pwd, String code)
            throws Exception {
        // TODO 这里暂时屏掉验证码
        /*
         * String vcode = (String) session.getAttribute("validateCode");
         * if (!code.equalsIgnoreCase(vcode)) {
         * ret.setRetcode("-1");
         * ret.setRetmsg("验证码错误。");
         * print(resp, JSONObject.fromObject(ret).toString());
         * return;
         * }
         */

        ResultRespT<Plat_User> ret = userUtils.login(session, loginname, pwd);
        if (null == ret) {
            return;
        }
        if ("-1".equals(ret.getRetcode())) {
            print(resp, JSONObject.fromObject(ret).toString());
            return;
        }
        Plat_User pu = ret.getT();
        UserBean ub = new UserBean();
        ub.setLoginname(loginname);
        ub.setTruename(pu.getTruename());
        ub.setJsonMenuTree(pu.getJsonMenuTree());
        ub.setJsonMenuBtn(pu.getJsonMenuBtn());

        userBiz.clearCache(loginname);
        // 获取该用户绑定的仓库
        List<UserWarehouse> warehouseList = userBiz.getWarehouseMap(ub.getLoginname());
        UserWarehouse userWarehouse = warehouseList.get(0);

        session.setAttribute("warehouseList", warehouseList);
        session.setAttribute(StringUtil.DEFAULT_WH_CODE, userWarehouse.getWhCode());
        session.setAttribute(StringUtil.DEFAULT_WH_NAME, userWarehouse.getWhName());
        boolean isReciver = userUtils.isReciver(loginname);
        session.setAttribute("isReciver", isReciver);
        if (isReciver) {
            dockUtils.defaultBindWmsDock(session, loginname, userWarehouse.getWhCode());
        }
        ret.setRetcode("0");
        ret.setRetmsg("登录成功。");
        print(resp, JSONObject.fromObject(ret).toString());
    }

    @RequestMapping("/loginui")
    public String loginui() {
        return "loginUI";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "loginUI";
    }

    @RequestMapping("/toMgr.action")
    public String toMgr() {
        return "mgr";
    }

}
