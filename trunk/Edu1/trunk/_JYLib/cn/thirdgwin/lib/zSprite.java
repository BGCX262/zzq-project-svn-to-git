package cn.thirdgwin.lib;


/**
 * AnimPlayer
 * @author MaNing
 * 只包括了动画的绘制和更新部分
 */



	import javax.microedition.lcdui.*;


public class zSprite
{
	public String Filename;
	private int id = -1;
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}

// ASprite_DrawLine.jpp

//------------------------------------------------------------------------------
/// Bresenham's Line-Drawing Algorithm
/// &param g The graphics context
/// &param x1 X coordinate at the start of the line
/// &param y1 Y coordinate at the start of the line
/// &param x2 X coordinate at the end of the line
/// &param y2 Y coordinate at the end of the line
/// &param color The color of the line
/// &param radius The radius of the line
/// &param initial Initial offset
//------------------------------------------------------------------------------
static void DrawLine( int x1, int y1, int x2, int y2, int color, int radius, int initial)
{
	DrawLine( GLLib.g,x1,y1,x2,y2,color,radius,initial);
}
static void DrawLine( Graphics g, int x1, int y1, int x2, int y2, int color, int radius, int initial)
{
	int		dx,		//deltas
			dy,
			dx2,	//scaled deltas
			dy2,
			ix = 0,		//increase rate on the x axis
			iy = 0,		//increase rate on the y axis
			err,	//the error term
			i;		//looping variable

	int old_color = g.getColor();

	g.setColor(color);

	// identify the first pixel
	int currX = x1;
	int currY = y1;

	// difference between starting and ending points
	dx = x2 - x1;
	dy = y2 - y1;

	// calculate direction of the vector and store in ix and iy
	if (dx >= 0)
		ix = 1;

	if (dx < 0)
	{
		ix = -1;
		dx = Math.abs(dx);
	}

	if (dy >= 0)
		iy = 1;

	if (dy < 0)
	{
		iy = -1;
		dy = Math.abs(dy);
	}

	// scale deltas and store in dx2 and dy2
	dx2 = dx * 2;
	dy2 = dy * 2;

	int dist = 0;
	int maxdist = (5 - initial) * radius;

	if (dx > dy)	// dx is the major axis
	{
		// initialize the error term
		err = dy2 - dx;

		for (i = 0; i <= dx; i++)
		{
			if (dist == maxdist)
			{
				if (! DevConfig.sprite_fillRoundRectBug)
				{
                    
					    g.fillRoundRect(currX - radius, currY - radius, radius * 2, radius * 2, 30, 30);

				}
				else
				{
					g.fillRect(currX - radius, currY - radius + 1, radius << 1, (radius << 1) - 2);
					g.fillRect(currX - radius + 1, currY - radius, (radius << 1) - 2, radius << 1);
				}
			}
			dist += ix * ix + iy * iy;
			if (dist > 6 * radius)
				dist = 0;

			//g.fillRect(currX, currY, 1, 1);
			if (err >= 0)
			{
				err -= dx2;
				currY += iy;
			}
			err += dy2;
			currX += ix;
		}
	}
	
	else 		// dy is the major axis
	{
		// initialize the error term
		err = dx2 - dy;

		for (i = 0; i <= dy; i++)
		{
			if (dist == maxdist)
			{
				if (! DevConfig.sprite_fillRoundRectBug)
				{
                    
					    g.fillRoundRect(currX - radius, currY - radius, radius * 2, radius * 2, 30, 30);

				}
				else
				{
					g.fillRect(currX - radius, currY - radius + 1, radius << 1, (radius << 1) - 2);
					g.fillRect(currX - radius + 1, currY - radius, (radius << 1) - 2, radius << 1);
				}
			}

			dist += ix * ix + iy * iy;
			if (dist > 6 * radius)
				dist = 0;
			//g.fillRect(currX, currY, 1, 1);
			if (err >= 0)
			{
				err -= dy2;
				currX += ix;
			}
			err += dx2;
			currY += iy;
		}
	}

	g.setColor(old_color);

} 

//--------------------------------------------------------------------------------------------------------------------
 

// ASprite_Flags.jpp - zSprite Flags
//--------------------------------------------------------------------------------------------------------------------

	//////////////////////////////////////////////////

	final static short BSPRITE_v003			    = (short)0x03DF; // NON supported version
	final static short BSPRITE_v004			    = (short)0x04DF; // NON supported version
	final static short BSPRITE_v005			    = (short)0x05DF; // supported version
	final static short SUPPORTED_VERSION 	    = BSPRITE_v005;

	//////////////////////////////////////////////////
	// BSprite flags

	final static int BS_MODULES			        = (1 << 0);
	final static int BS_MODULES_XY		        = (1 << 1);
	final static int BS_MODULES_IMG		        = (1 << 2);
	final static int BS_MODULE_IMAGES_TC_BMP    = (1 << 3);
	final static int BS_MODULES_WH_SHORT	    = (1 << 4);		// export w, h for each module as short
	final static int BS_MODULES_XY_SHORT	    = (1 << 5);		// export x, y for each module as short
	final static int BS_MODULES_USAGE		    = (1 << 6);	    // export for each module which transformations are used in the sprite
    final static int BS_IMAGE_SIZE_INT          = (1 << 7);     // module size will be stored on int, for larger module
	final static int BS_FRAMES			        = (1 << 8);
	final static int BS_FM_OFF_SHORT	        = (1 << 10);    // export fm offsets as shorts


	final static int BS_NFM_1_BYTE		        = (1 << 11);    // export nfm as byte

	final static int BS_SKIP_FRAME_RC   	    = (1 << 12);    // do not export frame rect
	final static int BS_FRAME_COLL_RC   	    = (1 << 13);	// export collision rect
	final static int BS_FM_PALETTE		        = (1 << 14);	// export palette used by the module
	final static int BS_FRAME_RECTS			    = (1 << 15);	// export frame rects
	final static int BS_ANIMS		    	    = (1 << 16);
	final static int BS_NO_AF_START			    = (1 << 17);	// do not export start of AFrames
	final static int BS_AF_OFF_SHORT    	    = (1 << 18);    // export af offsets as shorts


	final static int BS_NAF_1_BYTE	            = (1 << 19);    // export naf as byte


	final static int BS_FM_INDEX_SHORT	        = (1 << 20);    // export frame module ID's as shorts
	final static int BS_AF_INDEX_SHORT	        = (1 << 21);    // export animation frame ID's as shorts

	final static int BS_MODULE_IMAGES_FX	    = (1 << 23);	// export encoded images for each module (flipped horizontally)
	final static int BS_MODULE_IMAGES	        = (1 << 24);
	final static int BS_PNG_CRC			        = (1 << 25);
	final static int BS_KEEP_PAL		        = (1 << 26);
	final static int BS_TRANSP_FIRST	        = (1 << 27);
	final static int BS_TRANSP_LAST		        = (1 << 28);
	final static int BS_SINGLE_IMAGE	        = (1 << 29);
	final static int BS_MULTIPLE_IMAGES	        = (1 << 30);
	final static int BS_GIF_HEADER			    = (1 << 31);	// export gif header instead of palette

	final static int BS_DEFAULT_DOJA	        = (BS_MODULES | BS_FRAMES | BS_ANIMS| BS_MODULE_IMAGES | BS_GIF_HEADER | BS_MODULES_USAGE);
	final static int BS_DEFAULT_MIDP2	        = (BS_MODULES | BS_FRAMES | BS_ANIMS | BS_MODULE_IMAGES);
	final static int BS_DEFAULT_NOKIA	        = (BS_DEFAULT_MIDP2);
	final static int BS_DEFAULT_MIDP1	    	= (BS_MODULES | BS_MODULES_XY | BS_FRAMES | BS_ANIMS);
	final static int BS_DEFAULT_MIDP1b	    	= (BS_MODULES | BS_FRAMES | BS_ANIMS | BS_MODULE_IMAGES | BS_PNG_CRC);
	final static int BS_DEFAULT_MIDP1c	    	= (BS_MODULES | BS_MODULES_XY | BS_FRAMES | BS_ANIMS | BS_SINGLE_IMAGE);

	//////////////////////////////////////////////////

	final static short PIXEL_FORMAT_8888		= (short)0x8888;
	final static short PIXEL_FORMAT_4444		= (short)0x4444;
	final static short PIXEL_FORMAT_1555		= (short)0x5515;
	final static short PIXEL_FORMAT_0565		= (short)0x6505;
	final static short PIXEL_FORMAT_0332		= (short)0x3203;
	final static short PIXEL_FORMAT_8808		= (short)0x8808;

	//////////////////////////////////////////////////

	final static short ENCODE_FORMAT_I2			= 0x0200;
	final static short ENCODE_FORMAT_I4			= 0x0400;
//	final static short ENCODE_FORMAT_I8			= 0x0800;
	final static short ENCODE_FORMAT_I16		= 0x1600;
//	final static short ENCODE_FORMAT_I16MP		= 0x16??;
//	final static short ENCODE_FORMAT_I32		= 0x3200;
//	final static short ENCODE_FORMAT_I64		= 0x6400;
//	final static short ENCODE_FORMAT_I128		= 0x2801;
	final static short ENCODE_FORMAT_I256		= 0x5602;
//	final static short ENCODE_FORMAT_I127_		= 0x2701;
	final static short ENCODE_FORMAT_I64RLE		= 0x64F0;
	final static short ENCODE_FORMAT_I127RLE	= 0x27F1;
	final static short ENCODE_FORMAT_I256RLE	= 0x56F2;

	//new data format for alpha channel
	final static short ENCODE_FORMAT_A8_I32         = (short)0x05A3;
	final static short ENCODE_FORMAT_A32_I8         = (short)0x03A5;

	final static short ENCODE_FORMAT_A256_I64       = (short)0xA064;
	final static short ENCODE_FORMAT_A256_I128      = (short)0xA128;
	final static short ENCODE_FORMAT_A256_I256      = (short)0xA562;
	final static short ENCODE_FORMAT_A256_I64RLE	= (short)0xA640;
	final static short ENCODE_FORMAT_A256_I127RLE	= (short)0xA127;
	final static short ENCODE_FORMAT_A256_I256RLE	= (short)0xA256;

	//////////////////////////////////////////////////
	// Frames/Anims flags...

	final static byte FLAG_FLIP_X	= 0x01;
	final static byte FLAG_FLIP_Y	= 0x02;
	final static byte FLAG_ROT_90	= 0x04;

	final static byte FLAG_USER0	= 0x10; // user flag 0
	final static byte FLAG_USER1	= 0x20; // user flag 1

	final static byte FLAG_HYPER_FM	= 0x10; // Hyper FModule, used by FModules

	// Index extension...
	final static int FLAG_INDEX_EX_MASK = 0xC0; // 11000000, bits 6, 7
	final static int INDEX_MASK			= 0x03FF; // max 1024 values
	final static int INDEX_EX_MASK		= 0x0300;
	final static int INDEX_EX_SHIFT 	= 2;

	//////////////////////////////////////////////////
	// flags passed as params...

	final static byte FLAG_OFFSET_FM = 0x10;
	final static byte FLAG_OFFSET_AF = 0x20;

	//////////////////////////////////////////////////


    final static int OPERATION_DRAW			= 0;
	final static int OPERATION_COMPUTERECT	= 1;
	final static int OPERATION_RECORD		= 2;
	final static int OPERATION_MARK			= 3;


    final static int RESIZE_NONE			= 0;
	final static int RESIZE_CREATERGB	    = 1;
	final static int RESIZE_DRAW_ON_MUTABLE	= 2;
    final static int RESIZE_NOT_CACHED	    = 3;


    final static int MD_IMAGE		        = 0;
	final static int MD_RECT			    = 1;
	final static int MD_FILL_RECT	        = 2;
	final static int MD_ARC			        = 3;
	final static int MD_FILL_ARC		    = 4;
	final static int MD_MARKER		    	= 5;
	final static int MD_TRIANGLE	        = 6;
	final static int MD_FILL_TRIANGLE	    = 7;

	//////////////////////////////////////////////////

	final static int MAX_TRANSFORMATION_FLAGS	= 8;

	//////////////////////////////////////////////////
	// Generic Flags for a sprite
	final static int FLAG_USE_CACHE_RGB     = (1<<0);		// For use when sprite_useCacheRGBArrays = false, and sprite_useManualCacheRGBArrays = true, when set sprite will use int[] for cache
	final static int FLAG_USE_MODULE_MASK   = (1<<1);       // For use when sprite_allowModuleMarkerMasking = true, when set module masking will be enable for this sprite

	//////////////////////////////////////////////////
	// Constants for Alpha Property
	final static int ALPHA_NONE             = 0;
	final static int ALPHA_TRANSPARENT      = 1;
	final static int ALPHA_FULL             = 2;


// ASprite_Load.jpp
//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// unload the sprite
	//------------------------------------------------------------------------------
    void unload()
    {
        if (DevConfig.sprite_useModuleXYShort)
        {
            _modules_y_short        = null;
    	    _modules_x_short        = null;
        }
        else
        {
            _modules_x_byte         = null;
            _modules_y_byte         = null;
        }

        if (DevConfig.sprite_useModuleWHShort)
        {
    	    _modules_w_short        = null;
            _modules_h_short        = null;
        }
        else
        {
            _modules_w_byte         = null;
            _modules_h_byte         = null;
        }
        
        _modules_img_index = null;

		// FModules

		if (DevConfig.sprite_allowShortIndexForFModules)
		{
			_fmodules_id_short      = null;
	    	_fmodules_id_byte       = null;
		}
		else

		{
			_fmodules_id_byte       = null;
		}

		// Frames
		if (DevConfig.sprite_allowShortNumOfFModules)
		{
			_frames_nfm_short       = null;
	    	_frames_nfm_byte        = null;
		}
		else
		{
			_frames_nfm_byte        = null;
		}

		// AFrames
		if (DevConfig.sprite_allowShortIndexForAFrames)
		{
			_aframes_frame_short = null;
			_aframes_frame_byte  = null;
		}
		else

		{
			_aframes_frame_byte  = null;
		}

		// Anims
		if (DevConfig.sprite_allowShortNumOfAFrames)
		{
			_anims_naf_short = null;
			_anims_naf_byte  = null;
		}
		else
		{
			_anims_naf_byte  = null;
		}

    	_frames_fm_start        = null;

    	_frames_rc              = null;
    	_frames_rc_short        = null;

    	_frames_col             = null;
    	_frames_col_short       = null;

    	_frames_rects           = null;
    	_frames_rects_short     = null;
    	_frames_rects_start     = null;
    	_fmodules               = null;
    	_anims_af_start         = null;
    	_aframes                = null;

		if (_transp != null){ for (int _i = _transp.length - 1; _i >=0 ; _i--){ _transp[_i] = null; } _transp = null; };

        if (DevConfig.sprite_useNokiaUI)
	    {
			if (_pal_short != null){ for (int _i = _pal_short.length - 1; _i >=0; _i--){ _pal_short[_i] = null; } _pal_short = null; };
	    }
	    else if (DevConfig.sprite_useCreateRGB)
	    {
			if (_pal_int != null){ for (int _i = _pal_int.length - 1; _i >=0 ; _i--){ _pal_int[_i] = null; } _pal_int = null; };
	    }

		FreeCachedModules();

    
    	_main_image             = null;
    	_PNG_packed_PLTE_CRC    = null;
    	_PNG_packed_tRNS_CRC    = null;
    	_PNG_packed_IHDR_CRC    = null;
    	_PNG_packed_IDAT_ADLER  = null;
    	_PNG_packed_IDAT_CRC    = null;

    }


    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Load the sprite from the given byte array
	/// &param file A byte array containing a sprite file
	/// &param offset The offset inside the array at which to start reading
	//------------------------------------------------------------------------------
	public void Load(byte[] file, int offset)
	{
		Load( file, offset, 0xFFFFFF, 1, null);
	}

    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Load the sprite from the given byte array with flags
	/// &param file A byte array containing a sprite file
	/// &param offset The offset inside the array at which to start reading
	/// &param pal_flags Palette flags
	/// &param tr_flags Transformation flags
	//------------------------------------------------------------------------------
	void Load(byte[] file, int offset, int pal_flags, int tr_flags)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			Load(file, offset, pal_flags, tr_flags, null);
		}
	}

    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Load the sprite from the given byte array and associated image
	/// &param file A byte array containing a sprite file
	/// &param offset The offset inside the array at which to start reading
	/// &param sprImage The image associated with the sprite
	//------------------------------------------------------------------------------
	void Load(byte[] file, int offset, Image sprImage)
	{
		if (DevConfig.sprite_useExternImage)
		{
			Load(file, offset, 0xFFFFFF, 1, sprImage);
		}
	}

    //--------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------


	private int LoadModules( int offset, byte[] file )
    {
        // Modules...
		_nModules = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
    	//Guijun accelerate begin
    	int _nModules = this._nModules;
    	short[] _modules_x_short = null;
    	short[] _modules_y_short = null;
    	byte[] _modules_x_byte;
    	byte[] _modules_y_byte;
    	byte[] _module_types;
    	byte[] _modules_img_index;
    	byte[] _module_colors_byte;
    	int[] _module_colors_int;
		short[] _modules_w_short;
		short[] _modules_h_short;
		byte[] _modules_w_byte;
		byte[] _modules_h_byte;

    	
		if (DevConfig.sprite_debugLoading){Utils.Dbg("nModules = " + _nModules);;};

		if (_nModules > 0)
		{
			if (DevConfig.sprite_useModuleXYShort && (!DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
			{
				_modules_x_short = this._modules_x_short = new short[_nModules];
				_modules_y_short = this._modules_y_short = new short[_nModules];
			}
			else if (DevConfig.sprite_useModuleXY && (!DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_XY) != 0)))
			{
				_modules_x_byte = this._modules_x_byte = new byte[_nModules];
				_modules_y_byte = this._modules_y_byte = new byte[_nModules];
			}

			/**只有开了BS_MODULES_IMG的才能使用多图*/
            if(  (DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_IMG) != 0) )
			{
            	_modules_img_index = this._modules_img_index = new byte[_nModules];
			}
			
			if (DevConfig.sprite_useModuleWHShort)
			{
				_modules_w_short = this._modules_w_short  = new short[_nModules];
				_modules_h_short = this._modules_h_short  = new short[_nModules];
			}
			else
			{
				_modules_w_byte = this._modules_w_byte  = new byte[_nModules];
				_modules_h_byte = this._modules_h_byte  = new byte[_nModules];
			}
			
            //Tmp info for the arcs : StartAngle Angle
            int     tmpExtraInfoCount   = 0;
            int     tmpExtraInfoSize    = 0;
            short[][] tmpExtraInfo       = null;


            if( DevConfig.sprite_useMultipleModuleTypes )
            {
            	_module_types = this._module_types = new byte[_nModules];

                if( DevConfig.sprite_useModuleColorAsByte )
                	_module_colors_byte = this._module_colors_byte = new byte[ _nModules ];
                else
                	_module_colors_int = this._module_colors_int  = new int[ _nModules ];
            }

        	boolean bLoadAModuleColor       = false;
        	boolean bLoadAModulePosition    = false;
        	boolean bLoadAModuleSize        = false;
        	boolean bLoadAModuleArc         = false;
        	boolean bLoadAModuleTriangle    = false;
        	boolean bLoadModuleImgIndex 	= (DevConfig.sprite_useBSpriteFlags && ((_bs_flags & BS_MODULES_IMG) != 0));

			for (int i = 0; i < _nModules; i++)
			{
			    bLoadAModuleArc         = false;
			    bLoadAModuleTriangle    = false;

                if( DevConfig.sprite_useMultipleModuleTypes )
                {
                    if( ((file[offset]) & 0xFF) == 0x00 )
					{
                        offset += (1);

                        _module_types[ i ] = MD_IMAGE;

       					if (bLoadModuleImgIndex)
        					{
        						_modules_img_index[i] = (file[offset++]);
        					}
                       
			        	bLoadAModuleColor = false;
			        	bLoadAModulePosition = true;
			        	bLoadAModuleSize = true;
					}
					else if( ((file[offset]) & 0xFF) == 0xFF )
                    {
                        offset += (1);

			            _module_types[ i ] = MD_RECT;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xFE )
                    {
                        offset += (1);

                        _module_types[ i ] = MD_FILL_RECT;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xFD )
                    {
                        offset += (1);

                        _module_types[ i ] = MD_MARKER;

			        	bLoadAModuleColor = false;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xFC )
                    {
                        offset += (1);

                        _module_types[ i ] = MD_ARC;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = true;
			        	bLoadAModuleArc = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xFB)
                    {
                        offset += (1);

                        _module_types[ i ] = MD_FILL_ARC;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = true;
			        	bLoadAModuleArc = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xFA)
                    {
                        offset += (1);
                        _module_types[ i ] = MD_TRIANGLE;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = false;
			        	bLoadAModuleTriangle = true;
                    }
					else if( ((file[offset]) & 0xFF) == 0xF9)
                    {
                        offset += (1);
                        _module_types[ i ] = MD_FILL_TRIANGLE;

			        	bLoadAModuleColor = true;
			        	bLoadAModulePosition = false;
			        	bLoadAModuleSize = false;
			        	bLoadAModuleTriangle = true;
                    }
					else
					{
		            	if(!(false))Utils.DBG_PrintStackTrace(false, "Invalid module type : " + ((file[offset]) & 0xFF) + "   module &" + i);;
					}
                }
                else
                {
					if( ((file[offset]) & 0xFF) == 0x00 )
					{
                        offset += (1);

                       // _module_types[ i ] = MD_IMAGE; //dont need to keep it, they should all be MD_IMAGE

			        	bLoadAModuleColor = false;
			        	bLoadAModulePosition = true;
			        	bLoadAModuleSize = true;
					}
					else
					{
		            	if(!(false))Utils.DBG_PrintStackTrace(false, "Invalid module type : " + ((file[offset]) & 0xFF) + "   module &" + i);;
					}
				}

				if( DevConfig.sprite_useMultipleModuleTypes )
	            {
					if(bLoadAModuleColor)
					{
	                 	
	                        if( DevConfig.sprite_useModuleColorAsByte )
	                        {
	                            _module_colors_byte[ i ] = (file[offset++]);
	                            offset += (3);
	                        }
	                        else
	                        {
	                            _module_colors_int[ i ] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
	                        }
	                 	

					}
				}

			
				if(bLoadAModulePosition)
				{
					if (DevConfig.sprite_useModuleXYShort && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
					{
						_modules_x_short[ i ] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
						_modules_y_short[ i ] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
            		}
            		else if (DevConfig.sprite_useModuleXY && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0)))
            		{
						_modules_x_byte[ i ] = (file[offset++]);
						_modules_y_byte[ i ] = (file[offset++]);
            		}
				}

				if(bLoadAModuleSize)
				{
            		if (DevConfig.sprite_useModuleWHShort)
            		{
            			if(	( DevConfig.sprite_useBSpriteFlags) && ((_bs_flags & BS_MODULES_WH_SHORT) == 0) )
            			{
            				_modules_w_short[i] = (short)(((file[offset++]) & 0xFF));
            				_modules_h_short[i] = (short)(((file[offset++]) & 0xFF));
            			}
            			else
            			{
            				_modules_w_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
            				_modules_h_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
            			}
            		}
            		else // if (zJYLibConfig.sprite_useModuleWHShort)
            		{
            			_modules_w_byte[i] = (file[offset++]);
            			_modules_h_byte[i] = (file[offset++]);
            		}
				}

				if( DevConfig.sprite_useMultipleModuleTypes )
				{
					if(bLoadAModuleArc)
					{
					    if(tmpExtraInfo == null)
					    {
					        tmpExtraInfo = new short[_nModules][];
					    }

					    short[] aShort = new short[2];

	                    aShort[0] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    aShort[1] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    tmpExtraInfo[i] = aShort;

	                    tmpExtraInfoCount++;
	                    tmpExtraInfoSize += 2;
					}

					if(bLoadAModuleTriangle)
				    {
					    if(tmpExtraInfo == null)
					    {
					        tmpExtraInfo = new short[_nModules][];
					    }

					    short[] aShort = new short[4];

	                    aShort[0] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    aShort[1] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    aShort[2] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    aShort[3] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	                    tmpExtraInfo[i] = aShort;

	                    tmpExtraInfoCount++;
	                    tmpExtraInfoSize += 4;
				    }
				}
			}

			if( DevConfig.sprite_useMultipleModuleTypes )
            {
	            // Create Extra Module Info
	            // Arc support only for the moment.
	            if(tmpExtraInfoCount > 0)
	            {
	            	short[] _modules_extra_info = this._modules_extra_info     = new short[tmpExtraInfoSize];
	            	short[] _modules_extra_pointer = this._modules_extra_pointer  = new short[tmpExtraInfoCount << 1];

	                int     xCount  = 0;
	                short   xOffset = 0;

	                int     nSize;

	                for(short m = 0; m < _nModules; m++)
	                {
	                    if((_module_types[m] == MD_ARC) || (_module_types[m] == MD_FILL_ARC) )
	                    {
	                        nSize = 2;
	                    }
	                    else if((_module_types[m] == MD_TRIANGLE) || (_module_types[m] == MD_FILL_TRIANGLE) )
	                    {
	                        nSize = 4;
	                    }
	                    else
	                    {
	                        nSize = -1;
	                    }

	                    if(nSize > 0)
	                    {
	                        _modules_extra_pointer[(xCount << 1) + 0] = m;
	                        _modules_extra_pointer[(xCount << 1) + 1] = xOffset;

	                        for(int x = 0; x < nSize; x++)
	                        {
	                            _modules_extra_info[xOffset] = tmpExtraInfo[m][x];

	                            xOffset++;
	                        }

	                        tmpExtraInfo[m] = null;

	                        xCount++;
	                    }
	                }
	            }

	            tmpExtraInfo = null;
			}
		}


		if( DevConfig.sprite_useModuleUsageFromSprite )
		{
            // Module usage
	        // Module images transf. flags that can be applied at runtime:
	        //	final static byte FLAG_FLIP_X	= 0x01;
	        //	final static byte FLAG_FLIP_Y	= 0x02;
	        //	final static byte FLAG_ROT_90	= 0x04;
	        // Order of the pretransformed images loaded for each module:
	        // Values:
	        //	1: an image w/ the respective transf. was loaded;
	        //	0: the module doesn't have an image w/ the respective transformation(s);
	        // 00000001: normal, no transformations
	        // 00000010: Fx
	        // 00000100: Fy
	        // 00001000: Fx Fy
	        // 00010000: R
	        // 00100000: R Fx
	        // 01000000: R Fy
	        // 10000000: R Fx Fy

			if (DevConfig.sprite_debugLoading){Utils.Dbg("Module Usage: offset = " + offset + " _nModules = " + _nModules);;};

			if (!DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_USAGE) != 0))
			{
				_modules_usage = new byte[ _nModules ];

                ;{ System.arraycopy(file, offset, _modules_usage, 0, _nModules ); offset += _nModules; };
/*
				for (int i=0; i<_nModules; i++)
				{
					DBG("Module " + i + " has usage 0x" + Integer.toHexString(_modules_usage[i]));
				}
*/
			}
		} // if (zJYLibConfig.sprite_useModuleUsageFromSprite)

        return offset;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------

	private int LoadFModules( int offset, byte[] file )
    {
        // FModules...
		int nFModules = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
		byte[] _fmodules;
		short[] _fmodules_id_short = null;
		byte[] _fmodules_id_byte = null;
		short[] _fmodules_ox_short;
		short[] _fmodules_oy_short;
		byte[] _fmodules_ox_byte;
		byte[] _fmodules_oy_byte;
		byte[] _fmodules_flags;
		byte[] _fmodules_pal;
		if (DevConfig.sprite_debugLoading){Utils.Dbg("nFModules = " + nFModules);;};

		if (nFModules > 0)
		{
			{

				if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0)))
				   )
				{
					_fmodules_id_short = this._fmodules_id_short = new short[nFModules];
				}
				else

				{
					_fmodules_id_byte = this._fmodules_id_byte  = new byte[nFModules];
				}

				if (DevConfig.sprite_useFMOffShort)
				{
					_fmodules_ox_short = this._fmodules_ox_short = new short[nFModules];
					_fmodules_oy_short = this._fmodules_oy_short = new short[nFModules];
				}
				else
				{
					_fmodules_ox_byte = this._fmodules_ox_byte = new byte[nFModules];
					_fmodules_oy_byte = this._fmodules_oy_byte = new byte[nFModules];
				}

				_fmodules_flags = this._fmodules_flags = new byte[nFModules];

                if (DevConfig.sprite_useFMPalette)
				{
					if ( (!DevConfig.sprite_useBSpriteFlags)||	((_bs_flags & BS_FM_PALETTE) != 0) )
                    {
						_fmodules_pal = this._fmodules_pal = new byte[ nFModules ];
                    }
                }

				for(int i = 0; i < nFModules; i++)
				{
					// Frame Module ID's (BYTE/SHORT)

				if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0)))
				   )
					{
						_fmodules_id_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
					}
					else
					{
						_fmodules_id_byte [i] = (file[offset++]);
					}

					// Frame Module Position's (BYTE/SHORT)
					if (DevConfig.sprite_useFMOffShort)
					{
						if (	(DevConfig.sprite_useBSpriteFlags)
							&&	((_bs_flags & BS_FM_OFF_SHORT) == 0)
							)
						{
							_fmodules_ox_short[i] = (file[offset++]);
							_fmodules_oy_short[i] = (file[offset++]);
						}
						else
						{
							_fmodules_ox_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
							_fmodules_oy_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
						}
					}
					else // if (zJYLibConfig.sprite_useFMOffShort)
					{
						_fmodules_ox_byte[i] = (file[offset++]);
						_fmodules_oy_byte[i] = (file[offset++]);
					}

					// Frame Module Palette (BYTE/0)
					if (DevConfig.sprite_useFMPalette)
					{
						if (	(!DevConfig.sprite_useBSpriteFlags)
							||	((_bs_flags & BS_FM_PALETTE) != 0)
							)
						{
							_fmodules_pal[i] = (file[offset++]);
						}
					}

					// Frame Module flags (BYTE)
					_fmodules_flags[i] = (file[offset++]);
				}
			} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)
		}

        return offset;
    }

    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

	private int LoadFrames( int offset, byte[] file )
    {
		short[] _frames_nfm_short = null;
		byte[] _frames_nfm_byte = null;
		short[] _frames_fm_start;
		short[] _frames_rects_start = null;
		// Rect...
		if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
		{
			int nRects = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

            if((_bs_flags & BS_FM_OFF_SHORT) == 0)
            {
    			_frames_rects 	= new byte [nRects << 2];
	    		;{ System.arraycopy(file, offset, _frames_rects, 0, nRects << 2 ); offset += nRects << 2; };
	        }
	        else
	        {
    			_frames_rects_short = new short [nRects << 2];
	    		offset = readArray2Short( file, offset, _frames_rects_short, 0, nRects << 2, false, false );
	        }
		}

        // Frames...
		int nFrames = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nFrames = " + nFrames + "    & offset " + (offset - 2));;};

		if (nFrames > 0)
		{


			if ((DevConfig.sprite_allowShortNumOfFModules && (((_bs_flags & BS_NFM_1_BYTE) == 0)))
			   )

			{
				_frames_nfm_short = this._frames_nfm_short = new short[nFrames];
			}
			else
			{
				_frames_nfm_byte = this._frames_nfm_byte  = new  byte[nFrames];
			}

			_frames_fm_start = this._frames_fm_start = new short[nFrames];
			short frame_start = 0;

			if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
			{
				_frames_rects_start = this._frames_rects_start = new short[nFrames + 1];
			}
			short frames_rects_offset = 0;

			for (int i = 0; i < nFrames; i++)
			{
				if (DevConfig.sprite_debugLoading){Utils.Dbg("    " + i + "    & offset " + offset);;};


				if (
					(DevConfig.sprite_allowShortNumOfFModules) &&
					((_bs_flags & BS_NFM_1_BYTE) == 0)
				   )

				{
					_frames_nfm_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
				}
				else
				{
					_frames_nfm_byte[i] = (file[offset++]);
				}

				if (DevConfig.sprite_alwaysBsNoFmStart)
				{
					_frames_fm_start[i] = frame_start;

					frame_start += GetFModules(i);
				}
				else
				{
					_frames_fm_start[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
				}

				if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
				{
					if ((_bs_flags & BS_FRAME_RECTS) != 0)
					{
						_frames_rects_start[i] = frames_rects_offset;
						frames_rects_offset += (file[offset++]);
					}
				}
			}

			if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
			{
				_frames_rects_start[_frames_rects_start.length - 1] = frames_rects_offset;
			}


			if (!DevConfig.sprite_alwaysBsSkipFrameRc && (_bs_flags & BS_SKIP_FRAME_RC) == 0)
			{
				if (DevConfig.sprite_usePrecomputedFrameRect)
				{
					// Bound rect for each frame...
					int nFrames4 = nFrames<<2;

                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
    					byte[] _frames_rc = this._frames_rc   = new byte[ nFrames4 ];

    					for( int i = 0; i < nFrames4; i++ )
    					{
    						_frames_rc[ i ] = (file[offset++]);
    					}
    				}
    				else
    				{
    					short[] _frames_rc_short = this._frames_rc_short = new short[ nFrames4 ];

    					for( int i = 0; i < nFrames4; i++ )
    					{
    						_frames_rc_short[ i ] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
    					}
    				}
				}
				else // if (zJYLibConfig.sprite_usePrecomputedFrameRect)
				{
                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
                        offset += (nFrames<<2);
                    }
                    else
                    {
                        offset += (nFrames<<3);
                    }

				} // if (zJYLibConfig.sprite_usePrecomputedFrameRect)
			}
			else // if (! zJYLibConfig.sprite_alwaysBsSkipFrameRc)
			{
				// If the rect was not in the sprite data we will need to pre-computer, but not here, we must first load the image data
				//
			}

			if (DevConfig.sprite_useFrameCollRC)
			{
				if (	(! DevConfig.sprite_useBSpriteFlags)
					||	((_bs_flags & BS_FRAME_COLL_RC) != 0)
					)
				{
					// Collision rect for each frame...
					int nFrames4 = nFrames<<2;

                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
    					byte[] _frames_col = this._frames_col  = new byte[nFrames4];

    					for (int i = 0; i < nFrames4; i++)
    					{
    						_frames_col[i] = (file[offset++]);
    					}
    				}
    				else
    				{
    					short[] _frames_col_short = this._frames_col_short  = new short[nFrames4];

    					for (int i = 0; i < nFrames4; i++)
    					{
    						_frames_col_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
    					}
    				}
				}
			}
		}

        return offset;
    }

    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

	private int LoadAFrames( int offset, byte[] file )
    {
        // AFrames...
		int nAFrames = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nAFrames = " + nAFrames);;};

		if (nAFrames > 0)
		{
			byte[] _aframes;
			short[] _aframes_frame_short = null;
			byte[] _aframes_frame_byte = null;
			byte[] _aframes_time;
			short[] _aframes_ox_short;
			short[] _aframes_oy_short;
			byte[] _aframes_ox_byte;
			byte[] _aframes_oy_byte;
			byte[] _aframes_flags;
			{

				if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0)))
				   )
				{
					_aframes_frame_short = this._aframes_frame_short = new short[nAFrames];
				}
				else

				{
					_aframes_frame_byte = this._aframes_frame_byte  = new byte[nAFrames];
				}

				_aframes_time = this._aframes_time  = new byte[nAFrames];

				if (DevConfig.sprite_useAfOffShort)
				{
					_aframes_ox_short = this._aframes_ox_short = new short[nAFrames];
					_aframes_oy_short = this._aframes_oy_short = new short[nAFrames];
				}
				else
				{
					_aframes_ox_byte = this._aframes_ox_byte = new byte[nAFrames];
					_aframes_oy_byte = this._aframes_oy_byte = new byte[nAFrames];
				}

				_aframes_flags = this._aframes_flags = new byte[nAFrames];

				for(int i = 0; i < nAFrames; i++)
				{

					if (
						(DevConfig.sprite_allowShortIndexForAFrames) &&
						((_bs_flags & BS_AF_INDEX_SHORT) != 0)
					   )
					{
						_aframes_frame_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
					}
					else
					{
						_aframes_frame_byte [i] = (file[offset++]);
					}

					_aframes_time[i]  = (file[offset++]);

					if (DevConfig.sprite_useAfOffShort)
					{
						if (	(DevConfig.sprite_useBSpriteFlags)
							&&	((_bs_flags & BS_AF_OFF_SHORT) == 0)
							)
						{
							_aframes_ox_short[i] = (file[offset++]);
							_aframes_oy_short[i] = (file[offset++]);
						}
						else
						{
							_aframes_ox_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
							_aframes_oy_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
						}
					}
					else // if (zJYLibConfig.sprite_useAfOffShort)
					{
						_aframes_ox_byte[i] = (file[offset++]);
						_aframes_oy_byte[i] = (file[offset++]);
					}

					_aframes_flags[i] = (file[offset++]);
				}
			} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)
		}

        return offset;
    }
    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

	private int LoadAnims( int offset, byte[] file )
    {
        // Anims...
		int nAnims = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
		short[] _anims_naf_short = null;
		byte[] _anims_naf_byte = null;
		short[] _anims_af_start;
		if (DevConfig.sprite_debugLoading){Utils.Dbg("nAnims = " + nAnims);;};

		if (nAnims > 0)
		{


			if ((DevConfig.sprite_allowShortNumOfAFrames && (((_bs_flags & BS_NAF_1_BYTE) == 0)))
			   )

			{
				_anims_naf_short = this._anims_naf_short = new short[nAnims];
			}
			else
			{
				_anims_naf_byte = this._anims_naf_byte  = new  byte[nAnims];
			}

			_anims_af_start = this._anims_af_start = new short[nAnims];
			short af_start  = 0;

			for (int i = 0; i < nAnims; i++)
			{


				if ((DevConfig.sprite_allowShortNumOfAFrames) &&
				    ((_bs_flags & BS_NAF_1_BYTE) == 0)
				   )

				{
					_anims_naf_short[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
				}
				else
				{
					_anims_naf_byte [i] = (file[offset++]);
				}
				{
					_anims_af_start[i] = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
				}
			}
		}

        return offset;
    }
    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Load the sprite from the given byte array and associated image
	/// &param file A byte array containing a sprite file
	/// &param offset The offset inside the array at which to start reading
	/// &param pal_flags Palette flags
	/// &param tr_flags Transformation flags
	/// &param sprImage The image associated with the sprite
	//------------------------------------------------------------------------------
	public static volatile boolean s_isLoading = false;
	void Load(byte[] file, int offset, int pal_flags, int tr_flags, Image sprImage)

	
	{
			try {
				while (s_isLoading)
				{
					if(DevConfig.JY_RAISE_LOADEXCEPTION_WHEN_REENTER)Utils.DBG_PrintStackTrace(false, "Load() reentered");
					Thread.sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		s_isLoading = true;
		try
		{
            if(file == null)
            {
            	if(!(false))Utils.DBG_PrintStackTrace(false, "Cant load sprite, file[] is null");;
				s_isLoading = false;
	            return;
            }

            
			if (DevConfig.sprite_debugLoading){Utils.Dbg("zSprite.Load("+file.length+" bytes, "+offset+")..." + this);;};

			if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
			{
				System.gc();
			}

			short bs_version = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

			if (DevConfig.sprite_debugLoading){Utils.Dbg("  bs_version = 0x" + Integer.toHexString(bs_version));;};

			if(!(bs_version == SUPPORTED_VERSION))Utils.DBG_PrintStackTrace(false, "zSprite.Load.Invalid BSprite version !  GLLib supports only (0x" + Integer.toHexString(SUPPORTED_VERSION) + ")    this sprite version (0x" + Integer.toHexString(bs_version) + ")");;

			_bs_flags =  (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));

			if (DevConfig.sprite_debugLoading){Utils.Dbg("  bs_flags = 0x" + Integer.toHexString(_bs_flags));;};


			AssertFlags(_bs_flags);


			//////////////////////////////

            if( DevConfig.sprite_useNonInterlaced)
            {
                offset = LoadModules_NI ( offset, file );
			    offset = LoadFModules_NI( offset, file );
			    offset = LoadFrames_NI  ( offset, file );
                offset = LoadAFrames_NI ( offset, file );
                offset = LoadAnims_NI   ( offset, file );
            }
			else

            {
			    offset = LoadModules ( offset, file );
			    offset = LoadFModules( offset, file );
			    offset = LoadFrames  ( offset, file );
                offset = LoadAFrames ( offset, file );
                offset = LoadAnims   ( offset, file );
            }

			//////////////////////////////

			if (_nModules <= 0)
			{
				if (DevConfig.sprite_debugErrors)
				{
					System.out.println("WARNING: sprite with num modules = "+_nModules);
				}

				if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
				{
					System.gc();
				}
				s_isLoading = false;
				return;
			}
			//ASSERT(_nModules <= 0, "zSprite.Load. sprite has invalid nb of modules");

            //------------------------------------------------------------------------------------

		
				// Warning LOAD : counld not load SingleImage from Bsprite with PNG format
				if (DevConfig.sprite_useSingleImageForAllModules)
				{
					if ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_SINGLE_IMAGE) != 0))
					{
						offset = LoadData_useSingleImages( offset, file, pal_flags, tr_flags );
					}
				}

                if (DevConfig.sprite_useExternImage)
			    {
				    _main_image    = new Image[ DevConfig.DEFAULT_EX_IMG_NUM ];
				    _main_image[0] = sprImage;
			    }


			//////////////////////////////

			if (DevConfig.sprite_useDynamicPng)
			{
				if (! DevConfig.sprite_usePrecomputedCRC)
				{
//					_pal = null;
					if (DevConfig.sprite_useNokiaUI)
					{
						_pal_short = null;
					}
					else
					{
						_pal_int = null;
					}

				 } // if (! zJYLibConfig.sprite_usePrecomputedCRC) }
			} // if (zJYLibConfig.sprite_useDynamicPng)

			// Precompute Frame Bounding Rects
			if (DevConfig.sprite_usePrecomputedFrameRect && (DevConfig.sprite_alwaysBsSkipFrameRc || (_bs_flags & BS_SKIP_FRAME_RC) != 0))
			{
				int nFrames = GetFrameCount();

				// Make sure we actually have some frames!!
				if (nFrames > 0)
				{
					// Compute frame bounding rect for each frame...
					int index    = 0;

					if((_bs_flags & BS_FM_OFF_SHORT) == 0)
					{
						byte[] _frames_rc = this._frames_rc   = new byte[ nFrames<<2 ];

						for( int i = 0; i < nFrames; i++ )
						{
							GetFrameRect(s_rc, i, 0, 0, 0);
							_frames_rc[index++] = (byte)(s_rc[0]);
							_frames_rc[index++] = (byte)(s_rc[1]);
							_frames_rc[index++] = (byte)(s_rc[2] - s_rc[0]);
							_frames_rc[index++] = (byte)(s_rc[3] - s_rc[1]);
						}
					}
					else
					{
						short[] _frames_rc_short = this._frames_rc_short = new short[ nFrames<<2 ];

						for( int i = 0; i < nFrames; i++ )
						{
							GetFrameRect(s_rc, i, 0, 0, 0);
							_frames_rc_short[index++] = (short)(s_rc[0]);
							_frames_rc_short[index++] = (short)(s_rc[1]);
							_frames_rc_short[index++] = (short)(s_rc[2] - s_rc[0]);
							_frames_rc_short[index++] = (short)(s_rc[3] - s_rc[1]);
						}
					}
				}
			}

			if (DevConfig.sprite_debugLoading){Utils.Dbg("zSprite.Load --- ok!");;};

			if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
			{
				System.gc();
			}
		}
		catch (Exception e)
		{
			if (DevConfig.sprite_debugErrors)
			{
				Utils.Dbg("zSprite.Load()."+e);
			}

			if(!(false))Utils.DBG_PrintStackTrace(false, "Asprite.Load. error while loading sprite : "+ e);;

			if (true)
			{
				e.printStackTrace();
			}
		}
		s_isLoading = false;

	}


	//------------------------------------------------------------------------------
	/// Free all compressed data.
	/// Should be called once cache is built and the compressed data is no longer needed.
	//------------------------------------------------------------------------------
	void FreeCacheData()
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			return;
		}

		if( DevConfig.sprite_useSingleImageForAllModules )
		{
			if (DevConfig.sprite_debugUsedMemory)
			{
				_images_count--;
				_images_size -= GetModuleData(0).length;
			}

			if ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_SINGLE_IMAGE) != 0))
			{
				_pal_data = null;
			}
		} // if (zJYLibConfig.sprite_useSingleImageForAllModules)

		_pal_data           = null;
		_pal_short 			= null;
		_pal_int 			= null;

		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			if (_modules_data_array != null){ for (int _i = 0; _i < _modules_data_array.length; _i++){ _modules_data_array[_i] = null; } _modules_data_array = null; };
		}
		else
		{
    		_modules_data           = null;

	        if( DevConfig.sprite_useModuleDataOffAsShort )
	        {
    	    	_modules_data_off_short = null;
			}
        	else
        	{
            	_modules_data_off_int   = null;
			}
		}

		if(! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
		{
			System.gc();
		}
    }


	//--------------------------------------------------------------------------------------------------------------------

	private byte [] GetModulesUsage(int tr_flags)
	{
		if ((DevConfig.sprite_useDynamicPng) && (! DevConfig.sprite_useModuleUsageFromSprite))
		{
			int   mod, anim, afr, afr_off, afr_idx, afr_end, fr, fmod, fmod_off, fmod_idx, af_flags, fm_flags, tmp_flags;
			int   nModules;
			short nFModules;
			short nFrames;
			short nAFrames;
			short nAnims;

			//if (_modules != null)
			nModules  = _nModules; //(short)(_modules.length / 2);
			//else
			//	nModules = 0;

			{

				if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
				{
					if (_fmodules_id_short != null)
					{
						nFModules = (short)(_fmodules_id_short.length);
					}
					else
					{
						nFModules = 0;
					}
				}
				else

				{
					if (_fmodules_id_byte != null)
					{
						nFModules = (short)(_fmodules_id_byte.length);
					}
					else
					{
						nFModules = 0;
					}
				}
			} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)


			nFrames = (short)GetFrameCount();

			{

				if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0))))
				{
					if (_aframes_frame_short != null)
						nAFrames  = (short)(_aframes_frame_short.length);
					else
						nAFrames  = 0;
				}
				else

				{
					if (_aframes_frame_byte != null)
						nAFrames  = (short)(_aframes_frame_byte.length);
					else
						nAFrames  = 0;
				}
			} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)

			nAnims = (short)GetAnimationCount();

			byte [] mods_usg = new byte[ nModules ];

			//DBG("GetModulesUsage: spriteId = " + spriteId + "  nAnims = " + nAnims + "  nAFrames = " + nAFrames + "   nFrames = " + nFrames + "  nFModules = " + nFModules + " nModules = " + nModules);

			//for (mod = 0; mod < nModules; mod++)
			//	mods_usg[mod] = (byte)(tr_flags);

			for( fr = 0; fr < nFrames; fr++)
				GetModulesUsageInFrame(fr, 0, tr_flags, mods_usg);
			
			short[] _anims_af_start = this._anims_af_start;
			byte[] _aframes = this._aframes;
			byte[] _aframes_flags = this._aframes_flags;
			short[] _aframes_frame_short = this._aframes_frame_short;
			byte[] _aframes_frame_byte = this._aframes_frame_byte;
			for (anim = 0; anim < nAnims; anim++)
			{
				//DBG("anim = " + anim);
				afr_off = _anims_af_start[anim];
				afr_end = GetAFrames(anim);

				for (afr_idx = 0; afr_idx < afr_end; afr_idx++)
				{
					afr = afr_idx + afr_off;
					{
						af_flags = _aframes_flags[afr] & 0xFF;

						if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0))))
						{
							fr   = _aframes_frame_short[afr];
						}
						else

						{
							fr   = _aframes_frame_byte [afr] & 0xFF;
						}
					} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)


					if (DevConfig.sprite_useIndexExAframes && (!DevConfig.sprite_allowShortIndexForAFrames || (((_bs_flags & BS_AF_INDEX_SHORT) == 0))))


					{
						fr |= ((af_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
					}

					GetModulesUsageInFrame(fr, af_flags, tr_flags, mods_usg);
				}
			}

			for (mod = 0; mod < nModules; mod++)
				mods_usg[mod] |= (byte)(tr_flags);

			return mods_usg;
		} // if (zJYLibConfig.sprite_useDynamicPng)
		else
			return null;
	}

    //--------------------------------------------------------------------------------------------------------------------

	private void GetModulesUsageInFrame(int fr, int af_flags, int tr_flags, byte [] mods_usg)
	{
		if ((DevConfig.sprite_useDynamicPng) && (! DevConfig.sprite_useModuleUsageFromSprite))
		{
			int mod, fmod, fmod_off, fmod_idx, fm_flags, tmp_flags, fmod_end;

			//DBG("fr = " + fr);

			fmod_off = _frames_fm_start[fr] & 0xFFFF;


			if ((DevConfig.sprite_allowShortNumOfFModules && (((_bs_flags & BS_NFM_1_BYTE) == 0))))

			{
				fmod_end = _frames_nfm_short[fr];
			}
			else
			{
				fmod_end = _frames_nfm_byte[fr] & 0xFF;
			}

			byte[] _fmodules = this._fmodules;
			byte[] _fmodules_flags = this._fmodules_flags;
			short[] _fmodules_id_short = this._fmodules_id_short;
			byte[] _fmodules_id_byte = this._fmodules_id_byte;
			for (fmod_idx = 0; fmod_idx < fmod_end ; fmod_idx++)
			{
				fmod = fmod_off + fmod_idx;
				{
					fm_flags = _fmodules_flags[ fmod ] & 0xFF;

					if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
					{
						mod  = _fmodules_id_short [ fmod ];
					}
					else

					{
						mod  = _fmodules_id_byte  [ fmod ] & 0xFF;
					}
				} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)
				//DBG("fmod = " + fmod + "   fm_flags = " + (fm_flags & 0x3F));

				if (DevConfig.sprite_useIndexExFmodules && (!DevConfig.sprite_allowShortIndexForFModules || (((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


				{
					mod |= ((fm_flags & FLAG_INDEX_EX_MASK)<<INDEX_EX_SHIFT);
				}

				//DBG("mod = " + mod + "  flags = " + ((af_flags ^ fm_flags) & 0x07));

				for (int k = 0; k < DevConfig.MAX_FLIP_COUNT; k++)
				{
					if ((tr_flags & (1 << k)) != 0)
					{
						tmp_flags = af_flags ^ k;


						//DBG("mod = " + mod + "  set flags = " + ((fm_flags ^ tmp_flags) & 0x07));
						if (mod < mods_usg.length)
							mods_usg[mod] |= 1 << ((fm_flags ^ tmp_flags) & 0x07);
					}
				}
				//DBG("2 mods_usg[" + mod + "] = " + mods_usg[mod]);
			}
		} // if (zJYLibConfig.sprite_useDynamicPng)
	}


//--------------------------------------------------------------------------------------------------------------------
// ASprite_Get.jpp
//--------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------


	//------------------------------------------------------------------------------
	/// Gets the time of a frame in an animation
	/// &param anim The animation to be examined
	/// &param aframe The frame in the animation
	/// &returns The frame time
	//------------------------------------------------------------------------------
	final int GetAFrameTime (int anim, int aframe)
	{
		{
			return _aframes_time[_anims_af_start[anim] + aframe] & 0xFF;
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the flags associated with a frame in an animation
	/// &param anim The animation to be examined
	/// &param aframe The frame in the animation
	/// &returns the flags associated with this frame in the animation
	//------------------------------------------------------------------------------
	final int GetAFrameFlags (int anim, int aframe)
	{
			return (_aframes_flags[(_anims_af_start[anim] + aframe)] & 0x0F);
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Get the number of frames in an animation
	/// &param anim The animation to be examined
	/// &returns The number of frames
	//------------------------------------------------------------------------------
	public final int GetAFrames (int anim)
	{


		if ((DevConfig.sprite_allowShortNumOfAFrames && (((_bs_flags & BS_NAF_1_BYTE) == 0))))

		{
			return _anims_naf_short[anim];
		}
		else
		{
			return _anims_naf_byte [anim] & 0xFF;
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Get the number of modules in a frame
	/// &param frame The frame to be examined
	/// &returns The number of modules
	//------------------------------------------------------------------------------
	final int GetFModules(int frame)
	{


		if ((DevConfig.sprite_allowShortNumOfFModules && (((_bs_flags & BS_NAF_1_BYTE) == 0))))

	{
			return _frames_nfm_short[frame];
		}
		else
		{
			return _frames_nfm_byte[frame] & 0xFF;
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the type of this module
	/// &param module The module to be examined
	/// &returns The type of module this is
	///
	/// &see zJYLibConfig.sprite_useMultipleModuleTypes
	//------------------------------------------------------------------------------
	final int GetModuleType (int module)
    {
		if( DevConfig.sprite_useMultipleModuleTypes )
		{
			return _module_types[module];
		}
		else
		{
			return MD_IMAGE;
		}
    }

	//------------------------------------------------------------------------------
	/// Gets the x coordinate of a module
	/// &param module The module to be examined
	/// &returns The x coordinate
	//------------------------------------------------------------------------------
	final int GetModuleX(int module)
    {
		if (DevConfig.sprite_useModuleXYShort && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
		{
			return _modules_x_short[module]&0xFFFF;
		}
		else if (DevConfig.sprite_useModuleXY && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0)))
		{
			return _modules_x_byte[module]&0xFF;
		}
		else
		{
			return 0;
		}
    }

	//------------------------------------------------------------------------------
	/// Gets the y coordinate of a module
	/// &param module The module to be examined
	/// &returns The y coordinate
	//------------------------------------------------------------------------------
    final int GetModuleY(int module)
    {
		if (DevConfig.sprite_useModuleXYShort && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
		{
			return _modules_y_short[module]&0xFFFF;
		}
		else if (DevConfig.sprite_useModuleXY && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0)))
		{
			return _modules_y_byte[module]&0xFF;
		}
		else
		{
			return 0;
		}
    }

	//------------------------------------------------------------------------------
	/// Gets the width of a module
	/// &param module The module to be examined
	/// &returns The width
	//------------------------------------------------------------------------------
    final int GetModuleWidth(int module)
    {
        if (DevConfig.sprite_useModuleWHShort)
		{
			return _modules_w_short[ module ] & 0xFFFF;
		}
		else
		{
			return _modules_w_byte[ module ] & 0xFF;
		}
    }

	//------------------------------------------------------------------------------
	/// Gets the height of a module
	/// &param module The module to be examined
	/// &returns The height
	//------------------------------------------------------------------------------
    final int GetModuleHeight(int module)
    {
        if (DevConfig.sprite_useModuleWHShort)
		{
			return _modules_h_short[module]&0xFFFF;
		}
		else
		{
			return _modules_h_byte[module]&0xFF;
		}
    }

	//------------------------------------------------------------------------------
	/// To be documented...
	/// &param module
	/// &returns Int
	//------------------------------------------------------------------------------
    final int GetModuleWidthOrg (int module)
    {
		if (DevConfig.sprite_useModuleWHShort)
		{
			return _modules_w_short[ module ] & 0xFFFF;
		}
		else
		{
			return _modules_w_byte[ module ] & 0xFF;
		}
    }

	//------------------------------------------------------------------------------
	/// To be documented...
	/// &param module
	/// &returns Int
	//------------------------------------------------------------------------------
    final int GetModuleHeightOrg (int module)
    {
		if (DevConfig.sprite_useModuleWHShort)
		{
			return _modules_h_short[module]&0xFFFF;
		}
		else
		{
			return _modules_h_byte[module]&0xFF;
		}
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the frame X offset within an animation.
	/// &param v is the aframe ID
	/// &returns Int
	//------------------------------------------------------------------------------
	public final int GetAFramesOX (int v)
	{
		if (DevConfig.sprite_useAfOffShort)
		{
			return _aframes_ox_short[v];
		}
		else
		{
			return _aframes_ox_byte[v];
		}
	}

	//------------------------------------------------------------------------------
	/// Gets the X offset of a frame within an animation.
	/// &param anim is the ID of the animation.
	/// &param aframe is the frame within the anim (aframe).
	/// &returns int which is the pixel offset of this frame within this anim.
	//------------------------------------------------------------------------------
	final int GetAFramesOX (int anim, int aframe)
	{
		{
			int off = _anims_af_start[anim] + aframe;

			return GetAFramesOX(off);
		}
	}

	//------------------------------------------------------------------------------
	/// Sets the X offset of a frame within an animation.
	/// &param anim is the ID of the animation.
	/// &param aframe is the frame within the anim (aframe).
	/// &param x is the X offset for this frame
	//------------------------------------------------------------------------------
	final void SetAFramesOX (int anim, int aframe, int x)
	{
		{
			int off = _anims_af_start[anim] + aframe;

			if (DevConfig.sprite_useAfOffShort)
			{
				_aframes_ox_short[off] = (short)x;
			}
			else
			{
				_aframes_ox_byte[off] = (byte)x;
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Gets the frame Y offset within an animation.
	/// &param v is the aframe ID
	/// &returns Int
	//------------------------------------------------------------------------------
	public final int GetAFramesOY (int v)
	{
		if (DevConfig.sprite_useAfOffShort)
		{
			return _aframes_oy_short[v];
		}
		else
		{
			return _aframes_oy_byte[v];
		}
	}

	//------------------------------------------------------------------------------
	/// Gets the Y offset of a frame within an animation.
	/// &param anim is the ID of the animation.
	/// &param aframe is the frame within the anim (aframe).
	/// &returns int which is the pixel offset of this frame within this anim.
	//------------------------------------------------------------------------------
	final int GetAFramesOY (int anim, int aframe)
	{
		{
			int off = _anims_af_start[anim] + aframe;

			return GetAFramesOY(off);
		}
	}

	//------------------------------------------------------------------------------
	/// Sets the Y offset of a frame within an animation.
	/// &param anim is the ID of the animation.
	/// &param aframe is the frame within the anim (aframe).
	/// &param y is the Y offset for this frame
	//------------------------------------------------------------------------------
	final void SetAFramesOY (int anim, int aframe, int y)
	{
		{
			int off = _anims_af_start[anim] + aframe;

			if (DevConfig.sprite_useAfOffShort)
			{
				_aframes_oy_short[off] = (short)y;
			}
			else
			{
				_aframes_oy_byte[off] = (byte)y;
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// To be documented...
	/// &param v
	/// &returns Int
	//------------------------------------------------------------------------------
	public final int GetFModuleOX (int v)
	{
		if (DevConfig.sprite_useFMOffShort)
		{
			return _fmodules_ox_short[v];
		}
		else
		{
			return _fmodules_ox_byte[v];
		}
	}

	//------------------------------------------------------------------------------
	/// To be documented...
	/// &param v
	/// &returns Int
	//------------------------------------------------------------------------------
	public final int GetFModuleOY (int v)
	{
		if (DevConfig.sprite_useFMOffShort)
		{
			return _fmodules_oy_short[v];
		}
		else
		{
			return _fmodules_oy_byte[v];
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the width of a frame
	/// &param frame The frame to be examined
	/// &returns The frame width
	//------------------------------------------------------------------------------
    public int GetFrameWidth(int frame)
    {
        if( DevConfig.sprite_usePrecomputedFrameRect )
        {
            if((_bs_flags & BS_FM_OFF_SHORT) == 0)
            {
                return (_frames_rc[(frame<<2) + 2]&0xFF);
            }
            else
            {
                return (_frames_rc_short[(frame<<2) + 2]&0xFFFF);
            }
        }
        else
        {
            GetFrameRect( s_rc, frame, 0, 0, 0);
            return s_rc[2] - s_rc[0];
        }
    }

	//------------------------------------------------------------------------------
	/// Gets the height of a frame
	/// &param frame The frame to be examined
	/// &returns The frame height
	//------------------------------------------------------------------------------
    public int GetFrameHeight(int frame)
    {
        if( DevConfig.sprite_usePrecomputedFrameRect )
        {
            if((_bs_flags & BS_FM_OFF_SHORT) == 0)
            {
                return (_frames_rc[(frame<<2) + 3]&0xFF);
            }
            else
            {
                return (_frames_rc_short[(frame<<2) + 3]&0xFFFF);
            }
        }
        else
        {
            GetFrameRect( s_rc, frame, 0, 0, 0);
            return s_rc[3] - s_rc[1];
        }


    }

	//------------------------------------------------------------------------------
	/// Gets the left-most coordinate of this frame relative to its center.
	/// &param frame the frame to be examined
	/// &returns the left-most X coordinate
	///
	/// &see zJYLibConfig.sprite_usePrecomputedFrameRect
	//------------------------------------------------------------------------------
    int GetFrameMinX (int frame)
    {
        if( DevConfig.sprite_usePrecomputedFrameRect )
        {
            if((_bs_flags & BS_FM_OFF_SHORT) == 0)
            {
                return (_frames_rc[(frame<<2) + 0]);
            }
            else
            {
                return (_frames_rc_short[(frame<<2) + 0]);
            }
        }
        else
        {
            GetFrameRect( s_rc, frame, 0, 0, 0);
            return s_rc[0];
        }
    }

    //------------------------------------------------------------------------------
	/// Gets the top-most coordinate of this frame relative to its center.
	/// &param frame The frame to be examined
	/// &returns the top-most Y coordinate
	///
	/// &see zJYLibConfig.sprite_usePrecomputedFrameRect
	//------------------------------------------------------------------------------
	int GetFrameMinY (int frame)
	{
		if( DevConfig.sprite_usePrecomputedFrameRect )
		{
			if((_bs_flags & BS_FM_OFF_SHORT) == 0)
			{
				return (_frames_rc[(frame<<2) + 1]);
			}
			else
			{
				return (_frames_rc_short[(frame<<2) + 1]);
			}
		}
		else
		{
			GetFrameRect( s_rc, frame, 0, 0, 0);
			return s_rc[1];
		}
    }


//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Get the id of a module in a frame
	/// &param frame the frame containing the module
	/// &param fmodule the module to be examined
	/// &returns the module id
	//------------------------------------------------------------------------------
	int GetFrameModule( int frame, int fmodule )
    {
		int off, fm_flags, index;
		{
			off      = _frames_fm_start[ frame ] + fmodule;
			fm_flags = _fmodules_flags [ off ] & 0xFF;


			if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
			{
				index    = _fmodules_id_short[ off ];
			}
			else

			{
				index    = _fmodules_id_byte [ off ] & 0xFF;
			}
		}

		if (DevConfig.sprite_useIndexExFmodules && (!DevConfig.sprite_allowShortIndexForFModules || (((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


		{
			index |= ((fm_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
		}

		return index;
    }

	//------------------------------------------------------------------------------
	/// Get the flags set on a module in a frame
	/// &param frame the frame containing the module
	/// &param fmodule the module to be examined
	/// &returns the flags set on the module
	//------------------------------------------------------------------------------
    int GetFrameModuleFlags(int frame, int fmodule)
    {
		{
			return _fmodules_flags[_frames_fm_start[frame] + fmodule] & 0xFF;
		}
	}

    //------------------------------------------------------------------------------
    /// Retrieve a frame module's palette, if the sprite is exported using
    /// BS_FM_PALETTE flag. If this flag is not used or if zJYLibConfig.sprite_useFMPalette
    /// isn't used, returns current sprite palette.
    ///
    /// &param frame            Frame index
    /// &param fmodule          Frame module index
    ///
    /// &return                 Frame module palette
    ///
    /// &see                    zJYLibConfig&sprite_useFMPalette
    /// &see                    zSprite&BS_FM_PALETTE
    /// &see                    zSprite&GetCurrentPalette()
    //------------------------------------------------------------------------------
    int GetFrameModulePalette(int frame, int fmodule)
    {
        int offset = 0;
        int pal = 0;

        if (DevConfig.sprite_useFMPalette &&
            (!DevConfig.sprite_useBSpriteFlags || (_bs_flags & zSprite.BS_FM_PALETTE) != 0))
        {

            {
                offset = _frames_fm_start[frame] + fmodule;
            }

            pal = _fmodules_pal[offset];
        }
        else
        {
            pal = GetCurrentPalette();
        }

        return pal;
    }

	//------------------------------------------------------------------------------
	/// Gets the X coordinate of a module inside a frame
	/// &param frame The frame to be examined
	/// &param fmodule The module inside the frame
	/// &returns The X coordinate inside the frame
	//------------------------------------------------------------------------------
    final int GetFrameModuleX (int frame, int fmodule)
    {
		{
	        return GetFModuleOX(_frames_fm_start[frame] + fmodule);
		} // if (zJYLibConfig.sprite_useSingleArrayForFMAF)
    }

	//------------------------------------------------------------------------------
	/// Gets the Y coordinate of a module inside a frame
	/// &param frame The frame to be examined
	/// &param fmodule The module inside the frame
	/// &returns The Y coordinate inside the frame
	//------------------------------------------------------------------------------
    final int GetFrameModuleY (int frame, int fmodule)
    {
		{
	        return GetFModuleOY(_frames_fm_start[frame] + fmodule);
		} // if (zJYLibConfig.sprite_useSingleArrayForFMAF)
    }

	//------------------------------------------------------------------------------
	/// Get the width of a module inside a frame
	/// &param frame The frame to be examined
	/// &param fmodule The module inside the frame
	/// &returns The width of the module
	//------------------------------------------------------------------------------
    final int GetFrameModuleWidth(int frame, int fmodule)
    {
		return GetModuleWidth(GetFrameModule(frame, fmodule));
    }

	//------------------------------------------------------------------------------
	/// Get the height of a module inside a frame
	/// &param frame The frame to be examined
	/// &param fmodule The module inside the frame
	/// &returns The height of the module
	//------------------------------------------------------------------------------
    final int GetFrameModuleHeight(int frame, int fmodule)
    {
		return GetModuleHeight(GetFrameModule(frame, fmodule));
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the ID of a frame inside an animation
	/// &param anim The animation to examine
	/// &param aframe The frame inside the animation
	/// &returns The frame ID
	//------------------------------------------------------------------------------
    public int GetAnimFrame(int anim, int aframe)
    {
		int frame;

		{
			int off = _anims_af_start[anim] + aframe;

	        if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0))))
	        {
				frame = _aframes_frame_short[off];
			}
			else

			{
	        	frame = _aframes_frame_byte [off] & 0xFF;
			}


			if (DevConfig.sprite_useIndexExAframes && (!DevConfig.sprite_allowShortIndexForAFrames || (((_bs_flags & BS_AF_INDEX_SHORT) == 0))))


			{
				frame |= ((_aframes_flags[off]&FLAG_INDEX_EX_MASK)<<INDEX_EX_SHIFT);
			}
		}

		return frame;
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the rectangle occupied by an animation frame if it were to be drawn at posX/posY with flags.
	/// &param rc The rectangle array to be populated
	/// &param anim The animation containing the frame
	/// &param aframe The frame to be examined
	/// &param posX The X coordinate to be considered
	/// &param posY The Y coordinate to be considered
	/// &param flags The flags to be considered
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useOperationRect
	//------------------------------------------------------------------------------
	void GetAFrameRect(int[] rc, int anim, int aframe, int posX, int posY, int flags)
	{
        final Graphics g = null;

	//	System.out.println("GetAFrameRect(rc, "+anim+", "+aframe+", "+posX+", "+posY+", 0x"+Integer.toHexString(flags)+")");
		_rectX1 = Integer.MAX_VALUE;
		_rectY1 = Integer.MAX_VALUE;
		_rectX2 = Integer.MIN_VALUE;
		_rectY2 = Integer.MIN_VALUE;
		_operation = OPERATION_COMPUTERECT;

		PaintAFrame( g,  anim, aframe, posX, posY, flags);

		_operation = OPERATION_DRAW;
		rc[0] = _rectX1;
		rc[1] = _rectY1;
		rc[2] = _rectX2;
		rc[3] = _rectY2;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the rectangle occupied by a frame if it were to be drawn at posX/posY with flags
	/// &param rc The rectangle array to be populated
	/// &param frame The frame to be examined
	/// &param posX The X coordinate to be considered
	/// &param posY The Y coordinate to be considered
	/// &param flags The flags to be considered
	/// &param hx ??? Unused!! ???
	/// &param hy ??? Unused!! ???
	///
	/// &note This method just calls GetFrameRect without the last 2 params. Please use that one.
	/// &see void GetFrameRect(int[] rc, int frame, int posX, int posY, int flags)
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useOperationRect
	//------------------------------------------------------------------------------
    final void GetFrameRect(int[] rc, int frame, int posX, int posY, int flags, int hx, int hy )
    {
        GetFrameRect( rc, frame, posX, posY, flags);
    }

	//------------------------------------------------------------------------------
	/// Gets the rectangle occupied by a frame if it were to be drawn at posX/posY with flags
	/// &param rc The rectangle array to be populated
	/// &param frame The frame to be examined
	/// &param posX The X coordinate to be considered
	/// &param posY The Y coordinate to be considered
	/// &param flags The flags to be considered
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useOperationRect
	//------------------------------------------------------------------------------
	void GetFrameRect(int[] rc, int frame, int posX, int posY, int flags)
	{
        final Graphics g = null;

	//	System.out.println("GetFrameRect(rc, "+frame+", "+posX+", "+posY+", 0x"+Integer.toHexString(flags)+")");
		_rectX1 = Integer.MAX_VALUE;
		_rectY1 = Integer.MAX_VALUE;
		_rectX2 = Integer.MIN_VALUE;
		_rectY2 = Integer.MIN_VALUE;

		_operation = OPERATION_COMPUTERECT;
        {
		    PaintFrame( g,  frame, posX, posY, flags);
        }
		_operation = OPERATION_DRAW;

		rc[0] = _rectX1;
		rc[1] = _rectY1;
		rc[2] = _rectX2;
		rc[3] = _rectY2;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the rectangle occupied by a module in a frame if it were to be drawn at posX/posY with flags
	/// &param rc The rectangle array to be populated
	/// &param frame The frame containing the module
	/// &param fmodule The module to be examined
	/// &param posX The X coordinate to be considered
	/// &param posY The Y coordinate to be considered
	/// &param flags The flags to be considered
	/// &param hx ??? unused ???
	/// &param hy ??? unused ???
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useOperationRect
	//------------------------------------------------------------------------------
	void GetFModuleRect(int[] rc, int frame, int fmodule, int posX, int posY, int flags, int hx, int hy)
	{
        final Graphics g = null;

	//	System.out.println("GetFModuleRect(rc, "+frame+", "+fmodule+", "+posX+", "+posY+", 0x"+Integer.toHexString(flags)+")");
		_rectX1 = Integer.MAX_VALUE;
		_rectY1 = Integer.MAX_VALUE;
		_rectX2 = Integer.MIN_VALUE;
		_rectY2 = Integer.MIN_VALUE;

		_operation = OPERATION_COMPUTERECT;

		PaintFModule(  g,  frame, fmodule, posX, posY, flags);

		_operation = OPERATION_DRAW;

		rc[0] = _rectX1;
		rc[1] = _rectY1;
		rc[2] = _rectX2;
		rc[3] = _rectY2;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the rectangle occupied by a module if it were to be drawn at posX/posY with flags
	/// &param rc The rectangle array to be populated
	/// &param module The module to be examined
	/// &param posX The X coordinate to be considered
	/// &param posY The Y coordinate to be considered
	/// &param flags The flags to be considered
	//------------------------------------------------------------------------------
	void GetModuleRect(int[] rc, int module, int posX, int posY, int flags)
	{
		rc[0] = posX;
		rc[1] = posY;
		rc[2] = posX;
		rc[3] = posY;

		if ((flags & FLAG_ROT_90) == 0)
		{
			rc[2] += GetModuleWidth(module);
			rc[3] += GetModuleHeight(module);
		}
		else
		{
			rc[2] += GetModuleHeight(module);
			rc[3] += GetModuleWidth(module);
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Get the Module's Image of a specific Palette
	/// &param nModule Module number
	/// &param nPalette Palette number
	/// &return Image of the module/palette or null if nModule/nPalette are not valid or if the Image does not exist.
	//------------------------------------------------------------------------------
	Image GetModuleImage(int nModule, int nPalette)
	{
		if(_module_image_imageAA != null)
		{
			if((nPalette >= 0) && (nPalette < _module_image_imageAA.length) && (_module_image_imageAA[nPalette] != null))
			{
				if((nModule >= 0) && (nModule < _module_image_imageAA[nPalette].length))
				{
					return _module_image_imageAA[nPalette][nModule];
				}
			}
		}

		if(_module_image_imageAAA != null)
		{
			if((nPalette >= 0) && (nPalette < _module_image_imageAAA.length) && (_module_image_imageAAA[nPalette] != null))
			{
				if((nModule >= 0) && (nModule < _module_image_imageAAA[nPalette].length))
				{
					return _module_image_imageAAA[nPalette][nModule][0];
				}
			}
		}

		return null;
	}

	//------------------------------------------------------------------------------
	/// Get the Module's Data of a specific Palette
	/// &param nModule Module number
	/// &param nPalette Palette number
	/// &return Object (int[], short[] ...) of the module/palette or null if nModule/nPalette are not valid or if the Image does not exist.
	//------------------------------------------------------------------------------
	Object GetModuleData(int nModule, int nPalette)
	{
		//Nokia
		if((_modules_image_shortAAA != null) && (_modules_image_shortAAA[nPalette] != null))
		{
			return (Object)_modules_image_shortAAA[nPalette][nModule];
		}

		return null;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets a palette array
	/// &param nPalette Palette index
	/// &returns The palette array object
	//------------------------------------------------------------------------------
	Object GetPalette(int nPalette)
	{
		if((nPalette >= 0) && (nPalette < _palettes))
		{
			if (DevConfig.sprite_useNokiaUI)
			{
				if((_pal_short != null) && (nPalette < _pal_short.length))
				{
					return _pal_short[nPalette];
				}
			}
			else
			{
				if((_pal_int != null) && (nPalette < _pal_int.length))
				{
					return _pal_int[nPalette];
				}
			}
		}

		return null;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Get the number of modules in the sprite
	/// &returns the number of modules
	//------------------------------------------------------------------------------
	final int GetModuleCount ()
	{
		return _nModules;
	}

	//------------------------------------------------------------------------------
	/// Get the number of animations in the sprite
	/// &returns the number of animations
	//------------------------------------------------------------------------------
	int GetAnimationCount ()
	{


		if ((DevConfig.sprite_allowShortNumOfAFrames && (((_bs_flags & BS_NAF_1_BYTE) == 0))))

		{
			if (_anims_naf_short == null)
			{
				return 0;
			}

			return _anims_naf_short.length;
		}
		else
		{
			if (_anims_naf_byte == null)
			{
				return 0;
			}

			return _anims_naf_byte.length;
		}
	}

	//------------------------------------------------------------------------------
	/// Get the number of frames in the sprite
	/// &returns the number of frames
	//------------------------------------------------------------------------------
	int GetFrameCount()
	{


		if ((DevConfig.sprite_allowShortNumOfFModules && (((_bs_flags & BS_NFM_1_BYTE) == 0))))

		{
			if( _frames_nfm_short == null)
			{
				return 0;
			}

			return _frames_nfm_short.length;
		}
		else
		{
			if( _frames_nfm_byte == null)
			{
				return 0;
			}

			return _frames_nfm_byte.length;
		}
	}

	//------------------------------------------------------------------------------
	/// Gets the number of frames in the sprite
	/// &returns The number of frames
	//------------------------------------------------------------------------------
	final int GetFrames ()
	{
		return GetFrameCount();
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Count the number of modules in a frame
	/// &param frame The frame to be examined
	/// &returns The number of modules in the frame
	//------------------------------------------------------------------------------
    int CountFrameModules( int frame )
	{
		int count     = GetFModules( frame );
		int realcount = count;
		short[] _frames_fm_start = this._frames_fm_start;
		byte[] _fmodules = this._fmodules;
		byte[] _fmodules_flags = this._fmodules_flags;
		short[] _fmodules_id_short = this._fmodules_id_short;
		byte[] _fmodules_id_byte = this._fmodules_id_byte;
		for (int fmodule = 0; fmodule < count; fmodule++)
		{
            int off;
            int index;
            int fm_flags;
            {
			    off         = _frames_fm_start[frame] + fmodule;
			    fm_flags    = _fmodules_flags[ off ] & 0xFF;

				if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
				{
					index   = _fmodules_id_short[ off ];
				}
				else

				{
			    	index   = _fmodules_id_byte [ off ] & 0xFF;
				}
            }

			if (DevConfig.sprite_useIndexExFmodules && ((((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


			{
				index |= ((fm_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
			}

			if( (fm_flags & FLAG_HYPER_FM) != 0 )
            {
				realcount = realcount - 1 + CountFrameModules( index );
            }
		}

		return realcount;
	}

//--------------------------------------------------------------------------------------------------------------------

	private boolean IsModuleDataValid (int module)
	{
		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			if (_modules_data_array == null)
				return false;

			if (_modules_data_array[module] == null)
				return false;
		}
		else
		{
        	if( DevConfig.sprite_useModuleDataOffAsShort )
        	{
        	    if( _modules_data_off_short == null )
        	        return false;
        	}
        	else
        	{
        	    if( _modules_data_off_int == null )
        	        return false;
        	}

			if (_modules_data == null)
			{
				return false;
			}
		}

		return true;
	}

	//----------------------------------------------------------------------------------------------------------------
	private byte[] GetModuleData (int module)
	{
		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			if (_modules_data_array == null)
			{
				return null;
			}

			return _modules_data_array[module];
		}
		else
		{
			return _modules_data;
		}
	}

	//----------------------------------------------------------------------------------------------------------------
    private int GetStartModuleData (int module, int transf)
    {
		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			// Each module is in its own array... so START is always 0! :)
			return 0;
		}
		else
		{
			

				if( DevConfig.sprite_useModuleDataOffAsShort )
					return _modules_data_off_short[ module ] & 0xFFFF;
				else
					return _modules_data_off_int  [ module ];

		}
    }

    //----------------------------------------------------------------------------------------------------------------
	private int GetLenModuleData (int m, int transf)
    {
		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			return _modules_data_array[m].length;
		}
		else
		{
			

				if( DevConfig.sprite_useModuleDataOffAsShort )
				{
					return  m + 1 == _modules_data_off_short.length ?
									 _modules_data.length                    - _modules_data_off_short[ m ] & 0xFFFF:
									(_modules_data_off_short[ m + 1 ] & 0xFFFF) - _modules_data_off_short[ m ] & 0xFFFF;
				}
				else
				{
					return  m + 1 == _modules_data_off_int.length ?
									 _modules_data.length          - _modules_data_off_int[ m ] :
									(_modules_data_off_int[ m + 1 ] ) - _modules_data_off_int[ m ] ;
				}


		}
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Checks to see if the given frame has any markers.
	/// &param frame    - The frame to query for markers.
	/// &param markerID - If this is NOT -1 then only markers with this ID will be considered.
	/// &returns TRUE if any markers with the given ID are present.
	///
	/// &note If you will want to query the markers through GetFrameMarkers then its better
	///       to just use that right away and then check if the number of markers is 0.
	//------------------------------------------------------------------------------
	final public boolean IsMarkerInFrame (int frame, int markerID)
	{
		return GetNumFrameMarkers(frame, markerID) > 0;
	}

	//------------------------------------------------------------------------------
	/// Get the number of marker modules in this frame.
	/// &param frame    - The frame to query for markers.
	/// &param markerID - If this is NOT -1 then only markers with this ID will be considered.
	/// &returns number of markers present in this frame
	//------------------------------------------------------------------------------
	public int GetNumFrameMarkers(int frame, int markerID)
	{
		if( DevConfig.sprite_useMultipleModuleTypes )
		{
			int count     	= GetFModules(frame);
			int countMarker = 0;

			int off;
			int index;
			int fm_flags;
			short[] _frames_fm_start = this._frames_fm_start;
			byte[] _fmodules = this._fmodules;
			byte[] _fmodules_flags = this._fmodules_flags;
			short[] _fmodules_id_short = this._fmodules_id_short;
			byte[] _fmodules_id_byte = this._fmodules_id_byte;
			for (int fmodule = 0; fmodule < count; fmodule++)
			{
				{
					off         = _frames_fm_start[frame] + fmodule;
					fm_flags    = _fmodules_flags[ off ] & 0xFF;

					if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
					{
						index   = _fmodules_id_short[ off ];
					}
					else

					{
						index   = _fmodules_id_byte [ off ] & 0xFF;
					}
				}

				if (DevConfig.sprite_useIndexExFmodules && (!DevConfig.sprite_allowShortIndexForFModules || (((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


				{
					index |= ((fm_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
				}

				if(	((fm_flags & FLAG_HYPER_FM) == 0) && (_module_types[ index ] == MD_MARKER))
				{
					if ((markerID == -1) || (markerID == GetMarkerID(index)))
					{
						countMarker++;
					}
				}
			}

			return countMarker;
		}

		return 0;
	}

	//------------------------------------------------------------------------------
	/// Get all the marker positions of a frame.
	/// &param frame frame to query for markers.
	/// &returns an int array with all the markers or null if there are no marker on this frame.
	///          The array holds X,Y for each marker.
	//------------------------------------------------------------------------------
	final public int[] GetFrameMarkers(int frame)
	{
		return GetFrameMarkers(frame, null, 0, -1);
	}

	//------------------------------------------------------------------------------
	/// Returns the positions of all the markers which match the markerID
	///
	/// &param frame - The frame whose marker positions you want.
	/// &param markerID - The ID of the marker whose positions you want.
	/// &param markerPos - Array to hold the resulting positions (X, Y, etc
	///
	//------------------------------------------------------------------------------
	final public int[] GetFrameMarkerPos (int frame, int markerID, int[] markerPos)
	{
		return 	GetFrameMarkers(frame, markerPos, 0, markerID);
	}

	//------------------------------------------------------------------------------
	/// Get all the marker module positions of a frame.
	/// &param frame frame to query for markers.
	/// &param markerInfo - The array that will be used to return the data if large enough.
	///                     Otherwise, or if NULL, this array will be allocated and returned.
	/// &param format - Determines what information is returned in the array
	///                 0 = For each marker {X, Y}
	///                 1 = For each marker {X, Y, markerID, index in frame}
	///                 2 = Like 0, only first entry is the & of markers found
	///                 3 = Like 1, only first entry is the & of markers found
	/// &param markerID - If this is NOT -1 then only markers with this ID will be considered.
	/// &returns an int array with all the markers or null if there are no maker on this frame.
	//------------------------------------------------------------------------------
	public int[] GetFrameMarkers(int frame, int[] markerInfo, int format, int markerID)
	{
		if( DevConfig.sprite_useMultipleModuleTypes )
		{
			int countMarker = GetNumFrameMarkers(frame, markerID);

			if (countMarker > 0)
			{
				int   pos = 0;
				int[] res = markerInfo;

				// Happened to work out to this... really it is:
				// Format = 0 . {X, Y}            = 2
				//        = 1 . {X, Y, ID, index} = 4
				int   len = (countMarker << (1 + (format & 0x01)));

				// For format 2 and 3 we will need an extra slot for the marker num
				len += (format > 1) ? 1 : 0;

				// If param was NULL, or not large enough... then allocate.
				if ((res == null) || (res.length < len))
				{
					res = new int[len];
				}

				int count = GetFModules(frame);
				int off;
				int index;
				int fm_flags;

				if (format > 1)
				{
					// Convert to other format so code below is optimal... no need to remember after this...
					format -= 2;

					// Store the number of markers found
					res[pos] = countMarker;
					pos++;
				}
				byte[] _fmodules = this._fmodules;
				short[] _frames_fm_start = this._frames_fm_start;
				byte[] _fmodules_flags = this._fmodules_flags;
				short[] _fmodules_id_short = this._fmodules_id_short;
				byte[] _fmodules_id_byte = this._fmodules_id_byte;
				for (int fmodule = 0; fmodule < count; fmodule++)
				{
					{
						off         = _frames_fm_start[frame] + fmodule;
						fm_flags    = _fmodules_flags[ off ] & 0xFF;

						if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0))))
						{
							index   = _fmodules_id_short[ off ];
						}
						else

						{
							index   = _fmodules_id_byte [ off ] & 0xFF;
						}
					}

					if (DevConfig.sprite_useIndexExFmodules && (!DevConfig.sprite_allowShortIndexForFModules || (((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


					{
						index |= ((fm_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
					}

					if(	((fm_flags & FLAG_HYPER_FM) == 0) && (_module_types[index] == MD_MARKER))
					{
						if ((markerID == -1) || (markerID == GetMarkerID(index)))
						{
							res[pos  ] 	= GetFrameModuleX(frame, fmodule);
							res[pos+1] 	= GetFrameModuleY(frame, fmodule);

							if (format == 0)
							{
								pos += 2;
							}
							else
							{
								res[pos+2] = GetMarkerID(index);
								res[pos+3] = fmodule;
								pos += 4;
							}
						}
					}
				}

				return res;
			}
		}

		return null;
	}


	//------------------------------------------------------------------------------
	/// Gets the marker ID for this module if its a marker.
	/// &param module
	/// &returns the Frame rect count for this frame.
	//------------------------------------------------------------------------------
	final int GetMarkerID(int module)
	{
		if( DevConfig.sprite_useMultipleModuleTypes )
		{
			/*
			// TODO: Add this flag if needed
			if (zJYLibConfig.sprite_useColorForMarkerID)
			{
				// COLORS ARE NOT EXPORTED FOR MARKERS CURRENTLY!
				// TODO: Add color to marker info, or create new marker type.
				return GetModuleColor(module);
			}
			else
			*/
			{
				return module;
			}
		}

		return -1;
	}


	//------------------------------------------------------------------------------
	/// Get The FrameRect count for a specific frame.
	/// &param frame frame to query for Frame Rect Count.
	/// &returns the Frame rect count for this frame.
	//------------------------------------------------------------------------------
	int GetFrameRectCount(int frame)
	{
		if (DevConfig.sprite_useFrameRects)
		{
			short[] _frames_rects_start = this._frames_rects_start;
			if(_frames_rects_start != null)
			{
				return _frames_rects_start[frame + 1] - _frames_rects_start[frame];
			}
		}

		return 0;
	}


	//------------------------------------------------------------------------------
	/// Get a FrameRect from a specific frame.
	/// &param frame frame to query for Frame Rect.
	/// &param rectIndex which rect to get for this frame.
	/// &param rect int array to hlod the rect, [x, y, w, h].
	/// &param flags flip x/y flags
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useFrameRects
	//------------------------------------------------------------------------------
	void GetFrameRect(int frame, int rectIndex, int[] rect, int flags)
	{
		if (DevConfig.sprite_useFrameRects)
		{
			short[] _frames_rects_start = this._frames_rects_start;
			short[] _frames_rects_short = this._frames_rects_short;
			byte[] _frames_rects = this._frames_rects;
			if((_frames_rects_start != null) && (rect != null))
			{
				int nStart 	= _frames_rects_start[frame];
				int nEnd 	= _frames_rects_start[frame + 1];
				int nCount 	= nEnd - nStart;

				if((nCount > 0) && (rectIndex < nCount))
				{
					nStart  = (nStart + rectIndex) << 2;

					if (DevConfig.sprite_useFMOffShort && (_bs_flags & BS_FM_OFF_SHORT) != 0)
					{
						if (_frames_rects_short != null)
						{
		    				rect[0] = _frames_rects_short[nStart + 0];
		    				rect[1] = _frames_rects_short[nStart + 1];
		    				rect[2] = _frames_rects_short[nStart + 2] & 0xFFFF;
		    				rect[3] = _frames_rects_short[nStart + 3] & 0xFFFF;
						}
					}
					else
					{
		                if(_frames_rects != null)
		                {
		    				rect[0] = _frames_rects[nStart + 0];
		    				rect[1] = _frames_rects[nStart + 1];
		    				rect[2] = _frames_rects[nStart + 2] & 0xFF;
		    				rect[3] = _frames_rects[nStart + 3] & 0xFF;
		    			}
					}

					if ((flags & FLAG_FLIP_X) != 0)
					{
						rect[0] = -rect[0] - rect[2];
					}

					if ((flags & FLAG_FLIP_Y) != 0)
					{
						rect[1] = -rect[1] - rect[3];
					}
				}
				else
				{
					rect[0] = 0;
					rect[1] = 0;
					rect[2] = 0;
					rect[3] = 0;
				}
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Get a FrameRect from a specific anim and aframe.
	/// &param anim anim to query for Frame Rect.
	/// &param aframe aframe to query for Frame Rect.
	/// &param rectIndex which rect to get for this frame.
	/// &param rect int array to hlod the rect, [x, y, w, h].
	/// &param flags flip x/y flags
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useFrameRects
	///
	/// &note There is a more general version of this function that allows the AFrame offsets to be taken into account.
	/// &see zSprite&GetAFrameRect (int anim, int aframe, int rectIndex, int[] rect, int flags, boolean useAFrameOffsets)
	//------------------------------------------------------------------------------
	final void GetAFrameRect (int anim, int aframe, int rectIndex, int[] rect, int flags)
	{
		if (DevConfig.sprite_useFrameRects)
		{
			GetAFrameRect (anim, aframe, rectIndex, rect, flags, false);
		}
	}

	//------------------------------------------------------------------------------
	/// Get a FrameRect from a specific anim and aframe.
	/// &param anim anim to query for Frame Rect.
	/// &param aframe aframe to query for Frame Rect.
	/// &param rectIndex which rect to get for this frame.
	/// &param rect int array to hlod the rect, [x, y, w, h].
	/// &param flags flip x/y flags
	/// &param useAFrameOffsets if TRUE then the AFrame offsets will be used to adjust the rect
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.sprite_useFrameRects
	//------------------------------------------------------------------------------
	void GetAFrameRect (int anim, int aframe, int rectIndex, int[] rect, int flags, boolean useAFrameOffsets)
	{
		if (DevConfig.sprite_useFrameRects)
		{
			int off;
			int frame;
			{
				off = _anims_af_start[anim] + aframe;

		        if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0))))


		        {
					frame = _aframes_frame_short[off];
				}
				else
				{
		        	frame = _aframes_frame_byte [off] & 0xFF;
				}

				if (DevConfig.sprite_useIndexExAframes && (!DevConfig.sprite_allowShortIndexForAFrames || (((_bs_flags & BS_AF_INDEX_SHORT) == 0))))


				{
					frame |= ((_aframes_flags[off]&FLAG_INDEX_EX_MASK)<<INDEX_EX_SHIFT);
				}

				GetFrameRect(frame, rectIndex, rect, flags ^ (_aframes_flags[off]&0x0F));

				if(useAFrameOffsets)
				{
					if ((flags & FLAG_FLIP_X) != 0) {
						rect[0] -= GetAFramesOX(off);
					} else {
						rect[0] += GetAFramesOX(off);
					}
					if ((flags & FLAG_FLIP_Y) != 0) {
						rect[1] -= GetAFramesOY(off);
					} else {
						rect[1] += GetAFramesOY(off);
					}
				}
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Will return a reference to an int[] buffer.
	///
	/// &param buffer - The returned buffered will NOT equal this buffer.
	///
	/// &note If needed buffer is not allocated it will be allocated.
	/// &note Size of buffer will be zJYLibConfig.TMP_BUFFER_SIZE.
	//------------------------------------------------------------------------------
	public static int[] GetPixelBuffer_int (int[] buffer)
	{
		if ((buffer == null) || (buffer != temp_int))
		{
			if (temp_int == null)
			{
				temp_int = new int[DevConfig.TMP_BUFFER_SIZE];
			}

			return temp_int;
		}
		else if ((buffer == null) || (buffer != transform_int))
		{
			if (transform_int == null)
			{
				if (DevConfig.TMP_ALT_BUFFER_SIZE > 0)
				{
					transform_int = new int[DevConfig.TMP_ALT_BUFFER_SIZE];
				}
				else
				{
					transform_int = new int[DevConfig.TMP_BUFFER_SIZE];
				}
			}

			return transform_int;
		}

		Utils.Dbg("GetPixelBuffer_int: Error - passed buffer equals both temp_int AND transform_int, this means temp_int == transform_int?!?!");;
		return null;
	}

	//------------------------------------------------------------------------------
	/// Will return a reference to an int[] buffer.
	///
	/// &param buffer - The returned buffered will NOT equal this buffer.
	///
	/// &note If needed buffer is not allocated it will be allocated.
	/// &note Size of buffer will be zJYLibConfig.TMP_BUFFER_SIZE.
	//------------------------------------------------------------------------------
	public static short[] GetPixelBuffer_short (short[] buffer)
	{
		if ((buffer == null) || (buffer != temp_short))
		{
			if (temp_short == null)
			{
				temp_short = new short[DevConfig.TMP_BUFFER_SIZE];
			}

			return temp_short;
		}
		else if ((buffer == null) || (buffer != transform_short))
		{
			if (transform_short == null)
			{
				transform_short = new short[DevConfig.TMP_BUFFER_SIZE];
			}

			return transform_short;
		}

		Utils.Dbg("GetPixelBuffer_short: Error - passed buffer equals both temp_int AND transform_int, this means temp_short == transform_short?!?!");;
		return null;
	}

	//------------------------------------------------------------------------------
	/// Gets the number of palettes this sprite has.
	///
	/// &return Number of palettes
	//------------------------------------------------------------------------------
	final public int GetNumPalettes ()
	{
		return _palettes;
	}

	//------------------------------------------------------------------------------
	/// Gets the number of colors this sprite's palette has.
	///
	/// &return Number of colors in the palette.
	//------------------------------------------------------------------------------
	final public int GetNumColors ()
	{
		return _colors;
	}

	//------------------------------------------------------------------------------
	/// If the single frame module cache is supported this function will allow enabling
	/// and disabling of this feature at run-time (for performance evaluation purposes)
	///
	/// &param enable - TRUE if you want to enable this feature, FALSE if you want to disable it.
	///
	/// &note This only applies if this feature is present (disabled by default).
	/// &see zJYLibConfig.sprite_useSingleFModuleCache
	//------------------------------------------------------------------------------
	final static public void EnableSingleFModuleCache (boolean enable)
	{
		if (DevConfig.sprite_useSingleFModuleCache)
		{
			if (enable)
			{
				s_moduleBufferState &= ~MODULE_STATE_DISABLE_MASK;
			}
			else
			{
				s_moduleBufferState |= MODULE_STATE_DISABLE_MASK;
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Returns if the single frame module cache is currently enabled.
	///
	/// &return TRUE if the enable, FALSE otherwise
	///
	/// &note This only applies if this feature is present (disabled by default).
	/// &see zJYLibConfig.sprite_useSingleModuleCache
	//------------------------------------------------------------------------------
	final static public boolean IsSingleFModuleCacheEnabled ()
	{
		return DevConfig.sprite_useSingleFModuleCache && ((s_moduleBufferState & MODULE_STATE_DISABLE_MASK)==0);
	}

	//------------------------------------------------------------------------------
	/// Resets the var holding the cache state so the cache won't be used.
	//------------------------------------------------------------------------------
	final static public void ResetSingleFModuleCache ()
	{
		s_moduleBufferState |= MODULE_STATE_INIT_MASK;
	}

	//------------------------------------------------------------------------------
	/// Will prevent the zSprite class from calling garbage collection.
	///
	/// &note If sprite_useDeactivateSystemGc is TRUE then this does nothing.
	//------------------------------------------------------------------------------
	final static public void DisableGC ()
	{
		s_gcEnabled = false;
	}

	//------------------------------------------------------------------------------
	/// Will re-enable the zSprite class calling garbage collection.
	///
	/// &note If sprite_useDeactivateSystemGc is TRUE then this does nothing.
	//------------------------------------------------------------------------------
	final static public void EnableGC ()
	{
		s_gcEnabled = true;
	}

	//------------------------------------------------------------------------------
	/// Get the color of a given module when applicable (MD_RECT, MD_FILLRECT, etc)
	/// &param module The module ID of the modules whose color we want.
	/// &returns The color. On error returns 0.
	///
	/// &see zJYLibConfig.sprite_useMultipleModuleTypes
	/// &see zJYLibConfig.sprite_useModuleColorAsByte
	//------------------------------------------------------------------------------
	int GetModuleColor (int module)
	{
		if( DevConfig.sprite_useMultipleModuleTypes )
		{
			byte[] _module_types = this._module_types;
			if((_module_types != null) && (_module_types[module] != MD_IMAGE))
			{
				if(DevConfig.sprite_useModuleColorAsByte)
				{
					byte[] _module_colors_byte = this._module_colors_byte;
					if (_module_colors_byte != null)
					{
						return (int)(_module_colors_byte[module]);
					}
				}
				else
				{
					int[] _module_colors_int = this._module_colors_int;
					if (_module_colors_int != null)
					{
						return _module_colors_int[module];
					}
				}
			}
		}
		else
		{
			Utils.Dbg("GetModuleColor: sprite_useMultipleModuleTypes is FALSE so no modules can have color!");;
		}

		return 0;
	}


//--------------------------------------------------------------------------------------------------------------------
// GLLib_Set.jpp
//--------------------------------------------------------------------------------------------------------------------

	//-----------------------------------------------------------------------------
	/// Sets whether this sprite, when cached, will use Images or int[]'s
	/// for the cache. (Will obviously effect drawing of the sprite)
	///
	/// &param p_useCacheRGB if true then sets sprite to use int[]'s on cache
	///
	/// &see zJYLibConfig.sprite_useCacheRGBArrays
	/// &see zJYLibConfig.sprite_useManualCacheRGBArrays
	//-----------------------------------------------------------------------------
	void SetUseCacheRGB (boolean p_useCacheRGB)
	{
		if (DevConfig.sprite_useManualCacheRGBArrays)
		{
			if (p_useCacheRGB)
			{
				_flags |= FLAG_USE_CACHE_RGB;
			}
			else
			{
				_flags &= ~FLAG_USE_CACHE_RGB;
			}
		}
		else
		{
			Utils.Dbg("SetUseCacheRGB: You need to enable sprite_useManualCacheRGBArrays to use this functionality!");;
		}
	}

	//-----------------------------------------------------------------------------
	/// Sets whether this sprite will use module masking to prevent certain modules
	/// from drawing.
	///
	/// &param p_useModuleMasking - TRUE to enable this feature for this sprite, false to disable it.
	///
	/// &see zJYLibConfig.sprite_allowModuleMarkerMasking
	//-----------------------------------------------------------------------------
	void SetUseModuleMasking (boolean p_useModuleMasking)
	{
		if (DevConfig.sprite_allowModuleMarkerMasking)
		{
			if (p_useModuleMasking)
			{
				_flags |= FLAG_USE_MODULE_MASK ;
			}
			else
			{
				_flags &= ~FLAG_USE_MODULE_MASK ;
			}
		}
		else
		{
			Utils.Dbg("SetUseModuleMasking: You need to enable sprite_allowModuleMarkerMasking to use this functionality!");;
		}
	}

	//-----------------------------------------------------------------------------
	/// Sets the global module mask which will effect any sprites for which module
	/// masking has been enabled. This is never cleared so you must do it yourself.
	///
	/// &param mask - The bit mask according to the markers used withint the sprite.
	///
	/// &see zJYLibConfig.sprite_allowModuleMarkerMasking
	//-----------------------------------------------------------------------------
	static void SetModuleMask (int mask)
	{
		if (DevConfig.sprite_allowModuleMarkerMasking)
		{
			s_moduleMask = mask;
		}
		else
		{
			Utils.Dbg("SetModuleMask: You need to enable sprite_allowModuleMarkerMasking to use this functionality!");;
		}
	}

	//-----------------------------------------------------------------------------
	/// Allows for turning GC() on/off. Applies to all GC() inside zSprite class.
	///
	/// &param enableGC True if GC is to be enabled, False if you want it disabled
	//-----------------------------------------------------------------------------
	static void EnableGC (boolean enableGC)
	{
		s_gcEnabled = enableGC;
	}

	//------------------------------------------------------------------------------
	/// Sets a palette array
	/// &param nPalette Palette index
	/// &param pal The Palette to set
	//------------------------------------------------------------------------------
	void SetPalette(int nPalette, int[] pal)
	{
		int[][] _pal_int = this._pal_int;
		if(!(_pal_int != null))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Array has not been initialized!");;
		if(!(nPalette >= 0))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Index to set is invalid: " + nPalette);;
		if(!(nPalette < _pal_int.length))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Index to set is invalid: " + nPalette);;

		_pal_int[nPalette] = pal;
	}

	//------------------------------------------------------------------------------
	/// Sets a palette array
	/// &param nPalette Palette index
	/// &param pal The Palette to set
	//------------------------------------------------------------------------------
	void SetPalette(int nPalette, short[] pal)
	{
		short[][] _pal_short = this._pal_short;
		if(!(_pal_short != null))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Array has not been initialized!");;
		if(!(nPalette >= 0))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Index to set is invalid: " + nPalette);;
		if(!(nPalette < _pal_short.length))Utils.DBG_PrintStackTrace(false, "SetPalette: Palette Index to set is invalid: " + nPalette);;

		_pal_short[nPalette] = pal;
	}


// ASprite_Paint.jpp
//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Sets the current graphics context
	/// &param g1 The graphics context to be set
	//------------------------------------------------------------------------------
    static void SetGraphics( Graphics g1 )
	{
        

	}

	//------------------------------------------------------------------------------
	/// Sets the current graphics context
	/// &param buffer - The int[] which is to be the current rendering target.
	/// &param w - The width of the buffer.
	/// &param h - The height of the buffer.
	//------------------------------------------------------------------------------
	public final static void SetGraphics (int[] buffer, int w, int h)
	{
		SetGraphics(buffer, w, h, ALPHA_FULL);
	}

	//------------------------------------------------------------------------------
	/// Sets the current graphics context
	/// &param buffer - The int[] which is to be the current rendering target.
	/// &param w - The width of the buffer.
	/// &param h - The height of the buffer.
	/// &param alphaState - indicates the state of the alpha channel in the buffer currently
	//------------------------------------------------------------------------------
	static void SetGraphics (int[] buffer, int w, int h, int alphaState)
	{
		if (DevConfig.sprite_allowPixelArrayGraphics)
		{
			if(!((buffer == null) || (buffer.length >= (w*h))))Utils.DBG_PrintStackTrace(false, "SetGraphics: buffer size does not supported passed in dimensions!");;

			_customGraphics			  = buffer;
			_customGraphicsWidth	  = w;
			_customGraphicsHeight     = h;
			_customGraphicsAlpha      = alphaState;
			_customGraphicsClipX      = 0;
			_customGraphicsClipY      = 0;
			_customGraphicsClipW      = w;
			_customGraphicsClipH      = h;
		}
		else
		{
			Utils.Dbg("SetGraphics: Trying to set a custom pixel graphics context but sprite_allowPixelArrayGraphics is not enabled!!");;
		}
	}

    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws an animation frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param anim The animation containing the frame
	/// &param aframe The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	//------------------------------------------------------------------------------
	final void PaintAFrame(int anim, int aframe, int posX, int posY, int flags)
	{
		PaintAFrame(GLLib.g,  anim, aframe, posX, posY, flags, 0, 0, null);
	}
	public final void PaintAFrame(Graphics g, int anim, int aframe, int posX, int posY, int flags)
	{  Graphics _g;
		if (DevConfig.useSoftwareDoubleBuffer)
			{
			_g = GLLib.GetSoftwareDoubleBufferGraphics();
			if (_g!=null) g = _g;
			}
			PaintAFrame(g,  anim, aframe, posX, posY, flags, 0, 0, null);
	}

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws an animation frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param anim The animation containing the frame
	/// &param aframe The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &param hx The height of the area to be drawn
	/// &param hy The width of the area to be drawn
	//------------------------------------------------------------------------------
	void PaintAFrame(int anim, int aframe, int posX, int posY, int flags, int hx, int hy)
	{
		PaintAFrame(GLLib.g, anim, aframe, posX, posY, flags, hx, hy, null);
	}
	void PaintAFrame(Graphics g, int anim, int aframe, int posX, int posY, int flags, int hx, int hy)
	{
		PaintAFrame(g, anim, aframe, posX, posY, flags, hx, hy, null);
	}
	void PaintAFrame(int anim, int aframe, int posX, int posY, int flags, int hx, int hy, zAnimPlayer player)
	{
		PaintAFrame(GLLib.g,anim,aframe,posX,posY,flags, hx,hy,player);
	}
	void PaintAFrame(Graphics g, int anim, int aframe, int posX, int posY, int flags, int hx, int hy, zAnimPlayer player)
	{
		_player = player;
		
		//System.out.println("PaintAFrame(g_  "+anim+", "+aframe+", "+posX+", "+posY+", 0x"+Integer.toHexString(flags)+", "+hx+", "+hy+")");

		if (DevConfig.sprite_useTransMappings)
		{
			int off = _anims_af_start[anim] + aframe;
			int frame;


			if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0)))
			   )
			{
				frame = _aframes_frame_short[off];
			}
			else

			{
				frame = _aframes_frame_byte [off] & 0xFF;
			}

			if (DevConfig.sprite_useIndexExAframes && (!DevConfig.sprite_allowShortIndexForAFrames || (((_bs_flags & BS_AF_INDEX_SHORT) == 0))))


			{
				frame |= ((_aframes_flags[off]&FLAG_INDEX_EX_MASK)<<INDEX_EX_SHIFT);
			}

			int tmp;

			int offsetX = GetAFramesOX(off);
			int offsetY = GetAFramesOY(off);

			int fm_flags = _aframes_flags[off]&0x0F;

			if ((flags & FLAG_FLIP_X) != 0)
			{
				fm_flags = TRANSFORM_FLIP_X[fm_flags&0x07] | (fm_flags & ~0x07);
				offsetX = -offsetX;
			}

			if ((flags & FLAG_FLIP_Y) != 0)
			{
				fm_flags = TRANSFORM_FLIP_Y[fm_flags&0x07] | (fm_flags & ~0x07);
				offsetY = -offsetY;
			}

			if ((flags & FLAG_ROT_90) != 0)
			{
				fm_flags = TRANSFORM_ROT_90[fm_flags&0x07] | (fm_flags & ~0x07);
				tmp = offsetX;
				offsetX = -offsetY;
				offsetY = tmp;
			}

			PaintFrame(g,  frame, posX + offsetX, posY + offsetY, fm_flags, hx, hy);
		}
		else
		{
			{

				int off = _anims_af_start[anim] + aframe;
				int frame = 0;

				if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0)))
				   )
				{
					frame = _aframes_frame_short[off];
				}
				else

				{
					frame = _aframes_frame_byte [off] & 0xFF;
				}

				if (DevConfig.sprite_useIndexExAframes && (!DevConfig.sprite_allowShortIndexForAFrames || (((_bs_flags & BS_AF_INDEX_SHORT) == 0))))


				{
					frame |= ((_aframes_flags[off]&FLAG_INDEX_EX_MASK)<<INDEX_EX_SHIFT);
				}

				if(!DevConfig.sprite_useLoadImageWithoutTransf && DevConfig.sprite_useTransfFlip)
				{
					if((flags & FLAG_FLIP_X) != 0) hx += GetAFramesOX(off);
					else							hx -= GetAFramesOX(off);
					if((flags & FLAG_FLIP_Y) != 0) hy += GetAFramesOY(off);
					else							hy -= GetAFramesOY(off);
				}
				else
				{
					hx -= GetAFramesOX(off);
					hy -= GetAFramesOY(off);
				}

				//	if((flags & FLAG_FLIP_X) != 0)	hx += _frames_w[frame]&0xFF;
				//	if((flags & FLAG_FLIP_Y) != 0)	hy += _frames_h[frame]&0xFF;
				PaintFrame(g,  frame, posX-hx, posY-hy, flags ^ (_aframes_flags[off]&0x0F), hx, hy);
			} // if(! zJYLibConfig.sprite_useSingleArrayForFMAF)
		}
		
		_player = null;
	}

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws a frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	//------------------------------------------------------------------------------
	final void PaintFrame(int frame, int posX, int posY, int flags)
	{
		PaintFrame(GLLib.g,  frame, posX, posY, flags, 0, 0);
	}

	final void PaintFrame(Graphics g, int frame, int posX, int posY, int flags)
	{
		Graphics _g;
		if (DevConfig.useSoftwareDoubleBuffer)
			{
			_g = GLLib.GetSoftwareDoubleBufferGraphics();
			if (_g!=null) g = _g;
			}
			PaintFrame(g,  frame, posX, posY, flags, 0, 0);
	}
	

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws a frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &param hx The height of the area to be drawn
	/// &param hy The width of the area to be drawn
	//------------------------------------------------------------------------------
	void PaintFrame(int frame, int posX, int posY, int flags, int hx, int hy)
	{
		PaintFrame(GLLib.g,frame,posX,posY,flags,hx,hy);
	}
	public void PaintFrame(Graphics g, int frame, int posX, int posY, int flags, int hx, int hy)
	{
		

		int nFModules = GetFModules(frame);

		if (DevConfig.sprite_useCachedFrames && (_operation == OPERATION_DRAW) && IsFrameCached(frame, _crt_pal))
		{
			// Using Module Masking? Feature enabled + enabled for this sprite?
			if (DevConfig.sprite_allowModuleMarkerMasking && ((_flags & FLAG_USE_MODULE_MASK) != 0))
			{
				Utils.Dbg("PaintFrame: Frame is cached but because module masking is enabled it is not being used!");;
			}
			else if (PaintCachedFrame(g, frame, posX, posY, flags))
			{
				nFModules = -1;
			}
		}

		if (nFModules > 0)
		{
			if ( IsSingleFModuleCacheEnabled() )
			{
				s_moduleBufferState = MODULE_STATE_INIT_MASK;
			}

			// Using Module Masking? Feature enabled + enabled for this sprite?
			if (DevConfig.sprite_allowModuleMarkerMasking && ((_flags & FLAG_USE_MODULE_MASK) != 0))
			{
				int currentMask = 0;

				for (int fmodule = 0; fmodule < nFModules; fmodule++)
				{
					int moduleID   = GetFrameModule(frame, fmodule);
					int moduleType = GetModuleType(moduleID);

					if(moduleType == MD_MARKER && (moduleID < 32))
					{
						currentMask = (1 << moduleID);
						continue;
					}
					else
					{
						// MODULE MASK!
						if((currentMask & s_moduleMask) != 0)
						{
							PaintFModule(g,  frame, fmodule, posX, posY, flags, hx, hy);
						}
					}
				}
			}
			else
			{
				for (int fmodule = 0; fmodule < nFModules; fmodule++)
				{
					PaintFModule(g,  frame, fmodule, posX, posY, flags, hx, hy);
				}
			}

			if ( IsSingleFModuleCacheEnabled() )
			{
				s_moduleBufferState = MODULE_STATE_INIT_MASK;
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws a module from a frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame containing the module
	/// &param fmodule The module to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	//------------------------------------------------------------------------------
	final void PaintFModule(int frame, int fmodule, int posX, int posY, int flags)
	{
		  Graphics _g,g;
			if (DevConfig.useSoftwareDoubleBuffer)
				{
				_g = g = GLLib.GetSoftwareDoubleBufferGraphics();
				if (_g==null) g = GLLib.g;
				}
			PaintFModule(GLLib.g,  frame, fmodule, posX, posY, flags, 0 ,0);
	}
	final void PaintFModule(Graphics g, int frame, int fmodule, int posX, int posY, int flags)
	{
		  Graphics _g;
			if (DevConfig.useSoftwareDoubleBuffer)
				{
				_g = GLLib.GetSoftwareDoubleBufferGraphics();
				if (_g!=null) g = _g;
				}
			PaintFModule(g,  frame, fmodule, posX, posY, flags, 0 ,0);
	}

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Draws a module from a frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame containing the module
	/// &param fmodule The module to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &param hx The height of the area to be drawn
	/// &param hy The width of the area to be drawn
	//------------------------------------------------------------------------------
	void PaintFModule(int frame, int fmodule, int posX, int posY, int flags, int hx, int hy)
	{
		PaintFModule(GLLib.g,frame,fmodule,posX,posY,flags,hx,hy);
	}
	void PaintFModule(Graphics g, int frame, int fmodule, int posX, int posY, int flags, int hx, int hy)
	{
		int off, fm_flags, index, originalIndex, tmp;

		
		short[] _frames_fm_start = this._frames_fm_start;
		byte[] _fmodules = this._fmodules;
		{
			off      = _frames_fm_start[ frame ] + fmodule;
			fm_flags = _fmodules_flags [ off ] & 0xFF;

			if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0)))
			   )
			{
				index  = _fmodules_id_short[ off ];
			}
			else

			{
				index  = _fmodules_id_byte [ off ] & 0xFF;
			}
		}

		if (DevConfig.sprite_useIndexExFmodules && (!DevConfig.sprite_allowShortIndexForFModules || (((_bs_flags & BS_FM_INDEX_SHORT) == 0))))


		{
			index |= ((fm_flags & FLAG_INDEX_EX_MASK) << INDEX_EX_SHIFT);
		}

		if(DevConfig.sprite_useFMPalette)
		{
			if( (!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_FM_PALETTE) != 0))
			{
				_crt_pal = _fmodules_pal[off]&0xFF;
			}
		}

		// Needed in case of module mapping (need to modify index, but save old value to pass to PaintModule)
		originalIndex = index;

		int offsetX = 0;
		int offsetY = 0;
		int moduleW = 0;
		int moduleH = 0;

		// HyperFrames position is its center, so no need to account for W/H if it is a hyper-frame
		if ((!DevConfig.sprite_useHyperFM) || ((fm_flags & FLAG_HYPER_FM) == 0))
		{
			moduleW = GetModuleWidth(index);
			moduleH = GetModuleHeight(index);
		}

		if (DevConfig.sprite_useTransMappings)
		{
			{
				offsetX = GetFModuleOX(off);
				offsetY = GetFModuleOY(off);
			}

			// Get w/h of module as it will be rendered currently
			if ((fm_flags & FLAG_ROT_90) != 0)
			{
				tmp     = moduleW;
				moduleW = moduleH;
				moduleH = tmp;
			}

			// Resolve flags/offsets in correct order (same as Aurora)
			if ((flags & FLAG_FLIP_X) != 0)
			{
				fm_flags = TRANSFORM_FLIP_X[fm_flags&0x07] | (fm_flags & ~0x07);
				offsetX = -offsetX - moduleW;
			}

			if ((flags & FLAG_FLIP_Y) != 0)
			{
				fm_flags = TRANSFORM_FLIP_Y[fm_flags&0x07] | (fm_flags & ~0x07);
				offsetY = -offsetY - moduleH;
			}

			if ((flags & FLAG_ROT_90) != 0)
			{
				fm_flags = TRANSFORM_ROT_90[fm_flags&0x07] | (fm_flags & ~0x07);
				tmp = offsetX;
				offsetX = -offsetY - moduleH;
				offsetY = tmp;
			}
		}
		else
		{
			if(true)
			{
				if ((fm_flags & FLAG_ROT_90) != 0)
				{
					if ((flags & (FLAG_FLIP_X | FLAG_FLIP_Y)) != 0)
					{
						Utils.Dbg("Warning: Applying FLIP to frame that contains a ROTATED module! This will not work correctly, please enable sprite_useTransMappings to have this work properly!");;
					}
				}
			}

			// Old way of resolving flags, XOR
			fm_flags = flags ^ fm_flags; //!Le Chanh Tin: Didn't need to & 0x0F => We will lost FM flags.

			if ((fm_flags & FLAG_ROT_90) != 0)
			{
				tmp = moduleW;
				moduleW = moduleH;
				moduleH = tmp;
			}

			{
				if(!DevConfig.sprite_useLoadImageWithoutTransf && DevConfig.sprite_useTransfRot && ((flags & FLAG_ROT_90) != 0))
				{
					if(!DevConfig.sprite_useLoadImageWithoutTransf && DevConfig.sprite_useTransfFlip)
					{
						if((flags & FLAG_FLIP_X) != 0) offsetY = -(GetFModuleOX(off) + moduleH);
						else						   offsetY =  (GetFModuleOX(off));
						if((flags & FLAG_FLIP_Y) != 0) offsetX =  (GetFModuleOY(off) + moduleW);
						else						   offsetX = -(GetFModuleOY(off) + moduleW);
					}
					else
					{
						offsetY =  (GetFModuleOX(off));
						offsetX = -(GetFModuleOY(off) + moduleW);
					}
				}
				else // if((zJYLibConfig.sprite_useTransfRot) && ((flags & FLAG_ROT_90) != 0))
				{
					if(!DevConfig.sprite_useLoadImageWithoutTransf && DevConfig.sprite_useTransfFlip)
					{
						if((flags & FLAG_FLIP_X) != 0) offsetX = -(GetFModuleOX(off) + moduleW);
						else						   offsetX =  (GetFModuleOX(off));
						if((flags & FLAG_FLIP_Y) != 0) offsetY = -(GetFModuleOY(off) + moduleH);
						else 						   offsetY =  (GetFModuleOY(off));
					}
					else
					{
						offsetX = GetFModuleOX(off);
						offsetY = GetFModuleOY(off);
					}
				}
			} // if(! zJYLibConfig.sprite_useSingleArrayForFMAF)
		}


		// If scaling is enabled, and turned on then apply the scaling to the offsets as well
		if ((DevConfig.pfx_enabled)&&(DevConfig.pfx_useSpriteEffectScale && DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_SCALE)))
		{
			int percent = cPFX.PFX_GetParam(GLPixEffects.k_EFFECT_SCALE, GLPixEffects.k_PARAM_SCALE_PERCENT);

			offsetX = (percent * offsetX) / 100;
			offsetY = (percent * offsetY) / 100;
		}

		posX += offsetX;
		posY += offsetY;

		if((DevConfig.sprite_useHyperFM) && ((fm_flags & FLAG_HYPER_FM) != 0))
		{
			PaintFrame(g,  index, posX, posY, (fm_flags&0x0F), hx, hy);
		}
		else
		{
			// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g)) {
				if ((posX + moduleW < g.getClipX())
						|| (posX > g.getClipX() + g.getClipWidth()))
					return;
				if ((posY + moduleH < g.getClipY() 
						|| (posY > g.getClipY() + g.getClipHeight())))
					return;
			}
			// Use the pre-module-mapped index because PaintModule will re-map it
			PaintModule(g,  originalIndex, posX, posY, (fm_flags&0x0F));
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Renders an effect over a tile.
	///
	/// &param g - The graphics context to render to.
	/// &param moduleID - The module to use in applying the effect, where the alpha data comes from.
	/// &param img - The image from which we can extract the pixel for the effect.
	/// &param effectType - The type of effect to apply.
	/// &param posX - the X position of the render location.
	/// &param posY - The Y position of the render location.
	/// &param w - The width of the render area.
	/// &param h - The height of the render area.
	/// &param flag - The transform flags to use when applying the effect.
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.tileset_usePixelEffects
	//------------------------------------------------------------------------------
	void RenderTilesetEffect (int moduleID, Image img, int effectType, int posX, int posY, int w, int h, int flag)
	{
		RenderTilesetEffect (GLLib.g,moduleID, img, effectType, posX, posY, w, h, flag);
	}
	void RenderTilesetEffect (Graphics g, int moduleID, Image img, int effectType, int posX, int posY, int w, int h, int flag)
	{
		if (DevConfig.tileset_usePixelEffects)
		{
			if (img == null)
			{
				Utils.Dbg("RenderTilesetEffect: Image is null! Layer effect is applied to must be on the backbuffer!");;
			}


			{

				int[] buffer = zSprite.GetPixelBuffer_int(null);
				GLLib.GetRGB(img, buffer, 0, w, posX, posY, w, h);

				RenderTilesetEffect (g, moduleID, buffer, effectType, posX, posY, w, h, flag);

			}
		}
	}


	//------------------------------------------------------------------------------
	/// Renders an effect over a tile.
	///
	/// &param g - The graphics context to render to.
	/// &param moduleID - The module to use in applying the effect, where the alpha data comes from.
	/// &param buffer - The pixels currently on the screen in this location.
	/// &param effectType - The type of effect to apply.
	/// &param posX - the X position of the render location.
	/// &param posY - The Y position of the render location.
	/// &param w - The width of the render area.
	/// &param h - The height of the render area.
	/// &param flag - The transform flags to use when applying the effect.
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.tileset_usePixelEffects
	//------------------------------------------------------------------------------
	void RenderTilesetEffect (int moduleID, int[] buffer, int effectType, int posX, int posY, int w, int h, int flag)
	{
		RenderTilesetEffect (GLLib.g,moduleID, buffer, effectType, posX, posY, w, h, flag);
	}
	void RenderTilesetEffect (Graphics g, int moduleID, int[] buffer, int effectType, int posX, int posY, int w, int h, int flag)
	{
		if (DevConfig.tileset_usePixelEffects)
		{
			if (_module_image_intAAA == null || _module_image_intAAA[0] == null)
			{
				Utils.Dbg("RenderTilesetEffect: Alpha Module " + moduleID + " does not have an int cache!!");;
			}

			int[] modulePixels = _module_image_intAAA[0][moduleID];

			if (modulePixels == null)
			{
				Utils.Dbg("RenderTilesetEffect: Alpha Module " + moduleID + " does not have an int cache!!");;
			}

//			int[] output = GLLib.PFX_ProcessPixelEffect(g, modulePixels, buffer, posX, posY, w, h, flag,
//														(effectType == zAnimPlayer.TILESET_EFFECT_ADDITIVE),
//														(effectType == zAnimPlayer.TILESET_EFFECT_MULTIPLICATIVE), false);
			int[] output = cPFX.PFX_ProcessPixelEffect(g, modulePixels, buffer, posX, posY, w, h, flag, false, false, false);
			
			GLLib.DrawRGB(g, output, 0, w, posX, posY, w, h, false, false, 0);
		}
	}


	//------------------------------------------------------------------------------
	/// Draws a scaled frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &param scale The scale to draw at (100 = 100% = normal)
	///
	/// &note Currently this is only supported for MIDP2 and NOKIA.
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.pfx_useSpriteEffectScale
	//------------------------------------------------------------------------------
	void PaintFrameScaled(int frame, int posX, int posY, int flags, int scale)
	{
		PaintFrameScaled(GLLib.g,frame,posX,posY,flags,scale);
	}
	void PaintFrameScaled(Graphics g, int frame, int posX, int posY, int flags, int scale)
	{
		if (DevConfig.pfx_enabled && DevConfig.pfx_useSpriteEffectScale)
		{
			cPFX.PFX_EnableEffect(GLPixEffects.k_EFFECT_SCALE);
			cPFX.PFX_SetParam(GLPixEffects.k_EFFECT_SCALE, GLPixEffects.k_PARAM_SCALE_PERCENT, scale);

			PaintFrame(g,  frame, posX, posY, flags, 0, 0);

			cPFX.PFX_DisableEffect(GLPixEffects.k_EFFECT_SCALE);
		}
		else
		{
			Utils.Dbg("PaintFrameScaled: Functionality is not enabled, you must set pfx_useSpriteEffectScale to TRUE!");;
		}
	}

	//------------------------------------------------------------------------------
	/// Draws a blended frame at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param frame The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &param alpha The alpha value to set for this sprite [0,255]
	///
	/// &note Currently this is only supported for MIDP2 and NOKIA.
	///
	/// &note You need to enable this feature in the GLLib configuration (disabled by default)
	/// &see zJYLibConfig.pfx_useSpriteEffectBlend
	//------------------------------------------------------------------------------
	void PaintFrameBlended(int frame, int posX, int posY, int flags, int alpha)
	{
		PaintFrameBlended(GLLib.g,frame,posX,posY,flags,alpha);
	}
	void PaintFrameBlended(Graphics g, int frame, int posX, int posY, int flags, int alpha)
	{
		if (DevConfig.pfx_useSpriteEffectBlend)
		{
			cPFX.PFX_EnableEffect(GLPixEffects.k_EFFECT_BLEND);
			cPFX.PFX_SetParam(GLPixEffects.k_EFFECT_BLEND, GLPixEffects.k_PARAM_BLEND_AMOUNT, alpha);

			PaintFrame(g,  frame, posX, posY, flags, 0, 0);

			cPFX.PFX_DisableEffect(GLPixEffects.k_EFFECT_BLEND);
		}
		else
		{
			Utils.Dbg("PaintFrameBlended: Functionality is not enabled, you must set pfx_useSpriteEffectBlend to TRUE!");;
		}
	}

	//------------------------------------------------------------------------------
 	/// Draws a cached frame if it exists. Returns true if it existed and was painted.
	///
	/// &param g The Graphics context
	/// &param frame The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	/// &return TRUE if the cached frame existing and was painted
	//------------------------------------------------------------------------------
	boolean PaintCachedFrame (int frame, int posX, int posY, int flags)
	{
		return PaintCachedFrame(GLLib.g,frame,posX,posY,flags);
	}
	boolean PaintCachedFrame (Graphics g, int frame, int posX, int posY, int flags)
	{
		boolean bPainted = false;

		int sizeX = GetFrameWidth(frame);
		int sizeY = GetFrameHeight(frame);
		int offsetX = GetFrameMinX(frame);
		int offsetY = GetFrameMinY(frame);

		if ((flags & FLAG_ROT_90) != 0)
		{
			int tmp;
			{(tmp) = (offsetX);(offsetX) = (offsetY);(offsetY) = (tmp);};
		}

		// Resolve flags/offsets in correct order (same as Aurora)
		if ((flags & FLAG_FLIP_X) != 0)
		{
			offsetX = -offsetX - sizeX;
		}

		if ((flags & FLAG_FLIP_Y) != 0)
		{
			offsetY = -offsetY - sizeY;
		}

		if ((flags & FLAG_ROT_90) != 0)
		{
			int tmp = offsetX;
			offsetX = -offsetY - sizeY;
			offsetY = tmp;
		}

		posX += offsetX;
		posY += offsetY;

		Image imgCFrame = GetFrameImage(frame, _crt_pal);

		if(imgCFrame != null)
		{


			flags = midp2_flags[flags & 0x07];

			if (flags == 0)
			{
				g.drawImage(imgCFrame, posX, posY, 0);
				bPainted = true;
			}
	
			else if (DevConfig.sprite_drawRegionFlippedBug)
			{
				
					Image image = Image.createImage(imgCFrame, 0, 0, sizeX, sizeY, flags);
					g.drawImage(image, posX, posY, 0);
					bPainted = true;

			}

			else
			{
				g.drawRegion(imgCFrame, 0, 0, sizeX, sizeY, flags, posX, posY, 0);
				bPainted = true;
			}

		}
		else
		{
			int[] imgCPIX = GetFrameRGB(frame, _crt_pal);

			if(imgCPIX != null)
			{
				GLLib.DrawRGB(g, imgCPIX, 0, sizeX, posX, posY, sizeX, sizeY, true, true, flags);
				bPainted = true;
			}
		}

		return bPainted;
	}

	
//--------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------
//
// ASprite_Blit.jpp
//
// Handles low-level rendering operations (support alpha fully + clipping + bounds)
// 		- BlitARGB
//      - FillRectARGB
//      - DrawRectARGB
//      - CopyArea
//      - DrawLineARGB
//
// TODO: Add offset + scanlength param to BlitARGB.                       [MMZ] 10-28-2009
// TODO: Implement CopyArea using BlitARGB once above todo is done.       [MMZ] 10-28-2009
// TODO: Implement short version of these functions for NOKIA versions.   [MMZ] 10-28-2009
//
//--------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------
/// Blits one int[] image onto a destination int[] image.
///   - Supports clipping (checks global graphics for clip info)
///   - Supports out-of-bounds (treated as extra clip)
///   - Optimized depending on alpha states in both source and destination
///   - Supports transformations on blit!
///
/// &param dst  - the pixel buffer into which we want to blit (the destination)
/// &param dstW - the width of the pixel buffer
/// &param dstH - the height of the pixel buffer
/// &param dstA - the state of the alpha-channel in the pixel buffer
///
/// &param posX - the location within the pixel buffer where to blit the source
/// &param posY - the location within the pixel buffer where to blit the source
///
/// &param src  - the source which we want to blit onto the destination
/// &param srcW - the width of the source
/// &param srcH - the height of the source
/// &param srcA - the state of the alpha-channel in the source
///
/// &param flags - the transform flags to use when doing the blit
//------------------------------------------------------------------------------
static void BlitARGB (int[] dst, int dstW, int dstH, int dstA, int posX, int posY, int[] src, int srcW, int srcH, int srcA, int flags)
{
	if(!(dst != null))Utils.DBG_PrintStackTrace(false, "BlitARGB: Target is null!");;
	if(!(src != null))Utils.DBG_PrintStackTrace(false, "BlitARGB: Source is null!");;
	if(!(dst != src))Utils.DBG_PrintStackTrace(false, "BlitARGB: Target is same as Source!");;

	if(!(dst.length >= (dstW * dstH)))Utils.DBG_PrintStackTrace(false, "BlitARGB: Target array is smaller than the dimensions indicate!");;
	if(!(src.length >= (srcW * srcH)))Utils.DBG_PrintStackTrace(false, "BlitARGB: Target array is smaller than the dimensions indicate!");;

	if (srcW <= 0 || srcH <= 0)
	{
		return;
	}

	// Loop variables but declare here so we can use as temp vars first...
	int x, y;

	// Swap source Width and Height
	if ((flags & FLAG_ROT_90) != 0 )
	{
		{(x) = (srcW);(srcW) = (srcH);(srcH) = (x);};
	}

	// Check DESTINATIONS BOUNDS and limit BLIT area accordingly
	int startX = ((posX)>(0)?(posX):(0));
	int endX   = ((posX + srcW)<(dstW)?(posX + srcW):(dstW));
	int startY = ((posY)>(0)?(posY):(0));
	int endY   = ((posY + srcH)<(dstH)?(posY + srcH):(dstH));

	// Check CLIP BOUNDS and limit BLIT area accordingly
	startX = ((startX)>(_customGraphicsClipX)?(startX):(_customGraphicsClipX));
	endX   = ((endX)<(_customGraphicsClipX + _customGraphicsClipW)?(endX):(_customGraphicsClipX + _customGraphicsClipW));

	startY = ((startY)>(_customGraphicsClipY)?(startY):(_customGraphicsClipY));
	endY   = ((endY)<(_customGraphicsClipY + _customGraphicsClipH)?(endY):(_customGraphicsClipY + _customGraphicsClipH));

	// Anything left after limiting the BLIT area?
	if (startX > endX || startY > endY)
	{
		return;
	}

	// Prepare index-info...
	int scanW       = endX - startX;
	int scanH       = endY - startY;
	int srcScanStep = srcW - scanW;

	int dstStep     = 0;
	int dstScanStep = 0;
	int dstIndex    = 0;
	int srcIndex    = 0;

	// Mask to only relavent bits
	flags &= (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90);

	if ((flags & FLAG_ROT_90) != 0 )
	{
		{(x) = (scanW);(scanW) = (scanH);(scanH) = (x);};
		{(x) = (srcW);(srcW) = (srcH);(srcH) = (x);};
		srcScanStep = srcW - scanW;
	}

	// Alter these to store the W,H of the BLIT AREA
	if (flags == 0)
	{
		dstStep     = 1;
		dstScanStep = dstW - scanW;
		dstIndex    = ((startY       ) * dstW) + (startX       ) - 1;
		srcIndex    = ((startY - posY) * srcW) + (startX - posX) - 1;
	}
	else if (flags == FLAG_FLIP_X)
	{
		dstStep     = -1;
		dstScanStep = dstW + scanW;
		dstIndex    = ((startY       ) * dstW) + (endX);
		srcIndex    = ((startY - posY) * srcW) + (srcW - (endX - posX)) - 1;
	}
	else if (flags == FLAG_FLIP_Y)
	{
		dstStep     = 1;
		dstScanStep = -(dstW + scanW);
        dstIndex    = ((endY - 1)             * dstW) + (startX       ) - 1;
        srcIndex    = ((srcH - (endY - posY)) * srcW) + (startX - posX) - 1;
	}
	else if (flags == (FLAG_FLIP_X | FLAG_FLIP_Y))
	{
		dstStep     = -1;
		dstScanStep = -(dstW - scanW);
        dstIndex    = ((endY - 1)             * dstW) + endX;
        srcIndex    = ((srcH - (endY - posY)) * srcW) + (srcW - (endX - posX)) - 1;
	}
	else if (flags == FLAG_ROT_90)
	{
		dstStep     = dstW;
		dstScanStep = -(dstW * scanW) - 1;
		dstIndex    = ((startY - 1)           * dstW) + (endX         ) - 1;
		srcIndex    = ((srcH - (endX - posX)) * srcW) + (startY - posY) - 1;
	}
	else if (flags == (FLAG_ROT_90 | FLAG_FLIP_X))
	{
		dstStep     = -dstW;
		dstScanStep = (dstW * scanW) - 1;
		dstIndex    = ((endY)                 * dstW) + (endX                ) - 1;
		srcIndex    = ((srcH - (endX - posX)) * srcW) + (srcW - (endY - posY)) - 1;
	}
	else if (flags == (FLAG_ROT_90 | FLAG_FLIP_Y))
	{
		dstStep     = dstW;
		dstScanStep = -(dstW * scanW) + 1;
		dstIndex    = ((startY - 1   ) * dstW) + (startX);
		srcIndex    = ((startX - posX) * srcW) + (startY - posY) - 1;
	}
	else if (flags == (FLAG_ROT_90 | FLAG_FLIP_X | FLAG_FLIP_Y))
	{
		dstStep     = -dstW;
		dstScanStep = (dstW * scanW) + 1;
		dstIndex    = ((endY)          * dstW) + (startX);
		srcIndex    = ((startX - posX) * srcW) + (srcW - (endY - posY)) - 1;
	}

	y = scanH;

	// Source is known to be FULLY OPAQUE... just overwrite the destination!
	if (srcA == ALPHA_NONE)
	{
		while (--y >= 0)
		{
			x = scanW;

			while (--x >= 0)
			{
				dstIndex += dstStep;
				dst[dstIndex] = src[++srcIndex];
			}

			dstIndex += dstScanStep;
			srcIndex += srcScanStep;
		}
	}
	// Source is known to be FULLY OPAQUE or FULLY TRANSPARENT
	else if (srcA == ALPHA_TRANSPARENT)
	{
		while (--y >= 0)
		{
			x = scanW;

			while (--x >= 0)
			{
				dstIndex += dstStep;
				++srcIndex;

				// If not fully transparent assume fully opaque
				if ((src[srcIndex] & 0xFF000000) != 0)
				{
					dst[dstIndex] = src[srcIndex];
				}
			}

			dstIndex += dstScanStep;
			srcIndex += srcScanStep;
		}
	}
	// Destination is known to be FULLY OPAQUE...
	else if (dstA == ALPHA_NONE)
	{
		int a1, c0, c1, R;

		while (--y >= 0)
		{
			x = scanW;

			while (--x >= 0)
			{
				dstIndex += dstStep;

				// Get alpha value
				a1 = (src[++srcIndex] >> 24) & 0xFF;

				// Source      is fully OPAQUE....... just overwrite!
				if(a1 == 0xFF)
				{
					dst[dstIndex] =	src[srcIndex];
				}
				// Source is fully TRANSPARENT... nothing to write... do nothing!
				else if (a1 == 0)
				{
					// NO ACTION!
				}
				// Destination is fully OPAQUE, simpler computation (BLEND!)
				else
				{
					// Result will be fully opaque...
					R = 0xFF000000;

					// R and B Channels
					c0 = dst[dstIndex] & 0x00FF00FF;
					c1 = src[srcIndex] & 0x00FF00FF;

					R |= ((c0 + ((((c1 - c0) * a1)) >> 8)) & 0x00FF00FF);

					// G Channel
					c0 = dst[dstIndex] & 0x0000FF00;
					c1 = src[srcIndex] & 0x0000FF00;

					R |= ((c0 + ((((c1 - c0) * a1)) >> 8)) & 0x0000FF00);

					dst[dstIndex] = R;
				}
			}

			dstIndex += dstScanStep;
			srcIndex += srcScanStep;
		}
	}
	else
	{
		int a0, a1, c0, c1, A, R, G, B;

		while (--y >= 0)
		{
			x = scanW;

			while (--x >= 0)
			{
				dstIndex += dstStep;
				++srcIndex;

				// Get the 2 alpha values
				a0 = (dst[dstIndex] >> 24) & 0xFF;
				a1 = (src[srcIndex] >> 24) & 0xFF;

				// Destination is fully TRANSPARENT.. just overwrite!
				// Source      is fully OPAQUE....... just overwrite!
				if((a0 == 0) || (a1 == 0xFF))
				{
					dst[dstIndex] =	src[srcIndex];
				}
				// Source is fully TRANSPARENT... nothing to write... do nothing!
				else if (a1 == 0)
				{
					// NO ACTION!
				}
				// Destination is fully OPAQUE, simpler computation (BLEND!)
				else if (a0 == 0xFF)
				{
					// Result will be fully opaque...
					R = 0xFF000000;

					// R and B Channels
					c0 = dst[dstIndex] & 0x00FF00FF;
					c1 = src[srcIndex] & 0x00FF00FF;

					R |= ((c0 + ((((c1 - c0) * a1)) >> 8)) & 0x00FF00FF);

					// G Channel
					c0 = dst[dstIndex] & 0x0000FF00;
					c1 = src[srcIndex] & 0x0000FF00;

					R |= ((c0 + ((((c1 - c0) * a1)) >> 8)) & 0x0000FF00);

					dst[dstIndex] = R;
				}
				// Both Destination and Source have alpha values none 0 or 0xFF... worst case!
				else
				{
					A = ((0xFF * 0xFF)  - ((0xFF - a0) * (0xFF - a1))) >> 8;

					if (A == 0)
					{
						dst[dstIndex] = 0;
					}
					else
					{
						// Pre-compute... store in a0 since not used after this...
						a0 = (0xFF - a1) * a0;

						// R Channel
						c0 = (dst[dstIndex] >> 16) & 0xFF;
						c1 = (src[srcIndex] >> 16) & 0xFF;

						R  = ((c1 * a1) + ((a0 * c0)>>8)) / A;

						// G Channel
						c0 = (dst[dstIndex] >> 8) & 0xFF;
						c1 = (src[srcIndex] >> 8) & 0xFF;

						G  = ((c1 * a1) + ((a0 * c0)>>8)) / A;

						// B Channel
						c0 = dst[dstIndex] & 0xFF;
						c1 = src[srcIndex] & 0xFF;

						B  = ((c1 * a1) + ((a0 * c0)>>8)) / A;

						if (R > 0xFF) R = 0xFF;
						if (G > 0xFF) G = 0xFF;
						if (B > 0xFF) B = 0xFF;

						// Combine channels
						dst[dstIndex] = (A << 24) | (R << 16) | (G << 8) | (B);
					}
				}
			}

			dstIndex += dstScanStep;
			srcIndex += srcScanStep;
		}
	}
}

//------------------------------------------------------------------------------
/// Draws an ARGB color rect into a pixel buffer
///   - Supports clipping (checks global graphics for clip info)
///   - Supports out-of-bounds (treated as extra clip)
///   - Optimized depending on alpha states in both source and destination
///
/// &param dst  - the pixel buffer into which we want to blit (the destination)
/// &param dstW - the width of the pixel buffer
/// &param dstH - the height of the pixel buffer
/// &param dstA - the state of the alpha-channel in the pixel buffer
///
/// &param x - the location within the pixel buffer where to draw the rect
/// &param y - the location within the pixel buffer where to draw the rect
/// &param w - the width of the rect (will be clipped if out of destination)
/// &param h - the height of the rect (will be clipped if out of destination)
/// &param color - the color for the rect (ARGB)
//------------------------------------------------------------------------------
static void FillRectARGB (int[] dst, int dstW, int dstH, int dstA, int x, int y, int w, int h, int color)
{
	if(!(dst != null))Utils.DBG_PrintStackTrace(false, "FillRectARGB: Target is null!");;
	if(!(dst.length >= (dstW * dstH)))Utils.DBG_PrintStackTrace(false, "FillRectARGB: Target array is smaller than the dimensions indicate!");;

	if (w <= 0 || h <= 0)
	{
		return;
	}

	// Check DESTINATIONS BOUNDS and limit BLIT area accordingly
	int startX = ((x)>(0)?(x):(0));
	int endX   = ((x + w)<(dstW)?(x + w):(dstW));
	int startY = ((y)>(0)?(y):(0));
	int endY   = ((y + h)<(dstH)?(y + h):(dstH));

	// Check CLIP BOUNDS and limit BLIT area accordingly
	startX = ((startX)>(_customGraphicsClipX)?(startX):(_customGraphicsClipX));
	endX   = ((endX)<(_customGraphicsClipX + _customGraphicsClipW)?(endX):(_customGraphicsClipX + _customGraphicsClipW));

	startY = ((startY)>(_customGraphicsClipY)?(startY):(_customGraphicsClipY));
	endY   = ((endY)<(_customGraphicsClipY + _customGraphicsClipH)?(endY):(_customGraphicsClipY + _customGraphicsClipH));

	// Anything left after limiting the BLIT area?
	if (startX > endX || startY > endY)
	{
		return;
	}

	int alpha = (color >> 24) & 0xFF;

	// Fully transparent? Won't have effect...
	if (alpha == 0)
	{
		return;
	}

	// Prepare index-info...
	int scanW       = endX - startX;
	int scanH       = endY - startY;

	int dstScanStep = dstW - scanW;
	int dstIndex    = (startY * dstW) + (startX) - 1;

	y = scanH;

	// Color is FULLY OPAQUE... just overwrite the destination!
	if (alpha == 0xFF)
	{
		while (--y >= 0)
		{
			x = scanW;

			while (--x >= 0)
			{
				dst[++dstIndex] = color;
			}

			dstIndex += dstScanStep;
		}
	}
	else
	{
		int c1 = color & 0x00FF00FF;
		int c2 = color & 0x0000FF00;
		int R;

		// Destination is known to be FULLY OPAQUE...
		if (dstA == ALPHA_NONE)
		{
			alpha = 0xFF - alpha;

			while (--y >= 0)
			{
				x = scanW;

				while (--x >= 0)
				{
					++dstIndex;

					// Result will be fully opaque...
					R = 0xFF000000;

					// R and B Channels
					R |= ((c1 + (((((dst[dstIndex] & 0x00FF00FF) - c1) * alpha)) >> 8)) & 0x00FF00FF);
					// G Channel
					R |= ((c2 + (((((dst[dstIndex] & 0x0000FF00) - c2) * alpha)) >> 8)) & 0x0000FF00);

					dst[dstIndex] = R;
				}

				dstIndex += dstScanStep;
			}
		}
		else
		{
			int a0, c0, A, G, B;

			while (--y >= 0)
			{
				x = scanW;

				while (--x >= 0)
				{
					++dstIndex;

					// Get the alpha values
					a0 = (dst[dstIndex] >> 24) & 0xFF;

					// Destination is fully TRANSPARENT.. just overwrite!
					if (a0 == 0)
					{
						dst[dstIndex] =	color;
					}
					// Destination is fully OPAQUE, simpler computation (BLEND!)
					else if (a0 == 0xFF)
					{
						alpha = 0xFF - alpha;

						// Result will be fully opaque...
						R = 0xFF000000;

						// R and B Channels
						R |= ((c1 + (((((dst[dstIndex] & 0x00FF00FF) - c1) * alpha)) >> 8)) & 0x00FF00FF);
						// G Channel
						R |= ((c2 + (((((dst[dstIndex] & 0x0000FF00) - c2) * alpha)) >> 8)) & 0x0000FF00);

						dst[dstIndex] = R;
					}
					// Both Destination and Source have alpha values none 0 or 0xFF... worst case!
					else
					{
						A = ((0xFF * 0xFF)  - ((0xFF - a0) * (0xFF - alpha))) >> 8;

						if (A == 0)
						{
							dst[dstIndex] = 0;
						}
						else
						{
							// Pre-compute... store in a0 since not used after this...
							a0 = (0xFF - alpha) * a0;

							// R Channel
							c0 = (dst[dstIndex] >> 16) & 0xFF;
							R  = ((((color >> 16) & 0xFF) * alpha) + ((a0 * c0)>>8)) / A;

							// G Channel
							c0 = (dst[dstIndex] >> 8) & 0xFF;
							G  = ((((color >> 8)  & 0xFF) * alpha) + ((a0 * c0)>>8)) / A;

							// B Channel
							c0 = dst[dstIndex] & 0xFF;
							B  = (((color         & 0xFF) * alpha) + ((a0 * c0)>>8)) / A;

							if (R > 0xFF) R = 0xFF;
							if (G > 0xFF) G = 0xFF;
							if (B > 0xFF) B = 0xFF;

							// Combine channels
							dst[dstIndex] = (A << 24) | (R << 16) | (G << 8) | (B);
						}
					}
				}

				dstIndex += dstScanStep;
			}
		}
	}
}

//------------------------------------------------------------------------------
/// Draws an ARGB color rect into a pixel buffer
///   - Supports clipping (checks global graphics for clip info)
///   - Supports out-of-bounds (treated as extra clip)
///   - Optimized depending on alpha states in both source and destination
///
/// &param dst  - the pixel buffer into which we want to blit (the destination)
/// &param dstW - the width of the pixel buffer
/// &param dstH - the height of the pixel buffer
/// &param dstA - the state of the alpha-channel in the pixel buffer
///
/// &param x - the location within the pixel buffer where to draw the rect
/// &param y - the location within the pixel buffer where to draw the rect
/// &param w - the width of the rect (will be clipped if out of destination)
/// &param h - the height of the rect (will be clipped if out of destination)
/// &param color - the color for the rect (ARGB)
//------------------------------------------------------------------------------
static void DrawRectARGB (int[] dst, int dstW, int dstH, int dstA, int x, int y, int w, int h, int color)
{
	//FillRectARGB(dst, dstW, dstH, dstA, x    , y    , w, 1, color);
	//FillRectARGB(dst, dstW, dstH, dstA, x    , y + h, w, 1, color);
	//FillRectARGB(dst, dstW, dstH, dstA, x    , y    , 1, h, color);
	//FillRectARGB(dst, dstW, dstH, dstA, x + w, y    , 1, h + 1, color);
	DrawLineARGB(dst, dstW, dstH, dstA, x    , y    , x + w, y    , color);
	DrawLineARGB(dst, dstW, dstH, dstA, x    , y    , x    , y + h, color);
	DrawLineARGB(dst, dstW, dstH, dstA, x + w, y    , x + w, y + h, color);
	DrawLineARGB(dst, dstW, dstH, dstA, x    , y + h, x + w, y + h, color);
}

//------------------------------------------------------------------------------
/// Copies one int[] area onto another destination int[].
///   - Supports out-of-bounds
///
/// &param src  - the source which we want to blit from onto the destination
/// &param srcW - the width of the source
/// &param srcH - the height of the source
///
/// &param dst  - the pixel buffer into which we want to blit into
/// &param offset - the offset into the dst array where to start copying
/// &param scanlength - the distance in pixels between 2 rows
///
/// &param x - the location within the src buffer where to start copying
/// &param y - the location within the src buffer where to start copying
/// &param dstW - the width of the area to copy (will be clipped if outside of src area)
/// &param dstH - the height of the area to copy (will be clipped if outside of src area)
///
/// TODO: Implement this by using BlitARGB once it supports offset and scanlength parameters.
//------------------------------------------------------------------------------
static void CopyArea (int[] src, int srcW, int srcH, int[] dst, int offset, int scanlength, int x, int y, int dstW, int dstH)
{
	if(!(dst != null))Utils.DBG_PrintStackTrace(false, "CopyArea: Target is null!");;
	if(!(src != null))Utils.DBG_PrintStackTrace(false, "CopyArea: Source is null!");;
	if(!(dst != src))Utils.DBG_PrintStackTrace(false, "CopyArea: Target is same as Source!");;

	if(!(dst.length >= (dstW * dstH)))Utils.DBG_PrintStackTrace(false, "CopyArea: Target array is smaller than the dimensions indicate!");;
	if(!(src.length >= (srcW * srcH)))Utils.DBG_PrintStackTrace(false, "CopyArea: Target array is smaller than the dimensions indicate!");;

	// Check DESTINATIONS BOUNDS and limit COPY area accordingly
	int startX = ((x)>(0)?(x):(0));
	int endX   = ((x + dstW)<(srcW)?(x + dstW):(srcW));
	int startY = ((y)>(0)?(y):(0));
	int endY   = ((y + dstH)<(srcH)?(y + dstH):(srcH));

	// Anything left after limiting the COPY area?
	if (startX > endX || startY > endY)
	{
		return;
	}

	// Loop variables but declare here so we can use as temp vars first...
	int dstIndex = (    dstW - (endX - startX)) + (    (dstH - (endY - startY)) * scanlength) - 1 + offset;
	int srcIndex = (x + dstW - (endX - startX)) + ((y + dstH - (endY - startY)) * srcW      ) - 1;

	dstW = endX - startX;
	y    = endY - startY;

	while (--y >= 0)
	{
		x = dstW;

		while (--x >= 0)
		{
			dst[++dstIndex] = src[++srcIndex];
		}

		dstIndex += (scanlength - dstW);
		srcIndex += (srcW       - dstW);
	}
}

//------------------------------------------------------------------------------
/// Draws an ARGB line onto a pixel array graphics context.
///   - Supports out-of-bounds
///   - Supports clipping
///   - Supports any line color alpha and source with any alpha state
///
/// &param dst  - the pixel buffer into which we want to blit (the destination)
/// &param dstW - the width of the pixel buffer
/// &param dstH - the height of the pixel buffer
/// &param dstA - the state of the alpha-channel in the pixel buffer
///
/// &param x0 - X position of the starting point
/// &param y0 - Y position of the starting point
/// &param x1 - X position of the ending point
/// &param y1 - Y position of the ending point
/// &param color - the color for the line (ARGB)
//------------------------------------------------------------------------------
static void DrawLineARGB (int[] dst, int dstW, int dstH, int dstA, int x0, int y0, int x1, int y1, int color)
{
	if(!(dst != null))Utils.DBG_PrintStackTrace(false, "DrawLineARGB: Target is null!");;
	if(!(dst.length >= (dstW * dstH)))Utils.DBG_PrintStackTrace(false, "DrawLineARGB: Target array is smaller than the dimensions indicate!");;

	int srcA = (color >> 24) & 0xFF;

	// Draw Color is completely transparent... no effect!
	if (srcA == 0)
	{
		return;
	}

	int minX = ((_customGraphicsClipX)>(0)?(_customGraphicsClipX):(0));
	int maxX = ((_customGraphicsClipX + _customGraphicsClipW)<(dstW)?(_customGraphicsClipX + _customGraphicsClipW):(dstW));
	int minY = ((_customGraphicsClipY)>(0)?(_customGraphicsClipY):(0));
	int maxY = ((_customGraphicsClipY + _customGraphicsClipH)<(dstH)?(_customGraphicsClipY + _customGraphicsClipH):(dstH));

	// Make max endpoints inclusive, like min's
	maxX--;
	maxY--;

	// Quick AABB check
	if (
		(((x0)>(x1)?(x0):(x1)) < minX) ||
	    (((x0)<(x1)?(x0):(x1)) > maxX) ||
	    (((y0)>(y1)?(y0):(y1)) < minY) ||
	    (((y0)<(y1)?(y0):(y1)) > maxY)
	   )
	{
		return;
	}

	int dx = x1 - x0;
	int dy = y1 - y0;
	int tmp;
	boolean steep = false;

	if (((dy)<0 ? -(dy) : (dy)) > ((dx)<0 ? -(dx) : (dx)))
	{
		steep = true;
		{(tmp) = (x0);(x0) = (y0);(y0) = (tmp);};
		{(tmp) = (x1);(x1) = (y1);(y1) = (tmp);};

		{(tmp) = (minX);(minX) = (minY);(minY) = (tmp);};
		{(tmp) = (maxX);(maxX) = (maxY);(maxY) = (tmp);};
	}

	if (x0 > x1)
	{
		{(tmp) = (x0);(x0) = (x1);(x1) = (tmp);};
		{(tmp) = (y0);(y0) = (y1);(y1) = (tmp);};
	}

	dx = x1 - x0;
	dy = ((y1 - y0)<0 ? -(y1 - y0) : (y1 - y0));
	int error = dx / 2;
	int ystep = (y0 < y1) ? 1 : -1;

	// Skip portion out-of-bounds (negative-X)
	if (x0 < minX)
	{
		// Go from X0 to minX
		for( int x = x0; x < minX; x++)
		{
			error -= dy;

			if (error < 0)
			{
				y0 += ystep;
				error += dx;
			}
		}

		// Starting at minX!
		x0 = minX;
	}

	// Make sure y gets into bounds as well (negative-Y)
	while(y0 < minY || y0 > maxY)
	{
		x0++;
		error -= dy;

		if (error < 0)
		{
			y0 += ystep;
			error += dx;
		}

		// ASSERT: Loop ends! We can assume this since AABB check of line vs clip/bounds passed
	}

	// Skip portion out-of-bounds (positive-X)
	x1 = ((x1)<(maxX)?(x1):(maxX));

	// Convert to length (so we can compare to 0)
	x1 -= (x0 - 1);

	int xIndexStep, yIndexStep, index;

	if (steep)
	{
		index      = (x0 * dstW) + y0;
		xIndexStep = dstW;
		yIndexStep = ystep;
	}
	else
	{
		index      = (y0 * dstW) + x0;
		xIndexStep = 1;
		yIndexStep = (ystep > 0) ? dstW : -dstW;
	}

	// Draw Color is OPAQUE... can just write color directly with no check
	if (srcA == 0xFF)
	{
		while (--x1 >= 0)
		{
			// Color Update ------------------------------
			dst[index] = color;

			// Index Updates -----------------------------
			index += xIndexStep;
			error -= dy;

			if (error < 0)
			{
				y0    += ystep;
				index += yIndexStep;
				error += dx;

				// Skip portion out-of-bounds (positive-Y)
				if (y0 < minY || y0 > maxY)
				{
					break;
				}
			}
		}
	}
	// Source Color has alpha!
	else
	{
		// Target is Opaque... normal alpha blend
		if (dstA == ALPHA_NONE)
		{
			int R;

			while (--x1 >= 0)
			{
				// Color Update ------------------------------
				// Result will be fully opaque... A Channel
				R = 0xFF000000;

				// R and B Channels
				tmp = dst[index] & 0x00FF00FF;
				R |= ((tmp + (((((color & 0x00FF00FF) - tmp) * srcA)) >> 8)) & 0x00FF00FF);

				// G Channel
				tmp = dst[index] & 0x0000FF00;
				R |= ((tmp + (((((color & 0x0000FF00) - tmp) * srcA)) >> 8)) & 0x0000FF00);

				dst[index] = R;

				// Index Updates -----------------------------
				index += xIndexStep;
				error -= dy;

				if (error < 0)
				{
					y0    += ystep;
					index += yIndexStep;
					error += dx;

					// Skip portion out-of-bounds (positive-Y)
					if (y0 < minY || y0 > maxY)
					{
						break;
					}
				}
			}
		}
		// Target has alpha!!!! Ekkk! Complex operation... :(
		else
		{
			//DBG("DrawLineARGB: Attempting to Draw an alpha line onto an alpha surface! Implementation is TODO.");

			int R, G, B, A, c0, c1;

			while (--x1 >= 0)
			{
				// Color Update ------------------------------
				// Get the alpha value of dst here
				int a0 = (dst[index] >> 24) & 0xFF;

				// Destination is fully TRANSPARENT.. just overwrite!
				if(a0 == 0)
				{
					dst[index] = color;
				}
				// Destination is fully OPAQUE, simpler computation (BLEND!)(same as in other case above)
				else if (a0 == 0xFF)
				{
					// Result will be fully opaque...
					R = 0xFF000000;

					// R and B Channels
					c0 = dst[index] & 0x00FF00FF;
					R |= ((c0 + (((((color & 0x00FF00FF) - c0) * srcA)) >> 8)) & 0x00FF00FF);

					// G Channel
					c0 = dst[index] & 0x0000FF00;
					R |= ((c0 + (((((color & 0x0000FF00) - c0) * srcA)) >> 8)) & 0x0000FF00);

					dst[index] = R;
				}
				// Both Destination and Source have alpha values none 0 or 0xFF... worst case!
				else
				{
					A = ((0xFF * 0xFF)  - ((0xFF - a0) * (0xFF - srcA))) >> 8;

					if (A == 0)
					{
						dst[index] = 0;
					}
					else
					{
						// Pre-compute... store in a0 since not used after this...
						a0 = (0xFF - srcA) * a0;

						// R Channel
						c0 = (dst[index] >> 16) & 0xFF;
						c1 = (color >> 16) & 0xFF;

						R  = ((c1 * srcA) + ((a0 * c0)>>8)) / A;

						// G Channel
						c0 = (dst[index] >> 8) & 0xFF;
						c1 = (color >> 8) & 0xFF;

						G  = ((c1 * srcA) + ((a0 * c0)>>8)) / A;

						// B Channel
						c0 = dst[index] & 0xFF;
						c1 = color & 0xFF;

						B  = ((c1 * srcA) + ((a0 * c0)>>8)) / A;

						if (R > 0xFF) R = 0xFF;
						if (G > 0xFF) G = 0xFF;
						if (B > 0xFF) B = 0xFF;

						// Combine channels
						dst[index] = (A << 24) | (R << 16) | (G << 8) | (B);
					}
				}

				// Index Updates -----------------------------
				index += xIndexStep;
				error -= dy;

				if (error < 0)
				{
					y0    += ystep;
					index += yIndexStep;
					error += dx;

					// Skip portion out-of-bounds (positive-Y)
					if (y0 < minY || y0 > maxY)
					{
						break;
					}
				}
			}
		}
	}
}


    private int LoadModules_NI( int offset, byte[] file )
    {
        // Modules...
    	int _nModules = this._nModules = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
		short[] _modules_x_short = null;
		short[] _modules_y_short = null;
    	byte[] _modules_x_byte;
    	byte[] _modules_y_byte;
    	short[] _modules_w_short;
    	short[] _modules_h_short;
    	byte[] _modules_w_byte;
    	byte[] _modules_h_byte;
    	byte[] _module_types;
    	byte[] _module_colors_byte;
    	int[] _module_colors_int;
    	
		if (DevConfig.sprite_debugLoading){Utils.Dbg("nModules = " + _nModules);;};

		if (_nModules > 0)
		{
			if (DevConfig.sprite_useModuleXYShort && (!DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
			{
				_modules_x_short = this._modules_x_short = new short[_nModules];
				_modules_y_short = this._modules_y_short = new short[_nModules];
			}
			else if (DevConfig.sprite_useModuleXY && (!DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_XY) != 0)))
			{
				_modules_x_byte = this._modules_x_byte = new byte[_nModules];
				_modules_y_byte = this._modules_y_byte = new byte[_nModules];
			}

			if (DevConfig.sprite_useModuleWHShort)
			{
				_modules_w_short = this._modules_w_short  = new short[_nModules];
				_modules_h_short = this._modules_h_short  = new short[_nModules];
			}
			else
			{
				_modules_w_byte = this._modules_w_byte  = new byte[_nModules];
				_modules_h_byte = this._modules_h_byte  = new byte[_nModules];
			}


            if( DevConfig.sprite_useMultipleModuleTypes )
            {
            	_module_types = this._module_types = new byte[_nModules];

                if( DevConfig.sprite_useModuleColorAsByte )
                	_module_colors_byte = this._module_colors_byte = new byte[ _nModules ];
                else
                	_module_colors_int = this._module_colors_int  = new int[ _nModules ];
            }


            if( DevConfig.sprite_useMultipleModuleTypes )
            {
                //_module_types[ i ] = MD_IMAGE;

                if(  (! DevConfig.sprite_useBSpriteFlags)||
                     ((_bs_flags & BS_MODULES_IMG) != 0) )
                {

                    ;{ System.arraycopy(file, offset, _module_types, 0, _nModules ); offset += _nModules; };
                    /*
		            if( ReadUBYTE_NoInc() == 0xFF )
                    {
                        ReadINC_CURSOR( 1 );

			            _module_types[ i ] = MD_RECT;

                        if( zJYLibConfig.sprite_useModuleColorAsByte )
                        {
			                _module_colors_byte[ i ] = ReadBYTE();
                            ReadINC_CURSOR( 3 );
                        }else
                        {
			                _module_colors_int[ i ] = ReadINT();
                        }

                    } else
                    if( ReadUBYTE_NoInc() == 0xFE )
                    {
                        ReadINC_CURSOR( 1 );

                        _module_types[ i ] = MD_FILL_RECT;

                        if( zJYLibConfig.sprite_useModuleColorAsByte )
                        {
			                _module_colors_byte[ i ] = ReadBYTE();
                            ReadINC_CURSOR( 3 );
                        }else
                        {
			                _module_colors_int[ i ] = ReadINT();
                        }
                    }
                    */
                }
            }

			if (DevConfig.sprite_useModuleXYShort && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0)))
			{
				offset = readArray2Short( file, offset, _modules_x_short, 0, _nModules, false, false );
                offset = readArray2Short( file, offset, _modules_y_short, 0, _nModules, false, false );
			}
			else if (DevConfig.sprite_useModuleXY && ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0)))
			{
				;{ System.arraycopy(file, offset, _modules_x_byte, 0, _nModules ); offset += _nModules; };
                ;{ System.arraycopy(file, offset, _modules_y_byte, 0, _nModules ); offset += _nModules; };
			}

			if (DevConfig.sprite_useModuleWHShort)
			{
				if(	( DevConfig.sprite_useBSpriteFlags) &&
                    ((_bs_flags & BS_MODULES_WH_SHORT) == 0) )
				{
                    offset = readArray2Short( file, offset, _modules_w_short, 0, _nModules, true, true);
                    offset = readArray2Short( file, offset, _modules_h_short, 0, _nModules, true, true);
				}
				else
				{
                    offset = readArray2Short( file, offset, _modules_w_short, 0, _nModules, false, false );
                    offset = readArray2Short( file, offset, _modules_h_short, 0, _nModules, false, false );
				}
			}
			else
			{
                ;{ System.arraycopy(file, offset, _modules_w_byte, 0, _nModules ); offset += _nModules; };
                ;{ System.arraycopy(file, offset, _modules_h_byte, 0, _nModules ); offset += _nModules; };
			}
		}

		if( DevConfig.sprite_useModuleUsageFromSprite )
		{
            // Module usage
	        // Module images transf. flags that can be applied at runtime:
	        //	final static byte FLAG_FLIP_X	= 0x01;
	        //	final static byte FLAG_FLIP_Y	= 0x02;
	        //	final static byte FLAG_ROT_90	= 0x04;
	        // Order of the pretransformed images loaded for each module:
	        // Values:
	        //	1: an image w/ the respective transf. was loaded;
	        //	0: the module doesn't have an image w/ the respective transformation(s);
	        // 00000001: normal, no transformations
	        // 00000010: Fx
	        // 00000100: Fy
	        // 00001000: Fx Fy
	        // 00010000: R
	        // 00100000: R Fx
	        // 01000000: R Fy
	        // 10000000: R Fx Fy

			if (DevConfig.sprite_debugLoading){Utils.Dbg("Module usage... offset = " + offset + " _nModules = " + _nModules);;};

			if ( !DevConfig.sprite_useBSpriteFlags || ((_bs_flags & BS_MODULES_USAGE) != 0))
			{
				_modules_usage = new byte[ _nModules ];

                ;{ System.arraycopy(file, offset, _modules_usage, 0, _nModules ); offset += _nModules; };
			}
		} // if (zJYLibConfig.sprite_useModuleUsageFromSprite)

        return offset;
    }
    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

    private int LoadFModules_NI( int offset, byte[] file )
    {
        // FModules...
		int nFModules = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nFModules = " + nFModules);;};

		if (nFModules > 0)
		{
			{
				short[] _fmodules_id_short = null;
				byte[] _fmodules_id_byte = null;
				short[] _fmodules_ox_short;
				short[] _fmodules_oy_short;
				byte[] _fmodules_ox_byte;
				byte[] _fmodules_oy_byte;
				if ((DevConfig.sprite_allowShortIndexForFModules && (((_bs_flags & BS_FM_INDEX_SHORT) != 0)))
				   )
				{
					_fmodules_id_short = this._fmodules_id_short = new short[nFModules];
				}
				else

				{
					_fmodules_id_byte = this._fmodules_id_byte  = new byte[nFModules];
				}

				if (DevConfig.sprite_useFMOffShort)
				{
					_fmodules_ox_short = this._fmodules_ox_short = new short[nFModules];
					_fmodules_oy_short = this._fmodules_oy_short = new short[nFModules];
				}
				else
				{
					_fmodules_ox_byte = this._fmodules_ox_byte = new byte[nFModules];
					_fmodules_oy_byte = this._fmodules_oy_byte = new byte[nFModules];
				}

				_fmodules_flags = new byte[nFModules];

				if ((DevConfig.sprite_allowShortIndexForFModules) &&
				    ((_bs_flags & BS_FM_INDEX_SHORT) != 0)
				   )
				{
					offset = readArray2Short( file, offset, _fmodules_id_short, 0, nFModules, false, false );
				}
				else
				{
					;{ System.arraycopy(file, offset, _fmodules_id_byte, 0, nFModules ); offset += nFModules; };
				}

				if (DevConfig.sprite_useFMOffShort)
				{
					if (	(DevConfig.sprite_useBSpriteFlags)
						&&	((_bs_flags & BS_FM_OFF_SHORT) == 0)
						)
					{
                        offset = readArray2Short( file, offset, _fmodules_ox_short, 0, nFModules, true, true);
                        offset = readArray2Short( file, offset, _fmodules_oy_short, 0, nFModules, true, true);
					}
					else
					{
                        offset = readArray2Short( file, offset, _fmodules_ox_short, 0, nFModules, false, false );
                        offset = readArray2Short( file, offset, _fmodules_oy_short, 0, nFModules, false, false );
					}
				}
				else // if (zJYLibConfig.sprite_useFMOffShort)
				{
                    ;{ System.arraycopy(file, offset, _fmodules_ox_byte, 0, nFModules ); offset += nFModules; };
                    ;{ System.arraycopy(file, offset, _fmodules_oy_byte, 0, nFModules ); offset += nFModules; };
				}

				if (DevConfig.sprite_useFMPalette)
				{
					if (	(!DevConfig.sprite_useBSpriteFlags)
						||	((_bs_flags & BS_FM_PALETTE) != 0)
						)
					{
                        _fmodules_pal = new byte[ nFModules ];

                        ;{ System.arraycopy(file, offset, _fmodules_pal, 0, nFModules ); offset += nFModules; };
					}
				}

                ;{ System.arraycopy(file, offset, _fmodules_flags, 0, nFModules ); offset += nFModules; };

			} // if (! zJYLibConfig.sprite_useSingleArrayForFMAF)
		}

        return offset;
    }

    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

    private int LoadFrames_NI( int offset, byte[] file )
    {
		// Rect...
		if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
		{
			int nRects = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

            if((_bs_flags & BS_FM_OFF_SHORT) == 0)
            {
    			_frames_rects 	= new byte [nRects << 2];
	    		;{ System.arraycopy(file, offset, _frames_rects, 0, nRects << 2 ); offset += nRects << 2; };
	        }
	        else
	        {
    			_frames_rects_short = new short [nRects << 2];
	    		offset = readArray2Short( file, offset, _frames_rects_short, 0, nRects << 2, false, false );
	        }
        }

        // Frames...
		int nFrames = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nFrames = " + nFrames);;};

		if (nFrames > 0)
		{


			if ((DevConfig.sprite_allowShortNumOfFModules && (((_bs_flags & BS_NFM_1_BYTE) == 0)))
			   )

			{
				_frames_nfm_short = new short[nFrames];
			}
			else
			{
				_frames_nfm_byte  = new  byte[nFrames];
			}

			_frames_fm_start  = new short[nFrames];
			short frame_start = 0;

			//for( int i = 0; i < nFrames; i++)
			{


				if ((DevConfig.sprite_allowShortNumOfFModules) &&
				    ((_bs_flags & BS_NFM_1_BYTE) == 0)
				   )

				{
					offset = readArray2Short( file, offset, _frames_nfm_short, 0, nFrames, false, false );
				}
				else
				{
					;{ System.arraycopy(file, offset, _frames_nfm_byte, 0, nFrames ); offset += nFrames; };
				}

				if( DevConfig.sprite_alwaysBsNoFmStart )
				{
					short[] _frames_fm_start = this._frames_fm_start;
                    for( int i = 0; i < nFrames; i++)
                    {
					    _frames_fm_start[i] = frame_start;
						frame_start        += GetFModules(i);
                    }
				}
				else
				{
                    offset = readArray2Short( file, offset, _frames_fm_start, 0, nFrames, false, false );
				}

				if (DevConfig.sprite_useFrameRects && (_bs_flags & BS_FRAME_RECTS) != 0)
				{
					short[] _frames_rects_start = this._frames_rects_start = new short[nFrames + 1];
					short frames_rects_offset = 0;
					for ( int i = 0; i < nFrames; i++)
					{
						_frames_rects_start[i] = frames_rects_offset;
						frames_rects_offset += (file[offset++]);
					}
					_frames_rects_start[_frames_rects_start.length - 1] = frames_rects_offset;
				}
			}

			if (! DevConfig.sprite_alwaysBsSkipFrameRc)
			{
				if (DevConfig.sprite_usePrecomputedFrameRect)
				{
					// Bound rect for each frame...
					int nFrames4 = nFrames<<2;

                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
    					byte[] _frames_rc = this._frames_rc   = new byte[ nFrames4 ];

                        offset = readByteArrayNi( file, offset, _frames_rc, nFrames, 0, 4);
                        offset = readByteArrayNi( file, offset, _frames_rc, nFrames, 1, 4);
                        offset = readByteArrayNi( file, offset, _frames_rc, nFrames, 2, 4);
                        offset = readByteArrayNi( file, offset, _frames_rc, nFrames, 3, 4);
                    }
                    else
                    {
                        //TODO to short
//    					_frames_rc_short   = new short[ nFrames4 ];
//
//                        ReadBYTE_ARRAY_NI( _frames_rc_short, nFrames, 0, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_rc_short, nFrames, 1, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_rc_short, nFrames, 2, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_rc_short, nFrames, 3, 4 );
                    }
				}
				else // if (zJYLibConfig.sprite_usePrecomputedFrameRect)
				{
                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
                        offset += (nFrames<<2);
                    }
                    else
                    {
                        offset += (nFrames<<3);
                    }

				} // if (zJYLibConfig.sprite_usePrecomputedFrameRect)
			}
			else // if (! zJYLibConfig.sprite_alwaysBsSkipFrameRc)
			{
				if (DevConfig.sprite_usePrecomputedFrameRect)
				{
					// TODO: precompute frame rc
				}
			}

			if (DevConfig.sprite_useFrameCollRC)
			{
				if (	(! DevConfig.sprite_useBSpriteFlags)
					||	((_bs_flags & BS_FRAME_COLL_RC) != 0)
					)
				{
					// Collision rect for each frame...
					int nFrames4 = nFrames<<2;

                    if((_bs_flags & BS_FM_OFF_SHORT) == 0)
                    {
    					byte[] _frames_col = this._frames_col  = new byte[nFrames4];

                        offset = readByteArrayNi( file, offset, _frames_col, nFrames, 0, 4);
                        offset = readByteArrayNi( file, offset, _frames_col, nFrames, 1, 4);
                        offset = readByteArrayNi( file, offset, _frames_col, nFrames, 2, 4);
                        offset = readByteArrayNi( file, offset, _frames_col, nFrames, 3, 4);
                    }
                    else
                    {
                        //TODO to short
//    					_frames_col_short  = new short[nFrames4];
//
//                        ReadBYTE_ARRAY_NI( _frames_col_short, nFrames, 0, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_col_short, nFrames, 1, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_col_short, nFrames, 2, 4 );
//                        ReadBYTE_ARRAY_NI( _frames_col_short, nFrames, 3, 4 );
                    }
				}
			}
		}

        return offset;
    }

    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

    private int LoadAFrames_NI( int offset, byte[] file )
    {
        // AFrames...
		int nAFrames = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nAFrames = " + nAFrames);;};

		if (nAFrames > 0)
		{
			{
				short[] _aframes_frame_short = null;
				byte[] _aframes_frame_byte = null;
				if ((DevConfig.sprite_allowShortIndexForAFrames && (((_bs_flags & BS_AF_INDEX_SHORT) != 0)))
				   )
				{
					_aframes_frame_short = this._aframes_frame_short = new short[nAFrames];
				}
				else

				{
					_aframes_frame_byte = this._aframes_frame_byte  = new byte[nAFrames];
				}

				byte[] _aframes_time = this._aframes_time  = new byte[nAFrames];
				short[] _aframes_ox_short;
				short[] _aframes_oy_short;
				byte[] _aframes_ox_byte;
				byte[] _aframes_oy_byte;
//					_aframes_ox = new T_AFRAME_OFF[nAFrames];
//					_aframes_oy = new T_AFRAME_OFF[nAFrames];
				if (DevConfig.sprite_useAfOffShort)
				{
					_aframes_ox_short = this._aframes_ox_short = new short[nAFrames];
					_aframes_oy_short = this._aframes_oy_short = new short[nAFrames];
				}
				else
				{
					_aframes_ox_byte = this._aframes_ox_byte = new byte[nAFrames];
					_aframes_oy_byte = this._aframes_oy_byte = new byte[nAFrames];
				}

				byte[] _aframes_flags = this._aframes_flags = new byte[nAFrames];

				//for(int i = 0; i < nAFrames; i++)
				{

					if ((DevConfig.sprite_allowShortIndexForAFrames) &&
					    ((_bs_flags & BS_AF_INDEX_SHORT) != 0)
					   )
					{
						offset = readArray2Short( file, offset, _aframes_frame_short, 0, nAFrames, false, false );
					}
					else

					{
						;{ System.arraycopy(file, offset, _aframes_frame_byte, 0, nAFrames ); offset += nAFrames; };
					}

                    ;{ System.arraycopy(file, offset, _aframes_time, 0, nAFrames ); offset += nAFrames; };

					if (DevConfig.sprite_useAfOffShort)
					{
						if( (DevConfig.sprite_useBSpriteFlags) && ((_bs_flags & BS_AF_OFF_SHORT) == 0))
						{
                            offset = readArray2Short( file, offset, _aframes_ox_short, 0, nAFrames, true, false);
                            offset = readArray2Short( file, offset, _aframes_oy_short, 0, nAFrames, true, false);
						}
						else
						{
                            offset = readArray2Short( file, offset, _aframes_ox_short, 0, nAFrames, false, false );
                            offset = readArray2Short( file, offset, _aframes_oy_short, 0, nAFrames, false, false );
						}
					}
					else
					{
                        ;{ System.arraycopy(file, offset, _aframes_ox_byte, 0, nAFrames ); offset += nAFrames; };
                        ;{ System.arraycopy(file, offset, _aframes_oy_byte, 0, nAFrames ); offset += nAFrames; };
					}

                    ;{ System.arraycopy(file, offset, _aframes_flags, 0, nAFrames ); offset += nAFrames; };
				}
			}
		}

        return offset;
    }
    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------

    private int LoadAnims_NI( int offset, byte[] file )
    {
        // Anims...
		int nAnims = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("nAnims = " + nAnims);;};
		short[] _anims_naf_short = null;
		byte[] _anims_naf_byte = null;
		if (nAnims > 0)
		{


			if ((DevConfig.sprite_allowShortNumOfAFrames && (((_bs_flags & BS_NAF_1_BYTE) == 0)))
			   )

			{
				_anims_naf_short = this._anims_naf_short = new short[nAnims];
			}
			else
			{
				_anims_naf_byte = this._anims_naf_byte  = new  byte[nAnims];
			}

			short[] _anims_af_start = this._anims_af_start = new short[nAnims];
			short af_start  = 0;

			//for (int i = 0; i < nAnims; i++)
			{


				if ((DevConfig.sprite_allowShortNumOfAFrames) &&
				    ((_bs_flags & BS_NAF_1_BYTE) == 0)
				   )

				{
					offset = readArray2Short( file, offset, _anims_naf_short, 0, nAnims, false, false );
				}
				else
				{
					;{ System.arraycopy(file, offset, _anims_naf_byte, 0, nAnims ); offset += nAnims; };
				}

				{
					//_anims_af_start[i] = ReadSHORT();
                    offset = readArray2Short( file, offset, _anims_af_start, 0, nAnims, false, false );
				}
			}
		}

        return offset;
    }


private int LoadData_useModuleImages( int offset, byte[] file, int pal_flags, int tr_flags )
{
	if ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULE_IMAGES) != 0))
	{
		// Do we have Module Images to load ?
		if(offset >= file.length)
		{
			return offset;
		}

		// Pixel format (must be one of supported SPRITE_FORMAT_xxxx)...
		short _pixel_format = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("_pixel_format = 0x" + Integer.toHexString(_pixel_format));;};

		// Number of palettes...
		_palettes = (((file[offset++]) & 0xFF));

		if (DevConfig.sprite_debugLoading){Utils.Dbg("_palettes = " + _palettes);;};

		if(!(_palettes <= DevConfig.MAX_SPRITE_PALETTES))Utils.DBG_PrintStackTrace(false, "Sprite is using more palettes than MAX_SPRITE_PALETTES, please increase this value in zJYLibConfig!");;

		// Number of colors...
		_colors = (((file[offset++]) & 0xFF));

		if (DevConfig.sprite_useEncodeFormatI256 ||
			DevConfig.sprite_useEncodeFormatI256RLE ||
			DevConfig.sprite_useEncodeFormatA256_I256RLE)
		{
			if (_colors == 0) _colors = 256; // for I256 or I256RLE
		}

		if (DevConfig.sprite_debugLoading){Utils.Dbg("colors = " + _colors);;};

		// Palettes...
		if (DevConfig.sprite_useNokiaUI)
		{
			if (_pal_short == null)
				_pal_short = new short[DevConfig.MAX_SPRITE_PALETTES][];
		}
		else
		{
			if (_pal_int == null)
				_pal_int = new int[DevConfig.MAX_SPRITE_PALETTES][];
		}

		int _palettes = this._palettes;
		short[][] _pal_short = this._pal_short;
		int[][] _pal_int = this._pal_int;
		for (int p = 0; p < _palettes; p++)
		{
			if ( (DevConfig.sprite_useDynamicPng) && ((pal_flags & (1 << p)) == 0) )
			{
                offset += (_pixel_format == PIXEL_FORMAT_8888 ? _colors * 4 : _colors * 2);
			}
			else
			{
				if (DevConfig.sprite_useNokiaUI)
				{
					_pal_short[p] = new short[_colors];
				}
				else
				{
					_pal_int[p] = new int[_colors];
				}

				// HINT: Sort these pixel formats regarding how often are used by your game!
				if ((DevConfig.sprite_usePixelFormat8888) && (_pixel_format == PIXEL_FORMAT_8888))
				{
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
					{
						if (DevConfig.sprite_useNokiaUI)
						{
	  						// 8888 . 4444
	  						int _4444  = (( (file[offset++]) & 0xF0)>>4);
		  			 			_4444 += (( (file[offset++]) & 0xF0)   );
		  			 			_4444 += (( (file[offset++]) & 0xF0)<<4);
		  			 			_4444 += (( (file[offset++]) & 0xF0)<<8);

							if ((_4444 & 0xF000) != 0xF000)
							{
								_alpha = true;

								if ((_4444 & 0xF000) != 0)
								{
									_multiAlpha = true;
								}
							}

							_pal_short[p][c] = (short)(_4444 & 0xFFFF);
						}
						else // if (zJYLibConfig.sprite_useNokiaUI)
						{
                            int _8888  = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));

							if ((_8888 & 0xFF000000) != 0xFF000000)
							{
								_alpha = true;

								if ((_8888 & 0xFF000000) != 0)
								{
									_multiAlpha = true;
								}
							}

						
							_pal_int[ p ][ c ] = (int)SetSpecialColorAlpha(_pixel_format,_8888);
						

						}
					}
				}
				else if ((DevConfig.sprite_usePixelFormat4444) && (_pixel_format == PIXEL_FORMAT_4444))
				{
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
					{
                        int _4444  = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

						if ((_4444 & 0xF000) != 0xF000)
						{
							_alpha = true;

							if ((_4444 & 0xF000) != 0)
							{
								_multiAlpha = true;
							}
						}

						if (DevConfig.sprite_useNokiaUI)
						{
							_pal_short[ p ][ c ] = (short)(_4444 & 0xFFFF);
						}
						else // if (zJYLibConfig.sprite_useNokiaUI)
						{
							// 4444 . 8888
							int _temp = (int) (
										((_4444 & 0xF000) << 16) | ((_4444 & 0xF000) << 12) |	// A
										((_4444 & 0x0F00) << 12) | ((_4444 & 0x0F00) << 8) |	// R
										((_4444 & 0x00F0) << 8) | ((_4444 & 0x00F0) << 4) |	// G
										((_4444 & 0x000F) << 4) | ((_4444 & 0x000F)));	// B

						
							_pal_int[ p ][ c ] = SetSpecialColorAlpha(_pixel_format,_temp);	// B
						

						} // if (zJYLibConfig.sprite_useNokiaUI)
					}
				}
				else if ((DevConfig.sprite_usePixelFormat1555) && (_pixel_format == PIXEL_FORMAT_1555))
				{
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
					{
                        int _1555  = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

						int a = 0xFF000000;
						if ((_1555 & 0x8000) != 0x8000)
						{
							a = 0;
							_alpha = true;
						}

						if (DevConfig.sprite_useNokiaUI)
						{
							// 1555 . 8888
							int tmp_col = (
											a |						// A
										 ((_1555 & 0x7C00) << 9) |	// R
										 ((_1555 & 0x03E0) << 6) |	// G
										 ((_1555 & 0x001F) << 3));	// B

							_pal_short[ p ][ c ] = (short) (
										 (((tmp_col >> 24)	& 0xF0)	<< 8) |		// A
										 (((tmp_col >> 16)	& 0xF0) << 4) |		// R
										 (((tmp_col >> 8)	& 0xF0)		) |		// G
										 (((tmp_col)		& 0xF0)	>> 4));		// B
						}
						else // if (zJYLibConfig.sprite_useNokiaUI)
						{
							int _temp = (int) (
											a |						// A
										 ((_1555 & 0x7C00) << 9) |	// R
										 ((_1555 & 0x03E0) << 6) |	// G
										 ((_1555 & 0x001F) << 3));	// B

							// FF00FF . F800F8 when using 1555 format
							if (_temp == 0xF800F8)
							{
								_temp = 0xFF00FF;
							}

						
							_pal_int[ p ][ c ] = SetSpecialColorAlpha(_pixel_format,_temp);
						

						} // if (zJYLibConfig.sprite_useNokiaUI)
					}
				}
				else if ((DevConfig.sprite_usePixelFormat0565) && (_pixel_format == PIXEL_FORMAT_0565))
				{
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
					{
                        int _0565  = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

						int a = 0xFF000000;
						if ((_0565&0xFFFF) == 0xF81F)
						{
							a = 0;
							_alpha = true;
						}

						if (DevConfig.sprite_useNokiaUI)
						{
							// 0565 . 8888
							int tmp_col = (
											a |						// A
										 ((_0565 & 0xF800) << 8) |	// R
										 ((_0565 & 0x07E0) << 5) |	// G
										 ((_0565 & 0x001F) << 3));	// B

							// 8888 . 4444
							_pal_short[ p ][ c ] = (short) (
										 (((tmp_col >> 24)	& 0xF0)	<< 8) |		// A
										 (((tmp_col >> 16)	& 0xF0) << 4) |		// R
										 (((tmp_col >> 8)	& 0xF0)		) |		// G
										 (((tmp_col)		& 0xF0)	>> 4));		// B
						}
						else // if (zJYLibConfig.sprite_useNokiaUI)
						{
							int _temp = (int) (
											a |						// A
										 ((_0565 & 0xF800) << 8) |	// R
										 ((_0565 & 0x07E0) << 5) |	// G
										 ((_0565 & 0x001F) << 3));	// B

							// FF00FF . F800F8 when using 0565 format
							if(_temp == 0xF800F8)
							{
								_temp = 0xFF00FF;
							}


							_pal_int[ p ][ c ] = SetSpecialColorAlpha(_pixel_format,_temp);
						

						} // if (zJYLibConfig.sprite_useNokiaUI)
					}
				}
				else if ((DevConfig.sprite_usePixelFormat0332) && (_pixel_format == PIXEL_FORMAT_0332))
				{
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
				    {
				    	int _0332  = (((file[offset++]) & 0xFF));

				        int a = 0xFF000000;

				    	if (_0332 == 0xC0)
				        {
				        	a = 0;
				            _alpha = true;
				        }

				        if (DevConfig.sprite_useNokiaUI)
				        {
				        	int tmp_col = (
				                  			a |                      // A
				                         ((_0332 & 0xE0) << 16) |    // R
				                         ((_0332 & 0x1C) << 11) |    // G
				                         ((_0332 & 0x03) <<  6));    // B

				             _pal_short[p][c] = (short) (
				                          (((tmp_col >> 24) & 0xF0) << 8) |        // A
				                          (((tmp_col >> 16) & 0xF0) << 4) |        // R
				                          (((tmp_col >> 8)  & 0xF0)     ) |        // G
				                          (((tmp_col)       & 0xF0) >> 4));        // B
				        }
				        else
				        {
							

				              _pal_int[p][c] = (int) (
				                            a |                      // A
				                         ((_0332 & 0xE0) << 16) |    // R
				                         ((_0332 & 0x1C) << 11) |    // G
				                         ((_0332 & 0x03) <<  6));    // B

				        }
				 	}
			  	}
			  	else if ((DevConfig.sprite_usePixelFormat0888) && (_pixel_format == PIXEL_FORMAT_8808))
				{
					_multiAlpha = false;
					int _colors = this._colors;
					for (int c = 0; c < _colors; c++)
					{
						if (DevConfig.sprite_useNokiaUI)
						{
	  						// 0888 . 0444
	  						int _0444  = (( (file[offset++]) & 0xF0)>>4);
		  			 			_0444 += (( (file[offset++]) & 0xF0)   );
		  			 			_0444 += (( (file[offset++]) & 0xF0)<<4);

							if (_0444 == 0xF0F)
							{
								_alpha = true;
							}
							else
							{
								_0444 |= 0xF000;
							}

							_pal_short[p][c] = (short)(_0444 & 0xFFFF);
						}
						else // if (zJYLibConfig.sprite_useNokiaUI)
						{
                            int _0888  = (((file[offset++]) & 0xFF)      );
                                _0888 += (((file[offset++]) & 0xFF) <<  8);
                                _0888 += (((file[offset++]) & 0xFF) << 16);

							if (_0888 == 0xFF00FF)
							{
								_alpha = true;
							}
							else
							{
								_0888 |= 0xFF000000;
							}

							_pal_int[p][c] = _0888;
						}
					}
				}

				else
				{
					if(!(false))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Unsupported format! Format ID: 0x"+Integer.toHexString(_pixel_format));;
				}

				if      (_pixel_format == PIXEL_FORMAT_8888){ if(!(DevConfig.sprite_usePixelFormat8888))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 8888 but it is not enabled! Set sprite_usePixelFormat8888 to TRUE!");;}
				else if (_pixel_format == PIXEL_FORMAT_8808){ if(!(DevConfig.sprite_usePixelFormat0888))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 0888 but it is not enabled! Set sprite_usePixelFormat0888 to TRUE!");;}
				else if (_pixel_format == PIXEL_FORMAT_4444){ if(!(DevConfig.sprite_usePixelFormat4444))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 4444 but it is not enabled! Set sprite_usePixelFormat4444 to TRUE!");;}
				else if (_pixel_format == PIXEL_FORMAT_1555){ if(!(DevConfig.sprite_usePixelFormat1555))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 1555 but it is not enabled! Set sprite_usePixelFormat1555 to TRUE!");;}
				else if (_pixel_format == PIXEL_FORMAT_0565){ if(!(DevConfig.sprite_usePixelFormat0565))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 0565 but it is not enabled! Set sprite_usePixelFormat0565 to TRUE!");;}
				else if (_pixel_format == PIXEL_FORMAT_0332){ if(!(DevConfig.sprite_usePixelFormat0332))Utils.DBG_PrintStackTrace(false, "Sprite Pixel Format Error: Using 0332 but it is not enabled! Set sprite_usePixelFormat0332 to TRUE!");;}

			}
		}

		//////////////////////////////

		// Data format (must be one of supported ENCODE_FORMAT_xxxx)...
		_data_format = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
		short _data_format = this._data_format;
		if (DevConfig.sprite_debugLoading){Utils.Dbg("_data_format = 0x" + Integer.toHexString(_data_format));;};

		if (
			((DevConfig.sprite_useEncodeFormatA256_I64RLE)  && (_data_format == ENCODE_FORMAT_A256_I64RLE))  ||
			((DevConfig.sprite_useEncodeFormatA256_I127RLE) && (_data_format == ENCODE_FORMAT_A256_I127RLE)) ||
			((DevConfig.sprite_useEncodeFormatA256_I256RLE) && (_data_format == ENCODE_FORMAT_A256_I256RLE)) ||
			((DevConfig.sprite_useEncodeFormatA256_I64)     && (_data_format == ENCODE_FORMAT_A256_I64))     ||
			((DevConfig.sprite_useEncodeFormatA256_I128)    && (_data_format == ENCODE_FORMAT_A256_I128))    ||
			((DevConfig.sprite_useEncodeFormatA256_I256)    && (_data_format == ENCODE_FORMAT_A256_I256))    ||
			((DevConfig.sprite_useEncodeFormatA8_I32)       && (_data_format == ENCODE_FORMAT_A8_I32))       ||
			((DevConfig.sprite_useEncodeFormatA32_I8)       && (_data_format == ENCODE_FORMAT_A32_I8))
		   )
		{
			_alpha      = true;
			_multiAlpha = true;
		}

		if (DevConfig.sprite_useEncodeFormatI64RLE || DevConfig.sprite_useEncodeFormatA256_I64RLE)
		{
			if (_data_format == ENCODE_FORMAT_I64RLE || _data_format == ENCODE_FORMAT_A256_I64RLE)
			{
				int clrs = _colors - 1;
				_i64rle_color_mask = 1;
				_i64rle_color_bits = 0;

				while (clrs != 0)
				{
					clrs >>= 1;
					_i64rle_color_mask <<= 1;
					_i64rle_color_bits++;
				}
				_i64rle_color_mask--;
			}
		} // if (zJYLibConfig.sprite_useEncodeFormatI64RLE || zJYLibConfig.sprite_useEncodeFormatA256I64RLE)

		// Graphics data...
		int _nModules = this._nModules;
		if (_nModules > 0)
		{
			if (DevConfig.sprite_useDynamicPng)
			{
				// scanning anim data for module usage
				//mod = 0;
				//byte[] modules_usage = null;;

				if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
				{
                    if( !DevConfig.sprite_useOperationMark )
                    {
                        if (DevConfig.sprite_debugLoading){Utils.Dbg("zJYLibConfig.sprite_useOperationMark = true must be defined!");;};
                    }

                    // USE_OPERATION_MARK should be defined!
                    MarkTransformedModules( true, tr_flags );

					if (_module_image_imageAAA == null)
						_module_image_imageAAA = new Image [_palettes][][];
				}
				else //if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
				{
					if (_module_image_imageAA == null)
						_module_image_imageAA = new Image [_palettes][];
				}
				Image[][][]_module_image_imageAAA = this._module_image_imageAAA;
				for (int p = 0; p < _palettes; p++)
				{
					if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
					{
						if (((pal_flags & (1 << p)) != 0) && _module_image_imageAAA[p] == null)
						{
							_module_image_imageAAA[p] = new Image [_nModules][];

							for (int q = 0; q < _nModules; q++)
							{
								int flip_max = 1;

								for (int r = 0; r < DevConfig.MAX_FLIP_COUNT; r++)
								{
									if ((DevConfig.sprite_useModuleUsageFromSprite) && ((_modules_usage[q] & (1 << r)) != 0))
										flip_max = r + 1;
								}

								_module_image_imageAAA[p][q] = new Image[flip_max];
							}
						}
					}
					else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
					{
						Image[][] _module_image_imageAA = this._module_image_imageAA;
						if (((pal_flags & (1 << p)) != 0) && _module_image_imageAA[p] == null)
						{
							_module_image_imageAA[ p ] = new Image [_nModules];
						}
					} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
				}

				// creating images
				byte[] image_data;
				int mod_dim, sizeX, sizeY;

				if (DevConfig.sprite_usePrecomputedCRC)
				{
                    if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData with sprite_usePrecomputedCRC");;};

					if (DevConfig.sprite_useDoubleArrayForModuleData)
					{
						_modules_data_array = new byte[_nModules][];
					}
					else
					{
						short [] _modules_data_off_short = null;
						int[] _modules_data_off_int = null;
						if( DevConfig.sprite_useModuleDataOffAsShort )
						{
							_modules_data_off_short = this._modules_data_off_short = new short[ _nModules ];
						}
						else
						{
							_modules_data_off_int = this._modules_data_off_int   = new int[ _nModules ];
						}

						int len = 0;
						int offset_old = offset;

						for (int m = 0; m < _nModules; m++)
						{
							// Image data for the module...
							int size;

							if((_bs_flags & BS_IMAGE_SIZE_INT) != 0)
							{
								size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
							}
							else
							{
								size = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

								if(!(size >= 0))Utils.DBG_PrintStackTrace(false, "LoadingModules: module " + m + " size read as short is negative. You need to add BS_IMAGE_SIZE_INT to this sprites export flags");;
							}

							if( DevConfig.sprite_useModuleDataOffAsShort )
							{
								_modules_data_off_short[ m ] = (short)len;
							}
							else
							{
								_modules_data_off_int[ m ]   = len;
							}

							len += size;
							offset += (size);
						}

						offset = (offset_old);

						_modules_data = new byte[ len ];
					}
					byte[][] _modules_data_array = null;
					if (DevConfig.sprite_useDoubleArrayForModuleData)
					{
						_modules_data_array = this._modules_data_array;
					}
					for( int m = 0; m < _nModules; m++)
					{
						// Image data for the module...
						int size;

						if((_bs_flags & BS_IMAGE_SIZE_INT) != 0)
						{
						    size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}
						else
						{
						    size = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

						    if(!(size >= 0))Utils.DBG_PrintStackTrace(false, "LoadingModules: module " + m + " size read as short is negative. You need to add BS_IMAGE_SIZE_INT to this sprites export flags");;
						}

						if (DevConfig.sprite_debugLoading){Utils.Dbg("module["+m+"] size = " + size);;};

						if (DevConfig.sprite_useDoubleArrayForModuleData)
						{
							_modules_data_array[m] = new byte[size];
						}

						;{ System.arraycopy(file, offset, GetModuleData(m), GetStartModuleData(m, 0), size ); offset += size; };
					}

					if (	(!DevConfig.sprite_useBSpriteFlags)
						||	((_bs_flags & BS_PNG_CRC) != 0)
						)
					{
						// read CRCs...
						int[] _PNG_packed_PLTE_CRC = this._PNG_packed_PLTE_CRC	= new int[_palettes];
						int[] _PNG_packed_tRNS_CRC = this._PNG_packed_tRNS_CRC	= new int[_palettes];
						int[] _PNG_packed_IHDR_CRC = this._PNG_packed_IHDR_CRC	= new int[_nModules];
						int[] _PNG_packed_IDAT_ADLER = this._PNG_packed_IDAT_ADLER	= new int[_nModules];
						int[] _PNG_packed_IDAT_CRC = this._PNG_packed_IDAT_CRC	= new int[_nModules];
						
						for (int p = 0; p < _palettes; p++)
						{
							_PNG_packed_PLTE_CRC[p] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}

						for (int p = 0; p < _palettes; p++)
						{
							_PNG_packed_tRNS_CRC[p] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}

						for (int m = 0; m < _nModules; m++)
						{
							_PNG_packed_IHDR_CRC[m] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}

						for (int m = 0; m < _nModules; m++)
						{
							_PNG_packed_IDAT_ADLER[m] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}

						for (int m = 0; m < _nModules; m++)
						{
							_PNG_packed_IDAT_CRC[m] = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}
					}

					///offset = offset_old; unnecessarly

				}
				else // if (zJYLibConfig.sprite_usePrecomputedCRC)
				{
                    if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData WITHOUT sprite_usePrecomputedCRC");;};

					boolean bSkipPalette        = false;
					int     offset_modules_data = offset;

					for( int p = 0; p < _palettes; p++)
					{
						if ((pal_flags & (1 << p)) != 0)
						{
							bSkipPalette = false;
							offset = (offset_modules_data);

							for (int mod = 0; mod < _nModules; mod++)
							{
								mod_dim        = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
                                int offset_old = offset;
								//DBG("mod_dim = " + mod_dim);

								sizeX = GetModuleWidth(mod);
								sizeY = GetModuleHeight(mod);

								image_data = null;

								if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
								{
									for (int k = 0; k < DevConfig.MAX_FLIP_COUNT; k++)
									{
										if(k >= _module_image_imageAAA[ p ][ mod ].length)
										{
											break;
										}


										if(  ( !DevConfig.sprite_useModuleUsageFromSprite ||
                                               ((1 << k) & _modules_usage[mod]) != 0 ) &&
                                             _module_image_imageAAA[ p ][ mod ][ k ] == null)
										{
											if( image_data == null )
												image_data = DecodeImage_byte( file, offset, sizeX, sizeY);

											if( DevConfig.sprite_useBugFixImageOddSize )
											{
												sizeX = sizeX + (sizeX % 2);
												sizeY = sizeY + (sizeY % 2);
											}
											

												_module_image_imageAAA[ p ][ mod ][ k ] = BuildPNG8(p, bSkipPalette, image_data, sizeX, sizeY, k);


											bSkipPalette = true;
										}
									}
								}
								else
								{
									if (_module_image_imageAA[ p ][ mod ] == null )
									{
										if( image_data == null )
											image_data = DecodeImage_byte( file, offset, sizeX, sizeY );

										if (DevConfig.sprite_useBugFixImageOddSize)
										{
											sizeX = sizeX + (sizeX % 2);
											sizeY = sizeY + (sizeY % 2);
										}
										

											_module_image_imageAA[p][mod] = BuildPNG8(p, bSkipPalette, image_data, sizeX, sizeY, 0);

										bSkipPalette = true;
									}
								}

								offset = (offset_old + mod_dim);
							}
						}
					}
				} // if (! zJYLibConfig.sprite_usePrecomputedCRC)
			}
			else // if (zJYLibConfig.sprite_useDynamicPng)
			{
				if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData: Loading Module Offsets");;};
				byte[][] _modules_data_array ;
				short[] _modules_data_off_short;
				int[] _modules_data_off_int;
				if (DevConfig.sprite_useDoubleArrayForModuleData)
				{
					_modules_data_array = this._modules_data_array = new byte[_nModules][];
				}
				else
				{
					if( DevConfig.sprite_useModuleDataOffAsShort )
					{
						_modules_data_off_short = this._modules_data_off_short = new short[_nModules];
					}
					else
					{
						_modules_data_off_int = this._modules_data_off_int   = new int[_nModules];
					}

					int len        = 0;
					int offset_old = offset;
					int _bs_flags = this._bs_flags;
					for (int m = 0; m < _nModules; m++)
					{
						// Image data for the module...
						int size;

						if((_bs_flags & BS_IMAGE_SIZE_INT) != 0)
						{
							size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
						}
						else
						{
							size = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

							if(!(size >= 0))Utils.DBG_PrintStackTrace(false, "LoadingModules: module " + m + " size read as short is negative. You need to add BS_IMAGE_SIZE_INT to this sprites export flags");;
						}

						if( DevConfig.sprite_useModuleDataOffAsShort )
						{
							if(!(len <= 0xFFFF))Utils.DBG_PrintStackTrace(false, "Module data offset is larger than a short! This means your sprite image data is larger than 64K and you need to set sprite_useModuleDataOffAsShort to FALSE.");;

							_modules_data_off_short[ m ] = (short)len;
						}
						else
						{
							_modules_data_off_int[ m ]   = len;
						}

						len += size;
						offset += (size);
					}

					if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData: Loading Module Data");;};

					offset = (offset_old);

					_modules_data = new byte[len];
				}
				int _bs_flags = this._bs_flags;
				for (int m = 0; m < _nModules; m++)
				{
					// Image data for the module...
					int size;

					if((_bs_flags & BS_IMAGE_SIZE_INT) != 0)
					{
					    size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
					}
					else
					{
					    size = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

					    if(!(size >= 0))Utils.DBG_PrintStackTrace(false, "LoadingModules: module " + m + " size read as short is negative. You need to add BS_IMAGE_SIZE_INT to this sprites export flags");;
					}

					if (DevConfig.sprite_debugLoading){Utils.Dbg("module[" + m + "] size = " + size);;};

					if (DevConfig.sprite_useDoubleArrayForModuleData)
					{
						_modules_data_array[m] = new byte[size];
					}

					;{ System.arraycopy(file, offset, GetModuleData(m), GetStartModuleData(m, 0), size ); offset += size; };
            	}
			} // if (! zJYLibConfig.sprite_useDynamicPng)
		}
	}


    return offset;
}


// This function could not use with DOCOMO API
private int LoadData_useSingleImages( int offset, byte[] file, int pal_flags, int tr_flags )
{
    if ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_SINGLE_IMAGE) != 0))
    {
		// read png size
		int size = 0;
		byte[][] _modules_data_array;
		byte[] _modules_data;
		if((_bs_flags & BS_IMAGE_SIZE_INT) != 0)
		{
			size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));
		}
		else
		{
			size = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

			if(!(size >= 0))Utils.DBG_PrintStackTrace(false, "LoadingSingleImage: size read as short is negative. You need to add BS_IMAGE_SIZE_INT to this sprites export flags");;
		}

		if (DevConfig.sprite_debugLoading){Utils.Dbg("png size = " + size);;};

		int image_offset = 0;

        
			if (DevConfig.sprite_useDynamicPng)
			{
				// here I try an optimiation of memory usage. I re-using file buffer for create the image (not allocated another one)
				if (DevConfig.sprite_useDoubleArrayForModuleData)
				{
					_modules_data_array = this._modules_data_array = new byte[1][];
					_modules_data_array[0] = file;
				}
				else
				{
					_modules_data = this._modules_data   = file;
				}

				image_offset    = offset;
				offset += (size);
			}
			else

			if (DevConfig.sprite_useDoubleArrayForModuleData)
			{
				_modules_data_array = this._modules_data_array = new byte[1][];
				_modules_data_array[0] = new byte[size];

				;{ System.arraycopy(file, offset, _modules_data_array[0], 0, size ); offset += size; };
			}
			else
			{
				// read the png data
				_modules_data = this._modules_data = new byte[size];

				;{ System.arraycopy(file, offset, _modules_data, 0, size ); offset += size; };
			}

		// Number of palettes...
		_palettes = (((file[offset++]) & 0xFF));
		if (DevConfig.sprite_debugLoading){Utils.Dbg("_palettes = " + _palettes);;};

		if(!(_palettes <= DevConfig.MAX_SPRITE_PALETTES))Utils.DBG_PrintStackTrace(false, "Sprite is using more palettes than MAX_SPRITE_PALETTES, please increase this value in zJYLibConfig!");;

		// Number of colors...
		_colors = (((file[offset++]) & 0xFF));
		if (_colors == 0) _colors = 256;

		if (DevConfig.sprite_debugLoading){Utils.Dbg("colors = " + _colors);;};

		// Palettes...
		int _PLTE_size = _colors * 3 + 4;
		int _TRNS_size = _colors + 4;
		int pal_size = _PLTE_size + _TRNS_size;

		if (DevConfig.sprite_debugLoading){Utils.Dbg("pal_size = " + pal_size);;};

		_pal_data = new byte[_palettes * pal_size];

		if (DevConfig.sprite_useDoubleArrayForModuleData)
		{
			//get the first pal from module data
			System.arraycopy(_modules_data_array[0], 41 + image_offset, _pal_data, 0, _PLTE_size);
			System.arraycopy(_modules_data_array[0], 41 + _PLTE_size + 8 + image_offset, _pal_data, _PLTE_size, _TRNS_size);
		}
		else
		{
			//get the first pal from module data
			System.arraycopy(_modules_data, 41 + image_offset, _pal_data, 0, _PLTE_size);
			System.arraycopy(_modules_data, 41 + _PLTE_size + 8 + image_offset, _pal_data, _PLTE_size, _TRNS_size);
		}

		//read the other pals
        ;{ System.arraycopy(file, offset, _pal_data, pal_size, (_palettes-1)*pal_size ); offset += (_palettes-1)*pal_size; };

		if (DevConfig.sprite_debugLoading){Utils.Dbg("...read pal data");;};


		if (DevConfig.sprite_useDynamicPng)
		{
			int _palettes = this._palettes;
			Image[][][] _module_image_imageAAA = null;
			Image[][] _module_image_imageAA = null;
			if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
			{
				_module_image_imageAAA = this._module_image_imageAAA = new Image[_palettes][][];
			}
			else
			{
				_module_image_imageAA = this._module_image_imageAA = new Image[_palettes][];
			}
			byte[] _pal_data = this._pal_data;
			for (int p = 0; p < _palettes; p++)
			{
				if ((pal_flags & (1 << p)) != 0)
				{
					if (DevConfig.sprite_useDoubleArrayForModuleData)
					{
						// copy palette
						System.arraycopy(_pal_data, p * (_PLTE_size + _TRNS_size), _modules_data_array[0], 41 + image_offset, _PLTE_size);
						// copy transparency
						System.arraycopy(_pal_data, p * (_PLTE_size + _TRNS_size) + _PLTE_size, _modules_data_array[0], 41 + _PLTE_size + 8 + image_offset, _TRNS_size);
					}
					else
					{
						// copy palette
						System.arraycopy(_pal_data, p * (_PLTE_size + _TRNS_size), _modules_data, 41 + image_offset, _PLTE_size);
						// copy transparency
						System.arraycopy(_pal_data, p * (_PLTE_size + _TRNS_size) + _PLTE_size, _modules_data, 41 + _PLTE_size + 8 + image_offset, _TRNS_size);
					}


					if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
					{
						_module_image_imageAAA[p]       = new Image[1][1];

						if (DevConfig.sprite_useDoubleArrayForModuleData)
							_module_image_imageAAA[p][0][0] = GLLib.CreateImage( _modules_data_array[0], image_offset, size);
						else
                        	_module_image_imageAAA[p][0][0] = GLLib.CreateImage( _modules_data, image_offset, size);


						if (DevConfig.sprite_debugUsedMemory)
						{
							_images_count++;
							_images_size += _module_image_imageAAA[p][0][0].getWidth() *
                                            _module_image_imageAAA[p][0][0].getHeight();
						}
					}
					else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
					{
						_module_image_imageAA[p]    = new Image[1];

						if (DevConfig.sprite_useDoubleArrayForModuleData)
                        	_module_image_imageAA[p][0] = GLLib.CreateImage( _modules_data_array[0], image_offset, size);
                        else
	                        _module_image_imageAA[p][0] = GLLib.CreateImage( _modules_data, image_offset, size);


						if (DevConfig.sprite_debugUsedMemory)
						{
							_images_count++;
							_images_size += _module_image_imageAA[p][0].getWidth() *
                                            _module_image_imageAA[p][0].getHeight();
						}
					} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
				}
			}

			this._pal_data = null;


            	if (DevConfig.sprite_useDoubleArrayForModuleData)
            	{
					this._modules_data_array[0] = null;
					this._modules_data_array = null;
				}
                else
                {
					this._modules_data   = null;
				}

		} // if (zJYLibConfig.sprite_useDynamicPng)
	}

    return offset;
}


//-----------------------------------------------------------------------------
/// Loads RAW Images
/// - Does not support BS_SINGLE_IMAGE at the moment.
/*
	if (BS_MODULE_IMAGES_TC_BMP)
    {
	    if(BS_SINGLE_IMAGE)
	    {
	    	4 bytes - dataSize
		 	dataSize - RGB/ARGB data for each pixel. (depending on the _0888/_8888 pixel format)
	    }
	    else
	    {
	        2 bytes . pixel format _0888, _8888
	        2 bytes . data format //RAW DATA

	        for each module (nm):
	        {
	            4 bytes . ms = module image size

			    if(pixel_format == _0888)
			            ms bytes . module image data 3*width*height(1 byte for each channel- RGB)
			    if(pixel_format == _8888)
			            ms bytes . module image data 4*width*height(1 byte for each channel- ARGB)

	       	}
    	}
    }
*/
//-----------------------------------------------------------------------------
private int LoadData_useRAW (int offset, byte[] file)
{
	if(!((_bs_flags & BS_SINGLE_IMAGE) == 0))Utils.DBG_PrintStackTrace(false, "LoadData_useRAW: loading RAW Image data for sprite, SINGLE IMAGE not supported currently!");;

	short _pixel_format = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
	short _data_format  = ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));

	if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData_useRaw: Loading RAW Module Images");;};

	if ((_bs_flags & BS_MULTIPLE_IMAGES) != 0)
	{
		_palettes = (file[offset++]);

		if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData_useRaw: Loading RAW Module Images with " + _palettes + " images (aka palettes)");;};
	}
	else
	{
		_palettes = 1;
	}
	int _palettes = this._palettes;
	// Get array ready to hold the raw data
	if (_module_image_intAAA == null)
	{
		_module_image_intAAA = new int[_palettes][][];
	}
	int[][][] _module_image_intAAA = this._module_image_intAAA;
	if (_pixel_format == PIXEL_FORMAT_8888)
	{
		_alpha      = true;
		_multiAlpha = true;

		if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData_useRaw: Pixel Format is 8888");;};
	}
	else if (_pixel_format == PIXEL_FORMAT_8808)
	{
		_alpha      = false;	// maybe set to true if we detect FF00FF below
		_multiAlpha = false;

		if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData_useRaw: Pixel Format is 0888");;};
	}
	else if (_pixel_format == PIXEL_FORMAT_4444)
	{
		_alpha      = false;	// maybe set to true below
		_multiAlpha = false;    // maybe set to true below

		if (DevConfig.sprite_debugLoading){Utils.Dbg("LoadData_useRaw: Pixel Format is 4444");;};
	}
	int _nModules = this._nModules;
	for (int p = 0; p < _palettes; p++)
	{
		if (_module_image_intAAA[p] == null)
		{
			_module_image_intAAA[p] = new int[_nModules][];
		}

		for (int m = 0; m < _nModules; m++)
		{
			// Image data for the module...
			int size = (((file[offset++]&0xFF) ) +((file[offset++]&0xFF)<< 8) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<24));

			// Allocate memory for this modules raw data buffer
			int[] modulePix = new int[size];

			if (_pixel_format == PIXEL_FORMAT_8888)
			{
				// Convert from bytes to pixels
				size = size >> 2;

				for (int i=0; i<size; i++)
				{
					modulePix[i] = (((file[offset++]&0xFF)<<24) +((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<8) +((file[offset++]&0xFF)));

					if ((modulePix[i] & 0x00FFFFFF) == 0x00FF00FF)
					{
						modulePix[i] = 0x00FF00FF;
					}
				}
			}
			else if (_pixel_format == PIXEL_FORMAT_8808)
			{
				// Convert from bytes to pixels
				size = size / 3;

				for (int i=0; i<size; i++)
				{
					modulePix[i] = (((file[offset++]&0xFF)<<16) +((file[offset++]&0xFF)<<8) +((file[offset++]&0xFF))) | 0xFF000000;

					if ((modulePix[i] & 0x00FFFFFF) == 0x00FF00FF)
					{
						modulePix[i] = 0x00FF00FF;
						_alpha = true;
					}
				}
			}
			else if (_pixel_format == PIXEL_FORMAT_4444)
			{
				size = size >> 1;

				for (int i=0; i<size; i++)
				{
					int a = ((file[offset]   & 0xF0)   ) | ((file[offset]  >>4) & 0x0F);
				    int r = ((file[offset]   & 0x0F)<<4) | ((file[offset]     ) & 0x0F);
				    int g = ((file[offset+1] & 0xF0)   ) | ((file[offset+1]>>4) & 0x0F);
				    int b = ((file[offset+1] & 0x0F)<<4) | ((file[offset+1]   ) & 0x0F);
				    offset += 2;

					modulePix[i] = (a<<24) | (r<<16) | (g<<8) | (b);

					if ((modulePix[i] & 0x00FFFFFF) == 0x00FF00FF)
					{
						modulePix[i] = 0x00FF00FF;
						_alpha = true;
					}

				    if (a != 0xFF)
				    {
						_alpha = true;

						if (a != 0)
						{
							_multiAlpha = true;
						}
					}
				}
			}
			else
			{
				Utils.Dbg("LoadData_useRaw: Unknown Pixel Format: 0x" + Integer.toHexString(_pixel_format));;
			}

			// Set the reference
			_module_image_intAAA[p][m] = modulePix;
		}
	}

	return offset;
}

private int SetSpecialColorAlpha(int _pixel_format,int color)
{
	if(DevConfig.JY_special_alpha_color == -1)
	{
		return color;
	}
	if((_pixel_format == PIXEL_FORMAT_8888))
	{
		if((DevConfig.JY_special_alpha_color & 0x00FFFFFF) == (color & 0x00FFFFFF))
		{
			_alpha = true;
			color = (DevConfig.JY_special_alpha_color & 0x00FFFFFF)|DevConfig.JY_special_alpha_depth;
		}
	}
	else if((_pixel_format == PIXEL_FORMAT_4444))
	{
		if(((DevConfig.JY_special_alpha_color & 0x00F0F0F0)|((DevConfig.JY_special_alpha_color & 0x00F0F0F0)>>4)) == (color & 0x00FFFFFF))
		{
			_alpha = true;
			color = (DevConfig.JY_special_alpha_color & 0x00FFFFFF)|DevConfig.JY_special_alpha_depth;
		}
	}
	else if((_pixel_format == PIXEL_FORMAT_1555))
	{
		if((DevConfig.JY_special_alpha_color & 0x00F8F8F8) == (color & 0x00FFFFFF))
		{
			_alpha = true;
			color = (DevConfig.JY_special_alpha_color & 0x00FFFFFF)|DevConfig.JY_special_alpha_depth;
		}
	}
	else if((_pixel_format == PIXEL_FORMAT_0565))
	{
		if((DevConfig.JY_special_alpha_color & 0x00F8FCF8) == (color & 0x00FFFFFF))
		{
			_alpha = true;
			color = (DevConfig.JY_special_alpha_color & 0x00FFFFFF)|DevConfig.JY_special_alpha_depth;
		}
	}
	return color;
}


// ASprite_BuildCacheImages.jpp
//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Build Cache Images for this sprite
	/// &param pal palette to be initialized
	/// &param m1 first module
	/// &param m2 last module (-1 . to end)
	/// &param pal_copy mapping to another palette (-1 . build)
	///
	/// &note GC is called twice internally here. So if used during runtime you should
	///       probably disable the GC by wrapping the call in DisableGC()/EnableGC().
	//------------------------------------------------------------------------------
	void BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    {
		AllocateCacheArrays(pal);
		
 		if (s_BCI_supportsCacheEffects && (s_BCI_effects != null))
 		{
			BCI_InitCacheEffect(pal);
		}

		if (DevConfig.sprite_useDynamicPng)
		{
			if (DevConfig.sprite_usePrecomputedCRC)
			{
                //////////////////////////////////////////////////////


			if (DevConfig.sprite_debugLoading){Utils.Dbg("BuildCacheImages(" + pal + ", " + m1 + ", " + m2 + ", " + pal_copy + ")... " + this);;};

			if (_nModules == 0)
			{
				return;
			}

			if (m2 == -1)
			{
				m2 = _nModules - 1;
			}

			if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
			{
				if (_module_image_imageAAA == null)
				{
					_module_image_imageAAA = new Image[_palettes][][];
				}

				if (_module_image_imageAAA[pal] == null)
				{
					_module_image_imageAAA[pal] = new Image[_nModules][1];
				}
			}
			else
			{
				if (_module_image_imageAA == null)
				{
					_module_image_imageAA = new Image[_palettes][];
				}

				if (_module_image_imageAA[pal] == null)
				{
					_module_image_imageAA[pal] = new Image[_nModules];
				}
			}

			if (pal_copy >= 0)
			{
				if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
				{
					Image [][][] _module_image_imageAAA = this._module_image_imageAAA;
					for (int i = m1; i <= m2; i++)
					{
						_module_image_imageAAA[pal][i] = _module_image_imageAAA[pal_copy][i];
					}
				}
				else
				{
					Image[][] _module_image_imageAA = this._module_image_imageAA;
					for (int i = m1; i <= m2; i++)
					{
						_module_image_imageAA[pal][i] = _module_image_imageAA[pal_copy][i];
					}
				}
			}
			else
			{
				int 	total_area;
				int 	total_size;
				long 	mem;

				int 	old_pal = _crt_pal;

				_crt_pal = pal;

				if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
				{
					System.gc();
				}

				if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
				{
					Image[][][] _module_image_imageAAA = this._module_image_imageAAA;
					byte[] _module_types = this._module_types;
					for (int i = m1; i <= m2; i++)
					{
                        if( DevConfig.sprite_useMultipleModuleTypes )
                        {
							if( _module_types[ i ] != MD_IMAGE )
								continue;
                        }

                        if (DevConfig.sprite_debugLoading){Utils.Dbg(" Caching image for module " + i);;};

						if ((GetModuleWidth(i) > 0) && (GetModuleHeight(i) > 0))
						{
							_module_image_imageAAA[pal][i][0] = BuildPNG8( _crt_pal, false, i, GetModuleWidth(i), GetModuleHeight(i), 0);
						}
					}
				}
				else
				{
					byte[] _module_types = this._module_types;
					Image[][] _module_image_imageAA = this._module_image_imageAA;
					for (int i = m1; i <= m2; i++)
					{
                        if( DevConfig.sprite_useMultipleModuleTypes )
                        {
							if( _module_types[ i ] != MD_IMAGE )
								continue;
                        }

                        if (DevConfig.sprite_debugLoading){Utils.Dbg(" Caching image for module " + i);;};

						if ((GetModuleWidth(i) > 0) && (GetModuleHeight(i) > 0))
						{
							_module_image_imageAA[pal][i] = BuildPNG8(_crt_pal, false, i, GetModuleWidth(i), GetModuleHeight(i), 0);
						}
					}
				}

				if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
				{
					System.gc();
				}

				_crt_pal = old_pal;
			}

			if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
			{
				System.gc();
			}


                //////////////////////////////////////////////////////
			}
		}
		else
		{
			if (DevConfig.sprite_debugLoading){Utils.Dbg("BuildCacheImages(" + pal + ", " + m1 + ", " + m2 + ", " + pal_copy + ")... " + this);;};

			if (_nModules == 0)
			{
				return;
			}

			if (m2 == -1)
			{
				m2 = _nModules - 1;
			}


			if (DevConfig.sprite_useSingleImageForAllModules)
			{
				if ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_SINGLE_IMAGE) != 0))
				{
					//////////////////////////////////////////////////////


 				try
				{
 					int[][][]  _module_image_intAAA = null;
 					Image[][] _module_image_imageAA = null;
					if (
						(DevConfig.sprite_useCacheRGBArrays) ||
						(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   		   )
					{
						_module_image_intAAA = this._module_image_intAAA;
						if (_module_image_intAAA[pal] == null)
						{
							_module_image_intAAA[pal] = new int[1][];
						}
					}
					else // if (zJYLibConfig.sprite_useCacheRGBArrays)
					{
						_module_image_imageAA = this._module_image_imageAA;
						if (_module_image_imageAA[pal] == null)
						{
							_module_image_imageAA[pal] = new Image[1];
						}
					}

					int _PLTE_size = _colors * 3 + 4;
					int _TRNS_size = _colors + 4;

					byte[] _data = GetModuleData(0);
					byte[] _pal_data = this._pal_data;
					// copy palette
					System.arraycopy(_pal_data, pal * (_PLTE_size + _TRNS_size), _data, 41, _PLTE_size);

					// copy transparency
					System.arraycopy(_pal_data, pal * (_PLTE_size + _TRNS_size) + _PLTE_size, _data, 41 + _PLTE_size + 8, _TRNS_size);

					if (
						(DevConfig.sprite_useCacheRGBArrays) ||
						(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
					   )
					{
						_module_image_intAAA[pal][0] = new int[_data.length];

						System.arraycopy(_data, 0, _module_image_intAAA[pal][0], 0, _data.length);

						if (DevConfig.sprite_debugUsedMemory)
						{
							_images_count++;
							_images_size += _data.length;
						}
					}
					
					else // if (zJYLibConfig.sprite_useCacheRGBArrays)
					{
						_module_image_imageAA[pal][0] = GLLib.CreateImage(_modules_data, 0, _data.length);

						if (DevConfig.sprite_debugUsedMemory)
						{
							_images_count++;
							_images_size += _module_image_imageAA[pal][0].getWidth() * _module_image_imageAA[pal][0].getHeight();
						}
					}

				}
				catch (Exception e)
				{
					Utils.Dbg("exception "+e);
					//e.printStackTrace();
				}

					//////////////////////////////////////////////////////
				}
			}

			if (! DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
			{
				System.gc();
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Sets the cache for this module for this palette using the passed in data.
	///
	/// &param img_data - The image data to use in creating the cache.
	/// &param W - The width of the image that is being cached.
	/// &param H - The height of the image that is being cached.
	/// &param palID - The cache slot in which to cache this image.
	/// &param modID - The module slot in which to cache this image.
	///
	/// &note Memory will be allocated! Either an IMAGE or INT[].
	//------------------------------------------------------------------------------
	public void SetCache (int[] img_data, int W, int H, int palID, int modID)
	{
		int size = W * H;

		if (
			(DevConfig.sprite_useCacheRGBArrays) ||
			(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
		   )
		{
				int[][][] _module_image_intAAA = this._module_image_intAAA;
				if ((_module_image_intAAA[palID][modID] == null) || (_module_image_intAAA[palID][modID].length != size))
				{
					_module_image_intAAA[palID][modID] = new int[size];
				}

				System.arraycopy(img_data, 0, _module_image_intAAA[palID][modID], 0, size);
		}
		else
		{
			// Checck if this image contains any alpha data
			boolean bAlpha = false;

			for (int ii = 0; ii < size; ii++)
			{
				if ((img_data[ii] & 0xFF000000) != 0xFF000000)
				{
					bAlpha = true;
					break;
				}
			}

			// Using WRAPPER CALL here
			_module_image_imageAA[palID][modID] = GLLib.CreateRGBImage(img_data, W, H, bAlpha);
			

		}
	}

	//------------------------------------------------------------------------------
	/// Sets the cache for this module for this palette using the passed in data.
	///
	/// &param img_data - The image data to use in creating the cache.
	/// &param W - The width of the image that is being cached.
	/// &param H - The height of the image that is being cached.
	/// &param palID - The cache slot in which to cache this image.
	/// &param modID - The module slot in which to cache this image.
	///
	/// &note Memory will be allocated! Either an IMAGE or SHORT[].
	/// &note For IMAGE cache this currently requires the NokiaUI.
	//------------------------------------------------------------------------------
	public void SetCache (short[] img_data, int W, int H, int palID, int modID)
	{
		if(!(DevConfig.sprite_useNokiaUI))Utils.DBG_PrintStackTrace(false, "SetCache: using short[] to set cache without NokiaUI. Why do this?");;

		int size = W * H;


		if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
		{
			// Checck if this image contains any alpha data
			boolean bAlpha = false;

			for (int ii = 0; ii < size; ii++)
			{
				if ((img_data[ii] & 0xF000) != 0xF000)
				{
					bAlpha = true;
					break;
				}
			}

			
				Utils.Dbg("Trying to cache IMAGE from short[] without Nokia UI!!");;

		}
		else

		{
			short[][][] _modules_image_shortAAA = this._modules_image_shortAAA;
			if ((_modules_image_shortAAA[palID][modID] == null) || (_modules_image_shortAAA[palID][modID].length != size))
			{
				_modules_image_shortAAA[palID][modID] = null;
				_modules_image_shortAAA[palID][modID] = new short[size];
			}

			System.arraycopy(img_data, 0, _modules_image_shortAAA[palID][modID], 0, size);
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Will build all the modules needed for ALL the sprites frames for the
	/// given module mapping. Used to avoid loading all modules when only some
	/// mappings are used.
	///
	/// &param palette Sprite palette index. Will only cache this palette.
	/// &param modMap  Module Mapping index. Will only cache these modules.
	///
    /// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
	/// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
	/// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
	/// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	//------------------------------------------------------------------------------
	final void BuildModuleMappedCacheImages (int palette, int modMap)
	{
		BuildFrameCacheImages(palette, 0, -1, modMap);
	}

    //------------------------------------------------------------------------------
    /// Create cache images for all modules used by a frame.
    ///
    /// &param palette  Sprite palette index. Will be ignored if the sprite is
    ///                 exported using BS_FM_PALETTE flag and if
    ///                 zJYLibConfig.sprite_useFMPalette is enabled.
    /// &param frame    Frame index
    ///
    /// &see    zSprite&BS_FM_PALETTE
    /// &see    zJYLibConfig&sprite_useFMPalette
    /// &see    zJYLibConfig&sprite_useModuleMapping
	/// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
	/// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
	/// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
    //------------------------------------------------------------------------------
    final void BuildFrameCacheImages (int palette, int frame)
	{
		BuildFrameCacheImages(palette, frame, -1);
	}

	//------------------------------------------------------------------------------
	/// Create cache images for all modules used by a frame.
	///
	/// &param palette  Sprite palette index. Will be ignored if the sprite is
	///                 exported using BS_FM_PALETTE flag and if
	///                 zJYLibConfig.sprite_useFMPalette is enabled.
	/// &param frame    Frame index
	/// &param modMap   The module mapping to use, -1 means no mapping.
	///
	/// &see    zSprite&BS_FM_PALETTE
	/// &see    zJYLibConfig&sprite_useFMPalette
	/// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
    /// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
	//------------------------------------------------------------------------------
	void BuildFrameCacheImages (int palette, int frame, int modMap)
	{
		boolean saveEnabled = s_gcEnabled;

		if (!DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
		{
			System.gc();

			// Disable GC so that it is not called over and over per each call to BuildCacheImages
			s_gcEnabled = false;
		}

		// Iterate on this frame fmodules list
		int numModules = GetFModules(frame);
		for (int m = 0; m < numModules; m++)
		{
			int moduleId = GetFrameModule(frame, m);
			int fm_flags = GetFrameModuleFlags(frame, m);
			if((DevConfig.sprite_useHyperFM) && ((fm_flags & FLAG_HYPER_FM) != 0))
			{
				BuildFrameCacheImages(palette, moduleId, modMap);
			}
			else
			{
				if (modMap >= 0)
				{
					Utils.DBG_PrintStackTrace(false, "BuildFrameCacheImages: Module Mapping parameter is set but module mapping is not enabled!");;
				}


				int modulePal = palette;

				// Check if the frame holds information about module palette
				if (DevConfig.sprite_useFMPalette &&
				   (!DevConfig.sprite_useBSpriteFlags || (_bs_flags & zSprite.BS_FM_PALETTE) != 0))
				{
					modulePal = GetFrameModulePalette(frame, m);
				}

				// Check if this module has no cache image for this palette
				if (GetModuleImage(moduleId, modulePal) == null)
				{
					// Create the cache image
					BuildCacheImages(modulePal, moduleId, moduleId, -1);
				}
			}
		}

		if (!DevConfig.sprite_useDeactivateSystemGc)
		{
			// Restore variable to what it was
			s_gcEnabled = saveEnabled;

			if (s_gcEnabled)
			{
				System.gc();
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Will build all the modules for all the sprites frames of a given range.
	/// Used to avoid loading all modules when only some mappings are used.
	///
	/// &param palette Sprite palette index. Will only cache this palette.
	/// &param start   The starting index of the range of frames.
	/// &param end     The ending index of the range of frames. Use -1 to build to last frame.
	/// &param modMap  Module Mapping index. Will only cache these modules.
	///
    /// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
    /// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
	//------------------------------------------------------------------------------
	final void BuildFrameCacheImages (int palette, int start, int end, int modMap)
	{
		if (end == -1)
		{
			end = GetFrames();
		}

		boolean saveEnabled = s_gcEnabled;

		if (!DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
		{
			System.gc();

			// Disable GC so that it is not called over and over per each call to BuildCacheImages
			s_gcEnabled = false;
		}

		for (int frameIdx = start; frameIdx < end; frameIdx++)
		{
			BuildFrameCacheImages(palette, frameIdx, modMap);
		}

		if (!DevConfig.sprite_useDeactivateSystemGc)
		{
			// Restore variable to what it was
			s_gcEnabled = saveEnabled;

			if (s_gcEnabled)
			{
				System.gc();
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /// Create cache images for all modules all frame used by a animation. This
    /// funcion will call BuildFrameCacheImages for each animation frame.
    ///
    /// &param palette  Sprite palette index. Will be ignored if the sprite is
    ///                 exported using BS_FM_PALETTE flag and if
    ///                 zJYLibConfig.sprite_useFMPalette is enabled.
    /// &param anim     Animation index
    ///
    /// &see    zSprite&BS_FM_PALETTE
    /// &see    zJYLibConfig&sprite_useFMPalette
	/// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
    //------------------------------------------------------------------------------
    final void BuildAnimCacheImages (int palette, int anim)
    {
		BuildAnimCacheImages(palette, anim, -1);
	}

    //------------------------------------------------------------------------------
    /// Create cache images for all modules all frame used by a animation. This
    /// funcion will call BuildFrameCacheImages for each animation frame.
    ///
    /// &param palette  Sprite palette index. Will be ignored if the sprite is
    ///                 exported using BS_FM_PALETTE flag and if
    ///                 zJYLibConfig.sprite_useFMPalette is enabled.
    /// &param anim     Animation index
    /// &param modMap   The module mapping to use, -1 means no mapping.
    ///
    /// &see    zSprite&BS_FM_PALETTE
    /// &see    zJYLibConfig&sprite_useFMPalette
	/// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
    /// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int start, int end, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
    //------------------------------------------------------------------------------
    void BuildAnimCacheImages (int palette, int anim, int modMap)
    {
		boolean saveEnabled = s_gcEnabled;

		if (!DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
		{
			System.gc();

			// Disable GC so that it is not called over and over per each call to BuildCacheImages
			s_gcEnabled = false;
		}

        // Iterate on this animation's frame list
        int numFrames = GetAFrames(anim);
        for (int f = 0; f < numFrames; f++)
        {
            // Get real frame ID from aframe index and create cache images
            int frameId = GetAnimFrame(anim, f);
            BuildFrameCacheImages(palette, frameId, modMap);
        }

        if (!DevConfig.sprite_useDeactivateSystemGc)
		{
			// Restore variable to what it was
			s_gcEnabled = saveEnabled;

			if (s_gcEnabled)
			{
				System.gc();
			}
		}
    }

    //------------------------------------------------------------------------------
	/// Create cache images for all modules in all frames used by the given range
	/// of animatins. This funcion will call BuildAnimCacheImages for each
	/// animation.
	///
	/// &param palette  Sprite palette index. Will be ignored if the sprite is
	///                 exported using BS_FM_PALETTE flag and if
	///                 zJYLibConfig.sprite_useFMPalette is enabled.
	/// &param start    Animation index to start from.
	/// &param end      Animation index to end at. Use -1 to go to last anim.
	/// &param modMap   The module mapping to use, -1 means no mapping.
	///
	/// &see    zSprite&BS_FM_PALETTE
	/// &see    zJYLibConfig&sprite_useFMPalette
	/// &see    zJYLibConfig&sprite_useModuleMapping
    /// &see    zSprite&BuildCacheImages(int pal, int m1, int m2, int pal_copy)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int frame, int modMap)
    /// &see    zSprite&BuildFrameCacheImages (int palette, int start, int end, int modMap)
    /// &see    zSprite&BuildAnimCacheImages (int palette, int anim)
	/// &see    zSprite&BuildAnimCacheImages (int palette, int anim, int modMap)
	/// &see    zSprite&BuildModuleMappedCacheImages (int palette, int modMap)
	//------------------------------------------------------------------------------
	void BuildAnimCacheImages (int palette, int start, int end, int modMap)
	{
		if (end == -1)
		{
			end = GetAnimationCount();
		}

		boolean saveEnabled = s_gcEnabled;

		if (!DevConfig.sprite_useDeactivateSystemGc && s_gcEnabled)
		{
			System.gc();

			// Disable GC so that it is not called over and over per each call to BuildCacheImages
			s_gcEnabled = false;
		}

		for( int a=start; a<end; a++)
		{
			BuildAnimCacheImages(palette, a, modMap);
		}

		if (!DevConfig.sprite_useDeactivateSystemGc)
		{
			// Restore variable to what it was
			s_gcEnabled = saveEnabled;

			if (s_gcEnabled)
			{
				System.gc();
			}
		}
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Set the Module's Image of a specific Palette
	/// &param pImg Image to set in the zSprite
	/// &param nModule Module number
	/// &param nPalette Palette number
	//------------------------------------------------------------------------------
	void SetModuleImage (Image pImg, int nModule, int nPalette)
    {
		Image[][] _module_image_imageAA = this._module_image_imageAA;
		if(_module_image_imageAA != null)
		{
			if((nPalette >= 0) && (nPalette < _module_image_imageAA.length) && (_module_image_imageAA[nPalette] != null))
			{
				if((nModule >= 0) && (nModule < _module_image_imageAA[nPalette].length))
				{
					_module_image_imageAA[nPalette][nModule] = pImg;
				}
			}

			return;
		}
		Image[][][] _module_image_imageAAA = this._module_image_imageAAA;
		if(_module_image_imageAAA != null)
		{
			if((nPalette >= 0) && (nPalette < _module_image_imageAAA.length) && (_module_image_imageAAA[nPalette] != null))
			{
				if((nModule >= 0) && (nModule < _module_image_imageAAA[nPalette].length))
				{
					_module_image_imageAAA[nPalette][nModule][0] = pImg;
				}
			}

			return;
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Sets the module cached image array
	/// &param pData image array
	//------------------------------------------------------------------------------
	void SetModuleImagesArray (Object pData)
	{
		if (DevConfig.sprite_useNokiaUI)
		{

			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				_module_image_imageAA = (Image[][]) pData;
			}
			else

			{
				_modules_image_shortAAA = (short[][][]) pData;
			}
		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				_module_image_intAAA = (int[][][]) pData;
			}
			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
			{
				_module_image_imageAA = (Image[][]) pData;
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Gets the module cached image array
	/// &return image array
	//------------------------------------------------------------------------------
	Object GetModuleImagesArray ()
	{
		if (DevConfig.sprite_useNokiaUI)
		{

			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				return _module_image_imageAA;
			}
			else

			{
				return _modules_image_shortAAA;
			}
		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				return _module_image_intAAA;
			}
			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
			{
				return _module_image_imageAA;
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Sets the module cache for this sprite using another sprite
	/// No actual copy, just setting references
	/// &param sprite is the sprite from which we will set the image cache ref
	//------------------------------------------------------------------------------
	void SetModuleImagesArray (zSprite sprite)
	{
		if (DevConfig.sprite_useManualCacheRGBArrays)
		{
			if(!((this._flags & FLAG_USE_CACHE_RGB) == (sprite._flags & FLAG_USE_CACHE_RGB)))Utils.DBG_PrintStackTrace(false, "Error in SetModuleImagesArray: both sprites must have same cache format");;
		}

		SetModuleImagesArray( sprite.GetModuleImagesArray() );
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Allocates the arrays needed for the cache.
	/// &param cacheID - The cache slot within possible slots to allocate.
	//------------------------------------------------------------------------------
	private void AllocateCacheArrays (int cacheID)
	{
		if (DevConfig.sprite_useNokiaUI)
		{

			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				if (_module_image_imageAA == null)
				{
					_module_image_imageAA = new Image[_palettes][];
				}

				if (_module_image_imageAA[cacheID] == null)
				{
					_module_image_imageAA[cacheID] = new Image[_nModules];
				}
			}
			else

			{
				if (_modules_image_shortAAA == null)
				{
					_modules_image_shortAAA = new short[_palettes][][];
				}

				if (_modules_image_shortAAA[cacheID] == null)
				{
					_modules_image_shortAAA[cacheID] = new short[_nModules][];
				}
			}

		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				if (_module_image_intAAA == null)
				{
					_module_image_intAAA = new int[_palettes][][];
				}

				if (_module_image_intAAA[cacheID] == null)
				{
					_module_image_intAAA[cacheID] = new int[_nModules][];
				}
			}
			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
			{
				if (_module_image_imageAA == null)
				{
					_module_image_imageAA = new Image[_palettes][];
				}

				if (_module_image_imageAA[cacheID] == null)
				{
					_module_image_imageAA[cacheID] = new Image[_nModules];
				}
			}
		}
	}

	//------------------------------------------------------------------------------
	/// Allocates an extra cache slot.
	/// &note Only works if the cache has already been allocated.
	/// &return The cache slot ID if allocate, -1 if operation failed.
	//------------------------------------------------------------------------------
	int AllocateExtraCache ()
	{
		int cacheID = -1;

		if (DevConfig.sprite_useNokiaUI)
		{

			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				// Create new cache slot for this palette if the allocation already occured
				if (_module_image_imageAA != null)
				{
					int size = _module_image_imageAA.length + 1;
					Image[][] temp = _module_image_imageAA;
					_module_image_imageAA = new Image[size][];

					// Copy existing refs over
					for( int i=0; i<size-1; i++)
					{
						_module_image_imageAA[i] = temp[i];
					}

					cacheID = size-1;
				}
			}
			else

			{
				if (_modules_image_shortAAA != null)
				{
					int size = _modules_image_shortAAA.length + 1;
					short[][][] temp = _modules_image_shortAAA;
					short[][][] _modules_image_shortAAA = this._modules_image_shortAAA = new short[size][][];
					// Copy existing refs over
					for( int i=0; i<size-1; i++)
					{
						_modules_image_shortAAA[i] = temp[i];
					}

					cacheID = size-1;
				}
			}
		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				int[][][] _module_image_intAAA = this._module_image_intAAA;
				if (_module_image_intAAA != null)
				{
					int size = _module_image_intAAA.length + 1;
					int[][][] temp = _module_image_intAAA;
					_module_image_intAAA = this._module_image_intAAA = new int[size][][];

					// Copy existing refs over
					for( int i=0; i<size-1; i++)
					{
						_module_image_intAAA[i] = temp[i];
					}

					cacheID = size-1;
				}
			}
			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
			{
				// Create new cache slot for this palette if the allocation already occured
				Image[][] _module_image_imageAA = this._module_image_imageAA;
				if (_module_image_imageAA != null)
				{
					int size = _module_image_imageAA.length + 1;
					Image[][] temp = _module_image_imageAA;
					_module_image_imageAA = this._module_image_imageAA = new Image[size][];

					// Copy existing refs over
					for( int i=0; i<size-1; i++)
					{
						_module_image_imageAA[i] = temp[i];
					}

					cacheID = size-1;
				}
			}
		}

		if (cacheID > -1)
		{
			// TODO: Seperate _palettes into PalNUM and CacheNUM
			_palettes = cacheID + 1;

			AllocateCacheArrays(cacheID);
		}

		return cacheID;
	}

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Frees a cached module
	/// &param nPal The palette index
	/// &param nMod The module to be freed (-1 for all modules in this palette)
	//------------------------------------------------------------------------------
	void FreeModuleImage(int nPal, int nMod)
	{
		if (DevConfig.sprite_useNokiaUI)
		{

			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				if( (_module_image_imageAA != null) && (nPal < _module_image_imageAA.length) )
				{
					if(nMod == -1)
						_module_image_imageAA[nPal] = null;
					else
					{
						if(_module_image_imageAA[nPal] != null)
						{
							_module_image_imageAA[nPal][nMod] = null;
						}
					}
				}
			}
			else

			{
				if( (_modules_image_shortAAA != null) && (nPal < _modules_image_shortAAA.length) )
				{
					if(nMod == -1)
						_modules_image_shortAAA[nPal] = null;
					else
					{
						if(_modules_image_shortAAA[nPal] != null)
						{
							_modules_image_shortAAA[nPal][nMod] = null;
						}
					}
				}
			}
		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				if( (_module_image_intAAA != null) && (nPal < _module_image_intAAA.length) )
				{
					if(nMod == -1)
						_module_image_intAAA[nPal] = null;
					else
					{
						if(_module_image_intAAA[nPal] != null)
						{
							_module_image_intAAA[nPal][nMod] = null;
						}
					}
				}
			} // if (zJYLibConfig.sprite_useCacheRGBArrays)
			else if( DevConfig.sprite_useDynamicPng)
			{
				if ( (!DevConfig.sprite_useLoadImageWithoutTransf) && ( (DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip)))
				{
					if( (_module_image_imageAAA != null) && (nPal < _module_image_imageAAA.length) )
					{
						if(nMod == -1)
							_module_image_imageAAA[nPal] = null;
						else
						{
							if(_module_image_imageAAA[nPal] != null)
							{
								_module_image_imageAAA[nPal][nMod] = null;
							}
						}
					}
				}
				else
				{
					if( (_module_image_imageAA != null) && (nPal < _module_image_imageAA.length) )
					{
						if(nMod == -1)
							_module_image_imageAA[nPal] = null;
						else
						{
							if(_module_image_imageAA[nPal] != null)
							{
								_module_image_imageAA[nPal][nMod] = null;
							}
						}
					}
				}

			} // if( zJYLibConfig.sprite_useDynamicPng)
			else
			{
				if( (_module_image_imageAA != null) && (nPal < _module_image_imageAA.length) )
				{
					if(nMod == -1)
						_module_image_imageAA[nPal] = null;
					else
					{
						if(_module_image_imageAA[nPal] != null)
						{
							_module_image_imageAA[nPal][nMod] = null;
						}
					}
				}
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /// Free cache images for all modules used by a frame.
    ///
    /// &param palette  Sprite palette index. Will be ignored if the sprite is
    ///                 exported using BS_FM_PALETTE flag and if
    ///                 zJYLibConfig.sprite_useFMPalette is enabled.
    /// &param frame    Frame index
    ///
    /// &see    zSprite&FreeModuleImage(int nPal, int nMod)
    /// &see    zJYLibConfig&sprite_useFMPalette
    /// &see    zSprite&BS_FM_PALETTE
    //------------------------------------------------------------------------------
    void FreeFrameCacheImages(int palette, int frame)
    {
        // Iterate on this frame fmodules list
        int numModules = GetFModules(frame);
        for (int m = 0; m < numModules; m++)
        {
            int moduleId = GetFrameModule(frame, m);
            int fm_flags = GetFrameModuleFlags(frame, m);
            if((DevConfig.sprite_useHyperFM) && ((fm_flags & FLAG_HYPER_FM) != 0))
    		{
            	FreeFrameCacheImages(palette, moduleId);
    		}
            else
            {
	            int modulePal = palette;

	            // Check if the frame holds information about module palette
	            if (DevConfig.sprite_useFMPalette &&
	               (!DevConfig.sprite_useBSpriteFlags || (_bs_flags & zSprite.BS_FM_PALETTE) != 0))
	            {
	                modulePal = GetFrameModulePalette(frame, m);
	            }

	            // Check if this module has a cache image for this palette
	            if (GetModuleImage(moduleId, modulePal) != null)
	            {
	                // Free the cache image
	                FreeModuleImage(modulePal, moduleId);
	            }
    		}
        }
    }

//--------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /// Free cache images for all modules of all frames used by a animation. This
    /// funcion will call FreeFrameCacheImages(int palette, int frame)
    /// for each animation frame.
    ///
    /// &param palette  Sprite palette index. Will be ignored if the sprite is
    ///                 exported using BS_FM_PALETTE flag and if
    ///                 zJYLibConfig.sprite_useFMPalette is enabled.
    /// &param anim     Animation index
    ///
    /// &see    zSprite&FreeFrameCacheImages(int palette, int frame)
    /// &see    zSprite&FreeModuleImage(int nPal, int nMod)
    /// &see    zJYLibConfig&sprite_useFMPalette
    /// &see    zSprite&BS_FM_PALETTE
    //------------------------------------------------------------------------------
    void FreeAnimCacheImages(int palette, int anim)
    {
        // Iterate on this animation's frame list
        int numFrames = GetAFrames(anim);
        for (int f = 0; f < numFrames; f++)
        {
            // Get real frame ID from aframe index and free cache images
            int frameId = GetAnimFrame(anim, f);
            FreeFrameCacheImages(palette, frameId);
        }
    }

//--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Free data after the modules have been cached
	//------------------------------------------------------------------------------
	void FreeMemory()
	{
		if (DevConfig.sprite_usePrecomputedCRC)
		{
			if (DevConfig.sprite_useNokiaUI)
			{
				if (_pal_short != null){short[][]_pal_short = this._pal_short; for (int _i = _pal_short.length - 1; _i >=0 ; _i--){ _pal_short[_i] = null; } this._pal_short = null; };
			}
			else
			{
				if (_pal_int != null){int[][]_pal_int = this._pal_int; for (int _i = _pal_int.length - 1; _i >=0 ; _i--){ _pal_int[_i] = null; } this._pal_int = null; };
			}

			if (_transp != null){byte[][] _transp = this._transp; for (int _i = _transp.length-1; _i >=0; _i--){ _transp[_i] = null; } this._transp = null; };

			if (DevConfig.sprite_useDoubleArrayForModuleData)
			{
				if (_modules_data_array != null){byte[][]_modules_data_array = this._modules_data_array; for (int _i = _modules_data_array.length - 1; _i >=0; _i--){ _modules_data_array[_i] = null; } this._modules_data_array = null; };
			}
			else
			{
				_modules_data 			= null;

            	if( DevConfig.sprite_useModuleDataOffAsShort )
            	{
            	    _modules_data_off_short = null;
            	}
            	else
            	{
				    _modules_data_off_int	= null;
            	}
			}

			_PNG_packed_PLTE_CRC 	= null;
			_PNG_packed_IHDR_CRC 	= null;
			_PNG_packed_IDAT_CRC 	= null;
			_PNG_packed_IDAT_ADLER 	= null;
			_PNG_packed_tRNS_CRC 	= null;


            if( DevConfig.sprite_useMultipleModuleTypes )
            {
                _module_types = null;

                if( DevConfig.sprite_useModuleColorAsByte )
                {
                    _module_colors_byte = null;
                }else
                {
                    _module_colors_int  = null;
                }
            }
		}
    }

	//------------------------------------------------------------------------------
	/// Free all the cached module images
	//------------------------------------------------------------------------------
	void FreeCachedModules()
	{
    	if (DevConfig.sprite_useNokiaUI)
		{
			if (!DevConfig.sprite_useManualCacheRGBArrays || ((_flags & FLAG_USE_CACHE_RGB) == 0))
			{
				if (_module_image_imageAA != null){ Image[][]_module_image_imageAA = this._module_image_imageAA; for (int _i = _module_image_imageAA.length - 1; _i >=0 ; _i--){ _module_image_imageAA[_i] = null; } this._module_image_imageAA = null; };
			}
			else
			{
				short[][][] _modules_image_shortAAA = this._modules_image_shortAAA;
				if (_modules_image_shortAAA != null){ for (int _j = 0; _j < _modules_image_shortAAA.length; _j++){ if (_modules_image_shortAAA[_j] != null){ for (int _i = 0; _i < _modules_image_shortAAA[_j].length; _i++){ _modules_image_shortAAA[_j][_i] = null; } _modules_image_shortAAA[_j] = null; }; } this._modules_image_shortAAA = null; };
			}
		}
		else // if (zJYLibConfig.sprite_useNokiaUI)
		{
			if (
				(DevConfig.sprite_useCacheRGBArrays) ||
				(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   )
			{
				int[][][] _module_image_intAAA = this._module_image_intAAA;
				if (_module_image_intAAA != null){ for (int _j = 0; _j < _module_image_intAAA.length; _j++){ if (_module_image_intAAA[_j] != null){ for (int _i = 0; _i < _module_image_intAAA[_j].length; _i++){ _module_image_intAAA[_j][_i] = null; } _module_image_intAAA[_j] = null; }; } this._module_image_intAAA = null; };
			}
			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
			{
				Image[][] _module_image_imageAA = this._module_image_imageAA;
				if (_module_image_imageAA != null){ for (int _i = 0; _i < _module_image_imageAA.length; _i++){ _module_image_imageAA[_i] = null; } this._module_image_imageAA = null; };
			}
		}
	}


//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------


// ASprite_PaintModule.jpp
//--------------------------------------------------------------------------------------------------------------------


    private int GetModuleExtraInfoOffset(int module)
    {
    	short[] _modules_extra_pointer = this._modules_extra_pointer;
        if(_modules_extra_pointer != null)
        {
            for(int i = 0; i < _modules_extra_pointer.length; i += 2)
            {
                if(_modules_extra_pointer[i] == module)
                {
                    return _modules_extra_pointer[i + 1];
                }
            }
        }

        return -1;
    }


	//------------------------------------------------------------------------------
	/// Draws a module at PosX/PosY with Flags
	/// &param g The Graphics context
	/// &param module The frame to be drawn
	/// &param posX The X coordinate to be drawn to
	/// &param posY The Y coordinate to be drawn to
	/// &param flags The flags to be used for this operation
	//------------------------------------------------------------------------------
	void PaintModule(int module, int posX, int posY, int flags)
	{
		PaintModule(GLLib.g, module, posX, posY, flags);
	}

	void PaintModule(Graphics g, int module, int posX, int posY, int flags) {
		if (DevConfig.sprite_debugTogglePaintModule && s_debugSkipPaintModule) {
			return;
		}

		if (true && _bTraceNow) {
			Utils.Dbg("PaintModule(g_  " + module + ", " + posX + ", " + posY
					+ ", 0x" + Integer.toHexString(flags) + ")");
			;
		}

		if (DevConfig.sprite_useOperationMark) {
			if (_operation == OPERATION_MARK) {
				_modules_usage[module] |= (1 << (flags & 0x07));
				return;
			}
		}

		int sizeX = GetModuleWidth(module);
		int sizeY = GetModuleHeight(module);

		// Only referenced if using Pixel Effects (otherwise should obfuscate
		// away)
		int noneRotatedSizeX = sizeX;
		int noneRotatedSizeY = sizeY;

		if ((flags & FLAG_ROT_90) != 0) {
			int tmp = sizeX;
			sizeX = sizeY;
			sizeY = tmp;
		}
		byte[] _module_types = this._module_types;
		byte[] _module_colors_byte;
		int[] _module_colors_int;
		if (DevConfig.sprite_useModuleColorAsByte) {
			_module_colors_byte = this._module_colors_byte;
		} else {
			_module_colors_int = this._module_colors_int;
		}
		short[] _modules_extra_info = this._modules_extra_info;
		
		int _crt_pal = this._crt_pal;
		
		if (DevConfig.sprite_useMultipleModuleTypes) {
			if (_module_types[module] != MD_IMAGE && g != null) {
				if (DevConfig.sprite_useModuleColorAsByte) {
					g.setColor(_module_colors_byte[module]);
				} else {
					if (DevConfig.pfx_useSpriteEffectBlend
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_GRAYSCALE)) {
						g.setColor(cPFX
								.PFX_GetGrayscaleColor(_module_colors_int[module]));
					} else {
						g.setColor(_module_colors_int[module]);
					}
				}
				// Scale the size of when this PFX is turned on
				if (DevConfig.pfx_enabled
						&& DevConfig.pfx_useSpriteEffectScale && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_SCALE)) {
					int percent = cPFX.PFX_GetParam(
							GLPixEffects.k_EFFECT_SCALE,
							GLPixEffects.k_PARAM_SCALE_PERCENT);

					sizeX = (sizeX * percent) / 100;
					sizeY = (sizeY * percent) / 100;
				}
				// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
				if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g)) {
					if ((posX + sizeX < g.getClipX())
							|| (posX > g.getClipX() + g.getClipWidth()))
						return;
					if ((posY + sizeY < g.getClipY() || (posY > g
							.getClipY() + g.getClipHeight())))
						return;
				}

				switch (_module_types[module]) {
				case MD_FILL_RECT:

					if (DevConfig.sprite_useModuleColorAsByte) {
						GLLib.FillRect(g, posX, posY, sizeX, sizeY);
					} else {
						// OPAQUE RECT: Full-alpha or 0 alpha (due to how Aurora
						// has been working) yield normal fillRect
						if (((_module_colors_int[module] & 0xFF000000) == 0xFF000000)
								|| ((_module_colors_int[module] & 0xFF000000) == 0x00000000)) {
							// BLENDING: Applying alpha effect to solid Rect
							if (DevConfig.pfx_useSpriteEffectBlend
									&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_BLEND)) {
								int alpha = cPFX.PFX_GetParam(
										GLPixEffects.k_EFFECT_BLEND,
										GLPixEffects.k_PARAM_BLEND_AMOUNT);

								GLLib.AlphaRect_SetColor((_module_colors_int[module] & 0x00FFFFFF)
										| (alpha << 24));
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
							// NORMAL Solid Rect
							else {
								GLLib.FillRect(g, posX, posY, sizeX, sizeY);
							}
						}
						// ALPHA RECT: Rect in Aurora has Alpha Color
						else {
							// GRAYSCALE effect
							if (DevConfig.pfx_useSpriteEffectBlend
									&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_GRAYSCALE)) {
								GLLib.AlphaRect_SetColor(cPFX
										.PFX_GetGrayscaleColor(_module_colors_int[module]));
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
							// NORMAL ALPHA rect
							else {
								GLLib.AlphaRect_SetColor(_module_colors_int[module]);
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
						}
					}
					break;

				case MD_RECT:

					if (!DevConfig.sprite_useOriginalDrawRect) {
						sizeX--;
						sizeY--;
					}

					if (DevConfig.pfx_useSpriteEffectBlend
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_BLEND)) {
						int alpha = cPFX.PFX_GetParam(
								GLPixEffects.k_EFFECT_BLEND,
								GLPixEffects.k_PARAM_BLEND_AMOUNT);
						GLLib.DrawAlphaRect(g, posX, posY, sizeX, sizeY,
								(_module_colors_int[module] & 0x00FFFFFF)
										| (alpha << 24));
					} else {
						g.drawRect(posX, posY, sizeX, sizeY);
					}

					break;

				case MD_ARC:
				case MD_FILL_ARC: {
					// Find angles
					int nOffset = GetModuleExtraInfoOffset(module);

					if (nOffset != -1) {
						int sa = _modules_extra_info[nOffset + 0];
						int a = _modules_extra_info[nOffset + 1];

						if ((flags & FLAG_FLIP_X) != 0) {
							sa = 90 - sa;
						}
						if ((flags & FLAG_FLIP_Y) != 0) {
							sa = -sa;
							a = -a;
						}
						if ((flags & FLAG_ROT_90) != 0) {
							sa = sa - 90;
						}

						if (_module_types[module] == MD_ARC) {
							g.drawArc(posX, posY, sizeX, sizeY, sa, a);
						} else {
							g.fillArc(posX, posY, sizeX, sizeY, sa, a);
						}
					}
				}
					break;

				// TODO: Handle transformation flags correctly (flipX/Y and
				// Rot90)
				case MD_TRIANGLE:
				case MD_FILL_TRIANGLE:
					/** JYlib中使用这两个类型来做自己的数据格式定义 */
					if (DevConfig.JY_USE_TRIANGLE_MODULE_INFO) 
					{
						if (_player != null && _player.canTriggerListener()) {
							int color;
							if (DevConfig.sprite_useModuleColorAsByte) {
								color = _module_colors_byte[module];
							} else {
								color = _module_colors_int[module];
							}

							int nOffset = GetModuleExtraInfoOffset(module);
							//int[] params  = new int[] {1,2,3,4,5};
							int[] params = new int[]{color,
									(int)_modules_extra_info[nOffset + 0],
									(int)_modules_extra_info[nOffset + 1],
									(int)_modules_extra_info[nOffset + 2],
									(int)_modules_extra_info[nOffset + 3]
								};
							_player.triggerListener(params);
						}
					} else {
						
                            //Find tria
                            int nOffset = GetModuleExtraInfoOffset( module );

                            if( nOffset != -1)
                            {
                                int p2x = _modules_extra_info[nOffset + 0];
                                int p2y = _modules_extra_info[nOffset + 1];
                                int p3x = _modules_extra_info[nOffset + 2];
                                int p3y = _modules_extra_info[nOffset + 3];

                                if ((flags & FLAG_FLIP_X) != 0)
								{
									p3x = -p3x;
									p2x = -p2x;
								}
								if ((flags & FLAG_FLIP_Y) != 0)
								{
									p3y = -p3y;
									p2y = -p2y;
								}
								if ((flags & FLAG_ROT_90) != 0)
								{
									int tpx = p2x;
									
									p2x = -p2y;
									p2y = tpx;
									
									tpx = p3x;
									
									p3x = -p3y;
									p3y = tpx;
								}
                                if(_module_types[module] == MD_TRIANGLE)
                                {
                                    //TODO
                                    g.drawLine(posX, posY, posX + p2x, posY + p2y);
                                    g.drawLine(posX + p2x, posY + p2y, posX + p3x, posY + p3y);
                                    g.drawLine(posX, posY, posX + p3x, posY + p3y);
                                }
                                else
                                {
									g.fillTriangle(posX, posY, posX + p2x, posY + p2y, posX + p3x, posY + p3y);
                                }
                            }
					}
					break;

				case MD_MARKER:
					// Dont draw markers ... nothing to do.
					break;
				}

				return;
			}
		}

		if (DevConfig.sprite_useOperationRect) {
			if (_operation == OPERATION_COMPUTERECT) {
				if (posX < _rectX1)
					_rectX1 = posX;
				if (posY < _rectY1)
					_rectY1 = posY;
				if (posX + sizeX > _rectX2)
					_rectX2 = posX + sizeX;
				if (posY + sizeY > _rectY2)
					_rectY2 = posY + sizeY;
				return;
			}
		} else {
			if (_operation == OPERATION_COMPUTERECT) {
				if (!(false))
					Utils.DBG_PrintStackTrace(
							false,
							"Error: Trying to use COMPUTE RECT OPERATION but sprite_useOperationRect is FALSE!!!");
				;
			}
		}

		if (DevConfig.sprite_allowPixelArrayGraphics
				&& DevConfig.sprite_useMultipleModuleTypes) {
			// Since when a pixel-array-graphics is set g is null we need to
			// still make sure to RETURN here
			// TODO: Eventually once the various module types are implemented
			// for rendering onto a pixel-array-graphics object
			// the code block above will have to be modified to take that into
			// account and then this check will not be
			// necesary. [MMZ]
			if (_module_types[module] != MD_IMAGE) {
				return;
			}
		}

		if (DevConfig.sprite_useDynamicPaletteBlendingCache) {
			// Is the palette the blending palette?
			byte[] _palBlend_ModuleState = this._palBlend_ModuleState;
			if ((_crt_pal == _palBlend_dest) && (_palBlend_ModuleState != null)) {
				// Is the module we are trying to paint at the correct blend
				// value?
				if (_palBlend_ModuleState[module] != (byte) _palBlend_current) {
					// If not rebuild the module (with GC disabled!!!!)
					boolean saveTmp = s_gcEnabled;
					s_gcEnabled = false;
					BuildCacheImages(_crt_pal, module, module, -1);
					s_gcEnabled = saveTmp;

					_palBlend_ModuleState[module] = (byte) _palBlend_current;
				}
			}
		}

		if (DevConfig.sprite_useDynamicPng) {
			// Switch these back
			if ((flags & FLAG_ROT_90) != 0) {
				int tmp = sizeX;
				sizeX = sizeY;
				sizeY = tmp;
			}
			// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g)) {
				if ((posX + sizeX < g.getClipX())
						|| (posX > g.getClipX() + g.getClipWidth()))
					return;
				if ((posY + sizeY < g.getClipY() || (posY > g.getClipY()
						+ g.getClipHeight())))
					return;
			}

			if ((DevConfig.sprite_useSingleImageForAllModules)
					&& ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {

				if (sizeX <= 0 || sizeY <= 0) {
					return;
				}

				// TODO: Convert to use wrappers?
				// Main purpose would be to support custom graphics but I don't
				// think this is every used with single images. [MMZ] 10-11-2009
				int cx = g.getClipX();
				int cy = g.getClipY();
				int cw = g.getClipWidth();
				int ch = g.getClipHeight();

				int new_cx = posX;
				int new_cy = posY;
				int new_endcx = posX + sizeX;
				int new_endcy = posY + sizeY;

				if (posX < cx)
					new_cx = cx;

				if (posY < cy)
					new_cy = cy;

				if (new_endcx > cx + cw)
					new_endcx = cx + cw;

				if (new_endcy > cy + ch)
					new_endcy = cy + ch;

				if (!DevConfig.sprite_fpsRegion) {
					g.setClip(new_cx, new_cy, new_endcx - new_cx, new_endcy
							- new_cy);
					;
				}

				;

				if ((!DevConfig.sprite_useLoadImageWithoutTransf)
						&& ((DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip))) {
					if (DevConfig.sprite_useModuleXY) {
						Image[][][] _module_image_imageAAA = this._module_image_imageAAA;	
						byte[] _modules_x_byte = this._modules_x_byte;
						byte[] _modules_y_byte = this._modules_y_byte;
						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
								_module_image_imageAAA[_crt_pal][0][0], posX,
								posY, sizeX, sizeY, 0,
								(_modules_x_byte[module] & 0xFF),
								(_modules_y_byte[module] & 0xFF))) {
							g.drawImage(_module_image_imageAAA[_crt_pal][0][0],
									posX - (_modules_x_byte[module] & 0xFF),
									posY - (_modules_y_byte[module] & 0xFF), 0);
						}
					} else if (DevConfig.sprite_useModuleXYShort) {
						Image[][][] _module_image_imageAAA = this._module_image_imageAAA;
						short[] _modules_x_short = this._modules_x_short;
						short[] _modules_y_short = this._modules_y_short;
						
						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
								_module_image_imageAAA[_crt_pal][0][0], posX,
								posY, sizeX, sizeY, 0,
								_modules_x_short[module],
								_modules_y_short[module])) {
							g.drawImage(_module_image_imageAAA[_crt_pal][0][0],
									posX - (_modules_x_short[module]), posY
											- (_modules_y_short[module]), 0);
						}
					}
				} else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf)
						// && ( (zJYLibConfig.sprite_useTransfRot) ||
						// (zJYLibConfig.sprite_useTransfFlip)))
				{
					if (!DevConfig.sprite_fpsRegion) {
						if (DevConfig.sprite_useModuleXY) {
							Image[][] _module_image_imageAA = this._module_image_imageAA;
							byte[] _modules_x_byte = this._modules_x_byte;
							byte[] _modules_y_byte = this._modules_y_byte;
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									(_modules_x_byte[module] & 0xFF),
									(_modules_y_byte[module] & 0xFF))) {
								g.drawImage(
										_module_image_imageAA[_crt_pal][0],
										posX - (_modules_x_byte[module] & 0xFF),
										posY - (_modules_y_byte[module] & 0xFF),
										0);
							}
						} else if (DevConfig.sprite_useModuleXYShort) {
							Image[][] _module_image_imageAA = this._module_image_imageAA;
							short[] _modules_x_short = this._modules_x_short;
							short[] _modules_y_short = this._modules_y_short;
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									_modules_x_short[module],
									_modules_y_short[module])) {
								g.drawImage(_module_image_imageAA[_crt_pal][0],
										posX - (_modules_x_short[module]), posY
												- (_modules_y_short[module]), 0);
							}
						}
					} else {

						if (DevConfig.sprite_useModuleXY) {
							Image[][] _module_image_imageAA = this._module_image_imageAA;
							byte[] _modules_x_byte = this._modules_x_byte;
							byte[] _modules_y_byte = this._modules_y_byte;
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									(_modules_x_byte[module] & 0xFF),
									(_modules_y_byte[module] & 0xFF))) {
								if (DevConfig.sprite_drawRegionFlippedBug) {

									Image image = Image.createImage(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_byte[module] & 0xFF),
											(_modules_y_byte[module] & 0xFF),
											sizeX, // source coord
											sizeY, // source coord
											0); // transform
									g.drawImage(image, posX, // dest coord
											posY, // dest coord
											0);

								} else {
									g.drawRegion(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_byte[module] & 0xFF),
											(_modules_y_byte[module] & 0xFF),
											sizeX, // source coord
											sizeY, // source coord
											0, // transform
											posX, // dest coord
											posY, // dest coord
											0);
								}
							}
						} else if (DevConfig.sprite_useModuleXYShort) {
							Image[][] _module_image_imageAA = this._module_image_imageAA;
							short[] _modules_x_short = this._modules_x_short;
							short[] _modules_y_short = this._modules_y_short;
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									_modules_x_short[module],
									_modules_y_short[module])) {
								if (DevConfig.sprite_drawRegionFlippedBug) {

									Image image = Image.createImage(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_short[module]),
											(_modules_y_short[module]), sizeX, // source
																				// coord
											sizeY, // source coord
											0); // transform
									g.drawImage(image, posX, // dest coord
											posY, // dest coord
											0);

								} else {
									g.drawRegion(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_short[module]),
											(_modules_y_short[module]), sizeX, // source
																				// coord
											sizeY, // source coord
											0, // transform
											posX, // dest coord
											posY, // dest coord
											0); // dest coord
								}
							}
						}

					}
				} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && (
					// (zJYLibConfig.sprite_useTransfRot) ||
					// (zJYLibConfig.sprite_useTransfFlip)))

				if (!DevConfig.sprite_fpsRegion) {
					g.setClip(cx, cy, cw, ch);
					;
				}

			} else {

				if ((!DevConfig.sprite_useLoadImageWithoutTransf)
						&& ((DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip))) {
					Image[][][] _module_image_imageAAA = this._module_image_imageAAA;
					if (!(((_module_image_imageAAA != null)
							&& (_crt_pal < _module_image_imageAAA.length)
							&& (_module_image_imageAAA[_crt_pal] != null)
							&& (module < _module_image_imageAAA[_crt_pal].length)
							&& (_module_image_imageAAA[_crt_pal][module] != null)
							&& (flags < _module_image_imageAAA[_crt_pal][module].length) && (_module_image_imageAAA[_crt_pal][module][flags] != null))))
						Utils.DBG_PrintStackTrace(false, "Not loaded module image: pal = "
								+ _crt_pal + " module = " + module
								+ " flags = " + flags);
					;

					;

					int nTmpFlag;

					if ((DevConfig.sprite_usePrecomputedCRC)
							&& (DevConfig.sprite_useBSpriteFlags)
							&& ((_bs_flags & BS_PNG_CRC) != 0)) {
						nTmpFlag = 0;
					} else {
						nTmpFlag = flags;
					}

					Image img = null;

					if (nTmpFlag < _module_image_imageAAA[_crt_pal][module].length) {
						img = _module_image_imageAAA[_crt_pal][module][nTmpFlag];
					}

					if (img == null) {
						img = BuildPNG8(_crt_pal, false, module, sizeX, sizeY,
								nTmpFlag);
					}

					if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img, posX, posY, sizeX, sizeY, 0, 0, 0)) {
						g.drawImage(img, posX, posY, 0);
					}
				} else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf)
						// && ( (zJYLibConfig.sprite_useTransfRot) ||
						// (zJYLibConfig.sprite_useTransfFlip)))
				{
					Image[][] _module_image_imageAA = this._module_image_imageAA;
					if (!(((_module_image_imageAA != null)
							&& (_crt_pal < _module_image_imageAA.length)
							&& (_module_image_imageAA[_crt_pal] != null) && (module < _module_image_imageAA[_crt_pal].length))))
						Utils.DBG_PrintStackTrace(false, "Not loaded module image: pal = "
								+ _crt_pal + " module = " + module);
					;

					if ((DevConfig.sprite_usePrecomputedCRC)
							&& (DevConfig.sprite_useBSpriteFlags)
							&& ((_bs_flags & BS_PNG_CRC) != 0)) {
						Image img = _module_image_imageAA[_crt_pal][module];

						if (img == null) {
							img = BuildPNG8(_crt_pal, false, module, sizeX,
									sizeY, 0);
						}

						if (!((img != null)))
							Utils.DBG_PrintStackTrace(false, "Not loaded module image");
						;

						;

						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img, posX, posY, sizeX, sizeY, 0, 0,
								0)) {
							g.drawImage(img, posX, posY, 0);
						}
					} else {
						if (DevConfig.sprite_useLoadImageWithoutTransf) {
							int[] midp2_flags = this.midp2_flags;
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][module],
									posX, posY, sizeX, sizeY,
									midp2_flags[flags & 0x07], 0, 0)) {
								if (midp2_flags[flags & 0x07] == 0) {
									g.drawImage(
											_module_image_imageAA[_crt_pal][module],
											posX, posY, 0);
								} else {
									if (DevConfig.sprite_drawRegionFlippedBug) {

										Image image = Image
												.createImage(
														_module_image_imageAA[_crt_pal][module],
														0,
														0,
														sizeX,
														sizeY,
														midp2_flags[flags & 0x07]);
										g.drawImage(image, posX, posY, 0);

									} else {
										g.drawRegion(
												_module_image_imageAA[_crt_pal][module],
												0, 0, sizeX, sizeY,
												midp2_flags[flags & 0x07],
												posX, posY, 0);
									}

								}
							}

						} else {
							;

							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][module],
									posX, posY, sizeX, sizeY, 0, 0, 0)) {
								g.drawImage(
										_module_image_imageAA[_crt_pal][module],
										posX, posY, 0);
							}
						}
					}
				} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && (
					// (zJYLibConfig.sprite_useTransfRot) ||
					// (zJYLibConfig.sprite_useTransfFlip)))

			}
		} else // if (zJYLibConfig.sprite_useDynamicPng)
		{
			if ((sizeX <= 0) || (sizeY <= 0)) {
				return;
			}

			if (!DevConfig.sprite_useSkipFastVisibilityTest) {
				if (GLLib.IsClipValid(g)) {
					int cx = GLLib.GetClipX(g);
					int cy = GLLib.GetClipY(g);
					int cw = GLLib.GetClipWidth(g);
					int ch = GLLib.GetClipHeight(g);

					if (DevConfig.pfx_useSpriteEffectScale
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_SCALE)) {
						int percent = cPFX.PFX_GetParam(
								GLPixEffects.k_EFFECT_SCALE,
								GLPixEffects.k_PARAM_SCALE_PERCENT);
						int sizeSX = ((sizeX * percent) / 100);
						int sizeSY = ((sizeY * percent) / 100);

						// Fast visibility test...
						if ((posX + sizeSX < cx) || (posY + sizeSY < cy)
								|| (posX >= cx + cw) || (posY >= cy + ch)) {
							return;
						}
					} else {
						// Fast visibility test...
						if ((posX + sizeX < cx) || (posY + sizeY < cy)
								|| (posX >= cx + cw) || (posY >= cy + ch)) {
							return;
						}
					}
				}
			}

			if (DevConfig.sprite_useResize && s_resizeOn) {
				posX = zSprite.scaleX(posX);
				posY = zSprite.scaleY(posY);
			}

			int[] img_intA = null;
			Image img_image = null;
			short[] img_shortA = null;

			if (DevConfig.sprite_useSingleImageForAllModules) {
				if ((!DevConfig.sprite_useBSpriteFlags)
						|| ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {
					// Switch these back
					if ((flags & FLAG_ROT_90) != 0) {
						int tmp = sizeX;
						sizeX = sizeY;
						sizeY = tmp;
					}

					if ((!DevConfig.sprite_useBSpriteFlags)
							|| ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {
						if ((DevConfig.sprite_useCacheRGBArrays)
								|| (DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))) {
							int[][][] _module_image_intAAA = this._module_image_intAAA;
							if (_module_image_intAAA == null
									|| _module_image_intAAA[_crt_pal] == null) {
								BuildCacheImages(_crt_pal, 0, 0, 0);
							}

							;

							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_intAAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0, 0, 0)) {
								// Use GLLib's wrapper call
								GLLib.DrawRGB(g,
										_module_image_intAAA[_crt_pal][0], 0,
										sizeX, posX, posY, sizeX, sizeY,
										_alpha, _multiAlpha, 0); // no flip !!!
							}

						} else // if (zJYLibConfig.sprite_useCacheRGBArrays)
						{
							Image[][] _module_image_imageAA = this._module_image_imageAA;
							if ((_module_image_imageAA == null)
									|| (_module_image_imageAA[_crt_pal] == null)) {
								BuildCacheImages(_crt_pal, 0, 0, 0);
							}

							int img_x = 0;
							int img_y = 0;

							if (DevConfig.sprite_useModuleXYShort
									&& ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0))) {
								img_x = _modules_x_short[module];
								img_y = _modules_y_short[module];
							} else if (DevConfig.sprite_useModuleXY
									&& ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0))) {
								img_x = (_modules_x_byte[module] & 0xFF);
								img_y = (_modules_y_byte[module] & 0xFF);
							}

							if (DevConfig.sprite_useCacheFlipXY) {
								int[] midp2_flags= this.midp2_flags;
								int selected = midp2_flags[flags & 0x07];
								if (selected > 2)
									selected = 0;

								img_image = _module_image_imageAA[_crt_pal][selected];

								if (midp2_flags[flags & 0x07] == 2) {
									img_x = img_image.getWidth()
											- (img_x + sizeX);
								} else if (midp2_flags[flags & 0x07] == 1) {
									img_y = img_image.getHeight()
											- (img_y + sizeY);
								}

							} else {
								img_image = _module_image_imageAA[_crt_pal][0];
							}

							if (DevConfig.sprite_useDrawRegionClipping) {
								int cx = g.getClipX();
								int cy = g.getClipY();
								int cw = g.getClipWidth();
								int ch = g.getClipHeight();
								int new_cx = posX;
								int new_cy = posY;
								int new_endcx = posX + sizeX;
								int new_endcy = posY + sizeY;
								if (posX < cx)
									new_cx = cx;
								if (posY < cy)
									new_cy = cy;
								if (new_endcx > cx + cw)
									new_endcx = cx + cw;
								if (new_endcy > cy + ch)
									new_endcy = cy + ch;
								g.setClip(new_cx, new_cy, new_endcx - new_cx,
										new_endcy - new_cy);
								;

								;

								if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY,
										sizeX, sizeY, 0, img_x, img_y)) {
									g.drawImage(img_image, posX - img_x, posY
											- img_y, 0);
								}

								g.setClip(cx, cy, cw, ch);
								;
							} else // if
									// (zJYLibConfig.sprite_useDrawRegionClipping)
							{
								int[]midp2_flags = this.midp2_flags;
								if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY,
										sizeX, sizeY,
										midp2_flags[flags & 0x07], img_x, img_y)) {
									if (DevConfig.sprite_drawRegionFlippedBug) {

										Image image = Image.createImage(
												img_image, img_x, img_y, sizeX,
												sizeY,
												midp2_flags[flags & 0x07]);
										g.drawImage(image, posX, posY, 0);

									} else {

										g.drawRegion(img_image, img_x, img_y,
												sizeX, sizeY,
												midp2_flags[flags & 0x07],
												posX, posY, 0);
									}
								}

							}
						} // if (zJYLibConfig.sprite_useCacheRGBArrays)
					}

				}
			}
			if (DevConfig.sprite_useExternImage) {
				int image_idx = _cur_pal;
				byte[]_modules_img_index = this._modules_img_index;
				if (_modules_img_index != null)
					image_idx = _modules_img_index[module];
				Image[] _main_image = this._main_image;
				if ((_main_image != null) && (_main_image[image_idx] != null)) {
					img_image = _main_image[image_idx];
				}

				if (img_image != null) {
					int img_x = 0;
					int img_y = 0;

					if (DevConfig.sprite_useModuleXY) {
						img_x = (_modules_x_byte[module] & 0xFF);
						img_y = (_modules_y_byte[module] & 0xFF);
					} else if (DevConfig.sprite_useModuleXYShort) {
						img_x = _modules_x_short[module];
						img_y = _modules_y_short[module];
					}
					int []midp2_flags = this.midp2_flags;
					if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY, sizeX, sizeY,
							midp2_flags[flags & 0x07], img_x, img_y)) {
						if (DevConfig.sprite_drawRegionFlippedBug) {

							Image image = Image.createImage(img_image, img_x,
									img_y, sizeX, sizeY,
									midp2_flags[flags & 0x07]);
							g.drawImage(image, posX, posY, 0);

						} else {
							g.drawRegion(img_image, img_x, img_y, noneRotatedSizeX, noneRotatedSizeY,
									midp2_flags[flags & 0x07], posX, posY, 0);
						}
					}

				}

			}
		} // if (! zJYLibConfig.sprite_useDynamicPng)
	}
	//Guijun this function had been abandened. do NOT acclerate it then.
	void PaintModule2(Graphics g, int module, int posX, int posY, int flags) {
		if (DevConfig.sprite_debugTogglePaintModule && s_debugSkipPaintModule) {
			return;
		}

		if (true && _bTraceNow) {
			Utils.Dbg("PaintModule(g_  " + module + ", " + posX + ", " + posY
					+ ", 0x" + Integer.toHexString(flags) + ")");
			;
		}

		if (DevConfig.sprite_useOperationMark) {
			if (_operation == OPERATION_MARK) {
				_modules_usage[module] |= (1 << (flags & 0x07));
				return;
			}
		}

		int sizeX = GetModuleWidth(module);
		int sizeY = GetModuleHeight(module);

		// Only referenced if using Pixel Effects (otherwise should obfuscate
		// away)
		int noneRotatedSizeX = sizeX;
		int noneRotatedSizeY = sizeY;

		if ((flags & FLAG_ROT_90) != 0) {
			int tmp = sizeX;
			sizeX = sizeY;
			sizeY = tmp;
		}

		if (DevConfig.sprite_useMultipleModuleTypes) {
			if (_module_types[module] != MD_IMAGE && g != null) {
				if (DevConfig.sprite_useModuleColorAsByte) {
					g.setColor(_module_colors_byte[module]);
				} else {
					if (DevConfig.pfx_useSpriteEffectBlend
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_GRAYSCALE)) {
						g.setColor(cPFX
								.PFX_GetGrayscaleColor(_module_colors_int[module]));
					} else {
						g.setColor(_module_colors_int[module]);
					}
				}
				// Scale the size of when this PFX is turned on
				if (DevConfig.pfx_useSpriteEffectScale
						&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_SCALE)) {
					int percent = cPFX.PFX_GetParam(
							GLPixEffects.k_EFFECT_SCALE,
							GLPixEffects.k_PARAM_SCALE_PERCENT);

					sizeX = (sizeX * percent) / 100;
					sizeY = (sizeY * percent) / 100;
				}
				// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
				if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP
						&& GLLib.IsClipValid(g)) {
					if ((posX + sizeX < g.getClipX())
							|| (posX > g.getClipX() + g.getClipWidth()))
						return;
					if ((posY + sizeY < g.getClipY() || (posY > g.getClipY()
							+ g.getClipHeight())))
						return;
				}

				switch (_module_types[module]) {
				case MD_FILL_RECT:

					if (DevConfig.sprite_useModuleColorAsByte) {
						GLLib.FillRect(g, posX, posY, sizeX, sizeY);
					} else {
						// OPAQUE RECT: Full-alpha or 0 alpha (due to how Aurora
						// has been working) yield normal fillRect
						if (((_module_colors_int[module] & 0xFF000000) == 0xFF000000)
								|| ((_module_colors_int[module] & 0xFF000000) == 0x00000000)) {
							// BLENDING: Applying alpha effect to solid Rect
							if (DevConfig.pfx_useSpriteEffectBlend
									&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_BLEND)) {
								int alpha = cPFX.PFX_GetParam(
										GLPixEffects.k_EFFECT_BLEND,
										GLPixEffects.k_PARAM_BLEND_AMOUNT);

								GLLib.AlphaRect_SetColor((_module_colors_int[module] & 0x00FFFFFF)
										| (alpha << 24));
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
							// NORMAL Solid Rect
							else {
								GLLib.FillRect(g, posX, posY, sizeX, sizeY);
							}
						}
						// ALPHA RECT: Rect in Aurora has Alpha Color
						else {
							// GRAYSCALE effect
							if (DevConfig.pfx_useSpriteEffectBlend
									&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_GRAYSCALE)) {
								GLLib.AlphaRect_SetColor(cPFX
										.PFX_GetGrayscaleColor(_module_colors_int[module]));
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
							// NORMAL ALPHA rect
							else {
								GLLib.AlphaRect_SetColor(_module_colors_int[module]);
								GLLib.AlphaRect_Draw(g, posX, posY, sizeX,
										sizeY);
							}
						}
					}
					break;

				case MD_RECT:

					if (!DevConfig.sprite_useOriginalDrawRect) {
						sizeX--;
						sizeY--;
					}

					if (DevConfig.pfx_useSpriteEffectBlend
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_BLEND)) {
						int alpha = cPFX.PFX_GetParam(
								GLPixEffects.k_EFFECT_BLEND,
								GLPixEffects.k_PARAM_BLEND_AMOUNT);
						GLLib.DrawAlphaRect(g, posX, posY, sizeX, sizeY,
								(_module_colors_int[module] & 0x00FFFFFF)
										| (alpha << 24));
					} else {
						GLLib.DrawRect(posX, posY, sizeX, sizeY);
					}

					break;

				case MD_ARC:
				case MD_FILL_ARC: {
					// Find angles
					int nOffset = GetModuleExtraInfoOffset(module);

					if (nOffset != -1) {
						int sa = _modules_extra_info[nOffset + 0];
						int a = _modules_extra_info[nOffset + 1];

						if ((flags & FLAG_FLIP_X) != 0) {
							sa = 90 - sa;
						}
						if ((flags & FLAG_FLIP_Y) != 0) {
							sa = -sa;
							a = -a;
						}
						if ((flags & FLAG_ROT_90) != 0) {
							sa = sa - 90;
						}

						if (_module_types[module] == MD_ARC) {
							g.drawArc(posX, posY, sizeX, sizeY, sa, a);
						} else {
							g.fillArc(posX, posY, sizeX, sizeY, sa, a);
						}
					}
				}
					break;

				// TODO: Handle transformation flags correctly (flipX/Y and
				// Rot90)
				case MD_TRIANGLE:
				case MD_FILL_TRIANGLE:
					/** JYlib中使用这两个类型来做自己的数据格式定义 */
					if (true) {
						if (_player != null && _player.canTriggerListener()) {
							int color;
							if (DevConfig.sprite_useModuleColorAsByte) {
								color = _module_colors_byte[module];
							} else {
								color = _module_colors_int[module];
							}

							int nOffset = GetModuleExtraInfoOffset(module);

							int[] params = new int[5];
							params[0] = color;
							params[1] = _modules_extra_info[nOffset + 0];
							params[2] = _modules_extra_info[nOffset + 1];
							params[3] = _modules_extra_info[nOffset + 2];
							params[4] = _modules_extra_info[nOffset + 3];
							_player.triggerListener(params);
						}
					} else {
						// Find tria
						int nOffset = GetModuleExtraInfoOffset(module);

						if (nOffset != -1) {
							int p2x = _modules_extra_info[nOffset + 0];
							int p2y = _modules_extra_info[nOffset + 1];
							int p3x = _modules_extra_info[nOffset + 2];
							int p3y = _modules_extra_info[nOffset + 3];

							if (_module_types[module] == MD_TRIANGLE) {
								// TODO
								g.drawLine(posX, posY, posX + p2x, posY + p2y);
								g.drawLine(posX + p2x, posY + p2y, posX + p3x,
										posY + p3y);
								g.drawLine(posX, posY, posX + p3x, posY + p3y);
							} else {

								g.fillTriangle(posX, posY, posX + p2x, posY
										+ p2y, posX + p3x, posY + p3y);

							}
						}
					}
					break;

				case MD_MARKER:
					// Dont draw markers ... nothing to do.
					break;
				}

				return;
			}
		}

		if (DevConfig.sprite_useOperationRect) {
			if (_operation == OPERATION_COMPUTERECT) {
				if (posX < _rectX1)
					_rectX1 = posX;
				if (posY < _rectY1)
					_rectY1 = posY;
				if (posX + sizeX > _rectX2)
					_rectX2 = posX + sizeX;
				if (posY + sizeY > _rectY2)
					_rectY2 = posY + sizeY;
				return;
			}
		} else {
			if (_operation == OPERATION_COMPUTERECT) {
				if (!(false))
					Utils.DBG_PrintStackTrace(
							false,
							"Error: Trying to use COMPUTE RECT OPERATION but sprite_useOperationRect is FALSE!!!");
				;
			}
		}

		if (DevConfig.sprite_allowPixelArrayGraphics
				&& DevConfig.sprite_useMultipleModuleTypes) {
			// Since when a pixel-array-graphics is set g is null we need to
			// still make sure to RETURN here
			// TODO: Eventually once the various module types are implemented
			// for rendering onto a pixel-array-graphics object
			// the code block above will have to be modified to take that into
			// account and then this check will not be
			// necesary. [MMZ]
			if (_module_types[module] != MD_IMAGE) {
				return;
			}
		}

		if (DevConfig.sprite_useDynamicPaletteBlendingCache) {
			// Is the palette the blending palette?
			if ((_crt_pal == _palBlend_dest) && (_palBlend_ModuleState != null)) {
				// Is the module we are trying to paint at the correct blend
				// value?
				if (_palBlend_ModuleState[module] != (byte) _palBlend_current) {
					// If not rebuild the module (with GC disabled!!!!)
					boolean saveTmp = s_gcEnabled;
					s_gcEnabled = false;
					BuildCacheImages(_crt_pal, module, module, -1);
					s_gcEnabled = saveTmp;

					_palBlend_ModuleState[module] = (byte) _palBlend_current;
				}
			}
		}

		if (DevConfig.sprite_useDynamicPng) {
			// Switch these back
			if ((flags & FLAG_ROT_90) != 0) {
				int tmp = sizeX;
				sizeX = sizeY;
				sizeY = tmp;
			}
			// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g)) {
				if ((posX + sizeX < g.getClipX())
						|| (posX > g.getClipX() + g.getClipWidth()))
					return;
				if ((posY + sizeY < g.getClipY() || (posY > g.getClipY()
						+ g.getClipHeight())))
					return;
			}

			if ((DevConfig.sprite_useSingleImageForAllModules)
					&& ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {

				if (sizeX <= 0 || sizeY <= 0) {
					return;
				}

				// TODO: Convert to use wrappers?
				// Main purpose would be to support custom graphics but I don't
				// think this is every used with single images. [MMZ] 10-11-2009
				int cx = g.getClipX();
				int cy = g.getClipY();
				int cw = g.getClipWidth();
				int ch = g.getClipHeight();

				int new_cx = posX;
				int new_cy = posY;
				int new_endcx = posX + sizeX;
				int new_endcy = posY + sizeY;

				if (posX < cx)
					new_cx = cx;

				if (posY < cy)
					new_cy = cy;

				if (new_endcx > cx + cw)
					new_endcx = cx + cw;

				if (new_endcy > cy + ch)
					new_endcy = cy + ch;

				if (!DevConfig.sprite_fpsRegion) {
					g.setClip(new_cx, new_cy, new_endcx - new_cx, new_endcy
							- new_cy);
					;
				}

				;

				if ((!DevConfig.sprite_useLoadImageWithoutTransf)
						&& ((DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip))) {
					if (DevConfig.sprite_useModuleXY) {
						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
								_module_image_imageAAA[_crt_pal][0][0], posX,
								posY, sizeX, sizeY, 0,
								(_modules_x_byte[module] & 0xFF),
								(_modules_y_byte[module] & 0xFF))) {
							g.drawImage(_module_image_imageAAA[_crt_pal][0][0],
									posX - (_modules_x_byte[module] & 0xFF),
									posY - (_modules_y_byte[module] & 0xFF), 0);
						}
					} else if (DevConfig.sprite_useModuleXYShort) {
						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
								_module_image_imageAAA[_crt_pal][0][0], posX,
								posY, sizeX, sizeY, 0,
								_modules_x_short[module],
								_modules_y_short[module])) {
							g.drawImage(_module_image_imageAAA[_crt_pal][0][0],
									posX - (_modules_x_short[module]), posY
											- (_modules_y_short[module]), 0);
						}
					}
				} else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf)
						// && ( (zJYLibConfig.sprite_useTransfRot) ||
						// (zJYLibConfig.sprite_useTransfFlip)))
				{
					if (!DevConfig.sprite_fpsRegion) {
						if (DevConfig.sprite_useModuleXY) {
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									(_modules_x_byte[module] & 0xFF),
									(_modules_y_byte[module] & 0xFF))) {
								g.drawImage(
										_module_image_imageAA[_crt_pal][0],
										posX - (_modules_x_byte[module] & 0xFF),
										posY - (_modules_y_byte[module] & 0xFF),
										0);
							}
						} else if (DevConfig.sprite_useModuleXYShort) {
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									_modules_x_short[module],
									_modules_y_short[module])) {
								g.drawImage(_module_image_imageAA[_crt_pal][0],
										posX - (_modules_x_short[module]), posY
												- (_modules_y_short[module]), 0);
							}
						}
					} else {

						if (DevConfig.sprite_useModuleXY) {
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									(_modules_x_byte[module] & 0xFF),
									(_modules_y_byte[module] & 0xFF))) {
								if (DevConfig.sprite_drawRegionFlippedBug) {

									Image image = Image.createImage(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_byte[module] & 0xFF),
											(_modules_y_byte[module] & 0xFF),
											sizeX, // source coord
											sizeY, // source coord
											0); // transform
									g.drawImage(image, posX, // dest coord
											posY, // dest coord
											0);

								} else {
									g.drawRegion(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_byte[module] & 0xFF),
											(_modules_y_byte[module] & 0xFF),
											sizeX, // source coord
											sizeY, // source coord
											0, // transform
											posX, // dest coord
											posY, // dest coord
											0);
								}
							}
						} else if (DevConfig.sprite_useModuleXYShort) {
							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0,
									_modules_x_short[module],
									_modules_y_short[module])) {
								if (DevConfig.sprite_drawRegionFlippedBug) {

									Image image = Image.createImage(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_short[module]),
											(_modules_y_short[module]), sizeX, // source
																				// coord
											sizeY, // source coord
											0); // transform
									g.drawImage(image, posX, // dest coord
											posY, // dest coord
											0);

								} else {
									g.drawRegion(
											_module_image_imageAA[_crt_pal][0],
											(_modules_x_short[module]),
											(_modules_y_short[module]), sizeX, // source
																				// coord
											sizeY, // source coord
											0, // transform
											posX, // dest coord
											posY, // dest coord
											0); // dest coord
								}
							}
						}

					}
				} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && (
					// (zJYLibConfig.sprite_useTransfRot) ||
					// (zJYLibConfig.sprite_useTransfFlip)))

				if (!DevConfig.sprite_fpsRegion) {
					g.setClip(cx, cy, cw, ch);
					;
				}

			} else {

				if ((!DevConfig.sprite_useLoadImageWithoutTransf)
						&& ((DevConfig.sprite_useTransfRot) || (DevConfig.sprite_useTransfFlip))) {
					if (!(((_module_image_imageAAA != null)
							&& (_crt_pal < _module_image_imageAAA.length)
							&& (_module_image_imageAAA[_crt_pal] != null)
							&& (module < _module_image_imageAAA[_crt_pal].length)
							&& (_module_image_imageAAA[_crt_pal][module] != null)
							&& (flags < _module_image_imageAAA[_crt_pal][module].length) && (_module_image_imageAAA[_crt_pal][module][flags] != null))))
						Utils.DBG_PrintStackTrace(false, "Not loaded module image: pal = "
								+ _crt_pal + " module = " + module
								+ " flags = " + flags);
					;

					;

					int nTmpFlag;

					if ((DevConfig.sprite_usePrecomputedCRC)
							&& (DevConfig.sprite_useBSpriteFlags)
							&& ((_bs_flags & BS_PNG_CRC) != 0)) {
						nTmpFlag = 0;
					} else {
						nTmpFlag = flags;
					}

					Image img = null;

					if (nTmpFlag < _module_image_imageAAA[_crt_pal][module].length) {
						img = _module_image_imageAAA[_crt_pal][module][nTmpFlag];
					}

					if (img == null) {
						img = BuildPNG8(_crt_pal, false, module, sizeX, sizeY,
								nTmpFlag);
					}

					if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img, posX, posY, sizeX, sizeY, 0, 0, 0)) {
						g.drawImage(img, posX, posY, 0);
					}
				} else // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf)
						// && ( (zJYLibConfig.sprite_useTransfRot) ||
						// (zJYLibConfig.sprite_useTransfFlip)))
				{
					if (!(((_module_image_imageAA != null)
							&& (_crt_pal < _module_image_imageAA.length)
							&& (_module_image_imageAA[_crt_pal] != null) && (module < _module_image_imageAA[_crt_pal].length))))
						Utils.DBG_PrintStackTrace(false, "Not loaded module image: pal = "
								+ _crt_pal + " module = " + module);
					;

					if ((DevConfig.sprite_usePrecomputedCRC)
							&& (DevConfig.sprite_useBSpriteFlags)
							&& ((_bs_flags & BS_PNG_CRC) != 0)) {
						Image img = _module_image_imageAA[_crt_pal][module];

						if (img == null) {
							img = BuildPNG8(_crt_pal, false, module, sizeX,
									sizeY, 0);
						}

						if (!((img != null)))
							Utils.DBG_PrintStackTrace(false, "Not loaded module image");
						;

						;

						if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img, posX, posY, sizeX, sizeY, 0, 0,
								0)) {
							g.drawImage(img, posX, posY, 0);
						}
					} else {
						if (DevConfig.sprite_useLoadImageWithoutTransf) {

							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][module],
									posX, posY, sizeX, sizeY,
									midp2_flags[flags & 0x07], 0, 0)) {
								if (midp2_flags[flags & 0x07] == 0) {
									g.drawImage(
											_module_image_imageAA[_crt_pal][module],
											posX, posY, 0);
								} else {
									if (DevConfig.sprite_drawRegionFlippedBug) {

										Image image = Image
												.createImage(
														_module_image_imageAA[_crt_pal][module],
														0,
														0,
														sizeX,
														sizeY,
														midp2_flags[flags & 0x07]);
										g.drawImage(image, posX, posY, 0);

									} else {
										g.drawRegion(
												_module_image_imageAA[_crt_pal][module],
												0, 0, sizeX, sizeY,
												midp2_flags[flags & 0x07],
												posX, posY, 0);
									}

								}
							}

						} else {
							;

							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_imageAA[_crt_pal][module],
									posX, posY, sizeX, sizeY, 0, 0, 0)) {
								g.drawImage(
										_module_image_imageAA[_crt_pal][module],
										posX, posY, 0);
							}
						}
					}
				} // if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && (
					// (zJYLibConfig.sprite_useTransfRot) ||
					// (zJYLibConfig.sprite_useTransfFlip)))

			}
		} else // if (zJYLibConfig.sprite_useDynamicPng)
		{
			if ((sizeX <= 0) || (sizeY <= 0)) {
				return;
			}

			if (!DevConfig.sprite_useSkipFastVisibilityTest) {
				if (GLLib.IsClipValid(g)) {
					int cx = GLLib.GetClipX(g);
					int cy = GLLib.GetClipY(g);
					int cw = GLLib.GetClipWidth(g);
					int ch = GLLib.GetClipHeight(g);

					if (DevConfig.pfx_useSpriteEffectScale
							&& DevConfig.pfx_enabled && cPFX.PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_SCALE)) {
						int percent = cPFX.PFX_GetParam(
								GLPixEffects.k_EFFECT_SCALE,
								GLPixEffects.k_PARAM_SCALE_PERCENT);
						int sizeSX = ((sizeX * percent) / 100);
						int sizeSY = ((sizeY * percent) / 100);

						// Fast visibility test...
						if ((posX + sizeSX < cx) || (posY + sizeSY < cy)
								|| (posX >= cx + cw) || (posY >= cy + ch)) {
							return;
						}
					} else {
						// Fast visibility test...
						if ((posX + sizeX < cx) || (posY + sizeY < cy)
								|| (posX >= cx + cw) || (posY >= cy + ch)) {
							return;
						}
					}
				}
			}

			if (DevConfig.sprite_useResize && s_resizeOn) {
				posX = zSprite.scaleX(posX);
				posY = zSprite.scaleY(posY);
			}

			int[] img_intA = null;
			Image img_image = null;
			short[] img_shortA = null;

			if (DevConfig.sprite_useSingleImageForAllModules) {
				if ((!DevConfig.sprite_useBSpriteFlags)
						|| ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {
					// Switch these back
					if ((flags & FLAG_ROT_90) != 0) {
						int tmp = sizeX;
						sizeX = sizeY;
						sizeY = tmp;
					}

					if ((!DevConfig.sprite_useBSpriteFlags)
							|| ((_bs_flags & BS_SINGLE_IMAGE) != 0)) {
						if ((DevConfig.sprite_useCacheRGBArrays)
								|| (DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))) {
							if (_module_image_intAAA == null
									|| _module_image_intAAA[_crt_pal] == null) {
								BuildCacheImages(_crt_pal, 0, 0, 0);
							}

							;

							if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(
									_module_image_intAAA[_crt_pal][0], posX,
									posY, sizeX, sizeY, 0, 0, 0)) {
								// Use GLLib's wrapper call
								GLLib.DrawRGB(g,
										_module_image_intAAA[_crt_pal][0], 0,
										sizeX, posX, posY, sizeX, sizeY,
										_alpha, _multiAlpha, 0); // no flip !!!
							}

						} else // if (zJYLibConfig.sprite_useCacheRGBArrays)
						{
							if ((_module_image_imageAA == null)
									|| (_module_image_imageAA[_crt_pal] == null)) {
								BuildCacheImages(_crt_pal, 0, 0, 0);
							}

							int img_x = 0;
							int img_y = 0;

							if (DevConfig.sprite_useModuleXYShort
									&& ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY_SHORT) != 0))) {
								img_x = _modules_x_short[module];
								img_y = _modules_y_short[module];
							} else if (DevConfig.sprite_useModuleXY
									&& ((!DevConfig.sprite_useBSpriteFlags) || ((_bs_flags & BS_MODULES_XY) != 0))) {
								img_x = (_modules_x_byte[module] & 0xFF);
								img_y = (_modules_y_byte[module] & 0xFF);
							}

							if (DevConfig.sprite_useCacheFlipXY) {

								int selected = midp2_flags[flags & 0x07];
								if (selected > 2)
									selected = 0;

								img_image = _module_image_imageAA[_crt_pal][selected];

								if (midp2_flags[flags & 0x07] == 2) {
									img_x = img_image.getWidth()
											- (img_x + sizeX);
								} else if (midp2_flags[flags & 0x07] == 1) {
									img_y = img_image.getHeight()
											- (img_y + sizeY);
								}

							} else {
								img_image = _module_image_imageAA[_crt_pal][0];
							}

							if (DevConfig.sprite_useDrawRegionClipping) {
								int cx = g.getClipX();
								int cy = g.getClipY();
								int cw = g.getClipWidth();
								int ch = g.getClipHeight();
								int new_cx = posX;
								int new_cy = posY;
								int new_endcx = posX + sizeX;
								int new_endcy = posY + sizeY;
								if (posX < cx)
									new_cx = cx;
								if (posY < cy)
									new_cy = cy;
								if (new_endcx > cx + cw)
									new_endcx = cx + cw;
								if (new_endcy > cy + ch)
									new_endcy = cy + ch;
								g.setClip(new_cx, new_cy, new_endcx - new_cx,
										new_endcy - new_cy);
								;

								;

								if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY,
										sizeX, sizeY, 0, img_x, img_y)) {
									g.drawImage(img_image, posX - img_x, posY
											- img_y, 0);
								}

								g.setClip(cx, cy, cw, ch);
								;
							} else // if
									// (zJYLibConfig.sprite_useDrawRegionClipping)
							{

								if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY,
										sizeX, sizeY,
										midp2_flags[flags & 0x07], img_x, img_y)) {
									if (DevConfig.sprite_drawRegionFlippedBug) {

										Image image = Image.createImage(
												img_image, img_x, img_y, sizeX,
												sizeY,
												midp2_flags[flags & 0x07]);
										g.drawImage(image, posX, posY, 0);

									} else {

										g.drawRegion(img_image, img_x, img_y,
												sizeX, sizeY,
												midp2_flags[flags & 0x07],
												posX, posY, 0);
									}
								}

							}
						} // if (zJYLibConfig.sprite_useCacheRGBArrays)
					}

				}
			}
			if (DevConfig.sprite_useExternImage) {
				int image_idx = _cur_pal;
				if (_modules_img_index != null)
					image_idx = _modules_img_index[module];

				if ((_main_image != null) && (_main_image[image_idx] != null)) {
					img_image = _main_image[image_idx];
				}

				if (img_image != null) {
					int img_x = 0;
					int img_y = 0;

					if (DevConfig.sprite_useModuleXY) {
						img_x = (_modules_x_byte[module] & 0xFF);
						img_y = (_modules_y_byte[module] & 0xFF);
					} else if (DevConfig.sprite_useModuleXYShort) {
						img_x = _modules_x_short[module];
						img_y = _modules_y_short[module];
					}

					if ((!DevConfig.sprite_useOperationRecord) || CheckOperation(img_image, posX, posY, sizeX, sizeY,
							midp2_flags[flags & 0x07], img_x, img_y)) {
						if (DevConfig.sprite_drawRegionFlippedBug) {

							Image image = Image.createImage(img_image, img_x,
									img_y, sizeX, sizeY,
									midp2_flags[flags & 0x07]);
							g.drawImage(image, posX, posY, 0);

						} else {
							g.drawRegion(img_image, img_x, img_y, sizeX, sizeY,
									midp2_flags[flags & 0x07], posX, posY, 0);
						}
					}

				}

			}
		} // if (! zJYLibConfig.sprite_useDynamicPng)
	}


//--------------------------------------------------------------------------------------------------------------------


// ASprite_PNG.jpp
//--------------------------------------------------------------------------------------------------------------------

	public static final int BLOCK_INFO_SIZE = 11;	// only if (zJYLibConfig.sprite_useDynamicPng)
	public static final int PNG_INFO_SIZE = 57;		// only if (zJYLibConfig.sprite_useDynamicPng)
	public static final int HEADER_LEVEL0_MAX_WBITS = 30938;	// only if (zJYLibConfig.sprite_useDynamicPng)

	public static final int BASE = 65521;	// only if (zJYLibConfig.sprite_useDynamicPng)
	public static final int NMAX = 5552;	// only if (zJYLibConfig.sprite_useDynamicPng)

	protected static final byte MAGIC[]		= { -119, 80, 78, 71, 13, 10, 26, 10 };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte IHDR[]		= { 'I', 'H', 'D', 'R' };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte PLTE[]		= { 'P', 'L', 'T', 'E' };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte tRNS[]		= { 't', 'R', 'N', 'S' };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte IDAT[]		= { 'I', 'D', 'A', 'T' };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte IEND[]		= { 'I', 'E', 'N', 'D' };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte INFO32[]	= { 8, 6, 0, 0, 0 };	// only if (zJYLibConfig.sprite_useDynamicPng)
	protected static final byte INFO8[]		= { 8, 3, 0, 0, 0 };	// only if (zJYLibConfig.sprite_useDynamicPng)

	protected static final byte[] MAGIC_IEND = {0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4e, 0x44, (byte)0xae, 0x42, 0x60, (byte)0x82};	// only if (zJYLibConfig.sprite_useDynamicPng)
	static final byte[] MAGIC_IDAT_h = { (byte)0x78, (byte)0x9C, (byte)0x01, };	// only if (zJYLibConfig.sprite_useDynamicPng)

	static byte[]	_buffer_index;	// only if (zJYLibConfig.sprite_useDynamicPng)
	static byte[]	_png_index;	// only if (zJYLibConfig.sprite_useDynamicPng)
	static byte[]	_png_result;	// only if (zJYLibConfig.sprite_useDynamicPng)
	static int		_png_size;	// only if (zJYLibConfig.sprite_useDynamicPng)
	static int		_png_start_crc;	// only if (zJYLibConfig.sprite_useDynamicPng)

	static int		mod;	// only if (zJYLibConfig.sprite_useDynamicPng)

	public static       int crcTable[];	// only if (zJYLibConfig.sprite_useDynamicPng)
	public static final int CRC32_POLYNOMIAL = 0xEDB88320;	// only if (zJYLibConfig.sprite_useDynamicPng)

	static int currentChunkType;	// only if (zJYLibConfig.sprite_usePrecomputedCRC) && (zJYLibConfig.sprite_useDynamicPng)

    static
    {
        InitCrcTable();
    }

	private static void InitCrcTable()
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			_png_result = new byte[DevConfig.PNG_BUFFER_SIZE];

			for (int i = 0; i < 256; i++)
			{
				int crc = i;

				for (int j = 8; j > 0; j--)
					if ( (crc & 1) == 1)
						crc = (crc >>> 1) ^ CRC32_POLYNOMIAL;
					else
						crc >>>= 1;

				crcTable[i] = crc;
			}
		}
	};

	/**
	 * base for computing Adler32
	 */
	private static void PutArray(byte[] array)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			System.arraycopy(array, 0, _png_result, _png_size, array.length);
			_png_size+=array.length;
		}
	}

	private static void PutInt(int n)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			_png_result[_png_size++] =(byte)((n >> 24) & 0xFF);
			_png_result[_png_size++] =(byte)((n >> 16) & 0xFF);
			_png_result[_png_size++] =(byte)((n >>  8) & 0xFF);
			_png_result[_png_size++] =(byte)(n & 0xFF);
		}
	}

	private void BeginChunk(byte[] name, int len)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			PutInt(len);
			_png_start_crc = _png_size;
			PutArray(name);
		}
	}

	private void EndChunk()
	{
		if (DevConfig.sprite_useDynamicPng)
		{

			if (DevConfig.sprite_usePrecomputedCRC)
			{
				if ((DevConfig.sprite_useBSpriteFlags) && ((_bs_flags & BS_PNG_CRC) == 0))
				{
					PutInt(Crc32(_png_result, _png_start_crc, _png_size - _png_start_crc, 0));
				}
				else
				{
					// CRC...
					switch(currentChunkType)
					{
					case 0:
						PutInt(_PNG_packed_IHDR_CRC[mod]);
					break;
					case 1:
						PutInt(_PNG_packed_PLTE_CRC[_cur_pal]);
					break;
					case 2:
						PutInt(_PNG_packed_tRNS_CRC[_cur_pal]);
					break;
					case 3:
						PutInt(_PNG_packed_IDAT_CRC[mod]);
					break;
					}
				}
				currentChunkType++;
			}
			else // if (zJYLibConfig.sprite_usePrecomputedCRC)
			{
				PutInt(Crc32(_png_result, _png_start_crc, _png_size - _png_start_crc, 0));
			} // if (! zJYLibConfig.sprite_usePrecomputedCRC)
		}
	}

	int _cur_pal;

	private Image BuildPNG8(int pal, boolean bPalInited, int module, int width, int height, int flags)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			mod = module;
			return BuildPNG8(pal, bPalInited, DecodeImage_byte(module), width, height, flags);
		}
		return null;
	}

	private Image BuildPNG8(int pal, boolean bPalInited, byte[] data, int width, int height, int flags)
	{

		if (DevConfig.sprite_useDynamicPng)
		{
			int i, j, offset;
			int data_size;
			int start_block;
			int pixel;
			long adler = 1L;

			short[] _pal_short_pal = null;
			int[] _pal_int_pal = null;
			if (DevConfig.sprite_useNokiaUI)
			{
				_pal_short_pal = _pal_short[pal];
			}
			else
			{
				 _pal_int_pal = _pal_int[pal];
			}
			
			
			if (DevConfig.sprite_usePrecomputedCRC)
			{
				_cur_pal = pal;
				currentChunkType = 0;
			}

			int orig_png_size = _png_size;

			if (width == 0 || height == 0)
				return null;

			if ((DevConfig.sprite_useTransfRot) && ((flags & FLAG_ROT_90) != 0))
			{
				i      = width;
				width  = height;
				height = i;
			}

			_png_size = 0;

			PutArray( MAGIC);

			BeginChunk( IHDR, 0xD );
			PutInt( width );
			PutInt( height );
			PutArray( INFO8 );
			EndChunk();

			if (bPalInited)
			{
				// skip over PLTE and tRNS chunks
//				_png_size += 12 + _pal[pal].length * 3;
//				if (_alpha)
//					_png_size += 12 + _pal[pal].length;
				if (DevConfig.sprite_useNokiaUI)
				{
					_png_size += 12 + _pal_short_pal.length * 3;
					if( _alpha )
						_png_size += 12 + _pal_short_pal.length;
				}
				else
				{
					_png_size += 12 + _pal_int_pal.length * 3;
					if (_alpha)
						_png_size += 12 + _pal_int_pal.length;
				}

				if (DevConfig.sprite_usePrecomputedCRC)
				{
					currentChunkType++;
					currentChunkType++;
				}
			}
			else
			{
				int ssize;
				if (DevConfig.sprite_useNokiaUI)
				{
					ssize = _pal_short_pal.length;
				}
				else
				{
					ssize = _pal_int_pal.length;
				}

				BeginChunk(PLTE, ssize * 3);

				for (i = 0; i < ssize; ++i)
				{
//					pixel = _pal[pal][i];
					if (DevConfig.sprite_useNokiaUI)
					{
						pixel = _pal_short_pal[i];
					}
					else
					{
						pixel = _pal_int_pal[i];
					}
					_png_result[ _png_size++ ] = (byte) ( (pixel & 0x00FF0000) >>> 16	); // R
					_png_result[ _png_size++ ] = (byte) ( (pixel & 0x0000FF00) >>> 8	); // G
					_png_result[ _png_size++ ] = (byte) ( (pixel & 0x000000FF)		    ); // B
				}

				EndChunk();

				// rax - don't put a tRNS chunk if the image doesn't have transparency - bugfix &398116
				// (happens only on E310 for now...)
				if (_alpha)
				{
					// magic tRNS chunk
					BeginChunk(tRNS, ssize);
					for(i = 0; i < ssize; ++i)
					{
//						_png_result[_png_size++] = (byte)((_pal[pal][i] & 0xFF000000) >>> 24); // A
						if (DevConfig.sprite_useNokiaUI)
						{
							_png_result[_png_size++] = (byte)((_pal_short_pal[i] & 0xFF000000) >>> 24); // A
						}
						else
						{
							_png_result[_png_size++] = (byte)((_pal_int_pal[i] & 0xFF000000) >>> 24); // A
						}
					}
					EndChunk();
					// if ( (_pal[pal][0] & 0xf000) == 0){
					//   //DBG("has transparency");
					// PutArray(MAGIC_tRNS);
					//}
				}
			}

			data_size = width * height + height;
			BeginChunk(IDAT, data_size + BLOCK_INFO_SIZE);

			System.arraycopy(MAGIC_IDAT_h, 0, _png_result, _png_size, 3);
			_png_size += 3;


			// 4 bytes for stream size and stream size negated...
			_png_result[ _png_size++ ] = (byte)((( data_size) & 0x00FF));
			_png_result[ _png_size++ ] = (byte)((( data_size) & 0xFF00) >> 8);
			_png_result[ _png_size++ ] = (byte)(((~data_size) & 0x00FF));
			_png_result[ _png_size++ ] = (byte)(((~data_size) & 0xFF00) >> 8);

			start_block = _png_size;

			//Check Sizes
			if(true)
			{
				int nSize = height * width;

				if(nSize >= data.length)
				{
					Utils.Dbg("ERROR		: BuildPNG8 data array too small look at, TMP_BUFFER_SIZE constant");;
				}

				if((_png_size + nSize) >= _png_result.length)
				{
					Utils.Dbg("ERROR		: BuildPNG8 _png_result array too small look at, PNG_BUFFER_SIZE constant");;
				}
			}

			switch (flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90))
			{
			case 0:
			{
				for (j = 0; j < height; j++)
				{
					_png_result[_png_size++] = (byte) 0; // filter
					//arraycopy(Object src, int src_position, Object dst, int dst_position, int length)
					//(_png_index + j * width) ==>(_png_result+_png_size)
					System.arraycopy(data, j * width, _png_result, _png_size, width);
					_png_size += width;
				}
				break;
			}

			case FLAG_FLIP_X:
			{
				if (DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						offset = (j + 1) * width;
						_png_result[_png_size++] = (byte) 0; // filter

						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[--offset];
					}
				}
				break;
			}
			case FLAG_FLIP_Y:
			{
				if (DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						_png_result[_png_size++] = (byte) 0; // filter
						System.arraycopy( data, (height - j - 1) * width, _png_result, _png_size, width);
						_png_size += width;
					}
				}
				break;
			}
			case (FLAG_FLIP_X | FLAG_FLIP_Y):
			{
				if (DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						offset = (height - j) * width;
						_png_result[_png_size++] = (byte) 0; // filter
						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[--offset];
					}
				}
				break;
			}

			case FLAG_ROT_90:
			{
				if (DevConfig.sprite_useTransfRot)
				{
					for (j = 0; j < height; j++)
					{
						_png_result[_png_size++] = (byte) 0; // filter

						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[(width - 1 - i) * height + j];
					}
				}
				break;
			}
			case (FLAG_FLIP_X | FLAG_ROT_90):
			{
				if (DevConfig.sprite_useTransfRot && DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						_png_result[_png_size++] = (byte) 0; // filter

						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[(width - 1 - i) * height + (height - 1 - j)];
					}
				}
				break;
			}
			case (FLAG_FLIP_Y | FLAG_ROT_90):
			{
				if (DevConfig.sprite_useTransfRot && DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						_png_result[_png_size++] = (byte) 0; // filter

						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[i * height + j];
					}
				}
				break;
			}
			case (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90):
			{
				if (DevConfig.sprite_useTransfRot && DevConfig.sprite_useTransfFlip)
				{
					for (j = 0; j < height; j++)
					{
						_png_result[_png_size++] = (byte) 0; // filter

						for (i = 0; i < width; i++)
							_png_result[_png_size++] = data[i * height + (height - 1 - j)];
					}
				}
				break;
			}
			} // switch (flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90))

			if (DevConfig.sprite_usePrecomputedCRC)
			{
				if (	(DevConfig.sprite_useBSpriteFlags)
					&&	((_bs_flags & BS_PNG_CRC) == 0)
					)
				{
					adler = Adler32(adler, _png_result, start_block, data_size);
					PutInt( (int) adler);
				}
				else
				{
					// ADLER (still in the IDAT data section)
					PutInt(_PNG_packed_IDAT_ADLER[mod]);
				}
			}
			else // if (zJYLibConfig.sprite_usePrecomputedCRC)
			{
				adler = Adler32(adler, _png_result, start_block, data_size);
				PutInt( (int) adler);
			} // if (! zJYLibConfig.sprite_usePrecomputedCRC)

			EndChunk();

			PutArray(MAGIC_IEND); // IEND is always the same

			return GLLib.CreateImage(_png_result, 0, _png_size);
		}
		else
			return null;
	}


	//------------------------------------------------------------------------------
	/// Crc32
	/// &param buffer the bufer for wich the crc is computed
	/// &param start the index inthe buffer for wich the crc is computed
	/// &param count the length to compute crc
	/// &param crc ???to be documented???
	/// &return ???to be documented???
	//------------------------------------------------------------------------------
	static public int Crc32(byte buffer[], int start, int count, int crc)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			crc ^= 0xFFFFFFFF;
			while (count-- != 0)
				crc = crcTable[(crc ^ buffer[start++]) & 0xFF] ^ (crc >>> 8);
			return crc ^ 0xFFFFFFFF;
		}
		else
			return 0;
	}

	//------------------------------------------------------------------------------
	/// Adler32
	/// &param adler to be documented
	/// &param buf to be documented
	/// &param index to be documented
	/// &param len to be documented
	/// &return to be documented
	//------------------------------------------------------------------------------
	private static long Adler32(long adler, byte[] buf, int index, int len)
	{
		if (DevConfig.sprite_useDynamicPng)
		{
			long s1 = adler & 0xFFFF;
			long s2 = (adler >> 16) & 0xFFFF;

			while (len > 0)
			{
				int k = len < NMAX ? len : NMAX;
				len -= k;

				while (k-- > 0)
				{
					s1 += buf[index++] & 0xff;
					s2 += s1;
				}

				s1 %= BASE;
				s2 %= BASE;
			}

	//		while (len-- > 0)
	//		{
	//			s1 += buf[index++] & 0xFF;
	//			s2 += s1;
	//			s1 %= BASE;
	//			s2 %= BASE;
	//		}

			return (s2 << 16) | s1;
		}
		return 0;
	}


//-------------------------------------------------------------------------------------------------
/// Vars (re-used for Buffer Scaling, and Global Scaling since these are not supposed to work at the same time)
//-------------------------------------------------------------------------------------------------
public static boolean s_resizeOn         = false;
public static boolean s_resizeUseShifts  = false;
public static int     s_resizeShiftX;
public static int     s_resizeShiftY;
public static int     s_originalWidth;
public static int     s_originalHeight;
public static int     s_resizedWidth  = 0;
public static int     s_resizedHeight = 0;
public static int[]   s_resizeSrcBuffer;
public static int[]   s_resizeDstBuffer;


//-------------------------------------------------------------------------------------------------
/// Sets the global scaling that will be applied to all sprites and graphical function calls.
///
/// &param originalW - The width you are scaling FROM.
/// &param originalH - The height you are scaling FROM.
/// &param targetW - The target width you want to scale to, if using shifts then this is the shift value.
/// &param targetH - The target height you want to scale to, if using shifts then this is the shift value.
/// &param useShift - If TRUE the 3rd and 4th params are to be shift values to use for the scaling.
///
/// &note Only supported for MIDP2 configuration.
///
/// &note Only scaling up is currently supported (scaling down is an easy extension, ask if you need it)
///
/// &note This functionality is currently untested.
///
/// &note You need to enable this feature in the GLLib configuration (disabled by default)
/// &see zJYLibConfig.sprite_useResize
//-------------------------------------------------------------------------------------------------
public static void InitGlobalScaling (int originalW, int originalH, int targetW, int targetH, boolean useShift)
{
	if (DevConfig.sprite_useResize)
	{
		s_originalWidth  = originalW;
		s_originalHeight = originalH;

		if (useShift)
		{
			if( ((targetW)<0 ? -1 : 1) < 0 || ((targetH)<0 ? -1 : 1) < 0)
			{
				Utils.Dbg("InitGlobalScaling: Can't use negative shifts! Can only scale up!");;
				return;
			}

			s_resizeUseShifts = true;
			s_resizeShiftX  = targetW;
			s_resizeShiftY  = targetH;
			s_resizedWidth  = s_originalWidth  << s_resizeShiftX;
			s_resizedHeight = s_originalHeight << s_resizeShiftY;
		}
		else
		{
			s_resizedWidth  = targetW;
			s_resizedHeight = targetH;
		}

		if (!DevConfig.sprite_useDynamicTransformBuffer)
		{
			// We will need to make use to the 2nd temp buffer, and it will need to be larger!
			if( transform_int != null)
			{
				transform_int = null;
			}

			// Make larger according to both X/Y rations
			int largeSize = scaleY(scaleX(DevConfig.TMP_BUFFER_SIZE));
			transform_int = new int[largeSize];
		}

		s_resizeOn = true;
	}
}


//-------------------------------------------------------------------------------------------------
///
//-------------------------------------------------------------------------------------------------
private int[] DecodeImageAndResize(int module)
{
	if (DevConfig.sprite_useResize)
	{
		int[] img_data = DecodeImage_int(module);

		int w = GetModuleWidth(module);
		int h = GetModuleHeight(module);

		if (DevConfig.sprite_useDynamicTransformBuffer)
		{
			int largeSize = scaleX(w) * scaleY(h);
			int[] transform_int_t = new int[largeSize];
			Resize(img_data, transform_int_t, w, h, scaleX(w), scaleY(h));
			return transform_int_t;
		}

		Resize(img_data, transform_int, w, h, scaleX(w), scaleY(h));

		return transform_int;
	}

	return null;
}


//-------------------------------------------------------------------------------------------------
/// Scales the given value by the current global scale (X-Dimension)
/// &param x - value to scale
/// &return scaled value
///
/// &note You need to enable this feature in the GLLib configuration (disabled by default)
/// &see zJYLibConfig.sprite_useResize
//-------------------------------------------------------------------------------------------------
final public static int scaleX (int x)
{
	if (DevConfig.sprite_useResize)
	{
		if (s_resizeUseShifts)
		{
			return (x << s_resizeShiftX);
		}
		else
		{
			return (x * s_resizedWidth) / s_originalWidth;
		}
	}
	else
	{
		return x;
	}
}


//-------------------------------------------------------------------------------------------------
/// Scales the given value by the current global scale (Y-Dimension)
/// &param y - value to scale
/// &return scaled value
///
/// &note You need to enable this feature in the GLLib configuration (disabled by default)
/// &see zJYLibConfig.sprite_useResize
//-------------------------------------------------------------------------------------------------
final public static int scaleY (int y)
{
	if (DevConfig.sprite_useResize)
	{
		if (s_resizeUseShifts)
		{
			return (y << s_resizeShiftY);
		}
		else
		{
			return (y * s_resizedHeight) / s_originalHeight;
		}
	}
	else
	{
		return y;
	}
}


//-------------------------------------------------------------------------------------------------
///
//-------------------------------------------------------------------------------------------------
private void Resize(int[] src, int[] dst, int sW, int sH, int dW, int dH)
{
	if (DevConfig.sprite_useResize)
	{
		int valH;
		int offset = 0;

		// Perform the scaling
		if (s_resizeUseShifts)
		{
			for (int j = dH - 1; j >= 0; j--)
			{
				valH = (j >> s_resizeShiftY) * sW;
				offset = ((j + 1) * dW) - 1;

				for (int i = dW - 1; i >= 0; i--)
				{
					dst[offset--] = src[valH + (i >> s_resizeShiftX)];
				}
			}
		}
		else
		{
			for (int j = dH - 1; j >= 0; j--)
			{
				valH = ((j * sH) / dH) * sW;
				offset = ((j + 1) * dW) - 1;

				for (int i = dW - 1; i >= 0; i--)
				{
					dst[offset--] = src[valH + ((i * sW) /dW)];
				}
			}
		}
	}
}


/*
//-------------------------------------------------------------------------------------------------
/// NOTE: Only scaling the sprite data will not help with incoming positions for the sprites.
///       The global approach is to scale the positions inside PaintModule before blitting, this
///       will effect both incoming positions, and module alignment within frames.
///       Drawback: postions scaled at run-time (just a shift if initialized that way)
///
//-------------------------------------------------------------------------------------------------
public void ResizeSpriteData()
{
	if (zJYLibConfig.sprite_useResize)
	{
		_modules_w_scaled = new short[_nModules];
		_modules_h_scaled = new short[_nModules];

		for( int i = 0; i < _nModules; i++ )
		{
			_modules_w_scaled[i] = (short)GetModuleWidthOrg( i );
			_modules_h_scaled[i] = (short)GetModuleHeightOrg( i );
	    }

		//NOT A GOOD IDEEA TO SCALE AFRAME OFFSETS!
		//if(_anims_af_start != null)
		//{
		//	for (int anim = 0; anim < _anims_af_start.length; anim++)
		//	{
		//		for(int aframe = 0; aframe < _anims_naf[anim]; aframe++)
		//		{
		//			int off = (_anims_af_start[anim] + aframe) * 5;
		//			 _aframes[off+2] = (byte)scaleX(_aframes[off+2]);
		//			 _aframes[off+3] = (byte)scaleY(_aframes[off+3]);
		//		}
		//	}
		//}

		// Resize Offsets of Modules in Frames
		for (int frame = 0; frame < _frames_nfm.length; frame++)
		{
			int nFModules = _frames_nfm[ frame ]&0xFF;

			for (int fmodule = 0; fmodule < nFModules; fmodule++)
			{
				if (zJYLibConfig.sprite_useSingleArrayForFMAF)
				{
					int off = (_frames_fm_start[ frame ] + fmodule) << 2;

					_fmodules[ off + 1 ] = (byte)((_fmodules[ off + 1 ] * s_resizedWidth) / s_originalWidth);
					_fmodules[ off + 2 ] = (byte)((_fmodules[ off + 2 ] * s_resizedHeight) / s_originalHeight);
				}
				else
				{
					int off = (_frames_fm_start[ frame ] + fmodule);

					if (zJYLibConfig.sprite_useFMOffShort)
					{
						_fmodules_ox_short[ off ] = (short)(( _fmodules_ox_short[ off ] * s_resizedWidth) / s_originalWidth);
						_fmodules_oy_short[ off ] = (short)(( _fmodules_oy_short[ off ] * s_resizedHeight) / s_originalHeight);
					}
					else
					{
						_fmodules_ox_byte[ off ] = (byte)((_fmodules_ox_byte[ off ] * s_resizedWidth) / s_originalWidth);
						_fmodules_oy_byte[ off ] = (byte)((_fmodules_oy_byte[ off ] * s_resizedHeight) / s_originalHeight);
					}
				}
			}
		}

		// fmodule posY correction
		if ( mResizeCorrectY )
		{
			for (int frame = 0; frame < _frames_nfm.length; frame++)
			{
				//int off;
				int nFModules = _frames_nfm[frame]&0xFF;

				for (int fmodule = 1; fmodule < nFModules; fmodule++)
				{
					if (zJYLibConfig.sprite_useSingleArrayForFMAF)
					{
						int off  = (_frames_fm_start[ frame ] + fmodule -1) * 5;
						int off1 = (_frames_fm_start[ frame ] + fmodule   ) * 5;

						//_fmodules[ off + 1 ] = (short)scaleX( _fmodules[ off + 1 ] );
						_fmodules[ off1 + 2 ] = (byte) (_fmodules[ off + 2 ] + _modules_h_scaled[ _fmodules[ off ] ] );
					}
					else
					{

						int off  = (_frames_fm_start[ frame ] + fmodule -1);
						int off1 = (_frames_fm_start[ frame ] + fmodule   );

						if( zJYLibConfig.sprite_useAfOffShort )
						{
							// NOTE: Now split into BYTE/SHORT depending on zJYLibConfig.sprite_forceShortIndexForFModules/zJYLibConfig.sprite_allowShortIndexForFModules [MMZ] 11-3-2009
							_fmodules_oy_short[ off1 ] = (short) (_fmodules_oy_short[ off ] + _modules_h_scaled[ _fmodules_id[ off ] ] );
						}
						else
						{
							// NOTE: Now split into BYTE/SHORT depending on zJYLibConfig.sprite_forceShortIndexForFModules/zJYLibConfig.sprite_allowShortIndexForFModules [MMZ] 11-3-2009
							_fmodules_oy_byte[ off1 ] = (byte) (_fmodules_oy_byte[ off ] + _modules_h_scaled[ _fmodules_id[ off ] ] );
						}
					}

				}
			}
		}

	}
}
*/


//-------------------------------------------------------------------------------------------------
/// Init Scaling of Backbuffer
///
/// &param targetW - If using shifts this is the shift, otherwise this is the targetWidth
/// &param targetH - If using shifts this is the shift, otherwise this is the targetHeight
/// &param useShift - If true the first 2 params are shifts and will be used for optimized scaling
///
/// &note The shift values scale up, scaling down is not currently supported
///
/// &note You need to enable this feature in the GLLib configuration (disabled by default)
/// &see zJYLibConfig.useSoftwareDoubleBuffer
/// &see zJYLibConfig.useSoftwareDoubleBufferScaling
//-------------------------------------------------------------------------------------------------
public static void InitBufferScaling (int targetW, int targetH, boolean useShift)
{
	if (DevConfig.useSoftwareDoubleBuffer && DevConfig.useSoftwareDoubleBufferScaling)
	{
		s_originalWidth  = GLLib.GetScreenWidth();
		s_originalHeight = GLLib.GetScreenHeight();

		if (useShift)
		{
			if( ((targetW)<0 ? -1 : 1) < 0 || ((targetH)<0 ? -1 : 1) < 0)
			{
				Utils.Dbg("InitBufferScaling: Can't use negative shifts! Can only scale up!");;
				return;
			}

			s_resizeUseShifts = true;
			s_resizeShiftX  = targetW;
			s_resizeShiftY  = targetH;
			s_resizedWidth  = s_originalWidth  << s_resizeShiftX;
			s_resizedHeight = s_originalHeight << s_resizeShiftY;
		}
		else
		{
			s_resizedWidth  = targetW;
			s_resizedHeight = targetH;
		}

		s_resizeSrcBuffer = new int[s_originalWidth * s_originalHeight];
		s_resizeDstBuffer = new int[s_resizedWidth   * s_resizedHeight];
	}
	else
	{
		Utils.Dbg("InitBufferScaling: Must set useSoftwareDoubleBuffer and useSoftwareDoubleBufferScaling to TRUE!");;
	}
}


//-------------------------------------------------------------------------------------------------
/// Apply Scaling and blit the result
///
/// &param g - The graphics context to blit to
/// &param buffer - The buffer from which to get the pixels to scale
///
/// &note You need to enable this feature in the GLLib configuration (disabled by default)
/// &see zJYLibConfig.useSoftwareDoubleBuffer
/// &see zJYLibConfig.useSoftwareDoubleBufferScaling
//-------------------------------------------------------------------------------------------------
public static void ScaleAndBlitBuffer (Graphics g, Image buffer)
{

	if (DevConfig.useSoftwareDoubleBuffer && DevConfig.useSoftwareDoubleBufferScaling)
	{
		if (s_resizedWidth == 0 && s_resizedHeight == 0)
		{
			Utils.Dbg("ScaleAndBlitBuffer: Must call InitBufferScaling before this will work!");;
			return;
		}

		if (buffer == null)
		{
			Utils.Dbg("ScaleAndBlitBuffer: Incoming buffer is null!");;
			return;
		}

		GLLib.GetRGB(buffer, s_resizeSrcBuffer, 0, s_originalWidth, 0, 0, s_originalWidth, s_originalHeight);

		int valH;
		int offset = 0;

		// Perform the scaling
		if (s_resizeUseShifts)
		{
			if(s_resizeShiftX == 1 && s_resizeShiftY == 1)
			{
				int off_src = 0;
				int size = s_originalWidth * s_originalHeight;
				int dw = s_originalWidth << 1;
				int off_dest1 = 0;
				int off_dest2 = dw;
				int v = 0;
				int count = s_originalWidth;

				while (off_src < size)
				{
					v = s_resizeSrcBuffer[off_src++];
					s_resizeDstBuffer[off_dest1++] = v;
					s_resizeDstBuffer[off_dest1++] = v;
					s_resizeDstBuffer[off_dest2++] = v;
					s_resizeDstBuffer[off_dest2++] = v;

					if (--count == 0)
					{
						off_dest1 += dw;
						off_dest2 += dw;
						count = s_originalWidth;
					}
				}
			}
			else
			{
				for (int j = s_resizedHeight - 1; j >= 0; j--)
				{
					valH = (j >> s_resizeShiftY) * s_originalWidth;
					offset = ((j + 1) * s_resizedWidth) - 1;

					for (int i = s_resizedWidth - 1; i >= 0; i--)
					{
						s_resizeDstBuffer[offset--] = s_resizeSrcBuffer[valH + (i >> s_resizeShiftX)];
					}
				}
			}
		}
		else
		{
			for (int j = s_resizedHeight - 1; j >= 0; j--)
			{
				valH = ((j * s_originalHeight) / s_resizedHeight) * s_originalWidth;
				offset = ((j + 1) * s_resizedWidth) - 1;

				for (int i = s_resizedWidth - 1; i >= 0; i--)
				{
					s_resizeDstBuffer[offset--] = s_resizeSrcBuffer[valH + ((i * s_originalWidth) / s_resizedWidth)];
				}
			}
		}

		g.drawRGB(s_resizeDstBuffer, 0, s_resizedWidth, 0, 0, s_resizedWidth, s_resizedHeight, false);
	}
	else
	{
		Utils.Dbg("ScaleAndBlitBuffer: Must set useSoftwareDoubleBuffer and useSoftwareDoubleBufferScaling to TRUE!");;
	}


}


	///  which pool the cached images will be created in */
    int     _cur_pool = -1;

	///  table stores the indices of module images being cached.
    static short[][] _poolCacheStack;

	/// the loop cursor
    static int[] _poolCacheStackIndex;

	///  the pool size
    static int[] _poolCacheStackMax;

    static zSprite[][] _poolCacheSprites;

	//--------------------------------------------------------------------------------------------------------------------
	/// Initialize Cache Pool
	/// &param poolCount init number of pool
	//--------------------------------------------------------------------------------------------------------------------

    public static void InitCachePool(int poolCount)
    {
    	if (DevConfig.sprite_useCachePool)
    	{
	        _poolCacheStack = new short[poolCount][];
	        _poolCacheSprites = new zSprite[poolCount][];
	        _poolCacheStackIndex = new int[poolCount];
	        _poolCacheStackMax = new int[poolCount];
    	}
    }

	//--------------------------------------------------------------------------------------------------------------------
	/// Initialize pool size
	/// &param poolIndex index of pool
	/// &param size size of pool
	//--------------------------------------------------------------------------------------------------------------------
    public static void InitPoolSize (int poolIndex, int size)
    {
    	if (DevConfig.sprite_useCachePool)
    	{
	        _poolCacheStackMax[poolIndex] = size;
	        _poolCacheStack[poolIndex] = new short[size];
	        _poolCacheSprites[poolIndex] = new zSprite[size];
	        	for (int i = 0; i < _poolCacheStack[poolIndex].length; ++i) {
	        		_poolCacheStack[poolIndex][i] = -1;
	        }
    	}

    }

	//--------------------------------------------------------------------------------------------------------------------
	/// Release pool
	/// &param poolIndex index of pool
	//--------------------------------------------------------------------------------------------------------------------
    public static void ResetCachePool (int poolIndex)
    {
    	if (DevConfig.sprite_useCachePool)
    	{
	        _poolCacheStack[poolIndex] = null;
	        _poolCacheSprites[poolIndex] = null;
	        _poolCacheStackIndex[poolIndex] = 0;
	        _poolCacheStackMax[poolIndex] = 0;
    	}
    }

	//--------------------------------------------------------------------------------------------------------------------
	/// set cache pool of zSprite
	/// &param poolIndex index of pool
	//--------------------------------------------------------------------------------------------------------------------

    public void SetPool (int poolIndex)
    {
    	if (DevConfig.sprite_useCachePool)
    	{
    		_cur_pool = poolIndex;
    		int _nModules = this._nModules;
			int _palettes = this._palettes;
    		if (DevConfig.sprite_useDynamicPng)
    		{
				Utils.Dbg("WARNING_SetPool : The cache pool is nonsupport with zJYLibConfig.sprite_useDynamicPng " + "src\\zSprite\\ASprite_CachePool.jpp" + "   " + 77);;

    		}
    		else if (DevConfig.sprite_useNokiaUI)
    		{
    			short[][][] _modules_image_shortAAA = this._modules_image_shortAAA;
    			if (_modules_image_shortAAA == null)
    			{
    				_modules_image_shortAAA = this._modules_image_shortAAA = new short[_palettes][][];
    				for (int i = 0; i < _palettes; i++)
					{
    					_modules_image_shortAAA[i] = new short[_nModules][];
					}
    			}

    		}
    		else // if (zJYLibConfig.sprite_useNokiaUI)
    		{
    			if (
					(DevConfig.sprite_useCacheRGBArrays) ||					
					(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   	   )
    			{
					Utils.Dbg("WARNING_SetPool : The cache pool is nonsupport with zJYLibConfig.sprite_useCacheRGBArrays " + "src\\zSprite\\ASprite_CachePool.jpp" + "   " + 99);;
    			}
    			else // if (zJYLibConfig.sprite_useCacheRGBArrays)
    			{
    				Image[][] _module_image_imageAA = this._module_image_imageAA;
    				if (_module_image_imageAA == null)
    				{
    					_module_image_imageAA = this._module_image_imageAA = new Image[_palettes][];
    					for (int i = 0; i < _palettes; i++)
    					{
    						_module_image_imageAA[i] = new Image[_nModules];
    					}
    				}
    			}
    		}
    	}
    }

    private void UpdatePoolCache(int module, Object cached)
    {
    	if (DevConfig.sprite_useCachePool)
    	{
    		short[][][] _modules_image_shortAAA = null;
    		Image[][] _module_image_imageAA = null;
            if (DevConfig.sprite_useNokiaUI)
            {
            	_modules_image_shortAAA = this._modules_image_shortAAA;
            }
            else
            {
            	_module_image_imageAA = this._module_image_imageAA;
            }
    		int _cur_pool = this._cur_pool;
    		int _crt_pal = this._crt_pal;
    		if (_cur_pool >= 0)
    		{
	            if (DevConfig.sprite_useNokiaUI)
	            {
	            	if (_modules_image_shortAAA[_crt_pal][module] != null) {
	            		return;
	            	}
	            }
	            else
	            {
	            	if (_module_image_imageAA[_crt_pal][module] != null) {
	            		return;
	            	}
	            }
	            int[] _poolCacheStackIndex = this._poolCacheStackIndex;
	            short[][] _poolCacheStack = this._poolCacheStack;
	            zSprite[][] _poolCacheSprites = this._poolCacheSprites;
	            int cur_index = _poolCacheStackIndex[_cur_pool];
	            int img_index = _poolCacheStack[_cur_pool][cur_index];
	            int img_pal = img_index >> 10;
	            int img_module = img_index & 0x03FF;
	            if (img_index >= 0)
	            {
	            	zSprite sprite = _poolCacheSprites[_cur_pool][cur_index];
	            	if (sprite != null) {
			            if (DevConfig.sprite_useNokiaUI)
			            {
		            		sprite._modules_image_shortAAA[img_pal][img_module] = null;
			            }
			            else
			            {
		            		sprite._module_image_imageAA[img_pal][img_module] = null;
		            	}
		            }
	            }

	            short fake_module = (short)((module & 0x03FF) + (_crt_pal << 10));
	            _poolCacheStack[_cur_pool][cur_index] = fake_module;
	            _poolCacheSprites[_cur_pool][cur_index] = this;

	            _poolCacheStackIndex[_cur_pool] = (_poolCacheStackIndex[_cur_pool] + 1) % _poolCacheStackMax[_cur_pool];

	            if (DevConfig.sprite_useNokiaUI)
	            {
	            	_modules_image_shortAAA[_crt_pal][module] = (short[])cached;
	            }
	            else
	            {
	            	_module_image_imageAA[_crt_pal][module] = (Image)cached;
	            }
    		}
    	}
    }


//--------------------------------------------------------------------------------------------------------------------
/// ASprite_Palette.jpp
///
/// Extension to allow for blending between existing palettes within the sprite.
///
/// Author: Michael Zawadzki & NYC & January 2009
///
/// TODO: Create Nokia Implementation (since palette format is different)
///
//--------------------------------------------------------------------------------------------------------------------
private int _palBlend_srcA    = -1;
private int _palBlend_srcB    = -1;
private int _palBlend_dest    = -1;
private int _palBlend_current = -1;

private boolean _palBlend_UseOneColor = false;

private byte[] _palBlend_ModuleState;


//--------------------------------------------------------------------------------------------------------------------
/// Used to create a new palette that is a blend of either 2 existing palettes, or 1 existing palette and a constant color.
///
/// &param paletteA is the index of the 1st palette used for the blend.
/// &param paletteB is the index of the 2nd palette used for the blend, OR the actual constant color to use. See useColor
/// &param initBlend is the initial value for the blend. Values are [0, 255] where 0 = palette 1.
/// &param cacheImages if TRUE it will build the cache for the blended palette (using initial blend value)
/// &param useColor if TRUE the 2nd parameter will represent a constant color to blend with, and not a palette index to use for blending.
/// &return the palette index of the new blended palette (also stored internally)
///
/// &see PaletteBlending_InitDynamic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
//--------------------------------------------------------------------------------------------------------------------
int PaletteBlending_InitStatic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
{
	_palBlend_UseOneColor = useColor;
	_palBlend_srcA        = paletteA;
	_palBlend_srcB        = paletteB;
	_palBlend_dest        = AllocateExtraPalette();

	// Create the blended palette at the initial value
	PaletteBlending_SetBlend(initBlend);

	// Cache current blended palette images
	if( cacheImages )
	{
		BuildCacheImages(_palBlend_dest, 0, -1, -1);
	}

	return _palBlend_dest;
}

//--------------------------------------------------------------------------------------------------------------------
/// Used to create a new palette that is a blend of either 2 existing palettes, or 1 existing palette and a constant color.
/// This differs from the static function in that it allocates memory to use to store the blend value of each module in order
/// to be able to rebuild that modules cache when its blend value is not correct.
///
/// &param paletteA is the index of the 1st palette used for the blend.
/// &param paletteB is the index of the 2nd palette used for the blend, OR the actual constant color to use. See useColor
/// &param initBlend is the initial value for the blend. Values are [0, 255] where 0 = palette 1.
/// &param cacheImages if TRUE it will build the cache for the blended palette (using initial blend value)
/// &param useColor if TRUE the 2nd parameter will represent a constant color to blend with, and not a palette index to use for blending.
/// &return the palette index of the new blended palette (also stored internally)
///
/// &note In order to have the dynamic update you must enable this in the GLLib configuration (disabled by default)
///
/// &see PaletteBlending_InitStatic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
/// &see zJYLibConfig.sprite_useDynamicPaletteBlendingCache
//--------------------------------------------------------------------------------------------------------------------
int PaletteBlending_InitDynamic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
{
	PaletteBlending_InitStatic (paletteA, paletteB, initBlend, cacheImages, useColor);

	if (DevConfig.sprite_useDynamicPaletteBlendingCache)
	{
		int _nModules = this._nModules;
		// Allocate array to hold current blend value of each module
		_palBlend_ModuleState = new byte[_nModules];

		// Cache current blended palette images
		if( cacheImages )
		{
			int _palBlend_current = this._palBlend_current;
			byte[] _palBlend_ModuleState = this._palBlend_ModuleState;
			for( int i=0; i<_nModules ; i++)
			{
				_palBlend_ModuleState[i] = (byte)_palBlend_current;
			}
		}
	}

	return _palBlend_dest;
}

//--------------------------------------------------------------------------------------------------------------------
/// Changes the current blend value for the blended palette.
///
/// &param blendValue is the new blend value to use. [0, 255] where 0=1st palette and 255=2nd palette or color
///
/// &note In order for this to have any effect, either you are NOT cacheing anything, so the modules are rebuilt each time (kinda bad)
///       or you must enable dynamic cacheing in the GLLib configuration (disabled by default)
///
/// &see zJYLibConfig.sprite_useDynamicPaletteBlendingCache
/// &see PaletteBlending_InitDynamic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
//--------------------------------------------------------------------------------------------------------------------
void PaletteBlending_SetBlend (int blendValue)
{
	if(!((blendValue >= 0) && (blendValue <= 255)))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Blend value must be [0,255]!");;

	blendValue = ((blendValue)<(0)?(0):((blendValue)>(255)?(255):(blendValue)));

	if( blendValue != _palBlend_current )
	{
		PaletteBlending_BuildPalette(blendValue);

		_palBlend_current = blendValue;
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Gets the current blend value of the blended palette.
/// &return the current blend value, [0,255].
//--------------------------------------------------------------------------------------------------------------------
int PaletteBlending_GetBlend ()
{
	return _palBlend_current;
}

//--------------------------------------------------------------------------------------------------------------------
/// Goes through the cache and rebuilds any modules not at the correct blend value.
/// &note Only makes sense for dynamic palette blending
/// &see zJYLibConfig.sprite_useDynamicPaletteBlendingCache
//--------------------------------------------------------------------------------------------------------------------
void PaletteBlending_SynchCache ()
{
	if (DevConfig.sprite_useDynamicPaletteBlendingCache)
	{
		byte[] _palBlend_ModuleState = this._palBlend_ModuleState;
		if(!(_palBlend_ModuleState != null))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Can't synch cache if PaletteBlending_InitDynamic has never been called, or if dynamic has been released!");;

		int first = -1;
		int last  = -1;
		int _nModules = this._nModules;
		byte _palBlend_current = (byte)this._palBlend_current;
		int _crt_pal = this._crt_pal;
		for( int i=0; i<=_nModules ; i++)
		{
			if ((i < _nModules) && (_palBlend_ModuleState[i] != (byte)_palBlend_current))
			{
				_palBlend_ModuleState[i] = (byte)_palBlend_current;

				if (first == -1)
				{
					first = i;
				}

				last = i;
			}
			else
			{
				if(first != -1)
				{
					// If not rebuild the module
					BuildCacheImages(_crt_pal, first, last, -1);

					first = -1;
					last  = -1;
				}
			}
		}
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Releases memory allocated to track each modules current blend state (& modules * byte)
/// &note Only makes sense for dynamic palette blending
/// &see zJYLibConfig.sprite_useDynamicPaletteBlendingCache
//--------------------------------------------------------------------------------------------------------------------
void PaletteBlending_ReleaseDynamic ()
{
	if (DevConfig.sprite_useDynamicPaletteBlendingCache)
	{
		_palBlend_ModuleState = null;
	}
}

//--------------------------------------------------------------------------------------------------------------------
/// Builds the current blend palette using the new blend value.
///
/// &param blendValue is the new blend value to use. [0, 255]
///
/// &note This function uses the internally stored vars from the init function to know how to perform the blend.
/// &see PaletteBlending_InitDynamic (int paletteA, int paletteB, int initBlend, boolean cacheImages, boolean useColor)
//--------------------------------------------------------------------------------------------------------------------
boolean PaletteBlending_BuildPalette (int blendValue)
{
	return PaletteBlending_BuildPalette(blendValue, _palBlend_srcA, _palBlend_srcB, _palBlend_dest, _palBlend_UseOneColor);
}

//--------------------------------------------------------------------------------------------------------------------
/// Builds a palette using either 2 other palettes or 1 palette and a constant color.
///
/// &param blendValue is the blend value to use when building this palette. [0, 255]
/// &param paletteA is the index of the 1st palette used for the blend.
/// &param paletteB is the index of the 2nd palette used for the blend, OR the actual constant color to use. See useOneColor.
/// &param paletteResult is the index of the palette that will be written to with the blended values.
/// &param useOneColor if TRUE the 3rd parameter will represent a constant color to blend with, and not a palette index to use for blending.
/// &return TRUE if the resulting palette contains any alpha, FALSE if there is no alpha.
///
/// &note This will not work currently if zJYLibConfig.sprite_useNokiaUI is true. This is TODO.
/// &see zJYLibConfig.sprite_useNokiaUI
//--------------------------------------------------------------------------------------------------------------------
boolean PaletteBlending_BuildPalette (int blendValue, int paletteA, int paletteB, int paletteResult, boolean useOneColor)
{
	if (!DevConfig.sprite_useNokiaUI)
	{
		if(!((blendValue >= 0) && (blendValue <= 255)))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Blend value must be [0,255]!");;
		if(!((paletteA >= 0) && (paletteA < _palettes)))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Palette parameter 1 must be a valid palette");;
		if(!((paletteResult >= 0) && (paletteResult < _palettes)))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Palette parameter 3 must be a valid palette");;

		if (!useOneColor)
		{
			if(!((paletteB >= 0) && (paletteB < _palettes)))Utils.DBG_PrintStackTrace(false, "PaletteBlending: Palette parameter 2 must be a valid palette");;
		}

		boolean bAlpha   = false;

		int[] palA = _pal_int[paletteA];
		int[] palR = _pal_int[paletteResult];

		int palAval, palBval;
		int i = _colors;

		if (!useOneColor)
		{
			int[] palB = _pal_int[paletteB];

			while( --i >= 0)
			{
				if( (palA[i] & 0x00FFFFFF) == 0x00FF00FF )
				{
					bAlpha = true;
					palR[i] = 0x00FF00FF;
				}
				else
				{
					// Blend R and B channels
					palAval = palA[i] & 0x00FF00FF;
					palBval = palB[i] & 0x00FF00FF;
					palR[i] = (palAval + (((palBval - palAval) * blendValue) >> 8 )) & 0x00FF00FF;

					// Blend A and G channels
					palAval = (palA[i] >> 8) & 0x00FF00FF;
					palBval = (palB[i] >> 8) & 0x00FF00FF;
					palR[i] |= ((palAval << 8) + ((palBval - palAval) * blendValue))  & 0xFF00FF00;

					// Check if alpha is present (not 255)
					if((palR[i] & 0xFF000000) != 0xFF000000)
					{
						bAlpha = true;
					}
				}
			}
		}
		else
		{
			// Use these vars to precompute some constants for the constant color
			palAval = ((paletteB     ) & 0x00FF00FF) * blendValue;
			palBval = ((paletteB >> 8) & 0x00FF00FF) * blendValue;

			// Re-use this var to be (1-alpha)
			blendValue = 256 - blendValue;

			while( --i >= 0)
			{
				if( (palA[i] & 0x00FFFFFF) == 0x00FF00FF )
				{
					bAlpha = true;
					palR[i] = 0x00FF00FF;
				}
				else
				{
					// Blend R and B channels
					palR[i] = (((palA[i] & 0x00FF00FF) * blendValue + palAval) >> 8) & 0x00FF00FF;

					// Blend A and G channels
					palR[i] |= (((palA[i] >> 8) & 0x00FF00FF) * blendValue + palBval) & 0xFF00FF00;

					if((palR[i] & 0xFF000000) != 0xFF000000)
					{
						bAlpha = true;
					}
				}
			}
		}

		return bAlpha || _alpha;
	}
	else
	{
		return false;
	}
}


//-------------------------------------------------------------------------------------------------
// Pixel Effects for Sprite Cache (by Michael Zawadzki)
//
// All functionality in this file require the zJYLibConfig variable sprite_useCachePFX to be TRUE.
//-------------------------------------------------------------------------------------------------

/// Final Var that tells us if ANY cache effects exist
private static final boolean s_BCI_supportsCacheEffects     = DevConfig.sprite_useCacheEffectReflection;

/// Only required variable to support cache effects!
private static int[] s_BCI_effects = null;

/// BCI EFFECT TYPES
private static final int BCI_EFFECT_REFLECTION				= 1;
private static final int BCI_EFFECT_NUM						= 2;

/// Generic Param always holds the current type
private static final int BCI_EFFECT_PARAM_TYPE		   	    = 0;

/// REFLECTION PARAMETERS
private static final int BCI_REFLECTION_PARAM_NEW   	    = 1;
private static final int BCI_REFLECTION_PARAM_DIRECTION	    = 2;
private static final int BCI_REFLECTION_PARAM_ALPHA_START   = 3;
private static final int BCI_REFLECTION_PARAM_ALPHA_END     = 4;
private static final int BCI_REFLECTION_PARAM_PERCENT       = 5;
private static final int BCI_REFLECTION_PARAM_MODULE_START  = 6;
private static final int BCI_REFLECTION_PARAM_MODULE_END    = 7;
private static final int BCI_REFLECTION_PARAM_MODULE_END_ID = 8;
private static final int BCI_REFLECTION_PARAM_CACHE_ID      = 9;
private static final int BCI_REFLECTION_PARAM_NUM           = 10;

//-------------------------------------------------------------------------------------------------
/// Disables any cache effects if they are enabled.
//-------------------------------------------------------------------------------------------------
public static void BCI_DisableCacheParams()
{
	if (s_BCI_supportsCacheEffects)
	{
		s_BCI_effects = null;
	}
}

//-------------------------------------------------------------------------------------------------
/// Enables the cache effect of: Reflection.
/// Use: will create a seperate cache of modules with an alpha gradient to be used to render reflection.
///
/// &param newCache - If TRUE the reflection cache will get created in a new cache slot.
/// &param cacheID - The ID of the cache to use for the reflection cache. Doesn't apply if first param is false.
///                  If -1 then a cache ID will be assigned and you can get it with BCI_GetReflectionCacheID after the caching.
/// &param direction - The direction in which to apply the gradient, valid values are GLLib.LEFT, GLLib.RIGHT, GLLib.TOP, GLLib.BOTTOM.
/// &param startAlpha - The starting alpha value.
/// &param endAlpha - The ending alpha value.
/// &param percentFilled - What percentage of the module gets the gradient.
/// &param m0 - The first module which this effect should apply to.
/// &param m1 - The last module which this effect should apply to. Use -1 to reference last module.
///
/// &see zJYLibConfig.sprite_useCacheEffectReflection
//-------------------------------------------------------------------------------------------------
public static void BCI_EnableReflectionCache (boolean newCache, int cacheID, int direction, int startAlpha, int endAlpha, int percentFilled, int m0, int m1)
{
	if (DevConfig.sprite_useCacheEffectReflection)
	{
		s_BCI_effects = new int[BCI_REFLECTION_PARAM_NUM];

		s_BCI_effects[BCI_REFLECTION_PARAM_NEW]				= newCache ? 1 : 0;
		s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID]        = cacheID;
		s_BCI_effects[BCI_REFLECTION_PARAM_DIRECTION]       = direction;
		s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_START]     = startAlpha;
		s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_END]       = endAlpha;
		s_BCI_effects[BCI_REFLECTION_PARAM_PERCENT]         = -(100 - percentFilled);
		s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_START]    = m0;
		s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END]      = m1;

		s_BCI_effects[BCI_EFFECT_PARAM_TYPE]                = BCI_EFFECT_REFLECTION;
	}
	else
	{
		Utils.Dbg("BCI_EnableReflectionCache: Not enabled, you must set sprite_useCacheEffectReflection to TRUE!");;
	}
}

//-------------------------------------------------------------------------------------------------
/// Gets the cache ID which holds the reflection cache.
/// &return Cache index which holds the reflection cache.
///
/// &see zJYLibConfig.sprite_useCacheEffectReflection
//-------------------------------------------------------------------------------------------------
public static int BCI_GetReflectionCacheID ()
{
	if (DevConfig.sprite_useCacheEffectReflection)
	{
		if (s_BCI_effects != null && s_BCI_effects[BCI_EFFECT_PARAM_TYPE] == BCI_EFFECT_REFLECTION)
		{
			return s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID];
		}
	}
	else
	{
		Utils.Dbg("BCI_GetReflectionCacheID: Not enabled, you must set sprite_useCacheEffectReflection to TRUE!");;
	}

	return -1;
}

//-------------------------------------------------------------------------------------------------
/// Called at the start of BuildCacheImages if a cache effect is set.
/// &param cacheID - The cache ID of the source cache to use the effect on.
//-------------------------------------------------------------------------------------------------
private void BCI_InitCacheEffect (int cacheID)
{
	if (DevConfig.sprite_useCacheEffectReflection && (s_BCI_effects[BCI_EFFECT_PARAM_TYPE] == BCI_EFFECT_REFLECTION))
	{
		if (s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END] == -1)
		{
			s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END_ID] = _nModules - 1;
		}
		else
		{
			s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END_ID] = s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END];
		}

		if (s_BCI_effects[BCI_REFLECTION_PARAM_NEW] == 0)
		{
			// Don't allocate new cache, the cache effect goes directly onto the normal cache
			s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] = cacheID;
		}
		else
		{
			if (s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] < 0)
			{
				// Allocate new cache, the cache effect will be stored in this new cache
				s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] = AllocateExtraCache();
			}
			else
			{
				if(s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] >= _palettes)
				{
					Utils.Dbg("BCI_InitCacheEffect: CacheID parameter is out-of-range of the current valid cache's! Cache ID is " + s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] + " while max num is " + _palettes);;
					Utils.Dbg("BCI_InitCacheEffect: Assigning new cache ID and making new slot...");;

					// TODO: Should we auto-correct? Maybe better to ASSERT... [MMZ] 7-9-2009
					s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID] = AllocateExtraCache();
				}
			}
		}
	}
}

//-------------------------------------------------------------------------------------------------
/// Called during BuildCacheImages if a cache effect is set.
///
/// &param img_date - The current module in int[] form ready to be cached.
/// &param W - The width of the image that is in the img_data.
/// &param H - The height of the image that is in the img_data.
/// &param cacheID - The source cache ID used for the effect.
/// &param modID - The current module we are caching.
//-------------------------------------------------------------------------------------------------
private void BCI_SetCacheWithEffect (int[] img_data, int W, int H, int cacheID, int modID)
{
	if (DevConfig.sprite_useCacheEffectReflection && (s_BCI_effects[BCI_EFFECT_PARAM_TYPE] == BCI_EFFECT_REFLECTION))
	{
		// Module is in range?
		boolean bInRange = (modID >= s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_START]) &&
						   (modID <= s_BCI_effects[BCI_REFLECTION_PARAM_MODULE_END_ID]);

		// Effect is on current cache
		if (cacheID == s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID])
		{
			if (bInRange)
			{
				cPFX.PFX_ApplyAlphaGradient (img_data, W, H,
											  s_BCI_effects[BCI_REFLECTION_PARAM_DIRECTION],
											  s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_START],
											  s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_END],
											  s_BCI_effects[BCI_REFLECTION_PARAM_PERCENT], 0);
			}

			// Current slot holds processed pix, or normal pix if not in range
			SetCache(img_data, W, H, cacheID, modID);
		}
		// Effect goes into new cache slot
		else
		{
			// Cache the original normal one
			SetCache(img_data, W, H, cacheID, modID);

			if (bInRange)
			{
				// Process the pix
				cPFX.PFX_ApplyAlphaGradient (img_data, W, H,
											  s_BCI_effects[BCI_REFLECTION_PARAM_DIRECTION],
											  s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_START],
											  s_BCI_effects[BCI_REFLECTION_PARAM_ALPHA_END],
											  s_BCI_effects[BCI_REFLECTION_PARAM_PERCENT], 0);

				// Set the alt cache
				SetCache(img_data, W, H, s_BCI_effects[BCI_REFLECTION_PARAM_CACHE_ID], modID);
			}
		}
	}
	else
	{
		Utils.Dbg("Unknown Cache Effect is set: " + s_BCI_effects[BCI_EFFECT_PARAM_TYPE]);;
	}
}


//--------------------------------------------------------------------------------------------------------------------
/// Functions for dealing with caching FRAMES
//--------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------
/// Build Cache Frames for this sprite
/// &param pal palette to be initialized
/// &param f1 First frame to cache
/// &param f2 Last frame to cache (-1 . to end)
//------------------------------------------------------------------------------
void BuildCacheFrames(int pal, int f1, int f2)
{
	if(!(f1 >= 0))Utils.DBG_PrintStackTrace(false, "BuildCacheFrames: First frame index is " + f1 + ", it can't be negative!");;
	if(!(pal >= 0))Utils.DBG_PrintStackTrace(false, "BuildCacheFrames: Palette index is " + pal + ", it can't be negative!");;
	if(!(pal < _palettes))Utils.DBG_PrintStackTrace(false, "BuildCacheFrames: Palette index is " + pal +", its out of bounds! Current number of palettes is " + _palettes + ".");;

	int numFrames = GetFrameCount();

	if (f2 == -1 || f2 >= numFrames)
	{
		f2 = numFrames - 1;
	}

	// Make sure array refs exists to hold the cache
	if (
		(DevConfig.sprite_useCacheRGBArrays) ||
		(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
	   )
	{
		// Cache will be PIXEL ARRAY
		if (_frame_image_intAAA == null)
		{
			_frame_image_intAAA = new int[_palettes][][];
		}

		// Mare sure this palette has a slot
		if (_frame_image_intAAA[pal] == null)
		{
			_frame_image_intAAA[pal] = new int[numFrames][];
		}

		// CACHE!!
		int[][][] _frame_image_intAAA = this._frame_image_intAAA;
		for( int frame = f1; frame <= f2; frame++)
		{
			_frame_image_intAAA[pal][frame] = BuildFrameRGB(frame, pal);
		}
	}
	else
	{
		// Cache will be IMAGES
		if (_frame_image_imageAA == null)
		{
			_frame_image_imageAA = new Image[_palettes][];
		}

		// Mare sure this palette has a slot
		if (_frame_image_imageAA[pal] == null)
		{
			_frame_image_imageAA[pal] = new Image[numFrames];
		}

		// CACHE!!
		Image[][] _frame_image_imageAA = this._frame_image_imageAA;
		for( int frame = f1; frame <= f2; frame++)
		{
			_frame_image_imageAA[pal][frame] = BuildFrameImage(frame, pal);
		}
	}
}

//------------------------------------------------------------------------------
/// Allocates an image the size of the frame and renders the frame into it.
/// The current palette will be used in this case for the rendering.
///
/// &param frame - The frame ID of the frame we want to build
/// &return Image - A reference to a Image object of the rendered frame
//------------------------------------------------------------------------------
final Image BuildFrameImage (int frame)
{
	return BuildFrameImage(frame, _crt_pal);
}

//------------------------------------------------------------------------------
/// Allocates an image the size of the frame and renders the frame into it.
///
/// &param frame - The frame ID of the frame we want to build
/// &param pal - The palette to use when rendering the frame
/// &return Image - A reference to a Image object of the rendered frame
//------------------------------------------------------------------------------
Image BuildFrameImage (int frame, int pal)
{
	int w = GetFrameWidth(frame);
	int h = GetFrameHeight(frame);

	if (w == 0 || h == 0)
	{
		return null;
	}

	int[] pix = BuildFrameRGB(frame, pal);

	if (pix == null)
	{
		return null;
	}

	// Use Wrapper! (so this code is platform independent)
	return GLLib.CreateRGBImage(pix, w, h, s_frame_image_bHasAlpha);
}

//------------------------------------------------------------------------------
/// Allocates an int[] the size of the frame and renders the frame into it.
/// The current palette will be used in this case for the rendering.
///
/// &param frame - The frame ID of the frame we want to build
/// &return A reference to the int[] that represents this frame
//------------------------------------------------------------------------------
final int[] BuildFrameRGB (int frame)
{
	return BuildFrameRGB(frame, _crt_pal, null);
}

//------------------------------------------------------------------------------
/// Allocates an int[] the size of the frame and renders the frame into it.
///
/// &param frame - The frame ID of the frame we want to build
/// &param pal - The palette to use when rendering the frame
/// &return A reference to the int[] that represents this frame
//------------------------------------------------------------------------------
final int[] BuildFrameRGB (int frame, int pal)
{
	return BuildFrameRGB(frame, pal,      null);
}

//------------------------------------------------------------------------------
/// Allocates an int[] the size of the frame and renders the frame into it.
///
/// &param frame - The frame ID of the frame we want to build
/// &param pix[] - If you want the function not to allocate the returned pixel buffer
///                but to use an existing one pass it in. It will be ignored if not large enough.
/// &return A reference to the int[] that represents this frame
//------------------------------------------------------------------------------
final int[] BuildFrameRGB (int frame, int[] pix)
{
	return BuildFrameRGB(frame, _crt_pal, pix);
}

//------------------------------------------------------------------------------
/// Allocates an int[] the size of the frame and renders the frame into it.
///
/// &param frame - The frame ID of the frame we want to build
/// &param pal - The palette to use when rendering the frame
/// &param pix[] - If you want the function not to allocate the returned pixel buffer
///                but to use an existing one pass it in. It will be ignored if not large enough.
/// &return A reference to the int[] that represents this frame
//------------------------------------------------------------------------------
int[] 		BuildFrameRGB (int frame, int pal, int[] pix)
{
	boolean bAlpha = false;
	int w = GetFrameWidth(frame);
	int h = GetFrameHeight(frame);
	int size = w * h;

	if (w == 0 || h == 0)
	{
		return pix;
	}

	// Check if we have proper PIXEL ARRAY to get the pixel data
	if (pix == null || pix.length < size)
	{
		pix = new int[size];
	}

	int tmp = _crt_pal;
	_crt_pal = pal;

	if (DevConfig.sprite_useCacheFramesWithCustomBlit)
	{
		if(!(DevConfig.sprite_allowPixelArrayGraphics))Utils.DBG_PrintStackTrace(false, "BuildFrameRGB: sprite_useCacheFramesWithCustomBlit is set to TRUE but sprite_allowPixelArrayGraphics is FALSE, please enable it!");;

		SetGraphics(pix, w, h);
		PaintFrame(GLLib.g, frame, -GetFrameMinX(frame), -GetFrameMinY(frame), 0);
		SetGraphics(null, 0, 0);

		// TODO: Actually set this, either loop through pixels or set somehow inside the paint [MMZ] 7-9-2009
		s_frame_image_bHasAlpha = true;
	}
	else
	{
		// Create the Image. Use Wrapper! (so this code is platform independent)
		Image    image         = GLLib.CreateImage(w,h);
		Graphics imageGraphics = GLLib.GetGraphics(image);

		// Render MAGENTA (aka transparent)
		imageGraphics.setColor(0xFFFF00FF);
		imageGraphics.fillRect(0,0,w,h);

		// Render the zSprite FRAME
		PaintFrame(imageGraphics, frame, -GetFrameMinX(frame), -GetFrameMinY(frame), 0);

		GLLib.GetRGB(image, pix, 0, w, 0, 0, w, h);

		// Make all magenta transparent!
		int transparentColor = GLLib.GetDisplayColor(0x00FF00FF);

		while(size > 0)
		{
			size--;

			if((pix[size] & 0x00FFFFFF) == transparentColor)
			{
				pix[size] = 0x00FF00FF;
				bAlpha = true;
			}
		}

		s_frame_image_bHasAlpha = bAlpha;
	}

	_crt_pal = tmp;

	return pix;
}

//------------------------------------------------------------------------------
/// Returns the cached frame RGB data if it exists for the current palette.
/// &param frame - The frame ID of the frame we want to get.
//------------------------------------------------------------------------------
final int[] GetFrameRGB (int frame)
{
	return GetFrameRGB(frame,  _crt_pal);
}

//------------------------------------------------------------------------------
/// Returns the cached frame RGB data if it exists.
/// &param frame - The frame ID of the frame we want to get.
/// &param pal - The palette with which we want this frame.
//------------------------------------------------------------------------------
int[] GetFrameRGB (int frame, int pal)
{
	int[][][] _frame_image_intAAA = this._frame_image_intAAA;
	if ((_frame_image_intAAA == null) || (_frame_image_intAAA[pal] == null))
	{
		return null;
	}

	return _frame_image_intAAA[pal][frame];
}

//------------------------------------------------------------------------------
/// Returns the cached frame image if it exists for the current palette.
/// &param frame - The frame ID of the frame we want to get.
//------------------------------------------------------------------------------
final Image GetFrameImage(int frame)
{
	return GetFrameImage(frame, _crt_pal);
}

//------------------------------------------------------------------------------
/// Returns the cached frame image if it exists.
/// &param frame - The frame ID of the frame we want to get.
/// &param pal - The palette with which we want this frame.
//------------------------------------------------------------------------------
Image GetFrameImage(int frame, int pal)
{
	Image[][] _frame_image_imageAA = this._frame_image_imageAA;
	if ((_frame_image_imageAA == null) || (_frame_image_imageAA[pal] == null))
	{
		return null;
	}

	return _frame_image_imageAA[pal][frame];
}

//------------------------------------------------------------------------------
/// Returns weather this frame is cached
/// &param frame - The frame ID of the frame check if is cached.
/// &param pal - The palette of the frame.
//------------------------------------------------------------------------------
final boolean IsFrameCached (int frame, int pal)
{
	Image[][] _frame_image_imageAA = this._frame_image_imageAA;
	int[][][] _frame_image_intAAA = this._frame_image_intAAA;
	return ((_frame_image_imageAA != null) && (_frame_image_imageAA[pal] != null) && (_frame_image_imageAA[pal][frame] != null)) ||
		   ((_frame_image_intAAA  != null) && (_frame_image_intAAA[pal]  != null) && (_frame_image_intAAA[pal][frame]  != null));
}

//------------------------------------------------------------------------------
/// Free all the cached frames
//------------------------------------------------------------------------------
void FreeCachedFrames()
{
	if (
		(DevConfig.sprite_useCacheRGBArrays) ||
		(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
	   )
	{
		int[][][] _frame_image_intAAA = this._frame_image_intAAA;
		if (_frame_image_intAAA != null)
		{
			for (int i = 0; i < _frame_image_intAAA.length; i++)
			{
				_frame_image_intAAA[i] = null;
			}
		}
	}
	else // if (zJYLibConfig.sprite_useCacheRGBArrays)
	{
		Image[][] _frame_image_imageAA = this._frame_image_imageAA;
		if (_frame_image_imageAA != null)
		{
			for (int i = 0; i < _frame_image_imageAA.length; i++)
			{
				_frame_image_imageAA[i] = null;
			}
		}
	}
}


// ASprite_DecodeImage.jpp
//--------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------
/// Decodes loaded module data
/// &param module Module to be decoded
/// &returns Object The decoded image data
//------------------------------------------------------------------------------
Object DecodeImage(int module)
{
	if (DevConfig.sprite_useCreateRGB)
	{
		return (Object)DecodeImage_int(module);
	}
	else if (DevConfig.sprite_useNokiaUI)
	{
		return (Object)DecodeImage_short(module);
	}
	else
	{
		return (Object)DecodeImage_byte(module);
	}
}

private int[] DecodeImage_int(int module)
{
	if (DevConfig.sprite_useCreateRGB)
	{
		if (IsModuleDataValid(module))
		{
			if ( DecodeImage_Algorithm(GetModuleData(module),
									   GetStartModuleData(module, 0),
									   GetModuleWidth(module),
									   GetModuleHeight(module))
			   )
			{
				return temp_int;
			}
		}
	}
	else
	{
		Utils.Dbg("DecodeImage_int: sprite_useCreateRGB is FALSE, can't use this function!");;
	}

	return null;
}

private short[] DecodeImage_short(int module)
{
	if (DevConfig.sprite_useNokiaUI)
	{
		if (IsModuleDataValid(module))
		{
			if ( DecodeImage_Algorithm(GetModuleData(module),
									   GetStartModuleData(module, 0),
									   GetModuleWidth(module),
									   GetModuleHeight(module))
			   )
			{
				return temp_short;
			}
		}
	}

	return null;
}

private byte[] DecodeImage_byte(int module)
{
	if (IsModuleDataValid(module))
	{
		if ( DecodeImage_Algorithm(GetModuleData(module),
								   GetStartModuleData(module, 0),
								   GetModuleWidth(module),
								   GetModuleHeight(module))
		   )
		{
			return temp_byte;
		}
	}

	return null;
}

private byte[] DecodeImage_byte(byte[] image, int offset, int sizeX, int sizeY)
{
	if (DecodeImage_Algorithm(image, offset, sizeX, sizeY))
	{
		return temp_byte;
	}
	else
	{
		return null;
	}
}

private boolean DecodeImage_Algorithm( byte[] image, int si, int sizeX, int sizeY)
{

	int di = 0;
	int ds = sizeX * sizeY;


	if( DevConfig.sprite_useCreateRGB )
	{
		if (temp_int == null)
		{
			temp_int = new int[ DevConfig.TMP_BUFFER_SIZE ];
		}

		if(!(sizeX * sizeY <= temp_int.length))Utils.DBG_PrintStackTrace(false, "ERROR: sizeX x sizeY > temp_int.length ("+sizeX+" x "+sizeY+" = "+sizeX * sizeY+" > "+temp_int.length+") !!!");;
	}
	else if (DevConfig.sprite_useNokiaUI)
	{
		if (temp_short == null)
		{
			temp_short = new short[DevConfig.TMP_BUFFER_SIZE];
		}

		if(!(sizeX * sizeY <= temp_short.length))Utils.DBG_PrintStackTrace(false, "ERROR: sizeX x sizeY > temp_short.length ("+sizeX+" x "+sizeY+" = "+sizeX * sizeY+" > "+temp_short.length+") !!!");;
	}
	else
	{
		if (temp_byte == null)
		{
			temp_byte = new byte[DevConfig.TMP_BUFFER_SIZE];
		}

		if(!(sizeX * sizeY <= temp_byte.length))Utils.DBG_PrintStackTrace(false, "ERROR: sizeX x sizeY > temp_byte.length ("+sizeX+" x "+sizeY+" = "+sizeX * sizeY+" > "+temp_byte.length+") !!!");;
	}

	short[] pal_short 	= null;// dummy init
	int[] 	pal_int 	= null;// dummy init

	if (DevConfig.sprite_useNokiaUI)
	{
		if (_pal_short == null)
		{
			return false;
		}

		pal_short = _pal_short[_crt_pal];

		if (pal_short == null)
		{
			return false;
		}
	}

	if (DevConfig.sprite_useCreateRGB)
	{
		if (_pal_int == null)
		{
			return false;
		}

		pal_int = _pal_int[_crt_pal];

		if (pal_int == null)
		{
			return false;
		}
	}

	int 	clr_int 	= 0;// dummy init
	short 	clr_short 	= 0;// dummy init
	byte 	clr_byte 	= 0;// dummy init

	// HINT: Sort these encoders regarding how often are used by your game!
	if ((DevConfig.sprite_useEncodeFormatI64RLE) && (_data_format == ENCODE_FORMAT_I64RLE))
	{
		// variable RLE compression, max 64 colors...
		int _i64rle_color_mask = this._i64rle_color_mask;
		int _i64rle_color_bits = this._i64rle_color_bits;
		while (di < ds)
		{
			int c = image[si++]&0xFF;

			if (DevConfig.sprite_useCreateRGB)
			{
				clr_int = pal_int[c & _i64rle_color_mask];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				clr_short = pal_short[c & _i64rle_color_mask];
			}
			else
			{
				clr_byte = (byte)(c & _i64rle_color_mask);
			}

			c >>= _i64rle_color_bits;

			while (c-- >= 0)
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di++] = clr_int;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di++] = clr_short;
				}
				else
				{
					temp_byte[di++] = clr_byte;
				}
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI127RLE) && (_data_format == ENCODE_FORMAT_I127RLE))
	{
		// RLE compression, max 127 colors...
		while (di < ds)
		{
			int c = image[si++]&0xFF;

			if (c > 127)
			{
				int c2 = image[si++]&0xFF;

				if (DevConfig.sprite_useCreateRGB)
				{
					clr_int = pal_int[c2];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					clr_short = pal_short[c2];
				}
				else
				{
					clr_byte = (byte)(c2);
				}

				c -= 128;

				while (c-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = clr_int;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = clr_short;
					}
					else
					{
						temp_byte[di++] = clr_byte;
					}
				}
			}
			else
			{
				//img_data[di++] = MAP_COLOR(c);
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di++] = pal_int[c];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di++] = pal_short[c];
				}
				else
				{
					temp_byte[di++] = (byte)(c);
				}
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI256RLE) && (_data_format == ENCODE_FORMAT_I256RLE))
	{
		// fixed RLE compression, max 256 colors...
		while (di < ds)
		{
			int c = image[si++]&0xFF;

			if (c > 127)
			{
				c -= 128;
				while (c-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = pal_int[image[si++]&0xFF];
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = pal_short[image[si++]&0xFF];
					}
					else
					{
						temp_byte[di++] = (byte)(image[si++]&0xFF);
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					clr_int = pal_int[image[si++]&0xFF];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					clr_short = pal_short[image[si++]&0xFF];
				}
				else
				{
					clr_byte = (byte)(image[si++]&0xFF);
				}

				while (c-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = clr_int;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = clr_short;
					}
					else
					{
						temp_byte[di++] = clr_byte;
					}
				}
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI16) && (_data_format == ENCODE_FORMAT_I16))
	{
		// Make sure to round up when shifting to catch that last byte
		ds += ((ds & 0x00000001) == 0) ? 0 : 2;
		ds = ds >> 1;

		// 2 pixels/byte, max 16 colors...
		//while (di < ds)
		while (--ds >= 0)
		{
			int c = image[si++];

			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = pal_int[(c >> 4) & 0x0F];
				temp_int[di++] = pal_int[(c     ) & 0x0F];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = pal_short[(c >> 4) & 0x0F];
				temp_short[di++] = pal_short[(c     ) & 0x0F];
			}
			else
			{
				temp_byte[di++] = (byte)((c >> 4) & 0x0F);
				temp_byte[di++] = (byte)((c     ) & 0x0F);
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI4) && (_data_format == ENCODE_FORMAT_I4))
	{
		// Make sure to round up when shifting to catch that last byte
		ds += ((ds & 0x00000003) == 0) ? 0 : 4;
		ds = ds >> 2;

		// 4 pixels/byte, max 4 colors...
		//while (di < ds)
		while (--ds >= 0)
		{
			int c = image[si++];

			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = pal_int[(c >> 6) & 0x03];
				temp_int[di++] = pal_int[(c >> 4) & 0x03];
				temp_int[di++] = pal_int[(c >> 2) & 0x03];
				temp_int[di++] = pal_int[(c     ) & 0x03];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = pal_short[(c >> 6) & 0x03];
				temp_short[di++] = pal_short[(c >> 4) & 0x03];
				temp_short[di++] = pal_short[(c >> 2) & 0x03];
				temp_short[di++] = pal_short[(c     ) & 0x03];
			}
			else
			{
				temp_byte[di++] = (byte)((c >> 6) & 0x03);
				temp_byte[di++] = (byte)((c >> 4) & 0x03);
				temp_byte[di++] = (byte)((c >> 2) & 0x03);
				temp_byte[di++] = (byte)((c     ) & 0x03);
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI2) && (_data_format == ENCODE_FORMAT_I2))
	{
		// Make sure to round up when shifting to catch that last byte
		ds += ((ds & 0x00000007) == 0) ? 0 : 8;
		ds = ds >> 3;

		// 8 pixels/byte, max 2 colors...
		//while (di < ds)
		while (--ds >= 0)
		{
			int c = image[si++];

			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = pal_int[(c >> 7) & 0x01];
				temp_int[di++] = pal_int[(c >> 6) & 0x01];
				temp_int[di++] = pal_int[(c >> 5) & 0x01];
				temp_int[di++] = pal_int[(c >> 4) & 0x01];
				temp_int[di++] = pal_int[(c >> 3) & 0x01];
				temp_int[di++] = pal_int[(c >> 2) & 0x01];
				temp_int[di++] = pal_int[(c >> 1) & 0x01];
				temp_int[di++] = pal_int[(c     ) & 0x01];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = pal_short[(c >> 7) & 0x01];
				temp_short[di++] = pal_short[(c >> 6) & 0x01];
				temp_short[di++] = pal_short[(c >> 5) & 0x01];
				temp_short[di++] = pal_short[(c >> 4) & 0x01];
				temp_short[di++] = pal_short[(c >> 3) & 0x01];
				temp_short[di++] = pal_short[(c >> 2) & 0x01];
				temp_short[di++] = pal_short[(c >> 1) & 0x01];
				temp_short[di++] = pal_short[(c     ) & 0x01];
			}
			else
			{
				temp_byte[di++] = (byte)((c >> 7) & 0x01);
				temp_byte[di++] = (byte)((c >> 6) & 0x01);
				temp_byte[di++] = (byte)((c >> 5) & 0x01);
				temp_byte[di++] = (byte)((c >> 4) & 0x01);
				temp_byte[di++] = (byte)((c >> 3) & 0x01);
				temp_byte[di++] = (byte)((c >> 2) & 0x01);
				temp_byte[di++] = (byte)((c >> 1) & 0x01);
				temp_byte[di++] = (byte)((c     ) & 0x01);
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatI256) && (_data_format == ENCODE_FORMAT_I256))
	{
		// 1 pixel/byte, max 256 colors...
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = pal_int[image[si++]&0xFF];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = pal_short[image[si++]&0xFF];
			}
			else
			{
				temp_byte[di++] = (byte)(image[si++]&0xFF);
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA256_I256) && (_data_format == ENCODE_FORMAT_A256_I256))
	{
		// 1 pixel/byte, max 256 colors...
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = ((image[si++]&0xFF) << 24) | (pal_int[image[si++]&0xFF] & 0x00FFFFFF);
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = (short)(((image[si++]&0xF0) << 8) | (pal_short[image[si++]&0xFF] & 0x0FFF));
			}
			else
			{
				// Not sure about this one...
				temp_byte[di++] = (byte)(image[si++]&0xFF);
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA8_I32) && (_data_format == ENCODE_FORMAT_A8_I32))
	{
		// 1 pixel/byte, max 32 colors, 8 alpha values
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = ((image[si]&0xE0) << 24) | (pal_int[image[si]&0x1F] & 0x00FFFFFF);
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = (short)(((image[si]&0xE0) << 8) | (pal_short[image[si]&0x1F] & 0x0FFF));
			}
			else
			{
				// Not sure about this one...
				temp_byte[di++] = (byte)(image[si]&0xFF);
			}

			si++;
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA32_I8) && (_data_format == ENCODE_FORMAT_A32_I8))
	{
		// 1 pixel/byte, max 8 colors, 32 alpha values
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di++] = ((image[si]&0xF8) << 24) | (pal_int[image[si]&0x07] & 0x00FFFFFF);
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di++] = (short)(((image[si]&0xF0) << 8) | (pal_short[image[si]&0x07] & 0x0FFF));
			}
			else
			{
				// Not sure about this one...
				temp_byte[di++] = (byte)(image[si]&0xFF);
			}

			si++;
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA256_I64) && (_data_format == ENCODE_FORMAT_A256_I64))
	{
		int off = 0;

		while (di < ds)
		{
			switch (off)
			{
				case 0:
					temp_int[di] = ((image[si]&0xFC) >> 2);
					di++;
				break;

				case 1:
					temp_int[di] = ((image[si]&0x03) << 4);
					si++;
					temp_int[di] = (((image[si]&0xF0) >> 4) | temp_int[di]);
					di++;
				break;

				case 2:
					temp_int[di] = ((image[si]&0x0F) << 2);
					si++;
					temp_int[di] = (((image[si]&0xC0) >> 6) | temp_int[di]);
					di++;
				break;

				case 3:
					temp_int[di] = (image[si]&0x3F);
					di++;
					si++;
					off = -1;
				break;
			}
			off++;
		}

		di = 0;
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di] = ((image[si++]&0xFF) << 24) | (pal_int[temp_int[di]&0xFF] & 0x00FFFFFF);
				di++;
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di] = (short)(((image[si++]&0xF0) << 8) | (pal_short[image[di]&0xFF] & 0x0FFF));
				di++;
			}
			else
			{
				temp_byte[di++] = (byte)(image[si++]&0xFF);
			}
		}
    }
	else if ((DevConfig.sprite_useEncodeFormatA256_I128) && (_data_format == ENCODE_FORMAT_A256_I128))
	{
		int off = 0;

        while (di < ds)
        {
			switch (off)
			{
				case 0:
					temp_int[di] = ((image[si]&0xFE) >> 1);
					di++;
				break;

				case 1:
					temp_int[di] = ((image[si]&0x01) << 6);
					si++;
					temp_int[di] = (((image[si]&0xFC) >> 2) | temp_int[di]);
					di++;
				break;

				case 2:
					temp_int[di] = ((image[si]&0x03) << 5);
					si++;
					temp_int[di] = (((image[si]&0xF8) >> 3) | temp_int[di]);
					di++;
				break;

				case 3:
					temp_int[di] = ((image[si]&0x07) << 4);
					si++;
					temp_int[di] = (((image[si]&0xF0) >> 4) | temp_int[di]);
					di++;
				break;

				case 4:
					temp_int[di] = ((image[si]&0x0F) << 3);
					si++;
					temp_int[di] = (((image[si]&0xE0) >> 5) | temp_int[di]);
					di++;
				break;

				case 5:
					temp_int[di] = ((image[si]&0x1F) << 2);
					si++;
					temp_int[di] = (((image[si]&0xC0) >> 6) | temp_int[di]);
					di++;
				break;

				case 6:
					temp_int[di] = ((image[si]&0x3F) << 1);
					si++;
					temp_int[di] = (((image[si]&0x80) >> 7) | temp_int[di]);
					di++;
				break;

				case 7:
					temp_int[di] = (image[si]&0x7F);
					si++;
					di++;
					off = -1;
				break;
			}
			off++;
		}

		di = 0;
		while (--ds >= 0)
		{
			if (DevConfig.sprite_useCreateRGB)
			{
				temp_int[di] = ((image[si++]&0xFF) << 24) | (pal_int[temp_int[di]&0xFF] & 0x00FFFFFF);
				di++;
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				temp_short[di] = (short)(((image[si++]&0xF0) << 8) | (pal_short[image[di]&0xFF] & 0x0FFF));
				di++;
			}
			else
			{
				temp_byte[di++] = (byte)(image[si++]&0xFF);
			}
		}
    }
	else if ((DevConfig.sprite_useEncodeFormatA256_I64RLE) && (_data_format == ENCODE_FORMAT_A256_I64RLE))
	{
		// variable RLE compression, max 64 colors...
		int _i64rle_color_mask = this._i64rle_color_mask;
		int _i64rle_color_bits = this._i64rle_color_bits;
		while (di < ds)
		{
			int c = image[si++]&0xFF;

			if (DevConfig.sprite_useCreateRGB)
			{
				clr_int = pal_int[c & _i64rle_color_mask];
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				clr_short = pal_short[c & _i64rle_color_mask];
			}
			else
			{
				clr_byte = (byte)(c & _i64rle_color_mask);
			}
			c >>= _i64rle_color_bits;

			while (c-- >= 0)
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di++] = clr_int;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di++] = clr_short;
				}
				else
				{
					temp_byte[di++] = clr_byte;
				}
			}
		}

		di = 0;
		while (di < ds)
		{
			int a = image[si++]&0xFF;

			//if special flag
			if (a == 0xFE)
			{
				int n = image[si++]&0xFF;
				a = image[si++]&0xFF;

				while (n-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
						di++;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
						di++;
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
					di++;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
					di++;
				}
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA256_I127RLE) && (_data_format == ENCODE_FORMAT_A256_I127RLE))
	{
		// RLE compression, max 127 colors...
		while (di < ds)
		{
			int c = image[si++]&0xFF;

			if (c > 127)
			{
				int c2 = image[si++]&0xFF;

				if (DevConfig.sprite_useCreateRGB)
				{
					clr_int = pal_int[c2];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					clr_short = pal_short[c2];
				}
				else
				{
					clr_byte = (byte)(c2);
				}

				c -= 128;
				while (c-- > 0)
				{
					//img_data[di++] = clr;
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = clr_int;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = clr_short;
					}
					else
					{
						temp_byte[di++] = clr_byte;
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di++] = pal_int[c];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di++] = pal_short[c];
				}
				else
				{
					temp_byte[di++] = (byte)(c);
				}
			}
		}

		di = 0;
		while (di < ds)
		{
			int a = image[si++]&0xFF;

			//if special flag
			if (a == 0xFE)
			{
				int n = image[si++]&0xFF;
				a = image[si++]&0xFF;

				while (n-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
						di++;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
						di++;
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
					di++;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
					di++;
				}
			}
		}
	}
	else if ((DevConfig.sprite_useEncodeFormatA256_I256RLE) && (_data_format == ENCODE_FORMAT_A256_I256RLE))
	{
		// fixed RLE compression, max 256 colors...
		while (di < ds)
		{
			int c = image[si++]&0xFF;
			if (c > 127)
			{
				c -= 128;
				while (c-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = pal_int[image[si++]&0xFF];
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = pal_short[image[si++]&0xFF];
					}
					else
					{
						temp_byte[di++] = (byte)(image[si++]&0xFF);
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					clr_int = pal_int[image[si++]&0xFF];
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					clr_short = pal_short[image[si++]&0xFF];
				}
				else
				{
					clr_byte = (byte)(image[si++]&0xFF);
				}

				while (c-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di++] = clr_int;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di++] = clr_short;
					}
					else
					{
						temp_byte[di++] = clr_byte;
					}
				}
			}
		}

		di = 0;
		while (di < ds)
		{
			int a = image[si++]&0xFF;

			//if special flag
			if (a == 0xFE)
			{
				int n = image[si++]&0xFF;
				a = image[si++]&0xFF;

				while (n-- > 0)
				{
					if (DevConfig.sprite_useCreateRGB)
					{
						temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
						di++;
					}
					else if (DevConfig.sprite_useNokiaUI)
					{
						temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
						di++;
					}
				}
			}
			else
			{
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[di] = (a << 24) | (temp_int[di]&0x00FFFFFF);
					di++;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[di] = (short)(((a&0xF0) << 8) | (temp_short[di]&0x0FFF));
					di++;
				}
			}
		}
	}
	else
	{
		

		if(!(false))Utils.DBG_PrintStackTrace(false, "ERROR : sprite encoding Not Enabled, look at your configuration. Encoding requested : " + Integer.toHexString(_data_format));;
	}

	if (DevConfig.sprite_useBugFixImageOddSize)
	{
//			T_TMP transIndex = 0;
//			for(int color = 0; color < _pal[0].length; color++)
//			{
//				//if ((_pal[0][color] & 0xFFFFFF) == 0xFF00FF)
//				if ((_pal[0][color]>>24) == 0)
//				{
//					transIndex = (T_TMP)color;
//					break;
//				}
//			}
		if (DevConfig.sprite_useCreateRGB)
		{
			clr_int = 0;
			int[] _pal_int_0 = _pal_int[0];
			for(int color = 0; color < _pal_int_0.length; color++)
			{
				if ((_pal_int_0[color]>>24) == 0)
				{
					clr_int = (int)color;
					break;
				}
			}
		}
		else if (DevConfig.sprite_useNokiaUI)
		{
			clr_short = 0;
			short[] _pal_short_0 = _pal_short[0]; 
			for(int color = 0; color < _pal_short_0.length; color++)
			{
				if ((_pal_short_0[color]>>24) == 0)
				{
					clr_short = (short)color;
					break;
				}
			}
		}
		else
		{
			clr_byte = 0;
			int[] _pal_int_0 = _pal_int[0];
			for(int color = 0; color < _pal_int_0.length; color++)
			{
				if ((_pal_int_0[color]>>24) == 0)
				{
					clr_byte = (byte)color;
					break;
				}
			}
		}

		int newSizeX = sizeX + (sizeX % 2);
		int newSizeY = sizeY + (sizeY % 2);
		if (newSizeY  != sizeY)
		{
			for(int x = 0; x < newSizeX; x++)
			{
				//img_data[sizeY * newSizeX + x] = transIndex;
				if (DevConfig.sprite_useCreateRGB)
				{
					temp_int[sizeY * newSizeX + x] = clr_int;
				}
				else if (DevConfig.sprite_useNokiaUI)
				{
					temp_short[sizeY * newSizeX + x] = clr_short;
				}
				else
				{
					temp_byte[sizeY * newSizeX + x] = clr_byte;
				}
			}
		}
		for (int y = sizeY - 1; y >= 0; y--)
		{
			//System.arraycopy(img_data, sizeX * y, img_data, newSizeX * y, sizeX);
			//if (newSizeX != sizeX)
			//	img_data[y * newSizeX + sizeX] = transIndex;
			if (DevConfig.sprite_useCreateRGB)
			{
				System.arraycopy(temp_int, sizeX * y, temp_int, newSizeX * y, sizeX);
				if (newSizeX != sizeX)
					temp_int[y * newSizeX + sizeX] = clr_int;
			}
			else if (DevConfig.sprite_useNokiaUI)
			{
				System.arraycopy(temp_short, sizeX * y, temp_short, newSizeX * y, sizeX);
				if (newSizeX != sizeX)
					temp_short[y * newSizeX + sizeX] = clr_short;
			}
			else
			{
				System.arraycopy(temp_byte, sizeX * y, temp_byte, newSizeX * y, sizeX);
				if (newSizeX != sizeX)
					temp_byte[y * newSizeX + sizeX] = clr_byte;
			}
		}
	} // if (zJYLibConfig.sprite_useBugFixImageOddSize)

	return true;
}

//------------------------------------------------------------------------------
/// Decodes loaded module data to a byte array
/// &param dest The destination array
/// &param module The module to be decoded
/// &param img2dRGBA True if the decoded image should be RGBA, false if RGB only
/// &param half True if the decoded image should be half-scaled
//------------------------------------------------------------------------------
void DecodeImageToByteArray(byte[] dest, int module, boolean img2dRGBA, boolean half )
{
	if ( !IsModuleDataValid(module) )
	{
		return;
	}

	int sizeX = GetModuleWidth( module );
	int sizeY = GetModuleHeight( module );

	if(dest == null)
	{
		return;
	}

	//byte[] image = _modules_data;
	//int si = _modules_data_off[ module ];//&0xFFFF;
	//int di = 0;
	//int ds = sizeX * sizeY;

	byte [] img_data = dest;

    int bytesPerPixel;
    if ( img2dRGBA == true )
    {
        bytesPerPixel = 4;
    }
    else
    {
        bytesPerPixel = 3;
    }

	if( !DecodeImage_Algorithm( GetModuleData(module), GetStartModuleData( module, 0 ), sizeX, sizeY) )
	{
		return;
	}

    if (DevConfig.sprite_useCreateRGB)
	{
        img_data = new byte[ sizeX * sizeY * bytesPerPixel ];

        for (int j = 0; j < sizeY; j++)
	    {
	        for (int i = 0; i < sizeX; i++)
            {
                int off = (i + j*sizeX);

                img_data[ off * bytesPerPixel + 0 ] = (byte)((temp_int[ off ] >> 0 ) & 0xFF);
                img_data[ off * bytesPerPixel + 1 ] = (byte)((temp_int[ off ] >> 8 ) & 0xFF);
                img_data[ off * bytesPerPixel + 2 ] = (byte)((temp_int[ off ] >> 16 ) & 0xFF);

                if (bytesPerPixel == 4)
                {
                    img_data[ off * bytesPerPixel + 3 ] = (byte)((temp_int[ off ] >> 24 ) & 0xFF);
                }
            }
        }
		if (temp_int == null)
		{
			temp_int = new int[DevConfig.TMP_BUFFER_SIZE];
		}

	}else
    {
        if( true )
            return;
    }


	//scale to half in the same buffer
	int width  = sizeX >> 1;
	int height = sizeY >> 1;

    int x = 0, y = 0;
    int r0, g0, b0, a0 = 0;
    int r1, g1, b1, a1 = 0;
    int r2, g2, b2, a2 = 0;
    int r3, g3, b3, a3 = 0;
    int r, gr,  b, a;
    int di=0;

	if( half )
	{

	    for (int j = 0; j < height; j++)
	    {
	        for (int i = 0; i < width; i++)
	        {
	            int idx = (j*2 * bytesPerPixel) * sizeX + (i*2 * bytesPerPixel);
                r0 = img_data[idx  ]&0xFF;
                g0 = img_data[idx+1]&0xFF;
                b0 = img_data[idx+2]&0xFF;

                if (bytesPerPixel == 4)
                {
                    a0 = img_data[idx+3]&0xFF;
                }

	            idx += bytesPerPixel;
                r1 = img_data[idx  ]&0xFF;
                g1 = img_data[idx+1]&0xFF;
                b1 = img_data[idx+2]&0xFF;

                if (bytesPerPixel == 4)
                {
                    a1 = img_data[idx+3]&0xFF;
                }

	            idx = ((j*2 + 1) * bytesPerPixel) * sizeX + (i*2 * bytesPerPixel);
                r2 = img_data[idx  ]&0xFF;
                g2 = img_data[idx+1]&0xFF;
                b2 = img_data[idx+2]&0xFF;

                if (bytesPerPixel == 4)
                {
                    a2 = img_data[idx+3]&0xFF;
                }

	            idx += bytesPerPixel;
                r3 = img_data[idx  ]&0xFF;
                g3 = img_data[idx+1]&0xFF;
                b3 = img_data[idx+2]&0xFF;

                if (bytesPerPixel == 4)
                {
                    a3 = img_data[idx+3]&0xFF;
                }

                r  = (r0 + r1 + r2 + r3) >> 2;
                gr = (g0 + g1 + g2 + g3) >> 2;
                b  = (b0 + b1 + b2 + b3) >> 2;
                a  = (a0 + a1 + a2 + a3) >> 2;

                img_data[di++] = (byte)(r  & 0xFF);
                img_data[di++] = (byte)(gr & 0xFF);
                img_data[di++] = (byte)(b  & 0xFF);

                if (bytesPerPixel == 4)
                    img_data[di++] = (byte)(a&0xFF);
            }
        }
	}
}

//------------------------------------------------------------------------------
/// Sets the temporary buffer array to be used for decoding operations
/// &param pArray The buffer array to be used
//------------------------------------------------------------------------------
public static void SetTempBuffer(Object pArray)
{
	if (pArray instanceof int[])
	{
		temp_int 	= (int[])pArray;
	}
	else if (pArray instanceof short[])
	{
		temp_short 	= (short[])pArray;
	}
	else
	{
		temp_byte 	= (byte[])pArray;
	}
}


//------------------------------------------------------------------------------
/// Sets the current palette for the sprite
/// &param pal the palette to be set
//------------------------------------------------------------------------------
void SetCurrentPalette(int pal)
{
	if ((pal < _palettes) && (pal >=0)) {
		_crt_pal = pal;
	} else {
		_crt_pal = 0;
		return;
	}
}

//------------------------------------------------------------------------------
/// Gets the current palette for the sprite
/// & returns the current palette
//------------------------------------------------------------------------------
int  GetCurrentPalette()			{ return _crt_pal; }

//--------------------------------------------------------------------------------------------------------------------

/*
	void SetColor(int index, short color)
	{
	//	_font_color =
		_pal[_crt_pal][index] = color;
	}

//--------------------------------------------------------------------------------------------------------------------

	void SetColor(int index, int color)
	{
	//	_font_color =
		_pal[_crt_pal][index] = (short)(((((color & 0xFF000000)>>24) & 0xF0)<<8) |
	   						  			((((color & 0x00FF0000)>>16) & 0xF0)<<4) |
							  			((((color & 0x0000FF00)>>8 ) & 0xF0)   ) |
							 			((((color & 0x000000FF)    ) & 0xF0)>>4));
	}

//--------------------------------------------------------------------------------------------------------------------

	void SetColor(int index, int a, int r, int g_  int b)
	{
	//	_font_color =
		_pal[_crt_pal][index] = (short)(((a & 0xF0)<<8) |
	   						  			((r & 0xF0)<<4) |
							  			((g & 0xF0)   ) |
							 			((b & 0xF0)>>4));
	}
*/

//--------------------------------------------------------------------------------------------------------------------

    // Palette generation based on other palette...

    public final static int PAL_ORIGINAL    = -1; //- original colors
    public final static int PAL_INVISIBLE   = 0;  //- invisible (the sprite will be hidden)
    public final static int PAL_RED_YELLOW  = 1;  //- red-yellow
    public final static int PAL_BLUE_CYAN   = 2;  //- blue-cyan
    public final static int PAL_GREEN       = 3;  //- green
    public final static int PAL_GREY        = 4;  //- grey
    public final static int PAL_BLEND_BLACK = 5;  //- blend back 50%


    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &returns The generated palette
	//------------------------------------------------------------------------------
    static int[] GenPalette(int type, int[] pal )
	{
		int[] new_pal = new int[pal.length];
		return GenPalette(type, pal, new_pal);
	}

	//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &param new_pal The INDEX of the sprite's palette the result is going into
	/// &returns The generated palette
	//------------------------------------------------------------------------------
	int[] GenPalette(int type, int[] pal, int new_pal)
	{
		return GenPalette(type, pal, (int[])GetPalette(new_pal));
	}

	//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &param new_pal The array into which the result goes
	/// &returns The generated palette
	//------------------------------------------------------------------------------
    static int[] GenPalette(int type, int[] pal, int[] new_pal )
	{
		if (type <  0) return pal;	// original
		if (type == PAL_INVISIBLE) return null; // invisible
		int pal_length = pal.length;
		if( (DevConfig.sprite_useGenPalette & (1<<PAL_RED_YELLOW) ) != 0 && type == PAL_RED_YELLOW )
		{
			for( int i = 0; i < pal_length; i++)
				new_pal[i] = (pal[i] | 0x00FF3300) & 0xFFFFFF00;
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_BLUE_CYAN) ) != 0 && type == PAL_BLUE_CYAN )
        {
			for (int i = 0; i < pal_length; i++)
				new_pal[i] = (pal[i] | 0x000033FF) & 0xFF00FFFF;
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_GREEN) ) != 0 && type == PAL_GREEN )
        {
			for (int i = 0; i < pal_length; i++)
				new_pal[i] = (pal[i] | 0x00000000) & 0xFF00FF00;
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_GREY) ) != 0 && type == PAL_GREY )
        {
			for (int i = 0; i < pal_length; i++)
			{
				//int a = (pal[i] & 0xFF000000);
                int r = (pal[i] & 0x00FF0000) >> 16;
                int g = (pal[i] & 0x0000FF00) >> 8;
                int b = (pal[i] & 0x000000FF);

				r = ((r + g + b) / 3) & 0xFF;

                //new_pal[i] = a | (( (r + 0xFF)>>1 ) << 16) | ((g>>1) << 8) | (b>>1);
                new_pal[i] = (pal[i] & 0xFF000000) | (r << 16) | (r << 8) | (r);
			}
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_BLEND_BLACK) ) != 0 && type == PAL_BLEND_BLACK )
        {
			for( int i = 0; i < pal_length; i++)
			{
				int a = (pal[i] & 0xFF000000);
				int r = (pal[i] & 0x00FC0000) >> 2;
				int g = (pal[i] & 0x0000FC00) >> 2;
				int b = (pal[i] & 0x000000FC) >> 2;

				new_pal[i] = (a | r | g | b);
			}
		}

		return new_pal;
	}

	//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &returns The generated palette
	//------------------------------------------------------------------------------
    static short[] GenPalette(int type, short[] pal )
	{
		short[] new_pal = new short[pal.length];
		return GenPalette(type, pal, new_pal);
	}

	//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &param new_pal The INDEX of the sprite's palette the result is going into
	/// &returns The generated palette
	//------------------------------------------------------------------------------
	short[] GenPalette(int type, short[] pal, int new_pal)
	{
		return GenPalette(type, pal, (short[])GetPalette(new_pal));
	}

    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Generates a palette based on another palette
	/// &param type Palette type (PAL_ORIGINAL, PAL_INVISIBLE, PAL_RED_YELLOW, PAL_BLUE_CYAN, PAL_GREEN, PAL_GREY or PAL_BLEND_BLACK)
	/// &param pal The existing palette
	/// &param new_pal The array into which the result goes
	/// &returns The generated palette
	//------------------------------------------------------------------------------
	static short[] GenPalette(int type, short[] pal, short[] new_pal )
	{
		if (type <  0) return pal;	// original
		if (type == PAL_INVISIBLE) return null; // invisible
		int pal_length = pal.length;
		if( (DevConfig.sprite_useGenPalette & (1<<PAL_RED_YELLOW) ) != 0 && type == PAL_RED_YELLOW )
		{
			for( int i = 0; i < pal_length; i++)
				new_pal[i] = (short)((pal[i] | 0x0F30) & 0xFFF0);
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_BLUE_CYAN) ) != 0 && type == PAL_BLUE_CYAN )
        {
			for (int i = 0; i < pal_length; i++)
				new_pal[i] = (short)( (pal[i] | 0x003F) & 0xF0FF );
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_GREEN) ) != 0 && type == PAL_GREEN )
        {
			for (int i = 0; i < pal_length; i++)
				new_pal[i] = (short)( (pal[i] | 0x0000) & 0xF0F0 );
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_GREY) ) != 0 && type == PAL_GREY )
        {
			for (int i = 0; i < pal_length; i++)
			{
				//int a = (pal[i] & 0xF000) ;
				int r = (pal[i] & 0x0F00) >> 8;
				int g = (pal[i] & 0x00F0) >> 4;
				int b = (pal[i] & 0x000F);

				r = ((r + g + b) / 3 ) & 0x0F;

                //new_pal[i] = (short)( a | (( (r + 0xF)>>1 ) << 8) | ((g>>1) << 4) | (b>>1) );
                new_pal[i] = (short)((pal[i] & 0xF000) | (r<<8) | (r<<4) | (r));

			}
        }
        if( (DevConfig.sprite_useGenPalette & (1<<PAL_BLEND_BLACK) ) != 0 && type == PAL_BLEND_BLACK )
        {
			for( int i = 0; i < pal_length; i++)
			{
				int a = (pal[i] & 0xF000);
				int r = (pal[i] & 0x0F00) >> 2;
				int g = (pal[i] & 0x00F0) >> 2;
				int b = (pal[i] & 0x000F) >> 2;

				new_pal[i] = (short)( a | r | g | b );
			}
		}

		return new_pal;
	}

//--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Modify the alpha value of a palette
	/// &param pal The palette number
	/// &param alpha The alpha to be set
	//------------------------------------------------------------------------------
	void ModifyPaletteAlpha( int pal, int alpha )
	{
		ModifyPaletteAlpha(pal, alpha, _alpha, _multiAlpha);
	}

	//------------------------------------------------------------------------------
	/// Modify the alpha value of a palette
	/// &param pal The palette number
	/// &param alpha The alpha to be set
	/// &param hasAlpha - If TRUE the processing will account for transparent color 0xFF00FF
	/// &param isAlphaSprite - If TRUE the processing will multiply alpha value not just set them.
	//------------------------------------------------------------------------------
	void ModifyPaletteAlpha( int pal, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!(pal >= 0))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlpha: Trying to modify palette of negative index!");;
		if(!(pal < _palettes))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlpha: Trying to modify palette which is out-of-range: " + pal);;

		if (DevConfig.sprite_useNokiaUI)
		{
			if(!(_pal_short != null))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlpha: This palette data is null!");;
			short[] _pal_short_pal = _pal_short[pal];
			int size = _pal_short_pal.length;

            // Multi-Level Alpha
			if (isAlphaSprite)
			{
				for (int i = size-1; i >= 0; i--)
				{
					int a = (_pal_short_pal[i] >> 12) & 0xF;
					a = (a * alpha) >> 8;
					a = (a & 0xF) << 12;

					_pal_short_pal[i] = (short)(a | (_pal_short_pal[i] & 0x0FFF));
				}
			}
			// Contains Transparency
			else if (hasAlpha)
			{
				alpha = ((alpha>>4) & 0xF) << 12;

				for (int i = size-1; i >= 0; i--)
				{
					if( ((_pal_short_pal[i] & 0xFFF) != 0xF0F) &&
						((_pal_short_pal[i] >> 12) != 0)
					  )
					{
						_pal_short_pal[i] = (short)(alpha | (_pal_short_pal[i] & 0x0FFF));
					}
				}
			}
			// No alpha
			else
			{
				alpha = ((alpha>>4) & 0xF) << 12;

				for (int i = size-1; i >= 0; i--)
				{
					_pal_short_pal[i] = (short)(alpha | (_pal_short_pal[i] & 0x00FFFFFF));
				}
			}
		}
		else
		{
			if(!(_pal_int != null))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlpha: This palette data is null!");;
			int[] _pal_int_pal = _pal_int[pal];
			int size = _pal_int_pal.length;

			// Multi-Level Alpha
			if (isAlphaSprite)
			{
				for (int i = size-1; i >= 0; i--)
				{
					int a = (_pal_int_pal[i] >> 24) & 0xFF;
					a = (a * alpha) >> 8;
					a = (a & 0xFF) << 24;

					_pal_int_pal[i] = (a | (_pal_int_pal[i] & 0x00FFFFFF));
				}
			}
			// Contains Transparency
			else if (hasAlpha)
			{
				alpha = (alpha & 0xFF) << 24;

				for (int i = size-1; i >= 0; i--)
				{
					if( ((_pal_int_pal[i] & 0xFFFFFF) != 0xFF00FF) &&
					    ((_pal_int_pal[i] >> 24) != 0)
					  )
					{
						_pal_int_pal[i] = (alpha | (_pal_int_pal[i] & 0x00FFFFFF));
					}
				}
			}
			// No alpha
			else
			{
				alpha = (alpha & 0xFF) << 24;

				for (int i = size-1; i >= 0; i--)
				{
					_pal_int_pal[i] = (alpha | (_pal_int_pal[i] & 0x00FFFFFF));
				}
			}
		}
	}

    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Modify palette, original palette will turn into the alpha value, and set one and only color for all palette
	/// original palette = 0x00RRGGBB, color parameter = 0x00CCCCCC
	/// result palette = 0xBBCCCCCC
	/// &param palNb The palette number
	/// &param color The color to be set
	//------------------------------------------------------------------------------
	void ModifyPalette(int palNb, int color)
	{
		_alpha = true;

		if (DevConfig.sprite_useNokiaUI)
		{
			color = color & 0x0FFF;
			int _4444  = (( color & 0xF0)>>4);
	 			_4444 += (( color & 0xF000)>>8);
	 			_4444 += (( color & 0xF00000)>>12);
	 		short[] _pal_short_Nb = _pal_short[ palNb ];
			for( int i = 0; i < _pal_short_Nb.length;i++)
			{
				if( (_pal_short_Nb[ i ] & 0x0FFF) != 0x0F0F && (_pal_short_Nb[ i ] >> 12) != 0)
					_pal_short_Nb[ i ] = (short)( ( (_pal_short_Nb[i] & 0xF) << 12) | _4444);
			}
		}
		else
		{
			color = color & 0x00FFFFFF;
	 		int[] _pal_int_Nb = _pal_int[ palNb ];
		    for ( int i = 0; i < _pal_int_Nb.length; i++ )
		    {
			    if( (_pal_int_Nb[ i ] & 0xFFFFFF ) != 0xFF00FF && (_pal_int_Nb[i] >> 24) != 0)
			    	_pal_int_Nb[ i ] = ( ( ( _pal_int_Nb[ i ] & 0xFF) << 24 ) | color);
		    }
		}
	}


    //--------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------
    /// Modify palette, original palette will have its alpha channel set using the second palette's RED channel.
    /// Original palette = 0x00RRGGBB, Alpha palette = 0x00AA0000
    /// Result palette = 0xAARRGGBB
    /// &param p_iPaletteID is the index of the palette you want to alter
    /// &param p_iAlphaPaletteID is the index of the palette that holds the alpha channel in its RED channel
    //------------------------------------------------------------------------------
    void ModifyPaletteAlphaUsingAltPalette (int p_iPaletteID, int p_iAlphaPaletteID)
    {
        if(!(p_iPaletteID >= 0))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlphaUsingAltPalette: trying to modify palette of negative index!");;
        if(!(p_iPaletteID < _palettes))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlphaUsingAltPalette: trying to modify palette which is out-of-range: " + p_iPaletteID);;
        if(!(p_iAlphaPaletteID >= 0))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlphaUsingAltPalette: trying to use alpha palette of negative index!");;
        if(!(p_iAlphaPaletteID < _palettes))Utils.DBG_PrintStackTrace(false, "ModifyPaletteAlphaUsingAltPalette: trying to use alpha palette which is out-of-range: " + p_iAlphaPaletteID);;

        _alpha = true;
        _multiAlpha = true;

        if (DevConfig.sprite_useNokiaUI)
        {
        	short[][] _pal_short = this._pal_short;
        	short[] _pal_short_this = _pal_short[p_iPaletteID];
            for (int i=0; i < _pal_short_this.length; i++)
            {
                if ((_pal_short_this[i] & 0x0FFF) != 0x0F0F && (_pal_short_this[i] >> 12) != 0)
                {
                	_pal_short_this[i] &= 0x0FFF;
                	_pal_short_this[i] |= ((_pal_short[p_iAlphaPaletteID][i] & 0x0F00) << 4);
                }
            }
        }
        else
        {
            // For each color in the palette
        	int[][] _pal_int = this._pal_int;
        	int[] _pal_int_this = _pal_int[p_iPaletteID];
            for (int i=0; i < _pal_int_this.length; i++)
            {
                // If its not invisible
                if( (_pal_int_this[i] & 0x00FFFFFF ) != 0x00FF00FF && (_pal_int_this[i] >> 24 ) != 0)
                {
                    // Strip alpha channel
                    _pal_int_this[i] &= 0x00FFFFFF;

                    // Set alpha channel using the other palettes RED channel
                    _pal_int_this[i] |= ((_pal_int[p_iAlphaPaletteID][i] & 0x00FF0000) << 8);
                }
            }
        }
    }


    //--------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------
    /// This is just a wrapper around ModifyPaletteAlphaUsingAltPalette which automatically uses
    /// the last palette for the alpha information.
    /// &param p_iPaletteID is the index of the palette you want to alter
    ///
    /// &note This is the old approach of having alpha palettes where you need to assemble the ARGB palette from 2 palettes.
    ///       However, Aurora can now export directly the ARGB palette! This new approach is easier to work with.
    ///
    /// &see zSprite&ModifyPaletteAlphaUsingAltPalette (int p_iPaletteID, int p_iAlphaPaletteID)
    //------------------------------------------------------------------------------
    void ModifyPaletteAlphaUsingLastPalette (int p_iPaletteID)
    {
        ModifyPaletteAlphaUsingAltPalette(p_iPaletteID, _palettes - 1);
    }


    //--------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/// Will allocate memory for another palette and modify existing vars accordingly.
	/// &returns the palette index of the new palette.
    //------------------------------------------------------------------------------
    int AllocateExtraPalette ()
    {
		// Create memory for new palette within this sprite to hold blended palette
		_pal_int[_palettes] = new int[_colors];

		// Allocate extra cache for this palette
		if (AllocateExtraCache() < 0 )
		{
			// If failed inc var and correct & will get allocated when its time
			_palettes++;
		}

		return (_palettes-1);
	}


	//------------------------------------------------------------------------------
	/// Will copy the palette data from one slot to another.
	///
	/// &note Both palettes memory needs to already be allocated.
	///
	/// &param palA - index of the palette to copy from.
	/// &param palB - index of the palette to write to.
	//------------------------------------------------------------------------------
	public final void CopyPalette (int palA, int palB)
	{
		if(!(palA >= 0))Utils.DBG_PrintStackTrace(false, "CopyPalette: trying to read palette of negative index: " + palA);;
		if(!(palA < _palettes))Utils.DBG_PrintStackTrace(false, "CopyPalette: trying to read palette which is out-of-range: " + palA);;
		if(!(palB >= 0))Utils.DBG_PrintStackTrace(false, "CopyPalette: trying to set palette of negative index: " + palB);;
		if(!(palB < _palettes))Utils.DBG_PrintStackTrace(false, "CopyPalette: trying to set palette which is out-of-range: " + palB);;

		if (DevConfig.sprite_useNokiaUI)
		{
			short[][] _pal_short = this._pal_short;
			if(!(_pal_short != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Palettes are null!");;
			if(!(_pal_short[palA] != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Source palette is null!!");;
			if(!(_pal_short[palB] != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Destination palette is null!!");;
			if(!(_pal_short[palA].length == _pal_short[palB].length))Utils.DBG_PrintStackTrace(false, "CopyPalette: Source and Destination has different sizes!");;

			System.arraycopy(_pal_short[palA], 0, _pal_short[palB], 0, _pal_short[palA].length);
		}
		else
		{
			int[][] _pal_int = this._pal_int;
			if(!(_pal_int != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Palettes are null!");;
			if(!(_pal_int[palA] != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Source palette is null!!");;
			if(!(_pal_int[palB] != null))Utils.DBG_PrintStackTrace(false, "CopyPalette: Destination palette is null!!");;
			if(!(_pal_int[palA].length == _pal_int[palB].length))Utils.DBG_PrintStackTrace(false, "CopyPalette: Source and Destination has different sizes!");;

			System.arraycopy(_pal_int[palA], 0, _pal_int[palB], 0, _pal_int[palA].length);
		}
    }


    //--------------------------------------------------------------------------------------------------------------------
	/// Copies one palette into another while simulatously modifying its alpha values.
	///
	/// &param srcPal is the index of the palette that will be the source.
	/// &param dstPal is the index of the palette that will be written to.
	/// &param alpha is the alpha blend value to use when copying this palette. [0, 255]
	/// &return TRUE if the resulting palette contains any alpha, FALSE if there is no alpha.
	///
	/// &note This will not work currently if zJYLibConfig.sprite_useNokiaUI is true. This is TODO.
	/// &see zJYLibConfig.sprite_useNokiaUI
	///
	/// &see Palette_CopyAndApplyAlpha (int srcPal, int dstPal, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	//--------------------------------------------------------------------------------------------------------------------
	final boolean Palette_CopyAndApplyAlpha (int srcPal, int dstPal, int alpha)
	{
		return Palette_CopyAndApplyAlpha (srcPal, dstPal, alpha, _alpha, _multiAlpha);
	}


    //--------------------------------------------------------------------------------------------------------------------
	/// Copies one palette into another while simulatously modifying its alpha values.
	///
	/// &param srcPal is the index of the palette that will be the source.
	/// &param dstPal is the index of the palette that will be written to.
	/// &param alpha is the alpha blend value to use when copying this palette. [0, 255]
	/// &param hasAlpha does the source palette have ANY alpha
	/// &param isAlphaSprite does the source palette contain alpha values besides 0 and 255
	/// &return TRUE if the resulting palette contains any alpha, FALSE if there is no alpha.
	///
	/// &note This will not work currently if zJYLibConfig.sprite_useNokiaUI is true. This is TODO.
	/// &see zJYLibConfig.sprite_useNokiaUI
	///
	/// &see Palette_CopyAndApplyAlpha (int srcPal, int dstPal, int alpha)
	//--------------------------------------------------------------------------------------------------------------------
	boolean Palette_CopyAndApplyAlpha (int srcPal, int dstPal, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
	    if (!DevConfig.sprite_useNokiaUI)
	    {
	        if(!((alpha >= 0) && (alpha <= 255)))Utils.DBG_PrintStackTrace(false, "Palette_CopyAndApplyAlpha: Blend value must be [0,255]!");;
	        if(!((srcPal >= 0) && (srcPal < _palettes)))Utils.DBG_PrintStackTrace(false, "Palette_CopyAndApplyAlpha: Palette parameter 1 must be a valid palette");;
	        if(!((dstPal >= 0) && (dstPal < _palettes)))Utils.DBG_PrintStackTrace(false, "Palette_CopyAndApplyAlpha: Palette parameter 2 must be a valid palette");;

	        boolean bAlpha   = false;

	        int[] palA = _pal_int[srcPal];
	        int[] palR = _pal_int[dstPal];

	        int palAval, blend;

			if (isAlphaSprite)
			{
				for(int i = _colors; --i >= 0;)
				{
					if( (palA[i] & 0x00FFFFFF) == 0x00FF00FF )
					{
						bAlpha = true;
						palR[i] = 0x00FF00FF;
					}
					else
					{
						// Blend A Channel
						palAval = ((palA[i] & 0xFF000000)>>24)&0x000000FF;
						blend = (palAval * alpha) >> 8;

						if( blend != 0xFF )
						{
							bAlpha = true;
						}

						palR[i] = (blend<<24) | palA[i] & 0x00FFFFFF;
					}
				}
			}
			else if (hasAlpha)
			{
				blend = (alpha << 24);

				for(int i = _colors; --i >= 0;)
				{
					if( (palA[i] & 0x00FFFFFF) == 0x00FF00FF )
					{
						bAlpha = true;
						palR[i] = 0x00FF00FF;
					}
					else
					{
						palR[i] = (blend) | palA[i] & 0x00FFFFFF;
					}
				}
			}
			else
			{
				blend = (alpha << 24);

				for(int i = _colors; --i >= 0;)
				{
					palR[i] = (blend) | palA[i] & 0x00FFFFFF;
				}

			}

	        return bAlpha || _alpha;
	    }
	    else
	    {
	        return false;
	    }
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Sets one color inside of a given palette.
	///
	/// &param palette is the index of the palette whose color we want to set
	/// &param index is the index within the palette whose color we want to set
	/// &param color is the color to be set (use common internal ARGB format: 8888)
	//--------------------------------------------------------------------------------------------------------------------
	final void Palette_SetColor (int palette, int index, int color)
	{
	    if(!(palette >= 0))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Error: palette ID is negative!" + palette);;
	    if(!(palette < _palettes))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Error: palette ID is greater than available palettes!" + palette);;
		if(!(index >= 0))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Error: color index is negative!" + index);;

		if (DevConfig.sprite_useNokiaUI)
		{
			short[][] _pal_short = this._pal_short;
			if(!(_pal_short != null))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Palettes are null!");;
			if(!(_pal_short[palette] != null))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Palette["+palette+"] is null!!");;
			if(!(_pal_short[palette].length > index))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Error: color index is greater than this palette has!" + index);;

			_pal_short[palette][index] = (short)(((color>>16) & 0xF000) | ((color>>12) & 0x0F00) | ((color>> 8) & 0x00F0) | ((color>> 4) & 0x000F));
		}
		else
		{
			int[][] _pal_int = this._pal_int;
			if(!(_pal_int != null))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Palettes are null!");;
			if(!(_pal_int[palette] != null))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Palette["+palette+"] is null!!");;
			if(!(_pal_int[palette].length > index))Utils.DBG_PrintStackTrace(false, "Palette_SetColor: Error: color index is greater than this palette has!" + index);;

			_pal_int[palette][index] = color;
		}
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Returns the size of the requested palette.
	///
	/// &param palette is the palette ID of the palette whose size you want.
	/// &return the size of the given palette
	//--------------------------------------------------------------------------------------------------------------------
	final int Palette_GetSize (int palette)
	{
		if(!(palette >= 0))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Error: palette ID is negative!" + palette);;
		if(!(palette < _palettes))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Error: palette ID is greater than available palettes!" + palette);;

		if (DevConfig.sprite_useNokiaUI)
		{
			short[][] _pal_short = this._pal_short;
			if(!(_pal_short != null))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Palettes are null!");;
			if(!(_pal_short[palette] != null))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Palette["+palette+"] is null!!");;

			return _pal_short[palette].length;
		}
		else
		{
			int[][] _pal_int = this._pal_int;
			if(!(_pal_int != null))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Palettes are null!");;
			if(!(_pal_int[palette] != null))Utils.DBG_PrintStackTrace(false, "Palette_GetSize: Palette["+palette+"] is null!!");;

			return _pal_int[palette].length;
		}
	}

//--------------------------------------------------------------------------------------------------------------------


    // ASprite_DrawList.jpp


    //--------------------------------------------------------------------------------------------------------------------

	Object[][]	_aryPrecomputedImages;
	short[][]	_aryPrecomputedX;
	short[][]	_aryPrecomputedY;
	short[][]	_aryPrecomputedSizeX;
	short[][]	_aryPrecomputedSizeY;
    short[][]	_aryPrecomputedImgX;
    short[][]	_aryPrecomputedImgY;
	int[][]		_aryPrecomputedFlags;

    static int record_index = -1;
	static int record_frame = -1;

    static int _operation   = OPERATION_DRAW;


    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------
	private void MarkTransformedModules( boolean bUsedByFrames, int tr_flags)
	{
        if( DevConfig.sprite_useModuleUsageFromSprite &&
            DevConfig.sprite_useOperationMark )
        {

		    if( tr_flags != 0 )
            {
			    byte[] _modules_usage = this._modules_usage = new byte[ _nModules ]; // all are initialized with 0
			    int _nModules = this._nModules;
			    for( int i = 0; i < _nModules; i++)
				     _modules_usage[i] = (byte)tr_flags;
		    }

			int num_frames = GetFrameCount();

			if (num_frames == 0)
			{
				return;
			}

		    _operation = OPERATION_MARK;
            final Graphics g = null;

		    int num_anims  = 0;


		    if ((DevConfig.sprite_allowShortNumOfAFrames && (((_bs_flags & BS_NAF_1_BYTE) == 0))))

			{
				num_anims = (_anims_naf_short == null) ? 0 : _anims_naf_short.length;
			}
			else
			{
		    	num_anims = (_anims_naf_byte == null) ? 0 : _anims_naf_byte.length;
			}

            {
			    if (bUsedByFrames)
                {
			        for (int frame = 0; frame < num_frames; frame++)
				        PaintFrame( g,  frame, 0, 0, 0);
                }

			    for (int anim = 0; anim < num_anims; anim++)
			    {
				    int nfrms = GetAFrames(anim);

				    for (int frm = 0; frm < nfrms; frm++)
					    PaintAFrame( g,  anim, frm, 0, 0, 0);
			    }
            }

		    _operation = OPERATION_DRAW;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------

	private boolean RectOperation( int posX, int posY, int sizeX, int sizeY )
    {
        if( DevConfig.sprite_useOperationRect )
        {
		    if (_operation == OPERATION_COMPUTERECT)
		    {
			    if (posX < _rectX1)         _rectX1 = posX;
			    if (posY < _rectY1)         _rectY1 = posY;
			    if (posX + sizeX > _rectX2) _rectX2 = posX + sizeX;
			    if (posY + sizeY > _rectY2) _rectY2 = posY + sizeY;

			    return true;
		    }
        }

        return false;
    }

    //--------------------------------------------------------------------------------------------------------------------

	private boolean MarkOperation( int module, int flags )
    {
        if( DevConfig.sprite_useOperationMark )
        {
	        if (_operation == OPERATION_MARK)
	        {
		        _modules_usage[ module ] |= (1 << (flags & 0x07));
                return true;
	        }
        }

        return false;
    }

    //--------------------------------------------------------------------------------------------------------------------

	private boolean CheckOperation( Object img, int posX, int posY, int sizeX, int sizeY, int flg, int ImgX, int ImgY )
    {
		if(img == null)
		{
			Utils.Dbg("ERROR		: (!zJYLibConfig.sprite_useOperationRecord) || CheckOperation img is null");;
			return false;
		}

        if( DevConfig.sprite_useOperationRecord )
        {
            if( _operation == OPERATION_DRAW )
            {
                return true;
            }
			else if( _operation == OPERATION_RECORD )
            {
				int record_frame = this.record_frame;
				int record_index = this.record_index;
                _aryPrecomputedImages[record_frame][record_index] = img;
	            _aryPrecomputedX     [record_frame][record_index] = (short)posX;
	            _aryPrecomputedY     [record_frame][record_index] = (short)posY;
	            _aryPrecomputedSizeX [record_frame][record_index] = (short)sizeX;
	            _aryPrecomputedSizeY [record_frame][record_index] = (short)sizeY;
	            _aryPrecomputedFlags [record_frame][record_index] = flg;

                {
                    _aryPrecomputedImgX [record_frame][record_index] = (short)ImgX;
	                _aryPrecomputedImgY [record_frame][record_index] = (short)ImgY;
                }

	            record_index++;

                return false;
            }
			else
			{
                return false;
			}
        }
		else
        {
            return true;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Paints a pre-computed frame at x, y
	/// &param g The graphics context
	/// &param x The X coordinate to draw to
	/// &param y The Y coordinate to draw to
	/// &param frame The frame to draw
	//------------------------------------------------------------------------------
	void PaintPrecomputedFrame( int x, int y, int frame)
	{
		PaintPrecomputedFrame1(GLLib.g,x,y,frame);
	}
	void PaintPrecomputedFrame( Graphics g, int x, int y, int frame)
    {
        PaintPrecomputedFrame1( g,  x, y, frame);
    }
    //--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Precomputes all frames
	/// &param g The graphics context
	//------------------------------------------------------------------------------
	void PrecomputeAllFrames()
	{
		PrecomputeAllFrames(GLLib.g);
	}
	void PrecomputeAllFrames( Graphics g )
	{
		int numfr = GetFrameCount();

		for (int i = 0; i < numfr; i++)
        {
			PrecomputeFrame( g,  i, 0);
        }
	}

    //--------------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	/// Precompute a frame
	/// &param g The graphics context
	/// &param frame The frame to be precomputed
	/// &param flags The drawing flags to be used
	//------------------------------------------------------------------------------
	void PrecomputeFrame( Graphics g, int frame, int flags)
	{
        if( DevConfig.sprite_useOperationRecord )
        {
		    if (_aryPrecomputedImages == null)
		    {
			    int numfr               = GetFrameCount();

			    _aryPrecomputedImages   = new Object[numfr][];

			    _aryPrecomputedX        = new short[numfr][];
			    _aryPrecomputedY        = new short[numfr][];
			    _aryPrecomputedSizeX    = new short[numfr][];
			    _aryPrecomputedSizeY    = new short[numfr][];
			    _aryPrecomputedFlags    = new int  [numfr][];

                {
                    _aryPrecomputedImgX    = new short[numfr][];
			        _aryPrecomputedImgY    = new short[numfr][];
                }
		    }

		    int nmodules = CountFrameModules(frame);

		    _aryPrecomputedImages[frame] = new Object[nmodules];

		    _aryPrecomputedX[frame]     = new short[nmodules];
		    _aryPrecomputedY[frame]     = new short[nmodules];
		    _aryPrecomputedSizeX[frame] = new short[nmodules];
		    _aryPrecomputedSizeY[frame] = new short[nmodules];
		    _aryPrecomputedFlags[frame] = new int  [nmodules];

            {
                _aryPrecomputedImgX[frame] = new short[nmodules];
		        _aryPrecomputedImgY[frame] = new short[nmodules];
            }

		    record_frame = frame;
		    record_index = 0;
		    _operation   = OPERATION_RECORD;

		    PaintFrame( g,  frame, 0, 0, flags);

		    _operation   = OPERATION_DRAW;
		    record_frame = -1;
		    record_index = -1;
        }
	}

    //--------------------------------------------------------------------------------------------------------------------
	private void PaintPrecomputedFrame1(int x, int y, int frame)
	{
		PaintPrecomputedFrame1(GLLib.g,x,y,frame);
	}

	private void PaintPrecomputedFrame1( Graphics g, int x, int y, int frame)
	{
        if( DevConfig.sprite_useOperationRecord )
        {
        	Object[][] _aryPrecomputedImages = this._aryPrecomputedImages;
		    if (_aryPrecomputedImages == null ||
			    _aryPrecomputedImages[frame] == null)
		    {
			    PaintFrame( g,  frame, x, y, 0);
			    return;
		    }

		    Object[] _aryPrecomputedImages_frame = _aryPrecomputedImages[frame];
		    int len = _aryPrecomputedImages_frame.length;
		    short[] _aryPrecomputedX_frame = this._aryPrecomputedX[frame];
		    short[] _aryPrecomputedY_frame = this._aryPrecomputedY[frame];
		    short[] _aryPrecomputedSizeX_frame = this._aryPrecomputedSizeX[frame];
		    short[] _aryPrecomputedSizeY_frame = this._aryPrecomputedSizeY[frame];
		    int[] _aryPrecomputedFlags_frame = this._aryPrecomputedFlags[frame];
		    boolean _alpha = this._alpha;
		    boolean _multiAlpha = this._multiAlpha;
		    for (int i = 0; i < len; i++)
		    {
			    if( _aryPrecomputedImages_frame[ i ] == null)
			    {
				    g.setColor( 0xFF0000);
				    g.fillRect( _aryPrecomputedX_frame[i] + x, _aryPrecomputedY_frame[i] + y,
						        _aryPrecomputedSizeX_frame[i], _aryPrecomputedSizeY_frame[i]);
			    }
			    else
			    {
                    Object img = _aryPrecomputedImages_frame[i];

                    int x1     = _aryPrecomputedX_frame[i] + x;
                    int y1     = _aryPrecomputedY_frame[i] + y;
                    int w      = _aryPrecomputedSizeX_frame[i];
                    int h      = _aryPrecomputedSizeY_frame[i];
                    int flg    = _aryPrecomputedFlags_frame[i];
                    short[][] _aryPrecomputedImgX = this._aryPrecomputedImgX;
                    short[][] _aryPrecomputedImgY = this._aryPrecomputedImgY;

                        if (
							(DevConfig.sprite_useCacheRGBArrays) ||
							(DevConfig.sprite_useManualCacheRGBArrays && ((_flags & FLAG_USE_CACHE_RGB) != 0))
			   			   )
                        {
                                int img_x = _aryPrecomputedImgX[frame][i];
		                        int img_y = _aryPrecomputedImgY[frame][i];

                                int cx = g.getClipX();int cy = g.getClipY();int cw = g.getClipWidth();int ch = g.getClipHeight();int new_cx = x1;int new_cy = y1;int new_endcx = x1 + w;int new_endcy = y1 + h;if (x1 < cx)new_cx = cx;if (y1 < cy)new_cy = cy;if (new_endcx > cx+cw)new_endcx = cx+cw;if (new_endcy > cy+ch)new_endcy = cy+ch; g.setClip(new_cx, new_cy, new_endcx - new_cx, new_endcy - new_cy);;

						        
									// Use GLLib's wrapper call
									GLLib.DrawRGB(g, (int[])img, 0, w, x1 - img_x, y1 - img_y, w, h, _alpha, _multiAlpha, 0);
						        g.setClip(cx, cy, cw, ch);;
                        }
                        else
                        {
                                int img_x = _aryPrecomputedImgX[frame][i];
		                        int img_y = _aryPrecomputedImgY[frame][i];

                                if( flg == 0)
                                {
                                    int cx = g.getClipX();int cy = g.getClipY();int cw = g.getClipWidth();int ch = g.getClipHeight();int new_cx = x1;int new_cy = y1;int new_endcx = x1 + w;int new_endcy = y1 + h;if (x1 < cx)new_cx = cx;if (y1 < cy)new_cy = cy;if (new_endcx > cx+cw)new_endcx = cx+cw;if (new_endcy > cy+ch)new_endcy = cy+ch; g.setClip(new_cx, new_cy, new_endcx - new_cx, new_endcy - new_cy);;

							        g.drawImage( (Image) img, x1 - img_x, y1 - img_y, 0);

							        g.setClip(cx, cy, cw, ch);;
                                }
			                    else
                                {
                                    
										if (DevConfig.sprite_drawRegionFlippedBug)
										{
												Image image = Image.createImage( (Image) img, img_x, img_y, w, h, flg);
												g.drawImage(image, x1, y1, 0);
										}
										else
										{
											g.drawRegion( (Image) img, img_x, img_y, w, h, flg, x1, y1, 0);
										}
                                }
                        }
			    }
		    }
        }
    }

    //--------------------------------------------------------------------------------------------------------------------


    private int readByteArrayNi( byte[] file, int offset, byte[] data, int data_len, int module_off, int module_size)
    {
        for( int i = 0; i < data_len; i++)
        {
            data[ i*module_size + module_off ] = (file[offset++]);
        }
        return offset;
    }

    private int readArray2Short( byte[] file, int offset, short[] data, int data_off, int data_len, boolean bByte, boolean bUnsigned)
    {
        for( int i = 0; i < data_len; i++)
        {
            data[ i + data_off ] = bByte? (file[offset++]) : ((short)(((file[offset++]&0xFF) ) + ((file[offset++]&0xFF)<< 8)));
        }
        return offset;
    }


	//-------------------------------------------------------------------------------------------------
	// Structure below is used so we can write the code ONCE but have the function exist in both
	// int and short form.
	//-------------------------------------------------------------------------------------------------
	
	
// ASprite_Tansform.jpp
//
// Optimized Version (original code remains for reference but is commented out)
//
//--------------------------------------------------------------------------------------------------------------------


//------------------------------------------------------------------------------
/// Applies transformations to an RGB buffer
///
/// &param image_data RGB buffer to transform
/// &param sizeX width of the rgb data
/// &param sizeY height of the rgb data
/// &param flags Transformation flags
//------------------------------------------------------------------------------
final static int[] TransformRGB (int[] image_data, int sizeX, int sizeY, int flags)
{
	// no transformations?
	if((flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90)) == 0)
	{
		return image_data;
	}

	// Get a buffer that is NOT the same as the incoming buffer
	// IF the image_data is coming from a CACHE, then this will be the temp_int buffer
	// IF not, and this has been decoded on the-fly into the temp_int buffer, this will be the 2nd buffer (transform_int)
	int[] output = GetPixelBuffer_int(image_data, sizeX, sizeY);

	int pointer = 0;
	int offset  = 0;
	int i;
	int j;
	int kTmp;

	switch (flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90))
	{
		case FLAG_FLIP_X:

			pointer = sizeX * sizeY;
			offset  = sizeX * (sizeY - 1);

			for (j = sizeY; --j >=0;)
			{
				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset++];
				}

				offset -= (sizeX << 1);
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				offset = (j + 1) * sizeX;
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[--offset];
				}
			}
			*/
		break;

		case FLAG_FLIP_Y:

			pointer = (sizeY - 1) * sizeX;

			for (j = sizeY; --j >=0;)
			{
				System.arraycopy(image_data, offset, output, pointer, sizeX);
				pointer -= sizeX;
				offset  += sizeX;
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				System.arraycopy(image_data, (sizeY - j - 1) * sizeX, output, pointer, sizeX);
				pointer += sizeX;
			}
			*/
		break;

		case (FLAG_FLIP_X | FLAG_FLIP_Y):

			offset = sizeX * sizeY - 1;

			while (offset >= 0)
			{
				output[pointer++] = image_data[offset--];
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				offset = (sizeY - j) * sizeX;

				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[--offset];
				}
			}
			*/
		break;


		case FLAG_ROT_90:

			pointer = sizeX * sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = j;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset += sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[(sizeX - 1 - i) * sizeY + j];
				}
			}
			*/
		break;


		case (FLAG_FLIP_X | FLAG_ROT_90):

			pointer = sizeX * sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = (sizeY - 1 - j);

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset += sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[(sizeX - 1 - i) * sizeY + (sizeY - 1 - j)];
				}
			}*/
		break;


		case (FLAG_FLIP_Y | FLAG_ROT_90):

			pointer = sizeX * sizeY;
			kTmp = pointer - 1;

			for (j = sizeY; --j >=0;)
			{
				offset = kTmp--;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset -= sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[i * sizeY + j];
				}
			}
			*/
		break;


		case (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90):

			pointer = sizeX * sizeY;
			kTmp = pointer - sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = kTmp++;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset -= sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[i * sizeY + (sizeY - 1 - j)];
				}
			}
			*/
		break;
	}

	return output;
}
//主要供AVATAR系统使用
public final void SetImages(Image[] _imgs)
{
	if(DevConfig.sprite_useExternImage)
	{
		_main_image = _imgs; 
	}
}
final Image[] GetImages()
{
	if(DevConfig.sprite_useExternImage)
	{
		return _main_image;
	}
	else
	{
		return null;
	}
}
final Image GetImage(int idx)
{
	if(DevConfig.sprite_useExternImage)
	{
		Image[] _main_image = this._main_image;
		if (_main_image!=null && idx < _main_image.length)
			return _main_image[idx]; 
	}
	return null;
}
final void SetImage(int idx,Image _img)
{
	if(DevConfig.sprite_useExternImage)
	{
		Image[] _main_image;
		if((_main_image = this._main_image)==null)
			_main_image = this._main_image = new Image[DevConfig.DEFAULT_EX_IMG_NUM];
		
		if (idx < _main_image.length)
			_main_image[idx] = _img; 
	}
}
//以上主要供AVATAR系统使用

//------------------------------------------------------------------------------
/// Auxilary function: Alt version of GET_PIXEL_BUFFER to wrap zJYLibConfig flag.
///
/// GET_PIXEL_BUFFER(buffer) is used to aquire 1 of 2 main buffers. However, we have
/// a flag to prevent static allocation of the 2nd buffer for optimization. This
/// function allows us to have this varaible appear once instead of repeating this code.
///
/// &param buffer - The returned buffered will NOT equal this buffer.
/// &param w - how large this buffer should be (in case we will allocate on the fly)
/// &param h - how large this buffer should be (in case we will allocate on the fly)
///
/// &see zJYLibConfig.sprite_useDynamicTransformBuffer
//------------------------------------------------------------------------------
final static int[] GetPixelBuffer_int (int[] buffer, int w, int h)
{
	int[] output = null;

	if (DevConfig.sprite_useDynamicTransformBuffer)
	{
		// Attempt to grab the 1st main buffer (which ALWAYS exists)
		output = GetPixelBuffer_int(null);

		// Check to see if its already being used
		if (output == buffer)
		{
			// Already used!
			//!Tin Le Chanh: Triplet need optimized alot for heap size. Do this to get the smallest buffer.
			output = new int[w*h];
		}
	}
	else
	{
		// Use the normal version here...
		output = GetPixelBuffer_int(buffer);
	}

	return output;
}

	
	//-------------------------------------------------------------------------------------------------
	
	
// ASprite_Tansform.jpp
//
// Optimized Version (original code remains for reference but is commented out)
//
//--------------------------------------------------------------------------------------------------------------------


//------------------------------------------------------------------------------
/// Applies transformations to an RGB buffer
///
/// &param image_data RGB buffer to transform
/// &param sizeX width of the rgb data
/// &param sizeY height of the rgb data
/// &param flags Transformation flags
//------------------------------------------------------------------------------
final static short[] TransformRGB (short[] image_data, int sizeX, int sizeY, int flags)
{
	// no transformations?
	if((flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90)) == 0)
	{
		return image_data;
	}

	// Get a buffer that is NOT the same as the incoming buffer
	// IF the image_data is coming from a CACHE, then this will be the temp_int buffer
	// IF not, and this has been decoded on the-fly into the temp_int buffer, this will be the 2nd buffer (transform_int)
	short[] output = GetPixelBuffer_short(image_data, sizeX, sizeY);

	int pointer = 0;
	int offset  = 0;
	int i;
	int j;
	int kTmp;

	switch (flags & (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90))
	{
		case FLAG_FLIP_X:

			pointer = sizeX * sizeY;
			offset  = sizeX * (sizeY - 1);

			for (j = sizeY; --j >=0;)
			{
				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset++];
				}

				offset -= (sizeX << 1);
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				offset = (j + 1) * sizeX;
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[--offset];
				}
			}
			*/
		break;

		case FLAG_FLIP_Y:

			pointer = (sizeY - 1) * sizeX;

			for (j = sizeY; --j >=0;)
			{
				System.arraycopy(image_data, offset, output, pointer, sizeX);
				pointer -= sizeX;
				offset  += sizeX;
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				System.arraycopy(image_data, (sizeY - j - 1) * sizeX, output, pointer, sizeX);
				pointer += sizeX;
			}
			*/
		break;

		case (FLAG_FLIP_X | FLAG_FLIP_Y):

			offset = sizeX * sizeY - 1;

			while (offset >= 0)
			{
				output[pointer++] = image_data[offset--];
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				offset = (sizeY - j) * sizeX;

				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[--offset];
				}
			}
			*/
		break;


		case FLAG_ROT_90:

			pointer = sizeX * sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = j;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset += sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[(sizeX - 1 - i) * sizeY + j];
				}
			}
			*/
		break;


		case (FLAG_FLIP_X | FLAG_ROT_90):

			pointer = sizeX * sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = (sizeY - 1 - j);

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset += sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[(sizeX - 1 - i) * sizeY + (sizeY - 1 - j)];
				}
			}*/
		break;


		case (FLAG_FLIP_Y | FLAG_ROT_90):

			pointer = sizeX * sizeY;
			kTmp = pointer - 1;

			for (j = sizeY; --j >=0;)
			{
				offset = kTmp--;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset -= sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[i * sizeY + j];
				}
			}
			*/
		break;


		case (FLAG_FLIP_X | FLAG_FLIP_Y | FLAG_ROT_90):

			pointer = sizeX * sizeY;
			kTmp = pointer - sizeY;

			for (j = sizeY; --j >=0;)
			{
				offset = kTmp++;

				for (i = sizeX; --i >= 0;)
				{
					output[--pointer] = image_data[offset];
					offset -= sizeY;
				}
			}

			/*
			for (j = 0; j < sizeY; j++)
			{
				for (i = 0; i < sizeX; i++)
				{
					output[pointer++] = image_data[i * sizeY + (sizeY - 1 - j)];
				}
			}
			*/
		break;
	}

	return output;
}

//------------------------------------------------------------------------------
/// Auxilary function: Alt version of GET_PIXEL_BUFFER to wrap zJYLibConfig flag.
///
/// GET_PIXEL_BUFFER(buffer) is used to aquire 1 of 2 main buffers. However, we have
/// a flag to prevent static allocation of the 2nd buffer for optimization. This
/// function allows us to have this varaible appear once instead of repeating this code.
///
/// &param buffer - The returned buffered will NOT equal this buffer.
/// &param w - how large this buffer should be (in case we will allocate on the fly)
/// &param h - how large this buffer should be (in case we will allocate on the fly)
///
/// &see zJYLibConfig.sprite_useDynamicTransformBuffer
//------------------------------------------------------------------------------
final static short[] GetPixelBuffer_short (short[] buffer, int w, int h)
{
	short[] output = null;

	if (DevConfig.sprite_useDynamicTransformBuffer)
	{
		// Attempt to grab the 1st main buffer (which ALWAYS exists)
		output = GetPixelBuffer_short(null);

		// Check to see if its already being used
		if (output == buffer)
		{
			// Already used!
			//!Tin Le Chanh: Triplet need optimized alot for heap size. Do this to get the smallest buffer.
			output = new short[w*h];
		}
	}
	else
	{
		// Use the normal version here...
		output = GetPixelBuffer_short(buffer);
	}

	return output;
}

	
	//-------------------------------------------------------------------------------------------------


// ASprite_Variables.jpp
//--------------------------------------------------------------------------------------------------------------------

	//USE_CREATE_RGB
	static int 		temp_int[];

	//USE_NOKIA_UI
	static short 	temp_short[];

	//ELSE
	static byte 	temp_byte[];

	//RGBArraysUseDrawRGB transformRGB's cache
	static int		transform_int[];
	static short	transform_short[];


	//////////////////////////////////////////////////
	//记录在游戏中的逻辑ID值。用于SP的animReplacer，替换动画ID使用
	int		m_sprID = -1;//魔龙和法师的代码，To delete
	
	// Modules...
	int			_nModules;					// number of modules
	byte[]		_modules_img_index;			// 每个Module所使用的Image Index  for each module [BS_MODULES_IMG]

	byte[]		_modules_x_byte;			// x  for each module [BS_MODULES_XY] only if (! zJYLibConfig.sprite_useModuleXYShort)
	byte[]		_modules_y_byte;			// y  for each module [BS_MODULES_XY] only if (! zJYLibConfig.sprite_useModuleXYShort)
	short[]		_modules_x_short;			// x  for each module [BS_MODULES_XY] only if (zJYLibConfig.sprite_useModuleXYShort)
	short[]		_modules_y_short;			// y  for each module [BS_MODULES_XY] only if (zJYLibConfig.sprite_useModuleXYShort)
    short[]     _modules_w_scaled;
	short[]     _modules_h_scaled;

    short[]		_modules_w_short;			// width for each module only if (zJYLibConfig.sprite_useModuleWHShort)
	short[]		_modules_h_short;			// height for each module only if (zJYLibConfig.sprite_useModuleWHShort)
	byte[]		_modules_w_byte;			// width for each module only if (! zJYLibConfig.sprite_useModuleWHShort)
	byte[]		_modules_h_byte;			// height for each module only if (! zJYLibConfig.sprite_useModuleWHShort)

    short[]     _modules_extra_info;        // start angle and angle for each arc module ... could be used for other things later
    short[]     _modules_extra_pointer;     // arc info indirection, to keep the wasted memory to

	// Frames...
	byte[]		_frames_nfm_byte;			// number of FModules (max 256   FModules/Frame)
	short[]     _frames_nfm_short;          // number of FModules (max 65536 FModules/Frame)
	short[]		_frames_fm_start;			// index of the first FModule

	byte[]		_frames_rc;					// frame bound rect (x y width height) Only if (! zJYLibConfig.BS_FM_OFF_SHORT)
	short[]		_frames_rc_short;   		// frame bound rect (x y width height) Only if (zJYLibConfig.BS_FM_OFF_SHORT)

	byte[]		_frames_col;				// collision rect for each frame (x y width height) Only if (! zJYLibConfig.BS_FM_OFF_SHORT)
	short[]		_frames_col_short;			// collision rect for each frame (x y width height) Only if (zJYLibConfig.BS_FM_OFF_SHORT)

	byte[]		_frames_rects;				// rects for each frame (x y width height). Only if (! zJYLibConfig.BS_FM_OFF_SHORT)
	short[]		_frames_rects_short;		// rects for each frame (x y width height). Only if (zJYLibConfig.BS_FM_OFF_SHORT)
	short[]		_frames_rects_start;		// index of the first rect

	// FModules...
	byte[]		_fmodules;					// 4 bytes for each FModule // only if (zJYLibConfig.sprite_useSingleArrayForFMAF)
											// 0 : module index
											// 1 : ox
											// 2 : oy
											// 3 : flags

	byte[]		_fmodules_id_byte;			// fmodule index 			// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (!sprite_forceShortIndexForFModules)

	short[]     _fmodules_id_short;         // fmodule index 			// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (sprite_forceShortIndexForFModules || sprite_allowShortIndexForFModules)

	byte[]		_fmodules_flags;			// fmodule flags			// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF)

	short[]		_fmodules_ox_short;			// fmodule ox				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (zJYLibConfig.sprite_useFMOffShort)
	short[]		_fmodules_oy_short;			// fmodule oy				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (zJYLibConfig.sprite_useFMOffShort)
	byte[]		_fmodules_ox_byte;			// fmodule ox				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (!zJYLibConfig.sprite_useFMOffShort)
	byte[]		_fmodules_oy_byte;			// fmodule oy				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (!zJYLibConfig.sprite_useFMOffShort)

	byte[]		_fmodules_pal;				// fmodule palette			// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (zJYLibConfig.sprite_useFMPalette)

	// Anims...
	byte[]		_anims_naf_byte;			// number of AFrames (max 256   AFrames/Anim)
	short[]		_anims_naf_short;			// number of AFrames (max 65536 AFrames/Anim)
	short[]		_anims_af_start;			// index of the first AFrame

	// AFrames...
	byte[]		_aframes;					// 5 bytes for each AFrame 	// only if (zJYLibConfig.sprite_useSingleArrayForFMAF)
											// 0 : frame index
											// 1 : time
											// 2 : ox
											// 3 : oy
											// 4 : flags

	byte[]		_aframes_frame_byte;		// frame index				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (!sprite_forceShortIndexForAFrames)
	short[]		_aframes_frame_short;		// frame index				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (sprite_forceShortIndexForAFrames || sprite_allowShortIndexForAFrames)
	byte[]		_aframes_time;				// aframe time				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF)

	public short[] 	_aframes_ox_short;			// aframe ox				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (zJYLibConfig.sprite_useAfOffShort)
	public short[] 	_aframes_oy_short;			// aframe oy				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (zJYLibConfig.sprite_useAfOffShort)
	byte[] 		_aframes_ox_byte;			// aframe ox				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (! zJYLibConfig.sprite_useAfOffShort)
	byte[] 		_aframes_oy_byte;			// aframe oy				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF) && (! zJYLibConfig.sprite_useAfOffShort)

	byte[]		_aframes_flags;				// aframe flags				// only if (! zJYLibConfig.sprite_useSingleArrayForFMAF)


	// Palettes...

    
	    int[]		_w_pos;					// width and height position in GIF header
	    int[]		_header_size;			// GIF header size
	    byte[][]	_gifHeader;				// actual GIF header


    byte []		_modules_data;				// encoded image data for all modules                    ONLY IF (!zJYLibConfig.sprite_useDoubleArrayForModuleData)
    short[]		_modules_data_off_short;	// offset for the image data of each module              ONLY IF (!zJYLibConfig.sprite_useDoubleArrayForModuleData)
    int  []		_modules_data_off_int;      //                                                       ONLY IF (!zJYLibConfig.sprite_useDoubleArrayForModuleData)

	byte [][]   _modules_data_array;        // encoded image data for all modules stored per module. ONLY IF (zJYLibConfig.sprite_useDoubleArrayForModuleData)

//	short 		_pixel_format;				// always converted to 8888
	int			_bs_flags;					// sprite format

	//USE_NOKIA_UI
	short[][]	_pal_short; 				// all palettes
	//ELSE
	int[][]		_pal_int; 					// all palettes


	byte[][]	_transp;					// all transparences (A)
	byte[]		_pal_data; 					// palettes data for BS_DEFAULT_MIDP1c
	int			_palettes;					// number of palettes
	int			_colors;					// number of colors
	private int _crt_pal;					// current palette
	boolean		_alpha;						// has transparency ?
	boolean     _multiAlpha;                // has alpha value not equal to 0 or 255

	int         _flags;                     // generic flags

	// Graphics data (for each module)...
	Image[]		_main_image;				// Sprite的Module所在Image.比如一个Sprite对应多个Image的时候	
	short 		_data_format;
	int			_i64rle_color_mask;			// used by ENCODE_FORMAT_I64RLE
	int			_i64rle_color_bits;			// used by ENCODE_FORMAT_I64RLE

	boolean 	_bTraceNow;


    static Graphics 	_graphics;

	// Alternate Graphics Context to allow us to render directly to a pixel buffer
	static int[]	_customGraphics;
	static int		_customGraphicsWidth;
	static int      _customGraphicsHeight;
	static int      _customGraphicsAlpha;
	static int      _customGraphicsClipX;
	static int      _customGraphicsClipY;
	static int      _customGraphicsClipW;
	static int      _customGraphicsClipH;

	// CACHE
	short[][][]	_modules_image_shortAAA;	//USE_NOKIA_UI
	Image[][][] _module_image_imageAAA; 	// if ( (!zJYLibConfig.sprite_useLoadImageWithoutTransf) && ( (zJYLibConfig.sprite_useTransfRot) || (zJYLibConfig.sprite_useTransfFlip)))
	int[][][] 	_module_image_intAAA;		//USE_CACHE_RGB_ARRAYS
	Image[][] 	_module_image_imageAA;		// else

	int[][][]   _frame_image_intAAA;
	Image[][]   _frame_image_imageAA;
	static boolean s_frame_image_bHasAlpha;



	// CRC data for creating a valid PNG stream...
	int[]       _PNG_packed_PLTE_CRC;		// for each palette
	int[]       _PNG_packed_tRNS_CRC;		// for each palette
	int[]       _PNG_packed_IHDR_CRC;		// for each module
	int[]       _PNG_packed_IDAT_ADLER;		// for each module
	int[]       _PNG_packed_IDAT_CRC;		// for each module


	static int	_images_count;
	static int	_images_size;

	static int	mem = 0;

//--------------------------------------------------------------------------------------------------------------------


	final static int[] midp2_flags = 	//ony if (zJYLibConfig.sprite_useCreateRGB || zJYLibConfig.sprite_useLoadImageWithoutTransf)
	{									// R Y X
		javax.microedition.lcdui.game.Sprite.TRANS_NONE,			// 0 0 0
		javax.microedition.lcdui.game.Sprite.TRANS_MIRROR,			// 0 0 1
		javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180,	// 0 1 0
		javax.microedition.lcdui.game.Sprite.TRANS_ROT180,			// 0 1 1
		javax.microedition.lcdui.game.Sprite.TRANS_ROT90,			// 1 0 0
		javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT90,	// 1 0 1
		javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT270,	// 1 1 0
		javax.microedition.lcdui.game.Sprite.TRANS_ROT270,			// 1 1 1
	};


	final static int[] TRANSFORM_FLIP_X = {1, 0, 3, 2, 6, 7, 4, 5};
	final static int[] TRANSFORM_FLIP_Y = {2, 3, 0, 1, 5, 4, 7, 6};
	final static int[] TRANSFORM_ROT_90 = {4, 5, 6, 7, 3, 2, 1, 0};


byte[]				_module_types;		    // MD_IMAGE, MD_RECT, MD_FILL_RECT
byte[] 	            _module_colors_byte;	// colors list used for the modules of MD_RECT, MD_FILL_RECT types
int[] 	            _module_colors_int;		// colors list used for the modules of MD_RECT, MD_FILL_RECT types
byte[] 				_modules_usage;

static int          s_resizeType; // RESIZE_NONE
static int			_rectX1;
static int			_rectY1;
static int			_rectX2;
static int			_rectY2;


static int[]        s_rc = new int[4];

/// Used to allow manual disabling of GC. Only makes sense is zJYLibConfig.sprite_useDeactivateSystemGc is FALSE.
static boolean      s_gcEnabled = true;

/// Used to allow turning off of the module painting. Just for debug for performance testing.
static boolean      s_debugSkipPaintModule = false;

/// Used for zJYLibConfig.sprite_allowModuleMarkerMasking, will hold the mask that determines which modules get painted.
static int          s_moduleMask;

/// Used for zJYLibConfig.sprite_useSingleFModuleCache, will store the pal and module the current int buffer holds.
static int          s_moduleBufferState;

// Constants for above var
final static int    MODULE_STATE_MODULE_ID_MASK		= 0x0000FFFF;
final static int    MODULE_STATE_PALETTE_MASK		= 0x0FFF0000;
final static int    MODULE_STATE_PALETTE_SHIFT		= 16;
final static int    MODULE_STATE_DISABLE_MASK       = 0x80000000;
final static int    MODULE_STATE_INIT_MASK          = (MODULE_STATE_MODULE_ID_MASK | MODULE_STATE_PALETTE_MASK);


//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Empty constructor. Does nothing.
	//--------------------------------------------------------------------------------------------------------------------
	public zSprite()
	{

	}


//-----------------------------------------------------------------------------
/// Used to assert that GLLibConfiguration and Sprite Export Flags do not have
/// any conflicts.
///
/// Called during sprite loading for each sprite as soon as flags are read.
/// Only used in DEBUG.
///
/// &param flag - A sprite flags
//-----------------------------------------------------------------------------
private final static void AssertFlags (int flags)
{
	if (DevConfig.sprite_useBSpriteFlags)
	{
		if(!(DevConfig.sprite_useModuleXY || ((flags & BS_MODULES_XY) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_MODULES_XY but sprite_useModuleXY is FALSE!");;

		if(DevConfig.sprite_useModuleXY && DevConfig.sprite_useModuleXYShort)Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite_useModuleXYShort and sprite_useModuleXY can't be TRUE at the same time!");;

		if(!(DevConfig.sprite_useModuleWHShort || ((flags & BS_MODULES_WH_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_MODULES_WH_SHORT but sprite_useModuleWHShort is FALSE!");;

		if(!(DevConfig.sprite_useModuleXYShort || ((flags & BS_MODULES_XY_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_MODULES_XY_SHORT but sprite_useModuleXYShort is FALSE!");;

		if(!(DevConfig.sprite_useModuleUsageFromSprite || ((flags & BS_MODULES_USAGE) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_MODULES_USAGE but sprite_useModuleUsageFromSprite is FALSE!");;

		if(!(DevConfig.sprite_useFMOffShort || ((flags & BS_FM_OFF_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_FM_OFF_SHORT but sprite_useFMOffShort is FALSE!");;


		if(!(DevConfig.sprite_useFrameCollRC || ((flags & BS_FRAME_COLL_RC) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_FRAME_COLL_RC but sprite_useFrameCollRC is FALSE!");;


		if(!(DevConfig.sprite_useFMPalette || ((flags & BS_FM_PALETTE) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_FM_PALETTE but sprite_useFMPalette is FALSE!");;


		if(!(DevConfig.sprite_useFrameRects || ((flags & BS_FRAME_RECTS) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_FRAME_RECTS but sprite_useFrameRects is FALSE!");;


		if(!(DevConfig.sprite_useAfOffShort || ((flags & BS_AF_OFF_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: Using BS_AF_OFF_SHORT but sprite_useAfOffShort is FALSE!");;


	    if(!(!DevConfig.sprite_alwaysBsSkipFrameRc || ((flags & BS_SKIP_FRAME_RC) != 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite_alwaysBsSkipFrameRc is TRUE but this sprite does not have BS_SKIP_FRAME_RC set!");;

		//-----------------------------------------------------------------------------------------
		/// FModule Index
		//-----------------------------------------------------------------------------------------

		if(!(DevConfig.sprite_allowShortIndexForFModules || ((flags & BS_FM_INDEX_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite has BS_FM_INDEX_SHORT flag set but this is not enabled, please set sprite_forceShortIndexForFModules or sprite_allowShortIndexForFModules to TRUE");;


		if(!(!DevConfig.sprite_allowShortIndexForFModules || DevConfig.sprite_useBSpriteFlags))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: configuration does not support bsprite flags but you are using sprite_allowShortIndexForFModules! Either enable bsprite flags or use sprite_forceShortIndexForFModules instead.");;


		//-----------------------------------------------------------------------------------------
		/// FModule Count
		//-----------------------------------------------------------------------------------------


		if(!(DevConfig.sprite_allowShortNumOfFModules || ((flags & BS_NFM_1_BYTE) != 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite has BS_NFM_1_BYTE flag set but this is enabled, please set sprite_forceShortNumOfFModules or sprite_allowShortNumOfFModules to FALSE");;


		if(!(!DevConfig.sprite_allowShortNumOfFModules || DevConfig.sprite_useBSpriteFlags))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: configuration does not support bsprite flags but you are using sprite_allowShortNumOfFModules! Either enable bsprite flags or use sprite_forceShortNumOfFModules instead.");;


		//-----------------------------------------------------------------------------------------
		/// AFrame Index
		//-----------------------------------------------------------------------------------------

		if(!(DevConfig.sprite_allowShortIndexForAFrames || ((flags & BS_AF_INDEX_SHORT) == 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite has BS_AF_INDEX_SHORT flag set but this is not enabled, please set sprite_forceShortIndexForAFrames or sprite_allowShortIndexForAFrames to TRUE");;


		if(!(!DevConfig.sprite_allowShortIndexForAFrames || DevConfig.sprite_useBSpriteFlags))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: configuration does not support bsprite flags but you are using sprite_allowShortIndexForAFrames! Either enable bsprite flags or use sprite_forceShortIndexForAFrames instead.");;


		//-----------------------------------------------------------------------------------------
		/// AFrame Count
		//-----------------------------------------------------------------------------------------


		if(!(DevConfig.sprite_allowShortNumOfAFrames || ((flags & BS_NAF_1_BYTE) != 0)))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: sprite has BS_NAF_1_BYTE flag set but this is enabled, please set sprite_forceShortNumOfAFrames or sprite_allowShortNumOfAFrames to FALSE");;


		if(!(!DevConfig.sprite_allowShortNumOfAFrames || DevConfig.sprite_useBSpriteFlags))Utils.DBG_PrintStackTrace(false, "Sprite Flag Error: configuration does not support bsprite flags but you are using sprite_allowShortNumOfAFrames! Either enable bsprite flags or use sprite_forceShortNumOfAFrames instead.");;

	}
}

/**用于确定是否从player绘制还是直接用sprite绘制*/
public zAnimPlayer _player = null;

}

//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------

/// &}
