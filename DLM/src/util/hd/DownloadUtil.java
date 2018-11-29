package util.hd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;

import main.RestClient;
import main.Webdriver;

public class DownloadUtil {

	private RestClient restClient;
	private static Map<String, Gallery> download_list = new HashMap<>();;
	private static File homepath = new File("./hiyobi/");
	private int pbar_selection = 1;
	private static ChromeDriver driver;
	
	public DownloadUtil() {
		this.restClient = new RestClient();
	}
	
	public void closeWebDriver() {
		driver.close();
		driver.quit();
	}
	
	public Map<String, Gallery> getDownloadList(){
		return download_list;
	}
	
	public void GetDownloadList(Table table, int pages) throws Exception {	
		new ProgressMonitorDialog(table.getShell()).run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				// TODO Auto-generated method stub
				try {
					Webdriver wd = new Webdriver();
					driver = wd.getWebDriver();
					monitor.beginTask("Getting List from Hiyobi..", pages+1);
					download_list.clear();
					List<String> skip_list = restClient.getListByColumn("tb_hiyobi_info", "h_code");
					for(int i = 1; i<pages+1; i++) {
						Document doc = Jsoup.connect("https://hiyobi.me/list/"+i).get();
						download_list = getGalleryDataFromPage(doc, skip_list);
						monitor.worked(1);
					}
					table.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(Gallery g : download_list.values()) {
								TableItem item = new TableItem(table, 0);
								item.setText(0, g.getCode());
								item.setText(1, g.getTitle());
								item.setText(2, "Ready..");
							}
						}
					});	
					closeWebDriver();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Map<String, Gallery> getGalleryDataFromPage(Document doc, List<String> skip_list) {
		Map<String, Gallery> gallery_list = download_list;
		try {
			Elements contents = doc.select("div.gallery-content");
			for(Element content : contents) {
				Gallery g = new Gallery();
				g.setTitle(content.select("b").text());
				g.setUrl("https://hiyobi.me"+content.child(0).attr("href"));
				g.setCode(g.getUrl().substring(g.getUrl().lastIndexOf("/")+1));
				if(skip_list.contains(g.getCode())) continue;
				Elements DataSet = content.getElementsByTag("td");
				for(int i=0; i < DataSet.size(); i++) {
					if(DataSet.get(i).text().equals("작가 :")) {
						Pattern p = Pattern.compile("\\((.*?)\\)");
						Matcher m = p.matcher(DataSet.get(i+1).text());
						while(m.find()) {
							g.setGroup(m.group(0).replaceAll("[\\(|\\)]", "").trim());
						}
						if(g.getGroup()!=null) {
							g.setArtist(DataSet.get(i+1).text().replaceAll(g.getGroup(), "").replaceAll("[\\(|\\)]", "").trim());
						}else {
							g.setArtist(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
						}
					}else if(DataSet.get(i).text().equals("원작 :")) {
						g.setOriginal(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
					}else if(DataSet.get(i).text().equals("종류 :")) {
						g.setType(DataSet.get(i+1).text().replaceAll("[\\(|\\)]", "").trim());
					}else if(DataSet.get(i).text().equals("태그 :")) {
						String str_tag = "";
						for(Element tag : DataSet.get(i+1).getElementsByTag("a")) {
							str_tag += "|"+tag.text();
						}
						if(str_tag.length() > 0)str_tag = str_tag.substring(1);
						g.setKeyword(str_tag);
					}
				}
				String tmp="";
				if(g.getArtist() != null) {
					tmp += "["+StringUtils.capitalize(g.getArtist())+"]";
				}
				tmp += g.getTitle().replaceAll("[/|\\|:|\\*|\\?|\"|<|>|\\|]", "_");
				tmp += "("+g.getCode()+")";
				g.setPath(tmp);
				gallery_list.put(g.getCode(), g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gallery_list;
	}
	
	public void ImageDownload(String link, String path, int retry) {
		try {
			path = DuplicateCheck(path);
			InputStream inputStream = null;
			OutputStream outputStream = null;
			URL url = new URL(link);
			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENT);
			inputStream = con.getInputStream();
			outputStream = new FileOutputStream(path);
			byte[] buffer = new byte[2048];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.close();
			inputStream.close();
		}catch(ConnectException ce) {
			retry++;
			System.out.println("["+retry+"]Timeout Exception::Retrying..");
			ImageDownload(link, path, retry);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDownloadByTable(Table table) {
		// TODO Auto-generated method stub
		try {
			ziputil zu = new ziputil();
			final HashMap<String, TableItem> item_map = new HashMap<>();
			Runnable getItemData = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for(TableItem item : table.getItems()) {
						item_map.put(item.getText(0), item);
					}
					synchronized (item_map) {
						item_map.notify();
					}
				}
			};
			table.getDisplay().asyncExec(getItemData);
			synchronized (item_map) {
				try {
					item_map.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Webdriver wd = new Webdriver();
			driver = wd.getWebDriver();
			for(Entry<String, TableItem> entry : item_map.entrySet()) {
				entry.getValue().getDisplay().asyncExec(new Runnable() {
					public void run() {
						table.setSelection(entry.getValue());
						table.showItem(entry.getValue());
						entry.getValue().setText(2, "Fetch..");
					}
				});
				Gallery gal = download_list.get(entry.getKey());
				getGalleryImages(gal, entry.getValue());
				String toPath = homepath.getPath()+"/new/";
				if(gal.getOriginal()!=null && !gal.getOriginal().contains(",")) {
					toPath += gal.getType()+"/"+gal.getOriginal()+"/";
				}else {
					toPath += gal.getType()+"/";
				}
				if(!new File(toPath).exists()) new File(toPath).mkdirs();
				entry.getValue().getDisplay().asyncExec(new Runnable() {
					public void run() {
						entry.getValue().setText(2, "Compressing..");
					}
				});
				zu.createZipFile(homepath.getPath()+"/"+gal.getPath()+"/", toPath, gal.getPath()+".zip");
				deleteDirectory(new File(homepath.getPath()+"/"+gal.getPath()+"/"));
				restClient.post_json(restClient.makeInsertGalleryJSON(gal));
				entry.getValue().getDisplay().asyncExec(new Runnable() {
					public void run() {
						entry.getValue().setText(2, "Done");
					}
				});
			}
			closeWebDriver();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getGalleryImages(Gallery gal, TableItem item) {
		File folder = new File(homepath.getPath()+"/"+gal.getPath()+"/");
		if(!folder.exists()) folder.mkdirs();
		else return;
		
		try {
			Elements img_list = new Elements();
			driver.get(gal.getUrl());
			Thread.sleep(1000);
			Document doc = Jsoup.parse(driver.getPageSource());
			img_list = doc.select(".img-url");
			
			if(img_list.size() == 0) {
				item.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						item.setText(2, "has no images..");
						return;
					}
				});
				return;
			}
			
			pbar_selection = 1;
			int max_selection = img_list.size();
			List<Thread> threads = new ArrayList<>();
			List<Elements> work_pool = new ArrayList<>();
			
			while(img_list.size()>0) {
				Elements tmp = new Elements();
				for(int i=0; i<100 && img_list.size()>0; i++) {
					tmp.add(img_list.get(0));
					img_list.remove(0);
				}
				work_pool.add(tmp);
			}
			
			if(work_pool.size() != 0) {
				for(Elements partial_work : work_pool) {
					for(Element e : partial_work) {
						String save_path = folder.getPath()+"/"+e.text().substring(e.text().lastIndexOf('/'), e.text().length())+".png";
						Runnable task = new Runnable() {					
							@Override
							public void run() {
								ImageDownload(e.text(), save_path, 0);
								item.getDisplay().asyncExec(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										item.setText(2, String.valueOf(pbar_selection)+"/"+String.valueOf(max_selection));
										pbar_selection++;
									}
								});
							}
						};
						Thread download_thread = new Thread(task);
						download_thread.start();
						threads.add(download_thread);
					}
					for(Thread t : threads) {
						t.join();
					}
				}
			}
					

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String DuplicateCheck(String path) {
		File tempForCheck = new File(path);
		String modifiedPath;
		if(tempForCheck.exists()) {
			modifiedPath = DuplicateCheck(path.substring(0, path.lastIndexOf('.'))+" (1)"+path.substring(path.lastIndexOf('.')));
			return modifiedPath;
		}else {
			return path;
		}
	}
	
	public boolean deleteDirectory(File path) {
		if (!path.exists()) return false;
		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) deleteDirectory(file);
		    else file.delete();
		}
		return path.delete();
	}
	
	public void subDirList(String source){
		File dir = new File(source); 
		File[] fileList = dir.listFiles();
		
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					FileDeleteStrategy.FORCE.delete(file);
				}else if(file.isDirectory()){
					subDirList(file.getCanonicalPath().toString()); 
					FileDeleteStrategy.FORCE.delete(file);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

