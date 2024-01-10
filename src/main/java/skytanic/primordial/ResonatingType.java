package skytanic.primordial;

public enum ResonatingType {
    SHRIEKING (20, 10, 1.3f),
    SPEAKING(35, 20, 1.2f),
    WHISPERING (50, 25, 1f),
    SILENT (100, 0, 1f);

    public final int cooldown_length;
    public final int activation_length;
    public final float pitch;

    ResonatingType(int cooldown_length, int activation_length, float pitch) {
        this.cooldown_length = cooldown_length;
        this.activation_length = activation_length;
        this.pitch = pitch;
    }

    public static ResonatingType getByDistance(int distance) {
        if (distance <= 250) {
            return ResonatingType.SHRIEKING;
        } else if (distance <= 500) {
            return ResonatingType.SPEAKING;
        } else if (distance <= 1500) {
            return ResonatingType.WHISPERING;
        } else {
            return ResonatingType.SILENT;
        }
    }

}
