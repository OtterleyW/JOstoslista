/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Jenni
 */
@Controller
public class ShoppingListController {

    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ShopperRepository shopperRepository;

    @RequestMapping(value = "/myshoppinglists", method = RequestMethod.GET)
    public String myShoppingLists(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<ShoppingList> ownLists = new ArrayList<ShoppingList>();
        for (ShoppingList sl : this.shoppingListRepository.findAll()) {
            for (Shopper shopper : sl.getShoppers()) {
                if (shopper.getName().equals(username)) {
                    ownLists.add(sl);
                }
            }
        }

        model.addAttribute("shoppinglists", ownLists);
        return "myshoppinglists";
    }

    @RequestMapping(value = "/myshoppinglists", method = RequestMethod.POST)
    public String newShoppingList(@RequestParam String listname) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (this.shopperRepository.findByName(username) == null) {
            Shopper shopper = new Shopper();
            shopper.setName(username);
            this.shopperRepository.save(shopper);
        }

        ShoppingList list = new ShoppingList();
        list.setName(listname);
        Date date = new Date();
        list.setCreatedAt(date);
        if (list.getShoppers() == null) {
            List<Shopper> shoppers = new ArrayList<Shopper>();
            shoppers.add(this.shopperRepository.findByName(username));
            list.setShoppers(shoppers);
        } else {
            list.addShopper(this.shopperRepository.findByName(username));
        }
        this.shoppingListRepository.save(list);

        return "redirect:/myshoppinglists";
    }

    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.GET)
    public String showShoppingList(Model model, @PathVariable Long id) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        if (checkIfOwner(list)) {
            model.addAttribute("shoppinglist", this.shoppingListRepository.findOne(id));
            model.addAttribute("shoppers", this.shoppingListRepository.findOne(id).getShoppers());
            model.addAttribute("items", this.shoppingListRepository.findOne(id).getItems());
            return "shoppinglist";
        }
        return "redirect:/myshoppinglists";
    }

    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.DELETE)
    public String deleteShoppingList(@PathVariable Long id) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        if (checkIfOwner(list)) {
            this.shoppingListRepository.delete(id);
        }
        return "redirect:/myshoppinglists";
    }

    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.POST)
    public String addItem(@PathVariable Long id, @RequestParam String name, @RequestParam String type) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        if (checkIfOwner(list)) {
            if (this.itemRepository.findByName(name) == null) {
                Item item = new Item();
                item.setName(name);
                item.setType(type);
                this.itemRepository.save(item);
            }
            list.addItem(this.itemRepository.findByName(name));
            this.shoppingListRepository.save(list);
        }

        return "redirect:/shoppinglist/" + list.getId();
    }

    @RequestMapping(value = "/shoppinglist/{id}/item/{itemid}", method = RequestMethod.DELETE)
    public String deleteItem(@PathVariable Long id, @PathVariable Long itemid) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        if (checkIfOwner(list)) {
            Item item = this.itemRepository.findOne(itemid);
            List<Item> items = list.getItems();
            items.remove(item);
            list.setItems(items);
            this.shoppingListRepository.save(list);
        }

        return "redirect:/shoppinglist/" + list.getId();
    }

    private boolean checkIfOwner(ShoppingList sl) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        for (Shopper shopper : sl.getShoppers()) {
            if (shopper.getName().equals(username)) {
                return true;
            }
        }

        return false;
    }
}
