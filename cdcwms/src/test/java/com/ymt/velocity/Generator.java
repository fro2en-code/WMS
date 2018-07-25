package com.ymt.velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * Hello world!
 *
 */
public class Generator {
    private static final String strCharset = Charset.forName("UTF-8").name();
    /**
     * velocity引擎
     */
    private VelocityEngine engine;

    /**
     * 设置模版引擎，主要指向获取模版路径
     */
    private VelocityEngine getVelocityEngine() {
        if (engine == null) {
            Properties p = new Properties();
            p.setProperty("file.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, strCharset);
            p.setProperty(Velocity.INPUT_ENCODING, strCharset);
            p.setProperty(Velocity.OUTPUT_ENCODING, strCharset);
            p.setProperty("file.resource.loader.unicode", "true");
            engine = new VelocityEngine(p);
        }
        return engine;
    }

    /**
     * 根据模板生成文件
     *
     * @param context
     * @param templatePath
     */
    public String vmToString(VelocityContext context, String templatePath) {
        VelocityEngine velocity = getVelocityEngine();
        StringWriter stringWriter = new StringWriter();
        velocity.mergeTemplate(templatePath, strCharset, context, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 将模板转化成为文件
     *
     * @param context
     *            内容对象
     * @param templatePath
     *            模板文件
     * @param outputFile
     *            文件生成的目录
     */
    public void vmToFile(VelocityContext context, String templatePath, String outputFile) throws IOException {
        String str = vmToString(context, templatePath);
        File file = new File(outputFile);
        File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(file.toPath(), str.getBytes(strCharset));
    }

}
