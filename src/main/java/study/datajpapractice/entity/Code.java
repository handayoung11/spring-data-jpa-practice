package study.datajpapractice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data @Setter(AccessLevel.NONE)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code implements Persistable<String> {
    @Id
    private String code;

    @CreatedDate
    LocalDateTime createdDate;

    public Code(String code) {
        this.code = code;
    }

    @Override
    public String getId() {
        return code;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
