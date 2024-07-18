package com.lgq.pdf_util.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author liuguoqiang
 * @Date 2024/7/10
 * @Version 1.0
 */
public class PdfUtils {
    public static void pdfToDoc(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream);
             XWPFDocument wordDoc = new XWPFDocument()) {

            // 使用PDFTextStripper从PDF中提取文本
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // 将提取的文本添加到Word文档中
            wordDoc.createParagraph().createRun().setText(text);

            // 将Word文档保存到文件
            wordDoc.write(outputStream);
        }
    }
}
