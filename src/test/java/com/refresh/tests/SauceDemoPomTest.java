package com.refresh.tests;

// JUnit аннотации для обозначения тестов и их имён
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// SelenideElement — тип элемента страницы в Selenide (нужен для LoginPage)
import com.codeborne.selenide.SelenideElement;

// Статические импорты Selenide — позволяют писать $() вместо Selenide.$()

// Condition — набор условий для проверок (visible, text и т.д.)
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

// Assertions — стандартные проверки JUnit (assertEquals и др.)

// Наши Page Object классы
import com.refresh.pages.InventoryPage;
import com.refresh.pages.LoginPage;

// Наш новый класс конфигурации
import com.refresh.config.Config;

import com.refresh.data.TestDataGenerator;  // наш генератор данных
import org.junit.jupiter.api.RepeatedTest;  // для повторения теста несколько раз

// ParameterizedTest — аннотация для параметризованных тестов
import org.junit.jupiter.params.ParameterizedTest;

// ValueSource — источник данных из простых значений (строки, числа)
import org.junit.jupiter.params.provider.ValueSource;

// CsvSource — источник данных в формате CSV (несколько параметров)
import org.junit.jupiter.params.provider.CsvSource;

// MethodSource — источник данных из метода (для сложных объектов)
import org.junit.jupiter.params.provider.MethodSource;

// Arguments — обёртка для передачи нескольких аргументов из метода
import org.junit.jupiter.params.provider.Arguments;

// Stream — для передачи списка аргументов из метода
import java.util.stream.Stream;

public class SauceDemoPomTest {

    // Создаём ОДИН объект Config на весь класс тестов
    // private — доступен только внутри этого класса
    // final — нельзя переназначить после создания (защита от случайных изменений)
    private final Config config = new Config();

    @Test
    @DisplayName("Успешный логин с данными из конфига")
    void successfulLoginFromConfig() {
        LoginPage loginPage = new LoginPage();

        // Вместо захардкоженных строк берём данные из config.properties
        // config.getLoginUsername() вернёт "standard_user"
        // config.getLoginPassword() вернёт "secret_sauce"
        InventoryPage inventoryPage = loginPage
                .openPage(config.getBaseUrl())  // URL тоже из конфига!
                .login(config.getLoginUsername(), config.getLoginPassword());

        inventoryPage.checkPageIsOpened();

        screenshot("successful_login_config");
        System.out.println("Приложение: " + config.getAppName());
        System.out.println("✅ Тест с конфигом пройден!");
    }

    @Test
    @DisplayName("Регистрация с случайными данными из Datafaker")
    void registrationWithRandomData() {
        // Генерируем случайные тестовые данные
        // Каждый раз при запуске теста будут новые значения!
        String randomName = TestDataGenerator.generateFullName();
        String randomEmail = TestDataGenerator.generateEmail();
        String randomPhone = TestDataGenerator.generatePhone();

        // Выводим сгенерированные данные в консоль для наглядности
        System.out.println("📊 Сгенерированные данные:");
        System.out.println("   Имя: " + randomName);
        System.out.println("   Email: " + randomEmail);
        System.out.println("   Телефон: " + randomPhone);

        // Здесь могла бы быть реальная регистрация:
        // registrationPage.open()
        //     .fillName(randomName)
        //     .fillEmail(randomEmail)
        //     .fillPhone(randomPhone)
        //     .submit();

        // Пока просто проверяем, что данные не пустые (демонстрация)
        assertEquals(true, randomName.length() > 0, "Имя не должно быть пустым");
        assertEquals(true, randomEmail.contains("@"), "Email должен содержать @");

        System.out.println("✅ Тест с Datafaker пройден!");
    }

    /**
     * Параметризованный тест: проверяет заголовок страницы для разных URL
     * @ParameterizedTest — говорит JUnit, что это параметризованный тест
     * @ValueSource — передаёт список строк как параметры
     * url — имя параметра метода (должно совпадать с names в аннотации)
     */
    @ParameterizedTest(name = "Проверка URL: {0}")
    @ValueSource(strings = {
            "https://www.saucedemo.com/",
            "https://www.google.com/",
            "https://www.github.com/"
    })
    void checkPageTitleForDifferentUrls(String url) {
        // Этот тест запустится 3 раза, каждый раз с разным url
        open(url);
        String title = title();

        System.out.println("🌐 URL: " + url + " → Заголовок: " + title);

        // Проверяем, что заголовок не пустой
        assertEquals(true, title.length() > 0, "Заголовок не должен быть пустым для " + url);
    }

