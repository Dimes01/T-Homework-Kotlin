package controllers;

import com.example.homework5.Homework5Application;
import com.example.homework5.controllers.LocationController;
import com.example.homework5.models.Location;
import com.github.tomakehurst.wiremock.common.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link LocationController}
 */
@SpringBootTest(classes = Homework5Application.class)
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String locationString = Json.write(new Location("slug1", "name1", "timezone1", "language1", "currency1"));

    @Test
    public void getAllLocations_return200() throws Exception {
        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocationById__existedId_return200() throws Exception {
        mockMvc.perform(post("/api/v1/locations/{0}", "1")
                        .content(locationString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/locations/{0}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocationById_notExistedId_return400() throws Exception {
        mockMvc.perform(get("/api/v1/locations/{0}", "-1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postCreateLocation_return200() throws Exception {
        mockMvc.perform(post("/api/v1/locations/{0}", "1")
                        .content(locationString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void putUpdateLocation_existedId_return200() throws Exception {
        String newLocation = Json.write(new Location("newSlug1", "name1", "timezone1", "language1", "currency1"));

        mockMvc.perform(post("/api/v1/locations/{0}", "1")
                        .content(locationString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/locations/{0}", "1")
                        .content(newLocation)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void putUpdateLocation_notExistedId_return400() throws Exception {
        mockMvc.perform(put("/api/v1/locations/{0}", "-1")
                        .content(locationString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteLocation_existedId_return200() throws Exception {
        mockMvc.perform(post("/api/v1/locations/{0}", "1")
                        .content(locationString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/locations/{0}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteLocation_notExistedId_return400() throws Exception {
        mockMvc.perform(delete("/api/v1/locations/{0}", "-1"))
                .andExpect(status().is4xxClientError());
    }
}
