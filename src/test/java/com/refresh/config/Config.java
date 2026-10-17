package com.refresh.config;

// java.io.InputStream — интерфейс для чтения потоков данных (файлов, сети и т.д.)
// Нужен, чтобы "открыть" файл config.properties для чтения
import java.io.InputStream;

// java.util.Properties — встроенный Java-класс для работы с .properties файлами
// Хранит данные в формате "ключ → значение" и умеет читать их из файла
import java.util.Properties;

/**
 * Класс Config — единая точка доступа ко всем настройкам фреймворка.
 * Загружает config.properties один раз при создании и предоставляет
 * методы для получения значений по ключам.
 */
public class Config {

    // Объект Properties, в котором будут храниться все настройки из файла
    private final Properties properties;

    /**
     * Конструктор — вызывается при создании объекта new Config()
     * Читает файл config.properties из папки resources и загружает его в память
     */
    public Config() {
        // Создаём пустой объект Properties
        properties = new Properties();

        // Получаем поток (InputStream) для чтения файла config.properties
        // getClassLoader().getResourceAsStream() ищет файл в classpath (папка resources)
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

        // Проверяем, что файл действительно найден
        if (inputStream == null) {
            throw new RuntimeException("❌ Файл config.properties не найден в resources!");
        }

        try {
            // Загружаем содержимое файла в объект properties
            // Теперь все ключи и значения из файла доступны через properties.getProperty("ключ")
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при чтении config.properties: " + e.getMessage());
        }
    }

    /**
     * Универсальный метод для получения строкового значения по ключу
     * @param key имя ключа из config.properties (например, "base.url")
     * @return значение ключа (например, "https://www.saucedemo.com/")
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Метод для получения целого числа (например, таймаута)
     * @param key имя ключа
     * @return значение как int
     */
    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * Метод для получения boolean значения (например, headless=true/false)
     * @param key имя ключа
     * @return true или false
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    // ============================================
    // Удобные методы-геттеры для конкретных настроек
    // ============================================

    /** Возвращает базовый URL приложения */
    public String getBaseUrl() {
        return get("base.url");
    }

    /** Возвращает имя браузера */
    public String getBrowser() {
        return get("browser");
    }

    /** Возвращает таймаут в миллисекундах */
    public int getTimeout() {
        return getInt("timeout");
    }

    /** Возвращает username для логина */
    public String getLoginUsername() {
        return get("login.username");
    }

    /** Возвращает password для логина */
    public String getLoginPassword() {
        return get("login.password");
    }

    public String getAppName() {
        return get("app.name");
    }

}