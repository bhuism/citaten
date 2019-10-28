package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.repository.CategorieRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategorieHandler extends AbstractHandler<Categorie> {

    public CategorieHandler(final CategorieRepository categorieRepository) {
        super(categorieRepository, Categorie.class);
    }

}
