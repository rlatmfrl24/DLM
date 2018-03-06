package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import randomchooser.util.ConfigLoader;
import randomchooser.view.RCFrame;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Manager extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
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
				ConfigLoader configLoader = new ConfigLoader();
				RCFrame mainFrame = new RCFrame(configLoader);
				mainFrame.intialize();
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
