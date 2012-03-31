package hakaplanet.net.android.DrunkWorm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Pit‰‰ yll‰ pikseleit‰ ja hoitaa n‰iden piirt‰misen kuvaruudulle
 * @author tojuhaka
 *
 */
public class TileContainerView extends View {

	private static int mTileSize = 12; //kuinka monta piksei‰ kukin tile on
	private final Paint mPaint = new Paint();
	protected int[][] mTileGrid;
	private Bitmap[] mTileArray;

	protected static int mXTileCount;
    protected static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;
    
    
	
	public TileContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
    public TileContainerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
    public int getTile(int x, int y) {
    	return mTileGrid[x][y];
    }
    
    /**
     * Piirt‰‰ annetun laatan canvasiin bitmappia hyv‰ksi k‰ytt‰en. Parametrina annetaan myˆs avain,
     * jotta voidaan piirt‰minen yhdist‰‰ kaksiulotteiseen muistitaulukkoon piirroksita (mTileGrid)
     * @param key
     * @param tile
     */
	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);
        
        mTileArray[key] = bitmap; //lis‰‰ piirrett‰v‰n avaimeen

	}
	
	public void loadBigTile(int key, Drawable tile, int kerroin) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize*kerroin, mTileSize*kerroin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize*kerroin, mTileSize*kerroin);
        tile.draw(canvas);
        
        mTileArray[key] = bitmap;

	}
	
	
	
	public int getXTileCount() {
		return mXTileCount;
	}
	  public void resetTiles(int tilecount) {
	        mTileArray = new Bitmap[tilecount];
	    }
	  
	  /**
	   * Piirt‰‰ n‰ytˆlle kaikki mit‰ tulee piirt‰‰. T‰m‰ on muistissa mTileGrid taulukossa, josta piirret‰‰n
	   * bitmap arrayn avulla ruudulle.
	   */
	  @Override
	    public void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        for (int x = 0; x < mXTileCount; x += 1) {
	            for (int y = 0; y < mYTileCount; y += 1) {
	            	
	                if (mTileGrid[x][y] > 0 && mTileGrid[x][y] < 9) {
	                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], 
	                                mXOffset + x * mTileSize,
	                                mYOffset + y * mTileSize,
	                                mPaint);
	                }
	            }
	        }

	  }
	  
	  //Tullaan tarvitsemaan ehk‰
	  public void clearTiles() {
	        for (int x = 0; x < mXTileCount; x++) {
	            for (int y = 0; y < mYTileCount; y++) {
	                setTile(0, x, y);
	            }
	        }
	  }
	  
	  public void setTile(int tileindex, int x, int y) {
	        mTileGrid[x][y] = tileindex;
	        
	  }
	  
	  /**
	   * Alustaa koot ruudun mukaan
	   */
	  @Override
	  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      
		  	mXTileCount = (int) Math.floor(w / mTileSize);
	        mYTileCount = (int) Math.floor(h / mTileSize);

	        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
	        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

	        mTileGrid = new int[mXTileCount][mYTileCount];
	        clearTiles();
	        setTile(5, 2,2);
	        
	        
	  }
}
