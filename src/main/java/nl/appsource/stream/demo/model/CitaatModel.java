package nl.appsource.stream.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;


@Getter
@Relation("citaten")
@RequiredArgsConstructor
public class CitaatModel extends EntityModel<CitaatModel> {

    private final Long id;

    private final String name;

    public CitaatModel withId(Long id) {
        return new CitaatModel(id, this.name);
    }

    public static CitaatModel of(String name) {
        return new CitaatModel(null, name);
    }

}
