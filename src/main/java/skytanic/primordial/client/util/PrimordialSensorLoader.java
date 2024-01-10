package skytanic.primordial.client.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.*;
import skytanic.primordial.SensorStatus;
import skytanic.primordial.data.PrimordialDataAttachments;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class PrimordialSensorLoader implements IGeometryLoader<PrimordialSensorLoader.SensorGeometry>
{
    @Override
    public SensorGeometry read(JsonObject contents, JsonDeserializationContext ctx)
    {
        return new SensorGeometry(ctx.deserialize(contents.get("base_model"), BlockModel.class));
    }

    static class SensorGeometry implements IUnbakedGeometry<SensorGeometry>
    {
        private final BlockModel tabletModel;

        SensorGeometry(BlockModel tabletModel) { this.tabletModel = tabletModel; }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
        {
            BakedModel bakedTablet = tabletModel.bake(baker, tabletModel, spriteGetter, modelState, modelLocation, false);
            return new SensorOverrideModel(bakedTablet);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
        {
            tabletModel.resolveParents(modelGetter);
        }
    }

    private static class SensorOverrideModel extends BakedModelWrapper<BakedModel>
    {
        private final ItemOverrides overrideList;

        SensorOverrideModel(BakedModel originalModel)
        {
            super(originalModel);
            this.overrideList = new SensorOverrideList();
        }

        @Override
        public ItemOverrides getOverrides()
        {
            return overrideList;
        }
    }

    private static class SensorModel extends BakedModelWrapper<BakedModel>
    {
        private final List<BakedQuad> quads;

        SensorModel(BakedModel tabletModel, List<BakedQuad> quads)
        {
            super(tabletModel);
            this.quads = quads;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
        {
            if (side == null)
            {
                return quads;
            }
            return List.of();
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext ctx, PoseStack poseStack, boolean applyLeftHandTransform)
        {
            getTransforms().getTransform(ctx).apply(applyLeftHandTransform, poseStack);
            return this;
        }

        @Override
        public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous)
        {
            return List.of(this);
        }
    }

    private static class SensorOverrideList extends ItemOverrides
    {
        private static final RandomSource RANDOM = RandomSource.create();
        private final Map<SensorStatus, BakedModel> cachedModels = new EnumMap<>(SensorStatus.class);

        @Nullable
        @Override
        public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed)
        {
            SensorStatus status = stack.getData(PrimordialDataAttachments.SENSOR_DATA).status;

            return cachedModels.computeIfAbsent(status, mat ->
            {
                List<BakedQuad> quads = new ArrayList<>(model.getQuads(null, null, RANDOM, ModelData.EMPTY, null));

                BakedModel headModel = Minecraft.getInstance().getModelManager().getModel(mat.toModelLocation());
                quads.addAll(headModel.getQuads(null, null, RANDOM, ModelData.EMPTY, null));

                return new SensorModel(model, quads);
            });
        }
    }
}
