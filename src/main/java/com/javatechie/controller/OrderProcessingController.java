package com.javatechie.controller;

import com.javatechie.entity.Order;
import com.javatechie.entity.Product;
import com.javatechie.service.InventoryService;
import com.javatechie.service.OrderProcessingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderProcessingController {

    private final OrderProcessingService orderProcessingService;

    private final InventoryService inventoryService;

    public OrderProcessingController(OrderProcessingService orderProcessingService,
                                     InventoryService inventoryService) {
        this.orderProcessingService = orderProcessingService;
        this.inventoryService=inventoryService;
    }

    /**
     * API to place an order
     *
     * @param order the order details
     * @return the processed order with updated total price
     */
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        try {
            Order processedOrder = orderProcessingService.placeAnOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(processedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the order.");
        }
    }

    /**
     * Health check for the Order API
     *
     * @return simple health status
     */
    @PostMapping("/addProducts")
    public Product addProduct(@RequestBody Product product) {
        Product productDetails = inventoryService.updateProductDetails(product);
        return productDetails;
    }
}
