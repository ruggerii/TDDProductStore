package br.com.productstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import br.com.productstore.enums.MessageEnum;
import br.com.productstore.exceptions.NotFoundException;
import br.com.productstore.exceptions.ServiceOperationException;
import br.com.productstore.mapper.ProductMapper;
import br.com.productstore.models.Product;
import br.com.productstore.models.dto.ProductDTO;
import br.com.productstore.repository.ProductRepository;
import br.com.productstore.services.impl.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.TransientDataAccessResourceException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    private ProductService service;

    @BeforeEach
    public void initService() {
        service = new ProductServiceImpl(repository, mapper);
    }


    public Product createProduct() {
        Product product = new Product();
        product.setUuid("123123123213");
        product.setName("Apple");
        product.setWeight(BigDecimal.TEN);
        product.setPriceWeight(BigDecimal.ONE);

        return product;
    }

    public ProductDTO createProductDto() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setUuid("123123123213");
        productDTO.setName("Apple");
        productDTO.setWeight(BigDecimal.TEN);
        productDTO.setPriceWeight(BigDecimal.ONE);

        return productDTO;
    }

    @Test
    @DisplayName("Should return Product by uuid")
    public void shouldReturnProductByUuid() {

        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        final Optional<Product> optProduct = Optional.of(product);

        when(repository.findByUuid("10")).thenReturn(optProduct);
        when(mapper.toDto(product)).thenReturn(productDto);

        final ProductDTO returnedProduct = service.findByUuid("10");
        assertNotNull(returnedProduct);
        assertEquals(product.getName(), returnedProduct.getName());
    }

    @Test
    @DisplayName("Should throw NotFoundException when try to get Product by uuid")
    public void shouldThrowNotFoundExceptionWhenGetProductByUuid() {

        when(repository.findByUuid("10")).thenReturn(Optional.empty());

        final NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            this.service.findByUuid("10");
        });

        assertEquals(MessageEnum.NOT_FOUND.getMsg(), notFoundException.getMessage());
    }

    @Test
    public void shouldThrowServiceUnavailableWhenFindProduct() {
        when(repository.findByUuid("10"))
            .thenThrow(new TransientDataAccessResourceException("Failed to connect to database"));

        final ServiceOperationException serviceOperationException = assertThrows(
            ServiceOperationException.class, () -> {
                service.findByUuid("10");
            });

        assertEquals(MessageEnum.SERVICE_UNAVAILABLE.getMsg(),
            serviceOperationException.getMessage());
    }

    @Test
    @DisplayName("Should return ProductDTO when insert Product")
    public void shouldReturnProductDtoWhenInsertProduct() {

        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        when(repository.findByName(productDto.getName())).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);
        when(mapper.toEntity(productDto)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(productDto);

        final ProductDTO returnedProductDto = this.service.insert(productDto);

        assertNotNull(returnedProductDto);
    }

    @Test
    @DisplayName("Should throw ProductAlreadyRegisteredException when insert Product")
    public void shouldThrowExceptionProductExistsWhenInsertProduct() {

        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        when(repository.findByName(productDto.getName())).thenReturn(Optional.of(product));

        final ServiceOperationException serviceOperationException = assertThrows(
            ServiceOperationException.class, () -> {
                service.insert(productDto);
            });

        assertEquals(MessageEnum.PRODUCT_ALREADY_REGISTERED.getMsg(),
            serviceOperationException.getMessage());
    }

    @Test
    @DisplayName("Should ServiceUnavailable message when insert Product")
    public void shouldThrowServiceUnavailableWhenInsertProduct() {
        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        when(mapper.toEntity(productDto)).thenReturn(product);
        when(repository.findByName(productDto.getName())).thenReturn(Optional.empty());
        when(repository.save(product)).thenThrow(new TransientDataAccessResourceException(""));

        assertThrows(ServiceOperationException.class, () -> {
            this.service.insert(productDto);
        });
    }

    @Test
    @DisplayName("Should return ProductDTO when update Product")
    public void shouldReturnProductDtoWhenUpdateProduct() {
        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        when(repository.findByUuid(productDto.getUuid())).thenReturn(Optional.of(product));
        when(repository.findByName(productDto.getName())).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(mapper.toEntity(productDto)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(productDto);

        service.update(productDto);

        assertNotNull(productDto);
    }

    @Test
    @DisplayName("Should throw ProductAlreadyException when update Product")
    public void shouldThrowProductAlreadyExceptionWhenUpdateProduct() {
        final Product product = createProduct();
        final ProductDTO productDto = createProductDto();

        when(repository.findByUuid(productDto.getUuid())).thenReturn(Optional.of(product));
        product.setUuid(UUID.randomUUID().toString());
        when(repository.findByName(productDto.getName())).thenReturn(Optional.of(product));

        final ServiceOperationException serviceOperationException = assertThrows(
            ServiceOperationException.class, () -> {
                service.update(productDto);
            });

        assertEquals(MessageEnum.PRODUCT_ALREADY_REGISTERED.getMsg(),
            serviceOperationException.getMessage());

    }

    @Test
    @DisplayName("Should throw NotFoundException message when update Product")
    public void shouldThrowNotFoundExceptionWhenUpdateProduct() {
        final ProductDTO productDto = createProductDto();

        final NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            service.update(productDto);
        });

        assertEquals(MessageEnum.NOT_FOUND.getMsg(), notFoundException.getMessage());
    }


    @Test
    @DisplayName("Should throw ServiceUnavailable message when update Product")
    public void shouldThrowServiceUnavilableWhenUpdateProduct() {
        final ProductDTO productDto = createProductDto();

        when(repository.findByUuid(productDto.getUuid()))
            .thenThrow(new ServiceOperationException(MessageEnum.SERVICE_UNAVAILABLE));

        final ServiceOperationException serviceUnavailableException = assertThrows(
            ServiceOperationException.class, () -> {
                service.update(productDto);
            });

        assertEquals(MessageEnum.SERVICE_UNAVAILABLE.getMsg(),
            serviceUnavailableException.getMessage());
    }

}
