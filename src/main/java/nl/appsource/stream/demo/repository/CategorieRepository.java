package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CategorieRepository extends AbstractReactiveRepository<Categorie> {

    @Query("SELECT x.* FROM Categorie x WHERE x.uuid=:uuid")
    Mono<Categorie> findByUuid(@Param("uuid") final UUID uuid);

    @Query("DELETE FROM Categorie x WHERE x.uuid=:uuid")
    Mono<Void> deleteByUuid(@Param("uuid") final UUID uuid);

}