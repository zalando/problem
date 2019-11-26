package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatusTypeAdapterTest {

    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(StatusType.class, new StatusTypeAdapter(Collections.emptyMap()))
            .create();

    @Test
    void shouldSerializeNull() {
        String json = gson.toJson(null, StatusType.class);
        assertEquals("null", json);
    }

    @Test
    void shouldDeserializeNull() {
        StatusType status = gson.fromJson("null", StatusType.class);
        assertNull(status);
    }

}