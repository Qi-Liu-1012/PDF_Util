package com.lgq.pdf_util.util;

import com.aspose.pdf.*;

import java.io.FileOutputStream;

public class PDFHelper3 {

    //pdf转doc
    public static void pdfToDoc(String pdfPath) {
        long old = System.currentTimeMillis();
        try {
            //新建一个word文档
            String wordPath=pdfPath.substring(0,pdfPath.lastIndexOf("."))+".docx";
            FileOutputStream os = new FileOutputStream(wordPath);
            //doc是将要被转化的word文档
            Document doc = new Document(pdfPath);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.DocX);
            os.close();
            //转化用时
            long now = System.currentTimeMillis();
            System.out.println("Pdf 转 Word 共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            System.out.println("Pdf 转 Word 失败...");
            e.printStackTrace();
        }
    }

    /**
     * 删除PDF某页
     * @param pdfPath
     */
    public static void pdfDeletePage(String pdfPath, Integer page) {
        long old = System.currentTimeMillis();
        try {
            Document doc = new Document(pdfPath);
            PageCollection pages = doc.getPages();
            Resources resources = pages.get_Item(page).getResources();
            resources.getImages().delete();
            doc.save(doc.getFileName() + "_new.pdf");
            long now = System.currentTimeMillis();
            System.out.println("删除共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            System.out.println("删除 失败...");
            e.printStackTrace();
        }
    }
}