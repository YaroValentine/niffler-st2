package niffler.db.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllureSqlLogger extends StdoutLogger {

    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
    private final String templatePath = "sql-query.ftl";

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);

        if (isNotEmpty(sql)) {
            SqlAttachment sqlAttachment = new SqlAttachment(
                    "SQL statement and query",
                    SqlFormatter.of(Dialect.StandardSql).format(prepared),
                    SqlFormatter.of(Dialect.StandardSql).format(sql));
            attachmentProcessor.addAttachment(sqlAttachment, new FreemarkerAttachmentRenderer(templatePath));
        }
    }

    @Override
    public void logException(Exception e) {
        super.logException(e);
        Allure.addAttachment("Exception stacktrace", e.getMessage());
    }

    @Override
    public void logText(String sql) {
        super.logText(sql);
    }

}
