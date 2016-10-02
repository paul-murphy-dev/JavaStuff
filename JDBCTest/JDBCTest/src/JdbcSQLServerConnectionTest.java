
 
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.ibm.domain.pm.TestItem;
import com.ibm.domain.pm.User;
import com.pm.data.nfa.DataLayer;
import com.pm.data.nfa.DatabaseType;
 
/**
 * This program demonstrates how to establish database connection to Microsoft
 * SQL Server.
 * @author www.codejava.net
 *
 */
public class JdbcSQLServerConnectionTest {
	
	public static void main(String[] args) {		
	
		try {
        	
        	DataLayer.Init(DatabaseType.SQLServer, "PMURPHYW530", "SQL2012", "SpringTest", "pmWeb", "varicent123");      	
			
            
            List<TestItem> stuff = DataLayer.GetObjectList("select * from TestTable;", TestItem.class);
            
            List<User> users = DataLayer.GetObjectList("select * from Users;", User.class);
            
            Optional<TestItem> firstone = stuff.stream().findFirst();
 
        }catch(Exception exAll){        	
        	exAll.printStackTrace();
        	
        }  finally {
           
        }
    }
}