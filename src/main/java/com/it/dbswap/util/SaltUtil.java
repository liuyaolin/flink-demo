package com.it.dbswap.util;
import com.it.dbswap.constant.CommonConstant;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.apache.hadoop.util.hash.MurmurHash;

import java.io.IOException;
import java.text.ParseException;

/**
 * @description: rowkey分区前缀生成工具
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class SaltUtil {
	
	/**
	 * 根据输入值，计算前缀
	 * @param value
	 * @param partition 预分区个数
	 * @return
	 */
	public static String generateSalt(String value, int partition) {
		return generateSalt(value, partition, 1);
	}

	/**
	 * 根据输入值，计算前缀
	 * @param value
	 * @param partition 预分区个数
	 * @param topSeed	高位预留位
	 * @return 例如partition=10，topSeed=1000，加盐后前缀样例：01001
	 */
	public static String generateSalt(String value, int partition, int topSeed) {
		//没有做预分区情况，直接做md5打散
		if(partition <= 0) {
			return MD5Hash.getMD5AsHex(value.getBytes()).substring(0, 8);
		}

		StringBuffer salt = new StringBuffer("");
		//哈希函数获取hash值
		long hashCode = MurmurHash.getInstance().hash(value.getBytes());

		if(hashCode < 0) {
			hashCode = 0 - hashCode;
		}

		/**
		 * topSeed：高位预留位s
		 * 预留位主要是防止表非常大时，会再次分裂
		 */
		long modPartition = hashCode % partition;
		long modTopSeed = hashCode % topSeed;

		/**
		 * 计算前缀，
		 * 比如mod小于2位数，partition为100或10以上的两位数，
		 * 则结果会是mod前面加个0
		 */
		String partitionSalt = generateSalt(modPartition, partition);
		String topSeedSalt = "";
		// topSeed只能是10、100、1000、1000
		if(topSeed >= 10 && topSeed%10 == 0) {
			topSeedSalt = generateSalt(modTopSeed, topSeed);
		}
		salt.append(partitionSalt).append(topSeedSalt);
		return salt.toString();
	}

	/**
	 * 根据输入值，计算前缀，
	 * 比如mod小于2位数，partition为100或10以上的两位数，
	 * 则结果会是mod前面加个0
	 * @param mod
	 * @param partition 预分区个数
	 * @return 例如partition=10，topSeed=1000，加盐后前缀样例：01001
	 */
	public static String generateSalt(long mod, int partition) {
		StringBuffer salt = new StringBuffer("");

		/**
		 * 比如传入的partition=1500，解析得到1000为begin
		 */
		int length = String.valueOf(partition).length();
		StringBuffer begin = new StringBuffer("1");
		for(int i=0; i<length-1; i++) {
			begin.append("0");
		}

		/**
		 * 拼装得到format格式，例如：%02d
		 * 注意传入1000跟传入1500的区别
		 * 传入1000，得到的是000到999的区间
		 * 传入1500，得到的是0000到1499的区间
		 */
		String format = "";
		if(partition > Integer.parseInt(begin.toString())) {
			format = "%0" + length + "d";
		} else {
			format = "%0" + (length-1) + "d";
		}


		salt.append(String.format(format, mod));

		return salt.toString();
	}


	/**
	 * 根据输入值，计算前缀
	 * @param value
	 * @param partition 预分区个数
	 * @return
	 */
	public static String generateSaltOrigin(String value, int partition) {
		StringBuffer salt = new StringBuffer("");
		long hashCode = MurmurHash.getInstance().hash(value.getBytes());

		if(hashCode < 0) {
			hashCode = 0 - hashCode;
		}

		long mod = hashCode % partition;
		if(partition <= 10) {
			salt.append(mod);
		} else if(partition <= 100) {
			salt.append(String.format("%02d", mod));
		} else if(partition <= 1000) {
			salt.append(String.format("%03d", mod));
		} else if(partition <= 10000) {
			salt.append(String.format("%04d", mod));
		}

		return salt.toString();
	}

	public static void main(String[] args) throws ParseException, IOException {
		String salt = SaltUtil.generateSalt(1, 100);
		System.out.println(salt);
		/*
		String salt = SaltUtil.generateSalt("222", 200, 10000);
		System.out.println(salt);

		salt = generateSalt(44636, 200*10000);
		System.out.println(salt);

		long hash = 594044636L;
		System.out.println(hash%2000000);
		*/
		String hash = SaltUtil.generateSalt("LJVA8GDD3MT126726!!150",
				CommonConstant.VIN_HBASE_PARTITION,CommonConstant.VIN_HBASE_SALT_SEED);


		System.out.println("======================================================"+hash);
//
//		int partitionNum = 10;
//		String salt = SaltUtil.generateSaltOrigin("201433968413", partitionNum);
//		System.out.println(salt);
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);
//
//		partitionNum = 100;
//		salt = SaltUtil.generateSaltOrigin("201433968413", partitionNum);
//		System.out.println(salt);
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);
//
//
//		partitionNum = 1000;
//		salt = SaltUtil.generateSaltOrigin("201433968413", partitionNum);
//		System.out.println(salt);
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);
//
//		partitionNum = 10000;
//		salt = SaltUtil.generateSaltOrigin("201433968413", partitionNum);
//		System.out.println(salt);
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);
//
//		partitionNum = 1431;
//		salt = SaltUtil.generateSalt("201433968413", partitionNum);
//		System.out.println(salt);
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);
//
//		System.out.println("---------------------------------------------------------");
//
//		partitionNum = 100;
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 10);
//		System.out.println(salt);
//
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 100);
//		System.out.println(salt);
//
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 11);
//		System.out.println(salt);
//
//		partitionNum = 1100;
//		salt = SaltUtil.generateSalt("201433968413", partitionNum, 1);
//		System.out.println(salt);

		/*
		System.out.println("===========================");

		for(int i=0; i<35; i++) {
			System.out.println(generateSalt(i, 35));
		}
		*/
/*
		String waybillNo = "SF1026853029688";

		String salt = SaltUtil.generateSalt(waybillNo, 300, 10000);

		String rowkey = new StringBuffer()
				.append(salt).append(HBaseService.HBASE_ROWKEY_DEFAULT_SEPARATOR)
				.append(waybillNo).toString();

		System.out.println(rowkey);*/

	}
	
}