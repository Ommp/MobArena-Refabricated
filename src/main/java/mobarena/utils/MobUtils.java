package mobarena.utils;

import mobarena.Wave.WaveType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class MobUtils {

    //idea for randomisation
    //create arrays with equipment based on the level or difficulty
    //for example, if the level is 30, create arraylists of helmets,chests,legs,feet containing leather, golden, chain
    //for example, if the level is 40, create arraylists of helmets,chests,legs,feet containing leather, golden, chain, iron, diamond
    //pick random elements from these arraylists and add the corresponding items to the mob

    private MobUtils() {}

    private enum WaveDifficulty {
        EASY, MEDIUM, HARD, INSANE
    }

    private static int calculateLevel(int wave) {

        var random = new Random().nextInt((100 - 1) + 1) + 1;

        if (random >= 50) {
            if (wave >= 1 && wave <= 10) {
                return 10;
            }
            else if (wave >= 11 && wave <= 20) {
                return 20;
            }
            else if (wave >= 21 && wave <= 30) {
                return 30;
            }
            else if (wave >= 31 && wave <= 40) {
                return 40;
            }
            else if (wave >= 41 && wave <= 50) {
                return 50;
            }
        }

        return 0;

    }

    private static void createEquipment(int level, MobEntity entity) {
        if (level == 10) {
            var head = Items.LEATHER_HELMET;
            var chest = Items.LEATHER_CHESTPLATE;
            var legs = Items.LEATHER_LEGGINGS;
            var feet = Items.LEATHER_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }
        else if (level == 20) {
            var head = Items.GOLDEN_HELMET;
            var chest = Items.GOLDEN_CHESTPLATE;
            var legs = Items.GOLDEN_LEGGINGS;
            var feet = Items.GOLDEN_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }
        else if (level == 30) {
            var head = Items.CHAINMAIL_HELMET;
            var chest = Items.CHAINMAIL_CHESTPLATE;
            var legs = Items.CHAINMAIL_LEGGINGS;
            var feet = Items.CHAINMAIL_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }
        else if (level == 40) {
            var head = Items.IRON_HELMET;
            var chest = Items.IRON_CHESTPLATE;
            var legs = Items.IRON_LEGGINGS;
            var feet = Items.IRON_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }
        else if (level == 50) {
            var head = Items.DIAMOND_HELMET;
            var chest = Items.DIAMOND_CHESTPLATE;
            var legs = Items.DIAMOND_LEGGINGS;
            var feet = Items.DIAMOND_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }

        else if (level == 60) {
            var head = Items.NETHERITE_HELMET;
            var chest = Items.NETHERITE_CHESTPLATE;
            var legs = Items.NETHERITE_LEGGINGS;
            var feet = Items.NETHERITE_BOOTS;

            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head));
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest));
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs));
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet));
        }

    }

    public static void addEquipment(int wave, WaveType waveType, List<MobEntity> monsters) {
        for (var mob: monsters) {
            int level = calculateLevel(wave);
            createEquipment(level, mob);
        }
    }

    @Nullable
    public static Item getEquipmentForSlot(EquipmentSlot equipmentSlot, int equipmentLevel) {
        switch (equipmentSlot) {
            case HEAD: {
                if (equipmentLevel == 0) {
                    return Items.LEATHER_HELMET;
                }
                if (equipmentLevel == 1) {
                    return Items.GOLDEN_HELMET;
                }
                if (equipmentLevel == 2) {
                    return Items.CHAINMAIL_HELMET;
                }
                if (equipmentLevel == 3) {
                    return Items.IRON_HELMET;
                }
                if (equipmentLevel == 4) {
                    return Items.DIAMOND_HELMET;
                }
            }
            case CHEST: {
                if (equipmentLevel == 0) {
                    return Items.LEATHER_CHESTPLATE;
                }
                if (equipmentLevel == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                }
                if (equipmentLevel == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                if (equipmentLevel == 3) {
                    return Items.IRON_CHESTPLATE;
                }
                if (equipmentLevel == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            }
            case LEGS: {
                if (equipmentLevel == 0) {
                    return Items.LEATHER_LEGGINGS;
                }
                if (equipmentLevel == 1) {
                    return Items.GOLDEN_LEGGINGS;
                }
                if (equipmentLevel == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                if (equipmentLevel == 3) {
                    return Items.IRON_LEGGINGS;
                }
                if (equipmentLevel == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            }
            case FEET: {
                if (equipmentLevel == 0) {
                    return Items.LEATHER_BOOTS;
                }
                if (equipmentLevel == 1) {
                    return Items.GOLDEN_BOOTS;
                }
                if (equipmentLevel == 2) {
                    return Items.CHAINMAIL_BOOTS;
                }
                if (equipmentLevel == 3) {
                    return Items.IRON_BOOTS;
                }
                if (equipmentLevel != 4) break;
                return Items.DIAMOND_BOOTS;
            }
        }
        return null;
    }

}
