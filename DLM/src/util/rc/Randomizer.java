package util.rc;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Randomizer {
	
	static ArrayList<File> fl = new ArrayList<>();
	
	/**
	 * 범위 내 랜덤값 추출
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static int randomRange(int n1, int n2) {
	    return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	}
	
	/**
	 * 입력된 경로의 파일 탐색 수행
	 * @param source 파일 탐색을 수행할 경로
	 */
	public void subDirList(String source){
		File dir = new File(source); 
		File[] fileList = dir.listFiles();
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					fl.add(file);
				}else if(file.isDirectory()){
					subDirList(file.getCanonicalPath().toString()); 
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void subDirList(String source, ArrayList<String> allows){
		File dir = new File(source); 
		File[] fileList = dir.listFiles();
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile() && file.getName().contains(".") && allows.contains(file.getName().substring(file.getName().lastIndexOf('.')))){
					fl.add(file);
				}else if(file.isDirectory()){
					subDirList(file.getCanonicalPath().toString(), allows); 
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 입력된 경로에서 지정된 Count 만큼의 FileSet(폴더, 파일명)을 추출하여 HashMap으로 반환
	 * @param target_path FileSet을 추출할 경로
	 * @param count 
	 * @return
	 */
	public HashMap<File, String> getRandomSet(String target_path, int count){
		HashMap<File, String> result = new HashMap<>();
		try{
			fl.clear();
			subDirList(target_path);
			for(int i = 0; i < count && fl.size()>0; i++){
				File popfile = fl.get(randomRange(0, fl.size()-1));
				fl.remove(popfile);
				result.put(popfile, popfile.getParent());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public HashMap<File, String> getRandomSet(String target_path, int count, ArrayList<String> allows){
		HashMap<File, String> result = new HashMap<>();
		try{
			fl.clear();
			subDirList(target_path, allows);
			for(int i = 0; i < count && fl.size()>0; i++){
				File popfile = fl.get(randomRange(0, fl.size()-1));
				fl.remove(popfile);
				result.put(popfile, popfile.getParent());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}


