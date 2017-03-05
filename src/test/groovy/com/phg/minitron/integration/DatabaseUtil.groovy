package com.phg.minitron.integration

import com.phg.minitron.dao.BaseDao
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

import java.sql.Connection

/**
 * Created by milesporter on 3/5/17.
 */
class DatabaseUtil {

    def createDatabase() {
        BaseDao bd = new BaseDao()
        Connection conn = bd.getConnection()
        def st = conn.createStatement()

        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("MinitronDBCreate.sql");


        String statements = resource.getInputStream().getText()

        statements.split(";").each { statement ->
            st.execute(statement)
        }
    }
}
