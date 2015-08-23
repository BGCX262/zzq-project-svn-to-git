package cn.thirdgwin.lib;
/** 
 * 配置方法小贴士:
 * 		个别图片Single Image方式: 	ExportBSpriteEx("mc.bsprite", BS_MODULES| BS_MODULES_IMG | BS_FRAMES | BS_ANIMS | BS_FRAME_RECTS | BS_FM_OFF_SHORT | BS_MODULES_XY_SHORT | BS_MULTIPLE_IMAGES, A256I256RLE, _8888)
 *			sprite_useBSpriteFlags = true
 *			sprite_useModuleXY = true
 *			sprite_useModuleXYShort = 可选，根据export指令
 *			sprite_useExternImage 必须打开(未确定)
 *
 *		单Sprite多Image方式换装的思路是不同的身体部件使用不同的Image,
 *		打开sprite_useExternImage，打开Single Image方式，将装备的图片手工载入并SetImage即可。
 * 		MAX_SPRITE_PALETTES:
 * 			用于设定一个ASprite对象所持有的Image数量，这些Image可用于换装系统。
 **/
//--------------------------------------------------------------------------------
import javax.microedition.rms.*;
import java.util.*;
import java.io.*;
import javax.microedition.lcdui.*;
import cn.thirdgwin.io.zRMS;
import cn.thirdgwin.utils.zProfile;
import java.util.Hashtable;
		
//--------------------------------------------------------------------------------------------------------------------
/// The GLLib class is the main class to do a game creation at Gameloft. The GLLib main function is to be the hearth of
/// your game, it impletements Runnable by starting a Thread.
/// The GLLib provides the developer with methods to handle game key events by doing the mapping to your device.
/// Package management for your resources is also provided by the GLLib class. You have acces also to basic math support.
//--------------------------------------------------------------------------------------------------------------------

public abstract class GLLib extends javax.microedition.lcdui.Canvas implements Runnable

{
	static public boolean k_EnableFPSLimiter							= true;
/// \defgroup GLLib_coreSystem GLLib : Core System.
/// all low level phone integration
/// \ingroup GLLibMain
///!{

	/// Graphics context where all rendering operation will happen. this graphic context can be changed/reseted with SetCurrentGraphics
	///!see SetCurrentGraphics
	static public javax.microedition.lcdui.Graphics				g 		= null;

	/// reference on the latest gaphic context as passed by paint() or the double buffer graphic context if zJYLibConfig.useSoftwareDoubleBuffer is true
	static protected javax.microedition.lcdui.Graphics s_lastPaintGraphics = null;

	// reference to screen graphics as passed to paint. Will never change. Needed since other graphics context may point to double buffers.
	static public javax.microedition.lcdui.Graphics s_screenGraphics     = null;

	/// Pause state of the game. True if the game is paused.
	static boolean 			s_game_isPaused;
	
	/// Current time when the frame started.
	private static long 			s_Tick_Paint_FrameStart;

	/// Similar to s_Tick_Paint_FrameStart but internally used for zJYLibConfig.useFakeInterruptHandling
	private static long 			s_Tick_FakeInt_Paint_LastFrame;

	/// Interrupt notifier. Set to true when an interrupt occured.
	static boolean 			s_game_interruptNotify;

	/// Screen Width of your device
	static private int 		s_screenWidth 		= DevConfig.SCR_W;
	/// Screen Height of your device
	static private int 		s_screenHeight 		= DevConfig.SCR_H;


	/// reference to the display
	static Display 			s_display;

	/// Multi-entry control. To be sure that we are only once into the paint method.
	private static boolean 	s_game_isInPaint;
	
	private static boolean 	s_bRunning	= true;

	/// Reference to the application. Usually its a reference to a midlet (or IApplication for doja).
	/*private*/ static 			javax.microedition.midlet.MIDlet 	s_application;

	/// Idle time to reach "ideal" fps. Allow to limit the frame rate on very powerful phones<br>
	/// Max fps = 1000/m_FPSLimiter
	private static int 		m_FPSLimiter 		= 1000/DevConfig.FPSLimiter;
	/// Time tracker to compute the frame limiter.
	private static long 	s_Tick_Run_Last;
	public static long 		s_Tick_Run_DT = 1;

	/// Allows user to overwrite sleep duration used per frame. Only used when allowCustomSleepTime is TRUE
	private static int      m_customSleepTime   = -1;


	// if zJYLibConfig.useSoftwareDoubleBuffer
		/// Software double buffer image. &note Non null only if zJYLibConfig.useSoftwareDoubleBuffer is true.
		private static Image 	   m_imgBackBuffer 		= null;
		/// Software double buffer graphic context. &note Non null only if zJYLibConfig.useSoftwareDoubleBuffer is true.
		private static Graphics    m_gBackBuffer 		= null;
		/// Allows turning off of the backbuffer
		private static boolean     m_bBackBufferEnabled = true;
	// if zJYLibConfig.useSoftwareDoubleBuffer END


	//	if zJYLibConfig.useFrameDT == true
		/// Delta time between the previous frame and this one. &note valid only if zJYLibConfig.useFrameDT is true.
		public static int 			s_Tick_Paint_FrameDT;
		/**用于动画更新的上一帧时间间隔。会被Cinematic或者其他机制进行更改。*/
		static int 			s_anim_frameDT;
		/// End time of the previous frame. &note valid only if zJYLibConfig.useFrameDT is true.
		static private long s_Tick_Paint_FrameStart_Last;
		/// Total game execution time. &note valid only if zJYLibConfig.useFrameDT is true.
		static int 			s_game_totalExecutionTime;
		/// Average fps * 100. &note valid only if zJYLibConfig.useFrameDT is true.
		static int 			s_game_FPSAverage;
	//	if zJYLibConfig.useFrameDT == true   END

	/// Current frame number. Increased every frame.
	static int 				s_game_currentFrameNB;

	//--------------------------------------------------------------------------------------------------------------------
	/// Function to be implemented in every game.
	/// This is where you put the code of your game. This function will be called once per frame,
	/// You have to do the game Logic/Ai and Painting from whitin.
	/// This function is called from the paint call of this canvas.
	//--------------------------------------------------------------------------------------------------------------------
	/** 游戏逻辑、AI、绘制应在这个函数完成,如果希望 */
	public abstract void OnPaint () throws Exception;
	/** 当游戏从后台恢复到前台的时候，画面绘制函数，建议直接绘制暂停中 */
	public abstract void OnPaintPause() throws Exception;
	/** 代码主线程运行会调用此函数，可以为空 */
	public abstract void OnTick () throws Exception;
//--------------------------------------------------------------------------------------------------------------------
/// Constructor.
/// The constructor, will keep reference to the application and display, it will also setup its display
/// This call wont start the engine of the game, you need to call init to start the game engine.
///!param application Reference on the application (midlet, IApplication, etc...).
///!param display Reference on the display.
/// \sa GLLib.init
//--------------------------------------------------------------------------------------------------------------------
	public GLLib(Object application, Object display) {
		Utils.Dbg("GLLib.constructor");
		;
		Init(application, display);
	}

