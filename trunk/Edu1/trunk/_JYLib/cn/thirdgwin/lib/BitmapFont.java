package cn.thirdgwin.lib;
import java.io.*;
import javax.microedition.lcdui.*;		
//import android.R.integer;
//import android.text.StaticLayout;
	public class BitmapFont
	{
		final static int k_iSpaceCharWidth = 6;
		final static int k_iNormalCharWidth = 12;
		final static int k_iCharSpacing = 1;
		final static char k_cCanBreakLine = '|';
		
		public int m_iFontID;
		public int m_iFontHeight;
		public int m_iFontColor;
		public int m_iBorderColor;
		public int m_iSpaceCharWidth;
		public boolean m_hasBorder;
		/** 不透明 */
		public int m_curAlpha = 0xFF000000;
		/** 字库中总字数 */
		private int  m_iTotalChars;
		private char[] m_aEnumChars; //array stores all chars in game
		private String m_sEnumChars; //String stores all chars in game
		private int[] m_aiFontImageData; //array stores data of font's image
		private byte[] m_bFontWidth; // array stores width of all chars in game
		private byte[] m_abFontPackData; //array stores font data of all chars in game
		private int[] m_aiFontPackDataOffset;
		/** 每像素的位 */
		private int m_iBitPerPixel;
		/** 位转换为字节，需要shift的次数 */
		private int m_iBitToShift;
		/** 计算用 */
		private int m_iBitAND;
		/** 检查边框的位 */
		private int m_iBitToCheckBorder;
		private int m_iScale;
		private boolean m_isLoadDataBitmapFont = false;
		private int m_iCurrentPal;
		private boolean m_useGradualEffect = false;
		
		public static int s_iMaxCachePool = 0;
		public static int s_iCurCachePool;
		public static Image[] s_imgCachePool;
		/** 池中的字符 */
		public static String s_sCharsCachePool;
		public static int[] s_aiPalCachePool;
		private static int s_iLastID = 0;
		
		/**记录当前帧cache的font个数。（同一帧cache的数量超过最大值，则不再更新进入pool池.）
		 * 避免当池子上限不够用时导致每帧全部文字重新cache的问题。*/
		public static int s_iCurFrameCachedCount = 0;

		public BitmapFont ()
		{
			s_iLastID++;
			m_iFontID = s_iLastID;			
		}		

		public BitmapFont (String strPackageName)
		{
			s_iLastID++;
			m_iFontID = s_iLastID;
			LoadFont(strPackageName);
		}
		
		public boolean LoadFont (String strPackageName)
		{	 int i = 0;
			m_iFontColor = 0xff000000;
			m_iBorderColor = 0xff000000;
			m_hasBorder = true;
			m_iBitPerPixel = 1;
			m_iSpaceCharWidth = k_iSpaceCharWidth;

			if (strPackageName.charAt(0) != '/')
			{
				strPackageName = "/" + strPackageName;
			}
			try
			{
				if (DevConfig.sprite_debugErrors) System.out.println("Load data Bitmap font (" + m_iFontID + ")");
				InputStream packStream = Utils.GetResourceAsStream(strPackageName);
				if (packStream == null)
				{
					Utils.Dbg("--------------------------------------------------------------");
					Utils.Dbg("!!!Error!!! Can't load bitmap font data: " + strPackageName);
					Utils.Dbg("--------------------------------------------------------------");
					return false; // return value from read() method of InputStream
				}
				int iCurrValue = 0;
				m_iTotalChars = packStream.read(); //get num of character
				if (m_iTotalChars == -1)
				{
					return false;
				}
				iCurrValue = packStream.read();
				if (iCurrValue == -1)
				{
					return false;
				}
				m_iTotalChars = m_iTotalChars | (iCurrValue << 8);
			
				int m_iTotalChars = this.m_iTotalChars;
				//allocate mem
				m_aEnumChars = new char[m_iTotalChars];
				m_bFontWidth = new byte[m_iTotalChars];

				//get height of font
				m_iFontHeight = packStream.read();
				if (m_iFontHeight == -1)
				{
					return false;
				}
				int m_iFontHeight = this.m_iFontHeight;
				//allocate mem for decoded data buffer
				m_aiFontImageData = new int[m_iFontHeight * m_iFontHeight * 2];
				//get bits system
				m_iBitPerPixel = packStream.read();
				m_iBitToShift = 4 - m_iBitPerPixel;
				if (m_iBitPerPixel == 1)
				{
					m_iBitAND = 0x07;
					m_iBitToCheckBorder = 0x01;
					m_iScale = 0;
				}
				else
				{
					m_iBitAND = 0x03;
					m_iBitToCheckBorder = 0x03;
					m_iScale = 1;
				}
				//get data of each character
				m_aiFontPackDataOffset = new int[m_iTotalChars + 1];
				m_aiFontPackDataOffset[0] = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] abUnicodeBuffer = new byte[2];
				int iDataSize = 0;
				byte[] abTemp = new byte[1000];
				char[] m_aEnumChars = this.m_aEnumChars;
				int m_iBitToShift = this.m_iBitToShift;
				
				for (i = 0; i < m_iTotalChars; i++)
				{
					//Read Unicode of character
					iCurrValue = packStream.read(abUnicodeBuffer, 0, 2);
					if (iCurrValue == -1)
					{
						return false;
					}
					m_aEnumChars[i] = (char)((abUnicodeBuffer[0] & 0xFF) | ((abUnicodeBuffer[1] & 0xFF) << 8));
					//Read width of character in this font
					iCurrValue = packStream.read();
					if (iCurrValue == -1)
					{
						return false;
					}
					m_bFontWidth[i] = (byte)iCurrValue;
					//Read font data of character
					iDataSize = 1 + ((m_bFontWidth[i] * m_iFontHeight) >> m_iBitToShift);
					int sizeLeft = iDataSize;
					byte[] _abTemp = new byte[1000];
					while(sizeLeft > 0)
					{
						iCurrValue = packStream.read(_abTemp, 0, sizeLeft);
//						if(sizeLeft != iCurrValue){
//							System.out.println("===i===:"+i);
//						}
						System.arraycopy(_abTemp, 0, abTemp, iDataSize-sizeLeft, iCurrValue);
						sizeLeft -= iCurrValue;
					}
					
			
					m_aiFontPackDataOffset[i + 1] = m_aiFontPackDataOffset[i] + iDataSize;
					baos.write(abTemp, 0, iDataSize);
//					if (iCurrValue == -1)
//					{
//						return false;
//					}
				}
				packStream.close();
				m_abFontPackData = baos.toByteArray();
				baos.close();
				baos = null;
				m_sEnumChars = new String(m_aEnumChars);
				m_isLoadDataBitmapFont = true;
			}
			catch (Exception ex)
			{
				System.out.println("eeeeeeeeeex==" + ex.toString());
				return false;
			}
			return true;
		}

		public void SetColor (int iNewColor)
		{
			m_iFontColor = iNewColor | 0xFF000000;
		}

		public void SetBorderColor (int iNewColor)
		{
			if(iNewColor == -1){
				SetBorderHidden();
				return;
			}
			m_hasBorder = true;
			m_iBorderColor = iNewColor | 0xFF000000;
		}
		public final void SetGradualEffect(boolean bGradual)
		{
			m_useGradualEffect = bGradual;
		}
		public final boolean IsGradual()
		{
			return m_useGradualEffect; 
		}
		public void SetBorderHidden ()
		{
			m_hasBorder = false;
		}

		public int GetColor ()
		{
			return m_iFontColor;
		}

		public int DrawChar (Graphics g, char ch, int x, int y)
		{
			if (ch == ' ') return m_iSpaceCharWidth;
			if (ch == k_cCanBreakLine) return 0;
			if (DevConfig.sprite_useBitmapFontCachePool > 0 && s_iMaxCachePool > 0 && m_iCurrentPal >= 0)
			{
				return DrawCachePoolChar(g, ch, x, y);
			}
			else if (DevConfig.sprite_drawRGBTransparencyBug)
			{
				return DrawCharPixel(g, ch, x, y);
			}
			else
			{
				return DrawCharImage(g, ch, x, y);
			}
		}
		
		public void SetCurrentPalette (int cur)
		{
			m_iCurrentPal = cur;
		}
		
		private int DrawCachePoolChar (Graphics g, char ch, int x, int y)
		{
			if (!m_isLoadDataBitmapFont)
			{
				Utils.Err("ERROR: Not load data bitmap font yet");
				return 0;
			}
			/** 首先找到找到字符的索引值 */
			int iCharIndex = MapChar(ch);
			if (iCharIndex == -1)
			{
				return 0;
			}			
			/** 字符宽度 */
			int iCharWidth = m_bFontWidth[iCharIndex];
			
			// Clip剪裁，对于超出的剪裁区的绘制，直接返回.
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g))
			{			
				if ((x+iCharWidth<=g.getClipX())|| (x>g.getClipX()+g.getClipWidth()))return iCharWidth;
				if ((y+m_iFontHeight<=g.getClipY())|| (y>g.getClipY()+g.getClipHeight()))return iCharWidth;
			};
		
			String s_sCharsCachePool = this.s_sCharsCachePool;
			/** 先在Cache里面找 */
			int id = s_sCharsCachePool.indexOf(ch);	
			/** 调色板和字体合并为当前的Cache的ID,猜测 */
			int curID = m_iCurrentPal | (m_iFontID << 8);
			/** 如果本字符在缓存中，但是调色板不对则 */
			if (id >= 0 && s_aiPalCachePool[id] != curID)
			{	
				boolean cache = true;
				while (id >= 0 && id < s_iMaxCachePool)
				{
					/** 在缓存中继续向后找字符 */
					id = s_sCharsCachePool.indexOf(ch, id);
					/** 如果没找见，则直接退出本循环，如果找到了,???阅读进度 */
					if (id >= 0)
					{
						if (s_aiPalCachePool[id] == curID)
						{
							cache = false;
							break;
						}
						else
						{
							id++;
						}
					}											
				} 	
				if (cache) id = -1;								
			}
			if (id < 0)
				{
					DecodeFont(iCharIndex, m_aiFontImageData);
					int[] m_aiFontImageData = this.m_aiFontImageData;
					Image img = GLLib.CreateRGBImage(m_aiFontImageData,
							iCharWidth, m_iFontHeight, true);
					// 当前帧要绘制的文字数量超出了池的大小的话，就不再往缓冲里追加了
					if (s_iCurFrameCachedCount < s_iMaxCachePool)
					{
						s_iCurCachePool = (s_iCurCachePool + 1)	% s_iMaxCachePool;
						StringBuffer sb = new StringBuffer(s_sCharsCachePool);
						sb.setCharAt(s_iCurCachePool, ch);
						s_sCharsCachePool = this.s_sCharsCachePool = sb.toString();
						s_aiPalCachePool[s_iCurCachePool] = curID;
						s_iCurFrameCachedCount++;
						s_imgCachePool[s_iCurCachePool] = img;
					}
					g.drawImage(img, x, y, 0);
				}
			else
			{				
				g.drawImage(s_imgCachePool[id], x, y, 0);								
			}			
			return iCharWidth;
		}

		private int DrawCharImage (Graphics g, char ch, int x, int y)
		{
			if (!m_isLoadDataBitmapFont)
			{
				if (DevConfig.sprite_debugErrors) System.out.println("ERROR: Not load data bitmap font yet");
				return 0;
			}
			int iCharIndex = MapChar(ch);
			if (iCharIndex == -1)
			{
				return 0;
			}
			int iCharWidth = m_bFontWidth[iCharIndex];
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g))
			{			
				if ((x+iCharWidth<=g.getClipX())|| (x>g.getClipX()+g.getClipWidth()))return iCharWidth;
				if ((y+m_iFontHeight<=g.getClipY())|| (y>g.getClipY()+g.getClipHeight()))return iCharWidth;
			};
			DecodeFont(iCharIndex, m_aiFontImageData);

			GLLib.DrawRGB(g, m_aiFontImageData, 0, iCharWidth, x, y, iCharWidth, m_iFontHeight, true);
			return iCharWidth;
		}
		
		private int DrawCharPixel (Graphics g, char ch, int x, int y)
		{
			if (!m_isLoadDataBitmapFont)
			{
				if (DevConfig.sprite_debugErrors) System.out.println("ERROR: Not load data bitmap font yet");
				return 0;
			}
			int iCharIndex = MapChar(ch);
			if (iCharIndex == -1)
			{
				return 0;
			}
			int iCharWidth = m_bFontWidth[iCharIndex];
			if (DevConfig.JY_BITMAP_FONT_CHECK_CLIP && GLLib.IsClipValid(g))
			{
				if ((x+iCharWidth<=g.getClipX())|| (x>g.getClipX()+g.getClipWidth()))return iCharWidth;
				if ((y+m_iFontHeight<=g.getClipY())|| (y>g.getClipY()+g.getClipHeight()))return iCharWidth;
			};		
			int iCharSize = m_iFontHeight * iCharWidth;
			int iByteOfPixel = 0;
			int iBitIndex = 0;
			int iDrawingX = x;
			int iTempColor;
			iTempColor = g.getColor();
			//If font has no border . need to call setColor() only 1 time.
			g.setColor(m_iFontColor);
			int i;
			int iTypeOfPixel;
			int m_iBitToShift = this.m_iBitToShift;
			int m_iBitAND = this.m_iBitAND;
			int m_iScale = this.m_iScale;
			byte[] m_abFontPackData = this.m_abFontPackData;
			int[] m_aiFontPackDataOffset = this.m_aiFontPackDataOffset;
			int m_iBitToCheckBorder = this.m_iBitToCheckBorder;
			boolean m_hasBorder = this.m_hasBorder;
			int m_iBorderColor = this.m_iBorderColor;
			int m_iFontColor = this.m_iFontColor;
			for (i = 0; i < iCharSize; i++)
			{
				iByteOfPixel = i >> m_iBitToShift;
				iBitIndex = ((i & m_iBitAND) << m_iScale);
				iTypeOfPixel = (m_abFontPackData[m_aiFontPackDataOffset[iCharIndex] + iByteOfPixel] >> iBitIndex) & m_iBitToCheckBorder;
				--iTypeOfPixel;
				if (iTypeOfPixel == 0)
				{
					g.drawLine(iDrawingX, y, iDrawingX, y); //Draw 1 pixel
				}
				else if (m_hasBorder && (iTypeOfPixel > 0))
				{
					g.setColor(m_iBorderColor);
					g.drawLine(iDrawingX, y, iDrawingX, y); //Draw 1 pixel
					g.setColor(m_iFontColor);
				}
				iDrawingX++;
				if ((iDrawingX - x) >= iCharWidth)
				{
					iDrawingX = x;
					y++;
				}
			}
			g.setColor(iTempColor);
			return iCharWidth;
		}

		public int GetFontHeight ()
		{
			return m_iFontHeight;
		}

		public int GetCharWidth (char ch)
		{
			if (ch == ' ') return m_iSpaceCharWidth;
			if (ch == k_cCanBreakLine) return 0;
			int iCharIndex = MapChar(ch);

			if (iCharIndex == -1)
			{
				return 0;
			}
			return m_bFontWidth[iCharIndex];
		}

		private int MapChar (char ch)
		{
			return m_sEnumChars.indexOf(ch);
		}
		
		/**
		 * alpha : 0~255
		 * */
		public void setAlpha(int alpha){
			m_curAlpha = alpha << 24;
		}
		
		public void setDefaultAlpha(){
			m_curAlpha = 0xFF000000;
		}
		/**
		 * 渐变特效，在DecodeFont时使用，待优化
		 * @param color
		 * @param totalH
		 * @param curH
		 * @return
		 */
		private int getGraduaColor(int color,int totalH,int curH)
		{
			if(curH >= totalH)
				return color;
			
			int a = (color>>24) & 0xFF;
			int r = (color>>16) & 0xFF;
			int g = (color>>8) & 0xFF;
			int b = (color)&0xFF;
			
			int nr = r  * curH / totalH;
			int ng = g  * curH / totalH ;
			int nb = b  * curH / totalH;
			
			return (a<<24 | nr<<16 | ng << 8 | nb);
		}
		private void DecodeFont (int iCharID, int[] aiRGB)
		{
			int m_iFontHeight = this.m_iFontHeight;
			byte[] m_bFontWidth = this.m_bFontWidth;
			int iCharSize = m_iFontHeight * m_bFontWidth[iCharID];
			int iByteOfPixel = 0;
			int iBitIndex = 0;
			if (aiRGB == null)
			{
				aiRGB = new int[iCharSize];
			}
			int i;
			int iTypeOfPixel;
			int m_iBitToShift = this.m_iBitToShift;
			int m_iBitAND = this.m_iBitAND;
			int m_iScale = this.m_iScale;
			byte[] m_abFontPackData = this.m_abFontPackData;
			int[] m_aiFontPackDataOffset = this.m_aiFontPackDataOffset;
			int m_iFontColor = this.m_iFontColor;
			boolean m_useGradualEffect = this.m_useGradualEffect;
			boolean m_hasBorder = this.m_hasBorder;
			int m_iBorderColor = this.m_iBorderColor;
			int m_curAlpha = this.m_curAlpha;
			int color;
			for (i = iCharSize; --i >= 0;)
			{
				/** 第几个字符 */
				iByteOfPixel = i >> m_iBitToShift;
				iBitIndex = ((i & m_iBitAND) << m_iScale);
				iTypeOfPixel = (m_abFontPackData[m_aiFontPackDataOffset[iCharID] + iByteOfPixel] >> iBitIndex) & m_iBitToCheckBorder;
				--iTypeOfPixel;
				/** 有效像素点 */
				if (iTypeOfPixel == 0)
				{
					color = m_iFontColor;
					if(m_useGradualEffect)
					{
						color = getGraduaColor(color,m_iFontHeight,m_iFontHeight - i/m_bFontWidth[iCharID]);
					}
				}
				/** 边框 */
				else if (m_hasBorder && (iTypeOfPixel > 0))
				{
					color = m_iBorderColor;
				}
				/** 透明部分 */
				else
				{
					color = 0;
				}
				/**  */
				if ((m_curAlpha!=0xFF000000) && (color != 0)){
					color &= 0x00FFFFFF;
					color |= m_curAlpha;
				}
				aiRGB[i] = color;
			}
		}
	}


