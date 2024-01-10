package skytanic.primordial.data;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import skytanic.primordial.Primordial;

import java.util.function.Supplier;

public class PrimordialDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Primordial.MOD_ID);

    public static final Supplier<AttachmentType<SensorData>> SENSOR_DATA =
            ATTACHMENT_TYPES.register("sensor_data", () -> AttachmentType.serializable(SensorData::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
