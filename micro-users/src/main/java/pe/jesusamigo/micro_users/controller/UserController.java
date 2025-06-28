package pe.jesusamigo.micro_users.controller;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;

//@RestController
//@RequestMapping("/users")
public class UserController {

//    @Autowired
//    private RestTemplate restTemplate;
//
//    private static final String NOMBRE_CB = "productosCB";
//
//    @GetMapping("/message")
//    @CircuitBreaker(name = NOMBRE_CB, fallbackMethod = "fallbackProductos")
//    public String obtenerMensajeProductos() {
//        String url = "http://micro-products/products/message";
//        return "Respuesta de productos: " + restTemplate.getForObject(url, String.class);
//    }
//
//    public String fallbackProductos(Exception e) {
//        return "Microservicio de productos no disponible (fallback)";
//    }
}