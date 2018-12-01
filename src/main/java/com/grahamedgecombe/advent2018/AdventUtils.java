package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.io.Resources;

public final class AdventUtils {
	public static String readString(String file) throws IOException {
		try {
			Path path = Paths.get(Resources.getResource(file).toURI());
			return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
		} catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}

	public static List<String> readLines(String file) throws IOException {
		try {
			Path path = Paths.get(Resources.getResource(file).toURI());
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}

	private AdventUtils() {
		/* empty */
	}
}
