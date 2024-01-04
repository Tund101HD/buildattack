package me.tund.utils.gui;

;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SellOrder {
    private static AtomicLong idCounter = new AtomicLong();
    private Player name;
    private UUID owner;
    private ItemStack item;
    private float price;
    private String ID;
    private int amount;
    private float pricePer;

    public SellOrder(Player Name, UUID Owner, ItemStack Item, float Price, int Amount) {
        this.name = Name;
        this.owner = Owner;
        this.item = Item;
        this.price = Price;
        this.ID = createID() + "/" + Price;
        this.amount = Amount;
        this.pricePer = Price / Amount;
    }


    public SellOrder(Player Name, UUID Owner, ItemStack Item, float Price, String id, int Amount) {
        this.name = Name;
        this.owner = Owner;
        this.item = Item;
        this.price = Price;
        this.ID = id;
        this.amount = Amount;
        this.pricePer = Price / Amount;
    }

    private static String createID() {
        return String.valueOf(idCounter.getAndIncrement());
    }

    public String getOwnerName() {
        return this.name.getName();
    }

    public Player getOwnerPlayer() {
        return this.name;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public float getPrice() {
        return this.price;
    }

    public String getID() {
        return this.ID;
    }

    public float getPricePer() {
        return this.pricePer;
    }

    public int getAmount() {
        return this.amount;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Owner", getOwnerName());
        map.put("UUID", getOwner().toString());
        map.put("Item", getStackName(item));
        map.put("Amount", String.valueOf(this.amount));
        map.put("Price", String.valueOf(price));

        map.put("ID", ID);


        return map;
    }

    private String getStackName(ItemStack itemstack) {
        switch (itemstack.getType()) {
            case DIAMOND:
                return "Diamond";
            case GOLD_INGOT:
                return "Gold";
            case NETHERITE_INGOT:
                return "Netherite";
            case EMERALD:
                return "Emerald";
            case GHAST_TEAR:
                return "GhastTear";
            case GUNPOWDER:
                return "GunPowder";
        }

        return "MISSING";
    }
}
