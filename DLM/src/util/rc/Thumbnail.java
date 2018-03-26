package util.rc;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Thumbnail {

	public InputStream getStreamFromZip(String filepath) throws Exception {
		ZipFile zipFile = new ZipFile(filepath);
		ZipEntry zipEntry = zipFile.entries().nextElement();
		InputStream entryStream = zipFile.getInputStream(zipEntry);
		return entryStream;
	}

	public Image resize(Image image, int width, int height) throws Exception {
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
