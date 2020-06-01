package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Spreker;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface SprekerRepository extends AbstractReactiveRepository<Spreker> {

    Mono<Spreker> findByUuid(@Param("uuid") final UUID uuid);

    Mono<Void> deleteByUuid(@Param("uuid") final UUID uuid);

}