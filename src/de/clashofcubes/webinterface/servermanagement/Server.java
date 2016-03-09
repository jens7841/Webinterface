package de.clashofcubes.webinterface.servermanagement;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import de.clashofcubes.webinterface.servermanagement.serverfiles.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.versions.Version;

public class Server {

	protected boolean autostart;
	protected File folder;
	protected String name;
	protected Process process;

	private String startParameter; // max memory etc..
	private String stopCommand; // stop or end etc..
	private Version version;

	public Server(String name, File folder, Version version, String startParameter, String stopCommand,
			boolean autostart) {
		this.name = name;
		this.folder = folder;
		this.version = version;
		this.startParameter = startParameter;
		this.stopCommand = stopCommand;
		this.autostart = autostart;
	}

	public void startServer() throws ServerException {
		Runtime runtime = Runtime.getRuntime();

		try {
			this.process = runtime.exec(
					"java " + startParameter + " -jar " + version.getServerFile().getFile().getAbsolutePath(), null,
					folder);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public void stopServer() {
		PrintStream stream = new PrintStream(process.getOutputStream());
		stream.println(stopCommand);
		stream.flush();
	}

	public String getStartParameter() {
		return startParameter;
	}

	public String getStopCommand() {
		return stopCommand;
	}

	public String getName() {
		return name;
	}

	public Process getProcess() {
		return process;
	}

	public Version getVersion() {
		return version;
	}

	public File getServerFolder() {
		return folder;
	}

	public boolean isAutoStart() {
		return autostart;
	}
}