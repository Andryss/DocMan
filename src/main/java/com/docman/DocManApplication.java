package com.docman;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DocManApplication extends Application {
    @Override
    public void start(Stage stage) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                DocManApplication.class.getPackageName()
        );
        applicationContext.getBean(ScreenManager.class).openMain();
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
