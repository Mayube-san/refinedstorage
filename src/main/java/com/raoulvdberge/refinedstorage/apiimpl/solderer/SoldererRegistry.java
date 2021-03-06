package com.raoulvdberge.refinedstorage.apiimpl.solderer;

import com.raoulvdberge.refinedstorage.api.solderer.ISoldererRecipe;
import com.raoulvdberge.refinedstorage.api.solderer.ISoldererRegistry;
import com.raoulvdberge.refinedstorage.api.util.IComparer;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SoldererRegistry implements ISoldererRegistry {
    private List<ISoldererRecipe> recipes = new ArrayList<>();

    @Override
    public void addRecipe(@Nonnull ISoldererRecipe recipe) {
        recipes.add(recipe);
    }

    @Override
    @Nullable
    public ISoldererRecipe getRecipe(@Nonnull IItemHandler ingredients) {
        for (ISoldererRecipe recipe : recipes) {
            int rowsFound = 0;

            for (int i = 0; i < 3; ++i) {
                NonNullList<ItemStack> possibilities = recipe.getRow(i);

                if (possibilities.isEmpty() && ingredients.getStackInSlot(i).isEmpty()) {
                    rowsFound++;

                    continue;
                }

                for (ItemStack possibility : possibilities) {
                    if (API.instance().getComparer().isEqual(possibility, ingredients.getStackInSlot(i), IComparer.COMPARE_NBT | IComparer.COMPARE_DAMAGE | IComparer.COMPARE_STRIP_NBT)) {
                        if (ingredients.getStackInSlot(i).getCount() >= possibility.getCount()) {
                            rowsFound++;
                        }
                    }
                }
            }

            if (rowsFound == 3) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    public List<ISoldererRecipe> getRecipes() {
        return recipes;
    }

    @Nonnull
    @Override
    public ISoldererRecipe createSimpleRecipe(@Nonnull ItemStack result, int duration, NonNullList<ItemStack>... rows) {
        if (rows.length != 3) {
            throw new IllegalArgumentException("Solderer recipe expects 3 rows, got " + rows.length + " rows");
        }

        return new ISoldererRecipe() {
            @Nonnull
            @Override
            public NonNullList<ItemStack> getRow(int row) {
                return rows[row];
            }

            @Nonnull
            @Override
            public ItemStack getResult() {
                return result;
            }

            @Override
            public int getDuration() {
                return duration;
            }
        };
    }
}
