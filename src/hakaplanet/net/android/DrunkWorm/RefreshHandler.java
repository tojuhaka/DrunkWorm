package hakaplanet.net.android.DrunkWorm;

import android.os.Handler;
import android.os.Message;

/**
 * Handleri joka pyörii taustalla. Käytätä update() metodia päivittämään ruutua, sekä invalidate()
 * metodilla käskee itseään päivittymään. Samalla laitetaan myös nukkumaan.
 * @author tojuhaka
 *
 */
 public class RefreshHandler extends Handler {
	 	private DrunkWormView view;
	 
	 	public RefreshHandler( DrunkWormView view) {
	 		this.view = view;
	 	}
        @Override
        public void handleMessage(Message msg) {
            view.update();
            view.invalidate();
        }

        public void sleep(long delayMillis) {
                this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }