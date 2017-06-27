package com.codepath.android.booksearch.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;
import com.codepath.android.booksearch.net.BookClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class BookDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);



        // Extract book object from intent extras
        final Book book = Parcels.unwrap(getIntent().getParcelableExtra("book"));
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        Drawable firstImage = ivBookCover.getDrawable();
        Glide.with(this)
                .load(book.getCoverUrl())
                .error(firstImage)
                .into(ivBookCover);

        BookClient client = new BookClient();
        client.getBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject details = response.getJSONObject("OLID:" + book.getOpenLibraryId());

                    String publisherName = details
                            .getJSONArray("publishers")
                            .getJSONObject(0)
                            .getString("name");
                    tvPublisher.setText(publisherName);
                    Integer pageCount = details.getInt("number_of_pages");
                    tvPageCount.setText(pageCount.toString() + " pages");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

        // Use book object to populate data into views
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
