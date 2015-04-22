package vandy.mooc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A main Activity that prompts the user for a URL to an image and then uses
 * Intents and other Activities to download the image and view it.
 */
public class MainActivity extends LifecycleLoggingActivity implements DownloadCompletedCallback, ImageFilteredCallback{
	/**
	 * Debugging tag used by the Android logger.
	 */
	private final String TAG = getClass().getSimpleName();

	/**
	 * A value that uniquely identifies the request to download an image.
	 */
	private static final int DOWNLOAD_IMAGE_REQUEST = 1;

	/**
	 * EditText field for entering the desired URL to an image.
	 */
	private EditText mUrlEditText;

	/**
	 * URL for the image that's downloaded by default if the user doesn't
	 * specify otherwise.
	 */
	private Uri mDefaultUrl = Uri
			.parse("http://www.dre.vanderbilt.edu/~schmidt/kitten.png");
	
	private Uri theImageUri;
	
	private ImageView imageView;

	/**
	 * Hook method called when a new instance of Activity is created. One time
	 * initialization code goes here, e.g., UI layout and some class scope
	 * variable initialization.
	 *
	 * @param savedInstanceState
	 *            object that contains saved state information.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, "Entering MainActivity.onCreate");
		super.onCreate(savedInstanceState);

		
		// Set the default layout.
		setContentView(R.layout.main_activity);
		mUrlEditText = (EditText) findViewById(R.id.url);
		imageView = (ImageView) findViewById(R.id.imageView);
		//handle rotation of the screen, recovers the image.
		if(savedInstanceState != null) {
	        Bitmap bitmap = (Bitmap)savedInstanceState.getParcelable("image");
	        imageView.setImageBitmap(bitmap);
	     }
		Button downloadButton = (Button) findViewById(R.id.button1);
		downloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadImage(v);
			}
		});
	}

	/**
	 * Called by the Android Activity framework when the user clicks the
	 * "Find Address" button.
	 *
	 * @param view - The view.
	 */
	public void downloadImage(View view) {
		Log.i(TAG, "Entering MainActivity.downloadImage");
		try {
			// Hide the keyboard.
			hideKeyboard(this, mUrlEditText.getWindowToken());

			Uri url = getUrl();
			if (url != null) {
				downloadImage();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Get the URL to download based on user input.
	 */
	protected Uri getUrl() {
		Uri url = null;

		// Get the text the user typed in the edit text (if anything).
		url = Uri.parse(mUrlEditText.getText().toString());

		// If the user didn't provide a URL then use the default.
		String uri = url.toString();
		if (uri == null || uri.equals(""))
			url = mDefaultUrl;

		// Do a sanity check to ensure the URL is valid, popping up a
		// toast if the URL is invalid.
		if (isUrlValid(url.toString())) {
			return url;
		} else {
			Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	/**
	 * This method is used to hide a keyboard after a user has finished typing
	 * the url.
	 */
	public void hideKeyboard(Activity activity, IBinder windowToken) {
		InputMethodManager mgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(windowToken, 0);
	}

	/**
	 * Better validation of url as the UrlUtil.isValidUrl is not accurate.
	 * 
	 * @param url
	 *            - The url to be validated
	 * @return - true if valid false otherwise.
	 */
	private boolean isUrlValid(String url) {
		Pattern p = Patterns.WEB_URL;
		Matcher m = p.matcher(url);
		if (m.matches())
			return true;
		else
			return false;
	}
	
	
	public void downloadImage() {
		ImageDownloadAsyncTask task = new ImageDownloadAsyncTask(getUrl(), this, this, imageView);
		task.execute();
	}
	
	public void filterImage(){
		ImageFilterAsyncTask imageFilterAsyncTask = new ImageFilterAsyncTask(theImageUri, getApplicationContext(), imageView, this);
		imageFilterAsyncTask.execute();
	}
    
	@Override
	public void downloadCompleted(Uri downloadedImageUri) {
		Log.i(TAG, "Image Downloaded");
		theImageUri = downloadedImageUri;
		filterImage();
	}

	@Override
	public void imageFiltered(Uri imageFilteredUri) {
		theImageUri = imageFilteredUri;
	}
	
	//handles rotation of the screen
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "saving ImageView...");
	    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
	    Bitmap bitmap = drawable.getBitmap();
	    outState.putParcelable("image", bitmap);
	    
	}
}
