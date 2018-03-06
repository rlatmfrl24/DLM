package util;

import java.util.ArrayList;
import java.util.Arrays;

public class Expansion {

	public enum Type {
		IMAGE, MOVIE, MUSIC, SUBTITLE, COMPRESSED
	}
	
	public Type TYPE;
	
	ArrayList<String> expansion_image = new ArrayList<>(
			Arrays.asList(".jpg", ".jpeg", ".gif", ".bmp", ".tiff", ".php", ".png", ".raw", ".pcx", ".ico")
	);
	ArrayList<String> expansion_movie = new ArrayList<>(
			Arrays.asList(".avi", ".wmv", ".mp4", ".mpg", ".mpe", ".asf", ".asx", ".flv", ".mov", ".dat", ".rm", ".mkv", ".mpeg")
	);
	ArrayList<String> expansion_music = new ArrayList<>(
			Arrays.asList(".mp3", ".flac", ".wma", "wav")
	);
	ArrayList<String> expansion_subtitle = new ArrayList<>(
			Arrays.asList(".smi", ".srt")
	);
	ArrayList<String> expansion_compressed = new ArrayList<>(
			Arrays.asList(".zip", ".7z", ".rar", ".gz", ".bz2", ".alz", ".egg", ".lua")
	);
	
	public boolean check_expansion(String s, Type t) {
		
		String input_expansion = "";
		ArrayList<String> check_list = new ArrayList<>();
		
		if(s.contains(".")) {
			input_expansion = s.substring(s.lastIndexOf('.'));			
			switch(t) {
			case IMAGE:
				check_list = expansion_image;
				break;
			case MOVIE:
				check_list = expansion_movie;
				break;
			case MUSIC:
				check_list = expansion_music;
				break;
			case SUBTITLE:
				check_list = expansion_subtitle;
				break;
			case COMPRESSED:
				check_list = expansion_compressed;
				break;
			}
		}else {
			return false;
		}
		
		if(check_list.contains(input_expansion)) return true;
		
		return false;
	}
}
