package com.behindcurtain3.khet;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Context;


public class Drawable {

	private Sprite mSprite;
	private Texture mTexture;
	private TextureRegion mTextureRegion;
	
	public Drawable(){
		
	}
	
	public void loadResource(Context context, String resource){
		this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, context, resource, 0, 0);
	}
	
	public void loadScene(){
		this.mSprite = new Sprite(0, 0, this.mTextureRegion);
	}
	
	public Sprite getSprite(){
		return mSprite;
	}
	
	public Texture getTexture(){
		return mTexture;
	}
}
