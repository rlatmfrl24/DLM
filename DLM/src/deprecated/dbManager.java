package deprecated;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.hd.Gallery;

public class dbManager {
	private static Connection connection;
	private static String DriverName = "com.mysql.cj.jdbc.Driver";
	private static String dbURL = "jdbc:mysql://35.233.250.217:3306/";
	private static String db_option = "?useUnicode=yes&characterEncoding=UTF-8";
	private static String db_id = "root";
	private static String db_pwd = "Love397!@";
	private static String dbName;
	
	public dbManager() {
	}
	
	public void Connect(String name) {
		dbName = name;
		try {
			Class.forName(DriverName).newInstance();
			connection = DriverManager.getConnection(dbURL+dbName+db_option, db_id, db_pwd);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getDataFromDB(String column, String tbname){
		List<String> list = new ArrayList<>();
		try {
			String sql = "SELECT "+column+" FROM "+tbname+";";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				list.add(rs.getString(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<String> getLogs(String domain){
		List<String> loglist = new ArrayList<>();
		try {
			String sql = "SELECT link FROM tb_link_info WHERE domain like '"+domain+"';";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				loglist.add(rs.getString(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return loglist;
	}

	public void insertLog(List<String> list) {
		try {
			String sql = "INSERT INTO tb_link_info (domain, link) VALUES (?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			for(String link : list) {
				try {
					URL url = new URL(link);
					stmt.setString(1, url.getAuthority());
				}catch(MalformedURLException mue) {
					System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
					stmt.setString(1, "");
				}
				stmt.setString(2, link);
				stmt.addBatch();
			}
			stmt.executeBatch();
			stmt.close();
		}catch(BatchUpdateException ec_batch) {
			if(ec_batch.getMessage().contains("Duplicate entry")) return;
			else ec_batch.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertLog(String str_url) throws Exception {
		String sql = "INSERT INTO tb_link_info (domain, link) VALUES (?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		try {
			URL url = new URL(str_url);
			stmt.setString(1, url.getAuthority());
		}catch(MalformedURLException mue) {
			System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
			stmt.setString(1, "");
		}
		stmt.setString(2, str_url);
		stmt.addBatch();
		stmt.executeBatch();
		stmt.close();
	} 
	
	public void UpdateBookmark(List<String> list) throws Exception {
		String sql = "DELETE FROM tb_bookmark_info";
		Statement del_stmt = connection.createStatement();
		del_stmt.executeUpdate(sql);
		del_stmt.close();
		sql = "INSERT INTO tb_bookmark_info (domain, link) VALUES (?, ?);";
		PreparedStatement stmt = connection.prepareStatement(sql);
		for(String link : list) {
			URL url = new URL(link);
			stmt.setString(1, url.getAuthority());
			stmt.setString(2, link);
			stmt.addBatch();
		}
		stmt.executeBatch();
		stmt.close();
	}
	
	public void insertDownloadLog(Gallery gal) throws Exception {
		String sql = "INSERT INTO tb_hiyobi_info (h_code, h_title, h_url, h_artist, h_group, h_original, h_type, h_keyword, h_path) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, gal.getCode());
		
		for(int i=2; i<=9; i++) {
			stmt.setNull(i, java.sql.Types.VARCHAR);
		}
		if(gal.getTitle()!=null) stmt.setString(2, gal.getTitle());
		if(gal.getUrl()!=null) stmt.setString(3, gal.getUrl());
		if(gal.getArtist()!=null) stmt.setString(4, gal.getArtist());
		if(gal.getGroup()!=null) stmt.setString(5, gal.getGroup());
		if(gal.getOriginal()!=null) stmt.setString(6, gal.getOriginal());
		if(gal.getType()!=null) stmt.setString(7, gal.getType());
		if(gal.getKeyword()!=null) stmt.setString(8, gal.getKeyword());
		if(gal.getPath()!=null) stmt.setString(9, gal.getPath());
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void ConnectionClose() throws Exception {
		connection.close();
	}
}


