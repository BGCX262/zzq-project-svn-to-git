package cn.thirdgwin.lib;
//--------------------------------------------------------------------------------------------------------------------
/// Constants to use for Pixel Effects.
///!ingroup GLLib_pixEffects
//--------------------------------------------------------------------------------------------------------------------
public interface GLPixEffects
{
	///!addtogroup GLLib_pixEffects
	///!{

	///!name Enumeration of all the Pixel Effects
	//!{
	/// Fullscreen Blur Effect
	final static int k_EFFECT_FULLSCREEN_BLUR				   = 0;

	/// Fullscreen Blend (aka alpha) Effect
	final static int k_EFFECT_FULLSCREEN_BLEND				   = 1;

	/// Fullscreen Additive Effect
	final static int k_EFFECT_FULLSCREEN_ADDITIVE      	       = 2;

	/// Fullscreen Subtractive Effect
	final static int k_EFFECT_FULLSCREEN_SUBTRACTIVE     	   = 3;

	/// Fullscreen Multiplicative Effect
	final static int k_EFFECT_FULLSCREEN_MULTIPLICATIVE 	   = 4;

	/// Sprite Additive Effect
	final static int k_EFFECT_ADDITIVE 						   = 5;

	/// Sprite Multiplicative Effect
	final static int k_EFFECT_MULTIPLICATIVE 				   = 6;

	/// Sprite Grayscale Effect
	final static int k_EFFECT_GRAYSCALE 	    			   = 7;

	/// Sprite Shine Effect
	final static int k_EFFECT_SHINE   	    				   = 8;

	/// Regional Glow Effect
	final static int k_EFFECT_GLOW   	    				   = 9;

	/// Sprite Blend (aka alpha) Effect
	final static int k_EFFECT_BLEND							   = 10;

	/// Sprite Scale Effect
	final static int k_EFFECT_SCALE                     	   = 11;

	/// Total number of effects
	final static int k_EFFECT_NUM                   		   = 12;
	//!}

	///!name Masks for Specific Functionality
	//!{
	/// Effects that need the double screen image buffer.
	final static int k_EFFECTS_THAT_NEED_SCREEN_BUFFER_MASK    = (1 << k_EFFECT_FULLSCREEN_BLUR) |
															     (1 << k_EFFECT_FULLSCREEN_BLEND) |
															     (1 << k_EFFECT_FULLSCREEN_ADDITIVE) |
															     (1 << k_EFFECT_FULLSCREEN_SUBTRACTIVE) |
															     (1 << k_EFFECT_FULLSCREEN_MULTIPLICATIVE) |
															     (1 << k_EFFECT_ADDITIVE) |
															     (1 << k_EFFECT_MULTIPLICATIVE) |
															     (1 << k_EFFECT_MULTIPLICATIVE) |
															     (1 << k_EFFECT_GLOW);

	/// Effects that are fullscreen.
	final static int k_EFFECTS_IN_FULL_SCREEN_MASK 			   = (1 << k_EFFECT_FULLSCREEN_BLUR) |
															     (1 << k_EFFECT_FULLSCREEN_BLEND) |
															     (1 << k_EFFECT_FULLSCREEN_ADDITIVE) |
															     (1 << k_EFFECT_FULLSCREEN_SUBTRACTIVE) |
															     (1 << k_EFFECT_FULLSCREEN_MULTIPLICATIVE);

	/// Effects that are for sprites.
	final static int k_EFFECTS_SPRITE_MASK         			   = (1 << k_EFFECT_ADDITIVE) |
											       			     (1 << k_EFFECT_MULTIPLICATIVE) |
											       			     (1 << k_EFFECT_GRAYSCALE) |
											       			     (1 << k_EFFECT_SHINE) |
											       			     (1 << k_EFFECT_BLEND) |
											       			     (1 << k_EFFECT_SCALE);

