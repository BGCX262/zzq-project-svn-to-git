package cn.thirdgwin.lib;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TileLayer. 作为场景的一个层而存在。一个场景由多个层构成如下图：
 * +----+-------------------+
 * | A  |     LayerB        |
 * |----+-------------------+
 * |    |                   |    
 * |    |                   |
 * | C  |     LayerD        |
 * |    |                   |
 * |    |                   |
 * +----+-------------------+
 * 同一个场景的所有层可以用imagelayer，也可以使用tiledlayer。
 * TODO: 目前的imageLayer只有LayerB和LayerD可用
 *       tiledLayer只有LayerD可用。
 *       需要实现imagelayer的A，c和tiledLayer的A，B，C * 
 * 
 * 现在的逻辑大概是：
 * 1. 场景（即关卡)是由多个Layer构成。 各个layer可以任意位置放置（ini中可配置）。每个Layer可以用图片实现(imagedLayer)，也可以用原始的tileset实现(tiledLayer)。
 * 2. 每个Layer可以由多个tileset构成。例如地面层可以分ground和cover两层。 他们的tilesize，layer坐标，layer尺寸都是一样的。
 * 3. 所有场景中的object都在场景中活动,他们的坐标都是相对场景的（这里就可以避免很多LANDSCAPE的特殊处理）。他们只能看到场景信息(场景宽高，场景tilesize，某坐标是否在物理层中)
 * 4. 场景宽高由如下几种方式配置：
 * 	a. imagedLayer使用INT字段中的场景宽高进行配置
 * 	b. tiledLayer使用a方式进行配置；或者使用物理层宽高作为场景宽高；
 * 5. 目前zTileLayer中的Camera只能使用WRAP_NONE标志。
 * 
 * @author MaNing
 * 只包括了Tiled Layer的绘制和更新部分
 */
public class zTileLayer {

	/// wrappng parameter, specify that the tileset is to be repeated only once
	public final static int WRAP_CLAMP	= 0;
	/// wrappng parameter, specify that the tileset is to be repeated indefinitely
	public final static int WRAP_REPEAT	= 1;
	/// wrappng parameter, specify that the tileset is to be repeated only once but the camera won't be limited
	public final static int WRAP_NONE	= 2;

	// Constants for Tileset Effects
	public final static int TILESET_EFFECT_NONE                 = 0;
	public final static int TILESET_EFFECT_NORMAL               = 1;
	public final static int TILESET_EFFECT_ADDITIVE             = 2;
	public final static int TILESET_EFFECT_MULTIPLICATIVE       = 3;


	private boolean 		s_bTilesetPlayerInitialized 	= false;
	private int  		s_TilesetMaxLayerCount;

	private int          s_TilesetEffectLayer            = -1;
	private int          s_TilesetAlphaLayer             = -1;
	private int          s_TilesetEffectType             = 0;

	private final static int 	k_TilesetInfoDestWidth			= 0;
	private final static int 	k_TilesetInfoDestHeight			= k_TilesetInfoDestWidth+1;
	private final static int 	k_TilesetInfoTileWidth			= k_TilesetInfoDestHeight+1;
	private final static int 	k_TilesetInfoTileWidthShift		= k_TilesetInfoTileWidth+1;
	private final static int 	k_TilesetInfoTileWidthMask		= k_TilesetInfoTileWidthShift+1;
	private final static int 	k_TilesetInfoTileHeight			= k_TilesetInfoTileWidthMask+1;
	private final static int 	k_TilesetInfoTileHeightShift	= k_TilesetInfoTileHeight+1;
	private final static int 	k_TilesetInfoTileHeightMask		= k_TilesetInfoTileHeightShift+1;
	private final static int 	k_TilesetInfoLayerX 			= k_TilesetInfoTileHeightMask+1;
	private final static int 	k_TilesetInfoLayerY 			= k_TilesetInfoLayerX+1;
	private final static int 	k_TilesetInfoCOUNT				= k_TilesetInfoLayerY+1;

	private final static int 	k_TilesetLayerInitialized		= 0;
	private final static int 	k_TilesetLayerEnabled			= k_TilesetLayerInitialized+1;
	private final static int 	k_TilesetLayerTileCountWidth	= k_TilesetLayerEnabled+1;
	private final static int 	k_TilesetLayerTileCountHeight	= k_TilesetLayerTileCountWidth+1;
	private final static int 	k_TilesetLayerWidth				= k_TilesetLayerTileCountHeight+1;
	private final static int 	k_TilesetLayerHeight			= k_TilesetLayerWidth+1;
	private final static int 	k_TilesetLayerCBID				= k_TilesetLayerHeight+1;
	private final static int 	k_TilesetLayerCBWidth			= k_TilesetLayerCBID+1;
	private final static int 	k_TilesetLayerCBHeight			= k_TilesetLayerCBWidth+1;
	private final static int 	k_TilesetLayerFirstTileX		= k_TilesetLayerCBHeight+1;
	private final static int 	k_TilesetLayerFirstTileY		= k_TilesetLayerFirstTileX+1;
	private final static int 	k_TilesetLayerLastTileX			= k_TilesetLayerFirstTileY+1;
	private final static int 	k_TilesetLayerLastTileY			= k_TilesetLayerLastTileX+1;
	private final static int 	k_TilesetLayerCamX				= k_TilesetLayerLastTileY+1;
	private final static int 	k_TilesetLayerCamY				= k_TilesetLayerCamX+1;
	private final static int 	k_TilesetLayerFlag				= k_TilesetLayerCamY+1;
	private final static int 	k_TilesetLayerCOUNT				= k_TilesetLayerFlag+1;

//private final static int 	k_TilesetLayerUseCB				= k_TilesetLayerHeight+1;

	private final static int 	k_TilesetLayerImageCB			= 0;
	private final static int 	k_TilesetLayerImageCOUNT		= k_TilesetLayerImageCB+1;

	private final static int 	k_TilesetLayerGraphicsCB		= 0;
	private final static int 	k_TilesetLayerGraphicsCOUNT		= k_TilesetLayerGraphicsCB+1;

	private final static int 	k_TilesetLayerDataMap			= 0;
	private final static int 	k_TilesetLayerDataFlip			= k_TilesetLayerDataMap+1;
	private final static int 	k_TilesetLayerDataCOUNT			= k_TilesetLayerDataFlip+1;

	private final static int 	k_TilesetLayerAreaInfoX   		= 0;
	private final static int 	k_TilesetLayerAreaInfoY   		= k_TilesetLayerAreaInfoX + 1;
	private final static int 	k_TilesetLayerAreaInfoW   		= k_TilesetLayerAreaInfoY + 1;
	private final static int 	k_TilesetLayerAreaInfoH   		= k_TilesetLayerAreaInfoW + 1;
	private final static int 	k_TilesetLayerAreaInfoCOUNT		= k_TilesetLayerAreaInfoH + 1;

	private final static int    k_TilesetLayerAreaCount         = 6;
	
	/*距离代表的是一个相对比例（百分比）。 
	 * 当distance>0，则该层比物理坐标移动更快。当distance<0，则该层比物理坐标移动更慢
	 * 当distance==0，则该层和物理坐标一起移动。
	 * 例如distance==100，则该层移动是物理坐标的两倍，如果distance==-50，则该层移动是物理坐标的一半
	 * 如果distance==-100，则该层不移动
	 * 公式： x = ((100+distance) * x /100) */
	private int[] 		s_TilesetDistanceX;
	private int[] 		s_TilesetDistanceY;

	private int[]		s_TilesetInfo;
	private int[][]		s_TilesetLayerInfo;
	private byte[][][]	s_TilesetLayerData;
	private Image[][]	s_TilesetLayerImage;
	private Graphics[][]	s_TilesetLayerGraphics;

	// Used only if tileset_useLayerLastUpdatedArea is TRUE
	private int[][][]    s_TilesetLayerLastUpdatedArea;
	private int          s_TilesetLayerLastUpdatedAreaIndex;

	private zSprite[]	s_TilesetSprite;

	private final static int 	k_Flag_WrappingX 				= 0x01;
	private final static int 	k_Flag_WrappingY 				= 0x02;
	private final static int 	k_Flag_UseCB     				= 0x04;
	private final static int 	k_Flag_Origin    				= 0x08;
	private final static int 	k_Flag_LimitCamX 				= 0x10;
	private final static int 	k_Flag_LimitCamY 				= 0x20;
	private final static int    k_Flag_CreatedCB                = 0x40;
	private final static int    k_Flag_FillColorInTileBG        = 0x80;//在绘制tile之前先绘制一层底色


	final public int Tileset_GetTileW()
	{
		return s_TilesetInfo[k_TilesetInfoTileWidth];
	}
	
