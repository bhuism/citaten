package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.UUID;

@NoRepositoryBean
public interface CustomRepository<T> {

    Mono<T> findByUuid(@Param("uuid") final UUID uuid);

}
