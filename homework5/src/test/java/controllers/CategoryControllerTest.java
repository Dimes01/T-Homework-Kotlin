package controllers;

import com.example.homework5.Homework5Application;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.example.homework5.controllers.CategoryController;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link CategoryController}
 */
@SpringBootTest(classes = Homework5Application.class)
@AutoConfigureMockMvc
@WireMockTest
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        stubFor(
            WireMock.get(urlPathMatching("/public-api/v1.4/place-categories/"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody("""
                    [
                        {"id": 1, "name": "Museums"},
                        {"id": 2, "name": "Parks"}
                    ]
                    """))
        );
    }

    @Test
    public void getAllCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getCategoryById() throws Exception {
        mockMvc.perform(get("/api/v1/places/categories/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void postCreateCategory() throws Exception {
        String category = """
                {
                    "id": 1,
                    "slug": "slug1",
                    "name": "name1"
                }""";

        mockMvc.perform(post("/api/v1/places/categories/{0}", "1")
                        .content(category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void putUpdateCategory() throws Exception {
        String category = """
                {
                    "id": 1,
                    "slug": "newSlug1",
                    "name": "name1"
                }""";

        mockMvc.perform(put("/api/v1/places/categories/{0}", "1")
                        .content(category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/places/categories/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
