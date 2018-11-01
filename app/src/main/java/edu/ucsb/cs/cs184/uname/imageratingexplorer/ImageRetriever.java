package edu.ucsb.cs.cs184.uname.imageratingexplorer;

/**
 * Created by Yi Ding 11/01.
 *
 * Much of this is based on the Volley documentation -- Android's REST api library.
 * https://developer.android.com/training/volley/
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ImageRetriever {
    public static final String REQ_LIST_IMG_METADATA = "https://picsum.photos/list";

    private static ImageRetriever mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private ImageRetriever(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ImageRetriever getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageRetriever(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * This function will list all available images from Lorem Picsum and calls
     * successListener with the json object if successful
     */
    public void listImagesRequest(Response.Listener<JSONObject> successListener,
                                   Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                REQ_LIST_IMG_METADATA,
                null,
                successListener,
                errorListener);

        mInstance.addToRequestQueue(jsonObjectRequest);
    }


}

//    private static String baseUrl = "http://ec2-52-53-191-204.us-west-1.compute.amazonaws.com:8080/";
//
//    public static void getImageList(final ImageListResultListener listener) {
//        RetrieveImageListTask retrieveImageListTask = new RetrieveImageListTask(listener);
//        retrieveImageListTask.execute();
//    }
//
//    public static void getImageByIndex(int index, final ImageResultListener listener) {
//        RetrieveImageByIndexTask retrieveTaggedImageTask = new RetrieveImageByIndexTask(listener);
//        retrieveTaggedImageTask.execute(new Integer(index));
//    }
//
//    interface ImageListResultListener {
//        void onImageList(ArrayList<String> list);
//    }
//
//    interface ImageResultListener {
//        void onImage(Bitmap image);
//    }
//
//    private static class RetrieveImageListTask extends AsyncTask<Void, Void, ArrayList<String>> {
//        private ImageListResultListener listener;
//
//        public RetrieveImageListTask(ImageListResultListener listener) {
//            super();
//            this.listener = listener;
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(Void... params) {
//            try {
//                URL pageUrl = new URL(baseUrl + "ls");
//                URLConnection connection = pageUrl.openConnection();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder pageHtmlBuilder = new StringBuilder();
//                String inputLine;
//                while ((inputLine = reader.readLine()) != null) {
//                    pageHtmlBuilder.append(inputLine);
//                    pageHtmlBuilder.append('\n');
//                }
//                reader.close();
//                String[] files = pageHtmlBuilder.toString().split("\n");
//                ArrayList<String> picNames = new ArrayList<>();
//                for (String file : files) {
//                    picNames.add(file);
//                }
//                return picNames;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> picNames) {
//            if (listener != null && picNames != null) {
//                listener.onImageList(picNames);
//            }
//        }
//    }
//
//    private static class RetrieveImageByIndexTask extends AsyncTask<Integer, Void, Bitmap> {
//        private ImageResultListener listener;
//
//        public RetrieveImageByIndexTask(ImageResultListener listener) {
//            super();
//            this.listener = listener;
//        }
//
//        @Override
//        protected Bitmap doInBackground(Integer... params) {
//            try {
//                URL imageUrl = new URL(baseUrl + "img/" + params[0] + "/pic");
//                URLConnection imageConnection = imageUrl.openConnection();
//                Bitmap image = BitmapFactory.decodeStream(imageConnection.getInputStream());
//
//                URL pageUrl = new URL(baseUrl + "img/" + params[0] + "/tags");
//                URLConnection connection = pageUrl.openConnection();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder pageHtmlBuilder = new StringBuilder();
//                String inputLine;
//                while ((inputLine = reader.readLine()) != null) {
//                    pageHtmlBuilder.append(inputLine);
//                    pageHtmlBuilder.append('\n');
//                }
//                reader.close();
//                String[] tags = pageHtmlBuilder.toString().split("\n");
//                ArrayList<String> picTags = new ArrayList<String>(Arrays.asList(tags));
//                return image;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap image) {
//            if (listener != null) {
//                listener.onImage(image);
//            }
//        }
//    }
//}