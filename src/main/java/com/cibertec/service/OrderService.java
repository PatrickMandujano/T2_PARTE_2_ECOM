package com.cibertec.service;

import com.cibertec.entity.Customer;
import com.cibertec.entity.OrderItem;
import com.cibertec.entity.OrderRequest;
import com.cibertec.entity.Product;
import com.cibertec.repository.CustomerRepository;
import com.cibertec.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public String createOrder(OrderRequest orderRequest) {

        var customer = customerRepository.findById(orderRequest.getCustomerId()).orElse(null);

        //Validación de cliente existente y activo
        if (customer == null || !customer.isActive()) {
            return "Cliente inválido";
        }

        //Cancelación de orden por falta de stock y cálculo del total
        double total = 0;

        for (OrderItem item : orderRequest.getItems()) {

            var product = productRepository.findById(item.getProductId()).orElse(null);

            if (product.getStock() < item.getQuantity()) {
                return "Orden cancelada por falta de stock";
            }

            total += product.getPrice() * item.getQuantity();
        }
        //descuento si importe total es mayor a 500
        if (total > 500) {
            return "Orden registrada con descuento aplicado";
        }

        return "Orden registrada correctamente";
    }
}
