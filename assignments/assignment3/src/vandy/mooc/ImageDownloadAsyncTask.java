package vandy.mooc;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloadAsyncTask extends AsyncTask<Void, Void, Uri> {

	private Context context;
	private Uri imageUri;
	private DownloadCompletedCallback downloadCompletedCallback;
	private ImageView imageView;

	ImageDownloadAsyncTask(Uri imageUri, Context ctx, DownloadCompletedCallback callback, ImageView imageView) {
		super();
		this.imageUri = imageUri;
		this.context = ctx;
		this.downloadCompletedCallback = callback;
		this.imageView = imageView;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utils.showToast(context, "Image downloading");
	}
	
	@Override
	protected Uri doInBackground(Void... params) {
		Uri downloadedImageUri = Utils.downloadImage(context, imageUri);
		return downloadedImageUri;
	}

	@Override
	protected void onPostExecute(Uri downloadedImageUri) {
		super.onPostExecute(downloadedImageUri);
		imageView.setImageURI(downloadedImageUri);
		this.downloadCompletedCallback.downloadCompleted(downloadedImageUri);
	}
}
