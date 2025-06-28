package pe.jesusamigo.micro_products.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping("/message")
    public String mensaje() {
        return "Hola desde el microservicio de productos";
    }
}