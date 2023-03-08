package org.changeconfig.command.executor;

import org.changeconfig.domain.Application;
import org.changeconfig.domain.ConfigManager;

import java.io.IOException;

public class ShowCommandExecutor implements CommandExecutor {

	private final ConfigManager configManager = new ConfigManager();

	@Override
	public void execute(String[] args) throws IOException, IllegalAccessException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Invalid command. Try: cmanager show <application>");
		}

		String applicationName = args[1].toLowerCase();

		Application application = configManager.getRegisteredApplication(applicationName);
		application.showProfiles();
	}
}
