package com.javatechie.service.isolation;

import com.javatechie.entity.Product;
import com.javatechie.repository.InventoryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductService {

    @Autowired
    private InventoryRepository inventoryRepository;

    //Hibernate might cache certain queries.
    // Flush after each query using entityManager.flush().

    @Autowired
    private EntityManager entityManager;

    // Transaction A: Update stock without committing, then simulate rollback
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateStock(int productId, int stock) throws InterruptedException {
        // Retrieve the product and update its stock
        Product product = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStockQuantity(stock);
        inventoryRepository.save(product);
        entityManager.flush(); // Ensure the update is sent to the DB
        // Simulate a long-running transaction (does not commit yet)
        System.out.println("Transaction A: Stock updated to " + stock);

        // Wait to allow Transaction B to read uncommitted data
        Thread.sleep(5000);  // Wait for 5 seconds to let Transaction B read uncommitted data

        System.out.println("Transaction A: Committed the update");
        // Rollback the transaction after the delay
        //System.out.println("Transaction A: Rolling back the update");
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // Explicit rollback
    }

    // Transaction B: Read stock
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int checkStock(int productId) {
        // Retrieve the product and read its stock (potentially dirty read)
        Product product = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Transaction B: Read stock as " + product.getStockQuantity());
        return product.getStockQuantity();
    }

    // Transaction B: Read stock multiple times
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fetchStock(int productId) {
        // First read
        Product product1 = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Transaction B: First read stock as " + product1.getStockQuantity());

        // Simulate a delay to allow Transaction A to update the stock
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Second read
        Product product2 = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Transaction B: Second read stock as " + product2.getStockQuantity());
    }
}
