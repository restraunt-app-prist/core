package kpi.fict.prist.core.user.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@Document("user_profile")
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
public class UserProfileEntity implements Persistable<String> {

    @Id
    private String id;
    private String externalId;
    private String firstName;
    private String lastName;
    private String email;
    private String pictureUrl;
    private String phoneNumber;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
