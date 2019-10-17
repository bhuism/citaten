package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CitaatRepository extends R2dbcRepository<Citaat, Long> {

    @Query("SELECT s.* FROM Spreker s INNER JOIN Citaat c ON c.spreker=s.id WHERE c.id=:id")
    Mono<Spreker> getSprekerByCitaatId(@Param("id") final Long id);

    @Query("SELECT a.* FROM Categorie a INNER JOIN Citaat c ON c.categorie=a.id WHERE c.id=:id")
    Mono<Categorie> getCategorieByCitaatId(@Param("id") final Long id);

}