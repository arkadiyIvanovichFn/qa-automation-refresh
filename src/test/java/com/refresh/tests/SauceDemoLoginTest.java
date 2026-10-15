package com.refresh.tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

public class SauceDemoLoginTest {

    @Test
    @DisplayName("Успешный логин с валидными данными")
    void successfulLogin() {

        open("https://www.saucedemo.com/");
        $("#user-name").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();

        Selenide.webdriver().shouldHave(com.codeborne.selenide.WebDriverConditions.urlContaining("/inventory.html"));

        String pageTitle = title();
        assertEquals("Swag Labs", pageTitle, "Заголовок должен быть 'Swag Labs'");

        $(".title").shouldHave(text("Products"));
        Selenide.screenshot("successful_login");

        System.out.println("✅ Тест 'Успешный логин' пройден!");
    }

    @Test
    @DisplayName("Неуспешный логин с заблокированным пользователем")
    void unsuccessfulLoginWithLockedUser() {
        // Шаг 1: Открываем страницу
        open("https://www.saucedemo.com/");

        $("#user-name").setValue("locked_out_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();
        $("h3[data-test='error']").shouldHave(text("Epic sadface: Sorry, this user has been locked out."));

        Selenide.webdriver().shouldHave(com.codeborne.selenide.WebDriverConditions.urlContaining("https://www.saucedemo.com/"));

        $("#login-button").shouldBe(visible);

        Selenide.screenshot("locked_user_error");

        System.out.println("✅ Тест 'Неуспешный логин' пройден!");
    }

    @Test
    @DisplayName("Добавление товара в корзину и проверка счётчика")
    void addToCartAndCheckCounter() {
        // Шаг 1: Открываем страницу и логинимся
        open("https://www.saucedemo.com/");

        $("#user-name").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();
        $(".title").shouldBe(visible);
        $$(".btn_inventory").first().click();
        $(".shopping_cart_badge").shouldHave(text("1"));
        $$(".btn_inventory").first().shouldHave(text("Remove"));
        $$(".btn_inventory").get(1).click();
        $(".shopping_cart_badge").shouldHave(text("2"));

        Selenide.screenshot("add_to_cart");

        System.out.println("✅ Тест 'Добавление в корзину' пройден!");
    }

    @Test
    @DisplayName("Удаление товара из корзины")
    void removeFromCartAndCheckNameButton() {
        open("http://www.saucedemo.com/");

        $("#user-name").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();

        $(".title").shouldBe(visible);
        $$(".btn_inventory").first().click();
        $(".shopping_cart_badge").shouldHave(text("1"));
        $$(".btn_inventory").first().click();
        $$(".btn_inventory").first().shouldHave(text("Add to cart"));

        Selenide.screenshot("Remove");

        System.out.println("✅ Тест 'Удаление товара из корзины' пройден!");
    }

}
