package com.lgq.pdf_util.elements;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.lgq.pdf_util.util.TimerUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 自定义弹出框 组件
 */
public class CustomDialog {
    //气泡
    public static int BUBBLE = 0;
    //弹窗
    public static int POP_UP = 1;
    //弹窗
    public static int CHECK = 2;
    //内嵌式弹窗
    public static int IFRAME = 3;

    private Integer height;
    private Integer width;
    private JFXDialog dialog;

    public static JFXDialog bubbleMsg(Pane context, String msg) {
       return bubbleMsg(context, msg, 2L, TimeUnit.SECONDS);
    }

    public static JFXDialog bubbleMsg(Pane context, String msg, Long time, TimeUnit timeUnit) {
        StackPane root = new StackPane();
        root.setPrefHeight(context.getHeight());
        root.setPrefWidth(context.getWidth());

        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Label(msg));
        JFXDialog result = new JFXDialog(root, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        context.getChildren().add(root);
        result.show(root);
        TimerUtils.timerDelayDo(new TimerTask() {
            @Override
            public void run() {
                result.close();
//                context.getChildren().remove(root);
            }
        }, time, timeUnit);
        return result;
    }
}
