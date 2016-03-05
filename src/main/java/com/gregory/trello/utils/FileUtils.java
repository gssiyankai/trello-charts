package com.gregory.trello.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readResourceLines(String path) {
        try {
            Path templatePath = Paths.get(FileUtils.class.getResource("/" + path).toURI());
            List<String> lines = Files.readAllLines(templatePath, Charset.defaultCharset());
            String content = "";
            for (String line : lines) {
                content += line + "\n";
            }
            return content;
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String path, String content) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
