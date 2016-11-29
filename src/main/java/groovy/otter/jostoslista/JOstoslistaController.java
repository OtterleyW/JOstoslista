/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovy.otter.jostoslista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Jenni
 */

@Controller
public class JOstoslistaController {
    @RequestMapping("*")
    @ResponseBody
    public String home() {
        return "Jee Ostoslista!";
    }
}
