package com.luffy.testautomation.utils.helpers;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.openqa.selenium.WebElement;

/** Used to concurrently wait for one of expected elements. */
public class ConcurrentWait<T extends WebElement> {

    public static final int TIMEOUT = 30;

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final CompletionService<WebElement> service = new ExecutorCompletionService<>(pool);
    private final List<Supplier<T>> tasks;

    private ConcurrentWait(List<Supplier<T>> tasks) {
        this.tasks = tasks;
    }

    public static <T extends WebElement> ConcurrentWait<T> from(List<Supplier<T>> tasks) {
        return new ConcurrentWait<>(tasks);
    }

    /**
     * Waits for the first returning supplier.
     *
     * @return WebElement returned from the first completed supplier.
     */
    @Nullable
    public WebElement first() {
        tasks.forEach(i -> service.submit(i::get));
        try {
            return service.poll(TIMEOUT, TimeUnit.SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
