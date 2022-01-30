import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.startsWith;
import static ru.netology.geo.GeoServiceImpl.*;

public class MessageSenderTests {

    @ParameterizedTest
    @MethodSource("argumentsSource")
    void tests_to_check_geo_service_byIp(Location expectedLocation, String ip) {
        GeoService geoService = new GeoServiceImpl();
        Location resultLocation;

        resultLocation = geoService.byIp(ip);

        assertEquals(expectedLocation, resultLocation);

    }

    private static Stream<Arguments> argumentsSource() {
        return Stream.of(Arguments.of(new Location(null, null, null, 0), LOCALHOST,
                Arguments.of(new Location("Moscow", Country.RUSSIA, "Lenina", 15), MOSCOW_IP),
                Arguments.of(new Location("New York", Country.USA, " 10th Avenue", 32), NEW_YORK_IP),
                Arguments.of(new Location("Moscow", Country.RUSSIA, null, 0), "172.0.43.12"),
                Arguments.of(new Location("New York", Country.USA, null, 0), "96.45.101.111")));
    }

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

    @Test
    void coordinateThrowTest() {
        GeoService geoService = new GeoServiceImpl();
        assertThrows(RuntimeException.class,
                () -> geoService.byCoordinates(Mockito.any(), Mockito.any()));
    }
}

