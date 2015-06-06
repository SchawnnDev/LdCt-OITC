/*
 * ******************************************************
 *  * Copyright (C) 2015 SchawnnDev <contact@schawnndev.fr>
 *  *
 *  * This file (fr.schawnndev.api.Achat) is part of LCCosmetiques.
 *  *
 *  * Created by SchawnnDev on 23/05/15 18:51.
 *  *
 *  * LCCosmetiques can not be copied and/or distributed without the express
 *  * permission of SchawnnDev.
 *  ******************************************************
 */

package fr.schawnndev.api;

import fr.lyneteam.lcmaster.LCMaster;
import fr.schawnndev.CosmetiqueManager;
import fr.schawnndev.CosmetiqueManager.*;
import fr.schawnndev.api.utils.GlassColor;
import fr.schawnndev.data.ItemStackManager;
import fr.schawnndev.gadgets.GadgetManager;
import fr.schawnndev.math.PositionConverter;
import fr.schawnndev.particules.ParticleManager;
import fr.schawnndev.particules.particules.ParticleMagicien;
import fr.schawnndev.sql.SQLManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Achat {

    @Getter
    private int price;

    @Getter
    private String id;

    @Getter
    private Cosmetique cosmetique;

    @Getter
    private Player player;

    @Getter
    private boolean generated;

    @Getter
    private boolean allowToBuy;

    @Getter
    private boolean isOpened;

    @Getter
    private boolean finishToPay;

    @Getter
    private String inventoryName;

    private Inventory inventory;

    public Achat(String id, Cosmetique cosmetique, Player player){
        this.id = id;
        this.price = cosmetique.getPrice();
        this.player = player;
        this.generated = false;
        this.isOpened = false;
        this.allowToBuy = true;
        this.inventoryName = "§6Achat: §c" + id + " §6| §c#" + new Random().nextInt(99);
        this.cosmetique = cosmetique;
        System.out.println("Nouvel achat de " + player.getName() + " | id: " + id + " | date: " + new Date().toLocaleString());
        Manager.achats.add(this);
    }

    public void generate(){
        inventory = Bukkit.createInventory(null, 5*9, inventoryName);

        PositionConverter converter = new PositionConverter();

        ItemStack glassBlueStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0, GlassColor.CYAN.getData());
        ItemMeta glassBlueMeta = glassBlueStack.getItemMeta();
        glassBlueMeta.setDisplayName("§7-");
        glassBlueStack.setItemMeta(glassBlueMeta);

        ItemStack glassCyanStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0, GlassColor.WHITE.getData());
        ItemMeta glassCyanMeta = glassCyanStack.getItemMeta();
        glassCyanMeta.setDisplayName("§7-");
        glassCyanStack.setItemMeta(glassCyanMeta);

        for(int i = 1; i <= 9; i++) {
            inventory.setItem(converter.convert(i, 1), glassBlueStack);
            inventory.setItem(converter.convert(i, 5), glassBlueStack);
        }

        for(int i = 4; i <= 6; i++) {
            inventory.setItem(converter.convert(i, 2), glassCyanStack);
            inventory.setItem(converter.convert(i, 4), glassCyanStack);
        }

        inventory.setItem(converter.convert(4, 3), glassCyanStack);
        inventory.setItem(converter.convert(6, 3), glassCyanStack);

        inventory.setItem(converter.convert(1, 2), glassBlueStack);
        inventory.setItem(converter.convert(1, 3), glassBlueStack);
        inventory.setItem(converter.convert(1, 4), glassBlueStack);

        inventory.setItem(converter.convert(9, 2), glassBlueStack);
        inventory.setItem(converter.convert(9, 3), glassBlueStack);
        inventory.setItem(converter.convert(9, 4), glassBlueStack);

        ItemStack woolGreen = new ItemStack(Material.WOOL, 1, (short) 0, (byte)5);
        ItemMeta woolGreenMeta = woolGreen.getItemMeta();
        woolGreenMeta.setDisplayName("§aValider");
        woolGreen.setItemMeta(woolGreenMeta);

        inventory.setItem(converter.convert(2, 2), woolGreen);
        inventory.setItem(converter.convert(3, 2), woolGreen);
        inventory.setItem(converter.convert(2, 3), woolGreen);
        inventory.setItem(converter.convert(3, 3), woolGreen);
        inventory.setItem(converter.convert(2, 4), woolGreen);
        inventory.setItem(converter.convert(3, 4), woolGreen);

        ItemStack woolRed = new ItemStack(Material.WOOL, 1, (short)0, (byte) 14);
        ItemMeta woolRedMeta = woolRed.getItemMeta();
        woolRedMeta.setDisplayName("§cAnnuler");
        woolRed.setItemMeta(woolRedMeta);

        inventory.setItem(converter.convert(7, 2), woolRed);
        inventory.setItem(converter.convert(8, 2), woolRed);
        inventory.setItem(converter.convert(7, 3), woolRed);
        inventory.setItem(converter.convert(8, 3), woolRed);
        inventory.setItem(converter.convert(7, 4), woolRed);
        inventory.setItem(converter.convert(8, 4), woolRed);

        ItemStack itemStack = new ItemStack(Material.CAKE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String cosmetiqueTypeString = cosmetique.getCosmetiqueType().toString().toLowerCase().substring(0, 1).toUpperCase() + cosmetique.getCosmetiqueType().toString().toLowerCase().substring(1);
        itemMeta.setDisplayName("§6" + cosmetiqueTypeString + ": " + id);
        itemMeta.setLore(Arrays.asList("§7------------","§bPrix: §6" + price + " §bLCoins"));
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(converter.convert(5, 3), itemStack);

        generated = true;
    }

    public void proceedOpening(){
        float money = LCMaster.api.getCoins(player.getName());

        if(money >= (float) price) {
            allowToBuy = true;
            open();
        } else {
            finish(true, false);
        }
    }

    public void open(){
        if(!isGenerated()) return;
        if(!isAllowToBuy()) return;

        player.openInventory(inventory);
        Manager.playersBuying.add(player.getUniqueId());

        isOpened = true;
    }

    public void buy(){
        if(!isOpened()) return;

        float money = LCMaster.api.getCoins(player.getName());

        if(money >= (float) price && !SQLManager.hasBuyCosmetic(player, id)) {

            SQLManager.addCosmetic(player, id);

            money -= price;

            LCMaster.api.setCoins(player.getName(), money);

            finish(false, false);
            player.sendMessage("§aTu viens d'acheter §b" + id);
            System.out.println("Achat confirme de " + player.getName() + " | id: " + id + " | date: " + new Date().toLocaleString());

            if(cosmetique.getCosmetiqueType() == CosmetiqueType.PARTICLE)
                ParticleManager.activeParticleByName(player, id);
            else if (cosmetique.getCosmetiqueType() == CosmetiqueType.GADGET){
                player.getInventory().setItem(4, ItemStackManager.getItemStack(cosmetique));
                GadgetManager.addGadget(player, id, false);
            }

        }

        finishToPay = true;
    }

    public void finish(boolean hasNotEnoughtCoins, boolean annuler){
        if(Manager.playersBuying.contains(player.getUniqueId()))
            Manager.playersBuying.remove(player.getUniqueId());

        if(Manager.achats.contains(this))
            Manager.achats.remove(this);

        player.closeInventory();

        if(annuler) {
            player.sendMessage("§cTu as annulé l'achat de " + id);
            System.out.println("Achat annule (annule par joueur) de " + player.getName() + " | id: " + id + " | date: " + new Date().toLocaleString());
        }

        if(hasNotEnoughtCoins) {
            player.sendMessage("§cTu n'as pas assez de LCCoins.");
            System.out.println("Achat annule (pas assez de coins) de " + player.getName() + " | id: " + id + " | date: " + new Date().toLocaleString());
        }
    }

}
