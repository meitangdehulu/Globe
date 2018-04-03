package me.ichun.mods.globe.common.block;

import me.ichun.mods.globe.common.Globe;
import me.ichun.mods.globe.common.tileentity.TileEntityGlobeStand;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.util.EnumFacing.Axis.X;
import static net.minecraft.util.EnumFacing.Axis.Z;

public class BlockGlobeStand extends Block implements ITileEntityProvider
{
    public static final AxisAlignedBB GLOBE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
    public static final AxisAlignedBB STAND_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.125D, 0.8125D);
    public static final AxisAlignedBB STAND_AND_GLOBE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);

    public BlockGlobeStand()
    {
        super(Material.IRON);
        setRegistryName(new ResourceLocation("globe", "globe_stand"));
        setUnlocalizedName("globe.block.globeStand");
        setCreativeTab(CreativeTabs.DECORATIONS); //TODO emit light
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityGlobeStand(null, true);
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityGlobeStand)
        {
            TileEntityGlobeStand gs = (TileEntityGlobeStand)te;
            if(!gs.isStand)
            {
                return GLOBE_AABB;
            }
            else if(gs.itemTag == null)
            {
                return STAND_AABB;
            }
            else
            {
                return STAND_AND_GLOBE_AABB;
            }
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityGlobeStand)
        {
            TileEntityGlobeStand gs = (TileEntityGlobeStand)te;
            if(!gs.isStand)
            {
                return GLOBE_AABB;
            }
            else if(gs.itemTag == null)
            {
                return STAND_AABB;
            }
            else
            {
                return STAND_AND_GLOBE_AABB;
            }
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityGlobeStand)
        {
            TileEntityGlobeStand gs = (TileEntityGlobeStand)te; //TODO snow?
            if(gs.isStand && gs.itemTag != null) // interact!
            {
                gs.bobAmp = (playerIn.rotationPitch < 0 ? -10F : 10F) + playerIn.rotationPitch / 10F;

                float x = -MathHelper.sin(playerIn.rotationYaw * 0.017453292F) * MathHelper.cos(playerIn.rotationPitch * 0.017453292F);
                float y = -MathHelper.sin((playerIn.rotationPitch) * 0.017453292F);
                float z = MathHelper.cos(playerIn.rotationYaw * 0.017453292F) * MathHelper.cos(playerIn.rotationPitch * 0.017453292F);
                float f = MathHelper.sqrt(x * x + y * y + z * z);
                x = x / f * 0.2F;
                z = z / f * 0.2F;

                gs.rubberbandX = x;
                gs.rubberbandZ = z;

                if(facing.getAxis() == Z)
                {
                    float rot = (float)(hitX - 0.5D) * facing.getFrontOffsetZ() * 10F;
                    if(rot < 0 && gs.rotateFactor > 0 || rot > 0 && gs.rotateFactor < 0)
                    {
                        gs.rotateFactor *= 0.7F;
                    }
                    gs.rotateFactor += rot;
                }
                else if(facing.getAxis() == X)
                {
                    float rot = (float)(hitZ - 0.5D) * -facing.getFrontOffsetX() * 10F;
                    if(rot < 0 && gs.rotateFactor > 0 || rot > 0 && gs.rotateFactor < 0)
                    {
                        gs.rotateFactor *= 0.7F;
                    }
                    gs.rotateFactor += rot;
                }
                if(Math.abs(gs.rotateFactor) > 30F)
                {
                    gs.rotateFactor = gs.rotateFactor < 0 ? -30F : 30F;
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if(!canPlaceBlockAt(worldIn, pos))
        {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityGlobeStand)
        {
            TileEntityGlobeStand gs = (TileEntityGlobeStand)te;
            if(gs.isStand)
            {
                drops.add(new ItemStack(Globe.blockGlobeStand, 1));
            }
            if(gs.itemTag != null)
            {
                ItemStack is = new ItemStack(Globe.itemGlobe, 1, 1);
                is.setTagCompound(gs.itemTag);
                drops.add(is);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLASS;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}
