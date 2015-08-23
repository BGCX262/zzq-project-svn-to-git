package cn.thirdgwin.app;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;
import cn.thirdgwin.lib.cMath;
import cn.thirdgwin.lib.zAnimPlayer;
import cn.thirdgwin.lib.zFont;
import cn.thirdgwin.lib.zServiceAnim;
import cn.thirdgwin.lib.zSoundPlayer;
import cn.thirdgwin.lib.zSprite;

public class zPlay {
	static public byte ct = 0;// 记数器
	private int m_mode;
	private int m_curIndex = 0; //光标位置
	private Subject m_sub;    //相当题目
	
	private int m_totalSub;   //本轮总题 数
	private int m_curSub = 0;  //当前题目ID
	private boolean m_showResult;
	private boolean m_isCorrect = true;//当前这题是否回答正确
	
	private int BG_ID;
	
	
	private static int m_score = 0;
	private long m_timeUsed;
	private long m_timeLimit;
	private int m_correctNum;
	private int m_finishNum;
	
	private zAnimPlayer m_effectRight;
	private zAnimPlayer m_effectWrong;
	
	private zAnimPlayer m_handupPlayer;
	private zAnimPlayer m_questionBGPlayer;
	
	private zAnimPlayer m_timerPlayer;
	
	private zAnimPlayer[] m_teacherPlayer;
	private int m_curTeacher;
	
	private static final int TIME_LIMIT = 60000;
	
	private zSoundPlayer m_soundWrong;
	public zSoundPlayer m_soundCorrect;

	public void Init()
	{
		
		m_curIndex = 0;
		m_curSub = 0;
		m_showResult = false;
		m_score = 0;
		m_timeUsed = 0;
		m_correctNum = 0;
		m_finishNum = 0;
		m_soundWrong = new zSoundPlayer();
		m_soundWrong.Load("/crow.wav",zSoundPlayer.TYPE_WAVE);
		m_soundCorrect = new zSoundPlayer();
		m_soundCorrect.Load("/bonus.mid",zSoundPlayer.TYPE_MIDI);

		
		BG_ID = cMath.Math_Rand(2,4);
		
		m_effectRight = zServiceAnim.AnimCreate(Const.MINames[10][0], Const.MINames[10][1]);
		m_effectWrong = zServiceAnim.AnimCreate(Const.MINames[11][0], Const.MINames[11][1]);
		m_handupPlayer = zServiceAnim.AnimCreate(Const.MINames[15][0], Const.MINames[15][1]);
		m_handupPlayer.SetAnim(HAND_UP.ANIM_POINTER, 1);
		
		m_questionBGPlayer = zServiceAnim.AnimCreate(Const.MINames[16][0], Const.MINames[16][1]);
		m_questionBGPlayer.SetAnim(BLACK_BOARD.ANIM_BG, -1);
		m_questionBGPlayer.SetPos(DevConfig.SCR_W/2, 0);
		
		m_timerPlayer = zServiceAnim.AnimCreate(Const.MINames[21][0], Const.MINames[21][1]);
		//#if SCREENWIDTH>176
		m_timerPlayer.SetAnim(time.ANIM_PANDA, -1);
		m_timerPlayer.SetPos(DevConfig.SCR_W /2, DevConfig.SCR_H / 12);
		//#endif
		
		
		m_teacherPlayer = new zAnimPlayer[3];
		for(int i = 0;i<m_teacherPlayer.length;i++)
		{
			m_teacherPlayer[i] = zServiceAnim.AnimCreate(Const.MINames[24+i][0], Const.MINames[24+i][1]);
			m_teacherPlayer[i].SetAnim(0, -1);
			m_teacherPlayer[i].SetPos(DevConfig.SCR_W * 3 / 4, DevConfig.SCR_H * 2 / 5);
		}
		m_curTeacher = cMath.Math_Rand(0, m_teacherPlayer.length);
		UpdateSubject();
	}
	
	public void Load(int libID, int mode) {
		m_mode = mode;
		
		Libs.Load(libID);
		
		switch(m_mode)
		{
		case Const.GM_NORMAL:
			m_totalSub = 20;
			break;
		case Const.GM_TIMER:
			m_totalSub = Libs.GetSubjectNum();
			m_timeLimit = TIME_LIMIT;
			break;
		case Const.GM_RANDOM:
			m_totalSub = 20;
			break;
		}
		
		Init();
	}

