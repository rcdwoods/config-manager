package org.changeconfig.command.executor;

import java.io.IOException;

public interface CommandExecutor {
	void execute(String[] args) throws IOException, IllegalAccessException;
}
