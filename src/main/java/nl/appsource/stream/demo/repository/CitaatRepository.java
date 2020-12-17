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

    @Query("SELECT s.* FROM author s INNER JOIN quote c ON c.author_id=s.id WHERE c.id=:uuid")
    Mono<Spreker> getSprekerByCitaatUuid(@Param("uuid") final UUID uuid);

    @Query("SELECT a.* FROM genre a INNER JOIN quote c ON c.genre_id=a.id WHERE c.id=:uuid")
    Mono<Categorie> getCategorieByCitaatUuid(@Param("uuid") final UUID uuid);

}