package spring.security.jwt.springsecurity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.jwt.springsecurity.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
