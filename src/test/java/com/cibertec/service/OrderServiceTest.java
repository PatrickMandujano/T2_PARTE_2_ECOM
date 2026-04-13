package com.cibertec.service;

import com.cibertec.entity.Customer;
import com.cibertec.entity.Order;
import com.cibertec.entity.OrderItem;
import com.cibertec.entity.Product;
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
    @DisplayName("La orden no procede si no hay stock.")
    void shouldCancelOrderWhenNoStock() {

        Customer customer = new Customer(1L, true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Product product = new Product(1L, 0, 200);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Order order = new Order(
                "OR-0001",
                1L,
                List.of(new OrderItem(1L, 1))
        );

        String result = service.createOrder(order);

        assertEquals("Orden cancelada por falta de stock", result);
    }



}