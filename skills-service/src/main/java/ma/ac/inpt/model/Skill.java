package ma.ac.inpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skill {
    @Id
    private String id;
    private String name;
    private Integer usedCount;
    private Boolean isActivated;

    public Skill() {

    }
    public Skill(String id, String name, Integer usedCount, Boolean isActivated) {
        this.id = id;
        this.name = name;
        this.usedCount = usedCount;
        this.isActivated = isActivated;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }
}
