package org.mohan.app;

import org.springframework.stereotype.Repository;

@Repository("dataBase")
public class DataBase {
	public String data() {
		return "From DataBase";
	}
}
