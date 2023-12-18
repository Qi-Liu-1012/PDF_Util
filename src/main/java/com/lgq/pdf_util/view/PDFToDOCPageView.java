package com.lgq.pdf_util.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@FXMLView(value = "/view/PDFToDOCPage.fxml")
@Component
public class PDFToDOCPageView extends AbstractFxmlView {

    public PDFToDOCPageView(ConfigurableApplicationContext context) {
        this.setApplicationContext(context);
    }
}
