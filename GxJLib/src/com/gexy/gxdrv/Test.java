package com.gexy.gxdrv;

import java.sql.SQLException;
import java.util.ArrayList;

import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.gxdrv.exception.GxDrvFileNotFoundException;
import com.gexy.mysql.Mysql;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;

public class Test {

	public static void main(String[] args) {
		try {
			Mysql db = new Mysql("10.70.0.1","gexy_275879","H4Rtgl67@gexy.it","8pWOpapGaAF8pXqsnAm2");
			db.open();
			GxDrvFile file = new GxDrvFile(db,2);
			file.open();
			ArrayList<Integer> ls = file.read();
			for (int item : ls) {
				System.out.println("Inode: "+item);
			}
			
			

		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException
				| ConfigDBNameException | MysqlTryOpenWithoutData e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (GxDrvFileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
