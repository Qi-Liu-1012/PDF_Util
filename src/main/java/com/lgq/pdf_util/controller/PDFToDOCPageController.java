package com.lgq.pdf_util.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.lgq.pdf_util.util.*;
import com.lgq.pdf_util.view.BasePageView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@FXMLController
public class PDFToDOCPageController extends FXController {
    @FXML
    private BorderPane context;
    @FXML
    private JFXButton toBasePageBtn;
    @FXML
    private JFXButton chooseFileBtn;
    @FXML
    private JFXButton convertBtn;
    @FXML
    private JFXButton chooseDestinationFolderBtn;
    @FXML
    private TextField fileNameTF;
    @FXML
    private TextField outFolderTF;
    @FXML
    private TextField outFileNameTF;
    @FXML
    private TextField startPageTF;
    @FXML
    private TextField endPageTF;
    @FXML
    private TextField deletePagesTF;
    @FXML
    private Label totalPageLab;
    @FXML
    private JFXButton toDocBtn;
    @FXML
    private JFXButton toPdfBtn;
    @FXML
    private JFXButton deletePageBtn;
    @FXML
    private JFXTextArea messageJFX;

    private File file;
    private Integer totalPage;
    private static String cacheInFolder = SystemUtils.getDocumentsPath();
    private String cacheOutFolder;

    public String getCacheOutFolder() {
        return cacheOutFolder == null ? cacheInFolder : cacheOutFolder;
    }

    public void toBasePage(ActionEvent actionEvent) {
        Stage stage = getStage(toBasePageBtn);
        goToPage(stage, BasePageView.class);
    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(cacheInFolder));
        fileChooser.setTitle("选择PDF文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        file = fileChooser.showOpenDialog(getStage(chooseFileBtn));
        file = new File(file.getAbsolutePath());
        cacheInFolder = file.getParent();
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");
        fileNameTF.setText(fileName);
        outFileNameTF.setText(fileName.substring(0, lastIndex) + "_" + UUID.randomUUID());
        try {
            Integer page = XEasyPdfUtils.getPdfPages(file);
            totalPage = page;
            totalPageLab.setText(page.toString());
        } catch (Exception e) {
//            CustomDialog.bubbleMsg(context, e.getMessage());
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
//        CustomDialog.bubbleMsg(context, "导入成功");
        printMsg("导入成功");
    }

    public void chooseDestinationFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择导出文件夹");
        directoryChooser.setInitialDirectory(new File(getCacheOutFolder()));
        File outDirectory = directoryChooser.showDialog(getStage(chooseDestinationFolderBtn));
        String outFolderPath = outDirectory.getPath();
        outFolderTF.setText(outFolderPath);
        cacheOutFolder = outFolderPath;
    }

    public void toPdf(ActionEvent actionEvent) {
        int fromPage = fxGetInt(startPageTF);
        int toPage = fxGetInt(endPageTF);
        if (fromPage <= 0) {
            fromPage = 1;
        }
        if (toPage <= 0) {
            toPage = totalPage;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String outPath = getOutFilePath(".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            XEasyPdfUtils.splitPdf(fileInputStream, fileOutputStream, fromPage, toPage);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        printMsg("导出成功");
    }

    public void toDoc(ActionEvent actionEvent) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            String outPath = getOutFilePath(".doc");
            fileOutputStream = new FileOutputStream(outPath);
            PDFHelper3.pdfToDoc(fileInputStream, fileOutputStream);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            if (Objects.nonNull(fileInputStream)) {
                fileInputStream.close();
            }
            if (Objects.nonNull(fileOutputStream)) {
                fileOutputStream.close();
            }
        }
        printMsg("导出成功");
    }

    public void deletePage(ActionEvent actionEvent) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            List<Integer> deletePages = getDeletePages();
            fileInputStream = new FileInputStream(file);
            String outPath = getOutFilePath(".pdf");
            fileOutputStream = new FileOutputStream(outPath);
            XEasyPdfUtils.deletePdfPage(fileInputStream, fileOutputStream, deletePages);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            if (Objects.nonNull(fileInputStream)) {
                fileInputStream.close();
            }
            if (Objects.nonNull(fileOutputStream)) {
                fileOutputStream.close();
            }
        }
        printMsg("导出成功");
    }

    public List<Integer> getDeletePages() {
        String text = deletePagesTF.getText();
        if (!StringUtils.hasText(text)) {
            printMsg("请正确输入要删除的页码, 规则【页码之间使用英文逗号分隔】。例如(1,2,3)");
        }
        try {
            return CommonUtils.splitNum(text);
        } catch (Exception e) {
            printMsg("请正确输入要删除的页码, 规则【页码之间使用英文逗号分隔】。例如(1,2,3)");
            throw e;
        }
    }

    private String getOutFilePath(String suffix) {
        String outPath = "";
        if (StringUtils.hasText(cacheOutFolder)) {
            outPath = cacheOutFolder;
        } else {
            outPath = cacheInFolder;
        }
        return outPath + "\\" + outFileNameTF.getText() + suffix;
    }

    private Integer fxGetInt(TextInputControl c) {
        String text = c.getText();
        if (StringUtils.hasText(text)) {
            return Integer.parseInt(startPageTF.getText());
        }
        return 0;
    }

    public void printMsg(String msg) {
        messageJFX.setText(msg);
    }
}
