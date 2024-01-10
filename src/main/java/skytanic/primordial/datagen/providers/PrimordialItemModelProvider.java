package skytanic.primordial.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import skytanic.primordial.Primordial;
import skytanic.primordial.item.PrimordialItems;

public class PrimordialItemModelProvider extends ItemModelProvider {
    public PrimordialItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Primordial.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(PrimordialItems.PRIMORDIAL_SENSOR.get());
    }
}
