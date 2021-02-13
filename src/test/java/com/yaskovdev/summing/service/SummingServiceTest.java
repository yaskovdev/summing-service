package com.yaskovdev.summing.service;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

public class SummingServiceTest {

    private static final int NUMBER_OF_THREADS = 100;

    private final AtomicInteger submitted = new AtomicInteger(0);
    private final AtomicInteger released = new AtomicInteger(0);

    @RepeatedTest(20)
    public void shouldNotLoseAnyNumbers() {
        final SummingService testedObject = new SummingService();
        final List<Thread> threads = new ArrayList<>(NUMBER_OF_THREADS);
        final Random random = new Random();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            final Thread thread = new Thread(() -> {
                if (random.nextInt(4) % 4 == 0) {
                    released.addAndGet(testedObject.release());
                } else {
                    testedObject.submitAndWait(1);
                    submitted.addAndGet(1);
                }
            });
            threads.add(thread);
        }
        for (final Thread thread : threads) {
            thread.start();
        }
        try {
            await().atMost(10, SECONDS)
                    .until(() -> submitted.get() > 0 && submitted.get() == released.get());
        } catch (final ConditionTimeoutException e) {
            fail("Submitted numbers are not equal to released numbers, " +
                    "submitted is " + submitted.get() + ", released is " + released.get());
        }
    }
}
