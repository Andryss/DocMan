package com.docman;

import javafx.application.Application;
import javafx.stage.Stage;

public class DocManApplication extends Application {
    @Override
    public void start(Stage stage) {
        ScreenUtil.openMain();
    }

    public static void main(String[] args) {
        launch();
    }
}
