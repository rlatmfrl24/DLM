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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import main.Manager;
import main.dbManager;
import util.hm.bpupdate;
import util.hm.ddupdate;
import util.hm.hrmupdate;
import util.rc.SystemUtility;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
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
	
	public HMFrame(dbManager dm) {
		this.dm = dm;
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
		shlHrm.setText("갱신");
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
		table_hrm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table_hrm.getSelectionCount() > 0) {
					String link = table_hrm.getSelection()[0].getText(1);
					System.out.println(link);
					su.open_browser(link);
					table_hrm.getSelection()[0].setChecked(true);
				}
			}
		});
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer_hrm, SWT.NONE);
		TableColumn tblclmnCategory = tableViewerColumn.getColumn();
		tblclmnCategory.setWidth(100);
		tblclmnCategory.setText("Category");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer_hrm, SWT.NONE);
		TableColumn tblclmnUrl_hrm = tableViewerColumn_1.getColumn();
		tblclmnUrl_hrm.setWidth(100);
		tblclmnUrl_hrm.setText("URL");
		
		Menu menu_hrm = new Menu(table_hrm);
		table_hrm.setMenu(menu_hrm);
		
		MenuItem mntmCheckAllSelected = new MenuItem(menu_hrm, SWT.NONE);
		mntmCheckAllSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_hrm.getSelection()) {
					selected.setChecked(true);
				}
			}
		});
		mntmCheckAllSelected.setText("Check All Selected Item");
		MenuItem mntmOpenSelectedLink = new MenuItem(menu_hrm, SWT.NONE);
		mntmOpenSelectedLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_hrm.getSelection()) {
					su.open_browser(selected.getText(1));
					selected.setChecked(true);
				}
			}
		});
		mntmOpenSelectedLink.setText("Open Selected link");
		
		

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
		table_bp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table_bp.getSelectionCount() > 0) {
					String link = table_bp.getSelection()[0].getText(2);
					System.out.println(link);
					su.open_browser(link);
					table_bp.getSelection()[0].setChecked(true);
				}
			}
		});
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_bp, SWT.NONE);
		TableColumn tblclmnBoard = tableViewerColumn_2.getColumn();
		tblclmnBoard.setWidth(100);
		tblclmnBoard.setText("Board");
		
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
		
		MenuItem menuItem = new MenuItem(menu_bp, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_bp.getSelection()) {
					selected.setChecked(true);
				}
			}
		});
		menuItem.setText("Check All Selected Item");
		
		MenuItem menuItem_1 = new MenuItem(menu_bp, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_bp.getSelection()) {
					su.open_browser(selected.getText(2));
					selected.setChecked(true);
				}
			}
		});
		menuItem_1.setText("Open Selected link");
		
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
		table_dd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table_dd.getSelectionCount() > 0) {
					String link = table_dd.getSelection()[0].getText(1);
					System.out.println(link);
					su.open_browser(link);
					table_dd.getSelection()[0].setChecked(true);
				}
			}
		});
		
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
		
		MenuItem menuItem_2 = new MenuItem(menu_dd, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_dd.getSelection()) {
					selected.setChecked(true);
				}
			}
		});
		menuItem_2.setText("Check All Selected Item");
		
		MenuItem menuItem_3 = new MenuItem(menu_dd, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem selected : table_dd.getSelection()) {
					su.open_browser(selected.getText(1));
					selected.setChecked(true);
				}
			}
		});
		menuItem_3.setText("Open Selected link");
		
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
		
		table_bmk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table_bmk.getSelectionCount() > 0) {
					String link = table_bmk.getSelection()[0].getText(1);
					System.out.println(link);
					su.open_browser(link);
					table_bmk.getSelection()[0].setChecked(true);
				}
			}
		});
		
		
		display.addFilter(SWT.KeyDown, new Listener() {	
			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
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
				default:
					break;
				}
				
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
					if(current_table.getSelectionCount() > 0) {
						for(TableItem item : current_table.getSelection()) {
							String link;
							if(current_table == table_bp) {
								link = item.getText(2);
							}else {
								link = item.getText(1);
							}
							System.out.println(link);
							su.open_browser(link);
							item.setChecked(true);
						}
					}
				}
			}
		});

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
					tblclmnCategory.setWidth(width / 4);
					tblclmnUrl_hrm.setWidth(width - tblclmnCategory.getWidth());
					table_hrm.setSize(area.width, area.height);
				} else {
					table_hrm.setSize(area.width, area.height);					
					tblclmnCategory.setWidth(width / 4);
					tblclmnUrl_hrm.setWidth(width - tblclmnCategory.getWidth());
				}
				
				preferredSize = table_bp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				width = area.width - 2 * table_bp.getBorderWidth();
				if (preferredSize.y > area.height + table_bp.getHeaderHeight()) {
					Point vBarSize = table_bp.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				oldSize = table_bp.getSize();
				if (oldSize.x > area.width) {
					tblclmnBoard.setWidth(width / 5);
					tblclmnTitle_bp.setWidth(width / 5 * 2);
					tblclmnUrl_bp.setWidth(width - tblclmnBoard.getWidth() - tblclmnTitle_bp.getWidth());
					table_bp.setSize(area.width, area.height);
				} else {
					table_bp.setSize(area.width, area.height);					
					tblclmnBoard.setWidth(width / 5);
					tblclmnTitle_bp.setWidth(width / 5 * 2);
					tblclmnUrl_bp.setWidth(width - tblclmnBoard.getWidth() - tblclmnTitle_bp.getWidth());
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
		GridData gd_lblReady = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblReady.widthHint = 640;
		lblReady.setLayoutData(gd_lblReady);
		lblReady.setText("Ready..");

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String, List<String>> refreshed;
				btnRefresh.setEnabled(false);
				switch(tabFolder.getSelectionIndex()) {
				case 0:
					//Hrm
					table_hrm.removeAll();
					lblReady.setText("Refreshing Hrm Pages..");
					refreshed = hu.LoadHrm();
					for(String tag : refreshed.keySet()) {
						for(String url : refreshed.get(tag)) {
							TableItem ti = new TableItem(table_hrm, 0);
							ti.setText(0, tag);
							ti.setText(1, url);
						}
					}
					break;
				case 1:
					//BP
					table_bp.removeAll();
					lblReady.setText("Refreshing BP Pages..");
					refreshed = bp.LoadBP();
					for(String id : refreshed.keySet()) {
						TableItem ti = new TableItem(table_bp, 0);
						ti.setText(0, refreshed.get(id).get(0));
						ti.setText(1, refreshed.get(id).get(1));
						ti.setText(2, refreshed.get(id).get(2));
					}
					break;
				case 2:
					//DD
					table_dd.removeAll();
					lblReady.setText("Refreshing DD Pages..");
					refreshed = du.LoadDD();
					for(String id : refreshed.keySet()) {
						TableItem ti = new TableItem(table_dd, 0);
						ti.setText(0, refreshed.get(id).get(0));
						ti.setText(1, refreshed.get(id).get(1));
					}
					break;
				}
				btnRefresh.setEnabled(true);
				lblReady.setText("Done.");
			}
		});
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					List<String> visited = new ArrayList<>();
					List<Integer> selected_idx = new ArrayList<>();
					int[] remove_idx;
					switch (tabFolder.getSelectionIndex()) {
					case 0:
						for(TableItem item : table_hrm.getItems()) {
							if(item.getChecked()) {
								visited.add(item.getText(1));
								selected_idx.add(table_hrm.indexOf(item));
							}
						}
						remove_idx = new int[selected_idx.size()];
						for(int i = 0; i < selected_idx.size(); i++) remove_idx[i] = selected_idx.get(i);
						table_hrm.remove(remove_idx);
						dm.insertLog(visited);
						break;
					case 1:
						for(TableItem item : table_bp.getItems()) {
							if(item.getChecked()) {
								visited.add(item.getText(2));
								selected_idx.add(table_bp.indexOf(item));
							}
						}
						remove_idx = new int[selected_idx.size()];
						for(int i = 0; i < selected_idx.size(); i++) remove_idx[i] = selected_idx.get(i);
						table_bp.remove(remove_idx);
						dm.insertLog(visited);
						break;
					case 2:
						for(TableItem item : table_dd.getItems()) {
							if(item.getChecked()) {
								visited.add(item.getText(1));
								selected_idx.add(table_dd.indexOf(item));
							}
						}
						remove_idx = new int[selected_idx.size()];
						for(int i = 0; i < selected_idx.size(); i++) remove_idx[i] = selected_idx.get(i);
						table_dd.remove(remove_idx);
						dm.insertLog(visited);
						break;
					case 3:
						for(TableItem item : table_bmk.getItems()) {
							visited.add(item.getText(1));
						}
						dm.UpdateBookmark(visited);
						break;
					}
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
		System.gc();
		Manager m = new Manager();
		m.open();
	}
	public void open_load(Shell shlHrm) {
		shlHrm.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Shell popup_load = new Shell(shlHrm, 0);
				popup_load.setText("Alert");
				popup_load.setSize(449, 166);
				popup_load.setLayout(new FillLayout(SWT.HORIZONTAL));
				Composite composite = new Composite(popup_load, 0);
				composite.setLayout(new GridLayout(1, false));
				
				Label lblMsg = new Label(composite, SWT.NONE);
				lblMsg.setAlignment(SWT.CENTER);
				lblMsg.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1));
				lblMsg.setText("Load new pages.. Please Wait..");
				
				Label lblProgress = new Label(composite, SWT.NONE);
				lblProgress.setAlignment(SWT.CENTER);
				lblProgress.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1));
				lblProgress.setText("Open headless webdriver..");
				
				popup_load.open();
				Map<String, List<String>> refreshed;
				refreshed = hu.LoadHrm();
				for(String tag : refreshed.keySet()) {
					for(String url : refreshed.get(tag)) {
						TableItem ti = new TableItem(table_hrm, 0);
						ti.setText(0, tag);
						ti.setText(1, url);
					}
				}
				refreshed = bp.LoadBP();
				for(String id : refreshed.keySet()) {
					TableItem ti = new TableItem(table_bp, 0);
					ti.setText(0, refreshed.get(id).get(0));
					ti.setText(1, refreshed.get(id).get(1));
					ti.setText(2, refreshed.get(id).get(2));
				}
				refreshed = du.LoadDD();
				for(String id : refreshed.keySet()) {
					TableItem ti = new TableItem(table_dd, 0);
					ti.setText(0, refreshed.get(id).get(0));
					ti.setText(1, refreshed.get(id).get(1));
				}
				popup_load.dispose();
			}
		});
	}
}