	/// Sprite effects that need the transform flags applied (because they don't do it themselves).
	final static int k_EFFECTS_SPRITE_NEED_TRANSFORM_MASK      = (1 << k_EFFECT_GRAYSCALE) |
															     (1 << k_EFFECT_SHINE) |
											         		     (1 << k_EFFECT_BLEND) |
											         		     (1 << k_EFFECT_SCALE);

	/// Effects where we can apply the transform flags AFTER doing the effect.
	/// (optimization for NokiaUI since it can handle these in the drawPixel function)
	final static int k_EFFECTS_OK_TO_TRANSFORM_POST_PROCESS    = (1 << k_EFFECT_GRAYSCALE) |
											         		     (1 << k_EFFECT_BLEND);
	//!}

	///!name Alpha Processing Types
	/// Types of processing for the alpha channel, applies to several effects.
	//!{
	/// The type is automatically choosen depending on the alpha channel values present in the sprite
	final static int k_ALPHA_PROCESSING_AUTO        		   = -1;
	/// All opaque pixels, simply sets the alpha channel to the desired alpha value for all pixels
	final static int k_ALPHA_PROCESSING_NONE        		   = 0;
	/// Transparent pixels might exist, so check for FF00FF and leaves the alpha on those at 0, all other pixels alpha is set
	final static int k_ALPHA_PROCESSING_BINARY      		   = 1;
	/// Alpha channel exists, each pixels alpha value must be scaled by the desired alpha
	final static int k_ALPHA_PROCESSING_MULTI       		   = 2;
	//!}

	///!}


	//--------------------- BLUR (full-screen)---------------------------------
	///!defgroup PFX_FULLSCREEN_BLUR Fullscreen Blur
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_SetFullScreenEffectWindow
	///!see GLLib.PFX_SetBlurEffect
	///!see GLLib.PFX_ApplyFullScreenBlur
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name FULLSCREEN BLUR PARAMETERS
	//!{
	/// Which direction is the blur being applied in (currently supports LEFT and RIGHT)
	///!see GLPixEffects.k_BLUR_DIRECTION_RIGHT
	///!see GLPixEffects.k_BLUR_DIRECTION_LEFT
	final static int k_PARAM_FULLSCREEN_BLUR_DIRECTION 		   = 0;
	/// The target strength of the blurring we want to reach, range [0,128]
	final static int k_PARAM_FULLSCREEN_BLUR_TARGET_AMOUNT 	   = 1;
	/// The starting amount of blur, range [0,128]
	final static int k_PARAM_FULLSCREEN_BLUR_CURRENT_AMOUNT    = 2;
	/// The current state of the blurring
	final static int k_PARAM_FULLSCREEN_BLUR_CURRENT_STATE 	   = 3;
	/// The rate at which the strength of the blurring changes
	///!note Default value is GLPixEffects.k_DEFAULT_FULLSCREEN_BLUR_AMOUNT_INC
	final static int k_PARAM_FULLSCREEN_BLUR_AMOUNT_INC_RATE   = 4;
	/// Number of parameters for fullscreen blur
	final static int k_PARAM_FULLSCREEN_BLUR_NUM 	           = 5;
	//!}

	///!name BLUR DEFAULT VALUES
	//!{
	/// Default value of GLPixEffects.k_PARAM_FULLSCREEN_BLUR_AMOUNT_INC_RATE
	final static int k_DEFAULT_FULLSCREEN_BLUR_AMOUNT_INC 	   = 250;
	//!}

	///!name BLUR STATES
	/// States that the fullscreen blur effect can be in.
	//!{
	/// Becoming more blurry (blurring).
	final static int k_BLUR_STATE_IN 						   = 0;
	/// Blurred and not changing.
	final static int k_BLUR_STATE_STABLE 					   = 1;
	/// Becoming less blurry (removing blur).
	final static int k_BLUR_STATE_OUT 						   = 2;
	/// No more blur.
	final static int k_BLUR_STATE_FINISHED 					   = 3;
	//!}

	///!name BLUR DIRECTIONS
	/// Directions for the application of blur.
	//!{
	/// Blurring towards the right
	final static int k_BLUR_DIRECTION_RIGHT 				   = 0;
	/// Blurring towards the left
	final static int k_BLUR_DIRECTION_LEFT 					   = 1;
	//!}
	///!}


