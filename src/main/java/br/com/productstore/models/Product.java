package br.com.productstore.models;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("product")
@Data
@NoArgsConstructor
public class Product {

    @Id
    private String id;

    private String uuid;

    private String name;

    private BigDecimal weight;

    private BigDecimal priceWeight;

}
