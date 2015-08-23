package cn.thirdgwin.lib;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class cPFX {
	//-------------------------------------------------------------------------------------------------
	// Pixel Effects from Catalin Vasile and David Flix, integrated into GLLib by Michael Zawadzki
	//
	// Also see GLPixEffects interface inside GLLibInterface.java
	//-------------------------------------------------------------------------------------------------

	/**
	 * \defgroup GLLib_pixEffects GLLib : Pixel Effects
	 *
	 * Pixel Processing Effects
	 *
	 * \ingroup GLLibMain
	 * &{
	 *
	 * <h2>Pixel Effect Types</h2>
	 * - Fullscreen Effects: Applied to the whole screen (or a windowed version).
	 * - Regional Effects:   Applied to an area on screen.
	 * - Sprite Effects:     Applied to sprite during module painting.
	 * - Tileset Effects:    Applied to tileset on circular buffer on buffer rendering.
	 *
	 * <h2>General Info</h2>
	 * There is a sample implementation of each effect in the GLLibSampleApplication
	 * <A HREF="https://svn01/vc/gllib_sampleapplication/sonyericsson/k800/trunk">https://svn01/vc/gllib_sampleapplication/sonyericsson/k800/trunk</A>
	 *
	 * Each effect is enabled independently within the zJYLibConfig file (see list below).
	 *
	 * If using the pixel effects GLLib.PFX_Init must be called before using any effect.
	 * Also, GLLib.PFX_Update must be called at the end of each update.
	 *
	 * <h2>Readable Screen Buffer</h2>
	 * <p>
	 * Some effects require reading the currently rendered screen. In order to support this
	 * a backbuffer must be enabled (zJYLibConfig.pfx_useScreenBuffer). If this is required for an effect
	 * it will be mentioned in the description of its zJYLibConfig flag. This is a very important property of an effect.
	 * </p><p>
	 * If your game will be supporting effects that require the readable buffer than you need to determine carefully
	 * how to handle the buffer, allocate once?, allocate for gameplay but free for menus?. If you can it is best to allocate
	 * once and keep it around. GLLib.PFX_Init will allocate the buffer, and you can release it using GLLib.PFX_Release.
	 * You can always reuse this buffer for other purposes, you can access it through: GLLib.PFX_GetBuffer and GLLib.PFX_GetReadableGraphics
	 * </p><p>
	 * The other complexity regarding using effects that require the readable buffer is that the engine must
	 * be notified during the UPDATE (before calling GLLib.PFX_Update) that the readable buffer is required. There are several ways to do this.
	 * Some effects, once enabled, will internally request the buffer themselves while enabled (like fullscreen blend and blur). Any effect in
	 * the GLPixEffects.k_EFFECTS_IN_FULL_SCREEN_MASK mask will auto-request the buffer IF IT IS ENABLED. For sprite effects, since the effects
	 * are just enabled locally, the dev needs to have some way to tell ahead of time (during the update) if one of these effects will be needed.
	 * In these cases you can use GLLib.PFX_EnableScreenBufferThisFrame.
	 * </p>
	 *
	 * <h2>Pixel Effects List</h2>
	 *
	 * - <STRONG>Fullscreen Effects</STRONG>
	 *     - <B>Blur</B>
	 *         - ID:     GLPixEffects.k_EFFECT_FULLSCREEN_BLUR
	 *         - Config: zJYLibConfig.pfx_useFullScreenEffectBlur
	 *         - Docs:   &ref PFX_FULLSCREEN_BLUR
	 *     - <B>Blend </B>(aka alpha fade in/out)
	 *         - ID:     GLPixEffects.k_EFFECT_FULLSCREEN_BLEND
	 *         - Config: zJYLibConfig.pfx_useFullScreenEffectBlend
	 *         - Docs:   &ref PFX_FULLSCREEN_BLEND
	 *     - <B>Additive</B>
	 *         - ID:     GLPixEffects.k_EFFECT_FULLSCREEN_ADDITIVE
	 *         - Config: zJYLibConfig.pfx_useFullScreenEffectAdditive
	 *     - <B>Subtractive</B>
	 *         - ID:     GLPixEffects.k_EFFECT_FULLSCREEN_SUBTRACTIVE
	 *         - Config: zJYLibConfig.pfx_useFullScreenEffectSubtractive
	 *     - <B>Multiplicative</B>
	 *         - ID:     GLPixEffects.k_EFFECT_FULLSCREEN_MULTIPLICATIVE
	 *         - Config: zJYLibConfig.pfx_useFullScreenEffectMultiplicative
	 *
	 * - <STRONG>Regional Effects</STRONG>
	 *     - <B>Glow</B>
	 *         - ID:     GLPixEffects.k_EFFECT_GLOW
	 *         - Config: zJYLibConfig.pfx_useEffectGlow
	 *         - Docs:   &ref PFX_REGIONAL_GLOW
	 *
	 * - <STRONG>Sprite Effects</STRONG>
	 *     - <B>Additive</B>
	 *         - ID:     GLPixEffects.k_EFFECT_ADDITIVE
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectAdditive
	 *         - Docs:
	 *     - <B>Multiplicative</B>:
	 *         - ID:     GLPixEffects.k_EFFECT_MULTIPLICATIVE
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectMultiplicative
	 *         - Docs:
	 *     - <B>Grayscale</B>
	 *         - ID:     GLPixEffects.k_EFFECT_GRAYSCALE
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectGrayscale
	 *         - Docs:   &ref PFX_SPRITE_GRAYSCALE
	 *     - <B>Shine</B>
	 *         - ID:     GLPixEffects.k_EFFECT_SHINE
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectShine
	 *         - Docs:   &ref PFX_SPRITE_SHINE
	 *     - <B>Blend</B>
	 *         - ID:     GLPixEffects.k_EFFECT_BLEND
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectBlend
	 *         - Docs:   &ref PFX_SPRITE_BLEND
	 *     - <B>Scale</B>
	 *         - ID:     GLPixEffects.k_EFFECT_SCALE
	 *         - Config: zJYLibConfig.pfx_useSpriteEffectScale
	 *         - Docs:   &ref PFX_SPRITE_SCALE
	 *
	 * - <STRONG>Tileset Effects</STRONG>
	 *     - zJYLibConfig: zJYLibConfig.tileset_usePixelEffects
	 *
	 */


	//-------------------------------------------------------------------------------------------------


	//-------------------------------------------------------------------------------------------------
	// Get/Set defines in case we want to support various 16bit pixel formats (NOT USED CURRENTLY)
	//-------------------------------------------------------------------------------------------------


	/*
	// TODO: Create these vars if needed eventually...


	*/

	//-------------------------------------------------------------------------------------------------
	/// Blurs right a given buffer by a given amount.
	///
	///!param size Size of the buffer to process
	///!param buf The buffer to process
	///!param amount Is the strength of the blur
	///!param initPixel starting color for the blur when missing pixels (at end)
	///!return final blurred pixel value
	//-------------------------------------------------------------------------------------------------
	private final static short PFX_BlurLBlock (int size, short buf[], int amount, int initPixel)
	{
		if(!DevConfig.pfx_enabled) return 0;

		int r1b1 = initPixel & 0xF0F;
		int g1   = initPixel & 0x0F0;
		amount = amount >> 4;

		for (int k = size - 1; k >= 0; k--)
		{
			short c2 = buf[k];

			int r2b2 = (c2) & 0xF0F;
			int g2   = (c2) & 0x0F0;

			r1b1 = (r2b2 + ((r1b1 - r2b2) * (amount) >> 3)) & 0xF0F;
			g1   = (g2   + ((g1 - g2)     * (amount) >> 3)) & 0x0F0;

			buf[k] = (short)(0xF000 | r1b1 | g1);
		}

		return (short)(r1b1 | g1);
	}


	//-------------------------------------------------------------------------------------------------
	/// Blurs left a given buffer by a given amount.
	///
	///!param size Size of the buffer to process
	///!param buf The buffer to process
	///!param amount Is the strength of the blur
	///!param initPixel starting color for the blur when missing pixels (at start)
	///!return final blurred pixel value
	//-------------------------------------------------------------------------------------------------
	private final static short PFX_BlurRBlock (int size, short buf[], int amount, int initPixel)
	{
		if(!DevConfig.pfx_enabled) return 0;
		int r1b1 = initPixel & 0xF0F;
		int g1   = initPixel & 0x0F0;
		amount = amount >> 4;

		for (int k = 0; k < size; k++)
		{
			short c2 = buf[k];

			int r2b2 = (c2) & 0xF0F;
			int g2   = (c2) & 0x0F0;

			r1b1 = (r2b2 + ((r1b1 - r2b2) * (amount) >> 3)) & 0xF0F;
			g1   = (g2   + ((g1 - g2)     * (amount) >> 3)) & 0x0F0;

			buf[k] = (short)(0xF000 | r1b1 | g1);
		}

		return (short)(r1b1 | g1);
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessAdditive (short src[], int srcStart, int srcOffset, int srcStep, short dst[], int outerStart, int outerStep, int outerEnd, int innerStart, int innerStep)
	{
		if (!DevConfig.pfx_enabled)return;
		int c1, c2, r1, g1, b1, r2, g2, b2, rr, gg, bb, srcOff;
		int dstOff = 0;

		for (int outer = outerStart; outer != outerEnd; outer += outerStep)
		{
			srcOff = srcStart + srcOffset;

			for (int inner = innerStart; inner >= 0; inner--)
			{
				c1 = src[srcOff];
				c2 = dst[dstOff];

				//color 1
				r1 = (c1) & 0xF00;
				g1 = (c1) & 0xF0;
				b1 = (c1) & 0xF;

				//color 2
				r2 = (c2) & 0xF00;
				g2 = (c2) & 0xF0;
				b2 = (c2) & 0xF;

				rr = r1 + r2;
				if (rr > 0xF00)
				{
					rr = 0xF00;
				}

				gg = g1 + g2;
				if (gg > 0xF0)
				{
					gg = 0xF0;
				}

				bb = b1 + b2;
				if (bb > 0xF)
				{
					bb = 0xF;
				}

				dst[dstOff] = (short)(0xF000 | rr | gg | bb);

				srcOff += innerStep;
				dstOff++;
			}

			srcStart += srcStep;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessMultiplicative (short src[], int srcStart, int srcOffset, int srcStep, short dst[], int outerStart, int outerStep, int outerEnd, int innerStart, int innerStep)
	{
		if(!DevConfig.pfx_enabled) return;
		int c1, c2, r1, g1, b1, r2, g2, b2, rr, gg, bb, srcOff;
		int dstOff = 0;

		for (int outer = outerStart; outer != outerEnd; outer += outerStep)
		{
			srcOff = srcStart + srcOffset;

			for (int inner = innerStart; inner >= 0; inner--)
			{
				c1 = src[srcOff];
				c2 = dst[dstOff];

				//color 2
				r1 = ((c1) & 0xF00) >> 8;
				g1 = ((c1) & 0xF0) >> 4;
				b1 = ((c1) & 0xF);

				//color 2
				r2 = ((c2) & 0xF00) >> 8;
				g2 = ((c2) & 0xF0) >> 4;
				b2 = ((c2) & 0xF);

				rr = (r1 * r2 >> 3) + r2;
				if (rr > 0xF)
				{
					rr = 0xF;
				}

				gg = (g1 * g2 >> 3) + g2;
				if (gg > 0xF)
				{
					gg = 0xF;
				}

				bb = (b1 * b2 >> 3) + b2;
				if (bb > 0xF)
				{
					bb = 0xF;
				}

				dst[dstOff] = (short)(0xF000 | (rr << 8) | (gg << 4) | bb);

				srcOff += innerStep;
				dstOff++;
			}

			srcStart += srcStep;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_AdditiveBlock (int size, short buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color >> 12) & 0xF00000);
		int g1 = ((color >>  8) & 0x00F000);
		int b1 = ((color >>  4) & 0x0000F0);

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0xF00);
			int g2 = ((c2) & 0x0F0);
			int b2 = ((c2) & 0x00F);

			r2 += r1;

			if (r2 > 0xF00)
			{
				r2 = 0xF00;
			}

			g2 += g1;

			if (g2 > 0xF0)
			{
				g2 = 0xF0;
			}

			b2 += b1;

			if (b2 > 0xF)
			{
				b2 = 0xF;
			}

			buf[k] = (short)(0xF000 | r2 | g2 | b2);
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_SubtractiveBlock (int size, short buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color) & 0x00FF0000) >> 20;
		int g1 = ((color) & 0x0000FF00) >> 12;
		int b1 = ((color) & 0x000000FF) >>  4;

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0xF00) >> 8;
			int g2 = ((c2) & 0x0F0) >> 4;
			int b2 = ((c2) & 0x00F);

			r2 -= r1;

			if (r2 < 0)
			{
				r2 = 0;
			}

			g2 -= g1;

			if (g2 > 0)
			{
				g2 = 0;
			}

			b2 -= b1;

			if (b2 > 0)
			{
				b2 = 0;
			}

			buf[k] = (short)(0xF000 | (r2 << 8) | (g2 << 4) | b2);
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_MultiplicativeBlock (int size, short buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color) & 0xFF0000) >> 20;
		int g1 = ((color) & 0x00FF00) >> 12;
		int b1 = ((color) & 0x0000FF) >>  4;

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0xF00) >> 8;
			int g2 = ((c2) & 0x0F0) >> 4;
			int b2 = ((c2) & 0x00F);

			int rr = (r1 * r2 >> 3) + r2;
			if (rr > 0xF)
			{
				rr = 0xF;
			}

			rr <<= 8;

			int gg = (g1 * g2 >> 3) + g2;
			if (gg > 0xF)
			{
				gg = 0xF;
			}

			gg <<= 4;

			int bb = (b1 * b2 >> 3) + b2;

			if (bb > 0xF)
			{
				bb = 0xF;
			}

			buf[k] = (short)(0xF000 | rr | gg | bb);
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static short[] PFX_ProcessGrayscaleEffect (short[] src, short[] dst, int size, int saturation, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		int offset = size;

		if (alpha < 0)
		{
			// Completely de-saturation... pure GRAYSCALE
			if (saturation <= 0)
			{
				while (offset != 0)
				{
					offset--;

					short c = src[offset];
					int   r = ((c & 0x0F00) >> 8) & 0x0F;

					dst[offset] = (short)((c & 0xFF00) | (r << 4) | (r));
				}
			}
			// Must compute saturated value
			else if (saturation < 255)
			{
				while (offset != 0)
				{
					offset--;

					int c = src[offset];
					int r = ((c & 0x0F00) >> 8) & 0xF;

					// Lerp G
					int p = ((c & 0x00F0) >> 4) & 0xF;
					int g = r + (((p - r) * saturation) >> 8 );

					// Lerp B
					p = (c & 0x000F);
					int b = r + (((p - r) * saturation) >> 8 );

					dst[offset] = (short)((c & 0xFF00) | ((g & 0xF) << 4) | (b & 0xF));
				}
			}
			// Saturated Completely... so just original...
			else
			{
				dst = src;
			}
		}
		else
		{
			s_PFX_hasAlpha = true;

			if (isAlphaSprite)
			{
				while (offset != 0)
				{
					offset--;

					short c = src[offset];
					int r = ((c & 0x0F00) >> 8) & 0x0F;
					int a = (src[offset] >> 12) & 0x0F;
					a = (a * alpha) >> 8;
					a = (a & 0x0F) << 12;

					dst[offset] = (short)(a | (r << 8) | (r << 4) | (r));
				}
			}
			else if (hasAlpha)
			{
				alpha = (alpha << 8) & 0xF000;

				while (offset != 0)
				{
					offset--;

					short c = src[offset];

					if ((c & 0x0F0F) != 0x0F0F)
					{
						int r = (c >> 8) & 0x0F;
						dst[offset] = (short)(alpha | (r << 8) | (r << 4) | (r));
					}
					else
					{
						dst[offset] = 0;
					}
				}
			}
			else
			{
				alpha = (alpha << 8) & 0xF000;

				while (offset != 0)
				{
					offset--;

					short c = src[offset];
					int r = (c >> 8) & 0x0F;
					dst[offset] = (short)(alpha | (r << 8) | (r << 4) | (r));
				}
			}
		}

		return dst;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessGlowBlock(short src[], short[] dst, int hspread, int vspread, int intensity, int w, int h, int offset)
	{
		if(!DevConfig.pfx_enabled) return;

		final int thresholdR = (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_RED]   & 0xF0) << 4;
		final int thresholdG = (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_GREEN] & 0xF0);
		final int thresholdB = (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_BLUE]  & 0xF0) >> 4;

		hspread = hspread >> 4;
		vspread = vspread >> 4;

		int r1b1 = 0;
		int g1   = 0;
		int r1, b1;
		int pos = 0;
		int pos2 = 0;

		if (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE] == GLPixEffects.k_THRESHOLD_FILTER_HIGH)
		{
			for (int j = 0; j < h; j++)
			{
				pos = j * w;
				pos2 = pos + offset;

				for (int i = w - 1; i >= 0; i--)
				{
					short c2 = src[pos2 + i];
					int r2b2 = (c2) & 0xF0F;
					int g2   = (c2) & 0x0F0;
					int b2   = (c2) & 0x00F;

					//filter out all shining colors
					if ((r2b2 > thresholdR) && (g2 > thresholdG) && (b2 > thresholdB))
					{
						r2b2 = 0;
						g2 = 0;
					}

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 3)) & 0xF0F;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 3)) & 0x0F0;

					dst[pos + i] = (short)(r1b1 | g1);
				}

				for (int i = 0; i < w; i++)
				{
					short c2 = dst[pos + i];

					int r2b2 = (c2) & 0xF0F;
					int g2   = (c2) & 0x0F0;

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 3)) & 0xF0F;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 3)) & 0x0F0;

					dst[pos + i] = (short)(r1b1 | g1);
				}
			}
		}
		else if(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE] == GLPixEffects.k_THRESHOLD_FILTER_LOW)
		{
			for (int j = 0; j < h; j++)
			{
				pos = j * w;
				pos2 = pos + offset;

				for (int i = w - 1; i >= 0; i--)
				{
					short c2 = src[pos2 + i];
					int r2b2 = (c2) & 0xF0F;
					int g2   = (c2) & 0x0F0;
					int b2   = (c2) & 0x00F;

					//filter out all shining colors
					if ((r2b2 < thresholdR) && (g2 < thresholdG) && (b2 < thresholdB))
					{
						r2b2 = 0;
						g2 = 0;
					}

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 3)) & 0xF0F;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 3)) & 0x0F0;

					dst[pos + i] = (short)(r1b1 | g1);
				}

				for (int i = 0; i < w; i++)
				{
					short c2 = dst[pos + i];

					int r2b2 = (c2) & 0xF0F;
					int g2   = (c2) & 0x0F0;

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 3)) & 0xF0F;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 3)) & 0x0F0;

					dst[pos + i] = (short)(r1b1 | g1);
				}
			}
		}
		else
		{
			Utils.Dbg("PFX_ProcessGlowBlock: Unknown GLOW thresholding type!");;
			return;
		}

		for (int i = 0; i < w; i++)
		{
			pos = i;
			for (int j = h - 1; j >= 0; j--)
			{
				short c2 = dst[pos];

				int r2b2 = (c2) & 0xF0F;
				int g2   = (c2) & 0x0F0;

				r1b1 = (r2b2 + ((r1b1 - r2b2) * (vspread) >> 3)) & 0xF0F;
				g1   = (g2 + ((g1 - g2)       * (vspread) >> 3)) & 0x0F0;

				dst[pos] = (short)(r1b1 | g1);
				pos += w;
			}

			pos -= w;

			for (int j = 0; j < h; j++)
			{
				short c2 = dst[pos];

				int r2b2 = (c2) & 0xF0F;
				int g2   = (c2) & 0x0F0;

				r1b1 = (r2b2 + ((r1b1 - r2b2) * (vspread) >> 3)) & 0xF0F;
				g1   = (g2   + ((g1 - g2)     * (vspread) >> 3)) & 0x0F0;

				dst[pos] =  (short)(r1b1 | g1);
				pos -= w;
			}
		}

		//aditive
		intensity = (intensity * intensity) >> s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_DIVISOR];

		for (int j = 0; j < h; j++)
		{
			pos = j * w;
			pos2 = pos + offset;

			for (int i = w - 1; i >= 0; i--)
			{
				short c1 = dst[pos + i];
				short c2 = src[pos2 + i];

				//additive part
				r1 = (c1 >> 8) & 0x0F;
				g1 = (c1 >> 4) & 0x0F;
				b1 = (c1)      & 0x0F;

				r1 = ((r1 * intensity) >> 8) << 8;
				g1 = ((g1 * intensity) >> 8) << 4;
				b1 = ((b1 * intensity) >> 8);

				int r2 = (c2) & 0xF00;
				int g2 = (c2) & 0x0F0;
				int b2 = (c2) & 0x00F;

				int rr = r1 + r2;
				if (rr > 0xF00)
				{
					rr = 0xF00;
				}

				int gg = g1 + g2;
				if (gg > 0x0F0)
				{
					gg = 0x0F0;
				}

				int bb = b1 + b2;
				if (bb > 0x00F)
				{
					bb = 0x00F;
				}

				dst[pos + i] = (short)(0xF000 | rr | gg | bb);
			}
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply blend
	//-------------------------------------------------------------------------------------------------
	private final static short[] PFX_ProcessBlend (short[] src, short[] dst, int size, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		alpha = ((alpha)<(0)?(0):((alpha)>(255)?(255):(alpha)));
		alpha = (alpha & 0xFF);

		// Sprite contains varying levels of alpha, must process alpha carefully.
		if (isAlphaSprite)
		{
			int a;

			while(size > 0)
			{
				size--;

				a = (src[size] >> 12) & 0xF;
				a = (a * alpha) >> 8;
				a = (a & 0xF)  << 12;

				dst[size] = (short)((src[size] & 0x0FFF) | a);
			}
		}
		// Sprite contains transparent pixels (alpha = 255 or 0), must check for (FF00FF)
		else if (hasAlpha)
		{
			alpha = (alpha << 8) & 0xF000;

			while(size > 0)
			{
				size--;

				if( (src[size] & 0xFFF) != 0xF0F )
				{
					dst[size] = (short)((src[size] & 0x0FFF) | alpha);
				}
				else
				{
					dst[size] = 0;
				}
			}
		}
		// Sprite contains no alpha... just apply our constant blend
		else
		{
			alpha = (alpha << 8) & 0xF000;

			while(size > 0)
			{
				size--;
				dst[size] = (short)((src[size] & 0x0FFF) | alpha);
			}
		}

		s_PFX_hasAlpha = true;
		return dst;
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply blend
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessBlendSimple (short[] buffer, int alpha, int size)
	{
		alpha = (alpha << 8) & 0xF000;

		// Process those pixels
		for(int iPixel = size-1; iPixel >= 0; iPixel--)
		{
			buffer[iPixel] = (short)(alpha | (buffer[iPixel] & 0x0FFF));
		}
	}

	//-------------------------------------------------------------------------------------------------
	/// Apply Scaling
	//-------------------------------------------------------------------------------------------------
	private final static short[] PFX_ProcessScaling (Graphics g, short[] src, short[] dst, int posX, int posY, int sWidth, int sHeight, int percent, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		int size = src.length;
		int offset = 0;
		int valH, valPos;

		int dWidth  = ((sWidth  * percent) / 100) + 1;
		int dHeight = ((sHeight * percent) / 100) + 1;

		int blockH    = dst.length / dWidth;
		int linesLeft = blockH;
		int currentY  = posY;

		// No alpha: don't touch alpha channel at all
		if(alpha < 0)
		{
			// aplha disabled
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						dst[offset++] = src[valPos];
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has NO transparency
		else if (!isAlphaSprite && !hasAlpha)
		{
			hasAlpha = true;
			alpha    = (alpha << 8) & 0xF000;

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						dst[offset++] = (short)(alpha | (src[valPos] & 0x0FFF));
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has TRANSPARENT pixels (FF00FF)
		else if (!isAlphaSprite && hasAlpha)
		{
			hasAlpha = true;
			alpha    = (alpha << 8) & 0xF000;

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						int color = src[valPos] & 0x0FFF;

						if(color != 0x0F0F)
						{
							dst[offset++] = (short)(alpha | color);
						}
						else
						{
							dst[offset++] = 0;
						}
					}
					else
					{
						dst[offset++] = 0;
					}
				}


				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has varying levels of alpha
		else
		{
			hasAlpha = true;

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						int a = (src[valPos] >> 12) & 0xF;
						a = (a * alpha) >> 8;
						a = (a & 0xF) << 12;

						dst[offset++] = (short)(a | (src[valPos] & 0x0FFF));
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}

		if (linesLeft != blockH)
		{
			PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH - linesLeft, hasAlpha);
		}

		// Performs all necesary blits internally and no need to return anything...
		return null;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static short[] PFX_ProcessShineEffect (Graphics g, short[] src, short[] dst, int posX, int posY, int sWidth, int sHeight, int flags, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		if (DevConfig.pfx_useSpriteEffectShine)
		{
			int shinePos   = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_POSX];
			int shineWidth = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_WIDTH];
			int shineColor = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_COLOR] & 0x00FFFFFF;
			int sPos = 0;
			int alphaValue;

			shineColor = (((shineColor & 0xFF000000) >> 16) & 0xF000) |
	   					 (((shineColor & 0x00FF0000) >> 12) & 0x0F00) |
						 (((shineColor & 0x0000FF00) >>  8) & 0x00F0) |
						 (((shineColor & 0x000000FF) >>  4) & 0x000F);

			// First just blit the normal module... we will blit the effect over it (return the effect)
			PFX_WritePixelData(g, src, 0, sWidth, posX, posY, sWidth, sHeight, true);

			if (!isAlphaSprite)
			{
				for (int j = 0; j < sHeight; j++)
				{
					for (int i = 0; i < sWidth; i++)
					{
						alphaValue = 0;

						if ((i > shinePos) && (i < (shinePos + shineWidth)) && ((src[sPos] & 0xF000) != 0) && ((src[sPos] & 0x0FFF) != 0))
						{
							alphaValue = 255 - ((((i - (shinePos + (shineWidth >> 1)))<0 ? -(i - (shinePos + (shineWidth >> 1))) : (i - (shinePos + (shineWidth >> 1)))) << 8) / (shineWidth >> 1));
						}

						dst[sPos] = (short)(((alphaValue << 12) & 0xF000) | shineColor);

						sPos++;
					}
				}
			}
			else
			{
				for (int j = 0; j < sHeight; j++)
				{
					for (int i = 0; i < sWidth; i++)
					{
						alphaValue = 0;

						if ((i > shinePos) && (i < (shinePos + shineWidth)) && ((src[sPos] & 0xF000) != 0) && ((src[sPos] & 0x0FFF) != 0))
						{
							alphaValue = 255 - ((((i - (shinePos + (shineWidth >> 1)))<0 ? -(i - (shinePos + (shineWidth >> 1))) : (i - (shinePos + (shineWidth >> 1)))) << 8) / (shineWidth >> 1));
						}

						int alphaFinal = (src[sPos] >> 12) & 0x0F;
						alphaFinal = (alphaFinal * alphaValue) >> 8;

						dst[sPos] = (short)(((alphaFinal << 12) & 0xF000) | shineColor);

						sPos++;
					}
				}
			}

			s_PFX_hasAlpha = true;
			return dst;
		}
		else
		{
			Utils.Dbg("PFX_ProcessShineEffect: Shine Effect is not enabled, you must set pfx_useSpriteEffectShine to TRUE!");;
			return null;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Computes an oscillating color, done per channel (ARGB).
	///
	///!param period - Fixed point period to use (higher = blink faster, smaller = blink slower)
	///!param c1 - 1st color.
	///!param c2 - 2nd color.
	///
	///!note: Requires calling PFX_Update to update timer.
	//-------------------------------------------------------------------------------------------------
	final static short PFX_ComputeBlinkingColor (int period, short c1, short c2)
	{
		if(!DevConfig.pfx_enabled) return 0;
		int angle = cMath.Math_FixedPoint_Multiply(s_PFX_timer * 360, period) >> 10;
		int i = cMath.Math_Sin(angle);
		i = ((i)<0 ? -(i) : (i));

		int a1 = (c1 >> 12) & 0xF;
		int r1 = (c1 >>  8) & 0xF;
		int g1 = (c1 >>  4) & 0xF;
		int b1 = (c1      ) & 0xF;

		int a2 = (c2 >> 12) & 0xF;
		int r2 = (c2 >>  8) & 0xF;
		int g2 = (c2 >>  4) & 0xF;
		int b2 = (c2      ) & 0xF;

		int a = a1 + cMath.Math_FixedPointToInt((a2 - a1) * i);
		int r = r1 + cMath.Math_FixedPointToInt((r2 - r1) * i);
		int g = g1 + cMath.Math_FixedPointToInt((g2 - g1) * i);
		int b = b1 + cMath.Math_FixedPointToInt((b2 - b1) * i);

		return (short)(((a & 0xF) << 12) | ((r & 0xF) << 8) | ((g & 0xF) << 4) | (b & 0xF));
	}


	//-------------------------------------------------------------------------------------------------
	/// Given a color this function returns the grayscale value the PFX would convert this color into.
	/// The PFX takes the RED channel and sets the BLUE and GREEN channels to the RED value.
	///
	///!param color - The color which we want to convert to a grayscale value
	///!return The grayscale value for this color according to the PFX.
	//-------------------------------------------------------------------------------------------------
	final static short PFX_GetGrayscaleColor (short color)
	{
		int red = ((color & 0x0F00) >> 8) & 0x0F;
		return (short)((color & 0xFF00) | (red << 4) | (red));
	}


	//-------------------------------------------------------------------------------------------------
	/// Blurs right a given buffer by a given amount.
	///
	///!param size Size of the buffer to process
	///!param buf The buffer to process
	///!param amount Is the strength of the blur
	///!param initPixel starting color for the blur when missing pixels (at end)
	///!return final blurred pixel value
	//-------------------------------------------------------------------------------------------------
	private final static int PFX_BlurLBlock (int size, int buf[], int amount, int initPixel)
	{
		if(!DevConfig.pfx_enabled) return 0;
		int r1b1 = initPixel & 0xFF00FF;
		int g1   = initPixel & 0x00FF00;

		for (int k = size - 1; k >= 0; k--)
		{
			int c2 = buf[k];

			int r2b2 = (c2) & 0xFF00FF;
			int g2   = (c2) & 0x00FF00;

			r1b1 = (r2b2 + ((r1b1 - r2b2) * (amount) >> 7)) & 0xFF00FF;
			g1   = (g2   + ((g1 - g2)     * (amount) >> 7)) & 0x00FF00;

			buf[k] = 0xFF000000 | r1b1 | g1;
		}

		return r1b1 | g1;
	}


	//-------------------------------------------------------------------------------------------------
	/// Blurs left a given buffer by a given amount.
	///
	///!param size Size of the buffer to process
	///!param buf The buffer to process
	///!param amount Is the strength of the blur
	///!param initPixel starting color for the blur when missing pixels (at start)
	///!return final blurred pixel value
	//-------------------------------------------------------------------------------------------------
	private final static int PFX_BlurRBlock (int size, int buf[], int amount, int initPixel)
	{
		int r1b1 = initPixel & 0xFF00FF;
		int g1   = initPixel & 0x00FF00;

		for (int k = 0; k < size; k++)
		{
			int c2 = buf[k];

			int r2b2 = (c2) & 0xFF00FF;
			int g2   = (c2) & 0x00FF00;

			r1b1 = (r2b2 + ((r1b1 - r2b2) * (amount) >> 7)) & 0xFF00FF;
			g1   = (g2   + ((g1 - g2)     * (amount) >> 7)) & 0x00FF00;

			buf[k] = 0xFF000000 | r1b1 | g1;
		}

		return r1b1 | g1;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessAdditive (int src[], int srcStart, int srcOffset, int srcStep, int dst[], int outerStart, int outerStep, int outerEnd, int innerStart, int innerStep)
	{
		if (!DevConfig.pfx_enabled)return;

		int c1, c2, r1, g1, b1, r2, g2, b2, rr, gg, bb, srcOff;
		int dstOff = 0;

		for (int outer = outerStart; outer != outerEnd; outer += outerStep)
		{
			srcOff = srcStart + srcOffset;

			for (int inner = innerStart; inner >= 0; inner--)
			{
				c1 = src[srcOff];
				c2 = dst[dstOff];

				//color 1
				r1 = (c1) & 0xFF0000;
				g1 = (c1) & 0xFF00;
				b1 = (c1) & 0xFF;

				//color 2
				r2 = (c2) & 0xFF0000;
				g2 = (c2) & 0xFF00;
				b2 = (c2) & 0xFF;

				rr = r1 + r2;
				if (rr > 0xFF0000)
				{
					rr = 0xFF0000;
				}

				gg = g1 + g2;
				if (gg > 0xFF00)
				{
					gg = 0xFF00;
				}

				bb = b1 + b2;
				if (bb > 0xFF)
				{
					bb = 0xFF;
				}

				dst[dstOff] = 0xFF000000 | rr | gg | bb;

				srcOff += innerStep;
				dstOff++;
			}

			srcStart += srcStep;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessMultiplicative (int src[], int srcStart, int srcOffset, int srcStep, int dst[], int outerStart, int outerStep, int outerEnd, int innerStart, int innerStep)
	{
		if (!DevConfig.pfx_enabled)return;

		int c1, c2, r1, g1, b1, r2, g2, b2, rr, gg, bb, srcOff;
		int dstOff = 0;

		for (int outer = outerStart; outer != outerEnd; outer += outerStep)
		{
			srcOff = srcStart + srcOffset;

			for (int inner = innerStart; inner >= 0; inner--)
			{
				c1 = src[srcOff];
				c2 = dst[dstOff];

				//color 2
				r1 = ((c1) & 0xFF0000) >> 16;
				g1 = ((c1) & 0xFF00) >> 8;
				b1 = ((c1) & 0xFF);

				//color 2
				r2 = ((c2) & 0xFF0000) >> 16;
				g2 = ((c2) & 0xFF00) >> 8;
				b2 = ((c2) & 0xFF);

				rr = (r1 * r2 >> 6) + r2;
				if (rr > 0xFF)
				{
					rr = 0xFF;
				}

				gg = (g1 * g2 >> 6) + g2;
				if (gg > 0xFF)
				{
					gg = 0xFF;
				}

				bb = (b1 * b2 >> 6) + b2;
				if (bb > 0xFF)
				{
					bb = 0xFF;
				}

				dst[dstOff] = 0xFF000000 | (rr << 16) | (gg << 8) | bb;

				srcOff += innerStep;
				dstOff++;
			}

			srcStart += srcStep;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_AdditiveBlock (int size, int buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color) & 0x00FF0000) >> 16;
		int g1 = ((color) & 0x0000FF00) >> 8;
		int b1 = ((color) & 0x000000FF);

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0x00FF0000) >> 16;
			int g2 = ((c2) & 0x0000FF00) >> 8;
			int b2 = ((c2) & 0x000000FF);

			r2 += r1;
			if (r2 > 0xFF)
			{
				r2 = 0xFF;
			}
			r2 <<= 16;

			g2 += g1;
			if (g2 > 0xFF)
			{
				g2 = 0xFF;
			}
			g2 <<= 8;

			b2 += b1;
			if (b2 > 0xFF)
			{
				b2 = 0xFF;
			}

			buf[k] = 0xFF000000 | r2 | g2 | b2;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_SubtractiveBlock (int size, int buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color) & 0x00FF0000) >> 16;
		int g1 = ((color) & 0x0000FF00) >> 8;
		int b1 = ((color) & 0x000000FF);

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0x00FF0000) >> 16;
			int g2 = ((c2) & 0x0000FF00) >> 8;
			int b2 = ((c2) & 0x000000FF);

			r2 -= r1;
			if (r2 < 0)
			{
				r2 = 0;
			}
			r2 <<= 16;

			g2 -= g1;
			if (g2 < 0)
			{
				g2 = 0;
			}
			g2 <<= 8;

			b2 -= b1;
			if (b2 < 0)
			{
				b2 = 0;
			}

			buf[k] = 0xFF000000 | r2 | g2 | b2;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_MultiplicativeBlock (int size, int buf[], int color)
	{
		if(!DevConfig.pfx_enabled) return;
		//color 2
		int r1 = ((color) & 0x00FF0000) >> 16;
		int g1 = ((color) & 0x0000FF00) >> 8;
		int b1 = ((color) & 0x000000FF);

		for (int k = size; --k >= 0;)
		{
			int c2 = buf[k];

			//color 2
			int r2 = ((c2) & 0x00FF0000) >> 16;
			int g2 = ((c2) & 0x0000FF00) >> 8;
			int b2 = ((c2) & 0x000000FF);

			int rr = (r1 * r2 >> 6) + r2;
			if (rr > 0xFF)
			{
				rr = 0xFF;
			}

			rr <<= 16;

			int gg = (g1 * g2 >> 6) + g2;
			if (gg > 0xFF)
			{
				gg = 0xFF;
			}

			gg <<= 8;

			int bb = (b1 * b2 >> 6) + b2;

			if (bb > 0xFF)
			{
				bb = 0xFF;
			}

			buf[k] = 0xFF000000 | rr | gg | bb;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static int[] PFX_ProcessGrayscaleEffect (int[] src, int[] dst, int size, int saturation, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{

		if(!DevConfig.pfx_enabled) return null;

		int offset = size;

		// No blending... just GRAYSCALE IT!
		if (alpha < 0)
		{
			// Completely de-saturation... pure GRAYSCALE
			if (saturation <= 0)
			{
				while (offset != 0)
				{
					offset--;

					int c = src[offset];
					int r = ((c & 0x00FF0000) >> 16) & 0xFF;

					dst[offset] = (c & 0xFFFF0000) | (r << 8) | (r);
				}
			}
			// Must compute saturated value
			else if (saturation < 255)
			{
				while (offset != 0)
				{
					offset--;

					int c = src[offset];
					int r = ((c & 0x00FF0000) >> 16) & 0xFF;

					dst[offset] = (c & 0xFFFF0000);

					// Lerp G
					int p = ((c & 0x0000FF00) >> 8) & 0xFF;
					int g = r + (((p - r) * saturation) >> 8 );
					dst[offset] |= ((g & 0xFF) << 8);

					// Lerp B
					p = (c & 0x000000FF);
					int b = r + (((p - r) * saturation) >> 8 );
					dst[offset] |= (b & 0xFF);
				}
			}
			// Saturated Completely... so just original...
			else
			{
				dst = src;
			}
		}
		// Blending Too...
		else
		{
			s_PFX_hasAlpha = true;

			if (isAlphaSprite)
			{
				while (offset != 0)
				{
					offset--;

					int c = src[offset];
					int r = ((c & 0x00FF0000) >> 16) & 0xFF;
					int a = (src[offset] >> 24) & 0xFF;
					a = (a * alpha) >> 8;
					a = (a & 0xFF) << 24;

					dst[offset] = a | (r << 16) | (r << 8) | (r);
				}
			}
			else if (hasAlpha)
			{
				alpha = (alpha << 24);

				while (offset != 0)
				{
					offset--;

					int c = src[offset];

					if ((c & 0xFF00FF) != 0xFF00FF)
					{
						int r = ((c & 0x00FF0000) >> 16) & 0xFF;
						dst[offset] = alpha | (r << 16) | (r << 8) | (r);
					}
					else
					{
						dst[offset] = 0;
					}
				}
			}
			else
			{
				alpha = (alpha << 24);

				while (offset != 0)
				{
					offset--;

					int c = src[offset];
					int r = ((c & 0x00FF0000) >> 16) & 0xFF;
					dst[offset] = alpha | (r << 16) | (r << 8) | (r);
				}
			}
		}


		return dst;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessGlowBlock(int src[], int[] dst, int hspread, int vspread, int intensity, int w, int h, int offset)
	{
		if(!DevConfig.pfx_enabled) return;

		final int thresholdR = s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_RED]   << 16;
		final int thresholdG = s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_GREEN] <<  8;
		final int thresholdB = s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_BLUE];

		int r1b1 = 0;
		int g1   = 0;
		int r1, b1;
		int pos = 0;
		int pos2 = 0;

		if (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE] == GLPixEffects.k_THRESHOLD_FILTER_HIGH)
		{
			for (int j = 0; j < h; j++)
			{
				pos = j * w;
				pos2 = pos + offset;

				for (int i = w - 1; i >= 0; i--)
				{
					int c2 = src[pos2 + i];
					int r2b2 = (c2) & 0xFF00FF;
					int g2   = (c2) & 0x00FF00;
					int b2   = (c2) & 0x0000FF;

					//filter out all shining colors
					if ((r2b2 > thresholdR) && (g2 > thresholdG) && (b2 > thresholdB))
					{
						r2b2 = 0;
						g2 = 0;
					}

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 7)) & 0xFF00FF;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 7)) & 0x00FF00;

					dst[pos + i] = r1b1 | g1;
				}

				for (int i = 0; i < w; i++)
				{
					int c2 = dst[pos + i];

					int r2b2 = (c2) & 0xFF00FF;
					int g2   = (c2) & 0x00FF00;

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 7)) & 0xFF00FF;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 7)) & 0x00FF00;

					dst[pos + i] = r1b1 | g1;
				}
			}
		}
		else if(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE] == GLPixEffects.k_THRESHOLD_FILTER_LOW)
		{
			for (int j = 0; j < h; j++)
			{
				pos = j * w;
				pos2 = pos + offset;

				for (int i = w - 1; i >= 0; i--)
				{
					int c2 = src[pos2 + i];
					int r2b2 = (c2) & 0xFF00FF;
					int g2   = (c2) & 0x00FF00;
					int b2   = (c2) & 0x0000FF;

					//filter out all shining colors
					if ((r2b2 < thresholdR) && (g2 < thresholdG) && (b2 < thresholdB))
					{
						r2b2 = 0;
						g2 = 0;
					}

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 7)) & 0xFF00FF;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 7)) & 0x00FF00;

					dst[pos + i] = r1b1 | g1;
				}

				for (int i = 0; i < w; i++)
				{
					int c2 = dst[pos + i];

					int r2b2 = (c2) & 0xFF00FF;
					int g2   = (c2) & 0x00FF00;

					r1b1 = (r2b2 + ((r1b1 - r2b2) * (hspread) >> 7)) & 0xFF00FF;
					g1   = (g2   + ((g1 - g2)     * (hspread) >> 7)) & 0x00FF00;

					dst[pos + i] = r1b1 | g1;
				}
			}
		}
		else
		{
			Utils.Dbg("PFX_ProcessGlowBlock: Unknown GLOW thresholding type!");;
			return;
		}

		for (int i = 0; i < w; i++)
		{
			pos = i;
			for (int j = h - 1; j >= 0; j--)
			{
				int c2 = dst[pos];

				int r2b2 = (c2) & 0xFF00FF;
				int g2   = (c2) & 0x00FF00;

				r1b1 = (r2b2 + ((r1b1 - r2b2) * (vspread) >> 7)) & 0xFF00FF;
				g1   = (g2 + ((g1 - g2)       * (vspread) >> 7)) & 0x00FF00;

				dst[pos] = r1b1 | g1;
				pos += w;
			}

			pos -= w;

			for (int j = 0; j < h; j++)
			{
				int c2 = dst[pos];

				int r2b2 = (c2) & 0xFF00FF;
				int g2   = (c2) & 0x00FF00;

				r1b1 = (r2b2 + ((r1b1 - r2b2) * (vspread) >> 7)) & 0xFF00FF;
				g1   = (g2   + ((g1 - g2)     * (vspread) >> 7)) & 0x00FF00;

				dst[pos] =  r1b1 | g1;
				pos -= w;
			}
		}

		//aditive
		intensity = (intensity * intensity) >> s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_DIVISOR];

		for (int j = 0; j < h; j++)
		{
			pos = j * w;
			pos2 = pos + offset;

			for (int i = w - 1; i >= 0; i--)
			{
				int c1 = dst[pos + i];
				int c2 = src[pos2 + i];

				//additive part
				r1 = (c1 >> 16) & 0xFF;
				g1 = (c1 >> 8)  & 0xFF;
				b1 = (c1)       & 0xFF;

				r1 = ((r1 * intensity) >> 8) << 16;
				g1 = ((g1 * intensity) >> 8) << 8;
				b1 = ((b1 * intensity) >> 8);

				int r2 = (c2) & 0xFF0000;
				int g2 = (c2) & 0xFF00;
				int b2 = (c2) & 0xFF;

				int rr = r1 + r2;
				if (rr > 0xFF0000)
				{
					rr = 0xFF0000;
				}

				int gg = g1 + g2;
				if (gg > 0xFF00)
				{
					gg = 0xFF00;
				}

				int bb = b1 + b2;
				if (bb > 0xFF)
				{
					bb = 0xFF;
				}

				dst[pos + i] = 0xFF000000 | rr | gg | bb;
			}
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply blend
	//-------------------------------------------------------------------------------------------------
	private final static int[] PFX_ProcessBlend (int[] src, int[] dst, int size, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		alpha = ((alpha)<(0)?(0):((alpha)>(255)?(255):(alpha)));
		alpha = (alpha & 0xFF);

		// Sprite contains varying levels of alpha, must process alpha carefully.
		if (isAlphaSprite)
		{
			int a;

			while(size > 0)
			{
				size--;

				a = (src[size] >> 24) & 0xFF;
				a = (a * alpha) >> 8;
				a = (a & 0xFF) << 24;

				dst[size] = (src[size] & 0x00FFFFFF) | a;
			}
		}
		// Sprite contains transparent pixels (alpha = 255 or 0), must check for (FF00FF)
		else if (hasAlpha)
		{
			alpha = alpha << 24;

			while(size > 0)
			{
				size--;

				if( (src[size] & 0xFFFFFF) != 0xFF00FF )
				{
					dst[size] = (src[size] & 0x00FFFFFF) | alpha;
				}
				else
				{
					dst[size] = 0;
				}
			}
		}
		// Sprite contains no alpha... just apply our constant blend
		else
		{
			alpha = alpha << 24;

			while(size > 0)
			{
				size--;
				dst[size] = (src[size] & 0x00FFFFFF) | alpha;
			}
		}

		s_PFX_hasAlpha = true;
		return dst;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ProcessBlendSimple (int[] buffer, int alpha, int size)
	{
		if(!DevConfig.pfx_enabled) return;
		alpha = (alpha << 24) & 0xFF000000;

		// Process those pixels
		for(int iPixel = size-1; iPixel >= 0; iPixel--)
		{
			buffer[iPixel] = alpha | (buffer[iPixel] & 0x00FFFFFF);
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply Scaling
	//-------------------------------------------------------------------------------------------------
	private final static int[] PFX_ProcessScaling (Graphics g, int[] src, int[] dst, int posX, int posY, int sWidth, int sHeight, int percent, int alpha, boolean hasAlpha, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;

		int size = src.length;
		int offset = 0;
		int valH, valPos;

		int dWidth  = ((sWidth  * percent) / 100) + 1;
		int dHeight = ((sHeight * percent) / 100) + 1;

		int blockH    = dst.length / dWidth;
		int linesLeft = blockH;
		int currentY  = posY;

		// No alpha: don't touch alpha channel at all
		if(alpha < 0)
		{
			// aplha disabled
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						dst[offset++] = src[valPos];
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has NO transparency
		else if (!isAlphaSprite && !hasAlpha)
		{
			hasAlpha = true;
			alpha    = (alpha << 24);

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						dst[offset++] = (alpha) | (src[valPos] & 0x00FFFFFF);
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has TRANSPARENT pixels (FF00FF)
		else if (!isAlphaSprite && hasAlpha)
		{
			hasAlpha = true;
			alpha    = (alpha << 24);

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						int color = src[valPos] & 0x00FFFFFF;

						if(color != 0x00FF00FF)
						{
							dst[offset++] = alpha | color;
						}
						else
						{
							dst[offset++] = 0;
						}
					}
					else
					{
						dst[offset++] = 0;
					}
				}


				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}
		// Want to set alpha, and source has varying levels of alpha
		else
		{
			hasAlpha = true;

			// apply alpha
			for (int j = 0; j < dHeight; j++)
			{
				valH = PFX_Scale_GetNum(j, sHeight, dHeight) * sWidth;

				for (int i = 0; i < dWidth; i++)
				{
					valPos = valH + PFX_Scale_GetNum(i, sWidth, dWidth);

					if (valPos < size)
					{
						int a = (src[valPos] >> 24) & 0xFF;
						a = (a * alpha) >> 8;
						a = (a & 0xFF) << 24;

						dst[offset++] = (a) | (src[valPos] & 0x00FFFFFF);
					}
					else
					{
						dst[offset++] = 0;
					}
				}

				linesLeft--;

				if (linesLeft == 0)
				{
					// Blit current RGB
					PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH, hasAlpha);

					currentY += blockH;
					linesLeft = blockH;
					offset    = 0;
				}
			}
		}

		if (linesLeft != blockH)
		{
			PFX_WritePixelData(g, dst, 0, dWidth, posX, currentY, dWidth, blockH - linesLeft, hasAlpha);
		}

		// Performs all necesary blits internally and no need to return anything...
		return null;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static int[] PFX_ProcessShineEffect (Graphics g, int[] src, int[] dst, int posX, int posY, int sWidth, int sHeight, int flags, boolean isAlphaSprite)
	{
		if(!DevConfig.pfx_enabled) return null;
		if (DevConfig.pfx_useSpriteEffectShine)
		{
			int       shinePos   = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_POSX];
			final int shineWidth = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_WIDTH];
			final int shineColor = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_COLOR] & 0x00FFFFFF;
			int shineShift = s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_MAX_VALUE_SHIFT];
			int shineMax   = (1 << shineShift) - 1;

			int sPos = 0;
			int alphaValue;

			if (s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_DISTANCE_ABSOLUTE] != 0)
			{
				shinePos -= posX;
				shinePos -= (shineWidth>>1);
			}

			// First just blit the normal module... we will blit the effect over it (return the effect)
			PFX_WritePixelData(g, src, 0, sWidth, posX, posY, sWidth, sHeight, true);

			if (!isAlphaSprite)
			{
				for (int j = 0; j < sHeight; j++)
				{
					for (int i = 0; i < sWidth; i++)
					{
						alphaValue = 0;

						if ((i > shinePos) && (i < (shinePos + shineWidth)) && ((src[sPos] & 0xFF000000) != 0))
						{
							alphaValue = shineMax - (((((i - (shinePos + (shineWidth >> 1)))<0 ? -(i - (shinePos + (shineWidth >> 1))) : (i - (shinePos + (shineWidth >> 1)))) << shineShift)-1) / (shineWidth >> 1));
						}

						dst[sPos] = ((alphaValue & 0xFF) << 24) | (shineColor);

						sPos++;
					}
				}
			}
			else
			{
				for (int j = 0; j < sHeight; j++)
				{
					for (int i = 0; i < sWidth; i++)
					{
						alphaValue = 0;

						if ((i > shinePos) && (i < (shinePos + shineWidth)) && ((src[sPos] & 0xFF000000) != 0))
						{
							alphaValue = shineMax - ((((i - (shinePos + (shineWidth >> 1)))<0 ? -(i - (shinePos + (shineWidth >> 1))) : (i - (shinePos + (shineWidth >> 1)))) << shineShift) / (shineWidth >> 1));
						}

						int alphaFinal = (src[sPos] >> 24) & 0xFF;
						alphaFinal = (alphaFinal * alphaValue) >> 8;

						dst[sPos] = ((alphaFinal & 0xFF) << 24) | (shineColor);

						sPos++;
					}
				}
			}

			s_PFX_hasAlpha = true;
			return dst;
		}
		else
		{
			Utils.Dbg("PFX_ProcessShineEffect: Shine Effect is not enabled, you must set pfx_useSpriteEffectShine to TRUE!");;
			return null;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Computes an oscillating color, done per channel (ARGB).
	///
	///!param period - Fixed point period to use (higher = blink faster, smaller = blink slower)
	///!param c1 - 1st color.
	///!param c2 - 2nd color.
	///
	///!note: Requires calling PFX_Update to update timer.
	//-------------------------------------------------------------------------------------------------
	final static int PFX_ComputeBlinkingColor (int period, int c1, int c2)
	{
		if(!DevConfig.pfx_enabled) return 0;
		int angle = cMath.Math_FixedPoint_Multiply(s_PFX_timer * 360, period) >> 10;
		int i = cMath.Math_Sin(angle);
		i = ((i)<0 ? -(i) : (i));

		int a1 = (c1 >> 24) & 0xFF;
		int r1 = (c1 >> 16) & 0xFF;
		int g1 = (c1 >> 8 ) & 0xFF;
		int b1 = (c1      ) & 0xFF;

		int a2 = (c2 >> 24) & 0xFF;
		int r2 = (c2 >> 16) & 0xFF;
		int g2 = (c2 >> 8 ) & 0xFF;
		int b2 = (c2      ) & 0xFF;

		int a = a1 + cMath.Math_FixedPointToInt((a2 - a1) * i);
		int r = r1 + cMath.Math_FixedPointToInt((r2 - r1) * i);
		int g = g1 + cMath.Math_FixedPointToInt((g2 - g1) * i);
		int b = b1 + cMath.Math_FixedPointToInt((b2 - b1) * i);

		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}

	//-------------------------------------------------------------------------------------------------
	/// Given an image as an int[] this method will apply an alpha gradient onto the alpha channel.
	///
	///!param pix - the array representing the image whose alpha channel will be modified
	///!param w - the width of the image
	///!param h - the height of the image
	///!param direction - the direction in which to apply the gradient, valid values are GLLib.LEFT, GLLib.RIGHT, GLLib.TOP, GLLib.BOTTOM.
	///!param startAlpha - initial alpha value to start from [0, 255]
	///!param endAlpha - final alpha value to end at [0, 255]
	///!param startBuffer - how many pixels to skip before starting the gradient.
	///                      If this value is NEGATIVE it will be treated as a PERCENTAGE and the buffer pixel size will be computed
	///                      using the image size in the gradient dimension and the percent.
	///!param endBuffer - how many pixels to skip before starting the gradient.
	///                      If this value is NEGATIVE it will be treated as a PERCENTAGE and the buffer pixel size will be computed
	///                      using the image size in the gradient dimension and the percent.
	///
	///!note TODO - The buffer parameters are currently not implemented yet.
	///!note Currently only 4 directions are supported.
	///
	//-------------------------------------------------------------------------------------------------
	final static void PFX_ApplyAlphaGradient (int[] pix, int w, int h, int direction, int startAlpha, int endAlpha, int startBuffer, int endBuffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if(!((0 <= startAlpha) && (startAlpha <= 255)))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: startAlpha parameter is not [0,255], its " + startAlpha);;
		if(!((0 <= endAlpha) && (endAlpha <= 255)))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: endAlpha parameter is not [0,255], its " + endAlpha);;
		if(!(startBuffer >= -100))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: startBuffer parameter is invalid, its " + startBuffer);;
		if(!(endBuffer >= -100))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: endBuffer parameter is invalid, its " + endBuffer);;
		if(!(pix.length >= (w*h)))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: pix length is not large enough for the given W and H paramters!");;

		if (direction == GLLib.BOTTOM || direction == GLLib.TOP)
		{
			if (startBuffer < 0){ startBuffer = (((startBuffer)<0 ? -(startBuffer) : (startBuffer)) * h) / 100; }
			if (endBuffer   < 0){ endBuffer   = (((endBuffer)<0 ? -(endBuffer) : (endBuffer))   * h) / 100; }

			if(!(h >= (startBuffer + endBuffer)))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: Error, buffers are larger than gradient dimension!");;

			// Switch alpha direction if going the other way
			if (direction == GLLib.BOTTOM)
			{
				int tmp;
				{(tmp) = (startAlpha);(startAlpha) = (endAlpha);(endAlpha) = (tmp);};
				{(tmp) = (startBuffer);(startBuffer) = (endBuffer);(endBuffer) = (tmp);};
			}

			// Delta-Alpha and Current-Alpha
			int dA = endAlpha - startAlpha;
			int cA = startAlpha;
			int len = (h - 1) - ((0)>(startBuffer - 1)?(0):(startBuffer - 1)) - ((0)>(endBuffer - 1)?(0):(endBuffer - 1));
			startBuffer = h - startBuffer;

			// Use 16bit precision for the running alpha value
			dA = (len == 0) ? 0 : (dA << 16) / (len);
			cA = (cA << 16);

			int alphaMask = (cA << 8) & 0xFF000000;

			int index = w*h;
			int y = h;
			int x;

			while (y > 0)
			{
				y--;
				x = w;

				while (x > 0)
				{
					x--;
					index--;

					if ((pix[index] & 0xFF000000) != 0)
					{
						pix[index] = alphaMask | (pix[index] & 0x00FFFFFF);
					}
				}

				if (y >= endBuffer && y <= startBuffer)
				{
					cA += dA;
					alphaMask = (cA << 8) & 0xFF000000;
				}
			}
		}
		else if (direction == GLLib.LEFT || direction == GLLib.RIGHT)
		{
			if (startBuffer < 0){ startBuffer = (((startBuffer)<0 ? -(startBuffer) : (startBuffer)) * w) / 100; }
			if (endBuffer   < 0){ endBuffer   = (((endBuffer)<0 ? -(endBuffer) : (endBuffer))   * w) / 100; }

			if(!(w >= (startBuffer + endBuffer)))Utils.DBG_PrintStackTrace(false, "PFX_ApplyAlphaGradient: Error, buffers are larger than gradient dimension!");;

			// Switch alpha direction if going the other way
			if (direction == GLLib.RIGHT)
			{
				int tmp;
				{(tmp) = (startAlpha);(startAlpha) = (endAlpha);(endAlpha) = (tmp);};
				{(tmp) = (startBuffer);(startBuffer) = (endBuffer);(endBuffer) = (tmp);};
			}

			// Delta-Alpha and Current-Alpha
			int dA = endAlpha - startAlpha;
			int cA = startAlpha;
			int len = (w - 1) - ((0)>(startBuffer - 1)?(0):(startBuffer - 1)) - ((0)>(endBuffer - 1)?(0):(endBuffer - 1));
			startBuffer = w - startBuffer;


			// Use 16bit precision for the running alpha value
			dA = (len == 0) ? 0 : (dA << 16) / (len);
			cA = (cA << 16);

			int alphaMask = (cA << 8) & 0xFF000000;

			// Starting Y-index as we sweep across X
			int superIndex = (w * h);
			int index;
			int x = w;

			while (x > 0)
			{
				x--;
				superIndex--;
				index = superIndex;

				while (index >= 0)
				{
					if ((pix[index] & 0xFF000000) != 0)
					{
						pix[index] = alphaMask | (pix[index] & 0x00FFFFFF);
					}

					index -= w;
				}

				if (x >= endBuffer && x <= startBuffer)
				{
					cA += dA;
					alphaMask = (cA << 8) & 0xFF000000;
				}
			}
		}
	}

	//-------------------------------------------------------------------------------------------------
	/// Given a color this function returns the grayscale value the PFX would convert this color into.
	/// The PFX takes the RED channel and sets the BLUE and GREEN channels to the RED value.
	///
	///!param color - The color which we want to convert to a grayscale value
	///!return The grayscale value for this color according to the PFX.
	//-------------------------------------------------------------------------------------------------
	final static int PFX_GetGrayscaleColor (int color)
	{
		int red = ((color & 0x00FF0000) >> 16) & 0xFF;
		return (color & 0xFFFF0000) | (red << 8) | (red);
	}


	//-------------------------------------------------------------------------------------------------
	///!name Auxilary Configuration Constants
	//!{
	/// Determines if a screen buffer should be used.
	final static boolean pfx_usingScreenBuffer		= DevConfig.pfx_useScreenBuffer &&
													  (
													  	DevConfig.pfx_useFullScreenEffectBlur ||
													    DevConfig.pfx_useFullScreenEffectBlend ||
													    DevConfig.pfx_useFullScreenEffectAdditive ||
													    DevConfig.pfx_useFullScreenEffectSubtractive ||
													    DevConfig.pfx_useFullScreenEffectMultiplicative ||
													    DevConfig.pfx_useEffectGlow ||
													    DevConfig.pfx_useSpriteEffectAdditive ||
													    DevConfig.pfx_useSpriteEffectMultiplicative
													  );

	/// Determines if any sprite effect is enabled.
	final static boolean pfx_useSpriteEffects		= DevConfig.pfx_useSpriteEffectAdditive ||
											 		  DevConfig.pfx_useSpriteEffectMultiplicative ||
													  DevConfig.pfx_useSpriteEffectGrayscale ||
													  DevConfig.pfx_useSpriteEffectShine ||
													  DevConfig.pfx_useSpriteEffectBlend ||
													  DevConfig.pfx_useSpriteEffectScale;
	//!}
	//------------------------------------------------------------------------------------------------
	public static boolean      s_PFX_initializd 	= false;
	public static int 			s_PFX_type   		= 0;
	public static int[][]      s_PFX_params 		= null;

	/// Set by GLLib.PFX_ProcessSpriteEffects to indicate how the returned buffer should be blit if it exists.
	static boolean      		s_PFX_hasAlpha;
	/// Set by GLLib.PFX_ProcessSpriteEffects to indicate how the returned buffer should be blit if it exists.
	static int					s_PFX_sizeX;
	/// Set by GLLib.PFX_ProcessSpriteEffects to indicate how the returned buffer should be blit if it exists.
	static int          		s_PFX_sizeY;

	/// Special image buffer for specific effects (additive, multiplicative, FS blur, FS alpha)
	public static Image 		s_PFX_screenBuffer  = null;
	public static Graphics 	s_PFX_screenBufferG = null;

	/// Global control vars to set if need to use screen buffer.
	private static int 			s_PFX_enableScreenBuffer          = 0;
	public static int 			s_PFX_enableScreenBufferThisFrame = 0;

	/// Used to indicate when we do not need to render the scene since it is already buffered
	private static boolean      s_PFX_screenIsBuffered = false;

	/// Full-Screen Effects Window
	private static int          s_PFX_windowX;
	private static int          s_PFX_windowY;
	private static int          s_PFX_windowWidth;
	private static int          s_PFX_windowHeight;

	private static int          s_PFX_timer;


	//-------------------------------------------------------------------------------------------------
	/// Wrapper for API calls that get pixel data. (int[] version)
	///
	///!param buffer[] - is the buffer to read the pixel data into
	///!param scanlength - the relative offset in the pixels array between corresponding pixels in consecutive rows
	///!param offset - the array index of the first ARGB value
	///!param x - the x-coordinate of the upper left corner of the region
	///!param y - the y-coordinate of the upper left corner of the region
	///!param w - the width of the region
	///!param h - the height of the region
	///
	///!note This function is only implemented for NokiaUI and MIDP2.
	///
	///!note There is also a version of this function that handles pixels in a short[]
	///!see  GLLib&PFX_ReadPixelData (short[] buffer, int offset, int scanlength, int x, int y, int w, int h)
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ReadPixelData (int[] buffer, int offset, int scanlength, int x, int y, int w, int h)
	{
		if(!DevConfig.pfx_enabled) return;
		if(!(buffer != null))Utils.DBG_PrintStackTrace(false, "Error in PFX_ReadPixelData: destination buffer is null!");;


		if (DevConfig.sprite_allowPixelArrayGraphics && zSprite._customGraphics != null)
		{
			zSprite.CopyArea (zSprite._customGraphics, zSprite._customGraphicsWidth, zSprite._customGraphicsHeight, buffer, offset, scanlength, x, y, w, h);
		}
		else if(pfx_usingScreenBuffer)
		{
			GLLib.GetRGB(s_PFX_screenBuffer, buffer, offset, scanlength, x, y, w, h);
		}
		else
		{
			Utils.Dbg("PFX_ReadPixelData: In MIDP2 you must enable pfx_useScreenBuffer to have this work! Or use a custom graphics (sprite_allowPixelArrayGraphics)");;
		}


	}


	//-------------------------------------------------------------------------------------------------
	/// Wrapper for API calls that get pixel data. (short[] version)
	///
	///!param buffer[] - is the buffer to read the pixel data into
	///!param scanlength - the relative offset in the pixels array between corresponding pixels in consecutive rows
	///!param offset - the array index of the first ARGB value
	///!param x - the x-coordinate of the upper left corner of the region
	///!param y - the y-coordinate of the upper left corner of the region
	///!param w - the width of the region
	///!param h - the height of the region
	///
	///!note This function is only implemented for NokiaUI.
	///
	///!note There is also a version of this function that handles pixels in an int[]
	///!see  GLLib&PFX_ReadPixelData (int[] buffer, int offset, int scanlength, int x, int y, int w, int h)
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ReadPixelData (short[] buffer, int offset, int scanlength, int x, int y, int w, int h)
	{
		if(!DevConfig.pfx_enabled) return;
		if(!(buffer != null))Utils.DBG_PrintStackTrace(false, "Error in PFX_ReadPixelData: destination buffer is null!");;


		// TODO: Use getRGB to get int[] and manually convert to short[]?
		Utils.Dbg("PFX_ReadPixelData: In MIDP2 (without Nokia) you are trying to read a short[]!!! (not implemented, would require manual conversion to int[])");;


	}


	//-------------------------------------------------------------------------------------------------
	/// Wrapper for API calls that get pixel data. (int[] version)
	///
	///!param source[] - is the buffer containing the data to write
	///!param offset - the array index of the first ARGB value
	///!param scanlength - the relative offset in the pixels array between corresponding pixels in consecutive rows
	///!param x - the x-coordinate of the upper left corner of the region
	///!param y - the y-coordinate of the upper left corner of the region
	///!param w - the width of the region
	///!param h - the height of the region
	///!param alpha - if the pixel data contains and alpha values not equal to 255
	///
	///!note This function is only implemented for NokiaUI and MIDP2.
	///
	///!note There is also a version of this function that handles pixels in an short[]
	///!see  GLLib&PFX_WritePixelData (short[] source, int offset, int scanlength, int x, int y, int w, int h, boolean alpha)
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_WritePixelData (Graphics g, int[] source, int offset, int scanlength, int x, int y, int w, int h, boolean alpha)
	{
		if(!DevConfig.pfx_enabled) return;
		if(!(source != null))Utils.DBG_PrintStackTrace(false, "Error in PFX_WritePixelData: source buffer is null!");;


		GLLib.DrawRGB( g, source, offset, scanlength, x, y, w, h, alpha);


	}


	//-------------------------------------------------------------------------------------------------
	/// Wrapper for API calls that get pixel data. (short[] version)
	///
	///!param source[] - is the buffer containing the data to write
	///!param offset - the array index of the first ARGB value
	///!param scanlength - the relative offset in the pixels array between corresponding pixels in consecutive rows
	///!param x - the x-coordinate of the upper left corner of the region
	///!param y - the y-coordinate of the upper left corner of the region
	///!param w - the width of the region
	///!param h - the height of the region
	///!param alpha - if the pixel data contains and alpha values not equal to 255
	///
	///!note This function is only implemented for NokiaUI.
	///
	///!note There is also a version of this function that handles pixels in an int[]
	///!see  GLLib&PFX_WritePixelData (int[] source, int offset, int scanlength, int x, int y, int w, int h, boolean alpha)
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_WritePixelData (Graphics g, short[] source, int offset, int scanlength, int x, int y, int w, int h, boolean alpha)
	{
		if(!DevConfig.pfx_enabled) return;
		if(!(source != null))Utils.DBG_PrintStackTrace(false, "Error in PFX_WritePixelData: source buffer is null!");;


		// TODO: Convert short[] to int[] manually and use drawRGB from MIDP2?
		Utils.Dbg("PFX_WritePixelData: In MIDP2 (without Nokia) you are trying to write a short[] to screen!!! (not implemented, would require manual conversion to int[])");;


	}

	//-------------------------------------------------------------------------------------------------
	/// Initialize pixel effects system.
	///
	///!note If configuration uses double buffer it will be allocated here.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_Init ()
	{
		if(!DevConfig.pfx_enabled) return;
		s_PFX_type 			= 0;
		s_PFX_timer         = 0;
		s_PFX_windowX		= 0;
		s_PFX_windowY		= 0;
		s_PFX_windowWidth 	= GLLib.GetScreenWidth();
		s_PFX_windowHeight	= GLLib.GetScreenHeight();

		s_PFX_params = new int[GLPixEffects.k_EFFECT_NUM][];

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useFullScreenEffectBlur)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR] 	 = new int[GLPixEffects.k_PARAM_FULLSCREEN_BLUR_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_FULLSCREEN_BLUR);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useFullScreenEffectBlend)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND] 	 = new int[GLPixEffects.k_PARAM_FULLSCREEN_BLEND_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_FULLSCREEN_BLEND);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useSpriteEffectGrayscale)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE]            = new int[GLPixEffects.k_PARAM_GRAYSCALE_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_GRAYSCALE);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useSpriteEffectShine)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_SHINE]                = new int[GLPixEffects.k_PARAM_SHINE_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_SHINE);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useEffectGlow)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_GLOW]                = new int[GLPixEffects.k_PARAM_GLOW_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_GLOW);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useSpriteEffectBlend)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_BLEND]                = new int[GLPixEffects.k_PARAM_BLEND_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_BLEND);
		}

		//-------------------------------------------------------------------------------------------------
		if (DevConfig.pfx_useSpriteEffectScale)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_SCALE]                = new int[GLPixEffects.k_PARAM_SCALE_NUM];

			PFX_SetDefaultEffectParameters(GLPixEffects.k_EFFECT_SCALE);
		}

		// create image screen buffer
		if (pfx_usingScreenBuffer)
		{
			s_PFX_screenBuffer  = Image.createImage(GLLib.GetScreenWidth(), GLLib.GetScreenHeight());
			

				s_PFX_screenBufferG = s_PFX_screenBuffer.getGraphics();

		}

		// by default disabled
		s_PFX_enableScreenBuffer = 0;

		s_PFX_initializd = true;
	}


	//-------------------------------------------------------------------------------------------------
	/// Sets given effects parameters to their default values
	///
	///!param effect - The effect whose parameters we would like to reset.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetDefaultEffectParameters (int effect)
	{
		if(!DevConfig.pfx_enabled) return;
		switch(effect)
		{
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_FULLSCREEN_BLUR:
			{
				if (DevConfig.pfx_useFullScreenEffectBlur)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_AMOUNT_INC_RATE] = GLPixEffects.k_DEFAULT_FULLSCREEN_BLUR_AMOUNT_INC;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_FULLSCREEN_BLEND:
			{
				if (DevConfig.pfx_useFullScreenEffectBlend)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_AMOUNT_INC_RATE] = GLPixEffects.k_DEFAULT_FULLSCREEN_BLEND_AMOUNT_INC;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_GRAYSCALE:
			{
				if (DevConfig.pfx_useSpriteEffectGrayscale)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA]  		   = -1;
					s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA_PROCESSING] = GLPixEffects.k_ALPHA_PROCESSING_AUTO;
					s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_SATURATION]	   = 0;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_SHINE:
			{
				if (DevConfig.pfx_useSpriteEffectShine)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_POSX]  		    = 0;
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_WIDTH] 		    = 40;
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_COLOR] 		    = 0xFFFFFF;
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_DISTANCE_ABSOLUTE] = 0;
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_MAX_VALUE_SHIFT]   = 8;
					s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_ALPHA_PROCESSING]  = GLPixEffects.k_ALPHA_PROCESSING_AUTO;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_GLOW:
			{
				if (DevConfig.pfx_useEffectGlow)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_RED]  	  = GLPixEffects.k_DEFAULT_GLOW_THRESHOLD_RED;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_GREEN]   = GLPixEffects.k_DEFAULT_GLOW_THRESHOLD_GREEN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_BLUE]    = GLPixEffects.k_DEFAULT_GLOW_THRESHOLD_BLUE;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE]    = GLPixEffects.k_DEFAULT_GLOW_THRESHOLD_TYPE;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH_MARGIN]      = GLPixEffects.k_DEFAULT_GLOW_WIDTH_MARGIN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT_MARGIN]  	  = GLPixEffects.k_DEFAULT_GLOW_HEIGHT_MARGIN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_PERIOD1]  		  = cMath.Math_IntToFixedPoint(GLPixEffects.k_DEFAULT_GLOW_PERIOD1);
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_PERIOD2]  		  = cMath.Math_IntToFixedPoint(GLPixEffects.k_DEFAULT_GLOW_PERIOD2);
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HSPREAD_MIN] 	  = GLPixEffects.k_DEFAULT_GLOW_HSPREAD_MIN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_VSPREAD_MIN]  	  = GLPixEffects.k_DEFAULT_GLOW_VSPREAD_MIN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_MIN]     = GLPixEffects.k_DEFAULT_GLOW_INTENSITY_MIN;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HSPREAD_MAX]  	  = GLPixEffects.k_DEFAULT_GLOW_HSPREAD_MAX;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_VSPREAD_MAX]  	  = GLPixEffects.k_DEFAULT_GLOW_VSPREAD_MAX;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_MAX]  	  = GLPixEffects.k_DEFAULT_GLOW_INTENSITY_MAX;
					s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_DIVISOR] = GLPixEffects.k_DEFAULT_GLOW_INTENSITY_DIVISOR;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_BLEND:
			{
				if (DevConfig.pfx_useSpriteEffectBlend)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_AMOUNT]           = 255;
					s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_ALPHA_PROCESSING] = GLPixEffects.k_ALPHA_PROCESSING_AUTO;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_SCALE:
			{
				if (DevConfig.pfx_useSpriteEffectScale)
				{
					s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_PERCENT] 		   = 100;
					s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA]     	   = -1;
					s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA_PROCESSING] = GLPixEffects.k_ALPHA_PROCESSING_AUTO;
				}
			}
			break;
			//-----------------------------------------------------------------------------------------
			case GLPixEffects.k_EFFECT_FULLSCREEN_ADDITIVE:
			case GLPixEffects.k_EFFECT_FULLSCREEN_SUBTRACTIVE:
			case GLPixEffects.k_EFFECT_FULLSCREEN_MULTIPLICATIVE:
			case GLPixEffects.k_EFFECT_ADDITIVE:
			case GLPixEffects.k_EFFECT_MULTIPLICATIVE:
			break;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Sets given effects parameters to default values for all sprite effects.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetDefaultSpriteEffectParameters ()
	{
		for (int i=0; i<GLPixEffects.k_EFFECT_NUM; i++)
		{
			if (((1<<i) & GLPixEffects.k_EFFECTS_SPRITE_MASK) != 0)
			{
				PFX_SetDefaultEffectParameters(i);
			}
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Releases all memory for Pixel Effects (mainly buffer if used).
	//-------------------------------------------------------------------------------------------------
	final static void PFX_Release ()
	{
		s_PFX_screenBuffer  = null;
		s_PFX_screenBufferG = null;

		for (int i=0; i<GLPixEffects.k_EFFECT_NUM; i++)
		{
			s_PFX_params[i] = null;
		}

		s_PFX_params	    = null;
		s_PFX_initializd    = false;
	}


	//-------------------------------------------------------------------------------------------------
	/// Wheather the PFX have been initialized
	///!return True if the PFX are initialized
	///
	///!see GLLib&PFX_Init ()
	///!see GLLib&PFX_Release ()
	//-------------------------------------------------------------------------------------------------
	final static boolean PFX_IsInitialized ()
	{
		return s_PFX_initializd;
	}


	//-------------------------------------------------------------------------------------------------
	/// When true paint does not need to render the screen to the main graphics context.
	///!return Wheather the screen is being painted to a buffer this frame.
	//-------------------------------------------------------------------------------------------------
	final static boolean PFX_IsScreenBuffered ()
	{
		return s_PFX_screenIsBuffered;
	}


	//-------------------------------------------------------------------------------------------------
	/// Gets a graphics context from which it will be possible to read the pixel data.
	///!return graphics context to render to.
	//-------------------------------------------------------------------------------------------------
	final static javax.microedition.lcdui.Graphics PFX_GetReadableGraphics ()
	{


		return s_PFX_screenBufferG;

	}


	//-------------------------------------------------------------------------------------------------
	/// Gets the screen image buffer.
	///!return reference to image buffer
	//-------------------------------------------------------------------------------------------------
	final static Image PFX_GetBuffer ()
	{
		return s_PFX_screenBuffer;
	}


	//-------------------------------------------------------------------------------------------------
	/// Enable screen buffer this frame.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_EnableScreenBufferThisFrame ()
	{
		s_PFX_enableScreenBufferThisFrame = 1;
	}


	//-------------------------------------------------------------------------------------------------
	/// Enable screen buffer.
	///
	///!param effect ID of effect that will enable the screen buffer.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_EnableScreenBuffer (int effect)
	{
		s_PFX_enableScreenBuffer |= (1<<effect);
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable screen buffer.
	///
	///!param effect ID of effect that no longer will remain enabled.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableScreenBuffer (int effect)
	{
		s_PFX_enableScreenBuffer &= ~(1<<effect);
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable all screen buffer effects.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableAllScreenBuffer ()
	{
		s_PFX_enableScreenBuffer = 0;
	}


	//-------------------------------------------------------------------------------------------------
	/// Returns if the screen buffer is needed
	//-------------------------------------------------------------------------------------------------
	final static boolean PFX_NeedScreenBufferThisFrame ()
	{
		return (s_PFX_enableScreenBufferThisFrame != 0) || (s_PFX_enableScreenBuffer != 0);
	}


	//-------------------------------------------------------------------------------------------------
	/// Set specified effect to enable
	///
	///	&param effect - ID of effect to enable.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_EnableEffect (int effect)
	{
		if(!DevConfig.pfx_enabled) return;
		PFX_EnableEffect(effect, false);
	}


	//-------------------------------------------------------------------------------------------------
	/// Set specified effect to enable
	///
	///	&param effect - ID of effect to enable.
	///!param reset  - If true this effects parameters will be reset to their defaults.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_EnableEffect (int effect, boolean reset)
	{
		if(!DevConfig.pfx_enabled) return;
		s_PFX_type |= (1<<effect);


		if(!pfx_usingScreenBuffer && !DevConfig.sprite_allowPixelArrayGraphics)
		{
			if (((1<<effect) & GLPixEffects.k_EFFECTS_THAT_NEED_SCREEN_BUFFER_MASK) != 0)
			{
				Utils.Dbg("PFX_EnableEffect: Setting a full-screen effect when neither NokiaUI is available nor a full-screen buffer!");;

				// if it's not enabled then disable all effects that need it
				s_PFX_type &= ~GLPixEffects.k_EFFECTS_THAT_NEED_SCREEN_BUFFER_MASK;
			}
		}


		// apply buffer automatically when the effect enabled is full screen type (these effects are enabled in the update code section)
		if (((1<<effect) & GLPixEffects.k_EFFECTS_IN_FULL_SCREEN_MASK) != 0)
		{
			PFX_EnableScreenBuffer(effect);
		}

		if ((effect == GLPixEffects.k_EFFECT_SCALE) && !DevConfig.pfx_useSpriteEffectScale)
		{
			Utils.Dbg("PFX_EnableEffect: Scale Effect is not enabled, you must set pfx_useSpriteEffectScale to TRUE!");;
		}
		else if ((effect == GLPixEffects.k_EFFECT_BLEND) && !DevConfig.pfx_useSpriteEffectBlend)
		{
			Utils.Dbg("PFX_EnableEffect: Blend Effect is not enabled, you must set pfx_useSpriteEffectBlend to TRUE!");;
		}
		else if ((effect == GLPixEffects.k_EFFECT_GRAYSCALE) && !DevConfig.pfx_useSpriteEffectGrayscale)
		{
			Utils.Dbg("PFX_EnableEffect: Grayscale Effect is not enabled, you must set pfx_useSpriteEffectGrayscale to TRUE!");;
		}
		else if ((effect == GLPixEffects.k_EFFECT_SHINE) && !DevConfig.pfx_useSpriteEffectShine)
		{
			Utils.Dbg("PFX_EnableEffect: Shine Effect is not enabled, you must set pfx_useSpriteEffectShine to TRUE!");;
		}
		else if ((effect == GLPixEffects.k_EFFECT_ADDITIVE) && !DevConfig.pfx_useSpriteEffectAdditive)
		{
			Utils.Dbg("PFX_EnableEffect: Additive Effect is not enabled, you must set pfx_useSpriteEffectAdditive to TRUE!");;
		}
		else if ((effect == GLPixEffects.k_EFFECT_MULTIPLICATIVE) && !DevConfig.pfx_useSpriteEffectMultiplicative)
		{
			Utils.Dbg("PFX_EnableEffect: Multiplicative Effect is not enabled, you must set pfx_useSpriteEffectMultiplicative to TRUE!");;
		}

		if (reset)
		{
			PFX_SetDefaultEffectParameters(effect);
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable specified effect
	///
	///	&param effect ID of effect to disable.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableEffect (int effect)
	{
		PFX_DisableEffect(effect, false);
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable specified effect
	///
	///	&param effect ID of effect to disable.
	///!param reset  - If true this effects parameters will be reset to their defaults.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableEffect (int effect, boolean reset)
	{
		if(!DevConfig.pfx_enabled) return;
		if ( (DevConfig.pfx_useFullScreenEffectBlur) &&
		     (effect == GLPixEffects.k_EFFECT_FULLSCREEN_BLUR) &&
		     (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] != GLPixEffects.k_BLUR_STATE_FINISHED)
		   )
		{
			if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] < GLPixEffects.k_BLUR_STATE_OUT)
			{
				s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] = GLPixEffects.k_BLUR_STATE_OUT;
			}
		}
		else
		{
			s_PFX_type &= ~(1<<effect);

			// apply buffer automatically when the effect enabled is full screen type (these effects are enabled in the update code section)
			if (((1<<effect) & GLPixEffects.k_EFFECTS_IN_FULL_SCREEN_MASK) != 0)
			{
				PFX_DisableScreenBuffer(effect);
			}

			if (DevConfig.pfx_useFullScreenEffectBlend)
			{
				if (effect == GLPixEffects.k_EFFECT_FULLSCREEN_BLEND)
				{
					s_PFX_screenIsBuffered = false;
				}
			}
		}

		if (reset)
		{
			PFX_SetDefaultEffectParameters(effect);
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable all effects
	//-------------------------------------------------------------------------------------------------
	final static boolean PFX_IsEffectEnabled (int effect)
	{
		return (s_PFX_type & (1<<effect)) != 0;
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable all effects
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableAllEffects ()
	{
		s_PFX_type = 0;
	}


	//-------------------------------------------------------------------------------------------------
	/// Returns mask indicating which sprite effects are enabled.
	//-------------------------------------------------------------------------------------------------
	final static int PFX_GetEnabledSpriteEffectsMask ()
	{
		return s_PFX_type & GLPixEffects.k_EFFECTS_SPRITE_MASK;
	}


	//-------------------------------------------------------------------------------------------------
	/// Disable all sprite effects
	//-------------------------------------------------------------------------------------------------
	final static void PFX_DisableAllSpriteEffects ()
	{
		s_PFX_type &= ~GLPixEffects.k_EFFECTS_SPRITE_MASK;
	}

	//-------------------------------------------------------------------------------------------------
	/// Set effect parameter value.
	///
	///	&param effectId	ID of effect id in the params array
	///	&param paramId	ID of effect param id
	///	&param value	effect param value
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetParam (int effectId, int paramId, int value)
	{
		if(!DevConfig.pfx_enabled) return;
		if((1 == 0))
		{
			if(!((effectId >= 0) && (effectId < GLPixEffects.k_EFFECT_NUM)))Utils.DBG_PrintStackTrace(false, "PFX_SetParam: Invalid effect ID: " + effectId);;
			if(!(s_PFX_params != null))Utils.DBG_PrintStackTrace(false, "PFX_SetParam: Parameters are null, did you call PFX_Init?");;
			if(!(s_PFX_params[effectId] != null))Utils.DBG_PrintStackTrace(false, "PFX_SetParam: Parameters for this effect are null, did you call PFX_Init? Did you enable this effect in zJYLibConfig?");;
		}

		s_PFX_params[effectId][paramId] = value;
	}


	//-------------------------------------------------------------------------------------------------
	/// Get effect parameter value.
	///
	///	&param effectId	ID of effect id in the params array
	///	&param paramId	ID of effect param id
	//-------------------------------------------------------------------------------------------------
	final static int PFX_GetParam (int effectId, int paramId)
	{
		return s_PFX_params[effectId][paramId];
	}


	//-------------------------------------------------------------------------------------------------
	/// Alters the graphics context if needed.
	/// MUST BE CALLED ONCE AT THE END OF UPDATE!!
	///
	///!param deltaT - time step to use for update (in ms).
	//-------------------------------------------------------------------------------------------------
	final static void PFX_Update (int deltaT)
	{
		if (!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectBlur)
		{
			// Is full-screen blur enabled? If so update it...
			if (PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_FULLSCREEN_BLUR))
			{
				int amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT];

				//crescendo
				if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] == GLPixEffects.k_BLUR_STATE_IN)
				{
					if (amount < s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_TARGET_AMOUNT])
					{
						amount += (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_AMOUNT_INC_RATE] * deltaT);

						if (amount >= s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_TARGET_AMOUNT])
						{
							amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_TARGET_AMOUNT];
							s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] = GLPixEffects.k_BLUR_STATE_STABLE;
						}

						s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT] = amount;
					}
				}
				//decrescendo
				else if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] == GLPixEffects.k_BLUR_STATE_OUT)
				{
					if (amount > 0)
					{
						amount -= (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_AMOUNT_INC_RATE] * deltaT);

						if (amount <= 0)
						{
							amount = 0;
							s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE] = GLPixEffects.k_BLUR_STATE_FINISHED;
							PFX_DisableEffect(GLPixEffects.k_EFFECT_FULLSCREEN_BLUR);
						}

						s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT] = amount;
					}
				}
			}
		}

		if (DevConfig.pfx_useFullScreenEffectBlend)
		{
			// Is full-screen blend enabled? If so update it...
			if (PFX_IsEffectEnabled(GLPixEffects.k_EFFECT_FULLSCREEN_BLEND))
			{
				if(s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_REPAINT_NUM] != 0)
				{
					if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_REPAINT_NUM] > 0)
					{
						s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_REPAINT_NUM]--;
					}

					s_PFX_screenIsBuffered = false;
					PFX_EnableScreenBufferThisFrame();
				}
				else
				{
					s_PFX_screenIsBuffered = true;
				}

				int amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT];

				//crescendo
				if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE] == GLPixEffects.k_BLEND_STATE_IN)
				{
					if (amount < s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_TARGET_AMOUNT])
					{
						amount += (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_AMOUNT_INC_RATE] * deltaT);

						if (amount >= s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_TARGET_AMOUNT])
						{
							amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_TARGET_AMOUNT];
							s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE] = GLPixEffects.k_BLEND_STATE_STABLE;
							PFX_DisableEffect(GLPixEffects.k_EFFECT_FULLSCREEN_BLEND);
						}

						s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT] = amount;
					}
				}
				//decrescendo
				else if (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE] == GLPixEffects.k_BLEND_STATE_OUT)
				{
					if (amount > 0)
					{
						amount -= (s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_AMOUNT_INC_RATE] * deltaT);

						if (amount <= 0)
						{
							amount = 0;
							s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE] = GLPixEffects.k_BLEND_STATE_FINISHED;
							PFX_DisableEffect(GLPixEffects.k_EFFECT_FULLSCREEN_BLEND);
						}

						s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT] = amount;
					}
				}
			}
		}

		// Perform this AFTER all effects update (since effect might request this during update, eg blend)
		if (PFX_NeedScreenBufferThisFrame())
		{
			if (pfx_usingScreenBuffer)
			{
				GLLib.g = GLLib.s_lastPaintGraphics = cPFX.s_PFX_screenBufferG;
			}

			
		}

		s_PFX_timer += deltaT;
	}


	//-------------------------------------------------------------------------------------------------
	/// Sets window within which to perform the full-screen effects.
	///
	///!param x - top-left X coordinate of window.
	///!param y - top-left Y coordinate of window.
	///!param w - width of the window.
	///!param h - height of the window.
	///
	///!note By default the window is full-screen.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetFullScreenEffectWindow (int x, int y, int w, int h)
	{
		if(DevConfig.pfx_useFullScreenEffectBlur || DevConfig.pfx_useFullScreenEffectBlend)
		{
			s_PFX_windowX      = x;
			s_PFX_windowY      = y;
			s_PFX_windowWidth  = w;
			s_PFX_windowHeight = h;
		}
		else
		{
			Utils.Dbg("PFX_SetFullScreenEffectWindow: No full-screen pixel effects are enabled!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
//	                                 BLUUUUUUUUR
	//-------------------------------------------------------------------------------------------------

	//-------------------------------------------------------------------------------------------------
	/// Sets Full-Screen Blur Effect using given parameters.
	///
	///!param initialAmount - amount of blur to start with [0,128].
	///!param maxAmount - the final target blur to reach [0,128].
	///!param direction - which way to apply the blur.
	///
	///!note Blur functionality needs to be enabled in zJYLibConfig, disabled by default.
	///!see zJYLibConfig.pfx_useFullScreenEffectBlur
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetBlurEffect (int initialAmount, int maxAmount, int direction)
	{
		if(!DevConfig.pfx_enabled) return;
		if(DevConfig.pfx_useFullScreenEffectBlur)
		{
			if (s_PFX_params == null)
			{
				Utils.Dbg("PFX_SetBlurEffect: PFX have not been initialized! Call PFX_Init() first!");;
			}

			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_DIRECTION] 	  = direction;
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_TARGET_AMOUNT]  = ((maxAmount)<(0)?(0):((maxAmount)>(128)?(128):(maxAmount))) << 10;
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT] = ((initialAmount)<(0)?(0):((initialAmount)>(128)?(128):(initialAmount))) << 10;
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE]  = GLPixEffects.k_BLUR_STATE_IN;
		}
		else
		{
			Utils.Dbg("PFX_SetBlurEffect: Effect is not enabled, please set pfx_useFullScreenEffectBlur to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply full screen blur. This function will read the currently rendered screen, process the
	/// pixels, and then render them to the passed graphics context.
	///
	///!param g - Graphics context to render to.
	//-------------------------------------------------------------------------------------------------
	static void PFX_ApplyFullScreenBlur (Graphics g)
	{
		if(!DevConfig.pfx_enabled) return;
		if(DevConfig.pfx_useFullScreenEffectBlur)
		{
			// check if any effect is active
			if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_FULLSCREEN_BLUR)) == 0)
			{
				return;
			}


			PFX_ApplyFullScreenBlur(g, zSprite.GetPixelBuffer_int(null));


		}
	}


	//-------------------------------------------------------------------------------------------------
//	                                 BLEND (aka alpha)
	//-------------------------------------------------------------------------------------------------

	//-------------------------------------------------------------------------------------------------
	/// Sets Full-Screen Blend Effect using given parameters.
	///
	///!param initialAmount - amount of initial blend (alpha) [0,255].
	///!param maxAmount - the final target blend to reach [0,255].
	///!param repaintNum - whether paint will need to render to readable buffer. \n
	///                     0  = never render, dev will render to backbuffer before starting the effect.\n
	///                     1  = render once, then its saved for the rest of the blend.\n
	///                     -1 = render always, needed for dynamic screens.
	///                     K  = render K times then use buffered version.
	///
	///!note For Nokia the 3rd param will always be forced to -1 because it is required for that implementation.
	///
	///!note Blend functionality needs to be enabled in zJYLibConfig, disabled by default.
	///!see zJYLibConfig.pfx_useFullScreenEffectBlend
	//-------------------------------------------------------------------------------------------------
	final static void PFX_SetBlendEffect (int initialAmount, int maxAmount, int repaintNum)
	{
		if(!DevConfig.pfx_enabled) return;
		if(DevConfig.pfx_useFullScreenEffectBlend)
		{
			if (s_PFX_params == null)
			{
				Utils.Dbg("PFX_SetBlendEffect: PFX have not been initialized! Call PFX_Init() first!");;
			}

			initialAmount = ((initialAmount)<(0)?(0):((initialAmount)>(255)?(255):(initialAmount))) << 10;
			maxAmount     = ((maxAmount)<(0)?(0):((maxAmount)>(255)?(255):(maxAmount))) << 10;

			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT] = initialAmount;
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_TARGET_AMOUNT]  = maxAmount;
			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE]  = maxAmount > initialAmount ? GLPixEffects.k_BLEND_STATE_IN : GLPixEffects.k_BLEND_STATE_OUT;


			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_REPAINT_NUM]    = repaintNum;
		}
		else
		{
			Utils.Dbg("PFX_SetBlendEffect: Effect is not enabled, please set pfx_useFullScreenEffectBlend to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Paints current pixel image buffer to graphics context with current blend value.
	///
	///!param g Is the graphics context to render to.
	///
	///!note You need to enable this feature in the GLLib configuration (disabled by default)
	///!see zJYLibConfig.pfx_useFullScreenEffectBlend
	///
	///!see GLLib&PFX_PaintFullScreenBlend (Graphics g, Image bottomBuffer)
	//-------------------------------------------------------------------------------------------------
	final static void PFX_PaintFullScreenBlend (Graphics g)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectBlend)
		{
			PFX_PaintFullScreenBlend(g, null);
		}
		else
		{
			Utils.Dbg("PFX_PaintFullScreenBlend: Effect is not enabled, you must set pfx_useFullScreenEffectBlend to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Paints current pixel image buffer to graphics context with current blend value.\n
	/// It will also render the passed image buffer which goes under first (unless null).
	///
	///!param g - Is the graphics context to render to.
	///!param bottomBuffer - If not null this will be blit to underneath the pixel buffer.
	///
	///!note You need to enable this feature in the GLLib configuration (disabled by default)
	///!see zJYLibConfig.pfx_useFullScreenEffectBlend
	///
	///!see GLLib&PFX_PaintFullScreenBlend (Graphics g)
	//-------------------------------------------------------------------------------------------------
	final static void PFX_PaintFullScreenBlend (Graphics g, Image bottomBuffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectBlend)
		{

			// check if any effect is active
			if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_FULLSCREEN_BLEND)) == 0)
			{
				return;
			}


			PFX_PaintFullScreenBlend(g, bottomBuffer, zSprite.GetPixelBuffer_int(null), false);


		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Applies full-screen additive effect.
	///
	///!param g - Graphics context to render to.
	///!param color - The color of the additive effect.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_ApplyFullScreenAdditive (Graphics g, int color)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectAdditive)
		{
			// check if any effect is active
			if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_FULLSCREEN_ADDITIVE)) == 0)
			{
				return;
			}


			PFX_ApplyFullScreenAdditive(g, color, zSprite.GetPixelBuffer_int(null));


		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenAdditive: Effect is not enabled, you must set pfx_useFullScreenEffectAdditive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Applies full-screen subtractive effect.
	///
	///!param g - Graphics context to render to.
	///!param color - The color of the subtractive effect.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_ApplyFullScreenSubtractive (Graphics g, int color)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectSubtractive)
		{
			// check if any effect is active
			if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_FULLSCREEN_SUBTRACTIVE)) == 0)
			{
				return;
			}


			PFX_ApplyFullScreenSubtractive(g, color, zSprite.GetPixelBuffer_int(null));


		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenSubtractive: Effect is not enabled, you must set pfx_useFullScreenEffectSubtractive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Applies full-screen multiplicative effect.
	///
	///!param g - Graphics context to render to.
	///!param color - The color of the multiplicative effect.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_ApplyFullScreenMultiplicative (Graphics g, int color)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectMultiplicative)
		{
			// check if any effect is active
			if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_FULLSCREEN_MULTIPLICATIVE)) == 0)
			{
				return;
			}


			PFX_ApplyFullScreenMultiplicative(g, color, zSprite.GetPixelBuffer_int(null));


		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenMultiplicative: Effect is not enabled, you must set pfx_useFullScreenEffectMultiplicative to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Computes an oscillating value.
	///
	///!param period - Fixed point period to use (higher = blink faster, smaller = blink slower)
	///!param v1 - low value to reach
	///!param v2 - high value to reach
	//-------------------------------------------------------------------------------------------------
	final static int PFX_ComputeBlinkingValue (int period, int v1, int v2)
	{
		int angle = cMath.Math_FixedPoint_Multiply(s_PFX_timer * 360, period) >> 10;
		int d     = (v2 - v1) >> 1;

		return v1 + d + cMath.Math_FixedPointToInt(d * cMath.Math_Sin(angle));
	}


	//-------------------------------------------------------------------------------------------------
	/// Set glow area.
	//-------------------------------------------------------------------------------------------------
	final static void PFX_InitGlowArea (int x, int y, int w, int h)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useEffectGlow)
		{
			s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_X] = x - s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH_MARGIN];
			s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_Y] = y - s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT_MARGIN];

			s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH]  = w + (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH_MARGIN]  << 1);
			s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT] = h + (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT_MARGIN] << 1);

			if (DevConfig.pfx_glowUseOneBuffer)
			{
				s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_OFFSET] = s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH] *
																						 	 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT];

				if(!(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH] * s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT] <= (DevConfig.TMP_BUFFER_SIZE / 2)))Utils.DBG_PrintStackTrace(false, "PFX_InitGlowArea: Area size is too large! W*H > (TMP_BUFFER_SIZE/2), either increase buffer or set pfx_glowUseOneBuffer to FALSE!");;

			}
			else
			{
				if(!(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH] * s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT] <= DevConfig.TMP_BUFFER_SIZE))Utils.DBG_PrintStackTrace(false, "PFX_InitGlowArea: Area size is too large! W*H > TMP_BUFFER_SIZE, increase buffer or decrease area!");;


			    if(!((DevConfig.TMP_ALT_BUFFER_SIZE < 0) || (s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH] * s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT] <= DevConfig.TMP_ALT_BUFFER_SIZE)))Utils.DBG_PrintStackTrace(false, "PFX_InitGlowArea: Area size is too large! W*H > TMP_ALT_BUFFER_SIZE, increase buffer or decrease area!");;

			}


			PFX_InitGlowArea(zSprite.GetPixelBuffer_int(null));


		}
		else
		{
			Utils.Dbg("PFX_InitGlowArea: Glow Effect is not enabled, you must set pfx_useEffectGlow to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Paint default configuration glow effect
	//-------------------------------------------------------------------------------------------------
	final static void PFX_ApplyGlow (Graphics g)
	{
		if(!DevConfig.pfx_enabled) return;

		if (DevConfig.pfx_useEffectGlow)
		{
			int hspread   = PFX_ComputeBlinkingValue(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_PERIOD2],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HSPREAD_MIN],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HSPREAD_MAX]);

			int vspread   = PFX_ComputeBlinkingValue(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_PERIOD1],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_VSPREAD_MIN],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_VSPREAD_MAX]);

			int intensity = PFX_ComputeBlinkingValue(s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_PERIOD1],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_MIN],
													 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_INTENSITY_MAX]);


			PFX_ApplyGlow(g, hspread, vspread, intensity, zSprite.GetPixelBuffer_int(null));


		}
		else
		{
			Utils.Dbg("PFX_PaintGlowEffect: Glow Effect is not enabled, you must set pfx_useEffectGlow to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Apply Scaling
	//-------------------------------------------------------------------------------------------------
	final static int PFX_Scale_GetNum (int num, int des, int src)
	{
		return ((num * des) / src);
	}


	//-------------------------------------------------------------------------------------------------
	/// Auxilary Function to update alpha processing var
	///
	///!param currentAlphaProcessing The current status of the processing
	///!param param The current alpha-processing param to use
	//-------------------------------------------------------------------------------------------------
	final private static boolean PFX_UpdateMultiAlpha (boolean currentMultiAlpha, int param)
	{
		if(!DevConfig.pfx_enabled) return false;

		if (param == GLPixEffects.k_ALPHA_PROCESSING_BINARY)
		{
			return false;
		}
		else if (param == GLPixEffects.k_ALPHA_PROCESSING_MULTI)
		{
			return true;
		}

		return currentMultiAlpha;
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	final static int[] PFX_ProcessSpriteEffects (Graphics g, javax.microedition.lcdui.Image img, int x, int y, int w, int h, int flags, boolean hasAlpha, boolean multiAlpha)
	{
		if(!DevConfig.pfx_enabled) return null;
		if(!(img != null))Utils.DBG_PrintStackTrace(false, "PFX_ProcessSpriteEffects: img parameter is null!");;


		{
			int[] buffer = zSprite.GetPixelBuffer_int(null);

			GLLib.GetRGB(img, buffer, 0, w, 0, 0, w, h);

			return PFX_ProcessSpriteEffects (g, buffer, x, y, w, h, flags, hasAlpha, multiAlpha);
		}


	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	final static short[] PFX_ProcessSpriteEffects (Graphics g, javax.microedition.lcdui.Image img, int x, int y, int w, int h, int flags, boolean hasAlpha, boolean multiAlpha, int dummy)
	{
		if(!DevConfig.pfx_enabled) return null;

		if(!(img != null))Utils.DBG_PrintStackTrace(false, "PFX_ProcessSpriteEffects: img parameter is null!");;


		{
			Utils.Dbg("PFX_ProcessSpriteEffects: No way to extract RGB data from image!");;
		}


		return null;
	}


	//-------------------------------------------------------------------------------------------------
	// Structure below is used so we can write the code ONCE but have the function exist in both
	// int and short form.
	//-------------------------------------------------------------------------------------------------


	//-------------------------------------------------------------------------------------------------
	// Functions that can take either INT[] or SHORT[] as params. (Used for GLLib_pixEffects).
	//
	// 1) Before including this file, you MUST DEFINE:
//	 		- A) 'PIXEL_TYPE' as some primitive type (short, int, etc)
//	      - B) 'PIXEL_BUFFER' as a allocated array of type PIXEL_TYPE
	//
	// 2) All functions within this file must use the PIXEL_TYPE DEFINE somewhere in the function header
//	    (otherwise they should not be in this file but inside GLLib_pixEffects.jpp)
	//
	//-------------------------------------------------------------------------------------------------


	//-------------------------------------------------------------------------------------------------
	/// Main Function to process SPRITE PIXEL EFFECTS (called from within PaintModule)
	///
	///!param g - the graphics context to render to.
	///!param source - the modules pixel data
	///!param x - the x position to render the frame to.
	///!param y - the y position to render the frame to.
	///!param w - the width of the module (non-transformed, in the case of rotations)
	///!param h - the height of the module (non-transformed, in the case of rotations)
	///!param flags - the transform flags which need to be applied to the module.
	///!param hasAlpha - true if there is ANY transparency in the module image.
	///!param multiAlpha - true if there are any pixel with alpha value not 0 or 255.
	///!return pixel data array that still needs to be rendered
	///
	///!note This function may blit internally or not, it will always return result that needs to be blit.
	///
	///!note This function will set GLLib.s_PFX_hasAlpha, GLLib.s_PFX_sizeX, and GLLib.s_PFX_sizeY to
	/// indicate how a returned buffer would need to be rendered.
	///
	//-------------------------------------------------------------------------------------------------
	final static int[] PFX_ProcessSpriteEffects (Graphics g, int[] source, int x, int y, int w, int h, int flags, boolean hasAlpha, boolean multiAlpha)
	{
		if(!DevConfig.pfx_enabled) return null;
		s_PFX_hasAlpha = hasAlpha;
		s_PFX_sizeX    = w;
		s_PFX_sizeY    = h;

		// Check if this effect requires a transform for flags
		if ((s_PFX_type & GLPixEffects.k_EFFECTS_SPRITE_NEED_TRANSFORM_MASK) != 0)
		{
			if ((flags & zSprite.FLAG_ROT_90) != 0)
			{
				s_PFX_sizeX = h;
				s_PFX_sizeY = w;
				w = s_PFX_sizeX;
				h = s_PFX_sizeY;
			}

			// Tranform (output buffer will != input buffer)
			source = zSprite.TransformRGB(source, w, h, flags);
		}

		// Get a pixel buffer that is not equal to the source
		int[] pixelBuffer = zSprite.GetPixelBuffer_int(source, w, h);

		//-------------------------------------------------------------------------------------------------
		if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_SCALE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectScale)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA_PROCESSING]);

				if ((s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_PERCENT] == 100) &&
				    (s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA]   == -1)
				   )
				{
					return source;
				}
				else
				{
					return PFX_ProcessScaling (g, source, pixelBuffer, x, y, w, h,
					                           s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_PERCENT],
					                           s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA],
					                           hasAlpha,
					                           multiAlpha);
				}
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_BLEND)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectBlend)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_ALPHA_PROCESSING]);

				return PFX_ProcessBlend(source, pixelBuffer, w * h,
				                        s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_AMOUNT],
				                        hasAlpha,
				                        multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_GRAYSCALE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectGrayscale)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA_PROCESSING]);

				return PFX_ProcessGrayscaleEffect(source, pixelBuffer, w * h,
												  s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_SATURATION],
												  s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA],
				                        		  hasAlpha,
				                        		  multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_SHINE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectShine)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_ALPHA_PROCESSING]);

				return PFX_ProcessShineEffect(g, source, pixelBuffer, x, y, w, h, flags, multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectAdditive)
			{
				return PFX_ProcessPixelEffect(g, source, pixelBuffer, x, y, w, h, flags,
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0),
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0), true);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectMultiplicative)
			{
				return PFX_ProcessPixelEffect(g, source, pixelBuffer, x, y, w, h, flags,
							 				  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0),
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0), true);
			}
		}

		return null;
	}


	//-------------------------------------------------------------------------------------------------
	/// Handles Additive/Multiplicate Sprite Module Processing
	//-------------------------------------------------------------------------------------------------
	final static int[] PFX_ProcessPixelEffect (Graphics g, int[] source, int[] buffer, int posX, int posY, int sWidth, int sHeight, int flags, boolean bAdditive, boolean bMultiplicative, boolean bReadPixels)
	{
		if (!DevConfig.pfx_enabled)return null;

		int srcX 			= 0;
		int srcY 			= 0;
		int width 			= sWidth;
		int height 			= sHeight;
		int widthClipped 	= sWidth;
		int heightClipped 	= sHeight;


		int cx = GLLib.GetClipX(g);
		int cy = GLLib.GetClipY(g);
		int cw = GLLib.GetClipWidth(g);
		int ch = GLLib.GetClipHeight(g);


		if ((flags & zSprite.FLAG_ROT_90) != 0)
		{
			width  = heightClipped;
			height = widthClipped;
		}

		// start clipped sizes are the same that normal sizes
		widthClipped  = width;
		heightClipped = height;

		//clip bottom
		if ((posY + heightClipped) > (ch + cy))
		{
			heightClipped = ((ch + cy) - posY);
		}
		//clip right
		if ((posX + widthClipped) > (cw + cx))
		{
			widthClipped = ((cw + cx) - posX);
		}
		//clip left
		if (posX < cx)
		{
			srcX = (cx - posX);
			widthClipped = width - srcX;
			posX = 0;
		}
		//clip up
		if (posY < cy)
		{
			srcY = (cy - posY);
			heightClipped = height - srcY;
			posY = 0;
		}

		//!Le Chanh Tin: Fixed bug throws exception if widthClipped or heightClipped is negative.
		if (width <= 0 || widthClipped<=0)
		{
			return source;
		}
		if (height <= 0 || heightClipped<=0)
		{
			return source;
		}

		int maxSize = height * (width + 1);

		if (bReadPixels)
		{
			PFX_ReadPixelData(buffer, 0, widthClipped, posX, posY, widthClipped, heightClipped);
		}

		// move clipped image to new position
		if (widthClipped != width || heightClipped != height)
		{
			for (int i = (heightClipped - 1); i >= 0; i--)
			{
				System.arraycopy(buffer, i * widthClipped, buffer, srcX + ((i + srcY) * width), widthClipped);
			}
		}

		int pos = 0;

		int sourcePos;
		int dstPos;
		int offset;
		int sStep;
		int xStart, xStep, xEnd;
		int yStart, yStep, yEnd;


		if ((flags & zSprite.FLAG_ROT_90) != 0)
		{
			xStart = 0;
			xEnd   = height;
			xStep  = 1;

			if ((flags & zSprite.FLAG_FLIP_X) != 0)
			{
				xStart = height - 1;
				xEnd   = -1;
				xStep  = -1;
			}

			yStart = width - 1;
			yStep  = -height;
			offset = (width - 1) * (height);

			if ((flags & zSprite.FLAG_FLIP_Y) != 0)
			{
				yStep  = height;
				offset = 0;
			}

			sStep     = xStep;
			sourcePos = xStart;

			if (bAdditive)
			{
				PFX_ProcessAdditive(source, sourcePos, offset, sStep, buffer, xStart, xStep, xEnd, yStart, yStep);
			}

			if (bMultiplicative)
			{
				PFX_ProcessMultiplicative(source, sourcePos, offset, sStep, buffer, xStart, xStep, xEnd, yStart, yStep);
			}
		}
		else
		{
			yStart = 0;
			yEnd   = height;
			yStep  = 1;

			if ((flags & zSprite.FLAG_FLIP_Y) != 0)
			{
				yStart = height - 1;
				yEnd   = -1;
				yStep  = -1;
			}

			xStart = width - 1;
			xStep  = 1;
			offset = 0;

			if ((flags & zSprite.FLAG_FLIP_X) != 0)
			{
				xStep  = -1;
				offset = width - 1;
			}

			sStep     = yStep  * sWidth;
			sourcePos = yStart * sWidth;

			if (bAdditive)
			{
				PFX_ProcessAdditive(source, sourcePos, offset, sStep, buffer, yStart, yStep, yEnd, xStart, xStep);
			}

			if (bMultiplicative)
			{
				PFX_ProcessMultiplicative(source, sourcePos, offset, sStep, buffer, yStart, yStep, yEnd, xStart, xStep);
			}
		}

		s_PFX_sizeX = width;
		s_PFX_sizeY = height;
		s_PFX_hasAlpha = false;

		return buffer;
	}


	//-------------------------------------------------------------------------------------------------
	/// Will read the currently rendered screen, process the pixels (blurring them) and then render
	/// the result to the passed graphics context (which may be the screen itself).
	///
	///!param g - The graphics context to render to.
	///!param buffer - An allocated buffer to use for processing the effect.
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenBlur (Graphics g, int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if(DevConfig.pfx_useFullScreenEffectBlur)
		{
			if (buffer == null)
			{
				return;
			}

			int x      = s_PFX_windowX;
			int y 	   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;
			int type   = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_DIRECTION];
			int amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT];

			// Using extra precision.
			amount = (amount >> 10);

			int maxBlock = buffer.length / width;
			int block = 80;

			int yy = 0;
			boolean done = false;
			int lastPixel = 0;

			// Process in chunks TOP . DOWN
			if (type == GLPixEffects.k_BLUR_DIRECTION_RIGHT)
			{
				block = maxBlock - 1;

				while (!done)
				{
					if (height - yy < maxBlock)
					{
						done = true;
					}

					// Read Pixel Data into pixel buffer
					PFX_ReadPixelData(buffer, 0, width, x, y + yy, width, block);

					// First pass? Init lastPixel!
					if(yy==0)
					{
						lastPixel = buffer[0];
					}

					// Process those pixels
					lastPixel = PFX_BlurRBlock(width * block, buffer, amount, lastPixel);

					// Write processes Pixel Data to graphics context
					PFX_WritePixelData(g, buffer, 0, width, x, y + yy, width, block, false);

					yy += block;
				}
			}
			// Process in chunks BOTTOM . UP
			else if(type == GLPixEffects.k_BLUR_DIRECTION_LEFT)
			{
				yy = height - (maxBlock - 1);
				block = maxBlock - 1;

				while (!done)
				{
					if (yy < 0)
					{
						block = (maxBlock - 1) + yy;
						yy = 0;
						done = true;
					}

					// Read Pixel Data into pixel buffer
					PFX_ReadPixelData(buffer, 0, width, x, y + yy, width, block);

					// First pass? Init lastPixel!
					if (yy == (height - (maxBlock - 1)))
					{
						lastPixel = buffer[(width * block)-1];
					}

					// Process those pixels
					lastPixel = PFX_BlurLBlock(width * block, buffer, amount, lastPixel);

					// Write processes Pixel Data to graphics context
					PFX_WritePixelData(g, buffer, 0, width, x, y + yy, width, block, false);

					yy -= block;
				}
			}
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_PaintFullScreenBlend (Graphics g, Image bottomBuffer, int[] buffer, boolean bChunks)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectBlend)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;


			if ((bottomBuffer != null) && !bChunks)
			{
				// Blit the image
				g.drawImage(bottomBuffer, 0, 0, 0);
			}


			int maxBlock  = buffer.length / width;
			int block     = maxBlock;
			int blockRest = height;
			int blockSize = block * width;
			int alpha     = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT];

			// Using 10bit precision internally for this
			alpha = (alpha >> 10);

			while(blockRest > 0)
			{
				if(blockRest < maxBlock)
				{
					block = blockRest;
					blockRest = 0;
					blockSize = block * width;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY, width, block);

				// Apply Blend
				PFX_ProcessBlendSimple(buffer, alpha, blockSize);


				if (bChunks)
				{
					g.drawRegion(bottomBuffer, posX, posY, width, block, 0, posX, posY, 0);
				}


				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY, width, block, true);


				blockRest -= block;
				posY += block;
			}

			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_SKIP_PAINT] = 1;
		}
		else
		{
			Utils.Dbg("PFX_PaintFullScreenBlend: Effect is not enabled, you must set pfx_useFullScreenEffectBlend to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenAdditive (Graphics g, int color, int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectAdditive)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_AdditiveBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenAdditive: Effect is not enabled, you must set pfx_useFullScreenEffectAdditive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenSubtractive (Graphics g, int color, int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectSubtractive)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_SubtractiveBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenSubtractive: Effect is not enabled, you must set pfx_useFullScreenEffectSubtractive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenMultiplicative (Graphics g, int color, int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectMultiplicative)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_MultiplicativeBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenMultiplicative: Effect is not enabled, you must set pfx_useFullScreenEffectMultiplicative to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Set glow area.
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_InitGlowArea (int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useEffectGlow)
		{
			PFX_ReadPixelData(buffer,
							  DevConfig.pfx_glowUseOneBuffer ? s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_OFFSET] : 0,
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_X],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_Y],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT]);
		}
		else
		{
			Utils.Dbg("PFX_InitGlowArea: Glow Effect is not enabled, you must set pfx_useEffectGlow to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Paint default configuration glow effect
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyGlow (Graphics g, int hspread, int vspread, int intensity, int[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useEffectGlow)
		{
			int[] result = null;

			if (DevConfig.pfx_glowUseOneBuffer)
			{
				// Result goes into same buffer we already have the pixels in
				result = buffer;

				// Process the effect
				PFX_ProcessGlowBlock(buffer, result, hspread, vspread, intensity,
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_OFFSET]);
			}
			else
			{
				result = zSprite.GetPixelBuffer_int(buffer,
				                          s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
				                          s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT]);

				// Process the effect
				PFX_ProcessGlowBlock(buffer, result, hspread, vspread, intensity,
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
									 0);
			}

			// Draw it
			PFX_WritePixelData(g, result, 0,
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_X],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_Y],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
					   		   false);
		}
	}


	//-------------------------------------------------------------------------------------------------


	//-------------------------------------------------------------------------------------------------
	// Functions that can take either INT[] or SHORT[] as params. (Used for GLLib_pixEffects).
	//
	// 1) Before including this file, you MUST DEFINE:
//	 		- A) 'PIXEL_TYPE' as some primitive type (short, int, etc)
//	      - B) 'PIXEL_BUFFER' as a allocated array of type PIXEL_TYPE
	//
	// 2) All functions within this file must use the PIXEL_TYPE DEFINE somewhere in the function header
//	    (otherwise they should not be in this file but inside GLLib_pixEffects.jpp)
	//
	//-------------------------------------------------------------------------------------------------


	//-------------------------------------------------------------------------------------------------
	/// Main Function to process SPRITE PIXEL EFFECTS (called from within PaintModule)
	///
	///!param g - the graphics context to render to.
	///!param source - the modules pixel data
	///!param x - the x position to render the frame to.
	///!param y - the y position to render the frame to.
	///!param w - the width of the module (non-transformed, in the case of rotations)
	///!param h - the height of the module (non-transformed, in the case of rotations)
	///!param flags - the transform flags which need to be applied to the module.
	///!param hasAlpha - true if there is ANY transparency in the module image.
	///!param multiAlpha - true if there are any pixel with alpha value not 0 or 255.
	///!return pixel data array that still needs to be rendered
	///
	///!note This function may blit internally or not, it will always return result that needs to be blit.
	///
	///!note This function will set GLLib.s_PFX_hasAlpha, GLLib.s_PFX_sizeX, and GLLib.s_PFX_sizeY to
	/// indicate how a returned buffer would need to be rendered.
	///
	//-------------------------------------------------------------------------------------------------
	final static short[] PFX_ProcessSpriteEffects (Graphics g, short[] source, int x, int y, int w, int h, int flags, boolean hasAlpha, boolean multiAlpha)
	{
		if(!DevConfig.pfx_enabled) return null;

		s_PFX_hasAlpha = hasAlpha;
		s_PFX_sizeX    = w;
		s_PFX_sizeY    = h;

		// Check if this effect requires a transform for flags
		if ((s_PFX_type & GLPixEffects.k_EFFECTS_SPRITE_NEED_TRANSFORM_MASK) != 0)
		{
			if ((flags & zSprite.FLAG_ROT_90) != 0)
			{
				s_PFX_sizeX = h;
				s_PFX_sizeY = w;
				w = s_PFX_sizeX;
				h = s_PFX_sizeY;
			}

			// Tranform (output buffer will != input buffer)
			source = zSprite.TransformRGB(source, w, h, flags);
		}

		// Get a pixel buffer that is not equal to the source
		short[] pixelBuffer = zSprite.GetPixelBuffer_short(source, w, h);

		//-------------------------------------------------------------------------------------------------
		if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_SCALE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectScale)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA_PROCESSING]);

				if ((s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_PERCENT] == 100) &&
				    (s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA]   == -1)
				   )
				{
					return source;
				}
				else
				{
					return PFX_ProcessScaling (g, source, pixelBuffer, x, y, w, h,
					                           s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_PERCENT],
					                           s_PFX_params[GLPixEffects.k_EFFECT_SCALE][GLPixEffects.k_PARAM_SCALE_ALPHA],
					                           hasAlpha,
					                           multiAlpha);
				}
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_BLEND)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectBlend)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_ALPHA_PROCESSING]);

				return PFX_ProcessBlend(source, pixelBuffer, w * h,
				                        s_PFX_params[GLPixEffects.k_EFFECT_BLEND][GLPixEffects.k_PARAM_BLEND_AMOUNT],
				                        hasAlpha,
				                        multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_GRAYSCALE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectGrayscale)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA_PROCESSING]);

				return PFX_ProcessGrayscaleEffect(source, pixelBuffer, w * h,
												  s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_SATURATION],
												  s_PFX_params[GLPixEffects.k_EFFECT_GRAYSCALE][GLPixEffects.k_PARAM_GRAYSCALE_ALPHA],
				                        		  hasAlpha,
				                        		  multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_SHINE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectShine)
			{
				multiAlpha = PFX_UpdateMultiAlpha(multiAlpha, s_PFX_params[GLPixEffects.k_EFFECT_SHINE][GLPixEffects.k_PARAM_SHINE_ALPHA_PROCESSING]);

				return PFX_ProcessShineEffect(g, source, pixelBuffer, x, y, w, h, flags, multiAlpha);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectAdditive)
			{
				return PFX_ProcessPixelEffect(g, source, pixelBuffer, x, y, w, h, flags,
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0),
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0), true);
			}
		}
		//-------------------------------------------------------------------------------------------------
		else if ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0)
		{
			if (DevConfig.pfx_useSpriteEffectMultiplicative)
			{
				return PFX_ProcessPixelEffect(g, source, pixelBuffer, x, y, w, h, flags,
							 				  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_ADDITIVE)) != 0),
											  ((s_PFX_type & (1<<GLPixEffects.k_EFFECT_MULTIPLICATIVE)) != 0), true);
			}
		}

		return null;
	}


	//-------------------------------------------------------------------------------------------------
	/// Handles Additive/Multiplicate Sprite Module Processing
	//-------------------------------------------------------------------------------------------------
	final static short[] PFX_ProcessPixelEffect (Graphics g, short[] source, short[] buffer, int posX, int posY, int sWidth, int sHeight, int flags, boolean bAdditive, boolean bMultiplicative, boolean bReadPixels)
	{
		if (!DevConfig.pfx_enabled)return null;

		int srcX 			= 0;
		int srcY 			= 0;
		int width 			= sWidth;
		int height 			= sHeight;
		int widthClipped 	= sWidth;
		int heightClipped 	= sHeight;


		int cx = GLLib.GetClipX(g);
		int cy = GLLib.GetClipY(g);
		int cw = GLLib.GetClipWidth(g);
		int ch = GLLib.GetClipHeight(g);


		if ((flags & zSprite.FLAG_ROT_90) != 0)
		{
			width  = heightClipped;
			height = widthClipped;
		}

		// start clipped sizes are the same that normal sizes
		widthClipped  = width;
		heightClipped = height;

		//clip bottom
		if ((posY + heightClipped) > (ch + cy))
		{
			heightClipped = ((ch + cy) - posY);
		}
		//clip right
		if ((posX + widthClipped) > (cw + cx))
		{
			widthClipped = ((cw + cx) - posX);
		}
		//clip left
		if (posX < cx)
		{
			srcX = (cx - posX);
			widthClipped = width - srcX;
			posX = 0;
		}
		//clip up
		if (posY < cy)
		{
			srcY = (cy - posY);
			heightClipped = height - srcY;
			posY = 0;
		}

		//!Le Chanh Tin: Fixed bug throws exception if widthClipped or heightClipped is negative.
		if (width <= 0 || widthClipped<=0)
		{
			return source;
		}
		if (height <= 0 || heightClipped<=0)
		{
			return source;
		}

		int maxSize = height * (width + 1);

		if (bReadPixels)
		{
			PFX_ReadPixelData(buffer, 0, widthClipped, posX, posY, widthClipped, heightClipped);
		}

		// move clipped image to new position
		if (widthClipped != width || heightClipped != height)
		{
			for (int i = (heightClipped - 1); i >= 0; i--)
			{
				System.arraycopy(buffer, i * widthClipped, buffer, srcX + ((i + srcY) * width), widthClipped);
			}
		}

		int pos = 0;

		int sourcePos;
		int dstPos;
		int offset;
		int sStep;
		int xStart, xStep, xEnd;
		int yStart, yStep, yEnd;


		if ((flags & zSprite.FLAG_ROT_90) != 0)
		{
			xStart = 0;
			xEnd   = height;
			xStep  = 1;

			if ((flags & zSprite.FLAG_FLIP_X) != 0)
			{
				xStart = height - 1;
				xEnd   = -1;
				xStep  = -1;
			}

			yStart = width - 1;
			yStep  = -height;
			offset = (width - 1) * (height);

			if ((flags & zSprite.FLAG_FLIP_Y) != 0)
			{
				yStep  = height;
				offset = 0;
			}

			sStep     = xStep;
			sourcePos = xStart;

			if (bAdditive)
			{
				PFX_ProcessAdditive(source, sourcePos, offset, sStep, buffer, xStart, xStep, xEnd, yStart, yStep);
			}

			if (bMultiplicative)
			{
				PFX_ProcessMultiplicative(source, sourcePos, offset, sStep, buffer, xStart, xStep, xEnd, yStart, yStep);
			}
		}
		else
		{
			yStart = 0;
			yEnd   = height;
			yStep  = 1;

			if ((flags & zSprite.FLAG_FLIP_Y) != 0)
			{
				yStart = height - 1;
				yEnd   = -1;
				yStep  = -1;
			}

			xStart = width - 1;
			xStep  = 1;
			offset = 0;

			if ((flags & zSprite.FLAG_FLIP_X) != 0)
			{
				xStep  = -1;
				offset = width - 1;
			}

			sStep     = yStep  * sWidth;
			sourcePos = yStart * sWidth;

			if (bAdditive)
			{
				PFX_ProcessAdditive(source, sourcePos, offset, sStep, buffer, yStart, yStep, yEnd, xStart, xStep);
			}

			if (bMultiplicative)
			{
				PFX_ProcessMultiplicative(source, sourcePos, offset, sStep, buffer, yStart, yStep, yEnd, xStart, xStep);
			}
		}

		s_PFX_sizeX = width;
		s_PFX_sizeY = height;
		s_PFX_hasAlpha = false;

		return buffer;
	}


	//-------------------------------------------------------------------------------------------------
	/// Will read the currently rendered screen, process the pixels (blurring them) and then render
	/// the result to the passed graphics context (which may be the screen itself).
	///
	///!param g - The graphics context to render to.
	///!param buffer - An allocated buffer to use for processing the effect.
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenBlur (Graphics g, short[] buffer)
	{
		if(DevConfig.pfx_useFullScreenEffectBlur)
		{
			if (buffer == null)
			{
				return;
			}

			int x      = s_PFX_windowX;
			int y 	   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;
			int type   = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_DIRECTION];
			int amount = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLUR][GLPixEffects.k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT];

			// Using extra precision.
			amount = (amount >> 10);

			int maxBlock = buffer.length / width;
			int block = 80;

			int yy = 0;
			boolean done = false;
			short lastPixel = 0;

			// Process in chunks TOP . DOWN
			if (type == GLPixEffects.k_BLUR_DIRECTION_RIGHT)
			{
				block = maxBlock - 1;

				while (!done)
				{
					if (height - yy < maxBlock)
					{
						done = true;
					}

					// Read Pixel Data into pixel buffer
					PFX_ReadPixelData(buffer, 0, width, x, y + yy, width, block);

					// First pass? Init lastPixel!
					if(yy==0)
					{
						lastPixel = buffer[0];
					}

					// Process those pixels
					lastPixel = PFX_BlurRBlock(width * block, buffer, amount, lastPixel);

					// Write processes Pixel Data to graphics context
					PFX_WritePixelData(g, buffer, 0, width, x, y + yy, width, block, false);

					yy += block;
				}
			}
			// Process in chunks BOTTOM . UP
			else if(type == GLPixEffects.k_BLUR_DIRECTION_LEFT)
			{
				yy = height - (maxBlock - 1);
				block = maxBlock - 1;

				while (!done)
				{
					if (yy < 0)
					{
						block = (maxBlock - 1) + yy;
						yy = 0;
						done = true;
					}

					// Read Pixel Data into pixel buffer
					PFX_ReadPixelData(buffer, 0, width, x, y + yy, width, block);

					// First pass? Init lastPixel!
					if (yy == (height - (maxBlock - 1)))
					{
						lastPixel = buffer[(width * block)-1];
					}

					// Process those pixels
					lastPixel = PFX_BlurLBlock(width * block, buffer, amount, lastPixel);

					// Write processes Pixel Data to graphics context
					PFX_WritePixelData(g, buffer, 0, width, x, y + yy, width, block, false);

					yy -= block;
				}
			}
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_PaintFullScreenBlend (Graphics g, Image bottomBuffer, short[] buffer, boolean bChunks)
	{
		if (DevConfig.pfx_useFullScreenEffectBlend)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;


			if ((bottomBuffer != null) && !bChunks)
			{
				// Blit the image
				g.drawImage(bottomBuffer, 0, 0, 0);
			}


			int maxBlock  = buffer.length / width;
			int block     = maxBlock;
			int blockRest = height;
			int blockSize = block * width;
			int alpha     = s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT];

			// Using 10bit precision internally for this
			alpha = (alpha >> 10);

			while(blockRest > 0)
			{
				if(blockRest < maxBlock)
				{
					block = blockRest;
					blockRest = 0;
					blockSize = block * width;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY, width, block);

				// Apply Blend
				PFX_ProcessBlendSimple(buffer, alpha, blockSize);


				if (bChunks)
				{
					g.drawRegion(bottomBuffer, posX, posY, width, block, 0, posX, posY, 0);
				}


				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY, width, block, true);


				blockRest -= block;
				posY += block;
			}

			s_PFX_params[GLPixEffects.k_EFFECT_FULLSCREEN_BLEND][GLPixEffects.k_PARAM_FULLSCREEN_BLEND_SKIP_PAINT] = 1;
		}
		else
		{
			Utils.Dbg("PFX_PaintFullScreenBlend: Effect is not enabled, you must set pfx_useFullScreenEffectBlend to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenAdditive (Graphics g, int color, short[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectAdditive)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_AdditiveBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenAdditive: Effect is not enabled, you must set pfx_useFullScreenEffectAdditive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenSubtractive (Graphics g, int color, short[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;

		if (DevConfig.pfx_useFullScreenEffectSubtractive)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_SubtractiveBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenSubtractive: Effect is not enabled, you must set pfx_useFullScreenEffectSubtractive to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	///
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyFullScreenMultiplicative (Graphics g, int color, short[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useFullScreenEffectMultiplicative)
		{
			int posX   = s_PFX_windowX;
			int posY   = s_PFX_windowY;
			int width  = s_PFX_windowWidth;
			int height = s_PFX_windowHeight;

			int maxBlock = buffer.length / width;
			int yy = 0;
			boolean done = false;

			while (!done)
			{
				int block = height - yy;

				if (block > maxBlock)
				{
					block = maxBlock - 1;
				}
				else
				{
					done = true;
				}

				// Read Pixel Data into pixel buffer
				PFX_ReadPixelData(buffer, 0, width, posX, posY + yy, width, block);

				// Process those pixels
				PFX_MultiplicativeBlock(width * block, buffer, color);

				// Write processes Pixel Data to graphics context
				PFX_WritePixelData(g, buffer, 0, width, posX, posY + yy, width, block, false);

				yy += block;
			}
		}
		else
		{
			Utils.Dbg("PFX_ApplyFullScreenMultiplicative: Effect is not enabled, you must set pfx_useFullScreenEffectMultiplicative to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Set glow area.
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_InitGlowArea (short[] buffer)
	{
		if (DevConfig.pfx_useEffectGlow)
		{
			PFX_ReadPixelData(buffer,
							  DevConfig.pfx_glowUseOneBuffer ? s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_OFFSET] : 0,
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_X],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_Y],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							  s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT]);
		}
		else
		{
			Utils.Dbg("PFX_InitGlowArea: Glow Effect is not enabled, you must set pfx_useEffectGlow to TRUE!");;
		}
	}


	//-------------------------------------------------------------------------------------------------
	/// Paint default configuration glow effect
	//-------------------------------------------------------------------------------------------------
	private final static void PFX_ApplyGlow (Graphics g, int hspread, int vspread, int intensity, short[] buffer)
	{
		if(!DevConfig.pfx_enabled) return;
		if (DevConfig.pfx_useEffectGlow)
		{
			short[] result = null;

			if (DevConfig.pfx_glowUseOneBuffer)
			{
				// Result goes into same buffer we already have the pixels in
				result = buffer;

				// Process the effect
				PFX_ProcessGlowBlock(buffer, result, hspread, vspread, intensity,
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_OFFSET]);
			}
			else
			{
				result = zSprite.GetPixelBuffer_short(buffer,
				                          s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
				                          s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT]);

				// Process the effect
				PFX_ProcessGlowBlock(buffer, result, hspread, vspread, intensity,
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
									 s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
									 0);
			}

			// Draw it
			PFX_WritePixelData(g, result, 0,
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_X],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_Y],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_WIDTH],
							   s_PFX_params[GLPixEffects.k_EFFECT_GLOW][GLPixEffects.k_PARAM_GLOW_HEIGHT],
					   		   false);
		}
	}

}
