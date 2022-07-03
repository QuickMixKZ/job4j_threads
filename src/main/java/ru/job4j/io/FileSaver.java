package ru.job4j.io;

import java.io.*;

public final class FileSaver {

    private final File file;

    public FileSaver(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            for (int i = 0; i < content.length(); i += 1) {
                writer.write(content.charAt(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
