package com.lgq.pdf_util.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author liuguoqiang
 * @Date 2023/12/18
 * @Version 1.0
 */
public class FileUtils {
    public static List<String> readPDFFiles(File folder) {
        ArrayList<String> list = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                    // 处理读取到的 PDF 文件
                    list.add(file.getName());
                }
            }
        }
        return list;
    }

    public static byte[] getFileBytes(File file) throws Exception {
        ByteArrayOutputStream out = null;
        InputStream is = null;
        try {
            out = new ByteArrayOutputStream();
            is = new FileInputStream(file);
            byte[] data = new byte[2048];
            int len;
            while ((len = is.read(data)) != -1) {
                out.write(data, 0, len);
            }
            return out.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
}
