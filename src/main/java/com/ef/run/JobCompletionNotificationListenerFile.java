package com.ef.run;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.ef.entity.IpComment;

@Component
public class JobCompletionNotificationListenerFile extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListenerFile.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			List<IpComment> results = jdbcTemplate.query("SELECT ip FROM ip_comment", new RowMapper<IpComment>() {
				public IpComment mapRow(ResultSet rs, int row) throws SQLException {
					return new IpComment(rs.getString(1));
				}
			});

			if (results.isEmpty()) {
				log.info("***" + "No IPs were found" + "*****");
			} else {
				for (IpComment ipComment : results) {
					log.info("*** IP: " + ipComment.getIp() + "*****");

				}
			}

			log.info("!!! JOB FINISHED! ");

			System.exit(0);
		}
	}
}
