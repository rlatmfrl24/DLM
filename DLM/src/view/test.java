package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class test {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test window = new test();
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
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		Shell popup_load = new Shell();
		popup_load.setText("Alert");
		popup_load.setSize(449, 166);
		popup_load.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(popup_load, 0);
		composite.setLayout(new GridLayout(1, false));
		
		
		Label lblMsg = new Label(composite, SWT.NONE);
		lblMsg.setAlignment(SWT.CENTER);
		lblMsg.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblMsg.setText("Load new pages.. Please Wait..");
		
		Label lblProgress = new Label(composite, SWT.NONE);
		lblProgress.setAlignment(SWT.CENTER);
		lblProgress.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1));
		lblProgress.setText("Open headless webdriver..");
		popup_load.open();

	}

}
