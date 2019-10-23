package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sprekers")
public class SprekerController extends AbstractController<Spreker> {

    public SprekerController(final SprekerRepository repository) {
        super(repository);
    }

}