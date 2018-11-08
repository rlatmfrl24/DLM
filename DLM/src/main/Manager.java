package main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.CTFrame;
import view.HDFrame;
import view.HMFrame;
import view.RCFrame;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class Manager {
	private static final String config_filepath = "./temp/config.properties";
	private static ConfigLoader configLoader;
	
	public Manager() {
		try {
			configLoader = new ConfigLoader(config_filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Manager window = new Manager();
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
		Shell shlManager = new Shell();
		shlManager.setSize(273, 227);
		shlManager.setText("Select Module");
		shlManager.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shlManager, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		
		Button btnRandomChooser = new Button(composite, SWT.NONE);
		btnRandomChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlManager.dispose();
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
				shlManager.dispose();
				CTFrame mainFrame = new CTFrame(configLoader);
				mainFrame.FrameInitialize();
			}
		});
		btnCategorizer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnCategorizer.setText("Categorizer");
		
		Button btnHiyobiDownloader = new Button(composite, SWT.NONE);
		btnHiyobiDownloader.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlManager.dispose();
				HDFrame mainFrame = new HDFrame();
				mainFrame.open();
			}
		});
		btnHiyobiDownloader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnHiyobiDownloader.setText("Hiyobi Downloader");
		
		Button btnTrendChecker = new Button(composite, SWT.NONE);
		btnTrendChecker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlManager.dispose();
				HMFrame mainFrame = new HMFrame();
				mainFrame.open();
			}
		});
		btnTrendChecker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnTrendChecker.setText("Trend Checker");

		shlManager.open();
		shlManager.layout();
		while (!shlManager.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
