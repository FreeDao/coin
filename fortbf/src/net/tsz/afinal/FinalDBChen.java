/**   
 * @Title: FinalDBChen.java
 * @Package net.tsz.afinal
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 陈红建
 * @date 2013-7-26 上午11:28:56
 * @version V1.0
 */
package net.tsz.afinal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.tsz.afinal.db.sqlite.MySqliteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * @ClassName: FinalDBChen
 * @Description: 数据库操作类
 * @author 陈红建
 * @date 2013-7-26 上午11:28:56
 * 
 */
public class FinalDBChen
{

	private SQLiteDatabase database;
	private String tabName;

	/**
	 * Title: Description:
	 */
	public FinalDBChen(Context context, String dbName, Object obj,
			String defaultTableName, String defaultSql)
	{
		this.tabName = defaultTableName;
		if (TextUtils.isEmpty(defaultTableName))
		{
			// 如果默认表名为空.以应用的包名进行创建
			defaultTableName = context.getPackageName();
		}

		MySqliteOpenHelper sqlOpenHelper = MySqliteOpenHelper.create(context,
				dbName, obj, defaultTableName, "");
		database = sqlOpenHelper.getDb();
	}

	public SQLiteDatabase getDatabase()
	{
		return database;
	}

	public void setDatabase(SQLiteDatabase database)
	{
		this.database = database;
	}

	public void closeDb()
	{
		
	}

	/**
	 * Title: Description:通过一个已经存在的数据库名字初始化对象
	 */
	public FinalDBChen(Context mContext , String dbName)
	{
//		database = SQLiteDatabase.openOrCreateDatabase(databaseNamePath, null);
		MySqliteOpenHelper sqlOpenHelper = MySqliteOpenHelper.create(mContext,
				dbName, null, null, null);
		database = sqlOpenHelper.getWritableDatabase();
	}

	
	// 增删改查和更新
	private String sel = "SELECT * FROM ";
	private String where = " WHERE ";

	/** 
	 * @Title: updateValue
	 * @Description: 通过一个条件来更新数据库中指定的数据 
	 * @param  whereName 更新目标
	 * @param  whereValue 更新的值
	 * @param  updateValues 需要更新的数据
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	public void updateValue(String tabName , String whereName ,String[] whereValue , ContentValues updateValues)
	{
		database.update(tabName, updateValues, whereName, whereValue);
		
	}
	/** 
	 * @Title: updateValuesByJavaBean
	 * @Description: 通过一个对象来更新一条数据
	 * @param  whereName 更新目标
	 * @param  whereValue 更新的值
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	public void updateValuesByJavaBean(String tabName , String whereName ,String[] whereValue , Object obj)
	{
		try
		{
			ContentValues values = new ContentValues();
			//得到这个对象的字段名和字段值
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				field.setAccessible(true);
				String key = field.getName();
				String value = field.get(obj)+"";
				values.put(key, value);
			}
			updateValue(tabName, whereName, whereValue, values);
		}
		catch (SecurityException e)
		{
			
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: insertMap
	 * @Description: 将一个Map<String,String>对象插入数据库
	 * @param
	 * @return void
	 * @author 陈红建
	 * @throws
	 */
	public long insertMap(Map<String, String> map, String tabName)
	{
		long status = -1;
		if (map != null && map.size() != 0)
		{
			// 遍历这个Map
			ContentValues values = new ContentValues();
			Set<String> key = map.keySet();
			for (Iterator<String> it = key.iterator(); it.hasNext();)
			{

				String s = (String) it.next();
				String v = map.get(s);
				// 构造数据
				values.put(s, v);
			}
			status = database.insert(tabName, null, values);
			
		}
		return status;
	}

