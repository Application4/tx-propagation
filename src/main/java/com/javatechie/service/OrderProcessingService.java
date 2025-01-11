package com.javatechie.service;

import com.javatechie.entity.Order;
import com.javatechie.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    private final OrderService orderService;

    private final InventoryService inventoryService;

    private final AuditLogService auditLogService;

    private final PaymentValidatorService paymentValidatorService;

    private final EmailService emailService;

    private final ProductRecommendationService recommendationService;

    public OrderProcessingService(OrderService orderService,
                                  InventoryService inventoryService,
                                  AuditLogService auditLogService,
                                  PaymentValidatorService paymentValidatorService,
                                  EmailService emailService,
                                  ProductRecommendationService recommendationService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.auditLogService=auditLogService;
        this.paymentValidatorService=paymentValidatorService;
        this.emailService=emailService;
        this.recommendationService=recommendationService;
    }

    // Main transaction method
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Order placeAnOrder(Order order) {

        // Step 1: Get product inventory
        Product product = inventoryService.getProduct(order.getProductId());

        // Step 2: Validate stock availability
        validateStockAvailability(order.getQuantity(), product.getStockQuantity());

        // Step 3: Update total price in order entity
        order.setTotalPrice(order.getQuantity() * product.getPrice());

        Order savedOrder = null;
        try {
            // Step 4: Save order (participates in the same transaction)
            savedOrder = orderService.saveOrder(order);

            // Step 5: Update stock in inventory (participates in the same transaction)
            updateInventoryStock(order, product);

            // If everything is successful, log success
            auditLogService.logAuditDetails(savedOrder, "Order Placed Successfully");
        } catch (Exception e) {
            // On failure, log failure
            auditLogService.logAuditDetails(order, "Order Placement Failed");
        }

        // Fetch product recommendations (NOT_SUPPORTED propagation)
        //recommendationService.getRecommendations(1L);
        return savedOrder;
    }







    private void updateInventoryStock(Order order, Product product) {
        int availableStock = product.getStockQuantity() - order.getQuantity();
        product.setStockQuantity(availableStock);
        inventoryService.updateProductDetails(product);
    }

    private void validateStockAvailability(int orderQuantity, int availableStock) {
        if (orderQuantity > availableStock) {
            throw new IllegalArgumentException("Insufficient stock!");
        }
    }

}
