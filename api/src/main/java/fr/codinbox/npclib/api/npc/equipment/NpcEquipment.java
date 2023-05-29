package fr.codinbox.npclib.api.npc.equipment;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the equipment of an NPC.
 */
public interface NpcEquipment {

    /**
     * Get the helmet of the NPC.
     * <br>
     * If the item is a null value, the helmet will be cleared.
     *
     * @return the helmet of the NPC
     */
    @Nullable ItemStack getHelmet();

    /**
     * Get the chestplate of the NPC.
     * <br>
     * If the item is a null value, the chestplate will be cleared.
     *
     * @return the chestplate of the NPC
     */
    @Nullable ItemStack getChestplate();

    /**
     * Get the leggings of the NPC.
     * <br>
     * If the item is a null value, the leggings will be cleared.
     *
     * @return the leggings of the NPC
     */
    @Nullable ItemStack getLeggings();

    /**
     * Get the boots of the NPC.
     * <br>
     * If the item is a null value, the boots will be cleared.
     *
     * @return the boots of the NPC
     */
    @Nullable ItemStack getBoots();

    /**
     * Get the item in the main hand of the NPC.
     * <br>
     * If the item is a null value, the main hand will be cleared.
     *
     * @return the item in the main hand of the NPC
     */
    @Nullable ItemStack getMainHand();

    /**
     * Get the item in the offhand of the NPC.
     * <br>
     * If the item is a null value, the offhand will be cleared.
     *
     * @return the item in the offhand of the NPC
     */
    @Nullable ItemStack getOffHand();

    /**
     * Set the helmet of the NPC.
     * <br>
     * If the item is a null value, the helmet will be cleared.
     *
     * @param helmet the helmet of the NPC
     */
    void setHelmet(@Nullable ItemStack helmet);

    /**
     * Set the chestplate of the NPC.
     * <br>
     * If the item is a null value, the chestplate will be cleared.
     *
     * @param chestplate the chestplate of the NPC
     */
    void setChestplate(@Nullable ItemStack chestplate);

    /**
     * Set the leggings of the NPC.
     * <br>
     * If the item is a null value, the leggings will be cleared.
     *
     * @param leggings the leggings of the NPC
     */
    void setLeggings(@Nullable ItemStack leggings);

    /**
     * Set the boots of the NPC.
     * <br>
     * If the item is a null value, the boots will be cleared.
     *
     * @param boots the boots of the NPC
     */
    void setBoots(@Nullable ItemStack boots);

    /**
     * Set the item in the main hand of the NPC.
     * <br>
     * If the item is a null value, the main hand will be cleared.
     *
     * @param mainHand the item in the main hand of the NPC
     */
    void setMainHand(@Nullable ItemStack mainHand);

    /**
     * Set the item in the offhand of the NPC.
     * <br>
     * If the item is a null value, the offhand will be cleared.
     *
     * @param offHand the item in the offhand of the NPC
     */
    void setOffHand(@Nullable ItemStack offHand);

    /**
     * Set the armor contents of the NPC.
     * <br>
     * The array must be of size 4, and the order must be:
     * <ol>
     *     <li>Helmet</li>
     *     <li>Chestplate</li>
     *     <li>Leggings</li>
     *     <li>Boots</li>
 *     </ol>
     * <br>
     * If the array is not of size 4, an {@link IllegalArgumentException} will be thrown.
     * <br>
     * If the array contains a null value, the corresponding armor slot will be cleared.
     *
     * @param contents the armor contents of the NPC
     */
    void setArmorContents(@NotNull ItemStack[] contents);

    /**
     * Set the item in the hand of the NPC.
     * <br>
     * If the item is a null value, the hand will be cleared.
     *
     * @param hand the hand to set the item in
     * @param item the item to set
     */
    default void setItemInHand(@NotNull Hand hand, @Nullable ItemStack item) {
        switch (hand) {
            case MAIN_HAND -> setMainHand(item);
            case OFF_HAND -> setOffHand(item);
        }
    }

    /**
     * The hand of the NPC.
     * <br>
     * A hand can be either the main hand or the offhand and can hold (or not) an item.
     */
    enum Hand {

        /**
         * The main hand of the NPC (the right hand).
         */
        MAIN_HAND,

        /**
         * The offhand of the NPC (the left hand).
         */
        OFF_HAND
    }

}
