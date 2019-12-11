package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 类描述: 读取配置文件. <br />
 *
 * @author hanjun.hw
 * @since 2019/5/21
 */
public class PropsReadUtil {
    /***
     *
     * @param isInClassPath
     *            : whether prop file is in classpath
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Properties getProperties(boolean isInClassPath,
                                           Object filePath) throws IOException {
        InputStream in = null;
        if (isInClassPath) {// if prop file is in classpath
            in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream((String) filePath);
        } else {
            File propFile = null;
            if (filePath instanceof File) {
                propFile = (File) filePath;
            } else {
                propFile = new File((String) filePath);
            }
            if (!propFile.exists()) {
                System.out.println("[PropsReadUtil]File \""
                        + propFile.getAbsolutePath() + "\" does not exist.");
            } else {
                in = new FileInputStream(propFile);
            }
        }
        if (in == null) {
            return null;
        }
        Properties prop = new Properties();
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return prop;
    }
}
