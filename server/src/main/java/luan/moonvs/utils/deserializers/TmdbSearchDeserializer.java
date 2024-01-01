package luan.moonvs.utils.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbSearch;

import java.io.IOException;

public class TmdbSearchDeserializer extends StdDeserializer<TmdbSearch> {
    public TmdbSearchDeserializer() {
        super(TmdbSearch.class);
    }

    @Override
    public TmdbSearch deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        String type = node.get("media_type").asText().toUpperCase();
        try {
            ContentType contentType = ContentType.valueOf(type);

            int id = node.get("id").asInt();
            String originalTitle = node.has("original_title") ? node.get("original_title").asText() : node.get("original_name").asText();
            String overview = node.get("overview").asText();
            String posterPath = node.get("poster_path").asText();
            double voteAvg = node.get("vote_average").asDouble();

            return new TmdbSearch(id, originalTitle, overview, contentType, posterPath, voteAvg);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
