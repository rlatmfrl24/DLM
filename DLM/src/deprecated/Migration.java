package deprecated;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import util.hd.Gallery;

public class Migration {

	public static Connection connection;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Migration dm = new Migration();
		dm.Connect();
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("d:/tb_hiyobi_info.csv"))));
			while((line=br.readLine())!=null) {
				System.out.println(line);
				String[] dat = line.split("\t");
				Gallery g = new Gallery();
				g.setCode(dat[1]);
				g.setTitle(dat[2]);
				g.setUrl(dat[3]);
				g.setArtist(dat[4]);
				g.setGroup(dat[5]);
				g.setOriginal(dat[6]);
				g.setType(dat[7]);
				g.setKeyword(dat[8]);
				g.setPath(dat[9]);
				dm.insertDownloadLog(g);
			}
			br.close();
			connection.close();
			System.out.println("done");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://35.233.230.219:3306/db_trends?useUnicode=yes&characterEncoding=UTF-8", "root", "Love397!@");
		}catch(Exception e) {
			e.printStackTrace();
		}		
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
		System.out.println(stmt.toString());
		stmt.executeUpdate();
		stmt.close();
	}
}
