package com.cloudedquartz.functionaltext;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class HomeViewController {

    @FXML
    private AnchorPane textViewContainer;

    private TextViewController textViewController;

    @FXML
    public void initialize() {
        try {
            FXMLLoader textLoader = new FXMLLoader(getClass().getResource("/com/cloudedquartz/functionaltext/text-view.fxml"));
            BorderPane textViewArea = textLoader.load();
            textViewController = textLoader.getController();
            setTextViewContainer(textViewController, textViewArea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTextViewContainer(TextViewController textViewController, BorderPane textViewArea) {
        this.textViewController = textViewController;
        AnchorPane.setTopAnchor(textViewArea, 10.0);
        AnchorPane.setBottomAnchor(textViewArea, 10.0);
        AnchorPane.setLeftAnchor(textViewArea, 10.0);
        AnchorPane.setRightAnchor(textViewArea, 10.0);
        textViewContainer.getChildren().add(textViewArea);
    }

    @FXML
    private void handleOpenFileAction() {
        if (textViewController != null) {
            textViewController.handleLoadAction();
        }
    }

    @FXML
    private void handleSaveFileAction() {
        if (textViewController != null) {
            textViewController.handleSaveAction();
        }
    }
    @FXML
    private void handleCloseFileAction() {
        if (textViewController != null) {
            textViewController.handleCloseAction();
        }
    }
    @FXML
    private void handleFindReplaceAction() {
        if (textViewController != null) {
            textViewController.handleFindReplaceAction();
        }
    }

}