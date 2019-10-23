package nl.appsource.stream.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractPersistable implements Persistable<Long> {

    @Id
    @NotNull
    private Long id;

    @NotNull
    private UUID uuid;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

}

