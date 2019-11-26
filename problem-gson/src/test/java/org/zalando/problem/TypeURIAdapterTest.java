package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeURIAdapterTest {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(URI.class, TypeURIAdapter.INSTANCE)
            .create();

    @Test
    void shouldSerializeDefaultTypeToNull() {
        final String json = gson.toJson(Problem.DEFAULT_TYPE);
        assertEquals("null", json);
    }

    @Test
    void shouldSerializeNull() {
        final String json = gson.toJson(null, URI.class);
        assertEquals("null", json);
    }

    @Test
    void shouldSerializeURI() {
        final String uriString = "http://example.org/article";
        final URI uri = URI.create(uriString);
        final String json = gson.toJson(uri);
        assertNotNull(json);
        with(json)
                .assertThat("$", is(uriString));
    }

    @Test
    void shouldDeserializeNullValueToDefaultType() {
        final URI uri = gson.fromJson("null", URI.class);
        assertEquals(Problem.DEFAULT_TYPE, uri);
    }

    @Test
    void shouldThrowErrorOnInvalidURI() {
        final JsonPrimitive invalidInput = new JsonPrimitive("http://example.org/{date}/article");
        assertThrows(JsonIOException.class, () -> gson.fromJson(invalidInput, URI.class));
    }

}