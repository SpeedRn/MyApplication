package com.stv.main;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

import java.io.File;
import java.io.IOException;

/**
 * This package should not modify, You can run this file as a single java
 * program ,this program will new some java file you can use . know more you can
 * visit http://greendao-orm.com/
 */
public class DvbDaoGenerator {
    public static final int VERSION_DB = 12;
    public static final String PACKAGE = "com.stv.dvb.db";
    public static final String FILE_ROOT_PATH = "app/src/main/java-gen";

    public static void main(String[] args) {
        Schema schema = new Schema(VERSION_DB, PACKAGE);
        addChannel(schema);
        addSettings(schema);
        addCollections(schema);
        addHistory(schema);
        addCategory(schema);
        addProgram(schema);
        addReserves(schema);
        addRecommend(schema);

        try {
            new DaoGenerator().generateAll(schema, FILE_ROOT_PATH);
        } catch (IOException e2) {
            File file = new File("src");
            file.mkdir();
            e2.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    private static void addCategory(Schema schema) {
        Entity entity = schema.addEntity("DvbCategoryTable");
        entity.addIdProperty();
        entity.addIntProperty("category_id").columnName("category_id").notNull();// 分类id
        entity.addStringProperty("category_name").columnName("category_name").notNull();// 分类名字
    }

    private static void addRecommend(Schema schema) {
        Entity entity = schema.addEntity("DvbRecommendTable");
        entity.addIdProperty();
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 频道ID
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道播放ID
        entity.addIntProperty("channel_number").columnName("channel_number").notNull();// 频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 频道英文名称
        entity.addBooleanProperty("isRecommend").columnName("isRecommend").notNull();// 推荐
    }

    private static void addHistory(Schema schema) {
        Entity entity = schema.addEntity("DvbHistoryTable");
        entity.addIdProperty();
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 频道ID
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道播放ID
        entity.addIntProperty("channel_number").columnName("channel_number").notNull();// 频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 频道英文名称
        entity.addLongProperty("total_watching_time").columnName("total_watching_time");// 观看时长
        entity.addLongProperty("last_watching_time").columnName("last_watching_time").notNull();// 上次观看时间
    }

    private static void addReserves(Schema schema) {
        Entity entity = schema.addEntity("DvbReserveTable");
        entity.addIdProperty();
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 频道ID
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道播放ID
        entity.addIntProperty("channel_number").columnName("channel_number").notNull();// 频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 频道英文名称
        entity.addBooleanProperty("isReserve").columnName("isReserve").notNull();// 预约
    }

    private static void addCollections(Schema schema) {
        Entity entity = schema.addEntity("DvbCollectionTable");
        entity.addIdProperty();
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 频道ID
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道播放ID
        entity.addIntProperty("channel_number").columnName("channel_number").notNull();// 频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 频道英文名称
        entity.addBooleanProperty("isCollection").columnName("isCollection").notNull();// 收藏
    }

    private static void addSettings(Schema schema) {
        Entity entity = schema.addEntity("DvbSettingTable");
        entity.addIdProperty();
        entity.addStringProperty("parameter").columnName("parameter");
        entity.addStringProperty("value").columnName("value");
    }

    private static void addChannel(Schema schema) {
        Entity entity = schema.addEntity("DvbChannelTable");
        entity.addIdProperty();
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道dvb播放ID
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 频道ID
        entity.addIntProperty("channel_number").columnName("channel_number").notNull();// 频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 频道英文名称
        entity.addIntProperty("channel_type").columnName("channel_type").notNull();// 频道类型(电视、广播)
        entity.addStringProperty("category_name").columnName("category_name").notNull();// 频道所属分类(央视、高清)
        entity.addIntProperty("category_id").columnName("category_id").notNull();// 频道分类ID(央视、高清)
        entity.addStringProperty("stream_url").columnName("stream_url");// 频道播放地址
        entity.addStringProperty("icon_url").columnName("icon_url");// 频道图标地址
        entity.addBooleanProperty("isCollection").columnName("isCollection").notNull();// 收藏
        entity.addBooleanProperty("isRecommend").columnName("isRecommend").notNull();// 推荐
        entity.addBooleanProperty("isReserve").columnName("isReserve").notNull();// 预约

        entity.addIntProperty("frequency").columnName("frequency");// 频道频率
        entity.addIntProperty("audioPid").columnName("audioPid");
        entity.addIntProperty("vidioPid").columnName("vidioPid");
        entity.addIntProperty("audioChannel").columnName("audioChannel");// 声道
        entity.addIntProperty("symbolRate").columnName("symbolRate");// 符号率
        entity.addIntProperty("modulation").columnName("modulation");// 调制
        entity.addIntProperty("volume").columnName("volume");// 音量
        entity.addLongProperty("total_watching_time").columnName("total_watching_time");// 观看时长
        entity.addLongProperty("last_watching_time").columnName("last_watching_time");// 上次观看时间
        entity.addStringProperty("channel_reserve1").columnName("channel_reserve1");// 预留
        entity.addStringProperty("channel_reserve2").columnName("channel_reserve2");// 预留
        entity.addStringProperty("channel_reserve3").columnName("channel_reserve3");// 预留
    }

    private static void addProgram(Schema schema) {
        Entity entity = schema.addEntity("DvbProgramTable");
        entity.addIdProperty();
        entity.addIntProperty("service_id").columnName("service_id").notNull();// 频道dvb播放ID
        entity.addIntProperty("channel_id").columnName("channel_id").notNull();// 所在频道ID
        entity.addStringProperty("channel_number").columnName("channel_number").notNull();// 所在频道号
        entity.addStringProperty("channel_name").columnName("channel_name").notNull();// 所在频道名称
        entity.addStringProperty("channel_ename").columnName("channel_ename");// 所在频道英文名称
        entity.addIntProperty("program_id").columnName("program_id").notNull();// 节目ID
        entity.addStringProperty("program_name").columnName("program_name").notNull();// 节目名称
        entity.addStringProperty("program_ename").columnName("program_ename");// 节目英文名称
        entity.addStringProperty("category_name").columnName("category_name").notNull();// 节目分类
        entity.addIntProperty("category_id").columnName("category_id").notNull();// 分类ID(央视、高清)
        entity.addStringProperty("program_url").columnName("program_url");// 节目播放地址
        entity.addStringProperty("icon_url").columnName("icon_url");// 节目缩略图
        entity.addBooleanProperty("isCollection").columnName("isCollection").notNull();// 收藏
        entity.addBooleanProperty("isReserve").columnName("isReserve").notNull();// 预约
        entity.addBooleanProperty("isRecommend").columnName("isRecommend").notNull();// 推荐
        entity.addLongProperty("begin_time").columnName("begin_time");// 开始时间
        entity.addLongProperty("duration").columnName("duration");// 持续时间
        entity.addLongProperty("end_time").columnName("end_time");// 结束时间
        entity.addStringProperty("program_reserve1").columnName("program_reserve1");// 预留
        entity.addStringProperty("program_reserve2").columnName("program_reserve2");// 预留
        entity.addStringProperty("program_reserve3").columnName("program_reserve3");// 预留
    }

}
