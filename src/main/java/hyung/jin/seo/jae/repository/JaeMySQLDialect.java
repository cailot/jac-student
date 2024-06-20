package hyung.jin.seo.jae.repository;

import org.hibernate.dialect.MySQL55Dialect;

public class JaeMySQLDialect extends MySQL55Dialect{

    @Override
    public String getTableTypeString(){
        return " ENGINE=InnoDB";
    }
    
}
