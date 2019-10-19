package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Spreker;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprekerRepository extends AbstractReactiveRepository<Spreker> {
}