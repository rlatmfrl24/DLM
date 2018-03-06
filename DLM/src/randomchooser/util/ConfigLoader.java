package randomchooser.util;

import java.io.FileInputStream;
import java.util.Observable;
import java.util.Properties;

public class ConfigLoader extends Observable {
	
    private Properties properties;

    public ConfigLoader() {
		// TODO Auto-generated constructor stub
        properties = new Properties();
	}
    
    public Properties getProperties() {
        return properties;
    }
	
    public void broadcast() {
    	setChanged();
    	notifyObservers();
    }
    
	public void loadConfig(String path) throws Exception{
		properties.load(new FileInputStream(path));
		setChanged();
		notifyObservers();
	}
	
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
	
	public String GetCurrentPath() {
		return properties.getProperty("TargetPath");
	}
	public String GetImageViewerPath() {
		return properties.getProperty("ImageViewerPath");
	}
	public String GetVideoViewerPath() {
		return properties.getProperty("VideoViewerPath");
	}
}
