package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();
    
    
    private Uri uri;
//	private Handler handler = new Handler(Looper.getMainLooper());
	private Uri theImage;
	private Intent imageIntent;

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TODO -- you fill in here.
    	super.onCreate(savedInstanceState);
    	Log.i(TAG, "Entering DownloadImageActivity.onCreate!");

        // Get the URL associated with the Intent data.
        // @@ TODO -- you fill in here.
    	uri = getIntent().getData();
    	

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.
    	
    	//thread that download image...
    	Thread downloadThread = new Thread(new DownloadThread());
    	downloadThread.start();
    	
    	//TODO - Question: maybe I should also add download thread to the Looper using handler????? 
    	//Actually this don't work for some reason.
//    	handler.post(downloadThread);
    	
    }
    
    //thread implementation to start the download in background
    private class DownloadThread implements Runnable{
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		theImage = DownloadUtils
    				.downloadImage(getApplicationContext(), uri);
    		imageIntent = new Intent();
    		imageIntent.setData(theImage);
    		int result = (theImage != null) ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
    		setResult(result, imageIntent);
//    		DownloadImageActivity.this.finish();
    		//close this activity using runOnuiThread...
    		runOnUiThread(new Runnable(){
        		@Override
        		public void run(){
        			DownloadImageActivity.this.finish();
        		}
        	});
    	}
    }
}


