package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class HMFrame {
	private Table table;

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
		
		TableItem i = new TableItem(table, 0);
		i.setText(0, "1");
		i.setText(1, "te");
		
		TabItem tbtmBattlepage = new TabItem(tabFolder, SWT.NONE);
		tbtmBattlepage.setText("Battlepage");
		
		Composite composite_1 = new Composite(shlHrm, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		Button btnUpdate = new Button(composite_1, SWT.NONE);
		btnUpdate.setText("Update");
		
		Button btnRefresh = new Button(composite_1, SWT.NONE);
		btnRefresh.setText("Refresh");

		shlHrm.open();
		shlHrm.layout();
		while (!shlHrm.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
