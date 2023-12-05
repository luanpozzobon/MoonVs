package luan.moonvs.utils;

import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpResponse;

@Getter
public class HttpRequestEntity<T> {
    private URI uri;
    private HttpMethod method;
    private HttpEntity<?> entity;
    private ParameterizedTypeReference<T> responseType;

    public HttpRequestEntity(String url, HttpMethod method, HttpEntity<?> entity) {
        this.uri = URI.create(url);
        this.method = method;
        this.entity = entity;
        this.responseType = new ParameterizedTypeReference<T>() { };
    }

    public HttpRequestEntity(String url, HttpMethod method, HttpHeaders headers) {
        this.uri = URI.create(url);
        this.method = method;
        this.entity = new HttpEntity<>(headers);
        this.responseType = new ParameterizedTypeReference<T>() { };
    }

    public HttpRequestEntity(String url, HttpMethod method, T body) {
        this.uri = URI.create(url);
        this.method = method;
        this.entity = new HttpEntity<>(body);
        this.responseType = new ParameterizedTypeReference<T>() { };
    }

    public HttpRequestEntity(String url, HttpMethod method, HttpHeaders headers, T body) {
        this.uri = URI.create(url);
        this.method = method;
        this.entity = new HttpEntity<>(body, headers);
        this.responseType = new ParameterizedTypeReference<T>() { };
    }

    public ResponseEntity<T> exchange() {
        return new RestTemplate()
                .exchange(
                        this.uri,
                        this.method,
                        this.entity,
                        this.responseType
                );
    }


}
