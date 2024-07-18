package com.lgq.pdf_util.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.dromara.pdf.pdfbox.doc.XEasyPdfDocument;
import org.dromara.pdf.pdfbox.doc.XEasyPdfPage;
import org.dromara.pdf.pdfbox.handler.XEasyPdfHandler;

import java.io.*;
import java.util.*;

/**
 * @Author liuguoqiang
 * @Date 2024/2/26
 * @Version 1.0
 */
public class XEasyPdfUtils {
    public static void main(String[] args) throws Exception {
        File file1 = new File("C:\\Users\\82703\\Desktop\\ceshi\\1-合同.pdf");
        File file1_1 = new File("E:\\user-file\\wechat\\WeChat Files\\wxid_vyj1cviixz9321\\FileStorage\\File\\2024-02\\经责审计取证单-out5e7b8320-3613-406d-9eec-88575da744a6.pdf");
        ArrayList<File> list = new ArrayList<>();
        list.add(file1);
        list.add(file1_1);
        File file2 = new File("E:\\user-file\\wechat\\WeChat Files\\" +
                "wxid_vyj1cviixz9321\\FileStorage\\File\\2024-02\\经责审计取证单-out" +
                UUID.randomUUID().toString() + ".pdf");
        System.out.println(getPdfPages(file1));
//        FileOutputStream fileOutputStream = new FileOutputStream(file2);
//        deletePdfPage(inputStream, fileOutputStream, Arrays.asList(23, 24));
//         System.out.println(getPdfPages(file1));
//        splitPdf(inputStream, fileOutputStream, 1, 23);
//        concatPDFsByPage(list, fileOutputStream);
    }

    private static void closeDocument(XEasyPdfDocument document) {
        if (Objects.nonNull(document)) {
            try {
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deletePdfPage(InputStream inputStream, OutputStream outputStream, List<Integer> deletes) {
        XEasyPdfDocument document = null;
        XEasyPdfDocument outDocument = null;
        try {
            document = XEasyPdfHandler.Document.load(inputStream);
            outDocument = XEasyPdfHandler.Document.build();
            Integer currentIndex = 0;
            for (XEasyPdfPage xEasyPdfPage : document.getPageList()) {
                currentIndex++;
                if (deletes.contains(currentIndex)) {
                    continue;
                }
                outDocument.addPage(xEasyPdfPage);
            }
            outDocument.flush();
            outDocument.save(outputStream);
        } finally {
            closeDocument(document);
            closeDocument(outDocument);
        }
    }

    public static Integer getPdfPages(File file) throws IOException {
        FileInputStream inputStream = null;
        XEasyPdfDocument document = null;
        try {
            inputStream = new FileInputStream(file);
            document = XEasyPdfHandler.Document.load(inputStream);
            return document.getPageList().size();
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
            closeDocument(document);
        }
    }


    /**
     * 截取pdf
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param fromPage     起始页
     * @param toPage       结束页
     */
    public static void splitPdf(InputStream inputStream, OutputStream outputStream, int fromPage, int toPage) {
        if (fromPage > toPage) {
            return;
        }
        XEasyPdfDocument document = null;
        XEasyPdfDocument outDocument = null;
        try {
            document = XEasyPdfHandler.Document.load(inputStream);
            outDocument = XEasyPdfHandler.Document.build();
            Integer currentIndex = 0;
            for (XEasyPdfPage xEasyPdfPage : document.getPageList()) {
                currentIndex++;
                if (currentIndex >= fromPage && currentIndex <= toPage) {
                    outDocument.addPage(xEasyPdfPage);
                }
            }
            outDocument.flush();
            outDocument.save(outputStream);

        } finally {
            closeDocument(document);
            closeDocument(outDocument);
        }
    }

    /**
     * 把多个pdf合并成一个pd
     */
    public static void concatPDFsByPage(List<File> files, OutputStream outputStream) throws FileNotFoundException {
        XEasyPdfDocument document = null;
        XEasyPdfDocument outDocument = null;
        FileInputStream inputStream = null;
        try {
            outDocument = XEasyPdfHandler.Document.build();
            for (File file : files) {
                inputStream = new FileInputStream(file);
                document = XEasyPdfHandler.Document.load(inputStream);
                for (XEasyPdfPage page : document.getPageList()) {
                    outDocument.addPage(page);
                }
                outDocument.flush();
            }
            outDocument.save(outputStream);
        } finally {
            closeDocument(document);
            closeDocument(outDocument);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void withdrawPdfPage(FileInputStream inputStream, FileOutputStream outputStream, List<Integer> pages) {
        XEasyPdfDocument document = null;
        XEasyPdfDocument outDocument = null;
        try {
            document = XEasyPdfHandler.Document.load(inputStream);
            outDocument = XEasyPdfHandler.Document.build();
            List<XEasyPdfPage> pageList = document.getPageList();
            for (Integer page : pages) {
                XEasyPdfPage pdfPage = CommonUtils.getListElementOrDefault(pageList, page - 1, null);
                if (Objects.nonNull(pdfPage)) {
                    outDocument.addPage(pdfPage);
                }
            }
            outDocument.flush();
            outDocument.save(outputStream);
        } finally {
            closeDocument(document);
            closeDocument(outDocument);
        }
    }
}
