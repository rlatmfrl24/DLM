package deprecated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import main.ConfigLoader;
import main.dbManager;
import view.CTFrame;
import view.HMFrame;
import view.HDFrame;
import view.RCFrame;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class deprecated_Manager extends Shell {
	private static final File TempPath = new File("./temp/");
	private static final File HiyobiPath = new File("./hiyobi/");
	private static final File deletedItemPath = new File("./temp/deleted/");
	private static final File moveItemPath = new File("./temp/moved/");
	private static ConfigLoader configLoader = new ConfigLoader();
	private static dbManager dbManager = new dbManager();
	
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
			
			if(!deletedItemPath.exists()) {
				deletedItemPath.mkdirs();
			}
			if(!moveItemPath.exists()) {
				moveItemPath.mkdirs();
			}
			if(!HiyobiPath.exists()) {
				HiyobiPath.mkdirs();
			}
			
			configLoader.loadConfig(TempPath+"/config.properties");
			dbManager.Connect();
			dbManager.initialize();
			Display display = Display.getDefault();
			deprecated_Manager shell = new deprecated_Manager(display);
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
	public deprecated_Manager(Display display) {
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
				dispose();
				HDFrame mainFrame = new HDFrame(dbManager);
				mainFrame.open();
			}
		});
		btnHiyobiDownloader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnHiyobiDownloader.setText("Hiyobi Downloader");
		
		Button btnTrendChecker = new Button(composite, SWT.NONE);
		btnTrendChecker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
				HMFrame mainFrame = new HMFrame(dbManager);
				mainFrame.open();
			}
		});
		btnTrendChecker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnTrendChecker.setText("Trend Checker");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Select Module");
		setSize(273, 227);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}