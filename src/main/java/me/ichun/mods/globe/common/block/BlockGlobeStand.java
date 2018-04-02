package me.ichun.mods.globe.common.block;

import me.ichun.mods.globe.common.tileentity.TileEntityGlobeStand;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGlobeStand extends Block implements ITileEntityProvider
{
    public BlockGlobeStand()
    {
        super(Material.IRON);
        setRegistryName(new ResourceLocation("globe", "globe_stand"));
        setUnlocalizedName("globe.block.globeStand");
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityGlobeStand();
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
