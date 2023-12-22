package com.lgq.pdf_util.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
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
    private JFXButton multiFileChooseBtn;
    @FXML
    private JFXButton startBtn;
    @FXML
    private TextField pathTF;
    @FXML
    private TextField outTF;
    @FXML
    private Label totalLab;
    @FXML
    private JFXTextArea filesJFX;
    @FXML
    private JFXTextArea messageJFX;

    private static String cachePath = SystemUtils.getDocumentsPath();
    private static String outPath = SystemUtils.getDocumentsPath();
    private static Integer pdfNum = 0;
    private static String fileNameFinal = "";
    private static List<File> fileFinal = new ArrayList<>();



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
        String text = fileNames.stream().peek(e -> {
            String filePath = cachePath + "\\" + e;
            fileFinal.add(new File(filePath));
        }).collect(Collectors.joining("\n"));
        if (StringUtils.hasText(fileNameFinal)) {
            text = "\n" + text;
        }
        fileNameFinal = fileNameFinal + text;
        filesJFX.setText(text);
        pdfNum += fileNames.size();
        totalLab.setText(pdfNum.toString());
        printMsg("导入成功");
    }

    public void chooseOutDestinationFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择文件夹");
        directoryChooser.setInitialDirectory(new File(outPath));
        File outDirectory = directoryChooser.showDialog(getStage(chooseDestinationFolderBtn));
        if (Objects.isNull(outDirectory)){
            return;
        }
        outPath = outDirectory.getPath();
        outTF.setText(outPath);
    }

    public void chooseMultiFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择PDF文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialDirectory(new File(cachePath));
        List<File> files = fileChooser.showOpenMultipleDialog(getStage(multiFileChooseBtn));
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        File file = files.get(0);
        cachePath = file.getParent();
        String text = files.stream().map(File::getName).collect(Collectors.joining("\n"));
        if (StringUtils.hasText(fileNameFinal)) {
            text = "\n" + text;
        }
        fileNameFinal = fileNameFinal + text;
        filesJFX.setText(fileNameFinal);
        pdfNum += files.size();
        totalLab.setText(pdfNum.toString());
        fileFinal.addAll(files);
        printMsg("导入成功");
    }

    public void start(ActionEvent actionEvent) {
        if (Objects.isNull(outPath)){
            return;
        }
        UUID uuid = UUID.randomUUID();
        String out = outPath + "\\" + "合并结果_" + uuid + ".pdf";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            OtherPdfHelper.concatPDFsByPage(fileFinal, fileOutputStream);
        } catch (Exception e) {
            printMsg(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            return;
        }
        printMsg("导出成功， 导出文件地址：" + outPath);
    }
}
