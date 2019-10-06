package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends R2dbcRepository<Categorie, Long> {
}