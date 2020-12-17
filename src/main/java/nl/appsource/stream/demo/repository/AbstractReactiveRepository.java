package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.AbstractPersistable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface AbstractReactiveRepository<T extends AbstractPersistable> extends R2dbcRepository<T, UUID>, CustomRepository<T> {

}
