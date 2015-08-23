package cn.thirdgwin.lib;

import java.util.Hashtable;

import javax.microedition.lcdui.Canvas;

public class cKeyboard {
	/// Timer used to disable keys
	private static int      s_keysDisabledTimer;
	/// keycode table for each key, for standard key
	private static java.util.Hashtable standardKeyTable;
	/// keycode table for each key, for game action key
	private static java.util.Hashtable gameActionKeyTable;
		/// Previous frame keys pressed. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_keys_pressed;
		/// Previous frame keys released. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_keys_released;
		/// Previous frame keys state. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_keys_state;
		/// Current keys state. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_current_keys_state;
		/// Current keys pressed. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_current_keys_pressed;
		/// Current keys released. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
		static int 			m_current_keys_released;
	    /// Previous key pressed. &note is valid only if zJYLibConfig.useKeyAccumulation is false.
	    public static int          m_last_key_pressed = -9999;
//--------------------------------------------------------------------------------------------------------------------
// Update the keypad. Called once per frame by the core system.
//-----------------------------------------------------------------------------
	public static void UpdateKeypad ()
	{
			final boolean ensureKeyProcessedInPoorDevice = true;
			
			if (ensureKeyProcessedInPoorDevice) {
				m_keys_pressed			= m_current_keys_pressed;
				
				int pressReleaseInOneFrame = m_current_keys_pressed & m_current_keys_released;
				if (pressReleaseInOneFrame != 0) {
					m_keys_released			= (m_current_keys_released & ~pressReleaseInOneFrame);
					m_current_keys_released = pressReleaseInOneFrame;
				} else {
					m_keys_released = m_current_keys_released;
					m_current_keys_released = 0;
				}
				
				m_keys_state &= ~m_keys_released;
				m_keys_state |= m_keys_pressed;
				
				m_current_keys_pressed 	= 0;
//				s_game_keyPressedTime	= s_game_timeWhenFrameStart;
			} else {
				m_keys_pressed			= m_current_keys_pressed;
				m_keys_released			= m_current_keys_released;
				m_keys_state			= m_current_keys_state;
				m_current_keys_pressed 	= 0;
				m_current_keys_released = 0;
//				s_game_keyPressedTime	= s_game_timeWhenFrameStart;
			}

		if (s_keysDisabledTimer > 0)
		{
			if (DevConfig.useFrameDT && (s_keysDisabledTimer != Integer.MAX_VALUE))
			{
				s_keysDisabledTimer -= GLLib.s_Tick_Paint_FrameDT;
			}

			ResetKey();
		}
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Setup the default key association for this device.
	/// in default key configuration, key2=up, key8=down, key4=left, key6=right and key5=fire<br>
	/// if you want to seperate those association, you need to reset the key configuration (Game_KeyClearKeyCode)<br>
	/// and use Game_keySetKeyCode so set your key code association
	///!see Game_keySetKeyCode
	///!see Game_keyClearKeyCode
	//--------------------------------------------------------------------------------------------------------------------
	public static void SetupDefaultKey ()
	{
		// reset all key association
		Game_KeyClearKeyCode();

		// set default key
		// here is use Hashtable.put instead of Game_keySetKeyCode(..) in order to save some byte code
		
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM0), 		new Integer(GLKey.k_num0));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM1), 		new Integer(GLKey.k_num1));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM2), 		new Integer(GLKey.k_num2));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM3), 		new Integer(GLKey.k_num3));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM4), 		new Integer(GLKey.k_num4));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM5), 		new Integer(GLKey.k_num5));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM6), 		new Integer(GLKey.k_num6));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM7), 		new Integer(GLKey.k_num7));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM8), 		new Integer(GLKey.k_num8));
			standardKeyTable.put(new Integer(DevConfig.KEY_NUM9), 		new Integer(GLKey.k_num9));

			standardKeyTable.put(new Integer(DevConfig.KEY_DIEZ), 	new Integer(GLKey.k_pound));
			standardKeyTable.put(new Integer(DevConfig.KEY_STAR), 		new Integer(GLKey.k_star));

			gameActionKeyTable.put(new Integer(DevConfig.KEY_FIRE),  new Integer(GLKey.k_fire));
			gameActionKeyTable.put(new Integer(DevConfig.KEY_UP),    new Integer(GLKey.k_up));
			gameActionKeyTable.put(new Integer(DevConfig.KEY_DOWN),  new Integer(GLKey.k_down));
			gameActionKeyTable.put(new Integer(DevConfig.KEY_LEFT),  new Integer(GLKey.k_left));
			gameActionKeyTable.put(new Integer(DevConfig.KEY_RIGHT), new Integer(GLKey.k_right));

		if(DevConfig.softkeyOKOnLeft)
		{
			standardKeyTable.put(new Integer(DevConfig.KEY_LSK),	new Integer(GLKey.k_menuOK));
			standardKeyTable.put(new Integer(DevConfig.KEY_RSK), 	new Integer(GLKey.k_menuBack));
		}
		else
		{
			standardKeyTable.put(new Integer(DevConfig.KEY_LSK),	new Integer(GLKey.k_menuBack));
			standardKeyTable.put(new Integer(DevConfig.KEY_RSK), 	new Integer(GLKey.k_menuOK));
		}

		if (DevConfig.useVolumeKeys)
		{
			standardKeyTable.put(new Integer(DevConfig.keycodeVolumeUp),	 new Integer(GLKey.k_volumeUp));
			standardKeyTable.put(new Integer(DevConfig.keycodeVolumeDown), new Integer(GLKey.k_volumeDown));
		}

	}
	/*
	 * 触摸，只模拟左右软键
	 * @see javax.microedition.lcdui.Canvas#pointerReleased(int, int)
	 */
	 protected static void pointerReleased(int x, int y)
	 {
	 }
	 
	//--------------------------------------------------------------------------------------------------------------------
	/// Key pressed method. Called when a key is pressed.
	///!param keyCode Key Code from the device. To be translated to GLLib constants.
	/// \sa Canvas.keyPressed
	//--------------------------------------------------------------------------------------------------------------------
	protected static void keyPressed(int keyCode)
	{
		if (DevConfig.useBugFixMultipleKeyPressed)
		{
			if (m_last_key_pressed != keyCode && m_last_key_pressed != -9999)
			{
				keyReleased(m_last_key_pressed);
			}
			m_last_key_pressed = keyCode;
		}
		
		{
			int keyFlag = Game_TranslateKeyCode(keyCode);

			m_current_keys_pressed |= keyFlag;
			m_current_keys_state |= keyFlag;
		}
	}
	 
	//--------------------------------------------------------------------------------------------------------------------
	/// Key released. Callded when a previously pressed key is released.
	///!param keyCode Key Code from the device. To be translated to GLLib constants.
	/// \sa Canvas.keyReleased
	//--------------------------------------------------------------------------------------------------------------------
	protected static void keyReleased (int keyCode)
	{
		if (DevConfig.useBugFixMultipleKeyPressed)
		{
			if (keyCode == m_last_key_pressed)
			{
				m_last_key_pressed = -9999;
			}
		}
		
		{
			int keyFlag = (Game_TranslateKeyCode(keyCode));

			m_current_keys_released |= keyFlag;
			m_current_keys_state &= ~keyFlag;
		}
	}





	//--------------------------------------------------------------------------------------------------------------------
	/// clear all the key code . no key input will be valid until you define new key association using Game_keySetKeyCode
	//--------------------------------------------------------------------------------------------------------------------
	public static void Game_KeyClearKeyCode()
	{
		gameActionKeyTable = new Hashtable();
		standardKeyTable = new Hashtable();
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set the keycode for a specific key
	///!param gameAction set to true if the key code is related to a game action.
	///!param keyCode keycode to associate.(eg Canvas.LEFT  for example)
	///!param key to associate with this key code. (eg GLKey.k_left for example)
	//--------------------------------------------------------------------------------------------------------------------
	public static void Game_KeySetKeyCode(boolean gameAction, int keyCode, int key)
	{
		//keyCodeValue[key] = keyCode;
		java.util.Hashtable hashtable = null;
		Integer ikey = new Integer(keyCode);

		if (gameAction)
		{
			hashtable = gameActionKeyTable;
		}
		else
		{
			hashtable = standardKeyTable;
		}

		Integer oldAssignation = (Integer) hashtable.get(ikey);
		if (oldAssignation != null)
		{
			hashtable.remove(ikey);
		}
		hashtable.put(ikey, new Integer(key));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Translate a keycode to a valid key value.
	///!param keyCode Key code to translate.
	///!return key Indice/value for this key code.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Game_TranslateKeyCode (int keyCode)
	{
		if ((DevConfig.useAbsoluteValueOfKeyCode) && (keyCode < 0))
			keyCode *= -1;

		Integer key = new Integer(keyCode);

		// check standard key

		// hai.phannhut fixed bug presses 5 repeatedly to launch game
		if(standardKeyTable == null)
		{
			return 0;
		}

		Integer code = (Integer) standardKeyTable.get(key);
		if (code != null)
			return code.intValue();

		//check gameAction keycode
		code = (Integer) gameActionKeyTable.get(key);
		if (code != null)
			return code.intValue();

	/*

		if (zJYLibConfig.useKeyAccumulation_useSwitchInsteadOfIfElse)
		{
			try
			{
				switch (getGameAction(keyCode))
				{
				case Canvas.UP: 					return GLKey.k_up;	//1
				case Canvas.LEFT:					return GLKey.k_left;	//2
				case Canvas.RIGHT:					return GLKey.k_right;//5
				case Canvas.DOWN:					return GLKey.k_down;	//6
				case Canvas.FIRE:					return GLKey.k_fire;	//8
				default:
				}
			}
			catch (Exception e)
			{
			}

	 		switch (keyCode)
			{
			case Canvas.KEY_POUND:					return GLKey.k_pound;//35
			case Canvas.KEY_STAR:					return GLKey.k_star;	//42
			case Canvas.KEY_NUM0:					return GLKey.k_num0;	//48
			case Canvas.KEY_NUM1:					return GLKey.k_num1;	//49
			case Canvas.KEY_NUM2:					return GLKey.k_num2;	//50
			case Canvas.KEY_NUM3:					return GLKey.k_num3;	//51
			case Canvas.KEY_NUM4:					return GLKey.k_num4;	//52
			case Canvas.KEY_NUM5:					return GLKey.k_num5;	//53
			case Canvas.KEY_NUM6:					return GLKey.k_num6;	//54
			case Canvas.KEY_NUM7:					return GLKey.k_num7;	//55
			case Canvas.KEY_NUM8:					return GLKey.k_num8;	//56
			case Canvas.KEY_NUM9:					return GLKey.k_num9;	//57
			default:
			}
		}
		else
		{
			try
			{
				int v = getGameAction(keyCode);
				if (v == Canvas.UP){				return GLKey.k_up;}		//1
				if (v == Canvas.LEFT){				return GLKey.k_left;}	//2
				if (v == Canvas.RIGHT){				return GLKey.k_right;}	//5
				if (v == Canvas.DOWN){				return GLKey.k_down;}	//6
				if (v == Canvas.FIRE){				return GLKey.k_fire;}	//8
			}
			catch (Exception e)
			{
			}

			if (keyCode == Canvas.UP){				return GLKey.k_up;}		//1
			if (keyCode == Canvas.LEFT){			return GLKey.k_left;}	//2
			if (keyCode == Canvas.RIGHT){			return GLKey.k_right;}	//5
			if (keyCode == Canvas.DOWN){			return GLKey.k_down;}	//6
			if (keyCode == Canvas.KEY_POUND){		return GLKey.k_pound;}	//35
			if (keyCode == Canvas.KEY_STAR){		return GLKey.k_star;}	//42
			if (keyCode == Canvas.KEY_NUM0){		return GLKey.k_num0;}	//48
			if (keyCode == Canvas.KEY_NUM1){		return GLKey.k_num1;}	//49
			if (keyCode == Canvas.KEY_NUM2){		return GLKey.k_num2;}	//50
			if (keyCode == Canvas.KEY_NUM3){		return GLKey.k_num3;}	//51
			if (keyCode == Canvas.KEY_NUM4){		return GLKey.k_num4;}	//52
			if (keyCode == Canvas.KEY_NUM5){		return GLKey.k_num5;}	//53
			if (keyCode == Canvas.KEY_NUM6){		return GLKey.k_num6;}	//54
			if (keyCode == Canvas.KEY_NUM7){		return GLKey.k_num7;}	//55
			if (keyCode == Canvas.KEY_NUM8){		return GLKey.k_num8;}	//56
			if (keyCode == Canvas.KEY_NUM9){		return GLKey.k_num9;}	//57

		}

		// soft key
		if (keyCode == KEY_S1){return GLKey.k_menuOK;}
		if (keyCode == KEY_S2){return GLKey.k_menuBack;}
	*/
		return GLKey.k_dummy;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Reset all keys to unpressed.
	//--------------------------------------------------------------------------------------------------------------------
	public static void ResetKey ()
	{
		{
			m_keys_pressed 			= 0;
			m_keys_released 		= 0;
			m_keys_state 			= 0;
			m_current_keys_state 	= 0;
			m_current_keys_pressed 	= 0;
			m_current_keys_released = 0;
		}
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Reset one key to unpressed state.
	///!param keyFlag Key to reset state.
	///!note If zJYLibConfig.useKeyAccumulation is False, this call is the same as ResetKey.
	//--------------------------------------------------------------------------------------------------------------------
	public static void ResetAKey (int keyFlag)
	{
		{
			if((m_keys_state & (keyFlag)) != 0)
			{
				m_keys_pressed 			= 0;
				m_keys_released 		= 0;
				m_keys_state 			= 0;
				m_current_keys_state 	= 0;
				m_current_keys_pressed 	= 0;
				m_current_keys_released = 0;
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Is there any key that is being held
	///!return GLKey.k_invalid if no key is held, the correct GLKey value otherwise.
	//--------------------------------------------------------------------------------------------------------------------
	public static int IsAnyKeyDown ()
	{
		{
			return m_keys_state;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Query a key to know if its down/pressed.
	///!param keyFlag Key to query.
	///!return Boolean value, true if the key is down/pressed.
	//--------------------------------------------------------------------------------------------------------------------
	public static boolean IsKeyDown (int keyFlag)
	{
		{
			return (m_keys_state & keyFlag) != 0;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Query a key to know if its up/unpressed.
	///!param keyFlag Key to query.
	///!return Boolean value, true if the key is up/unpressed.
	//--------------------------------------------------------------------------------------------------------------------
	public static boolean IsKeyUp (int keyFlag)
	{
		{
			return (m_keys_state & keyFlag) == 0;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Was there any key just pressed.
	///!return GLKey.k_invalid if no key was pressed, the correct GLKey value otherwise.
	//--------------------------------------------------------------------------------------------------------------------
	public static int WasAnyKeyPressed ()
	{
		{
			return m_keys_pressed;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Was there any key just released.
	///!return GLKey.k_invalid if no key was released, the correct GLKey value otherwise.
	//--------------------------------------------------------------------------------------------------------------------
	public static int WasAnyKeyReleased ()
	{
		{
			return m_keys_released;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Was there a key just pressed.
	///!param keyFlag Key to query.
	///!return Boolean value, true if the key was just pressed.
	//--------------------------------------------------------------------------------------------------------------------
	public static boolean WasKeyPressed (int keyFlag)
	{
		{
			return (m_keys_pressed & (keyFlag)) != 0;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Was there a key just released.
	///!param keyFlag Key to query.
	///!return Boolean value, true if the key was just released.
	//--------------------------------------------------------------------------------------------------------------------
	public static boolean WasKeyReleased (int keyFlag)
	{
		{
			return (m_keys_released & (keyFlag)) != 0;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Will disable the keys for the requested duration.
	///
	///!param duration - Time in ms for which not to return any key results
	///
	///!note Use Integer.MAX_VALUE to disable forever, until manually enabled.
	///!note Use -1 to re-enable immediately.
	///
	///!note If zJYLibConfig.useFrameDT is set to FALSE the timer will not auto-decrement. In this case the user needs to
	///       manually enable the keys again by setting the duration to 0. By default it is TRUE.

	///!see zJYLibConfig.useFrameDT
	//--------------------------------------------------------------------------------------------------------------------
	final static void DisableKeys (int duration)
	{
		s_keysDisabledTimer = duration;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Will disable the keys until enabled
	///!see DisableKeys (int duration)
	//--------------------------------------------------------------------------------------------------------------------
	final static void DisableKeys ()
	{
		DisableKeys(Integer.MAX_VALUE);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Will re-enable keys if they have been disabled
	///!see DisableKeys (int duration)
	//--------------------------------------------------------------------------------------------------------------------
	final static void EnableKeys ()
	{
		DisableKeys(-1);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
}
