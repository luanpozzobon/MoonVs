package luan.moonvs.utils;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Getter
@Deprecated
public class HttpRequestEntity<T, R> {
    private final URI uri;
    private final HttpMethod method;
    private final HttpEntity<T> entity;
    private final Class<R> responseType;

    public HttpRequestEntity(String url, HttpMethod method, HttpEntity<T> entity, Class<R> responseType) {
        this.uri = URI.create(url);
        this.method = method;
        this.entity = entity;
        this.responseType = responseType;
    }

    public HttpRequestEntity(String url, HttpMethod method, HttpHeaders headers, Class<R> responseType) {
        this(url, method, new HttpEntity<>(headers), responseType);
    }

    public HttpRequestEntity(String url, HttpMethod method, T body, Class<R> responseType) {
        this(url, method, new HttpEntity<>(body), responseType);
    }

    public HttpRequestEntity(String url, HttpMethod method, HttpHeaders headers, T body, Class<R> responseType) {
        this(url, method, new HttpEntity<>(body, headers), responseType);
    }

    public ResponseEntity<R> exchange() {
        return new RestTemplate()
                .exchange(
                        this.uri,
                        this.method,
                        this.entity,
                        this.responseType
                );
    }
}