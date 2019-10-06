package nl.appsource.stream.demo.repository;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaatRepository extends R2dbcRepository<Citaat, Long> {
}