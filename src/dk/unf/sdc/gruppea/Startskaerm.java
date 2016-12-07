package dk.unf.sdc.gruppea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Startskaerm extends Activity {

    private Button startSpil;
    private Button credits;
    
    //se under onCreaten (4 linje)
    Intent intentStart; 
    Intent intentCredits;
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_startskaerm);
    
    startSpil = (Button) findViewById(R.id.startSpil);
    credits = (Button) findViewById(R.id.credits);
    
    startSpil.setOnClickListener(startSpilListener);
    credits.setOnClickListener(creditsListener);
    
    intentStart = new Intent(this,SpilActivity.class);
    intentCredits = new Intent(this,Credits.class);
    }

	private OnClickListener startSpilListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intentStart);
		}
	};
	
	private OnClickListener creditsListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intentCredits);
		}
	};
	
	
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_startskaerm, menu);
        return true;
    }

    
}
