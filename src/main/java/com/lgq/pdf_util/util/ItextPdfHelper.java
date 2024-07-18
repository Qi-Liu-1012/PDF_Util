package com.lgq.pdf_util.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.*;

public class ItextPdfHelper {

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


    public static void toDoc(InputStream inputStream, OutputStream outputStream) throws DocumentException, IOException {
        Document document = new Document();
        try {
            PdfReader inputPdf = new PdfReader(inputStream);
            int totalPages = inputPdf.getNumberOfPages();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            for (int i = 1; i < totalPages; i++) {
                document.setPageSize(inputPdf.getPageSize(inputPdf.getPageN(i)));
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


    public static void deletePdfPage(InputStream inputStream, OutputStream outputStream, List<Integer> deletes) throws DocumentException, IOException {
        Document document = new Document();
        try {
            PdfReader inputPdf = new PdfReader(inputStream);
            int totalPages = inputPdf.getNumberOfPages();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte cb;
            PdfImportedPage page;
            for (int i = 1; i <= totalPages; i++) {
                if (deletes.contains(i)) {
                    continue;
                }
                cb = writer.getDirectContent();
                document.setPageSize(inputPdf.getPageSize(inputPdf.getPageN(i)));
                document.newPage();
                page = writer.getImportedPage(inputPdf, i);
                int rotation = inputPdf.getPageRotation(i);

                // 计算并应用正确的转换矩阵
                double[] matrix = getTransformationMatrix(rotation, inputPdf.getPageSizeWithRotation(i));
                cb.addTemplate(page, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
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

    // 获取旋转后用于addTemplate的矩阵参数
    private static double[] getTransformationMatrix(int rotation, Rectangle pageSize) {
        double width = pageSize.getWidth();
        double height = pageSize.getHeight();

        switch (rotation) {
            case 90:
                return new double[]{0, -1, 1, 0, 0, height};
            case 180:
                return new double[]{-1, 0, 0, -1, width, height};
            case 270:
                return new double[]{0, 1, -1, 0, width, 0};
            default: // 包括0度（即无旋转）
                return new double[]{1, 0, 0, 1, 0, 0};
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        File file1 = new File("E:\\user-file\\wechat\\WeChat Files\\wxid_vyj1cviixz9321\\FileStorage\\File\\2024-02\\经责审计取证单.pdf");
        File file2 = new File("E:\\user-file\\wechat\\WeChat Files\\" +
                "wxid_vyj1cviixz9321\\FileStorage\\File\\2024-02\\经责审计取证单-out" +
                UUID.randomUUID().toString() + ".pdf");
        FileInputStream inputStream = new FileInputStream(file1);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        deletePdfPage(inputStream, fileOutputStream, Arrays.asList(23, 24));
    }

    /**
     * 把多个pdf合并成一个pd
     */
    public static void concatPDFsByPage(List<File> files, OutputStream outputStream) {
        Document document = new Document();
        FileInputStream inputStream = null;
        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte cb;
            PdfReader inputPdf;
            PdfImportedPage page;
            for (File file : files) {
                int curPage = 1;
                inputStream = new FileInputStream(file);
                inputPdf = new PdfReader(inputStream);
                int totalPages = inputPdf.getNumberOfPages();
                cb = writer.getDirectContent();
                while (curPage <= totalPages) {
                    document.setPageSize(inputPdf.getPageSize(inputPdf.getPageN(curPage)));
                    document.newPage();
                    page = writer.getImportedPage(inputPdf, curPage);
                    cb.addTemplate(page, 0, 0);
                    curPage++;
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
