package com.ymt.utils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.utils.ExcelUtil;
import com.plat.common.utils.ExcelUtil.Excel;
import com.plat.common.utils.StringUtil;

/**
 * Excel操作工具类
 *
 * @author zhouxianglh@gmail.com
 *
 *         2016年11月29日
 */
public class ExcelUtils {
    /**
     * 内部类,用于协助导出Excel
     *
     * @author zhouxianglh@gmail.com
     *
     *         2016年11月29日
     */
    public static abstract class ExportExcelHelper {
        /**
         * 获取key
         */
        public abstract String getKey();

        /**
         * 执行查询
         */
        public abstract PageData<?> getPageDate(int page, int size) throws Exception;

        /**
         * 运行方法
         */
        public void run(HttpServletResponse resp) {
            String key = getKey();
            HSSFWorkbook workbook = ExcelUtils.makeExcel(key);
            Excel excel = ExcelUtil.ExcelTemplate.get(key);
            List<String> columnList = new ArrayList<>(excel.getColumn().keySet());
            // 因为数据可能很大,这里分页取出,分页写入(其实也想偷懒)
            int page = 1;
            while (true) {
                try {
                    PageData<?> pageData = getPageDate(page, pageSize);
                    ExcelUtils.defaultSetData(pageData.getRows(), workbook.getSheetAt(0), columnList);
                    if (pageData.getPageCount() <= page++) {
                        break;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            ExcelUtils.exportExcel(resp, key, workbook);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    public static final int pageSize = 200;

    /**
     * Excel 设置值
     */
    public static void defaultSetData(List<?> list, HSSFSheet sheet, List<String> columnList) {
        int nowRow = sheet.getLastRowNum();
        for (int m = 0, n = list.size(); m < n; m++) {
            HSSFRow bodyRow = sheet.createRow(nowRow + m + 1);
            Object obj = list.get(m);
            for (int i = 0; i < columnList.size(); i++) {
                String column = columnList.get(i);
                HSSFCell cell = bodyRow.createCell(i);
                if (obj instanceof BaseModel) {
                    BaseModel vo = (BaseModel) obj;
                    setCellValue(cell, column, vo);
                } else if (obj instanceof Map) {
                    @SuppressWarnings("rawtypes")
                    Map map = (Map) obj;
                    setCellValue(cell, column, map);
                }
            }
        }
    }

    /**
     * 生成Excel用于下载
     */
    public static void exportExcel(HttpServletResponse resp, String keyNo, HSSFWorkbook workbook) {
        try {
            Excel excel = ExcelUtil.ExcelTemplate.get(keyNo);
            resp.setContentType("application/vnd.ms-excel");
            String fileName = new String((excel.getTempName() + ".xls").getBytes("gb2312"), "ISO8859-1");
            resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write(resp.getOutputStream());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 生成Excel
     */
    public static HSSFWorkbook makeExcel(String keyNo) { // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        Excel excel = ExcelUtil.ExcelTemplate.get(keyNo);
        HSSFSheet sheet = workbook.createSheet(excel.getTempName());
        List<String> columnList = new ArrayList<>(excel.getColumn().values());
        // 头
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnList.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnList.get(i));
        }
        return workbook;
    }

    /**
     * 设置单元格的值
     */
    private static void setCellValue(HSSFCell cell, String column, BaseModel vo) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(column, vo.getClass());
            Method method = propertyDescriptor.getReadMethod();
            Object value = method.invoke(vo);
            setValue(cell, value);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 设置单元格的值
     */
    @SuppressWarnings("rawtypes")
    private static void setCellValue(HSSFCell cell, String column, Map map) {
        try {
            Object value = map.get(column);
            setValue(cell, value);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private static void setValue(HSSFCell cell, Object value) {
        if (value == null) {
            return;
        }
        String strValue = null;
        // TODO 这里需要考滤数字格式化等问题,但是现在没用到,所以不管它
        if (value instanceof Date) {
            strValue = StringUtil.DateToStr((Date) value);
        } else {
            strValue = value.toString();
        }
        cell.setCellValue(strValue);
    }
}
