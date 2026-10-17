package com.refresh.data;

// Faker — главный класс библиотеки Datafaker
// Содержит методы для генерации разных типов данных (имена, email, адреса и т.д.)
import net.datafaker.Faker;

// Locale — определяет язык и регион для генерации данных
// Например, Locale("ru") будет генерировать русские имена, Locale("en") — английские
import java.util.Locale;

/**
 * TestDataGenerator — утилитарный класс для создания тестовых данных.
 * Использует библиотеку Datafaker для генерации реалистичных случайных данных.
 */
public class TestDataGenerator {

    // Создаём объект Faker с русской локалью
    // new Faker(Locale.forLanguageTag("ru")) — будет генерировать данные на русском
    // Если нужны английские данные — используй new Faker() или new Faker(Locale.ENGLISH)
    private static final Faker faker = new Faker(Locale.forLanguageTag("ru"));

    /**
     * Генерирует случайное полное имя (например, "Иванов Иван Иванович")
     * @return строка с ФИО
     */
    public static String generateFullName() {
        // faker.name().fullName() — метод Datafaker для генерации полного имени
        return faker.name().fullName();
    }

    /**
     * Генерирует случайный email
     * @return строка с email (например, "anna.ivanova@example.com")
     */
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Генерирует случайный номер телефона в формате +7 (XXX) XXX-XX-XX
     * @return строка с телефоном
     */
    public static String generatePhone() {
        return faker.phoneNumber().phoneNumber();
    }

    /**
     * Генерирует случайный адрес
     * @return строка с адресом
     */
    public static String generateAddress() {
        return faker.address().fullAddress();
    }

    /**
     * Генерирует случайное число в заданном диапазоне
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (включительно)
     * @return случайное число
     */
    public static int generateRandomNumber(int min, int max) {
        // faker.number().numberBetween(min, max) — число от min (вкл.) до max (искл.)
        // Поэтому добавляем +1 к max, чтобы max тоже мог выпасть
        return faker.number().numberBetween(min, max + 1);
    }


    /**
     * Генерирует пароль с полным контролем параметров
     * @param minLength минимальная длина
     * @param maxLength максимальная длина
     * @param includeUppercase включать заглавные буквы
     * @param includeSpecial включать специальные символы
     * @return строка с паролем
     */
    public static String generatePassword(int minLength, int maxLength,
                                          boolean includeUppercase,
                                          boolean includeSpecial) {
        // В Datafaker 2.x есть перегруженный метод password() с такими параметрами
        return faker.internet().password(minLength, maxLength, includeUppercase, includeSpecial);
    }

    // Перегруженная версия без параметров (использует значения по умолчанию)
    public static String generatePassword() {
        return generatePassword(8, 12, true, true);
    }

    // День 5: добавлена генерация пароля

}