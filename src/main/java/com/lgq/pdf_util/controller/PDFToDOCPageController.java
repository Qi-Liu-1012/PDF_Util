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
import java.util.Arrays;

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
    private Label totalPageLab;
    @FXML
    private JFXButton toDocBtn;
    @FXML
    private JFXButton toPdfBtn;
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
        cacheInFolder = file.getParent();
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");
        fileNameTF.setText(fileName);
        outFileNameTF.setText(fileName.substring(0, lastIndex) + "_temp");
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

    public void toDoc(ActionEvent actionEvent) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String outPath = getOutFilePath(".doc");
            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            ItextPdfHelper.toDoc(fileInputStream, fileOutputStream);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        printMsg("导出成功");
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
