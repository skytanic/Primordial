package skytanic.primordial;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import skytanic.primordial.item.PrimordialItems;

import java.util.function.Supplier;

public class PrimordialCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Primordial.MOD_ID);

    public static final Supplier<CreativeModeTab> PRIMORDIAL_TAB = CREATIVE_MODE_TABS.register("primordial_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.primordial"))
                    .icon(() -> new ItemStack(PrimordialItems.PRIMORDIAL_SENSOR.get()))
                    .displayItems((params, output) -> {
                        output.accept(PrimordialItems.PRIMORDIAL_SENSOR.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
