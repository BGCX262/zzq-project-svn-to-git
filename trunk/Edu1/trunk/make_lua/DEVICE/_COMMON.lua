-- ���������ļ�
-- ע�Ȿϵͳ������__index��ʽ���м̳У������ø��Ƶķ���
DEV_COMMON =
		{
		Vender = "3gwin.cn",
		--MIDLET_CLASS = "JMIDlet",
		-- �豸��� --
		PLATFORM	= "J2ME",
		PLATFORM_VERSION = 2,
		PLATFORM_VERSION_STR	= "MIDP20",
		BRAND="Global",
		MODEL = "common",
		USE_BYTECODEOPTIMIZE = TRUE,
		USE_KZIP	= FALSE,
		ICON_SIZE = "64X64",
		NOKIA_DEVICE=TRUE,
		ENABLE_TOUCH		= FALSE,
		FPSLIMIT	= 12,
		SYSTEMFONT_SIZE_SMALL	 = 14,
		--  API  --
		MIDP10 = FALSE,
		MIDP20 = TRUE,
		NOKIA_UI = FALSE,
		WMA11 = TRUE,
		WMA20 = FALSE,
		CLDC10 = TRUE,
		CLDC11 = FALSE,
		JSR184 = FALSE,
		USE_MIDP10=FALSE,
		USE_MIDP20=TRUE,
		USE_CLDC10=TRUE,
		USE_CLDC11=FALSE,

		-- ���ݰ� --
		RESPACK	= "240_1M",
		RESPACK_WRAPPER = "240",
		SRCPACK	= "N73",
		RAMSIZE="1000",
		SCREENWIDTH="240",
		SCREENHEIGHT="320",

		USE_BITMAP_FONT = TRUE,
		SCREEN_ORIENTATION = "portrait",
		-- ����ģ��
		GC_BEFORE_NO_HEAP	= FALSE,			--  ��ǰ�ֹ�GC
		HAS_VOLUMECONTROL	= FALSE,			-- �Ƿ�����������
		--ABOUTUS			= FALSE,			-- �Ƿ��� ������Ϣ
		-- ��ֵ (N73) --
		KEY_LSK	= -6,
		KEY_RSK = -7,
		KEY_FIRE = -5,
		KEY_UP = -1,
		KEY_DOWN = -2,
		KEY_LEFT = -3,
		KEY_RIGHT = -4,
		KEY_VOLUMEUP = 0,
		KEY_VOLUMEDOWN = 0,
		SOFTKEY_OK_ON_LEFT = TRUE,
		-- API BUG --
		USE_CALLSERIALLY	= TRUE,
		USE_SERVICEREPAINT	= TRUE,
		USE_FAKE_INTERRUPT	= FALSE,			-- �Ƿ�ʹ�ü��ж�(�����ֻ��жϹ��ܲ�����)
		FAKE_INTERRUPT_THRESHOLD = 3000,			-- ���ж��ٽ�ֵ,��֡���Ƽ���������ֵʱ�򣬴����жϴ���
		
		}

print(string.format("\t%-20s%s",DEV_COMMON.BRAND,DEV_COMMON.MODEL))
