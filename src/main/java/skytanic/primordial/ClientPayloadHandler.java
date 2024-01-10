package skytanic.primordial;

import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import skytanic.primordial.data.PrimordialDataAttachments;
import skytanic.primordial.data.SensorData;
import skytanic.primordial.network.UpdateSensorStatusPacket;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleUpdateSensorStatusPacket(UpdateSensorStatusPacket updateSensorStatusPacket, PlayPayloadContext playPayloadContext) {
        SensorData sensorData = updateSensorStatusPacket.sensor().getData(PrimordialDataAttachments.SENSOR_DATA);
        sensorData.status = SensorStatus.byId(updateSensorStatusPacket.statusId());
    }
}
