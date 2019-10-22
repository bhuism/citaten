package nl.appsource.stream.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPersistable implements Persistable<Long> {

    @Id
    @NotNull
    private Long id;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

}

