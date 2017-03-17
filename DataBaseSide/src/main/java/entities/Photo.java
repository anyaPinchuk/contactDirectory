package entities;

public class Photo {
    private Long id;
    private String name;
    private String pathToFile;

    public Photo() {
    }

    public Photo(String name, String pathToFile) {
        this.name = name;
        this.pathToFile = pathToFile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
