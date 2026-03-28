import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {

    @Test
    public void shouldSubmitFormWithDirectInput() {
        Selenide.open("http://localhost:9999");

        SelenideElement form = $$("form").find(Condition.visible);

        String deliveryDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        form.$("[data-test-id='city'] input").setValue("Ярославль");
        form.$("[data-test-id='date'] input").setValue(deliveryDate);
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='phone'] input").setValue("+71234567890");
        form.$("[data-test-id='agreement'] .checkbox__box").click();

        $$("button").find(Condition.text("Забронировать")).click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__title")
                .shouldHave(Condition.text("Успешно!"));
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована"));

    }

}