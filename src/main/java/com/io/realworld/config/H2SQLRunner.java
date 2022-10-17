package com.io.realworld.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;


/*
@Component
@Log4j2
public class H2SQLRunner implements ApplicationRunner {


    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public H2SQLRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {

            log.info("{}",dataSource.getClass());
            log.info("{}",connection.getMetaData().getURL());
            log.info("{}",connection.getMetaData().getUserName());

            //Statement statement = connection.createStatement();
            //String sql = "CREATE TABLE t_product(product_no INTEGER NOT NULL, product_name VARCHAR(255), PRIMARY KEY (product_no))";
            //statement.executeUpdate(sql);
        }
        //jdbcTemplate.execute("INSERT INTO t_product VALUES (1, 'Big shirt')");
    }
}
 */