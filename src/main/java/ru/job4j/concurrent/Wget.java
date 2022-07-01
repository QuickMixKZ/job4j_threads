package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String fileName;

    public Wget(String url, int speed, String fileName) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        long timeStart = new Date().getTime();
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            long timeEnd = new Date().getTime();
            long spentMillis = timeEnd - timeStart;
            long fileSize = fileOutputStream.getChannel().size();
            int millisPerSecond = 1000;
            long requiredMillis = fileSize / speed * millisPerSecond;
            if (requiredMillis - spentMillis > 0) {
                Thread.sleep(requiredMillis - spentMillis);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Должно быть 2 параметра - \"url\" и \"speed\"");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Pattern pattern = Pattern.compile("([-a-zA-Z0-9]+\\.[0-9a-z]{2,5})$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Неверный формат url");
        }
        String fileName = matcher.group(0);
        Thread wget = new Thread(new Wget(url, speed, fileName));
        wget.start();
        wget.join();
    }
}