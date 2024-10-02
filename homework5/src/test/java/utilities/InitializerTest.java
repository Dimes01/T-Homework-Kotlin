package utilities

import com.example.homework5.models.Category;
import com.example.homework5.models.Location;
import com.example.homework5.services.KudaGOService;
import com.example.homework5.utilities.Storage;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

class InitializerTest {
    private static final String responseBodyCategories = """
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
                            ]""";
    private static final String responseBodyLocations = """
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
                            ]""";
    private static RestClient restClient;
    private static KudaGOService kudaGOService;

    @Autowired
    private Storage<Category> categoryStorage;

    @Autowired
    private Storage<Location> locationStorage;

    @Container
    private static final WireMockContainer wireMockServer = new WireMockContainer(DockerImageName.parse("wiremock/wiremock:3.9.1"))
            .withExposedPorts(8080);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.baseUrl", () -> "http://" + wireMockServer.getHost() + ":" + wireMockServer.getFirstMappedPort() + "/public-api/v1.4");
    }

    @BeforeAll
    public static void setUp() {
        wireMockServer.start();
        restClient = RestClient
                .builder()
                .baseUrl("http://" + wireMockServer.getHost() + ":" + wireMockServer.getFirstMappedPort() + "/public-api/v1.4")
                .build();
        kudaGOService = new KudaGOService(restClient);

        WireMock.configureFor(wireMockServer.getHost(), wireMockServer.getFirstMappedPort());

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/public-api/v1.4/place-categories/?lang=ru"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBodyCategories)
                )
        );

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/public-api/v1.4/locations/?lang=ru&fields=slug,name,timezone,coords,language,currency"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBodyLocations)
                )
        );
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void run() {
        var categories = kudaGOService.getCategories();
        var locations = kudaGOService.getLocations();

    }
}