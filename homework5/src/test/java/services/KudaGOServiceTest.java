package services;

import com.example.homework5.Homework5Application;
import com.example.homework5.models.Category;
import com.example.homework5.services.KudaGOService;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Homework5Application.class)
@Testcontainers
public class KudaGOServiceTest {
    private RestClient restClient;
    private KudaGOService kudaGOService;

    @Container
    private static final WireMockContainer wireMockServer = new WireMockContainer(DockerImageName.parse("wiremock/wiremock:3.9.1"))
            .withExposedPorts(8080);;

    @BeforeAll
    public static void setUp() {
        wireMockServer.start();

        WireMock.configureFor(wireMockServer.getHost(), wireMockServer.getFirstMappedPort());

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/public-api/v1.4/place-categories/?lang=ru"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            [
                                {
                                    "id": 1,
                                    "slug": "cat1",
                                    "category": "category1"
                                },
                                {
                                    "id": 2,
                                    "slug": "cat2",
                                    "category": "category2"
                                }
                            ]"""
                        )
                )
        );

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/public-api/v1.4/locations/?lang=ru&fields=slug,name,timezone,coords,language,currency"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            [
                                {
                                    "slug": "cat1",
                                    "name": "name1",
                                    "timezone": "timezone1",
                                    "language": "language1",
                                    "currency": "currency1"
                                },
                                {
                                    "slug": "cat2",
                                    "name": "name2",
                                    "timezone": "timezone2",
                                    "language": "language2",
                                    "currency": "currency2"
                                }
                            ]"""
                        )
                )
        );
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setUpEach() {
        restClient = RestClient.builder()
                .baseUrl("http://" + wireMockServer.getHost() + ":" + wireMockServer.getFirstMappedPort() + "/public-api/v1.4")
                .build();
        kudaGOService = new KudaGOService(restClient);
    }

    @Test
    public void getCategories_responseIsNotNull_listOfCategory() {
        // Arrange
        var expectedCategories = new Category[] {
            new Category(1, "cat1", "category1"),
            new Category(2, "cat2", "category2")
        };

        // Act
        var categories = kudaGOService.getCategories();

        // Assert
        assertEquals(2, categories.size());
        assertEquals("category1", categories.getFirst().getName());
        assertEquals("category2", categories.get(1).getName());
    }

//    @Test
//    public void getCategories_responseIsNull_emptyList() {
//        // Arrange
//        Mockito.when(restClient.getForObject(
//                Mockito.anyString(),
//                Mockito.eq(Category[].class)
//        )).thenReturn(null);
//
//        // Act
//        var categories = kudaGOService.getCategories();
//
//        // Assert
//        assertTrue(categories.isEmpty());
//    }
//
//    @Test
//    public void getLocations_responseIsNotNull_listOfLocations() {
//        // Arrange
//        var mockLocations = new Location[] {
//            new Location("slug1", "name1", "timezone1", "language1", "currency1"),
//            new Location("slug2", "name2", "timezone2", "language2", "currency2")
//        };
//        Mockito.when(restClient.getForObject(
//                Mockito.anyString(),
//                Mockito.eq(Location[].class)
//        )).thenReturn(mockLocations);
//
//        // Act
//        var locations = kudaGOService.getLocations();
//
//        // Assert
//        assertEquals(2, locations.size());
//        assertEquals("slug1", locations.getFirst().getSlug());
//        assertEquals("name1", locations.getFirst().getName());
//        assertEquals("timezone1", locations.getFirst().getTimezone());
//    }
//
//    @Test
//    public void getLocations_responseIsNull_emptyList() {
//        // Arrange
//        Mockito.when(restClient.getForObject(
//                Mockito.anyString(),
//                Mockito.eq(Location[].class)
//        )).thenReturn(null);
//
//        // Act
//        var locations = kudaGOService.getLocations();
//
//        // Assert
//        assertTrue(locations.isEmpty());
//    }
//
//    @Test
//    public void getCategories_throw_RestClientException() {
//        // Arrange
//        Mockito.when(restClient.getForObject(
//                Mockito.anyString(),
//                Mockito.eq(Category[].class)
//        )).thenThrow(RestClientException.class);
//
//        // Act
//        // Assert
//        assertThrows(RestClientException.class, () -> kudaGOService.getCategories());
//    }
//
//    @Test
//    public void getLocations_throw_RestClientException() {
//        // Arrange
//        Mockito.when(restClient.getForObject(
//                Mockito.anyString(),
//                Mockito.eq(Location[].class)
//        )).thenThrow(RestClientException.class);
//
//        // Act
//        // Assert
//        assertThrows(RestClientException.class, () -> kudaGOService.getLocations());
//    }
}
