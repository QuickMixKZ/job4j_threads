package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long bytesWrited = 0;
            long timeStart = new Date().getTime();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                bytesWrited += bytesRead;
                if (bytesWrited >= speed) {
                    long deltaTime = new Date().getTime() - timeStart;
                    if (deltaTime < 1000) {
                        Thread.sleep(1000 - deltaTime);
                    }
                    timeStart = new Date().getTime();
                    bytesWrited = 0;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        Map<String, String> validatedArgs = validateArgs(args);
        String url = validatedArgs.get("url");
        int speed = Integer.parseInt(validatedArgs.get("speed"));
        String fileName = validatedArgs.get("fileName");
        Thread wget = new Thread(new Wget(url, speed, fileName));
        wget.start();
        wget.join();
        LocalDateTime end = LocalDateTime.now();
        System.out.printf("Seconds spent: %d", end.getSecond() - start.getSecond());
    }

    private static Map<String, String> validateArgs(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Должно быть 2 параметра - \"url\" и \"speed\"");
        }
        String url = args[0];
        String speed = args[1];
        Pattern pattern = Pattern.compile("([-a-zA-Z0-9]+\\.[0-9a-z]{2,5})$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Неверный формат url");
        }
        String fileName = matcher.group(0);
        Map<String, String> result = new HashMap<>(3);
        result.put("url", url);
        result.put("speed", speed);
        result.put("fileName", fileName);
        return result;
    }
}