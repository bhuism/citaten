package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.Util;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Mono.justOrEmpty;

@Component
@RequiredArgsConstructor
@Slf4j
public class CitaatHandler {

    private final CitaatRepository citaatRepository;

    private static final Mono<ServerResponse> NOTFOUND = status(HttpStatus.NOT_FOUND).contentType(APPLICATION_JSON).build();

    public Mono<ServerResponse> getOne(final ServerRequest request) {
        return Mono.just(request)
                .flatMap(r -> Util.safeUuidValueofMono(r.pathVariable("uuid")))
                .flatMap(uuid -> citaatRepository.findByUuid(uuid))
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND)
        ;
    }

        public Mono<ServerResponse> getAll(final ServerRequest request) {
            return ok().contentType(APPLICATION_JSON).body(citaatRepository.findAll().limitRequest(Util.getLongOrDefault(request, "limit", 5L)), Citaat.class);
        }

    public Mono<ServerResponse> create(final ServerRequest request) {
        return request.body(toMono(Citaat.class))
                .flatMap(citaatRepository::save)
                .flatMap(citaat -> status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(fromValue(citaat)))
                .switchIfEmpty(NOTFOUND);

    }

    public Mono<ServerResponse> delete(final ServerRequest request) {
        return Mono.just(request)
                .flatMap(r -> Util.safeUuidValueofMono(r.pathVariable("uuid")))
                .flatMap(uuid -> citaatRepository.findByUuid(uuid))
                .flatMap(citaat -> ok().contentType(APPLICATION_JSON).build(citaatRepository.delete(citaat)))
                .switchIfEmpty(NOTFOUND)
                ;

    }


    public Mono<ServerResponse> getCitaatByIdSpreker(final ServerRequest request) {
        return Mono.just(request)
                .flatMap(r -> Util.safeUuidValueofMono(r.pathVariable("uuid")))
                .flatMap(uuid -> citaatRepository.getSprekerByCitaatId(uuid))
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND)

        ;

    }

    public Mono<ServerResponse> getCitaatByIdCategorie(final ServerRequest request) {
        return Mono.just(request)
                .flatMap(r -> Util.safeUuidValueofMono(r.pathVariable("uuid")))
                .flatMap(uuid -> citaatRepository.getCategorieByCitaatId(uuid))
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND)
        ;

    }



}
