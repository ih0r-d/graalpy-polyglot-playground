import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static com.github.ih0rd.utils.Constants.PROJ_RESOURCES_PATH;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PythonDirectoryStructureTest {

    @Test
    public void allFilesInSrcPythonShouldHavePyExtension() {
        File folder = new File(PROJ_RESOURCES_PATH);
        File[] files = folder.listFiles();
        assertTrue(files != null && files.length > 0, "Directory %s not found or is empty.".formatted(PROJ_RESOURCES_PATH));

        boolean allFilesHavePyExtension = Arrays.stream(files)
                .filter(File::isFile)
                .allMatch(file -> file.getName().endsWith(".py"));

        assertTrue(allFilesHavePyExtension, "Not all files in %s' have .py extension.".formatted(PROJ_RESOURCES_PATH));
    }
}