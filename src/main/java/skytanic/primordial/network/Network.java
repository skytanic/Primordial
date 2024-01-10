package skytanic.primordial.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import skytanic.primordial.ClientPayloadHandler;
import skytanic.primordial.Primordial;

public final class Network {
    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(Primordial.MOD_ID).versioned(PROTOCOL_VERSION);

        registrar.play(UpdateSensorStatusPacket.ID, UpdateSensorStatusPacket::new, handler -> handler
                .client(ClientPayloadHandler.getInstance()::handleUpdateSensorStatusPacket));
    }
}
