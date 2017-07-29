package dharmista.batman.com.voicetodo.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Dharmista on 7/20/2017.
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class VoiceData {
    private String id;
    private String fileName;
    private String date;
    private String month;
    private String year;
    private String hours;
    private String minutes;
    private String serviceId;
    private Integer isTaskCompleted;
    private String name;
    private String message;
}