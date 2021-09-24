package com.discountengine.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOperations {

    private static final Logger logger = LoggerFactory.getLogger(FileOperations.class);

    private FileOperations() {
    }

    public static Flux<String> readLines(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        Path path = Paths.get(file.getPath());
        return Flux.using(
                () -> Files.lines(path),
                Flux::fromStream,
                Stream::close
        );
    }

    public static <T> void writeLines(String fileName, Flux<T> flux) throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        flux
                .subscribe(s -> write(bufferedWriter, s.toString()),
                        e -> close(bufferedWriter),
                        () -> close(bufferedWriter)
                );
    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
            logger.info("File is closed");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void write(BufferedWriter bw, String string) {
        try {
            bw.write(string);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