	//--------------------- BLEND (full-screen)--------------------------------
	///!defgroup PFX_FULLSCREEN_BLEND Fullscreen Blend
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_SetFullScreenEffectWindow
	///!see GLLib.PFX_SetBlendEffect
	///!see GLLib.PFX_IsScreenBuffered
	///!see GLLib.PFX_PaintFullScreenBlend
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name FULLSCREEN BLEND PARAMETERS
	///!see GLLib.PFX_SetFullScreenEffectWindow, GLLib.PFX_SetBlurEffect
	//!{
	/// The target amount of the current screen to have, range [0, 255].
	final static int k_PARAM_FULLSCREEN_BLEND_TARGET_AMOUNT    = 0;
	/// The current amount of the current screen to have, range [0, 255].
	final static int k_PARAM_FULLSCREEN_BLEND_CURRENT_AMOUNT   = 1;
	/// The current state of the blending
	final static int k_PARAM_FULLSCREEN_BLEND_CURRENT_STATE    = 2;
	/// The rate at which the blend amount changes
	///!note Default value is GLPixEffects.k_DEFAULT_FULLSCREEN_BLEND_AMOUNT_INC
	final static int k_PARAM_FULLSCREEN_BLEND_AMOUNT_INC_RATE  = 3;
	/// For internal use only.
	final static int k_PARAM_FULLSCREEN_BLEND_SKIP_PAINT       = 4;
	/// How many times the current screen should be repainted to the buffer.
	///!see GLLib.PFX_SetBlendEffect
	final static int k_PARAM_FULLSCREEN_BLEND_REPAINT_NUM      = 5;
	/// Total number of blend parameters.
	final static int k_PARAM_FULLSCREEN_BLEND_NUM 			   = 6;
	//!}

	/// Default value of GLPixEffects.k_PARAM_FULLSCREEN_BLEND_AMOUNT_INC_RATE
	final static int k_DEFAULT_FULLSCREEN_BLEND_AMOUNT_INC 	   = 250;

	///!name BLEND STATES
	/// States that the fullscreen blend effect can be in.
	//!{
	/// Blending current state out
	final static int k_BLEND_STATE_IN 						   = 0;
	/// Blend is enabled and at the target value
	final static int k_BLEND_STATE_STABLE 					   = 1;
	/// Blending current state in
	final static int k_BLEND_STATE_OUT 						   = 2;
	/// Blending is done and off
	final static int k_BLEND_STATE_FINISHED 				   = 3;
	//!}
	///!}


	//--------------------- GRAYSCALE------------------------------------------
	///!defgroup PFX_SPRITE_GRAYSCALE Sprite Grayscale
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_SetParam
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_DisableAllSpriteEffects
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name SPRITE GRAYSCALE PARAMETERS
	//!{
	///	The type of alpha processing that should happen if alpha is enabled for this effect.
	///!note Default value is GLPixEffects.k_ALPHA_PROCESSING_AUTO
	final static int k_PARAM_GRAYSCALE_ALPHA_PROCESSING 	   = 0;
	/// Determines if alpha should be applied, -1 = no alpha processing, [0,255] sets alpha.
	final static int k_PARAM_GRAYSCALE_ALPHA				   = 1;
	/// Determines the grayscale amount. 0 = No Color, just grayscale, 255 = Full Color.
	final static int k_PARAM_GRAYSCALE_SATURATION			   = 2;
	/// Total number of grayscale effect parameters.
	final static int k_PARAM_GRAYSCALE_NUM 					   = 3;
	//!}
	///!}


