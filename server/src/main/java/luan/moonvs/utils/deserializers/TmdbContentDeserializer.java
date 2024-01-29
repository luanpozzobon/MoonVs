package luan.moonvs.utils.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import luan.moonvs.models.tmdb_responses.TmdbContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TmdbContentDeserializer extends StdDeserializer<TmdbContent> {
    public TmdbContentDeserializer() {
        super(TmdbContent.class);
    }

    @Override
    public TmdbContent deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            int id = node.get("id").asInt();
            JsonNode genresNode = node.get("genres");
            List<String> genres = new ArrayList<>();
            if (genresNode != null &&  genresNode.isArray())
                genresNode.forEach(genre -> genres.add(genre.get("name").asText()));


            boolean adult = node.get("adult").asBoolean();
            String originalTitle = node.has("original_title") ?
                    node.get("original_title").asText() :
                    node.get("original_name").asText();

            String ptTitle = node.has("title") ?
                    node.get("title").asText() :
                    node.get("name").asText();

            String overview = node.get("overview").asText();
            String posterPath = node.get("poster_path").asText();
            double voteAvg = node.get("vote_average").asDouble();
            int voteCount = node.get("vote_count").asInt();

            return new TmdbContent(id, genres, adult, originalTitle, ptTitle, overview, posterPath, voteAvg, voteCount);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
