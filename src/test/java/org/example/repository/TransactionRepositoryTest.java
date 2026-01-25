package org.example.repository;

import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString()); // REQUIRED
        transaction.setCustomerId("C456");
        transaction.setDescription("Another transaction");
        transaction.setAmount(BigDecimal.valueOf(50.0));
        transaction.setDate(LocalDate.now());
        transaction.setCategory(TransactionCategory.FOOD);
        Transaction saved = transactionRepository.save(transaction);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("C456", saved.getCustomerId());
        assertEquals(BigDecimal.valueOf(50.0), saved.getAmount());
    }

    @Test
    void testFindById() {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setCustomerId("C123");
        transaction.setDescription("Test transaction");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setDate(LocalDate.now());
        transaction.setCategory(TransactionCategory.TRAVEL);
        transactionRepository.save(transaction);
        Optional<Transaction> found = transactionRepository.findById(transaction.getId());
        assertTrue(found.isPresent());
        assertEquals("C123", found.get().getCustomerId());
    }

    @Test
    void testFindAll() {
        Transaction t1 = new Transaction();
        t1.setId(UUID.randomUUID().toString());
        t1.setCustomerId("C1");
        t1.setDescription("First");
        t1.setAmount(BigDecimal.valueOf(10));
        t1.setDate(LocalDate.now());
        t1.setCategory(TransactionCategory.FOOD);
        Transaction t2 = new Transaction();
        t2.setId(UUID.randomUUID().toString());
        t2.setCustomerId("C2");
        t2.setDescription("Second");
        t2.setAmount(BigDecimal.valueOf(20));
        t2.setDate(LocalDate.now());
        t2.setCategory(TransactionCategory.SHOPPING);
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size());
    }

    @Test
    void testDeleteTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setCustomerId("C999");
        transaction.setDescription("To delete");
        transaction.setAmount(BigDecimal.valueOf(30));
        transaction.setDate(LocalDate.now());
        transaction.setCategory(TransactionCategory.OTHER);
        transactionRepository.save(transaction);
        transactionRepository.deleteById(transaction.getId());
        Optional<Transaction> deleted = transactionRepository.findById(transaction.getId());
        assertTrue(deleted.isEmpty());
    }
}
