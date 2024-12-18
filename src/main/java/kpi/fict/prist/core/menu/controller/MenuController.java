package kpi.fict.prist.core.menu.controller;

import kpi.fict.prist.core.common.MenuCategory;
import kpi.fict.prist.core.menu.dto.CreateMenuItemRequest;
import kpi.fict.prist.core.menu.dto.UpdateMenuItemRequest;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;
import kpi.fict.prist.core.menu.service.MenuService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuItemEntity> getAllMenuItems() {
        return menuService.getAllMenuItems();
    }

    @GetMapping("{id}")
    public ResponseEntity<MenuItemEntity> getMenuItemById(@PathVariable String id) {
        return menuService.getMenuItemById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("category")
    public List<MenuItemEntity> getMenuItemsByCategory(@RequestParam(required = false, defaultValue = "MEALS") MenuCategory category) {
        return menuService.getMenuItemsByCategory(category);
    }
}
