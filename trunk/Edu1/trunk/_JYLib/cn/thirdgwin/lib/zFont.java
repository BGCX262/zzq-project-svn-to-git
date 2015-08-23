package cn.thirdgwin.lib;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class zFont {
	private BitmapFont m_bitmapFont;
	// 系统字相关变量======
	private Font m_systemFont;
	private int[] m_systemFontPalette = null;
	private int m_systemFontColor = 0xFFFFFF;
	private int m_systemShadowColor = -1;
	public static final int SYSTEM_FONT_SPACE_WIDTH = 6;
	public static final int SYSTEM_FONT_CHAR_SPACING = 1;
	// ====================
	private int m_iFontClipY1, m_iFontClipY2;
	private boolean m_isUseBitmapFont = false;
	
	public zFont(String file,int[] pal) {
		if(DevConfig.sprite_useBitmapFont ) {
			LoadBitmapFont(file);
			SetBitmapFontPalette(pal);
			InitBitmapFontCachePool();
		} else {
			InitSystemFont();
			if(pal != null)
			{
				setSystemFontPalette(pal);
			}
		}
	}

	private void LoadBitmapFont(String bitmapFontData) {
		m_isUseBitmapFont = true;
		m_bitmapFont = new BitmapFont(bitmapFontData);
		SetSpaceWidthToDefault();
		SetLineHeightToDefault();
		SetLineSpacingToDefault();
		SetCharSpacingToDefault();
		ResetFontClip();
	}

	// 系统字相关方法,参考：LoadBitmapFont ======
	private void InitSystemFont() {
		m_systemFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_SMALL);
		SetSpaceWidthToDefault();
		SetLineHeightToDefault();
		SetLineSpacingToDefault();
		SetCharSpacingToDefault();
		ResetFontClip();
	}
	
	public void InitSystemFont(int face,int style,int size)
		{
			m_systemFont = Font.getFont(face,style,size);
			SetSpaceWidthToDefault();
			SetLineHeightToDefault();
			SetLineSpacingToDefault();
			SetCharSpacingToDefault();
			ResetFontClip();			
		}
	
	
	/*
	 * 系统字的调色板，和位图字调色板一个结构(大小必须为2的倍数)， 偶数位是字的颜色，奇数位是阴影颜色(-1则不画阴影)。 如：int[] pal =
	 * { 0xFFFFFF,0, 0xFF0000,0xFFFFFF, 0xBF00AA,-1 };
	 */
	public void setSystemFontPalette(int[] pal) {
		m_systemFontPalette = pal;
		_palettes = pal.length >> 1;
	}

	public void SetSystemFontColor(int color)
	{
		m_systemFontColor = color;
	}
	public void SetSystemFontShadowColor(int color)
	{
		m_systemShadowColor = color;
	}
	public void drawSystemFontChar(char c, int x, int y) {
		drawSystemFontChar(GLLib.g, c, x, y);
	}

	public  void drawSystemFontChar(Graphics g, char c, int x, int y) {
		g.setFont(m_systemFont);

		if(m_systemFontPalette != null)
		{
			int shadowColor = m_systemFontPalette[_crt_pal * 2 + 1];
			if (shadowColor >= 0) {
				g.setColor(shadowColor);
				// g.drawChar(c, x+1, y, 0);
				// g.drawChar(c, x, y+1, 0);
				g.drawChar(c, x, y - 1, 0);
				// g.drawChar(c, x+2, y+1, 0);
			}
			g.setColor(m_systemFontPalette[_crt_pal * 2]);
			g.drawChar(c, x - 1, y - 2, 0);
		} else {
			int shadowColor = m_systemShadowColor;
			if (shadowColor >= 0) {
				g.setColor(shadowColor);
				// g.drawChar(c, x+1, y, 0);
				// g.drawChar(c, x, y+1, 0);
				g.drawChar(c, x, y - 1, 0);
				// g.drawChar(c, x+2, y+1, 0);
			}
			g.setColor(m_systemFontColor);
			g.drawChar(c, x - 1, y - 2, 0);
			
		}
		
		
		
		
	}

	// =============================
	public void setBitmapAlpha(int alpha) {
		m_bitmapFont.setAlpha(alpha);
	}

	public void setDefaultBitmapAlpha() {
		m_bitmapFont.setDefaultAlpha();
	}

	public  void SetFontClip(int y1, int y2) {
		m_iFontClipY1 = y1;
		m_iFontClipY2 = y2;
	}

	public  void ResetFontClip() {
		m_iFontClipY1 = 0;
		m_iFontClipY2 = GLLib.GetScreenHeight();
	}

	public void SetBitmapFontBorderHidden() {
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		if (m_bitmapFont != null) {
			m_bitmapFont.SetBorderHidden();
		}
	}
	public void SetBitmapFontGradualEffect(boolean bGradual) {
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		if (m_bitmapFont != null) {
			m_bitmapFont.SetGradualEffect(bGradual);
		}
	}
	public boolean BitmapFontIsGradual() {
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		if (m_bitmapFont != null) {
			return m_bitmapFont.IsGradual();
		}
		return false;
	}
	public void SetBitmapFontColor(int iNewColor) {
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		if (m_bitmapFont != null) {
			m_bitmapFont.SetCurrentPalette(-1);
			m_bitmapFont.SetColor(iNewColor);
		}
	}

	public void SetBitmapFontBorderColor(int iNewColor) {
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		if (m_bitmapFont != null) {
			m_bitmapFont.SetCurrentPalette(-1);
			m_bitmapFont.SetBorderColor(iNewColor);
		}
	}

	public void InitBitmapFontCachePool() {
		if (DevConfig.sprite_useBitmapFontCachePool > 0) {
			BitmapFont m_bitmapFont = this.m_bitmapFont;
//			if (iMaxPool > 0) {
				m_bitmapFont.s_iMaxCachePool = DevConfig.sprite_useBitmapFontCachePool;
				m_bitmapFont.s_imgCachePool = new Image[DevConfig.sprite_useBitmapFontCachePool];
				m_bitmapFont.s_aiPalCachePool = new int[DevConfig.sprite_useBitmapFontCachePool];
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < DevConfig.sprite_useBitmapFontCachePool; i++) {
					sb.append(" ");
				}
				m_bitmapFont.s_sCharsCachePool = sb.toString();

//			} else {
//				m_bitmapFont.s_iMaxCachePool = 0;
//				m_bitmapFont.s_imgCachePool = null;
//				m_bitmapFont.s_aiPalCachePool = null;
//				m_bitmapFont.s_sCharsCachePool = null;
//			}
			m_bitmapFont.s_iCurCachePool = 0;
			m_bitmapFont.s_iCurFrameCachedCount = 0;
		}
	}

	// ////////////////////////-------------
	private static int _old_pal;

	private int _palettes;
	private int _crt_pal;
	/**
	 * 得到字符串尺寸，调用UpdateStringSize()得到。
	 */
	public  static int _text_w;
	public  static int _text_h;

	/**
	 * 字体尺寸
	 */
	public int _nLineHeight; // 字体高度
	public int _nLineSpacing; // 行距
	public int _nCharSpacing; // 字距
	public int _nSpaceWidth; // 空格的宽度
	// For new text rendering
	public int _nFontAscent;

	// ------------------------------------------------------------------------------
	// Font Effects
	public boolean _bUnderline = false;
	public boolean _bBold = false;

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the current string width used by DrawString
	 * 
	 * @return The current string width
	 */
	public final static int GetCurrentStringWidth() {
		return _text_w;
	}

	/**
	 * Gets the current string height used by DrawString
	 * 
	 * @return The current string height
	 */
	public final static int GetCurrentStringHeight() {
		return _text_h;
	}

	/**
	 * / Sets the default values for font ascent, descent, height, space, / and
	 * line spacing, based on the data found in the sprite file.
	 */
	public void SetDefaultFontMetrics() {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			if (DevConfig.sprite_debugErrors) {
				if (!(m_bitmapFont != null))
					Utils.DBG_PrintStackTrace(
							false,
							"ERROR: Not load data bitmap font yet (m_bitmapFont == null). Do it like this:  sprite.m_bitmapFont = new BitmapFont(\"DataBitmapFont\")");
				;
			}

			_nFontAscent = 0;
			SetLineSpacingToDefault();
			SetSpaceWidthToDefault();
			SetCharSpacingToDefault();
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			_nFontAscent = 0;
			SetLineSpacingToDefault();
			SetSpaceWidthToDefault();
			SetCharSpacingToDefault();
		}
	}

	// ------------------------------------------------------------------------------
	// / Returns the current line spacing for the sprite
	// / &returns The current line spacing
	// ------------------------------------------------------------------------------
	public final int GetLineSpacing() {
		return _nLineSpacing;
	}

	// ------------------------------------------------------------------------------
	// / Sets the line spacing for the sprite
	// / &param spacing the line spacing to be set
	// ------------------------------------------------------------------------------
	public final void SetLineSpacing(int spacing) {
		_nLineSpacing = spacing;
	}

	// ------------------------------------------------------------------------------
	// / Reverts line spacing to the default for the sprite
	// ------------------------------------------------------------------------------
	private void SetLineSpacingToDefault() {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			_nLineSpacing = _nLineHeight >> 2;
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			_nLineSpacing = _nLineHeight >> 2;
		}
	}

	// ------------------------------------------------------------------------------
	// / Gets the font height of the sprite
	// / &returns Int font height
	// ------------------------------------------------------------------------------
	public final int GetFontHeight() {
		return _nLineHeight;
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Gets the current space width of the sprite
	// / &returns The space width
	// ------------------------------------------------------------------------------
	public final int GetSpaceWidth() {
		return _nSpaceWidth;
	}

	// ------------------------------------------------------------------------------
	// / Sets the space width of the sprite
	// / &param spacing the space width to set
	// ------------------------------------------------------------------------------
	public final void SetSpaceWidth(int spacing) {
		_nSpaceWidth = spacing;

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			m_bitmapFont.m_iSpaceCharWidth = _nSpaceWidth;
		}

	}

	// ------------------------------------------------------------------------------
	// / Reverts the space width to the default for the sprite
	// ------------------------------------------------------------------------------
	public  void SetSpaceWidthToDefault() {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			_nSpaceWidth = m_bitmapFont.k_iSpaceCharWidth;
			m_bitmapFont.m_iSpaceCharWidth = _nSpaceWidth;
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			_nSpaceWidth = SYSTEM_FONT_SPACE_WIDTH;
		}
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Gets the current character spacing for the sprite
	// / &returns The character spacing
	// ------------------------------------------------------------------------------
	public final int GetCharSpacing() {
		return _nCharSpacing;
	}

	// ------------------------------------------------------------------------------
	// / Sets the character spacing for the sprite
	// / &param spacing the character spacing
	// ------------------------------------------------------------------------------
	final void SetCharSpacing(int spacing) {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			if (spacing < 0)
				_nCharSpacing = 0;
			else
				_nCharSpacing = spacing;
		} else

		{
			_nCharSpacing = spacing;
		}
	}

	// ------------------------------------------------------------------------------
	// / Reverts the character spacing to default for the sprite
	// ------------------------------------------------------------------------------
	public void SetCharSpacingToDefault() {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			_nCharSpacing = m_bitmapFont.k_iCharSpacing;
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			_nCharSpacing = SYSTEM_FONT_CHAR_SPACING;
		}
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Gets the current line height of the sprite
	// / &returns The line height
	// ------------------------------------------------------------------------------
	public final int GetLineHeight() {
		return _nLineHeight;
	}

	// ------------------------------------------------------------------------------
	// / Sets the line height for the sprite
	// / &param nHeight the line height
	// ------------------------------------------------------------------------------
	public final void SetLineHeight(int nHeight) {
		_nLineHeight = nHeight;
	}

	// ------------------------------------------------------------------------------
	// / Reverts the line height to the default for the sprite
	// ------------------------------------------------------------------------------
	public void SetLineHeightToDefault() {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			_nLineHeight = m_bitmapFont.GetFontHeight();
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			// 如果外部设定使用动态获取系统字大小，则动态获取，否则使用config里设定的高度
			if (DevConfig.sprite_systemFontSmallHeight < 0) {
				_nLineHeight = javax.microedition.lcdui.Font.getFont(
						Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL)
						.getHeight() + 2; // 2 for shadow
			} else {
				_nLineHeight = DevConfig.sprite_systemFontSmallHeight + 2; // 2
																				// for
																				// shadow
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Gets a value that indicates whether this Font is underlined
	// / &returns The font is underlined
	// ------------------------------------------------------------------------------
	public final boolean GetUnderline() {
		return _bUnderline;
	}

	// ------------------------------------------------------------------------------
	// / Sets the underline for the font
	// / &param bUnderline the underline flag
	// ------------------------------------------------------------------------------
	public final void SetUnderline(boolean bUnderline) {
		_bUnderline = bUnderline;
	}

	// ------------------------------------------------------------------------------
	// / Gets a value that indicates whether this Font is Bold
	// / &returns The font is Bold
	// ------------------------------------------------------------------------------
	public final boolean GetBold() {
		return _bBold;
	}

	// ------------------------------------------------------------------------------
	// / Sets the bold for the font
	// / &param bBold the bold flag
	// ------------------------------------------------------------------------------
	final void SetBold(boolean bBold) {
		_bBold = bBold;
	}

	// ------------------------------------------------------------------------------
	// / Gets the pixel height given how many lines will be drawn.
	// / &param numLines - The number of lines the text will have.
	// / &return The height in pixels this will take.
	// ------------------------------------------------------------------------------
	public final int GetTextHeight(int numLines) {
		return (numLines * GetLineHeight())
				+ ((numLines - 1) * GetLineSpacing());
	}
	
	/**
	 * 指定的高度里面能显示几行文本。
	 * @param height 指定的高度
	 * @return 能显示几行文本。
	 */
	public final int GetTextLineShow(int height) {
		return (height / (GetLineHeight() + GetLineSpacing()));
	}

	// --------------------------------------------------------------------------------------------------------------------

	// private int[] nALetterRect = new int[4];

	// --------------------------------------------------------------------------------------------------------------------

	private static short[] _warpTextInfo;

	// Used only when sprite_bufferTextPageFormatting is TRUE
	private static final int WRAP_TEXT_FORMATTING_MASK_PALETTE = 0x0FFF;
	private static final int WRAP_TEXT_FORMATTING_FLAG_IS_BOLD = 0x1000;
	private static final int WRAP_TEXT_FORMATTING_FLAG_IS_UNDERLINED = 0x2000;

	/**
	 * 0 = & lines, 1 = end index for line, 2 = pixel width of line,... so 2 //
	 * entries per line.\n &see zFont.DrawPageB(Graphics_g_ String s, short[]
	 * info, int x, int // y, int startLine, int maxLines, int anchor) // / &see
	 * zFont.DrawPageB(Graphics_g_ String s, short[] info, int x, int // y, int
	 * startLine, int maxLines, int anchor, int maxChars)
	 * 
	 * @param s
	 *            The string to be wrapped
	 * @param width
	 *            The width of the area to wrap to
	 * @param height
	 *            The height of the area to wrap to (NOT USED!!)
	 * @return 0是总行数，接起第1位是本行结尾的文本位置，第2位是本行宽度。
	 */
	public short[] WraptextB(String s, int width, int height) {
		if (_warpTextInfo == null) {
			_warpTextInfo = new short[DevConfig.MAX_WRAP_TEXT_INFO];
		}

		int str_len = s.length();
		short lineSize = 0;
		short cnt = 1;

		char c;
		for (int i = 0; i < str_len; i++) {
			c = s.charAt(i);
			if (c == '\n') {
				_warpTextInfo[cnt++] = (short) i;
				_warpTextInfo[cnt++] = (short) lineSize;
				lineSize = 0;
				continue;
			} else if (DevConfig.sprite_fontBackslashChangePalette
					&& c == '\\') {
				i++;
				c = s.charAt(i);
				if (c == 'n') {
					_warpTextInfo[cnt++] = (short) (i + 1);
					_warpTextInfo[cnt++] = (short) lineSize;
					lineSize = 0;
				}
				continue;
			}
			lineSize += GetCharWidth(c) + GetCharSpacing();
			if (lineSize > width) {
				_warpTextInfo[cnt++] = (short) i;
				_warpTextInfo[cnt++] = (short) (lineSize - GetCharWidth(c) - GetCharSpacing());

				lineSize = 0;
				i--;
			}
		}
		_warpTextInfo[cnt++] = (short) str_len;
		_warpTextInfo[cnt++] = (short) lineSize;

		_warpTextInfo[0] = (short) (cnt >> 1);

		return _warpTextInfo;
	}

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * @param info
	 *            info is the page formatting data (from WraptextB)
	 * @param startLine
	 *            is the starting line of the page
	 * @param maxLines
	 *            is how many lines can fit on this page
	 * @return the number of characters present in this page &see short[]
	 *         WraptextB( String s, int width, int height)
	 */
	public static int GetPageCharSize(short[] info, int startLine, int maxLines) {
		int startIndex, endIndex;

		startLine = ((startLine) < (0) ? (0)
				: ((startLine) > (info[0]) ? (info[0]) : (startLine)));
		maxLines = ((maxLines) < (0) ? (0)
				: ((maxLines) > (info[0] - startLine) ? (info[0] - startLine)
						: (maxLines)));
		startIndex = (startLine == 0) ? 0 : info[2 * startLine - 1];
		endIndex = info[2 * (startLine + maxLines - 1) + 1];

		return (endIndex - startIndex);
	}

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * 给指定的文本在固定宽度内加上自动换行。
	 * 
	 * @param str
	 *            The byte string to be wrapped
	 * @param text_w
	 *            The width to be wrapped to
	 * @return The wrapped string
	 */
	public final byte[] TextFitToFixedWidth(byte[] str, int text_w) {
		String s = TextFitToFixedWidth(new String(str), text_w);

		return s.getBytes();
	}

	/**
	 * 给指定的文本在固定宽度内加上自动换行。
	 * 
	 * @param str
	 *            The string to be wrapped
	 * @param text_w
	 *            The width to be wrapped to
	 * @return The wrapped string
	 */
	public String TextFitToFixedWidth(String str, int text_w) {
		String string_ret = "";

		int lastEnter = 0;
		// kinba
		int str_length = str.length();
		// kinba

		short[] ret = WraptextB(str, text_w, 0);

		for (int i = 0; i < ret[0]; i++) {
			if (lastEnter != 0
					&& (lastEnter >= str_length || str.charAt(lastEnter) != '\n')) {
				string_ret += "\n";
			}
			string_ret += str.substring(lastEnter, ret[(i << 1) + 1]);

			lastEnter = ret[(i << 1) + 1];

		}

		return string_ret;
	}

	/**
	 * Draws a string with appropriate newline characters and wrapping //
	 * information as a page. // / Meant to be used with WrapTextB.
	 * 
	 * @param g
	 *            The graphics context
	 * @param s
	 *            The string to be drawn
	 * @param info
	 *            The wrapping information for the string
	 * @param x
	 *            X coordinate to be drawn to
	 * @param y
	 *            Y coordinate to be drawn to
	 * @param startLine
	 *            The starting line to begin drawing from
	 * @param maxLines
	 *            The maximum number of lines to draw
	 * @param anchor
	 *            The anchor flags to be used for drawing
	 */
	public final void DrawPageB(Graphics g, String s, short[] info, int x, int y,
			int startLine, int maxLines, int anchor) {
		DrawPageB(g, s, info, x, y, startLine, maxLines, anchor, -1);
	}

	/**
	 * Draws a string with appropriate newline characters and wrapping
	 * information as a page. Meant to be used with WrapTextB.
	 * 
	 * @param g
	 *            g The graphics context
	 * @param s
	 *            The string to be drawn
	 * @param info
	 *            info The wrapping information for the string
	 * @param x
	 *            The X coordinate to be drawn to
	 * @param y
	 *            The Y coordinate to be drawn to
	 * @param startLine
	 *            The starting line to begin drawing from
	 * @param maxLines
	 *            The maximum number of lines to draw
	 * @param anchor
	 *            The anchor flags to be used for drawing
	 * @param maxChars
	 *            will limit the number of characters draw on the page
	 */
	public void DrawPageB(Graphics g, String s, short[] info, int x, int y,
			int startLine, int maxLines, int anchor, int maxChars) {
		// Count lines...
		int s_len = s.length(); // kinba
		int lines = info[0];
		int maxchar = GetLineHeight();

		if (maxLines == -1) {
			maxLines = lines;
		}

		if (startLine + maxLines > lines) {
			maxLines = lines - startLine;
		}

		int th = GetLineSpacing() + maxchar;

		if ((anchor & Graphics.BOTTOM) != 0) {
			y -= (th * (maxLines - 1));
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y -= (th * (maxLines - 1)) >> 1;
		}

		_old_pal = _crt_pal;
		boolean old_bold = _bBold;
		boolean old_underline = _bUnderline;

		if (maxChars >= 0) {
			zFont._indexMax = (startLine > 0) ? info[(startLine - 1) * 2 + 1]
					: 0;

			zFont._indexMax += maxChars;
		}

		// Draw each line...
		int k = 0;
		for (int j = startLine; j < lines; j++, k++) {
			if (k > maxLines - 1)
				break;

			zFont._index1 = (j > 0) ? info[(j - 1) * 2 + 1] : 0;
			zFont._index2 = info[j * 2 + 1];

			// TO CHECK WHY index1 < s.length ???
			if (zFont._index1 < s_len && s.charAt(zFont._index1) == '\n')
				zFont._index1++;

			int xx = x;
			int yy = y + k * th;

			if ((anchor & (Graphics.RIGHT | Graphics.HCENTER | Graphics.BOTTOM | Graphics.VCENTER)) != 0) {
				if ((anchor & Graphics.RIGHT) != 0)
					xx -= info[(j + 1) * 2];
				else if ((anchor & Graphics.HCENTER) != 0)
					xx -= info[(j + 1) * 2] >> 1;

				if ((anchor & Graphics.BOTTOM) != 0) {
					if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
						yy -= GetLineHeight();
					} else
					// if(zJYLibConfig.sprite_useSystemFont)
					{
						yy -= GetLineHeight();
					}
				} else if ((anchor & Graphics.VCENTER) != 0) {

					if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
						yy -= GetLineHeight() >> 1;
					} else
					// if(zJYLibConfig.sprite_useSystemFont)
					{
						yy -= GetLineHeight() >> 1;
					}

				}
			}
			DrawString(g, s, xx, yy, 0, false);
		}

		// Disable substring...
		zFont._index1 = -1;
		zFont._index2 = -1;
		zFont._indexMax = -1;

		_crt_pal = _old_pal;

		if (DevConfig.sprite_useDrawStringSleep) {
			try {
				Thread.sleep(DevConfig.SLEEP_DRAWSTRINGB);
			} catch (Exception e) {
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------

	private static int _index1 = -1;
	private static int _index2 = -1;
	private static int _indexMax = -1;

	// ------------------------------------------------------------------------------
	// / Set the current substring
	// / &param i1 index 1
	// / &param i2 index 2
	// ------------------------------------------------------------------------------
	public final static void SetSubString(int i1, int i2) {
		_index1 = i1;
		_index2 = i2;
	}

	// --------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Updates the current string size for the sprite given a byte array
	// containing a string
	// / &param bs a byte array containing a string
	// ------------------------------------------------------------------------------
	public final void UpdateStringSize(byte[] bs) {
		UpdateStringSize(new String(bs));
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Updates the current string size for the sprite given a string
	// / &param s the string
	// ------------------------------------------------------------------------------
	public final void UpdateStringSize(String s) {
		UpdateStringOrCharsSize(s, null);
	}

	public void UpdateStringOrCharsSize(String s, char[] charBuff) {
		if (s == null && charBuff == null)
			return;

		_text_w = 0;
		_text_h = GetLineHeight();
		int tw = 0;

		boolean isDrawString = s != null;
		int index1;
		int index2;
		index1 = ((_index1 >= 0) ? _index1 : 0);
		if (isDrawString) {
			index2 = ((_index2 >= 0) ? _index2 : s.length());
		} else {
			index2 = ((_index2 >= 0) ? _index2 : charBuff.length);
		}

		boolean bold = _bBold;

		for (int i = index1; i < index2; i++) {
			int c = ((isDrawString) ? (s).charAt((i)) : (charBuff)[(i)]);

			if (DevConfig.sprite_fontBackslashChangePalette && c == '\\') {
				i++;
				if (!sprite_fontDisableBackslashChangePaletteEffect) {
					if (((isDrawString) ? (s).charAt((i)) : (charBuff)[(i)]) == '^') {
						if (!DevConfig.sprite_fontDisableUnderlineBoldEffect)
							bold = !bold;
					}
				}

				if (((isDrawString) ? (s).charAt((i)) : (charBuff)[(i)]) == 'n') {
					if (tw > _text_w)
						_text_w = tw;
					tw = 0;
					_text_h += GetLineSpacing() + GetLineHeight();
				}

				continue;
			} else if (c > 32) {

			}

			else if (c == ' '
					|| (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont && c == m_bitmapFont.k_cCanBreakLine)) {
				if (c == ' ')
					tw += GetSpaceWidth();
				continue;
			}

			else if (c == ' ') {
				tw += GetSpaceWidth();
				continue;
			} else if (c == '\n') {
				if (tw > _text_w)
					_text_w = tw;
				tw = 0;
				_text_h += GetLineSpacing() + GetLineHeight();
				continue;
			} else // if (c < 32)
			{
				if (c == '\u0001') // auto change current palette
				{
					i++;
					// _crt_pal = CHAR_AT(s, charBuff, i);
					continue;
				} else if (c == '\u0002') // select fmodule
				{
					i++;
					c = ((isDrawString) ? (s).charAt((i)) : (charBuff)[(i)]);
				} else
					continue;
			}

			tw += GetCharWidth(((isDrawString) ? (s).charAt((i))
					: (charBuff)[(i)])) + GetCharSpacing();
			if (bold) {
				tw++;
			}
		}

		if (tw > _text_w) {
			_text_w = tw;
		}

		if (_text_w > 0) {
			_text_w -= GetCharSpacing();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------
	// / Draws text given a byte array containing a string
	// / &param g The graphics context
	// / &param bs A byte array containing a string
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param maxIndex is the size in characters to limit this string (used
	// for typewriter effect)
	// ------------------------------------------------------------------------------

	public final void DrawString(Graphics g, byte[] bs, int x, int y, int anchor,
			int maxIndex) {
		_indexMax = maxIndex;
		DrawString(g, new String(bs), x, y, anchor, true);
		_indexMax = -1;
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a string
	// / &param g The graphics context
	// / &param s The string to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param maxIndex is the size in characters to limit this string (used
	// for typewriter effect)
	// ------------------------------------------------------------------------------
	public final void DrawString(Graphics g, String s, int x, int y, int anchor,
			int maxIndex) {
		_indexMax = maxIndex;
		DrawString(g, s, x, y, anchor, true);
		_indexMax = -1;
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a byte array containing a string
	// / &param g The graphics context
	// / &param bs A byte array containing a string
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// ------------------------------------------------------------------------------
	public final void DrawString(Graphics g, byte[] bs, int x, int y, int anchor) {
		DrawString(g, new String(bs), x, y, anchor, true);
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a string
	// / &param g The graphics context
	// / &param s The string to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// ------------------------------------------------------------------------------
	public final void DrawString(Graphics g, String s, int x, int y, int anchor) {
		DrawString(g, s, x, y, anchor, true);
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a byte array containing a string
	// / &param g The graphics context
	// / &param bs A byte array containing a string
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param restorecol Boolean determining if the currently set font palette
	// be restored after this operation
	// ------------------------------------------------------------------------------
	public final void DrawString(Graphics g, byte[] bs, int x, int y, int anchor,
			boolean restorecol) {
		DrawString(g, new String(bs), x, y, anchor, restorecol);
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a string
	// / &param g The graphics context
	// / &param s The string to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param restorecol Boolean determining if the currently set font palette
	// be restored after this operation
	// ------------------------------------------------------------------------------
	public void DrawString(Graphics g, String s, int x, int y, int anchor,
			boolean restorecol) {
		DrawStringOrChars(g, s, null, x, y, anchor, restorecol);
	}

	public final void DrawStringOrChars(Graphics g, String s, char[] charBuff, int x, int y,
			int anchor, boolean restorecol) {
		GLLib.Profiler_BeginNamedEvent("DrawString");

		if (s == null && charBuff == null)
			return;

		y += _nFontAscent;

		boolean isDrawString = (s != null);

		UpdateStringOrCharsSize(s, charBuff);

		if ((anchor & (Graphics.RIGHT | Graphics.HCENTER | Graphics.BOTTOM | Graphics.VCENTER)) != 0) {
			if ((anchor & Graphics.RIGHT) != 0)
				x -= _text_w;
			else if ((anchor & Graphics.HCENTER) != 0)
				x -= _text_w >> 1;
			if ((anchor & Graphics.BOTTOM) != 0)
				y -= _text_h;
			else if ((anchor & Graphics.VCENTER) != 0)
				y -= _text_h >> 1;
		}

		int xx = x;
		int yy = y;
		int ux, um, uc;

		if (restorecol)
			_old_pal = _crt_pal;

		int index1;
		int index2;
		index1 = ((_index1 >= 0) ? _index1 : 0);
		if (isDrawString) {
			index2 = ((_index2 >= 0) ? _index2 : s.length());
		} else {
			index2 = ((_index2 >= 0) ? _index2 : charBuff.length);
		}

		if (_indexMax >= 0) {
			if (index2 > _indexMax) {
				index2 = _indexMax;
			}
		}

		// boolean bTraceFirst = false;
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		int m_iFontClipY1 = this.m_iFontClipY1;
		int m_iFontClipY2 = this.m_iFontClipY2;
		boolean _bUnderline = this._bUnderline;
		boolean _bBold = this._bBold;
		boolean m_isUseBitmapFont = this.m_isUseBitmapFont;
		for (int i = index1; i < index2; i++) {
			int c = ((isDrawString) ? (s).charAt((i)) : (charBuff)[(i)]);

			if (DevConfig.sprite_fontBackslashChangePalette && c == '\\') {
				i++;

				if (!sprite_fontDisableBackslashChangePaletteEffect) {
					int c2 = ((isDrawString) ? (s).charAt((i))
							: (charBuff)[(i)]);
					if (c2 == '_') {
						if (!DevConfig.sprite_fontDisableUnderlineBoldEffect)
							_bUnderline = this._bUnderline = !_bUnderline;
					} else if (c2 == '^') {
						if (!DevConfig.sprite_fontDisableUnderlineBoldEffect)
							_bBold = this._bBold = !_bBold;
					} else if (c2 == 'n') {
						xx = x;
						yy += GetLineSpacing() + GetLineHeight();
					} else {
						c = c2 & 0xFF;
						SetCurrentPalette(c - '0');
					}
				}

				continue;
			} else if (c > 32) {

			}

			else if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont
					&& c == m_bitmapFont.k_cCanBreakLine) {
				continue;
			}

			else if (c == ' ') {
				if (_bUnderline) {

					if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
						// ux = xx + ((GetSpaceWidth() -
						// m_bitmapFont.GetCharWidth('_')) >> 1);
						ux = xx + ((GetSpaceWidth() - GetCharWidth('_')) >> 1);
						if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2)
							m_bitmapFont.DrawChar(g, '_', ux, yy);
					} else
					// if(zJYLibConfig.sprite_useSystemFont)
					{
						ux = xx + ((GetSpaceWidth() - GetCharWidth('_')) >> 1);
						if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2) {
							drawSystemFontChar(g, '_', ux, yy);
						}
					}

				}
				xx += GetSpaceWidth();
				continue;
			} else if (c == '\n') {
				xx = x;
				yy += GetLineSpacing() + GetLineHeight();
				continue;
			} else // if (c < 32)
			{
				continue;
			}

			if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
				if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2)
					m_bitmapFont.DrawChar(g, ((isDrawString) ? (s).charAt((i))
							: (charBuff)[(i)]), xx, yy);
			} else
			// if(zJYLibConfig.sprite_useSystemFont)
			{
				if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2) {
					drawSystemFontChar(g, ((isDrawString) ? (s).charAt((i))
							: (charBuff)[(i)]), xx, yy);
				}
			}

			if (_bUnderline) {

				if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
					// ux = xx + ((m_bitmapFont.GetCharWidth(CHAR_AT(s,
					// charBuff, i)) - m_bitmapFont.GetCharWidth('_')) >>
					// 1);
					ux = xx
							+ ((GetCharWidth(((isDrawString) ? (s).charAt((i))
									: (charBuff)[(i)])) - GetCharWidth('_')) >> 1);
					if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2)
						m_bitmapFont.DrawChar(g, '_', ux, yy);
				} else
				// if(zJYLibConfig.sprite_useSystemFont)
				{
					ux = xx
							+ ((GetCharWidth(((isDrawString) ? (s).charAt((i))
									: (charBuff)[(i)])) - GetCharWidth('_')) >> 1);
					if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2) {
						drawSystemFontChar(g, '_', ux, yy);
					}

				}
			}

			if (_bBold) {
				xx++;

				if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
					if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2)
						m_bitmapFont.DrawChar(g,
								((isDrawString) ? (s).charAt((i))
										: (charBuff)[(i)]), xx, yy);
				} else
				// if(zJYLibConfig.sprite_useSystemFont)
				{
					if (yy >= m_iFontClipY1 && yy <= m_iFontClipY2) {
						drawSystemFontChar(g, ((isDrawString) ? (s).charAt((i))
								: (charBuff)[(i)]), xx, yy);
					}

				}

			}

			xx += GetCharWidth(((isDrawString) ? (s).charAt((i))
					: (charBuff)[(i)])) + GetCharSpacing();

		}

		if (restorecol) {
			_crt_pal = _old_pal;
		}

		GLLib.Profiler_EndNamedEvent();
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Tokenize a string given a separating token character
	// / &param s The string to be tokenized
	// / &param index1 The character position to begin tokenizing
	// / &param index2 The character position to stop tokenizing
	// / &param token The token character to be used
	// / &param indices an int array where the tokenization configuration is to
	// be stored
	// / &returns The number of lines resulting from tokenization
	// ------------------------------------------------------------------------------
	public static int StringTokenize(String s, int index1, int index2, char token,
			int[] indices) {
		int lines = 0;
		indices[0] = index1 - 1;

		for (int i = index1; i < index2; i++) {
			if (s.charAt(i) == token)
				indices[++lines] = i;
		}

		indices[++lines] = index2;
		return lines;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------
	// / Draws a byte array containing a string with appropriate newline
	// characters as a page
	// / &param g The graphics context
	// / &param s The byte array to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// ------------------------------------------------------------------------------
	public final void DrawPage(Graphics g, byte[] s, int x, int y, int anchor) {
		String str = new String(s);
		DrawPage(g, str, x, y, anchor, 0, str.length());
	}

	// ------------------------------------------------------------------------------
	// / Draws a string with appropriate newline characters as a page
	// / &param g The graphics context
	// / &param s The string to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// ------------------------------------------------------------------------------
	public final void DrawPage(Graphics g, String s, int x, int y, int anchor) {
		DrawPage(g, s, x, y, anchor, 0, s.length());
	}

	// --------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------
	// / Draws a string with appropriate newline characters as a page given a
	// start and end
	// / &param g The graphics context
	// / &param s The string to be drawn
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param start The character position to begin drawing at
	// / &param end The character position to stop drawing at
	// ------------------------------------------------------------------------------
	public void DrawPage(Graphics g, String s, int x, int y, int anchor, int start,
			int end) {
		// TODO: Do we really need to parse the string, and then draw it?
		// Should be able to do these at the same time and not have to allocate
		// any memory.

		// Check how large this array needs to be
		int lines = 2;

		for (int i = start; i < end; i++) {
			if (s.charAt(i) == '\n') {
				lines++;
			}
		}

		// Allocate
		int[] off = new int[lines];

		// Get line start/end indecies
		lines = StringTokenize(s, start, end, '\n', off);

		int th = GetLineSpacing() + GetLineHeight();

		if ((anchor & Graphics.BOTTOM) != 0)
			y -= (th * (lines - 1));
		else if ((anchor & Graphics.VCENTER) != 0)
			y -= (th * (lines - 1)) >> 1;

		// Draw each line...
		for (int j = 0; j < lines; j++) {
			_index1 = off[j] + 1;
			_index2 = off[j + 1];

			DrawString(g, s, x, y + j * th, anchor, false);
		}

		// Disable substring...
		_index1 = -1;
		_index2 = -1;
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Gets the width of the given character using this sprite
	// / &param c The character to be examined
	// / &returns The width of the character in pixels
	// ------------------------------------------------------------------------------
	public int getCharSize(char c) {
		if (c == ' ') {
			return GetSpaceWidth();
		}

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			return m_bitmapFont.GetCharWidth(c);
		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{
			return m_systemFont.charWidth(c) + 2;// 2 for shadow
		}
	}

	// ------------------------------------------------------------------------------
	// / Gets the width of the given character for each implementation
	// /
	// / Not quite the same as the public method getCharSize(c).
	// /
	// / &param c The id of the character to look up, either actual char or
	// frame.
	// / &returns The width of the character in pixels
	// ------------------------------------------------------------------------------
	public final int GetCharWidth(int k) {

		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {

			return m_bitmapFont.GetCharWidth((char) k);

		} else
		// if(zJYLibConfig.sprite_useSystemFont)
		{

			return m_systemFont.charWidth((char) k);

		}
	}

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * All possible chars for representing a number as a String only support 10
	 * decimal system now
	 */
	// final static char[] digits = {
	// '0' , '1' , '2' , '3' , '4' , '5' ,
	// '6' , '7' , '8' , '9' , 'a' , 'b' ,
	// 'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
	// 'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
	// 'o' , 'p' , 'q' , 'r' , 's' , 't' ,
	// 'u' , 'v' , 'w' , 'x' , 'y' , 'z'
	// };

	static final int k_itoa_buffer_size = 33;
	static char[] _itoa_buffer;

	// ------------------------------------------------------------------------------
	// / Draws text given a number
	// / &param g The graphics context
	// / &param num The number to be drawn
	// / &param minDigit The min digit, not enough digits to make up for 0
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// ------------------------------------------------------------------------------
	public final void DrawNumber(Graphics g, int num, int minDigit, int x, int y,
			int anchor) {
		DrawNumber(g, num, 10, minDigit, x, y, anchor, true);
	}

	// ------------------------------------------------------------------------------
	// / Draws text given a number
	// / &param g The graphics context
	// / &param num The number to be drawn
	// / &param radix The radix of number, only support 10 decimal system now
	// / &param minDigit The min digit, not enough digits to make up for 0
	// / &param x The X coordinate to be drawn to
	// / &param y The Y coordinate to be drawn to
	// / &param anchor The anchor flags to be used for drawing
	// / &param restorecol Boolean determining if the currently set font palette
	// be restored after this operation
	// ------------------------------------------------------------------------------
	public void DrawNumber(Graphics g, int num, int radix, int minDigit, int x, int y,
			int anchor, boolean restorecol) {
		int charPos = GetChars(_itoa_buffer, num, radix, minDigit);
		int oldIndex1 = _index1;
		int oldIndex2 = _index2;
		SetSubString(charPos, k_itoa_buffer_size);
		DrawStringOrChars(g, null, _itoa_buffer, x, y, anchor, restorecol);
		SetSubString(oldIndex1, oldIndex2);
	}

	public static int GetChars(char[] charBuf, int i, int radix, int minDigit) {
		if (charBuf == null) {
			_itoa_buffer = new char[k_itoa_buffer_size];
			charBuf = _itoa_buffer;
		}
		int len = charBuf.length;
		int charPos = len - 1;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			// charBuf[charPos--] = digits[-(i % radix)];
			charBuf[charPos--] = (char) ('0' - (i % radix));
			i = i / radix;
		}
		// charBuf[charPos] = digits[-i];
		charBuf[charPos] = (char) ('0' - i);

		while (len - charPos < minDigit) {
			charBuf[--charPos] = '0';
		}
		if (negative) {
			charBuf[--charPos] = '-';
		}

		return charPos;
	}

	// ------------------------------------------------------------------------------
	// / Updates the text size for the sprite given a number
	// / &param num The number to be drawn
	// / &param radix The radix of number
	// / &param minDigit The min digit, not enough digits to make up for 0
	// ------------------------------------------------------------------------------
	public void UpdateNumberSize(int num, int radix, int minDigit) {
		int charPos = GetChars(_itoa_buffer, num, radix, minDigit);
		int oldIndex1 = _index1;
		int oldIndex2 = _index2;
		SetSubString(charPos, k_itoa_buffer_size);
		UpdateStringOrCharsSize(null, _itoa_buffer);
		SetSubString(oldIndex1, oldIndex2);
	}

	// --------------------------------------------------------------------------------------------------------------------

	// ASprite_Palette.jpp
	// --------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// / Sets the current palette for the sprite
	// / &param pal the palette to be set
	// ------------------------------------------------------------------------------
	public void SetCurrentPalette(int pal) {
		if ((pal < _palettes) && (pal >= 0)) {
			_crt_pal = pal;
		} else {
			_crt_pal = 0;
			return;
		}
		int _crt_pal = this._crt_pal;
		BitmapFont m_bitmapFont = this.m_bitmapFont;
		int[] m_aiBitmapFontPal = this.m_aiBitmapFontPal;
		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			if (m_bitmapFont != null) {
				if (m_aiBitmapFontPal != null) {
					if (pal * 2 + 1 >= m_aiBitmapFontPal.length) {
						if (DevConfig.sprite_debugErrors)
							System.out
									.println("ERROR: SetCurrentPalette() set the "
											+ pal
											+ " palette but you only have "
											+ (m_aiBitmapFontPal.length >> 1)
											+ "palette. Please modify your palette array by SetBitmapFontPalette()");
					} else {
						m_bitmapFont.SetCurrentPalette(_crt_pal);
						m_bitmapFont.SetColor(m_aiBitmapFontPal[pal * 2]);
						m_bitmapFont
								.SetBorderColor(m_aiBitmapFontPal[pal * 2 + 1]);
					}
				} else {
					if (DevConfig.sprite_debugErrors)
						System.out
								.println("ERROR: Not call SetBitmapFontPalette() yet (m_aiBitmapFontPal == null)");
				}
			}
		}

	}

	int[] m_aiBitmapFontPal;

	public void SetBitmapFontPalette(int[] pal) {
		if (DevConfig.sprite_useBitmapFont && m_isUseBitmapFont) {
			m_aiBitmapFontPal = pal;

			// Set the number of palettes,
			// otherwise WrapText will not work correctly
			// because _crt_pal and palettes are always 0
			_palettes = ((m_aiBitmapFontPal.length) >> 1);
		}
	}

	// ------------------------------------------------------------------------------
	// / Gets the current palette for the sprite
	// / & returns the current palette
	// ------------------------------------------------------------------------------
	public int GetCurrentPalette() {
		return _crt_pal;
	}
	
	private static boolean sprite_fontDisableBackslashChangePaletteEffect = false;
	/**
	 * 设置是否支持文本换调色板,默认支持,画完之后要还原。
	 * @param enable
	 */
	void SetFontSlashChangePalette(boolean enable) {
		sprite_fontDisableBackslashChangePaletteEffect = !enable;
	}
}
