import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {


    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldSubmitFormWithDirectInput() {
        Selenide.open("http://localhost:9999");

        SelenideElement form = $$("form").find(Condition.visible);

        String deliveryDate = generateDate(7, "dd.MM.yyyy");

        form.$("[data-test-id='city'] input").setValue("Ярославль");

        SelenideElement dateField = form.$("[data-test-id='date'] input");
        dateField.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        dateField.setValue(deliveryDate);
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='phone'] input").setValue("+71234567890");
        form.$("[data-test-id='agreement'] .checkbox__box").click();

        $$("button").find(Condition.text("Забронировать")).click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__title")
                .shouldHave(Condition.text("Успешно!"));
        String expectedDateText = generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + expectedDateText));

    }

}