package nl.appsource.stream.demo.model;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@RequiredArgsConstructor
@Table("Spreker")
@Generated
@ToString
public class Spreker {

    @Id
    private final Long id;

    private final String name;

    public Spreker withId(Long id) {
        return new Spreker(id, this.name);
    }

    public static Spreker of(String name) {
        return new Spreker(null, name);
    }

}