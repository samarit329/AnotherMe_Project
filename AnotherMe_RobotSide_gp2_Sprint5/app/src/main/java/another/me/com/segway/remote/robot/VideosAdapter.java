package another.me.com.segway.remote.robot;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class VideosAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public VideosAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    // display the title for each video
    // display the icon for each video
    public void bindView(View view, Context context, Cursor cursor) {
        TextView content = (TextView) view.findViewById(R.id.videos_item_title);
        ImageView thumb = (ImageView) view.findViewById(R.id.videos_item_icon);
        content.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)));

        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
        SetThumbTask s = new SetThumbTask(context, thumb);
        s.execute(new Integer(id));
    }

    @Override
    //create an interface to diaplay the list of videos
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.videos_list_item, parent, false);
    }



    // create calss to checks the videos on MediaStore
    // using Vitamio librales
    private class SetThumbTask extends AsyncTask<Integer, String, Bitmap> {
      // crate the vars for images and context
        Context context;
        ImageView thumb;


        public SetThumbTask(Context context, ImageView thumb) {
            this.context = context;
            this.thumb = thumb;
        }

        //bring the videos from MediaStore based on its id to display it
        protected Bitmap doInBackground(Integer... ids) {
            int id = ids[0].intValue();
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            //reques video from media store
            Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            return thumbnail;
        }

        protected void onPostExecute(Bitmap result) {
            thumb.setImageBitmap(result);
        }
    }
}// end class VideoAdapter
