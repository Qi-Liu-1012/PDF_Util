package com.lgq.pdf_util.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.lgq.pdf_util.elements.CustomDialog;
import com.lgq.pdf_util.util.FXUtils;
import com.lgq.pdf_util.util.OtherPdfHelper;
import com.lgq.pdf_util.util.PDFHelper3;
import com.lgq.pdf_util.util.SystemUtils;
import com.lgq.pdf_util.view.BasePageView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private JFXButton startBtn;
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
            Integer page = OtherPdfHelper.getPdfPages(file);
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

    public void start(ActionEvent actionEvent) {
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
            String outPath = getOutFilePath();
            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            OtherPdfHelper.splitPdf(fileInputStream, fileOutputStream, fromPage, toPage);

        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        printMsg("导出成功");
    }

    private String getOutFilePath() {
        String outPath = "";
        if (StringUtils.hasText(cacheOutFolder)) {
            outPath = cacheOutFolder;
        } else {
            outPath = cacheInFolder;
        }
        return outPath + "\\" + outFileNameTF.getText() + ".pdf";
    }

    private Integer fxGetInt(TextInputControl c) {
        String text = c.getText();
        if (StringUtils.hasText(text)) {
            return Integer.parseInt(startPageTF.getText());
        }
        return 0;
    }

//    public void initialize11(URL location, ResourceBundle resources) {
//        docBtn.setOnAction(e -> {
//            String filePath = a.getText();
//            try {
//                PDFHelper3.pdfToDoc(filePath);
//                c.setText("成功");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                c.setText(e.toString());
//            }
//        });
//        cutBtn.setOnAction(e -> {
//            String filePath = a.getText();
////            String outPath = b.getText();
//            int fromPage = Integer.parseInt(page1.getText());
//            int toPage = Integer.parseInt(page2.getText());
//            try {
//                FileInputStream fileInputStream = new FileInputStream(filePath);
////                FileOutputStream fileOutputStream = new FileOutputStream(outPath);
//                String outPath = filePath.substring(0, filePath.lastIndexOf(".")) + "-temp.pdf";
//                FileOutputStream fileOutputStream = new FileOutputStream(outPath);
//                OtherPdfHelper.splitPdf(fileInputStream, fileOutputStream, fromPage, toPage);
//                c.setText("成功");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                c.setText(e.toString());
//            }
//        });
//        delBtn.setOnAction(e -> {
//            String filePath = a.getText();
//            String outPath = b.getText();
//            List<String> deleteList = Arrays.asList(deletes.getText().split(","));
//            List<Integer> list = deleteList.stream().map(Integer::parseInt).collect(Collectors.toList());
//            try {
//                FileInputStream fileInputStream = new FileInputStream(filePath);
//                FileOutputStream fileOutputStream = new FileOutputStream(outPath);
//                OtherPdfHelper.deletePdfPage(fileInputStream, fileOutputStream, list);
//                c.setText("成功");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                c.setText(e.toString());
//            }
//        });
//    }

    public void printMsg(String msg) {
        messageJFX.setText(msg);
    }
}
