package com.meng.toolset.mediatool.picture.saucenao;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.toolset.mediatool.*;
import com.meng.tools.app.ExceptionCatcher;
import com.meng.tools.app.ThreadPool;

import java.net.*;
import java.util.*;


public class ResultAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<PicResults.Result> resultArrayList;
    private Map<String,Bitmap> bmpCache = new WeakHashMap<String,Bitmap>();

    public ResultAdapter(Activity context, ArrayList<PicResults.Result> resultArrayList) {
        this.activity = context;
        this.resultArrayList = resultArrayList;
    }

    public int getCount() {
        return resultArrayList.size();
    }

    public PicResults.Result getItem(int position) {
        return resultArrayList.get(position);
    }

    public long getItemId(int position) {
        return resultArrayList.get(position).hashCode();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item_saucenao_result, null);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.similarity = (TextView) convertView.findViewById(R.id.similarity);
            holder.metadata = (TextView) convertView.findViewById(R.id.metadata);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PicResults.Result result = resultArrayList.get(position);
        holder.title.setText(result.mTitle);
        holder.similarity.setText(result.mSimilarity);
        holder.metadata.setText(result.mColumns.get(0));
        Bitmap bitmap = bmpCache.get(result.mThumbnail);
        if (bitmap == null) {
            ThreadPool.execute(new DownloadThumbnailRunnable(holder.thumbnail, result.mThumbnail));
        } else {
            holder.thumbnail.setImageBitmap(bitmap);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView similarity;
        private TextView metadata;
    }

    private class DownloadThumbnailRunnable implements Runnable {
        private ImageView imageView;
        private String strUrl;

        public DownloadThumbnailRunnable(ImageView imageView, String strUrl) {
            this.imageView = imageView;
            this.strUrl = strUrl;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(60000);
                final Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream());
                bmpCache.put(strUrl, bmp);
                activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bmp);
                        }
                    });
            } catch (Exception e) {
                ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
            }
        }
    }
}
