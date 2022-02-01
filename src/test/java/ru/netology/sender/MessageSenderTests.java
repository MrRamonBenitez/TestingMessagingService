package ru.netology.sender;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.startsWith;


public class MessageSenderTests {

    @ParameterizedTest
    @MethodSource("ipSource")
    void testToCheckMessageSender(String expectedTextMsg, String ip) {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Mockito.when(geoService.byIp(startsWith("172.")))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        Mockito.when(geoService.byIp(startsWith("96.")))
                .thenReturn(new Location("New York", Country.USA, null,  0));
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String resultTextMsg = messageSender.send(headers);

        assertEquals(expectedTextMsg, resultTextMsg);
    }

    private static Stream<Arguments> ipSource() {
        String rusMsg = "Добро пожаловать";
        String engMsg = "Welcome";
        return Stream.of(Arguments.of(rusMsg, "172.225.134.192"),
                Arguments.of(rusMsg, "172.225.227.207"),
                Arguments.of(rusMsg, "172.225.35.99"),
                Arguments.of(rusMsg, "172.225.37.31"),
                Arguments.of(rusMsg, "172.225.52.0"),
                Arguments.of(rusMsg, "172.225.46.128"),
                Arguments.of(rusMsg, "172.225.71.255"),
                Arguments.of(rusMsg, "172.225.69.128"),
                Arguments.of(rusMsg, "172.225.227.210"),
                Arguments.of(rusMsg, "172.225.234.265"),
                Arguments.of(engMsg, "96.44.186.149"),
                Arguments.of(engMsg, "96.44.189.194"),
                Arguments.of(engMsg, "96.44.183.0"),
                Arguments.of(engMsg, "96.44.155.11"),
                Arguments.of(engMsg, "96.44.177.234"),
                Arguments.of(engMsg, "96.44.144.56"),
                Arguments.of(engMsg, "96.44.133.114"),
                Arguments.of(engMsg, "96.44.111.1"),
                Arguments.of(engMsg, "96.44.133.3"),
                Arguments.of(engMsg, "96.44.135.45"));
    }

}

