package org.changeconfig.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
	private final String name;
	private String currentProfile;
	private final Map<String, String> profiles;
	private final Path path;
	private final Path mainConfiguration;

	private static final String CURRENT_CONFIG = "current-configuration";
	private static final String CONFIGS_PREFIX = "configurations.";
	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final String CONFIG_MANAGER_HOME = System.getProperty("user.home").concat("/ConfigManager");
	private static final String APPLICATIONS_DIRECTORY = "applications";
	private static final String MAIN_CONFIGURATION = "main-configuration";

	public Application(String name, Map<String, String> applicationConfigs) {
		if (!applicationConfigs.containsKey(CURRENT_CONFIG) || !applicationConfigs.containsKey(MAIN_CONFIGURATION)) {
			throw new IllegalArgumentException("Current config has not been provided.");
		}
		this.name = name;
		this.currentProfile = applicationConfigs.get(CURRENT_CONFIG);
		this.profiles = filterConfigs(applicationConfigs);
		this.path = defaultPathForApplication(name);
		this.mainConfiguration = Paths.get(applicationConfigs.get(MAIN_CONFIGURATION));
	}

	private Map<String, String> filterConfigs(Map<String, String> applicationConfigs) {
		return applicationConfigs.entrySet().stream()
			.filter(configs -> configs.getKey().contains(CONFIGS_PREFIX))
			.map(this::removeConfigPrefix)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map.Entry<String, String> removeConfigPrefix(Map.Entry<String, String> applicationConfigs) {
		String keyWithoutConfigPrefix = applicationConfigs.getKey().replace(CONFIGS_PREFIX, "");
		return Map.entry(keyWithoutConfigPrefix, applicationConfigs.getValue());
	}

	public void changeProfile(String profile) throws IOException {
		if (!profiles.containsKey(profile)) {
			throw new IllegalArgumentException("Profile does not exist.");
		}

		changeCurrentProfileOnApplicationConfig(profile);
		changeCurrentProfileOnSystem(profile);
		this.currentProfile = profile;

		System.out.println(String.format("Profile changed to %s for %s application.", profile.toUpperCase(), name));
	}

	private void changeCurrentProfileOnSystem(String profile) throws IOException {
		Path pathConfiguration = Paths.get(profiles.get(profile));
		Files.copy(pathConfiguration, mainConfiguration, StandardCopyOption.REPLACE_EXISTING);
	}

	private void changeCurrentProfileOnApplicationConfig(String profile) throws IOException {
		try (Stream<String> currentLines = Files.lines(path)) {
			List<String> updatedLines = currentLines.map(line -> changeCurrentProfileFromLine(line, profile))
				.collect(Collectors.toList());

			Files.write(path, updatedLines);
		}
	}

	private String changeCurrentProfileFromLine(String line, String profile) {
		if (line.contains(CURRENT_CONFIG)) {
			return line.replace(currentProfile, profile);
		}

		return line;
	}


	public static Path defaultPathForApplication(String applicationName) {
		String applicationFile = applicationName.concat(PROPERTIES_SUFFIX);
		return Path.of(CONFIG_MANAGER_HOME).resolve(APPLICATIONS_DIRECTORY).resolve(applicationFile);
	}

	public void showProfiles() {
		System.out.printf("Available profiles for %s application:%n", name.toUpperCase());
		profiles.keySet().forEach(profile -> {
			if (profile.equalsIgnoreCase(currentProfile)) {
				profile += " (current)";
			}
			System.out.printf("- %s%n", profile);
		});
	}

	@Override
	public String toString() {
		return String.format("CurrentProfile: %s; Profiles: %s", currentProfile, profiles);
	}
}
