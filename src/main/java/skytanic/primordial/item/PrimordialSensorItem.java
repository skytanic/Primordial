package skytanic.primordial.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.network.PacketDistributor;
import skytanic.primordial.ResonatingType;
import skytanic.primordial.SensorStatus;
import skytanic.primordial.data.PrimordialDataAttachments;
import skytanic.primordial.data.SensorData;
import skytanic.primordial.network.UpdateSensorStatusPacket;
import skytanic.primordial.sound.PrimordialSounds;

public class PrimordialSensorItem extends Item {
    private static final String ANCIENT_CITY_STRUCTURE = "minecraft:ancient_city";

    public PrimordialSensorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) {
            SensorData sensor = stack.getData(PrimordialDataAttachments.SENSOR_DATA);
            if (sensor.isEnabled()) {
                if (sensor.isReady()) {
                    ResonatingType resonatingType = ResonatingType.getByDistance(getDistanceToTarget(level, entity));
                    sensor.cooldown = resonatingType.cooldown_length;
                    sensor.activation_timer = resonatingType.activation_length;
                    if (resonatingType != ResonatingType.SILENT) {
                        updateSensorStatus(stack, sensor, SensorStatus.RESONATING, (ServerPlayer) entity);
                        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                PrimordialSounds.PRIMORDIAL_SENSOR_RESONATING.get(), SoundSource.BLOCKS, 0.5f,
                                resonatingType.pitch);
                    }
                } else {
                    if (sensor.activation_timer == 0) {
                        updateSensorStatus(stack, sensor, SensorStatus.DORMANT, (ServerPlayer) entity);
                    }
                    sensor.activation_timer -= 1;
                    sensor.cooldown -= 1;
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            SensorData sensor = stack.getData(PrimordialDataAttachments.SENSOR_DATA);
            sensor.toggle();
            if (sensor.isEnabled()) {
                updateSensorStatus(stack, sensor, SensorStatus.DORMANT, (ServerPlayer) player);
                sensor.cooldown = 5;
            } else {
                updateSensorStatus(stack, sensor, SensorStatus.RETRACTED, (ServerPlayer) player);
            }
        }
//        if (level.isClientSide()) {
//            if (this.targetPos != null) {
//                double xDiff = this.targetPos.getX() - player.getX();
//                double yDiff = this.targetPos.getY() - player.getY();
//                double zDiff = this.targetPos.getZ() - player.getZ();
//                double x = xDiff / (Math.abs(xDiff) + Math.abs(yDiff) + Math.abs(zDiff));
//                double y = yDiff / (Math.abs(xDiff) + Math.abs(yDiff) + Math.abs(zDiff));
//                double z = zDiff / (Math.abs(xDiff) + Math.abs(yDiff) + Math.abs(zDiff));
//                level.addParticle(ParticleTypes.VIBRATION, player.getX(), player.getY(), player.getZ(), x * 0.25, y * 0.005, z * 0.25);
//            }
//        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private int getDistanceToTarget(Level level, Entity entity) {
        ResourceLocation structureLocation = ResourceLocation.tryParse(ANCIENT_CITY_STRUCTURE);
        if (structureLocation != null) {
            Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
            ResourceKey<Structure> structureKey = ResourceKey.create(Registries.STRUCTURE, structureLocation);
            HolderSet<Structure> featureHolderSet = structureRegistry
                    .getHolder(structureKey)
                    .map(HolderSet::direct)
                    .orElse(null);

            if (featureHolderSet != null) {
                BlockPos playerPosition = entity.blockPosition();
                ServerLevel serverLevel = (ServerLevel) level;
                ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();
                Pair<BlockPos, Holder<Structure>> pair = generator.findNearestMapStructure(serverLevel,
                        featureHolderSet, playerPosition, 100, false);
                BlockPos structurePos = pair != null ? pair.getFirst() : null;

                if (structurePos != null) {
                    return playerPosition.distManhattan(structurePos);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private void updateSensorStatus(ItemStack stack, SensorData sensor, SensorStatus newStatus, ServerPlayer player) {
        if (sensor.status != newStatus) {
            sensor.status = newStatus;
            new UpdateSensorStatusPacket(stack, newStatus.ordinal());
            PacketDistributor.PLAYER.with(player).send(new UpdateSensorStatusPacket(stack, newStatus.ordinal()));
        }
    }
}
