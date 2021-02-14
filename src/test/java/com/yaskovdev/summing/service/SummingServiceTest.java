package com.yaskovdev.summing.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.range;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SummingServiceTest {

    private static final int NUMBER_OF_SUBMITTING_THREADS = 180;
    private static final int NUMBER_OF_RELEASING_THREADS = 20;

    @RepeatedTest(10)
    void shouldNeitherLoseAnyNumbersNorCountThemMultipleTimes() {
        final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_SUBMITTING_THREADS + NUMBER_OF_RELEASING_THREADS + 1);
        final SummingService objectToTest = new SummingService();
        final Random random = new Random();
        final AtomicInteger submittedSum = new AtomicInteger(0);
        final AtomicInteger releasedSum = new AtomicInteger(0);
        final AtomicInteger numberOfRunningSubmittingThreads = new AtomicInteger(NUMBER_OF_SUBMITTING_THREADS);
        final AtomicInteger numberOfRunningReleasingThreads = new AtomicInteger(NUMBER_OF_RELEASING_THREADS);

        prepareThreads(NUMBER_OF_SUBMITTING_THREADS, () -> {
            awaitQuietly(barrier);
            final int number = 1 + random.nextInt(100);
            submittedSum.addAndGet(number);
            objectToTest.submitAndWait(number);
            numberOfRunningSubmittingThreads.decrementAndGet();
        });
        prepareThreads(NUMBER_OF_RELEASING_THREADS, () -> {
            awaitQuietly(barrier);
            releasedSum.addAndGet(objectToTest.release());
            numberOfRunningReleasingThreads.decrementAndGet();
        });

        startAllThreads(barrier);
        await().atMost(5, SECONDS)
                .until(() -> {
                    releasedSum.addAndGet(objectToTest.release());
                    return numberOfRunningSubmittingThreads.get() == 0 && numberOfRunningReleasingThreads.get() == 0;
                });

        final String reason = "Sum of submitted numbers is not equal to sum of returned results, " +
                "submitted sum is " + submittedSum.get() + ", returned sum is " + releasedSum.get();
        assertThat(reason, releasedSum.get(), is(submittedSum.get()));
    }

    private static void prepareThreads(final int amount, final Runnable runnable) {
        range(0, amount)
                .mapToObj(it -> runnable)
                .map(Thread::new)
                .forEach(Thread::start);
    }

    private static void startAllThreads(final CyclicBarrier barrier) {
        awaitQuietly(barrier);
    }

    @SneakyThrows
    private static void awaitQuietly(final CyclicBarrier barrier) {
        barrier.await();
    }
}
