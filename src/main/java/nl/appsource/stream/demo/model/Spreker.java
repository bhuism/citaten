package nl.appsource.stream.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("Spreker")
@ToString
@NoArgsConstructor
public class Spreker extends AbstractPersistable {

    private String name;

    public Spreker(Long id, final String name) {
        super(id);
        this.name = name;
    }

    public Spreker withId(Long id) {
        return new Spreker(id, this.name);
    }

    public static Spreker of(String name) {
        return new Spreker(null, name);
    }

}