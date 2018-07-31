package test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class dbManager {

	private static final String dbPath = "./temp/sample.db";
	private static Connection connection;
	
	public void Connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
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
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getHrmLog() throws Exception {
		List<String> loglist = new ArrayList<>();
		String sql = "SELECT link FROM tb_link_info;";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			loglist.add(rs.getString(1));
		}
		return loglist;
	}
	
	public List<String> getBPLog() throws Exception {
		List<String> loglist = new ArrayList<>();
		String sql = "SELECT link FROM tb_link_info WHERE domain like 'v12.battlepage.com';";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			loglist.add(rs.getString(1));
		}
		return loglist;
	}

	public void insertLog(List<String> list) throws Exception {
		String sql = "INSERT INTO tb_link_info (domain, link) VALUES (?, ?)";
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
	
	public void ConnectionClose() throws Exception {
		connection.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		dbManager t = new dbManager();
		t.Connect();
		t.initialize();
		connection.close();
	}
}


