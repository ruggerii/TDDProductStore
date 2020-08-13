package br.com.productstore.services;

import br.com.productstore.models.dto.ProductDTO;

public interface ProductService {

    ProductDTO findByUuid(String uuid);

    ProductDTO insert(ProductDTO productDTO);

    ProductDTO update(ProductDTO productDTO);
}
