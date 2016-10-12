package job_streamer.job_streamer_custom_console.model;

import java.util.Date;

import job_streamer.job_streamer_custom_console.edn.annotation.EdnKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.bpsm.edn.Keyword;

@Data
@NoArgsConstructor
public class Job {

    @EdnKey(":job/name")
    private String name;

    @EdnKey(":job/latest-execution :job-execution/exit-status")
    private String lastExitStatus;
    
    @EdnKey(":job/latest-execution :job-execution/batch-status :db/ident")
    private Keyword lastBatchStatus;

    @EdnKey(":job/latest-execution :job-execution/end-time")
    private Date lastExecutionEndTime;
    
    @EdnKey(":job/latest-execution :db/id")
    private Long lastExecutionId;

    // TODO: 他にも必要なプロパティがあれば追加
}
