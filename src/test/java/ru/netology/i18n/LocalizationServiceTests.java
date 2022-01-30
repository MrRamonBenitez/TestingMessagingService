package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationServiceTests {

    @ParameterizedTest
    @MethodSource("countrySource")
    void tests_to_check_localization_service(String expectedMsg, Country country) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String resultMsg;

        resultMsg = localizationService.locale(country);

        assertEquals(resultMsg, expectedMsg);
    }

    private static Stream<Arguments> countrySource() {
        return Stream.of(Arguments.of("Добро пожаловать", Country.RUSSIA),
                Arguments.of("Welcome", Country.USA),
                Arguments.of("Welcome", Country.BRAZIL),
                Arguments.of("Welcome", Country.GERMANY));
    }

}
