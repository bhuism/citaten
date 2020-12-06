package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CitaatHandler extends AbstractHandler<Citaat> {

    private final CitaatRepository citaatRepository;

    public CitaatHandler(final CitaatRepository citaatRepository, final R2dbcEntityTemplate template) {
        super(citaatRepository, Citaat.class, template);
        this.citaatRepository = citaatRepository;
    }

    public Mono<ServerResponse> getSprekerByCitaatId(final ServerRequest serverRequest) {
        return ((MyHandlerFunction) citaatRepository::getSprekerByCitaatUuid).handle(serverRequest);
    }

    public Mono<ServerResponse> getCategorieByCitaatId(final ServerRequest serverRequest) {
        return ((MyHandlerFunction) citaatRepository::getCategorieByCitaatUuid).handle(serverRequest);
    }

}
