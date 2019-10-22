package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.assembler.CategorieResourceAssembler;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.repository.CategorieRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/categorien")
public class CategorieController extends AbstractController<Categorie> {

    public CategorieController(final CategorieRepository repository, final CategorieResourceAssembler resourceAssembler) {
        super(repository, resourceAssembler);
    }

}