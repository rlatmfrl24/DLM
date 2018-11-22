package main;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import util.hd.Gallery;

public class RestClient {
	
	//private static String sdlm_url = "http://localhost:3000/db/test";
	private static String sdlm_url = "http://35.233.250.217:3000/db/";
	
	public JsonObject makeSelectJSON(String tbname, String column, String where_value){
		JsonObject json_query = new JsonObject();
		json_query.addProperty("table_name", tbname);
		json_query.addProperty("query_type", "SELECT");
		json_query.addProperty("column", column);
		if(!where_value.isEmpty()) json_query.addProperty("where_value", where_value);
		return json_query;
	}
	
	public JsonObject makeInsertLinkJSON(List<String> link_data) {
		JsonObject json_query = new JsonObject();
		json_query.addProperty("table_name", "tb_link_info");
		json_query.addProperty("query_type", "INSERT");
		json_query.addProperty("column", "(domain, link)");
		
		JsonArray value_array = new JsonArray();
		for(String link : link_data) {
			JsonArray dat = new JsonArray();
			try {
				URL url = new URL(link);
				dat.add(url.getAuthority());
			}catch(MalformedURLException mue) {
				System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
				dat.add("");
			}
			dat.add(link);
			value_array.add(dat);
		}
		json_query.add("values", value_array);
		return json_query;
	}
	
	
	public JsonObject makeInsertBMKJSON(List<String> link_data) {
		JsonObject json_query = new JsonObject();
		json_query.addProperty("table_name", "tb_bookmark_info");
		json_query.addProperty("query_type", "INSERT");
		json_query.addProperty("column", "(domain, link)");
		
		JsonArray value_array = new JsonArray();
		for(String link : link_data) {
			JsonArray dat = new JsonArray();
			try {
				URL url = new URL(link);
				dat.add(url.getAuthority());
			}catch(MalformedURLException mue) {
				System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
				dat.add("");
			}
			dat.add(link);
			value_array.add(dat);
		}
		json_query.add("values", value_array);
		return json_query;
	}
	
	public JsonObject makeInsertGalleryJSON(Gallery gallery) {
		JsonObject json_query = new JsonObject();
		json_query.addProperty("table_name", "tb_hiyobi_info");
		json_query.addProperty("query_type", "INSERT");
		json_query.addProperty("column", "(h_code, h_title, h_url, h_artist, h_group, h_original, h_type, h_keyword, h_path)");
		
		JsonArray value_array = new JsonArray();
		value_array.add(gallery.getCode());
		if(gallery.getTitle()!=null) value_array.add(gallery.getTitle());
		else value_array.add("");
		if(gallery.getUrl()!=null) value_array.add(gallery.getUrl());
		else value_array.add("");
		if(gallery.getArtist()!=null) value_array.add(gallery.getArtist());
		else value_array.add("");
		if(gallery.getGroup()!=null) value_array.add(gallery.getGroup());
		else value_array.add("");
		if(gallery.getOriginal()!=null) value_array.add(gallery.getOriginal());
		else value_array.add("");
		if(gallery.getType()!=null) value_array.add(gallery.getType());
		else value_array.add("");
		if(gallery.getKeyword()!=null) value_array.add(gallery.getKeyword());
		else value_array.add("");
		if(gallery.getPath()!=null) value_array.add(gallery.getPath());
		else value_array.add("");
		JsonArray dat_array = new JsonArray();
		dat_array.add(value_array);
		json_query.add("values", dat_array);

		return json_query;
	}
	
	public JsonObject makeDeleteJSON(String tbname) {
		JsonObject json_query = new JsonObject();
		json_query.addProperty("table_name", tbname);
		json_query.addProperty("query_type", "DELETE");
		
		return json_query;
	}

	public JsonElement post_json(JsonObject jobj) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(sdlm_url);
			StringEntity params = new StringEntity(jobj.toString(), "UTF-8");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Accept-Encoding", "UTF-8");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			if(response != null) {
				InputStream in = response.getEntity().getContent();
				JsonParser parser = new JsonParser();
				return parser.parse(new InputStreamReader(in, "UTF-8"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getListByColumn(String table_name, String column){
		JsonElement je = post_json(makeSelectJSON(table_name, column, ""));
		List<String> result_list = new ArrayList<>();
		if(je.isJsonArray()) {
			for(JsonElement element : (JsonArray)je) {
				JsonObject jdat = (JsonObject) element;
				result_list.add(jdat.get(column).getAsString());
			}
		}else {
			return null;
		}
		
		return result_list;
	}
	
	public List<String> getListByColumn(String table_name, String column, String where_value){
		JsonElement je = post_json(makeSelectJSON(table_name, column, where_value));
		List<String> result_list = new ArrayList<>();
		if(je.isJsonArray()) {
			for(JsonElement element : (JsonArray)je) {
				JsonObject jdat = (JsonObject) element;
				result_list.add(jdat.get(column).getAsString());
			}
		}else {
			return null;
		}
		
		return result_list;
	}
	
	public void UpdateBMK(String table_name, List<String> list) {
		post_json(makeDeleteJSON(table_name));
		if(list.size() != 0) {
			post_json(makeInsertBMKJSON(list));
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestClient con = new RestClient();
		
		for(String code : con.getListByColumn("tb_hiyobi_info", "h_code")) {
			System.out.println(code);
		}
		//System.out.println(con.getListByColumn("tb_hiyobi_info", "h_code"));
		//System.out.println(con.getListByColumn("tb_link_info", "link", "domain like 'www.dogdrip.net'"));
	}

}
