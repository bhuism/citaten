package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/sprekers")
public class SprekerController extends AbstractController<Spreker> {

    private final SprekerRepository sprekerRepository;

    public SprekerController(final SprekerRepository repository) {
        super(repository);
        this.sprekerRepository = repository;
    }

    public Mono<Spreker> getByUuid(final UUID uuid) {
        log.debug("getByUuid() uuid=" + uuid);
        return sprekerRepository.findByUuid(uuid);
    }

    @Override
    public Mono<Void> deleteByUuid(@PathVariable final UUID uuid) {
        log.debug("delete() uuid=" + uuid);
        return sprekerRepository.deleteByUuid(uuid);
    }

}