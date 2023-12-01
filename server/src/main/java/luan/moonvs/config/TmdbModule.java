package luan.moonvs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import luan.moonvs.deserializers.TmdbSearchDeserializer;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class TmdbModule extends SimpleModule {
    public TmdbModule() {
        super();
        addDeserializer(TmdbSearch.class, new TmdbSearchDeserializer());
    }

    public static ObjectMapper createConfiguredObjectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .build()
                .registerModule(new TmdbModule());
    }
}
