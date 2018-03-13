package test;

import java.io.File;

public class test {
	private static String default_explorer_path = "explorer.exe /select,";
	static String command_explorer = default_explorer_path;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String target = "G:/test/새 폴더/[빅데이터팀]  Category_Corpus_수집현황_20171120.xlsx";
		
		open_explorer(target);
		//open_other(target);

	}

	public static void open_explorer(String target){
		try{
			File f = new File(target);
			String command = command_explorer+f.getAbsolutePath();
			System.out.println(command);
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);
			p.waitFor();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static void open_other(String target) {
		try {
			//Desktop.getDesktop().open(new File(target));
			File f = new File(target);
			String[] cmd = {"cmd", "/c", "start explorer.exe /select," + f.getAbsolutePath()};
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(cmd);
			p.waitFor();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
