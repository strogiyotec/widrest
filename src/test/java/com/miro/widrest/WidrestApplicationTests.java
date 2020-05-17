package com.miro.widrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.impl.ImmutableWidget;
import com.miro.widrest.domain.impl.MockedDbWidget;
import com.miro.widrest.domain.impl.MockedWidget;
import com.miro.widrest.validation.WidgetValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WidrestApplicationTests {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Map<WidgetValidation.Type, WidgetValidation> validationMap;

    @Test
    @DisplayName("Test that Widget  without z index was created")
    void testCreateWithoutZIndex() throws Exception {
        final ImmutableWidget widget = new ImmutableWidget(1, 1, null, 1, 1);
        //POST
        final MvcResult result = this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.x").value(widget.getX()))
                .andExpect(jsonPath("$.y").value(widget.getY()))
                .andExpect(jsonPath("$.width").value(widget.getWidth()))
                .andExpect(jsonPath("$.height").value(widget.getHeight()))
                .andExpect(jsonPath("$.z").exists())
                .andReturn();
        final DbWidget saved = this.mapper.readValue(result.getResponse().getContentAsByteArray(), MockedDbWidget.class);
        this.mockMvc.perform(get("/widgets/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    @DisplayName("Test that widget with z index was created")
    public void testCreateWithZIndex() throws Exception {
        final int zIndex = 10;
        final ImmutableWidget widget = new MockedWidget(zIndex);
        //POST
        final MvcResult result = this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        final DbWidget saved = this.mapper.readValue(result.getResponse().getContentAsByteArray(), MockedDbWidget.class);
        this.mockMvc.perform(get("/widgets/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.z").value(zIndex));
    }

    @Test
    @DisplayName("Test that gets bad request response and appropriate description")
    public void testInvalidCreateRequest() throws Exception {
        final ImmutableWidget widget = new ImmutableWidget(null, null, 10, 1, 1);
        //POST
        this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value(this.validationMap.get(WidgetValidation.Type.INSERT).failReason()));
    }

    @Test
    @DisplayName("Test that after update widget has specified parameters")
    public void testUpdate() throws Exception {
        final ImmutableWidget widget = new MockedWidget();
        //POST
        final MvcResult post = this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isOk())
                .andReturn();
        final DbWidget saved = this.mapper.readValue(post.getResponse().getContentAsByteArray(), MockedDbWidget.class);
        //Update
        final MvcResult put = this.mockMvc.perform(
                put("/widgets/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new ImmutableWidget(
                                5, 5, 5, 5, 5
                        )))
        ).andExpect(status().isOk()).andReturn();
        final DbWidget updated = this.mapper.readValue(put.getResponse().getContentAsByteArray(), MockedDbWidget.class);
        //GET
        this.mockMvc.perform(get("/widgets/" + updated.getId()))
                .andExpect(status().isOk())
                //id is immutable
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.z").value(5))
                .andExpect(jsonPath("$.y").value(5))
                .andExpect(jsonPath("$.x").value(5))
                .andExpect(jsonPath("$.width").value(5))
                .andExpect(jsonPath("$.height").value(5));
    }


    @Test
    @DisplayName("Test that gets not found if use non existing widget id")
    public void testUpdateNonExisting() throws Exception {
        final ImmutableWidget widget = new MockedWidget();
        //PUT
        this.mockMvc.perform(
                put("/widgets/" + Integer.MIN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that widget was deleted")
    public void testDelete() throws Exception {
        final ImmutableWidget widget = new MockedWidget();
        //POST
        final MvcResult post = this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(widget))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        final DbWidget saved = this.mapper.readValue(post.getResponse().getContentAsByteArray(), MockedDbWidget.class);
        //Update
        this.mockMvc.perform(
                delete("/widgets/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
        //GET
        this.mockMvc.perform(get("/widgets/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that gets not found when can't delete widget with this id")
    public void testDeleteNonExisting() throws Exception {
        //PUT
        this.mockMvc.perform(
                delete("/widgets/" + Integer.MIN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {
        //POST
        this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new MockedWidget()))
        )
                .andExpect(status().isOk());
        this.mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new MockedWidget()))
        )
                .andExpect(status().isOk());
        final DbWidget[] mockedDbWidgets = this.mapper.readValue(this.mockMvc.perform(get("/widgets/"))
                .andReturn().getResponse().getContentAsByteArray(), MockedDbWidget[].class);
        Assertions.assertTrue(
                mockedDbWidgets[0].getZ() < mockedDbWidgets[1].getZ()
        );
    }
}
