package com.plat.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    public static String getShortName(String fileName) {
        if (fileName != null && fileName.trim().length() > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getSubfix(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 判断手机号格式是否合法
     *
     * @param phone
     * @return
     */
    public static boolean isValidPhone(String phone) {
        String regx = "^(13[0-9]|15[0-9]|18[0-9]|17[0-9]|147)\\d{8}$";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(phone).matches();
    }

    public static String getCurStringDate(String pattern) {
        SimpleDateFormat dataFormat = new SimpleDateFormat();
        dataFormat.applyPattern(pattern);
        return dataFormat.format(new Date());
    }

    /**
     * 产生的一个包括数字和字母的随机字符串
     *
     * @param length
     * @return
     */
    public static String genRandomNum(int length) {
        StringBuilder newRandom = new StringBuilder(35);
        Random rd = new Random();
        for (int i = 0; i < length; i++) {
            newRandom.append(NUM_CHAR[rd.nextInt(35)]);
        }
        String randomnum = newRandom.toString();
        if (!checkPass(randomnum)) {
            genRandomNum(length);
        }
        return randomnum;
    }

    /**
     * 密码只数字和字母组合
     *
     * @param password
     * @return
     */
    private static boolean checkPass(String password) {
        String myReg = "^(([a-z]+[0-9]+[a-z0-9]*)|([0-9]+[a-z]+[a-z0-9]*))$";
        return password.matches(myReg);
    }

    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     *
     * @param str
     *            要+1的字符串
     * @return +1后的字符串
     * @exception NumberFormatException
     */
    public static String addOne(String str) {

        String[] strs = str.split("[^0-9]");// 根据不是数字的字符拆分字符串
        String numStr = strs[strs.length - 1];// 取出最后一组数字
        if (numStr != null && numStr.length() > 0) {

            int n = numStr.length();// 取出字符串的长度
            int num = Integer.parseInt(numStr) + 1;
            String added = String.valueOf(num);

            n = Math.min(n, added.length());

            return str.subSequence(0, str.length() - n) + added;

        } else {
            throw new NumberFormatException();
        }

    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        return DateToStr(date, PATTERN);
    }

    public static String DateToStr(Date date, String pattern) {
        if (date != null) {
            String str = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                str = format.format(date);
                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        return StrToDate(str, PATTERN);
    }

    public static Date StrToDate(String strdate, String pattern) {
        if (strdate != null && !strdate.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = format.parse(strdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return null;
        }
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrDateToDate(String str) {
        return StrToDate(str, "yyyy-MM-dd");
    }

    /**
     * 将字符串日期补全
     */
    public static String compleStr(String str) {
        if (str != null && !str.equals("")) {
            String curTimeStr = StringUtil.getCurStringDate(PATTERN);
            int index = curTimeStr.indexOf(" ");
            String subStr = curTimeStr.substring(index + 1, index + 6);
            String newCurTimeStr = curTimeStr.replace(subStr, str);
            return newCurTimeStr;
        } else {
            return null;
        }
    }

    /**
     * 从当前日期往后增加几天的时间
     *
     * @param day
     *            天数
     * @return
     */
    public static String addDay(int day, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);// 把日期往后增加一天.整数往后推,负数往前移动
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(calendar.getTime());
    }

    public static int getIntervalDays(Date startDate, Date endDate) {
        if (null == startDate || null == endDate) {
            return -1;
        }
        long intervalMilli = endDate.getTime() - startDate.getTime();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    /**
     * 获取生成时间
     *
     * @param maxDate
     * @param day
     * @return
     */
    public static Date getGenDate(Date maxDate, int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(maxDate);
        instance.add(Calendar.DAY_OF_MONTH, day);
        return instance.getTime();
    }

    public static int getRandom() {
        Random random = new Random();
        return random.nextInt(9999) % (9999 - 1235 + 1) + 1235;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        return isValidPhone(str);
    }

    public static String replaceString(String src, char oldChar, String[] replArr) {
        String dst = "";

        int num = 0;
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == oldChar) {
                dst += replArr[num++];
            } else {
                dst += src.charAt(i);
            }
        }

        return dst;
    }

    public static String numberFormat(double d) {
        return format.format(d);
    }

    /**
     * 1年有效期
     *
     * @param dtTime
     * @return
     */
    public static String enDate(Date dtTime) {
        Calendar curr = Calendar.getInstance();
        curr.setTime(dtTime);
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        Date date = curr.getTime();
        return StringUtil.DateToStr(date, "yyyy-MM-dd");
    }

    public static double doubleSub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        BigDecimal b3 = b1.subtract(b2);
        return b3.doubleValue();
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    public static long timeDiff(Date st, Date en) {
        long diff = 0;
        try {
            diff = st.getTime() - en.getTime();
            diff = diff / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff;
    }

    private static DecimalFormat format = new DecimalFormat("#.##");
    public static final int pageItems = 20;// 每页显示条数
    public static final String USER_SESSION = "curLoginUser";
    public static final String DEFAULT_DOCK = "defaultDock";
    public static final String DEFAULT_WH_NAME = "defaultWhName";
    public static final String DEFAULT_WH_CODE = "defaultWhCode";
    public static final String USER_AUTH = "curLoginUserAuth";
    public static final String USER_BUTTON = "curLoginUserButton";
    public static final String OPEN_ID = "openId";
    public static final String USER_TOKEN = "token";
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final char[] NUM_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    public static final String WMS_DOCK= "wmsDock";
}
