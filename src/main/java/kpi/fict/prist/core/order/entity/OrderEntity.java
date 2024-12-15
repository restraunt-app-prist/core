package kpi.fict.prist.core.order.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("order")
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
public class OrderEntity implements Persistable<Long> {

    @Id
    private String id;
    @CreatedBy
    private String creatorUserId;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
