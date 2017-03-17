package dto;

public class PhotoDTO extends DTO{
    private Long id;
    private String name;
    private String pathToFile;
    private byte[] content;

    public PhotoDTO() {
    }

    public PhotoDTO(Long id, String name, String pathToFile) {
        this.id = id;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}