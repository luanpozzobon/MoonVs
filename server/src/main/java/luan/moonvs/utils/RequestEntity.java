package luan.moonvs.utils;

import lombok.Getter;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Getter
public class RequestEntity<T, R> {
    private URIBuilder uriBuilder;
    private URI uri;
    private final HttpMethod method;
    private final HttpEntity<T> entity;
    private final Class<R> responseType;

    public RequestEntity(String url,
                         HttpMethod method,
                         HttpEntity<T> entity,
                         Class<R> responseType) throws URISyntaxException {
        this.uriBuilder = new URIBuilder(url);

        this.method = method;
        this.entity = entity;
        this.responseType = responseType;
    }

    public RequestEntity(String url,
                         HttpMethod method,
                         HttpHeaders headers,
                         Class<R> responseType) throws URISyntaxException {
        this(url, method, new HttpEntity<>(headers), responseType);
    }

    public RequestEntity<T, R> addParameter(String parameterName, String parameterValue) {
        this.uriBuilder.setParameter(parameterName, parameterValue);

        return this;
    }

    public RequestEntity<T, R> addParameters(Map<String, String> parameters) {
        parameters.forEach(this::addParameter);

        return this;
    }

    public ResponseEntity<R> exchange() throws URISyntaxException {
        this.uri = uriBuilder.build();
        return new RestTemplate()
                .exchange(
                        this.uri,
                        this.method,
                        this.entity,
                        this.responseType
                );
    }
}
