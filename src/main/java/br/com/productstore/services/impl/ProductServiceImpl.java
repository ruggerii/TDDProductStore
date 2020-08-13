package br.com.productstore.services.impl;

import br.com.productstore.enums.MessageEnum;
import br.com.productstore.exceptions.NotFoundException;
import br.com.productstore.exceptions.ServiceOperationException;
import br.com.productstore.mapper.ProductMapper;
import br.com.productstore.models.Product;
import br.com.productstore.models.dto.ProductDTO;
import br.com.productstore.repository.ProductRepository;
import br.com.productstore.services.ProductService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Override
    public ProductDTO findByUuid(String uuid) {

        try {
            final Product product = repository.findByUuid(uuid).orElseThrow(NotFoundException::new);

            return mapper.toDto(product);
        } catch (DataAccessException exception) {
            throw new ServiceOperationException(MessageEnum.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ProductDTO insert(ProductDTO productDTO) {
        final Optional<Product> optProduct = repository.findByName(productDTO.getName());

        optProduct.ifPresent(p -> {
            throw new ServiceOperationException(MessageEnum.PRODUCT_ALREADY_REGISTERED);
        });

        final Product product = mapper.toEntity(productDTO);

        try {
            product.setUuid(UUID.randomUUID().toString());
            final Product save = repository.save(product);
            return mapper.toDto(save);
        } catch (DataAccessException exception) {
            throw new ServiceOperationException(MessageEnum.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        try {
            final Product dbProduct = repository.findByUuid(productDTO.getUuid())
                .orElseThrow(NotFoundException::new);

            final Optional<Product> optProductName = repository.findByName(productDTO.getName());

            if(optProductName.isPresent() && !optProductName.get().getUuid().equals(productDTO.getUuid())) {
                throw new ServiceOperationException(MessageEnum.PRODUCT_ALREADY_REGISTERED);
            }

            final Product product = mapper.toEntity(productDTO);

            product.setId(dbProduct.getId());
            final Product savedProduct = repository.save(product);

            return mapper.toDto(savedProduct);
        } catch (DataAccessException exception) {
            throw new ServiceOperationException(MessageEnum.SERVICE_UNAVAILABLE);
        }
    }
}
