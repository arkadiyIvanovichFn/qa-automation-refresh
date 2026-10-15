package com.refresh.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Класс описывает Страницу Логина.
 * Он хранит локаторы элементов и методы взаимодействия с ними.
 */
public class LoginPage {

    // 1. ЛОКАТОРЫ (SelenideElement)
    // Мы не ищем элемент сразу! Selenide найдёт его только в момент вызова метода (например, setValue).
    // Это называется "ленивая инициализация" (lazy evaluation).

    // Используем data-test атрибуты, так как они самые стабильные
    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton = $("[data-test='login-button']");
    private final SelenideElement errorMessage = $("[data-test='error']");

    // 2. МЕТОДЫ ДЕЙСТВИЙ

    /**
     * Открывает страницу логина.
     * @return тот же объект LoginPage (для цепочки вызовов, если нужно)
     */
    public LoginPage openPage() {
        open("https://www.saucedemo.com/");
        return this;
    }

    /**
     * Выполняет полный процесс логина.
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return объект следующей страницы (InventoryPage), так как после успешного логина мы попадаем туда
     */
    public InventoryPage login(String username, String password) {
        // setValue() очищает поле и вводит новый текст
        usernameInput.setValue(username);
        passwordInput.setValue(password);

        // click() имитирует клик пользователя
        loginButton.click();

        // Возвращаем объект следующей страницы, чтобы тест мог продолжить действия
        return new InventoryPage();
    }

    /**
     * Получает текст сообщения об ошибке (для проверок в тесте).
     * @return текст ошибки
     */
    public String getErrorMessageText() {
        // shouldBe(visible) гарантирует, что мы дождёмся появления ошибки перед чтением текста
        return errorMessage.shouldBe(com.codeborne.selenide.Condition.visible).getText();
    }
}