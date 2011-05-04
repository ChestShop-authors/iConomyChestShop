package com.Acrobot.iConomyChestShop;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Acrobot
 */
@Entity()
@Table(name = "ics_transactions")
public class Transaction {

    @Id
    private int id;
    @NotNull
    private boolean buy;
    @NotEmpty
    private String shopOwner;
    @NotEmpty
    private String shopUser;
    @NotNull
    private int itemID;
    @NotNull
    private int itemDurability;
    @NotNull
    private int amount;
    @NotNull
    private float price;
    @NotNull
    private long sec;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public long getSec() {
        return sec;
    }

    public void setSec(long sec) {
        this.sec = sec;
    }

    public String getShopUser() {
        return shopUser;
    }

    public void setShopUser(String shopUser) {
        this.shopUser = shopUser;
    }

    public int getItemDurability() {
        return itemDurability;
    }

    public void setItemDurability(int itemDurability) {
        this.itemDurability = itemDurability;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
}
