package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import main.dbManager;
import util.hm.bpupdate;
import util.hm.ddupdate;
import util.hm.hrmupdate;
import util.rc.SystemUtility;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class HMFrame {
	private Table table_hrm;
	private Table table_dd;
	private Table table_bp;
	private hrmupdate hu;
	private bpupdate bp;
	private ddupdate du;
	private dbManager dm;
	private SystemUtility su = new SystemUtility();
	private Table table_bmk;
	private Table table_rw_download;
	private Table table_rw_other;
	private Map<String, String> refreshed_hrm;
	private Map<String, String> refreshed_bp;
	private Map<String, String> refreshed_dd;
	private IRunnableWithProgress loadTask;
	
	public HMFrame(dbManager dm) {
		this.dm = dm;
		loadTask = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				// TODO Auto-generated method stub
				monitor.beginTask("Loading HRM pages..", 3);
				refreshed_hrm = hu.LoadHrm();
				monitor.worked(2);
				monitor.setTaskName("Loading BP pages..");
				refreshed_bp = bp.LoadBP();
				monitor.worked(1);
				monitor.setTaskName("Loading DD pages..");
				refreshed_dd = du.LoadDD();
				monitor.done();
			}
		};
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			dbManager dm = new dbManager();
			dm.Connect();
			dm.initialize();
			HMFrame window = new HMFrame(dm);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Open the window.
	 */
	public void open() {
		hu = new hrmupdate(dm);
		bp = new bpupdate(dm);
		du = new ddupdate(dm);
		Display display = Display.getDefault();
		Shell shlHrm = new Shell();
		shlHrm.setSize(786, 573);
		shlHrm.setText("Trend Checker");
		shlHrm.setLayout(new GridLayout(2, false));
		
		TabFolder tabFolder = new TabFolder(shlHrm, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_tabFolder.heightHint = 436;
		tabFolder.setLayoutData(gd_tabFolder);
		
		TabItem tbtmHrm = new TabItem(tabFolder, SWT.NONE);
		tbtmHrm.setText("Hrm");
		
		Composite composite_hrm = new Composite(tabFolder, SWT.NONE);
		tbtmHrm.setControl(composite_hrm);
		composite_hrm.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer_hrm = new TableViewer(composite_hrm, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
		table_hrm = tableViewer_hrm.getTable();
		GridData gd_table_hrm = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_hrm.heightHint = 471;
		table_hrm.setLayoutData(gd_table_hrm);
		table_hrm.setLinesVisible(true);
		table_hrm.setHeaderVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer_hrm, SWT.NONE);
		TableColumn tblclmnDomain_hrm = tableViewerColumn.getColumn();
		tblclmnDomain_hrm.setWidth(100);
		tblclmnDomain_hrm.setText("Domain");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer_hrm, SWT.NONE);
		TableColumn tblclmnUrl_hrm = tableViewerColumn_1.getColumn();
		tblclmnUrl_hrm.setWidth(100);
		tblclmnUrl_hrm.setText("URL");
		
		Menu menu_hrm = new Menu(table_hrm);
		table_hrm.setMenu(menu_hrm);
		
		MenuItem mntmCheckAllSelected_hrm = new MenuItem(menu_hrm, SWT.NONE);
		mntmCheckAllSelected_hrm.setText("Check All Selected Item");
		MenuItem mntmGoToBookmark_hrm = new MenuItem(menu_hrm, SWT.NONE);
		mntmGoToBookmark_hrm.setText("Go to Bookmark");
		MenuItem mntmOpenSelectedLink_hrm = new MenuItem(menu_hrm, SWT.NONE);
		mntmOpenSelectedLink_hrm.setText("Open Selected link (Enter)");
		
		TabItem tbtmBattlepage = new TabItem(tabFolder, SWT.NONE);
		tbtmBattlepage.setText("Battlepage");
		
		Composite composite_bp = new Composite(tabFolder, SWT.NONE);
		tbtmBattlepage.setControl(composite_bp);
		composite_bp.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer_bp = new TableViewer(composite_bp, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
		table_bp = tableViewer_bp.getTable();
		table_bp.setLinesVisible(true);
		table_bp.setHeaderVisible(true);
		GridData gd_table_bp = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		gd_table_bp.widthHint = 700;
		gd_table_bp.heightHint = 438;
		table_bp.setLayoutData(gd_table_bp);
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer_bp, SWT.NONE);
		TableColumn tblclmnTitle_bp = tableViewerColumn_3.getColumn();
		tblclmnTitle_bp.setWidth(100);
		tblclmnTitle_bp.setText("Title");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer_bp, SWT.NONE);
		TableColumn tblclmnUrl_bp = tableViewerColumn_4.getColumn();
		tblclmnUrl_bp.setWidth(100);
		tblclmnUrl_bp.setText("URL");
		
		Menu menu_bp = new Menu(table_bp);
		table_bp.setMenu(menu_bp);
		
		MenuItem mntmCheckAllSelected_bp = new MenuItem(menu_bp, SWT.NONE);
		mntmCheckAllSelected_bp.setText("Check All Selected Item");
		MenuItem mntmGoToBookmark_bp = new MenuItem(menu_bp, SWT.NONE);
		mntmGoToBookmark_bp.setText("Go to Bookmark");
		MenuItem mntmOpenSelectedLink_bp = new MenuItem(menu_bp, SWT.NONE);
		mntmOpenSelectedLink_bp.setText("Open Selected link (Enter)");
		
		TabItem tbtmDogdrip = new TabItem(tabFolder, SWT.NONE);
		tbtmDogdrip.setText("Dogdrip");
		
		Composite composite_dd = new Composite(tabFolder, SWT.NONE);
		tbtmDogdrip.setControl(composite_dd);
		composite_dd.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer_dd = new TableViewer(composite_dd, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
		table_dd = tableViewer_dd.getTable();
		table_dd.setLinesVisible(true);
		table_dd.setHeaderVisible(true);
		table_dd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer_dd, SWT.NONE);
		TableColumn tblclmnTitle_dd = tableViewerColumn_5.getColumn();
		tblclmnTitle_dd.setWidth(100);
		tblclmnTitle_dd.setText("Title");
		
		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer_dd, SWT.NONE);
		TableColumn tblclmnUrl_dd = tableViewerColumn_6.getColumn();
		tblclmnUrl_dd.setWidth(100);
		tblclmnUrl_dd.setText("URL");
		
		Menu menu_dd = new Menu(table_dd);
		table_dd.setMenu(menu_dd);
		
		MenuItem mntmCheckAllSelected_dd = new MenuItem(menu_dd, SWT.NONE);
		mntmCheckAllSelected_dd.setText("Check All Selected Item");
		MenuItem mntmGoToBookmark_dd = new MenuItem(menu_dd, SWT.NONE);
		mntmGoToBookmark_dd.setText("Go to Bookmark");
		MenuItem mntmOpenSelectedLink_dd = new MenuItem(menu_dd, SWT.NONE);
		mntmOpenSelectedLink_dd.setText("Open Selected link (Enter)");
		
		TabItem tbtmBookmark = new TabItem(tabFolder, SWT.NONE);
		tbtmBookmark.setText("Bookmark");
		
		Composite composite_bmk = new Composite(tabFolder, SWT.NONE);
		tbtmBookmark.setControl(composite_bmk);
		composite_bmk.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer_bmk = new TableViewer(composite_bmk, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		table_bmk = tableViewer_bmk.getTable();
		table_bmk.setLinesVisible(true);
		table_bmk.setHeaderVisible(true);
		table_bmk.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer_bmk, SWT.NONE);
		TableColumn tblclmnDomain = tableViewerColumn_7.getColumn();
		tblclmnDomain.setWidth(100);
		tblclmnDomain.setText("Domain");
		
		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(tableViewer_bmk, SWT.NONE);
		TableColumn tblclmnLink = tableViewerColumn_8.getColumn();
		tblclmnLink.setWidth(100);
		tblclmnLink.setText("Link");
		
		/*
		TabItem tbtmRearwarning = new TabItem(tabFolder, SWT.NONE);
		tbtmRearwarning.setText("RearWarning");
		
		Composite composite_rw = new Composite(tabFolder, SWT.NONE);
		tbtmRearwarning.setControl(composite_rw);
		composite_rw.setLayout(new GridLayout(1, false));
		
		Label lblDownloadList = new Label(composite_rw, SWT.NONE);
		lblDownloadList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblDownloadList.setText("Download List");
		
		TableViewer tableViewer = new TableViewer(composite_rw, SWT.BORDER | SWT.FULL_SELECTION);
		table_rw_download = tableViewer.getTable();
		table_rw_download.setHeaderVisible(true);
		table_rw_download.setLinesVisible(true);
		table_rw_download.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		TableColumn tblclmnDomain_1 = new TableColumn(table_rw_download, SWT.NONE);
		tblclmnDomain_1.setWidth(100);
		tblclmnDomain_1.setText("Domain");
		
		TableColumn tblclmnTitle_1 = new TableColumn(table_rw_download, SWT.NONE);
		tblclmnTitle_1.setWidth(100);
		tblclmnTitle_1.setText("Title");
		
		TableColumn tblclmnLink_1 = new TableColumn(table_rw_download, SWT.NONE);
		tblclmnLink_1.setWidth(100);
		tblclmnLink_1.setText("Link");
		
		Label lblNonimageLinkList = new Label(composite_rw, SWT.NONE);
		lblNonimageLinkList.setText("Non-Image Link List");
		
		TableViewer tableViewer_1 = new TableViewer(composite_rw, SWT.BORDER | SWT.FULL_SELECTION);
		table_rw_other = tableViewer_1.getTable();
		table_rw_other.setHeaderVisible(true);
		table_rw_other.setLinesVisible(true);
		table_rw_other.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		TableColumn tblclmnDomain_2 = new TableColumn(table_rw_other, SWT.NONE);
		tblclmnDomain_2.setWidth(100);
		tblclmnDomain_2.setText("Domain");
		
		TableColumn tblclmnTitle_2 = new TableColumn(table_rw_other, SWT.NONE);
		tblclmnTitle_2.setWidth(100);
		tblclmnTitle_2.setText("Title");
		
		TableColumn tblclmnLink_2 = new TableColumn(table_rw_other, SWT.NONE);
		tblclmnLink_2.setWidth(100);
		tblclmnLink_2.setText("Link");
		*/

		for(String link : dm.getDataFromDB("link", "tb_bookmark_info")) {
			try {
				URL url = new URL(link);
				TableItem item = new TableItem(table_bmk, 0);
				item.setText(0, url.getAuthority());
				item.setText(1, link);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		Composite composite_1 = new Composite(shlHrm, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite_1.setLayout(new GridLayout(3, false));

		Button btnUpdate = new Button(composite_1, SWT.NONE);
		btnUpdate.setText("Update");
		
		Button btnRefresh = new Button(composite_1, SWT.NONE);
		btnRefresh.setText("Refresh");
		
		Label lblReady = new Label(composite_1, SWT.NONE);
		lblReady.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				System.out.println(display.getShells()[0].getText());
			}
		});
		lblReady.setAlignment(SWT.RIGHT);
		GridData gd_lblReady = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_lblReady.widthHint = 640;
		lblReady.setLayoutData(gd_lblReady);
		lblReady.setText("Ready..");

		
		// Listener Part
		MouseAdapter doubleclickAdapter = new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Table current_table = getCurrentTable(tabFolder);	
				if(current_table.getSelectionCount() > 0) {
					String link = current_table.getSelection()[0].getText(1);
					System.out.println(link);
					su.open_browser(link);
					//table_hrm.getSelection()[0].setChecked(true);
					updateItem(current_table, current_table.getItem(current_table.getSelectionIndex()));
				}
			}
		};
		SelectionAdapter CheckAllSelectedAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table current_table = getCurrentTable(tabFolder);
				for(TableItem selected : current_table.getSelection()) {
					selected.setChecked(true);
				}
			}
		};
		SelectionAdapter GotoBookmarkAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRefresh.setEnabled(false);
				btnUpdate.setEnabled(false);
				Table current_table = getCurrentTable(tabFolder);
				List<String> list = new ArrayList<>();
				for(TableItem ti : table_bmk.getItems()) list.add(ti.getText(1));
				try {
					for(TableItem selected : current_table.getSelection()) {
						URL url = new URL(selected.getText(1));
						TableItem bmk_item = new TableItem(table_bmk, 0);
						bmk_item.setText(1, selected.getText(1));
						bmk_item.setText(0, url.getAuthority());
						list.add(selected.getText(1));
						current_table.remove(current_table.indexOf(selected));
					}
					dm.insertLog(list);
					dm.UpdateBookmark(list);
					btnRefresh.setEnabled(true);
					btnUpdate.setEnabled(true);
				}catch(Exception bme) {
					bme.printStackTrace();
				}
			}
		};
		SelectionAdapter OpenSelectedLinkAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnRefresh.setEnabled(false);
					btnUpdate.setEnabled(false);
					Table current_table = getCurrentTable(tabFolder);
					List<String> open_list = new ArrayList<>();
					for(TableItem selected : current_table.getSelection()) {
						su.open_browser(selected.getText(1));
						open_list.add(selected.getText(1));
						current_table.remove(current_table.indexOf(selected));
					}
					dm.insertLog(open_list);
					btnRefresh.setEnabled(true);
					btnUpdate.setEnabled(true);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		table_hrm.addMouseListener(doubleclickAdapter);
		table_bp.addMouseListener(doubleclickAdapter);
		table_dd.addMouseListener(doubleclickAdapter);
		table_bmk.addMouseListener(doubleclickAdapter);
		
		mntmCheckAllSelected_hrm.addSelectionListener(CheckAllSelectedAdapter);
		mntmCheckAllSelected_bp.addSelectionListener(CheckAllSelectedAdapter);
		mntmCheckAllSelected_dd.addSelectionListener(CheckAllSelectedAdapter);
		
		mntmGoToBookmark_hrm.addSelectionListener(GotoBookmarkAdapter);
		mntmGoToBookmark_bp.addSelectionListener(GotoBookmarkAdapter);
		mntmGoToBookmark_dd.addSelectionListener(GotoBookmarkAdapter);
		
		mntmOpenSelectedLink_hrm.addSelectionListener(OpenSelectedLinkAdapter);
		mntmOpenSelectedLink_bp.addSelectionListener(OpenSelectedLinkAdapter);
		mntmOpenSelectedLink_dd.addSelectionListener(OpenSelectedLinkAdapter);

		//단축키 동작 설정
		display.addFilter(SWT.KeyDown, new Listener() {	
			@Override
			public void handleEvent(Event e) {
				if(shlHrm.isDisposed()) {
					display.removeFilter(SWT.KeyDown, this);
					return;
				}
				// TODO Auto-generated method stub
				Table current_table = getCurrentTable(tabFolder);	
				if((e.stateMask & SWT.CTRL)==SWT.CTRL && e.keyCode=='v' && tabFolder.getSelectionIndex()==3) {
					try {
						String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
						System.out.println(data);
						URL url = new URL(data);
						
						List<String> list_bmk = new ArrayList<>();
						for(TableItem item : table_bmk.getItems()) {
							list_bmk.add(item.getText(1));
						}
						if(!list_bmk.contains(data)) {
							TableItem item = new TableItem(table_bmk, 0);
							item.setText(1, data);
							item.setText(0, url.getAuthority());
						}
					} catch (MalformedURLException me) {  
				        System.out.println("URL is not vaild");
				    } catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnsupportedFlavorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(e.keyCode==SWT.DEL) {
					if(table_bmk.getSelectionCount()>0) {
						table_bmk.remove(table_bmk.getSelectionIndices());
					}
				}else if((e.stateMask & SWT.CTRL)==SWT.CTRL && e.keyCode=='a') {
					try {
						current_table.setSelection(current_table.getItems());
					} catch (Exception e1) {  
				        e1.printStackTrace();
				    }
				}else if(e.keyCode==SWT.CR || e.keyCode==SWT.KEYPAD_CR) {
					try {
						btnRefresh.setEnabled(false);
						btnUpdate.setEnabled(false);
						if(current_table.getSelectionCount() > 0) {
							List<String> visit_list = new ArrayList<>();
							for(TableItem item : current_table.getSelection()) {
								String link = item.getText(1);
								visit_list.add(link);
								System.out.println(link);
								su.open_browser(link);
								current_table.remove(current_table.indexOf(item));
								//item.setChecked(true);
							}
							dm.insertLog(visit_list);
						}
						btnRefresh.setEnabled(true);
						btnUpdate.setEnabled(true);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		//Resize Listener
		tabFolder.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				Rectangle area = tabFolder.getClientArea();

				Point preferredSize = table_hrm.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table_hrm.getBorderWidth();
				if (preferredSize.y > area.height + table_hrm.getHeaderHeight()) {
					Point vBarSize = table_hrm.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table_hrm.getSize();
				if (oldSize.x > area.width) {
					tblclmnDomain_hrm.setWidth(width / 4);
					tblclmnUrl_hrm.setWidth(width - tblclmnDomain_hrm.getWidth());
					table_hrm.setSize(area.width, area.height);
				} else {
					table_hrm.setSize(area.width, area.height);					
					tblclmnDomain_hrm.setWidth(width / 4);
					tblclmnUrl_hrm.setWidth(width - tblclmnDomain_hrm.getWidth());
				}
				
				preferredSize = table_bp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				width = area.width - 2 * table_bp.getBorderWidth();
				if (preferredSize.y > area.height + table_bp.getHeaderHeight()) {
					Point vBarSize = table_bp.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				oldSize = table_bp.getSize();
				if (oldSize.x > area.width) {
					tblclmnTitle_bp.setWidth(width / 2);
					tblclmnUrl_bp.setWidth(width - tblclmnTitle_bp.getWidth());
					table_bp.setSize(area.width, area.height);
				} else {
					table_bp.setSize(area.width, area.height);					
					tblclmnTitle_bp.setWidth(width / 2);
					tblclmnUrl_bp.setWidth(width - tblclmnTitle_bp.getWidth());
				}
				
				preferredSize = table_dd.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				width = area.width - 2 * table_dd.getBorderWidth();
				if (preferredSize.y > area.height + table_dd.getHeaderHeight()) {
					Point vBarSize = table_dd.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				oldSize = table_dd.getSize();
				if (oldSize.x > area.width) {
					tblclmnTitle_dd.setWidth(width / 2);
					tblclmnUrl_dd.setWidth(width - tblclmnTitle_dd.getWidth());
					table_dd.setSize(area.width, area.height);
				} else {
					table_dd.setSize(area.width, area.height);					
					tblclmnTitle_dd.setWidth(width / 2);
					tblclmnUrl_dd.setWidth(width - tblclmnTitle_dd.getWidth());
				}
				
				preferredSize = table_bmk.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				width = area.width - 2 * table_bmk.getBorderWidth();
				if (preferredSize.y > area.height + table_bmk.getHeaderHeight()) {
					Point vBarSize = table_bmk.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				oldSize = table_bmk.getSize();
				if (oldSize.x > area.width) {
					tblclmnDomain.setWidth(width / 4);
					tblclmnLink.setWidth(width - tblclmnDomain.getWidth());
					table_bmk.setSize(area.width, area.height);
				} else {
					table_bmk.setSize(area.width, area.height);					
					tblclmnDomain.setWidth(width / 4);
					tblclmnLink.setWidth(width - tblclmnDomain.getWidth());
				}
			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		//Refresh 버튼 동작
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openQuestion(shlHrm, "목록 초기화", "전체 목록을 삭제하고 다시 로드합니다. 괜찮습니까?")) {
					open_load(shlHrm);
				}
			}
		});
		
		//Update 버튼 동작
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnRefresh.setEnabled(false);
					btnUpdate.setEnabled(false);
					List<String> visited = new ArrayList<>();
					List<Integer> selected_idx = new ArrayList<>();
					int[] remove_idx;
					
					Table current_table = getCurrentTable(tabFolder);
					for(TableItem item : current_table.getItems()) {
						if(item.getChecked()) {
							visited.add(item.getText(1));
							selected_idx.add(current_table.indexOf(item));
						}
					}
					remove_idx = new int[selected_idx.size()];
					for(int i = 0; i < selected_idx.size(); i++) remove_idx[i] = selected_idx.get(i);
					current_table.remove(remove_idx);
					dm.insertLog(visited);
					btnRefresh.setEnabled(true);
					btnUpdate.setEnabled(true);
					lblReady.setText("Log Updated.");
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		shlHrm.open();
		shlHrm.layout();
		
		Thread thread_load = new Thread(new Runnable() {
			public void run() {
				open_load(shlHrm);
			}
		});
		thread_load.start();
		while (!shlHrm.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void updateItem(Table table, TableItem item) {
		try {
			if(table.equals(table_bp)) dm.insertLog(item.getText(2));
			else dm.insertLog(item.getText(1));
			table.remove(table.indexOf(item));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Table getCurrentTable(TabFolder tabFolder) {
		Table current_table=null;
		switch (tabFolder.getSelectionIndex()) {
		case 0:
			current_table = table_hrm;
			break;
		case 1:
			current_table = table_bp;
			break;
		case 2:
			current_table = table_dd;
			break;
		case 3:
			current_table = table_bmk;
			break;
		case 4:
			current_table = table_rw_download;
		default:
			break;
		}
		return current_table;
	}
	

	
	public void open_load(Shell shlHrm) {
		
		shlHrm.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new ProgressMonitorDialog(shlHrm).run(true, false, loadTask);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					MessageDialog.openError(shlHrm, "Error", e.getMessage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					MessageDialog.openInformation(shlHrm, "Cancelled", e.getMessage());
				}
				
				for(Entry<String, String> entry : refreshed_bp.entrySet()) {
					TableItem ti = new TableItem(table_bp, 0);
					ti.setText(0, entry.getValue());
					ti.setText(1, entry.getKey());
				}
				for(Entry<String, String> entry : refreshed_hrm.entrySet()) {
					TableItem ti = new TableItem(table_hrm, 0);
					ti.setText(0, entry.getValue());
					ti.setText(1, entry.getKey());
				}
				for(Entry<String, String> entry : refreshed_dd.entrySet()) {
					TableItem ti = new TableItem(table_dd, 0);
					ti.setText(0, entry.getValue());
					ti.setText(1, entry.getKey());
				}
			}
		});
	}
}