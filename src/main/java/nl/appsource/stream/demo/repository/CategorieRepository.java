package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CategorieRepository extends AbstractReactiveRepository<Categorie> {

    Mono<Categorie> findByUuid(@Param("uuid") final UUID uuid);

    Mono<Void> deleteByUuid(@Param("uuid") final UUID uuid);

}