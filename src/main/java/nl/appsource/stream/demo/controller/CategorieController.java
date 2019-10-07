package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.repository.CategorieRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categorien")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    private CategorieResourceAssembler categorieResourceAssembler = new CategorieResourceAssembler();

    @GetMapping
    public Mono<CollectionModel<EntityModel<Categorie>>> getAll() {
        return categorieRepository.findAll()
                .limitRequest(10)
                .collectList()
                .map(categorieResourceAssembler::toCollectionModel)
                ;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<Categorie>> create(@RequestBody EntityModel<Categorie> categorie) {
        return categorieRepository.save(categorie.getContent())
                .map(categorieResourceAssembler::toModel);

    }

    @GetMapping("/{id}")
    public Mono<EntityModel<Categorie>> getById(@PathVariable Long id) {
        return categorieRepository.findById(id)
                .map(categorieResourceAssembler::toModel)
                .map(categorie -> categorie.add(linkTo(methodOn(CategorieController.class).getAll()).withRel("categorien")))
                ;
    }

}