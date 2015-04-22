package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import static vandy.mooc.Utils.grayScaleFilter;

/**
 * Created by jules on 4/21/15.
 */
public class FilterImageTask extends AsyncTask<Uri, Integer, Uri>{

    private Activity mActivity;

    public FilterImageTask(Activity activity) {
        mActivity = activity;
    }

    // Runs in background
    @Override
    protected Uri doInBackground(Uri... downloadedImage) {
        // Filter image. Returns the local storage image uri / path of the filtered image
        Uri filteredImage = grayScaleFilter(mActivity.getApplicationContext(), downloadedImage[0]);
        return filteredImage;
    }

    // Runs in context where it is constructed
    @Override
    protected void onPostExecute(Uri filteredImage) {
        /*
         * After image has been filtered, set result for the Activity and put the downloaded and filtered
         * image path into the result's bundled extra
         */
        int result;
        Intent intent = new Intent();

        if (filteredImage != null) {
            intent.putExtra(DownloadAndFilterImageActivity.DOWNLOAD_IMAGE_PATH_CODE, filteredImage.toString());
            result = Activity.RESULT_OK;
        }
        else
            result = Activity.RESULT_CANCELED;

        mActivity.setResult(result, intent);

        mActivity.finish();
    }
}
