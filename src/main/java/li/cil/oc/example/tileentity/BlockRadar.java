package li.cil.oc.example.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRadar extends Block {
    public BlockRadar(int blockId) {
        super(blockId, Material.anvil);
        setCreativeTab(CreativeTabs.tabAllSearch);
        setUnlocalizedName("Radar");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityRadar();
    }
}
