package com.refresh.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.*;

/**
 * Класс описывает Страницу Товаров (Inventory), куда мы попадаем после логина.
 */
public class InventoryPage {

    // ЛОКАТОРЫ
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement cartBadge = $(".shopping_cart_badge");

    // $$ ищет СПИСОК элементов. Мы берём первый товар в списке.
    private final SelenideElement firstAddToCartButton = $$(".btn_inventory").first();
    private final SelenideElement firstRemoveButton = $$(".btn_inventory").first(); // Тот же элемент, но текст изменится

    /**
     * Проверяет, что мы действительно на странице товаров.
     * Используется в тесте как предусловие.
     */
    public void checkPageIsOpened() {
        // shouldHave проверяет условие и ждёт до 4 секунд (по умолчанию), пока элемент не появится
        pageTitle.shouldHave(text("Products"));
    }

    /**
     * Добавляет первый попавшийся товар в корзину.
     * @return этот же объект InventoryPage (чтобы можно было вызвать .getCartCount() следом)
     */
    public InventoryPage addToFirstCart() {
        firstAddToCartButton.click();
        return this;
    }

    /**
     * Возвращает текущее количество товаров в корзине (текст бейджа).
     * @return строка с числом, например "1" или "2"
     */
    public String getCartCount() {
        return cartBadge.shouldBe(visible).getText();
    }

    /**
     * Проверяет, что текст кнопки первого товара изменился на "Remove"
     */
    public void checkButtonIsRemove() {
        firstRemoveButton.shouldHave(text("Remove"));
    }
}