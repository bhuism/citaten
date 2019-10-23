package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/citaten")
public class CitaatController extends AbstractController<Citaat> {

    @Autowired
    private CitaatRepository citaatRepository;

    public CitaatController(final CitaatRepository repository) {
        super(repository);
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