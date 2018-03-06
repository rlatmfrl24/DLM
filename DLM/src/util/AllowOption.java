package util;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import main.ConfigLoader;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;

public class AllowOption extends Dialog implements Observer {

	protected Object result;
	private Shell shlOption;
	private ConfigLoader config;
	private Table table;
	private Text text;
	private HashMap<String, Boolean> allows = new HashMap<>();
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text_imgpath;
	private Text text_movpath;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AllowOption(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		getShlOption().open();
		getShlOption().layout();
		Display display = getParent().getDisplay();
		while (!getShlOption().isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		setShlOption(new Shell(getParent(), SWT.DIALOG_TRIM));
		getShlOption().setModified(true);
		getShlOption().setSize(745, 216);
		getShlOption().setText("Option");
		getShlOption().setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(getShlOption(), SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		
		TabItem tbtmEx = new TabItem(tabFolder, SWT.NONE);
		tbtmEx.setText("Extension");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmEx.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		gd_table.heightHint = 91;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnExtension = tableViewerColumn.getColumn();
		tblclmnExtension.setResizable(false);
		tblclmnExtension.setText("extension");
		tblclmnExtension.setWidth(290);
		
		Group grpExtensionControl = new Group(composite, SWT.NONE);
		GridData gd_grpExtensionControl = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpExtensionControl.heightHint = 102;
		gd_grpExtensionControl.widthHint = 422;
		grpExtensionControl.setLayoutData(gd_grpExtensionControl);
		grpExtensionControl.setText("Extension");
		
		text = new Text(grpExtensionControl, SWT.BORDER);
		text.setBounds(10, 21, 234, 18);
		
		Button btnAdd = new Button(grpExtensionControl, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!text.getText().isEmpty() && text.getText().startsWith(".")) {
					TableItem item = new TableItem(table, 0);
					item.setText(text.getText());
				}
			}
		});
		btnAdd.setBounds(10, 45, 74, 22);
		btnAdd.setText("Add");
		
		Button btnModify = new Button(grpExtensionControl, SWT.NONE);
		btnModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionCount()!=0 && !text.getText().isEmpty()) {
					table.getItem(table.getSelectionIndex()).setText(text.getText());
				}
			}
		});
		btnModify.setBounds(90, 45, 74, 22);
		btnModify.setText("Modify");
		
		Button btnDelete = new Button(grpExtensionControl, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionCount()!=0) {
					table.remove(table.getSelectionIndex());
				}
			}
		});
		btnDelete.setBounds(170, 45, 74, 22);
		btnDelete.setText("Delete");
		
		TabItem tbtmReference = new TabItem(tabFolder, SWT.NONE);
		tbtmReference.setText("Reference");
		
		Composite composite_1 = formToolkit.createComposite(tabFolder, SWT.NO_BACKGROUND);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		tbtmReference.setControl(composite_1);
		formToolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));
		
		Group grpHoneyview = new Group(composite_1, SWT.NONE);
		grpHoneyview.setText("Image Viewer");
		formToolkit.adapt(grpHoneyview);
		formToolkit.paintBordersFor(grpHoneyview);
		grpHoneyview.setLayout(new GridLayout(3, false));
		
		text_imgpath = new Text(grpHoneyview, SWT.BORDER);
		text_imgpath.setEnabled(false);
		text_imgpath.setEditable(false);
		text_imgpath.setText(config.GetImageViewerPath());
		GridData gd_text_imgpath = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_text_imgpath.widthHint = 549;
		text_imgpath.setLayoutData(gd_text_imgpath);
		formToolkit.adapt(text_imgpath, true, true);
		
		Button btnConfigure_1 = new Button(grpHoneyview, SWT.NONE);
		btnConfigure_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		btnConfigure_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog flog = new FileDialog(getShlOption(), SWT.OPEN);
				String selectd = flog.open();
				if(selectd != null) {
					text_imgpath.setText(selectd);
				}
			}
		});
		formToolkit.adapt(btnConfigure_1, true, true);
		btnConfigure_1.setText("Configure");
		
		Button btnSetDefault_1 = new Button(grpHoneyview, SWT.NONE);
		btnSetDefault_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_imgpath.setText("DEFAULT");
			}
		});
		GridData gd_btnSetDefault_1 = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_btnSetDefault_1.widthHint = 73;
		btnSetDefault_1.setLayoutData(gd_btnSetDefault_1);
		formToolkit.adapt(btnSetDefault_1, true, true);
		btnSetDefault_1.setText("Set Default");
		
		Group grpPot = new Group(composite_1, SWT.NONE);
		grpPot.setText("Video Viewer");
		formToolkit.adapt(grpPot);
		formToolkit.paintBordersFor(grpPot);
		grpPot.setLayout(new GridLayout(3, false));
		
		text_movpath = new Text(grpPot, SWT.BORDER);
		text_movpath.setEnabled(false);
		text_movpath.setEditable(false);
		GridData gd_text_movpath = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_text_movpath.widthHint = 549;
		text_movpath.setLayoutData(gd_text_movpath);
		text_movpath.setText(config.GetVideoViewerPath());
		formToolkit.adapt(text_movpath, true, true);
		
		Button btnConfigure_2 = new Button(grpPot, SWT.NONE);
		btnConfigure_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		btnConfigure_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog flog = new FileDialog(getShlOption(), SWT.OPEN);
				String selectd = flog.open();
				if(selectd != null) {
					text_movpath.setText(selectd);
				}
			}
		});
		btnConfigure_2.setText("Configure");
		formToolkit.adapt(btnConfigure_2, true, true);
		
		Button btnSetDefault_2 = new Button(grpPot, SWT.NONE);
		btnSetDefault_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		btnSetDefault_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_movpath.setText("DEFAULT");
			}
		});
		btnSetDefault_2.setText("Set Default");
		formToolkit.adapt(btnSetDefault_2, true, true);
		
		for(Entry<String, Boolean> e : allows.entrySet()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(e.getKey());
			item.setChecked(e.getValue());
		}
		
		Button btnDone = new Button(getShlOption(), SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				config.setImageViewerPath(text_imgpath.getText());
				config.setVideoViewerPath(text_movpath.getText());
				allows.clear();
				for(TableItem item : table.getItems()) {
					allows.put(item.getText(), item.getChecked());
				}
				//shlOption.close();
				getShlOption().dispose();
			}
		});
		btnDone.setText("Done");
		
	}
	public ArrayList<String> getAllows() {
		ArrayList<String> allow_list = new ArrayList<>();
		for(Entry<String, Boolean> e : allows.entrySet()) {
			if(e.getValue()) allow_list.add(e.getKey());
		}
		return allow_list;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		config = (ConfigLoader) o;
	}

	public Shell getShlOption() {
		return shlOption;
	}

	public void setShlOption(Shell shlOption) {
		this.shlOption = shlOption;
	}
}
