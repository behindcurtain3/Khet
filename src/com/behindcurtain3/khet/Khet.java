package com.behindcurtain3.khet;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import com.behindcurtain3.khet.player.ComputerPlayer;
import com.behindcurtain3.khet.util.Bitboard;

public class Khet extends BaseGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================
	private static final String TAG = "Khet";
    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;

    // ===========================================================
    // Fields
    // ===========================================================

    private Camera mCamera;
    private Drawable mBackground;
    private Sprite mSprite;
    private Texture mTexPiece;
    private Texture mTexBackground;
    private TextureRegion mSTRSilverPharaoh;
    private TextureRegion mSTRRedPharaoh;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public Engine onLoadEngine() {
            this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
            return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
    }

    @Override
    public void onLoadResources() {
    	
    	mBackground = new Drawable();
    	mBackground.loadResource(this, "gfx/Background.png");
    	mEngine.getTextureManager().loadTexture(mBackground.getTexture());
    	
    	
    	this.mTexPiece = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mSTRSilverPharaoh = TextureRegionFactory.createFromAsset(this.mTexPiece, this, "gfx/SilverPharaoh.png", 0, 0);
        this.mSTRRedPharaoh = TextureRegionFactory.createFromAsset(this.mTexPiece, this, "gfx/RedPharaoh.png", 0, 0);
 
        this.mEngine.getTextureManager().loadTexture(this.mTexPiece);
    }

    @Override
    public Scene onLoadScene() {
            this.mEngine.registerUpdateHandler(new FPSLogger());

            final Scene scene = new Scene(1);
            scene.setBackground(new ColorBackground(0, 0, 0.8784f)); 
            
            mBackground.loadScene();
            scene.getBottomLayer().addEntity(mBackground.getSprite());

            mSprite = new Sprite(100, 100, this.mSTRSilverPharaoh)
            {
                    @Override
                    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                    float pTouchAreaLocalX, float pTouchAreaLocalY)
                    {
                    	mSprite.setPosition(pSceneTouchEvent.getX() - (mSprite.getWidth() / 2), pSceneTouchEvent.getY() - (mSprite.getHeight() / 2));
                    	return true;
                    }
            };     
            scene.registerTouchArea(mSprite);
            
            scene.getTopLayer().addEntity(mSprite);
            
            Sprite r = new Sprite(250, 250, this.mSTRRedPharaoh);
            scene.getTopLayer().addEntity(r);

            return scene;
    }

    @Override
    public void onLoadComplete() {
    	ComputerPlayer p1 = new ComputerPlayer();
        ComputerPlayer p2 = new ComputerPlayer();
        
        //khet.startNewGame(p1, p2, Helper.getStandardConfig());
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public void printBitboard(Bitboard b){
    	String output = "";
        for(int y = 0; y < 8; y++){
        	output = "";
        	for(int x = 0; x < 10; x++){
        		output = output + b.get(y * 10 + x) + ","; 
        	}
        	Log.d(TAG, output);
        }
    }
    
    public void printMove(Move m){
    	Log.d(TAG, m.from + "->" + m.to + " : " + m.piece.type() + " : " + m.compass.getDirection() + " " + m.split + " " + m.join);
    }
    
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}