package skytanic.primordial.util;

import net.minecraft.resources.ResourceLocation;
import skytanic.primordial.Primordial;

public class PrimordialUtils {
    public static ResourceLocation asLocation(String name) {
        return new ResourceLocation(Primordial.MOD_ID, name);
    }
}
