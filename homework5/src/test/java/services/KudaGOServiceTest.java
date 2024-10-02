package services;

import com.example.homework5.Homework5Application;
import com.example.homework5.models.Category;
import com.example.homework5.models.Location;
import com.example.homework5.services.KudaGOService;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Homework5Application.class)
@Testcontainers
public class KudaGOServiceTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private KudaGOService kudaGOService;

    private static String baseUrl;


    @Test
    public void getCategories_responseIsNotNull_listOfCategory() {
        // Arrange
        var expectedCategories = new Category[] {
                new Category(1, "cat1", "category1"),
                new Category(2, "cat2", "category2")
        };
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Category[].class))
                .thenReturn(expectedCategories);

        // Act
        var categories = kudaGOService.getCategories();

        // Assert
        assertEquals(2, categories.size());
        assertEquals("category1", categories.getFirst().getName());
        assertEquals("category2", categories.get(1).getName());
    }

    @Test
    public void getCategories_responseIsNull_emptyList() {
        // Arrange
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Category[].class))
                .thenReturn(null);

        // Act
        var categories = kudaGOService.getCategories();

        // Assert
        assertTrue(categories.isEmpty());
    }

    @Test
    public void getLocations_responseIsNotNull_listOfLocations() {
        // Arrange
        var mockLocations = new Location[] {
            new Location("slug1", "name1", "timezone1", "language1", "currency1"),
            new Location("slug2", "name2", "timezone2", "language2", "currency2")
        };
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Location[].class))
                .thenReturn(mockLocations);

        // Act
        var locations = kudaGOService.getLocations();

        // Assert
        assertEquals(2, locations.size());
        assertEquals("slug1", locations.getFirst().getSlug());
        assertEquals("name1", locations.getFirst().getName());
        assertEquals("timezone1", locations.getFirst().getTimezone());
    }

    @Test
    public void getLocations_responseIsNull_emptyList() {
        // Arrange
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Location[].class))
                .thenReturn(null);

        // Act
        var locations = kudaGOService.getLocations();

        // Assert
        assertTrue(locations.isEmpty());
    }

    @Test
    public void getCategories_throw_RestClientException() {
        // Arrange
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Category[].class))
                .thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, () -> kudaGOService.getCategories());
    }

    @Test
    public void getLocations_throw_RestClientException() {
        // Arrange
        Mockito.when(restClient.get()
                .uri(Mockito.anyString())
                .retrieve()
                .body(Location[].class))
                .thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, () -> kudaGOService.getLocations());
    }
}
