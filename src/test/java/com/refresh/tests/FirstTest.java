package com.refresh.tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTest {

    @Test
    @DisplayName("Открываем SauceDemo и проверяем заголовок")
    void openSauceDemoAndCheckTitle() {
        // Открываем страницу
        open("https://www.saucedemo.com/");

        // Проверяем заголовок
        String expectedTitle = "Swag Labs";
        String actualTitle = title();

        assertEquals(expectedTitle, actualTitle, "Заголовок страницы не совпадает");

        // Делаем скриншот
        Selenide.screenshot("first_test_passed");

        System.out.println("✅ Тест пройден! Заголовок: " + actualTitle);
    }
}