package kpi.fict.prist.core.common.bootstrap;

import java.math.BigDecimal;
import kpi.fict.prist.core.common.MenuCategory;
import kpi.fict.prist.core.menu.dto.CreateMenuItemRequest;
import kpi.fict.prist.core.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BootstrapComponent {

    @Autowired
    MenuService menuService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeMenu() {
        if (menuService.getAllMenuItems().isEmpty()) {
            var pizzaCreateRequest = CreateMenuItemRequest.builder()
                .name("pizza")
                .description("some description for pizza")
                .available(true)
                .pictureUrl(
                    "https://www.foodandwine.com/thmb/Wd4lBRZz3X_8qBr69UOu2m7I2iw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/classic-cheese-pizza-FT-RECIPE0422-31a2c938fc2546c9a07b7011658cfd05.jpg")
                .price(BigDecimal.valueOf(12.50))
                .category(MenuCategory.MEALS)
                .build();
            menuService.createMenuItem(pizzaCreateRequest);

            var sushi = CreateMenuItemRequest.builder()
                .name("sushi")
                .description("some description for sushi")
                .pictureUrl("https://roll-club.kh.ua/wp-content/uploads/2014/12/4.jpg")
                .available(true)
                .price(BigDecimal.valueOf(25.99))
                .category(MenuCategory.MEALS)
                .build();
            menuService.createMenuItem(sushi);

            var tea = CreateMenuItemRequest.builder()
                .name("tea")
                .description("some description for tea")
                .pictureUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIKW0hDZrL0AuTFpzcoqY7tRzxlIgwb5tbpw&s")
                .available(true)
                .price(BigDecimal.valueOf(3))
                .category(MenuCategory.DRINKS)
                .build();
            menuService.createMenuItem(tea);

            var coffee = CreateMenuItemRequest.builder()
                .name("Coffee")
                .description("some description for coffee")
                .pictureUrl("https://thecircleofaroma.in/cdn/shop/files/picture-steaming-cup-coffee-perfect-coffee-lovers-can-be-used-illustrate-concept-cozy-morning-caffeine-addiction-warm-beverage.jpg?v=1719467884&width=3840")
                .available(true)
                .price(BigDecimal.valueOf(5.50))
                .category(MenuCategory.DRINKS)
                .build();
            menuService.createMenuItem(coffee);

            var cake = CreateMenuItemRequest.builder()
                .name("Cake")
                .description("some description for cake")
                .pictureUrl("https://assets.bonappetit.com/photos/59c924da3b3bf713cb63808a/1:1/w_2560%2Cc_limit/1017%2520WEB%2520WEEK1068.jpg")
                .available(true)
                .price(BigDecimal.valueOf(5.50))
                .category(MenuCategory.DESSERTS)
                .build();
            menuService.createMenuItem(cake);

            var sandwich = CreateMenuItemRequest.builder()
                .name("Sandwich with cheese")
                .description("some description for sandwich with cheese")
                .pictureUrl("https://static01.nyt.com/images/2023/02/28/multimedia/ep-air-fryer-grilled-cheese-vpmf/ep-air-fryer-grilled-cheese-vpmf-square640.jpg")
                .available(true)
                .price(BigDecimal.valueOf(6.25))
                .category(MenuCategory.SNACKS)
                .build();
            menuService.createMenuItem(sandwich);
        }
    }

}
