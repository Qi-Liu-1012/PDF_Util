package com.lgq.pdf_util.controller;

import com.jfoenix.controls.JFXButton;
import com.lgq.pdf_util.util.FXUtils;
import com.lgq.pdf_util.view.PDFSJointView;
import com.lgq.pdf_util.view.PDFToDOCPageView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

@FXMLController
public class BasePageController extends FXController {

    @FXML
    private JFXButton toPDFToDOCPageBtn;

    @FXML
    private JFXButton toPDFSJointPageBtn;

    public void toPDFToDOCPage(ActionEvent actionEvent) {
        Stage stage = getStage(toPDFToDOCPageBtn);
        goToPage(stage, PDFToDOCPageView.class);
    }

    public void toPDFSJointPage(ActionEvent actionEvent) {
        Stage stage = getStage(toPDFSJointPageBtn);
        goToPage(stage, PDFSJointView.class);
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }
}
