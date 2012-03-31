package hakaplanet.net.android.DrunkWorm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DrunkWorm extends Activity {
    /** Called when the activity is first created. */

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drunkworm_layout);
        
        DrunkWormView view = (DrunkWormView)findViewById(R.id.snake);
        TextView teksti = (TextView)findViewById(R.id.permilletext);
        view.setPermilleText(teksti);
    }
    
    /**
     * Tehd‰‰n erikseen oma handleri promilleille. Haluan pit‰‰ erill‰‰n TextViewin WormViewist‰
     * joten t‰m‰ tuntui ainoalta j‰rkev‰lt‰ ratkaisulta. Koska Viewist‰ ei n‰kˆj‰‰n
     * p‰‰se kovin hienosti k‰siksi sisarusVieweihin, vain omiin lapsiin tai itseens‰. 
     * Toivottavasti t‰m‰n asian voi hoitaa jotenkin paremmin.
     * Ehdotuksia siis otetaan vastaan :)
     * @author tojuhaka
     *
     */

  
}