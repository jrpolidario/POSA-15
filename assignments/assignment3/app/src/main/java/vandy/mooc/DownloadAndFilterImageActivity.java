package vandy.mooc;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadAndFilterImageActivity extends LifecycleLoggingActivity implements DownloadAndFilterImageRetainedFragment.TaskCallbacks {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    private Uri url;

    private DownloadAndFilterImageRetainedFragment mDownloadAndFilterImageRetainedFragment;

    final static String DOWNLOAD_IMAGE_PATH_CODE = "downloadedImagePath";
    final static String RETAINED_FRAGMENT_TAG = "retained_fragment";
    final static String FRAGMENT_URL_PARAMS_TAG = "fragment_url_params";

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

        FragmentManager fm = getFragmentManager();
        mDownloadAndFilterImageRetainedFragment = (DownloadAndFilterImageRetainedFragment) fm.findFragmentByTag(RETAINED_FRAGMENT_TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mDownloadAndFilterImageRetainedFragment == null) {
            Uri uri = getIntent().getData();

            Bundle args = new Bundle();
            args.putString(FRAGMENT_URL_PARAMS_TAG, uri.toString());

            mDownloadAndFilterImageRetainedFragment = new DownloadAndFilterImageRetainedFragment();
            mDownloadAndFilterImageRetainedFragment.setArguments(args);

            fm.beginTransaction().add(mDownloadAndFilterImageRetainedFragment, RETAINED_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {

    }
}

