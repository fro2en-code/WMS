package wms.web.excel.action;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.plat.common.action.BaseAction;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil;
import com.plat.common.utils.ExcelUtil.Excel;
import com.plat.common.utils.StringUtil;
import com.ymt.utils.Lock;

import net.sf.json.JSONObject;
import wms.business.biz.ToolBiz;

/**
 * 通用Excel Controller Author Hmgx data 2016-10-12 by 融通智联
 */
@Controller
@RequestMapping("/excel")
public class ExcelAction extends BaseAction {

    @Resource
    private ToolBiz toolBiz;

    /**
     * 渲染导入数据页面
     */
    @RequestMapping("/uploadImport.action")
    public String uploadImport(HttpServletResponse response, HttpServletRequest request, String excelHeader) {
        return "uploadExcel";
    }

    /**
     * 导入Excel数据
     */
    @Lock("keyNo")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/importExcel.action")
    public void importExcel(HttpServletRequest req, HttpServletResponse resp, HttpSession session, String keyNo) {
        ResultResp resultResp = new ResultResp();
        try {
            Excel excel = ExcelUtil.ExcelTemplate.get(keyNo);
            if (excel == null) {
                resultResp.setRetcode("-1");
                resultResp.setRetmsg("未找到表格列！");
                print(resp, JSONObject.fromObject(resultResp).toString());
                return;
            }
            Map<String, String> excelHeaderMap = excel.getColumn();
            Map<String, Object> defaultColumnMap = excel.getDefColumn();

            // 转换
            Gson gson = new Gson();
            List<String> fieldList = new ArrayList<String>(excelHeaderMap.keySet());
            List<Object> entityList = new ArrayList<Object>();
            Class clazz = Class.forName(excel.getClassName());

            MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) req;
            MultipartFile gbExcl = mulRequest.getFile("excelFile");
            String filename = gbExcl.getOriginalFilename();
            if (filename == null || "".equals(filename)) {
                resultResp.setRetcode("-1");
                resultResp.setRetmsg("请上传需要Excel文件！");
                print(resp, JSONObject.fromObject(resultResp).toString());
                return;
            }
            try {
                InputStream is = gbExcl.getInputStream();
                Workbook xssfWorkbook = null;
                String type = filename.substring(filename.lastIndexOf(".") + 1).trim().toLowerCase();
                if ("xls".equals(type)) {
                    xssfWorkbook = new HSSFWorkbook(is);
                } else if ("xlsx".equals(type)) {
                    xssfWorkbook = new XSSFWorkbook(is);
                } else {
                    resultResp.setRetcode("-1");
                    resultResp.setRetmsg("请导入.xls/.xlsx格式的文件");
                    print(resp, JSONObject.fromObject(resultResp).toString());
                    return;
                }

                // 获取第一个工作簿
                Sheet sheet = xssfWorkbook.getSheetAt(0);

                for (int j = 1; j <= sheet.getLastRowNum(); j++) {

                    // 获取行
                    Row row = sheet.getRow(j);
                    Map<String, Object> rowMap = new LinkedHashMap<String, Object>();

                    // 获取每一个单元格
                    if (row != null) {
                        for (int ii = 0; ii < fieldList.size(); ii++) {
                            Cell cell = row.getCell(ii);
                            Object value = this.parseExcel(cell);
                            if (null != value && value instanceof String && StringUtils.isEmpty((CharSequence) value)) {
                                continue;
                            }
                            rowMap.put(fieldList.get(ii), value);
                        }
                    }

                    // 合并默认列
                    if (defaultColumnMap != null) {
                        rowMap.putAll(defaultColumnMap);
                    }

                    // 转换为实体类
                    String entityStr = gson.toJson(rowMap);
                    Object entity = clazz.newInstance();
                    entity = gson.fromJson(entityStr, clazz);
                    entityList.add(entity);
                }

                if (entityList.isEmpty()) {
                    resultResp.setRetcode("-1");
                    resultResp.setRetmsg("数据为空请检查导入文件！");
                } else {
                    try {
                        toolBiz.saveExcleToDatabase(entityList);
                        resultResp.setRetcode("0");
                        resultResp.setRetmsg("共导入[" + entityList.size() + "]条数据.<br>请手动刷新页面查看导入结果!");
                    } catch (Exception e) {
                        resultResp.setRetcode("-1");
                        resultResp.setRetmsg(e.getMessage());
                        logger.error("", e);
                    }
                }
            } catch (Exception e) {
                resultResp.setRetcode("-1");
                resultResp.setRetmsg(e.getMessage());
                logger.error("", e);
            }
        } catch (Exception e) {
            logger.error("", e);
            resultResp.setRetcode("-1");
            resultResp.setRetmsg(e.getMessage());
        }

