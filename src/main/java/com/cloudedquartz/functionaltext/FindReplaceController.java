package com.cloudedquartz.functionaltext;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.StyleSpan;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FindReplaceController {

    @FXML
    private TextField findField;

    @FXML
    private TextField replaceField;

    private StyleClassedTextArea textArea;
    private Map<Integer, StyleSpans<Collection<String>>> originalStyles = new HashMap<>();

    public void setTextArea(StyleClassedTextArea textArea) {
        this.textArea = textArea;
        findField.textProperty().addListener((observable, oldValue, newValue) -> clearHighlights());
    }

    @FXML
    void handleFindAction() {
        String searchText = findField.getText();
        highlightAllOccurrences(searchText);
    }

    @FXML
    void handleReplaceAction() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        replaceTextWithStyle(searchText, replaceText);
    }

    @FXML
    void handleCloseAction() {
        restoreOriginalStyles();
        Stage stage = (Stage) findField.getScene().getWindow();
        stage.close();
    }

    private void highlightAllOccurrences(String searchText) {
        String content = textArea.getText();
        int index = content.indexOf(searchText);
        while (index >= 0) {
            originalStyles.put(index, textArea.getStyleSpans(index, index + searchText.length()));
            textArea.setStyle(index, index + searchText.length(), Collections.singleton("highlight"));
            index = content.indexOf(searchText, index + searchText.length());
        }
    }

    private void replaceTextWithStyle(String searchText, String replaceText) {
        String content = textArea.getText();
        StyledDocument<Collection<String>, String, Collection<String>> document = textArea.getDocument();
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        int lastIndex = 0;
        int index = content.indexOf(searchText);
        while (index >= 0) {
            spansBuilder.addAll(document.getStyleSpans(lastIndex, index).subView(0, index - lastIndex));
            document.getStyleSpans(index, index + searchText.length()).forEach(span -> {
                int length = span.getLength();
                Collection<String> style = span.getStyle();
                if (replaceText.length() > searchText.length()) {
                    spansBuilder.add(new StyleSpan<>(style, length));
                } else {
                    int remainingLength = replaceText.length();
                    while (remainingLength > 0) {
                        int spanLength = Math.min(length, remainingLength);
                        spansBuilder.add(new StyleSpan<>(style, spanLength));
                        remainingLength -= spanLength;
                    }
                }
            });
            lastIndex = index + searchText.length();
            index = content.indexOf(searchText, lastIndex);
        }
        spansBuilder.addAll(document.getStyleSpans(lastIndex, content.length()).subView(0, content.length() - lastIndex));

        textArea.replaceText(0, content.length(), content.replace(searchText, replaceText));
        textArea.setStyleSpans(0, spansBuilder.create());
    }

    private void restoreOriginalStyles() {
        for (Map.Entry<Integer, StyleSpans<Collection<String>>> entry : originalStyles.entrySet()) {
            int index = entry.getKey();
            StyleSpans<Collection<String>> styles = entry.getValue();
            textArea.setStyleSpans(index, styles);
        }
        originalStyles.clear();
    }

    private void clearHighlights() {
        for (Map.Entry<Integer, StyleSpans<Collection<String>>> entry : originalStyles.entrySet()) {
            int index = entry.getKey();
            StyleSpans<Collection<String>> styles = entry.getValue();
            textArea.setStyleSpans(index, styles);
        }
        originalStyles.clear();
    }
}