	/**
	 * @Title: insertByListOrMap
	 * @Description: 将一个List<JavaBean>对象插入数据库
	 * @param
	 * @return void
	 * @author 陈红建
	 * @throws
	 */
	public <T> long insertBeanList(List<? extends Object> list, String tabName)
	{
		long status = -1;
		try
		{
			if (list != null && list.size() != 0)
			{
				int size = list.size();
				for (int i = 0; i < size; i++)
				{
					Field[] fields = list.get(i).getClass().getDeclaredFields();
					ContentValues values = new ContentValues();
					for (int j = 0; j < fields.length; j++)
					{
						Field f = fields[j];
						f.setAccessible(true);
						String key = f.getName();
						String value = f.get(list.get(i)) + "";
						values.put(key, value);
					}
					status = database.insert(tabName, null, values);
					
				}
			}
		}
		catch (SecurityException e)
		{

			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{

			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{

			e.printStackTrace();
		}

		return status;
	}

	/**
	 * @param <T>
	 * @Title: insertByObject
	 * @Description: 向数据库中插入一个javaBean
	 * @param
	 * @return 插入失败返回-1
	 * @author 陈红建
	 * @throws
	 */
	public <T> long insertObject(T obj, String tabName)
	{

		long status = -1;
		try
		{
			if (obj != null)
			{
				if (TextUtils.isEmpty(tabName))
				{
					// 如果tabName为空,将创建数据库时的表作为查询表
					tabName = this.tabName;
				}
				ContentValues values = new ContentValues();
				// 遍历对象中所有的字段
				Class<? extends Object> clasz = obj.getClass();
				Field[] fields = clasz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++)
				{
					Field f = fields[i];
					f.setAccessible(true);
					String key = f.getName();
					String value = f.get(obj) + "";
					values.put(key, value);
				}
				status = database.insert(tabName, null, values);
				
			}
		}
		catch (SecurityException e)
		{

			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{

			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{

			e.printStackTrace();
		}
		return status;
	}

	/**
	 * @Title: findItemsByWhereAndWhereValue
	 * @Description: 通过一个对象的字节码和where条件得到一个对象集合 ,如果查询条件其中一个值为空,查询出所有的数据
	 * @param whereName
	 *            如果whereValue为空 将whereName 单独作为查询条件
	 * @param whereValue
	 *            指定要查询的表名,如果为空,将创建数据库时的表作为查询表,
	 * @param clasz
	 *            一个对象的字节码
	 * @param clasz
	 *            排序条件
	 * @return void
	 * @author 陈红建
	 * @throws
	 */
	public <T> List<T> findItemsByWhereAndWhereValue(String whereName,
			String whereValue, Class<T> clasz, String tabName,
			String orderCommand)
	{
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try
		{
			if (TextUtils.isEmpty(tabName))
			{
				// 如果tabName为空,将创建数据库时的表作为查询表
				tabName = this.tabName;
			}
			// 查询出是所有值
			// 初始化列名
			Field[] fields = clasz.getDeclaredFields();
			String[] columns = new String[fields.length];
			for (int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
				columns[i] = fields[i].getName();
			}

			// 构造查询条件
			String selection = whereName + "=?";
			// 构造查询参数
			String[] args = new String[]
			{ whereValue };
			if (TextUtils.isEmpty(whereName) && TextUtils.isEmpty(whereValue))
			{
				// 如果没有查询条件,查询所有的数据
				selection = null;
				args = null;
				cursor = database.query(tabName, columns, selection, args, null, null, orderCommand);
			}
			else if (!TextUtils.isEmpty(whereName)
					&& TextUtils.isEmpty(whereValue))
			{
				// 如果条件的值为空,可以指定一个复杂的条件
				// 需要构造数据库语句

				String sql = sel + tabName + where + whereName;
				if (!TextUtils.isEmpty(orderCommand))
				{
					sql = sql + " " + orderCommand;
				}
				cursor = database.rawQuery(sql, null);
			}
			else
			{
				cursor = database.query(tabName, columns, selection, args,
						null, null, orderCommand);
			}
			while (cursor.moveToNext())
			{
				T obj = (T) clasz.newInstance();// 初始化要创建的对象
				for (int x = 0; x < columns.length; x++)
				{
					// 遍历列名
					String c = columns[x];// 拿到这个列名
//					String value = cursor.getString(cursor.getColumnIndex(c));// 获得指定列名上的数据
					Object objValue = null;
					// 得到值之后,将值设置到对象中
					for (int i = 0; i < fields.length; i++)
					{
						fields[i].setAccessible(true);
						// 遍历字段名
						String fname = fields[i].getName();
						// 得到字段类型
						
						if (fname.equals(c))
						{
							// 如果字段名与列名相同
							
							String type = fields[i].getType().getSimpleName();
							if (type.equals("String"))
							{
								objValue = cursor.getString(cursor.getColumnIndex(c));// 获得指定列名上的数据
							}
							else if (type.equals("Long") || type.equals("long"))
							{

								objValue = cursor.getLong(cursor.getColumnIndex(c));// 获得指定列名上的数据
							}
							else if (type.equals("Integer") || type.equals("int"))
							{
								objValue = cursor.getInt(cursor.getColumnIndex(c));// 获得指定列名上的数据

							}
							else if (type.equals("boolean"))
							{
								objValue = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(c)));// 获得指定列名上的数据
							}
							else if (type.equals("Float") || type.equals("float"))
							{
								objValue = cursor.getFloat(cursor.getColumnIndex(c));// 获得指定列名上的数据
								
							}
							else if (type.equals("Double") || type.equals("double"))
							{
								objValue = cursor.getDouble(cursor.getColumnIndex(c));// 获得指定列名上的数据
							}

							else if (type.equals("Short") || type.equals("short"))
							{
								objValue = cursor.getShort(cursor.getColumnIndex(c));// 获得指定列名上的数据

							}
							fields[i].set(obj, objValue);
						}
					}
				}
				// 得到一个对象之后
				list.add(obj);
			}
		}
		catch (SecurityException e)
		{

			e.printStackTrace();
		}
		catch (InstantiationException e)
		{

			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{

			e.printStackTrace();
		}finally {
			cursor.close();
			
		}
		return list;
	}
	/** 
	 * @Title: deleteItem
	 * @Description: 删除一条数据,如果条件指定为空,删除所有数据 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	public void deleteItem(String tabName , String whereClause ,String[] whereArgs)
	{
		database.delete(tabName, whereClause, whereArgs);
	}
}
