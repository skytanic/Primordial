package skytanic.primordial;

import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum SensorStatus {
    RETRACTED,
    DORMANT,
    RESONATING;

    private static final SensorStatus[] STATUSES = values();

    public ResourceLocation toModelLocation()
    {
        return new ResourceLocation(Primordial.MOD_ID, "item/sensor_tendrils_" + toString().toLowerCase(Locale.ROOT));
    }

    public static SensorStatus byId(int ordinal) { return STATUSES[ordinal]; }
}
