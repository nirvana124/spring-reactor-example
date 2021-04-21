package com.nirvana.springreactorexample.service;

import com.nirvana.springreactorexample.exception.ProfileNotFoundException;
import com.nirvana.springreactorexample.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final WebClient profileWebClient;

    public static String convertToQueryString(List<String> ids) {
        return ids.stream().map(id -> "id=" + id).collect(Collectors.joining("&"));
    }

    @Override
    public Mono<Profile> getById(String id) {
        return profileWebClient.get().uri("/profiles/" + id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Profile.class))
                .switchIfEmpty(Mono.error(new ProfileNotFoundException()));
    }

    @Override
    public Flux<Profile> profiles(List<String> ids) {
        return profileWebClient.get()
                .uri("/profiles?" + convertToQueryString(ids))
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Profile.class))
                .switchIfEmpty(Flux.error(new ProfileNotFoundException()));
    }
}
