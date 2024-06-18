package com.github.ih0rd.utils;

public interface Constants {
    String PYTHON = "python";

    String VENV_PREFIX = "/vfs/venv";
    String HOME_PREFIX = "/vfs/home";
    String PROJ_PREFIX = "/vfs/proj";

    String USER_DIR = System.getProperty("user.dir");
    String RESOURCES_PREFIX = USER_DIR + "/src/main/resources";
    String PROJ_RESOURCES_PATH = RESOURCES_PREFIX + PROJ_PREFIX;

}
