import java.io.File;
import java.util.ArrayList;

public class FileSystem {
	// private static ArrayList filelist = new ArrayList();

	public static void refreshFileList(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null) {
			System.out.println("" + strPath + "�ļ�Ŀ¼�����ڻ����ļ�Ŀ¼��û���ļ��� ");
			return;
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				if (files[i].getName().toLowerCase().endsWith(".svn")) {
					deleteSVN(files[i].getAbsolutePath());
					if (files[i].delete()) {
						System.out.println("ɾ���ļ���--->>>>" + files[i]);
						ii++;
					}
				} else {
					refreshFileList(files[i].getAbsolutePath());
				}
			}
		}
	}
	public static void deleteSVN(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null) {
			System.out.println("" + strPath + "�ļ�Ŀ¼�����ڻ����ļ�Ŀ¼��û���ļ��� ");
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteSVN(files[i].getAbsolutePath());
				files[i].delete();
			}else{
				if (files[i].delete()) {
					System.out.println("ɾ���ļ�--->>>>" + files[i]);
					
					ii++;
				}
			}
		}
	}

	static int ii = 0;

	public static void main(String[] args) {
		refreshFileList("D:\\GoogleCode_SVN\\zzq-project\\Edu1\\trunk");
		System.out.println();
		System.out.println("ɾ���ļ���" + ii + "��");

	}
}
