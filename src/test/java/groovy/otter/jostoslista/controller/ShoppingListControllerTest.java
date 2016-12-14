/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.controller;

import groovy.otter.jostoslista.domain.Item;
import groovy.otter.jostoslista.domain.ShoppingList;
import groovy.otter.jostoslista.repository.ItemRepository;
import groovy.otter.jostoslista.repository.ShopperRepository;
import groovy.otter.jostoslista.repository.ShoppingListRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Jenni
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingListControllerTest {
    
    private ShoppingListController shoppingListController;
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ShopperRepository shopperRepository;

    @Test
    public void testNewShoppingList() {
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);

        ShoppingList retrieved =  shoppingListRepository.findOne(list.getId());
        assertNotNull(retrieved);
        assertEquals("testilista", retrieved.getName());
    }
    
    @Test
    public void testDeleteShoppingList(){
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);
        assertNotNull(shoppingListRepository.findOne(list.getId()));
        
        shoppingListRepository.delete(list.getId());
        assertNull(shoppingListRepository.findOne(list.getId()));
    }
    
    @Test
    public void testAddItem(){
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);
        
        Item item = new Item();
        item.setName("testiostos");
        item.setType("testi");
        itemRepository.save(item);
        
        list.addItem(item);
        
        assertTrue(list.getItems().contains(item));
    }

}
