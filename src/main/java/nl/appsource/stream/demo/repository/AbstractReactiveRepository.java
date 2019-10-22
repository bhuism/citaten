package nl.appsource.stream.demo.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractReactiveRepository<T extends Persistable<Long>> extends R2dbcRepository<T, Long>  {
}
