package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.assembler.CategorieResourceAssembler;
import nl.appsource.stream.demo.assembler.CitaatResourceAssembler;
import nl.appsource.stream.demo.assembler.SprekerResourceAssembler;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/citaten")
public class CitaatController extends AbstractController<Citaat> {

    @Autowired
    private SprekerResourceAssembler sprekerResourceAssembler;

    @Autowired
    private CitaatResourceAssembler citaatResourceAssembler;

    @Autowired
    private CategorieResourceAssembler categorieResourceAssembler;

    @Autowired
    private CitaatRepository citaatRepository;

    public CitaatController(final CitaatRepository repository, final CitaatResourceAssembler resourceAssembler) {
        super(repository, resourceAssembler);
    }

    @GetMapping(value=  "test3", produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public Flux<Citaat> test3(@RequestParam(required = false, defaultValue = "5") @Max(100) @Min(1) final Long limit) {
        return citaatRepository.findAll().limitRequest(limit)
                ;
    }

    @GetMapping(value=  "test2", produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public Mono<CollectionModel<EntityModel<Citaat>>> test2(@RequestParam(required = false, defaultValue = "5") @Max(100) @Min(1) final Long limit) {
        log.debug("getAll() limit=" + limit);
        return citaatResourceAssembler
                .toCollectionModel(citaatRepository.findAll()
                        .limitRequest(limit), null)
                ;
    }

    @GetMapping(value = "/{id}/spreker", produces = APPLICATION_JSON_VALUE)
    public Mono<EntityModel<Spreker>> getCitaatByIdSpreker(@PathVariable Long id) {
        return citaatRepository
                .getSprekerByCitaatId(id)
                .flatMap(s -> sprekerResourceAssembler.toModel(s, null))
                ;
    }

    @GetMapping(value = "/{id}/categorie", produces = APPLICATION_JSON_VALUE)
    public Mono<EntityModel<Categorie>> getCitaatByIdCategorie(@PathVariable Long id) {
        return citaatRepository
                .getCategorieByCitaatId(id)
                .flatMap(s -> categorieResourceAssembler.toModel(s, null))
                ;
    }

}