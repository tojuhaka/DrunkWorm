package hakaplanet.net.android.DrunkWorm;

import android.os.Handler;
import android.os.Message;

/**
 * Handleri joka py�rii taustalla. K�yt�t� update() metodia p�ivitt�m��n ruutua, sek� invalidate()
 * metodilla k�skee itse��n p�ivittym��n. Samalla laitetaan my�s nukkumaan.
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