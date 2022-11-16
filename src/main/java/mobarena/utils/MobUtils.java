package mobarena.utils;

import mobarena.Wave.WaveType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobUtils {

    private MobUtils() {}

    private enum WaveDifficulty {
        EASY, MEDIUM, HARD, INSANE
    }

    private static boolean getsEquipment(WaveType waveType, int wave) {
        if (waveType.equals(WaveType.BOSS)) {
            return true;
        }

        var random = new Random().nextInt((100 - 1) + 1) + 1;
        return random >= 80-(wave/2);

    }

    private static boolean getsEnchantments(WaveType waveType, int wave) {
        if (waveType.equals(WaveType.BOSS)) {
            return true;
        }

        var random = new Random().nextInt((100 - 1) + 1) + 1;
        return random >= 90-(wave/2);

    }

    private static void createEquipment(int wave, WaveType waveType, MobEntity entity) {

        ArrayList<Item> head = new ArrayList<>();
        ArrayList<Item> chest = new ArrayList<>();
        ArrayList<Item> legs = new ArrayList<>();
        ArrayList<Item> feet = new ArrayList<>();
        ArrayList<Item> hand = new ArrayList<>();

        if (!waveType.equals(WaveType.BOSS)) {
            head.add(Items.AIR);
            chest.add(Items.AIR);
            legs.add(Items.AIR);
            feet.add(Items.AIR);
            hand.add(Items.AIR);
        }

        if (wave >= 0) {
            head.add(Items.LEATHER_HELMET);
            chest.add(Items.LEATHER_CHESTPLATE);
            legs.add(Items.LEATHER_LEGGINGS);
            feet.add(Items.LEATHER_BOOTS);
            hand.add(Items.WOODEN_SWORD);
        }
        if (wave >= 5) {
            head.add(Items.GOLDEN_HELMET);
            chest.add(Items.GOLDEN_CHESTPLATE);
            legs.add(Items.GOLDEN_LEGGINGS);
            feet.add(Items.GOLDEN_BOOTS);
            hand.add(Items.STONE_SWORD);
        }
        if (wave >= 10) {
            head.add(Items.CHAINMAIL_HELMET);
            chest.add(Items.CHAINMAIL_CHESTPLATE);
            legs.add(Items.CHAINMAIL_LEGGINGS);
            feet.add(Items.CHAINMAIL_BOOTS);
            hand.add(Items.GOLDEN_SWORD);
        }
        if (wave >= 15) {
            head.add(Items.IRON_HELMET);
            chest.add(Items.IRON_CHESTPLATE);
            legs.add(Items.IRON_LEGGINGS);
            feet.add(Items.IRON_BOOTS);
            hand.add(Items.IRON_SWORD);
        }
        if (wave >= 20) {
            head.add(Items.DIAMOND_HELMET);
            chest.add(Items.DIAMOND_CHESTPLATE);
            legs.add(Items.DIAMOND_LEGGINGS);
            feet.add(Items.DIAMOND_BOOTS);
            hand.add(Items.DIAMOND_SWORD);
        }

        if (wave >= 25) {
            head.add(Items.NETHERITE_HELMET);
            chest.add(Items.NETHERITE_CHESTPLATE);
            legs.add(Items.NETHERITE_LEGGINGS);
            feet.add(Items.NETHERITE_BOOTS);
            hand.add(Items.NETHERITE_SWORD);
        }

        var pickedHead = new Random().nextInt(head.size());
        var pickedChest = new Random().nextInt(chest.size());
        var pickedLegs = new Random().nextInt(legs.size());
        var pickedFeet = new Random().nextInt(feet.size());
        var pickedHand = new Random().nextInt(hand.size());

        entity.equipStack(EquipmentSlot.HEAD, new ItemStack(head.get(pickedHead)));
        entity.equipStack(EquipmentSlot.CHEST, new ItemStack(chest.get(pickedChest)));
        entity.equipStack(EquipmentSlot.LEGS, new ItemStack(legs.get(pickedLegs)));
        entity.equipStack(EquipmentSlot.FEET, new ItemStack(feet.get(pickedFeet)));

        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);

        if (itemStack.isEmpty()) {
            entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(hand.get(pickedHand)));
        }
    }

    public static void createEnchantments(WaveType waveType, MobEntity entity, int wave) {
        ArrayList<Enchantment> armour = new ArrayList<>(Arrays.asList(Enchantments.PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.THORNS));

        for (int i = 0; i < 4; i++) {
            if (getsEnchantments(waveType, wave)) {
                if (i==0) {
                    var randomLevel = new Random().nextInt((4 - 1) + 1) + 1;
                    var randomArmourEnchantment =  new Random().nextInt(armour.size());
                    entity.getEquippedStack(EquipmentSlot.HEAD).addEnchantment(armour.get(randomArmourEnchantment), randomLevel);
                }
                else if (i==1) {
                    var randomLevel = new Random().nextInt((4 - 1) + 1) + 1;
                    var randomArmourEnchantment =  new Random().nextInt(armour.size());
                    entity.getEquippedStack(EquipmentSlot.CHEST).addEnchantment(armour.get(randomArmourEnchantment), randomLevel);
                }
                else if (i==2) {
                    var randomLevel = new Random().nextInt((4 - 1) + 1) + 1;
                    var randomArmourEnchantment =  new Random().nextInt(armour.size());
                    entity.getEquippedStack(EquipmentSlot.LEGS).addEnchantment(armour.get(randomArmourEnchantment), randomLevel);
                }
                else {
                    var randomLevel = new Random().nextInt((4 - 1) + 1) + 1;
                    var randomArmourEnchantment =  new Random().nextInt(armour.size());
                    entity.getEquippedStack(EquipmentSlot.FEET).addEnchantment(armour.get(randomArmourEnchantment), randomLevel);
                }
            }
        }





    }

    public static void addEquipment(int wave, WaveType waveType, List<MobEntity> monsters) {
        for (var mob: monsters) {
            if (getsEquipment(waveType, wave)) {
            createEquipment(wave, waveType, mob);
            }
            createEnchantments(waveType, mob, wave);
        }
    }

}
