package br.com.productstore.repository;

import br.com.productstore.models.Product;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByUuid(String uuid);

    Optional<Product> findByName(String name);
}
