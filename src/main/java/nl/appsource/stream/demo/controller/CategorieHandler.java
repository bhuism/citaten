package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.repository.CategorieRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategorieHandler extends AbstractHandler<Categorie> {

    public CategorieHandler(final CategorieRepository categorieRepository, final R2dbcEntityTemplate template) {
        super(categorieRepository, Categorie.class, template);
    }

//    @Override
//    public Criteria getQueryCriteria(final String queryString) {
//        return Util.safeLongValueofOptional(queryString)
//            .map(aLong -> where("count").is(aLong))
//            .map(criteria -> super.getQueryCriteria(queryString).or(criteria))
//            .orElse(super.getQueryCriteria(queryString));
//    }

}