	final public int Tileset_GetTileH()
	{
		return s_TilesetInfo[k_TilesetInfoTileHeight];
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//image layer的特有成员变量
	
	//使用imagelayer的时候使用此值作为layer的宽高
	private int			s_LayerWidth;
	private int			s_LayerHeight;
	
	private boolean 	s_bUsingImageLayer;
	
	//TODO:setCamera+绘制
	
	public void Tileset_SetLayerWidthHeight(int w, int h) 
	{
		s_LayerWidth = w;
		s_LayerHeight = h;	
	}
	
	public void Tileset_SetUseImageLayer(boolean useImgLayer) 
	{
		s_bUsingImageLayer = useImgLayer;
	}
	
	public void Tileset_InitImageLayer(int layerCount, int layerW, int layerH, int cameraW, int cameraH)
	{
		Tileset_SetUseImageLayer(true);
	
		s_TilesetMaxLayerCount = layerCount;
		
		s_TilesetLayerImage 		= new Image[s_TilesetMaxLayerCount][k_TilesetLayerImageCOUNT];
		s_TilesetDistanceX 			= new int[s_TilesetMaxLayerCount];
		s_TilesetDistanceY 			= new int[s_TilesetMaxLayerCount];
		s_TilesetLayerInfo			= new int[s_TilesetMaxLayerCount][];
		s_TilesetInfo 				= new int[k_TilesetInfoCOUNT];
		
		Tileset_SetLayerWidthHeight(layerW, layerH);
		
		s_TilesetInfo[k_TilesetInfoDestWidth]			= cameraW;
		s_TilesetInfo[k_TilesetInfoDestHeight]			= cameraH;
		
		s_bTilesetPlayerInitialized = true;
	}
	
	public void Tileset_LoadImageLayer(int nLayer, Image img, int distanceX, int distanceY) 
	{
		//Guijun
		int[] layerdata;
		if (!s_bTilesetPlayerInitialized) 
			return;
		
		s_TilesetLayerImage[nLayer][k_TilesetLayerImageCB] = img;
		Tileset_SetLayerDistance(nLayer, distanceX, distanceY);
		//Guijun
		layerdata = s_TilesetLayerInfo[nLayer] 		= new int[k_TilesetLayerCOUNT];
		
		layerdata[k_TilesetLayerWidth] 		= s_LayerWidth;
		layerdata[k_TilesetLayerHeight] 		= s_LayerHeight;
		layerdata[k_TilesetLayerCamX]			= 0;
		layerdata[k_TilesetLayerCamY]			= 0;
		
		layerdata[k_TilesetLayerInitialized] 		= 1;
		layerdata[k_TilesetLayerEnabled] 			= 1;
	}
	
	private void Tileset_DrawImageLayer(Graphics g, int dx, int dy, int nLayer) {
		if(!s_bTilesetPlayerInitialized)
			return;
		
		Image tileImg = s_TilesetLayerImage[nLayer][k_TilesetLayerImageCB];
		if (tileImg == null || g == null)
			return;
		
		//record the clip rect
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipHeight();
		//Guijun
		int[] s_TilesetInfo = this.s_TilesetInfo;
		
		int layerX = s_TilesetInfo[k_TilesetInfoLayerX];
		int layerY = s_TilesetInfo[k_TilesetInfoLayerY];
		int sceneW = layerX + s_LayerWidth;
		int sceneH = layerY + s_LayerHeight;
		//Guijun
		int [] xy = Tileset_GetCameraXY(nLayer);
		int cameraX =xy[0];
		int cameraY =xy[1];
		int cameraW = s_TilesetInfo[k_TilesetInfoDestWidth];
		int cameraH =s_TilesetInfo[k_TilesetInfoDestHeight];
		
		int srcX, srcY, srcW, srcH;
		int destX, destY, destW, destH;
		
		if (cameraX < layerX) {
			srcX = layerX;			
			srcW = cameraW - (layerX - cameraX);
			destX = layerX - cameraX;
			destW = srcW;
		} else {
			srcX = cameraX;			
			if (cameraX + cameraW > sceneW) {
				srcW = sceneW - cameraX;
			} else {
				srcW = cameraW;
			}
			destX = 0;
			destW = srcW;
		}
		if (cameraY < layerY) {
			srcY = layerY;
			srcH = cameraH - (layerY - cameraY);
			destY = layerY - cameraY;
			destH = srcH;
		} else {
			srcY = cameraY;
			if (cameraY + cameraH > sceneH) {
				srcH = sceneH - cameraY;
			} else {
				srcH = cameraH;
			}
			destY = 0;
			destH = srcH;
		}
		
		if (srcW <= 0 || srcH <= 0) {
			//绘画区域小于0,不用绘制
			return;
		}
		
		int tileW = tileImg.getWidth();
		int tileH = tileImg.getHeight();
		
		int relatedSX = (srcX - layerX);
		int relatedSY = (srcY - layerY);
		
		int startTileX = (relatedSX) / tileW;
		int startTileY = (relatedSY) / tileH;
		int toDrawTileCountX = (relatedSX + srcW - 1) / tileW - startTileX + 1;
		int toDrawTileCountY = (relatedSY + srcH - 1) / tileH - startTileY + 1;
		
		int x;
		int y = destY - ((srcY - layerY) % tileH);
		int w = tileW;
		int h = tileH;
		for (int i = 0; i < toDrawTileCountY; i++) {
			x = destX - ((srcX - layerX) % tileW);
			for (int j = 0; j < toDrawTileCountX; j++) {
				g.setClip(x, y, w, h);				
//				g.drawRegion(tileImg, 0, 0, w, h, 0, x, y, Graphics.TOP|Graphics.LEFT);
				g.drawImage(tileImg, x, y, Graphics.TOP|Graphics.LEFT);
				
				x += w;
			}
			y += h;
		}
		
		g.setClip(clipX, clipY, clipW, clipH);//revert the clip rect
		
//		for (int i = 0; i < toDrawTileCountY; i++) {
//			int y = srcY % tileH;
//			int h = tileH - y;
//			for (int j = 0; j < toDrawTileCountX; i++) {
//				int x = srcX % tileW;
//				int w = tileW - x;
//				
//				g.drawRegion(tileImg, x, y, w, h, 0, destX, destY, Graphics.TOP|Graphics.LEFT);
//				
//				destX += w;
//				srcX += w;
//			}
//			destY += h;
//			srcY += h;
//		}
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------
	/// Initialize the zJYLibPlayerTileSet engine. You cannot use tileset before you call this function.
	/// &param nDestWidth Destination Width in pixel; Usually Screen Width.
	/// &param nDestHeight Destination Height in pixel; Usually Screen Height.
	/// &param nTileWidth tile widh
	/// &param nTileHeight tile height

	//--------------------------------------------------------------------------------------------------------------------
	public void Tileset_Init(int layerCount, int nDestWidth, int nDestHeight, int nTileWidth, int nTileHeight)
	{
		s_TilesetMaxLayerCount = layerCount;
		
		s_TilesetInfo 			= new int[k_TilesetInfoCOUNT];
		s_TilesetLayerInfo		= new int[s_TilesetMaxLayerCount][k_TilesetLayerCOUNT];
		s_TilesetLayerData		= new byte[s_TilesetMaxLayerCount][k_TilesetLayerDataCOUNT][];
		s_TilesetLayerImage		= new Image[s_TilesetMaxLayerCount][k_TilesetLayerImageCOUNT];
		s_TilesetLayerGraphics	= new Graphics[s_TilesetMaxLayerCount][k_TilesetLayerGraphicsCOUNT];
		s_TilesetSprite			= new zSprite[s_TilesetMaxLayerCount];
		s_TilesetDistanceX 		= new int[s_TilesetMaxLayerCount];
		s_TilesetDistanceY 		= new int[s_TilesetMaxLayerCount];
		//Guijun
		int[] s_TilesetInfo = this.s_TilesetInfo;
		
		if (DevConfig.tileset_useLayerLastUpdatedArea)
		{
		    s_TilesetLayerLastUpdatedArea = new int[s_TilesetMaxLayerCount][k_TilesetLayerAreaCount][k_TilesetLayerAreaInfoCOUNT];
		}

		s_TilesetInfo[k_TilesetInfoDestWidth]			= nDestWidth;
		s_TilesetInfo[k_TilesetInfoDestHeight]			= nDestHeight;

		if (DevConfig.tileset_useTileShift)
		{
			// w and jh are jsut for debug mode
			int w, h;
			if (true)
			{
				w = nTileWidth;
				h = nTileHeight;
			}
			// convert tile width and tile height to power of 2 values
			// uses Math_sqrt(long) because it doesn't need to initialise Math engine
			nTileWidth = cMath.Math_Log2(nTileWidth);
			nTileHeight = cMath.Math_Log2(nTileHeight);

			if(!((1<<nTileWidth)==w))Utils.DBG_PrintStackTrace(false, "Tileset_Init. using zJYLibConfig.tileset_UseTileShift with non power of 2 tile width"+nTileWidth);;
			if(!((1<<nTileHeight)==h))Utils.DBG_PrintStackTrace(false, "Tileset_Init. using zJYLibConfig.tileset_UseTileShift with non power of 2 tile height"+nTileHeight);;
			s_TilesetInfo[k_TilesetInfoTileWidthShift]		= nTileWidth;
			s_TilesetInfo[k_TilesetInfoTileWidth]			= 1 << nTileWidth;
			s_TilesetInfo[k_TilesetInfoTileWidthMask]		= s_TilesetInfo[k_TilesetInfoTileWidth] - 1;

			s_TilesetInfo[k_TilesetInfoTileHeightShift]		= nTileHeight;
			s_TilesetInfo[k_TilesetInfoTileHeight]			= 1 << nTileHeight;
			s_TilesetInfo[k_TilesetInfoTileHeightMask]		= s_TilesetInfo[k_TilesetInfoTileHeight] - 1;
		}
		else
		{
			//s_TilesetInfo[k_TilesetInfoTileWidthShift]		= 0;
			s_TilesetInfo[k_TilesetInfoTileWidth]			= nTileWidth;
			s_TilesetInfo[k_TilesetInfoTileWidthMask]		= 0;

			//s_TilesetInfo[k_TilesetInfoTileHeightShift]		= 0;
			s_TilesetInfo[k_TilesetInfoTileHeight]			= nTileHeight;
			s_TilesetInfo[k_TilesetInfoTileHeightMask]		= 0;

		}

		s_bTilesetPlayerInitialized = true;
	}

	public void Tileset_SetLayerPos(int x, int y)
	{
		//Guijun
		int []s_TilesetInfo = this.s_TilesetInfo;
		s_TilesetInfo[k_TilesetInfoLayerX] = x;
		s_TilesetInfo[k_TilesetInfoLayerY] = y;
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	final private boolean isFlag(int nLayer, int flag)
	{
		return (s_TilesetLayerInfo[nLayer][k_TilesetLayerFlag] & flag) != 0;
	}
	final private void setFlag(int nLayer, int flag, boolean value)
	{
		if (value)
			s_TilesetLayerInfo[nLayer][k_TilesetLayerFlag] |= flag;
		else
			s_TilesetLayerInfo[nLayer][k_TilesetLayerFlag] &= ~flag;
	}

		
		public void Tileset_SetLayerDistance(int nLayer, int distanceX, int distanceY) 
		{
			s_TilesetDistanceX[nLayer] = distanceX;
			s_TilesetDistanceY[nLayer] = distanceY;
		}
	
		//--------------------------------------------------------------------------------------------------------------------
		/// Load a tileset layer into the player.
		/// &param nLayer Layer to load. It will replace the information if this layer is allready loaded.
		/// &param MapSizes Array of short containing this layer size in tile count. Array generated by Aurora (.game).
		/// &param MapData Array of byte containing the value of each position of the map. The Size of this array should be w*h in tile count. Byte array generated by Aurora (.game).
		/// &param MapFlip Array of byte containing the flip value of each tile on the map. The Size of this array should be w*h in tile count. Byte array generated by Aurora (.game). can be null, in which case no flip will occurs on the tile
		/// &param MapSprite Sprite to use to draw each tile. If this sprite contains any frame, the engine will use PaintFrame to draw each tile of this layer. If the sprite contains only module, the tile will be drawn with PaintModule.
		/// &param bUseCB True if you want to use a Circular buffer (backbuffer) for this layer. Usually this should only be used with the background layer, when drawing this layer, it will overwrite what was on the destination, without transparancy. &note Using this will create an image of the size of the destination.
		/// &param origin set the position of coord 0,0 in layer. 0.top left,  1.bottom left
		/// &param wrappingX tileset wrapping policy in X direction
		/// &param wrappingY tileset wrapping policy in Y direction
		/// &param origin set the position of coord 0,0 in tileset (either GLLib.TOP or GLLib.BOTTOM)
		///&deprecated use Tileset_LoadLayer(int nLayer, byte[] MapSizes, byte[] MapData, byte[] MapFlip, zSprite MapSprite, int iUseCB, int origin, int wrappingX, int wrappingY)  instead
		//--------------------------------------------------------------------------------------------------------------------
		public void Tileset_LoadLayer(int nLayer, byte[] MapSizes, byte[] MapData, byte[] MapFlip, zSprite MapSprite, boolean bUseCB, int origin, int wrappingX, int wrappingY)
		{
			if (bUseCB)
				Tileset_LoadLayer(nLayer, MapSizes, MapData, MapFlip, MapSprite, nLayer, origin, wrappingX, wrappingY);
			else
				Tileset_LoadLayer(nLayer, MapSizes, MapData, MapFlip, MapSprite, -1, origin, wrappingX, wrappingY);
		}



	//--------------------------------------------------------------------------------------------------------------------
	/// Load a tileset layer into the player.
	/// &param nLayer Layer to load. It will replace the information if this layer is allready loaded.
	/// &param MapSizes Array of short containing this layer size in tile count. Array generated by Aurora (.game).
	/// &param MapData Array of byte containing the value of each position of the map. The Size of this array should be w*h in tile count. Byte array generated by Aurora (.game).
	/// &param MapFlip Array of byte containing the flip value of each tile on the map. The Size of this array should be w*h in tile count. Byte array generated by Aurora (.game). can be null, in which case no flip will occurs on the tile
	/// &param MapSprite Sprite to use to draw each tile. If this sprite contains any frame, the engine will use PaintFrame to draw each tile of this layer. If the sprite contains only module, the tile will be drawn with PaintModule.
	/// &param iUseCB possible values are<br> -1 to disable circular buffer <br>   nLayer to allocate a circular buffer for this layer<br>   0..n to reuse the previously allocated buffer of layer 0..n.
	/// &param origin set the position of coord 0,0 in layer. 0.top left,  1.bottom left
	/// &param wrappingX tileset wrapping policy in X direction
	/// &param wrappingY tileset wrapping policy in Y direction
	/// &param origin set the position of coord 0,0 in tileset (either GLLib.TOP or GLLib.BOTTOM)
	//--------------------------------------------------------------------------------------------------------------------
	
		void Tileset_LoadLayer(int nLayer, byte[] MapSizes, byte[] MapData, byte[] MapFlip, zSprite MapSprite, int iUseCB, int origin, int wrappingX, int wrappingY)
	

	{
		if(!(MapSizes != null))Utils.DBG_PrintStackTrace(false, "zJYLibPlayer.Tileset_LoadLayer.MapSizes is null");;
		if(!(MapData != null))Utils.DBG_PrintStackTrace(false, "zJYLibPlayer.Tileset_LoadLayer.MapData is null");;
//			ASSERT(MapFlip != null, "zJYLibPlayer.Tileset_LoadLayer.MapFlip is null");
		//sprite就不再限制必须非空。因为物理层的sprite可能为空

		if(!(MapSprite != null))Utils.Dbg("Warning: zJYLibPlayer.Tileset_LoadLayer.MapSprite is null");//Utils.DBG_PrintStackTrace(false, "zJYLibPlayer.Tileset_LoadLayer.MapSprite is null");;
		if(!((wrappingX == WRAP_CLAMP) || (wrappingX == WRAP_REPEAT) || (wrappingX == WRAP_NONE)))Utils.DBG_PrintStackTrace("zJYLibPlayer.Tileset_LoadLayer. X wrapping is not valid");;
		if(!((wrappingY == WRAP_CLAMP) || (wrappingY == WRAP_REPEAT) || (wrappingY == WRAP_NONE)))Utils.DBG_PrintStackTrace("zJYLibPlayer.Tileset_LoadLayer. Y wrapping is not valid");;
		if(!((origin == GLLib.TOP) || (origin == GLLib.BOTTOM))) Utils.DBG_PrintStackTrace("zJYLibPlayer.Tileset_LoadLayer. origin is not valid");


		if(true)
		{
			if (MapFlip == null)
			{
				Utils.Dbg("WARNING zJYLibPlayer.Tileset_LoadLayer.MapFlip is null, no flip will occur");
			}
		}

		if(!s_bTilesetPlayerInitialized)
		{
			return;
		}

		Tileset_Destroy(nLayer, false);
		//GUIJUN LOCAL VAR OPTIMIZE
		byte[][] LayerData = this.s_TilesetLayerData[nLayer];
		
		LayerData[k_TilesetLayerDataMap]			= MapData;
		LayerData[k_TilesetLayerDataFlip]			= MapFlip;
		
		//GUIJUN LOCAL VAR OPTIMIZE
		int[] LayerInfo = this.s_TilesetLayerInfo[nLayer];
		int[] s_TilesetInfo = this.s_TilesetInfo;
		
		
		LayerInfo[k_TilesetLayerTileCountWidth] 	= (short)((MapSizes[0] & 0xFF) |
				  ((MapSizes[1] & 0xFF) << 8));//cPack.Mem_GetShort(MapSizes, 0);
		LayerInfo[k_TilesetLayerTileCountHeight]  = (short)((MapSizes[2] & 0xFF) |
				  ((MapSizes[3] & 0xFF) << 8));//cPack.Mem_GetShort(MapSizes, 2);

		LayerInfo[k_TilesetLayerWidth] 			= LayerInfo[k_TilesetLayerTileCountWidth]  * s_TilesetInfo[k_TilesetInfoTileWidth];
		LayerInfo[k_TilesetLayerHeight] 		= LayerInfo[k_TilesetLayerTileCountHeight] * s_TilesetInfo[k_TilesetInfoTileHeight];

		s_TilesetSprite[nLayer]										= MapSprite;

		//DBG("Tileset_Load map size : " + s_TilesetLayerInfo[nLayer][k_TilesetLayerWidth] + "x" + s_TilesetLayerInfo[nLayer][k_TilesetLayerHeight]);

		
			if (iUseCB > -1)
			{
				try
				{
					LayerInfo[k_TilesetLayerCBID] = iUseCB;					
					
					if (DevConfig.tileset_useTileShift)
					{
						LayerInfo[k_TilesetLayerCBWidth]			= (s_TilesetInfo[k_TilesetInfoDestWidth] & ~s_TilesetInfo[k_TilesetInfoTileWidthMask]) + (1 * s_TilesetInfo[k_TilesetInfoTileWidth]);		// only 1 extra tile for the moment
						LayerInfo[k_TilesetLayerCBHeight]			= (s_TilesetInfo[k_TilesetInfoDestHeight] & ~s_TilesetInfo[k_TilesetInfoTileHeightMask]) + (1 * s_TilesetInfo[k_TilesetInfoTileHeight]);		// only 1 extra tile for the moment

						// we need at least 1 full tile larger than the screen
						if((LayerInfo[k_TilesetLayerCBWidth] - s_TilesetInfo[k_TilesetInfoDestWidth]) < s_TilesetInfo[k_TilesetInfoTileWidth])
						{
							LayerInfo[k_TilesetLayerCBWidth] += s_TilesetInfo[k_TilesetInfoTileWidth];
						}

						// we need at least 1 full tile taller than the screen
						if((LayerInfo[k_TilesetLayerCBHeight] - s_TilesetInfo[k_TilesetInfoDestHeight]) < s_TilesetInfo[k_TilesetInfoTileHeight])
						{
							LayerInfo[k_TilesetLayerCBHeight] += s_TilesetInfo[k_TilesetInfoTileHeight];
						}
					}
					else
					{
						//We need to have a multiple of tile width/height
						//We also need to have a full tile over the canvas width/height

						int nRestX													= s_TilesetInfo[k_TilesetInfoDestWidth] % s_TilesetInfo[k_TilesetInfoTileWidth];
						int nAddX													= 1 + ((nRestX != 0) ? 1 : 0);
						LayerInfo[k_TilesetLayerCBWidth]			= s_TilesetInfo[k_TilesetInfoDestWidth] - nRestX + (nAddX * s_TilesetInfo[k_TilesetInfoTileWidth]);

						int nRestY													= s_TilesetInfo[k_TilesetInfoDestHeight] % s_TilesetInfo[k_TilesetInfoTileHeight];
						int nAddY													= 1 + ((nRestY != 0) ? 1 : 0);
						LayerInfo[k_TilesetLayerCBHeight]			= s_TilesetInfo[k_TilesetInfoDestHeight] - nRestY + (nAddY * s_TilesetInfo[k_TilesetInfoTileHeight]);

						// Force backbuffer dimensions to be EVEN!
						if (DevConfig.tileset_useBugFixImageOddSize)
						{
							if((LayerInfo[k_TilesetLayerCBWidth] & 0x01) != 0)
							{
								LayerInfo[k_TilesetLayerCBWidth] += s_TilesetInfo[k_TilesetInfoTileWidth];
							}

							if((LayerInfo[k_TilesetLayerCBHeight] & 0x01) != 0)
							{
								LayerInfo[k_TilesetLayerCBHeight] += s_TilesetInfo[k_TilesetInfoTileHeight];
							}
						}
					}
					//GUIJUN LOCAL VAR OPTIMIZE
					Image [] LayerImage =  s_TilesetLayerImage[nLayer];

					if (iUseCB == nLayer)
					{
						
						if (LayerImage[k_TilesetLayerImageCB] == null
								|| LayerImage[k_TilesetLayerImageCB].getWidth() != LayerInfo[k_TilesetLayerCBWidth]
								|| LayerImage[k_TilesetLayerImageCB].getHeight() != LayerInfo[k_TilesetLayerCBHeight])
						{
							Exception exGot = null;

							do {//对于E398来说，内存回收很慢，可能因为某些情况内存还没回收完导致内存爆掉

								//所以循环尝试直至成功。

								try {

									if (exGot != null) {

										GLLib.Gc();

										Thread.sleep(10);

									}

									

									if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
									{
										LayerImage[k_TilesetLayerImageCB]				= Image.createImage(zSprite.scaleX(LayerInfo[k_TilesetLayerCBWidth]),
																														    zSprite.scaleY(LayerInfo[k_TilesetLayerCBHeight]));
									}
									else
									{
										LayerImage[k_TilesetLayerImageCB]				= Image.createImage(LayerInfo[k_TilesetLayerCBWidth], LayerInfo[k_TilesetLayerCBHeight]);
									}
								} catch (Exception e) {

									exGot = e;

								}

							} while (exGot != null);

							

							s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB]		= LayerImage[k_TilesetLayerImageCB].getGraphics();
							s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB].setColor(0x0);//先涂个黑色底色
							s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB].fillRect(
									0, 0, LayerImage[k_TilesetLayerImageCB].getWidth(), 
									LayerImage[k_TilesetLayerImageCB].getHeight());


							if (DevConfig.tileset_useLayerLastUpdatedArea)
							{
								// Mark that this layer is the one to have created the backbuffer!
								setFlag(nLayer, k_Flag_CreatedCB, true);
							}
						}
					}
					else
					{
						if(!(s_TilesetLayerImage[iUseCB][k_TilesetLayerImageCB] != null))Utils.DBG_PrintStackTrace(false, "zJYLibPlayer.Tileset_LoadLayer. layer "+iUseCB+" has no circular buffer allocated");;

						LayerImage[k_TilesetLayerImageCB]				= s_TilesetLayerImage[iUseCB][k_TilesetLayerImageCB];
						s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB]		= s_TilesetLayerGraphics[iUseCB][k_TilesetLayerGraphicsCB];
					}

					setFlag(nLayer, k_Flag_UseCB, true);
				}
				catch (Exception e)
				{
					if(!(false))Utils.DBG_PrintStackTrace(false, "zJYLibPlayer.Tileset_LoadLayer.pb while ceating circular buffer : "+e.toString());;
				}
			}



