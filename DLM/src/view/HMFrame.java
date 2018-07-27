package view;

import org.eclipse.swt.widgets.Display;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import util.hm.hrmupdate;
import util.rc.SystemUtility;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class HMFrame {
	private Table table;
	private hrmupdate hu = new hrmupdate();
	private SystemUtility su = new SystemUtility();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HMFrame window = new HMFrame();
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
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmHrm.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 471;
		table.setLayoutData(gd_table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table.getSelectionCount() > 0) {
					String link = table.getSelection()[0].getText(1);
					System.out.println(link);
					su.open_browser(link);
					table.getSelection()[0].setChecked(true);
				}
			}
		});
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnCategory = tableViewerColumn.getColumn();
		tblclmnCategory.setWidth(100);
		tblclmnCategory.setText("Category");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnUrl = tableViewerColumn_1.getColumn();
		tblclmnUrl.setWidth(100);
		tblclmnUrl.setText("URL");
		
		tabFolder.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				Rectangle area = tabFolder.getClientArea();
				Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table.getBorderWidth();
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					Point vBarSize = table.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					tblclmnCategory.setWidth(width / 4);
					tblclmnUrl.setWidth(width - tblclmnCategory.getWidth());
					table.setSize(area.width, area.height);
				} else {
					table.setSize(area.width, area.height);					
					tblclmnCategory.setWidth(width / 4);
					tblclmnUrl.setWidth(width - tblclmnCategory.getWidth());
				}
			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		TabItem tbtmBattlepage = new TabItem(tabFolder, SWT.NONE);
		tbtmBattlepage.setText("Battlepage");
		
		Composite composite_1 = new Composite(shlHrm, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite_1.setLayout(new GridLayout(3, false));
		
		Button btnUpdate = new Button(composite_1, SWT.NONE);
		btnUpdate.setText("Update");
		
		Button btnRefresh = new Button(composite_1, SWT.NONE);
		btnRefresh.setText("Refresh");
		
		Label lblReady = new Label(composite_1, SWT.NONE);
		lblReady.setAlignment(SWT.RIGHT);
		GridData gd_lblReady = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblReady.widthHint = 640;
		lblReady.setLayoutData(gd_lblReady);
		lblReady.setText("Ready..");

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.removeAll();
				btnRefresh.setEnabled(false);
				lblReady.setText("Refreshing Hrm Pages..");
				hu.LoadHrm();
				Map<String, List<String>> refreshed = hu.getResMap();
				for(String tag : refreshed.keySet()) {
					for(String url : refreshed.get(tag)) {
						TableItem ti = new TableItem(table, 0);
						ti.setText(0, tag);
						ti.setText(1, url);
					}
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
					List<Integer> idx = new ArrayList<>();
					for(TableItem item : table.getItems()) {
						if(item.getChecked()) {
							visited.add(item.getText(1));
							idx.add(table.indexOf(item));
						}
					}
					int[] remove_idx = new int[idx.size()];
					for(int i = 0; i < idx.size(); i++) remove_idx[i] = idx.get(i);
					table.remove(remove_idx);
					hu.SaveLog(visited);
					lblReady.setText("Log Updated.");
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		shlHrm.open();
		shlHrm.layout();
		while (!shlHrm.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
