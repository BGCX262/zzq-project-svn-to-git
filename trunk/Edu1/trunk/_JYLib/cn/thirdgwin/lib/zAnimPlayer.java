package cn.thirdgwin.lib;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

//#if PLATFORM=="Android"
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
//#endif


/**
 * AnimPlayer
 * 动画播放器
 * @author MaNing
 */
public class zAnimPlayer {
	public static int I_X 	= 0;
	public static int I_Y 	= 1 + I_X;
	public static int I_FLAG 	= 1 + I_Y;
	public static int I_ANIM 	= 1 + I_FLAG;
	public static int I_FRAME 	= 1 + I_ANIM;
	public static int I_TIME 	= 1 + I_FRAME;
	public static int I_LOOP 	= 1 + I_TIME;
	public static int I_OVER	= 1 + I_LOOP;
	public static int I_MAX 	= 1 + I_OVER;
	
	public int[] Ints;
	public int[] LastFrameRC = new int[]{-1,-1,-1,-1,-1};
	private zSprite sprite;

	private final static int k_animBaseFrameTime = DevConfig.FPS_INTERVAL;

/**************************************************************************************
 * Android 缩放相关	
 **************************************************************************************/
//#if PLATFORM=="Android"
	public float ScaleX;
	public float ScaleY;
	private Matrix Transform = new Matrix();
	/** 是否绘制的时候，需要使用Transform */
	private boolean bNeedTransform = false;
	
	private Canvas mTransformCanvas = null; //这个是否需要，待定
	private Graphics mTransformG	= null;
	private Image  mTransformImage = null;
	public void TransformReset()
		{
			Transform.reset();
			bNeedTransform = false;
		}
	/**
	 * 确保绘图缓冲的尺寸
	 * @param w
	 * @param h
	 */
	public void EnsureTransformImageSize(int w,int h)
		{
			Image img;
//			if (null==(img = mTransformImage) || img.getWidth()<w || img.getHeight()<h)
//			if (true)
//			if(null == mTransformImage)
				{
					img = mTransformImage = Image.createImage(w,h,Config.ARGB_8888);
					mTransformG = img.getGraphics();
				}
//			else
//				{
//					mTransformG.setARGBColor(0x00000000);
//					mTransformG.fillRect(0,0,img.getWidth(),img.getHeight());
//				}
		}
	/**
	 * 设定缩放，注：缩放的结果貌似会放 旋转的轴心偏移，尚未检查修正
	 * @param sx
	 * @param sy
	 */
	public final void TransformScale(float sx,float sy)
		{
			if(!bNeedTransform)
				Transform.setScale((ScaleX=sx), (ScaleY=sy));
			else
				Transform.postScale((ScaleX=sx), (ScaleY=sy));
			bNeedTransform = true;
		}
	public final void TransformsetRotate(float degrees)
		{
			if(!bNeedTransform)
				Transform.setRotate(degrees);
			else
				Transform.postRotate(degrees);
			bNeedTransform = true;
		}
	public final void TransformsetRotate(float degrees, float px, float py)
		{
			if(!bNeedTransform)
				Transform.setRotate(degrees, px, py);
			else
				Transform.postRotate(degrees, px, py);
			bNeedTransform = true;
		}
	public void TransformFlush(Graphics g,int x, int y)
		{
			if(!bNeedTransform)
				Transform.setTranslate(x,y);
			else
				Transform.postTranslate(x,y);
			g.mCanvas.drawBitmap(mTransformImage.mBitmap,Transform, null);
		}
//#endif	
	
