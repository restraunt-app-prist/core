package kpi.fict.prist.core.order.entity;

import java.time.LocalDateTime;
import java.util.List;

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
@Document("order")
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
public class OrderEntity implements Persistable<String> {

    @Id
    private String id;

    private List<CartItem> items;
    private Double totalPrice;

    private String paymentId;
    private OrderStatus status; 
    private String userExternalId;

    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String menuItemId;
        private Integer quantity;
    }

    public enum OrderStatus {
        PENDING, PAID, FAILED
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
