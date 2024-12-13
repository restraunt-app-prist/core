package kpi.fict.prist.core.menu.controller;

import kpi.fict.prist.core.common.MenuCategory;
import kpi.fict.prist.core.menu.dto.CreateMenuItemRequest;
import kpi.fict.prist.core.menu.dto.UpdateMenuItemRequest;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;
import kpi.fict.prist.core.menu.service.MenuService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuItemEntity> createMenuItem(@RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.ok(menuService.createMenuItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemEntity> updateMenuItem(@PathVariable String id, @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(menuService.updateMenuItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MenuItemEntity>> getAllMenuItems() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemEntity> getMenuItemById(@PathVariable String id) {
        Optional<MenuItemEntity> menuItem = menuService.getMenuItemById(id);
        return menuItem.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemEntity>> getMenuItemsByCategory(@PathVariable MenuCategory category) {
        return ResponseEntity.ok(menuService.getMenuItemsByCategory(category));
    }
}
