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

        //Validación de código de orden de acuerdo con su formato
        if (!isValidCode(orderRequest.getCode())) {
            return "Código de orden inválido";
        }

        //Búsqueda de cliente por ID y validación de su estado activo
        var customer = customerRepository.findById(orderRequest.getCustomerId())
                .filter(Customer::isActive)
                .orElse(null);

        if (customer == null) {
            return "Cliente inválido";
        }

        //Validación de productos en la orden, cantidad y stock disponible
        double total = 0;
        Set<Long> ids = new HashSet<>();

        for (OrderItem item : orderRequest.getItems()) {

            if (!ids.add(item.getProductId())) {
                return "Productos duplicados en la orden";
            }

            if (item.getQuantity() <= 0) {
                return "Cantidad inválida";
            }

            var product = productRepository.findById(item.getProductId()).orElse(null);

            if (product == null || product.getStock() < item.getQuantity()) {
                return "Orden cancelada por falta de stock";
            }

            total += product.getPrice() * item.getQuantity();

        }
        //descuento si importe total es mayor a 500
        return total > 500
                ? "Orden registrada con descuento aplicado"
                : "Orden registrada correctamente";

    }
    //Validación de código de orden con formato específico (ejemplo: OR-1234)
    private boolean isValidCode(String code) {
        return code.matches("^OR-\\d{4}$");
    }
}
