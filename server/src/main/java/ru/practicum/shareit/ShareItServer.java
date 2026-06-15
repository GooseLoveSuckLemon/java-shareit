package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения ShareIt Server.
 * Запускает серверную часть приложения для управления шерингом вещей.
 *
 * @author ShareIt Team
 * @version 1.0
 */
@SpringBootApplication
public class ShareItServer {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ShareItServer.class, args);
    }
}