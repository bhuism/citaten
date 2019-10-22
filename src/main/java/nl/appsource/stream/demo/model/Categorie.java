package nl.appsource.stream.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("Categorie")
@ToString
@NoArgsConstructor
public class Categorie extends AbstractPersistable {

    @JsonProperty("name")
    private String name;

    public Categorie(final Long id, final String name) {
        super(id);
        this.name = name;
    }

    public Categorie withId(Long id) {
        return new Categorie(id, this.name);
    }

    public static Categorie of(String name) {
        return new Categorie(null, name);
    }

}