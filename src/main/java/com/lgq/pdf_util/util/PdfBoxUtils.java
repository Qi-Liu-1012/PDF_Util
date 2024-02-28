package com.lgq.pdf_util.util;

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @Author liuguoqiang
 * @Date 2024/2/26
 * @Version 1.0
 */
public class PdfBoxUtils {
    //pdf转doc
    public static void pdfToDoc(InputStream inputStream, OutputStream outputStream) throws IOException {
        PDDocument document = null;

        //获取PDDocumentInformation
        PDDocumentInformation pdd = null;
        try {
            document = PDDocument.load(inputStream);
            pdd = document.getDocumentInformation();
            //获取页数
            int pages = document.getNumberOfPages();

            //使用 PDFTextStripper 提取文本
            PDFTextStripper stripper = new PDFTextStripper();
            //设置转换的页数
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            //获取文本
            String content = stripper.getText(document);
            outputStream.write(content.getBytes());
        } finally {
            if (Objects.nonNull(document)) {
                document.close();
            }
        }
    }
}
