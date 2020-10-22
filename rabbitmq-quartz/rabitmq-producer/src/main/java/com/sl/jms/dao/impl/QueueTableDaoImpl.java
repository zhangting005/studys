package com.sl.jms.dao.impl;

 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sl.entity.QueueTable;
import com.sl.jms.dao.QueueTableDao;

@Repository
public class QueueTableDaoImpl  implements QueueTableDao{

	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int save(final QueueTable queueTable) {
		String sql ="insert into queue_table_production(request,type,status,entry_date)"
				+ "values(?,?,?,?)";
		 return jdbcTemplate.update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, queueTable.getRequest());
				ps.setString(2, queueTable.getType());
				ps.setString(3, queueTable.getStatus());
				ps.setTimestamp(4, new Timestamp(queueTable.getEntryDate().getTime()));
			}
		 });
	}

	@Override
	public List<QueueTable> queryAll() {
		String sql ="select * from queue_table_production";
		 return  getResult(sql);
	}

	@Override
	public List<QueueTable> queryStatusNotSend() {
		String sql="select * from queue_table_production where status='NOT_SEND'";
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
	public void clearMessage() {
		String sql ="update queue_table_production set status='NOT_SEND'";
		jdbcTemplate.update(sql);
	}
	
}
