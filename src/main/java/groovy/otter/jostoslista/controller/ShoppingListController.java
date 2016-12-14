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
import groovy.otter.jostoslista.service.ShoppingListService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @Autowired
    private ShoppingListService shoppingListService;

    @ModelAttribute("shoppingList")
    private ShoppingList getShoppingList() {
        return new ShoppingList();
    }

    @ModelAttribute("item")
    private Item getItem() {
        return new Item();
    }

    //Näyttää kaikki käyttäjän omat ostoslistat
    @RequestMapping(value = "/myshoppinglists", method = RequestMethod.GET)
    public String myShoppingLists(Model model) {
        model.addAttribute("shoppinglists", shoppingListService.getOwnShoppingLists());
        return "myshoppinglists";
    }

    //Luo uuden ostoslistan
    @RequestMapping(value = "/myshoppinglists", method = RequestMethod.POST)
    public String newShoppingList(@Valid @ModelAttribute("shoppingList") ShoppingList shoppingList, BindingResult bindingResult, Model model, @RequestParam String name) {
        if (bindingResult.hasErrors()) {
            //Jotta virheviestit ei katoa matkalla, ei voi käyttää redirectiä, vaan pitää lisätä modelin kautta tiedot myshoppinglists näkymään
            model.addAttribute("shoppinglists", shoppingListService.getOwnShoppingLists());
            return "myshoppinglists";
        }

        shoppingListService.saveShoppingList(name);

        return "redirect:/myshoppinglists";
    }

    //Näyttää yhden ostoslistan
    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.GET)
    public String showShoppingList(Model model, @PathVariable Long id) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        //Tarkistaa onko sisäänkirjautunut käyttäjä ostoslistan omistaja
        if (shoppingListService.checkIfOwner(list)) {
            model.addAttribute("shoppinglist", this.shoppingListRepository.findOne(id));
            model.addAttribute("shoppers", this.shoppingListRepository.findOne(id).getShoppers());
            model.addAttribute("items", this.shoppingListRepository.findOne(id).getItems());
            return "shoppinglist";
        }
        //Jos käyttäjä ei ole ostoslistan omistaja, ohjataan takaisin kaikkien ostoslistojen listaussivulle
        return "redirect:/myshoppinglists";
    }

    //Poistaa ostoslistan
    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.DELETE)
    public String deleteShoppingList(@PathVariable Long id) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        //Tarkistaa onko sisäänkitjautunut käyttäjä ostoslistan omistaja ennen kuin suorittaa poistamisen
        if (shoppingListService.checkIfOwner(list)) {
            shoppingListService.deleteShoppingList(list);
        }
        return "redirect:/myshoppinglists";
    }

    //Lisää ostoslistalle ostoksen
    @RequestMapping(value = "/shoppinglist/{id}", method = RequestMethod.POST)
    public String addItem(@Valid @ModelAttribute("item") Item item, BindingResult bindingResult, @PathVariable Long id, @RequestParam String name, @RequestParam String type, Model model) {
        if (bindingResult.hasErrors()) {
            //Jotta virheviestit ei katoa matkalla, ei voi käyttää redirectiä, vaan pitää lisätä modelin kautta tiedot shoppinglist näkymään
            model.addAttribute("shoppinglist", this.shoppingListRepository.findOne(id));
            model.addAttribute("shoppers", this.shoppingListRepository.findOne(id).getShoppers());
            model.addAttribute("items", this.shoppingListRepository.findOne(id).getItems());
            return "shoppinglist";
        }

        ShoppingList list = this.shoppingListRepository.findOne(id);
        //Tarkistaa onko käyttäjä listan omistaja
        if (shoppingListService.checkIfOwner(list)) {
            shoppingListService.addItemToShoppingList(name, type, list);
        }

        return "redirect:/shoppinglist/" + list.getId();
    }

    //Poistaa itemin ostolistalta, mutta ei kokonaan tietokannasta
    @RequestMapping(value = "/shoppinglist/{id}/item/{itemid}", method = RequestMethod.DELETE)
    public String deleteItem(@PathVariable Long id, @PathVariable Long itemid) {
        ShoppingList list = this.shoppingListRepository.findOne(id);
        //Tarkistaa onko kirjautunut käyttäjä listan ommistaja ennen kuin tekee poiston
        if (shoppingListService.checkIfOwner(list)) {
            Item item = this.itemRepository.findOne(itemid);
            shoppingListService.deleteItemFromShoppingList(list, item);
        }

        return "redirect:/shoppinglist/" + list.getId();
    }

}
