package brad.tw.asyncterst;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private MyTask mt1;
    private TextView mesg;
    private ImageView img;
    private Bitmap bmp;
    private UIHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mesg = (TextView)findViewById(R.id.mesg);
        img = (ImageView)findViewById(R.id.img);
        handler = new UIHandler();
    }
    public void test1(View v){
        mt1 = new MyTask();
        mt1.execute("Brad","Kevin","Tony","Peter","Eric");
    }
    public void test2(View v){
        if (mt1 != null && !mt1.isCancelled()){
            mt1.cancel(true);
        }
    }
    public void test3(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.androidcentral.com/sites/androidcentral.com/files/styles/w700/public/postimages/%5Buid%5D/podcast-ac-new.jpg?itok=tKuELSVP");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    handler.sendEmptyMessage(0);
                }catch(Exception ee){
                    Log.d("brad", ee.toString());
                }
            }
        }.start();
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(bmp);

        }
    }

    private class MyTask extends AsyncTask<String,Object,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("brad", "onPreExecute");
        }
        @Override
        protected String doInBackground(String... params) {
            Log.d("brad", "doInBackground");

            int i = 0; boolean isCancel = false;
            for (String name : params){
                if (isCancelled()){
                    isCancel = true;
                    break;
                }

                Log.d("brad", "Hello, " + name);
                i++;
                publishProgress(i, name);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }

            return isCancel?"Cancel!":"Game Over";
        }
        @Override
        protected void onPostExecute(String end) {
            super.onPostExecute(end);
            Log.d("brad", "onPostExecute:" + end);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Log.d("brad", "onProgressUpdate");
            mesg.setText((Integer)values[0] + ":" + (String)values[1]);
        }

        @Override
        protected void onCancelled(String end) {
            super.onCancelled(end);
            Log.d("brad", "onCancelled:" +end);
        }
    }

}
