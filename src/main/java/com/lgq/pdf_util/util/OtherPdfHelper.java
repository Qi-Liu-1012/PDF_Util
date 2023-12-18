package com.lgq.pdf_util.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.*;

public class OtherPdfHelper {

    public static Integer getPdfPages(File file) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            PdfReader inputPdf = new PdfReader(inputStream);
            return inputPdf.getNumberOfPages();
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
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
    public static void splitPdf(InputStream inputStream, OutputStream outputStream, int fromPage, int toPage) throws DocumentException, IOException {
        Document document = new Document();
        try {
            PdfReader inputPdf = new PdfReader(inputStream);
            int totalPages = inputPdf.getNumberOfPages();
            if (fromPage > toPage) {
                fromPage = toPage;
            }
            if (toPage > totalPages) {
                toPage = totalPages;
            }
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            while (fromPage <= toPage) {
                document.setPageSize(inputPdf.getPageSize(inputPdf.getPageN(fromPage)));
                document.newPage();
                page = writer.getImportedPage(inputPdf, fromPage);
                cb.addTemplate(page, 0, 0);
                fromPage++;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    public static void deletePdfPage(InputStream inputStream, OutputStream outputStream, List<Integer> deletes) throws DocumentException, IOException {
        Document document = new Document();
        try {
            PdfReader inputPdf = new PdfReader(inputStream);
            int totalPages = inputPdf.getNumberOfPages();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            for (int i = 1; i <= totalPages; i++) {
                if (deletes.contains(i)) {
                    continue;
                }
                document.newPage();
                page = writer.getImportedPage(inputPdf, i);
                cb.addTemplate(page, 0, 0);
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 把多个pdf合并成一个pd
     */
    public static void concatPDFsByPage(List<File> files, OutputStream outputStream) {
        Document document = new Document();
        FileInputStream inputStream = null;
        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            int outPage = 0;
            PdfContentByte cb;
            PdfReader inputPdf;
            PdfImportedPage page;
            for (File file : files) {
                int curPage = 1;
                inputStream = new FileInputStream(file);
                inputPdf = new PdfReader(inputStream);
                int totalPages = inputPdf.getNumberOfPages();
                document.open();
                cb = writer.getDirectContent();
                while (curPage <= totalPages) {
                    document.setPageSize(inputPdf.getPageSize(inputPdf.getPageN(curPage)));
                    document.newPage();
                    page = writer.getImportedPage(inputPdf, curPage);
                    cb.addTemplate(page, 0, 0);
                    curPage++;
                    outPage++;
                }
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
