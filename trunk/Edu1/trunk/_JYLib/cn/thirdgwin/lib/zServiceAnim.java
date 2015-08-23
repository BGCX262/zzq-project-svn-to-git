package cn.thirdgwin.lib;

public class zServiceAnim
	{

	public static zAnimPlayer AnimCreate(zSprite spr)
	{
			zAnimPlayer anim = zAnimPlayer.Create();
			anim.SetSprite(spr);
			return anim;
	}

	public static zAnimPlayer AnimCreate(String sprname,String pngname)
	{
		return zServiceAnim.AnimCreate(sprname,new String[] {pngname});
	}

	public static zAnimPlayer AnimCreate(String sprname,String[] pngnames)
	{
			zSprite spr = zServiceSprite.Get(sprname,pngnames,false);
			zAnimPlayer anim = zAnimPlayer.Create();
			anim.SetSprite(spr);
			return anim;
	}

	public static void AnimDestroy(zAnimPlayer player)
	{
		if(player != null)
		{
			zServiceSprite.Put(player.GetSprite(),false,false);
		}
		
	}

	public static void AnimDestroy(zAnimPlayer player,boolean freeWhenNoNeed,boolean freeImageWhenNoNeed)
	{
		if(player != null)
		{
			zServiceSprite.Put(player.GetSprite(),freeWhenNoNeed,freeImageWhenNoNeed);
		}
		
	}

	}
