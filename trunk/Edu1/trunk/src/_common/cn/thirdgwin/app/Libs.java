package cn.thirdgwin.app;

import java.util.Vector;

import cn.thirdgwin.io.zByteArrayStream;
import cn.thirdgwin.lib.Utils;
import cn.thirdgwin.lib.cMath;


final class Subject
{
	public int type;//reserve
	public String question;
	public int rightAnswerID;
	public String[] answers;
	public int score;
}

public class Libs 
{
	/**原始题库*/ 
	private static Subject[] s_subs;
	/***/
	private static int[]     s_idxs;


	public static void CreateSubject(int count) 
	{
		if(s_subs == null || count <= 0 || count > s_subs.length)
		{
			return;
		}
		
		s_idxs = new int[count];
	
		
		for(int i = 0;i<count;i++)
		{
			s_idxs[i] = i;
		}
		
		for(int i = 0;i<count;i++)
		{
			int rand = cMath.Math_Rand(0, count);
			int temp = s_idxs[0];
			s_idxs[0] = s_idxs[rand];
			s_idxs[rand] = temp;
		}
		/** 打乱答案 */
		for(int i = 0;i<count;i++)
		{
			Subject sub = s_subs[s_idxs[i]];
			int rand = cMath.Math_Rand(0, sub.answers.length);
			String strTemp = sub.answers[sub.rightAnswerID];
			sub.answers[sub.rightAnswerID] = sub.answers[rand];
			sub.answers[rand] = strTemp;
			sub.rightAnswerID = rand;
		}
	}
	private static void CreateSubjectForRandomMode(int libcount,int TotalCount) 
		{
			String[] catalog;
			int Added = 0;
			int needCount = 0;
			int AddedLib = 0;
			int libindex = 0;
			
			int totalinlib = 0;
			while (Added < TotalCount)
			{
			AddedLib ++;
			
			
			
			if (AddedLib>=libcount)
			{
				needCount = cMath.Math_Rand(1,TotalCount - Added);
			}
			else
			{
				needCount = (TotalCount - Added);
			}
			
			libindex = cMath.Math_Rand(1, libcount);
			
			catalog = Libs.GetCatalog(libindex);
			totalinlib = Integer.valueOf(catalog[CAT_TOTAL]).intValue();
			if (totalinlib<needCount)needCount = totalinlib;
			
			s_idxs = new int[needCount];
			
			for(int i = 0;i<needCount;i++)
			{
				s_idxs[i] = i;
			}
			
			for(int i = 0;i<needCount;i++)
			{
				int rand = cMath.Math_Rand(0, needCount);
				int temp = s_idxs[0];
				s_idxs[0] = s_idxs[rand];
				s_idxs[rand] = temp;
			}
			
			for(int i = 0;i<needCount;i++)
			{
				Subject sub = s_subs[s_idxs[i]];
				int rand = cMath.Math_Rand(0, sub.answers.length);
				String strTemp = sub.answers[sub.rightAnswerID];
				sub.answers[sub.rightAnswerID] = sub.answers[rand];
				sub.answers[rand] = strTemp;
				sub.rightAnswerID = rand;
			}
			};
		}	
	public static Subject GetSubject(int idx)
	{
		if(s_subs == null || idx < 0 || idx >= s_idxs.length)
			return null;
		
		return s_subs[s_idxs[idx]];
	}

	/**************************************************************************\
	 *  库结构：
	 *  总题数(int)
	 *  题1的分数(byte),文字条数(byte)，
	 *  题1的文字( 长度(short),byte[]) * 文字条数
	\**************************************************************************/
	public static void Load(int libidx)
	{
		byte[] data = Utils.GetBin("/lib/ct_" + (libidx+1));
		if(data == null)
		{
			return;
		}
		zByteArrayStream bas = new zByteArrayStream(data);
	
		int ItemCount = bas.GetInt();
		if(ItemCount <= 0)
		{
			return;
		}
		s_subs = new Subject[ItemCount];
		for(int i = 0;i<ItemCount;i++)
		{
			s_subs[i] = new Subject();
			s_subs[i].score = bas.GetByte();
			
			int textCount = bas.GetByte();
			s_subs[i].rightAnswerID = 0;
			s_subs[i].question = bas.GetStr();//LoadString(bas);
			s_subs[i].answers = new String[textCount - 1];
			for(int a = 0;a<textCount - 1;a++)
			{
				s_subs[i].answers[a] = bas.GetStr();// LoadString(bas);
			}
		}
		
		CreateSubject(ItemCount);
	}
	public static int GetSubjectNum()
	{
		return s_subs.length;
	}
//	public static String LoadString(zByteArrayStream bas)
//    {
//        String str = null;
//        short msgLen = bas.GetShort();
//        if (msgLen <= 0)
//            return null;
//        byte[] utfMsg = new byte[msgLen];
//        bas.GetArray(utfMsg, 0, msgLen);
//        try
//        {
//            str = new String(utfMsg,"UTF-8");
//        }
//        catch (Exception e)
//        {
//            str = null;
//        }
//        return str;
//    }
	public static void Unload()
	{
		s_subs = null;
	}
	
	/**************************************************************************\
	 *  库目录：
	 *  库总数(short)
	 *  库1标题文字(长度(short),byte[])
	 *  库1标题文字(库文件名(short),byte[])
	 *  库1开启等级(short)	达到此等级后，才可挑战
	 *  库1题目数量(short)	本库题目数量	
	\**************************************************************************/
	public static final int CAT_TITLE	= 0;
	public static final int CAT_LIBNAME	= 1 + CAT_TITLE;
	public static final int CAT_NEEDLV	= 1 + CAT_LIBNAME;
	public static final int CAT_TOTAL	= 1 + CAT_NEEDLV;
	public static final int CAT_MAX	= 1 + CAT_TOTAL;
	public static Vector Catalog = new Vector(10);
	
	public static void LoadCatalog()
	{
		zByteArrayStream bas = new zByteArrayStream("/lib/cat");
		int Count = bas.GetShort();
		String[] Line;
		for(int i = 0;i<Count;i++)
		{
			Line = new String[CAT_MAX];
			Line[CAT_TITLE] = bas.GetStr();
			Line[CAT_LIBNAME] = bas.GetStr();
			Line[CAT_NEEDLV] = String.valueOf(bas.GetShort());
			Line[CAT_TOTAL] = String.valueOf(bas.GetShort());
			Catalog.addElement(Line);
		}
		
	}
	public static String[] GetCatalog(int idx)
		{
			return (String[])Catalog.elementAt(idx);
		}

}
