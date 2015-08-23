package cn.thirdgwin.lib;

import javax.microedition.lcdui.Canvas;

public class DevConfig extends zLibConfigration_PleaseUseDevConfig_To_Access_This_Class
{
/***********************************************************************\
 * 	屏幕定义，请在make_lua\device下进行修正调整
\***********************************************************************/
//#expand	public static final short SCR_W				= %SCREENWIDTH%;
	public static final short SCR_W				= 240;
//#expand	public static final short SCR_H				= %SCREENHEIGHT%;
	public static final short SCR_H				= 320;
/***********************************************************************\
 * 	键值定义，请在make_lua\device下进行修正调整
\***********************************************************************/
//#expand 	public static final short KEY_LSK			= %KEY_LSK%;
	public static final short KEY_LSK			= -6;
//#expand 	public static final short KEY_RSK			= %KEY_RSK%;
	public static final short KEY_RSK			= -7;
//#expand 	public static final short KEY_FIRE			= %KEY_FIRE%;
	public static final short KEY_FIRE			= -5;
//#expand 	public static final short KEY_UP			= %KEY_UP%;
	public static final short KEY_UP			= -1;
//#expand 	public static final short KEY_DOWN			= %KEY_DOWN%;
	public static final short KEY_DOWN			= -2;
//#expand 	public static final short KEY_LEFT			= %KEY_LEFT%;
	public static final short KEY_LEFT			= -3;
//#expand 	public static final short KEY_RIGHT			= %KEY_RIGHT%;
	public static final short KEY_RIGHT			= -4;
//#expand 	public static final short KEY_VOLUMEUP		= %KEY_VOLUMEUP%;
	public static final short KEY_VOLUMEUP		= 0;
//#expand 	public static final short KEY_VOLUMEDOWN	= %KEY_VOLUMEDOWN%;
	public static final short KEY_VOLUMEDOWN	= 0;

//#if MODEL=="E62"	
	public static final int KEY_NUM0 = 109;
	public static final int KEY_NUM1 = 114;
	public static final int KEY_NUM2 = 116;
	public static final int KEY_NUM3 = 121;
	public static final int KEY_NUM4 = 102;
	public static final int KEY_NUM5 = 103;
	public static final int KEY_NUM6 = 104;
	public static final int KEY_NUM7 = 118;
	public static final int KEY_NUM8 = 98;
	public static final int KEY_NUM9 = 110;
	public static final int KEY_STAR = 117;
	public static final int KEY_DIEZ = 106;
//#else
	public static final int KEY_NUM0 = Canvas.KEY_NUM0;
	public static final int KEY_NUM1 = Canvas.KEY_NUM1;
	public static final int KEY_NUM2 = Canvas.KEY_NUM2;
	public static final int KEY_NUM3 = Canvas.KEY_NUM3;
	public static final int KEY_NUM4 = Canvas.KEY_NUM4;
	public static final int KEY_NUM5 = Canvas.KEY_NUM5;
	public static final int KEY_NUM6 = Canvas.KEY_NUM6;
	public static final int KEY_NUM7 = Canvas.KEY_NUM7;
	public static final int KEY_NUM8 = Canvas.KEY_NUM8;
	public static final int KEY_NUM9 = Canvas.KEY_NUM9;
	public static final int KEY_STAR = Canvas.KEY_STAR;
	public static final int KEY_DIEZ = Canvas.KEY_POUND;
//#endif
	  
	 
//#if SOFTKEY_OK_ON_LEFT=="TRUE"
	public static final boolean softkeyOKOnLeft = true;
//#else
	public static final boolean softkeyOKOnLeft = false;
//#endif	
	
///May solve some slowness/key response. This is the only option for some phones (default = false)
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : false<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>	
	
//#if USE_CALLSERIALLY=="TRUE"
	public static final boolean useCallSerially = true;
//#else
	public static final boolean useCallSerially = false;
//#endif	
///May solve some devices will freeze if calling serviceRepaints. This is the only option for some phones (default = true)
/// 
/// <code>
///    Default MIDP 1          : true<br>
///    Default MIDP 1 NOKIA    : true<br>
///    Default MIDP 1 SPRINT   : true<br>
///    Default MIDP 2          : true<br>
///    Default MIDP 2 NOKIA    : true<br>
///    Default MIDP 2 SPRINT   : true<br>
///    Default DOJA            : true<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : true<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : true<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : true<br>
///    Default MIDP 2 SAMSUNG  : true<br>
/// </code>	
//#if USE_SERVICEREPAINT=="TRUE"
	public static final boolean useServiceRepaints = true;
//#else
	public static final boolean useServiceRepaints = false;
//#endif	
	
	
///For devices which don't catch interrupt properly (i.e. neither hideNotity() nor pauseApp() is called when the game is interrupted), this option will possibly help
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : false<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>	
//#if USE_FAKE_INTERRUPT=="TRUE"
	public static final boolean useFakeInterruptHandling = true;
//#expand 	public static final short FakeInterruptThreshold	= %FAKE_INTERRUPT_THRESHOLD%;
	public static final short FakeInterruptThreshold	= 2000;
//#else
	public static final boolean useFakeInterruptHandling = false;
///If the interruption duration is longer than this value, then the game will know that it was interrupted. Default value: 3000 (milliseconds). If there's issue like "in-game menu unexpectedly opens after loading from main-menu to in-game", try increasing this value.
/// &warning Works only if useFakeInterruptHandling is True.
	public static final short FakeInterruptThreshold	= 5000;
//#endif	

	
	
/***********************************************************************\
 * 	API配置，请在make_lua\device下进行修正调整
\***********************************************************************/
	public static final boolean	API_BUG_FILLROUNDRECT				= false;
/***********************************************************************\
 * 	SPRITE配置，请在make_lua\device下进行修正调整
\***********************************************************************/
	public static final boolean SPRITE_USEMODULEWHSHORT				= true;
	public static final boolean sprite_useNokiaUI					= true;
	public static final boolean sprite_useCreateRGB					= true;
/***********************************************************************\
 * 	FPS 定义 
\***********************************************************************/	
	public static final int FPS					= 12;
	public static final int FPS_INTERVAL		= 900/FPS;			//不用1000了，因为有误差
/***********************************************************************\
 * 	临时放在这里:Engine
\***********************************************************************/	
	/** Sprite按Frame更新，不按时间 ,本设定已作废 */
	public static final boolean SPRITE_UPDATE_FRAME_BASED	 				= true;	
	/** 如果Frame的持续时间为0，则认为是永久 */
	public static final boolean SPRITE_ANIM_DURATIONZERO_AS_INFINITE		= true;
	/** 不允许跳帧 */
	public static final boolean SPRITE_NOSKIPFRAME						= true;
/***********************************************************************\
 * 	从zLibConfig转过来了，还未整理
\***********************************************************************/		
	public static final int     sprite_systemFontSmallHeight = 17;
	///System.gc() will be called automatically (with GLLib.Gc()) if some resource be destroyed in GLLib internal, set to false to deactivate this feature, and do it yourselfery.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : true<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
	public static final boolean useSystemGc                                    = false;
	///Low memory value (in byte). System.gc() will be called automatically if the available free memory is lower than this value, set to 0 to deactivate this feature
	/// 
	/// <code>
	///    Default MIDP 1          : 0<br>
	///    Default MIDP 1 NOKIA    : 0<br>
	///    Default MIDP 1 SPRINT   : 0<br>
	///    Default MIDP 2          : 0<br>
	///    Default MIDP 2 NOKIA    : 0<br>
	///    Default MIDP 2 SPRINT   : 0<br>
	///    Default DOJA            : 0<br>
	///    Default BLACKBERRY      : 0<br>
	///    Default WIPI_JAVA       : 0<br>
	///    Default DOCOMO          : 0<br>
	///    Default MIDP 2 SOFTBANK : 0<br>
	///    Default DOCOMO_STAR     : 0<br>
	///    Default MIDP 2 DANGER   : 0<br>
	///    Default MIDP 2 SAMSUNG  : 0<br>
	/// </code>
	public static final int     lowMemoryLimit                                 = 0;
	///This boolean indicates if platformRequest should be done when exiting the app. Note that the responsibility of calling PlatformRequest when exiting still lays on the game. The actual call is not automatic.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : true<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
	public static final boolean platformRequestOnExit                          = true;
	///Use repaint() at Resume() function
	/// &warning Should be false with Toshiba series in Softbank plaform
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : true<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : true<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : true<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
	public static final boolean useRepaintAtResume                             = true;
	///Fix for the drawLine method bug, when it draws outside the current clip if y1 > y2
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useDrawLineClippingBug                         = false;
	///May prevent exceptions and slowdowns during drawing. Slightly
	/// &warning increases jar size.
	/// 
	/// <code>
	///    Default MIDP 1          : true    // INVALID<br>
	///    Default MIDP 1 NOKIA    : true    // INVALID<br>
	///    Default MIDP 1 SPRINT   : true    // INVALID<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : true    // INVALID<br>
	///    Default DOCOMO          : true<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : true<br>
	///    Default MIDP 2 DANGER   : true<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
	public static final boolean useSafeDrawRegion                              = true;
	///Workarounds the problem of fillRect() having no effect on a backbuffer outside the size of the screen. Slightly increases memory consumption and reduces fillRect's performace. Reference: https://devwiki.gameloft.org/twiki/bin/view/Main/SamsungFillRectInAnOffscreenBufferIsIgnored
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useSafeFillRect                                = false;
	///Set to true to enable a software double buffer
	/// &warning On MIDP2 phones, there is allready a Double Buffer, setting this to true, would result in having a triple buffer.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false    // Warning, be carefull<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // Warning, be carefull<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean useSoftwareDoubleBuffer                        = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Set to true if you want to use scaling on the backbuffer. Can be used to adjust screen resolution if the phone is fast enough to handle it. 
	/// &warning Huge performance overhead. 
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false    // Warning, be carefull<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // Warning, be carefull<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean useSoftwareDoubleBufferScaling                 = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Will force the double buffer to be sqaure, using the larger of W and H for the size (used to account for screen rotations).
	/// &warning Only applies if useSoftwareDoubleBuffer is set to TRUE.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false    // Warning, be carefull<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // Warning, be carefull<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean useSoftwareDoubleBufferLarge                   = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Limit the game speed to this fps value.
	/// 
	/// <code>
	///    Default MIDP 1          : 25<br>
	///    Default MIDP 1 NOKIA    : 25<br>
	///    Default MIDP 1 SPRINT   : 25<br>
	///    Default MIDP 2          : 16<br>
	///    Default MIDP 2 NOKIA    : 25<br>
	///    Default MIDP 2 SPRINT   : 25<br>
	///    Default DOJA            : 25<br>
	///    Default BLACKBERRY      : 25<br>
	///    Default WIPI_JAVA       : 25<br>
	///    Default DOCOMO          : 25<br>
	///    Default MIDP 2 SOFTBANK : 25<br>
	///    Default DOCOMO_STAR     : 25<br>
	///    Default MIDP 2 DANGER   : 25<br>
	///    Default MIDP 2 SAMSUNG  : 25<br>
	/// </code>
//#if PLATFORM=="Android"
	public static final int     FPSLimiter                                     = 30;
//#else
	public static final int     FPSLimiter                                     = 12;
//#endif
	///May solve slowness if the game looks like it's using too much CPU
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : true<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : true<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : true<br>
	///    Default MIDP 2 DANGER   : true<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
//#if PLATFORM=="Android"
	public static final boolean useSleepInsteadOfYield                         = false;
//#else
	public static final boolean useSleepInsteadOfYield                         = true;
//#endif
	///When set the user will be able to overwrite the normal sleep/yield behavior and set a custom sleep time (or yield) to use during update by using SetCustomSleepTime. Reverting to normal behavior can be done by passing in -1.
	/// &warning Used for loading to prevent normal frame limiting from happening.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean allowCustomSleepTime                           = false;
	///For devices that drawing with RGB outside the screen make them crash.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useDrawPartialRGB                              = false;
	///For devices where drawRGB does not honor the graphics translation values. When enabled the translation will be queried and applied manually. 
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useDrawRGBTranslationFix                       = false;
	///Force MIDP2 device to stay in non fullscreen mode
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean MIDP2forceNonFullScreen                        = false;
	///Workaround to fix an issue on some Nokia S60 phone such as 6600 where images drawn on screen may be broken 'coz there's something wrong with the setClip function.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useNokiaS60SetClipBugfix                       = false;
	///In a Nokia 6280 compatibility phone. DrawRGB with offset > 0 cause game crash.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useDrawRGBOffsetFix                            = false;
	///The size (side) of the square buffer used to draw the alpha rect. 
	/// &warning If alphaRectUseImage is enabled then this value squared should be less than or equal to TMP_BUFFER_SIZE. This is because then we can reuse the int[] buffer and do not need to store the int[] ourselves (saves memory).
	/// 
	/// <code>
	///    Default MIDP 1          : 16<br>
	///    Default MIDP 1 NOKIA    : 16<br>
	///    Default MIDP 1 SPRINT   : 16<br>
	///    Default MIDP 2          : 64<br>
	///    Default MIDP 2 NOKIA    : 16<br>
	///    Default MIDP 2 SPRINT   : 16<br>
	///    Default DOJA            : 16<br>
	///    Default BLACKBERRY      : 16<br>
	///    Default WIPI_JAVA       : 16<br>
	///    Default DOCOMO          : 16<br>
	///    Default MIDP 2 SOFTBANK : 16<br>
	///    Default DOCOMO_STAR     : 16<br>
	///    Default MIDP 2 DANGER   : 16<br>
	///    Default MIDP 2 SAMSUNG  : 16<br>
	/// </code>
	public static final int     alphaRectBufferSize                            = 64;
	///If set to true, then when setting the color of the alphaRect, an Image will be created to be later used when drawing the alphaRect. Using this will make changing colors slower (since it will need to create the Image) but drawing fill be faster since it will not use drawRGB. 
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean alphaRectUseImage                              = true;
	///use drawLine() to draw fillTrianglee() for Softbank platform.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // INVALID<br>
	///    Default MIDP 2 NOKIA    : false    // INVALID<br>
	///    Default MIDP 2 SPRINT   : false    // INVALID<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
	/// </code>
	public static final boolean useFillTriangleSoft                            = false;    // Invalid option for this plateform : MIDP2
	///When set to some value GetDisplayColor will return this value when the color is FF00FF (mageneta).
	/// 
	/// <code>
	///    Default MIDP 1          : -1<br>
	///    Default MIDP 1 NOKIA    : -1<br>
	///    Default MIDP 1 SPRINT   : -1<br>
	///    Default MIDP 2          : -1<br>
	///    Default MIDP 2 NOKIA    : -1<br>
	///    Default MIDP 2 SPRINT   : -1<br>
	///    Default DOJA            : -1<br>
	///    Default BLACKBERRY      : -1<br>
	///    Default WIPI_JAVA       : -1<br>
	///    Default DOCOMO          : -1<br>
	///    Default MIDP 2 SOFTBANK : -1<br>
	///    Default DOCOMO_STAR     : -1<br>
	///    Default MIDP 2 DANGER   : -1<br>
	///    Default MIDP 2 SAMSUNG  : -1<br>
	/// </code>
	public static final int     useGetDisplayColorMagentaFix                   = -1;
	///Use absolute value for keycode, if keycode can be positive or negative (as to maintain compatibility on triplets phones for example)
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useAbsoluteValueOfKeyCode                      = false;
	///Fix the bug that some keys are not released when multiple keys are pressed. (Something common in low end Samsung and Pantech phones).
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useBugFixMultipleKeyPressed                    = false;
	///Enable this if you want the device to handle end and send key.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // INVALID<br>
	///    Default MIDP 2 NOKIA    : false    // INVALID<br>
	///    Default MIDP 2 SPRINT   : false    // INVALID<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
	/// </code>
	public static final boolean useNativeKeyBehavior                           = false;    // Invalid option for this plateform : MIDP2
	///Enable this if you want the volume key codes below  assigned to k_volumeUp and k_volumeDown.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useVolumeKeys                                  = false;
	///Keycode for the volume Up key.
	/// 
	/// <code>
	///    Default MIDP 1          : 0<br>
	///    Default MIDP 1 NOKIA    : 0<br>
	///    Default MIDP 1 SPRINT   : 0<br>
	///    Default MIDP 2          : 0<br>
	///    Default MIDP 2 NOKIA    : 0<br>
	///    Default MIDP 2 SPRINT   : 0<br>
	///    Default DOJA            : 0<br>
	///    Default BLACKBERRY      : 0<br>
	///    Default WIPI_JAVA       : 0<br>
	///    Default DOCOMO          : 0<br>
	///    Default MIDP 2 SOFTBANK : 0<br>
	///    Default DOCOMO_STAR     : 0<br>
	///    Default MIDP 2 DANGER   : 0<br>
	///    Default MIDP 2 SAMSUNG  : 0<br>
	/// </code>
	public static final int     keycodeVolumeUp                                = 0;
	///Keycode for the volume Down key.
	/// 
	/// <code>
	///    Default MIDP 1          : 0<br>
	///    Default MIDP 1 NOKIA    : 0<br>
	///    Default MIDP 1 SPRINT   : 0<br>
	///    Default MIDP 2          : 0<br>
	///    Default MIDP 2 NOKIA    : 0<br>
	///    Default MIDP 2 SPRINT   : 0<br>
	///    Default DOJA            : 0<br>
	///    Default BLACKBERRY      : 0<br>
	///    Default WIPI_JAVA       : 0<br>
	///    Default DOCOMO          : 0<br>
	///    Default MIDP 2 SOFTBANK : 0<br>
	///    Default DOCOMO_STAR     : 0<br>
	///    Default MIDP 2 DANGER   : 0<br>
	///    Default MIDP 2 SAMSUNG  : 0<br>
	/// </code>
	public static final int     keycodeVolumeDown                              = 0;
	///Default number of frames that the simulated key will remain pressed.
	/// &warning The simulated holding can be changed afterwards (see setSimulatedNavDuration() method).
	/// 
	/// <code>
	///    Default MIDP 1          : 2<br>
	///    Default MIDP 1 NOKIA    : 2<br>
	///    Default MIDP 1 SPRINT   : 2<br>
	///    Default MIDP 2          : 2<br>
	///    Default MIDP 2 NOKIA    : 2<br>
	///    Default MIDP 2 SPRINT   : 2<br>
	///    Default DOJA            : 2<br>
	///    Default BLACKBERRY      : 2<br>
	///    Default WIPI_JAVA       : 2<br>
	///    Default DOCOMO          : 2<br>
	///    Default MIDP 2 SOFTBANK : 2<br>
	///    Default DOCOMO_STAR     : 2<br>
	///    Default MIDP 2 DANGER   : 2<br>
	///    Default MIDP 2 SAMSUNG  : 2<br>
	/// </code>
	public static final int     simulatedHoldDuration                          = 2;
	///Fixed point base (default = 8, but 6 is a good candidate).
	/// &warning Some math function may fail if value is too big (due to  Fixed Point overflow)
	/// 
	/// <code>
	///    Default MIDP 1          : 8<br>
	///    Default MIDP 1 NOKIA    : 8<br>
	///    Default MIDP 1 SPRINT   : 8<br>
	///    Default MIDP 2          : 8<br>
	///    Default MIDP 2 NOKIA    : 8<br>
	///    Default MIDP 2 SPRINT   : 8<br>
	///    Default DOJA            : 8<br>
	///    Default BLACKBERRY      : 8<br>
	///    Default WIPI_JAVA       : 8<br>
	///    Default DOCOMO          : 8<br>
	///    Default MIDP 2 SOFTBANK : 8<br>
	///    Default DOCOMO_STAR     : 8<br>
	///    Default MIDP 2 DANGER   : 8<br>
	///    Default MIDP 2 SAMSUNG  : 8<br>
	/// </code>
	public static final int     math_fixedPointBase                            = 8;
	///If set to true, flash backLight instead of using vibration when calling Vibrate(int duration) function
	/// &warning Vibration and Lights are not supported often, make sure your device support on or the other.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // Warning, be carefull<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // Warning, be carefull<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean useFlashLightInsteadOfVibration                = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Enable / Disable simulatedNavigation by default.
	/// &warning The simulated navigation can be activated afterwards (see setSimulatedNavigation() method).
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // INVALID<br>
	///    Default MIDP 2 NOKIA    : false    // INVALID<br>
	///    Default MIDP 2 SPRINT   : false    // INVALID<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
	/// </code>
	public static final boolean simulateNavigation                             = false;    // Invalid option for this plateform : MIDP2
	///Angle fixed point base (angle goes from 0 to (1<<Math_AngleFixedPointBase), 0=0, and (1<<Math_AngleFixedPointBase)=360
	/// &warning Some math function may fail if value is too big (due to  Fixed Point overflow)
	/// 
	/// <code>
	///    Default MIDP 1          : 8<br>
	///    Default MIDP 1 NOKIA    : 8<br>
	///    Default MIDP 1 SPRINT   : 8<br>
	///    Default MIDP 2          : 8<br>
	///    Default MIDP 2 NOKIA    : 8<br>
	///    Default MIDP 2 SPRINT   : 8<br>
	///    Default DOJA            : 8<br>
	///    Default BLACKBERRY      : 8<br>
	///    Default WIPI_JAVA       : 8<br>
	///    Default DOCOMO          : 8<br>
	///    Default MIDP 2 SOFTBANK : 8<br>
	///    Default DOCOMO_STAR     : 8<br>
	///    Default MIDP 2 DANGER   : 8<br>
	///    Default MIDP 2 SAMSUNG  : 8<br>
	/// </code>
	public static final int     math_angleFixedPointBase                       = 8;
	///Use a table to store Atan result, greatly speed up Atan, but consume (1<<math_fixedPointBase)+1 int to store table
	/// &warning This table must be present in your resources, and will be leaded at the Math initialization.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : true<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : true<br>
	///    Default MIDP 2 SPRINT   : true<br>
	///    Default DOJA            : true<br>
	///    Default BLACKBERRY      : true<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : true<br>
	///    Default MIDP 2 SOFTBANK : true<br>
	///    Default DOCOMO_STAR     : true<br>
	///    Default MIDP 2 DANGER   : true<br>
	///    Default MIDP 2 SAMSUNG  : true<br>
	/// </code>
	public static final boolean math_AtanUseCacheTable                         = true;
	///Used to go around some firmeware bug of UTF-8 byte[] to String convertions.
	/// &warning Slow string creation, use with caution.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean text_useInternalUTF8Converter                  = true;
	///When enabled the tileset backbuffer will be forced to have even dimensions. 
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean tileset_useBugFixImageOddSize                  = false;
	///
	/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
	/// 
	/// <code>
	///    Default MIDP 1          : 256<br>
	///    Default MIDP 1 NOKIA    : 256<br>
	///    Default MIDP 1 SPRINT   : 256<br>
	///    Default MIDP 2          : 20480<br>
	///    Default MIDP 2 NOKIA    : 256<br>
	///    Default MIDP 2 SPRINT   : 256<br>
	///    Default DOJA            : 256<br>
	///    Default BLACKBERRY      : 256<br>
	///    Default WIPI_JAVA       : 256<br>
	///    Default DOCOMO          : 256<br>
	///    Default MIDP 2 SOFTBANK : 256<br>
	///    Default DOCOMO_STAR     : 256<br>
	///    Default MIDP 2 DANGER   : 256<br>
	///    Default MIDP 2 SAMSUNG  : 256<br>
	/// </code>
	public static final int     TMP_BUFFER_SIZE                                = 20480;
	///
	/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
	/// 
	/// <code>
	///    Default MIDP 1          : 256<br>
	///    Default MIDP 1 NOKIA    : 256<br>
	///    Default MIDP 1 SPRINT   : 256<br>
	///    Default MIDP 2          : 16384<br>
	///    Default MIDP 2 NOKIA    : 256<br>
	///    Default MIDP 2 SPRINT   : 256<br>
	///    Default DOJA            : 256<br>
	///    Default BLACKBERRY      : 256<br>
	///    Default WIPI_JAVA       : 20 * 1024<br>
	///    Default DOCOMO          : 256<br>
	///    Default MIDP 2 SOFTBANK : 256<br>
	///    Default DOCOMO_STAR     : 256<br>
	///    Default MIDP 2 DANGER   : 256<br>
	///    Default MIDP 2 SAMSUNG  : 256<br>
	/// </code>
	public static final int     PNG_BUFFER_SIZE                                = 16384;
	///The sprite's Images are Dechunked PNG in the resources. The PNGs are recreated in memory at load time and pass to Image.createImage(byte[]); The CRC and some chuncks are removed fro the data to save space. Sprite's image internal format is : Image[].
	/// &warning Sprite export format should be BS_DEFAULT_MIDP1b in the .sprcmd file. Note without sprite_usePrecomputedCRC, you wont be able to have palettes on you sprites.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false    // Warning, be carefull<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean sprite_useDynamicPng                           = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Are the CRC exported with the image's data ? For RGB or Nokia's phone, don’t use this.
	/// &warning Other than midp 1 should leave that to False.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false    // Warning, be carefull<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean sprite_usePrecomputedCRC                       = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Use Bsprite Flags. Must be true if sprite_usePrecomputedCRC.
	/// 
	/// <code>
	///    Default MIDP 1          : true<br>
	///    Default MIDP 1 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 1 SPRINT   : true<br>
	///    Default MIDP 2          : false    // Warning, be carefull<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // Warning, be carefull<br>
	///    Default BLACKBERRY      : false    // Warning, be carefull<br>
	///    Default WIPI_JAVA       : true<br>
	///    Default DOCOMO          : true<br>
	///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
	///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
	/// </code>
	public static final boolean sprite_useBSpriteFlags                         = true;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2
	///Frame's Position and Size are exported in the sprite data at compile time. Its faster but takes more memory. You need to use the BS_FRAME_RECTS flag in the .sprcmd file to export your sprites.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_usePrecomputedFrameRect                 = false;
	///Some phones do not support the creation of Image with Odd width. Using this option the Images will be created with an Even width, by padding the width to the nearest even size.
	/// &warning Also see sprite_useBugFixImageOddSize.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useBugFixImageOddSize                   = false;
	///Simulate drawregion by using drawImage and Intersection of setClip.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useDrawRegionClipping                   = false;
	///For really Old Nokia firmware, some vertical flip where not working, this option will flip the data manually.
	/// &warning Caution, this create a temporary buffer each time a vertical flip is used, this could fragment the memory.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // INVALID<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false    // INVALID<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
	/// </code>
	public static final boolean sprite_useNokia7650DrawPixelBug                = false;    // Invalid option for this plateform : MIDP2
	///For some Nokia firmware, the drawPixels does not use the clipping, this option will simulate the clipping by adjusting the drawPixels parameters.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false    // INVALID<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false    // INVALID<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false    // INVALID<br>
	///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
	/// </code>
	public static final boolean sprite_drawPixelClippingBug                    = false;    // Invalid option for this plateform : MIDP2
	///If your device has a bugged fillRoundRect, internal call to this function will be replaced by fillRect.
	/// &warning Direct call to Graphics.fillRoundRect wont be trapped/replaced.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_fillRoundRectBug                        = false;
	///If your device has a bugged drawRegion with flipped,this function will be replaced by drawImage.
	/// &warning Call CreateImage to create flipped image then call drawImage to paint. Like moto w510
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_drawRegionFlippedBug                    = false;
	///On some low end device drawing large string on screen will choke the pipeline because there are too many drawImages, some phone will get a speed increase by doing tiny sleep at the end of each draw strings.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useDrawStringSleep                      = false;
	///Amount on ms to sleep at the end of each DrawString.
	/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
	/// 
	/// <code>
	///    Default MIDP 1          : 1<br>
	///    Default MIDP 1 NOKIA    : 1<br>
	///    Default MIDP 1 SPRINT   : 1<br>
	///    Default MIDP 2          : 1<br>
	///    Default MIDP 2 NOKIA    : 1<br>
	///    Default MIDP 2 SPRINT   : 1<br>
	///    Default DOJA            : 1<br>
	///    Default BLACKBERRY      : 1<br>
	///    Default WIPI_JAVA       : 1<br>
	///    Default DOCOMO          : 1<br>
	///    Default MIDP 2 SOFTBANK : 1<br>
	///    Default DOCOMO_STAR     : 1<br>
	///    Default MIDP 2 DANGER   : 1<br>
	///    Default MIDP 2 SAMSUNG  : 1<br>
	/// </code>
	public static final int     SLEEP_DRAWSTRINGB                              = 1;
	///On some devices Image.createRGBImage the bProccessAlpha is bugged and must be always true, if tis your case, use this option.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useCreateRGBTransparencyBug             = false;
	///Only use this flag when drawRGB can not draw transparency (Ex: Softbank's Samsung MIDP2 phones)
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_drawRGBTransparencyBug                  = false;
	///Only use this flag when getRGB does not read the alpha correctly.
	/// &warning An alternate solution would be to not use getRGB, which can sometimes be accomplished by caching your sprites as int[] and not images. 
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_getRGBTransparencyBug                   = false;
	///Will process the output of createImage (from byte[]) and recreate the Image. Bug fix for A870 where transparency does not get created correctly.  
	/// &warning Note: Since this blits the Image first to an Image initialized with FF00FF any ALPHA SPRITES will no longer show correctly. 
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false    // INVALID<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useA870CreateRGBTransparencyFix         = false;
	///If you device cant load a lot (count, not size) of images, this option will use only one big image for all the modules.
	/// &warning You need sprite_useModuleXY or sprite_useModuleXYShort. You also need to modify your export flags in Aurora.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useSingleImageForAllModules             = false;
	///Keep a version of the Single Image flipped in X and another flipped in Y.
	/// &warning Takes a lot of memory. 需要和 sprite_useSingleImageForAllModules一起使用
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useCacheFlipXY                          = false;
	///Keep the sprite's images as int[]. Use only if your device's drawRGB is really fast. Can also be usefull if you want to manipulate the sprite's images, to create FX.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useCacheRGBArrays                       = false;
	///Simular to sprite_useCacheRGBArrays, but will not force ALL sprites to use int[], rather it will allow the programmer to select which sprites should use which (by default will use Images not int[]'s) by using SetUseCacheRGB. Note: This does not make sense if sprite_useCacheRGBArrays is TRUE because that will force ALL sprites to use int[]'s.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false    // Warning, be carefull<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useManualCacheRGBArrays                 = true;
	///When using sprite_ModuleMapping_useModuleImages and NOT using sprite_useCacheRGBArrays, transform images directly on the image's data in int[]. Then create a temporary image to draw it on screen. (Only applies if sprite_useCacheRGBArrays is False)
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_RGBArraysUseDrawRGB                     = true;
	///On some phones , if the clip region not in screen , drawRGB cause game crash.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean useDrawRGBClipNotOnScreenBugFix                = false;
	///Has same exact function as useDrawPartialRGB.
	/// 
	/// <code>
	///    Default MIDP 1          : false    // INVALID<br>
	///    Default MIDP 1 NOKIA    : false    // INVALID<br>
	///    Default MIDP 1 SPRINT   : false    // INVALID<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false    // INVALID<br>
	///    Default DOCOMO          : false    // INVALID<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false    // INVALID<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useTruncatedRGBBuffer                   = false;
	///Support LZMA decompression for resources and streams.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : true<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false    // INVALID<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean pack_supportLZMADecompression                  = true;

