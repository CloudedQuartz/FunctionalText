package com.cloudedquartz.functionaltext;

import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.SegmentOps;
import org.fxmisc.richtext.model.TextOps;

import java.io.*;
import java.util.Collection;
import java.util.Collections;

public class FileHandler {

    private static final TextOps<String, Collection<String>> TEXT_OPS = SegmentOps.styledTextOps();

    public static void saveToFile(File file, StyledDocument<Collection<String>, String, Collection<String>> document) {
        String content = styledDocumentToString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StyledDocument<Collection<String>, String, Collection<String>> loadFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringToStyledDocument(content.toString());
    }

    private static StyledDocument<Collection<String>, String, Collection<String>> stringToStyledDocument(String content) {
        return ReadOnlyStyledDocument.fromString(content, Collections.emptyList(), Collections.emptyList(), TEXT_OPS);
    }

    private static String styledDocumentToString(StyledDocument<Collection<String>, String, Collection<String>> document) {
        StringBuilder sb = new StringBuilder();
        document.getParagraphs().forEach(paragraph -> sb.append(paragraph.getText()).append("\n"));
        return sb.toString();
    }
}