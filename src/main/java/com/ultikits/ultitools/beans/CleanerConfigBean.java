package com.ultikits.ultitools.beans;

import java.util.List;

public class CleanerConfigBean {
    private final double configVersion;
    private final String cleanerName;
    private final boolean isCleanTaskEnable;
    private final boolean isSmartCleanEnable;
    private final boolean isChunkTaskEnable;
    private final int cleanPeriod;
    private final List<String> cleanType;
    private final List<String> cleanWorlds;
    private final int itemMax;
    private final int mobMax;
    private final int entityMax;

    public CleanerConfigBean(double configVersion, String cleanerName, boolean isCleanTaskEnable, boolean isSmartCleanEnable, boolean isChunkTaskEnable, int cleanPeriod, List<String> cleanType, List<String> cleanWorlds, int itemMax, int mobMax, int entityMax) {
        this.configVersion = configVersion;
        this.cleanerName = cleanerName;
        this.isCleanTaskEnable = isCleanTaskEnable;
        this.isSmartCleanEnable = isSmartCleanEnable;
        this.isChunkTaskEnable = isChunkTaskEnable;
        this.cleanPeriod = cleanPeriod;
        this.cleanType = cleanType;
        this.cleanWorlds = cleanWorlds;
        this.itemMax = itemMax;
        this.mobMax = mobMax;
        this.entityMax = entityMax;
    }

    public double getConfigVersion() {
        return configVersion;
    }

    public String getCleanerName() {
        return cleanerName;
    }

    public boolean isCleanTaskEnable() {
        return isCleanTaskEnable;
    }

    public boolean isSmartCleanEnable() {
        return isSmartCleanEnable;
    }

    public boolean isChunkTaskEnable() {
        return isChunkTaskEnable;
    }

    public int getCleanPeriod() {
        return cleanPeriod;
    }

    public List<String> getCleanType() {
        return cleanType;
    }

    public List<String> getCleanWorlds() {
        return cleanWorlds;
    }

    public int getItemMax() {
        return itemMax;
    }

    public int getMobMax() {
        return mobMax;
    }

    public int getEntityMax() {
        return entityMax;
    }
}
