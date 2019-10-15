package nl.appsource.stream.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("Citaat")
public class Citaat {

    @Id
    private Long id;

    private String name;

    @Column("spreker")
    private Long sprekerId;

    @Column("categorie")
    private Long categorieId;

    public Citaat withId(Long id) {
        return new Citaat(id, this.name, this.sprekerId, this.categorieId);
    }

    public static Citaat of(String name, Long sprekerId, Long categorieId) {
        return new Citaat(null, name, sprekerId, categorieId);
    }

}