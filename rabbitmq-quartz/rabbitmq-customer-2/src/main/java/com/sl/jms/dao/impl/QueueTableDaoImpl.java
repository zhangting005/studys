package com.sl.jms.dao.impl;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.sl.entity.QueueTable;
import com.sl.jms.dao.QueueTableDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class QueueTableDaoImpl implements QueueTableDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(final QueueTable queueTable) {
        String sql = "insert into queue_table_customer_2(request,type,status,entry_date,comment)"
                     + "values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, queueTable.getRequest());
                ps.setString(2, queueTable.getType());
                ps.setString(3, queueTable.getStatus());
                ps.setTimestamp(4, new Timestamp(queueTable.getEntryDate().getTime()));
                ps.setString(5, queueTable.getComment());
            }
        });
    }

    @Override
    public List<QueueTable> queryAll() {
        String sql = "select * from queue_table_customer_2";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    private RowMapper<QueueTable> getRowMapper() {
        return new RowMapper<QueueTable>() {
            @Override
            public QueueTable mapRow(ResultSet rs, int rowNum) throws SQLException {
                QueueTable queueTable = new QueueTable();
                queueTable.setId(rs.getInt("id"));
                queueTable.setRequest(rs.getString("request"));
                queueTable.setType(rs.getString("type"));
                queueTable.setStatus(rs.getString("status"));
                queueTable.setEntryDate(rs.getDate("entry_date"));
                queueTable.setComment(rs.getString("comment"));
                return queueTable;
            }
        };
    }

    @Override
    public Integer updateStatus(final QueueTable queueTable) {
        String sql = "update queue_table_customer_2 set status=? where id =?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, queueTable.getStatus());
                ps.setInt(2, queueTable.getId());
            }
        });
    }

    @Override
    public void clearMessage() {
        String sql = "delete from queue_table_customer_2";
        jdbcTemplate.update(sql);
    }

    @Override
    public QueueTable quryByEntryDate(Date entryDate) {
        String sql = "select * from queue_table_customer_2 where  timestampdiff(microsecond,'" + entryDate + "',entry_date)  = 0";
        List<QueueTable> queueTableResult = jdbcTemplate.query(sql, getRowMapper());
        if (queueTableResult != null && !queueTableResult.isEmpty()) {
            return queueTableResult.get(queueTableResult.size() - 1);
        }
        return null;
    }

    @Override
    public void updateComment(final QueueTable queue) {
        String sql = "update queue_table_customer_2 set comment=? where id =?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, queue.getComment());
                ps.setInt(2, queue.getId());
            }
        });
    }
}
