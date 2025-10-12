package com.henningstorck.tswrealweather.tsw;

import com.henningstorck.tswrealweather.tsw.exceptions.NoApiKeyException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class TswApiKeyLoader {
	private static final Path HOME_PATH = Path.of(System.getProperty("user.home"));
	private static final Path MY_GAMES_PATH = HOME_PATH.resolve("Documents", "My Games");

	public String getApiKey() throws NoApiKeyException {
		try {
			Path apiKeyPath = getApiKeyPath().orElseThrow(() -> new NoApiKeyException("Unable to find API key file."));
			return Files.readAllLines(apiKeyPath).stream().findFirst().orElseThrow(() -> new NoApiKeyException("API key file is empty."));
		} catch (IOException e) {
			throw new NoApiKeyException("Unable to read API key file.", e);
		}
	}

	private Optional<Path> getApiKeyPath() {
		File[] files = MY_GAMES_PATH.toFile().listFiles();

		if (files == null) {
			return Optional.empty();
		}

		return Arrays.stream(files)
			.filter(File::isDirectory)
			.filter(file -> file.getName().matches("^TrainSimWorld[0-9]+$"))
			.sorted(Comparator.reverseOrder())
			.map(File::toPath)
			.findFirst()
			.map(path -> path.resolve("Saved", "Config", "CommAPIKey.txt"));
	}
}
