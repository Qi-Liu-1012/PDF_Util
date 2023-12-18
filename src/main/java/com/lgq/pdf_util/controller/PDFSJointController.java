package com.lgq.pdf_util.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.lgq.pdf_util.PdfUtilApplication;
import com.lgq.pdf_util.util.FileUtils;
import com.lgq.pdf_util.util.OtherPdfHelper;
import com.lgq.pdf_util.util.SystemUtils;
import com.lgq.pdf_util.view.BasePageView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liuguoqiang
 * @Date 2023/12/18
 * @Version 1.0
 */
@FXMLController
public class PDFSJointController extends FXController {
    @FXML
    private JFXButton toBasePageBtn;
    @FXML
    private JFXButton chooseDestinationFolderBtn;
    @FXML
    private JFXButton startBtn;
    @FXML
    private TextField pathTF;
    @FXML
    private Label totalLab;
    @FXML
    private JFXTextArea filesJFX;
    @FXML
    private JFXTextArea messageJFX;

    private static String cachePath = SystemUtils.getDocumentsPath();
    private static String fileNameFinal = "";

    public void toBasePage(ActionEvent actionEvent) {
        Stage stage = getStage(toBasePageBtn);
        goToPage(stage, BasePageView.class);
    }

    public void printMsg(String msg) {
        messageJFX.setText(msg);
    }

    public void chooseDestinationFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择文件夹");
        directoryChooser.setInitialDirectory(new File(cachePath));
        File outDirectory = directoryChooser.showDialog(getStage(chooseDestinationFolderBtn));
        String outFolderPath = outDirectory.getPath();

        //读取文件夹下的所有pdf
        List<String> fileNames = FileUtils.readPDFFiles(outDirectory);
        pathTF.setText(outFolderPath);
        cachePath = outFolderPath;
        String text = String.join("\n", fileNames);
        fileNameFinal = text;
        filesJFX.setText(text);
        totalLab.setText(String.valueOf(fileNames.size()));
        printMsg("导入成功");
    }

    public void start(ActionEvent actionEvent) {
        try {
            List<File> fileList = Arrays.stream(fileNameFinal.split("\n")).map(e -> {
                String filePath = cachePath + "\\" + e;
                return new File(filePath);
            }).toList();
            String outPath = cachePath + "\\" + "合并结果.pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            OtherPdfHelper.concatPDFsByPage(fileList, fileOutputStream);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        printMsg("导出成功");
    }
}
