package com.docman;

import javafx.application.Application;
import javafx.stage.Stage;

import static com.docman.ScreenUtil.DocManScreen.MAIN;

public class DocManApplication extends Application {
    @Override
    public void start(Stage stage) {
        ScreenUtil.open(MAIN);
    }

    public static void main(String[] args) {
        launch();
    }
}
