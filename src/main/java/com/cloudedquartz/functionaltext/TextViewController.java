package com.cloudedquartz.functionaltext;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyledDocument;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TextViewController {

    @FXML
    private ChoiceBox<String> fontFamilyChoiceBox;

    @FXML
    private ChoiceBox<Integer> fontSizeChoiceBox;

    @FXML
    private ToggleButton boldToggleButton;

    @FXML
    private ToggleButton italicToggleButton;

    @FXML
    private ToggleButton highlightButton;

    @FXML
    private StyleClassedTextArea textArea;

    @FXML
    public void initialize() {
        fontFamilyChoiceBox.setItems(FXCollections.observableArrayList(javafx.scene.text.Font.getFamilies()));
        fontSizeChoiceBox.setItems(FXCollections.observableArrayList(12, 14, 16, 18, 20, 22, 24));

        fontFamilyChoiceBox.setValue("Arial");
        fontSizeChoiceBox.setValue(12);

        fontFamilyChoiceBox.setOnAction(event -> applyFontStyle());
        fontSizeChoiceBox.setOnAction(event -> applyFontStyle());

        boldToggleButton.setOnAction(event -> toggleStyle(boldToggleButton, "bold"));
        italicToggleButton.setOnAction(event -> toggleStyle(italicToggleButton, "italic"));
        highlightButton.setOnAction(event -> toggleStyle(highlightButton, "highlight"));
    }

    private void toggleStyle(ToggleButton toggleButton, String style) {
        int start = textArea.getSelection().getStart();
        int end = textArea.getSelection().getEnd();

        var styleSpans = textArea.getStyleSpans(start, end);

        var newStyleSpans = styleSpans.mapStyles(styles -> {
            Set<String> newStyles = new HashSet<>(styles);

            if (toggleButton.isSelected()) {
                newStyles.add(style);
            } else {
                newStyles.remove(style);
            }
            return newStyles;
        });
        textArea.setStyleSpans(start, newStyleSpans);
    }

    @FXML
    private void applyFontStyle() {
        String fontFamily = fontFamilyChoiceBox.getValue();
        int fontSize = fontSizeChoiceBox.getValue();
        String fontStyle = String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", fontFamily, fontSize);

        String currentInlineStyle = textArea.getStyle();
        textArea.setStyle(currentInlineStyle + fontStyle);
    }

    @FXML
    private File savedFile;

    @FXML
    void handleSaveAction() {
        if (savedFile == null) {
            FileChooser fileChooser = new FileChooser();
            savedFile = fileChooser.showSaveDialog(null);
        }
        if (savedFile != null) {
            FileHandler.saveToFile(savedFile, textArea.getDocument());
        }
    }

    @FXML
    void handleLoadAction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            StyledDocument<Collection<String>, String, Collection<String>> document = FileHandler.loadFromFile(file);
            if (document != null) {
                textArea.replace(document);
            }
        }
        savedFile = null;
    }

    @FXML
    void handleCloseAction() {
        savedFile = null;
        textArea.clear();
    }

    @FXML
    void handleFindReplaceAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cloudedquartz/functionaltext/find-replace-view.fxml"));
            Parent root = loader.load();
            FindReplaceController controller = loader.getController();
            controller.setTextArea(textArea);

            Stage stage = new Stage();
            stage.setTitle("Find and Replace");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void highlightAllOccurrences(String searchText) {
        String content = textArea.getText();
        int index = content.indexOf(searchText);
        while (index >= 0) {
            textArea.setStyle(index, index + searchText.length(), Collections.singleton("highlight"));
            index = content.indexOf(searchText, index + searchText.length());
        }
    }
}