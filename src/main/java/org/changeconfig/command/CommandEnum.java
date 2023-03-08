package org.changeconfig.command;

import org.changeconfig.command.executor.CommandExecutor;
import org.changeconfig.command.executor.ShowCommandExecutor;
import org.changeconfig.command.executor.UseCommandExecutor;

import java.util.Arrays;

public enum CommandEnum {
	USE("use") {
		@Override
		public CommandExecutor getExecutor() {
			return new UseCommandExecutor();
		}
	},
	SHOW("show") {
		@Override
		public CommandExecutor getExecutor() {
			return new ShowCommandExecutor();
		}
	};

	private final String name;

	CommandEnum(String name) {
		this.name = name;
	}

	public abstract CommandExecutor getExecutor();

	public String getName() {
		return name;
	}

	public static boolean isValid(String command) {
		return Arrays.stream(CommandEnum.values())
			.map(CommandEnum::getName)
			.anyMatch(command::equals);
	}
}
