package pct.botanic.energistics.blocks;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import pct.botanic.energistics.BotanicEnergistics;
import pct.botanic.energistics.blocks.tile.TileAEDaisy;
import pct.botanic.energistics.references.CoreRefs;

public class AEPureDaisy extends BlockContainer {

    public AEPureDaisy(){
        super(Material.iron);
        setBlockName("aedaisy");
        setBlockTextureName(CoreRefs.MODID + ":aedaisy");
        setCreativeTab(BotanicEnergistics.botanicEnergisticsTab);
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileAEDaisy();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (world.isRemote) {
            return;
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileAEDaisy) {
            TileAEDaisy tileC = (TileAEDaisy) tile;
            tileC.getProxy().onReady();
            IGridNode node = tileC.getGridNode(ForgeDirection.UNKNOWN);
            if (entity != null && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                node.setPlayerID(AEApi.instance().registries().players().getID(player));
            }
            node.updateState();
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        if (world.isRemote)
            return;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileAEDaisy) {
                IGridNode node = ((TileAEDaisy) tile).getGridNode(ForgeDirection.UNKNOWN);
                if (node != null) {
                    node.destroy();
                }
            }
        }
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        //return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
        if (!world.isRemote){player.addChatComponentMessage(new ChatComponentText(String.valueOf(((TileAEDaisy)world.getTileEntity(x, y ,z)).getCurrentMana())));}
        return true;
    }

}
