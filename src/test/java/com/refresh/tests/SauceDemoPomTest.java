package com.refresh.tests;

import com.refresh.pages.InventoryPage;
import com.refresh.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.screenshot;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SauceDemoPomTest {

    @Test
    @DisplayName("Успешный логин с валидными данными (через POM)")
    void successfulLoginPom() {
        // 1. Создаём объект страницы
        LoginPage loginPage = new LoginPage();

        // 2. Выполняем действие: открываем и логинимся.
        // Метод login() возвращает нам объект следующей страницы (InventoryPage)
        InventoryPage inventoryPage = loginPage.openPage().login("standard_user", "secret_sauce");

        // 3. Проверяем результат на новой странице
        inventoryPage.checkPageIsOpened();

        // 4. Делаем скриншот для отчёта
        screenshot("successful_login_pom");

        System.out.println("✅ POM Тест 'Успешный логин' пройден!");
    }

    @Test
    @DisplayName("Неуспешный логин с заблокированным пользователем (через POM)")
    void unsuccessfulLoginPom() {
        LoginPage loginPage = new LoginPage();

        // Логинимся заблокированным пользователем
        loginPage.openPage().login("locked_out_user", "secret_sauce");

        // Проверяем текст ошибки через метод страницы
        String actualError = loginPage.getErrorMessageText();
        assertEquals("Epic sadface: Sorry, this user has been locked out.", actualError, "Текст ошибки не совпадает");

        screenshot("locked_user_error_pom");
        System.out.println("✅ POM Тест 'Неуспешный логин' пройден!");
    }

    @Test
    @DisplayName("Добавление товара в корзину (через POM)")
    void addToCartPom() {
        LoginPage loginPage = new LoginPage();

        // Логинимся и сразу получаем объект InventoryPage
        InventoryPage inventoryPage = loginPage.openPage().login("standard_user", "secret_sauce");

        // Проверяем, что страница открылась, добавляем товар и проверяем счётчик
        // Благодаря тому, что методы возвращают this, мы можем строить цепочки (Fluent Interface)
        inventoryPage.checkPageIsOpened();   // Проверяем что страница открылась
        inventoryPage.addToFirstCart();      // Добавляем товар
        inventoryPage.checkButtonIsRemove(); // Проверяем, что кнопка сменила текст на "Remove"

        assertEquals("1", inventoryPage.getCartCount(), "Счётчик корзины должен быть равен 1");

        screenshot("add_to_cart_pom");
        System.out.println("✅ POM Тест 'Добавление в корзину' пройден!");
    }
}