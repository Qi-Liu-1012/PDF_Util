package com.lgq.pdf_util;

import com.lgq.pdf_util.view.BasePageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author liuguoqiang
 * @Date 2023/8/16
 * @Version 1.0
 */
@SpringBootApplication
public class PDFUtilApplication extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(PDFUtilApplication.class, BasePageView.class, args);
    }
}