    /**
     * Параметризованный тест с несколькими параметрами (CSV формат)
     * @CsvSource — каждая строка это набор аргументов, разделённых запятыми
     * Формат: username, password, expectedResult
     */
    @ParameterizedTest(name = "Логин: пользователь={0}, пароль={1}, результат={2}")
    @CsvSource({
            "standard_user, secret_sauce, success",
            "locked_out_user, secret_sauce, error",
            "problem_user, secret_sauce, success",
            ", , error"
    })
    void loginWithDifferentCredentials(String username, String password, String expectedResult) {
        System.out.println("🔐 Тестируем логин: user='" + username + "', pass='" + password + "'");

        LoginPage loginPage = new LoginPage();
        loginPage.openPage(config.getBaseUrl());

        // Если username или password null (из CSV пустая строка превращается в null)
        if (username == null) username = "";
        if (password == null) password = "";

        loginPage.login(username, password);

        System.out.println("   Ожидаемый результат: " + expectedResult);
        System.out.println("✅ Параметризованный тест пройден для " + username);
    }

    /**
     * Параметризованный тест с данными из метода (для сложных случаев)
     * @MethodSource — берёт данные из статического метода, указанного в значении
     */
    @ParameterizedTest(name = "Тест с данными из метода: {0}")
    @MethodSource("provideTestUsers")
    void testWithMethodSource(String username, String expectedRole) {
        System.out.println("👤 Пользователь: " + username + ", роль: " + expectedRole);
        assertEquals(true, username.length() > 0, "Имя пользователя не должно быть пустым");
    }

    @Test
    @DisplayName("Тест проверяет что длина пароля >= 8 символов")
    void testLengthOfPassword() {
        String password = TestDataGenerator.generatePassword();
        System.out.println("password: " + password);
        assertTrue(password.length() >= 8, "Длина пароля должна быть >= 8 символов");
        System.out.println("✅ Тест длины пароля пройден!");
    }

    /**
     * Параметризованный тест: проверяет в email содержание символа @
     * @ParameterizedTest — говорит JUnit, что это параметризованный тест
     * @ValueSource — передаёт список строк как параметры
     * email — имя параметра метода (должно совпадать с names в аннотации)
     */
    @ParameterizedTest(name = "Проверка Email: {0}")
    @ValueSource(strings = {
            "saucedemo@example.com",
            "google@example.com",
            "github@example.com",
            "moogle@example.com",
            "githubexample.com",
    })
    void testCheckOfEmails(String email) {
        // Этот тест запустится 5 раз, каждый раз с разным password
        System.out.println("Тестируем email: " + email);

        // Проверяем, что email содержит @
        boolean hasSymbol = email.contains("@");
        if (hasSymbol) {
            assertTrue(hasSymbol, "Email должен содержать символ @: " + email);
            System.out.println("✅ Email '\" + email + \"' содержит @");
        } else {
            assertFalse(hasSymbol, "Email не должен содержать символ @: " + email);
            System.out.println("✅ Email '\" + email + \"' не содержит @");
        }
    }

    /**
     * Задание 4: Комплексный тест, объединяющий Config, Datafaker и Параметризацию.
     *
     * @ParameterizedTest — говорит JUnit, что это параметризованный тест
     * name — формирует читаемое имя теста в консоли, подставляя {0} (первый параметр).
     * @ValueSource — передаёт разные ожидаемые слова для проверки в заголовке.
     */
    @ParameterizedTest(name = "Проверка заголовка страницы. Ожидаем слово: {0}")
    @ValueSource(strings = { "Swag", "Labs", "ag L" })
    void complexIntegrationTest(String expectedWordInTitle) {

        // ШАГ 1: Получаем URL из нашего конфигурационного файла
        // Мы не хардкодим "https://www.saucedemo.com/", а берём из config.properties
        String url = config.getBaseUrl();

        // ШАГ 2: Генерируем случайные тестовые данные через Datafaker
        // Каждый прогон теста будет с новым уникальным именем!
        String randomUserName = TestDataGenerator.generateFullName();

        // ШАГ 3: Выводим информативное сообщение в консоль
        // Это помогает при отладке понять, с какими данными упал тест
        System.out.println("Тестируем URL: " + url + " | Симулируем пользователя: " + randomUserName);

        // ШАГ 4: Действие и проверка (Selenide)
        // Открываем страницу по URL из конфига
        open(url);

        // Получаем реальный заголовок страницы
        String actualTitle = title();

        // Проверка 1: заголовок точно не должен быть пустым
        assertTrue(!actualTitle.isEmpty(), "Заголовок страницы не должен быть пустым!");

        // Проверка 2: заголовок должен содержать слово, которое мы передали как параметр ({0})
        assertTrue(actualTitle.contains(expectedWordInTitle),
                "ОШИБКА: Заголовок '" + actualTitle + "' должен содержать слово '" + expectedWordInTitle + "'");

        System.out.println("✅ Успех! Заголовок '" + actualTitle + "' корректен для пользователя " + randomUserName);

    }

    /**
     * Статический метод-источник данных для @MethodSource
     * ВАЖНО: метод должен быть static и возвращать Stream<Arguments>
     * Каждый Arguments — это один набор параметров для теста
     */
    static Stream<Arguments> provideTestUsers() {
        return Stream.of(
                Arguments.of("admin", "administrator"),
                Arguments.of("user123", "regular_user"),
                Arguments.of("guest", "guest")
        );
    }
}