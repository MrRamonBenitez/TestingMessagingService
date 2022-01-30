package ru.netology.geo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.geo.GeoServiceImpl.*;

public class GeoServiceTests {

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

}
