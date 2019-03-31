package edu.etzion.koletzion.comments;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.etzion.koletzion.Adapters.rvFeedAdapter;
import edu.etzion.koletzion.models.BroadcastPost;

public class CommentsDataSource extends AsyncTask<Void,Void, BroadcastPost> {
    private final String POSTS_API_KEY = "mitereeneringledituriess";
    private final String POSTS_API_SECRET = "7a76edb293ad60dbef1a92be96248116b74d9ea3";
    private final String POSTS_DB = "posts";
    private final String DB_USER_NAME = "41c99d88-3264-4be5-b546-ff5a5be07dfb-bluemix";

    private BroadcastPost postClicked;
    private WeakReference<RecyclerView> rv;

    public CommentsDataSource(BroadcastPost postClicked, RecyclerView rv) {
        this.postClicked = postClicked;
        this.rv = new WeakReference<>(rv);
    }

    @Override
    protected BroadcastPost doInBackground(Void... voids) {
       
        return getBroadcastPostById(postClicked.get_id());
    }
    @Override
    protected void onPostExecute(BroadcastPost broadcastPost) {
        RecyclerView rv = this.rv.get();
        rv.setAdapter(new CommentAdapter(broadcastPost));
        rv.setLayoutManager(new LinearLayoutManager(this.rv.get().getContext()));
    }

    // gets the updated current broadcastpost from the server.

    public BroadcastPost getBroadcastPostById(String id){
        BroadcastPost broadcastPost = null;
        CloudantClient client = ClientBuilder.account(DB_USER_NAME)
                .username(POSTS_API_KEY)
                .password(POSTS_API_SECRET)
                .build();

        Database db = client.database(POSTS_DB, false);

        List<BroadcastPost> list = db.findByIndex("{\n" +
                "   \"selector\": {\n" +
                "      \"_id\": \""+id+"\"\n" +
                "   }\n" +
                "}", BroadcastPost.class);
        for (BroadcastPost item : list) {
            Log.e("check", "checkResult: "+item.toString());
            broadcastPost=item;
        }
        Log.e("check", list.toString());
        return broadcastPost;

    }
}
