package com.cibertec.service;

import com.cibertec.entity.*;
import com.cibertec.repository.CustomerRepository;
import com.cibertec.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService service;

    @Test
    @DisplayName("La orden es cancelada si no hay stock.")
    void shouldCancelOrderWhenNoStock() {
        when(customerRepository.findById(1L))
                .thenReturn(java.util.Optional.of(new Customer(1L, true)));

        when(productRepository.findById(1L))
                .thenReturn(java.util.Optional.of(new Product(1L, 0, 200)));

        OrderRequest request = new OrderRequest(
                "OR-0001",
                1L,
                List.of(new OrderItem(1L, 1))
        );

        String result = service.createOrder(request);

        assertEquals("Orden cancelada por falta de stock", result);
    }

    @Test
    @DisplayName("si total > 500 → aplicar descuento")
    void shouldApplyDiscountWhenTotalGreaterThan500() {

        when(customerRepository.findById(1L))
                .thenReturn(java.util.Optional.of(new Customer(1L, true)));

        when(productRepository.findById(1L))
                .thenReturn(java.util.Optional.of(new Product(1L, 10, 300)));

        OrderRequest request = new OrderRequest(
                "OR-0001",
                1L,
                List.of(new OrderItem(1L, 2)) // total = 600
        );

        String result = service.createOrder(request);

        assertEquals("Orden registrada con descuento aplicado", result);
    }

}