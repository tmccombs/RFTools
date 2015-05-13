package mcjty.rftools.blocks.storage.sorters;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;

public class NameItemSorter implements ItemSorter {
    @Override
    public String getName() {
        return "name";
    }

    @Override
    public String getTooltip() {
        return "Sort on name";
    }

    @Override
    public int getU() {
        return 12*16;
    }

    @Override
    public int getV() {
        return 16;
    }

    @Override
    public Comparator<Pair<ItemStack, Integer>> getComparator() {
        return new Comparator<Pair<ItemStack, Integer>>() {
            @Override
            public int compare(Pair<ItemStack, Integer> o1, Pair<ItemStack, Integer> o2) {
                String name1 = o1.getLeft().getDisplayName().toLowerCase();
                String name2 = o2.getLeft().getDisplayName().toLowerCase();
                return name1.compareTo(name2);
            }
        };
    }
}