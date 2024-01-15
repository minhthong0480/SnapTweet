package rmit.ad.snaptweet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import rmit.ad.snaptweet.Model.UserModel;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static void passUserAsIntent(Intent intent, UserModel model){
        intent.putExtra("usename",model.getUsername());
        intent.putExtra("useID",model.getUserId());
    }
    public static UserModel getUserFromIntent(Intent intent){
        UserModel user= new UserModel();
        user.setUsername(intent.getStringExtra("usename"));
        user.setUserId(intent.getStringExtra("useID"));
        return user;
    }
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }


}