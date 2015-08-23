package cn.thirdgwin.lib;

import javax.microedition.lcdui.Graphics;

public class zAnimNumber
	{
		public zAnimPlayer[]	Numbers;
		public String					Val;
		public int						ValWidth;
		public int						ValHeight;

		public int						FontWidth;
		public int						FontHeight;
		public int						Align;
		public String					m_sprName;
		public String					m_skinName;
		public int						ANIM_IDX_NUM0;
		public int						ANIM_IDX_MATHOP;	// +-*/()=:
		public String					FontText;
		public int charType;
		public final static int TYPENUMBER = 0;
		public final static int TYPEABC = 1+TYPENUMBER;
		public int abcTypeIndex;
		
		public void SetFontText(String str)
			{
				FontText = str;
			}

		public zAnimNumber( String sprName, String skinName, String fontText, int idx_of_num_0, int fontWidth, int fontHeight)
			{
				m_sprName = sprName;
				m_skinName = skinName;
				ANIM_IDX_NUM0 = idx_of_num_0;
				FontWidth = fontWidth;
				FontHeight = fontHeight;
				FontText =fontText;
				charType = TYPENUMBER;
			};
			public zAnimNumber( String sprName, String skinName, String fontText, int idx_of_num_0, int fontWidth, int fontHeight,int type)
				{
					m_sprName = sprName;
					m_skinName = skinName;
					ANIM_IDX_NUM0 = idx_of_num_0;
					FontWidth = fontWidth;
					FontHeight = fontHeight;
					FontText =fontText;
					charType = type;
				};
		public String GetText(){
				return Val;
		}
		public void SetText(String val)
			{
				int idx;
				Clean();
				Val = val;
				if(Val!="")
				if((int)Val.charAt(0)<97)
					abcTypeIndex = 65;
				else
					abcTypeIndex = 97;
				//****new*****
				
				//****new**
				Numbers = new zAnimPlayer[Val.length()];
				for (int i = Val.length() - 1; i >= 0; i--)
					{
						if(charType == TYPENUMBER)
							idx = FontText.indexOf(Val.charAt(i));
						else
							idx = (int)Val.charAt(i)-abcTypeIndex;
						if (idx >= 0)
							{
								Numbers[i] = zServiceAnim.AnimCreate(m_sprName, m_skinName);
								Numbers[i].SetAnim(ANIM_IDX_NUM0 + idx, -1);
							}
						;
					}
				ValWidth = FontWidth * Val.length();
				ValHeight = FontHeight;
			}
		public void SetText(String val,int loop)
		{
			int idx;
			Clean();
			Val = val;
			if(Val!="")
			if((int)Val.charAt(0)<97)
				abcTypeIndex = 65;
			else
				abcTypeIndex = 97;
			//****new*****
			
			//****new**
			Numbers = new zAnimPlayer[Val.length()];
			for (int i = Val.length() - 1; i >= 0; i--)
				{
					if(charType == TYPENUMBER)
						idx = FontText.indexOf(Val.charAt(i));
					else
						idx = (int)Val.charAt(i)-abcTypeIndex;
					if (idx >= 0)
						{
							Numbers[i] = zServiceAnim.AnimCreate(m_sprName, m_skinName);
							Numbers[i].SetAnim(ANIM_IDX_NUM0 + idx, loop);
						}
					;
				}
			ValWidth = FontWidth * Val.length();
			ValHeight = FontHeight;
		}
		public zAnimPlayer getAmin(){
			return Numbers[0];
		}
		public void Draw(Graphics g, int x, int y, int align)
			{
				if (Val == null)return;
				if ((align & Graphics.RIGHT) != 0)
					x -= ValWidth;
				else if ((align & Graphics.HCENTER) != 0)
					x -= (ValWidth>>1);
					
				if ((align & Graphics.BOTTOM) != 0)
					y -= ValHeight;
				else if ((align & Graphics.VCENTER) != 0)
					y -= (ValHeight>>1);
				zAnimPlayer num;
				for (int i = Numbers.length - 1; i >= 0; i--)
					{
						if ((num = Numbers[i]) != null)
							{
								num.SetPos(x + i * FontWidth, y);
								num.Update();
								num.Render(g);
							}
						;
					}

			}
			public void Clean()
			{
				Val = null;
				if (null != Numbers)
					for (int i = 0; i < Numbers.length; i++)
						{
							if (null != Numbers[i])
								{
									zServiceAnim.AnimDestroy(Numbers[i]);
									Numbers[i] = null;
								}
							;
						}
			}
	}
