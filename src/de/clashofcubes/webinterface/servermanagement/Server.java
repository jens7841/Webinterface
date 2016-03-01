package de.clashofcubes.webinterface.servermanagement;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import de.clashofcubes.webinterface.servermanagement.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;

public class Server {

	private String name; // name
	private File folder; // server folder
	private Process process; // runtime process
	private ServerFile execServerFile; // eg spigot.jar craftbukkit.jar
	private String startParameter; // max memory etc..
	private String stopCommand;
	private boolean autostart;

	public Server(String name, File folder, ServerFile serverFile, String startParameter, String stopCommand,
			boolean autostart) {
		this.name = name;
		this.folder = folder;
		this.execServerFile = serverFile;
		this.startParameter = startParameter;
		this.stopCommand = stopCommand;
		this.autostart = autostart;
	}

	public void startServer() throws ServerException {
		Runtime runtime = Runtime.getRuntime();

		try {
			this.process = runtime.exec(
					"java " + startParameter + " -jar " + execServerFile.getFile().getAbsolutePath(), null, folder);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public Process getProcess() {
		return process;
	}

	public void stopServer() {
		PrintStream stream = new PrintStream(process.getOutputStream());
		stream.println(stopCommand);
		stream.flush();
	}

	public File getFolder() {
		return folder;
	}

	public ServerFile getServerFile() {
		return execServerFile;
	}

	public String getStartParameter() {
		return startParameter;
	}

	public String getStopCommand() {
		return stopCommand;
	}

	public boolean isAutostart() {
		return autostart;
	}

}