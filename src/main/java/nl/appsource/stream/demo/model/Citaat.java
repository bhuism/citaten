package nl.appsource.stream.demo.model;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.hateoas.server.core.Relation;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("Citaat")
@Generated
@ToString
public class Citaat {

    @Id
    private Long id;

    private String name;

    @Column("spreker")
    private Long spreker;

    @Column("categorie")
    private Long categorie;

    public Citaat withId(final Long id) {
        return new Citaat(id, this.name, this.spreker, this.categorie);
    }

    public static Citaat of(final String name, final Long spreker, final Long categorie) {
        return new Citaat(null, name, spreker, categorie);
    }

}