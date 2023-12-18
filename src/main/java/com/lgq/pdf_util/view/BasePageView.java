package com.lgq.pdf_util.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@FXMLView(value = "/view/BasePage.fxml")
@Component
public class BasePageView extends AbstractFxmlView {

    public BasePageView(ConfigurableApplicationContext context) {
        this.setApplicationContext(context);
    }
}
