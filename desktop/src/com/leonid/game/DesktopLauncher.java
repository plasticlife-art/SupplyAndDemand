package com.leonid.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DesktopLauncher {

    public static void main(String[] arg) {
        SpringApplication.run(DesktopLauncher.class);
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context, Game game) {
        return (args) -> {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("Supply And Demand");
            new Lwjgl3Application(game, config);
        };
    }
}
