package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractReactiveRepository<T> extends R2dbcRepository<T, Long>  {
}
