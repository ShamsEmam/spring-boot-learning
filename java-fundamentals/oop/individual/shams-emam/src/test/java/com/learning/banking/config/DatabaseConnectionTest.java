package com.learning.banking.config;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void getInstanceShouldAlwaysReturnTheSameObject() {

        assertSame(
                DatabaseConnection.getInstance(),
                DatabaseConnection.getInstance()
        );
    }

    @Test
    void singletonShouldBeUsableAsAConnectionProvider() {

        ConnectionProvider provider = DatabaseConnection.getInstance();

        assertNotNull(provider);
    }

    @Test
    void getInstanceShouldReturnOneObjectEvenWhenCalledByManyThreadsAtOnce()
            throws Exception {

        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startTogether = new CountDownLatch(1);

        try {
            List<Future<DatabaseConnection>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                Callable<DatabaseConnection> task = () -> {
                    startTogether.await();
                    return DatabaseConnection.getInstance();
                };
                futures.add(executor.submit(task));
            }

            startTogether.countDown();

            Set<DatabaseConnection> distinctInstances = new HashSet<>();
            for (Future<DatabaseConnection> future : futures) {
                distinctInstances.add(future.get());
            }

            assertEquals(1, distinctInstances.size());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void constructorShouldBePrivateSoNobodyCanCreateASecondInstance()
            throws Exception {

        var constructor = DatabaseConnection.class.getDeclaredConstructor();

        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
    }
}
