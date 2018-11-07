package deprecated;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.ConfigLoader;
import util.hd.Gallery;

public class dbManager_old {

	private static String dbPath;
	private static Connection connection;
	
	
	public void Connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
			if(!new File(dbPath).exists()) initialize();
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public boolean isTableExists(String tbname) throws Exception {
		String sql = "SELECT COUNT(*) FROM sqlite_master WHERE name='"+tbname+"';";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs.getBoolean(1);
	}
	
	public void initialize() {
		try {
			Statement stmt = connection.createStatement();
			if(!isTableExists("tb_link_info")) {
				String sql = "CREATE TABLE tb_link_info (" + 
						"idx    INTEGER PRIMARY KEY ASC AUTOINCREMENT, " + 
						"domain VARCHAR, " + 
						"link   TEXT" + 
						");";
				stmt.executeUpdate(sql);
			}
			if(!isTableExists("tb_hiyobi_info")) {
				String sql = "CREATE TABLE tb_hiyobi_info (" + 
						"idx      INTEGER PRIMARY KEY ASC AUTOINCREMENT," + 
						"code     VARCHAR UNIQUE NOT NULL," + 
						"title    TEXT," + 
						"url      TEXT," + 
						"artist   VARCHAR," + 
						"[group]  VARCHAR," + 
						"original VARCHAR," + 
						"type     VARCHAR," + 
						"keyword  TEXT," + 
						"path     TEXT" + 
						");";
				stmt.executeUpdate(sql);
			}
			if(!isTableExists("tb_bookmark_info")) {
				String sql = "CREATE TABLE tb_bookmark_info (" + 
						"idx    INTEGER PRIMARY KEY ASC AUTOINCREMENT, " + 
						"domain VARCHAR, " + 
						"link   TEXT" + 
						");";
				stmt.executeUpdate(sql);
			}
			stmt.close();
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

	public void insertLog(List<String> list) throws Exception {
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
		String sql = "INSERT INTO tb_hiyobi_info (code, title, url, artist, [group], original, type, keyword, path) "
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

