package nl.appsource.stream.demo.model;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@RequiredArgsConstructor
@Table("Categorie")
@Generated
public class Categorie {

    @Id
    private final Long id;

    private final String name;

    public Categorie withId(Long id) {
        return new Categorie(id, this.name);
    }

    public static Categorie of(String name) {
        return new Categorie(null, name);
    }

}