package com.gdut.dongjun.util;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gdut.dongjun.domain.po.ModuleHitchEvent;

/**
 * 根据po类自动生成对应的dao接口和实现类，service接口和实现类 
 * Service的实现类要自己添加未实现的方法
 * 
 * @author Gordan_Deng
 * @date 2017年4月12日
 */
public class CodeCreator {

	// 公共部分
	private static final String RT_1 = "\r\n";
	private static final String RT_2 = RT_1 + RT_1;
	private static final String BLANK_1 = " ";
	private static final String BLANK_4 = "    ";
	private static final String BLANK_8 = BLANK_4 + BLANK_4;

	// 注释部分
	private static final String ANNOTATION_AUTHOR_PARAMTER = "@author ";
	private static final String ANNOTATION_AUTHOR_NAME = "Gordan_Deng";
	private static final String ANNOTATION_AUTHOR = ANNOTATION_AUTHOR_PARAMTER + ANNOTATION_AUTHOR_NAME;
	private static final String ANNOTATION_DATE = "@date ";
	private static final String ANNOTATION = "/**" + RT_1 + BLANK_1 + "*" + BLANK_1 + ANNOTATION_AUTHOR + RT_1 + BLANK_1
			+ "*" + BLANK_1 + ANNOTATION_DATE + getDate() + RT_1 + BLANK_1 + "*/" + RT_1;

	// 文件 地址
	// private static final String BEAN_PATH = "com/b510/base/bean";
	private static final String DAO_PATH = "main/java/com/gdut/dongjun/domain/dao";
	private static final String DAO_IMPL_PATH = "main/java/com/gdut/dongjun/domain/dao/impl";
	private static final String SERVICE_PATH = "main/java/com/gdut/dongjun/service";
	private static final String SERVICE_IMPL_PATH = "main/java/com/gdut/dongjun/service/impl";

	// 包名
	private static final String BEAN_URL = "com.gdut.dongjun.domain.po";
	private static final String DAO_URL = "com.gdut.dongjun.domain.dao";
	private static final String DAO_IMPL_URL = "com.gdut.dongjun.domain.dao.impl";
	private static final String SERVICE_URL = "com.gdut.dongjun.service";
	private static final String SERVICE_IMPL_URL = "com.gdut.dongjun.service.impl";

	// 基本类名称
	private static final String BASE_DAO_NAME = DAO_URL + ".BaseDao";
	private static final String BASE_SERVICE_NAME = SERVICE_URL + ".BaseService";
	private static final String DAO_INTERFACE = "Mapper.java";
	private static final String DAO_IMPLEMENT = "DAOImpl.java";
	private static final String SERVICE_INTERFACE = "Service.java";
	private static final String SERVICE_IMPLEMENT = "ServiceImpl.java";
	private static final String BASE_DAO_INTERFACE_IMPORT = "import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;";
	private static final String PO_IMPORT = "import com.gdut.dongjun.domain.po.";
	private static final String REPOSITORY_IMPORT = "import org.springframework.stereotype.Repository;";
	private static final String BASE_DAO_IMPLEMENT_IMPORT = "import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;";
	private static final String IMPORT = "import";
	private static final String BASE_SERVICE_INTERFACE_IMPORT = "import com.gdut.dongjun.service.base.BaseService;";
	private static final String BASE_SERVICE_IMPLEMENT_IMPORT = "import com.gdut.dongjun.service.base.impl.BaseServiceImpl;";
	private static final String SERVICE_IMPORT = "import org.springframework.stereotype.Service;";
	
