package com.example.twitterapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import java.util.List;

import static com.example.twitterapp.MainActivity.authentication;
import static com.example.twitterapp.MainActivity.userid;

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
            final Tweet tweet = mTweets.get(position);
            TextView username = convertView.findViewById(R.id.tweetUsername);
            TextView name = convertView.findViewById(R.id.tweetName);
            TextView tweetText = convertView.findViewById(R.id.tweetText);
            TextView date = convertView.findViewById(R.id.tweetDate);
            final ImageView reply = convertView.findViewById(R.id.replybtn);
            final ImageView retweet = convertView.findViewById(R.id.retweetbtn);
            final ImageView like = convertView.findViewById(R.id.likebtn);
            ImageView profileImage = convertView.findViewById(R.id.tweetProfileImage);
            tweet.getUser().setProfile_image_url(tweet.getUser().getProfile_image_url());
            Picasso.get().load(tweet.getUser().getProfile_image_url()).transform(new CircleTransform()).into(profileImage);
            username.setText("@" + tweet.getUser().getScreen_name());
            name.setText(tweet.getUser().getName());
            tweetText.setText(tweet.getText());
            date.setText(tweet.getCreated_at());

            //Image Changer retweets
            Bitmap retweetingIcon = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.retweet2);
            Bitmap fullretweetIcon = BitmapFactory.decodeResource(convertView.getResources(),R.drawable.retweet);

            final Drawable[] retweetAction = new Drawable[2];
            retweetAction[0] = new BitmapDrawable(convertView.getResources(), retweetingIcon);
            retweetAction[1] = new BitmapDrawable(convertView.getResources(),fullretweetIcon);

            final Drawable[] unRetweetAction = new Drawable[2];
            unRetweetAction[0] = new BitmapDrawable(convertView.getResources(), fullretweetIcon);
            unRetweetAction[1] = new BitmapDrawable(convertView.getResources(), retweetingIcon);

            //image Changer Likes
            Bitmap likingIcon = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.like);
            Bitmap fullLikingIcon = BitmapFactory.decodeResource(convertView.getResources(),R.drawable.like2);

            final Drawable[] likingAction = new Drawable[2];
            likingAction[0] = new BitmapDrawable(convertView.getResources(), likingIcon);
            likingAction[1] = new BitmapDrawable(convertView.getResources(), fullLikingIcon);

            final Drawable[] unLikingAction = new Drawable[2];
            unLikingAction[0] = new BitmapDrawable(convertView.getResources(), fullLikingIcon);
            unLikingAction[1] = new BitmapDrawable(convertView.getResources(), likingIcon);

            //buttons
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tweet.isRetweeted()) {
                        authentication.retweet(tweet.getId());
                        TransitionDrawable transitionDrawable = new TransitionDrawable(retweetAction);
                        retweet.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(10);
                        tweet.setRetweeted(true);
                    }else{
                        authentication.unRetweet(tweet.getId());
                        TransitionDrawable transitionDrawable = new TransitionDrawable(unRetweetAction);
                        retweet.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(10);
                        tweet.setRetweeted(false);
                    }
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tweet.isLiked()) {
                        authentication.like(tweet.getId());
                        TransitionDrawable transitionDrawable = new TransitionDrawable(likingAction);
                        like.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(10);
                        tweet.setLiked(true);

                    } else {
                        authentication.unlike(tweet.getId());
                        TransitionDrawable transitionDrawable = new TransitionDrawable(unLikingAction);
                        like.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(10);
                        tweet.setLiked(false);
                    }
                }
            });
        }
        return convertView;
    }

    public static class CircleTransform implements Transformation {
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
