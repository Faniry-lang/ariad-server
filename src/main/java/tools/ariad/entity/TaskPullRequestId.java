package tools.ariad.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaskPullRequestId implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID taskId;
    private UUID pullRequestId;
}
