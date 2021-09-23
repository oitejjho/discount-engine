package com.discountengine.demo.util;

import reactor.core.publisher.Flux;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOperations {

    public static final String INPUT_FILE_PATH = "src/main/resources/input.txt";
    public static final String OUTPUT_FILE_PATH = "src/main/resources/output.txt";


    public static Flux<String> readLines(String fileName) {
        Path path = Paths.get(fileName);
        Flux<String> lineFlux = Flux.using(
                () -> Files.lines(path),
                Flux::fromStream,
                Stream::close
        );
        return lineFlux;
    }

    public static <T> void writeLines(String fileName, Flux<T> flux) throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        flux
            .subscribe(s -> write(bufferedWriter, s.toString()),
                    (e) -> close(bufferedWriter),
                    () -> close(bufferedWriter)
            );
    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("File is closed");
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
