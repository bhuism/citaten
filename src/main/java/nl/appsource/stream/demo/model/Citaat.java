package nl.appsource.stream.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Table("Citaat")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Citaat extends AbstractPersistable {

    private String name;

    @Column("spreker")
    private Long spreker;

    @Column("categorie")
    private Long categorie;

    public Citaat(final Long id, final UUID uuid, final String name, final Long spreker, final Long categorie) {
        super(id, uuid);
        this.name = name;
        this.spreker = spreker;
        this.categorie = categorie;
    }

    public Citaat withId(final UUID uuid) {
        return new Citaat(null, uuid, this.name, this.spreker, this.categorie);
    }

    public static Citaat of(final String name, final Long spreker, final Long categorie) {
        return new Citaat(null, null, name, spreker, categorie);
    }

}