package com.emojicipher;

import com.emojicipher.app.Emoji;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    private static final Emoji emoji = new Emoji();

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    public static Emoji getEmoji() {
        return emoji;
    }

}
