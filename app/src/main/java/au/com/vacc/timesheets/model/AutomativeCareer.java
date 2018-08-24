package au.com.vacc.timesheets.model;

import org.codehaus.jackson.JsonNode;

import java.io.Serializable;

public class AutomativeCareer implements Serializable {

	private static final long serialVersionUID = -7007014696099017450L;
	private String name;
	private String fileName;
	private String background;

	public AutomativeCareer(JsonNode jsonNode) {
		this.name = jsonNode.path("name").asText();
		this.fileName = jsonNode.path("fileName").asText();
		this.background = jsonNode.path("background").asText();
	}

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getBackground() {
        return background;
    }
}
