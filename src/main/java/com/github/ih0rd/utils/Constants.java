package com.github.ih0rd.utils;

public interface Constants {

//    This constant holds the string "python", representing the Python programming language.
//    It's used to specify the language context within the GraalVM.
    String PYTHON = "python";

//    This constant defines the prefix for the virtual environment (venv) directory.
//    It's used to specify the path where the Python virtual environment is located.
    String VENV_PREFIX = "/vfs/venv";

//    This constant sets the prefix for the home directory within the virtual file system (vfs).
//    It helps in organizing user-specific data.
    String HOME_PREFIX = "/vfs/home";

//    This constant defines the prefix for the project directory within the virtual file system.
//    It is used to structure project-specific files and resources.
    String PROJ_PREFIX = "/vfs/proj";

//    This constant retrieves the current user directory where the Java application is running.
//    It is dynamically set using System.getProperty("user.dir").
    String USER_DIR = System.getProperty("user.dir");

//    This constant retrieves the current user directory where the Java application is running.
//    It is dynamically set using System.getProperty("user.dir").
    String RESOURCES_PREFIX = USER_DIR + "/src/main/resources";

//    This constant creates the full path to the project resources directory by
//    concatenating RESOURCES_PREFIX with PROJ_PREFIX.
    String PROJ_RESOURCES_PATH = RESOURCES_PREFIX + PROJ_PREFIX;
}
