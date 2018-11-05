package deprecated;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import main.ConfigLoader;

public class Dapina {

	private static DbxRequestConfig dbx_config;
	private static DbxClientV2 client;
	private static ConfigLoader config;
	
	public Dapina(ConfigLoader config) {
		try {
			Dapina.config = config;
			dbx_config = DbxRequestConfig.newBuilder("dropbox/dapina").build();
			client = new DbxClientV2(dbx_config, config.GetDbxToken());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DownloadDB() {
		try {
			OutputStream os_localdb = new FileOutputStream(config.GetLocalDBPath());
			client.files().downloadBuilder(config.GetDbxDBPath()).download(os_localdb);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void UploadDB() {
		try(InputStream io_localdb = new FileInputStream(config.GetLocalDBPath())){
			client.files().uploadBuilder(config.GetDbxDBPath()).uploadAndFinish(io_localdb);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