	//--------------------- SHINE ---------------------------------------------
	///!defgroup PFX_SPRITE_SHINE Sprite Shine
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_SetParam
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_DisableAllSpriteEffects
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name SPRITE SHINE PARAMETERS
	//!{
	///	The type of alpha processing that should happen if alpha is enabled for this effect.
	///!note Default value is GLPixEffects.k_ALPHA_PROCESSING_AUTO
	final static int k_PARAM_SHINE_ALPHA_PROCESSING 		   = 0;
	/// The offset from the module left-side where to place the effect left-bounds
	final static int k_PARAM_SHINE_POSX 					   = 1;
	/// The width of the effect, as starting from the left-bound.
	final static int k_PARAM_SHINE_WIDTH 					   = 2;
	/// The color of the shine to apply.
	final static int k_PARAM_SHINE_COLOR 					   = 3;
	/// If true then the effect will be computed using absolute coordinates.
	final static int k_PARAM_SHINE_DISTANCE_ABSOLUTE           = 4;
	/// The maximum value of the shine as a power of 2.
	/// The default value is 8 (so max of 256).
	final static int k_PARAM_SHINE_MAX_VALUE_SHIFT             = 5;
	/// The total number of shine effect parameters.
	final static int k_PARAM_SHINE_NUM 						   = 6;
	//!}
	///!}


	//--------------------- GLOW ----------------------------------------------
	///!defgroup PFX_REGIONAL_GLOW Regional Glow
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_SetParam
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_DisableAllSpriteEffects
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name REGIONAL GLOW PARAMETERS
	//!{
	/// Threshold for red channel, threshold applied according to GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE
	final static int k_PARAM_GLOW_THRESHOLD_RED     		   = 0;
	/// Threshold for green channel, threshold applied according to GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE
	final static int k_PARAM_GLOW_THRESHOLD_GREEN			   = 1;
	/// Threshold for blue channel, threshold applied according to GLPixEffects.k_PARAM_GLOW_THRESHOLD_TYPE
	final static int k_PARAM_GLOW_THRESHOLD_BLUE			   = 2;
	/// The type of thresholding to use.
	final static int k_PARAM_GLOW_THRESHOLD_TYPE               = 3;
	/// The size of the horizontal margin to use for the effect (in case of undesired artifacts on the edges)
	final static int k_PARAM_GLOW_WIDTH_MARGIN      		   = 4;
	/// The size of the vertical margin to use for the effect (in case of undesired artifacts on the edges)
	final static int k_PARAM_GLOW_HEIGHT_MARGIN				   = 5;
	/// Fixed point period to control vertical blur and intensity
	final static int k_PARAM_GLOW_PERIOD1					   = 6;
	/// Fixed point period to control horizontal blur
	final static int k_PARAM_GLOW_PERIOD2					   = 7;
	/// Minimum strength of the horizontal blur
	final static int k_PARAM_GLOW_HSPREAD_MIN				   = 8;
	/// Minimum strength of the vertical blur
	final static int k_PARAM_GLOW_VSPREAD_MIN				   = 9;
	/// Minimum intensity of the glow
	final static int k_PARAM_GLOW_INTENSITY_MIN				   = 10;
	/// Maximum strength of the horizontal blur
	final static int k_PARAM_GLOW_HSPREAD_MAX				   = 11;
	/// Maximum strength of the vertical blur
	final static int k_PARAM_GLOW_VSPREAD_MAX				   = 12;
	/// Maximum intensity of the glow
	final static int k_PARAM_GLOW_INTENSITY_MAX				   = 13;
	/// Divisor for intensity
	final static int k_PARAM_GLOW_INTENSITY_DIVISOR 		   = 14;
	/// Holds the top-left x position of the glow area
	final static int k_PARAM_GLOW_X							   = 15;
	/// Holds the top-left y position of the glow area
	final static int k_PARAM_GLOW_Y							   = 16;
	/// Holds the width of the current glow area
	final static int k_PARAM_GLOW_WIDTH						   = 17;
	/// Holds the height of the current glow area
	final static int k_PARAM_GLOW_HEIGHT					   = 18;
	/// For internal use.
	final static int k_PARAM_GLOW_OFFSET					   = 19;
	/// Number of glow parameters.
	final static int k_PARAM_GLOW_NUM 						   = 20;
	//!}

