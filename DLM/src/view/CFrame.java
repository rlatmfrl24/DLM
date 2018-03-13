package view;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import main.ConfigLoader;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import util.AutoCategorizer;
import org.eclipse.swt.layout.RowLayout;

public class CFrame implements Observer {
	private static Text text_path;
	private static ConfigLoader config;
	private static Tree tree_transform;
	private static AutoCategorizer ac = new AutoCategorizer();
	
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
		
		Composite composite_center = new Composite(main_composite, SWT.NONE);
		composite_center.setLayout(new GridLayout(3, false));
		
		Tree tree_origin = new Tree(composite_center, SWT.BORDER);
		tree_origin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(composite_center, SWT.NONE);
		RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
		rl_composite.justify = true;
		composite.setLayout(rl_composite);
		
		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TreeItem item : tree_origin.getItems()) {
					String transform_path = ac.GetCategorizedName((File)item.getData(item.getText()));
					tree_transform = addPath(transform_path, tree_transform);
				}
			}
		});
		button.setText("→");
		
				tree_transform = new Tree(composite_center, SWT.BORDER);
				tree_transform.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Group grpTargetPath = new Group(composite_path, SWT.NONE);
		grpTargetPath.setText("Target Path");
		grpTargetPath.setLayout(new GridLayout(2, false));
		
		text_path = new Text(grpTargetPath, SWT.BORDER | SWT.READ_ONLY);
		text_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_path.setText("Please Select Folder..");
		
		Composite composite_command = new Composite(main_composite, SWT.NONE);
		composite_command.setLayoutData(BorderLayout.SOUTH);
		composite_command.setLayout(new GridLayout(1, false));
		
		Button btnApply = new Button(composite_command, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(tree_transform.getItems().length==0) {
						//MessageBox
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Check transform preview first..");
						msg.open();
					}else {
						for (TreeItem item : tree_origin.getItems()) {
							String transform_path = ac.GetCategorizedName((File) item.getData(item.getText()));
							try {
								FileUtils.moveFile((File) item.getData(item.getText()), new File(text_path.getText()+"/"+transform_path));
							}catch(FileExistsException fee){
								File duplicated_file = (File) item.getData(item.getText());
								MessageBox msg = new MessageBox(shell);
								msg.setText("Alert");
								msg.setMessage(duplicated_file.getName()+"\n\nThis File is Dulicated, Check This File");
								msg.open();
							}
						}
						MessageBox msg = new MessageBox(shell);
						msg.setText("Alert");
						msg.setMessage("Transform Done!");
						msg.open();
						tree_origin.removeAll();
						tree_transform.removeAll();
					}
				}catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		btnApply.setText("Apply");
		
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
						t.setData(f.getName(), f);
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
	
public Tree addPath(String s, Tree root) {	
		
		TreeItem parent = null;
		for(TreeItem item : root.getItems()) {
			if(item.getText().equals(s.split("/")[0])) {
				s = s.substring(s.indexOf('/')+1, s.length());
				parent = item;
			}
		}
		if(parent == null) {
			TreeItem item = new TreeItem(root, 0);
			item.setText(s.split("/")[0]);
			s = s.substring(s.indexOf('/')+1, s.length());
			parent = item;
		}
		
		while(s.indexOf('/')!=-1) {

			TreeItem subitem = null;
			for(TreeItem item : parent.getItems()) {
				if(item.getText().equals(s.split("/")[0])) {
					s = s.substring(s.indexOf('/')+1, s.length());
					subitem = item;
					parent = subitem;
				}
			}
			if(subitem == null) {
				subitem = new TreeItem(parent, 0);
				subitem.setText(s.split("/")[0]);
				s = s.substring(s.indexOf('/')+1, s.length());
				parent = subitem;
			}
		}
		TreeItem subitem = new TreeItem(parent, 0);
		subitem.setText(s);
		return root;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		config = (ConfigLoader) o;
	}


}
