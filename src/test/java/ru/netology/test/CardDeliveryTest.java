package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        Configuration.headless = true;
        Configuration.holdBrowserOpen = false;
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldMakeAnAppointmentAndChangeTheDate() {

        String originalDate = deliveryDate();
        $("[data-test-id='city'] input").setValue(city());
        $("[data-test-id='date'] input").sendKeys((Keys.chord(Keys.CONTROL,"a",Keys.DELETE)));
        $("[data-test-id='date'] input").setValue(originalDate);
        $("[data-test-id='name'] input").setValue(name());
        $("[data-test-id='phone'] input").setValue(phone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $( "[data-test-id=success-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + originalDate)
                        , Duration.ofSeconds(15));

        String editedDate = deliveryDate();
        $("[data-test-id='date'] input").sendKeys((Keys.chord(Keys.CONTROL,"a",Keys.DELETE)));
        $("[data-test-id='date'] input").setValue(editedDate);
        $(byText("Запланировать")).click();
        $( "[data-test-id=replan-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")
                        , Duration.ofSeconds(15));

        $(byText("Перепланировать")).click();
        $( "[data-test-id=success-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + editedDate)
                        , Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotMakeAnAppointmentSameDate() {

        String originalDate = deliveryDate();
        $("[data-test-id='city'] input").setValue(city());
        $("[data-test-id='date'] input").sendKeys((Keys.chord(Keys.CONTROL,"a",Keys.DELETE)));
        $("[data-test-id='date'] input").setValue(originalDate);
        $("[data-test-id='name'] input").setValue(name());
        $("[data-test-id='phone'] input").setValue(phone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $( "[data-test-id=success-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + originalDate)
                        , Duration.ofSeconds(15));

        $("[data-test-id='date'] input").sendKeys((Keys.chord(Keys.CONTROL,"a",Keys.DELETE)));
        $("[data-test-id='date'] input").setValue(originalDate);
        $(byText("Запланировать")).click();
        $( "[data-test-id=replan-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на эту дату")
                        , Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotMakeAnAppointmentDateUnmodified() {

        String originalDate = deliveryDate();
        $("[data-test-id='city'] input").setValue(city());
        $("[data-test-id='date'] input").sendKeys((Keys.chord(Keys.CONTROL,"a",Keys.DELETE)));
        $("[data-test-id='date'] input").setValue(originalDate);
        $("[data-test-id='name'] input").setValue(name());
        $("[data-test-id='phone'] input").setValue(phone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $( "[data-test-id=success-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + originalDate)
                        , Duration.ofSeconds(15));

        $(byText("Запланировать")).click();
        $( "[data-test-id=replan-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на эту дату")
                        , Duration.ofSeconds(15));
    }
}