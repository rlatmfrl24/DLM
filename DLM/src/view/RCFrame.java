package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import main.ConfigLoader;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import swing2swt.layout.BorderLayout;
import util.AllowOption;
import util.Expansion;
import util.Randomizer;
import util.SystemUtility;
import util.Expansion.Type;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class RCFrame implements Observer {
	private static Table table;
	private static Text text;
	private static Randomizer randomizer = new Randomizer();
	private static SystemUtility util = new SystemUtility();
	private static Expansion expansion = new Expansion();
	private static ConfigLoader config;
	private static Boolean ApplyAllows = false;

	public RCFrame(ConfigLoader c) {
		config = c;
		config.addObserver(this);
		config.addObserver(util);
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public void FrameIntialize() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(829, 554);
		shell.setText("File Random Selector");
		AllowOption allowOption = new AllowOption(shell, 0);
		config.addObserver(allowOption);
		config.broadcast();
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));

		Composite composite_table = new Composite(composite, SWT.NONE);
		composite_table.setLayoutData(BorderLayout.CENTER);
		composite_table.setLayout(new FillLayout(SWT.HORIZONTAL));

		table = new Table(composite_table, SWT.BORDER | SWT.FULL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				String path = table.getSelection()[0].getText(1) + "\\" + table.getSelection()[0].getText(0);
				util.open_explorer(path);
			}
		});
		
		table.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == SWT.F2 && table.getSelectionCount() == 1) {
					String path = table.getSelection()[0].getText(1) + "\\" + table.getSelection()[0].getText(0);
					if (expansion.check_expansion(path, Type.IMAGE) || expansion.check_expansion(path, Type.COMPRESSED)) {
						util.open_imgview(path);
					}else {
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Invaild File Type");
						msg.open();
					}
				}else if(e.keyCode == SWT.F3 && table.getSelectionCount() == 1) {
					String path = table.getSelection()[0].getText(1) + "\\" + table.getSelection()[0].getText(0);
					if (expansion.check_expansion(path, Type.IMAGE) || expansion.check_expansion(path, Type.MOVIE) || expansion.check_expansion(path, Type.MUSIC)) {
						util.open_movview(path);
					}else {
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Invaild File Type");
						msg.open();
					}
				}
			}
		});
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
				TableColumn tblclmnFileName = new TableColumn(table, SWT.LEFT);
				tblclmnFileName.setWidth(200);
				tblclmnFileName.setText("File Name");
		TableColumn tblclmnPath = new TableColumn(table, SWT.NONE);
		tblclmnPath.setWidth(200);
		tblclmnPath.setText("Path");
		table.pack();
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		MenuItem mntmViewOnHoneyview = new MenuItem(menu, SWT.NONE);
		mntmViewOnHoneyview.setText("View on Image Viewer	(F2)");
		MenuItem mntmViewOnPotplayer = new MenuItem(menu, SWT.NONE);
		mntmViewOnPotplayer.setText("View on Video Viewver	(F3)");
		mntmViewOnHoneyview.addListener(SWT.Selection ,new Listener() {
			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() != 0) {
					String path = table.getSelection()[0].getText(1) + "\\" + table.getSelection()[0].getText(0);
					if(expansion.check_expansion(path, Type.IMAGE) || expansion.check_expansion(path, Type.COMPRESSED)) {
						util.open_imgview(path);
					}else {
						//error dialog
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Invaild File Type");
						msg.open();
					}
				}
			}
		});
		mntmViewOnPotplayer.addListener(SWT.Selection ,new Listener() {
			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() != 0) {
					String path = table.getSelection()[0].getText(1) + "\\" + table.getSelection()[0].getText(0);
					if(expansion.check_expansion(path, Type.IMAGE) || expansion.check_expansion(path, Type.MOVIE) || expansion.check_expansion(path, Type.MUSIC)) {
						util.open_movview(path);
					}else {
						//error dialog
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Invaild File Type");
						msg.open();
					}
				}
			}
		});
		
		
		composite_table.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				Rectangle area = composite_table.getClientArea();
				Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table.getBorderWidth();
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					Point vBarSize = table.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					tblclmnFileName.setWidth(width / 3);
					tblclmnPath.setWidth(width - tblclmnFileName.getWidth());
					table.setSize(area.width, area.height);
				} else {

					table.setSize(area.width, area.height);
					tblclmnFileName.setWidth(width / 3);
					tblclmnPath.setWidth(width - tblclmnFileName.getWidth());
				}
			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Composite composite_selector = new Composite(composite, SWT.NONE);
		composite_selector.setLayoutData(BorderLayout.SOUTH);
		composite_selector.setLayout(new GridLayout(2, false));

		Button btnTarget = new Button(composite_selector, SWT.NONE);

		Label lblTargetPath = new Label(composite_selector, SWT.NONE);
		GridData gd_lblTargetPath = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblTargetPath.widthHint = 747;
		lblTargetPath.setLayoutData(gd_lblTargetPath);
		lblTargetPath.setFont(SWTResourceManager.getFont("굴림", 9, SWT.NORMAL));
		lblTargetPath.setText("Select Target Path..");

		btnTarget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlog = new DirectoryDialog(shell, SWT.OPEN);
				dlog.setText("Target Directory Select");
				if (config.GetCurrentPath() == null)
					dlog.setFilterPath("C:/");
				else
					dlog.setFilterPath(config.GetCurrentPath());
				String selected = dlog.open();
				if (selected != null) {
					lblTargetPath.setText(selected);
					config.setCurrentPath(selected);
				}
			}
		});
		btnTarget.setText("Change");

		Composite composite_roll = new Composite(composite, SWT.NONE);
		composite_roll.setLayoutData(BorderLayout.NORTH);
		composite_roll.setLayout(new GridLayout(4, false));

		Label lblCount = new Label(composite_roll, SWT.NONE);
		lblCount.setFont(SWTResourceManager.getFont("굴림", 11, SWT.BOLD));
		lblCount.setText("Count:");

		text = new Text(composite_roll, SWT.BORDER);

		Button btnRoll = new Button(composite_roll, SWT.NONE);
		btnRoll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.removeAll();
				// System.out.println(o.getAllows());
				if (allowOption.getAllows().size() > 0)
					ApplyAllows = true;
				else
					ApplyAllows = false;
				if (config.GetCurrentPath() == null || text.getText().isEmpty()) {
					MessageBox msg = new MessageBox(shell);
					msg.setText("Alert");
					msg.setMessage("Please select directory or input roll count!");
					msg.open();
				} else {
					HashMap<File, String> recommand_set;
					if (ApplyAllows)
						recommand_set = randomizer.getRandomSet(config.GetCurrentPath(), Integer.valueOf(text.getText()), allowOption.getAllows());
					else
						recommand_set = randomizer.getRandomSet(config.GetCurrentPath(), Integer.valueOf(text.getText()));
					for (Entry<File, String> en : recommand_set.entrySet()) {
						String[] s = {en.getKey().getName(),  en.getValue()};
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(s);
					}
				}
			}
		});
		btnRoll.setText("ROLL!");

		Button btnOption = new Button(composite_roll, SWT.NONE);
		btnOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (allowOption.getShlOption() == null || allowOption.getShlOption().isDisposed()) {
					allowOption.open();
				}
			}
		});
		btnOption.setText("Option");
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		config = (ConfigLoader) o;
	}
}
