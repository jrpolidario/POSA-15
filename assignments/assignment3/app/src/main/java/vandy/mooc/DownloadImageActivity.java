package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import vandy.mooc.DownloadUtils;

import static vandy.mooc.DownloadUtils.downloadImage;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    private Uri url;

    final static String DOWNLOAD_IMAGE_PATH_CODE = "downloadedImagePath";

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

        // Get the URL associated with the Intent data.
        // @@ TODO -- you fill in here.

        Intent intent = getIntent();


        if (null != intent) {
            url = intent.getData();
        }

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.

        /*
         * HaMeR implementation using Handler, Message, and Runnables
         */
        Thread backgroundThread = new Thread(
            new Runnable() {
                // This handler will manage messages and runnables to this thread's queue.
                // And will allow to run finish() on the UI Thread
                Handler mHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        // Get the downloaded image local path stored in the message data
                        String downloadedImagePath = msg.getData().getString(DOWNLOAD_IMAGE_PATH_CODE);

                        int result;
                        Intent intent = new Intent();

                        if (downloadedImagePath != null) {
                            intent.putExtra(DOWNLOAD_IMAGE_PATH_CODE, downloadedImagePath);
                            result = RESULT_OK;
                        }
                        else
                            result = RESULT_CANCELED;

                        setResult(result, intent);

                        // Run finish() on UI Thread
                        runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }
                        );
                    }
                };

                @Override
                public void run() {
                    // Set thread priority
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                    // Downloads image and stores it locally. Returns the local storage image uri / path
                    Uri downloadedImagePath = downloadImage(getApplicationContext(), url);

                    // Fetch a new message from the pool
                    Message message = mHandler.obtainMessage();
                    // Initialize a bundle that will be stored with the downloaded image path / uri
                    Bundle bundle = new Bundle();

                    if (null != downloadedImagePath) {
                        // Add the downloaded image path string to the bundle that will be stored in the message data
                        bundle.putString(DOWNLOAD_IMAGE_PATH_CODE, downloadedImagePath.toString());
                    }

                    // Assign the bundle to the message data
                    message.setData(bundle);
                    /* Send message to this thread queue in which after a while, handler will process
                     * this message on the handleMessage() method when the message is up next from the queue
                     */
                    mHandler.sendMessage(message);
                }
            }
        );

        backgroundThread.start();
    }
}

