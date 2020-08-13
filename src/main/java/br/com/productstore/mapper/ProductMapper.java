package br.com.productstore.mapper;

import br.com.productstore.models.Product;
import br.com.productstore.models.dto.ProductDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDTO toDto(Product entity);

    List<ProductDTO> toDto(List<Product> entities);

    Product toEntity(ProductDTO dto);

    List<Product> toEntity(List<ProductDTO> dtos);

}
