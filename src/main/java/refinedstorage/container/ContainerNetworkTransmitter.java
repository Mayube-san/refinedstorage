package refinedstorage.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import refinedstorage.tile.TileNetworkTransmitter;

public class ContainerNetworkTransmitter extends ContainerBase {
    public ContainerNetworkTransmitter(EntityPlayer player, TileNetworkTransmitter networkTransmitter) {
        super(player);

        addSlotToContainer(new SlotItemHandler(networkTransmitter.getNetworkCard(), 0, 8, 20));

        addPlayerInventory(8, 55);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = null;

        Slot slot = getSlot(index);

        if (slot != null && slot.getHasStack()) {
            stack = slot.getStack();

            if (index == 0) {
                if (!mergeItemStack(stack, 1, inventorySlots.size(), false)) {
                    return null;
                }
            } else if (!mergeItemStack(stack, 0, 1, false)) {
                return null;
            }

            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return stack;
    }
}
