package org.superbiz.moviefun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static final long SECONDS = 1000;

    private final JdbcTemplate jdbcTemplate;
    private final AlbumsUpdater albumsUpdater;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static boolean tableCreated = false;

    public AlbumsUpdateScheduler(DataSource dataSource, AlbumsUpdater albumsUpdater) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.albumsUpdater = albumsUpdater;
    }


    @Scheduled(initialDelay = 5 * SECONDS, fixedRate = 15 * SECONDS)
    public void run() {
        try {
            logger.debug("Checking for albums task to start");

            if (startAlbumSchedulerTask()) {
                logger.debug("Starting albums update");

                albumsUpdater.update();

                logger.debug("Finished albums update");
            } else {
                logger.debug("Nothing to start");
            }

        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
        }
    }

    private boolean startAlbumSchedulerTask() {

        if (!tableCreated ) {
            createTable();
            tableCreated = true;
        }
        int updatedRows = jdbcTemplate.update(
            "UPDATE album_scheduler_task" +
                " SET started_at = now()" +
                " WHERE started_at IS NULL" +
                " OR started_at < date_sub(now(), INTERVAL 2 MINUTE)"
        );

        return updatedRows > 0;
    }

    private void createTable() {

        logger.warn("invoking createTable()...");

        String tableName = jdbcTemplate.query("SELECT table_name FROM information_schema.tables where table_name='album_scheduler_task'", //new MyResultSetExtractor());
                new ResultSetExtractor<String>()
                {
                    public String extractData(ResultSet resultSet) throws SQLException,
                            DataAccessException {
                        if (resultSet != null && resultSet.next()) {
                            return resultSet.getString("table_name");
                        }
                        return null;
                    }
                });

        logger.warn("Table name returned...:" + tableName);

        if (tableName == null) {
            //createTable..
            logger.warn("Creating Tables...");
            jdbcTemplate.execute("CREATE TABLE album_scheduler_task (started_at TIMESTAMP NULL DEFAULT NULL)");
            jdbcTemplate.execute("INSERT INTO album_scheduler_task (started_at) VALUES (NULL)");
        }
    }
}
