package fr.codinbox.npclib.core.impl.npc.equipment;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.equipment.NpcEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NpcEquipmentImpl implements NpcEquipment {

    private final @NotNull Npc npc;

    private @Nullable ItemStack helmet;
    private @Nullable ItemStack chestplate;
    private @Nullable ItemStack leggings;
    private @Nullable ItemStack boots;
    private @Nullable ItemStack mainHand;
    private @Nullable ItemStack offHand;

    public NpcEquipmentImpl(@NotNull Npc npc) {
        this.npc = npc;
    }

    @Override
    public @Nullable ItemStack getHelmet() {
        return this.helmet;
    }

    @Override
    public @Nullable ItemStack getChestplate() {
        return this.chestplate;
    }

    @Override
    public @Nullable ItemStack getLeggings() {
        return this.leggings;
    }

    @Override
    public @Nullable ItemStack getBoots() {
        return this.boots;
    }

    @Override
    public @Nullable ItemStack getMainHand() {
        return this.mainHand;
    }

    @Override
    public @Nullable ItemStack getOffHand() {
        return this.offHand;
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        if (helmet == null) {
            this.helmet = null;
            return;
        }
        this.helmet = helmet.clone();
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        if (chestplate == null) {
            this.chestplate = null;
            return;
        }
        this.chestplate = chestplate.clone();
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        if (leggings == null) {
            this.leggings = null;
            return;
        }
        this.leggings = leggings.clone();
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        if (boots == null) {
            this.boots = null;
            return;
        }
        this.boots = boots.clone();
    }

    @Override
    public void setMainHand(@Nullable ItemStack mainHand) {
        if (mainHand == null) {
            this.mainHand = null;
            return;
        }
        this.mainHand = mainHand.clone();
    }

    @Override
    public void setOffHand(@Nullable ItemStack offHand) {
        if (offHand == null) {
            this.offHand = null;
            return;
        }
        this.offHand = offHand.clone();
    }

    @Override
    public void setArmorContents(@NotNull ItemStack[] contents) {
        if (contents.length != 4) {
            throw new IllegalArgumentException("The contents array must have a length of 4");
        }
        this.helmet = contents[0];
        this.chestplate = contents[1];
        this.leggings = contents[2];
        this.boots = contents[3];
    }

}
