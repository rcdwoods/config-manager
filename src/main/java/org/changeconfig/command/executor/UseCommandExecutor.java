package org.changeconfig.command.executor;

import org.changeconfig.domain.Application;
import org.changeconfig.domain.ConfigManager;

import java.io.IOException;

public class UseCommandExecutor implements CommandExecutor {

	private final ConfigManager configManager = new ConfigManager();
	@Override
	public void execute(String[] args) throws IOException, IllegalAccessException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Invalid command. Try: cmanager use <application> <profile>");
		}

		String applicationName = args[1].toLowerCase();
		String profile = args[2].toLowerCase();

		Application application = configManager.getRegisteredApplication(applicationName);
		application.changeProfile(profile);
	}
}
