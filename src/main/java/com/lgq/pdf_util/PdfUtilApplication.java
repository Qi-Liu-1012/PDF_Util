package com.lgq.pdf_util;

import com.lgq.pdf_util.controller.MainController;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfUtilApplication {

    public static void main(String[] args) {
        Application.launch(MainController.class, args);
    }
}
