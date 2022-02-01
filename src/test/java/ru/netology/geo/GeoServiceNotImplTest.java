package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GeoServiceNotImplTest {

    @Test
    void coordinateThrowTest() {
        GeoService geoServiceMock = Mockito.mock(GeoServiceImpl.class);

        Mockito.when(geoServiceMock.byCoordinates(Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new RuntimeException("Not implemented"));

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class,
                () -> geoServiceMock.byCoordinates(Mockito.anyDouble(), Mockito.anyDouble()));

        Assertions.assertEquals("Not implemented", thrown.getMessage());
    }
}