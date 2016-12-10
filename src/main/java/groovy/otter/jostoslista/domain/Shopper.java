/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Jenni
 */
@Entity
public class Shopper extends AbstractPersistable<Long>{

    private String name;
    @ManyToMany(mappedBy = "shoppers")
    private List<ShoppingList> shoppingLists;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShoppingList> getShoppingLists() {
        return this.shoppingLists;
    }

    public void setShopppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
    
    public void addShoppingList(ShoppingList shoppingList){
        this.shoppingLists.add(shoppingList);
    }

}
