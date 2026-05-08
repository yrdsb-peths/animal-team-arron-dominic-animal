public class CurrencyManager {
    private static int gold = 0;
    public static final int STARTING_GOLD = GameConfig.STARTING_GOLD;

    public static void reset() {
        gold = STARTING_GOLD;
    }

    public static void earn(int amount) {
        gold += amount;
    }

    public static boolean spend(int cost) {
        if (gold >= cost) {
            gold -= cost;
            return true;
        }
        return false;
    }

    public static int getGold() {
        return gold;
    }
}
