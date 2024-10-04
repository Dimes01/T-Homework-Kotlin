package services;

import com.example.homework5.Homework5Application;
import com.example.homework5.models.Category;
import com.example.homework5.models.Location;
import com.example.homework5.services.KudaGOService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClientException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.web.client.RestClient.*;

@SpringBootTest(classes = Homework5Application.class)
public class KudaGOServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private KudaGOService kudaGOService;

    private static final Category[] expectedCategories = new Category[] {
            new Category(1, "cat1", "category1"),
            new Category(2, "cat2", "category2")
    };

    private static final Location[] expectedLocations = new Location[] {
            new Location("slug1", "name1", "timezone1", "language1", "currency1"),
            new Location("slug2", "name2", "timezone2", "language2", "currency2")
    };

    private static Stream<Arguments> categories_allSituations() {
        return Stream.of(
            Arguments.of((Object) expectedCategories),
            Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("categories_allSituations")
    public void getCategories(Category[] inputArray) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Category[].class)).thenReturn(inputArray);

        // Act
        var categories = kudaGOService.getCategories();
        var expectedSize = inputArray != null ? inputArray.length : 0;

        // Assert
        assertEquals(expectedSize, categories.size());
        for (int i = 0; i < expectedSize; ++i) {
            assertEquals(inputArray[i], categories.get(i));
        }
    }


    private static Stream<Arguments> locations_allSituations() {
        return Stream.of(
                Arguments.of((Object) expectedLocations),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("locations_allSituations")
    public void getLocations(Location[] inputArray) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Location[].class)).thenReturn(inputArray);

        // Act
        var categories = kudaGOService.getLocations();
        var expectedSize = inputArray != null ? inputArray.length : 0;

        // Assert
        assertEquals(expectedSize, categories.size());
        for (int i = 0; i < expectedSize; ++i) {
            assertEquals(inputArray[i], categories.get(i));
        }
    }


    @Test
    public void getCategories_throw_RestClientException() {
        // Arrange
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Category[].class)).thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, () -> kudaGOService.getCategories());
    }

    @Test
    public void getLocations_throw_RestClientException() {
        // Arrange
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Location[].class)).thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, () -> kudaGOService.getLocations());
    }
}
