/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.service;

import groovy.otter.jostoslista.domain.Item;
import groovy.otter.jostoslista.domain.Shopper;
import groovy.otter.jostoslista.domain.ShoppingList;
import groovy.otter.jostoslista.repository.ItemRepository;
import groovy.otter.jostoslista.repository.ShopperRepository;
import groovy.otter.jostoslista.repository.ShoppingListRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jenni
 */
@Service
public class ShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ShopperRepository shopperRepository;

    public void saveShoppingList(String name, String username) {
        Shopper shopper = checkIfShopperExists(username);

        //Luo uuden ostoslistan
        ShoppingList list = new ShoppingList();
        list.setName(name);
        Date date = new Date();
        list.setCreatedAt(date);

        //Jos ostoslistalla ei ole vielä yhtään omistajaa, luodaan ensin uusi lista omistajista ja lisätään sitten kirjautunut käyttäjä omistajaksi. Muuten lisätään käyttäjä suoraan omistajien listalle.
        if (list.getShoppers() == null) {
            List<Shopper> shoppers = new ArrayList<Shopper>();
            shoppers.add(shopper);
            list.setShoppers(shoppers);
        } else {
            list.addShopper(shopper);
        }

        //Tallentaa ostoslistan
        this.shoppingListRepository.save(list);
    }

    public void deleteShoppingList(ShoppingList list) {
        this.shoppingListRepository.delete(list);
    }

    public void addItemToShoppingList(String name, String type, ShoppingList list) {
        Item newItem = new Item();
        newItem.setName(name);
        newItem.setType(type);

        //Jos tietokannasta ei löydy samannimistä itemiä, tallennetaan uusi item
        if (this.itemRepository.findByName(name) == null) {
            this.itemRepository.save(newItem);
        } else {
            Boolean tallenna = true;
            //Jos tietokannasta löytyy samannimisiä itemejä, tarkistetaan, löytyykö item, jolla on sama tyyppi
            for (Item i : this.itemRepository.findByName(name)) {
                if (i.getType().equals(type)) {
                    tallenna = false;
                }
            }
            if (tallenna) {
                this.itemRepository.save(newItem);
            }
        }

        //Jos ostoslistalla ei ole vielä yhtään itemiä, luodaan ensin uusi lista itemeistä ja lisätään sitten luotu item listalle.
        if (list.getItems() == null) {
            List<Item> items = new ArrayList<Item>();
            items.add(newItem);
            list.setItems(items);
        } else {
            list.addItem(newItem);
        }

        //Tallentaa ostoksen ostoslistalle
        list.addItem(newItem);
        this.shoppingListRepository.save(list);
    }

    public void deleteItemFromShoppingList(ShoppingList list, Item item) {
        List<Item> items = list.getItems();
        items.remove(item);
        list.setItems(items);
        this.shoppingListRepository.save(list);
    }

    //Jos sisäänkirjautunutta käyttäjää ei löydy tietokannasta, luo uuden Shopper-olion
    private Shopper checkIfShopperExists(String username) {
        if (this.shopperRepository.findByName(username) == null) {
            Shopper shopper = new Shopper();
            shopper.setName(username);
            return this.shopperRepository.save(shopper);
        }
        return this.shopperRepository.findByName(username);
    }

    //Tarkistaa onko kirjautunut käyttäjä ostoslistan sl omistajissa
    public boolean checkIfOwner(ShoppingList sl, String username) {
        for (Shopper shopper : sl.getShoppers()) {
            if (shopper.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    //Palauttaa kaikki kirjautuneen käyttäjän ostoslistat, joissa käyttäjä on omistajissa 
    public List<ShoppingList> getOwnShoppingLists(String username) {
        List<ShoppingList> ownLists = new ArrayList<ShoppingList>();
        for (ShoppingList sl : this.shoppingListRepository.findAll()) {
            if (checkIfOwner(sl, username)) {
                ownLists.add(sl);
            }
        }
        return ownLists;
    }

}