		//Initialize everything	to -1 just in case camera is init'ed to	0, 0 - So the top-left tile	is properly	drawn
		LayerInfo[k_TilesetLayerFirstTileX]		= -1;
		LayerInfo[k_TilesetLayerFirstTileY]		= -1;
		LayerInfo[k_TilesetLayerLastTileX]			= -1;
		LayerInfo[k_TilesetLayerLastTileY]			= -1;

		LayerInfo[k_TilesetLayerInitialized] 		= 1;
		LayerInfo[k_TilesetLayerEnabled] 			= 1;

		LayerInfo[k_TilesetLayerCamX]				= 0;
		LayerInfo[k_TilesetLayerCamY]				= 0;

		setFlag(nLayer, k_Flag_WrappingX, wrappingX == WRAP_REPEAT);
		setFlag(nLayer, k_Flag_LimitCamX, wrappingX == WRAP_CLAMP);

		setFlag(nLayer, k_Flag_WrappingY, wrappingY == WRAP_REPEAT);
		setFlag(nLayer, k_Flag_LimitCamY, wrappingY == WRAP_CLAMP);

		setFlag(nLayer, k_Flag_Origin, origin == GLLib.BOTTOM);
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
		/**禁止/关闭tile底色。某些游戏可能没有全地图的map，需要用纯色填充背景*/
	void Tileset_EnableTileBGColor(int nLayer,boolean flag)

