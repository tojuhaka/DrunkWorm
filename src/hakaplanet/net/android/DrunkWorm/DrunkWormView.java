package hakaplanet.net.android.DrunkWorm;


import java.util.ArrayList;
import java.util.Random;




import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

public class DrunkWormView extends TileContainerView {
	/**
	 * Ohjelman tilat RUNNING = suorittaa, READY = valmiina, PAUSE = pys‰ytetty ja
	 * LOSE = h‰vinnyt. Peli alkaa aina ready-tilasta.
	 */
	private int mode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    
    /** Suunnat madolle. */
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    
    /** Graafiset avaimet madolle. N‰iden avulla pidet‰‰n yll‰
     * mit‰ kohtaa piirret‰‰n ruudulle.
     */
	private final static int WALL = 1;
	private final static int HEAD = 2;
	private final static int BODY = 3;
	private final static int BEER = 4;
	private final static int LOGO = 5;
	private final static int LOSEPIC = 6;
	
	/** Aika millisekuntteina, kuinka usein ruutua p‰ivitet‰‰n. V‰hennettyn‰ ruutua p‰ivitet‰‰n
	 * useammin, jolloin matokin liikkuu kovempaa vauhtia */
	private long moveDelay = 600;
	
	/** Arvotaan kaljat randomilla ruudulle. */
	private static final Random ran = new Random();
	
	/** Pyˆrii taustalla kokoajan p‰ivitt‰en ruutua */
    private RefreshHandler mRedrawHandler = new RefreshHandler(this);
    private long lastMove=0;
    private double permille=0;
    private TextView permilleText;
    
    /** Suunnat madolle */
    private int direction = SOUTH;
    private int nextDirection = NORTH;
    
    /** Lista, joka pit‰‰ yll‰ koordinaatteja ukosta*/
    private ArrayList<Coordinate> wormTrail = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> beers = new ArrayList<Coordinate>();
	    
	    /** 
	     * Pakolliset konstruktorit.
	     * @param context
	     * @param attrs
	     */
		public DrunkWormView(Context context, AttributeSet attrs) {
			super(context, attrs);
			initDrunkWormView();
		}
		
