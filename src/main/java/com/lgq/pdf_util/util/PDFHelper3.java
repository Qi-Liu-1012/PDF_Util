package com.lgq.pdf_util.util;

import com.aspose.pdf.*;
import org.dromara.pdf.pdfbox.doc.XEasyPdfDocument;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class PDFHelper3 {

    //pdf转doc
    public static void pdfToDoc(InputStream inputStream, OutputStream outputStream) {
        Document doc = null;
        try {
            //doc是将要被转化的word文档
            doc = new Document(inputStream);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(outputStream, SaveFormat.DocX);
        } finally {
            if (Objects.nonNull(doc)) {
                doc.close();
            }
        }
    }

    /**
     * 删除PDF某页
     *
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