	//--------------------------------------------------------------------------------------------------------------------
	/// Empty constructor.
	/// &note It is up to user to fill all information.
	//--------------------------------------------------------------------------------------------------------------------
	private zAnimPlayer ()
	{
        Reset();
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Basic constructor. Default constructor to use to Play some animations.
	/// &param sprite Sprite to use by this player. The player will only keep a reference to the zSprite eg it won't be copied.
	/// &param x Ininital X Position of the player.
	/// &param y Ininital Y Position of the player.
	//--------------------------------------------------------------------------------------------------------------------
	private zAnimPlayer(zSprite sprite, int x, int y)
	{
        Reset();
		Ints[I_X] 	= x;
		Ints[I_Y] 	= y;
		SetSprite(sprite);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Reset the current player. The player will be in the same state as if you would have called the empty constructor.
	/// &note Usefull only on the zSprite player, no effect on the sound.
	//--------------------------------------------------------------------------------------------------------------------
	public final void Reset()
	{
		if (Ints!=null)
		{
			for(int i = Ints.length-1;i>=0;i--)
			{
				Ints[i] = 0;
			}
		}
		else
		{
			Ints = new int[I_MAX];
		}
		
		if(LastFrameRC!=null)
		{
			// 当前矩形数据的帧索引
			LastFrameRC[LastFrameRC.length - 1] = -1;
		}
		
		Ints[I_ANIM] = -1;
		Ints[I_LOOP] = 1;
		sprite 	= null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Set new position of sprite.
	/// &param x Sprite new X Position.
	/// &param y Sprite new Y Position.
	/// &note There are no validation of the new position, it can be outside of the screen. You can also access directly the Ints[I_X] and Ints[I_Y] variables.
	//--------------------------------------------------------------------------------------------------------------------
	public final void SetPos (int x, int y)
	{
		int[] Ints = this.Ints;
		Ints[I_X] = x;
		Ints[I_Y] = y;
	}
	public final int GetPosX()
	{
		return Ints[I_X];
	}
	public final int GetPosY()
	{
		return Ints[I_Y];
	}
	public final int[] GetPos()
	{
		return new int[] {Ints[I_X],Ints[I_Y]};
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Get the current sprite
	//--------------------------------------------------------------------------------------------------------------------
	public zSprite GetSprite ()
	{
		return sprite;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Set a new zSprite reference in the player. This call will also reset the animation number.
	/// &param sprite Sprite to be used from now on. If the sprite is null, this will remove the reference to any sprite from this player.
	//--------------------------------------------------------------------------------------------------------------------
	public final void SetSprite (zSprite newsprite)
	{
		sprite = newsprite;
		if (newsprite != null)
			SetAnim (-1, -1,true);
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Set a new animation number to play.
	/// &param anim Animation number to play
	/// &param Ints[I_LOOP] how many time this animation should loop (-1 for forever)(cannot be 0)
	/// &param restart restart animation from begining if set to true, continue where it was otherwise
	//--------------------------------------------------------------------------------------------------------------------
	 void SetAnim (int anim, int nbLoop, boolean restart)
	{
		if (restart)
			SetAnim(-1, 1);

		SetAnim(anim, nbLoop);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Set a new animation number to play.
	/// &param anim Animation number to play
	/// &param Ints[I_LOOP] how many time this animation should loop (-1 for forever)(cannot be 0)
	///&note if an animation is currently playing, and try a setanim on the same anim . nothing is done, do SetAnim(-1, 1);SetAnim(anim, loop) to force an update
	//--------------------------------------------------------------------------------------------------------------------
	public final void SetAnim (int anim, int nloop)
	{
		if((sprite == null)) 
			{
			Utils.Err("zAnimPlayer.SetAnim().sprite is not set");;
			return;
			}
		if(nloop == 0)Utils.DBG_PrintStackTrace(false, "GLLibPlayer.SetAnim().nbLoop is invalid");;
		if(anim >= GetNbanim())
			{
			Utils.DBG_PrintStackTrace(false, "zAnimPlayer().anim out of range");;
			}
		int[] Ints = this.Ints;
		Ints[I_OVER] = 0;
		Ints[I_ANIM] = anim;
		Ints[I_LOOP] = nloop - 1;
		SetFrame(0);
	}
	public final int[] GetFrameRect(int anim, int aframe) {
		int [] LastFrameRC = this.LastFrameRC;
		int frame = sprite.GetAnimFrame(anim, aframe);
		if ((LastFrameRC!=null) && (LastFrameRC[4]==frame))return LastFrameRC;
		sprite.GetFrameRect(LastFrameRC, frame, 0, 0, 0);
		LastFrameRC[4]=frame;
		return LastFrameRC;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get animation
	/// &return current selected animation, -1 if none
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetAnim ()
	{
		return Ints[I_ANIM];
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Set a new frame position in the current animation.
	/// &param frame New frame postion.
	/// &return Frame adjusted if frame was larger than the frame count. Ex: if you ask for frame 7 of a 5 frame animation, this call will place the frame at 2. return -1 if no animation
	/// &note In DEBUG, there will be an assert if frame is smaller than 0, but not in RELEASE.
	//--------------------------------------------------------------------------------------------------------------------
	public final int SetFrame (int frame)
	{
		if (sprite == null) {
			Utils.Dbg("zAnimPlayer.SetFrame().sprite is not set");;
			return -1;
		}
		if(frame < 0)
			{
			Utils.DBG_PrintStackTrace(false, "zAnimPlayer.SetFrame().frame is negative");;
			};

		if (Ints[I_ANIM] < 0)
			return -1;
		int nbFrame = GetNbFrame();
		while (frame > nbFrame)
		{
			frame -= nbFrame;
		}
		Ints[I_FRAME] 	= frame;
		Ints[I_TIME] 	= 0;
		return frame;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get current frame nb
	/// &return current frame nb in current animation, result is invalid if no animation is set
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetFrame ()
	{
		return Ints[I_FRAME];
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get current frame ID of the current frame
	/// &return frame ID of the current frame within the animation
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetAnimFrame ()
	{
		int[] Ints = this.Ints;
		if (sprite == null || Ints[I_ANIM] == -1)
		{
			return -1;
		}

		return sprite.GetAnimFrame(Ints[I_ANIM], Ints[I_FRAME]);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// 得到动画当前帧的矩形框
	/// &return int[] 返回当前动画当前帧的动画矩形框 
	/// TODO 可以使用一定的缓存机制将当前帧的数据缓存起来 
	//--------------------------------------------------------------------------------------------------------------------
	public int[] GetCurFrameRect() {
		return GetCurFrameRect(0, 0, Ints[I_FLAG]);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// 给定位置和标志，得到动画当前帧的矩形框
	/// &param x, y int 动画的坐标。 flag int 动画的标志
	/// &return int[] 返回当前动画当前帧的动画矩形框 
	/// TODO 可以使用一定的缓存机制将当前帧的数据缓存起来 
	//--------------------------------------------------------------------------------------------------------------------
	
	public final int[] GetCurFrameRect(int x, int y, int flag) {
		if (sprite == null || GetAnim() < 0)
			return null;

		int[] rect = new int[4];
		sprite.GetAFrameRect(rect, GetAnim(), GetFrame(), x, y, flag);

		return rect;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// 得到当前动画的翻转标志 
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetFlag() {
		return Ints[I_FLAG];
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// 设置当前动画的翻转标志 
	//--------------------------------------------------------------------------------------------------------------------
	public final void SetFlag(int flag) {
		Ints[I_FLAG] = flag;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set current sprite transformation
	/// &param transform  any of GLLib transform
	///&see GLLib.TRANS_NONE, GLLib.TRANS_ROT90, GLLib.TRANS_ROT180, GLLib.TRANS_ROT270, GLLib.TRANS_MIRROR, GLLib.TRANS_MIRROR_ROT90, GLLib.TRANS_MIRROR_ROT180, GLLib.TRANS_MIRROR_ROT270
	//--------------------------------------------------------------------------------------------------------------------
	public final void SetTransform (int transform)
	{
		int value;
		switch (transform)
		{
			case GLLib.TRANS_NONE:
				value = 0;
			break;
			case GLLib.TRANS_MIRROR:
				value = zSprite.FLAG_FLIP_X;
			break;
			case GLLib.TRANS_MIRROR_ROT180:
				value = zSprite.FLAG_FLIP_Y;
			break;
			case GLLib.TRANS_ROT180:
				value = zSprite.FLAG_FLIP_X | zSprite.FLAG_FLIP_Y;
			break;
			case GLLib.TRANS_ROT90:
				value = zSprite.FLAG_ROT_90;
			break;
			case GLLib.TRANS_ROT270:
				value = zSprite.FLAG_FLIP_X | zSprite.FLAG_FLIP_Y | zSprite.FLAG_ROT_90;
			break;
			case GLLib.TRANS_MIRROR_ROT90:
				value = zSprite.FLAG_FLIP_Y | zSprite.FLAG_ROT_90;
			break;
			case GLLib.TRANS_MIRROR_ROT270:
				value = zSprite.FLAG_FLIP_X | zSprite.FLAG_ROT_90;
			break;
			default:
			return;
		}
		Ints[I_FLAG] = value;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get current sprite transformation
	///&return one of GLLib.TRANS_NONE, GLLib.TRANS_ROT90, GLLib.TRANS_ROT180, GLLib.TRANS_ROT270, GLLib.TRANS_MIRROR, GLLib.TRANS_MIRROR_ROT90, GLLib.TRANS_MIRROR_ROT180, GLLib.TRANS_MIRROR_ROT270
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetTransform ()
	{
		switch(Ints[I_FLAG])
		{
			case 0:
				return GLLib.TRANS_NONE;
			case (zSprite.FLAG_FLIP_X):
				return GLLib.TRANS_MIRROR;
			case (zSprite.FLAG_FLIP_Y):
				return GLLib.TRANS_MIRROR_ROT180;
			case (zSprite.FLAG_FLIP_X | zSprite.FLAG_FLIP_Y):
				return GLLib.TRANS_ROT180;
			case (zSprite.FLAG_ROT_90):
				return GLLib.TRANS_ROT90;
			case (zSprite.FLAG_FLIP_X | zSprite.FLAG_FLIP_Y | zSprite.FLAG_ROT_90):
				return GLLib.TRANS_ROT270;
			case (zSprite.FLAG_FLIP_Y | zSprite.FLAG_ROT_90):
				return GLLib.TRANS_MIRROR_ROT90;
			case (zSprite.FLAG_FLIP_X | zSprite.FLAG_ROT_90):
				return GLLib.TRANS_MIRROR_ROT270;
		}
		return -1;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the animation count of the current sprite.
	/// &return The animation count of the current sprite.
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetNbanim ()
	{
		if(sprite == null){Utils.DBG_PrintStackTrace(false, "zAnimPlayer.GetNbanim().sprite is not set");return -1;}
		return sprite.GetAnimationCount();
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the frame count of the current animation.
	/// &return The frame count of the current animation or -1 if the animation is not set.
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetNbFrame ()
	{
		if(sprite == null)Utils.DBG_PrintStackTrace(false, "zAnimPlayer.GetNbFrame().sprite is not set");;
		if (Ints[I_ANIM] >= 0)
			return sprite.GetAFrames(Ints[I_ANIM]);
		else
			return -1;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the duration of the current frame of the current animation.
	/// &return The duration of the current frame of the current animation or 0 if the animation is not set.
	//--------------------------------------------------------------------------------------------------------------------
	public final int GetDuration ()
	{
		 if(sprite == null)Utils.DBG_PrintStackTrace(false, "zAnimPlayer.GetDuration().sprite is not set");;

		if (Ints[I_ANIM] >= 0)
			return sprite.GetAFrameTime(Ints[I_ANIM], Ints[I_FRAME]) * k_animBaseFrameTime;
		else
			return 0;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Query the state of the current animation to see if its over.
	/// &return True if the current animation is over.
	//--------------------------------------------------------------------------------------------------------------------
	public final boolean IsAnimOver ()
	{
		if((sprite == null)) {
			return true;
		}
		if (Ints[I_ANIM] < 0)
			return true;
		if (Ints[I_LOOP] < 0)
			return false;
		return (Ints[I_OVER]!=0);
	}
	//-------------------------------------------
	//make sure the anim is over!
	//-----------------------------------
	public final void SetAnimOver(){
		if((sprite == null))
			return;
		Ints[I_OVER] = 1;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Render the curreny animation in GLLib curent graphic context. The animation will be drawn at Ints[I_X],Ints[I_Y].
	/// \sa zSprite.setCurrentGraphics
	//--------------------------------------------------------------------------------------------------------------------
	public final void Render (Graphics g)
	{
		if((sprite == null)) {
			return;
		}
		int[] Ints = this.Ints;
		if (Ints[I_ANIM] < 0)
			return;
		
//#if PLATFORM=="Android"
		if (bNeedTransform)
			{
				//先绘制到转换用的缓冲里
				int [] R = GetFrameRect(Ints[I_ANIM], Ints[I_FRAME]);
				EnsureTransformImageSize(R[2]-R[0],R[3]-R[1]);
				sprite.PaintAFrame(mTransformG, Ints[I_ANIM], Ints[I_FRAME], (R[2]-R[0])>>1, (R[3]-R[1])>>1, Ints[I_FLAG], 0, 0, this);
//				TransformFlush(g,Ints[I_X] + ((R[2]-R[0])>>1), Ints[I_Y]+((R[3]-R[1])>>1));
				TransformFlush(g,Ints[I_X] - ((R[2]-R[0])>>1), Ints[I_Y]-((R[3]-R[1])>>1));
			}
		else
			{
				sprite.PaintAFrame(g, Ints[I_ANIM], Ints[I_FRAME], Ints[I_X], Ints[I_Y], Ints[I_FLAG], 0, 0, this);
			}
//#else		
		sprite.PaintAFrame(g, Ints[I_ANIM], Ints[I_FRAME], Ints[I_X], Ints[I_Y], Ints[I_FLAG], 0, 0, this);
//#endif

	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Update current animation time from 1 time unit. If the current frame time is exceeded the current frame will be increased, if the current
	/// frame reach the animation frame count, the current frame will be looped to 0.
	///&deprecated use void Update(int DT) instead
	//--------------------------------------------------------------------------------------------------------------------
	public final void Update ()
	{
		 if (sprite == null)
			 return;
//		Update(DevConfig.FPS_INTERVAL);
		Update(GLLib.s_Tick_Paint_FrameDT);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Update current animation time from 1 time unit. If the current frame time is exceeded the current frame will be increased, if the current
	/// frame reach the animation frame count, the current frame will be looped to 0.
	/// &param DT nb of time unit elapsed since last frame
	//--------------------------------------------------------------------------------------------------------------------
	public final void Update (int DT)
	{
		int[] Ints = this.Ints;
		if(DT < 0)
			Utils.DBG_PrintStackTrace(false, "zAnimPlayer.Update.DT is negative");;
			//Time Based
		if(DT == 1)
		{
			Utils.Err("zAnimPlayer.Update was called with DT equal 1, the player is currently working as a Time Based Player, it should be more than 1 ms ?");;
		}
		// if anim is over, or no anim, do nothing
		if ((Ints[I_OVER]!=0) || (Ints[I_ANIM] < 0))
			return;

		// get duration of current frame
		int duration = GetDuration();

		if (DevConfig.SPRITE_ANIM_DURATIONZERO_AS_INFINITE)
		{
			if (duration == 0)
			{
				// -- no need to update, it's a never ending animation
				return;
			}
		}
		else
		{
			if(duration == 0)Utils.DBG_PrintStackTrace(false, "zAnimPlayer.Update.frame "+Ints[I_FRAME]+" of animation "+Ints[I_ANIM]+" has a duration of 0");;
		}

		if (DevConfig.SPRITE_NOSKIPFRAME)
		{
			// pass frame until duration is matched (or anim is over)
			if (Ints[I_TIME] >= duration) // could cause a pb if Ints[I_ANIM]==-1
			{
				// update current time to set time for next frame
				Ints[I_TIME] -= duration;

				// pass to next frame or loop or end animation
				if (Ints[I_FRAME] < sprite.GetAFrames(Ints[I_ANIM]) - 1)
				{
					// pass to next frame
					Ints[I_FRAME]++;
				}
				else
				{
					// if end of loop for this animation -. animation is over
					if (Ints[I_LOOP] == 0)
					{
						Ints[I_OVER] = 1;
					}
					else
					{
						// decrease nb of loop if not looping forever
						if (Ints[I_LOOP] > 0)
							Ints[I_LOOP]--;
						// reset to first frame of animation
						Ints[I_FRAME] = 0;
					}
				}
			}
		}
		else
		{
			// pass frame until duration is matched (or anim is over)
			while (Ints[I_TIME] >= duration) // could cause a pb if Ints[I_ANIM]==-1
			{
				// update current time to set time for next frame
				Ints[I_TIME] -= duration;

				// pass to next frame or loop or end animation
				if (Ints[I_FRAME] < sprite.GetAFrames(Ints[I_ANIM]) - 1)
				{
					// pass to next frame
					Ints[I_FRAME]++;
				}
				else
				{
					// if end of loop for this animation -. animation is over
					if (Ints[I_LOOP] == 0)
					{
						Ints[I_OVER] = 1;
						break;
					}
					else
					{
						// decrease nb of loop if not looping forever
						if (Ints[I_LOOP] > 0)
							Ints[I_LOOP]--;
						// reset to first frame of animation
						Ints[I_FRAME] = 0;
					}
				}
				// get duration of new frame
				duration = GetDuration();

				if (DevConfig.SPRITE_ANIM_DURATIONZERO_AS_INFINITE)
				{
					if (duration == 0)
					{
						break;
					}
				}
			}
		}

		// update current frame time
		Ints[I_TIME] += DT;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Update current animation by revert order.
	//--------------------------------------------------------------------------------------------------------------------
	void UpdateRevert ()
	{
		Utils.Err("zAnimPlayer.UpdateRevert(). this function is deprecated, use zAnimPlayer.UpdateRevert(int DT) instead");;
			//Time Based
			UpdateRevert(DevConfig.FPS_INTERVAL);
	}
	void UpdateRevert (int DT)
	{
		int[] Ints = this.Ints;
		if(!(DT >= 0))
			Utils.DBG_PrintStackTrace(false, "zAnimPlayer.Update.DT is negative");;
		//Time Based
		if(DT == 1)
		{
			Utils.Err("zAnimPlayer.Update was called with DT equal 1, the player is currently working as a Time Based Player, it should be more than 1 ms ?");;
		}

		// if anim is over, or no anim, do nothing
		if ((Ints[I_OVER]!=0) || (Ints[I_ANIM] < 0))
			return;

		// get duration of current frame
		int duration = GetDuration();

		if (DevConfig.SPRITE_ANIM_DURATIONZERO_AS_INFINITE)
		{
			if (duration == 0)
			{
				// -- no need to update, it's a never ending animation
				return;
			}
		}
		else
		{
			if(!(duration != 0))Utils.DBG_PrintStackTrace(false, "zAnimPlayer.Update.frame "+Ints[I_FRAME]+" of animation "+Ints[I_ANIM]+" has a duration of 0");;
		}

		boolean isJustStart = (Ints[I_TIME] == 0 && Ints[I_FRAME] == 0);
		
		// update current frame time
		Ints[I_TIME] -= DT;
		zSprite sprite = this.sprite;
		while (Ints[I_TIME] < 0)
		{
			// get previous frame
			if (Ints[I_FRAME] == 0)
			{
				if (isJustStart) {
					// get last frame of animation
					Ints[I_FRAME] = sprite.GetAFrames(Ints[I_ANIM]) - 1;
				} else if (Ints[I_LOOP] < 0) {
					Ints[I_FRAME] = sprite.GetAFrames(Ints[I_ANIM]) - 1;
				} else if (Ints[I_LOOP] >= 1) {
					Ints[I_LOOP]--;
					Ints[I_FRAME] = sprite.GetAFrames(Ints[I_ANIM]) - 1;
				} else {
					Ints[I_OVER]=1;
				}
			}
			else
				Ints[I_FRAME]--;
		
			// get previous frame duration
			duration = GetDuration();
		
			Ints[I_TIME] += duration;
		}

	}

	zAnimPlayerListener playerListener;
	
	public final void setPlayerListener(zAnimPlayerListener pl) {
		playerListener = pl;
	}
	
	public final boolean canTriggerListener() {
		return (playerListener != null);
	}
	public final boolean triggerListener(int[] data) {
		zAnimPlayerListener playerListener = this.playerListener;
		if (playerListener != null) {
			return playerListener.onTouchedJYModule(data, data.length);
		} else {
			return false;
		}
	}
	
/**********************************************************\
	技能对象管理池
\**********************************************************/
	private final static int ZANIMPLAYER_POOL_ENABLE = 0;
	private final static int ZANIMPLAYER_POOL_SIZE = 10;
	private static zAnimPlayer[] __animplayer_pool= new zAnimPlayer[ZANIMPLAYER_POOL_ENABLE* ZANIMPLAYER_POOL_SIZE];
	private static int __animplayer_pool_count = 0;
	/** 技能系统 */
	public final static zAnimPlayer Create()
	{
		if(ZANIMPLAYER_POOL_ENABLE!=0 && (__animplayer_pool_count>0))
			{
			zAnimPlayer retV;
			Utils.Dbg("zAnimPlayer by Pool s_poolCount = " + __animplayer_pool_count);			
			int idx = __animplayer_pool_count -1;
			retV = __animplayer_pool[idx];
			__animplayer_pool[idx] = null;
			__animplayer_pool_count--;
			return retV;
			}
			else
			{
				Utils.Dbg("zAnimPlayer.Create by New");			
				return new zAnimPlayer();
			}	
	}
	public final static void Destroy(zAnimPlayer animplayer)
	{
		if (ZANIMPLAYER_POOL_ENABLE!=0 && __animplayer_pool_count < ZANIMPLAYER_POOL_SIZE ) {
			__animplayer_pool[__animplayer_pool_count] = animplayer;
			__animplayer_pool_count ++;
			Utils.Dbg("zAnimPlayer.Destroy s_poolCount = " + __animplayer_pool_count);				
		} else {
		}
	}		
	
}
