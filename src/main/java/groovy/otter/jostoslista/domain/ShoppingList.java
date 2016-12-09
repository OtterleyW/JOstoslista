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
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    private Date getCreatedAt(){
        return this.createdAt;
    }
    
    private void setCreatedAt(Date date){
        this.createdAt = date;
    }
    
    
}
