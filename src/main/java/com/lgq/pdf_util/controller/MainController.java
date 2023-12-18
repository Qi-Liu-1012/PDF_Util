package com.lgq.pdf_util.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.lgq.pdf_util.PdfUtilApplication;
import com.lgq.pdf_util.view.BasePageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author liuguoqiang
 * @Date 2023/8/16
 * @Version 1.0
 */
public class MainController extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(PdfUtilApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        BasePageView basePageView = SpringUtil.getBean(BasePageView.class);
        stage.setScene(new Scene(basePageView.getView()));
        stage.show();
    }
}