	protected void Init(Object application, Object display) {
		Utils.Dbg("GLLib.init");
		;
		s_game_isInPaint = true;
		// set reference to the application
		s_application = (javax.microedition.midlet.MIDlet) application;
		// set the display object
		s_display = (Display) display;
		SetupDisplay();
		cKeyboard.SetupDefaultKey();
		// init dt timer
		s_Tick_Paint_FrameStart_Last = System.currentTimeMillis();
		// init coherence
		s_Tick_Run_Last = s_Tick_Paint_FrameStart_Last;

		// init math random number generator
		cMath.Math_RandSetSeed(System.currentTimeMillis());
		// launch game
		if (DevConfig.useCallSerially) {
			if (s_display != null) {
				s_display.callSerially(this);
			}
		} else {
			new Thread(this).start();
		}
	}

//--------------------------------------------------------------------------------------------------------------------
/// Initialize and start the game engine.
/// Allocate basic game variable/structure, launch game thread.
//--------------------------------------------------------------------------------------------------------------------
	public void Init_softDoublebuffer() {
		if (DevConfig.useSoftwareDoubleBuffer) {
			if ((m_imgBackBuffer != null) && (m_gBackBuffer != null))
				return;
			// create and allocate the software double buffer
			if (DevConfig.useSoftwareDoubleBufferLarge) {
				// get the larger dimension and use that for the buffer
				int size = ((getWidth()) > (getHeight()) ? (getWidth())
						: (getHeight()));
				m_imgBackBuffer = Image.createImage(size, size);
			} else {
				m_imgBackBuffer = Image.createImage(getWidth(), getHeight());
			}

			m_gBackBuffer = m_imgBackBuffer.getGraphics();
		}
	}
	protected static Image GetSoftwareDoubleBuffer()
	{
		if (DevConfig.useSoftwareDoubleBuffer)
		{
			return m_imgBackBuffer;
		}
		else
		{
			return null;
		}
	}
	protected static Graphics GetSoftwareDoubleBufferGraphics()
	{
		if (DevConfig.useSoftwareDoubleBuffer)
		{
			return m_gBackBuffer;
		}
		else
		{
			return null;
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Quit game engine, free all memory and object.
	// / This function will be called automaticaly at the end of the game loop.
	// --------------------------------------------------------------------------------------------------------------------
	protected void UnInit() {
		Utils.Dbg("GLLib.deInit");
		;

		// deallocate double buffer if needed
		if (DevConfig.useSoftwareDoubleBuffer) {
			// create and allocate the software double buffer
			m_imgBackBuffer = null;
			m_gBackBuffer = null;
		}
		// deallocate mime buffer
		cPack.MIME_type = null;

		cKeyboard.Game_KeyClearKeyCode();
		Gc();
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Pause the game engine. Can be called by the Midlet, GLLib.hideNotify or
	// the game itself.
	// / \sa GLLib.hideNotify
	// --------------------------------------------------------------------------------------------------------------------
	protected void Pause() {
		Utils.Dbg("GLLib.Pause Call");
		;
		if (!s_game_isPaused) {
			s_game_isPaused = true;
			Utils.Dbg("GLLib.Pausing");
			;
			zSoundPlayer.Snd_PauseNotify();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Resume the game engine. Can be called by the Midlet or
	// GLLib.showNotify.
	// / If the game was not in pause state, nothing happens.
	// / \sa GLLib.showNotify
	// --------------------------------------------------------------------------------------------------------------------
	protected void Resume() {
		Utils.Dbg("GLLib.Resume Call");
		;
		if (s_game_isPaused) {
			Utils.Dbg("GLLib.Resuming");
			long time = System.currentTimeMillis();
			s_Tick_Paint_FrameStart = time;
			s_Tick_Paint_FrameStart_Last = time;
			s_Tick_Run_Last = time;
			s_game_isPaused = false;
			s_game_isInPaint = false;
			SetupDisplay();
			s_game_interruptNotify = false;
			OnResume();
			repaint();
			cKeyboard.ResetKey();
		}
	}
	/** 当游戏被切换到前台的时候，本事件发生一次，伴生变量s_game_interruptNotify = true */
	public abstract void OnResume();
	// --------------------------------------------------------------------------------------------------------------------
	// / Override of Canvas.hideNotify().
	// / Standard devices will call this function when the game is interrupted.
	// / \sa GLLib.pause
	// --------------------------------------------------------------------------------------------------------------------
	public void hideNotify() {
		Pause();
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Override of Canvas.showNotify().
	// / Standard devices will call this function when the game is made visible
	// on the display.
	// / \sa GLLib.resume
	// --------------------------------------------------------------------------------------------------------------------
	public void showNotify() {
		Resume();
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Called when the drawable area has been changed
	// --------------------------------------------------------------------------------------------------------------------
	public void sizeChanged(int w, int h) {
		// Malean:2010/11/5 N97在切换canvas之后会调用该函数修改屏幕宽高导致游戏出错。
		// s_screenWidth = w;
		// s_screenHeight = h;

	}

//--------------------------------------------------------------------------------------------------------------------
/// Setup the display. Does different thing denpending of the device. <br>
/// For MIDP20 it will setup the display to this instance of GLLib and put the game in fullscreen.<br>
/// For NOLIA UI it will setup the display to this instance of GLLib and set the command listener to null.<br>
/// etc...
//--------------------------------------------------------------------------------------------------------------------
	protected void SetupDisplay() {
		if (s_display != null) {
			if (s_display.getCurrent() != this) {
				s_display.setCurrent(this);
			}
		}

		if (!DevConfig.MIDP2forceNonFullScreen) {
			setFullScreenMode(true);
		}
//#if BRAND=="Nokia"
		// WuJun：解决N73手机的canvas切换失灵bug。添加sleep(10)
		// Malean: 2011/1/20
		// 解决N7370手机上不能全屏的bug。测试结果：setCurrent之后不立即setFullScreen会导致不能全屏。所以将代码移到此位置
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
//#endif
	}
//--------------------------------------------------------------------------------------------------------------------
/// Request to quit the game engine.
/// When the user select exit/quit (or for any other reason the request quit a game) you should use this function.
//--------------------------------------------------------------------------------------------------------------------
final static protected void Quit ()
{
	s_bRunning = false;
	Utils.Dbg("GLLib.quit");
}


//--------------------------------------------------------------------------------------------------------------------
/// Game engine main loop/thread. Override of Runnable.run.
///!note The game should not call this function, it will be called automaticaly by the thread. The game code has to go into the abstract function GLLib.Game_update.
/// \sa GLLib.Game_update
//--------------------------------------------------------------------------------------------------------------------
public void run ()
 {
		if (!DevConfig.useCallSerially) {
			Utils.Dbg("GLLib.run");
		}
		// set a general try ... catch to avoid many small ones
		try {
			if (!DevConfig.useCallSerially) {
				SetupDisplay();
			}
			// unblock paint call
			s_game_isInPaint = false;
			// main loop
			while (s_bRunning) {
				if (!s_game_isPaused) {
					repaint();
					if (DevConfig.useServiceRepaints) {
						serviceRepaints();
					}
					zProfile.Reset(false);
					// call game specific run code
					OnTick();
					s_game_interruptNotify = false;
					// Prevent bug, when the clock skip backward after an
					// interrupt.
					long curTime = System.currentTimeMillis();
					if (s_Tick_Run_Last > curTime)
						s_Tick_Run_Last = curTime;
					if (k_EnableFPSLimiter) {
						if (DevConfig.allowCustomSleepTime
								&& (m_customSleepTime >= 0)) {
							if (m_customSleepTime == 0) {
								Thread.yield();
							} else {
								Thread.sleep(m_customSleepTime);
							}
						} else {
							// frame rate limiter FPS 限制
							if (DevConfig.useSleepInsteadOfYield) {
								Thread.sleep(Math.max(0, m_FPSLimiter
										- (curTime - s_Tick_Run_Last)));
							} else {
								while (curTime - s_Tick_Run_Last < m_FPSLimiter) {
									Thread.yield();
									curTime = System.currentTimeMillis();
									s_Tick_Run_Last = Math.min(s_Tick_Run_Last,
											curTime);
								}
							}
						}
					}
					s_Tick_Run_DT = System.currentTimeMillis()
							- s_Tick_Run_Last;
					s_Tick_Run_Last = curTime;
				} else {
					s_Tick_Run_Last = Math.min(s_Tick_Run_Last,
							System.currentTimeMillis());

					if (DevConfig.useSleepInsteadOfYield) {
						Thread.sleep(DevConfig.sleepDurationWhenSuspended);
					} else {
						Thread.yield();
					}
				}

				if (DevConfig.useCallSerially) {
					s_display.callSerially(this);
					return;
				}
			}
		} catch (Exception e) {
			Utils.Dbg("!!FATAL ERROR!! in cGame.run()." + e);
			e.printStackTrace();
			s_bRunning = false;
		}
		Utils.Dbg("GLLib.Quitting main loop");
		// free all the engine ressource
		UnInit();
		if (DevConfig.disableNotifyDestroyed == false) {
			// tell the system that notifyDestroy doesn't need to be called
			s_application.notifyDestroyed();
		}

	}

	// --------------------------------------------------------------------------------------------------------------------
	// 按键接入专门的类cKeyboard
	// --------------------------------------------------------------------------------------------------------------------

	public void keyPressed(int keycode) {
		cKeyboard.keyPressed(keycode);
	}

	public void keyReleased(int keycode) {
		cKeyboard.keyReleased(keycode);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// / Standard rendering function. This function is called by the device to
	// enable us to draw on the display. Override of Canvas.paint.
	// /!note The game should not call this function, it will be called
	// automaticaly by the device, request are made in GLLib.run.
	// / The game drawing code has to go into the abstract function
	// GLLib.Game_update.
	// / \sa GLLib.Game_update
	// -----------------------------------------eee---------------------------------------------------------------------------

	public void paint(Graphics _g) {
		if (s_game_isInPaint)
			return;
		s_game_isInPaint = true;
		if (DevConfig.useSoftwareDoubleBuffer && m_imgBackBuffer != null) {
			Init_softDoublebuffer();
		}

		s_screenGraphics = _g;
		if (DevConfig.useFakeInterruptHandling) {
			// elapsed time since last Game_Paint() call
			long elapsedTime = System.currentTimeMillis()
					- s_Tick_FakeInt_Paint_LastFrame;
			// re-mark the time
			s_Tick_FakeInt_Paint_LastFrame = System.currentTimeMillis();

			if ((elapsedTime > DevConfig.FakeInterruptThreshold)
					&& (s_Tick_FakeInt_Paint_LastFrame != 0)) {
				Pause();
				Resume();
			}
		}
		if (s_game_isPaused)
			return;

		// update key state
		cKeyboard.UpdateKeypad();

		if (DevConfig.sprite_debugTogglePaintModule) {
			if (cKeyboard.WasKeyPressed(GLKey.k_num0)) {
				zSprite.s_debugSkipPaintModule = !zSprite.s_debugSkipPaintModule;
			}
		}
		// set current time
		s_Tick_Paint_FrameStart = System.currentTimeMillis();

		// if (DevConfig.useFrameDT)
		{
			// calculate this frame DT
			s_Tick_Paint_FrameDT = (int) (s_Tick_Paint_FrameStart - s_Tick_Paint_FrameStart_Last);
			// maximise game DT
			if (s_Tick_Paint_FrameDT < 0) {
				s_Tick_Paint_FrameDT = 0;
			}
			if (s_Tick_Paint_FrameDT > 1000) {
				s_Tick_Paint_FrameDT = 1000;
			}

			s_anim_frameDT = s_Tick_Paint_FrameDT;

			s_Tick_Paint_FrameStart_Last = s_Tick_Paint_FrameStart;

			// total execution time
			s_game_totalExecutionTime += s_Tick_Paint_FrameDT;

			// current fps
			s_game_FPSAverage = (100000 * s_game_currentFrameNB)
					/ (s_game_totalExecutionTime + 1);
		}

		// update current frame number
		s_game_currentFrameNB++;

		if (DevConfig.sprite_useBitmapFontCachePool > 0) {
			// 重置cache计数器
			BitmapFont.s_iCurFrameCachedCount = 0;
		}

		try {
			// if we use a software double buffer
			if (DevConfig.useSoftwareDoubleBuffer && m_bBackBufferEnabled) {
				g = s_lastPaintGraphics = m_gBackBuffer;
				OnPaint();
				if (DevConfig.useSoftwareDoubleBufferScaling) {
					zSprite.ScaleAndBlitBuffer(_g, m_imgBackBuffer);
				} else {
					_g.drawImage(m_imgBackBuffer, 0, 0, Graphics.TOP
							| Graphics.LEFT);

				}
			} else // 没有使用双缓冲
			{
				if (cPFX.pfx_usingScreenBuffer && cPFX.PFX_NeedScreenBufferThisFrame()) {
					g = s_lastPaintGraphics = cPFX.s_PFX_screenBufferG;
				} else {
					g = s_lastPaintGraphics = _g;
				}

				if (DevConfig.JY_TRANSLATE_X != 0
						|| DevConfig.JY_TRANSLATE_Y != 0) {
					SetColor(0);
					FillRect(0, 0, getWidth(), getHeight());
					Translate(DevConfig.JY_TRANSLATE_X,
							DevConfig.JY_TRANSLATE_Y);
				}
				OnPaint();
				if (cPFX.pfx_usingScreenBuffer && cPFX.s_PFX_initializd) {
					boolean skipPaint = false;

					if (DevConfig.pfx_useFullScreenEffectBlend) {
						if (cPFX.s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_SKIP_PAINT] != 0) {
							skipPaint = true;
						}

						cPFX.s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_SKIP_PAINT] = 0;
					}

					if (cPFX.PFX_NeedScreenBufferThisFrame()) {
						if (!skipPaint) {
							// blit the buffer now
							_g.drawImage(cPFX.s_PFX_screenBuffer, 0, 0, Graphics.TOP
									| Graphics.LEFT);
						}

						cPFX.s_PFX_enableScreenBufferThisFrame = 0;
					}
				}

			}

		} catch (Exception e) {
			Utils.Dbg("!!FATAL ERROR!! in Game_paint()." + e);
			;
			e.printStackTrace();

		}
		if ((DevConfig.lowMemoryLimit > 0)
				&& (Runtime.getRuntime().freeMemory() < DevConfig.lowMemoryLimit)) {
			System.gc();
		}
		s_game_isInPaint = false;
		s_screenGraphics = null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Gets the graphics context to the screen. Will be null once out of paint.
	//--------------------------------------------------------------------------------------------------------------------
	final public static javax.microedition.lcdui.Graphics GetScreenGraphics ()
	{
		return s_screenGraphics;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// If a double buffer exists and was disabled this will reable it.
	///
	///!note Only applies if zJYLibConfig.useSoftwareDoubleBuffer is TRUE.
	///!note This prevents the double buffer from being used. It does not release any memory.
	//--------------------------------------------------------------------------------------------------------------------
	final public static void EnableDoubleBuffer()
	{
		m_bBackBufferEnabled = true;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// If a double buffer exists it will disble it.
	///
	///!note Only applies if zJYLibConfig.useSoftwareDoubleBuffer is TRUE.
	///!note This prevents the double buffer from being used. It does not release any memory.
	//--------------------------------------------------------------------------------------------------------------------
	final public static void DisableDoubleBuffer()
	{
		m_bBackBufferEnabled = false;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Used to overwrite the sleep/yield behavior during non-suspended update.
	/// Usage is intended to allow shorter sleep times during loading for example.
	///
	///!param sleepTime - the duration in MS that we should sleep per frame.
	///                    -1 = disable custom sleep and use normal behavior.
	///                    0  = use yield
	///                    X  = sleep X MS
	///
	///!note This only overwrites the NONE-suspended update.
	///!see zJYLibConfig.allowCustomSleepTime
	//--------------------------------------------------------------------------------------------------------------------
	final public static void SetCustomSleepTime (int sleepTime)
	{
		if (DevConfig.allowCustomSleepTime)
		{
			m_customSleepTime = sleepTime;
		}
		else
		{
			Utils.Dbg("SetCustomSleepTime: Feature is not enabled! Please set allowCustomSleepTime to TRUE.");;
		}
	}



/// \defgroup GLLib_wrapper GLLib : MIDP wrapper
/// wrapper for some usual midp function, use this instead of the midp ones
/// \ingroup GLLibMain
///!{

/// Constant for centering text and images horizontally around the anchor point
final static int HCENTER 	= 1;

/// Constant for centering  text and images images vertically around the anchor point.
final static int VCENTER 	= 2;

/// Constant for positioning the anchor point of text and images to the left of the text or image.
final static int LEFT 		= 4;

/// Constant for positioning the anchor point of text and images to the right of the text or image.
final static int RIGHT 		= 8;

/// Constant for positioning the anchor point of text and images above the text or image.
public final static int TOP 		= 16;

/// Constant for positioning the anchor point of text and images below the text or image
/// Constant for positioning the anchor point of text and images below the text or image
final static int BOTTOM 	= 32;

/// Constant for the SOLID stroke style.
final static int SOLID 	= 0;

/// Constant for the DOTTED stroke style.
final static int DOTTED 	= 1;


/// No transform is applied
final static int TRANS_NONE = 0;

/// causes the specified image region to be rotated clockwise by 90 degrees.
final static int TRANS_ROT90 = 5;

/// causes the specified image region to be rotated clockwise by 180 degrees.
final static int TRANS_ROT180 = 3;

/// causes the specified image region to be rotated clockwise by 270 degrees.
final static int TRANS_ROT270 = 6;

/// causes the specified image region to be reflected about its vertical center.
final static int TRANS_MIRROR = 2;

/// causes the specified image region to be reflected about its vertical center and then rotated clockwise by 90 degrees.
final static int TRANS_MIRROR_ROT90 = 7;

/// causes the specified image region to be reflected about its vertical center and then rotated clockwise by 180 degrees.
final static int TRANS_MIRROR_ROT180 = 1;

/// causes the specified image region to be reflected about its vertical center and then rotated clockwise by 270 degrees.
final static int TRANS_MIRROR_ROT270 = 4;

//--------------------------------------------------------------------------------------------------------------------
/// set current graphics context -. all rendering operation will occurs on this context
///!param graphics - graphic context, all rendering operation will occurs on this context, or null to reset
///!note gaphic context is always reseted at beginning of each frame
//--------------------------------------------------------------------------------------------------------------------
final static void SetCurrentGraphics(Graphics graphics)
{
	if (graphics == null)
		g = s_lastPaintGraphics;
	else
		g = graphics;
}

//--------------------------------------------------------------------------------------------------------------------
/// set current graphics context -. all rendering operation will occurs on this context
///!param img - get the current graphic context from this image, or null to reset
///!note gaphic context is always reseted at beginning of each frame
//--------------------------------------------------------------------------------------------------------------------
final static void SetCurrentGraphics(javax.microedition.lcdui.Image img)
{
	if (img == null)
		g = s_lastPaintGraphics;
	else
	

		g = img.getGraphics();

}

//--------------------------------------------------------------------------------------------------------------------
/// get real time at the moment the fucntion is called
///!return real time
//--------------------------------------------------------------------------------------------------------------------
final static long GetRealTime()
{
	return System.currentTimeMillis();
}

//--------------------------------------------------------------------------------------------------------------------
/// get time for this frame (real time when this frame started)
///!return time
//--------------------------------------------------------------------------------------------------------------------
final static long GetFrameTime()
{
	return s_Tick_Paint_FrameStart;
}


//--------------------------------------------------------------------------------------------------------------------
/// get screen width, (if screen orientation changes, it will return the new screen width automatically
///!return screen width
//--------------------------------------------------------------------------------------------------------------------
final static int GetScreenWidth ()
{
	return s_screenWidth;
}

//--------------------------------------------------------------------------------------------------------------------
/// get screen height, (if screen orientation changes, it will return the new screen height automatically
///!return screen height
//--------------------------------------------------------------------------------------------------------------------
final static int GetScreenHeight ()
{
	return s_screenHeight;
}

//--------------------------------------------------------------------------------------------------------------------
/// call the garbage collector to free memory
///!note gc is not guaranteed to be called immediately in order to bufferise gc call
//--------------------------------------------------------------------------------------------------------------------
public final static void Gc ()
{
	if (DevConfig.useSystemGc)
	{
		System.gc();
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Translates the origin of the graphics context to the point (x, y) in the current coordinate system.
///
/// Translates the origin of the graphics context to the point (x, y) in the current coordinate system. All coordinates used in subsequent rendering operations on this graphics context will be relative to this new origin.<br>
/// <br>
/// The effect of calls to translate() are cumulative. For example, calling translate(1, 2) and then translate(3, 4) results in a translation of (4, 6).<br>
/// <br>
/// The application can set an absolute origin (ax, ay) using the following technique:<br>
/// g.translate(ax - g.getTranslateX(), ay - g.getTranslateY()) <br>
///!param x - the x coordinate of the new translation origin
///!param y - the y coordinate of the new translation origin
//--------------------------------------------------------------------------------------------------------------------
final static void Translate (int x, int y)
{
	g.translate(x, y);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the X coordinate of the translated origin of this graphics context.
///!return X of current origin
//--------------------------------------------------------------------------------------------------------------------
final static int GetTranslateX ()
{
	return g.getTranslateX();
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the Y coordinate of the translated origin of this graphics context.
///!return Y of current origin
//--------------------------------------------------------------------------------------------------------------------
final static int GetTranslateY ()
{
	return g.getTranslateY();
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the current color.
///!return an integer in form 0x00RRGGBB
//--------------------------------------------------------------------------------------------------------------------
final static int GetColor ()
{
	return g.getColor();
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the red component of the current color.
///!return integer value in range 0-255
//--------------------------------------------------------------------------------------------------------------------

final static int GetRedComponent ()
{
	return g.getRedComponent();
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the green component of the current color.
///!return integer value in range 0-255
//--------------------------------------------------------------------------------------------------------------------

final static int GetGreenComponent ()
{
	return g.getGreenComponent();
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the blue component of the current color.
///!return integer value in range 0-255
//--------------------------------------------------------------------------------------------------------------------

final static int GetBlueComponent ()
{
	return g.getBlueComponent();
}


//--------------------------------------------------------------------------------------------------------------------
/// Gets the current grayscale value of the color being used for rendering operations.
///
/// Gets the current grayscale value of the color being used for rendering operations. If the color was set by setGrayScale(), that value is simply returned. If the color was set by one of the methods that allows setting of the red, green, and blue components, the value returned is computed from the RGB color components (possibly in a device-specific fashion) that best approximates the brightness of that color.
///!return integer value in range 0-255
//--------------------------------------------------------------------------------------------------------------------

final static int GetGrayScale ()
{
	

	return g.getGrayScale();

}


//--------------------------------------------------------------------------------------------------------------------
/// Sets the current color to the specified RGB values.
///
/// All subsequent rendering operations will use this specified color. The RGB value passed in is interpreted with the least significant eight bits giving the blue component, the next eight more significant bits giving the green component, and the next eight more significant bits giving the red component. That is to say, the color component is specified in the form of 0x00RRGGBB. The high order byte of this value is ignored.
///!param RGB - the color being set
//--------------------------------------------------------------------------------------------------------------------
public final static void SetColor (int RGB)
{
	g.setColor(RGB);
}

//--------------------------------------------------------------------------------------------------------------------
/// Sets the current color to the specified RGB values.
///
/// All subsequent rendering operations will use this specified color. The RGB value passed in is interpreted with the least significant eight bits giving the blue component, the next eight more significant bits giving the green component, and the next eight more significant bits giving the red component. That is to say, the color component is specified in the form of 0x00RRGGBB. The high order byte of this value is ignored.
///!param R - value of red channel
///!param G - value of green channel
///!param B - value of blue channel
//--------------------------------------------------------------------------------------------------------------------
final static void SetColor (int R, int G, int B)
{
	g.setColor(0xFF000000 | (R<<16) | (G<<8) | (B));
}


//--------------------------------------------------------------------------------------------------------------------
/// Sets the current color to the specified RGB values.
///
/// All subsequent rendering operations will use this specified color.
///!param red - the red component of the color being set in range 0-255
///!param green - the green component of the color being set in range 0-255
///!param blue - the blue component of the color being set in range 0-255
//--------------------------------------------------------------------------------------------------------------------
final static void setColor (int red, int green, int blue)
{
	if(!(red <= 0xFF))Utils.DBG_PrintStackTrace(false, "setColor. red is bigger than 0xFF");;
	if(!(green <= 0xFF))Utils.DBG_PrintStackTrace(false, "setColor. green is bigger than 0xFF");;
	if(!(blue <= 0xFF))Utils.DBG_PrintStackTrace(false, "setColor. blue is bigger than 0xFF");;
	g.setColor((red<<16)|(green<<8)|blue);
}

//--------------------------------------------------------------------------------------------------------------------
/// Sets the current grayscale to be used for all subsequent rendering operations.
///
/// For monochrome displays, the behavior is clear. For color displays, this sets the color for all subsequent drawing operations to be a gray color equivalent to the value passed in. The value must be in the range 0-255.
///!param value - the desired grayscale value
//--------------------------------------------------------------------------------------------------------------------
final static void SetGrayScale (int value)
{
	if(!(value <= 0xFF))Utils.DBG_PrintStackTrace(false, "SetGrayScale. value is bigger than 0xFF");;
	
		try
		{
			
				g.setGrayScale(value);

		}
		catch (Exception e)
		{
		}

}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the current font.
///!return current font
//--------------------------------------------------------------------------------------------------------------------
final static javax.microedition.lcdui.Font GetFont ()
{
	return g.getFont();
}

//--------------------------------------------------------------------------------------------------------------------
/// Sets the stroke style used for drawing lines, arcs, rectangles, and rounded rectangles.
///
/// This does not affect fill, text, and image operations.
///!param style - can be SOLID or DOTTED
//--------------------------------------------------------------------------------------------------------------------


final static void SetStrokeStyle (int style)
{
	
	g.setStrokeStyle(style);

}


//--------------------------------------------------------------------------------------------------------------------
/// Gets the stroke style used for drawing operations.
///!return stroke style, SOLID or DOTTED
//--------------------------------------------------------------------------------------------------------------------

final static int GetStrokeStyle ()
{
	

	return g.getStrokeStyle();

}


//--------------------------------------------------------------------------------------------------------------------
/// Sets the font for all subsequent text rendering operations.
///
/// If font is null, it is equivalent to setFont(Font.getDefaultFont()).
///!param font - the specified font
//--------------------------------------------------------------------------------------------------------------------
final static void SetFont (javax.microedition.lcdui.Font font)
{
	g.setFont(font);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the X offset of the current clipping area, relative to the coordinate system origin of this graphics context.
///!return X offset of the current clipping area
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipX ()
{
	return GetClipX(g);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the Y offset of the current clipping area, relative to the coordinate system origin of this graphics context.
///!return Y offset of the current clipping area
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipY ()
{
	return GetClipY(g);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the width of the current clipping area.
///!return width of the current clipping area.
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipWidth ()
{
	return GetClipWidth(g);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the height of the current clipping area.
///!return height of the current clipping area.
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipHeight ()
{
	return GetClipHeight(g);
}

//--------------------------------------------------------------------------------------------------------------------
/// Intersects the current clip with the specified rectangle.
///!param x - the x coordinate of the rectangle to intersect the clip with
///!param y - the y coordinate of the rectangle to intersect the clip with
///!param width - the width of the rectangle to intersect the clip with
///!param height - the height of the rectangle to intersect the clip with
///!see setClip
//--------------------------------------------------------------------------------------------------------------------
final static void ClipRect (int x, int y, int width, int height)
{
	ClipRect(g, x, y, width, height);
}

//--------------------------------------------------------------------------------------------------------------------
/// Sets the current clip to the rectangle specified by the given coordinates.
///
/// Rendering operations have no effect outside of the clipping area.
///!param x - the x coordinate of the new clip rectangle
///!param y - the y coordinate of the new clip rectangle
///!param width - the width of the new clip rectangle
///!param height - the height of the new clip rectangle
//--------------------------------------------------------------------------------------------------------------------
public final static void SetClip (int x, int y, int width, int height)
{
	SetClip(g, x, y, width, height);
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the X offset of the current clipping area, relative to the coordinate system origin of this graphics context.
///!param g - The graphics context whose clip we are interested in.
///!return X offset of the current clipping area
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipX (Graphics g)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		return zSprite._customGraphicsClipX;
	}
	else
	{
		return g.getClipX();
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the Y offset of the current clipping area, relative to the coordinate system origin of this graphics context.
///!param g - The graphics context whose clip we are interested in.
///!return Y offset of the current clipping area
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipY (Graphics g)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		return zSprite._customGraphicsClipY;
	}
	else
	{
		return g.getClipY();
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the width of the current clipping area.
///!param g - The graphics context whose clip we are interested in.
///!return width of the current clipping area.
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipWidth (Graphics g)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		return zSprite._customGraphicsClipW;
	}
	else
	{
		return g.getClipWidth();
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the height of the current clipping area.
///!param g - The graphics context whose clip we are interested in.
///!return height of the current clipping area.
//--------------------------------------------------------------------------------------------------------------------
final static int GetClipHeight (Graphics g)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		return zSprite._customGraphicsClipH;
	}
	else
	{
		return g.getClipHeight();
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Intersects the current clip with the specified rectangle.
///!param g - The graphics context whose clip we are interested in.
///!param x - the x coordinate of the rectangle to intersect the clip with
///!param y - the y coordinate of the rectangle to intersect the clip with
///!param width - the width of the rectangle to intersect the clip with
///!param height - the height of the rectangle to intersect the clip with
///!see setClip
//--------------------------------------------------------------------------------------------------------------------
final static void ClipRect (Graphics g, int x, int y, int width, int height)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		// Convert W,H . Final Position
		zSprite._customGraphicsClipW += zSprite._customGraphicsClipX;
 		zSprite._customGraphicsClipH += zSprite._customGraphicsClipY;

 		width  += x;
 		height += y;

		// Clip
		zSprite._customGraphicsClipX = ((zSprite._customGraphicsClipX)>(x)?(zSprite._customGraphicsClipX):(x));
		zSprite._customGraphicsClipY = ((zSprite._customGraphicsClipY)>(y)?(zSprite._customGraphicsClipY):(y));
		zSprite._customGraphicsClipW = ((zSprite._customGraphicsClipW)<(width)?(zSprite._customGraphicsClipW):(width));
		zSprite._customGraphicsClipH = ((zSprite._customGraphicsClipH)<(height)?(zSprite._customGraphicsClipH):(height));

		// Convert Final Position . W,H
		zSprite._customGraphicsClipW -= zSprite._customGraphicsClipX;
		zSprite._customGraphicsClipH -= zSprite._customGraphicsClipY;
	}
	else
	{
		g.clipRect(x, y, width, height);
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Sets the current clip to the rectangle specified by the given coordinates.
///
/// Rendering operations have no effect outside of the clipping area.
///
///!param g - The graphics context whose clip we are interested in.
///!param x - the x coordinate of the new clip rectangle
///!param y - the y coordinate of the new clip rectangle
///!param width - the width of the new clip rectangle
///!param height - the height of the new clip rectangle
//--------------------------------------------------------------------------------------------------------------------
final static void SetClip (Graphics g, int x, int y, int width, int height)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		zSprite._customGraphicsClipX = x;
		zSprite._customGraphicsClipY = y;
		zSprite._customGraphicsClipW = width;
		zSprite._customGraphicsClipH = height;
	}
	else
	{
		g.setClip(x, y, width, height);

		if (DevConfig.useNokiaS60SetClipBugfix)
		{
			g.clipRect(x, y, width, height);
		}
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Returns true if there is valid clip data that can be accessed.
///
///!param g - The graphics context whose clip we are interested in.
///!return TRUE if there is valid clip data to access
//--------------------------------------------------------------------------------------------------------------------
final static boolean IsClipValid (Graphics g)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		return true;
	}
	else
	{
		return (g != null);
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws a line between the coordinates (x1,y1) and (x2,y2) using the current color and stroke style.
///!param x1 - the x coordinate of the start of the line
///!param y1 - the y coordinate of the start of the line
///!param x2 - the x coordinate of the end of the line
///!param y2 - the y coordinate of the end of the line
//--------------------------------------------------------------------------------------------------------------------
public final static void DrawLine (int x1, int y1, int x2, int y2)
{
	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		zSprite.DrawLineARGB(zSprite._customGraphics, zSprite._customGraphicsWidth, zSprite._customGraphicsHeight, zSprite._customGraphicsAlpha,
							 x1, y1, x2, y2, 0xFF000000 | g.getColor());
	}
	else
	{
		if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
		{
			x1 = zSprite.scaleX(x1);
			y1 = zSprite.scaleY(y1);
			x2 = zSprite.scaleX(x2);
			y2 = zSprite.scaleY(y2);
		}

		if(DevConfig.useDrawLineClippingBug)
		{
			if(y1 > y2)
			{
				int tmp;

				//swap coordinates
				{(tmp) = (x1);(x1) = (x2);(x2) = (tmp);};
				{(tmp) = (y1);(y1) = (y2);(y2) = (tmp);};
			}
		}

		g.drawLine(x1, y1, x2, y2);
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Fills the specified rectangle with the current color.
///
/// If either width or height is zero or less, nothing is drawn.
///!param x - the x coordinate of the rectangle to be filled
///!param y - the y coordinate of the rectangle to be filled
///!param width - the width of the rectangle to be filled
///!param height - the height of the rectangle to be filled
//--------------------------------------------------------------------------------------------------------------------
public final static void FillRect (int x, int y, int width, int height)
{
	FillRect(g, x, y, width, height);
}

//--------------------------------------------------------------------------------------------------------------------
/// Fills the specified rectangle with the current color.
///
/// If either width or height is zero or less, nothing is drawn.
///!param x - the x coordinate of the rectangle to be filled
///!param y - the y coordinate of the rectangle to be filled
///!param width - the width of the rectangle to be filled
///!param height - the height of the rectangle to be filled
//--------------------------------------------------------------------------------------------------------------------
final static void FillRect (Graphics g, int x, int y, int width, int height)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		zSprite.FillRectARGB(zSprite._customGraphics, zSprite._customGraphicsWidth, zSprite._customGraphicsHeight, zSprite._customGraphicsAlpha,
		                    x, y, width, height, 0xFF000000 | g.getColor());
	}
	else
	{

		if (DevConfig.useSafeFillRect)
		{
			Image fillRectImage = Image.createImage(width, height);
			Graphics fillRectGraphics = fillRectImage.getGraphics();
			fillRectGraphics.setColor(g.getColor());
			fillRectGraphics.fillRect(0, 0, width, height);
			g.drawImage(fillRectImage, x, y, 0);
		}
		else

		{
			g.fillRect(x, y, width, height);
		}
	}
}

//--------------------------------------------------------------------------------------------------------------------
///Draws the outline of the specified rectangle using the current color and stroke style.
///
/// The resulting rectangle will cover an area (width + 1)  pixels wide by (height + 1) pixels tall. If either width or height is less than zero, nothing is drawn.
///!param x - the x coordinate of the rectangle to be drawn
///!param y - the y coordinate of the rectangle to be drawn
///!param width - the width of the rectangle to be drawn
///!param height - the height of the rectangle to be drawn
//--------------------------------------------------------------------------------------------------------------------
public final static void DrawRect (int x, int y, int width, int height)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		zSprite.DrawRectARGB(zSprite._customGraphics, zSprite._customGraphicsWidth, zSprite._customGraphicsHeight, zSprite._customGraphicsAlpha,
							x, y, width, height, 0xFF000000 | g.getColor());
	}
	else
	{
		g.drawRect(x, y, width, height);
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the outline of the specified rounded corner rectangle using the current color and stroke style.
///
/// The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. If either width or height is less than zero, nothing is drawn.
///!param x - the x coordinate of the rectangle to be drawn
///!param y - the y coordinate of the rectangle to be drawn
///!param width - the width of the rectangle to be drawn
///!param height - the height of the rectangle to be drawn
///!param arcWidth - the horizontal diameter of the arc at the four corners
///!param arcHeight - the vertical diameter of the arc at the four corners
//--------------------------------------------------------------------------------------------------------------------
final static void DrawRoundRect (int x, int y, int width, int height, int arcWidth, int arcHeight)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
}

//--------------------------------------------------------------------------------------------------------------------
/// Fills the specified rounded corner rectangle with the current color.
///
/// If either width or height is zero or less, nothing is drawn.
///!param x - the x coordinate of the rectangle to be filled
///!param y - the y coordinate of the rectangle to be filled
///!param width - the width of the rectangle to be filled
///!param height - the height of the rectangle to be filled
///!param arcWidth - the horizontal diameter of the arc at the four corners
///!param arcHeight - the vertical diameter of the arc at the four corners
//--------------------------------------------------------------------------------------------------------------------
final static void FillRoundRect (int x, int y, int width, int height, int arcWidth, int arcHeight)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
}

//--------------------------------------------------------------------------------------------------------------------
/// Fills a circular or elliptical arc covering the specified rectangle.
///
///The resulting arc begins at startAngle and extends for arcAngle degrees. Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation. <br>
/// <br>
///The center of the arc is the center of the rectangle whose origin is (x, y) and whose size is specified by the width and height arguments. <br>
/// <br>
/// If either width or height is zero or less, nothing is drawn.
/// <br>
///The filled region consists of the "pie wedge" region bounded by the arc segment as if drawn by drawArc(), the radius extending from the center to this arc at startAngle degrees, and radius extending from the center to this arc at startAngle + arcAngle degrees. <br>
/// <br>
///The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
///!param x - the x coordinate of the upper-left corner of the arc to be filled.
///!param y - the y coordinate of the upper-left corner of the arc to be filled.
///!param width - the width of the arc to be filled
///!param height - the height of the arc to be filled
///!param startAngle - the beginning angle.
///!param arcAngle - the angular extent of the arc, relative to the start angle.
//--------------------------------------------------------------------------------------------------------------------
final static void FillArc (int x, int y, int width, int height, int startAngle, int arcAngle)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	g.fillArc(x, y, width, height, startAngle, arcAngle);
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the outline of a circular or elliptical arc covering the specified rectangle, using the current color and stroke style.
///
/// The resulting arc begins at startAngle and extends for arcAngle degrees, using the current color. Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation.<br>
/// <br>
/// The center of the arc is the center of the rectangle whose origin is (x, y) and whose size is specified by the width and height arguments.<br>
/// <br>
/// The resulting arc covers an area width + 1 pixels wide by height + 1 pixels tall. If either width or height is less than zero, nothing is drawn.<br>
/// <br>
///The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
///
///!param x - the x coordinate of the upper-left corner of the arc to be drawn
///!param y - the y coordinate of the upper-left corner of the arc to be drawn
///!param width - the width of the arc to be drawn
///!param height - the height of the arc to be drawn
///!param startAngle - the beginning angle
///!param arcAngle - the angular extent of the arc, relative to the start angle
//--------------------------------------------------------------------------------------------------------------------
final static void DrawArc (int x, int y, int width, int height, int startAngle, int arcAngle)
{
	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
		width  = zSprite.scaleX(width);
		height = zSprite.scaleY(height);
	}

	g.drawArc(x, y, width, height, startAngle, arcAngle);
}

//--------------------------------------------------------------------------------------------------------------------
/// transform text anchor point to midp standard anchor point
///!param anchor - the anchor to transform
///!return new anchor
//--------------------------------------------------------------------------------------------------------------------
final static private int transformAnchorForText(int anchor)
{
	if ((anchor & VCENTER) != 0)
	{
		anchor &= ~VCENTER;
		anchor |= g.BASELINE;
	}
	return anchor;
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified String using the current font and color.
///
/// The x,y position is the position of the anchor point
///!param str - the String to be drawn
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the text
//--------------------------------------------------------------------------------------------------------------------
public final static void DrawString (String str, int x, int y, int anchor)
{
	if(!(str != null))Utils.DBG_PrintStackTrace(false, "DrawString.str is null");;
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawString.anchor miss vertical positionning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawString.anchor miss horizontal positionning");;

	anchor = transformAnchorForText(anchor);

	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
	}

	try
	{
		g.drawString(str, x, y, anchor);
	}
	catch (Exception e)
	{
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified String using the current font and color.
///
/// The x,y position is the position of the anchor point. See anchor points.<br>
/// The offset and len parameters must specify a valid range of characters within the string str. The offset parameter must be within the range [0..(str.length())], inclusive. The len parameter must be a non-negative integer such that (offset + len) <= str.length().
///!param str - the String to be drawn
///!param offset - zero-based index of first character in the substring
///!param len - length of the substring
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the text
//--------------------------------------------------------------------------------------------------------------------
final static void DrawSubstring (String str, int offset, int len, int x, int y, int anchor)
{
	if(!(str != null))Utils.DBG_PrintStackTrace(false, "DrawSubstring.str is null");;
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawSubstring.anchor miss vertical positionning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawSubstring.anchor miss horizontal positionning");;
	if(!(offset < str.length()))Utils.DBG_PrintStackTrace(false, "DrawSubstring.offset is invalid");;
	if(!(offset >= 0))Utils.DBG_PrintStackTrace(false, "DrawSubstring.offset is negative");;
	if(!(offset+len <= str.length()))Utils.DBG_PrintStackTrace(false, "DrawSubstring.len is invalid");;
	if(!(len >= 0))Utils.DBG_PrintStackTrace(false, "DrawSubstring.len is negative");;

	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
	}

	anchor = transformAnchorForText(anchor);
	

	try
	{
		g.drawSubstring(str, offset, len, x, y, anchor);
	}
	catch (Exception e)
	{
	}

}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified character using the current font and color.
///!param character - the character to be drawn
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the text; see anchor points
//--------------------------------------------------------------------------------------------------------------------


final static void DrawChar (char character, int x, int y, int anchor)
{
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawChar.anchor miss vertical positionning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawChar.anchor miss horizontal positionning");;

	if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
	{
		x = zSprite.scaleX(x);
		y = zSprite.scaleY(y);
	}

	anchor = transformAnchorForText(anchor);
	try
	{
		g.drawChar(character, x, y, anchor);
	}
	catch (Exception e)
	{
	}
}


//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified characters using the current font and color.
///
/// The offset and length parameters must specify a valid range of characters within the character array data. The offset parameter must be within the range [0..(data.length)], inclusive. The length parameter must be a non-negative integer such that (offset + length) <= data.length.
///!param data - the array of characters to be drawn
///!param offset - the start offset in the data
///!param length - the number of characters to be drawn
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the text; see anchor points
//--------------------------------------------------------------------------------------------------------------------
final static void DrawChars (char[] data, int offset, int length, int x, int y, int anchor)
{
	if(!(data != null))Utils.DBG_PrintStackTrace(false, "DrawChars.data is null");;
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawChars.anchor miss vertical positionning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawChars.anchor miss horizontal positionning");;
	if(!(offset < data.length))Utils.DBG_PrintStackTrace(false, "DrawChars.offset is invalid");;
	if(!(offset >= 0))Utils.DBG_PrintStackTrace(false, "DrawChars.offset is negative");;
	if(!(offset+length <= data.length))Utils.DBG_PrintStackTrace(false, "DrawChars.len is invalid");;
	if(!(length >= 0))Utils.DBG_PrintStackTrace(false, "DrawChars.len is negative");;

	anchor = transformAnchorForText(anchor);
	try
	{
		g.drawChars(data, offset, length, x, y, anchor);
	}
	catch (Exception e)
	{
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified image by using the anchor point.
///
/// The image can be drawn in different positions relative to the anchor point by passing the appropriate position constants. See anchor points.<br>
/// <br>
/// If the source image contains transparent pixels, the corresponding pixels in the destination image must be left untouched. If the source image contains partially transparent pixels, a compositing operation must be performed with the destination pixels, leaving all pixels of the destination image fully opaque.<br>
/// <br>
/// If img is the same as the destination of this Graphics object, the result is undefined. For copying areas within an Image, copyArea should be used instead.
///!param img - the specified image to be drawn
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the image
//--------------------------------------------------------------------------------------------------------------------
final static void DrawImage (javax.microedition.lcdui.Image img, int x, int y, int anchor)
{
	DrawImage(g, img, x, y, anchor);
}

//--------------------------------------------------------------------------------------------------------------------
/// Draws the specified image by using the anchor point.
///
/// The image can be drawn in different positions relative to the anchor point by passing the appropriate position constants. See anchor points.<br>
/// <br>
/// If the source image contains transparent pixels, the corresponding pixels in the destination image must be left untouched. If the source image contains partially transparent pixels, a compositing operation must be performed with the destination pixels, leaving all pixels of the destination image fully opaque.<br>
/// <br>
/// If img is the same as the destination of this Graphics object, the result is undefined. For copying areas within an Image, copyArea should be used instead.
///!param g - the graphics context to render this image to
///!param img - the specified image to be drawn
///!param x - the x coordinate of the anchor point
///!param y - the y coordinate of the anchor point
///!param anchor - the anchor point for positioning the image
//--------------------------------------------------------------------------------------------------------------------
final static void DrawImage (Graphics g, javax.microedition.lcdui.Image img, int x, int y, int anchor)
{
	if(!(img != null))Utils.DBG_PrintStackTrace(false, "DrawImage.data is null");;
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawImage.anchor miss vertical positioning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawImage.anchor miss horizontal positioning");;

	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		int w = img.getWidth();
		int h = img.getHeight();

		if      ((anchor & RIGHT)   == RIGHT)  { x -= w; 	    }
		else if ((anchor & HCENTER) == HCENTER){ x -= (w >> 1); }

		if      ((anchor & BOTTOM)  == BOTTOM) { y -= h; 	    }
		else if ((anchor & VCENTER) == VCENTER){ y -= (h >> 1);	}

		int[] buffer = zSprite.GetPixelBuffer_int(null);
		int hStep = buffer.length / w;
		int posY = 0;

		while (hStep != 0)
		{
			hStep = ((hStep)<(h - posY)?(hStep):(h - posY));
			GetRGB(img, buffer, 0, w, 0, posY,     w, hStep);
			DrawRGB(g,  buffer, 0, w, x, y + posY, w, hStep, false);
			posY += hStep;
		}
	}
	else
	{
		try
		{
			g.drawImage(img, x, y, anchor);
		}
		catch (Exception e)
		{
		}
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Copies a region of the specified source image to a location within the destination, possibly transforming (rotating and reflecting) the image data using the chosen transform function.
///
/// The destination, if it is an image, must not be the same image as the source image. If it is, an exception is thrown. This restriction is present in order to avoid ill-defined behaviors that might occur if overlapped, transformed copies were permitted.<br>
/// <br>
/// The transform function used must be one of the following, as defined in the Sprite class:<br>
/// Sprite.TRANS_NONE - causes the specified image region to be copied unchanged<br>
/// Sprite.TRANS_ROT90 - causes the specified image region to be rotated clockwise by 90 degrees.<br>
/// Sprite.TRANS_ROT180 - causes the specified image region to be rotated clockwise by 180 degrees.<br>
/// Sprite.TRANS_ROT270 - causes the specified image region to be rotated clockwise by 270 degrees.<br>
/// Sprite.TRANS_MIRROR - causes the specified image region to be reflected about its vertical center.<br>
/// Sprite.TRANS_MIRROR_ROT90 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 90 degrees.<br>
/// Sprite.TRANS_MIRROR_ROT180 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 180 degrees.<br>
/// Sprite.TRANS_MIRROR_ROT270 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 270 degrees.<br>
/// <br>
/// If the source region contains transparent pixels, the corresponding pixels in the destination region must be left untouched. If the source region contains partially transparent pixels, a compositing operation must be performed with the destination pixels, leaving all pixels of the destination region fully opaque.<br>
/// <br>
/// The (x_src, y_src) coordinates are relative to the upper left corner of the source image. The x_src, y_src, width, and height parameters specify a rectangular region of the source image. It is illegal for this region to extend beyond the bounds of the source image. This requires that:<br>
///    x_src >= 0<br>
///    y_src >= 0<br>
///    x_src + width <= source width<br>
///    y_src + height <= source height    <br>
/// <br>
/// The (x_dest, y_dest) coordinates are relative to the coordinate system of this Graphics object. It is legal for the destination area to extend beyond the bounds of the Graphics object. Pixels outside of the bounds of the Graphics object will not be drawn.<br>
/// <br>
/// The transform is applied to the image data from the region of the source image, and the result is rendered with its anchor point positioned at location (x_dest, y_dest) in the destination.
///!param src - the source image to copy from
///!param x_src - the x coordinate of the upper left corner of the region within the source image to copy
///!param y_src - the y coordinate of the upper left corner of the region within the source image to copy
///!param width - the width of the region to copy
///!param height - the height of the region to copy
///!param transform - the desired transformation for the selected region being copied
///!param x_dest - the x coordinate of the anchor point in the destination drawing area
///!param y_dest - the y coordinate of the anchor point in the destination drawing area
///!param anchor - the anchor point for positioning the region within the destination image
//--------------------------------------------------------------------------------------------------------------------
final static void DrawRegion (javax.microedition.lcdui.Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor)
{
	if(!(src != null))Utils.DBG_PrintStackTrace(false, "DrawRegion.src is null");;
	if(!((anchor & (VCENTER|TOP|BOTTOM))!=0))Utils.DBG_PrintStackTrace(false, "DrawRegion.anchor miss vertical positionning");;
	if(!((anchor & (HCENTER|LEFT|RIGHT))!=0))Utils.DBG_PrintStackTrace(false, "DrawRegion.anchor miss horizontal positionning");;
	if(!(transform >=0))Utils.DBG_PrintStackTrace(false, "DrawRegion.transform is invalid");;
	if(!(transform <=7))Utils.DBG_PrintStackTrace(false, "DrawRegion.transform is invalid");;

	if(!(x_src >= 0))Utils.DBG_PrintStackTrace(false, "DrawRegion.x_src is negative");;
	if(!(y_src >= 0))Utils.DBG_PrintStackTrace(false, "DrawRegion.y_src is negative");;

	
		if(!(x_src+width <= src.getWidth()))Utils.DBG_PrintStackTrace(false, "DrawRegion.x_src+width is bigger than source image");;
		if(!(y_src+height <= src.getHeight()))Utils.DBG_PrintStackTrace(false, "DrawRegion.x_src+height is bigger than source image");;

		if(DevConfig.useSafeDrawRegion)
		{
	        if(x_src < 0)
	        {
	                width += x_src;
	                x_src = 0;
	        }
	        //else
	        if(x_src+width >= src.getWidth())
	        {
	        	width += src.getWidth()-(x_src+width);
	        }

	        if(y_src < 0)
	        {
	                height += y_src;
	                y_src = 0;
	        }
	        //else
	        if(y_src+height >= src.getHeight())
	        {
	                height += src.getHeight()-(y_src+height);
	        }
	        if(height <= 0 || width <= 0)
	        {
	        	return;
	        }
		}

		try
		{
			
				if (DevConfig.sprite_drawRegionFlippedBug)
				{
					

							Image image = Image.createImage(src, x_src, y_src, width, height, transform);

						g.drawImage(image, x_dest, y_dest, anchor);

				}
				else

				{
					

						g.drawRegion(src, x_src, y_src, width, height, transform, x_dest, y_dest, anchor);

				}
		}
		

		catch (Exception e)
		{
		}
	

}

//--------------------------------------------------------------------------------------------------------------------
/// Copies the contents of a rectangular area (x_src, y_src, width, height) to a destination area, whose anchor point identified by anchor is located at (x_dest, y_dest).
///
/// The effect must be that the destination area contains an exact copy of the contents of the source area immediately prior to the invocation of this method. This result must occur even if the source and destination areas overlap.<br>
/// <br>
/// The points (x_src, y_src) and (x_dest, y_dest) are both specified relative to the coordinate system of the Graphics object. It is illegal for the source region to extend beyond the bounds of the graphic object. This requires that:<br>
///    x_src + tx >= 0<br>
///    y_src + ty >= 0<br>
///    x_src + tx + width <= width of Graphics object's destination<br>
///    y_src + ty + height <= height of Graphics object's destination<br>
/// <br>
/// where tx and ty represent the X and Y coordinates of the translated origin of this graphics object, as returned by getTranslateX() and getTranslateY(), respectively.<br>
/// <br>
/// However, it is legal for the destination area to extend beyond the bounds of the Graphics object. Pixels outside of the bounds of the Graphics object will not be drawn.<br>
/// <br>
/// The copyArea method is allowed on all Graphics objects except those whose destination is the actual display device. This restriction is necessary because allowing a copyArea method on the display would adversely impact certain techniques for implementing double-buffering.<br>
/// <br>
/// Like other graphics operations, the copyArea method uses the Source Over Destination rule for combining pixels. However, since it is defined only for mutable images, which can contain only fully opaque pixels, this is effectively the same as pixel replacement.<br>
///!param x_src - the x coordinate of upper left corner of source area
///!param y_src - the y coordinate of upper left corner of source area
///!param width - the width of the source area
///!param height - the height of the source area
///!param x_dest - the x coordinate of the destination anchor point
///!param y_dest - the y coordinate of the destination anchor point
///!param anchor - the anchor point for positioning the region within the destination image
//--------------------------------------------------------------------------------------------------------------------
final static void CopyArea (int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor)
{
	
		try
		{
			

				g.copyArea(x_src, y_src, width, height, x_dest, y_dest, anchor);

		}
		

		catch (Exception e)
		{
		}
	

}

//--------------------------------------------------------------------------------------------------------------------
/// Fills the specified triangle will the current color.
///
/// The lines connecting each pair of points are included in the filled triangle.
///!param x1 - the x coordinate of the first vertex of the triangle
///!param y1 - the y coordinate of the first vertex of the triangle
///!param x2 - the x coordinate of the second vertex of the triangle
///!param y2 - the y coordinate of the second vertex of the triangle
///!param x3 - the x coordinate of the third vertex of the triangle
///!param y3 - the y coordinate of the third vertex of the triangle
//--------------------------------------------------------------------------------------------------------------------
final static void FillTriangle (int x1, int y1, int x2, int y2, int x3, int y3)
{
	

		g.fillTriangle(x1, y1, x2, y2, x3, y3);
	

}


//--------------------------------------------------------------------------------------------------------------------
/// Wrapper for createRGBImage functionalty.
///
///!param data - Is the rgb pixel data
///!param w - Is the image width.
///!param h - Is the image height.
///!param alpha - If the image contains alpha.
//--------------------------------------------------------------------------------------------------------------------
final static Image CreateRGBImage(int[] data, int w, int h, boolean alpha)
{
	Image img = null;


	if (DevConfig.sprite_useCreateRGBTransparencyBug)
	{
		img = Image.createRGBImage(data, w, h, true);
	}
	else
	{
		img = Image.createRGBImage(data, w, h, alpha);
	}


	return img;
}

//--------------------------------------------------------------------------------------------------------------------
/// Renders a series of device-independent RGB+transparency values in a specified region.
///
///The values are stored in rgbData in a format with 24 bits of RGB and an eight-bit alpha value (0xAARRGGBB), with the first value stored at the specified offset. The scanlength  specifies the relative offset within the array between the corresponding pixels of consecutive rows. Any value for scanlength is acceptable (even negative values) provided that all resulting references are within the bounds of the rgbData array. The ARGB data is rasterized horizontally from left to right within each row. The ARGB values are rendered in the region specified by x, y, width and height, and the operation is subject to the current clip region and translation for this Graphics object.<br>
/// <br>
///Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, where rows and columns are numbered downward from the top starting at zero, and columns are numbered rightward from the left starting at zero. This operation can then be defined as:<br>
///    P(a, b) = rgbData[offset + (a - x) + (b - y) * scanlength]       <br>
///for<br>
///     x <= a < x + width<br>
///     y <= b < y + height    <br>
/// <br>
///This capability is provided in the Graphics class so that it can be used to render both to the screen and to offscreen Image objects. The ability to retrieve ARGB values is provided by the Image.getRGB(int[], int, int, int, int, int, int) method.<br>
/// <br>
///If processAlpha is true, the high-order byte of the ARGB format specifies opacity; that is, 0x00RRGGBB specifies a fully transparent pixel and 0xFFRRGGBB specifies a fully opaque pixel. Intermediate alpha values specify semitransparency. If the implementation does not support alpha blending for image rendering operations, it must remove any semitransparency from the source data prior to performing any rendering. (See Alpha Processing for further discussion.) If processAlpha is false, the alpha values are ignored and all pixels must be treated as completely opaque.<br>
/// <br>
///The mapping from ARGB values to the device-dependent pixels is platform-specific and may require significant computation.
///!param g - The graphics context to render to
///!param rgbData - an array of ARGB values in the format 0xAARRGGBB
///!param offset - the array index of the first ARGB value
///!param scanlength - the relative array offset between the corresponding pixels in consecutive rows in the rgbData array
///!param x - the horizontal location of the region to be rendered
///!param y - the vertical location of the region to be rendered
///!param width - the width of the region to be rendered
///!param height - the height of the region to be rendered
///!param processAlpha - true if rgbData has an alpha channel, false if all pixels are fully opaque
//--------------------------------------------------------------------------------------------------------------------
final static void DrawRGB (Graphics g, int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha)
{
	DrawRGB(g, rgbData, offset, scanlength, x, y, width, height, processAlpha, true, 0);
}

//--------------------------------------------------------------------------------------------------------------------
/// Renders a series of device-independent RGB+transparency values in a specified region.
///
///The values are stored in rgbData in a format with 24 bits of RGB and an eight-bit alpha value (0xAARRGGBB), with the first value stored at the specified offset. The scanlength  specifies the relative offset within the array between the corresponding pixels of consecutive rows. Any value for scanlength is acceptable (even negative values) provided that all resulting references are within the bounds of the rgbData array. The ARGB data is rasterized horizontally from left to right within each row. The ARGB values are rendered in the region specified by x, y, width and height, and the operation is subject to the current clip region and translation for this Graphics object.<br>
/// <br>
///Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, where rows and columns are numbered downward from the top starting at zero, and columns are numbered rightward from the left starting at zero. This operation can then be defined as:<br>
///    P(a, b) = rgbData[offset + (a - x) + (b - y) * scanlength]       <br>
///for<br>
///     x <= a < x + width<br>
///     y <= b < y + height    <br>
/// <br>
///This capability is provided in the Graphics class so that it can be used to render both to the screen and to offscreen Image objects. The ability to retrieve ARGB values is provided by the Image.getRGB(int[], int, int, int, int, int, int) method.<br>
/// <br>
///If processAlpha is true, the high-order byte of the ARGB format specifies opacity; that is, 0x00RRGGBB specifies a fully transparent pixel and 0xFFRRGGBB specifies a fully opaque pixel. Intermediate alpha values specify semitransparency. If the implementation does not support alpha blending for image rendering operations, it must remove any semitransparency from the source data prior to performing any rendering. (See Alpha Processing for further discussion.) If processAlpha is false, the alpha values are ignored and all pixels must be treated as completely opaque.<br>
/// <br>
///The mapping from ARGB values to the device-dependent pixels is platform-specific and may require significant computation.
///!param g - The graphics context to render to
///!param rgbData - an array of ARGB values in the format 0xAARRGGBB
///!param offset - the array index of the first ARGB value
///!param scanlength - the relative array offset between the corresponding pixels in consecutive rows in the rgbData array
///!param x - the horizontal location of the region to be rendered
///!param y - the vertical location of the region to be rendered
///!param width - the width of the region to be rendered
///!param height - the height of the region to be rendered
///!param processAlpha - true if rgbData has an alpha channel, false if all pixels are fully opaque
///!param processComplexAlpha - true is rgbData has an alpha channel with values not equal to 0 or FF.
///!param flags - the transformation flags to use when drawing
//--------------------------------------------------------------------------------------------------------------------
final static void DrawRGB (Graphics g, int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha, boolean processComplexAlpha, int flags)
{
	if(!(rgbData != null))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter: rgbData is null");;
	if(!(offset >= 0))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter: offset < 0");;
	if(!(width >= 0))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter: width < 0");;
	if(!(height >= 0))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter: height < 0");;
	if(!(scanlength > 0))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter: scanlength <= 0");;
	if(!(offset + width + (height - 1) * scanlength <= rgbData.length))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter(s) out of array bounds");;
	if(!((flags == 0) || (scanlength == width)))Utils.DBG_PrintStackTrace(false, "DrawRGB. invalid parameter mix: flags are not 0 and scanlength is not the width!");;

	if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
	{
		if(!(offset == 0))Utils.DBG_PrintStackTrace(false, "DrawRGB (graphics is pixel array): offset must be 0! Easy to add this if needed though...");;
		if(!(scanlength == width))Utils.DBG_PrintStackTrace(false, "DrawRGB (graphics is pixel array): scanlength must be equal to the width!");;

		zSprite.BlitARGB(zSprite._customGraphics, zSprite._customGraphicsWidth, zSprite._customGraphicsHeight, zSprite._customGraphicsAlpha,
		                 x, y, rgbData, width, height, processAlpha ? (processComplexAlpha ? zSprite.ALPHA_FULL : zSprite.ALPHA_TRANSPARENT) : zSprite.ALPHA_NONE, flags);
	}
	else
	{
  		if (flags != 0)
  		{
			if ((flags & zSprite.FLAG_ROT_90) != 0)
			{
				{(scanlength) = (width);(width) = (height);(height) = (scanlength);};
				scanlength = width;
			}

  			rgbData = zSprite.TransformRGB(rgbData, width, height, flags);
		}


		if (DevConfig.sprite_drawRGBTransparencyBug)
		{
			Image tmp_img = GLLib.CreateRGBImage(rgbData, width, height, processAlpha);
			g.drawImage(tmp_img, x, y, 0);
		}
		else

		// Both GLLib Config vars control this... keeping for backwards-compatibility...
		if (DevConfig.useDrawPartialRGB || DevConfig.sprite_useTruncatedRGBBuffer)
		{
			drawPartialRGB(g,rgbData, scanlength, offset%scanlength, offset/scanlength, x, y, width, height, processAlpha);
		}
		else
		{
			

				if (DevConfig.useDrawRGBTranslationFix)
				{
					int fixX = g.getTranslateX();
					int fixY = g.getTranslateY();
					g.translate(-fixX, -fixY);
					g.drawRGB(rgbData, offset, scanlength, x + fixX, y + fixY, width, height, processAlpha);
					g.translate(fixX, fixY);
				}
				else
				{
					g.drawRGB(rgbData, offset, scanlength, x, y, width, height, processAlpha);
				}

		}


	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Same as drawRGB but it draws safely (without drawing outside the screen)
///!param g - graphic context, all rendering operation will occurs on this context.
///!param rgbData - an array of ARGB values in the format 0xAARRGGBB
///!param scanlength - the relative array offset between the corresponding pixels in consecutive rows in the rgbData array
///!param srcX - the horizontal location of the source to be rendered
///!param srcY - the vertical location of the source to be rendered
///!param x - the horizontal location of the destination
///!param y - the vertical location of the destination
///!param width - the width of the region to be rendered
///!param height - the height of the region to be rendered
///!param processAlpha - true if rgbData has an alpha channel, false if all pixels are fully opaque
//--------------------------------------------------------------------------------------------------------------------
public static final void drawPartialRGB(Graphics g, int rgbData[], int scanlength, int srcX, int srcY, int x, int y, int width, int height,  boolean processAlpha)
{
	drawPartialRGB(g, GetScreenWidth(), GetScreenHeight(), rgbData, scanlength, srcX, srcY, x, y, width, height, processAlpha);
}

//--------------------------------------------------------------------------------------------------------------------
/// Same as drawPartialRGB but it's useful when you draw in a back buffer that is bigger than the screen.
///!param g - graphic context, all rendering operation will occurs on this context.
///!param screenWidth - the width of the backbuffer
///!param screenHeight - the height of the backbuffer
///!param rgbData - an array of ARGB values in the format 0xAARRGGBB
///!param scanlength - the relative array offset between the corresponding pixels in consecutive rows in the rgbData array
///!param srcX - the horizontal location of the source to be rendered
///!param srcY - the vertical location of the source to be rendered
///!param x - the horizontal location of the destination
///!param y - the vertical location of the destination
///!param width - the width of the region to be rendered
///!param height - the height of the region to be rendered
///!param processAlpha - true if rgbData has an alpha channel, false if all pixels are fully opaque
//--------------------------------------------------------------------------------------------------------------------
public static final void drawPartialRGB(Graphics g, int screenWidth, int screenHeight, int rgbData[], int scanlength, int srcX, int srcY, int x, int y, int width, int height,  boolean processAlpha)
{

	int fixX = 0;
	int fixY = 0;

	// Apply BEFORE bounds check since it will obviously effect where this is drawn...
	if (DevConfig.useDrawRGBTranslationFix)
	{
		fixX = g.getTranslateX();
		fixY = g.getTranslateY();
		g.translate(-fixX, -fixY);
		x += fixX;
		y += fixY;
	}


	int offset = srcX + (srcY * scanlength); //set the initial offset needed for srcX and srcY

	if (g == s_lastPaintGraphics || DevConfig.useDrawRGBClipNotOnScreenBugFix) //!Tin Le Chanh: why g == s_lastPaintGraphics?( Crash on Nokia 6280 if draw on other graphics).
	{
		if( x >= screenWidth || x + width <= 0 || y >= screenHeight || y + height <= 0)
		{ //image is out of screen bounds.
				return;
		}

		// Tin Le Chanh: Fixed bug in Nokia 6280.
		// If the clip region is not in screen.  Draw RGB will cause game crash. So we must sure the rect is on screen.
		// Flag :  useDrawRGBClipNotOnScreenBugFix.
		if (DevConfig.useDrawRGBClipNotOnScreenBugFix)
		{
			if (GLLib.g == null) {//JYWrapper可能会用的时候GLLib.g并没被初始化
				GLLib.SetCurrentGraphics(g);
			}
			int cx, cy, cw, ch;
			cx = GLLib.GetClipX();
			cy = GLLib.GetClipY();
			cw = GLLib.GetClipWidth();
			ch = GLLib.GetClipHeight();

			if( cx >= screenWidth || cx + cw <= 0 || cy >= screenHeight || cy + ch <= 0)
			{
				return;
			}
		}


		if( x <= 0 )
		{ //the rgbImage is leaving the screen from the left
				offset -= x;
				width += x;
				x = 0;
		}
		if( x + width >= screenWidth )
		{ //the rgbImage is leaving the screen from the right
				width = screenWidth - x;
		}
		if( y + height >=  screenHeight )
		{ //the rgbImage is leaving the screen from the bottom
				height = screenHeight - y;
		}
		if( y <= 0 )
		{   //the rgbImage is leaving the screen from the top
				offset -= (y * scanlength);
				height += y;
				y = 0;
		}
	}

	//!Tin Le Chanh: On some nokia phone. DrawRGB with offset > 0 cause game crash.
	if (DevConfig.useDrawRGBOffsetFix)
	{
		if (offset>0)
		{
			int len = rgbData.length;
			int off = 0;
			for (int i = offset ; i < len ; i++)
				rgbData[off++] = rgbData[i];
			offset = 0;
		}
	}


		g.drawRGB(rgbData, offset, scanlength, x, y, width, height, processAlpha);


	// Apply BEFORE bounds check since it will obviously effect where this is drawn...
	if (DevConfig.useDrawRGBTranslationFix)
	{
		g.translate(fixX, fixY);
	}


}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the color that will be displayed if the specified color is requested.
///
/// This method enables the developer to check the manner in which RGB values are mapped to the set of distinct colors that the device can actually display. For example, with a monochrome device, this method will return either 0xFFFFFF (white) or 0x000000 (black) depending on the brightness of the specified color.
///!param color - the desired color (in 0x00RRGGBB  format, the high-order byte is ignored)
///!return the corresponding color that will be displayed on the device's screen (in 0x00RRGGBB format)
///
///!note You can override the value returned for FF00FF.
///!see zJYLibConfig.useGetDisplayColorMagentaFix
//--------------------------------------------------------------------------------------------------------------------
final static int GetDisplayColor (int color)
{
	if (DevConfig.useGetDisplayColorMagentaFix != -1)
	{
		if ((color & 0x00FFFFFF) == 0x00FF00FF)
		{
			return DevConfig.useGetDisplayColorMagentaFix;
		}
	}

	
		return g.getDisplayColor(color);
	

}

//--------------------------------------------------------------------------------------------------------------------
/// Performs platformRequest
///
/// This method wraps calls to platformRequest, incorporating some workarounds for usual platformRequest problems
///!param url the url to send to the browser
///!return if the platformRequest was successful
//--------------------------------------------------------------------------------------------------------------------
final static boolean PlatformRequest (String url)
{
	
	try
	{
		

			return s_application.platformRequest(url);

	}
	catch(Exception e)
	{
		Utils.Dbg("PlatformRequest.Failed with url "+ url+" with exception "+e.getMessage());
		return false;
	}
	

}


/// buffer to prevent too many consecutive Vibrate call
private static long m_nextTimeVibrationAllowed;
//--------------------------------------------------------------------------------------------------------------------
/// Make the phone vibrate or flash the back light is the phone has this functionnality
///!param duration - duration of the vibration (or backlight flash)
//--------------------------------------------------------------------------------------------------------------------
public static void Vibrate (int duration)
{
	if(!(duration >= 0))Utils.DBG_PrintStackTrace(false, "Vibrate.duration is negative");;

	
		try
		{
			if (m_nextTimeVibrationAllowed < s_Tick_Paint_FrameStart)
			{
				

					if (DevConfig.useFlashLightInsteadOfVibration)
					{
						s_display.flashBacklight(duration);
					}
					else
					{
						s_display.vibrate(duration);
					}


				//avoid hanging up some phones with vibration
				m_nextTimeVibrationAllowed = s_Tick_Paint_FrameStart + 200;//(duration << 1);
			}
		}
		catch (Exception e)
		{
		}

}


//--------------------------------------------------------------------------------------------------------------------
/// Wrapper for CreateImage (which is created from PNG data)
///
///!param data - the array of image data in a supported image format
///!param offset - the offset of the start of the data in the array
///!param length - the length of the data in the array
///
///!see zJYLibConfig.sprite_useA870CreateRGBTransparencyFix
//--------------------------------------------------------------------------------------------------------------------

final public static javax.microedition.lcdui.Image CreateImage (byte[] data, int offset, int length)
{

	if (DevConfig.sprite_useA870CreateRGBTransparencyFix)
	{
		return FixImageLackingTransparencyBug(Image.createImage(data, offset, length));
	}
	else


	{
		return Image.createImage(data, offset, length);
	}

}


//--------------------------------------------------------------------------------------------------------------------
/// Given an image, it will attempt to correct some transparency issues.
///
///!param img - Is the image we want to fix.
///!return The fixed image.
///
///!note This function is slow and should not be used except during loading.
//--------------------------------------------------------------------------------------------------------------------

final public static javax.microedition.lcdui.Image FixImageLackingTransparencyBug (javax.microedition.lcdui.Image img)
{
	int w = img.getWidth();
	int h = img.getHeight();
	int size = w*h;

	Image imgTemp = Image.createImage(w, h);
	Graphics graTemp = imgTemp.getGraphics();

	graTemp.setColor(0xFF00FF);
	graTemp.fillRect(0, 0, w, h);
	graTemp.drawImage(img, 0, 0, 0);

	// Attempt to re-use ASprite int buffer
	int[] rgbData = zSprite.temp_int;

	if (rgbData == null || rgbData.length < size)
	{
		rgbData = new int[w * h];
	}

	GLLib.GetRGB(imgTemp, rgbData, 0, w, 0, 0, w, h);

	for (int i=size-1; i>=0; i--)
	{
		if ((rgbData[i] & 0xFFFFFF) == 0xFF00FF)
		{
			rgbData[i] = 0x00FF00FF;
		}
	}

	return Image.createRGBImage(rgbData, w, h, true);
}


//--------------------------------------------------------------------------------------------------------------------
/// Wrapper for CreateImage(w,h)
///
///!param w - The width of the image to create.
///!param h - The height of the image to create.
///!return The Image
//--------------------------------------------------------------------------------------------------------------------
final public static javax.microedition.lcdui.Image CreateImage (int w, int h)
{
	return Image.createImage(w, h);
}

//--------------------------------------------------------------------------------------------------------------------
/// Wrapper for image.getGraphics()
///
///!param img - Is the image whose graphics context we want to get.
///!return The graphics context for this image
///
///!note This function is slow and should not be used except during loading.
//--------------------------------------------------------------------------------------------------------------------
final public static Graphics GetGraphics (javax.microedition.lcdui.Image img)
{

	return img.getGraphics();


}

//--------------------------------------------------------------------------------------------------------------------
/// Wrapper for image.getRGB in MIDP2 API
///
///!param img - The image whose argb values we want to get.
///!param data - an array of integers in which the ARGB pixel data is stored
///!param offset - the index into the array where the first ARGB value is stored
///!param scanlength - the relative offset in the array between corresponding pixels in consecutive rows of the region
///!param x - the x-coordinate of the upper left corner of the region
///!param y - the y-coordinate of the upper left corner of the region
///!param w - the width of the region
///!param h - the height of the region
///
//--------------------------------------------------------------------------------------------------------------------
final public static void GetRGB(Image img, int[] data, int offset, int scanlength, int x, int y, int w, int h)
{

	img.getRGB(data, offset, scanlength, x, y, w, h);


	if (DevConfig.sprite_getRGBTransparencyBug)
	{
		int index = offset + (w * h);

		while (index-- > offset)
		{
			if((data[index] & 0xFFFFFF) == 0xFF00FF)
			{
				data[index] = 0x00FF00FF;
			}
		}
	}
}

///!}




///!}






///!}


	/// \defgroup GLLib_rms GLLib : Recordstore management
	/// handle RMS (save data) access (read write)
	///!ingroup GLLibMain
	///!{


	

	///!}


/// \defgroup GLLib_profiler GLLib : GLLib Profiler
/// Utility functions for profiling in emulator and phone
/// \ingroup GLLibMain
///!{

/// Maximum number of events
static final int PROFILER_MAX_EVENTS = 200;

/// Name of events
private static String[] s_profiler_eventNames;

/// Beginning time stamp of events
private static long[]	s_profiler_eventBegins;

/// Ending time stamp of events
private static long[]	s_profiler_eventEnds;

/// Depths for events
private static short[]	s_profiler_eventDepths;

/// Current stack for events
private static short[]	s_profiler_eventStack;

/// Current counter for events
private static int		s_profiler_eventCount;

/// Current stack for events
private static int		s_profiler_eventStackIndex;

/// Recording flag
private static boolean	s_profiler_recording;

/// Emulator supports begin/end event (to prevent flooding emulator log window if not supported)
private static boolean	s_profiler_emulator;

// Initialize if emulator supports begin/end event
static
{
	if (true)
	{
		s_profiler_emulator = System.getProperty("EMU://EndNamedEvent") != null;
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Start capturing events.
//--------------------------------------------------------------------------------------------------------------------
static void Profiler_Start ()
{
	if(s_profiler_eventNames == null)
	{
		s_profiler_eventNames		= new String[PROFILER_MAX_EVENTS];
		s_profiler_eventBegins		= new long[PROFILER_MAX_EVENTS];
		s_profiler_eventEnds		= new long[PROFILER_MAX_EVENTS];
		s_profiler_eventDepths		= new short[PROFILER_MAX_EVENTS];
		s_profiler_eventStack		= new short[PROFILER_MAX_EVENTS];
	}

	s_profiler_eventCount = 0;
	s_profiler_eventStackIndex = 0;
	s_profiler_recording = true;
}

//--------------------------------------------------------------------------------------------------------------------
/// End capturing events.
//--------------------------------------------------------------------------------------------------------------------
static void Profiler_End ()
{
	s_profiler_recording = false;
}

//--------------------------------------------------------------------------------------------------------------------
/// Draw profiler events to screen.
//--------------------------------------------------------------------------------------------------------------------
static void Profiler_Draw ()
{
	SetColor(0);

	
		FillRect(0, 0, GetScreenWidth(), s_profiler_eventCount * g.getFont().getHeight());
	

	SetColor(0xFFFFFF);
	
	for(int i = 0, y = 0; i < s_profiler_eventCount; i++)
	{
		
			g.drawString(s_profiler_eventNames[i], s_profiler_eventDepths[i] * 10, y, 0);
			g.drawString(Long.toString(s_profiler_eventEnds[i] - s_profiler_eventBegins[i]), GetScreenWidth() - 2, y, Graphics.RIGHT | Graphics.TOP);
			
			y += g.getFont().getHeight();
		

	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Export profiler events to a file using JSR-75 FileConnection API.
//--------------------------------------------------------------------------------------------------------------------
//static boolean Profiler_Export ()
//{
//	try
//	{
//		ASSERT(System.getProperty("microedition.io.file.FileConnection.version") != null, "File connection not supported");
//		DataOutputStream out = javax.microedition.io.Connector.openDataOutputStream("file://c:/profiler.dat");
//		
//		if(out != null)
//		{
//			// Version and event count
//			out.writeInt(0x00000000);
//			out.writeInt(s_profiler_eventCount);
//
//			// Event details
//			for(int i = 0; i < s_profiler_eventCount; i++)
//			{
//				out.writeUTF(s_profiler_eventNames[i]);
//				out.writeShort(s_profiler_eventDepths[i]);
//				out.writeLong(s_profiler_eventBegins[i]);
//				out.writeLong(s_profiler_eventEnds[i]);
//			}
//			out.close();
//			return true;
//		}
//	}
//	catch(Exception e)
//	{
//	}
//
//	return false;
//}

//--------------------------------------------------------------------------------------------------------------------
/// Mark the beginning of an event.
///!param name Name of the event.
//--------------------------------------------------------------------------------------------------------------------
static void Profiler_BeginNamedEvent (String name)
{
	if (true)
	{
		if(s_profiler_emulator)
		{
			if(!(System.getProperty("EMU://BeginNamedEvent:" + name) != null))Utils.DBG_PrintStackTrace(false, "Failed to call BeginNamedEvent");;
		}
	}

	if(s_profiler_recording)
	{
		if(!(s_profiler_eventCount < PROFILER_MAX_EVENTS))Utils.DBG_PrintStackTrace(false, "Profiler: Too many events " + s_profiler_eventCount);;

		s_profiler_eventNames[s_profiler_eventCount] = name;
		s_profiler_eventBegins[s_profiler_eventCount] = GetRealTime();
		s_profiler_eventDepths[s_profiler_eventCount] = (short)s_profiler_eventStackIndex;
		s_profiler_eventStack[s_profiler_eventStackIndex] = (short)s_profiler_eventCount;
		s_profiler_eventCount++;
		s_profiler_eventStackIndex++;
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Mark the end of an event.
//--------------------------------------------------------------------------------------------------------------------
static void Profiler_EndNamedEvent ()
{
	if (true)
	{
		if(s_profiler_emulator)
		{
			if(!(System.getProperty("EMU://EndNamedEvent") != null))Utils.DBG_PrintStackTrace(false, "Failed to call EndNamedEvent");;
		}
	}

	if(s_profiler_recording)
	{
		s_profiler_eventStackIndex--;

		if(!(s_profiler_eventStackIndex >= 0 && s_profiler_eventStackIndex < PROFILER_MAX_EVENTS))Utils.DBG_PrintStackTrace(false, "Profiler: Begin/End event match problem");;

		s_profiler_eventEnds[s_profiler_eventStack[s_profiler_eventStackIndex]] = GetRealTime();
	}
}


///!}


/// \defgroup GLLib_displayUtil GLLib : Display Utilities
/// Utility functions for drawing
/// \ingroup GLLibMain
///!{

//--------------------------------------------------------------------------------------------------------------------
// GLLib_drawAlpha.jpp
//--------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------
/// Draw Alpha Primitives (for now only Rect, later maybe we can add Triangles and Arcs)
///
/// See:
/// 	zJYLibConfig.alphaRectBufferSize
/// 	zJYLibConfig.alphaRectUseImage
//--------------------------------------------------------------------------------------------------------------------

	// Buffer
	private static int   s_alphaRectCurrentARGB;
	private static int[] s_alphaRectARGBData;
	private static Image s_alphaRectImage;

	//-----------------------------------------------------------------------------
	/// Sets the color of the buffer used to draw alpha rects (AlphaRect_Draw).
	///
	///
	///!param p_iColor is the color we want, with alpha support
	///
	///!see AlphaRect_Draw
	///!see zJYLibConfig.alphaRectBufferSize
	///!see zJYLibConfig.alphaRectUseImage
	///
	///!note Currently only implemented for MIDP20
	//-----------------------------------------------------------------------------
	public static void AlphaRect_SetColor (int p_iColor)
	{

		// Only rebuild buffer if needed
		if (
			 (p_iColor != s_alphaRectCurrentARGB) ||
		     (DevConfig.alphaRectUseImage  && s_alphaRectImage == null) ||
		     (!DevConfig.alphaRectUseImage && s_alphaRectARGBData == null)
		   )
		{
			s_alphaRectCurrentARGB = p_iColor;

			// Attempt to use the main ASprite buffer to create the image (if its large enough)
			if(DevConfig.alphaRectUseImage)
			{
				if (DevConfig.alphaRectBufferSize * DevConfig.alphaRectBufferSize < DevConfig.TMP_BUFFER_SIZE)
				{
					s_alphaRectARGBData = zSprite.temp_int;
				}
			}

			// Allocate if not re-using sprite buffer (can't re-use it when using drawRGB)
			if (s_alphaRectARGBData == null)
			{
				s_alphaRectARGBData = new int[DevConfig.alphaRectBufferSize * DevConfig.alphaRectBufferSize];
			}

			int i = DevConfig.alphaRectBufferSize * DevConfig.alphaRectBufferSize;

			// Set ARGB buffer
			while (i > 0)
			{
				s_alphaRectARGBData[--i] = p_iColor;
			}

			// Create image
			if(DevConfig.alphaRectUseImage)
			{
				s_alphaRectImage = GLLib.CreateRGBImage(s_alphaRectARGBData, DevConfig.alphaRectBufferSize, DevConfig.alphaRectBufferSize, true);
				s_alphaRectARGBData = null;
			}
		}


	}

	//-----------------------------------------------------------------------------
	/// Draws an alpha rect. Set color by using AlphaRect_SetColor.
	///
	///!param g the graphics context
	///!param x x-position of top-left corner of rect
	///!param y y-position of top-left corner of rect
	///!param w the width of the rect in pixels
	///!param h the height of the rect in pixels
	///
	///!see AlphaRect_SetColor
	///!see zJYLibConfig.alphaRectBufferSize
	///!see zJYLibConfig.alphaRectUseImage
	///
	///!note Currently only implemented for MIDP20
	//-----------------------------------------------------------------------------
	public static void AlphaRect_Draw (Graphics g, int x, int y, int w, int h)
	{

		if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
		{
			x = zSprite.scaleX(x);
			y = zSprite.scaleY(y);
			w = zSprite.scaleX(w);
			h = zSprite.scaleY(h);
		}


		int i, j;
		int cx, cy, cw, ch;
		int nx, ny, nw, nh;

		cx = GLLib.GetClipX(g);
		cy = GLLib.GetClipY(g);
		cw = GLLib.GetClipWidth(g);
		ch = GLLib.GetClipHeight(g);

		nx = ((x)>(cx)?(x):(cx));
		ny = ((y)>(cy)?(y):(cy));
		nw = ((x + w)<(cx + cw)?(x + w):(cx + cw)) - nx;
		nh = ((y + h)<(cy + ch)?(y + h):(cy + ch)) - ny;

		if (nw <= 0 || nh <= 0)
		{
			return;
		}

		GLLib.SetClip(g, nx, ny, nw, nh);

	    if (!DevConfig.alphaRectUseImage && (nw * nh) < DevConfig.alphaRectBufferSize * DevConfig.alphaRectBufferSize)
	    {
			// Use GLLib's wrapper call
			GLLib.DrawRGB(g, s_alphaRectARGBData, 0, nw, nx, ny, nw, nh, true);
		}
		else
		{
			nw += nx;
			nh += ny;

			for (i = nx; i < nw; i += DevConfig.alphaRectBufferSize)
			{
				for (j = ny; j < nh; j += DevConfig.alphaRectBufferSize)
				{
					if(DevConfig.alphaRectUseImage)
					{
						GLLib.DrawImage(g, s_alphaRectImage, i, j, TOP | LEFT);
					}
					else
					{
						// Use GLLib's wrapper call
						GLLib.DrawRGB(g, s_alphaRectARGBData, 0, DevConfig.alphaRectBufferSize, i, j, DevConfig.alphaRectBufferSize, DevConfig.alphaRectBufferSize, true);
					}
				}
			}
		}

		GLLib.SetClip(g, cx, cy, cw, ch);


	}

//--------------------------------------------------------------------------------------------------------------------

	//-----------------------------------------------------------------------------
	/// Draws an alpha line.
	///
	///!param dst - Is the graphics context to render to.
	///!param x1  - Is the X position of the starting point.
	///!param y1  - Is the Y position of the starting point.
	///!param x2  - Is the X position of the ending point.
	///!param y2  - Is the Y position of the ending point.
	///!param c   - The color to use (AARRGGBB format)
	///
	///!note Only supported with MIDP2 is present (uses drawRGB)
	//-----------------------------------------------------------------------------
	static void DrawAlphaLine (Graphics dst, int x1, int y1, int x2, int y2, int color)
	{
		int alpha = (color >> 24) & 0xFF;

		// TODO: Provide custom implementation that is optimized instead of using this general function [MMZ]
		DrawAAAlphaLine(dst, x1, y1, x2, y2, (color & 0x00FFFFFF), alpha, alpha);
	}

	//-----------------------------------------------------------------------------
	/// Draws an alpha rect (not filled)
	///
	///!param dst - Is the graphics context to render to.
	///!param x1  - Is the X position of the starting point.
	///!param y1  - Is the Y position of the starting point.
	///!param x2  - Is the X position of the ending point.
	///!param y2  - Is the Y position of the ending point.
	///!param c   - The color to use (AARRGGBB format)
	///
	///!note Only supported with MIDP2 is present (uses drawRGB)
	//-----------------------------------------------------------------------------
	static void DrawAlphaRect (Graphics dst, int x, int y, int w, int h, int color)
	{
		int alpha = (color >> 24) & 0xFF;
		color = color & 0x00FFFFFF;

		// TODO: Switch to DrawAlphaLine once it is customized [MMZ]
		DrawAAAlphaLine(dst, x + 1    ,     y, x + w, y    , color, alpha, alpha);
		DrawAAAlphaLine(dst, x        ,     y, x    , y + h, color, alpha, alpha);
		DrawAAAlphaLine(dst, x + w    , y + h, x + w, y    , color, alpha, alpha);
		DrawAAAlphaLine(dst, x + w + 1, y + h, x    , y + h, color, alpha, alpha);
	}

///!}




//-------------------------------------------------------------------------------------------------

///!}


///!addtogroup GLLib_displayUtil
///!{

//--------------------------------------------------------------------------------------------------------------------
// GLLib_drawGradient.jpp
//--------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------
/// Draws a rect with a gradient color.
///
///!param x   - The top-left X position of the rect.
///!param y   - The top-left Y position of the rect.
///!param w   - The width of the rect.
///!param h   - The height of the rect.
///!param c1  - The starting color of the gradient.
///!param c2  - The ending color of the gradient.
///!param direction - The direction of the gradient, valid values are GLLib.LEFT, GLLib.RIGHT, GLLib.TOP, GLLib.BOTTOM.
//-----------------------------------------------------------------------------
public static void DrawGradientRect (int x, int y, int w, int h, int c1, int c2, int direction)
{
    int r1 = (c1 >> 16) & 0xFF;
    int g1 = (c1 >> 8 ) & 0xFF;
    int b1 = (c1 >> 0 ) & 0xFF;

    int r2 = (c2 >> 16) & 0xFF;
    int g2 = (c2 >> 8 ) & 0xFF;
    int b2 = (c2 >> 0 ) & 0xFF;

    int dr = r2 - r1;
    int dg = g2 - g1;
    int db = b2 - b1;

    int x2 = x + w - 1;
    int y2 = y + h - 1;

    int steps;

    // Use 16bits of precision and add step per line
	r1 <<= 16;
	g1 <<= 16;
	b1 <<= 16;

    if (direction == GLLib.LEFT)
    {
        steps = x2 - x;

		dr = (dr << 16) / steps;
		dg = (dg << 16) / steps;
		db = (db << 16) / steps;

        for (int i = x2; i >= x; i--)
        {
            SetColor((r1 >> 16), (g1 >> 16), (b1 >> 16));
            DrawLine(i, y, i, y2);

            r1 += dr;
			g1 += dg;
            b1 += db;
        }
    }
    else if (direction == GLLib.RIGHT)
    {
        steps = x2 - x;

		dr = (dr << 16) / steps;
		dg = (dg << 16) / steps;
		db = (db << 16) / steps;

        for (int i = x; i <= x2; i++)
        {
            SetColor((r1 >> 16), (g1 >> 16), (b1 >> 16));
            DrawLine(i, y, i, y2);

            r1 += dr;
			g1 += dg;
            b1 += db;
        }
    }
    else if (direction == GLLib.TOP)
    {
        steps = y2 - y;

		dr = (dr << 16) / steps;
		dg = (dg << 16) / steps;
		db = (db << 16) / steps;

        for (int i = y2; i >= y; i--)
        {
            SetColor((r1 >> 16), (g1 >> 16), (b1 >> 16));
            DrawLine(x, i, x2, i);

            r1 += dr;
			g1 += dg;
            b1 += db;
        }
    }
    else if (direction == GLLib.BOTTOM)
    {
        steps = y2 - y;

		dr = (dr << 16) / steps;
		dg = (dg << 16) / steps;
		db = (db << 16) / steps;

        for (int i = y; i <= y2; i++)
        {
            SetColor((r1 >> 16), (g1 >> 16), (b1 >> 16));
            DrawLine(x, i, x2, i);

            r1 += dr;
			g1 += dg;
            b1 += db;
        }
    }
}

//--------------------------------------------------------------------------------------------------------------------

///!}


//--------------------------------------------------------------------------------------------------------------------
// GLLib_drawAlphaGradient.jpp
//--------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------
/// Draws an alpha rect with a gradient color.
///
///!param x   - The top-left X position of the rect.
///!param y   - The top-left Y position of the rect.
///!param w   - The width of the rect.
///!param h   - The height of the rect.
///!param c1  - The starting color of the gradient.
///!param c2  - The ending color of the gradient.
///!param direction - The direction of the gradient, valid values are GLLib.LEFT, GLLib.RIGHT, GLLib.TOP, GLLib.BOTTOM.
//-----------------------------------------------------------------------------
public static void DrawAlphaGradientRect (int x, int y, int w, int h, int c1, int c2, int direction)
{
	if(!((direction == GLLib.LEFT) || (direction == GLLib.RIGHT) || (direction == GLLib.TOP) || (direction == GLLib.BOTTOM)))Utils.DBG_PrintStackTrace(false, "DrawAlphaGradientRect: direction parameter is not valid: " + direction);;

	// Direction independent vars
	int gradLength = w;
	int gradWidth  = h;
	int index;

	if ((direction == GLLib.TOP) || (direction == GLLib.BOTTOM))
	{
		gradLength = h;
		gradWidth  = w;
	}

	// Reduce from 4 . 2 cases by swapping colors
	if ((direction == GLLib.LEFT) || (direction == GLLib.TOP))
	{
		{(index) = (c1);(c1) = (c2);(c2) = (index);};
	}

	// Pixel Buffer (uses temp_int)
	int[] buffer = zSprite.GetPixelBuffer_int(null);
	int    lines = ((gradWidth)<(buffer.length / gradLength)?(gradWidth):(buffer.length / gradLength));

	// Get Channels
	int a1 = (c1 >> 24) & 0xFF;
	int r1 = (c1 >> 16) & 0xFF;
	int g1 = (c1 >> 8 ) & 0xFF;
	int b1 = (c1 >> 0 ) & 0xFF;

	int a2 = (c2 >> 24) & 0xFF;
	int r2 = (c2 >> 16) & 0xFF;
	int g2 = (c2 >> 8 ) & 0xFF;
	int b2 = (c2 >> 0 ) & 0xFF;

	// Get Deltas (there are gradLength-1 steps)
	gradLength--;
	int da = ((a2 - a1) << 16) / gradLength;
	int dr = ((r2 - r1) << 16) / gradLength;
	int dg = ((g2 - g1) << 16) / gradLength;
	int db = ((b2 - b1) << 16) / gradLength;
	gradLength++;

	// Use 16bits of precision and add step per line
	a1 <<= 16;
	r1 <<= 16;
	g1 <<= 16;
	b1 <<= 16;

	// Buffer filling info (re-use some vars)
 	if ((direction == GLLib.LEFT) || (direction == GLLib.RIGHT))
    {
        g2 = w;
        b2 = -(w * lines) + 1;
    }
    else
    {
        g2 = 1;
        b2 = 0;
    }

	// Set the buffer to a strip of graident
	index = 0;

	for (int i=0; i<gradLength; i++)
	{
		int l = lines;

		while(--l >= 0)
		{
			buffer[index] = c1;
			index += g2;
		}

		// Apply delta step
		a1 += da;
		r1 += dr;
		g1 += dg;
		b1 += db;

		// Form Color
		c1 = ((a1 << 8) & 0xFF000000) | ((r1) & 0x00FF0000) | ((g1 >> 8) & 0x0000FF00) | ((b1 >> 16) & 0x000000FF);

		index += b2;
	}

	// Render the pixel buffer array as many times as needed...
 	if ((direction == GLLib.LEFT) || (direction == GLLib.RIGHT))
    {
		while (gradWidth > 0)
		{
			DrawRGB(g, buffer, 0, w, x, y, w, ((lines)<(gradWidth)?(lines):(gradWidth)), true);
			y += lines;
			gradWidth -= lines;
		}
    }
    else if ((direction == GLLib.TOP) || (direction == GLLib.BOTTOM))
    {
		while (gradWidth > 0)
		{
			DrawRGB(g, buffer, 0, lines, x, y, ((lines)<(gradWidth)?(lines):(gradWidth)), h, true);
			x += lines;
			gradWidth -= lines;
		}
    }
}


///!addtogroup GLLib_displayUtil
///!{

//--------------------------------------------------------------------------------------------------------------------
// GLLib_drawAA.jpp
//--------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------
/// Draw Anti-Aliased Primitives
//--------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------
/// Draws an anti-aliased thick line of a given width and color
///
///!param g     - Is the graphics context to render to.
///!param lineW - Is the width in pixels of the line.
///!param x0  - Is the X position of the starting point.
///!param y0  - Is the Y position of the starting point.
///!param x1  - Is the X position of the ending point.
///!param y1  - Is the Y position of the ending point.
///!param c   - The color to use.
///!param a0  - Alpha value at starting point of line.
///!param a1  - Alpha value at ending point of line.
///
///!note Only supported with MIDP2 is present (uses drawRGB)
//-----------------------------------------------------------------------------
public static void DrawAAAlphaLineWidth (Graphics g, int lineW, int x0, int y0, int x1, int y1, int c, int a0, int a1)
{
	int diffX = cMath.Math_Abs(x1 - x0);
	int diffY = cMath.Math_Abs(y1 - y0);

	lineW >>= 1;

	for(int i = -lineW; i < lineW; i++)
	{
		if(diffX > diffY)
		{
			DrawAAAlphaLine (g, x0, y0 + i, x1, y1 + i, c, a0, a1);
		}
		else
		{
			DrawAAAlphaLine (g, x0 + i, y0, x1 + i, y1, c, a0, a1);
		}
	}
}

//-----------------------------------------------------------------------------
/// Draws an anti-aliased line of a given color
///
///!param dst - Is the graphics context to render to.
///!param x1  - Is the X position of the starting point.
///!param y1  - Is the Y position of the starting point.
///!param x2  - Is the X position of the ending point.
///!param y2  - Is the Y position of the ending point.
///!param c   - The color to use.
///
///!note Only supported with MIDP2 is present (uses drawRGB)
//-----------------------------------------------------------------------------
final static void DrawAALine (Graphics dst, int x1, int y1, int x2, int y2, int c)
{
	DrawAAAlphaLine(dst, x1, y1, x2, y2, c, 0xFF, 0xFF);
}

//-----------------------------------------------------------------------------
/// Draws an anti-aliased line of a given color with varying alpha.
///
///!param dst - Is the graphics context to render to.
///!param x1  - Is the X position of the starting point.
///!param y1  - Is the Y position of the starting point.
///!param x2  - Is the X position of the ending point.
///!param y2  - Is the Y position of the ending point.
///!param c   - The color to use.
///!param a1  - Is the alpha value at the starting point of the line.
///!param a2  - Is the alpha value at the ending point of the line.
///
///!note Only supported with MIDP2 is present (uses drawRGB)
//-----------------------------------------------------------------------------
static void DrawAAAlphaLine (Graphics dst, int x1, int y1, int x2, int y2, int c, int a1, int a2)
{
	int dx = x2 - x1;
	int dy = y2 - y1;
	int tmp;

	if (((dx)<0 ? -(dx) : (dx)) > ((dy)<0 ? -(dy) : (dy)))
	{
		if (x2 < x1)
		{
			{(tmp) = (x1);(x1) = (x2);(x2) = (tmp);};
			{(tmp) = (y1);(y1) = (y2);(y2) = (tmp);};
			{(tmp) = (a1);(a1) = (a2);(a2) = (tmp);};
		}

		dx = x2 - x1;
		dy = y2 - y1;

		int gradient = 0;
		if (dx != 0)
			gradient = (dy << 16) / dx;

		//handle "horizontal" lines
		// handle first endpoint
		int xend = x1;
		int yend = (y1 << 16) + (gradient * (xend - x1));
		int xpxl1 = xend;  // this will be used in the main loop
		int ypxl1 = yend;
		//putpixel(dst, x1, y1, c, a1);
		int intery = yend + gradient; // first y-intersection for the main loop

		// handle second endpoint
		xend = x2;
		yend = (y2 << 16) + (gradient * (xend - x2));
		int xpxl2 = xend;  // this will be used in the main loop
		//int ypxl2 = yend;
		//putpixel(dst, x2, y2, c, a2);

		int da = 0;
		if (dx != 0)
			da = ((a2 - a1) << 16) / dx;

		int buf[] = zSprite.GetPixelBuffer_int(null);

		a1 <<= 16;
		// main loop
		for (int x = xpxl1; x < xpxl2; x++)
		{
			int yy = intery >> 16;

			int fpart = intery & 0xFFFF;
			int rfpart = 65535 - fpart;

			int alpha = ((rfpart >> 8) * a1) & 0xFF000000;
			buf[0] = alpha | c;

			alpha = ((fpart >> 8) * a1) & 0xFF000000;
			buf[1] = alpha | c;

			DrawRGB(dst, buf, 0, 1, x, yy, 1, 2, true);

			intery = intery + gradient;
			a1 += da;
		}
	}
	else
	{
		if (y2 < y1)
		{
			{(tmp) = (x1);(x1) = (x2);(x2) = (tmp);};
			{(tmp) = (y1);(y1) = (y2);(y2) = (tmp);};
			{(tmp) = (a1);(a1) = (a2);(a2) = (tmp);};
		}

		dx = x2 - x1;
		dy = y2 - y1;

		int gradient = 0;
		if (dy != 0)
			gradient = (dx << 16) / dy;

		//handle "vertical" lines
		// handle first endpoint
		int yend = y1;
		int xend = (x1 << 16) + (gradient * (yend - y1));
		int ypxl1 = yend;  // this will be used in the main loop
		int xpxl1 = xend;
		//putpixel(dst, x1, y1, c, a1);
		int intery = xend + gradient; // first y-intersection for the main loop

		// handle second endpoint
		yend = y2;
		xend = (x2 << 16) + (gradient * (yend - y2));
		int ypxl2 = yend;  // this will be used in the main loop
		//int xpxl2 = xend;
		//putpixel(dst, x2, y2, c, a2);

		int da = 0;
		if (dy != 0)
			da = ((a2 - a1) << 16) / dy;

		a1 <<= 16;

		int buf[] = zSprite.temp_int;
		// main loop
		for (int y = ypxl1; y < ypxl2; y++)
		{
			int xx = intery >> 16;
			int fpart = intery & 0xFFFF;
			int rfpart = 65535 - fpart;

			int alpha = ((rfpart >> 8) * a1) & 0xFF000000;
			buf[0] = alpha | c;

			alpha = ((fpart >> 8) * a1) & 0xFF000000;
			buf[1] = alpha | c;

			DrawRGB(dst, buf, 0, 2, xx, y, 2, 1, true);

			intery = intery + gradient;
			a1 += da;
		}
	}
}

//-----------------------------------------------------------------------------
/// Draws an anti-aliased circle (color can contain alpha, drawRGB is used)
///
///!param dst - Is the graphics context to render to.
///!param px  - Is the X position of the circles center.
///!param py  - Is the Y position of the circles center.
///!param r   - Is the radius of the circle.
///!param c   - Is the ARGB color to use.
///
///!note Only supported with MIDP2 is present (uses drawRGB)
//-----------------------------------------------------------------------------
public static void DrawAACircle (Graphics dst, int px, int py, int r, int c)
{
	int r2 = r*r;
	int vx = r;
	int vy = 0;
	int t = 0;
	int dry, s, fdry;

	int buf[] = zSprite.GetPixelBuffer_int(null);

	buf[0] = 0xFF000000 | c;

	DrawRGB(dst, buf, 0, 1, px+vx, py+vy, 1, 1, true);
	DrawRGB(dst, buf, 0, 1, px-vx, py+vy, 1, 1, true);
	DrawRGB(dst, buf, 0, 1, px+vy, py+vx, 1, 1, true);
	DrawRGB(dst, buf, 0, 1, px+vy, py-vx, 1, 1, true);

	while (vx > vy + 1)
	{
		vy++;
		s = cMath.Math_Sqrt((r2 - vy*vy) << 16);
		dry = 255 - (s & 255);

		if (dry < t)
		{
			vx--;
		}

		fdry = 0xFF - dry;

		buf[0] = (dry << 24)  | c;
		buf[1] = (fdry << 24) | c;

		DrawRGB(dst, buf, 0, 2, px + vx - 1, py + vy, 2, 1, true);
		DrawRGB(dst, buf, 0, 2, px + vx - 1, py - vy, 2, 1, true);
		DrawRGB(dst, buf, 0, 1, px + vy, py + vx - 1, 1, 2, true);
		DrawRGB(dst, buf, 0, 1, px - vy, py + vx - 1, 1, 2, true);

		buf[0] = (fdry << 24) | c;
		buf[1] = (dry << 24)  | c;

		DrawRGB(dst, buf, 0, 2, px - vx, py + vy, 2, 1, true);
		DrawRGB(dst, buf, 0, 2, px - vx, py - vy, 2, 1, true);
		DrawRGB(dst, buf, 0, 1, px + vy, py - vx, 1, 2, true);
		DrawRGB(dst, buf, 0, 1, px - vy, py - vx, 1, 2, true);

		t = dry;
	}
}


	// //////////////////////////////////////////////////////////////////////

public void pay_failed(int pay_type) {}

public void pay_success(int pay_type) {}

//--------------------------------------------------------------------------------------------------------------------

	////////////////////////////////////////////////////////////
	//对数组的操作
	
	//读取
	public static int Data_GetUInt8(byte[] data, int offset) throws Exception
	{		
		return (data[offset++] & 0xff);
	}	
	public static byte Data_GetByte(byte[] data, int offset) throws Exception
	{
		return (data[offset++]);
	}
	
	public static int Data_GetUInt16(byte[] data, int offset) throws Exception
	{
		return ((data[offset++] & 0xFF) + ((data[offset++] & 0xFF) << 8));
	}	
	public static short Data_GetShort(byte[] data, int offset) throws Exception
	{
		return (short) ((data[offset++] & 0xFF) + ((data[offset++] & 0xFF) << 8));
	}
	
	public static boolean Data_GetBoolean(byte[] data, int offset) throws Exception
	{
		return (data[offset++] != 0);
	}
	
	public static int Data_GetInt(byte[] data, int offset) throws Exception
	{
		return (int)((data[offset++] & 0xFF) + ((data[offset++] & 0xFF) << 8)
				+ ((data[offset++] & 0xFF) << 16) + ((data[offset++] & 0xFF) << 24));
	}
	public static int Data_GetArray(byte[] data, int offset, 
			byte[] dest, int d_offset, int size) throws Exception
	{
		System.arraycopy(data, offset, dest, d_offset, size);
		offset += size;
		
		return size;
	}
	public static int Data_GetArray(byte[] data, int offset, 
			int[] dest, int d_offset, int size) throws Exception
	{
		for (int i = 0; i < size; i++) {
			dest[d_offset + i] = Data_GetInt(data, offset);
			offset += 4;
		}
		
		return size;
	}
	
	//写入
	public static void Data_SetByte(byte[] data, int offset, byte b) throws Exception
	{	
		data[offset++] = b;
	}
	public static void Data_SetShort(byte[] data, int offset, short s) throws Exception
	{
		data[offset++] = (byte)(s & 0xFF);
		data[offset++] = (byte)((s >> 8 ) & 0xFF);
	}
	
	public static void Data_SetBoolean(byte[] data, int offset, boolean bl) throws Exception
	{
		data[offset++] = (byte)(bl ? 1 : 0);
	}

	public static void Data_SetInt(byte[] data, int offset, int i) throws Exception
	{
		data[offset++] = (byte)((i >> 0) & 0xFF);
		data[offset++] = (byte)((i >> 8 ) & 0xFF);
		data[offset++] = (byte)((i >> 16 ) & 0xFF);
		data[offset++] = (byte)((i >> 24 ) & 0xFF);
	}
	
	public static void Data_SetArray(byte[] data, int offset, 
			byte[] src, int s_offset, int size) throws Exception
	{
		System.arraycopy(src, s_offset, data, offset, size);
		offset += size;
	}

	public static void Data_SetArray(byte[] data, int offset, 
			int[] src, int s_offset, int size) throws Exception
	{
		for (int i = 0; i < size; i++) {
			Data_SetInt(data, offset, src[s_offset + i]);
			offset += 4;
		}
	}
}

///!}