	///!name GLOW THRESHOLD TYPES
	//!{
	/// Glow gets applied to DARK colors on a LIGHT background (filter out values ABOVE all thresholds)
	final static int k_THRESHOLD_FILTER_HIGH                   = 0;
	/// Glow gets applied to LIGHT colors on a DARK background (filter out values BELOW all thresholds)
	final static int k_THRESHOLD_FILTER_LOW                    = 1;
	//!}

	///!name GLOW DEFAULT VALUES
	//!{
	final static int k_DEFAULT_GLOW_THRESHOLD_RED   		   = 172;
	final static int k_DEFAULT_GLOW_THRESHOLD_GREEN 		   = 104;
	final static int k_DEFAULT_GLOW_THRESHOLD_BLUE  		   = 2;
	final static int k_DEFAULT_GLOW_THRESHOLD_TYPE             = k_THRESHOLD_FILTER_HIGH;

	final static int k_DEFAULT_GLOW_WIDTH_MARGIN 			   = 0;
	final static int k_DEFAULT_GLOW_HEIGHT_MARGIN 			   = 0;

	final static int k_DEFAULT_GLOW_PERIOD1 				   = 1;
	final static int k_DEFAULT_GLOW_PERIOD2 				   = 2;

	final static int k_DEFAULT_GLOW_HSPREAD_MIN   			   = 0x4;
	final static int k_DEFAULT_GLOW_VSPREAD_MIN   			   = 0x4;
	final static int k_DEFAULT_GLOW_INTENSITY_MIN 			   = 0x10;

	final static int k_DEFAULT_GLOW_HSPREAD_MAX   			   = 0x70;
	final static int k_DEFAULT_GLOW_VSPREAD_MAX   			   = 0x40;
	final static int k_DEFAULT_GLOW_INTENSITY_MAX 			   = 0x48;

	final static int k_DEFAULT_GLOW_INTENSITY_DIVISOR 		   = 3;
	//!}
	///!}


	//--------------------- BLEND ---------------------------------------------
	///!defgroup PFX_SPRITE_BLEND Sprite Blend (aka alpha)
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_SetParam
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_DisableAllSpriteEffects
	///!see GLLib.PaintFrameBlended
	///!see GLLibPlayer.SetBlend
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name SPRITE BLEND PARAMETERS
	//!{
	///	The type of alpha processing that should happen if alpha is enabled for this effect.
	///!note Default value is GLPixEffects.k_ALPHA_PROCESSING_AUTO
	final static int k_PARAM_BLEND_ALPHA_PROCESSING 		   = 0;
	/// The alpha to set
	final static int k_PARAM_BLEND_AMOUNT           		   = 1;
	/// The total number of sprite blend parameters
	final static int k_PARAM_BLEND_NUM                         = 2;
	//!}
	///!}


	//--------------------- SCALE ---------------------------------------------
	///!defgroup PFX_SPRITE_SCALE Sprite Scale
	///
	///!see GLLib.PFX_Init
	///!see GLLib.PFX_Update
	///!see GLLib.PFX_EnableEffect
	///!see GLLib.PFX_SetParam
	///!see GLLib.PFX_DisableEffect
	///!see GLLib.PFX_DisableAllSpriteEffects
	///!see ASprite.PaintFrameScaled
	///!see ASprite.DrawStringScaled
	///!see GLLibPlayer.SetScale
	///
	///!ingroup GLLib_pixEffects
	///!{

	///!name SPRITE SCALE PARAMETERS
	//!{
	///	The type of alpha processing that should happen if alpha is enabled for this effect.
	///!note Default value is GLPixEffects.k_ALPHA_PROCESSING_AUTO
	final static int k_PARAM_SCALE_ALPHA_PROCESSING 		   = 0;
	/// How much to scale, 100 = 100%.
	final static int k_PARAM_SCALE_PERCENT          		   = 1;
	/// The alpha value to set during the processing, -1 for none.
	final static int k_PARAM_SCALE_ALPHA            		   = 2;
	/// The number of sprite scale parameters.
	final static int k_PARAM_SCALE_NUM              		   = 3;
	//!}
	///!}
};
