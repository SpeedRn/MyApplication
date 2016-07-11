package com.stv.main;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {
    public static final int VERSION_DB = 1;
    public static final String PACKAGE = "com.stv.dvb.db";
    public static final String FILE_ROOT_PATH = "app/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION_DB, "com.stv.launcher.db");
//        addScreen(schema);
        addAppBadge(schema);
        addShortcut(schema);
        new DaoGenerator().generateAll(schema, FILE_ROOT_PATH);
    }


    private static void addScreen(Schema schema) {
        Entity note = schema.addEntity("ScreenInfo");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("tag");
        note.addStringProperty("name");
        note.addBooleanProperty("local");
        note.addBooleanProperty("sortable");
        note.addBooleanProperty("removable");
        note.addStringProperty("pluginUrl");
        note.addIntProperty("pluginId");
        note.addStringProperty("pluginPath");
        note.addStringProperty("pluginType");
        note.addStringProperty("pluginSize");
        note.addStringProperty("pluginState");
        note.addStringProperty("updateType");
        note.addStringProperty("versionName");

        // new
        note.addLongProperty("index");
        note.addStringProperty("versionCode");
        note.addStringProperty("pluginPkg");
        note.addStringProperty("pluginImg");
        note.addStringProperty("pluginDescribe");
        note.addStringProperty("pluginDependVersion");
        note.addStringProperty("pluginDependModel");
    }

    private static void addShortcut(Schema schema) {
        Entity note = schema.addEntity("ShortcutInfo");
        note.addIdProperty().primaryKey().autoincrement();
        note.addIntProperty("index");
        note.addIntProperty("type");
        note.addIntProperty("spanX");
        note.addIntProperty("spanY");
        note.addIntProperty("row");
        note.addIntProperty("column");
        note.addIntProperty("width");
        note.addIntProperty("height");
        note.addStringProperty("title");
        note.addStringProperty("description");
        note.addStringProperty("packageName");
        note.addStringProperty("className");
        note.addIntProperty("flags");
        note.addLongProperty("container");
        note.addStringProperty("containerName");
        note.addLongProperty("installTime");
        note.addStringProperty("componentNameStr");
        note.addStringProperty("iconUrl");
        note.addIntProperty("backgroundResID");
        note.addIntProperty("inFolderIndex");
        note.addStringProperty("reserve1");
        note.addStringProperty("reserve2");
        note.addStringProperty("reserve3");
    }

    private static void addAppBadge(Schema schema) {
        Entity note = schema.addEntity("AppBadge");
        note.addIdProperty();
        note.addStringProperty("packageName");
        note.addStringProperty("className");
        note.addStringProperty("msgType");
        note.addStringProperty("msgCount");
    }
}
