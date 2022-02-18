package org.apache.myfaces.tobago.renderkit.css;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Helpful for tests. Not for use in production.
 */
public class FileTestUtils {

  public static String fileToString(final String file) throws IOException {

    final StringBuilder stringBuilder = new StringBuilder();
    try (Stream<String> lines = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
      lines.forEach(stringBuilder::append);
    }

    return stringBuilder.toString();
  }

}
