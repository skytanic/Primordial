package skytanic.primordial.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import skytanic.primordial.util.PrimordialUtils;

public record UpdateSensorStatusPacket(ItemStack sensor, int statusId) implements CustomPacketPayload {
    public static final ResourceLocation ID = PrimordialUtils.asLocation("sensor_status");

    public UpdateSensorStatusPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(this.sensor);
        buffer.writeInt(this.statusId);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
