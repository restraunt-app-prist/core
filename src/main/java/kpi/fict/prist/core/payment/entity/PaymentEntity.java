package kpi.fict.prist.core.payment.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@Document("payment")
public class PaymentEntity implements Persistable<String> {

    @Id
    private String id;

    private String paymentIntentId;
    private long amount;
    private String currency;
    private String description;
    private String status;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @Override
    public boolean isNew() {
        return id == null;
    }
}