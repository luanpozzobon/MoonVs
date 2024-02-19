package luan.moonvs.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieListRequest(@Value("id_list") Long idList,
                               @Value("id_user") UUID idUser,
                               @Value("list_name") String listName,
                               @Value("list_description") String listDescription) { }
