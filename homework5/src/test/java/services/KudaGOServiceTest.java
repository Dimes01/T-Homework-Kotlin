package services;

import com.example.homework5.models.Category;
import com.example.homework5.models.Location;
import com.example.homework5.services.KudaGOService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KudaGOServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KudaGOService kudaGOService;

    @Test
    public void getCategories_responseIsNotNull_listOfCategory() {
        // Arrange
        var mockCategories = new Category[] {
            new Category(1, "cat1", "category1"),
            new Category(2, "cat2", "category2")
        };
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Category[].class)
        )).thenReturn(mockCategories);

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
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Category[].class)
        )).thenReturn(null);

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
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Location[].class)
        )).thenReturn(mockLocations);

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
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Location[].class)
        )).thenReturn(null);

        // Act
        var locations = kudaGOService.getLocations();

        // Assert
        assertTrue(locations.isEmpty());
    }

    @Test
    public void getCategories_throw_RestClientException() {
        // Arrange
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Category[].class)
        )).thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, (Executable) kudaGOService.getCategories());
    }

    @Test
    public void getLocations_throw_RestClientException() {
        // Arrange
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(Location[].class)
        )).thenThrow(RestClientException.class);

        // Act
        // Assert
        assertThrows(RestClientException.class, (Executable) kudaGOService.getLocations());
    }
}
