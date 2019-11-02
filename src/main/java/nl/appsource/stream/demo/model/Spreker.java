package nl.appsource.stream.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Table("Spreker")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Spreker extends AbstractPersistable {

    private String name;

    public Spreker(final Long id, final UUID uuid, final String name) {
        super(id, uuid);
        this.name = name;
    }

}