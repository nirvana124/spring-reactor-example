package com.nirvana.springreactorexample.service;

import com.nirvana.springreactorexample.model.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProfileService {
    Mono<Profile> getById(String id);
    Flux<Profile> profiles(List<String> ids);
}
