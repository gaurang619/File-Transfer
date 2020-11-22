package filetransfer.in.ac.adit.com.transferfiles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.nextButton);
        TextView textView = (TextView)findViewById(R.id.txt1);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/rtm.ttf");
        button.setTypeface(typeface);
        button.setBackgroundColor(Color.parseColor("#000000"));
        textView.setTypeface(typeface);
        textView.setTextColor(Color.parseColor("#000000"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Index.class);
                startActivity(intent);

            }
        });
    }
}
