package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.Util;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Slf4j
@Component
public class CitaatHandler extends AbstractHandler<Citaat> {

    private final CitaatRepository citaatRepository;

    private static final Mono<ServerResponse> NOTFOUND = status(HttpStatus.NOT_FOUND).contentType(APPLICATION_JSON).build();

    public CitaatHandler(final CitaatRepository citaatRepository) {
        super(citaatRepository, Citaat.class);
        this.citaatRepository = citaatRepository;
    }

    public Mono<ServerResponse> getCitaatByIdSpreker(final ServerRequest request) {
        return Mono.just(request)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(citaatRepository::getSprekerByCitaatId)
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> getCitaatByIdCategorie(final ServerRequest request) {
        return Mono.just(request)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(citaatRepository::getCategorieByCitaatId)
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND);

    }

}
