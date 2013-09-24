/**   
* @Title: MySqliteOpenHelper.java
* @Package net.tsz.afinal.db.sqlite
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-26 下午3:21:35
* @version V1.0
*/ 
package net.tsz.afinal.db.sqlite;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.widget.Toast;

/** 
 * @ClassName: MySqliteOpenHelper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈红建
 * @date 2013-7-26 下午3:21:35
 * 
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper
{
	private String defaultSql; // 创建数据库时执行的SQL语句
	private Object obj ; //一个对象的字节码
	private  String defaultTableName; // 默认的数据库表
	private Context mContext;
	private static MySqliteOpenHelper dbHelper;
	/**
	 * Title: Description:
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param onjClass 某个对象的字节码,如果这个对象不为空,说明要以对象字段的名称创建一张默认的表
	 * 这张表是 数据库首次创建时创建的表
	 */
	private MySqliteOpenHelper(Context context, String name , Object obj , String defaultTableName , String defaultSql)
	{
		
		super(context, name, null, 1);
		this.mContext = context;
		this.obj = obj;
		this.defaultTableName = defaultTableName;
		this.defaultSql = defaultSql;
	}

	/** 
	 * @Title: cerate
	 * @Description: 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	synchronized public static MySqliteOpenHelper create(Context context, String name , Object obj , String defaultTableName , String defaultSql)
	{
		if(dbHelper == null) {
			dbHelper = new MySqliteOpenHelper(context, name, obj, defaultTableName, defaultSql);
		}
		return dbHelper;
	}
	/**
	 * (非 Javadoc) Title: onCreate Description:
	 * 
	 * @param arg0
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		if(!TextUtils.isEmpty(defaultSql)) {
			//defaultSql这条语句是为了 更灵活的创建数据库,当用户指定一个默认的SQL语句时.说明程序可以不进行默认的数据库和表的创建的操作
			//此语句是没有反馈信息的.一般是为了创建默认数据库时灵活的进行表或者字段的创建
			db.execSQL(defaultSql);
		}else {
			
			//如果用户没有指定默认的数据库语句
			//执行默认创建数据库和表的操作
			if(this.obj != null) {
				//如果用户想通过一个javaBean的字段来创建默认的表
				initDB(db);
			}else {
				//如果用户不想通过 javaBean来创建默认的字段,那就不特么的创建表
				Toast.makeText(mContext,"不能创建一个空的数据表", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** 
	* @Title: initDB
	* @Description: 初始化数据库
	* @param 
	* @return void
	* @author 陈红建
	* @throws 
	*/ 
	private String initDB(SQLiteDatabase db)
	{
		String sql = "";
		try
		{
//			ContentValues values = new ContentValues();
			sql = "CREATE TABLE IF NOT EXISTS "+defaultTableName;
			StringBuffer sb = new StringBuffer();
			String l = "(";
			String r = ")";
			String d = ",";
			String varchar = "VARCHAR(200)"+d;
			//得到对象中的所有字段名称
			Field[] fields =  this.obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName()+" ";//得到要构造的字段名称
				//得到字段的值
//				Object fieldValue = (Object) field.get(obj);
//				//将值存入ContentValues中
//				if(fieldValue != null) {
//					
//					if(fieldValue.getClass().getName().equals("java.lang.String")) {
//						values.put(fieldName,  (String)fieldValue);
//					}else if(fieldValue.getClass().getName().equals("java.lang.Long")) {
//						values.put(fieldName,  (Long)fieldValue);
//						
//					}else if(fieldValue.getClass().getName().equals("java.lang.Integer")) {
//						values.put(fieldName,  (Integer)fieldValue);
//						
//					}else if(fieldValue.getClass().getName().equals("java.lang.Boolean")) {
//						values.put(fieldName,  (Boolean)fieldValue);
//					
//					}else if(fieldValue.getClass().getName().equals("java.lang.Float")) {
//						values.put(fieldName,  (Float)fieldValue);
//					
//					}else if(fieldValue.getClass().getName().equals("java.lang.Double")) {
//						values.put(fieldName,  (Double)fieldValue);
//					
//					}else if(fieldValue.getClass().getName().equals("java.lang.Byte")) {
//						values.put(fieldName,  (Byte)fieldValue);
//					
//					}else if(fieldValue.getClass().getName().equals("java.lang.Short")) {
//						values.put(fieldName,  (Short)fieldValue);
//					
//					}
//				}
//				values.put(fieldName, fieldValue);
				//构造数据库语句
				sb.append(fieldName+varchar);
			}
			//得到构造好的 字段字符串
			//去掉结尾的","得到 "name VARCHAR(200),age VARCHAR(200)"
			sb.deleteCharAt(sb.lastIndexOf(","));
			sql = sql + l + sb.toString() + r; //得到构造好的sql语句
			//创建这个表
			db.execSQL(sql);
//			//将默认数据插入库中
//			db.insert(defaultTableName, null, values);
		}
		catch (SecurityException e)
		{
			
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			
			e.printStackTrace();
		}
		
		return sql;
	}

	/**
	 * (非 Javadoc) Title: onUpgrade Description:
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{

	}


	public SQLiteDatabase getDb()
	{
		return getWritableDatabase();
	}

	

}

