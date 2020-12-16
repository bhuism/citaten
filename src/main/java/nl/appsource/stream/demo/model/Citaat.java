package nl.appsource.stream.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Table("quote")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Citaat extends AbstractPersistable {

    private String name;

    @Column("spreker_id")
    private UUID spreker;

    @Column("categorie_id")
    private UUID categorie;

    public Citaat(final UUID uuid, final String name, final UUID spreker, final UUID categorie) {
        super(uuid);
        this.name = name;
        this.spreker = spreker;
        this.categorie = categorie;
    }

}