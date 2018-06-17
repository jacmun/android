package com.example.jackiemun1.shoppinglist.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.jackiemun1.shoppinglist.R;

import java.io.Serializable;

@Entity
public class Item implements Serializable {

    public enum ItemType {
        FOOD(0, R.drawable.food), CLOTHES(1, R.drawable.clothes),
        ELECTRONICS(2, R.drawable.electronics);

        private int value;
        private int iconId;

        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType p : ItemType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return FOOD;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private long itemID;

    private String itemName;
    private String description;
    private double itemPrice;
    private boolean itemStatus;
    private int itemType;

    public Item(String itemName, String description, double itemPrice, boolean itemStatus, int itemType) {
        this.itemName = itemName;
        this.description = description;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.itemType = itemType;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getItemType() {return itemType; }

    public void setItemType(int itemType) {this.itemType = itemType; }

    public ItemType getItemTypeAsEnum() { return ItemType.fromInt(itemType);}
}
