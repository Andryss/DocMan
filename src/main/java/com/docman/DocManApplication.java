package com.docman;

import java.awt.Taskbar;
import java.awt.Toolkit;
import java.net.URL;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static java.util.Objects.requireNonNull;

public class DocManApplication extends Application {
    @Override
    public void start(Stage stage) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                DocManApplication.class.getPackageName()
        );
        applicationContext.getBean(ScreenManager.class).openMain();

        if (Taskbar.isTaskbarSupported()) {
            URL iconResource = requireNonNull(DocManApplication.class.getResource("icon.png"), "icon");
            Taskbar.getTaskbar().setIconImage(Toolkit.getDefaultToolkit().getImage(iconResource));
        }
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            Logger log = LoggerFactory.getLogger("DocManMain");
            log.error("Catch unhandled exception", e);
        }
    }
}