	    public DrunkWormView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initDrunkWormView();
	    }
	    
	    /**
	     * Alustetaan n‰kym‰ k‰ytt‰en hyv‰ksi perittyj‰ metodeja. TileContainer -n‰kym‰st‰.
	     * Loadtile lataa Containeriin piirrett‰v‰t laatat vastaamaan annettua avainta.
	     * 
	     */
		private void initDrunkWormView() {
			setFocusable(true);
			
			Resources res = this.getContext().getResources();
			resetTiles(7);
			loadTile(WALL, res.getDrawable(R.drawable.wall));
			loadTile(HEAD, res.getDrawable(R.drawable.head));
			loadTile(BODY, res.getDrawable(R.drawable.body));
			loadTile(BEER, res.getDrawable(R.drawable.beer));
			loadBigTile(LOGO, res.getDrawable(R.drawable.logo), 22);
			loadBigTile(LOSEPIC, res.getDrawable(R.drawable.losepic), 22);
			
		}
		
		public String getPermille() {
			return ""+permille;
		}
		
		/**
		 * P‰ivitt‰‰ madon k‰ytt‰en apuna arraylistaa, jossa ensimm‰inen alkio on madon p‰‰ ja
		 * taas viimeinen alkio on madon h‰nt‰. P‰ivitys tapahtuu muuttamalla madon p‰‰(ensimm‰inen alkio)
		 * uudeksi koordinaatiksi ja poistamalla viimeinen alkio. Kuitenkin madon kasvaessa viimeist‰
		 * alkiota ei poisteta.
		 */
		public void updateWorm() {
			boolean wormGrow = false;
			
			Coordinate head = wormTrail.get(0); //madon p‰‰ on aina listan ensimm‰inen
			Coordinate newHead = new Coordinate(1,1);
			
			direction = nextDirection;
			int index = 0;
			
			switch(direction) {
				case NORTH: {
					newHead = new Coordinate(head.x, head.y-1);	
					break;
				}
					
				case SOUTH: {
					newHead = new Coordinate(head.x, head.y+1);
					break;
				}
					
				case EAST: {
					newHead = new Coordinate(head.x+1, head.y);	
					break;
				}
					
				case WEST: {
					newHead = new Coordinate(head.x-1, head.y);
					break;
				}
			}
			
			/** Osuuko sein‰‰n */
			if (newHead.x == 0 || newHead.y == 0 || newHead.x == mXTileCount-1 || newHead.y == mYTileCount-1) {
				setMode(LOSE);
				clearTiles();
				setTile(LOSEPIC, 2,2);
				beers.clear();
				return;
			}
			
			/** Tarkasta lˆytyykˆ bissee matkalta */
			int size = beers.size();
			for (int i=0; i < size; i++) {
				if (newHead.equals(beers.get(i))) {
					beers.remove(beers.get(i));
					permille += 0.18;
					permilleText.setText("Promillet: "+makeStrPermille());
					addBeer();
					wormGrow = true;
					moveDelay *= 0.9;
				}
			}
			wormTrail.add(0, newHead); 
			if (!wormGrow) {
				wormTrail.remove(wormTrail.size() - 1);
			}
			
			/** Piirret‰‰n ruudulle p‰‰ ja kroppa */
			for (Coordinate crd : wormTrail) {
				if(index == 0) {
					setTile(HEAD, newHead.x, newHead.y );
				}
				else {
					setTile(BODY, crd.x, crd.y);
				}
				index++;
			}
		}
		
		/** Muuntaa promillet stringiksi ja katkaisee ne oikeesta kohdasta, ettei
		 * tule liian pitki‰ stringej‰ */
		public String makeStrPermille() {
			String permilleStr = ""+permille;
			if (permilleStr.length() > 4) {
				return permilleStr.substring(0,4);
			}
			return permilleStr;
		}
		
		/** P‰ivitet‰‰n kaljat ruudulle k‰ytt‰en avuksi beerslistaa, jossa kaljojen sijainnit */
		public void updateBeers() {
			for (Coordinate crd : beers) {
				setTile(BEER, crd.x, crd.y );
			}
		}
		
		/** Lis‰‰ randomkoordinaateilla kaljan ja tarkastaa ettei tˆrm‰‰ matoon */
		public void addBeer() {
	          boolean found = false;
	          Coordinate crd = null;
	          
	          while(!found) {
	        	  int newX = 1 + ran.nextInt(mXTileCount - 2);
		          int newY = 1 + ran.nextInt(mYTileCount - 2);
		          crd = new Coordinate(newX, newY);
		          
		          boolean collide = false;
		          for (int i=0; i < wormTrail.size(); i++) {
		        	  if (wormTrail.get(i).equals(crd))  {
		        		  collide = true;
		        	  }
		          }
		          found = !collide;
	          }
	          beers.add(crd);    
		}
		/** Tapahtuu aina kerran moveDelay -ajan kuluttua. P‰ivitt‰‰ kaiken ruudulla */
		public void update() {
			if (mode == RUNNING) {
				long now = System.currentTimeMillis();
				if (now - lastMove > moveDelay) {
					clearTiles();
					drawWalls();
					updateWorm();
					updateBeers();
					
					lastMove = now;
				}
				mRedrawHandler.sleep(moveDelay);
			}
		}
	    
		/** Piirret‰‰n sein‰t */
		public void drawWalls() {
			
			for (int x = 0; x < mXTileCount; x++) {
		        setTile(WALL, x, 0);
		        setTile(WALL, x, mYTileCount - 1);

		    }
		    for (int y = 1; y < mYTileCount - 1; y++) {
		         setTile(WALL, 0, y);
		         setTile(WALL, mXTileCount - 1, y);
		    }
		    
		}
		
		/** asetetaan pelin tila */
		public void setMode(int newMode) {
			this.mode = newMode;
		}
		
		/** Alustetaan uusipeli */
		private void initNewGame() {
			wormTrail.clear();
			permille = 0;
			permilleText.setText("Promillet: " + permille);
			moveDelay = 600;
			
			wormTrail.add(new Coordinate(7,7));
			wormTrail.add(new Coordinate(8,7));
			wormTrail.add(new Coordinate(9,7));
			wormTrail.add(new Coordinate(10,7));
			
			beers.clear();
			addBeer();
			addBeer();
			addBeer();
	
		}
		
		
		/**
		 * M‰‰ritell‰‰n nappejen painallukset
		 */
		@Override
		 public boolean onKeyDown(int keyCode, KeyEvent msg) {	 
		     if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
		    	
		    	 if (mode == READY || mode == LOSE) { //jos READY-tilassa tai LOSE-tilassa. Asetetaan uusipeli.
		    		 initNewGame();
		    		 setMode(RUNNING);
		    		 update();
		             return true;
		    	 }
		    	 if (mode == RUNNING) {
		    		 if (direction != SOUTH) {
		    			 nextDirection = NORTH;
		    		 } 
		    		 return true;
		    	 }
		    	 return true;
		     }
		     if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
		    	 if (mode != PAUSE && mode != LOSE) {
		    		 setMode(PAUSE);
		    		 return true;
		    	 }
		    	 if (mode != LOSE) {
		    		 setMode(RUNNING);
		    		 mRedrawHandler.sleep(moveDelay);
		    	 }
		     }
		     if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
		    	 if (direction != EAST) {
		    		 nextDirection = WEST;
		    	 }
		    	 
		     }
		     if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
		    	 if (direction != WEST) {
		    		 nextDirection = EAST;
		    	 }
		    	 
		    	 return true;
		     }
		     
		     if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
		    	 if (direction != NORTH) {
		    		 nextDirection = SOUTH;
		    	 }
		    	 
		    	 return true;
		     }
		     return super.onKeyDown(keyCode, msg);
		 }
		
		public void setPermilleText(TextView permilleText) {
			this.permilleText = permilleText;
		}
		
		
}
