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

public class HDFrame {
	private Text cnt_searchpage;
	private Text cnt_itemcount;
	public Thread downloader;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shlHiyobi = new Shell();
		shlHiyobi.setSize(230, 118);
		shlHiyobi.setText("Hiyobi");
		shlHiyobi.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shlHiyobi, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblSearchPage = new Label(composite, SWT.NONE);
		lblSearchPage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		lblSearchPage.setText("Search Page : ");
		
		cnt_searchpage = new Text(composite, SWT.BORDER);
		GridData gd_cnt_searchpage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cnt_searchpage.widthHint = 79;
		cnt_searchpage.setLayoutData(gd_cnt_searchpage);
		
		Label lblDownloadCount = new Label(composite, SWT.NONE);
		lblDownloadCount.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		lblDownloadCount.setText("Download Count : ");
		
		cnt_itemcount = new Text(composite, SWT.BORDER);
		cnt_itemcount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cnt_itemcount.setText("0");
		
		
		Label lblProgress = new Label(composite, SWT.NONE);
		lblProgress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblProgress.setAlignment(SWT.CENTER);
		lblProgress.setText("Ready..");
		
		Button btnCrawl = new Button(composite, SWT.NONE);
		btnCrawl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnCrawl.setText("Crawl!");
		btnCrawl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblProgress.setText("Progressing..");
				downloader = new Thread(new DownlaodManager(Integer.valueOf(cnt_searchpage.getText()), Integer.valueOf(cnt_itemcount.getText())));
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
					lblProgress.setText("Done.");
				}
				before_state=downloader.getState();
			}
			
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
