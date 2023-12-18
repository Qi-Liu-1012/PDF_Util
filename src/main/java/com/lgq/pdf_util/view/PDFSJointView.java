package com.lgq.pdf_util.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@FXMLView(value = "/view/PDFSJoint.fxml")
@Component
public class PDFSJointView extends AbstractFxmlView {

    public PDFSJointView(ConfigurableApplicationContext context) {
        this.setApplicationContext(context);
    }
}
