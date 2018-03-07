package util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoCategorizer {
	
	public String GetCategorizedName(File file) {
		ArrayList<String> keylist = new ArrayList<>();
		//String origin_name = file.getName();
		String transform_name = file.getName();
		String tmp = file.getName();
		String directory = "";
		
		Pattern p = Pattern.compile("\\[(.*?)\\]|\\((.*?)\\)");
		Matcher m = p.matcher(tmp);
		while(m.find()) {
			keylist.add(m.group(0));
		}
		
		for(int i=0; i<keylist.size(); i++) {
			String keyword = keylist.get(i);
			if (keyword.toLowerCase().contains("korean") || 
				keyword.toLowerCase().contains("decensored") || 
				keyword.toLowerCase().contains("[cg]") ||
				keyword.matches("[\\[\\(][0-9]+[\\]\\)]") || 
				keyword.matches("\\[(C[0-9].*?)\\]|\\((C[0-9].*?)\\)") ||
				keyword.contains("번역") ||
				keyword.contains("[한]") ||
				keyword.contains("망가") ||
				keyword.contains("만화") ||
				keyword.contains("한글") ||
				keyword.contains("풀컬러")) {
				transform_name = transform_name.replace(keyword, "");
			}else if(keyword.contains("동인지") || keyword.contains("동인")) {
				directory+="동인지/";
			}else if(keyword.contains("[")) {
				directory+=keyword.substring(1, keyword.length()-1).trim()+"/";
				//transform_name = transform_name.replace(keyword, "");
			}else if(keyword.contains("(") && transform_name.trim().startsWith(keyword)) {
				directory+=keyword.replaceAll("[\\(|\\)|\\[|\\]]", "")+"/";
				//transform_name = transform_name.replace(keyword, "");
			}
		}
		if(directory.length() < 1) {
			directory+="미분류/";
		}
		return directory+transform_name;
	}
}
