package li.cil.oc.example.tileentity;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityRadar extends TileEntityEnvironment implements ITickable {
    public static final double EnergyCostPerTick = 0.5;

    public static final double RadarRange = 32;

    protected boolean isEnabled = true;

    protected boolean hasEnergy;

    public TileEntityRadar() {
        // The 'node' of a tile entity is used to connect it to other components
        // including computers. They are connected to nodes of neighboring
        // blocks, forming a network that way. That network is also used for
        // distributing energy among components for the mod.
        node = Network.newNode(this, Visibility.Network).
                withConnector().
                withComponent("radar").
                create();
    }

    @Override
    public void update() {
        // Nodes are only created on the server side, so we have to check.
        if (node != null && isEnabled) {
            // Consume some energy per tick to keep the radar running!
            hasEnergy = ((Connector) node).tryChangeBuffer(-EnergyCostPerTick);
        }
    }

    // The following methods will be callable from Lua due to the Callback
    // annotation. Methods in an environment specified as the owner of a
    // component node are searched for this annotation. Note that methods
    // annotated with the Callback annotation must have the exact signature the
    // following methods have. The returned array is treated as a 'tuple' when
    // pushed to the computer, i.e. as multiple returned values.

    @Callback
    public Object[] isEnabled(Context context, Arguments args) {
        return new Object[]{isEnabled};
    }

    @Callback
    public Object[] setEnabled(Context context, Arguments args) {
        isEnabled = args.checkBoolean(0);
        return new Object[]{isEnabled};
    }

    @Callback
    public Object[] getEntities(Context context, Arguments args) {
        List<Map> entities = new ArrayList<Map>();
        if (isEnabled) {
            // Get a initial list of entities near the tile entity.
            AxisAlignedBB bounds = AxisAlignedBB.
                    fromBounds(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1).
                    expand(RadarRange, RadarRange, RadarRange);
            for (Object obj : getWorld().getEntitiesWithinAABB(EntityLiving.class, bounds)) {
                EntityLiving entity = (EntityLiving) obj;
                double dx = entity.posX - (getPos().getX() + 0.5);
                double dz = entity.posZ - (getPos().getZ() + 0.5);
                // Check if the entity is actually in range.
                if (Math.sqrt(dx * dx + dz * dz) < RadarRange) {
                    // Maps are converted to tables on the Lua side.
                    Map<String, Object> entry = new HashMap<String, Object>();
                    if (entity.hasCustomName()) {
                        entry.put("name", entity.getCustomNameTag());
                    } else {
                        entry.put("name", entity.getName());
                    }
                    entry.put("x", (int) dx);
                    entry.put("z", (int) dz);
                    entities.add(entry);
                }
            }

            // Force the computer that made the call to sleep for a bit, to
            // avoid calling this method excessively (since it could be quite
            // expensive). The time is specified in seconds.
            context.pause(0.5);
        }

        // The returned array is treated as a tuple, meaning if we return the
        // entities as an array directly, we'd end up with each entity as an
        // individual result value (i.e. in Lua we'd have to write
        //   result = {radar.getEntities()}
        // and we'd be limited in the number of entities, due to the limit of
        // return values. So we wrap it in an array to return it as a list.
        return new Object[]{entities.toArray()};
    }
}
