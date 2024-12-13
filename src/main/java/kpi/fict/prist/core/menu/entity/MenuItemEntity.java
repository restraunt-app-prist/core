package kpi.fict.prist.core.menu.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import kpi.fict.prist.core.common.MenuCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@Document("menu_items")
public class MenuItemEntity implements Persistable<String> {

    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private boolean available;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
