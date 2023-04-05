package com.it.dbswap.hbase;

import com.alibaba.fastjson.JSONObject;
import com.it.dbswap.util.SerializerUtil;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HBaseProcessor {
	protected static final Logger LOGGER = LoggerFactory.getLogger(HBaseProcessor.class);
	private static Connection conn = null;
	static {
		conn = HBaseConnection.getConn();
	}

	public static void saveToHbase(List<Put> puts, String tableName, String familyName) throws Exception {
		Table table = null;
		try {
			table = save(puts, tableName);
		} catch (Exception e) {

			LOGGER.error("Hbase exception {}", e);
			try {
				Thread.sleep(10000);
				conn = HBaseConnection.reConn();
				table = save(puts, tableName);
			} catch (Exception e1) {
				LOGGER.error("Hbase exception {}", e);
				throw e1;
			}

		} finally {
			if (table != null) {
				table.close();
			}
		}

	}
	@SuppressWarnings("resource")
	public static void saveToHbase(Put put, String tableName, String familyName) throws Exception {
		Table table = null;
		try {
			table = save(put, tableName);
		} catch (Exception e) {
			e.printStackTrace();
			conn = HBaseConnection.reConn();
			LOGGER.error("Hbase exception {}", e);
			try {
				table = save(put, tableName);
			} catch (Exception e1) {
				LOGGER.error("Hbase exception {}", e);
				throw e1;
			}
			
		} finally {
			if (table != null) {
				table.close();
			}
		}

	}

	private static Table save(List<Put> puts, String tableName) throws IOException, InterruptedException {
		Table table;
		table = conn.getTable(TableName.valueOf(tableName));
		if (!puts.isEmpty()) {
			table.put(puts);
		}
		return table;
	}

	private static Table save(Put put, String tableName) throws IOException, InterruptedException {
		Table table;
		table = conn.getTable(TableName.valueOf(tableName));
		table.put(put);
		return table;
	}

	public static <T> T get(String rowId, String tableName, Class<T> T) throws IOException {
		Table table = null;
		try {
			return realGet(table, rowId, tableName, T);
		} catch (Exception e) {
			conn = HBaseConnection.reConn();
			LOGGER.error("Hbase get Exception {}", e);
			return realGet(table, rowId, tableName, T);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	@SuppressWarnings("resource")
	public static void delete(String rowId, String tableName) throws IOException {
		Table table = null;
		try {
			table = conn.getTable(TableName.valueOf(tableName));
			Delete delete = new Delete(Bytes.toBytes(rowId));
			table.delete(delete);
		} catch (Exception e) {
			conn = HBaseConnection.reConn();
			table = conn.getTable(TableName.valueOf(tableName));
			Delete delete = new Delete(Bytes.toBytes(rowId));
			table.delete(delete);
			LOGGER.error("Hbase get Exception {}", e);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}


	
	
	

	private static <T> T realGet(Table table, String rowId, String tableName, Class<T> T) throws IOException {
		table = conn.getTable(TableName.valueOf(tableName));
		Result result = table.get(new Get(Bytes.toBytes(rowId)));
		Cell[] keyValues = result.rawCells();
		if (keyValues.length == 1) {
			return JSONObject.parseObject(Bytes.toString(CellUtil.cloneValue(keyValues[0])), T);
		}
		return null;
	}

	public  static Long getTimestamp(String rowId, String tableName) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Result result = table.get(new Get(Bytes.toBytes(rowId)));
		Cell[] keyValues = result.rawCells();
		if (keyValues.length >0) {
			return keyValues[0].getTimestamp();
		}
		return null;
	}

	public static <T> T get(String rowId, String tableName,String cf ,String column, Class<T> T) throws IOException {
		Table table = null;
		try {
			return realGet(table, rowId, tableName,cf,column,T);
		} catch (Exception e) {
			conn = HBaseConnection.reConn();
			LOGGER.error("Hbase get Exception {}", e);
			return realGet(table, rowId, tableName,cf,column,T);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}
	private static <T> T realGet(Table table, String rowId, String tableName,String cf ,String column, Class<T> T) throws IOException {
		table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(Bytes.toBytes(rowId));
		get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(column));
		Result result = table.get(get);
		Cell[] keyValues = result.rawCells();
		if (keyValues.length == 1) {
			return JSONObject.parseObject(Bytes.toString(CellUtil.cloneValue(keyValues[0])), T);
		}
		return null;
	}



	
	public static <T> List<T> batchGet(List<Get> gets, String tableName, Class<T> T) throws IOException {
		List<T> resp = new ArrayList<>();
		Table table = conn.getTable(TableName.valueOf(tableName));
		try {
			realBatchGet(gets, T, resp, table,false);
		} catch (Exception e) {
			e.printStackTrace();
			conn = HBaseConnection.reConn();
			LOGGER.error("batchGet error {}",e);
			boolean needSkip = false;
			realBatchGet(gets, T, resp, table,needSkip);
		}finally {
			if (table != null) {
				table.close();
			}
		}
		return resp;
	}

	private static <T> void realBatchGet(List<Get> gets, Class<T> T, List<T> resp, Table table,boolean needSkip) throws IOException {
		Result[] results = table.get(gets);
		for(Result r : results) {
			Cell[] keyValues = r.rawCells();
			if(needSkip) {
				if(keyValues==null || keyValues.length==0)continue;
			}
			
			T t  = SerializerUtil.deserialize(CellUtil.cloneValue(keyValues[0]), T);
			resp.add(t);
		}
	}


	public static <T> List<T> getColumns(String rowkey, Class<T> T, String tableName) throws IOException {
		List<T> resp = new ArrayList<>();
		Table table = null;
		try {
			table = conn.getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(rowkey));
			Result result = table.get(get);
			Cell[] keyValues = result.rawCells();
			if(keyValues!=null && keyValues.length>0)
			for(Cell c : keyValues){
				T t  = SerializerUtil.deserialize(CellUtil.cloneValue(c), T);
				resp.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (table != null) {
				table.close();
			}
		}

      return  resp;
	}

	


}
