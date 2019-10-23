package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.repository.CategorieRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/categorien")
public class CategorieController extends AbstractController<Categorie> {

    private final CategorieRepository categorieRepository;

    public CategorieController(final CategorieRepository repository) {
        super(repository);
        this.categorieRepository = repository;
    }

    @Override
    public Mono<Categorie> getByUuid(@PathVariable final UUID uuid) {
        log.debug("getByUuid() uuid=" + uuid);
        return categorieRepository.findByUuid(uuid);
    }

    @Override
    public Mono<Void> deleteByUuid(@PathVariable final UUID uuid) {
        log.debug("delete() uuid=" + uuid);
        return categorieRepository.deleteByUuid(uuid);
    }


}