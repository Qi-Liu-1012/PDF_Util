package com.lgq.pdf_util.util;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author liuguoqiang
 * @Date 2024/4/1
 * @Version 1.0
 */
public class ItextPdfUtils {

    public static Map<String, Object> getKeywordLocation(File file, String keyword) {
        Map<String, Object> result = new HashMap<>();
        FileInputStream inputStream = null;

        //使用这种方式，无需再关闭了，处理完自动关闭
        try  {
            inputStream = new FileInputStream(file);
            PdfReader reader = new PdfReader(inputStream);
            PdfDocument pdfDocument = new PdfDocument(reader);
            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
                PdfPage page = pdfDocument.getPage(i);
                RegexBasedLocationExtractionStrategy strategy = new RegexBasedLocationExtractionStrategy(keyword);
                PdfCanvasProcessor canvasProcessor = new PdfCanvasProcessor(strategy);
                canvasProcessor.processPageContent(page);
                Collection<IPdfTextLocation> resultantLocations = strategy.getResultantLocations();
                // 自定义结果处理
                if (!resultantLocations.isEmpty()) {
                    List<Map<String, Object>> locationList = new ArrayList<>();
                    for (IPdfTextLocation item : resultantLocations) {
                        Map<String, Object> map = new HashMap<>();
                        Rectangle rectangle = item.getRectangle();
                        map.put("page", item.getPageNumber());
                        map.put("absoluteX", rectangle.getX());
                        map.put("absoluteY", rectangle.getY());
                        map.put("width", rectangle.getWidth());
                        map.put("height", rectangle.getHeight());
                        map.put("keyword", item.getText());
                        map.put("top", rectangle.getTop());
                        map.put("bottom", rectangle.getBottom());
                        map.put("left", rectangle.getLeft());
                        map.put("right", rectangle.getRight()); // 右坐标
                        map.put("count", locationList.size() + 1); // 出现次数
                        locationList.add(map);
                    }
                    result.put(String.valueOf(i), locationList);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> getTagLocation(File file, String tag) {
        Map<String, Object> result = new HashMap<>();
        FileInputStream inputStream = null;

        //使用这种方式，无需再关闭了，处理完自动关闭
        try  {
            inputStream = new FileInputStream(file);
            PdfReader reader = new PdfReader(inputStream);
            PdfDocument pdfDocument = new PdfDocument(reader);
            Rectangle rect = new Rectangle(1, 1, pdfDocument.getPage(1).getPageSize().getWidth() - 1,
                    pdfDocument.getPage(1).getPageSize().getHeight() - 1);
            LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
            PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy);
            parser.processPageContent(pdfDocument.getPage(1));
            String resultantText = strategy.getResultantText();
            System.out.println(resultantText);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        File file = new File("E:\\user-file\\work\\test.pdf");
        Map<String, Object> result = getTagLocation(file, "draw:frame");
        System.out.println(result);
    }
}
