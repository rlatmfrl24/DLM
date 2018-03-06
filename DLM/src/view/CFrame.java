package view;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import main.ConfigLoader;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CFrame implements Observer {
	private static Text text_path;
	private static ConfigLoader config;
	
	public CFrame(ConfigLoader c) {
		config = c;
		c.addObserver(this);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void FrameInitialize() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(437, 552);
		shell.setText("Categorizer");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite main_composite = new Composite(shell, SWT.NONE);
		main_composite.setLayout(new BorderLayout(0, 0));
		
		Composite composite_path = new Composite(main_composite, SWT.NONE);
		composite_path.setLayoutData(BorderLayout.NORTH);
		composite_path.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpTargetPath = new Group(composite_path, SWT.NONE);
		grpTargetPath.setText("Target Path");
		grpTargetPath.setLayout(new GridLayout(2, false));
		
		text_path = new Text(grpTargetPath, SWT.BORDER | SWT.READ_ONLY);
		text_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_path.setText(config.GetCurrentPath());
		
		SashForm sashForm = new SashForm(main_composite, SWT.NONE);
		sashForm.setLayoutData(BorderLayout.CENTER);
		
		Composite composite_origin = new Composite(sashForm, SWT.NONE);
		composite_origin.setLayout(new GridLayout(1, false));
		
		Tree tree_origin = new Tree(composite_origin, SWT.BORDER);
		tree_origin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_transform = new Composite(sashForm, SWT.NONE);
		composite_transform.setLayout(new GridLayout(1, false));
		
		Tree tree_transform = new Tree(composite_transform, SWT.BORDER);
		tree_transform.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {1, 1});
		
		Composite composite_1 = new Composite(main_composite, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.SOUTH);
		composite_1.setLayout(new GridLayout(1, false));
		
		Button btnDone = new Button(composite_1, SWT.NONE);
		btnDone.setText("Done");
		
		Button btnBrowse = new Button(grpTargetPath, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tree_origin.removeAll();
				DirectoryDialog dlog = new DirectoryDialog(shell, SWT.NONE);
				if (config.GetCurrentPath() == null)
					dlog.setFilterPath("C:/");
				else
					dlog.setFilterPath(config.GetCurrentPath());
				String selected = dlog.open();
				if (selected != null) {
					text_path.setText(selected);
					config.setCurrentPath(selected);
				}
				File current_directory = new File(config.GetCurrentPath());
				for(File f : current_directory.listFiles()) {
					if(f.isFile()) {
						TreeItem t = new TreeItem(tree_origin, 0);
						t.setText(f.getName());
					}
				}
			}
		});
		btnBrowse.setText("Browse");
		
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
