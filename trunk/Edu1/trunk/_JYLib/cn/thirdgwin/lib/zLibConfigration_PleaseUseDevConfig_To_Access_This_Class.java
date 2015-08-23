package cn.thirdgwin.lib;

/** 任何游戏请从本类继承出新类 zLibConfig */

class zLibConfigration_PleaseUseDevConfig_To_Access_This_Class
{

///Use DeltaTime between each frame. eg s_game_frameDT
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
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : true<br>
/// </code>
public static final boolean useFrameDT                                     = true;

///Use this option to remove the call to notifyDestroyed() at the end of the application loop i.e. When your application is quitting
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : false    // INVALID<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false    // INVALID<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : true    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean disableNotifyDestroyed                         = false;


///When set to true a failed assertion will cause the program to exit, otherwise just a message and stacktrace is shown.
/// &warning Only applies to DEBUG release since ASSERTS are not compiled in for RELEASE. 
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
public static final boolean exitOnFailedAssert                             = false;


///When using useSleepInsteadOfYield this value will determine the number of MS the main loop will sleep for when the application is SUSPENDED.
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
public static final int     sleepDurationWhenSuspended                     = 1;

///When InputStream.skip() is slower than a read(), we use a skip buffer by reading the data to skip/discard into a temporary buffer. This buffer as to be as large as the largest chuck to skip in your game. Set to 0 if no skip buffer should be used.
/// 
/// <code>
///    Default MIDP 1          : 256<br>
///    Default MIDP 1 NOKIA    : 256<br>
///    Default MIDP 1 SPRINT   : 256<br>
///    Default MIDP 2          : 256<br>
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
public static final int     pack_skipbufferSize                            = 256;


///Once a pack or a subpack is loaded into memory, keep it there. It will never get freed. Usually usefull for some Nokia S60 phone which leaks alot when loading a file.
/// &warning Carefull, this option takes a lot of memory.
/// 
/// <code>
///    Default MIDP 1          : false    // Warning, be carefull<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false    // Warning, be carefull<br>
///    Default MIDP 2          : false    // Warning, be carefull<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false    // Warning, be carefull<br>
///    Default DOJA            : false    // Warning, be carefull<br>
///    Default BLACKBERRY      : false    // Warning, be carefull<br>
///    Default WIPI_JAVA       : false    // Warning, be carefull<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false    // Warning, be carefull<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false    // Warning, be carefull<br>
/// </code>
public static final boolean pack_keepLoaded                                = false;    // Warning, you should not use/modify this option for this plateform without caution : MIDP2


///File/Package debug trigger. Set this variable to true to debug pack loading.
/// &warning Could be removed, usefull for GLLib debugging.
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
public static final boolean pack_dbgDataAccess                             = false;

///(Docomo only) Use SDCard to store resource.
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
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static final boolean pack_useSDCard                                 = false;    // Invalid option for this plateform : MIDP2


///Set to true if you want to use the pack filesystem when reading from the RMS.
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
public static final boolean rms_usePackRead                                = false;


///Set to true if you want to read/write recordstores owned by other midlets.
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
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean rms_useSharing                                 = false;


///If > 0 then this will be the largest size a RecordStore can be. When saving larger amounts of information it will be split into multiple RecordStores internally. 
/// &warning Used on some devices which have problems with the RMS (W760i, W890i)
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
public static final int     rms_maxRecordSize                              = -1;


///(Docomo only) Use SDCard replace ScratchPad(to debug game have jar size large then limit (1 MB))
/// &warning Only avaliable on debug mode
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
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static final boolean rms_disableScratchPad                          = false;    // Invalid option for this plateform : MIDP2


///Set to true if you work with power of 2 tile size (recommanded).
/// &warning This will only change the speed of the manipulations by using shifts (<< >>) instead of std operators (* /)
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
public static final boolean tileset_useTileShift                           = true;


///How many layer  the tileset with the largest layer count will have. Must be >= 1.
/// 
/// <code>
///    Default MIDP 1          : 4<br>
///    Default MIDP 1 NOKIA    : 4<br>
///    Default MIDP 1 SPRINT   : 4<br>
///    Default MIDP 2          : 5<br>
///    Default MIDP 2 NOKIA    : 4<br>
///    Default MIDP 2 SPRINT   : 4<br>
///    Default DOJA            : 4<br>
///    Default BLACKBERRY      : 4<br>
///    Default WIPI_JAVA       : 4<br>
///    Default DOCOMO          : 4<br>
///    Default MIDP 2 SOFTBANK : 4<br>
///    Default DOCOMO_STAR     : 4<br>
///    Default MIDP 2 DANGER   : 4<br>
///    Default MIDP 2 SAMSUNG  : 4<br>
/// </code>
public static final int     tileset_maxLayerCount                          = 5;


///Use short instead of byte to keep tileset indexes. For indexes larger than -128-127. This option will take more memory.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean tileset_useIndexAsShort                        = false;


///Will enable the user to set tileset alpha effects. 
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
public static final boolean tileset_usePixelEffects                        = true;


///When enabled will set the clip to the tile size and location before painting each tile. 
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
public static final boolean tileset_useClip                                = true;


///When enabled will cause frames painted to backbuffer to be clipped to the area of the back buffer that has been updated last.
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
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
public static final boolean tileset_useLayerLastUpdatedArea                = false;


///How many palette the sprite with the largest palette count will have. Must be >= 1.
/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
/// 
/// <code>
///    Default MIDP 1          : 16<br>
///    Default MIDP 1 NOKIA    : 16<br>
///    Default MIDP 1 SPRINT   : 16<br>
///    Default MIDP 2          : 16<br>
///    Default MIDP 2 NOKIA    : 16<br>
///    Default MIDP 2 SPRINT   : 16<br>
///    Default DOJA            : 16<br>
///    Default BLACKBERRY      : 16<br>
///    Default WIPI_JAVA       : 16<br>
///    Default DOCOMO          : 15<br>
///    Default MIDP 2 SOFTBANK : 16<br>
///    Default DOCOMO_STAR     : 15<br>
///    Default MIDP 2 DANGER   : 16<br>
///    Default MIDP 2 SAMSUNG  : 16<br>
/// </code>
public static final int     MAX_SPRITE_PALETTES                            = 16;


///
/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
/// 
/// <code>
///    Default MIDP 1          : 3<br>
///    Default MIDP 1 NOKIA    : 3<br>
///    Default MIDP 1 SPRINT   : 3<br>
///    Default MIDP 2          : 3<br>
///    Default MIDP 2 NOKIA    : 3<br>
///    Default MIDP 2 SPRINT   : 3<br>
///    Default DOJA            : 8<br>
///    Default BLACKBERRY      : 3<br>
///    Default WIPI_JAVA       : 3<br>
///    Default DOCOMO          : 3<br>
///    Default MIDP 2 SOFTBANK : 3<br>
///    Default DOCOMO_STAR     : 3<br>
///    Default MIDP 2 DANGER   : 3<br>
///    Default MIDP 2 SAMSUNG  : 3<br>
/// </code>
public static final int     MAX_FLIP_COUNT                                 = 3;


///Set this value if you want to force the alt buffer to some other size. If negative (default) the size used will simply be TMP_BUFFER_SIZE. 
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
public static final int     TMP_ALT_BUFFER_SIZE                            = -1;


///
/// &warning Needs a rename, since they are not following the remaining's of the GLLib's format.
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
public static final int     FIXED_PRECISION                                = 8;

///Use this option if you will never use flip or rotations when drawing your sprites.
/// &warning Cannot use this with sprite_useTransfRot or sprite_useTransfFlip or sprite_useTransMappings.
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
public static final boolean sprite_useLoadImageWithoutTransf               = false;


///When drawing a Rotated Frame/Module, apply a translation to keep Frame/Module Cohesion.
/// &warning Cannot use this with sprite_useLoadImageWithoutTransf.
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
public static final boolean sprite_useTransfRot                            = true;


///When drawing a flipped Frame/Module, apply a translation to keep Frame/Module Cohesion.
/// &warning Cannot use this with sprite_useLoadImageWithoutTransf.
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
///    Default WIPI_JAVA       : true<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useTransfFlip                           = true;


///Will correctly support all flag combinations across frame modules and animation frames. The flags and positions will all be correctly resolved.
/// &warning Cannot use this with sprite_useLoadImageWithoutTransf. Not tested with hyperframes, if you find a problem report it. 
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
public static final boolean sprite_useTransMappings                        = true;


///This flag is used for optimized heap size. Only create the smallest buffer need to be used at one time.
/// &warning Slow framerate in some case.
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
public static final boolean sprite_useDynamicTransformBuffer               = false;

///Don’t use clipping when drawing sprites with drawRegion by manupulating the call's parameters.
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
public static final boolean sprite_fpsRegion                               = false;

///In PaintModule, check to see if the the module is in part in the current clipping selection. Use on device with slow drawImage.
/// 这个貌似是用来做是否可见的检查的
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
public static final boolean sprite_useSkipFastVisibilityTest               = true;

///If you have special way to create, load, access the module's images. Usefull when working with half transparency.
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
public static final boolean sprite_useExternImage                          = true;

///如果没有从外部设定sprite的_main_images数组，则使用该值作为默认的image数组大小
public static final int DEFAULT_EX_IMG_NUM									= 3;				

///Set to FALSE only if one of your sprites is larger than 64KB, the data will then be stored in a int[] instead.
/// 
/// <code>
///    Default MIDP 1          : true<br>
///    Default MIDP 1 NOKIA    : true<br>
///    Default MIDP 1 SPRINT   : true<br>
///    Default MIDP 2          : false<br>
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
public static final boolean sprite_useModuleDataOffAsShort                 = false;


///Keep the sprite's images to cache pool. cache image to pool when draw it on screen.Use on device with small memory and slow createImage.
/// &warning Be used with sprite_ModuleMapping_useModuleImages
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
///    Default MIDP 2 DANGER   : true<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useCachePool                            = false;


///Uses the temp buffer as a cache during a PaintFrame call. The state of what the temp buffer holds is stored and if that module is requested again the temp buffer will be used. So this flag only would effect sprites that are 1) not cached and 2) have consecutive instances of the same module within a frame.
/// &warning Needs to be used with sprite_ModuleMapping_useModuleImages and currently is not supported for the Nokia_UI (although this can be added). Also, ifsprite_debugModuleUsage is set to TRUE a message will be output each time the cache is hit.
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
public static final boolean sprite_useSingleFModuleCache                   = false;


///8888 Palette color encoding.
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
public static final boolean sprite_usePixelFormat8888                      = false;


///0888 Palette color encoding.
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
public static final boolean sprite_usePixelFormat0888                      = false;


///4444 Palette color encoding.
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
public static final boolean sprite_usePixelFormat4444                      = false;


///1555 Palette color encoding.
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
public static final boolean sprite_usePixelFormat1555                      = true;


///0565 Palette color encoding.
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
public static final boolean sprite_usePixelFormat0565                      = false;


///0332 Palette color encoding.
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
public static final boolean sprite_usePixelFormat0332                      = false;


///Support for Image containg 2 colors or less.
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
public static final boolean sprite_useEncodeFormatI2                       = false;


///Support for Image containg 4 colors or less.
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
public static final boolean sprite_useEncodeFormatI4                       = false;


///Support for Image containg 16 colors or less.
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
public static final boolean sprite_useEncodeFormatI16                      = true;


///Support for Image containg 64 colors or less. Using RLE compression.
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
public static final boolean sprite_useEncodeFormatI64RLE                   = true;


///Support for Image containg 127 colors or less. Using RLE compression.
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
public static final boolean sprite_useEncodeFormatI127RLE                  = false;


///Support for Image containg 256 colors or less.  Using RLE compression.
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
public static final boolean sprite_useEncodeFormatI256RLE                  = true;


///Support for Image containg 256 colors or less.
/// &warning Bugged.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useEncodeFormatI256                     = true;


///Support for Image containg alpha channel (8 values of less) and 32 colors or less.
/// &warning Be used for loading a bsprite exported with flag A8I32
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
public static final boolean sprite_useEncodeFormatA8_I32                   = false;


///Support for Image containg alpha channel (32 values or less) and 8 colors or less.
/// &warning Be used for loading a bsprite exported with flag A32I8
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
public static final boolean sprite_useEncodeFormatA32_I8                   = false;


///Support for Image containg alpha channel and 64 colors or less.
/// &warning Be used for loading a bsprite exported with flag A256I64
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
public static final boolean sprite_useEncodeFormatA256_I64                 = false;


///Support for Image containg alpha channel and 128 colors or less.
/// &warning Be used for loading a bsprite exported with flag A256I128
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
public static final boolean sprite_useEncodeFormatA256_I128                = false;


///Support for Image containg alpha channel and 256 colors or less.
/// &warning Be used for loading a bsprite exported with flag A256I256
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
public static final boolean sprite_useEncodeFormatA256_I256                = false;


///Support for Image containg alpha channel and 64 colors or less.  Using RLE compression.
/// &warning Be used for loading a bsprite exported with flag A256I64RLE
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
public static final boolean sprite_useEncodeFormatA256_I64RLE              = false;


///Support for Image containg alpha channel and 127 colors or less.  Using RLE compression.
/// &warning Be used for loading a bsprite exported with flag A256I127RLE
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
public static final boolean sprite_useEncodeFormatA256_I127RLE             = false;


///Support for Image containg alpha channel and 256 colors or less.  Using RLE compression.
/// &warning Be used for loading a bsprite exported with flag A256I256RLE
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
public static final boolean sprite_useEncodeFormatA256_I256RLE             = false;


///Use this flag to load the Sprite exported by Aurora with .fft config file. NonInterlaced sprites has a good compression
/// &warning You have to use .fft export config with Aurora.
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
public static final boolean sprite_useNonInterlaced                        = false;




///If the sprite have more than 256 FModules per sprite you can extend to 1024 using this flag.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useIndexExFmodules                      = true;


///If the sprite has more than 1024 modules and your frames need to reference these, you can extend to 2^16 using this flag. This option will only use shorts for those sprites that were exported this way (less heap, slower speed).SpriteEditor 0.1.0.5不支持这个设定
/// &warning Only supported if not using sprite_useSingleArrayForFMAF.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_allowShortIndexForFModules              = true;

///If frames in your sprite contain more than 256 modules use this to extend to 2^16. This option will only use shorts for those sprites that were exported this way (less heap, slower speed).
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_allowShortNumOfFModules                 = true;

///Keep internal position of a module Inside a frame in short[] instead of byte[]. 
/// &warning Cant be used with sprite_useSingleArrayForFMAF.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useFMOffShort                           = true;


///Use if Aurora exported the number of frame modules as 1 byte.
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
public static final boolean sprite_alwaysBsNfm1Byte                        = false;


///Use if Aurora exported the Palette to use for each module in a frame (fmodule).
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
public static final boolean sprite_useFMPalette                            = false;


///Use if your sprite contains some Hyperframe. Hyperframes are a way to put a frame inside another frame.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useHyperFM                              = false;


///Use if Aurora did not export start offsets for frame modules, they are one after the other.
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
public static final boolean sprite_alwaysBsNoFmStart                       = false;


///Use if Aurora exported animation frames offsets as short[] instead of byte[].
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
public static final boolean sprite_useAfOffShort                           = true;

///If the sprite have more than 256 Frames per sprite you can extend to 1024 using this flag.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useIndexExAframes                       = true;


///If the sprite has more than 1024 frames and your animations need to reference these, you can extend to 2^16 using this flag. This option will only use shorts for those sprites that were exported this way (less heap, slower speed).SpriteEditor 0.1.0.5不支持这个设定
/// &warning Only supported if not using sprite_useSingleArrayForFMAF.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_allowShortIndexForAFrames               = true;


///If animations in your sprite contain more than 256 frames use this to extend to 2^16. This option will only use shorts for those sprites that were exported this way (less heap, slower speed).
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_allowShortNumOfAFrames                  = true;

///Use for recording drawing operations.
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
public static final boolean sprite_useOperationRecord                      = false;


///Use for recording drawing operations.
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : true<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : true<br>
///    Default BLACKBERRY      : false<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useOperationRect                        = true;


///Use for recording drawing operations. And to mark flipped module for treatement, usefull in MIDP1 to load sprite with flipped module.
/// 
/// <code>
///    Default MIDP 1          : true<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : true<br>
///    Default MIDP 2          : false<br>
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
public static final boolean sprite_useOperationMark                        = false;


///Use for recording drawing operations.
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : true<br>
///    Default BLACKBERRY      : false<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useModuleUsageFromSprite                = false;

///When drawing a string, the '\' char is used to changed palette, it has to be followed by a number correcponding to the palette number.
/// &warning Soon to be replaced by new Font system.
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
public static final boolean sprite_fontBackslashChangePalette              = true;


///Disable effect of sprite_fontBackslashChangePalette flags but still keep change palette effect. 
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
public static final boolean sprite_fontDisableUnderlineBoldEffect          = false;


///Disable effect of sprite_fontBackslashChangePalette flags. 
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
//public static final boolean sprite_fontDisableBackslashChangePaletteEffect = false;


///Support sprite/module resize.
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
public static final boolean sprite_useResize                               = false;


///GenPalette generates a palette based on another palette. You have to select a Flag : 1 << Flag.  Available flags are :  PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK
/// 
/// <code>
///    Default MIDP 1          : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 1 NOKIA    : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 1 SPRINT   : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2          : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2 NOKIA    : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2 SPRINT   : 1 << ASprite.PAL_GREY<br>
///    Default DOJA            : 2 << ASprite.PAL_GREY<br>
///    Default BLACKBERRY      : 1 << ASprite.PAL_GREY<br>
///    Default WIPI_JAVA       : 1 << ASprite.PAL_GREY<br>
///    Default DOCOMO          : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2 SOFTBANK : 1 << ASprite.PAL_GREY<br>
///    Default DOCOMO_STAR     : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2 DANGER   : 1 << ASprite.PAL_GREY<br>
///    Default MIDP 2 SAMSUNG  : 1 << ASprite.PAL_GREY<br>
/// </code>
public static final int     sprite_useGenPalette                           = 1 << zSprite.PAL_GREY;


///Using this flag a module can be an image, a rectangle(MD_RECT), a solid Rectangle (MD_FILL_RECT), a Marker (MD_MARKER).
/// &warning You also need to modify your export flags in Aurora.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useMultipleModuleTypes                  = true;


///The color of the special module is stored as a byte instead of an int.
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
public static final boolean sprite_useModuleColorAsByte                    = false;


///The RECT API call will render with width and height +1. We now adjust for this so that what you see in Aurora is exactly what you see in-game. However, for backwards compatibility you may want to revert to the old incorrect implementation.
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
public static final boolean sprite_useOriginalDrawRect                     = false;


///If you have exported your sprite with frame rect, you have to load them.
/// &warning You also need to modify your export flags in Aurora.
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useFrameRects                           = true;


///Don’t load the frame rects. Save some memory.
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
public static final boolean sprite_alwaysBsSkipFrameRc                     = false;


///Your sprite's frames are using collision box. 
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
public static final boolean sprite_useFrameCollRC                          = false;


///Use this option to disable calls to System.gc() everywhere in the sprite management.
/// 
/// <code>
///    Default MIDP 1          : false<br>
///    Default MIDP 1 NOKIA    : false<br>
///    Default MIDP 1 SPRINT   : false<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false<br>
///    Default DOJA            : false<br>
///    Default BLACKBERRY      : true<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : true<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_useDeactivateSystemGc                   = false;


///On X1 phone , createImage slow , but drawRGB with alpha  is malfunctioned .Use this option to replace drawRGB to drawImage for PFX feature.
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
public static final boolean sprite_useCreateImageOnDrawPFX                 = false;


///Use to debug sprite engine and management.
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
public static final boolean sprite_debugLoading                            = false;


///Use to debug sprite engine and management.
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
public static final boolean sprite_debugErrors                             = false;


///Use to debug sprite engine and management.
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
public static final boolean sprite_debugUsedMemory                         = false;


///Use to debug sprite engine and management.
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
public static final boolean sprite_debugModuleUsage                        = false;


///Use to debug sprite engine and management.
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
public static final boolean sprite_debugColision                           = false;


///When enabled, a variable s_debugSkipPaintModule will control if PaintModule will simply exit. GLLib will toggle this value when 0 is pressed. 
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
public static final boolean sprite_debugTogglePaintModule                  = false;


///Use new text rendering code
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
//public static final boolean sprite_newTextRendering                        = true;


///When using WrapTextB to break text into lines, if this is set to TRUE the formatting (palette, bold, underline) will be saved PER line so that it displays correctly if you display something not starting from the first line.
/// &warning Requires extra short per line so max number of lines will be smaller once this is enabled, edit MAX_WRAP_TEXT_INFO to support more lines).
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
//public static final boolean sprite_bufferTextPageFormatting                = false;


///Size of buffer to use for text paging.
/// &warning Number of lines supported will be: (K-1)/2 if sprite_bufferTextPageFormatting = F, and (K-1)/3 if its true.
/// 
/// <code>
///    Default MIDP 1          : 100<br>
///    Default MIDP 1 NOKIA    : 100<br>
///    Default MIDP 1 SPRINT   : 100<br>
///    Default MIDP 2          : 300<br>
///    Default MIDP 2 NOKIA    : 100<br>
///    Default MIDP 2 SPRINT   : 100<br>
///    Default DOJA            : 100<br>
///    Default BLACKBERRY      : 100<br>
///    Default WIPI_JAVA       : 100<br>
///    Default DOCOMO          : 100<br>
///    Default MIDP 2 SOFTBANK : 100<br>
///    Default DOCOMO_STAR     : 100<br>
///    Default MIDP 2 DANGER   : 100<br>
///    Default MIDP 2 SAMSUNG  : 100<br>
/// </code>
public static final int     MAX_WRAP_TEXT_INFO                             = 300;


///Set to TRUE if you are blending palettes on the fly. This will rebuild the blended palettes cache on a need-by-need basis (when painting if the module is not at correct blend it will be re-cached).
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
public static final boolean sprite_useDynamicPaletteBlendingCache          = false;


///use Bitmap font to draw string instead of sprite font. It use to draw UTF-8 character like Japanese character.
/// &warning Note: Example in https://terminus.mdc.gameloft.org/vc/gllib_sampleapplication/Softbank/Sharp_3G/trunk
/// 
/// <code>
///    Default MIDP 1          : false    // INVALID<br>
///    Default MIDP 1 NOKIA    : false    // INVALID<br>
///    Default MIDP 1 SPRINT   : false    // INVALID<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false    // INVALID<br>
///    Default DOJA            : false    // INVALID<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false    // INVALID<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static  boolean sprite_useBitmapFont                           = false;


///use cache pool for bitmap font
/// &warning sprite_useBitmapFont must be true
/// 
/// <code>
///    Default MIDP 1          : false    // INVALID<br>
///    Default MIDP 1 NOKIA    : false    // INVALID<br>
///    Default MIDP 1 SPRINT   : false    // INVALID<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false    // INVALID<br>
///    Default DOJA            : false    // INVALID<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false    // INVALID<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static final int sprite_useBitmapFontCachePool                  = 0;


///use to break the line by width (like chinese language which not have space character)
/// &warning sprite_useBitmapFont must be true
/// 
/// <code>
///    Default MIDP 1          : false    // INVALID<br>
///    Default MIDP 1 NOKIA    : false    // INVALID<br>
///    Default MIDP 1 SPRINT   : false    // INVALID<br>
///    Default MIDP 2          : false<br>
///    Default MIDP 2 NOKIA    : false<br>
///    Default MIDP 2 SPRINT   : false    // INVALID<br>
///    Default DOJA            : false    // INVALID<br>
///    Default BLACKBERRY      : false    // INVALID<br>
///    Default WIPI_JAVA       : false    // INVALID<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static final boolean sprite_useBitmapFontFreeWrap                   = true;


///Use System font to draw string instead of sprite font. It use to draw UTF-8 character like Japanese character.
/// &warning Note: Although only present on the DOCOMO platform we can easily allow this on other platforms. If you have a need for this contact the mailinglist. 
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
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false    // INVALID<br>
///    Default DOCOMO_STAR     : false    // INVALID<br>
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
//public static final boolean sprite_useSystemFont                           = false;    // Invalid option for this plateform : MIDP2


///Enables having the cache create an extra slot containing an alpha gradient over each module. 
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
public static final boolean sprite_useCacheEffectReflection                = false;


///When true, PaintFrame will check to see if the frame it is supposed to paint is cached, and if so will use it for the painting. 
/// &warning Currently painting cached frames will ignore any PFX that may be set. We can add this support if needed though.
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
public static final boolean sprite_useCachedFrames                         = false;


///When true, the caching of frames will be done by rendering to a int[] instead of an image. Use this when the sprite you are caching frames for is an alpha sprite. You must also enable sprite_allowPixelArrayGraphicswhen using this. 
/// &warning Currently rendering is very slow due to processing alpha and should only be used during loading time. 
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
public static final boolean sprite_useCacheFramesWithCustomBlit            = false;


///When enabled, the user will be able to set a graphics context of the form: int[]. 
/// &warning Currently only DrawRGB makes use of this as well as PaintModule. We can fill in support are needed. 
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
public static final boolean sprite_allowPixelArrayGraphics                 = false;


///When enabled, sprites will support enabling a feature where by setting a mask you can control which modules get painted within a frame.
/// &warning This requires that you setup markers in your source sprite and then set the masks accordingly.
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
public static final boolean sprite_allowModuleMarkerMasking                = false;


///When TRUE once an animation ends it will still draw on Render.
/// &warning This is added for backwards compatibility. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean sprite_animStillDrawWhenOver                   = true;


///When TRUE a animation frame time of 0 will be treated as never ending.
/// &warning This is added for backwards compatibility. 
/// 
/// <code>
///    Default MIDP 1          : true<br>
///    Default MIDP 1 NOKIA    : true<br>
///    Default MIDP 1 SPRINT   : true<br>
///    Default MIDP 2          : false<br>
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
public static final boolean sprite_animDurationZeroAsInfinite              = false;


///don not cache one pixel module
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
///    Default MIDP 2 DANGER   : false    // INVALID<br>
///    Default MIDP 2 SAMSUNG  : false    // INVALID<br>
/// </code>
public static final boolean sprite_removeOnePixelModule                    = false;    // Invalid option for this plateform : MIDP2


///When true each modules image data will be held in a unique array. This is useful if you want to release only some modules image data.
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
public static final boolean sprite_useDoubleArrayForModuleData             = false;

///Maximum node count the algorythm could visit when trying to find a path. 
/// &warning This should be at least your collision map's width multiplied by its height.
/// 
/// <code>
///    Default MIDP 1          : 400<br>
///    Default MIDP 1 NOKIA    : 400<br>
///    Default MIDP 1 SPRINT   : 400<br>
///    Default MIDP 2          : 400<br>
///    Default MIDP 2 NOKIA    : 400<br>
///    Default MIDP 2 SPRINT   : 400<br>
///    Default DOJA            : 400<br>
///    Default BLACKBERRY      : 400<br>
///    Default WIPI_JAVA       : 400<br>
///    Default DOCOMO          : 400<br>
///    Default MIDP 2 SOFTBANK : 400<br>
///    Default DOCOMO_STAR     : 400<br>
///    Default MIDP 2 DANGER   : 400<br>
///    Default MIDP 2 SAMSUNG  : 400<br>
/// </code>
public static final int     pathfinding_MaxNode                            = 400;


///Use this option to see all the pathfinding information in the console.
/// &warning Debug only.
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
public static final boolean pathfinding_Debug                              = false;


///If a readable buffer will be required by some effect, then this determines if we will allocate one manually. If FALSE, then an alternate way to read rendered pixels must exist (for NokiaUI for example we can read the screen directly.)
/// &warning If TRUE then if a pixel effect exists that requires a readable buffer pfx_Init will allocate a screen buffer!
/// 
/// <code>
///    Default MIDP 1          : false    // INVALID<br>
///    Default MIDP 1 NOKIA    : false    // INVALID<br>
///    Default MIDP 1 SPRINT   : false    // INVALID<br>
///    Default MIDP 2          : true<br>
///    Default MIDP 2 NOKIA    : true<br>
///    Default MIDP 2 SPRINT   : true<br>
///    Default DOJA            : false<br>
///    Default BLACKBERRY      : false<br>
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : true<br>
///    Default MIDP 2 SOFTBANK : true<br>
///    Default DOCOMO_STAR     : true<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : true<br>
/// </code>
public static final boolean pfx_useScreenBuffer                            = false;


///Enables blurring of the whole screen.
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useFullScreenEffectBlur                    = true;


///Enables blending the current screen with some static buffered image. 
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useFullScreenEffectBlend                   = true;


///Enables a solid color additive effect over the whole screen.
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useFullScreenEffectAdditive                = true;


///Enables a solid color subtractive effect over the whole screen.
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useFullScreenEffectSubtractive             = true;


///Enables a solid color multiplicative effect over the whole screen.
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useFullScreenEffectMultiplicative          = true;


///Enables regional glow effect.
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useEffectGlow                              = true;


///Will force the glow effect to limit itself to only the primary pixel buffer. Only enable this if you are certain your game does not use the 2nd buffer in any other case. It will also decreae the max possible area you can use for the glow effect by half.
/// &warning Only applies if GLOW effect is enabled. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_glowUseOneBuffer                           = true;


///Enables additive effects to be used on sprites. 
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectAdditive                    = true;


///Enables multiplicative effects to be used on sprites. 
/// &warning REQUIRES READABLE BUFFER
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectMultiplicative              = true;


///Enables grayscale effect to be used on sprites. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectGrayscale                   = true;


///Enables shine effect to be used on sprites. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectShine                       = true;


///Enables blending effect (alpha level) to be used on sprites. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectBlend                       = false;


///Enables scaling effect to be used on sprites. 
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectScale                       = true;


///Enables reflection (alpha gradien) effect on sprites.
/// &warning NOT IMPLEMENTED YET
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
///    Default WIPI_JAVA       : false<br>
///    Default DOCOMO          : false<br>
///    Default MIDP 2 SOFTBANK : false<br>
///    Default DOCOMO_STAR     : false<br>
///    Default MIDP 2 DANGER   : false    // Warning, be carefull<br>
///    Default MIDP 2 SAMSUNG  : false<br>
/// </code>
public static final boolean pfx_useSpriteEffectReflection                  = true;


/**
*指定特定的颜色为透明色，一般用于object的阴影。
*/
public static final int JY_special_alpha_color = 0x1A1500;
/**
*指定特定的颜色为透明色的透明度。
*/
public static final int JY_special_alpha_depth = 0x66000000;


/**
*数据包的后缀名，这个名字要与打包时候设定的一致，用pack的时候有用。
*/
public static final String JY_DATA_SPECIAL_END = ".png";


/**
 * 关掉菜单等游戏中的部分半透明效果。
 */
public static final boolean JY_USE_ALPHA_EFFECT = false;

/**
 * 丢弃掉地图的阴影层。
 */
public static final boolean JY_DISCARD_SHADOW_TILESET = false;

public static final boolean JY_CLOSE_VIBRATE_EFFECT = false;

///**
// * 关掉付费功能
// */
//public static final boolean JY_CLOSE_PAY = false;

/**
 * 使用demo
 */
public static final boolean JY_USE_DEMO = false;

public static final int JY_TRANSLATE_X = 0;
public static final int JY_TRANSLATE_Y = 0;

/**
 * 声音用player数组，把声音全部cache成player
 * 当JY_SOUND_CACHE_PLAYER和JY_SOUND_CACHE_DATA全部是false的时候，就是单player，每次播放声音都重新load资源。
 */
public static final boolean JY_SOUND_CACHE_PLAYER = false;

/**
 * 声音用一个player，但是把生成声音的int数据cache出来，省掉load数据的时间。
 * 当JY_SOUND_CACHE_PLAYER和JY_SOUND_CACHE_DATA全部是false的时候，就是单player，每次播放声音都重新load资源。
 */
public static final boolean JY_SOUND_CACHE_DATA = false;

/**
 * 对Asprite.load()函数加锁，防止重入。这个标志设定是否在发生重入的时候等待，还是Raise Exception
 * */
public static final boolean JY_RAISE_LOADEXCEPTION_WHEN_REENTER = false;

/**
 * 试验：针对Bitmapfont在绘制单独字符的时候，是否检查Clip区.(检查的原因是，BitmapFont会无视Clip去创建和缓冲字符，造成不必要的大浪费
 * */
public static final boolean JY_BITMAP_FONT_CHECK_CLIP = !true;

public static final boolean pfx_enabled = false;

/*
paintModule时画三角形的数据是否用到别处（不画三角形）
*/
public static final boolean JY_USE_TRIANGLE_MODULE_INFO = true;

public static final boolean JY_USE_SIMPLE_QUICK_SEARCH = true;

/** ANIMPLAYER 缓冲 */
public static final int ZANIMPLAYER_POOL_ENABLE		= 0;
public static final int ZANIMPLAYER_POOL_SIZE 		= 20;

/** 是否显示错误信息 */
public static final boolean ENABLE_ERR_INFO		= true;
/** 是否显示调试信息信息 */
public static final boolean ENABLE_DBG_INFO		= true;
/** 测试耗时,性能测试 */
public static final boolean ENABLE_PROFILE	= false;

}

/// &}
