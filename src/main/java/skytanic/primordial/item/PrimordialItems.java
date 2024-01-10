package skytanic.primordial.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import skytanic.primordial.Primordial;

public class PrimordialItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Primordial.MOD_ID);

    public static final DeferredItem<Item> PRIMORDIAL_SENSOR = ITEMS.register("primordial_sensor",
            () -> new PrimordialSensorItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
