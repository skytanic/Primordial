package skytanic.primordial.data;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import skytanic.primordial.SensorStatus;

public class SensorData implements INBTSerializable<CompoundTag> {
    private static final String ENABLED = "enabled";

    public SensorStatus status;
    public boolean enabled;
    public int activation_timer;
    public int cooldown;

    public SensorData() {
        this.status = SensorStatus.RETRACTED;
        this.enabled = false;
        this.activation_timer = 0;
        this.cooldown = 20;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean(ENABLED, enabled);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.enabled = nbt.getBoolean(ENABLED);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isReady() {
        return cooldown == 0;
    }

    public void toggle() {
        enabled = !enabled;
    }
}
