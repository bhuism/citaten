package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/citaten")
public class CitaatController extends AbstractController<Citaat> {

    private final CitaatRepository citaatRepository;

    public CitaatController(final CitaatRepository citaatRepository) {
        super(citaatRepository);
        this.citaatRepository = citaatRepository;
    }

    @GetMapping(value = "/{id}/spreker", produces = {APPLICATION_JSON_VALUE})
    public Mono<Spreker> getCitaatByIdSpreker(@PathVariable Long id) {
        return citaatRepository.getSprekerByCitaatId(id);
    }

    @GetMapping(value = "/{id}/categorie", produces = {APPLICATION_JSON_VALUE})
    public Mono<Categorie> getCitaatByIdCategorie(@PathVariable Long id) {
        return citaatRepository.getCategorieByCitaatId(id);
    }

}