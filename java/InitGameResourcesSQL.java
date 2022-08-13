import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class InitGameResourcesSQL {

    public static void main(String[] args) throws Exception {
        File baseDir = new File(InitGameResourcesSQL.class.getResource("/").toURI());
        StringBuilder sql = new StringBuilder();
        sql.append("TRUNCATE table game_resource;").append("\n");
        initSQL(baseDir, "/", sql);

        URI uri = InitGameResourcesSQL.class.getResource("/").toURI();
        File file = new File(uri.getPath() + "/init_game_resource.sql");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(sql.toString().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();
    }

    public static void initSQL(File dir, String parentPath, StringBuilder sql) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    initSQL(file, parentPath + file.getName() + "/", sql);
                } else {
                    String name = file.getName();
                    if (name.endsWith(".class")) {
                        continue;
                    }
                    sql.append(String.format("INSERT INTO `game_resource`(`path`, `file_size`, `is_valid`, `create_time`) " +
                            "VALUES ('%s', %s, 1, now());", parentPath + name, file.length())).append("\n");
                }
            }
        }
    }

}
