package nl.appsource.stream.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("Citaat")
@Generated
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Citaat {

    @Id
    @JsonProperty
    @NotNull
    private Long id;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @Column("spreker")
    @NotNull
    private Long spreker;

    @Column("categorie")
    @NotNull
    private Long categorie;

    public Citaat withId(final Long id) {
        return new Citaat(id, this.name, this.spreker, this.categorie);
    }

    public static Citaat of(final String name, final Long spreker, final Long categorie) {
        return new Citaat(null, name, spreker, categorie);
    }

}