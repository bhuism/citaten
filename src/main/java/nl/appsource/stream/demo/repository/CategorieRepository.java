package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends AbstractReactiveRepository<Categorie> {

}