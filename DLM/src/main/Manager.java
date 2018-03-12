package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.CFrame;
import view.RCFrame;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Manager extends Shell {
	private static File TempPath = new File("./temp/");
	private static ConfigLoader configLoader = new ConfigLoader();
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			if (!TempPath.exists()) {
				TempPath.mkdirs();
				File configFile = new File(TempPath + "/config.properties");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
				bw.write("TargetPath=C:/");
				bw.newLine();
				bw.write("ImageViewerPath=DEFAULT");
				bw.newLine();
				bw.write("VideoViewerPath=DEFAULT");
				bw.flush();
				bw.close();
			}
			configLoader.loadConfig(TempPath+"/config.properties");
			Display display = Display.getDefault();
			Manager shell = new Manager(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public Manager(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		
		Button btnRandomChooser = new Button(composite, SWT.NONE);
		btnRandomChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
				RCFrame mainFrame = new RCFrame(configLoader);
				mainFrame.FrameIntialize();
			}
		});
		btnRandomChooser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnRandomChooser.setText("Random Chooser");
		
		Button btnCategorizer = new Button(composite, SWT.NONE);
		btnCategorizer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
				CFrame mainFrame = new CFrame(configLoader);
				mainFrame.FrameInitialize();
			}
		});
		btnCategorizer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnCategorizer.setText("Categorizer");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Select Module");
		setSize(270, 168);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}