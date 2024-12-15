package kpi.fict.prist.core.common.exception;

public class MenuItemNotFoundException extends RuntimeException {
    private final String menuItemId;

    public MenuItemNotFoundException(String menuItemId) {
        super("Menu item not found by id: " + menuItemId);
        this.menuItemId = menuItemId;
    }
}
