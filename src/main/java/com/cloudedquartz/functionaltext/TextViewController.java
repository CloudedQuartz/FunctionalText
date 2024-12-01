package com.cloudedquartz.functionaltext;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fxmisc.richtext.StyleClassedTextArea;

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
}