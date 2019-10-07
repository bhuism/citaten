package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/citaten")
public class CitaatController {

    private final CitaatRepository citaatRepository;

    private CitaatResourceAssembler citaatResourceAssembler = new CitaatResourceAssembler();

    @GetMapping
    public Mono<CollectionModel<EntityModel<Citaat>>> getAll() {
        return citaatRepository.findAll()
                .limitRequest(10)
                .collectList()
                .map(citaatResourceAssembler::toCollectionModel)
                ;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<Citaat>> create(@RequestBody EntityModel<Citaat> citaat) {
        return citaatRepository.save(citaat.getContent())
                .map(citaatResourceAssembler::toModel)
                ;

    }

    @GetMapping("/{id}")
    public Mono<EntityModel<Citaat>> getById(@PathVariable Long id) {
        return citaatRepository.findById(id)
                .map(citaatResourceAssembler::toModel)
                .map(citaat -> citaat.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten")))
                ;
    }

}