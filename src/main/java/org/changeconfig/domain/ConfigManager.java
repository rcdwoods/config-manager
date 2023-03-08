package org.changeconfig.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {

	private static final char ATTRIBUTE_SEPARATOR = '=';
	private static final String APPLICATION_PREFIX = "application.";
	private static final String CONFIGURATION_FILE = "config.properties";
	private static final String APPLICATIONS_DIRECTORY = "Applications";
	private static final String CONFIG_MANAGER_HOME = System.getProperty("user.home").concat("/ConfigManager");

	private Map<String, String> getRegisteredApplications() throws IOException {
		Path configFile = getConfigPath();

		try (Stream<String> configLines = Files.lines(configFile)) {
			return configLines.collect(Collectors.toMap(this::getAttribute, this::getValue));
		}
	}

	public Path getConfigPath() throws IOException {
		Path userHome = Paths.get(CONFIG_MANAGER_HOME);
		Path configPath = userHome.resolve(CONFIGURATION_FILE);
		Path applicationsPath = userHome.resolve(APPLICATIONS_DIRECTORY);

		if (!configPath.toFile().exists()) {
			System.out.println("Configuration file not found. Creating a new one...");
			Files.createFile(configPath);
		}

		if (!applicationsPath.toFile().exists()) {
			System.out.println("Applications folder not found. Creating a new one...");
			applicationsPath.toFile().mkdir();
		}

		return configPath;
	}

	public Application getRegisteredApplication(String applicationName) throws IllegalAccessException, IOException {
		Map<String, String> registeredApplications = getRegisteredApplications();
		Path applicationPath = Application.defaultPathForApplication(applicationName);

		if (!registeredApplications.containsKey(APPLICATION_PREFIX.concat(applicationName)) || !Files.exists(applicationPath)) {
			throw new IllegalAccessException("Application not registered.");
		}

		try (Stream<String> lines = Files.lines(applicationPath)) {
			Map<String, String> applicationConfigs =
				lines.collect(Collectors.toMap(this::getAttribute, this::getValue));
			return new Application(applicationName, applicationConfigs);
		} catch (IOException e) {
			throw new IllegalAccessException("Error when getting application configurations.");
		}
	}

	private String getAttribute(String line) {
		return line.substring(0, line.indexOf(ATTRIBUTE_SEPARATOR));
	}

	private String getValue(String line) {
		return line.substring(line.indexOf(ATTRIBUTE_SEPARATOR) + 1);
	}
}
