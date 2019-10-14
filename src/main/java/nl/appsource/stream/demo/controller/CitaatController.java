package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/citaten")
public class CitaatController {

    private final CitaatRepository citaatRepository;

    private CitaatResourceAssembler citaatResourceAssembler = new CitaatResourceAssembler();

    @GetMapping
    public Mono<CollectionModel<EntityModel<Citaat>>> getAll() {
        return new CitaatResourceAssembler()
                .toCollectionModel(citaatRepository.findAll()
                .limitRequest(5), null)
                ;

    }

    @GetMapping("/{id}")
    public Mono<EntityModel<Citaat>> getById(@PathVariable Long id) {
        return citaatRepository
                .findById(id)
                .map(c -> citaatResourceAssembler.toModel2(c, null))
                ;
    }

}