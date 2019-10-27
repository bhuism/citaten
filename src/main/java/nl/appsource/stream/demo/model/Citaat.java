package nl.appsource.stream.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Table("Citaat")
@ToString(callSuper = true)
@NoArgsConstructor
public class Citaat extends AbstractPersistable {

    @NotBlank
    private String name;

    @Column("spreker")
    @NotNull
    private Long spreker;

    @Column("categorie")
    @NotNull
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