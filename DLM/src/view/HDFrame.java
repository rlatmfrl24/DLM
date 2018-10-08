package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import main.dbManager;
import util.hd.DownloadUtil;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;

import java.lang.Thread.State;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class HDFrame {
	private Text text;
	private Table table;
	private DownloadUtil downloadUtil;
	private Thread download;
	
	public HDFrame() {
		
	}
	
	public HDFrame(dbManager dbManager) {
		downloadUtil = new DownloadUtil(dbManager);
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			dbManager dm = new dbManager();
			dm.Connect();
			HDFrame window = new HDFrame(dm);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(707, 300);
		shell.setText("Hiyobi Downloader");
		shell.setLayout(new GridLayout(1, false));
		
		Composite composite_input = new Composite(shell, SWT.NONE);
		composite_input.setLayout(new GridLayout(4, false));
		GridData gd_composite_input = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_input.heightHint = 41;
		composite_input.setLayoutData(gd_composite_input);
		
		Label lblSearchPage = new Label(composite_input, SWT.NONE);
		lblSearchPage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		lblSearchPage.setText("Search Page:");
		
		text = new Text(composite_input, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 150;
		text.setLayoutData(gd_text);
		
		Button btnFind = new Button(composite_input, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().length() > 0) {
					table.removeAll();
					downloadUtil.GetDownloadList(table, Integer.parseInt(text.getText()));
				}
			}
		});
		GridData gd_btnFind = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFind.widthHint = 63;
		btnFind.setLayoutData(gd_btnFind);
		btnFind.setText("Find");
		
		Button btnDownload = new Button(composite_input, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				download = new Thread(new Runnable() {					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						downloadUtil.getDownloadByTable(table);
					}
				});
				download.start();
			}
		});
		btnDownload.setText("Download");
		
		Composite composite_list = new Composite(shell, SWT.NONE);
		composite_list.setLayout(new GridLayout(1, false));
		composite_list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewer tableViewer = new TableViewer(composite_list, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnCode = tableViewerColumn.getColumn();
		tblclmnCode.setWidth(100);
		tblclmnCode.setText("Code");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_1.getColumn();
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText("Title");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnRemarks = tableViewerColumn_3.getColumn();
		tblclmnRemarks.setWidth(100);
		tblclmnRemarks.setText("Remarks");
		
		Composite composite_progress = new Composite(shell, SWT.NONE);
		GridData gd_composite_progress = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_progress.heightHint = 29;
		composite_progress.setLayoutData(gd_composite_progress);
		
		composite_list.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				Rectangle area = composite_list.getClientArea();
				Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table.getBorderWidth();
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					Point vBarSize = table.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					tblclmnCode.setWidth(width / 7);
					tblclmnTitle.setWidth(width / 7 * 3);
					tblclmnRemarks.setWidth(width - tblclmnCode.getWidth() - tblclmnTitle.getWidth());
					table.setSize(area.width, area.height);
				} else {
					table.setSize(area.width, area.height);					
					tblclmnCode.setWidth(width / 7);
					tblclmnTitle.setWidth(width / 7 * 3);
					tblclmnRemarks.setWidth(width - tblclmnCode.getWidth() - tblclmnTitle.getWidth());
					table.setSize(area.width, area.height);
				}
			}
			
			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		shell.open();
		shell.layout();
		State before_state = State.TERMINATED;
		while (!shell.isDisposed()) {
			
			if(download!=null) {
				if(download.isAlive()) {
					btnDownload.setEnabled(false);
				}else if(!download.isAlive()) {
					btnDownload.setEnabled(true);
				}
				if(before_state!=download.getState() && download.getState() == State.TERMINATED) {
					MessageBox msg = new MessageBox(shell);
					msg.setText("Alert");
					msg.setMessage("Download Complete.");
					msg.open();
				}
				before_state=download.getState();
			}
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
