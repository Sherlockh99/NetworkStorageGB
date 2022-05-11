package org.sherlock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Getter
@Setter
public class FileInfoServer {

    @Getter
    @AllArgsConstructor
    public enum FileType {
        FILE("F"), DIRECTORY ("D");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum DirLevel {
        L0("0"), L1("1"), L2("2"), L3("3"), L4("4"), L5("..");
        private final String name;
    }

    private String filename;
    private FileType type;
    private DirLevel level;
    private long size;
    private LocalDateTime lastModified;

    public FileInfoServer(Path path) {
        try {
            this.filename = path.getFileName().toString();
            this.size = Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (this.type == FileType.DIRECTORY) {
                this.size = -1L;
            }
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(3));

            switch (getDirLevel(path)) {
                case 3:
                    this.level = DirLevel.L0;
                    break;
                case 4:
                    this.level = DirLevel.L0;
                    break;
                case 5 :
                    this.level = DirLevel.L0;
                    break;
                case 6:
                    this.level = DirLevel.L1;
                    break;
                case 7:
                    this.level = DirLevel.L2;
                    break;
                case 8:
                    this.level = DirLevel.L3;
                    break;
                case 9:
                    this.level = DirLevel.L4;
                    break;
                default:
                    this.level = DirLevel.L5;
                    break;
            }

        }
        catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");
        }
    }
    private int getDirLevel(Path path) {
        int count = 0;
        while(path != null && Files.exists(path)) {
            count++;
            path = path.getParent();
            //Files.isDirectory(path);
        }
        return count;
    }
}
