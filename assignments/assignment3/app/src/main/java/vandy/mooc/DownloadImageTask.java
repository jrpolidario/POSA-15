package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import static vandy.mooc.Utils.downloadImage;

/**
 * Created by jules on 4/21/15.
 */
public class DownloadImageTask extends AsyncTask<Uri, Integer, Uri>{

    private Activity mActivity;

    public DownloadImageTask(Activity activity) {
        mActivity = activity;
    }

    // Runs in background
    @Override
    protected Uri doInBackground(Uri... remoteImage) {
        // Downloads image and stores it locally. Returns the local storage image uri / path
        Uri downloadedImage = downloadImage(mActivity.getApplicationContext(), remoteImage[0]);
        return downloadedImage;
    }

    // Runs in context where it is constructed
    @Override
    protected void onPostExecute(Uri downloadedImage) {
        // After download has finished, then filter the image through FilterImageTask
        FilterImageTask filterImageTask = new FilterImageTask(mActivity);
        filterImageTask.execute(downloadedImage);
    }
}
