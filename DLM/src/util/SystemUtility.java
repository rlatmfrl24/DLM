package randomchooser.util;

import java.util.Observable;
import java.util.Observer;

public class SystemUtility implements Observer {

	private String default_explorer_path = "explorer.exe /select,";
	private String default_hv_path = System.getenv("PROGRAMFILES")+"/Honeyview/Honeyview.exe ";
	private String default_pp_path = System.getenv("PROGRAMFILES")+"/DAUM/PotPlayer/PotPlayerMini64.exe ";
	String command_explorer = default_explorer_path;
	String command_imgviewer = default_hv_path;
	String command_movviewer = default_pp_path;
	
	public void open_explorer(String target){
		try{
			String command = command_explorer+target;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);
			p.waitFor();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void open_imgview(String target) {
		try{
			String command = command_imgviewer+target;
			System.out.println(command);
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);
			p.waitFor();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void open_movview(String target) {
		try{
			String command = command_movviewer+target;
			Runtime rt = Runtime.getRuntime();
			rt.exec(command);
			//p.waitFor();
		}catch(Exception e){
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
