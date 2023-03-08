package org.changeconfig;

import org.changeconfig.command.CommandEnum;
import org.changeconfig.command.executor.CommandExecutor;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		try {
			String option = args[0].toLowerCase();

			if (!CommandEnum.isValid(option)) {
				throw new IllegalArgumentException("Invalid command. Try: cmanager <option> <application> <profile>");
			}

			CommandEnum command = CommandEnum.valueOf(option.toUpperCase());
			CommandExecutor commandExecutor = command.getExecutor();
			commandExecutor.execute(args);
		} catch (IllegalAccessException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
