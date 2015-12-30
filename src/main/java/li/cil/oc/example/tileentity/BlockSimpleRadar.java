package li.cil.oc.example.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSimpleRadar extends Block {
    public BlockSimpleRadar() {
        super(Material.anvil);
        setCreativeTab(CreativeTabs.tabAllSearch);
        setUnlocalizedName("SimpleRadar");
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(final World world, final IBlockState state) {
        return new TileEntitySimpleRadar();
    }
}
