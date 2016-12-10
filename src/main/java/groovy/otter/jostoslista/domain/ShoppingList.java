/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Jenni
 */
@Entity
public class ShoppingList extends AbstractPersistable<Long>{
    
    private String name;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @ManyToMany
    private List<Item> items;
    @ManyToMany
    private List<Shopper> shoppers;
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public Date getCreatedAt(){
        return this.createdAt;
    }
    
    public void setCreatedAt(Date date){
        this.createdAt = date;
    }
    
    public List<Item> getItems(){
        return this.items;
    }
    
    public void setItems(List<Item> items){
        this.items = items;
    }
    
    public void addItem(Item item){
        this.items.add(item);
    }
    
    public List<Shopper> getShoppers() {
        return this.shoppers;
    }

    public void setShoppers(List<Shopper> hoppers) {
        this.shoppers = shoppers;
    }

    public void addShopper(Shopper shopper) {
        this.shoppers.add(shopper);
    }
    
    
    
}
