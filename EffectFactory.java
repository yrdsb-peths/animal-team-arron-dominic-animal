public class EffectFactory {

    /** 
     * Creates a standard slow effect.
     * @param duration Seconds it lasts
     * @param power 0.5f means 50% speed, 0.0f means frozen.
     */
    public static StatusEffect createSlow(double duration, float power) {
        return new SlowEffect(duration, power);
    }

    /** 
     * Creates a Damage-Over-Time (Poison/Fire) effect.
     */
    public static StatusEffect createDoT(double duration, int damagePerTick, String type) {
        return new DamageOverTimeEffect(duration, damagePerTick, type);
    }
}