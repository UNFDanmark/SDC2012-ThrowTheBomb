package dk.unf.sdc.gruppea;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class MainActivityu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activityu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_activityu, menu);
        return true;
    }

    
}
