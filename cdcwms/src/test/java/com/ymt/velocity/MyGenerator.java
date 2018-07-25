package com.ymt.velocity;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

public class MyGenerator {
    public static void main(String[] args) throws IOException {
        String[] strArr = new String[] { "SysMessage", "SysWarning", "WarnRule", "WmsAdj", "WmsDeliverDock", "WmsDock",
                "WmsHandworkReceive", "WmsHandworkReceiveList", "WmsHandworkSend", "WmsHandworkSendList",
                "WmsLesReceiveListLES", "WmsLesReceiveLES", "WmsLesSendListLES", "WmsLesSendLES", "WmsMove",
                "WmsObject", "WmsStock", "WmsStocktake", "WmsStocktakePlan", "WmsTask", "WmsTaskBill",
                "WmsTaskBillList", "WmsTaskLog" };
        for (String string : strArr) {
            get(string);
        }
        System.out.println("自动生成代码操作成功");
    }

    private static void get(String key) throws IOException {
        MyGenerator generator = new MyGenerator(key, "wms", "business");
        // generator.createAction("template/Action.java.vm", "C://");
        generator.createService("template/Service.java.vm", "C:\\Java\\workspace\\wms\\cdcwms\\src\\main\\java\\");
        generator.createServiceImpl("template/ServiceImpl.java.vm",
                "C:\\Java\\workspace\\wms\\cdcwms\\src\\main\\java\\");
    }

    private String basePackage;
    private String entry;
    private String entryName;
    private String unitName;
    public final VelocityContext context = new VelocityContext();
    public Generator generator = new Generator();

    public MyGenerator(String entry, String basePackage, String unitName) {
        super();
        this.entry = entry;
        this.basePackage = basePackage;
        this.unitName = unitName;
        entryName = getFirstLowerCase(entry);
        context.put("entry", entry);
        context.put("entryName", entryName);
        context.put("basePackage", basePackage);
        context.put("author", "zhouxianglh@gmail.com");
        context.put("date", "2017.03.17");
        context.put("unitName", unitName);
    }

    public void createAction(String templatePath, String basePath) throws IOException {
        createByTag(templatePath, basePath, "Action");
    }

    public void createService(String templatePath, String basePath) throws IOException {
        createByTag(templatePath, basePath, "Service");
    }

    public void createServiceImpl(String templatePath, String basePath) throws IOException {
        createByTag(templatePath, basePath, "ServiceImpl");
    }

    public void createBiz(String templatePath, String basePath) throws IOException {
        createByTag(templatePath, basePath, "biz");
    }

    private void createByTag(String templatePath, String basePath, String tag) throws IOException {
        String tagName = getFirstLowerCase(tag);
        String packagePath = StringUtils.join(basePackage, ".", unitName, ".", tagName);
        String filePath = StringUtils.join(basePath, packagePath.replace(".", File.separator), File.separator, entry,
                tag, ".java");
        generator.vmToFile(context, templatePath, filePath);

    }

    public String getFirstLowerCase(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1, str.length());
    }
}
