package spring.security.jwt.springsecurity.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.security.jwt.springsecurity.Dto.RequestResponse;
import spring.security.jwt.springsecurity.Model.Product;
import spring.security.jwt.springsecurity.Repository.ProductRepository;

@RestController
public class AdminController {

    private ProductRepository productRepository;
    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/public/product")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/admin/saveproduct")
    public ResponseEntity<Object> signUp(@RequestBody RequestResponse productRequest) {
        Product productToSave = new Product();
        productToSave.setName(productRequest.getName());
        return ResponseEntity.ok(productRepository.save(productToSave));
    }

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone() {
        return ResponseEntity.ok("Users alone can access this api");
    }

    @GetMapping("/adminuser/both")
    public ResponseEntity<Object> bothAdminAndUserApi() {
        return ResponseEntity.ok("Both admin and user can access this api");
    }
}
