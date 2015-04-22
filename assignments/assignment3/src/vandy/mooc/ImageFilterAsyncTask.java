package vandy.mooc;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageFilterAsyncTask extends AsyncTask<Void, Void, Uri> {
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private Uri imageUri;
	private ImageView imageView;
	private ImageFilteredCallback imageFilteredCallback;

	ImageFilterAsyncTask(Uri imageUri, Context ctx, ImageView imageView, ImageFilteredCallback imageFilteredCallback) {
		super();
		Log.i(TAG, "Executing constructor ImageFilterAsyncTask");
		this.imageUri = imageUri;
		this.context = ctx;
		this.imageView = imageView;
		this.imageFilteredCallback = imageFilteredCallback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utils.showToast(context, "Filtering be patient pls!");
	}

	@Override
	protected Uri doInBackground(Void... params) {
		Uri imageFilteredUri = Utils.grayScaleFilter(context, imageUri);
		return imageFilteredUri;
	}

	@Override
	protected void onPostExecute(Uri imageFilteredUri) {
		super.onPostExecute(imageFilteredUri);
		imageView.setImageURI(imageFilteredUri);
		this.imageFilteredCallback.imageFiltered(imageFilteredUri);
	}
}
