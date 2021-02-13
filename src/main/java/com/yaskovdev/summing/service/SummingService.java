package com.yaskovdev.summing.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
class SummingService {

    private final Queue<SumHolder> queue = new LinkedList<>();
    private int sum = 0;

    @SneakyThrows
    synchronized int submitAndWait(final int number) {
        final SumHolder holder = new SumHolder();
        sum += number;
        queue.add(holder);
        wait();
        return holder.getSum();
    }

    synchronized int release() {
        while (isNotEmpty(queue)) {
            queue.remove().setSum(sum);
        }
        final int answer = sum;
        sum = 0;
        notifyAll();
        return answer;
    }
}
