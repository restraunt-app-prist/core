package kpi.fict.prist.core.cart.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@Document("carts")
public class CartEntity implements Persistable<String> {

    @Id
    private String id;

    private String userExternalId;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public CartEntity(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    @Data
    @NoArgsConstructor
    public static class CartItem {
        private String menuItemId;
        private int quantity;

        public CartItem(String menuItemId, int quantity) {
            this.menuItemId = menuItemId;
            this.quantity = quantity;
        }
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
