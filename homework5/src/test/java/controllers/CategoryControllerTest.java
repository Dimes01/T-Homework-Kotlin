package controllers;

import com.example.homework5.Homework5Application;
import com.example.homework5.controllers.CategoryController;
import com.example.homework5.models.Category;
import com.github.tomakehurst.wiremock.common.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link CategoryController}
 */
@SpringBootTest(classes = Homework5Application.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String categoryString = Json.write(new Category(1, "slug1", "name1"));

    @Test
    public void getAllCategories_return200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/places/categories"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoryById_existedId_return200() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories/{0}", "1")
                        .content(categoryString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/places/categories/{0}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoryById_notExistedId_return400() throws Exception {
        mockMvc.perform(get("/api/v1/places/categories/{0}", "-1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postCreateCategory_return200() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories/{0}", "1")
                        .content(categoryString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void putUpdateCategory_existedId_return200() throws Exception {
        String newCategory = Json.write(new Category(1, "newSlug1", "name1"));

        mockMvc.perform(post("/api/v1/places/categories/{0}", "1")
                        .content(newCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/places/categories/{0}", "1")
                        .content(newCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void putUpdateCategory_notExistedId_return400() throws Exception {
        mockMvc.perform(put("/api/v1/places/categories/{0}", "-1")
                        .content(categoryString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteCategory_existedId_return200() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories/{0}", "1")
                        .content(categoryString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/places/categories/{0}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCategory_notExistedId_return400() throws Exception {
        mockMvc.perform(delete("/api/v1/places/categories/{0}", "1"))
                .andExpect(status().is4xxClientError());
    }
}