	public void Unload() 
	{
		Libs.Unload();
	
		m_sub = null;
		
		zServiceAnim.AnimDestroy(m_effectRight,true,true);
		m_effectRight = null;
		
		zServiceAnim.AnimDestroy(m_effectWrong,true,true);
		m_effectWrong = null;
		
		zServiceAnim.AnimDestroy(m_handupPlayer,true,true);
		m_handupPlayer = null;
		
		zServiceAnim.AnimDestroy(m_questionBGPlayer,true,true);
		m_questionBGPlayer = null;
		
		zServiceAnim.AnimDestroy(m_timerPlayer,true,true);
		m_timerPlayer = null;

		if(m_teacherPlayer != null)
		{
			for(int i = 0;i<m_teacherPlayer.length;i++)
			{
				zServiceAnim.AnimDestroy(m_teacherPlayer[i],true,true);
				m_teacherPlayer[i] = null;
			}
			
			m_teacherPlayer = null;
		}
		
		if(m_soundWrong != null)
		{
			m_soundWrong.Unload();
			m_soundWrong = null;
		}
		if(m_soundCorrect != null)
		{
			m_soundCorrect.Unload();
			m_soundCorrect = null;
		}
	}

	public void keyPressed(int keyCode)
	{
		int ansNum = m_sub.answers.length;
		switch (GameCanvas.getStateSub())
		{
			case Const.PLAY_S_Init:
				{
				GameCanvas.setStateSub(Const.PLAY_S_Play);
				break;
				}
			case Const.PLAY_S_Play:
				{
				switch(keyCode)
					{
						case DevConfig.KEY_UP:
						case DevConfig.KEY_NUM2:
						{
							m_curIndex = (m_curIndex + ansNum - 1) % ansNum;
							m_handupPlayer.SetAnim(HAND_UP.ANIM_POINTER, 1);
							break;
						}
						case DevConfig.KEY_DOWN:
						case DevConfig.KEY_NUM8:
						{
							m_curIndex = (m_curIndex + 1) % ansNum;
							m_handupPlayer.SetAnim(HAND_UP.ANIM_POINTER, 1);
							break;
						}
						case DevConfig.KEY_FIRE:
						case DevConfig.KEY_NUM5:
						case DevConfig.KEY_LSK:
						{
							if(!m_showResult && m_effectRight.IsAnimOver() && m_effectWrong.IsAnimOver())
							{
								m_isCorrect = (m_curIndex == m_sub.rightAnswerID);
								if(m_isCorrect)
								{
									m_score += m_sub.score;
									m_correctNum++;
									m_effectRight.SetAnim(ANSWERCORRECT.ANIM_CORRECT, 1);
									m_effectRight.SetPos(DevConfig.SCR_W>>1, DevConfig.SCR_H *1 / 3);
									
									if(GameCanvas.s_SoundOn)
									{
										m_soundCorrect.Play(1);
									}
								} else {
									m_effectWrong.SetAnim(ANSWERWRONG.ANIM_WRONG, 1);
									m_effectWrong.SetPos(DevConfig.SCR_W>>1, DevConfig.SCR_H * 1 / 3);
									if(m_mode == Const.GM_TIMER)
									{
										m_score -= m_sub.score;
										if(m_score <= 0)
										{
											m_score = 0;
										}
									}
									if(GameCanvas.s_SoundOn)
									{
										m_soundWrong.Play(1);
									}
								}
								m_showResult = true;
							} 
			
							break;
						}
						case DevConfig.KEY_RSK:
							GameCanvas.IGM.m_visible = true;
							GameCanvas.setStateSub(Const.PLAY_S_IGM);
							break;
					}
				break;
				}
			case Const.PLAY_S_IGM:
				{
				GameCanvas.IGM.keyPressed(keyCode);
				break;
				}
		};	
	}
	
	public void UpdateSubject()
	{
		if(m_curSub >= m_totalSub)
		{
			if(m_mode == Const.GM_TIMER)
			{
				m_curSub = 0;
				Libs.CreateSubject(m_totalSub);
			} else {
				GameCanvas.SetState(Const.GS_SUMMARY);
				//**************切换为上传积分界面*********************
				//**************切换为上传积分界面*********************
				return;
			}
			
			
		}
		m_sub = Libs.GetSubject(m_curSub);
		m_curSub++;
		m_finishNum++;
		m_curIndex = 0;
		m_curTeacher = cMath.Math_Rand(0, m_teacherPlayer.length);
	}