	/**
	 * 创建bean的Dao<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanDao(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + DAO_PATH + "/" + getLastChar(cName) + DAO_INTERFACE;
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package " + DAO_URL + ";" + RT_2 //包名
				+ BASE_DAO_INTERFACE_IMPORT + RT_1 	//导入BaseDao接口
				+ PO_IMPORT + getLastChar(cName) + ";" + RT_2 //导入po类
				+ "public interface " + getLastChar(cName) + "Mapper extends " 
				+ "SinglePrimaryKeyBaseMapper" + " <" + getLastChar(cName) + "> {" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的Dao的实现类
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanDaoImpl(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + DAO_IMPL_PATH + "/" + getLastChar(cName) + DAO_IMPLEMENT;
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package " + DAO_IMPL_URL + ";" + RT_2
				+ REPOSITORY_IMPORT + RT_2		//注解
				+ IMPORT + BLANK_1 + DAO_URL + "." + getLastChar(cName) + "Mapper" + ";" + RT_1		//dao类
				+ BASE_DAO_IMPLEMENT_IMPORT + RT_1	//BaseDaoImpl
				+ PO_IMPORT + getLastChar(cName) + ";" + RT_2 	//po类
				+ "@Repository" + RT_1
				+ "public class " + getLastChar(cName) + "DAOImpl extends " 
				+ "SinglePrimaryKeyBaseDAOImpl" + "<" + getLastChar(cName) + "> implements "
				+ getLastChar(cName) + "Mapper {" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的service
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanService(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + SERVICE_PATH + "/" + getLastChar(cName)
				+ "Service.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package " + SERVICE_URL + ";" + RT_2
				+ BASE_SERVICE_INTERFACE_IMPORT + RT_1	//BaseService
				+ PO_IMPORT + getLastChar(cName) + ";" + RT_2		//po类
				+ "public interface " + getLastChar(cName)
				+ "Service extends " + "BaseService" + "<" + getLastChar(cName) + "> {" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的service的实现类
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanServiceImpl(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + SERVICE_IMPL_PATH + "/" + getLastChar(cName)
				+ "ServiceImpl.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package " + SERVICE_IMPL_URL + ";" + RT_2
				+ SERVICE_IMPORT + RT_2
				+ BASE_SERVICE_IMPLEMENT_IMPORT + RT_1
				+ IMPORT + BLANK_1 + SERVICE_URL + "." + getLastChar(cName) + "Service" + ";" + RT_1
				+ PO_IMPORT + getLastChar(cName) + ";" + RT_2		//po类
				+ "@Service" + RT_1
				+ "public class " + getLastChar(cName) + "ServiceImpl extends " 
				+ "BaseServiceImpl" + "<" + getLastChar(cName) + "> implements " + getLastChar(cName) + "Service {" + RT_1 + " }"); 
//				+ RT_2 + BLANK_4 + "private " + DAO_URL + "."
//				+ getLastChar(cName) + "Dao " + getLowercaseChar(getLastChar(cName)) + "Dao;" + RT_2 + BLANK_4
//				+ "public void set" + getLastChar(cName) + "Dao(" + DAO_URL + "." + getLastChar(cName) + "Dao "
//				+ getLowercaseChar(getLastChar(cName)) + "Dao){" + RT_1 + BLANK_8 + "this."
//				+ getLowercaseChar(getLastChar(cName)) + "Dao = " + getLowercaseChar(getLastChar(cName)) + "Dao;" + RT_1
//				+ BLANK_4 + "}" + RT_2 + BLANK_4 + "@Override" + RT_1 + BLANK_4 + "public " + DAO_URL + "." + "BaseDao<"
//				+ BEAN_URL + "." + getLastChar(cName) + "> getBaseDao(){" + RT_1 + BLANK_8 + "return "
//				+ getLowercaseChar(getLastChar(cName)) + "Dao;" + RT_1 + BLANK_4 + "}" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 获取路径的最后面字符串<br>
	 * 如：<br>
	 * <code>str = "com.b510.base.bean.User"</code><br>
	 * <code> return "User";<code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLastChar(String str) {
		if ((str != null) && (str.length() > 0)) {
			int dot = str.lastIndexOf('.');
			if ((dot > -1) && (dot < (str.length() - 1))) {
				return str.substring(dot + 1);
			}
		}
		return str;
	}

	/**
	 * 把第一个字母变为小写<br>
	 * 如：<br>
	 * <code>str = "UserDao";</code><br>
	 * <code>return "userDao";</code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLowercaseChar(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 显示信息
	 * 
	 * @param info
	 */
	public void showInfo(String info) {
		System.out.println("创建文件：" + info + "成功！");
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
//	public static String getDate() {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		return simpleDateFormat.format(new Date());
//	}
//	
//	public static void main(String[] args) throws Exception {
//		CodeCreator creator = new CodeCreator();
//		List<Class> list = new ArrayList<Class>();
//		list.add(ModuleHitchEvent.class);
//		
//		for (Class c : list) {
//			creator.createBeanDao(c);	
//			creator.createBeanDaoImpl(c);
//			creator.createBeanService(c);
//			creator.createBeanServiceImpl(c);
//		}
//	}
}
