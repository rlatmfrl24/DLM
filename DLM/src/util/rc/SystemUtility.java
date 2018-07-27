package util.rc;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import main.ConfigLoader;

public class SystemUtility implements Observer {

	private String default_explorer_path = "explorer.exe /select,";
	private String default_hv_path = System.getenv("PROGRAMFILES")+"/Honeyview/Honeyview.exe ";
	private String default_pp_path = System.getenv("PROGRAMFILES")+"/DAUM/PotPlayer/PotPlayerMini64.exe ";
	String command_explorer = default_explorer_path;
	String command_imgviewer = default_hv_path;
	String command_movviewer = default_pp_path;
	
	public void open_explorer(String target) {
		try {
			File f = new File(target);
			String[] cmd = {"cmd", "/c", "start "+command_explorer+ f.getAbsolutePath()};
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(cmd);
			p.waitFor();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void open_imgview(String target) {
		try{
			File f = new File(target);
			String[] command = {command_imgviewer, f.getAbsolutePath()};
			Runtime rt = Runtime.getRuntime();
			rt.exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void open_movview(String target) {
		try{
			File f = new File(target);
			String[] command = {command_movviewer, f.getAbsolutePath()};
			Runtime rt = Runtime.getRuntime();
			rt.exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void open_browser(String url) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ConfigLoader c = (ConfigLoader) o;
		if(!c.GetImageViewerPath().equals("DEFAULT")) {
			command_imgviewer = c.GetImageViewerPath()+" ";
		}else {
			command_imgviewer = default_hv_path;
		}
		if(!c.GetVideoViewerPath().equals("DEFAULT")) {
			command_movviewer = c.GetVideoViewerPath()+" ";
		}else {
			command_movviewer = default_pp_path;
		}
	}
}