	///Use/Keep each module position in a Single Image. Needed if you are using :  sprite_useExternImage or sprite_useSingleImageForAllModules.  Module's positions will be stored in byte[].
	/// &warning You also need to modify your export flags in Aurora.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	/// 如果要打开 BS_MODULES_XY 那么 sprite_useBSpriteFlags   必须开，并且sprite_useModuleXY也必须开
	/// 如果要打开 BS_MODULES_XY_SHORT 那么 sprite_useBSpriteFlags   必须开，并且sprite_useModuleXY也必须开
	/// 下面的 sprite_useModuleXYShort
	public static final boolean sprite_useModuleXY                             = false;


	///Use/Keep each module position in a Single Image. Needed if you are using :  sprite_useExternImage or sprite_useSingleImageForAllModules.  Module's positions will be stored in short[].
	/// &warning You also need to modify your export flags in Aurora.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	/// 如果要打开 BS_MODULES_XY_SHORT 那么 sprite_useBSpriteFlags   必须开，并且sprite_useModuleXYShort也必须开
	public static final boolean sprite_useModuleXYShort                        = true;
	

	///Use short instead of byte to keep module sizes. For modules larger than -128-127. This option will take more memory.
	/// &warning You also need to modify your export flags in Aurora.
	/// 
	/// <code>
	///    Default MIDP 1          : false<br>
	///    Default MIDP 1 NOKIA    : false<br>
	///    Default MIDP 1 SPRINT   : false<br>
	///    Default MIDP 2          : false<br>
	///    Default MIDP 2 NOKIA    : false<br>
	///    Default MIDP 2 SPRINT   : false<br>
	///    Default DOJA            : false<br>
	///    Default BLACKBERRY      : false<br>
	///    Default WIPI_JAVA       : false<br>
	///    Default DOCOMO          : false<br>
	///    Default MIDP 2 SOFTBANK : false<br>
	///    Default DOCOMO_STAR     : false<br>
	///    Default MIDP 2 DANGER   : false<br>
	///    Default MIDP 2 SAMSUNG  : false<br>
	/// </code>
	public static final boolean sprite_useModuleWHShort                        = true;

	
}
