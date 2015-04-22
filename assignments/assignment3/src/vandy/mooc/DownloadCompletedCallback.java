package vandy.mooc;

import android.net.Uri;

public interface DownloadCompletedCallback {
	public void downloadCompleted(Uri downloadedImageUri);
}
