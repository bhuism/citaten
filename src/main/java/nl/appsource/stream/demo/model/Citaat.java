package nl.appsource.stream.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@RequiredArgsConstructor
@Table("Citaat")
public class Citaat {

    @Id
    private final Long id;

    private final String name;

    @Column("spreker")
    private final Long sprekerId;

    @Column("categorie")
    private final Long categorieId;

    public Citaat withId(Long id) {
        return new Citaat(id, this.name, this.sprekerId, this.categorieId);
    }

    public static Citaat of(String name, Long sprekerId, Long categorieId) {
        return new Citaat(null, name, sprekerId, categorieId);
    }

}