package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CitaatRepository extends AbstractReactiveRepository<Citaat> {

    @Query("SELECT s.* FROM Spreker s INNER JOIN Citaat c ON c.spreker=s.id WHERE c.uuid=:uuid")
    Mono<Spreker> getSprekerByCitaatUuid(@Param("uuid") final UUID uuid);

    @Query("SELECT a.* FROM Categorie a INNER JOIN Citaat c ON c.categorie=a.id WHERE c.uuid=:uuid")
    Mono<Categorie> getCategorieByCitaatUuid(@Param("uuid") final UUID uuid);

}