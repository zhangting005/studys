package com.sl.quartz.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.sl.entity.QueueTable;
import com.sl.quartz.dao.QueueTableDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class QueueTableDaoImpl implements QueueTableDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<QueueTable> queryStatusNotSend() {
        String sql = "select * from queue_table_production where status='NOT_SEND'";
        return getResult(sql);
    }

    private List<QueueTable> getResult(String sql) {
        return jdbcTemplate.query(sql, new RowMapper<QueueTable>() {

            @Override
            public QueueTable mapRow(ResultSet rs, int rowNum) throws SQLException {
                QueueTable queueTable = new QueueTable();
                queueTable.setId(rs.getInt("id"));
                queueTable.setRequest(rs.getString("request"));
                queueTable.setType(rs.getString("type"));
                queueTable.setStatus(rs.getString("status"));
                queueTable.setEntryDate(rs.getTimestamp("entry_date"));
                return queueTable;
            }

        });
    }


    @Override
    public Integer updateStatus(final QueueTable queueTable) {
        String sql = "update queue_table_production set status=? where id =?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, queueTable.getStatus());
                ps.setInt(2, queueTable.getId());
            }
        });
    }

}
