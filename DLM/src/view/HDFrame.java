package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import java.lang.Thread.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import util.hd.DownlaodManager;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.forms.widgets.FormToolkit;

import main.dbManager;

public class HDFrame {
	private Text cnt_searchpage;
	private Text cnt_itemcount;
	public Thread downloader;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private int selection_driver = 0;
	private dbManager dm;
	
	public HDFrame() {
		
	}
	
	public HDFrame(dbManager dm) {
		this.dm = dm;
	}
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shlHiyobi = new Shell();
		shlHiyobi.setSize(483, 129);
		shlHiyobi.setText("Hiyobi");
		shlHiyobi.setLayout(new FillLayout(SWT.HORIZONTAL));
		formToolkit.setBackground(null);
		
		Composite composite = new Composite(shlHiyobi, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Label lblSearchPage = new Label(composite, SWT.NONE);
		lblSearchPage.setText("Search Page : ");
		
		cnt_searchpage = new Text(composite, SWT.BORDER);
		GridData gd_cnt_searchpage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cnt_searchpage.widthHint = 183;
		cnt_searchpage.setLayoutData(gd_cnt_searchpage);
		
		Button btnCrawl = new Button(composite, SWT.NONE);
		btnCrawl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCrawl.setText("Crawl!");
		
		Label lblDownloadCount = new Label(composite, SWT.NONE);
		lblDownloadCount.setText("Download Count : ");
		
		cnt_itemcount = new Text(composite, SWT.BORDER);
		cnt_itemcount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cnt_itemcount.setText("0");
		
		Composite composite_combo = new Composite(composite, SWT.NONE);
		composite_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_combo.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button btnPhantom = new Button(composite_combo, SWT.RADIO);
		btnPhantom.setSelection(true);
		formToolkit.adapt(btnPhantom, true, true);
		btnPhantom.setText("Phantom");
		
		Button btnChrome = new Button(composite_combo, SWT.RADIO);
		formToolkit.adapt(btnChrome, true, true);
		btnChrome.setText("Chrome");
		
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		formToolkit.adapt(label, true, true);

		Label lblCurrenttitle = new Label(composite, SWT.NONE);
		lblCurrenttitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		lblCurrenttitle.setAlignment(SWT.CENTER);
		lblCurrenttitle.setText("Waiting for start..");
		
		ProgressBar progressBar = new ProgressBar(composite, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		progressBar.setToolTipText("");

		btnCrawl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnPhantom.getSelection() && !btnChrome.getSelection()) selection_driver = 1;
				else if(btnChrome.getSelection() && !btnPhantom.getSelection()) selection_driver = 2;
				downloader = new Thread(new DownlaodManager(dm, lblCurrenttitle, progressBar ,Integer.valueOf(cnt_searchpage.getText()), Integer.valueOf(cnt_itemcount.getText()), selection_driver));
				downloader.start();
			}
		});
		
		shlHiyobi.open();
		shlHiyobi.layout();
		State before_state = State.TERMINATED;
		while (!shlHiyobi.isDisposed()) {
			
			if(downloader!=null) {
				if(downloader.isAlive()) {
					btnCrawl.setEnabled(false);
				}else if(!downloader.isAlive()) {
					btnCrawl.setEnabled(true);
				}
				if(before_state!=downloader.getState() && downloader.getState() == State.TERMINATED) {
					MessageBox msg = new MessageBox(shlHiyobi);
					msg.setText("Alert");
					msg.setMessage("Download Complete.");
					msg.open();
				}
				before_state=downloader.getState();
			}
			
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
