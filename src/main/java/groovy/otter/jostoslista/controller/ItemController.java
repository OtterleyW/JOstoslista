/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.controller;

import groovy.otter.jostoslista.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Jenni
 */
@Controller
public class ItemController {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String myShoppingLists(Model model) {
        model.addAttribute("items", this.itemRepository.findAll());
        return "items";
    }
}
