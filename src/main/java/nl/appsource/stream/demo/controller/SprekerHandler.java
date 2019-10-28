package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SprekerHandler extends AbstractHandler<Spreker> {

    public SprekerHandler(final SprekerRepository sprekerRepository) {
        super(sprekerRepository, Spreker.class);
    }

}