	{
		setFlag(nLayer, k_Flag_FillColorInTileBG, flag);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Delete a layer from the player. Image and data are going to be freed.
	/// &param nLayer Layer to delete.
	//--------------------------------------------------------------------------------------------------------------------
	public void Tileset_Destroy(int nLayer)
	{
		Tileset_Destroy(nLayer, true);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Delete a layer from the player.
	/// &param nLayer Layer to delete.
	/// &param bFreeBufferImage Free buffer image
	//--------------------------------------------------------------------------------------------------------------------
	void Tileset_Destroy(int nLayer, boolean bFreeBufferImage)
	{
		if(!s_bTilesetPlayerInitialized)
			return;
		
		s_TilesetLayerInfo[nLayer]		= new int[k_TilesetLayerCOUNT];
		if (bFreeBufferImage)
		{
			s_TilesetLayerImage[nLayer]		= new Image[k_TilesetLayerImageCOUNT];
			s_TilesetLayerGraphics[nLayer]	= new Graphics[k_TilesetLayerGraphicsCOUNT];
		}
		s_TilesetLayerData[nLayer]		= new byte[k_TilesetLayerDataCOUNT][];
		s_TilesetSprite[nLayer]			= null;
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	public final void Tileset_Draw(Graphics g)
	{
	//Guijun
		int[][] s_TilesetLayerInfo = this.s_TilesetLayerInfo;
		int s_TilesetMaxLayerCount = this.s_TilesetMaxLayerCount;
		for (int i = 0; i < s_TilesetMaxLayerCount; i++) {
			if (s_TilesetLayerInfo[i][k_TilesetLayerCBID] < i) {
				//本buffer在CB中，CB只要更新前面的layer的时候更新过，则后面使用同一个CB的layer就不需要再往屏幕上绘制
				continue;
			}
			Tileset_Draw(g, i);
		}
    }
	
	//--------------------------------------------------------------------------------------------------------------------
	/// Draw a specific onto destination Graphics.
	/// If this layer is using a circular buffer it will be drawn to destination using correct order.
	/// If this layer is not using a circular buffer, every tile will be drawn to fill the destination.
	/// &param g Graphics destination.
	/// &param nLayer Layer to draw. Use -1 to draw every valid layer.
	//--------------------------------------------------------------------------------------------------------------------
	public final void Tileset_Draw(Graphics g, int nLayer)
	{
	    Tileset_Draw(g, 0, 0, nLayer);
    }

	//--------------------------------------------------------------------------------------------------------------------
	/// Draw a specific onto destination Graphics.
	/// If this layer is using a circular buffer it will be drawn to destination using correct order.
	/// If this layer is not using a circular buffer, every tile will be drawn to fill the destination.
	/// &param g Graphics destination.
	/// &param dx X offset in the destination.
	/// &param dy Y offset in the destination.
	/// &param g Graphics destination.
	/// &param nLayer Layer to draw. Use -1 to draw every valid layer.
	//--------------------------------------------------------------------------------------------------------------------
	public void Tileset_Draw(Graphics g, int dx, int dy, int nLayer)
	{
		if (this.s_bUsingImageLayer) {
			Tileset_DrawImageLayer(g, dx, dy, nLayer);
			return;
		}
		
		if(!s_bTilesetPlayerInitialized)
			return;
			//Guijun
		int[]s_TilesetInfo = this.s_TilesetInfo;
		// set some accelerator
		int destWidth = s_TilesetInfo[k_TilesetInfoDestWidth];
		int destHeight = s_TilesetInfo[k_TilesetInfoDestHeight];
			//Guijun
	 	int s_TilesetMaxLayerCount = this.s_TilesetMaxLayerCount;
		int s_TilesetAlphaLayer = this.s_TilesetAlphaLayer;
		if(nLayer == -1)
		{
			for(int i = 0; i < s_TilesetMaxLayerCount; i++)
			{
				if (DevConfig.tileset_usePixelEffects)
				{
					if(i != s_TilesetAlphaLayer)
					{
						Tileset_Draw(g, dx , dy, i);
					}
				}
				else
				{
					Tileset_Draw(g, dx, dy, i);
				}
			}
			return;
		}

		// accelerator Guijun confirm this
		int[] layerInfo = s_TilesetLayerInfo[nLayer];

		if ((layerInfo[k_TilesetLayerInitialized] != 1) || (layerInfo[k_TilesetLayerEnabled] != 1))
		{
			return;
		}

		GLLib.Profiler_BeginNamedEvent("Tileset_Draw");

		//cam position is actually the position of the top-left corner of the screen
		int	originX	= layerInfo[k_TilesetLayerCamX];
		int	originY	= layerInfo[k_TilesetLayerCamY];

	
		// USING a Circular Buffer
		if (isFlag(nLayer, k_Flag_UseCB))
		{
			// Save the clip
			int cx = 0;
			int cy = 0;
			int cw = 0;
			int ch = 0;

			if ( GLLib.IsClipValid(g) )
			{
				cx = GLLib.GetClipX(g);
				cy = GLLib.GetClipY(g);
				cw = GLLib.GetClipWidth(g);
				ch = GLLib.GetClipHeight(g);
			}

			// coordinates of background in map (by tile)
			int	firstTileX;
			int	firstTileY;
			int	lastTileX;
			int	lastTileY;

			if (DevConfig.tileset_useTileShift)
			{
				firstTileX	= originX >> s_TilesetInfo[k_TilesetInfoTileWidthShift];
				firstTileY	= originY >> s_TilesetInfo[k_TilesetInfoTileHeightShift];
				lastTileX	= (originX + s_TilesetInfo[k_TilesetInfoDestWidth]) 	>> s_TilesetInfo[k_TilesetInfoTileWidthShift];
				lastTileY	= (originY + s_TilesetInfo[k_TilesetInfoDestHeight])	>> s_TilesetInfo[k_TilesetInfoTileHeightShift];
			}
			else
			{
				// Small hack, because / (division) behave differently than >> (shift)
				// EX :
				// 		-1 >> 4 = -1
				// 		-1 / 16 =  0
				// We need the >> (shift) behaviour.

				int nTmpStartX 	= originX;
				int nTmpStartY 	= originY;

				if(nTmpStartX < 0)
					nTmpStartX -= s_TilesetInfo[k_TilesetInfoTileWidth];

				if(nTmpStartY < 0)
					nTmpStartY -= s_TilesetInfo[k_TilesetInfoTileHeight];

				firstTileX	= nTmpStartX / s_TilesetInfo[k_TilesetInfoTileWidth];
				firstTileY	= nTmpStartY / s_TilesetInfo[k_TilesetInfoTileHeight];
				lastTileX 	= firstTileX + (layerInfo[k_TilesetLayerCBWidth] / s_TilesetInfo[k_TilesetInfoTileWidth]) - 1;
				lastTileY 	= firstTileY + (layerInfo[k_TilesetLayerCBHeight] / s_TilesetInfo[k_TilesetInfoTileHeight]) - 1;
			}

			int	startX = 0;
			int endX   = 0;
			int startY = 0;
			int endY   = 0;

			// Compute Range of Y Tiles that need to be re-drawn (due to up/down movement)
			if ((layerInfo[k_TilesetLayerFirstTileY] !=	firstTileY) || (layerInfo[k_TilesetLayerLastTileY] != lastTileY))
			{
				// Moving UP
				if ((layerInfo[k_TilesetLayerFirstTileY] < firstTileY) || (layerInfo[k_TilesetLayerLastTileY] < lastTileY))
				{
					// Completely outside the region
					if (layerInfo[k_TilesetLayerLastTileY] < firstTileY)
					{
						startY = firstTileY;
						endY   = lastTileY;
					}
					// Partially outside the region
					else
					{
						startY = layerInfo[k_TilesetLayerLastTileY] + 1;
						endY   = lastTileY;
					}
				}
				// Moving DOWN
				else
				{
					// Completely outside the region
					if (layerInfo[k_TilesetLayerFirstTileY] > lastTileY)
					{
						startY = firstTileY;
						endY   = lastTileY;
					}
					// Partially outside the region
					else
					{
						startY = firstTileY;
						endY   = layerInfo[k_TilesetLayerFirstTileY] - 1;
					}
				}
			}

    		if (DevConfig.tileset_useLayerLastUpdatedArea)
    		{
				// If multiple layers use the same CB we only want to update the rects once... choose the layer that created the CB!
				if (isFlag(nLayer, k_Flag_CreatedCB))
				{
					s_TilesetLayerLastUpdatedAreaIndex = 0;
				}
            }

			// Compute Range of X Tiles that need to be re-drawn (due to left/right movement)
			if ((layerInfo[k_TilesetLayerFirstTileX] !=	firstTileX) || (layerInfo[k_TilesetLayerLastTileX]  != lastTileX))
			{
				// Moving LEFT
				if ((layerInfo[k_TilesetLayerFirstTileX] < firstTileX) || (layerInfo[k_TilesetLayerLastTileX] < lastTileX))
				{
					// COMPLETELY outside of current area
					if (layerInfo[k_TilesetLayerLastTileX] < firstTileX)
					{
						startX = firstTileX;
						endX   = lastTileX;
					}
					// PARTIALLY outside of current area
					else
					{
						startX = layerInfo[k_TilesetLayerLastTileX] + 1;
						endX   = lastTileX;
					}
				}
				// Moving RIGHT
				else
				{
					// COMPLETELY outside of current area
					if (layerInfo[k_TilesetLayerFirstTileX] > lastTileX)
					{
						startX = firstTileX;
						endX   = lastTileX;
					}
					// PARTIALLY outsdie of current area
					else
					{
						startX = firstTileX;
						endX   = layerInfo[k_TilesetLayerFirstTileX] - 1;
					}
				}

				int lenY       = lastTileY-firstTileY;
				int tempStartY = firstTileY;

				// Check for movement in other dimension... to catch case where we need to update BOTH in 1 frame.
				// In this case we need to prevent the 2 updates from overlapping... we do this by limiting 1 of them.
				if ((layerInfo[k_TilesetLayerFirstTileY] !=	firstTileY) || (layerInfo[k_TilesetLayerLastTileY] != lastTileY))
				{
					// Moving DOWN
					if ((layerInfo[k_TilesetLayerFirstTileY] < firstTileY) || (layerInfo[k_TilesetLayerLastTileY] < lastTileY))
					{
						// Just alter the number of Y tiles the X-movement update will draw
						lenY -= (endY-startY+1);
					}
					// Moving UP
					else
					{
						// Also alter the starting point
						lenY -= (endY-startY+1);
						tempStartY += (endY-startY+1);
					}
				}

				// Check if we need to draw anything
				if (lenY >= 0)
				{
					Tileset_UpdateBuffer(s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB],
										 nLayer,
										 startX,
										 tempStartY,
										 endX-startX,
										 lenY,
										 0,
										 0);
				}

				// Set these no matter what... they will still be updated due to the other update...
				layerInfo[k_TilesetLayerFirstTileX]	= firstTileX;
				layerInfo[k_TilesetLayerLastTileX]	= lastTileX;
			}

			// Finally perform the 2nd update... movement in Y
			if ((layerInfo[k_TilesetLayerFirstTileY] !=	firstTileY) || (layerInfo[k_TilesetLayerLastTileY] != lastTileY))
			{
				Tileset_UpdateBuffer(	s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB],
										nLayer,
										firstTileX,
										startY,
										lastTileX-firstTileX,
										endY-startY,
										0,
										0);

				layerInfo[k_TilesetLayerFirstTileY]	= firstTileY;
				layerInfo[k_TilesetLayerLastTileY]	= lastTileY;
			}

			if(g != null)
			{
				boolean mightBeOffscreen = false;

				if (!isFlag(nLayer, k_Flag_LimitCamX | k_Flag_WrappingX) )
				{
					if (originX < 0)
					{
						dx -= originX;
						originX = 0;
						mightBeOffscreen = true;
					}
					else if (originX > (layerInfo[k_TilesetLayerWidth] - destWidth))
					{
						dx -= (originX - (layerInfo[k_TilesetLayerWidth] - destWidth));
						originX = (layerInfo[k_TilesetLayerWidth] - destWidth);
						mightBeOffscreen = true;
					}
				}
				else
				{
					while(originX < 0)
					{
						originX += layerInfo[k_TilesetLayerCBWidth];
					}
				}

				if (!isFlag(nLayer, k_Flag_LimitCamY | k_Flag_WrappingY) )
				{
					if (originY < 0)
					{
						dy -= originY;
						originY = 0;
						mightBeOffscreen = true;
					}
					else if (originY > (layerInfo[k_TilesetLayerHeight] - destHeight))
					{
						dy -= (originY - (layerInfo[k_TilesetLayerHeight] - destHeight));
						originY = (layerInfo[k_TilesetLayerHeight] - destHeight);
						mightBeOffscreen = true;
					}
				}
				else
				{
					while(originY < 0)
					{
						originY += layerInfo[k_TilesetLayerCBHeight];
					}
				}

    			int	modX0	= originX %	layerInfo[k_TilesetLayerCBWidth];
    			int	modY0	= originY %	layerInfo[k_TilesetLayerCBHeight];
    			int	modX1	= (originX + destWidth) % layerInfo[k_TilesetLayerCBWidth];
    			int	modY1	= (originY + destHeight) % layerInfo[k_TilesetLayerCBHeight];

				//Reset	clip
				GLLib.SetClip(g, cx, cy, cw, ch);

    			if (modX1 >	modX0)
    			{
    				if (modY1 >	modY0)
    				{
    					Tileset_Draw2Screen(g, nLayer, modX0, modY0, destWidth, destHeight, 0 + dx, 0 + dy, mightBeOffscreen);
    				}
    				else
    				{
    					Tileset_Draw2Screen(g, nLayer, modX0, modY0, destWidth, destHeight - modY1, 0 + dx, 0 + dy, mightBeOffscreen);
    					GLLib.SetClip(g, cx, cy, cw, ch);
    					Tileset_Draw2Screen(g, nLayer, modX0, 0, destWidth, modY1, 0 + dx, destHeight - modY1 + dy, mightBeOffscreen);
    				}
    			}
    			else
    			{
    				if (modY1 >	modY0)
    				{
    					Tileset_Draw2Screen(g, nLayer, modX0, modY0, destWidth - modX1, destHeight, 0 + dx, 0 + dy, mightBeOffscreen);
    					GLLib.SetClip(g, cx, cy, cw, ch);
    					Tileset_Draw2Screen(g, nLayer, 0, modY0, modX1,	destHeight, destWidth - modX1 + dx, 0 + dy, mightBeOffscreen);
    				}
    				else
    				{
    					Tileset_Draw2Screen(g, nLayer, modX0, modY0, destWidth - modX1, destHeight - modY1, 0 + dx, 0 + dy, mightBeOffscreen);
    					GLLib.SetClip(g, cx, cy, cw, ch);
    					Tileset_Draw2Screen(g, nLayer, modX0, 0, destWidth - modX1, modY1, 0 + dx, destHeight - modY1 + dy, mightBeOffscreen);
    					GLLib.SetClip(g, cx, cy, cw, ch);
    					Tileset_Draw2Screen(g, nLayer, 0, modY0, modX1,	destHeight - modY1, destWidth - modX1 + dx, 0 + dy, mightBeOffscreen);
    					GLLib.SetClip(g, cx, cy, cw, ch);
    					Tileset_Draw2Screen(g, nLayer, 0, 0, modX1,	modY1, destWidth - modX1 + dx, destHeight - modY1 + dy, mightBeOffscreen);
    				}
    			}

				//Reset	clip
    			GLLib.SetClip(g, cx, cy, cw, ch);
		    }
		}
		else


		if (g != null)
		{
			// NOT using a Circular Buffer
			// Draw Directly on the Graphics
			int tileX0;
			int tileY0;
			int nbTileX;
			int nbTileY;
			int offsetX;
			int offsetY;

			if (DevConfig.tileset_useTileShift)
			{
				tileX0	= originX >> s_TilesetInfo[k_TilesetInfoTileWidthShift];
				tileY0	= originY >> s_TilesetInfo[k_TilesetInfoTileHeightShift];

				nbTileX	= destWidth >> s_TilesetInfo[k_TilesetInfoTileWidthShift];
				if((nbTileX << s_TilesetInfo[k_TilesetInfoTileWidthShift]) < destWidth)
				{
					nbTileX++;
				}
				nbTileY	= destHeight >> s_TilesetInfo[k_TilesetInfoTileHeightShift];
				if((nbTileY << s_TilesetInfo[k_TilesetInfoTileHeightShift]) < destHeight)
				{
					nbTileY++;
				}

				offsetX = (tileX0 << s_TilesetInfo[k_TilesetInfoTileWidthShift]) - originX;
				offsetY = (tileY0 << s_TilesetInfo[k_TilesetInfoTileHeightShift]) - originY;
			}
			else
			{
				// Small hack, because / (division) behave differently than >> (shift)
				// EX :
				// 		-1 >> 4 = -1
				// 		-1 / 16 =  0
				// We need the >> (shift) behaviour.

				int nTmpStartX 	= originX;
				int nTmpStartY 	= originY;

				if(nTmpStartX < 0)
					nTmpStartX -= s_TilesetInfo[k_TilesetInfoTileWidth];

				if(nTmpStartY < 0)
					nTmpStartY -= s_TilesetInfo[k_TilesetInfoTileHeight];

				tileX0	= nTmpStartX / s_TilesetInfo[k_TilesetInfoTileWidth];
				tileY0	= nTmpStartY / s_TilesetInfo[k_TilesetInfoTileHeight];

				nbTileX	= destWidth / s_TilesetInfo[k_TilesetInfoTileWidth];
				if((nbTileX * s_TilesetInfo[k_TilesetInfoTileWidth]) < destWidth)
				{
					nbTileX++;
				}

				nbTileY	= destHeight / s_TilesetInfo[k_TilesetInfoTileHeight];
				if((nbTileY * s_TilesetInfo[k_TilesetInfoTileHeight]) < destHeight)
				{
					nbTileY++;
				}

				offsetX = (tileX0 * s_TilesetInfo[k_TilesetInfoTileWidth]) - originX;
				offsetY = (tileY0 * s_TilesetInfo[k_TilesetInfoTileHeight]) - originY;
			}

			Tileset_UpdateBuffer( g, nLayer, tileX0, tileY0, nbTileX, nbTileY, offsetX + dx, offsetY + dy);
		}

		GLLib.Profiler_EndNamedEvent();
	}
	

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void Tileset_Draw2Screen(Graphics g, int nLayer, int srcX, int srcY, int width,	int	height,	int	destX, int destY, boolean maybeOffscreen)
	{
		if (DevConfig.sprite_useResize && zSprite.s_resizeOn)
		{
			srcX = zSprite.scaleX(srcX);
			srcY = zSprite.scaleY(srcY);
			width  = zSprite.scaleX(width);
			height = zSprite.scaleY(height);
			destX = zSprite.scaleX(destX);
			destY = zSprite.scaleY(destY);
		}

		if (maybeOffscreen)
		{
			// Quick AABB check to see if entire section if off-screen
			if (
				 (destX > GLLib.GetScreenWidth())  ||
			     (destY > GLLib.GetScreenHeight()) ||
			     ((destX + width)  < 0) ||
			     ((destY + height) < 0)
			   )
			{
				return;
			}

			// Add screen-area to the clip
			GLLib.ClipRect(g, destX, destY, width, height);
			GLLib.ClipRect(g, 0, 0, GLLib.GetScreenWidth(),GLLib.GetScreenHeight());
			g.drawImage(s_TilesetLayerImage[nLayer][k_TilesetLayerImageCB], destX - srcX, destY - srcY, 0);
		}
		else
		{
			GLLib.ClipRect(g, destX, destY,	width, height);
			g.drawImage(s_TilesetLayerImage[nLayer][k_TilesetLayerImageCB], destX - srcX, destY - srcY, 0);
		}
	}



//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	public final void Tileset_Update()
	{
	//Guijun
		int s_TilesetMaxLayerCount = this.s_TilesetMaxLayerCount;
		for (int i = 0; i < s_TilesetMaxLayerCount; i++) {
			Tileset_Update(i);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Update a specific layer circular buffer (back buffer) but does nothing
	/// if this layer is not using a circular buffer.
	/// &param nLayer Layer to update. Use -1 to update every valid layer.
	//--------------------------------------------------------------------------------------------------------------------
	public final void Tileset_Update(int nLayer)
	{
		Tileset_Draw(null, 0, 0, nLayer);
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void Tileset_UpdateBuffer(Graphics gDest, int nLayer, int tileX0, int tileY0, int nbTileX, int nbTileY, int offsetX, int offsetY)
	{
	
		boolean useCB 		= isFlag(nLayer, k_Flag_UseCB);
		int cx = 0;
		int cy = 0;
		int cw = 0;
		int ch = 0;
		//Guijun
		int[] layerInfo = s_TilesetLayerInfo[nLayer];
		byte[][] layerData = s_TilesetLayerData[nLayer];
		int[] s_TilesetInfo = this.s_TilesetInfo;
		boolean repeatX 	= isFlag(nLayer, k_Flag_WrappingX);
		boolean repeatY 	= isFlag(nLayer, k_Flag_WrappingY);
		int tileMapWidth 	= layerInfo[k_TilesetLayerTileCountWidth];
		int tileMapHeight 	= layerInfo[k_TilesetLayerTileCountHeight];
		byte[] dataArray 	= layerData[k_TilesetLayerDataMap];
		byte[] flagArray 	= layerData[k_TilesetLayerDataFlip];
		int tileWidth 		= s_TilesetInfo[k_TilesetInfoTileWidth];
		int tileHeight 		= s_TilesetInfo[k_TilesetInfoTileHeight];
		int originDestX;
		int originDestY;


		if (DevConfig.tileset_useClip)
		{
			cx = GLLib.GetClipX(gDest);
			cy = GLLib.GetClipY(gDest);
			cw = GLLib.GetClipWidth(gDest);
			ch = GLLib.GetClipHeight(gDest);
		}

		if (useCB)
		{
			if (DevConfig.tileset_useTileShift)
			{
				originDestX	= ((tileX0 << s_TilesetInfo[k_TilesetInfoTileWidthShift]) % layerInfo[k_TilesetLayerCBWidth]) + offsetX;
				originDestY	= ((tileY0 << s_TilesetInfo[k_TilesetInfoTileHeightShift]) % layerInfo[k_TilesetLayerCBHeight]) + offsetY;
			}
			else
			{
				originDestX	= ((tileX0 * s_TilesetInfo[k_TilesetInfoTileWidth]) % layerInfo[k_TilesetLayerCBWidth]) + offsetX;
				originDestY	= ((tileY0 * s_TilesetInfo[k_TilesetInfoTileHeight]) % layerInfo[k_TilesetLayerCBHeight]) + offsetY;
			}

			if( originDestX < 0)
				originDestX += layerInfo[k_TilesetLayerCBWidth];

			if( originDestY < 0)
				originDestY += layerInfo[k_TilesetLayerCBHeight];
		}
		else


		{
			originDestX	= offsetX;
			originDestY	= offsetY;
		}

		if (repeatX)
		{
			while (tileX0 < 0)
				tileX0 += tileMapWidth;

			while (tileX0 >= tileMapWidth)
				tileX0 -= tileMapWidth;
		}
		else
		{
			if( !isFlag(nLayer, k_Flag_LimitCamX) )
			{
				// Tileset start (LEFT edge) is to the RIGHT of LEFT screen edge
				if (tileX0 < 0)
				{
					originDestX -= (tileX0 * tileWidth);
					nbTileX += tileX0;
					tileX0 = 0;

					
						if (useCB && (originDestX >= layerInfo[k_TilesetLayerCBWidth]))
						{
							originDestX = 0;
						}


				}

				// Tileset end (RIGHT edge) is to the LEFT of RIGHT screen edge
				if ((tileX0 + nbTileX) >= tileMapWidth)
				{
					nbTileX = tileMapWidth - tileX0 - 1;
				}

				if (nbTileX < 0)
				{
					return;
				}
			}
		}

		if (repeatY)
		{
			while (tileY0 < 0)
				tileY0 += tileMapHeight;

			while (tileY0 >= tileMapHeight)
				tileY0 -= tileMapHeight;
		}
		else
		{
			if( !isFlag(nLayer, k_Flag_LimitCamY) )
			{
				// Tileset start (TOP edge) is BELOW the TOP screen edge
				if (tileY0 < 0)
				{
					originDestY -= (tileY0 * tileHeight);
					nbTileY += tileY0;
					tileY0 = 0;

					
						if (useCB && (originDestY >= layerInfo[k_TilesetLayerCBHeight]))
						{
							originDestY = 0;
						}


				}

				// Tileset end (BOTTOM edge) is ABOVE the BOTTOM screen edge
				if ((tileY0 + nbTileY) >= tileMapHeight)
				{
					nbTileY = tileMapHeight - tileY0 - 1;
				}

				if (nbTileY < 0)
				{
					return;
				}
			}
		}

		int destX = originDestX;
		int destY = originDestY;
		int nbX;
		int tileX;
		int offsetCur;
		int data;
		int flag;
		boolean emptyIndex;

		byte[]  alphaDataArray = null;
		byte[]  alphaFlagArray = null;
		int     alphaData;
		boolean alphaEmpty;

		if (DevConfig.tileset_usePixelEffects)
		{
			if ((nLayer == s_TilesetEffectLayer) && (s_TilesetAlphaLayer > 0))
			{
			//Guijun
				byte[][] layerdata_alpha = s_TilesetLayerData[s_TilesetAlphaLayer];
				// get content of current alpha cell
				alphaDataArray = layerdata_alpha[k_TilesetLayerDataMap];
				alphaFlagArray = layerdata_alpha[k_TilesetLayerDataFlip];
			}
		}
		//Guijun
		zSprite tilesetsprite = s_TilesetSprite[nLayer];
		Image[] layerImage = s_TilesetLayerImage[nLayer];
		
		
		while (nbTileY-- >= 0)
		{
			destX 	= originDestX;
			nbX 	= nbTileX;
			tileX 	= tileX0;

			while (nbX-- >=0)
			{
				offsetCur = tileX + (tileY0 * tileMapWidth);

				if( (!DevConfig.tileset_useIndexAsShort && offsetCur < dataArray.length) ||
				    (DevConfig.tileset_useIndexAsShort  && ((offsetCur<<1) < dataArray.length))
				  )
				{
					// get content of current cell
					if (DevConfig.tileset_useIndexAsShort)
					{
						data = cPack.Mem_GetShort(dataArray, offsetCur << 1) & 0xFFFF;
						emptyIndex = (data == 0xFFFF);
					}
					else
					{
						data = dataArray[offsetCur] & 0xFF;
						emptyIndex = (data == 0xFF);
					}
					

					if (isFlag(nLayer, k_Flag_FillColorInTileBG)) {

						//用黑色填充空白区域

						int color = gDest.getColor();

						gDest.setColor(0);

						gDest.fillRect(destX, destY, tileWidth, tileHeight);

						gDest.setColor(color);

					}		
					

					if (!emptyIndex)
					{
						if (flagArray == null)
							flag = 0;
						else
							flag = flagArray[offsetCur]	& 0xFF;

						
							if (DevConfig.tileset_useClip)
							{
								GLLib.ClipRect(gDest, destX, destY, tileWidth, tileHeight);
							}



						if(tilesetsprite.GetFrameCount() == 0)
						{
							tilesetsprite.PaintModule(gDest, data, destX, destY, flag);
						}
						else
						{
							int tx = destX;
							int ty = destY;

							if (DevConfig.sprite_useTransfFlip)
							{
								// we have to remove the effect of : sprite_useTransfFlip because for tiles its not usefull.
								if((flag & zSprite.FLAG_FLIP_X) != 0)
								{
									tx += tileWidth;
								}
								if((flag & zSprite.FLAG_FLIP_Y) != 0)
								{
									ty += tileHeight;
								}
							}

							tilesetsprite.PaintFrame(gDest, data, tx, ty, flag);
						}

						// Any effects to perform now after the blit??
						if (DevConfig.tileset_usePixelEffects && (nLayer == s_TilesetEffectLayer) && (s_TilesetAlphaLayer > 0))
						{
							// get content of current alpha cell
							if (DevConfig.tileset_useIndexAsShort)
							{
								alphaData = cPack.Mem_GetShort(alphaDataArray, offsetCur << 1) & 0xFFFF;
								alphaEmpty = (alphaData == 0xFFFF);
							}
							else
							{
								alphaData = alphaDataArray[offsetCur] & 0xFF;
								alphaEmpty = (alphaData == 0xFF);
							}

							if (!alphaEmpty)
							{
								int alphaFlag = 0;
								zSprite	tilesetsprite_alpha =  s_TilesetSprite[s_TilesetAlphaLayer];
								if (alphaFlagArray != null)
								{
									alphaFlag = alphaFlagArray[offsetCur] & 0xFF;
								}
								// In case this sprite is frame based... but still only use the module
								if(tilesetsprite_alpha.GetFrameCount() != 0)
								{
									alphaData = tilesetsprite_alpha.GetFrameModule(alphaData, 0);
								}

								if ((s_TilesetEffectType == TILESET_EFFECT_ADDITIVE) || (s_TilesetEffectType == TILESET_EFFECT_MULTIPLICATIVE))
								{
									tilesetsprite_alpha.RenderTilesetEffect(gDest, alphaData, layerImage[k_TilesetLayerImageCB], s_TilesetEffectType, destX, destY, tileWidth, tileHeight, alphaFlag);
								}
								else if (s_TilesetEffectType == TILESET_EFFECT_NORMAL)
								{
									tilesetsprite_alpha.PaintModule(gDest, alphaData, destX, destY, alphaFlag);
								}
							}
						}

						
							if (DevConfig.tileset_useClip)
							{
								GLLib.SetClip(gDest, cx, cy, cw, ch);
							}


					}
				}
				//else
				//{
				//	System.out.println("Avoiding render tile (" + tileX + ", " + tileY0 + ") at pos ("+destX +", "+ destY +")");
				//}

				destX += tileWidth;

				tileX++;
				if (tileX >= tileMapWidth)
				{
					if (repeatX)
					{
						tileX = 0;
					}
					else
					{
						break;
					}
				}

			
				if (useCB && (destX >= layerInfo[k_TilesetLayerCBWidth]))
				{
					destX = 0;
				}


			}

			destY += tileHeight;

			tileY0++;
			if (tileY0 >= tileMapHeight)
			{
				if (repeatY)
					tileY0 = 0;
				else
					break;
			}

		
			if (useCB && (destY >= layerInfo[k_TilesetLayerCBHeight]))
			{
				destY = 0;
			}


		}

   		if (DevConfig.tileset_useLayerLastUpdatedArea)
		{
			// Only need to capture this info for 1 layer... use the layer that created the CB
			if (isFlag(nLayer, k_Flag_CreatedCB))
			{
				int fX, fX1;

				if (destX > originDestX)
				{
					fX = destX;
					fX1 = 0;
				}
				else
				{
					fX = layerInfo[k_TilesetLayerCBWidth];
					fX1 = destX;
				}

				int fY, fY1;
				if (destY > originDestY)
				{
					fY = destY;
					fY1 = 0;
				}
				else
				{
					fY = layerInfo[k_TilesetLayerCBHeight];
					fY1 = destY;
				}
				//Guijun
				int[][]s_TilesetLayerLastUpdatedArea_nLayer = s_TilesetLayerLastUpdatedArea[nLayer];	
				for (int i = 0; i < 4; i++)
				{
					if ((i == 1 || i == 3) && fX1 == 0)
						continue;
					if ((i == 2 || i == 3) && fY1 == 0)
						continue;

					int[] rect = s_TilesetLayerLastUpdatedArea_nLayer[s_TilesetLayerLastUpdatedAreaIndex];
					rect[k_TilesetLayerAreaInfoX] = (i == 0 || i == 2) ? originDestX : 0;
					rect[k_TilesetLayerAreaInfoY] = (i == 0 || i == 1) ? originDestY : 0;
					rect[k_TilesetLayerAreaInfoW] = (i == 0 || i == 2) ? fX - originDestX : fX1;
					rect[k_TilesetLayerAreaInfoH] = (i == 0 || i == 1) ? fY - originDestY : fY1;
					s_TilesetLayerLastUpdatedAreaIndex++;
				}
			}
        }
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	// IMPORTANT if you modify this func, then modify as well Tileset_GetCameraY (which does the opposite)
	// Guijun 此函数调用极其频繁，请不要调用，而采用直接内联方式
	final private int Tileset_GetTranslatedOriginY(int nLayer, int y)
	{
		//if origin is bottom left
		if (isFlag(nLayer, k_Flag_Origin))
			return s_TilesetLayerInfo[nLayer][k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - y;
		// if origin is top left
		else
			return y;
	}

	
	public final void Tileset_SetCamera(int x, int y)
	{
	//Guijun
		int s_TilesetMaxLayerCount = this.s_TilesetMaxLayerCount;
		for (int i = 0; i < s_TilesetMaxLayerCount; i++) {
			Tileset_SetCamera(i, x, y);
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	/// Set the camera position of a specific layer.
	/// &param nLayer Layer of which you want to set the Camera position.
	/// &param x the new X Position of the camera.
	/// &param y the new Y Position of the camera.
	//--------------------------------------------------------------------------------------------------------------------
	public final void Tileset_SetCamera(int nLayer, int x, int y)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		int[]s_TilesetInfo = this.s_TilesetInfo;
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_SetCamera: Tileset player is not initialized");;
				return;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_SetCamera: nLayer invalid : " + nLayer));;
				return;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_SetCamera: nLayer in not initialized or not enabled.");;
				return;
			}
		}
		
		/** 根据距离重新计算camera位置 */
		x = (100 + s_TilesetDistanceX[nLayer]) * x / 100;
		y = (100 + s_TilesetDistanceY[nLayer]) * y / 100;

		layerinfo[k_TilesetLayerCamX] = x;
		layerinfo[k_TilesetLayerCamY] = Tileset_GetTranslatedOriginY(nLayer, y);

		if (isFlag(nLayer, k_Flag_LimitCamX))
		{
	 		if(layerinfo[k_TilesetLayerCamX] < 0)
			{
				//DBG("Cam Capped in X, was out of screen on left : " + layerinfo[k_TilesetLayerCamX]);
				layerinfo[k_TilesetLayerCamX] = 0;
			}
			else if((layerinfo[k_TilesetLayerCamX] + s_TilesetInfo[k_TilesetInfoDestWidth]) > layerinfo[k_TilesetLayerWidth])
			{
				//DBG("Cam Capped in X, was out of screen on right : " + layerinfo[k_TilesetLayerCamX]);
				layerinfo[k_TilesetLayerCamX] = layerinfo[k_TilesetLayerWidth] - s_TilesetInfo[k_TilesetInfoDestWidth];
			}
		}

		if (isFlag(nLayer, k_Flag_LimitCamY))
		{
			if(layerinfo[k_TilesetLayerCamY] < 0)
			{
				//DBG("Cam Capped in Y, was out of screen on top : " + layerinfo[k_TilesetLayerCamY]);
				layerinfo[k_TilesetLayerCamY] = 0;
			}
			else if((layerinfo[k_TilesetLayerCamY] + s_TilesetInfo[k_TilesetInfoDestHeight]) > layerinfo[k_TilesetLayerHeight])
			{
				//DBG("Cam Capped in Y, was out of screen on bottom : " + layerinfo[k_TilesetLayerCamY]);
				layerinfo[k_TilesetLayerCamY] = layerinfo[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight];
			}
		}
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the camera position of a specific layer.
	/// &param nLayer Layer of which you want to know the Camera position.
	/// &return a 2 int array with the x and y in pixel of the camera. (null if entry params are invalid).
	/// &see Tileset_GetCameraX, Tileset_GetCameraY
	/// &deprecated use Tileset_GetCameraX and Tileset_GetCameraY instead
	//--------------------------------------------------------------------------------------------------------------------
	final int[] Tileset_GetCamera(int nLayer)
	{
		Utils.Err("zJYLibPlayer.Tileset_GetCamera(int nLayer) . this function is deprecated, use zJYLibPlayer.Tileset_GetCameraX(int nLayer) and zJYLibPlayer.Tileset_GetCameraY(int nLayer) instead");;
		return Tileset_GetCameraXY(nLayer);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the camera X position of a specific layer.
	/// &param nLayer Layer of which you want to know the Camera position.
	/// &return camera X position
	//--------------------------------------------------------------------------------------------------------------------
	//Guijun
	final int[] Tileset_GetCameraXY(int nLayer)
	{
		int[] layerdata = s_TilesetLayerInfo[nLayer];
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: Tileset player is not initialized");;
				return null;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetCamera: nLayer invalid : " + nLayer));;
				return null;
			}
			if((layerdata[k_TilesetLayerInitialized] != 1)|| (layerdata[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: nLayer in not initialized or not enabled.");;
				return null;
			}
		}
		return new int[]{layerdata[k_TilesetLayerCamX],layerdata[k_TilesetLayerCamY]};	
	}
	final int Tileset_GetCameraX(int nLayer)
	{
		Utils.Err("Tileset_GetCameraX(int nLayer) . this function is deprecated, use Tileset_GetCameraXY(int nLayer) instead");;
		int[] layerdata = s_TilesetLayerInfo[nLayer];
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetCamera: nLayer invalid : " + nLayer));;
				return -1;
			}
			if((layerdata[k_TilesetLayerInitialized] != 1)|| (layerdata[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}
		return layerdata[k_TilesetLayerCamX];	
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Get the camera Y position of a specific layer.
	/// &param nLayer Layer of which you want to know the Camera position.
	/// &return camera Y position
	//--------------------------------------------------------------------------------------------------------------------
	final int Tileset_GetCameraY(int nLayer)
	{
		Utils.Err("Tileset_GetCameraY(int nLayer) . this function is deprecated, use Tileset_GetCameraXY(int nLayer) instead");;
		int[] layerdata = s_TilesetLayerInfo[nLayer];

		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetCamera: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerdata[k_TilesetLayerInitialized] != 1)|| (layerdata[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetCamera: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}
		// if origin is on bottom, invert coord
		if (isFlag(nLayer, k_Flag_Origin))
		{
			return layerdata[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - layerdata[k_TilesetLayerCamY];
		}
		else
		{
			return layerdata[k_TilesetLayerCamY];
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	
//	final int Tileset_GetCameraW()
//	{
//		return s_TilesetInfo[k_TilesetInfoDestWidth];
//	}
//	
//	final int Tileset_GetCameraH()
//	{
//		return s_TilesetInfo[k_TilesetInfoDestHeight];
//	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the pixel width of a specific layer.
	/// &param nLayer Layer of the tile to query.
	/// &return the pixel width of the requested layer. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	final int Tileset_GetLayerWidth(int nLayer)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerWidth: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetLayerWidth: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerWidth: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}

		return layerinfo[k_TilesetLayerWidth];
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the pixel height of a specific layer.
	/// &param nLayer Layer of the tile to query.
	/// &return the pixel height of the requested layer. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	public final int Tileset_GetLayerHeight(int nLayer)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerHeight: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetLayerHeight: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerHeight: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}

		return layerinfo[k_TilesetLayerHeight];
	}


//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the tile count width of a specific layer.
	/// &param nLayer Layer of the tile to query.
	/// &return the tile count width of the requested layer. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	public final int Tileset_GetLayerTileCountWidth(int nLayer)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];

		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerTileCountWidth: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetLayerTileCountWidth: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerTileCountWidth: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}

		return layerinfo[k_TilesetLayerTileCountWidth];
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the tile count height of a specific layer.
	/// &param nLayer Layer of the tile to query.
	/// &return the tile count height of the requested layer. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	public final int Tileset_GetLayerTileCountHeight(int nLayer)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
	if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerTileCountHeight: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetLayerTileCountHeight: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetLayerTileCountHeight: nLayer in not initialized or not enabled.");;
				return -1;
			}
		}

		return layerinfo[k_TilesetLayerTileCountHeight];
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get value of a specific tile.
	/// &param nLayer Layer of the tile to query.
	/// &param x X Position of the tile to query.
	/// &param y Y Position of the tile to query.
	/// &return the value of the requested tile. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	public final int Tileset_GetTile(int nLayer, int x, int y)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		if ((layerinfo[k_TilesetLayerFlag] & k_Flag_Origin) != 0)
			{
			y = layerinfo[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - y;
			};
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetTile: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTile: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetTile: nLayer in not initialized or not enabled.");;
				return -1;
			}

			if((x < 0) || (x > layerinfo[k_TilesetLayerTileCountWidth]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTile: x value out of bound [" + x + "]  0 <= x < " + layerinfo[k_TilesetLayerTileCountWidth]));;
				return -1;
			}

			if((y < 0) || (y > layerinfo[k_TilesetLayerTileCountHeight]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTile: y value out of bound [" + y + "]  0 <= y < " + layerinfo[k_TilesetLayerTileCountHeight]));;
				return -1;
			}
		}

		if (DevConfig.tileset_useIndexAsShort)
		{
			return cPack.Mem_GetShort(s_TilesetLayerData[nLayer][k_TilesetLayerDataMap], ((y * layerinfo[k_TilesetLayerTileCountWidth]) + x) << 1) & 0xFFFF;
		}
		else
		{
			return s_TilesetLayerData[nLayer][k_TilesetLayerDataMap][(y * layerinfo[k_TilesetLayerTileCountWidth]) + x] & 0xFF;
		}
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the flags information of a specific tile.
	/// &param nLayer Layer of the tile to query.
	/// &param x X Position of the tile to query.
	/// &param y Y Position of the tile to query.
	/// &return the flags value of the requested tile. (-1 if entry params are invalid).
	//--------------------------------------------------------------------------------------------------------------------
	public final int Tileset_GetTileFlags(int nLayer, int x, int y)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		if ((layerinfo[k_TilesetLayerFlag] & k_Flag_Origin) != 0)
		{
			y = layerinfo[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - y;
		};
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetTileFlags: Tileset player is not initialized");;
				return -1;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTileFlags: nLayer invalid : " + nLayer));;
				return -1;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetTileFlags: nLayer in not initialized or not enabled.");;
				return -1;
			}

			if((x < 0) || (x > layerinfo[k_TilesetLayerTileCountWidth]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTileFlags: x value out of bound [" + x + "]  0 <= x < " + layerinfo[k_TilesetLayerTileCountWidth]));;
				return -1;
			}

			if((y < 0) || (y > layerinfo[k_TilesetLayerTileCountHeight]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetTileFlags: y value out of bound [" + y + "]  0 <= y < " + layerinfo[k_TilesetLayerTileCountHeight]));;
				return -1;
			}
		}
		//Guijun
		byte[] layerdata_flip = s_TilesetLayerData[nLayer][k_TilesetLayerDataFlip];
		if(layerdata_flip == null)
			return 0;
		else
			return layerdata_flip[(y * layerinfo[k_TilesetLayerTileCountWidth]) + x] & 0xFF;
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the image that is the buffer for this layer. Defaults to first image (only one usually)
	/// &param p_iLayer Layer whose buffer we want as an image
	/// &return the Image that is being used to buffer this layer
	//--------------------------------------------------------------------------------------------------------------------
	final Image Tileset_GetBufferImage (int p_iLayer)
	{
		return Tileset_GetBufferImage(p_iLayer, 0);
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get one of the images that is being used as a buffer for this layer.
	/// NOTE: Current implementation always uses 1 image, so for now you can always use this method with 1 parameter
	/// &param p_iLayer Layer whose buffer we want as an image
	/// &param p_iImage Image index we want
	/// &return the Image that is being used to buffer this layer
	//--------------------------------------------------------------------------------------------------------------------
	final Image Tileset_GetBufferImage (int p_iLayer, int p_iImage)
	{
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetBufferImage: Tileset player is not initialized");;
				return null;
			}

			if((p_iLayer < 0) || (p_iLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetBufferImage: p_iLayer invalid : " + p_iLayer));;
				return null;
			}

			if((p_iImage < 0) || (p_iImage >= k_TilesetLayerImageCOUNT))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetBufferImage: p_iImage invalid : " + p_iImage));;
				return null;
			}
		}

		return s_TilesetLayerImage[p_iLayer][p_iImage];
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the graphics context of the image being used as a buffer for this layer.
	/// &param p_iLayer Layer whose buffer graphics context we want
	/// &return the graphics context of the buffer
	//--------------------------------------------------------------------------------------------------------------------
	final Graphics Tileset_GetBufferGraphics (int p_iLayer)
	{
		return Tileset_GetBufferGraphics(p_iLayer, 0);
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the graphics context of one of the images being used as a buffer for this layer.
	/// NOTE: Current implementation always uses 1 image, so for now you can always use this method with 1 parameter
	/// &param p_iLayer Layer whose buffer graphics context we want
	/// &param p_iImage Image index we want
	/// &return the graphics context of the buffer
	//--------------------------------------------------------------------------------------------------------------------
	final Graphics Tileset_GetBufferGraphics (int p_iLayer, int p_iImage)
	{
		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_GetBufferGraphics: Tileset player is not initialized");;
				return null;
			}

			if((p_iLayer < 0) || (p_iLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetBufferGraphics: p_iLayer invalid : " + p_iLayer));;
				return null;
			}

			if((p_iImage < 0) || (p_iImage >= k_TilesetLayerGraphicsCOUNT))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_GetBufferGraphics: p_iImage invalid : " + p_iImage));;
				return null;
			}
		}

		return s_TilesetLayerGraphics[p_iLayer][p_iImage];
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Resets the variables that control what is redrawn to the buffer so that next draw paints entire buffer.
	/// &param nLayer Layer which needs to be redrawn
	//--------------------------------------------------------------------------------------------------------------------
	void Tileset_Refresh (int nLayer)
	{
	//Guijun
		int[] layerInfo = s_TilesetLayerInfo[nLayer];
		if(!s_bTilesetPlayerInitialized)
			return;

		layerInfo[k_TilesetLayerFirstTileX] = -1;
		layerInfo[k_TilesetLayerFirstTileY] = -1;
		layerInfo[k_TilesetLayerLastTileX]	 = -1;
		layerInfo[k_TilesetLayerLastTileY]	 = -1;
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Enables alpha effects on a given layer of the tileset.
	///
	/// &param effectLayer - The layer to which we want to apply the effect (must be on backbuffer!!!)
	/// &param alphaLayer - The layer which contains the alpha data.
	/// &param alphaEffect - The type of effect to apply.
	///
	/// &note To disable this effect, pass in -1 for the 1st and 2nd params
	///
	/// &note The effectLayer MUST BE ON THE BACKBUFFER!
	/// &note The alphaLayer's Sprite MUST BE CACHED AS AN INT[]!
	/// &see zJYLibConfig.sprite_useManualCacheRGBArrays
	///
	/// &note You need to enable this feature in the zJYLib configuration (disabled by default)
	/// &see zJYLibConfig.tileset_usePixelEffects
	//--------------------------------------------------------------------------------------------------------------------
	void Tileset_SetAlphaEffects (int effectLayer, int alphaLayer, int alphaEffect)
	{
		if (DevConfig.tileset_usePixelEffects)
		{
			s_TilesetEffectLayer = effectLayer;
			s_TilesetAlphaLayer  = alphaLayer;
			s_TilesetEffectType  = alphaEffect;
		}
		else
		{
			Utils.Dbg("Tileset_SetAlphaEffects: Effects are not enabled, you must set tileset_usePixelEffects to TRUE!");;
		}
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Edit the tilemap by setting one tile and/or its flags.
	///
	/// &param nLayer - The layer whose tilemap we want to modify.
	/// &param x - The x position on the tilemap where the tile you want to modify lies.
	/// &param y - The y position on the tilemap where the tile you want to modify lies.
	/// &param tile  - The tile value you want to set (-1 to not set)
	/// &param flags - The flip and rotation flags the tile should have (-1 to not set)
	//--------------------------------------------------------------------------------------------------------------------
	void Tileset_SetTile (int nLayer, int x, int y, int tile, int flags)
	{
	//Guijun
		int[] layerinfo = s_TilesetLayerInfo[nLayer];
		int[] s_TilesetInfo = this.s_TilesetInfo;
		if ((layerinfo[k_TilesetLayerFlag] & k_Flag_Origin) != 0)
		{
			y = layerinfo[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - y;
		};
	
		if (DevConfig.tileset_useTileShift)
		{
			x = x >> s_TilesetInfo[k_TilesetInfoTileWidthShift];
			y = y >> s_TilesetInfo[k_TilesetInfoTileHeightShift];
		}
		else
		{
			x = x / s_TilesetInfo[k_TilesetInfoTileWidth];
			y = y / s_TilesetInfo[k_TilesetInfoTileHeight];
		}

		if(true)
		{
			if(!s_bTilesetPlayerInitialized)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_SetTile: Tileset player is not initialized");;
				return;
			}

			if((nLayer < 0) || (nLayer >= s_TilesetMaxLayerCount))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_SetTile: layer invalid : " + nLayer));;
				return;
			}

			if((layerinfo[k_TilesetLayerInitialized] != 1)|| (layerinfo[k_TilesetLayerEnabled] != 1))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Tileset_SetTile: layer is not initialized or not enabled.");;
				return;
			}

			if((x < 0) || (x > layerinfo[k_TilesetLayerTileCountWidth]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_SetTile: x value out of bound [" + x + "]  0 <= x < " + layerinfo[k_TilesetLayerTileCountWidth]));;
				return;
			}

			if((y < 0) || (y > layerinfo[k_TilesetLayerTileCountHeight]))
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, ("Tileset_SetTile: y value out of bound [" + y + "]  0 <= y < " + layerinfo[k_TilesetLayerTileCountHeight]));;
				return;
			}
		}

		// Let's just use X as the index...
		x += (y * layerinfo[k_TilesetLayerTileCountWidth]);

		// Set the tile if not -1
		if (tile != -1)
		{
			if (DevConfig.tileset_useIndexAsShort)
			{
				cPack.Mem_SetShort(s_TilesetLayerData[nLayer][k_TilesetLayerDataMap], (x<<1), (short)tile);
			}
			else
			{
				s_TilesetLayerData[nLayer][k_TilesetLayerDataMap][x] = (byte)tile;
			}
		}

		// Set the flags if not -1
		if (flags != -1)
		{
			s_TilesetLayerData[nLayer][k_TilesetLayerDataFlip][x]  = (byte)flags;
		}
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	/// Paints a frame to the tileset dirty backbuffer. Care is taken to do this properly given that the dirty buffer is
	/// split into 1, 2, 3, or 4 sections depending on its current state.
	///
	/// This function only becomes useful when tileset_useLayerLastUpdatedArea is set because then the paint area is limited
	/// only to the actively updated buffer area which is usually very small (or non-existance if there is no motion).
	///
	/// &param nLayer - the layer to paint the object to.
	/// &param sprite - Sprite to use for drawing the frame.
	/// &param frame - The frame to paint.
	/// &param x - The X position in tileset world coordinates.
	/// &param y - The Y position in tileset world coordinates.
	/// &param flags - The flip and rotation flags the frame should have
	///
	/// &see zJYLibConfig.tileset_useLayerLastUpdatedArea
	/// &note When using this functionality it will only be useful when the tileset_useLayerLastUpdatedArea flag is enabled.
	//--------------------------------------------------------------------------------------------------------------------
	void Tileset_PaintSpriteFrame (int nLayer, zSprite sprite, int frame, int x, int y, int flags)
	{
	//GUijun

		int[] s_TilesetInfo = this.s_TilesetInfo;

		if (DevConfig.tileset_useLayerLastUpdatedArea)
		{
			// Nothing was updated on the backbuffer!! No need to draw :)
    		if (s_TilesetLayerLastUpdatedAreaIndex == 0)
    		    return;
    	}

		// accelerator
		int[] layerInfo = s_TilesetLayerInfo[nLayer];

        int fDx        = sprite.GetFrameMinX(frame);
        int fDy        = sprite.GetFrameMinY(frame);
        int destWidth  = sprite.GetFrameWidth(frame);
        int destHeight = sprite.GetFrameHeight(frame);


	//Guijun
		if ((layerInfo[k_TilesetLayerFlag] & k_Flag_Origin) != 0)
		{
			y = layerInfo[k_TilesetLayerHeight] - s_TilesetInfo[k_TilesetInfoDestHeight] - y;
		};
        
        
        

		int originX = layerInfo[k_TilesetLayerCamX];
        int originY = layerInfo[k_TilesetLayerCamY];

        // compute the offset from the tilegrid
        int tileDX = originX % s_TilesetInfo[k_TilesetInfoTileWidth];
        int tileDY = originY % s_TilesetInfo[k_TilesetInfoTileHeight];

        if (originX < 0)
		{
            tileDX = s_TilesetInfo[k_TilesetInfoTileWidth] + tileDX;
		}

        if (originY < 0)
        {
            tileDY = s_TilesetInfo[k_TilesetInfoTileHeight] + tileDY;
		}

        // adjust camera to grid
        originX -= tileDX;
        originY -= tileDY;

        // transform level coordinates to screen coordinates
        x -= originX;
        y -= originY;

		if (isFlag(nLayer, k_Flag_LimitCamX | k_Flag_WrappingX))
		{
			if (originX < 0)
			{
				originX = layerInfo[k_TilesetLayerCBWidth] + originX % layerInfo[k_TilesetLayerCBWidth];
			}
		}

		if (isFlag(nLayer, k_Flag_LimitCamY | k_Flag_WrappingY))
		{
			if (originY < 0)
			{
				originY = layerInfo[k_TilesetLayerCBHeight] + originY % layerInfo[k_TilesetLayerCBHeight];
			}
		}

		// compute camera origin pos in the backbuffer
		int camX0 = originX % layerInfo[k_TilesetLayerCBWidth];
		int camY0 = originY % layerInfo[k_TilesetLayerCBHeight];

        int camW0 = layerInfo[k_TilesetLayerCBWidth] - camX0;
        int camH0 = layerInfo[k_TilesetLayerCBHeight] - camY0;

        Graphics gDest = s_TilesetLayerGraphics[nLayer][k_TilesetLayerGraphicsCB];

		// preserve clip
		int	cx = GLLib.GetClipX(gDest);
        int	cy = GLLib.GetClipY(gDest);
		int	cw = GLLib.GetClipWidth(gDest);
		int	ch = GLLib.GetClipHeight(gDest);

		int passCount = 1;

		if (DevConfig.tileset_useLayerLastUpdatedArea)
		{
			passCount = s_TilesetLayerLastUpdatedAreaIndex;
		}
		//Guijun
		int[][] s_TilesetLayerLastUpdatedArea_nLayer = s_TilesetLayerLastUpdatedArea[nLayer];
		
        while (--passCount >= 0)
		{
    		int[] rect = null;

    		// compute the whole dirty area
    		if (DevConfig.tileset_useLayerLastUpdatedArea)
    		{
				// check if the obj is completly outside the clip area
    		    rect = s_TilesetLayerLastUpdatedArea_nLayer[passCount];

				// Check X Dimension
 				int areaX1 = rect[k_TilesetLayerAreaInfoX] - camX0;

                if (rect[k_TilesetLayerAreaInfoX] < camX0)
                {
                    areaX1 += layerInfo[k_TilesetLayerCBWidth];
				}

                int areaX2 = areaX1 + rect[k_TilesetLayerAreaInfoW];

                if (x + fDx >= areaX2 ||
                    x + fDx + destWidth < areaX1)
                {
                    continue;
                }

				// Check Y Dimension
                int areaY1 = rect[k_TilesetLayerAreaInfoY] - camY0;

                if (rect[k_TilesetLayerAreaInfoY] < camY0)
                {
                    areaY1 += layerInfo[k_TilesetLayerCBHeight];
				}

                int areaY2 = areaY1 + rect[k_TilesetLayerAreaInfoH];

                if (y + fDy >= areaY2 ||
                    y + fDy + destHeight < areaY1)
                {
                    continue;
                }
    		}

    		// 9|7 8
    		// -----
    		// 3|1 2
    		// 6|4 5

    		if (x + fDx < camW0)
    		{
                if (y + fDy < camH0)
                {
    				GLLib.SetClip(gDest, cx, cy, cw, ch);
    			    GLLib.ClipRect(gDest, camX0, camY0, camW0, camH0);
            		if (DevConfig.tileset_useLayerLastUpdatedArea)
            		{
            		    GLLib.ClipRect(gDest, rect[k_TilesetLayerAreaInfoX], rect[k_TilesetLayerAreaInfoY], rect[k_TilesetLayerAreaInfoW], rect[k_TilesetLayerAreaInfoH]);
            		}
    			    sprite.PaintFrame(gDest, frame, camX0 + x, camY0 + y, 0);
    			}

    			if (y + fDy + destHeight >= camH0 && camY0 != 0)
    			{
    				GLLib.SetClip(gDest, cx, cy, cw, ch);
    				GLLib.ClipRect(gDest, camX0, 0, camW0, camY0);
            		if (DevConfig.tileset_useLayerLastUpdatedArea)
            		{
            		    GLLib.ClipRect(gDest, rect[k_TilesetLayerAreaInfoX], rect[k_TilesetLayerAreaInfoY], rect[k_TilesetLayerAreaInfoW], rect[k_TilesetLayerAreaInfoH]);
            		}
    				sprite.PaintFrame(gDest, frame, camX0 + x, y - camH0, 0);
    			}
    		}

            if (x + fDx + destWidth >= camW0 && camX0 != 0)
            {
                if (y + fDy < camH0)
                {
    				GLLib.SetClip(gDest, cx, cy, cw, ch);
    			    GLLib.ClipRect(gDest, 0, camY0, camX0, camH0);
            		if (DevConfig.tileset_useLayerLastUpdatedArea)
            		{
            		    GLLib.ClipRect(gDest, rect[k_TilesetLayerAreaInfoX], rect[k_TilesetLayerAreaInfoY], rect[k_TilesetLayerAreaInfoW], rect[k_TilesetLayerAreaInfoH]);
            		}
    			    sprite.PaintFrame(gDest, frame, x - camW0, camY0 + y, 0);
    			}

    			if (y + fDy + destHeight >= camH0 && camY0 != 0)
    			{
    				GLLib.SetClip(gDest, cx, cy, cw, ch);
    				GLLib.ClipRect(gDest, 0,0, camX0, camY0);
            		if (DevConfig.tileset_useLayerLastUpdatedArea)
            		{
            		    GLLib.ClipRect(gDest, rect[k_TilesetLayerAreaInfoX], rect[k_TilesetLayerAreaInfoY], rect[k_TilesetLayerAreaInfoW], rect[k_TilesetLayerAreaInfoH]);
            		}
    				sprite.PaintFrame(gDest, frame, x - camW0, y - camH0, 0);
    			}
            }
        }
		// restore clip
		GLLib.SetClip(gDest, cx, cy, cw, ch);


	}
}
