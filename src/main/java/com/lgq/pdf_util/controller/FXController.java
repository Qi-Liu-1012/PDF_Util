package com.lgq.pdf_util.controller;

import cn.hutool.extra.spring.SpringUtil;
import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * @Author liuguoqiang
 * @Date 2023/8/20
 * @Version 1.0
 */
public abstract class FXController {

    protected Stage getStage(Control control) {
        Scene scene = control.getScene();
        return (Stage) scene.getWindow();
    }

    /**
     * 页面跳转
     * @param stage 场景
     * @param clazz 页面
     */
    protected void goToPage(Stage stage, Class<? extends AbstractFxmlView> clazz) {
        AbstractFxmlView view = SpringUtil.getBean(clazz);
        stage.getScene().setRoot(view.getView());
    }
}
