package luan.moonvs.models.responses;

import org.springframework.http.HttpStatus;

public record Response<T>(HttpStatus status,
                          T entity,
                          String message) {

    public Response(HttpStatus status, String message) {
        this(status, null, message);
    }

    public Response(HttpStatus status, T entity) {
        this(status, entity, "");
    }
}
