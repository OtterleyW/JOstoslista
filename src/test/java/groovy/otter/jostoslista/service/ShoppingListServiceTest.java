/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.service;

import groovy.otter.jostoslista.domain.Item;
import groovy.otter.jostoslista.domain.ShoppingList;
import groovy.otter.jostoslista.repository.ItemRepository;
import groovy.otter.jostoslista.repository.ShopperRepository;
import groovy.otter.jostoslista.repository.ShoppingListRepository;
import groovy.otter.jostoslista.service.ShoppingListService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Jenni
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShoppingListServiceTest {

    @Autowired
    private ShoppingListService shoppingListService;
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ShopperRepository shopperRepository;

    @Test
    public void testNewShoppingList() {
        this.shoppingListService.saveShoppingList("testilista", "user1");

        List<ShoppingList> retrieved = shoppingListRepository.findAll();
        assertNotNull(retrieved);
        assertEquals("testilista", retrieved.get(0).getName());
    }

    @Test
    public void testDeleteShoppingList() {
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);
        assertNotNull(shoppingListRepository.findOne(list.getId()));

        this.shoppingListService.deleteShoppingList(list);
        assertNull(shoppingListRepository.findOne(list.getId()));
    }

    @Test
    public void testAddItem() {
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);

        this.shoppingListService.addItemToShoppingList("testi item", "testi", list);
        assertNotNull(this.itemRepository.findByName("testi item"));
    }

    @Test
    public void testDeleteItem() {
        ShoppingList list = new ShoppingList();
        list.setName("testilista");
        shoppingListRepository.save(list);

        Item item = new Item();
        item.setName("testi item");
        item.setType("testi");
        this.itemRepository.save(item);

        List<Item> items = new ArrayList<Item>();
        items.add(item);
        list.setItems(items);
        this.shoppingListRepository.save(list);

        this.shoppingListService.deleteItemFromShoppingList(list, item);
        assertTrue(!this.shoppingListRepository.findOne(list.getId()).getItems().contains(item));
        assertNotNull(this.itemRepository.findByName("testi item"));
    }
}
