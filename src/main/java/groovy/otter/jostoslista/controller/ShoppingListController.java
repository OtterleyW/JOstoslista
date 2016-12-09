/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.controller;

import groovy.otter.jostoslista.domain.Item;
import groovy.otter.jostoslista.domain.ShoppingList;
import groovy.otter.jostoslista.repository.ItemRepository;
import groovy.otter.jostoslista.repository.ShoppingListRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ItemRepository itemRepository;
    
    @RequestMapping(value = "/myshoppinglists", method = RequestMethod.GET)
    public String myShoppingLists(Model model) {
        model.addAttribute("shoppinglists", this.shoppingListRepository.findAll());
        return "myshoppinglists";
    }
    
    @RequestMapping(value="/shoppinglist/{id}", method = RequestMethod.GET)
    public String showShoppingList(Model model, @PathVariable Long id) {
        model.addAttribute("shoppinglist", this.shoppingListRepository.findOne(id));
        model.addAttribute("items", this.shoppingListRepository.findOne(id).getItems());
        return "shoppinglist";
    }
    
    @RequestMapping(value="/shoppinglist/{id}", method = RequestMethod.POST)
    public String addItem(@PathVariable Long id, @RequestParam String name, @RequestParam String type){
        Item item = new Item();
        item.setName(name);
        item.setType(type);
        this.itemRepository.save(item);
        
        ShoppingList list = this.shoppingListRepository.findOne(id);
        list.addItem(item);
        this.shoppingListRepository.save(list);
         
        return "redirect:/shoppinglist";
    }
}
