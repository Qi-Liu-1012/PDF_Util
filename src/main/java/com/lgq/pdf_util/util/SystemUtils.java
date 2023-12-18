package com.lgq.pdf_util.util;

/**
 * @Author liuguoqiang
 * @Date 2023/8/21
 * @Version 1.0
 */
public class SystemUtils {
    public static String getDocumentsPath() {
        return System.getProperty("user.home") + "\\Documents";
    }
}