        try {
            print(resp, JSONObject.fromObject(resultResp).toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 下载模版
     */
    @RequestMapping("/downTemp.action")
    public void downTemp(HttpServletResponse resp, String keyNo) {

        if (StringUtil.isEmpty(keyNo)) {
            try {
                resp.setHeader("content-type", "text/html;charset=UTF-8");
                resp.getWriter().print("<h4>未接收到模板编号。</h4>");
            } catch (IOException e) {
                logger.error("", e);
            }
            return;
        }

        Excel excel = ExcelUtil.ExcelTemplate.get(keyNo);
        if (excel == null) {
            try {
                resp.setHeader("content-type", "text/html;charset=UTF-8");
                resp.getWriter().print("<h4>未找到Excel文件的表格列。</h4>");
            } catch (IOException e) {
                logger.error("", e);
            }
            return;
        }
        String fileName = excel.getTempName();
        if (StringUtil.isEmpty(fileName)) {
            fileName = "模板下载";
        }

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(fileName);

        // 表格标题行
        HSSFRow row = sheet.createRow(0);

        // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);// true 自动换行 false 不自动换行
        // 单元格背景
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 前景颜色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 填充方式，前色填充
        // 边框填充
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        cellStyle.setBottomBorderColor(IndexedColors.ORANGE.getIndex()); // 底部边框颜色
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        cellStyle.setLeftBorderColor(IndexedColors.ORANGE.getIndex());
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        cellStyle.setTopBorderColor(IndexedColors.ORANGE.getIndex());
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        cellStyle.setRightBorderColor(IndexedColors.ORANGE.getIndex());

        List<String> mapValuesList = new ArrayList<String>(excel.getColumn().values());
        for (int i = 0; i < mapValuesList.size(); i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(mapValuesList.get(i));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(text); // cell.setCellValue(new HSSFRichTextString("hello\r\n world!")); 强制换行
            // sheet.autoSizeColumn((short)i); //宽度(自动宽度)
            sheet.setColumnWidth(i, 4000);

        }

        try {
            resp.setContentType("application/vnd.ms-excel");
            fileName = new String((fileName + ".xls").getBytes("gb2312"), "ISO8859-1");
            resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write(resp.getOutputStream());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    // excel 单元格处理
    private Object parseExcel(Cell cell) {
        if (cell == null) {
            return "";
        }
        String result = new String();
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                return cell.getDateCellValue();
                // SimpleDateFormat sdf = null;
                // if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                // .getBuiltinFormat("h:mm")) {
                // sdf = new SimpleDateFormat("HH:mm");
                // } else {// 日期
                // sdf = new SimpleDateFormat("yyyy-MM-dd");
                // }
                // Date date = cell.getDateCellValue();
                // result = sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                result = sdf.format(date);
            } else {
                double value = cell.getNumericCellValue();
                BigDecimal bd = new BigDecimal(value);
                result = String.valueOf(bd.toPlainString());
            }
            break;
        case HSSFCell.CELL_TYPE_STRING:// String类型
            result = cell.getRichStringCellValue().toString().trim();
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            result = "";
            break;
        default:
            result = "";
            break;
        }
        return result;
    }

}
