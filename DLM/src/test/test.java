package test;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewerColumn;

public class test {
	private static Table table;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ZipFile zipFile = new ZipFile("G:/sequcediagram.zip");
	    ZipEntry zipEntry = zipFile.entries().nextElement();
	    InputStream entryStream = zipFile.getInputStream(zipEntry);
	    
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Canvas Example");
		shell.setLayout(new FillLayout());
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer tableViewer = new TableViewer(canvas, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnReak = tableViewerColumn.getColumn();
		tblclmnReak.setWidth(422);
		tblclmnReak.setText("reak");
		TableItem ti = new TableItem(table, 0);
		Image image = new Image(display, entryStream);
		image = resize(image, tblclmnReak.getWidth(), image.getBounds().height*tblclmnReak.getWidth()/image.getBounds().width);
		ti.setImage(image);
		
/*		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image = new Image(display, entryStream);
				e.gc.drawImage(image, 5, 5);
				image.dispose();
			}
		});*/

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	    zipFile.close();
		display.dispose();
	}
	
	private static Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}
}
