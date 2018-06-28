package com.example.twitterapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.R;
import com.example.twitterapp.View.TweetView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class TweetListAdapter extends ArrayAdapter{
    private LayoutInflater minflater;
    private List<Tweet> mTweets;



    public TweetListAdapter(@NonNull Context context, int resource, @NonNull List<Tweet> objects) {
        super(context, R.layout.tweet, objects);
        minflater = LayoutInflater.from(context);
        mTweets = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
  public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tweet, parent, false);
        } else {
            Tweet tweet = mTweets.get(position);
            TextView username = convertView.findViewById(R.id.tweetUsername);
            TextView name = convertView.findViewById(R.id.tweetName);
            TextView tweetText = convertView.findViewById(R.id.tweetText);
            TextView date = convertView.findViewById(R.id.tweetDate);
            ImageView profileImage = convertView.findViewById(R.id.tweetProfileImage);
            tweet.getUser().setProfile_image_url(tweet.getUser().getProfile_image_url());
            Picasso.get().load(tweet.getUser().getProfile_image_url()).transform(new CircleTransform( )).into(profileImage);
            username.setText(tweet.getUser().getScreen_name());
            name.setText(tweet.getUser().getName());
            tweetText.setText(tweet.getText());
            date.setText(tweet.getCreated_at());
        }
        return convertView;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
