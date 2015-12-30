package li.cil.oc.example.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRadar extends Block {
    public BlockRadar() {
        super(Material.anvil);
        setCreativeTab(CreativeTabs.tabAllSearch);
        setUnlocalizedName("Radar");
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(final World world, final IBlockState state) {
        return new TileEntityRadar();
    }
}
