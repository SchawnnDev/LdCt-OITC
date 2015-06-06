/*
 * ******************************************************
 *  * Copyright (C) 2015 SchawnnDev <contact@schawnndev.fr>
 *  *
 *  * This file (fr.schawnndev.CosmetiqueManager) is part of LCCosmetiques.
 *  *
 *  * Created by SchawnnDev on 20/05/15 18:41.
 *  *
 *  * LCCosmetiques can not be copied and/or distributed without the express
 *  * permission of SchawnnDev.
 *  ******************************************************
 */

package fr.schawnndev;

import fr.schawnndev.data.ItemStackManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CosmetiqueManager {

    public static void setCurrentCosmetique(Player player, Cosmetique cosmetique, boolean withMessage){
        player.getInventory().setItem(4, ItemStackManager.getItemStack(cosmetique));

        if(withMessage)
            player.sendMessage("§aTu viens d'activer §b"+ cosmetique.toString());
    }

    public static Cosmetique getCosmetiqueFromString(String cosmetique){
        for(Cosmetique c : Cosmetique.values())
            if(c.toString().equalsIgnoreCase(cosmetique))
                return c;
        return Cosmetique.AUCUN;
    }

    public static ItemStack getPlayerItem(Cosmetique cosmetique){
        return ItemStackManager.getPlayerItems().get(cosmetique);
    }

    public static boolean isParticle(String cosmetique){
        cosmetique.toUpperCase();

        for(Cosmetique c : Cosmetique.values())
            if(c.getCosmetiqueType() == CosmetiqueType.PARTICLE && c.toString().toUpperCase().equals(cosmetique))
                return true;

        return  false;
    }

    public enum CosmetiqueType {
        GADGET("current_active_gadget"),
        PET("current_active_pet"),
        PARTICLE("current_active_particle");

        @Getter
        private String MySQLTable;

        private CosmetiqueType(String MySQLTable){
            this.MySQLTable = MySQLTable;
        }

    }

    public enum Cosmetique {

        AUCUN(CosmetiqueType.GADGET, false, 0),

        // Gadgets

        DOUBLE_JUMP(CosmetiqueType.GADGET, true, 0),
        MUSIC(CosmetiqueType.GADGET, false, 1350),
        GATEAU_EMPOISONNE(CosmetiqueType.GADGET, false, 1400),
        LAISSE(CosmetiqueType.GADGET, true, 0),
        CANON(CosmetiqueType.GADGET, false, 2500),
        APPLE(CosmetiqueType.GADGET, true, 0),
        ENCRE(CosmetiqueType.GADGET, false, 2800),
        CANNE_A_PECHE(CosmetiqueType.GADGET, false, 1800),
        FIREBALL(CosmetiqueType.GADGET, true, 0),
        TNT(CosmetiqueType.GADGET, false, 1100),
        ARTIFICE(CosmetiqueType.GADGET, true, 0),

        // Particules

        MAGICIEN(CosmetiqueType.PARTICLE, false, 900),
        PLUIE(CosmetiqueType.PARTICLE, false, 1200),
        COEURS(CosmetiqueType.PARTICLE, false, 1500),
        LAVE(CosmetiqueType.PARTICLE, false, 600),
        CONTENT(CosmetiqueType.PARTICLE, false, 900),
        FUMEE(CosmetiqueType.PARTICLE, false, 1800),
        NOTES(CosmetiqueType.PARTICLE, false, 1600),
        FLAMES(CosmetiqueType.PARTICLE, false, 2000),
        SPIRALES(CosmetiqueType.PARTICLE, false, 1400),
        REDSTONE(CosmetiqueType.PARTICLE, false, 1000),
        LEGENDARY(CosmetiqueType.PARTICLE, false, 987654321),

        // Pets

        LOUP(CosmetiqueType.PET, true, 0),
        LAPIN(CosmetiqueType.PET, false, 1400),
        MOUTON(CosmetiqueType.PET, false, 800),
        POULET(CosmetiqueType.PET, true, 0),
        ZOMBIE(CosmetiqueType.PET, true, 0),
        CREEPER(CosmetiqueType.PET, false, 1200),
        SQUELETTE(CosmetiqueType.PET, false, 1500),
        VACHE(CosmetiqueType.PET, false, 600),
        PIGMAN(CosmetiqueType.PET, false, 1400),
        CHEVAL(CosmetiqueType.PET, false, 2600),
        VACHE_CHAMPIGNON(CosmetiqueType.PET, true, 0);

        @Getter
        private CosmetiqueType cosmetiqueType;

        @Getter
        private boolean vip;

        @Getter
        private int price;

        private Cosmetique(CosmetiqueType cosmetiqueType, boolean vip, int price){
            this.cosmetiqueType = cosmetiqueType;
            this.vip=vip;
            this.price=price;
        }

    }


}
