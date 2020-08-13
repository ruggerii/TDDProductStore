package br.com.productstore.models.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

    private String uuid;

    private String name;

    private BigDecimal weight;

    private BigDecimal priceWeight;

}
