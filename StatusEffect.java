public interface StatusEffect {
    void update(Enemy enemy);
    boolean isExpired();
    String getId();
}