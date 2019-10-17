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
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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

    @GetMapping("/{id}/spreker")
    public Mono<EntityModel<Spreker>> getCitaatByIdSpreker(@PathVariable Long id) {
        return citaatRepository
                .getSprekerByCitaatId(id)
                .map(s -> sprekerResourceAssembler.toModel2(s, null))
                ;
    }

    @GetMapping("/{id}/categorie")
    public Mono<EntityModel<Categorie>> getCitaatByIdCategorie(@PathVariable Long id) {
        return citaatRepository
                .getCategorieByCitaatId(id)
                .map(s -> categorieResourceAssembler.toModel2(s, null))
                ;
    }

}