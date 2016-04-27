package com.mapchat.managers;

public class Constants {

    //-----------------------------------------------------------
    // Change /Users/Balci/FileStorageLocation/ below to /home/cs4984/Balci/FileStorageLocation/
    // for deployment to the server by replacing Balci with your last name.
    //-----------------------------------------------------------
    public static final String ROOT_DIRECTORY = "C:/Users/Nathan/FileStorageLocation/";

    public static final String TEMP_FILE = "tmp_file";
    
    public static final Integer MAX_MESSAGES = 1000;

    public static final Integer THUMBNAIL_SZ = 200;

    public static final Integer ICON_SZ = 50;

    public static final Integer MAX_CAPTION_SIZE = 140;
    
    public static final String FILE_REGEX = "/(\\.|\\/)(gif|jpe?g|png|docx?|pptx?|xlsx?|pdf|txt|mp3|mp4|wav|rar|zip|tar|gz)$/";
    
    public static final String MAX_FILE_SIZE_BYTES = "25000000";
    
    public static final String[] GLOBAL_GROUPS = {
        "#Music",
        "#Technology",
        "#Religion",
        "#Entertainment"
    };

    public static final String[] QUESTIONS = {"Security Question (Please Select One)",
        "In what city were you born?",
        "What elementary school did you attend?",
        "What is the last name of your most favorite teacher?",
        "What is your father's middle name?",
        "What is your most favorite pet's name?"
    };

}
