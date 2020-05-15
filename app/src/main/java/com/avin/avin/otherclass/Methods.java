package com.avin.avin.otherclass;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.avin.avin.R;

public class Methods {

    private Context _context;

    public Methods(Context context) {
        this._context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) || (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting());
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        columnWidth = point.x;
        return columnWidth;
    }



    /*public void changeCart(View view) {
        TextView textView = (TextView) view.findViewById(R.id.textView_menu_no);
        textView.setText("" + Constant.menuCount);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, CartActivity.class);
                _context.startActivity(intent);
            }
        });
    }*/

    /*public void changeCartFrag(Menu menu) {
        View cart = menu.findItem(R.id.menu_cart_search).getActionView();
        TextView textView = (TextView) cart.findViewById(R.id.textView_menu_no);
        textView.setText("" + Constant.menuCount);

       cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, CartActivity.class);
                _context.startActivity(intent);
            }
        });
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    public void forceRTLIfSupported(Window window) {

        if (_context.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    public String getPathImage(Uri uri) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            } else {

                if (uri == null) {
                    return null;
                }

                // try to retrieve the image from the media store first
                // this will only work for images selected from gallery

                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = _context.getContentResolver().query(uri, projection, null, null, null);

                if (cursor != null) {
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String retunn = cursor.getString(column_index);
                    cursor.close();
                    return retunn;
                }

                // this is our fallback here
                return uri.getPath();
            }

        }

        catch (Exception e) {
            e.printStackTrace();
            if (uri == null) {
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery

            String[] projection = {MediaStore.Images.Media.DATA

            };

            Cursor cursor = _context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String returnn = cursor.getString(column_index);
                cursor.close();
                return returnn;
            }

            // this is our fallback here
            return uri.getPath();
        }

    }

    public void showToast(String message) {

        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

}
