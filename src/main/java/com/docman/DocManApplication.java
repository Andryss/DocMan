package com.docman;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocManApplication extends Application {
    @Override
    public void start(Stage stage) {
        ScreenUtil.openMain();
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