	public void Update() 
	{

	}

	public void Draw(Graphics g) 
	{
		switch (GameCanvas.getStateSub())
		{
		case Const.PLAY_S_Init:
			GameCanvas.setStateSub(Const.PLAY_S_Play);
			break;
		case Const.PLAY_S_Play:
			{
				DrawPlay(g);
				break;
			}
		case Const.PLAY_S_IGM:
			GameCanvas.IGM.Draw(g);
			if (!GameCanvas.IGM.m_visible)
				GameCanvas.setStateSub(Const.PLAY_S_Play);
			break;
		}
		
		
	}
	private void DrawPlay(Graphics g)
	{
		ct = (++ct >= 120) ? 0 : ct;
		g.setColor(0);
		g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
		DrawBG(g);
		DrawBlackBoard(g);
		DrawText(g);
		DrawScore(g);
		DrawTime(g);
		DrawJudge(g);
		
		m_timeUsed += GLLib.s_Tick_Paint_FrameDT;
		if(m_mode == Const.GM_TIMER)
		{
			m_timeLimit -= GLLib.s_Tick_Paint_FrameDT;
			if(m_timeLimit <= 0)
			{
				GameCanvas.SetState(Const.GS_SUMMARY);
				//**************切换为上传积分界面*********************
				//**************切换为上传积分界面*********************
			}
		}
	}
	private void DrawTime(Graphics g)
	{
		switch(m_mode)
		{
		case Const.GM_NORMAL:
		case Const.GM_RANDOM:
//#if SCREENWIDTH>=176
			DrawDigit(g, "s"+GetTimeString(m_timeUsed),DevConfig.SCR_W / 2, 10);
//#else
			DrawDigit(g, "s"+GetTimeString(m_timeUsed),DevConfig.SCR_W / 2, 2);
//#endif
//			g.setColor(0xFFFFFF);
//			g.drawString(GetTimeString(m_timeUsed), DevConfig.SCR_W/2, DevConfig.SCR_H / 30, Graphics.HCENTER | Graphics.TOP);
			break;
		case Const.GM_TIMER:
//#if SCREENWIDTH>=176
			DrawDigit(g, GetTimeString(m_timeLimit),DevConfig.SCR_W / 2, 10);
//#else
			DrawDigit(g, GetTimeString(m_timeLimit),DevConfig.SCR_W / 2, 2);
//#endif
			//#if SCREENWIDTH>176
			if(m_timeLimit <= TIME_LIMIT/6)
			{
				m_timerPlayer.Update();
				m_timerPlayer.Render(g);
			}	
			//#endif
			
//			int angle = (int)(m_timeLimit * 360 / TIME_LIMIT);
//			g.setColor(0xCC6600);
//			g.fillArc(DevConfig.SCR_W / 2 - 10, 6, 19, 19, 0, angle);
//			g.drawString(GetTimeString(m_timeLimit), DevConfig.SCR_W/2, DevConfig.SCR_H / 30, Graphics.HCENTER | Graphics.TOP);
			break;
		}
	}
	private void DrawDigit(Graphics g, String digit, int x, int y)
	{
		zSprite spr = m_timerPlayer.GetSprite();
		int[] rect = m_timerPlayer.GetFrameRect(time.ANIM_DIGIT_0, 0);
		int dw = rect[2] - rect[0];
		int dh = rect[3] - rect[1];
		rect = m_timerPlayer.GetFrameRect(time.ANIM_TIME, 0);
		int tw = rect[2] - rect[0];
		int th = rect[3] - rect[1];
		
		
		int curX = x - (dw * digit.length())/2;
		if(digit.charAt(0) == 's')
		{
			curX = curX + dw - tw; 
		} 
		for(int i = 0;i<digit.length();i++)
		{
			char c = digit.charAt(i);
			if(c >= '0' && c <= '9')
			{
				spr.PaintAFrame(g, time.ANIM_DIGIT_0 + c - '0', 0, curX, y, 0);
				curX += dw;
			}
			else if(c == '\'')
			{
				spr.PaintAFrame(g, time.ANIM_MINUTE, 0, curX, y, 0);
				curX += dw;
			}
			else if(c == '\"')
			{
				spr.PaintAFrame(g, time.ANIM_SECOND, 0, curX, y, 0);
				curX += dw;
			}
			else if(c == ':')
			{
				spr.PaintAFrame(g, time.ANIM_COLON, 0, curX, y, 0);
				curX += dw;
			}
			else if(c == 's')
			{
				spr.PaintAFrame(g, time.ANIM_TIME, 0, curX, y + dh / 2 - th / 2 , 0);
				curX += tw;
			}
			
		}
	}
	private void DrawScore(Graphics g)
	{
		
		
		DrawDigit(g,""+m_score,DevConfig.SCR_W / 2, DevConfig.SCR_H * 19 / 20);
		
//		g.setColor(0);
//		g.drawString(""+m_score, DevConfig.SCR_W / 2, DevConfig.SCR_H * 14 / 15, Graphics.HCENTER | Graphics.BOTTOM);
	}
	private void DrawJudge(Graphics g)
	{
		if(m_showResult)
		{
			if(!m_effectRight.IsAnimOver())
			{
				m_effectRight.Update();
				m_effectRight.Render(g);
				
			} else if(!m_effectWrong.IsAnimOver())
			{
				m_effectWrong.Update();
				m_effectWrong.Render(g);
			}
			
			
			if(m_effectRight.IsAnimOver() && m_effectWrong.IsAnimOver())
			{
				
				if(m_isCorrect == false){
					isShowResule = true;
					DrawShowResult(g);
				}else{
					UpdateSubject();
					isShowResule = false;
					m_showResult = false;
				}
			}
		}

	}
	//回答错误后，是否提示正确答案的标志
	boolean isShowResule = false;
	int ctt = 0;
	Image gou;
	/**
	 * 回答错误后，闪烁提示正确答案
	 * @param g
	 */
	private  void DrawShowResult(Graphics g){
//		ctt = (++ctt >= 120) ? 0 : ctt;
		ctt++;
		try {
			gou = Image.createImage("/gou.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setColor(0);
		g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
		DrawBG(g);
		DrawBlackBoard(g);
		DrawText(g);
		DrawScore(g);
		DrawTime(g);
		if(ctt >= 60){
			UpdateSubject();
			isShowResule = false;
			m_showResult = false;
			m_isCorrect = true;
			ctt=0;
		}
	}
	public void DrawBG(Graphics g) 
	{
		zAnimPlayer anim = GameCanvas.GetBGAnim(BG_ID);
		anim.SetPos(DevConfig.SCR_W>>1,DevConfig.SCR_H>>1);
		anim.Update();
		anim.Render(g);
	}
	public void DrawBlackBoard(Graphics g)
	{
		if(m_questionBGPlayer == null)
		{
			return;
		}
		m_questionBGPlayer.Update();
		m_questionBGPlayer.Render(g);
		
		m_teacherPlayer[m_curTeacher].Update();
		m_teacherPlayer[m_curTeacher].Render(g);
	}
	private void DrawText(Graphics g) 
	{
		if(m_sub == null)
			return;

		String quest = m_sub.question;
		int id = m_sub.rightAnswerID;
		String[] ans = m_sub.answers;

		zFont font = GameCanvas.MainFont;
			
		int[] bdRect = m_questionBGPlayer.GetCurFrameRect();
//#if SCREENWIDTH<"180" ||  MODEL=="S700" 
		quest = font.TextFitToFixedWidth(quest, DevConfig.SCR_W * 2 / 3);
		//#if SCREENWIDTH<176
		quest = font.TextFitToFixedWidth(quest, DevConfig.SCR_W);
		//#endif
//#elif MODEL=="N97"
		quest = font.TextFitToFixedWidth(quest, DevConfig.SCR_W * 2 / 4);
//#else
		quest = font.TextFitToFixedWidth(quest, DevConfig.SCR_W * 2 / 5);
//#endif

		//#if GAME_NAME=="EDU1"
		font.SetSystemFontColor(0xFFFFFF);
		//#elif GAME_NAME=="EDU2" || GAME_NAME=="EDU3" || GAME_NAME=="EduShuXue"  || GAME_NAME=="J_2011_12_Edu1_HappyNature" || GAME_NAME=="J_2011_11_Edu3_CrazyShuXue"
		font.SetSystemFontColor(0x000000);
		//#else
		font.SetSystemFontColor(0xFFFFFF);
		//#endif



//#if SCREENWIDTH<"180" ||  MODEL=="S700"
		font.DrawPage(g, quest, DevConfig.SCR_W>>1, bdRect[1] + (bdRect[3]-bdRect[1])/2, Graphics.HCENTER | Graphics.VCENTER);
//#else
		font.DrawPage(g, quest, DevConfig.SCR_W/3 + bdRect[0] + (bdRect[2]-bdRect[0])/2, bdRect[1] + (bdRect[3]-bdRect[1])/2, Graphics.HCENTER | Graphics.VCENTER);
//#endif
		font.SetSystemFontColor(0);
		final int LINE_HEIGHT = DevConfig.SCR_H / 6;
		int[] rectBar = m_questionBGPlayer.GetFrameRect(BLACK_BOARD.ANIM_BAR, 0);
		int ANSWER_Y = bdRect[3] + DevConfig.SCR_H / 40;
		
		
		int y = ANSWER_Y;
		
		for (int i = 0; i < ans.length; i++) 
			{
				m_questionBGPlayer.GetSprite().PaintAFrame(g, BLACK_BOARD.ANIM_BAR, 0, DevConfig.SCR_W / 2, y, 0);
				if(id == i){
					//乌鸦飞过后正确答案闪烁显示
					if(isShowResule == true ){
						if((ct % 10 < 5 )){
						font.DrawPage(g, ans[i], DevConfig.SCR_W / 2, y + (rectBar[3] - rectBar[1])/2,  Graphics.HCENTER | Graphics.VCENTER);
						}
						g.drawImage(gou, DevConfig.SCR_W / 2  + g.getFont().stringWidth(ans[i]), y + (rectBar[3] - rectBar[1])/2, Graphics.HCENTER | Graphics.VCENTER);
					}
					
					//回答问题或者答错乌鸦飞过时不闪烁显示
					if(m_isCorrect == true || isShowResule==false){
						font.DrawPage(g, ans[i], DevConfig.SCR_W / 2, y + (rectBar[3] - rectBar[1])/2,  Graphics.HCENTER | Graphics.VCENTER);
					}
					
					
				}else{
					font.SetSystemFontColor(0x00000);
					font.DrawPage(g, ans[i], DevConfig.SCR_W / 2, y + (rectBar[3] - rectBar[1])/2,  Graphics.HCENTER | Graphics.VCENTER);
				}
				if(i == m_curIndex)
				{
					m_handupPlayer.SetPos(DevConfig.SCR_W/10, y + (rectBar[3] - rectBar[1])/2);
					if(!m_handupPlayer.IsAnimOver())
					{
						m_handupPlayer.Update();
					}
					m_handupPlayer.Render(g);
				}
				
				y +=  LINE_HEIGHT;
			}
		GameCanvas.Arrow[ARROW.ANIM_UP].SetPos(DevConfig.SCR_W / 2, ANSWER_Y);
		GameCanvas.Arrow[ARROW.ANIM_UP].Update();
		GameCanvas.Arrow[ARROW.ANIM_UP].Render(g);
		
		GameCanvas.Arrow[ARROW.ANIM_DOWN].SetPos(DevConfig.SCR_W / 2, y);
		GameCanvas.Arrow[ARROW.ANIM_DOWN].Update();
		GameCanvas.Arrow[ARROW.ANIM_DOWN].Render(g);

	}
	
	public static int GetScore()
	{
		return m_score;
	}

	public int GetCorrectRate()
	{
		if(m_finishNum <= 0)
		{
			return 0;
		}
		return m_correctNum * 100 / m_finishNum;
	}
	
	public String GetTime()
	{
		if(m_mode == Const.GM_TIMER)
		{
			return GetTimeString(TIME_LIMIT);
		} 
		else 
		{
			return GetTimeString(m_timeUsed);
		}
	}
	public String GetTimeString(long timeTick)
	{
		long minute = timeTick / 60000;
		long second = timeTick % 60000 / 1000;

		return ""+minute+"'"+second+"\"";	
		
	}
	
	public final boolean isWin()
	{
		return (GetCorrectRate() >= 60);
	}
}
