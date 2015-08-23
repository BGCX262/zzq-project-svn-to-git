package cn.thirdgwin.lib;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;


/**
 * zSoundPlayer
 * 
 */
public class zSoundPlayer 
{

	private static final String[] AUDIO_TYPES = 
	{	
		"audio/midi",
		"audio/x-wav",
	};
	public static final int _TYPE_NONE  = -1;
	
	public static final int TYPE_MIDI = _TYPE_NONE + 1;
	public static final int TYPE_WAVE = TYPE_MIDI + 1;
	//add format here..
	
	public static final int _TYPE_NUM = TYPE_WAVE + 1;
	
	private Player m_player;

	
	/**
	 * android似乎不支持InputStream#reset，用void Load(String path, int type)代替
	 * @param path
	 */
	public void Load(String path)
	{
		if(isLoaded())
			return;
		InputStream is = Utils.GetResourceAsStream(path);
		int format = GetFormat(is);
		if(format == _TYPE_NONE)
		{
			try {
				is.close();
			} catch (IOException e) {}
			return;
		}
		Load(is, format);
		
	}
	/**
	 * android似乎不支持InputStream#reset,需要手动写.
	 * @param path
	 * @param type (zSoundPlayer.TYPE_MIDI, zSoundPlayer.TYPE_WAVE)
	 */
	public void Load(String path, int type)
	{
		InputStream is = Utils.GetResourceAsStream(path);
		Load(is, type);
	}

	private void Load(InputStream is, int type)
	{
		if(isLoaded())
			return;
		if(type <= _TYPE_NONE || type >= _TYPE_NUM)
			return;
		
		try {
			m_player = Manager.createPlayer(is, AUDIO_TYPES[type]);
			m_player.realize();
			m_player.prefetch();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (MediaException e) {
			
			e.printStackTrace();
		}
	}
	private int GetFormat(InputStream is)
	{
		int nFormat = TYPE_MIDI;
		byte[] bf = new byte[12];
		
		try {
			
			is.read(bf);
			if(
			  bf[0] == 'R' 
			  && bf[1] == 'I'
			  && bf[2] == 'F'
			  && bf[3] == 'F'
			  && bf[8] == 'W' 
			  && bf[9] == 'A'
			  && bf[10] == 'V'
			  && bf[11] == 'E')
			{
				nFormat = TYPE_WAVE;
			}
			is.reset();
		} catch (IOException e1) 
		{
			return _TYPE_NONE;
		}

		return nFormat;
	}
	
	
	public boolean isLoaded()
	{
		return m_player != null;
	}
	public void Unload()
	{
		if(m_player == null)
		{
			return;
		}
		try {
			if(m_player.getState() == Player.STARTED)
			{
				m_player.stop();
			}
			} catch (MediaException e) 
			{
				e.printStackTrace();
			}
			
		m_player.deallocate();
		m_player.close();
		
		m_player = null;
	}
	public void Play(int loop)
	{
		if(m_player == null)
		{
			return;
		}
		int state = m_player.getState();
		
		if(state == Player.STARTED)
		{
			Stop();
		}
		
		if(state == Player.UNREALIZED)
		{
			try {
				m_player.realize();
			} catch (MediaException e) {}
		}
		
		if(loop <= 0)
		{
			loop = -1;
		}
		m_player.setLoopCount(loop);
		SetVolume(100);
		try {
			m_player.start();
		} catch (MediaException e) {
			e.printStackTrace();
		}
		
	}
	public void SetVolume(int volume)
	{
		
		if(m_player != null)
		{
			VolumeControl vc = (VolumeControl)m_player.getControl("VolumeControl");
			if(vc != null)
			{
				vc.setLevel(volume);
			}	
		}
	}
	public void Play()
	{
		Play(-1);
	}
	public void Pause()
	{
		try {
			if(m_player.getState() == Player.STARTED)
			{
				m_player.stop();
			}
		} catch (MediaException e) 
		{
			e.printStackTrace();
		}
		
	}
	public void Resume()
	{
		try {
			m_player.start();
		} catch (MediaException e) 
		{
			e.printStackTrace();
		}
	}
	public void Stop()
	{
		try {
			if(m_player.getState() == Player.STARTED)
				{
					m_player.stop();
				}
		} catch (MediaException e) 
		{
			e.printStackTrace();
		}
	}
	
	//TO implement
	static void Snd_PauseNotify() 
	{
		
	}
	
}
