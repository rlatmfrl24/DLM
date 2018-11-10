package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Properties;

public class ConfigLoader extends Observable {

	private Properties properties;
	private static final String Default_TempPath = "./temp";
	private static final String Default_TargetPath = "./";
	private static final String Default_HiyobiPath = "./hiyobi/";
	private static final String Default_deletedItemPath = "./temp/deleted/";
	private static final String Default_movedItemPath = "./temp/moved/";
	private static final String Default_DbxAccessToken = "FMwnuyPWJdsAAAAAAABzAyZ1u1lAOczChSN0XSXJNfHmmUKUPrk-gS49cfTtSdg0";

	public ConfigLoader(String path) {
		// TODO Auto-generated constructor stub
		try {
			// 1. Check for Properties File
			File checkFile = new File(path);
			if (!checkFile.exists()) {
				initialize();
			}
			properties = new Properties();
			properties.load(new FileInputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize() throws Exception {
		new File(Default_TempPath).mkdirs();
		File configFile = new File(Default_TempPath + "/config.properties");
		new File(Default_deletedItemPath).mkdirs();
		new File(Default_movedItemPath).mkdirs();
		new File(Default_HiyobiPath).mkdirs();

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
		bw.write("TargetPath=" + Default_TargetPath);
		bw.newLine();
		bw.write("DeletedPath=" + Default_deletedItemPath);
		bw.newLine();
		bw.write("MovedPath=" + Default_movedItemPath);
		bw.newLine();
		bw.write("HiyobiPath=" + Default_HiyobiPath);
		bw.newLine();
		bw.write("ImageViewerPath=DEFAULT");
		bw.newLine();
		bw.write("VideoViewerPath=DEFAULT");
		bw.newLine();
		bw.write("DbxAccessToken=" + Default_DbxAccessToken);
		bw.newLine();
		bw.flush();
		bw.close();
	}

	public Properties getProperties() {
		return properties;
	}

	public void broadcast() {
		setChanged();
		notifyObservers();
	}

	public void loadConfig(String path) throws Exception {
		properties.load(new FileInputStream(path));
		setChanged();
		notifyObservers();
	}

	//Setter Method
	public void setCurrentPath(String path) {
		properties.setProperty("TargetPath", path);
		setChanged();
		notifyObservers();
	}

	public void setImageViewerPath(String path) {
		properties.setProperty("ImageViewerPath", path);
		setChanged();
		notifyObservers();
	}

	public void setVideoViewerPath(String path) {
		properties.setProperty("VideoViewerPath", path);
		setChanged();
		notifyObservers();
	}

	public void setDbxToken(String token) {
		properties.setProperty("DbxAccessToken", token);
		setChanged();
		notifyObservers();
	}

	//Getter Method
	public String GetCurrentPath() {
		return properties.getProperty("TargetPath");
	}

	public String GetImageViewerPath() {
		return properties.getProperty("ImageViewerPath");
	}

	public String GetVideoViewerPath() {
		return properties.getProperty("VideoViewerPath");
	}

	public String GetDbxToken() {
		return properties.getProperty("DbxAccessToken");
	}
}
