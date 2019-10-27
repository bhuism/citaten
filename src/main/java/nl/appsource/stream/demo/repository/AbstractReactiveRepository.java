package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.AbstractPersistable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractReactiveRepository<T extends AbstractPersistable> extends R2dbcRepository<T, Long> {

}
