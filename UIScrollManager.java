import greenfoot.*;

public class UIScrollManager {
    private static int scrollAmount = 0;
    
    public static void scroll(int amount) {
        setScroll(scrollAmount + amount);
    }

    // NEW METHOD: Jumps to an exact position, respecting the bottom limit
    public static void setScroll(int target) {
        scrollAmount = target;
        
        // Limit scrolling so we don't scroll into empty space
        int maxScroll = (UnitRegistry.roster.size() - 4) * GameConfig.MENU_CARD_SPACING;
        if (maxScroll < 0) maxScroll = 0; // Failsafe if you have fewer than 4 units
        
        if (scrollAmount < 0) scrollAmount = 0;
        if (scrollAmount > maxScroll) scrollAmount = maxScroll;
    }

    public static int getOffset() {
        return scrollAmount;
    }
    
    public static void reset() {
        scrollAmount = 0;
    }
}