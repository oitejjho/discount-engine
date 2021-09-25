package com.discountengine.demo.utils;

import com.discountengine.demo.util.FileOperations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FileOperationsTest {

    @Test
    public void testReadLinesSuccess() {
        Flux<String> stringFlux = FileOperations.readLines("input.txt");
        Assertions.assertNotNull(stringFlux);
    }

    @Test
    public void testReadLinesWithException() {
        assertThrows(NullPointerException.class, () -> {
            Flux<String> stringFlux = FileOperations.readLines("invalid.txt");
        });
    }

    @Test
    public void testWriteLinesSuccess() throws IOException {
        assertDoesNotThrow(() -> {
            FileOperations.writeLines("output.txt", Flux.just("nothing to write"));
        });
    }

    @Test
    public void testWriteLinesSuccessWithEmptyFlux() throws IOException {
        assertDoesNotThrow(() -> {
            FileOperations.writeLines("input.txt", Flux.empty());
        });
    }
}
