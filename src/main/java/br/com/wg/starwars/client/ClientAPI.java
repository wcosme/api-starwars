package br.com.wg.starwars.client;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class ClientAPI {

    private final WebClient webClient;

    public <T> Mono<T> exec(String url, Class<T> type) {
        if (StringUtils.isBlank(url)) {
            return Mono.empty();
        }
        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(type);
    }
}
