/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista.repository;

import groovy.otter.jostoslista.domain.Shopper;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Jenni
 */
public interface ShopperRepository extends JpaRepository<Shopper, Long>{
    Shopper findByName(String nimi);

}
