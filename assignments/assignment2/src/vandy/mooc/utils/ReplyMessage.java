package vandy.mooc.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;

/**
 * A thin facade around an Android Message that defines the schema of a reply
 * from the Service back to the Activity.
 */
public class ReplyMessage extends RequestReplyMessageBase {
	/**
	 * Constructor is private to ensure the makeReplyMessage() factory method is
	 * used.
	 */
	private ReplyMessage(Message message) {
		super(message);
	}

	/**
	 * Convert a Message into a ReplyMessage.
	 */
	public static ReplyMessage makeReplyMessage(Message message) {
		// Make a copy of @a message since it may be recycled.
		return new ReplyMessage(Message.obtain(message));
	}

	/**
	 * A factory method that creates a reply message to return to the Activity
	 * with the pathname of the downloaded image.
	 */
	public static ReplyMessage makeReplyMessage(Uri pathToImageFile, Uri url,
			int requestCode) {
		// Create a ReplyMessage that holds a reference to a Message
		// created via the Message.obtain() factory method.
		ReplyMessage replyMessage = new ReplyMessage(Message.obtain());

		// Create a new Bundle to handle the result.
		// TODO DONE-- you fill in here.
		Bundle bundle = new Bundle();
		// Set the Bundle to be the data in the message.
		// TODO DONE-- you fill in here.
		replyMessage.setData(bundle);
		// Put the URL to the image file into the Bundle
		// TODO DONE-- you fill in here.
		ReplyMessage.setImageURL(bundle, url);
		// Put the requestCode into the Bundle
		// TODO DONE-- you fill in here.
		ReplyMessage.setRequestCode(bundle, requestCode);
		// Set the result code to indicate whether the download
		// succeeded or failed.
		// TODO DONE -- you fill in here.
		int resultCode = 0;

		if (pathToImageFile != null) {
			resultCode = Activity.RESULT_OK;
			// Put the path to the image file into the Bundle
			// only if the download succeeded.
			// TODO DONE-- you fill in here.
			ReplyMessage.setDirectoryPathname(bundle,
					pathToImageFile.toString());
		} else {
			resultCode = Activity.RESULT_CANCELED;
		}

		replyMessage.setResultCode(resultCode);
		return replyMessage;
	}
}
