package kpi.fict.prist.core.menu.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kpi.fict.prist.core.common.MenuCategory;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;

import java.util.List;

@Repository
public interface MenuItemEntityRepository extends MongoRepository<MenuItemEntity, String> {

    List<MenuItemEntity> findByCategory(MenuCategory category);
}

