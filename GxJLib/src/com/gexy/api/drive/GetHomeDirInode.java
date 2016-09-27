package com.gexy.api.drive;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gexy.api.ApiObject;
import com.gexy.api.ApiResponces;
import com.gexy.api.exception.ApiException;
import com.gexy.auth.Organization;
import com.gexy.auth.excepion.AuthLoginFailedException;
import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;
import com.gexy.security.BCrypt;

public class GetHomeDirInode extends ApiObject {

	public GetHomeDirInode(){
		super("com.gexy.api.drive.GetHomeDirInode",new ArrayList<String>());

	}
	public void run()
	{
		try {
			Organization org = auth.getOrganization();
			ResultSet data =  org.getDatabase().query("SELECT drv_store.inode as inode "
					+ "FROM drv_store "
					+ "LEFT JOIN drv_tree drv_tree ON drv_tree.inode = drv_store.inode "
					+ "WHERE drv_tree.parent='1' "
					+ "AND drv_store.name='"+org.getUsers().get(0).getUsername().split("@")[0]+"' "
					+ "AND drv_store.type='1' "
					+ "AND drv_store.deleted='0' "
					);
			


			//nessun dato trovato
			if(!data.next()){
				//non esiste nessun utente con questo username
				System.out.println("Inode of home directory of user, for gexy drive, not found in database");
			}
			
			String inode  = data.getString("inode");
			System.out.println(inode);
			setResponce(inode);
			
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException
				| ConfigDBNameException | MysqlTryOpenWithoutData e) {
			System.out.println("Errore mysql");
			e.printStackTrace();
		}
		
		System.out.println(apiName +" argomenti: "+apiArgs.toString());
		setResponce(ApiResponces.OK);
	}



}
